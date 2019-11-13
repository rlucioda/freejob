import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import { ProdConfig } from './blocks/config/prod.config';
import { FreejobAppModule } from './app.module';

ProdConfig();

if (module['hot']) {
  module['hot'].accept();
}

platformBrowserDynamic()
  .bootstrapModule(FreejobAppModule, { preserveWhitespaces: true })
  // eslint-disable-next-line no-console
  .then(() => console.log('Application started'))
  .catch(err => console.error(err));
