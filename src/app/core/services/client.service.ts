import { effect, inject, Injectable, signal } from '@angular/core';
import { doc, updateDoc } from '@angular/fire/firestore';
import { collection, getDocs, setDoc } from '@firebase/firestore';
import { Client } from '../models/client.model';
import { CommonService } from './common.service';
import { CompanyService } from './company.service';
import { Collections } from '@utils/const';

@Injectable({ providedIn: 'root' })
export class ClientService extends CommonService {
  readonly companyService = inject(CompanyService)

  private clientsCache = new Map<string, Client[]>();

  private readonly _clients = signal<Client[]>([]);
  readonly clients = this._clients.asReadonly();

  constructor() {
    super()
    // Reacciona automáticamente cada vez que activeCompany cambia en CompanyService
    effect(async () => {
      const activeCompany = this.companyService.activeCompany();

      if (activeCompany?.id) {
        this.loadingService.track(this.loadClientsForCompany(activeCompany.id));
      } else {
        this._clients.set([]);
      }
    });
  }

  getCollection(companyId: string) {
    return `${Collections.Company}/${companyId}/${Collections.Clients}`;
  }

  async loadClientsForCompany(companyId: string, forceRefresh = false): Promise<void> {
    // 1. Si ya tenemos la empresa en caché y no forzamos recarga, la usamos (0 lecturas)
    if (!forceRefresh && this.clientsCache.has(companyId)) {
      this._clients.set(this.clientsCache.get(companyId) || []);
      return;
    }
    try {
      // Colección: companies/{companyId}/clients
      const colRef = collection(this.firestore, this.getCollection(companyId));
      const snap = await getDocs(colRef);

      const clientList = snap.docs.map(docSnap => ({
        id: docSnap.id,
        ...docSnap.data()
      } as Client));

      this.clientsCache.set(companyId, clientList);
      this._clients.set(clientList);
    } catch (error) {
      console.error('Error loading clients:', error);
      this._clients.set([]);
    }
  }


  async createClient(clientData: Omit<Client, 'id'>): Promise<void> {
    const activeCompanyId = this.companyService.activeCompany()?.id;
    if (!activeCompanyId) return;

    // Generamos una referencia con ID automático
    const colRef = collection(this.firestore, this.getCollection(activeCompanyId));
    const newDocRef = doc(colRef);

    const newClient: Client = {
      id: newDocRef.id,
      ...clientData,
      companyId: activeCompanyId
    };

    await setDoc(newDocRef, newClient);

    const currentList = this.clientsCache.get(activeCompanyId) || [];
    const updatedList = [...currentList, newClient];

    this.clientsCache.set(activeCompanyId, updatedList);
    this._clients.set(updatedList);
  }

  async updateClient(client: Client): Promise<void> {
    if (!client.id || !client.companyId) return;

    const docRef = doc(this.firestore, this.getCollection(client.companyId), client.id);
    await updateDoc(docRef, { ...client });

    // Actualizamos en la caché correspondiente
    const currentList = this.clientsCache.get(client.companyId) || [];
    const updatedList = currentList.map(c => (c.id === client.id ? client : c));

    this.clientsCache.set(client.companyId, updatedList);

    if (client.companyId === this.companyService.activeCompany()?.id) {
      this._clients.set(updatedList);
    }
  }
}
