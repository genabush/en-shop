import { en_AppTranslationsOverwrites } from './app/overwrites/app.en.translations.overwrites';
import { appTranslationsBuilder } from './app/app.translations.builder';

export const translationOverwrites = {
  en: en_AppTranslationsOverwrites,
  en_GB: appTranslationsBuilder.en_GB,
  en_US: appTranslationsBuilder.en_US, // TODO replace when available
  en_AU: appTranslationsBuilder.en_AU, // TODO replace when available
  en_CA: appTranslationsBuilder.en_CA, // TODO replace when available
  en_SG: appTranslationsBuilder.en_SG, // TODO replace when available
  es: appTranslationsBuilder.es, // TODO replace when available
  es_MX: appTranslationsBuilder.es_MX, // TODO replace when available
  fr: appTranslationsBuilder.fr, // TODO replace when available
  fr_CA: appTranslationsBuilder.fr_CA, // TODO replace when available
  nl: appTranslationsBuilder.nl, // TODO replace when available
  sv: appTranslationsBuilder.sv, // TODO replace when available
  de: appTranslationsBuilder.de, // TODO replace when available
  de_AT: appTranslationsBuilder.de_AT, // TODO replace when available
  da: appTranslationsBuilder.da // TODO replace when available
};
