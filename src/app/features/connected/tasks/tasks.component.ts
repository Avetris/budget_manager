import { Component, computed, inject, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { TranslocoModule } from '@jsverse/transloco';
import { CompanyService } from '@core/services/company.service';
import { MatDialog } from '@angular/material/dialog';
import { TaskDialogComponent } from './task-dialog/task-dialog.component';
import { TaskService } from '@core/services/task.service';
import { Task } from '@core/models/task.model';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';

@Component({
  selector: 'app-tasks-component',
  imports: [
    CommonModule,
    CurrencyPipe,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatFormFieldModule,
    MatInputModule,
    MatTooltipModule,
    MatProgressSpinnerModule,
    TranslocoModule
  ],
  templateUrl: './tasks.component.html',
  styleUrl: './tasks.component.scss',
})
export class TasksComponent {
  readonly dialog = inject(MatDialog);
  readonly taskService = inject(TaskService);
  companyService = inject(CompanyService)

  displayedColumns: string[] = ['title', 'description', 'price', 'actions'];

  searchTerm = signal<string>('');

  filteredTasks = computed(() => {
    const term = this.searchTerm().toLowerCase().trim();
    const tasks = this.taskService.tasks();

    if (!term) return tasks;

    return tasks.filter(t =>
      t.title.toLowerCase().includes(term) ||
      t.description.toLowerCase().includes(term)
    );
  });

  onSearchChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.searchTerm.set(input.value);
  }

  openCreateTaskModal(): void {
    this.dialog.open(TaskDialogComponent, {
      width: '520px',
      disableClose: true
    });
  }

  openEditTaskModal(task: Task): void {
    this.dialog.open(TaskDialogComponent, {
      width: '520px',
      data: task,
      disableClose: true
    });
  }
}
