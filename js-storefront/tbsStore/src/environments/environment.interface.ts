export interface Environment {
  production: boolean;
  baseUrl: string;
  amplienceContentPath: string;
  amplienceMasterTemplateName: string;
  pagesInterceptUrl?: Function;
  enableMockData?: boolean;
  mockEndpoints?: MockEndpoints;
  olapicConfig: Olapic;
  adyenConfig: AdyenConfig;
  storeConfig: StoreConfig[];
  googleConfig: GoogleConfig;
  termsLink: string;
}

export interface Olapic {
  src: string;
  apiKey: string;
  lang: string;
}

export interface MockEndpoints {
  pages: MockPageTypes;
  components: MockComponentTypes;
}

export interface MockPageTypes {
  homePage: HomePageScenarios;
}

export interface MockComponentTypes {
  amplience: MockEndPoint;
}

export interface HomePageScenarios {
  amplience: MockEndPoint;
  marketSelector: MockEndPoint;
  olapic: MockEndPoint;
}

export interface MockEndPoint {
  name: string;
  enabled: boolean;
  endpoint: string;
  pathname: string;
  isValid: Function;
}
export interface AdyenConfig {
  accountId: string;
  locale: string;
  environment: string;
}

export interface PaypalConfig {
  clientId: string;
  currency: string;
}

export interface StoreConfig {
  shopName: string;
  paypalConfig?: PaypalConfig;
  gigyaConfig?: GigyaRaasConfig;
}

export interface GoogleConfig {
  mapsApiKey: string;
}

export interface GigyaRaasConfig {
  apiKey: string;
  enableDebug?: boolean;
  gigyaDataCenter: string;
}
