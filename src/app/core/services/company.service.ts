import { inject, Injectable, signal } from '@angular/core';
import { doc, updateDoc } from '@angular/fire/firestore';
import { collection, getDoc, getDocs } from '@firebase/firestore';
import { Company } from '../models/company.model';
import { UserService } from './user.service';
import { CommonService } from './common.service';
import { Collections } from '@utils/const';


@Injectable({ providedIn: 'root' })
export class CompanyService extends CommonService {
  private userService = inject(UserService)

  private readonly _companies = signal<Company[]>([]);
  readonly companies = this._companies.asReadonly();

  _activeCompany = signal<Company | null>(null)
  activeCompany = this._activeCompany.asReadonly()

  constructor() {
    super()
    this.loadingService.track(this.load())
  }

  async load() {
    await this._waitForUser()

    let user = this.userService.user()
    if (user == null) {
      throw new Error("No user available")
    }
    if (!user?.companyId) {
      return this.loadAllCompaniesForAdmin()
    } else {
      return this.loadCompanyForUser(user.companyId)
    }
  }

  async loadCompanyForUser(companyId: string): Promise<void> {
    const snap = await getDoc(doc(this.firestore, Collections.Company, companyId));
    if (snap.exists()) {
      this._activeCompany.set({ id: snap.id, ...snap.data() } as Company);
    }
  }

  async loadAllCompaniesForAdmin(): Promise<void> {
    const colSnap = await getDocs(collection(this.firestore, Collections.Company));
    const list = colSnap.docs.map(doc => ({ id: doc.id, ...doc.data() } as Company));

    this._companies.set(list);

    if (list.length > 0) {
      this._activeCompany.set(list[0]);
    }
  }

  private _waitForUser(): Promise<void> {
    return new Promise((resolve) => {
      const interval = setInterval(() => {
        if (this.userService.user() !== null) {
          clearInterval(interval);
          resolve();
        }
      }, 50);
    });
  }

  selectCompanyById(companyId: string): void {
    const selected = this.companies().find(c => c.id === companyId);
    if (selected) {
      this._activeCompany.set(selected);
    }
  }

  async updateActiveCompany(data: Partial<Company>) {
    const current = this.activeCompany();
    if (!current) return

    const docRef = doc(this.firestore, Collections.Company, current.id);
    await updateDoc(docRef, data);

    const updated = { ...current, ...data };
    this._activeCompany.set(updated);

    this._companies.update(list =>
      list.map(c => c.id === updated.id ? updated : c)
    );
  }
}

