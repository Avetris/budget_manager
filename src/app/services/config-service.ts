import { inject, Injectable, signal } from '@angular/core';
import { addDoc, doc, Firestore, updateDoc } from '@angular/fire/firestore';
import { collection, getDoc } from '@firebase/firestore';
import { Config } from '@models/config';
import { AuthService } from './auth-service';

@Injectable({ providedIn: 'root' })
export class ConfigService  {
  private firestore = inject(Firestore)
  private auth = inject(AuthService)

  _config = signal<Config>({ adminUser: false } as Config)

  config = this._config.asReadonly()

  public isLoading = signal(false)

  _getCollection() {
    return collection(this.firestore, 'config')
  }

 async getConfig() {
    if(!this.auth.geCurrentUser()) {
      return 
    }
    const uid = this.auth.geCurrentUser()!.uid
    if (this._config()?.id == this.auth.geCurrentUser()!.uid) {
      return;
    }

    this.isLoading.set(true);
    const itemCollection = this._getCollection()
    
    try {
      // Obtenemos los datos (usando firstValueFrom para manejarlo como Promesa)
      const data = await getDoc(doc(itemCollection, uid));
      if(data.exists()) {
        this._config.set(data.data() as Config);
      } else {
        const newConf = {adminUser: false, id: uid} as Config
        await addDoc(itemCollection, newConf); 
        this._config.set(newConf)       
      }
    } catch (error) {
      console.error("Error getting budgets:", error);
    } finally {
      this.isLoading.set(false);
    }
  }
  
  async updateConfig(uuid: string, changes: Partial<Config>) {
    const itemCollection = this._getCollection()
    
    try {
      const itemRef = doc(itemCollection, uuid)
      await updateDoc(itemRef, changes);
      
      this._config.set({ ...this._config()!, ...changes } as Config)
    } catch (error) {
      console.error("Error updating budget:", error);
    }
  }
} 
