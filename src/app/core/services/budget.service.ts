import { Injectable, signal, inject, effect } from '@angular/core';
import { Budget, BudgetStatus } from '../models/budget.model';
import { OmittedId, CommonService } from '@core/services/common.service';
import { Collections } from '@utils/const';
import { collection, deleteDoc, doc, getDocs, setDoc, updateDoc } from 'firebase/firestore';
import { CompanyService } from '@core/services/company.service';

@Injectable({
  providedIn: 'root'
})
export class BudgetService extends CommonService {
  readonly companyService = inject(CompanyService);

  private budgetsCache = new Map<string, Budget[]>();

  private readonly _budgets = signal<Budget[]>([]);
  readonly budgets = this._budgets.asReadonly();

  constructor() {
    super()

    effect(async () => {
      const activeCompany = this.companyService.activeCompany();

      if (activeCompany?.id) {
        this.loadBudgetsForCompany(activeCompany.id);
      } else {
        this._budgets.set([]);
      }
    });
  }

  private getCollection(companyId: string) {
    return `${Collections.Company}/${companyId}/${Collections.Budgets}`;
  }

  async loadBudgetsForCompany(companyId: string, forceRefresh = false): Promise<void> {
    if (!forceRefresh && this.budgetsCache.has(companyId)) {
      this._budgets.set(this.budgetsCache.get(companyId) || []);
      return;
    }

    await this.loadingService.track((async () => {

      try {
        const colRef = collection(this.firestore, this.getCollection(companyId));
        const snap = await getDocs(colRef);

        const budgetList = snap.docs.map(docSnap => ({
          id: docSnap.id,
          ...docSnap.data()
        } as Budget));

        this.budgetsCache.set(companyId, budgetList);
        this._budgets.set(budgetList);
      } catch (error) {
        console.error('Error loading budgets:', error);
        this._budgets.set([]);
      }
    })())
  }

  cleanUndefined<T>(obj: T): T {
    if (obj === null || typeof obj !== 'object') {
      return obj;
    }

    if (Array.isArray(obj)) {
      return obj.map(this.cleanUndefined) as unknown as T;
    }

    const cleaned: any = {};
    for (const [key, value] of Object.entries(obj)) {
      if (value !== undefined) {
        cleaned[key] = typeof value === 'object' && value !== null
          ? this.cleanUndefined(value)
          : value;
      }
    }
    return cleaned;
  }

  async createBudget(budgetData: OmittedId<Budget>): Promise<void> {

    await this.loadingService.track((async () => {
      const activeCompanyId = this.companyService.activeCompany()?.id;
      if (!activeCompanyId) return;

      const colRef = collection(this.firestore, this.getCollection(activeCompanyId));

      const newDocRef = doc(colRef);

      const newBudget: Budget = {
        id: newDocRef.id,
        ...this.cleanUndefined(budgetData),
        companyId: activeCompanyId
      };

      await setDoc(newDocRef, newBudget);

      const currentList = this.budgetsCache.get(activeCompanyId) || [];
      const updatedList = [newBudget, ...currentList];

      this.budgetsCache.set(activeCompanyId, updatedList);
      this._budgets.set(updatedList);
    })())
  }


  async updateBudget(budget: Budget): Promise<void> {
    await this.loadingService.track((async () => {
      if (!budget.id || !budget.companyId) return;

      const docRef = doc(this.firestore, this.getCollection(budget.companyId), budget.id);
      await updateDoc(docRef, { ...budget });

      const currentList = this.budgetsCache.get(budget.companyId) || [];
      const updatedList = currentList.map(b => (b.id === budget.id ? budget : b));

      this.budgetsCache.set(budget.companyId, updatedList);

      if (budget.companyId === this.companyService.activeCompany()?.id) {
        this._budgets.set(updatedList);
      }
    })());
  }

  async updateStatus(budgetId: string, newStatus: BudgetStatus): Promise<void> {

    await this.loadingService.track((async () => {
      const activeCompanyId = this.companyService.activeCompany()?.id;
      if (!activeCompanyId || !budgetId) return;

      const docRef = doc(this.firestore, this.getCollection(activeCompanyId), budgetId);

      await updateDoc(docRef, { status: newStatus });

      const currentList = this.budgetsCache.get(activeCompanyId) || [];
      const updatedList = currentList.map(b =>
        b.id === budgetId ? { ...b, status: newStatus } : b
      );

      this.budgetsCache.set(activeCompanyId, updatedList);

      if (activeCompanyId === this.companyService.activeCompany()?.id) {
        this._budgets.set(updatedList);
      }
    })())
  }

  async deleteBudget(budgetId: string): Promise<void> {
    await this.loadingService.track((async () => {
      const activeCompanyId = this.companyService.activeCompany()?.id;
      if (!activeCompanyId || !budgetId) return;

      const docRef = doc(this.firestore, this.getCollection(activeCompanyId), budgetId);
      await deleteDoc(docRef);

      const currentList = this.budgetsCache.get(activeCompanyId) || [];
      const updatedList = currentList.filter(b => b.id !== budgetId);

      this.budgetsCache.set(activeCompanyId, updatedList);
      this._budgets.set(updatedList);
    })());
  }

  getBudgetById(id: string): Budget | undefined {
    return this._budgets().find(b => b.id === id);
  }
}