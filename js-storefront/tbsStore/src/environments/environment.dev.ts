import { Environment } from './environment.interface';
import { storeConfiguration } from './storeConfiguration/storeConfig';

export const environment: Environment = {
  production: false,
  baseUrl: 'https://localhost:9002/',
  amplienceContentPath: 'https://c1.adis.ws/v1/content/thebodyshop/content-item/',
  amplienceMasterTemplateName: 'master-template',
  pagesInterceptUrl: () => environment.baseUrl + '/rest/v2/thebodyshop-uk/cms/pages',
  enableMockData: true,
  mockEndpoints: {
    pages: {
      homePage: {
        amplience: {
          name: 'homePage',
          enabled: true,
          endpoint: 'assets/mock-data/pages/homepage/amplience-component/pages.json',
          pathname: 'pageLabelOrId=homepage',
          isValid: () =>
            window.location.pathname === environment.mockEndpoints.pages.homePage.amplience.pathname ? true : false
        },
        marketSelector: {
          name: 'homePage',
          enabled: false,
          endpoint: 'assets/mock-data/pages/homepage/market-selector/pages.json',
          pathname: 'pageLabelOrId=homepage',
          isValid: () =>
            window.location.pathname === environment.mockEndpoints.pages.homePage.marketSelector.pathname ? true : false
        },
        olapic: {
          name: 'homePage',
          enabled: false,
          endpoint: 'assets/mock-data/pages/homepage/olapic/pages.json',
          pathname: 'pageLabelOrId=homepage',
          isValid: () => true
        }
      }
    },
    components: {
      amplience: {
        name: 'amplienceComponent',
        enabled: false,
        endpoint: 'assets/mock-data/components/amplience/components.json',
        pathname: '/cms/components/AmplienceCMSComponent',
        isValid: () => true
      }
    }
  },
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
    mapsApiKey: 'AIzaSyAy8RckATcRgFdzKwgjtioIxthC4Tp_LXI'
  },
  termsLink: 'https://www.thebodyshop.com/en-gb/termsconditions/services/terms_promos'
};
