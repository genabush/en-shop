import { Environment } from './environment.interface';
import { storeConfiguration } from './storeConfiguration/storeConfig';

// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` no longer replaces `environment.ts` with `environment.prod.ts - CCV2 is currently not using it`.
// environment.ts is the new production config -
//
// The list of file replacements can be found in `angular.json`.

export const environment: Environment = {
  production: false,
  baseUrl: 'https://localhost:9002',
  amplienceContentPath: 'https://c1.adis.ws/v1/content/thebodyshop/content-item/',
  amplienceMasterTemplateName: 'master-template',
  olapicConfig: {
    src: 'https://photorankstatics-a.akamaihd.net/81b03e40475846d5883661ff57b34ece/static/frontend/latest/build.min.js',
    apiKey: 'ee66e4f829f88d5fba4d0a4559e4a0aebcdedd71290d308f00958e44ab9682d6',
    lang: 'en_GB'
  },
  adyenConfig: {
    accountId: 'TheBodyShopCOM',
    locale: 'en-GB',
    environment: 'test'
  },
  storeConfig: storeConfiguration,
  googleConfig: {
    mapsApiKey: 'AIzaSyAy8RckATcRgFdzKwgjtioIxthC4Tp_LXI' // this key is for development  and it needs to be replaced when we change production flag to true
  },
  termsLink: 'https://www.thebodyshop.com/en-gb/termsconditions/services/terms_promos'
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
