import { Component, inject, signal, computed } from '@angular/core';
import { CommonModule, CurrencyPipe, DatePipe } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatChipsModule } from '@angular/material/chips';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDialog } from '@angular/material/dialog';
import { TranslocoModule } from '@jsverse/transloco';

import { BudgetService } from '../../../core/services/budget.service';
import { ClientService } from '@core/services/client.service';
import { Budget, BudgetStatus } from '../../../core/models/budget.model';
import { BudgetDialogComponent } from './budget-dialog/budget-dialog.component';
import { LoadingService } from '@core/services/loading.service';

@Component({
  selector: 'app-budgets-component',
  standalone: true,
  imports: [
    CommonModule,
    CurrencyPipe,
    DatePipe,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatFormFieldModule,
    MatInputModule,
    MatChipsModule,
    MatTooltipModule,
    MatProgressSpinnerModule,
    TranslocoModule
  ],
  templateUrl: './budgets.component.html',
  styleUrl: './budgets.component.scss',
})
export class BudgetsComponent {
  readonly dialog = inject(MatDialog);
  readonly loadingService = inject(LoadingService);
  readonly budgetService = inject(BudgetService);
  readonly clientService = inject(ClientService);

  displayedColumns: string[] = ['number', 'client', 'date', 'status', 'total', 'actions'];
  expandedBudget: Budget | null = null;

  searchTerm = signal<string>('');

  filteredBudgets = computed(() => {
    const term = this.searchTerm().toLowerCase().trim();
    const budgets = this.budgetService.budgets();

    if (!term) return budgets;

    return budgets.filter(b => {
      const clientName = b.client.name.toLowerCase();
      return b.number.toLowerCase().includes(term) || clientName.includes(term);
    });
  });

  onSearchChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.searchTerm.set(input.value);
  }

  toggleExpand(budget: Budget): void {
    this.expandedBudget = this.expandedBudget === budget ? null : budget;
  }

  openCreateBudgetModal(): void {
    this.dialog.open(BudgetDialogComponent, {
      width: '850px',
      maxWidth: '95vw',
      disableClose: true
    });
  }

  openEditBudgetModal(budget: Budget, event: MouseEvent): void {
    event.stopPropagation(); // Evita expandir la fila al hacer clic en editar
    this.dialog.open(BudgetDialogComponent, {
      width: '850px',
      maxWidth: '95vw',
      data: budget,
      disableClose: true
    });
  }

  getStatusClass(status: BudgetStatus): string {
    switch (status) {
      case 'accepted': return 'status-accepted';
      case 'rejected': return 'status-rejected';
      case 'sent': return 'status-sent';
      default: return 'status-draft';
    }
  }
}