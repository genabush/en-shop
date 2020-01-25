export interface ProductVariantSelection {
  selected?: ProductVariant;
}

export interface ProductVariant {
  code: string;
  colour: string;
  name?: string;
  priceData?: PriceData;
  size?: string;
  stock?: Stock;
  url?: string;
  variantType?: string;
  images?: object;
}

export interface PriceData {
  currencyIso: string;
  formattedValue: string;
  pricePerMetric: string;
  priceType: string;
  value: number;
}

export interface Stock {
  stockLevel: number;
  stockLevelStatus: string;
}
