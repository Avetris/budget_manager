import { Component, inject, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatTableModule } from '@angular/material/table';
import { TranslocoModule } from '@jsverse/transloco';
import { MatTabsModule } from '@angular/material/tabs';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { BudgetsComponent } from '../budgets/budgets.component';
import { ClientsComponent } from '../clients/clients.component';
import { TasksComponent } from '../tasks/tasks.component';
import { UserService } from '@core/services/user.service';
import { CompanyService } from '@core/services/company.service';
import { LoadingService } from '@core/services/loading.service';
import { AuthService } from '@core/services/auth.service';
import { Router, RouterModule } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';

@Component({
  selector: 'app-content-component',
  imports: [
    MatFormFieldModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatMenuModule,
    MatProgressSpinnerModule,
    MatTableModule,
    MatTabsModule,
    MatToolbarModule,
    TranslocoModule,
    BudgetsComponent,
    ClientsComponent,
    TasksComponent,
    RouterModule
  ],
  templateUrl: './content.component.html',
  styleUrl: './content.component.scss',
})
export class ContentComponent implements OnInit {
  authService = inject(AuthService)
  loadingService = inject(LoadingService)
  userService = inject(UserService)
  companyService = inject(CompanyService)
  router = inject(Router)

  ngOnInit(): void {
  }

  console() {
    console.log("NETRA")
  }
  async logout() {
    await this.authService.logout()
    this.router.navigateByUrl("/login")
  }
}
