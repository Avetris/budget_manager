import { Component, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { TranslocoModule } from '@jsverse/transloco';
import { CompanyService } from '@core/services/company.service';

@Component({
  selector: 'app-tasks-component',
  imports: [
    MatButtonModule,
    MatIconModule,
    TranslocoModule
  ],
  templateUrl: './tasks.component.html',
  styleUrl: './tasks.component.css',
})
export class TasksComponent {
  companyService = inject(CompanyService)
}
