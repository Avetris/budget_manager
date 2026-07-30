import { inject, Injectable, signal } from '@angular/core';
import { Budget } from './budget.model'
import { addDoc, collectionData, doc, Firestore, query, updateDoc, where } from '@angular/fire/firestore';
import { collection, deleteDoc } from '@firebase/firestore';
import { firstValueFrom } from 'rxjs';
import { CommonService } from '@core/services/common.service';
import { UserService } from '@core/services/user.service';

@Injectable({ providedIn: 'root' })
export class BudgetService extends CommonService {
  userService = inject(UserService)

  _items = signal<Budget[]>([])

  items = this._items.asReadonly()

  _getCollection() {
    return collection(this.firestore, 'budgets')
  }

  async getBudgets(companyId: string, force: boolean = false): Promise<Budget[]> {
    if (this._items().length > 0 && !force) {
      return this.items();
    }

    const itemCollection = this._getCollection()

    try {
      let getQuery = query(itemCollection)
      if (!this.userService.user()!.isAdmin) {
        getQuery = query(itemCollection, where('companyId', '==', companyId))
      }
      const data = await firstValueFrom(collectionData(getQuery, { idField: 'id' })) as Budget[];
      this._items.set(data);
    } catch (error) {
      console.error("Error getting budgets:", error);
    }
    return this._items();
  }

  async getBudget(id: string): Promise<Budget | undefined> {
    //await this.getBudgets()
    return this._items().find(item => item.id === id)
  }

  async addBudget(newItem: Omit<Budget, 'id'>) {
    const itemCollection = this._getCollection();

    try {
      const docRef = await addDoc(itemCollection, newItem);

      const productWithId: Budget = { ...newItem, id: docRef.id };

      this._items.update(current => [...current, productWithId]);

      return productWithId;
    } catch (error) {
      console.error("Error adding budget:", error);
      throw error;
    }
  }

  async deleteBudget(id: string) {
    const itemCollection = this._getCollection()
    try {
      const itemRef = doc(itemCollection, id)
      await deleteDoc(itemRef)

      this._items.update(current => current.filter(item => item.id !== id));
    } catch (error) {
      console.error("error deleting budget;", error)
    }
  }
  async updateBudget(id: string, changes: Partial<Budget>) {
    const itemCollection = this._getCollection()

    try {
      const itemRef = doc(itemCollection, id)
      await updateDoc(itemRef, changes);

      this._items.update(current =>
        current.map(item => item.id === id ? { ...item, ...changes } : item)
      );
    } catch (error) {
      console.error("Error updating budget:", error);
    }
  }
} 
