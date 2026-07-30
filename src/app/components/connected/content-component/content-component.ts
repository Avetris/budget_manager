import { Component, inject, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatTableModule } from '@angular/material/table';
import { TranslocoModule } from '@jsverse/transloco';
import { MatTabsModule } from '@angular/material/tabs';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { BudgetsComponent } from '@components/connected/budgets-component/budgets-component';
import { ClientsComponent } from '@components/connected/clients-component/clients-component';
import { TasksComponent } from '@components/tasks-component/tasks-component';
import { UserService } from '@services/user-service';
import { CompanyService } from '@services/company-service';
import { LoadingService } from '@services/loading.service';
import { AuthService } from '@services/auth-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-content-component',
  imports: [
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
    TasksComponent
  ],
  templateUrl: './content-component.html',
  styleUrl: './content-component.css',
})
export class ContentComponent implements OnInit {
  authService = inject(AuthService)
  loadingService = inject(LoadingService)
  userService = inject(UserService)
  companyService = inject(CompanyService)
  router = inject(Router)

  ngOnInit(): void {
  }

  async logout() {
    await this.authService.logout()
    this.router.navigateByUrl("/login")
  }
}
