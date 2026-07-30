import { Component, inject, OnInit } from '@angular/core';
import { AuthService } from '@core/services/auth.service';
import { Router } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { TranslocoModule } from '@jsverse/transloco';

@Component({
  selector: 'app-login-component',
  imports: [
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    ReactiveFormsModule,
    TranslocoModule,
  ],
  templateUrl: './login.component.html',
  standalone: true,
  styleUrl: './login.component.css',
})
export class LoginComponent implements OnInit {

  authService = inject(AuthService);
  router = inject(Router);

  form: FormGroup = new FormGroup({
    user: new FormControl(''),
    password: new FormControl(''),
  });

  error: string = "";

  ngOnInit(): void {
    if (this.authService.isLoggedIn$) {
      this.router.navigate(['']);
    }
  }

  submit() {
    if (this.form.valid) {
      this.authService.login(this.form.value.user, this.form.value.password).then(res => {
        console.log(res);
        this.router.navigate(['']);
      }, err => {
        this.error = "login.error";
      });
    }
  }
}
