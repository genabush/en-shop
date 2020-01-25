import { DeliveryMode, Address, Region } from '@spartacus/core';
import { Card } from '@spartacus/storefront';

export interface IDeliveryModesResponse {
  deliveryModes: DeliveryMode[];
  deliveryModeRestrictionError?: {
    errorList: string[];
  };
}

export interface IDeliveryAddressResponse {
  restrictedProducts: string[];
}

export interface IBillingAddressResponse {
  addresses: ICustomAddress[];
}

export interface ICustomCard {
  address: Address;
  card: Card;
}

export interface ICustomAddress extends Address {
  billingAddress?: boolean;
}

export interface CardWithAddress {
  card: Card;
  address: ICustomAddress;
}

export interface IDeliveryModesChoice {
  modeIndex: number;
  deliveryMode: DeliveryMode;
}

export interface IDeliveryRestrictedState {
  isDeliveryRestricted: boolean;
  restrictedProducts?: string[];
}

export interface IDeliveryModesRestrictedState {
  isDeliveryModesRestricted: boolean;
  errorsList?: string[];
}

export interface ICheckoutOrchestratorState {
  fullFillmentState: boolean;
  deliveryModesState: boolean;
  paymentDetailsState: boolean;
  whoWillCollectState: boolean;
}

export interface ICustomRegionList {
  [key: string]: Region[];
}

export enum CheckoutFulfillmentTabs {
  'DELIVERY',
  'COLLECT_IN_STORE',
  'COLLECTION_POINT'
}

// https://en.wikipedia.org/wiki/Telephone_numbers_in_the_United_Kingdom
// https://en.wikipedia.org/wiki/North_American_Numbering_Plan
// https://en.wikipedia.org/wiki/Telephone_numbers_in_France
// https://en.wikipedia.org/wiki/Telephone_numbers_in_Spain (to expand)
// https://en.wikipedia.org/wiki/Telephone_numbers_in_Germany
// https://en.wikipedia.org/wiki/Telephone_numbers_in_Denmark
// https://en.wikipedia.org/wiki/Telephone_numbers_in_Austria
// https://en.wikipedia.org/wiki/Telephone_numbers_in_the_Netherlands
// https://en.wikipedia.org/wiki/Telephone_numbers_in_Hong_Kong
// https://en.wikipedia.org/wiki/Telephone_numbers_in_Sweden (to expand)
// https://en.wikipedia.org/wiki/Telephone_numbers_in_Singapore
// https://en.wikipedia.org/wiki/Telephone_numbers_in_Mexico

// Note: When implementing regular expressions for nG remember to double escape "\\"
/* prettier-ignore */
export const PHONE_NUMBER_PATTERNS = {
  GB: '^((\\+44){1}\\s([0-9]{3}){1}\\s([0-9]{3}){1}\\s([0-9]{4}){1}|(0044){1}\\s([0-9]{3}){1}\\s([0-9]{3}){1}\\s([0-9]{4}){1}|(0){1}([0-9]{10}){1})?$',
  US: '^(\\(([0-9]{3}\\)))?\\s([0-9]{3}){1}-?([0-9]{4}){1}?$',
  CA: '^(\\(([0-9]{3}\\)))?\\s([0-9]{3}){1}-?([0-9]{4}){1}?$',
  AU: '^\\(0([0-9]){1}\\)?\\s([0-9]{4}){1}\\s([0-9]{4}){1}?$',
  SG: '^((\\+65){1}\\s([0-9]{4}){1}\\s([0-9]{4}){1}|(0065){1}\\s([0-9]{4}){1}\\s([0-9]{4}){1}|(0){1}([0-9]{8}){1})?$',
  HK: '^((\\+852){1}\\s([0-9]{4}){1}\\s([0-9]{4}){1}|(00852){1}\\s([0-9]{4}){1}\\s([0-9]{4}){1}|(0){1}([0-9]{4}){1}\\s([0-9]{4}){1})?$',
  FR: '^((\\+590){1}\\s((590|690){1})\\s([0-9]{2})\\s([0-9]{2})\\s([0-9]{2})|(00590){1}\\s(590|690){1}\\s([0-9]{2})\\s([0-9]{2})\\s([0-9]{2})|(0){1}((590|690){1})\\s([0-9]{2})\\s([0-9]{2})\\s([0-9]{2}))?$',
  ES: '^((\\+34){1}\\s([0-9]{3}){1}\\s([0-9]{3}){1}\\s([0-9]{3}){1}|(0034){1}\\s([0-9]{3}){1}\\s([0-9]{3}){1}\\s([0-9]{3}){1}|(0){1}([0-9]{3}){1}\\s([0-9]{3}){1}\\s([0-9]{3}){1})?$',
  DE: '^((\\+49){1}\\s([0-9]{10}){1}|(0049){1}\\s([0-9]{10}){1}|(0){1}([0-9]{10}){1})?$',
  DA: '^((\\+45){1}\\s([0-9]{2})-([0-9]{2})-([0-9]{2})-([0-9]{2})|(0045){1}\\s([0-9]{2})-([0-9]{2})-([0-9]{2})-([0-9]{2})|(0){1}([0-9]{2})-([0-9]{2})-([0-9]{2})-([0-9]{2}))?$',
  AT: '^((\\+43){1}\\s([0-9]{4}){1}\\s([0-9]{4})|(0043){1}\\s([0-9]{4}){1}\\s([0-9]{4}){1}|(0){1}([0-9]{4}){1}\\s([0-9]{4}){1})?$',
  NL: '^((\\+31){1}\\s([0-9]{9})|(0031){1}\\s([0-9]{9})|(0){1}([0-9]{9}))?$',
  SV: '^((\\+46){1}\\s([0-9]{9}){1}|(0046){1}\\s([0-9]{9}){1}|(0){1}([0-9]{9}){1})?$',
  MX: '^((\\+52){1}\\s([0-9]{10,13}){1}|(0052){1}\\s([0-9]{10,13}){1}|(0){1}([0-9]{10,13}){1})?$'  
};
/* prettier-ignore-end */

