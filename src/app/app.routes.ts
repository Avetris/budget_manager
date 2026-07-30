import { Routes } from '@angular/router';
import { LoginComponent } from './features/login/login.component';
import { ContentComponent } from './features/connected/content/content.component';
import { authGuard } from '@core/services/auth.service';
import { BudgetComponent } from './features/connected/budgets/budget/budget.component';
import { CompanyConfigComponent } from './features/connected/company-config/company-config.component';

export const routes: Routes = [
  { path: '', canActivate: [authGuard], component: ContentComponent },
  { path: 'login', component: LoginComponent },
  { path: 'budget', canActivate: [authGuard], component: BudgetComponent },
  { path: 'settings', canActivate: [authGuard], component: CompanyConfigComponent },
  { path: 'budget/:id', canActivate: [authGuard], component: BudgetComponent },
  {
    path: '**',
    redirectTo: '',
    pathMatch: 'full'
  }
];