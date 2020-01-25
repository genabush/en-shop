import { checkoutTranslationsBuilder } from './checkout/checkout.translations.builder';
import { en_CheckoutTranslationOverwrites } from './checkout/overwrites/checkout.en.translations.overwrites';

export const checkoutTranslations = {
  en: en_CheckoutTranslationOverwrites,
  en_GB: checkoutTranslationsBuilder.en_GB,
  en_US: checkoutTranslationsBuilder.en_US,
  en_CA: checkoutTranslationsBuilder.en_CA,
  en_AU: checkoutTranslationsBuilder.en_AU,
  en_SG: checkoutTranslationsBuilder.en_SG,
  es: checkoutTranslationsBuilder.es,
  es_MX: checkoutTranslationsBuilder.es_MX,
  fr: checkoutTranslationsBuilder.fr,
  fr_CA: checkoutTranslationsBuilder.fr_CA,
  nl: checkoutTranslationsBuilder.nl,
  sv: checkoutTranslationsBuilder.sv,
  de: checkoutTranslationsBuilder.de,
  de_AT: checkoutTranslationsBuilder.de_AT,
  da: checkoutTranslationsBuilder.da
};
