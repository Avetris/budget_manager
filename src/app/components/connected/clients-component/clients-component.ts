import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { TranslocoModule } from '@jsverse/transloco';

@Component({
  selector: 'app-clients-component',
  imports: [
    MatButtonModule,
    MatIconModule,
    TranslocoModule
  ],
  templateUrl: './clients-component.html',
  styleUrl: './clients-component.css',
})
export class ClientsComponent {

}