// TODO default as we don't have information for the relavant markets
export const DEFAULT_ADDRESS_MAXLENGTHS = {
  line1: {
    maxlength: 45
  },
  line2: {
    maxlength: 45
  },
  city: {
    maxlength: 35
  },
  phone: {
    maxlength: 16
  },
  postcode: {
    maxlength: 10
  }
};

export const MARKET_FIELD_VALIDATORS = {
  en: {
    ...DEFAULT_ADDRESS_MAXLENGTHS,
    phone: {
      maxlength: 17
    }
  },
  en_GB: {
    ...DEFAULT_ADDRESS_MAXLENGTHS,
    phone: {
      maxlength: 17
    }
  },
  en_US: {
    ...DEFAULT_ADDRESS_MAXLENGTHS,
    phone: {
      maxlength: 14
    }
  },
  en_CA: {
    ...DEFAULT_ADDRESS_MAXLENGTHS,
    phone: {
      maxlength: 14
    }
  },
  en_AU: {
    ...DEFAULT_ADDRESS_MAXLENGTHS,
    phone: {
      maxlength: 14
    }
  },
  en_SG: {
    ...DEFAULT_ADDRESS_MAXLENGTHS,
    phone: {
      maxlength: 14
    }
  },
  en_HK: {
    ...DEFAULT_ADDRESS_MAXLENGTHS,
    phone: {
      maxlength: 15
    }
  },
  fr: {
    ...DEFAULT_ADDRESS_MAXLENGTHS,
    phone: {
      maxlength: 17
    }
  },
  fr_CA: {
    ...DEFAULT_ADDRESS_MAXLENGTHS,
    phone: {
      maxlength: 14
    }
  },
  es: {
    ...DEFAULT_ADDRESS_MAXLENGTHS,
    phone: {
      maxlength: 16
    }
  },
  es_MX: {
    ...DEFAULT_ADDRESS_MAXLENGTHS,
    phone: {
      maxlength: 18
    }
  },
  nl: {
    ...DEFAULT_ADDRESS_MAXLENGTHS,
    phone: {
      maxlength: 14
    }
  },
  da: {
    ...DEFAULT_ADDRESS_MAXLENGTHS,
    phone: {
      maxlength: 16
    }
  },
  sv: {
    ...DEFAULT_ADDRESS_MAXLENGTHS,
    phone: {
      maxlength: 14
    }
  },
  de: {
    ...DEFAULT_ADDRESS_MAXLENGTHS,
    phone: {
      maxlength: 14
    }
  },
  de_AT: {
    ...DEFAULT_ADDRESS_MAXLENGTHS,
    phone: {
      maxlength: 14
    }
  }
};

export interface LoyaltyVouchers {
  loyaltyVouchers: LoyaltyVoucher[];
  errorMessage?: string;
  maxVouchersReached?: boolean;
}
export interface LoyaltyVoucher {
  applied: boolean;
  expiryDate: string;
  type: string;
  value: number;
  voucherId: string;
}
