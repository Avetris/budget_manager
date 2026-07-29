import { Component, inject, OnInit } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { TranslocoModule } from '@jsverse/transloco';
import { MatTabsModule } from '@angular/material/tabs';
import { MatToolbarModule } from '@angular/material/toolbar';
import { BudgetsComponent } from '@components/connected/budgets-component/budgets-component';
import { ClientsComponent } from '@components/connected/clients-component/clients-component';
import { TasksComponent } from '@components/tasks-component/tasks-component';
import { ConfigService } from '@services/config-service';
import { AuthService } from '@services/auth-service';

@Component({
  selector: 'app-content-component',
  imports: [
    MatIconModule,
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
  configService = inject(ConfigService)  

  ngOnInit(): void {
    this.configService.getConfig()
  }
}
