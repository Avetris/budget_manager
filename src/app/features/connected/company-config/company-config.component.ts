import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormArray, FormBuilder, FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { CompanyService } from '@core/services/company.service';

@Component({
  selector: 'app-company-config',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './company-config.component.html',
  styleUrl: './company-config.component.scss'
})
export class CompanyConfigComponent implements OnInit {
  private fb = inject(FormBuilder).nonNullable;
  private snackBar = inject(MatSnackBar);
  readonly companyService = inject(CompanyService);

  readonly isSaving = signal<boolean>(false);

  form = this.fb.group({
    name: ['', Validators.required],
    nif: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    phone: [''],
    web: [''],
    street: [''],
    city: [''],
    conditions: [''],
    garanty: [''],
    header: this.fb.array<FormControl<string>>([])
  });

  // Getter helper para iterar sobre los controles de la lista header en el HTML
  get headerArray(): FormArray<FormControl<string>> {
    return this.form.get('header') as FormArray<FormControl<string>>;
  }

  get headerControls(): FormControl<string>[] {
    return this.headerArray.controls;
  }

  ngOnInit(): void {
    const currentCompany = this.companyService.activeCompany();
    if (currentCompany) {
      // 1. Poblamos los campos simples
      this.form.patchValue(currentCompany);

      // 2. Poblamos el FormArray 'header' si contiene elementos
      if (Array.isArray(currentCompany.header)) {
        this.headerArray.clear();
        currentCompany.header.forEach(line => this.addHeaderLine(line));
      }
    }
  }

  addHeaderLine(value: string = ''): void {
    this.headerArray.push(this.fb.control(value));
  }

  removeHeaderLine(index: number): void {
    this.headerArray.removeAt(index);
  }

  async onSave() {
    if (this.form.invalid) return;

    this.isSaving.set(true);
    try {
      const updatedConfig = this.form.value;

      await this.companyService.updateActiveCompany(updatedConfig);

      this.snackBar.open('Configuración guardada correctamente', 'Cerrar', { duration: 3000 });
    } catch (error) {
      console.error('Error al guardar la configuración:', error);
      this.snackBar.open('Error al guardar los cambios', 'Cerrar', { duration: 4000 });
    } finally {
      this.isSaving.set(false);
    }
  }
}