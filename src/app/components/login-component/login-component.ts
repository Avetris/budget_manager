import { Component, inject, OnInit } from '@angular/core';
import { AuthService } from '@services/auth-service';
import { Router } from '@angular/router';
import { MatCard, MatCardContent, MatCardHeader, MatCardTitle } from '@angular/material/card'
import { MatFormField } from '@angular/material/form-field'
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { TranslocoModule } from '@jsverse/transloco';


@Component({
  selector: 'app-login-component',
  imports: [
    MatButtonModule,
    MatCard,
    MatCardContent,
    MatCardHeader,
    MatCardTitle,
    MatFormField,
    MatInputModule,
    ReactiveFormsModule,
    TranslocoModule,
  ],
  templateUrl: './login-component.html',
  standalone: true,
  styleUrl: './login-component.css',
})
export class LoginComponent implements OnInit {

  authService = inject(AuthService)
  router = inject(Router)

  form: FormGroup = new FormGroup({
    user: new FormControl(''),
    password: new FormControl(''),
  });

  error: string = ""
  
  ngOnInit(): void {
    if(this.authService.isLoggedIn$) {
        this.router.navigate([''])
    }      
  }

  submit() {
    if(this.form.valid) {
      this.authService.login(this.form.value.user, this.form.value.password).then(res => {
        console.log(res)
        this.router.navigate([''])
      }, err => {
        this.error = "login.error"
      })
    }
  }
}
