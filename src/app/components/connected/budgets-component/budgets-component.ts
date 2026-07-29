import { Component, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { Router } from '@angular/router';
import { TranslocoModule } from '@jsverse/transloco';
import { BudgetService } from '@services/budget-service';

@Component({
  selector: 'app-budgets-component',
  imports: [
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    TranslocoModule
  ],
  templateUrl: './budgets-component.html',
  styleUrl: './budgets-component.css',
})
export class BudgetsComponent {
  budgetService = inject(BudgetService);
  readonly router = inject(Router);
  
  displayedColumns: string[] = ['budgetId', 'date', 'project', 'client', 'vat', 'tasks', 'delete'];

  ngOnInit() {
    this.budgetService.getBudgets();
  }

  goBudget(budgetId: string | undefined = undefined) {
    if(budgetId) {
      this.router.navigate(['/budget', budgetId]);
    } else {
      this.router.navigate(['/budget']);
    }
  }
}
