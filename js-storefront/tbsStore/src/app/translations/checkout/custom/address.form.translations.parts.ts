import { en_addressFormOverwrites } from '../overwrites/checkout.en.translations.overwrites';

export const mobileDisclaimers = {
  en_GB:
    'Please use format +44 987 654 3210. Adding your mobile number makes it easier for us to contact you about the delivery of your parcel.',
  en_US:
    'Please use format (555) 555-5555. Adding your mobile number makes it easier for us to contact you about the delivery of your parcel.',
  en_CA:
    'Please use format (555) 555-5555. Adding your mobile number makes it easier for us to contact you about the delivery of your parcel.',
  en_AU:
    'Please use format (09) 8765 4321. Adding your mobile number makes it easier for us to contact you about the delivery of your parcel.',
  en_HK:
    'Please use format +852 9876 5432. Adding your mobile number makes it easier for us to contact you about the delivery of your parcel.',
  en_SG:
    'Please use format +65 9876 5432. Adding your mobile number makes it easier for us to contact you about the delivery of your parcel.',
  es:
    'Please use format +34 987 654 321. Adding your mobile number makes it easier for us to contact you about the delivery of your parcel.',
  es_MX:
    'Please use format +52 9876543210987. Adding your mobile number makes it easier for us to contact you about the delivery of your parcel.',
  fr:
    'Please use format +590 590 98 76 54. Adding your mobile number makes it easier for us to contact you about the delivery of your parcel.',
  fr_CA:
    'Please use format (555) 555-5555. Adding your mobile number makes it easier for us to contact you about the delivery of your parcel.',
  nl:
    'Please use format +31 987654321. Adding your mobile number makes it easier for us to contact you about the delivery of your parcel.',
  sv:
    'Please use format +46 987654321. Adding your mobile number makes it easier for us to contact you about the delivery of your parcel.',
  da:
    'Please use format +45 98-76-54-32. Adding your mobile number makes it easier for us to contact you about the delivery of your parcel.',
  de:
    'Please use format +49 9876543210. Adding your mobile number makes it easier for us to contact you about the delivery of your parcel.',
  de_AT:
    'Please use format +43 9876 5432. Adding your mobile number makes it easier for us to contact you about the delivery of your parcel.'
};

export const en_Address = {
  ...en_addressFormOverwrites,
  companyName: { label: 'Company Name', placeholder: 'Company Name' },
  firstName: { label: 'First Name', placeholder: 'First Name' },
  lastName: { label: 'Last Name', placeholder: 'Last Name' },
  address1: 'Address Line 1',
  address2: 'Address Line 2',
  city: { label: 'Town/City', placeholder: 'Town/City' },
  zipCode: { label: 'Post code', placeholder: 'Post code' },
  country: 'Country'
};

export const fr_Address = {
  ...en_addressFormOverwrites, // TODO replace when available
  companyName: { label: 'Company Name', placeholder: 'Company Name' },
  firstName: { label: 'Prénom', placeholder: 'Prénom' },
  address1: 'Adresse ligne 1',
  address2: 'Adresse ligne 2',
  city: { label: 'Ville', placeholder: 'Ville' },
  state: { label: '', placeholder: '' },
  country: 'Pays'
};

export const es_Address = {
  ...en_addressFormOverwrites, // TODO replace when available
  firstName: { label: 'Nombre', placeholder: 'Nombre' },
  lastName: { label: 'Apellidos', placeholder: 'Apellidos' },
  address1: 'Línea de dirección 1',
  address2: 'Línea de dirección 2',
  state: { label: '', placeholder: '' },
  country: 'País'
};

export const de_Address = {
  ...en_addressFormOverwrites, // TODO replace when available
  companyName: { label: 'Firma', placeholder: 'Firma' },
  firstName: { label: 'Vorname', placeholder: 'Vorname' },
  lastName: { label: 'Nachname', placeholder: 'Nachname' },
  phoneNumber: { label: 'Mobilnummer', placeholder: '+49 9876543210', disclaimer: mobileDisclaimers.en_GB },
  address1: 'Adresszeile 1',
  address2: 'Adresszeile 2',
  city: { label: 'Ort', placeholder: 'Ort' },
  state: { label: '', placeholder: '' },
  zipCode: { label: 'Postleitzahl', placeholder: 'Postleitzahl' },
  country: 'Land'
};
export const nl_Address = {
  ...en_addressFormOverwrites, // TODO replace when available
  companyName: { label: 'Bedrijfsnaam', placeholder: 'Bedrijfsnaam' },
  firstName: { label: 'Voornaam', placeholder: 'Voornaam' },
  lastName: { label: 'Achternaam', placeholder: 'Achternaam' },
  phoneNumber: { label: 'Mobiel nr', placeholder: '+31 987654321', disclaimer: mobileDisclaimers.nl }, // TODO update
  address1: 'Adresregel 1',
  address2: 'Adresregel 2',
  city: { label: 'Stad / Woonplaats', placeholder: 'Stad / Woonplaats' },
  state: { label: '', placeholder: '' },
  zipCode: { label: 'Postcode', placeholder: 'Postcode' },
  country: 'Land',
  invalidAddressPoBox: 'We cannot ship to P.O. Boxes'
};

export const da_Address = {
  ...en_addressFormOverwrites, // TODO replace when available
  companyName: { label: 'Firmanavn', placeholder: 'Firmanavn' },
  firstName: { label: 'Fornavn', placeholder: 'Fornavn' },
  lastName: { label: 'Efternavn', placeholder: 'Efternavn' },
  phoneNumber: { label: 'Mobilnummer', placeholder: '+45 98-76-54-32', disclaimer: mobileDisclaimers.da }, // TODO update
  address1: 'Adresselinje 1',
  address2: 'Adresselinje 2',
  city: { label: 'By', placeholder: 'By' },
  zipCode: { label: 'Postnummer', placeholder: 'Postnummer' },
  country: 'Land'
};

export const sv_Address = {
  ...en_addressFormOverwrites, // TODO replace when available
  companyName: { label: 'Företag', placeholder: 'Företag' },
  firstName: { label: 'Förnamn', placeholder: 'Förnamn' },
  lastName: { label: 'Efternamn', placeholder: 'Efternamn' },
  phoneNumber: { label: 'Mobilnummer', placeholder: '+46 987654321', disclaimer: mobileDisclaimers.sv }, // TODO update
  address1: 'Adressrad 1',
  address2: 'Adressrad 2',
  city: { label: 'Stad/ort', placeholder: 'Stad/ort' },
  zipCode: { label: 'Postnummer', placeholder: 'Postnummer' },
  country: 'Land'
};
