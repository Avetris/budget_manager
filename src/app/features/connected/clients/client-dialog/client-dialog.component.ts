import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatIconModule } from '@angular/material/icon';
import { TranslocoModule } from '@jsverse/transloco';

import { Client } from '@core/models/client.model';
import { ClientService } from '@core/services/client.service';
import { CompanyService } from '@core/services/company.service';

@Component({
    selector: 'app-client-dialog',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        MatDialogModule,
        MatFormFieldModule,
        MatInputModule,
        MatButtonModule,
        MatButtonToggleModule,
        MatIconModule,
        TranslocoModule
    ],
    templateUrl: './client-dialog.component.html',
    styleUrl: './client-dialog.component.scss'
})
export class ClientDialogComponent implements OnInit {
    private fb = inject(FormBuilder).nonNullable;
    private dialogRef = inject(MatDialogRef<ClientDialogComponent>);
    private clientService = inject(ClientService);
    private companyService = inject(CompanyService);

    public data = inject<Client | null>(MAT_DIALOG_DATA, { optional: true });

    isEditing = false;
    isSaving = false;

    form = this.fb.group({
        isCompany: [false],
        name: ['', [Validators.required]],
        nif: ['', [Validators.required]],
        address: ['', [Validators.required]],
        phone: [''],
        email: ['', [Validators.email]]
    });

    ngOnInit(): void {
        if (this.data) {
            this.isEditing = true;
            this.form.patchValue({
                isCompany: this.data.isCompany ?? false,
                name: this.data.name,
                nif: this.data.nif,
                address: this.data.address,
                phone: this.data.phone ?? '',
                email: this.data.email ?? ''
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
                // Actualizar cliente existente
                const updatedClient: Client = {
                    ...this.data,
                    ...formValue,
                    companyId: activeCompanyId
                };
                await this.clientService.updateClient(updatedClient);
            } else {
                // Crear nuevo cliente
                const newClient: Omit<Client, 'id'> = {
                    ...formValue,
                    companyId: activeCompanyId
                };
                await this.clientService.createClient(newClient);
            }

            this.dialogRef.close(true);
        } catch (error) {
            console.error('Error saving client:', error);
        } finally {
            this.isSaving = false;
        }
    }

    onCancel(): void {
        this.dialogRef.close(false);
    }
}