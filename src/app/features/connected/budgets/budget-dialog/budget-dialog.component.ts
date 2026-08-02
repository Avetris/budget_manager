import { Component, inject, OnInit, signal } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { Budget, BudgetGroup, BudgetTask } from '../../../../core/models/budget.model';
import { TranslocoModule } from '@jsverse/transloco';
import { MatIconModule } from '@angular/material/icon';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatExpansionModule } from '@angular/material/expansion';
import { ClientService } from '@core/services/client.service';
import { TaskService } from '@core/services/task.service';
import { TaskUnit } from '@core/models/task.model';
import { BudgetService } from '../../../../core/services/budget.service';
import { BudgetStorageService } from '@core/services/budget-storage.service';
import { CompanyService } from '@core/services/company.service';
import { Client } from '@core/models/client.model';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-budget-dialog',
  imports: [
    CommonModule,
    CurrencyPipe,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatCheckboxModule,
    MatExpansionModule,
    MatProgressBarModule,
    MatProgressSpinnerModule,
    TranslocoModule
  ],
  templateUrl: './budget-dialog.component.html',
  styleUrl: './budget-dialog.component.scss',
})
export class BudgetDialogComponent implements OnInit {
  private fb = inject(FormBuilder).nonNullable;
  private dialogRef = inject(MatDialogRef<BudgetDialogComponent>);

  readonly clientService = inject(ClientService);
  readonly taskService = inject(TaskService);
  private readonly budgetService = inject(BudgetService);
  readonly companyService = inject(CompanyService);
  readonly storageService = inject(BudgetStorageService)
  readonly data = inject<Budget | null>(MAT_DIALOG_DATA, { optional: true });

  isEditing = false;
  vatRates = [21, 10, 4, 0];

  isSaving = signal<boolean>(false);

  selectedFiles: File[] = [];
  previewImageUrls = signal<string[]>([]);
  existingImages = signal<string[]>([]);

  subtotal = signal<number>(0);
  vatAmount = signal<number>(0);
  total = signal<number>(0);

  form = this.fb.group({
    number: [`PRES-${new Date().getFullYear()}-${Math.floor(100 + Math.random() * 900)}`, [Validators.required]],
    client: [null as Client | null, [Validators.required]],
    date: [new Date().toISOString().substring(0, 10), [Validators.required]],
    vatRate: [21, [Validators.required]],
    ungroupedTasks: this.fb.array<FormGroup>([]),
    groups: this.fb.array<FormGroup>([])
  });

  get ungroupedTasks(): FormArray<FormGroup> {
    return this.form.get('ungroupedTasks') as FormArray<FormGroup>;
  }

  get groups(): FormArray<FormGroup> {
    return this.form.get('groups') as FormArray<FormGroup>;
  }

  ngOnInit() {
    if (this.data) {
      this.isEditing = true;

      if (this.data.images) {
        this.existingImages.set(this.data.images);
      }

      this.form.patchValue({
        number: this.data.number,
        client: this.data.client,
        date: this.data.date,
        vatRate: this.data.vatRate,
      });
      this.data.ungroupedTasks.forEach((task) => {
        this.ungroupedTasks.push(this.buildTaskFormGroup(task));
      })

      this.data.groups.forEach((group) => {
        this.addGroup(group)
      })
    }

    // Recalcular totales cada vez que cambien los valores del formulario
    this.form.valueChanges.subscribe(() => this.calculateTotals());
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.files?.length) return;

    const files = Array.from(input.files);
    this.selectedFiles.push(...files);

