import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { TranslocoModule } from '@jsverse/transloco';

import { CompanyService } from '@core/services/company.service';
import { TaskService } from '@core/services/task.service';
import { Task, TaskUnit } from '@core/models/task.model';
import { MatSelectModule } from '@angular/material/select';

@Component({
    selector: 'app-task-dialog',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        MatDialogModule,
        MatFormFieldModule,
        MatInputModule,
        MatButtonModule,
        MatIconModule,
        MatSelectModule,
        TranslocoModule
    ],
    templateUrl: './task-dialog.component.html',
    styleUrl: './task-dialog.component.scss'
})
export class TaskDialogComponent implements OnInit {
    private fb = inject(FormBuilder).nonNullable;
    private dialogRef = inject(MatDialogRef<TaskDialogComponent>);
    private taskService = inject(TaskService);
    private companyService = inject(CompanyService);

    public data = inject<Task | null>(MAT_DIALOG_DATA, { optional: true });

    isEditing = false;
    isSaving = false;

    form = this.fb.group({
        title: ['', [Validators.required]],
        description: ['', [Validators.required]],
        price: [0, [Validators.required, Validators.min(0)]],
        unit: ['ud' as TaskUnit, [Validators.required]]
    });

    ngOnInit(): void {
        if (this.data) {
            this.isEditing = true;
            this.form.patchValue({
                title: this.data.title,
                description: this.data.description,
                price: this.data.price
            });
        }
    }

    async onSave(): Promise<void> {
        if (this.form.invalid) {
            this.form.markAllAsTouched();
            return;
        }

        this.isSaving = true;
        const formValue = this.form.getRawValue();
        const activeCompanyId = this.companyService.activeCompany()?.id;

        if (!activeCompanyId) {
            this.isSaving = false;
            return;
        }

        try {
            if (this.isEditing && this.data?.id) {
                const updatedTask: Task = {
                    ...this.data,
                    ...formValue,
                    companyId: activeCompanyId
                };
                await this.taskService.updateTask(updatedTask);
                this.dialogRef.close(updatedTask);
            } else {
                const newTask = await this.taskService.createTask({
                    ...formValue,
                    companyId: activeCompanyId
                });
                this.dialogRef.close(newTask);
            }
        } catch (error) {
            console.error('Error saving task:', error);
        } finally {
            this.isSaving = false;
        }
    }

    onCancel(): void {
        this.dialogRef.close(null);
    }
}