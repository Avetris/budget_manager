import { Routes } from '@angular/router';
import { LoginComponent } from './features/login/login.component';
import { ContentComponent } from './features/connected/content/content.component';
import { authGuard } from '@core/services/auth.service';
import { CompanyConfigComponent } from './features/connected/company-config/company-config.component';

export const routes: Routes = [
  { path: '', canActivate: [authGuard], component: ContentComponent },
  { path: 'login', component: LoginComponent },
  { path: 'settings', canActivate: [authGuard], component: CompanyConfigComponent },
  {
    path: '**',
    redirectTo: '',
    pathMatch: 'full'
  }
];