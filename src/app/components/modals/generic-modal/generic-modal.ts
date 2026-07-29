import { Component, inject } from '@angular/core';
import { TranslocoModule } from '@jsverse/transloco';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';


export interface GenericModalData {
  title: string
  message: string
}

@Component({
  selector: 'app-generic-modal',
  imports: [
    MatButtonModule,
    MatDialogModule,
    TranslocoModule
  ],
  templateUrl: './generic-modal.html',
  styleUrl: './generic-modal.css',
})
export class GenericModal {
  readonly dialogRef = inject(MatDialogRef<GenericModal>);
  readonly data = inject<GenericModalData>(MAT_DIALOG_DATA);
}
