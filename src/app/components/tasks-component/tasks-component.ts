import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { TranslocoModule } from '@jsverse/transloco';

@Component({
  selector: 'app-tasks-component',
  imports: [
    MatButtonModule,
    MatIconModule,
    TranslocoModule
  ],
  templateUrl: './tasks-component.html',
  styleUrl: './tasks-component.css',
})
export class TasksComponent {

}
