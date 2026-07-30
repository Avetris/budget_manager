import { inject, Injectable, signal } from '@angular/core';
import { doc } from '@angular/fire/firestore';
import { collection, deleteDoc, getDoc, setDoc } from '@firebase/firestore';
import { UserConfig } from '../models/user-config.model';
import { AuthService } from './auth.service';
import { CommonService } from './common.service';

@Injectable({ providedIn: 'root' })
export class UserService extends CommonService {
  private auth = inject(AuthService)

  private readonly _user = signal<UserConfig | null>(null)

  readonly user = this._user.asReadonly()

  constructor() {
    super()
    this.loadingService.track(this.loadUserConfig())
  }

  resetState() {
    this._user.set(null)
  }

  _getCollection() {
    return collection(this.firestore, 'user')
  }

  async loadUserConfig() {
    try {
      const currentUser = this.auth.geCurrentUser()
      if (!currentUser) {
        throw new Error("No user logged in")
      }
      const uid = currentUser.uid
      const email = currentUser.email!
      if (this._user()?.id == uid) {
        return
      }
      const itemCollection = this._getCollection()

      const ref = doc(itemCollection, uid)
      const data = await getDoc(ref);
      if (data.exists()) {
        this._user.set({ id: data.id, ...(data.data() as Omit<UserConfig, 'id'>) });
      } else {
        const tmpCollection = collection(this.firestore, 'user_temp')
        const emailData = await getDoc(doc(tmpCollection, email))
        if (!emailData.exists()) {
          return
        }
        await setDoc(ref, emailData.data() as Omit<UserConfig, 'id'>);
        deleteDoc(doc(tmpCollection, emailData.id))
        this._user.set({ id: uid, ...(emailData.data() as Omit<UserConfig, 'id'>) });
      }
    } catch (error) {
      console.error("Error getting user config:", error);
    }
  }
} 
