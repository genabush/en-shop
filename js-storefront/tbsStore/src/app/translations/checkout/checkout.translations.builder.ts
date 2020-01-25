import {
  en_Address,
  fr_Address,
  es_Address,
  de_Address,
  nl_Address,
  da_Address,
  sv_Address,
  mobileDisclaimers
} from './custom/address.form.translations.parts';
import { en_CheckoutTranslationOverwrites } from './overwrites/checkout.en.translations.overwrites';

export const checkoutTranslationsBuilder = {
  en: {
    ...en_CheckoutTranslationOverwrites, // TODO replace when available
    address: {
      addressForm: {
        ...en_Address,
        phoneNumber: { label: 'Mobile Number', placeholder: '+44 987 654 3210', disclaimer: mobileDisclaimers.en_GB },
        state: { label: 'County', placeholder: 'County' },
        zipCode: { label: 'Post code', placeholder: 'Post code' }
      }
    }
  },
  en_GB: {
    ...en_CheckoutTranslationOverwrites, // TODO replace when available
    address: {
      addressForm: {
        ...en_Address,
        phoneNumber: { label: 'Mobile Number', placeholder: '+44 987 654 3210', disclaimer: mobileDisclaimers.en_GB },
        state: { label: 'County', placeholder: 'County' },
        zipCode: { label: 'Post code', placeholder: 'Post code' }
      }
    }
  },
  // english us
  en_US: {
    ...en_CheckoutTranslationOverwrites, // TODO replace when available
    address: {
      addressForm: {
        ...en_Address,
        phoneNumber: { label: 'Mobile Number', placeholder: '(555) 555-0123', disclaimer: mobileDisclaimers.en_US },
        state: { label: 'State', placeholder: 'State' },
        zipCode: { label: 'Post code', placeholder: 'Post code' }
      }
    }
  },
  // english australia
  en_AU: {
    ...en_CheckoutTranslationOverwrites, // TODO replace when available
    address: {
      addressForm: {
        ...en_Address,
        phoneNumber: { label: 'Mobile', placeholder: '(09) 8765 4321', disclaimer: mobileDisclaimers.en_AU }, // TODO update
        city: { label: 'Suburb', placeholder: 'Suburb' },
        state: { label: 'State', placeholder: 'State' }
      }
    }
  },
  // english canada
  en_CA: {
    ...en_CheckoutTranslationOverwrites, // TODO replace when available
    address: {
      addressForm: {
        ...en_Address,
        phoneNumber: { label: 'Mobile', placeholder: '(555) 555-0123', disclaimer: mobileDisclaimers.en_CA },
        state: { label: 'Province', placeholder: 'Province' },
        zipCode: { label: 'Postal Code', placeholder: 'Postal Code' }
      }
    }
  },
  // english hong kong
  en_HK: {
    ...en_CheckoutTranslationOverwrites, // TODO replace when available
    address: {
      addressForm: {
        ...en_Address,
        phoneNumber: { label: 'Mobile Number', placeholder: '+852 9876 5432', disclaimer: mobileDisclaimers.en_HK }
      }
    }
  },
  // english singapore
  en_SG: {
    ...en_CheckoutTranslationOverwrites, // TODO replace when available
    address: {
      addressForm: {
        ...en_Address,
        phoneNumber: { label: 'Mobile Number', placeholder: '+65 9876 5432', disclaimer: mobileDisclaimers.en_SG }
      }
    }
  },
  // french canada
  fr_CA: {
    ...en_CheckoutTranslationOverwrites, // TODO replace when available
    address: {
      addressForm: {
        ...fr_Address,
        lastName: { label: 'Nom de famille', placeholder: 'Nom de famille' },
        phoneNumber: { label: 'Cellulaire', placeholder: '(555) 555-5555', disclaimer: mobileDisclaimers.fr_CA },
        state: { label: 'Province', placeholder: 'Province' },
        zipCode: { label: 'Code postal', placeholder: 'Code postal' }
      }
    }
  },
  // france
  fr: {
    ...en_CheckoutTranslationOverwrites, // TODO replace when available
    address: {
      addressForm: {
        ...fr_Address,
        companyName: { label: 'Entreprise', placeholder: 'Entreprise' },
        lastName: { label: 'Nom', placeholder: 'Nom' },
        phoneNumber: {
          label: 'Numéro de téléphone',
          placeholder: '+590 590 98 76 54',
          disclaimer: mobileDisclaimers.fr
        },
        zipCode: { label: 'Code postal', placholder: 'Code postal' }
      }
    }
  },
  // spain
  es: {
    ...en_CheckoutTranslationOverwrites, // TODO replace when available
    address: {
      addressForm: {
        ...es_Address,
        companyName: { label: 'Empresa', placeholder: 'Empresa' },
        phoneNumber: { label: 'Móvil', placeholder: '+34 987 654 321', disclaimer: mobileDisclaimers.es },
        city: { label: 'Población / Ciudad', placeholder: 'Población / Ciudad' },
        zipCode: { label: 'C.P.', placeholder: 'C.P.' }
      }
    }
  },
  // spain
  es_MX: {
    ...en_CheckoutTranslationOverwrites, // TODO replace when available
    address: {
      addressForm: {
        ...es_Address,
        companyName: { label: 'Empresa', placeholder: 'Empresa' },
        phoneNumber: { label: 'Celular', placeholder: '+52 9876543210987', disclaimer: mobileDisclaimers.es_MX },
        city: { label: 'Ciudad', placeholder: 'Ciudad' },
        zipCode: { label: 'Código postal', placeholder: 'Código postal' }
      }
    }
  },
  // netherlands
  nl: {
    ...en_CheckoutTranslationOverwrites, // TODO replace when available
    address: {
      addressForm: {
        ...nl_Address
      }
    }
  },
  // denmark
  da: {
    ...en_CheckoutTranslationOverwrites, // TODO replace when available
    address: {
      addressForm: {
        ...da_Address
      }
    }
  },
  // sweden
  sv: {
    ...en_CheckoutTranslationOverwrites, // TODO replace when available
    address: {
      addressForm: {
        ...sv_Address
      }
    }
  },
  // german
  de: {
    ...en_CheckoutTranslationOverwrites, // TODO replace when available
    address: {
      addressForm: {
        ...de_Address
      }
    }
  },
  // austria german
  de_AT: {
    ...en_CheckoutTranslationOverwrites, // TODO replace when available
    address: {
      addressForm: {
        ...de_Address,
        phoneNumber: { label: 'Mobilnummer', placeholder: '+43 9876 5432', disclaimer: mobileDisclaimers.de_AT }
      }
    }
  }
};
