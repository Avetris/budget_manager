import { effect, inject, Injectable, signal } from '@angular/core';
import { doc, updateDoc } from '@angular/fire/firestore';
import { collection, getDocs, setDoc } from '@firebase/firestore';
import { Task } from '../models/task.model';
import { CommonService, OmittedId } from './common.service';
import { CompanyService } from './company.service';
import { Collections } from '@utils/const';

@Injectable({ providedIn: 'root' })
export class TaskService extends CommonService {
  readonly companyService = inject(CompanyService)

  private tasksCache = new Map<string, Task[]>();

  private readonly _tasks = signal<Task[]>([]);
  readonly tasks = this._tasks.asReadonly();

  constructor() {
    super()
    effect(async () => {
      const activeCompany = this.companyService.activeCompany();
      if (activeCompany?.id) {
        this.loadingService.track(this.loadTasksForCompany(activeCompany.id));
      } else {
        this._tasks.set([]);
      }
    });
  }

  getCollection(companyId: string) {
    return `${Collections.Company}/${companyId}/${Collections.Tasks}`;
  }

  async loadTasksForCompany(companyId: string, forceRefresh = false): Promise<void> {
    if (!forceRefresh && this.tasksCache.has(companyId)) {
      this._tasks.set(this.tasksCache.get(companyId) || []);
      return;
    }

    try {
      const colRef = collection(this.firestore, this.getCollection(companyId));
      const snap = await getDocs(colRef);

      const taskList = snap.docs.map(docSnap => ({
        id: docSnap.id,
        ...docSnap.data()
      } as Task));

      this.tasksCache.set(companyId, taskList);
      this._tasks.set(taskList);
    } catch (error) {
      console.error('Error al cargar tareas:', error);
      this._tasks.set([]);
    }
  }

  async createTask(taskData: OmittedId<Task>): Promise<Task> {
    const activeCompanyId = this.companyService.activeCompany()?.id;
    if (!activeCompanyId) throw new Error('No active company selected');

    const colRef = collection(this.firestore, this.getCollection(activeCompanyId));
    const newDocRef = doc(colRef);

    const newTask: Task = {
      id: newDocRef.id,
      ...taskData,
      companyId: activeCompanyId
    };

    await setDoc(newDocRef, newTask);

    const currentList = this.tasksCache.get(activeCompanyId) || [];
    const updatedList = [...currentList, newTask];

    this.tasksCache.set(activeCompanyId, updatedList);
    this._tasks.set(updatedList);

    return newTask;
  }

  async updateTask(task: Task): Promise<void> {
    if (!task.id || !task.companyId) return;

    const docRef = doc(this.firestore, this.getCollection(task.companyId), task.id);
    await updateDoc(docRef, { ...task });

    const currentList = this.tasksCache.get(task.companyId) || [];
    const updatedList = currentList.map(t => (t.id === task.id ? task : t));

    this.tasksCache.set(task.companyId, updatedList);

    if (task.companyId === this.companyService.activeCompany()?.id) {
      this._tasks.set(updatedList);
    }
  }
}