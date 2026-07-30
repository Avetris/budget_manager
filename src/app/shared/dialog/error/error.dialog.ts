import { Component, inject } from '@angular/core';
import { TranslocoModule } from '@jsverse/transloco';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';


export interface ErrorDialogData {
  title: string
  message: string
}

@Component({
  selector: 'app-error-dialog',
  imports: [
    MatButtonModule,
    MatDialogModule,
    TranslocoModule
  ],
  templateUrl: './error.dialog.html',
  styleUrl: './error.dialog.css',
})
export class ErrorDialog {
  readonly dialogRef = inject(MatDialogRef<ErrorDialog>);
  readonly data = inject<ErrorDialogData>(MAT_DIALOG_DATA);
}
