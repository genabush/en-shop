/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

import { StoreConfig } from '../environment.interface';

export const storeConfiguration: StoreConfig[] = [
  {
    shopName: 'thebodyshop-au',
    paypalConfig: {
      clientId: 'AaLR93naB4wmRv2tANurbaboXDEXQ6UlXwAbtNSB8YrjFLslzmOhGShlMjMQJlvAOGwbOG3HThbp9haf',
      currency: 'EUR'
    },
    gigyaConfig: {
      apiKey: '3_3-Z2xMFxT-nOuphPBFBduUhKrZQF014yyIwbwzmkRioJQMf1CRfTC5xYUQ9XBYpb',
      enableDebug: true,
      gigyaDataCenter: 'au1.gigya.com'
    }
  },
  {
    shopName: 'thebodyshop-uk',
    paypalConfig: {
      clientId: 'Ab29lN4aCLLfL2GsUM4b6iLIN45w0XPuFLFK8bzlZZ4pxo-egYtyFRpc6VsdkYd8oPrBEYLSeHE4eCDA',
      currency: 'GBP'
    },
    gigyaConfig: {
      apiKey: '3_19V2TmEUzQ6qV7wXJJ3vzTwG_tbY_PD-PlzB0ASVWe5NGmq-xJaFKVN94SdvBVDe',
      enableDebug: true,
      gigyaDataCenter: 'eu1.gigya.com'
    }
  },
  {
    shopName: 'thebodyshop-ca',
    gigyaConfig: {
      apiKey: '3_TnX5IMus9CKaa_wQIhilzyjubly6AuXErAM2xaB-kS8J0LQm9OSPJDzm23ZNeoTZ',
      enableDebug: true,
      gigyaDataCenter: 'us1.gigya.com'
    }
  },
  {
    shopName: 'thebodyshop-de',
    paypalConfig: {
      clientId: 'AUsoc3Ha09PpRx0EsDCcDJdtlJ3qZbBeYYkwUs76AB7mWq-GFd2PDi-WanvMXdcw-e27A3C6rh_MV5PY',
      currency: 'EUR'
    },
    gigyaConfig: {
      apiKey: '3_LbdVBXWmgeqYZeyVTCm_jK9T04n04p4Un5TIgA950VC9ndofD8yi8qFSSFi540JN',
      enableDebug: true,
      gigyaDataCenter: 'eu1.gigya.com'
    }
  },
  {
    shopName: 'thebodyshop-dk',
    paypalConfig: {
      clientId: 'AebOYhX8bzBrKF7EgGEbrDWGi-R_yIdooiISj6iPe0OE_iSe0TY2iGWYxVmGyawT3lUFgX7nuwYI1a-s',
      currency: 'DKK'
    },
    gigyaConfig: {
      apiKey: '3_coEdAmyVkXbE71Bf4zX3sAAKHEBFdYa76zA49TkSbmSRjIm7B5cY5mqJtb_NVnP2',
      enableDebug: true,
      gigyaDataCenter: 'eu1.gigya.com'
    }
  },
  {
    shopName: 'thebodyshop-es',
    paypalConfig: {
      clientId: 'AZrjCa3DHqIM0nbG-CPEkeqq9fXLrQ-Pkg6bhaTT-s8Ku3ucJ_AYZ2hZpXebRSdzPvZc2BeRXiOtIUSb',
      currency: 'EUR'
    },
    gigyaConfig: {
      apiKey: '3_Rtcky500RK_FRhoy1qWZKCGROZP_f8TOOweZySI-8OCCUoqykfjWTVTkTs4cVusW',
      enableDebug: true,
      gigyaDataCenter: 'eu1.gigya.com'
    }
  },
  {
    shopName: 'thebodyshop-fr',
    paypalConfig: {
      clientId: 'AYukIuZgpN2JbYUH6Q5yuT0VjjfZsciyQ9jbdwvUtAI9UKsuRywyfQmWerCmZos5em5ZaWoXzSX5ganJ',
      currency: 'EUR'
    },
    gigyaConfig: {
      apiKey: '3_dh0yq0Rpj3oIuLeDgkMiIUSJPNoC3pdlHHVWeAk1pgvqbNk0ChLB74eCsi2BknxK',
      enableDebug: true,
      gigyaDataCenter: 'eu1.gigya.com'
    }
  },
  {
    shopName: 'thebodyshop-nl',
    paypalConfig: {
      clientId: 'AebOYhX8bzBrKF7EgGEbrDWGi-R_yIdooiISj6iPe0OE_iSe0TY2iGWYxVmGyawT3lUFgX7nuwYI1a-s',
      currency: 'EUR'
    },
    gigyaConfig: {
      apiKey: '3_k6hCERs7aCV7MnDjqqE4OVv1_-QQDkxhvbCt8nEQ6FGhNS504YRT8jCR7GzWK-kE',
      enableDebug: true,
      gigyaDataCenter: 'eu1.gigya.com'
    }
  },
  {
    shopName: 'thebodyshop-se',
    paypalConfig: {
      clientId: 'ARcgZTSKLoVxuWAEScFaUKnS2MxSYbfr3cYIqbB9SfveAorcdLSfH8uTXiOlOw37XDreCyKdCeUmsCdo',
      currency: 'SEK'
    },
    gigyaConfig: {
      apiKey: '3_eQ_tC-8TU5ChEotQzBnAmiHXWSQ_4lpeKa4mrnm3uEPGRaG6C6CtsgV-jRSzyuQI',
      enableDebug: true,
      gigyaDataCenter: 'eu1.gigya.com'
    }
  },
  {
    shopName: 'thebodyshop-us',
    gigyaConfig: {
      apiKey: '3_i0aPsIiPHyAPXrsYCMEALOBS5e29h31WnNFW2uLv1tSh3_7Ajgo3N22g-z_-LXbV',
      enableDebug: true,
      gigyaDataCenter: 'us1.gigya.com'
    }
  },
  {
    shopName: 'thebodyshop-at',
    paypalConfig: {
      clientId: 'AclV2YrdZBTiMhVNls_kKrkHlgWjHTReNSaKRn_lonY1Q0PjQHxOOJ3pMvuAekG6MzLfoj7N6ZY3k_uY',
      currency: 'AUD'
    },
    gigyaConfig: {
      apiKey: '3_GLPQXWkes8Ge6bT7v1UNei_-P4A431yXX6_ILVX-OToJ9GmagKWJclFcNTw03mmb',
      enableDebug: true,
      gigyaDataCenter: 'eu1.gigya.com'
    }
  },
  {
    shopName: 'thebodyshop-hk',
    gigyaConfig: {
      apiKey: '3_0uhYPw1F2pcThlWLsEaefI8xC6yjmtBXDylL70FrFeRrDonx5YLQMOiyUSnGRdg8',
      enableDebug: true,
      gigyaDataCenter: 'au1.gigya.com'
    }
  },
  {
    shopName: 'thebodyshop-sg',
    gigyaConfig: {
      apiKey: '3_5WYHqNgAXrm17f9ze5JEGVFtg0YaJkxbKw6LJhFyX1JTeuNuMgHZgWJQqPHRdr-8',
      enableDebug: true,
      gigyaDataCenter: 'au1.gigya.com'
    }
  }
];
