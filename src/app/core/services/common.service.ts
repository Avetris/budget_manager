import { inject, Injectable } from '@angular/core';
import { Firestore } from '@angular/fire/firestore';
import { LoadingService } from './loading.service';

@Injectable({ providedIn: 'root' })
export abstract class CommonService {
  firestore = inject(Firestore)
  loadingService = inject(LoadingService)
}


export type OmittedId<T> = Omit<T, 'id'>;