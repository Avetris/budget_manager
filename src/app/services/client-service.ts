import { inject, Injectable, signal } from '@angular/core';
import { addDoc, collectionData, doc, Firestore, query, updateDoc, where } from '@angular/fire/firestore';
import { collection, deleteDoc } from '@firebase/firestore';
import { firstValueFrom } from 'rxjs';
import { Client } from '@models/client';
import { ConfigService } from './config-service';
import { Config } from '@models/config';

@Injectable({ providedIn: 'root' })
export class ClientService  {
  firestore = inject(Firestore)
  conf: Config

  _items = signal<Client[]>([])

  items = this._items.asReadonly()
  public isLoading = signal(false)

  constructor() {
      this.conf = inject(ConfigService).config()
  }

  _getCollection() {
    return collection(this.firestore, 'clients')
  }

 async getClients(): Promise<Client[]> {
    if (this._items().length > 0) {
      return this.items();
    }

    this.isLoading.set(true);
    const itemCollection = this._getCollection()
    
    try {
      let getQuery = query(itemCollection)
      if (!this.conf.adminUser) {
        getQuery = query(itemCollection, where('userId', '==', this.conf.id))
      } 
      const data = await firstValueFrom(collectionData(getQuery, { idField: 'id' })) as Client[];
      this._items.set(data);
    } catch (error) {
      console.error("Error getting clients:", error);
    } finally {
      this.isLoading.set(false);
      return this._items();
    }
  }

  async getClient(id: string): Promise<Client | undefined> {
    await this.getClients()
    return this._items().find(item => item.id === id)
  }

  async addClient(newItem: Omit<Client, 'id'>) {
    const itemCollection = this._getCollection();
    
    try {
      const docRef = await addDoc(itemCollection, newItem);
      
      const productWithId: Client = { ...newItem, id: docRef.id };

      this._items.update(current => [...current, productWithId]);
      
      return productWithId;
    } catch (error) {
      console.error("Error adding client:", error);
      throw error;
    }
  }

  async deleteClient(id: string) {
    const itemCollection = this._getCollection()
    try {
      const itemRef = doc(itemCollection, id)
      await deleteDoc(itemRef)

      this._items.update(current => current.filter(item => item.id !== id));
    } catch(error) {
      console.error("error deleting client;", error)
    }
  }
  async updateClient(id: string, changes: Partial<Client>) {
    const itemCollection = this._getCollection()
    
    try {
      const itemRef = doc(itemCollection, id)
      await updateDoc(itemRef, changes);
      
      this._items.update(current => 
        current.map(item => item.id === id ? { ...item, ...changes } : item)
      );
    } catch (error) {
      console.error("Error updating client:", error);
    }
  }
} 
