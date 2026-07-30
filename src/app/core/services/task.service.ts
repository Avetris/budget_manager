import { inject, Injectable, signal } from '@angular/core';
import { addDoc, collectionData, doc, query, updateDoc, where } from '@angular/fire/firestore';
import { collection, deleteDoc } from '@firebase/firestore';
import { firstValueFrom } from 'rxjs';
import { Task } from '../models/task.model';
import { UserService } from './user.service';
import { CommonService } from './common.service';
import { Collections } from '@utils/const';

@Injectable({ providedIn: 'root' })
export class TaskService extends CommonService {
  userService = inject(UserService);

  _items = signal<Task[]>([]);
  items = this._items.asReadonly();

  _getCollection() {
    return collection(this.firestore, Collections.Tasks);
  }

  async getTasks(companyId?: string, force: boolean = false): Promise<Task[]> {
    if (this._items().length > 0 && !force) {
      return this.items();
    }
    const itemCollection = this._getCollection();

    try {
      let getQuery = query(itemCollection);
      if (companyId && !this.userService.user()?.isAdmin) {
        getQuery = query(itemCollection, where('companyId', '==', companyId));
      }
      const data = await firstValueFrom(collectionData(getQuery, { idField: 'id' })) as Task[];
      this._items.set(data);
    } catch (error) {
      console.error("Error getting tasks:", error);
    }
    return this._items();
  }

  async getTask(id: string): Promise<Task | undefined> {
    return this._items().find(item => item.id === id);
  }

  async addTask(newItem: Omit<Task, 'id'>) {
    const itemCollection = this._getCollection();

    try {
      const docRef = await addDoc(itemCollection, newItem);
      const taskWithId: Task = { ...newItem, id: docRef.id };
      this._items.update(current => [...current, taskWithId]);
      return taskWithId;
    } catch (error) {
      console.error("Error adding task:", error);
      throw error;
    }
  }

  async deleteTask(id: string) {
    const itemCollection = this._getCollection();
    try {
      const itemRef = doc(itemCollection, id);
      await deleteDoc(itemRef);
      this._items.update(current => current.filter(item => item.id !== id));
    } catch (error) {
      console.error("Error deleting task:", error);
    }
  }

  async updateTask(id: string, changes: Partial<Task>) {
    const itemCollection = this._getCollection();
    try {
      const itemRef = doc(itemCollection, id);
      await updateDoc(itemRef, changes);
      this._items.update(current =>
        current.map(item => item.id === id ? { ...item, ...changes } : item)
      );
    } catch (error) {
      console.error("Error updating task:", error);
    }
  }
}
