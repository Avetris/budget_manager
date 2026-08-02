import { Component, computed, inject, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { TranslocoModule } from '@jsverse/transloco';
import { CompanyService } from '@core/services/company.service';
import { MatDialog } from '@angular/material/dialog';
import { ClientDialogComponent } from './client-dialog/client-dialog.component';
import { Client } from '@core/models/client.model';
import { ClientService } from '@core/services/client.service';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatChipsModule } from '@angular/material/chips';
import { LoadingService } from '@core/services/loading.service';

@Component({
  selector: 'app-clients-component',
  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatChipsModule,
    MatFormFieldModule,
    MatInputModule,
    MatTooltipModule,
    MatProgressSpinnerModule,
    TranslocoModule
  ],
  templateUrl: './clients.component.html',
  styleUrl: './clients.component.scss',
})
export class ClientsComponent {
  readonly dialog = inject(MatDialog);
  readonly clientService = inject(ClientService);
  readonly companyService = inject(CompanyService);
  readonly loadingService = inject(LoadingService)

  displayedColumns: string[] = ['type', 'name', 'nif', 'contact', 'address', 'actions'];

  searchTerm = signal<string>('');

  filteredClients = computed(() => {
    const term = this.searchTerm().toLowerCase().trim();
    const clients = this.clientService.clients();

    if (!term) return clients;

    return clients.filter(c =>
      c.name.toLowerCase().includes(term) ||
      c.nif.toLowerCase().includes(term) ||
      (c.email && c.email.toLowerCase().includes(term)) ||
      (c.phone && c.phone.includes(term))
    );
  });

  onSearchChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.searchTerm.set(input.value);
  }

  openCreateClientModal() {
    this.dialog.open(ClientDialogComponent, {
      width: '520px',
      disableClose: true
    });
  }

  // Abrir para EDITAR cliente
  openEditClientModal(client: Client) {
    this.dialog.open(ClientDialogComponent, {
      width: '520px',
      data: client,
      disableClose: true
    });
  }

}
