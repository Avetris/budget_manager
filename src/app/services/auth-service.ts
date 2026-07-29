import { inject, Injectable } from '@angular/core';
import { Auth, authState, signInWithEmailAndPassword, signOut, UserCredential } from '@angular/fire/auth';
import { CanActivateFn, Router } from '@angular/router';
import { map } from 'rxjs';

export const authGuard: CanActivateFn = (route, state) => {
  const auth = inject(Auth)
  const router = inject(Router)

  return authState(auth).pipe(
    map(user => {
      if(user) {
        return true;
      } else {
        return router.createUrlTree(['/login'])
      }
    })
  );
};

@Injectable({ providedIn: 'root' })
export class AuthService {
  private auth = inject(Auth);

  user$ = authState(this.auth);

  isLoggedIn$ = authState(this.auth).pipe(
    map(user => !!user)
  );

  geCurrentUser() {
    return this.auth.currentUser
  }

  async login(email: string, password: string) : Promise<UserCredential> {
    return signInWithEmailAndPassword(this.auth, email, password);
  }

  async logout() {
    return signOut(this.auth);
  }
} 
