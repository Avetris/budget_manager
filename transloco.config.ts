import {TranslocoGlobalConfig} from '@jsverse/transloco-utils';
    
const config: TranslocoGlobalConfig = {
  rootTranslationsPath: 'public/i18n/',
  langs: [ 'en', 'es' ],
  defaultLang: 'es',
  keysManager: {}
};
    
export default config;