import { Component, inject, OnInit } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatDialog } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatDividerModule } from '@angular/material/divider';
import { MatSelectModule } from '@angular/material/select';
import { ActivatedRoute, Router } from '@angular/router';
import { ErrorDialog } from '@shared/dialog/error/error.dialog';
import { Budget } from '../budget.model';
import { BudgetService } from '../budget.service';
import { TranslocoModule } from '@jsverse/transloco';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule, getCurrencySymbol } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { map, startWith } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { UserService } from '@core/services/user.service';
import { ClientService } from '@core/services/client.service';
import { Client } from '@core/models/client.model';
import { UserConfig } from '@core/models/user-config.model';

@Component({
  selector: 'app-budget-component',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatCardModule,
    MatDatepickerModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatDividerModule,
    MatSelectModule,
    MatNativeDateModule,
    MatTooltipModule,
    MatAutocompleteModule,
    TranslocoModule
  ],
  templateUrl: './budget.component.html',
  styleUrl: './budget.component.scss',
})
export class BudgetComponent implements OnInit {
  readonly dialog = inject(MatDialog);
  readonly router = inject(Router)
  readonly route = inject(ActivatedRoute)
  readonly budgetService = inject(BudgetService)
  readonly userService = inject(UserService)
  readonly clientService = inject(ClientService)

  config: UserConfig = {} as UserConfig

  budgetId: string | null = null;
  budgetForm!: FormGroup;
  budget: Budget = {} as Budget
  currency: string = 'EUR'

  clients: Client[] = [];


  commonTasks = [
    { title: 'Diseño UI/UX', description: 'Diseño de interfaces en Figma y prototipado.', price: 50 },
    { title: 'Desarrollo Frontend', description: 'Implementación de componentes en Angular.', price: 65 },
    { title: 'Consultoría Técnica', description: 'Análisis de arquitectura y viabilidad.', price: 90 }
  ];

  filteredTasks: Observable<any[]>[] = [];

  constructor(private fb: FormBuilder) { }

  ngOnInit() {
    this.clientService.getClients().then(clients => {
      this.clients = clients
    })
    // Access route parameter
    this.budgetId = this.route.snapshot.paramMap.get('id');
    if (this.budgetId != "" && this.budgetId != null) {
      this.budgetService.getBudget(this.budgetId!).then(budget => {
        if (budget) {
          this.budget = budget
          this.initForm()
        } else {
          this.dialog.open(ErrorDialog, {
            data: { "title": "budgets.errors.exist.title", "message": "budgets.errors.exist.message" }
          }).afterClosed().subscribe(() => {
            this.router.navigate(['/'])
          });
        }
      })
    } else {
      this.initForm()
    }
  }

  getCurrencySymbol(): string {
    // 'wide' devuelve el símbolo ($, €), 'narrow' devuelve el más corto
    return getCurrencySymbol(this.currency, 'wide');
  }

  get tasks() {
    return this.budgetForm.get('tasks') as FormArray;
  }

  initForm() {
    this.budgetForm = this.fb.group({
      id: [''],
      budgetId: ['', Validators.required],
      date: [new Date(), Validators.required],
      project: ['', Validators.required],
      client: [null, Validators.required],
      vat: [21, [Validators.required, Validators.min(0)]],
      tasks: this.fb.array([])
    });

    this.addTask();
  }

  calculateTaskSubtotal(index: number): number {
    return this.tasks.controls.at(index)?.get('quantity')?.value * this.tasks.at(index)?.get('price')?.value;
  }

  calculateSubtotal(): number {
    return this.tasks.controls.reduce((acc, control) => {
      const qty = control.get('quantity')?.value || 0;
      const price = control.get('price')?.value || 0;
      return acc + (qty * price);
    }, 0);
  }

  calculateTotal(): number {
    const subtotal = this.calculateSubtotal();
    const vatPercent = this.budgetForm.get('vat')?.value || 0;
    return subtotal + (subtotal * (vatPercent / 100));
  }


  private createTaskGroup(): FormGroup {
    return this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      quantity: [1, [Validators.required, Validators.min(1)]],
      price: [0, [Validators.required, Validators.min(0)]]
    });
  }

  addTask() {
    const taskGroup = this.createTaskGroup();
    this.tasks.push(taskGroup);

    const index = this.tasks.length - 1;
    this.manageAutocomplete(index);
  }

  manageAutocomplete(index: number) {
    const control = this.tasks.at(index).get('title');

    if (control) {
      this.filteredTasks[index] = control.valueChanges.pipe(
        startWith(''),
        map(value => this._filter(value || ''))
      );
    }
  }

  private _filter(value: any): any[] {
    const filterValue = (typeof value === 'string' ? value : value?.title || '').toLowerCase();
    if (!filterValue) return this.commonTasks;
    return this.commonTasks.filter(task =>
      task.title.toLowerCase().includes(filterValue)
    );
  }

  onTaskSelected(event: any, index: number) {
    const selectedTask = event.option.value;
    this.tasks.at(index).patchValue({
      title: selectedTask.title,
      description: selectedTask.description,
      price: selectedTask.price
    });
  }

  displayTitle(task: any): string {
    return task && task.title ? task.title : (typeof task === 'string' ? task : '');
  }

  removeTask(index: number) {
    if (this.tasks.length > 1) {
      this.tasks.removeAt(index);
    }
  }
  saveBudget() {
    if (this.budgetForm.valid) {
      console.log('Datos del Presupuesto:', this.budgetForm.value);
      // Aquí llamarías a tu servicio: this.budgetService.save(this.budgetForm.value);
    }
  }
}
