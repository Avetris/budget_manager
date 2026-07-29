import { Routes } from '@angular/router';
import { LoginComponent } from '@components/login-component/login-component';
import { ContentComponent } from '@components/connected/content-component/content-component';
import { authGuard } from '@services/auth-service';
import { BudgetComponent } from '@components/connected/budgets-component/budget-component/budget-component';

export const routes: Routes = [
  { path: '', canActivate: [authGuard], component: ContentComponent },
  { path: 'login', component: LoginComponent },
  { path: 'budget', canActivate: [authGuard], component: BudgetComponent },
  { path: 'budget/:id', canActivate: [authGuard], component: BudgetComponent },
  {
    path: '**',
    redirectTo: '',
    pathMatch: 'full'
  }
];