import { inject, Injectable, signal } from '@angular/core';
import { collectionData, doc, updateDoc } from '@angular/fire/firestore';
import { collection, getDoc, getDocs } from '@firebase/firestore';
import { Company } from '@models/company';
import { UserService } from './user-service';
import { CommonService } from './common.service';
import { firstValueFrom } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class CompanyService extends CommonService {
  private userService = inject(UserService)

  _company = signal<Company | null>(null)

  company = this._company.asReadonly()

  constructor() {
    super()
    this.loadingService.track(this.getCompany())
  }

  _getCollection() {
    return collection(this.firestore, 'companies')
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

  async getCompany() {
    try {
      let user = this.userService.user()
      if (user == null) {
        await this._waitForUser()
        user = this.userService.user()
        if (user == null) {
          throw new Error("No user available")
        }
      }
      if (!user?.companyId) {
        return
      }

      const itemCollection = this._getCollection()
      const ref = doc(itemCollection, user.companyId)
      const data = await getDoc(ref);
      if (data.exists()) {
        this._company.set(data.data() as Company);
      } else {
        throw new Error("User company not found")
      }
    } catch (error) {
      console.error("Error getting company:", error);
    }
  }

  selectCompany(company: Company | null) {
    this._company.set(company)
  }

  async getAllCompanies(): Promise<Company[]> {
    try {
      let user = this.userService.user()
      if (user == null) {
        await this._waitForUser()
        user = this.userService.user()
        if (user == null) {
          throw new Error("No user available")
        }
      }
      if (!user?.isAdmin) {
        throw Error("User is not admin for getting all companies")
      }
      console.log(user.companyId)

      const itemCollection = this._getCollection()

      return await firstValueFrom(collectionData(itemCollection)) as Company[];
    } catch (error) {
      console.error("Error getting companies:", error);
    }
    return []
  }

  async updateCompany(uuid: string, changes: Partial<Company>) {
    const itemCollection = this._getCollection()

    try {
      const itemRef = doc(itemCollection, uuid)
      await updateDoc(itemRef, changes);

      this._company.set({ ...this._company()!, ...changes } as Company)
    } catch (error) {
      console.error("Error updating company:", error);
    }
  }
}

function toObservable(config: any) {
  throw new Error('Function not implemented.');
}

