import { ICustomAddress } from './custom-checkout.interface';

export interface CollectPoint {
  serviceType?: string;
  distance?: string;
  distanceUnit?: string;
  isActive?: boolean;
  displayName?: string;
  available?: boolean;
  stockInfo?: any;
  name?: string;
  geoPoint?: {
    longitude: number;
    latitude: number;
  };
  address?: {
    companyName?: string;
    line1: string;
    line2: string;
    town: string;
    postalCode: string;
    phone: string;
  };
  openingHours?: {
    weekDayOpeningList?: {
      weekDay: string;
      openingTime?: {
        formattedHour: string;
      };
      closingTime?: {
        formattedHour: string;
      };
    };
  };
}

export interface CollectorDetails {
  companyName: string;
  line1: string;
  line2: string;
  town: string;
  postalCode: string;
  cellphone: string;
  firstName: string;
  lastName: string;
}
export interface CollectPointSelection {
  selected?: {};
  searchResults: [];
}

export interface IShoppingTime {
  formattedHour: string;
  hour: number;
  minute: number;
}

export interface IWeekdayOpenings {
  closed: boolean;
  weekday: string;
  closingTime: IShoppingTime;
  openingTime: IShoppingTime;
}

export interface ICheckoutCollectionData {
  address?: CollectorDetails;
  available: boolean;
  distance: number;
  distanceUnit: string;
  geoPoint: {
    latitude: 51.50841;
    longitude: -0.1341;
  };
  openingHours: {
    weekDayOpeningList: IWeekdayOpenings[];
  };
  serviceType: string;
  sourceLatitude: number;
  sourceLongitude: number;
}