    // Generar previews locales DataURL
    files.forEach(file => {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.previewImageUrls.update(urls => [...urls, e.target.result]);
      };
      reader.readAsDataURL(file);
    });
  }

  removeNewImage(index: number): void {
    this.selectedFiles.splice(index, 1);
    this.previewImageUrls.update(urls => urls.filter((_, i) => i !== index));
  }

  removeExistingImage(index: number): void {
    this.existingImages.update(urls => urls.filter((_, i) => i !== index));
  }

  buildTaskFormGroup(task?: Partial<BudgetTask>): FormGroup {
    return this.fb.group({
      taskId: [task?.taskId || ''],
      title: [task?.title || '', [Validators.required]],
      description: [task?.description || ''],
      price: [task?.price ?? 0, [Validators.required, Validators.min(0)]],
      unit: [task?.unit || 'ud', [Validators.required]],
      count: [task?.count ?? 1, [Validators.required, Validators.min(1)]],
      saveToCatalog: [false] // Checkbox para decidir si guardar como plantilla
    });
  }

  addUngroupedTask(): void {
    this.ungroupedTasks.push(this.buildTaskFormGroup());
  }

  removeUngroupedTask(index: number): void {
    this.ungroupedTasks.removeAt(index);
  }

  // Al seleccionar una tarea del catálogo global
  onSelectTaskFromCatalog(taskGroup: FormGroup, selectedTaskId: string): void {
    const catalogTask = this.taskService.tasks().find(t => t.id === selectedTaskId);
    if (catalogTask) {
      taskGroup.patchValue({
        taskId: catalogTask.id,
        title: catalogTask.title,
        description: catalogTask.description,
        price: catalogTask.price,
        unit: catalogTask.unit || 'ud'
      });
    }
  }

  // --- MÉTODOS PARA GRUPOS ---
  addGroup(group?: BudgetGroup): void {
    const groupForm = this.fb.group({
      name: [group?.name || '', [Validators.required]],
      tasks: this.fb.array<FormGroup>([])
    });

    const taskForm = (groupForm.get('tasks')! as FormArray<FormGroup>)
    group?.tasks.forEach(task => {
      taskForm.push(this.buildTaskFormGroup(task));
    })
    this.groups.push(groupForm);
  }

  removeGroup(groupIndex: number): void {
    this.groups.removeAt(groupIndex);
  }

  getGroupTasks(groupIndex: number): FormArray<FormGroup> {
    return this.groups.at(groupIndex).get('tasks') as FormArray<FormGroup>;
  }

  addTaskToGroup(groupIndex: number): void {
    this.getGroupTasks(groupIndex).push(this.buildTaskFormGroup());
  }

  removeTaskFromGroup(groupIndex: number, taskIndex: number): void {
    this.getGroupTasks(groupIndex).removeAt(taskIndex);
  }

  // --- CÁLCULO DE TOTALES E IVA ---
  calculateTotals(): void {
    let rawSubtotal = 0;

    // Sumar tareas sueltas
    this.ungroupedTasks.controls.forEach(control => {
      const p = Number(control.get('price')?.value) || 0;
      const c = Number(control.get('count')?.value) || 0;
      rawSubtotal += p * c;
    });

    // Sumar tareas dentro de grupos
    this.groups.controls.forEach(groupControl => {
      const tasksArray = groupControl.get('tasks') as FormArray<FormGroup>;
      tasksArray.controls.forEach(control => {
        const p = Number(control.get('price')?.value) || 0;
        const c = Number(control.get('count')?.value) || 0;
        rawSubtotal += p * c;
      });
    });

    const currentVatRate = Number(this.form.get('vatRate')?.value) || 0;
    const computedVat = rawSubtotal * (currentVatRate / 100);

    this.subtotal.set(rawSubtotal);
    this.vatAmount.set(computedVat);
    this.total.set(rawSubtotal + computedVat);
  }

  async onSave(): Promise<void> {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const activeCompanyName = this.companyService.activeCompany()?.name || this.companyService.activeCompany()?.id;
    if (!activeCompanyName) return;

    try {
      this.isSaving.set(true);

      await this.processCatalogNewTasks();

      const rawForm = this.form.getRawValue();
      const budgetNumber = this.data?.number || this.data?.id || crypto.randomUUID();

      const originalImages = this.data?.images || [];
      const currentExistingImages = this.existingImages();

      const removedImages = originalImages.filter(url => !currentExistingImages.includes(url));

      // 2. Registrar las imágenes borradas en la colección 'deleted_images'
      if (removedImages.length > 0) {
        await this.storageService.trackDeletedImages(
          removedImages,
          activeCompanyName,
          budgetNumber
        );
      }

      // 3. Subir las imágenes NUEVAS a Cloudinary
      let uploadedImageUrls: string[] = [];
      if (this.selectedFiles.length > 0) {
        uploadedImageUrls = await this.storageService.uploadBudgetImages(
          activeCompanyName,
          budgetNumber,
          this.selectedFiles
        );
      }

      const finalImages = [...this.existingImages(), ...uploadedImageUrls];

      const ungroupedTasks: BudgetTask[] = rawForm.ungroupedTasks.map(t => ({
        taskId: t['taskId'] || undefined,
        title: t['title'],
        description: t['description'],
        price: Number(t['price']),
        unit: t['unit'] as TaskUnit,
        count: Number(t['count'])
      }));

      const groups: BudgetGroup[] = rawForm.groups.map(g => ({
        name: g['name'],
        tasks: (g['tasks'] as any[]).map(t => ({
          taskId: t.taskId || undefined,
          title: t.title,
          description: t.description,
          price: Number(t.price),
          unit: t.unit as TaskUnit,
          count: Number(t.count)
        }))
      }));

      const resultBudget = {
        id: this.data?.id,
        companyId: this.data?.companyId,
        number: rawForm.number,
        client: rawForm.client!,
        date: rawForm.date,
        vatRate: Number(rawForm.vatRate),
        images: finalImages,
        ungroupedTasks,
        groups,
        subtotal: this.subtotal(),
        vatAmount: this.vatAmount(),
        total: this.total(),
        status: this.data?.status || 'draft'
      };


      if (this.isEditing && this.data?.id) {
        await this.budgetService.updateBudget(resultBudget);
      } else {
        await this.budgetService.createBudget(resultBudget);
      }
    } catch (error) {
      console.error('Error savinf bugdet:', error);
    } finally {
      this.isSaving.set(false)
    }

    this.dialogRef.close(true);
  }

  private async processCatalogNewTasks(): Promise<void> {
    // Busca tareas marcadas con saveToCatalog === true y las guarda mediante TaskService
    const checkAndSave = async (taskGroup: FormGroup) => {
      const val = taskGroup.value;
      if (val.saveToCatalog && !val.taskId) {
        const createdTask = await this.taskService.createTask({
          title: val.title,
          description: val.description,
          price: val.price,
          unit: val.unit
        });
        taskGroup.patchValue({ taskId: createdTask.id });
      }
    };

    for (const control of this.ungroupedTasks.controls) {
      await checkAndSave(control);
    }

    for (const groupControl of this.groups.controls) {
      const tasksArray = groupControl.get('tasks') as FormArray<FormGroup>;
      for (const control of tasksArray.controls) {
        await checkAndSave(control);
      }
    }
  }

  onCancel(): void {
    this.dialogRef.close(null);
  }
}
