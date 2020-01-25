import { en_CartTranslationsOverwrites } from './cart/overwrites/cart.en.translations.overwrites';
import { cartTranslationsBuilder } from './cart/cart.translations.builder';

export const cartTranslations = {
  en: en_CartTranslationsOverwrites,
  en_GB: cartTranslationsBuilder.en_GB,
  en_US: cartTranslationsBuilder.en_US,
  en_CA: cartTranslationsBuilder.en_CA,
  en_AU: cartTranslationsBuilder.en_AU,
  en_SG: cartTranslationsBuilder.en_SG,
  es: cartTranslationsBuilder.es,
  es_MX: cartTranslationsBuilder.es_MX,
  fr: cartTranslationsBuilder.fr,
  fr_CA: cartTranslationsBuilder.fr_CA,
  nl: cartTranslationsBuilder.nl,
  sv: cartTranslationsBuilder.sv,
  de: cartTranslationsBuilder.de,
  de_AT: cartTranslationsBuilder.de_AT,
  da: cartTranslationsBuilder.da
};
