import { Component, inject } from '@angular/core';
import { TranslocoModule } from '@jsverse/transloco';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';


export interface GenericModalData {
  title: string
  message: string
}

@Component({
  selector: 'app-error-modal',
  imports: [
    MatButtonModule,
    MatDialogModule,
    TranslocoModule
  ],
  templateUrl: './error-modal.html',
  styleUrl: './error-modal.css',
})
export class ErrorModal {
  readonly dialogRef = inject(MatDialogRef<ErrorModal>);
  readonly data = inject<GenericModalData>(MAT_DIALOG_DATA);
}
