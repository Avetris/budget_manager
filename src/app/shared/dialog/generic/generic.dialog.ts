import { Component, inject } from '@angular/core';
import { TranslocoModule } from '@jsverse/transloco';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';


export interface GenericDialogData {
  title: string
  message: string
}

@Component({
  selector: 'app-generic-dialog',
  imports: [
    MatButtonModule,
    MatDialogModule,
    TranslocoModule
  ],
  templateUrl: './generic.dialog.html',
  styleUrl: './generic.dialog.css',
})
export class GenericDialog {
  readonly dialogRef = inject(MatDialogRef<GenericDialog>);
  readonly data = inject<GenericDialogData>(MAT_DIALOG_DATA);
}
