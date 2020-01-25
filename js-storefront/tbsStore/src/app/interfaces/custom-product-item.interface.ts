import {
  Images,
  BaseOption,
  Category,
  Classification,
  FutureStock,
  Promotion,
  Price,
  PriceRange,
  ProductReferences,
  Review,
  Stock,
  VariantMatrixElement,
  VariantOptionQualifier
} from '@spartacus/core';

interface ICustomProductImageItem {
  altText: string;
  format: string;
  imageType: string; // TOOD adjust to match custom types e.g. 'PRIMARY'
  url: string;
}

export interface ICustomProductImage {
  PRIMARY: {
    zoom: ICustomProductImageItem;
    product: ICustomProductImageItem;
    thumbnail: ICustomProductImageItem;
  };
}

export interface ICustomVariantOption {
  code?: string;
  colour?: string;
  colourName?: string;
  name?: string;
  size?: string;
  technicalIngredients?: string;
  priceData?: Price | ICustomPrice;
  stock?: Stock;
  url?: string;
  variantOptionQualifiers?: VariantOptionQualifier[];
  variantType?: string;
  selectedFlag?: boolean;
  maxOrderProductQuantity?: number;
  emailMeWhenInStockToggle?: boolean;
}

export interface ICustomPrice extends Price {
  pricePerMetric?: string;
  loyaltyPoints?: number;
}

export interface IBreadCrumbCategory {
  code: string;
  name: string;
  url: string;
}

export interface ICustomProduct {
  availableForPickup?: boolean;
  averageRating?: number;
  baseOptions?: BaseOption[];
  baseProduct?: string;
  categories?: Category[];
  classifications?: Classification[];
  code?: string;
  description?: string;
  futureStocks?: FutureStock[];
  images?: Images | ICustomProductImage;
  manufacturer?: string;
  multidimensional?: boolean;
  name?: string;
  nameHtml?: string;
  numberOfReviews?: number;
  potentialPromotions?: Promotion[];
  price?: ICustomPrice;
  priceRange?: PriceRange;
  productReferences?: ProductReferences;
  purchasable?: boolean;
  reviews?: Review[];
  stock?: Stock;
  summary?: string;
  url?: string;
  variantMatrix?: VariantMatrixElement[];
  variantOptions?: ICustomVariantOption[];
  variantType?: string;
  volumePrices?: Price[] | ICustomPrice[];
  volumePricesFlag?: boolean;
  configurable?: boolean;
  isVariant?: boolean;
  breadcrumbCategories?: IBreadCrumbCategory[];
  keyIngredients?: any[];
  straplines?: any[];
  emailMeWhenInStockToggle?: boolean;
  findInStoreEnabled?: boolean;
  multiVariant?: boolean;
  variants?: string;
}

export interface IPLPScrollIndex {
  pageUrl: string;
  currentPage: any;
  scrollPos: number;
}
