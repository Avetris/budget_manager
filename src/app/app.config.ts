import { ApplicationConfig, provideZonelessChangeDetection, isDevMode, LOCALE_ID } from '@angular/core';
import { provideRouter, withViewTransitions } from '@angular/router';

import { routes } from './app.routes';
import { provideServiceWorker } from '@angular/service-worker';
import { environment } from '../environments/environment';

import { provideFirebaseApp, initializeApp, getApp } from '@angular/fire/app';
import { provideFirestore, persistentLocalCache, persistentMultipleTabManager, initializeFirestore } from '@angular/fire/firestore';
import { getAuth, provideAuth } from '@angular/fire/auth';
import { provideHttpClient } from '@angular/common/http';
import { TranslocoHttpLoader } from './transloco-loader';
import { provideTransloco } from '@jsverse/transloco';
import localeEs from '@angular/common/locales/es'; // Importamos el pack de español
import { registerLocaleData } from '@angular/common';

registerLocaleData(localeEs, 'es-ES');

export const appConfig: ApplicationConfig = {
  providers: [
    { provide: LOCALE_ID, useValue: 'es-ES' },
    provideZonelessChangeDetection(),
    provideRouter(routes, withViewTransitions()), // withViewTransitions añade animaciones suaves nativas
    provideServiceWorker('ngsw-worker.js', {
      enabled: !isDevMode(),
      registrationStrategy: 'registerWhenStable:30000'
    }),
    // Reemplaza esto con los datos de tu consola de Firebase
    
    provideFirebaseApp(() => initializeApp(environment.firebaseConfig)),
    provideAuth(() => getAuth()),
    provideFirestore(() => initializeFirestore(getApp(), {
        localCache: persistentLocalCache({
          tabManager: persistentMultipleTabManager(),
        }),
      })), 
    provideHttpClient(), 
    provideTransloco({
        config: { 
          availableLangs: ['en', 'es'],
          defaultLang: 'es',
          fallbackLang: 'es',
          reRenderOnLangChange: false,
          prodMode: !isDevMode(),
        },
        loader: TranslocoHttpLoader
      }), provideServiceWorker('ngsw-worker.js', {
            enabled: !isDevMode(),
            registrationStrategy: 'registerWhenStable:30000'
          })
  ]
};
