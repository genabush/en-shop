export interface PaymentDetailsResponse {
  type: string;
  appliedOrderPromotions: any[];
  appliedProductPromotions: any[];
  appliedVouchers: any[];
  calculated: boolean;
  code: string;
  deliveryAddress: DeliveryAddress;
  deliveryCost: DeliveryCost;
  deliveryItemsQuantity: number;
  deliveryMode: DeliveryMode;
  deliveryOrderGroups: DeliveryOrderGroup[];
  entries: Entry[];
  guid: string;
  net: boolean;
  orderDiscounts: DeliveryCost;
  paymentInfo: PaymentInfo;
  pickupItemsQuantity: number;
  pickupOrderGroups: any[];
  productDiscounts: DeliveryCost;
  site: string;
  store: string;
  subTotal: DeliveryCost;
  totalDiscounts: DeliveryCost;
  totalItems: number;
  totalPrice: DeliveryCost;
  totalPriceWithTax: DeliveryCost;
  totalTax: DeliveryCost;
  user: User;
  consignments: any[];
  created: string;
  guestCustomer: boolean;
  statusDisplay: string;
  unconsignedEntries: Entry[];
  paymentRedirect?: PaymentRedirect;
}

interface User {
  name: string;
  uid: string;
}

interface PaymentInfo {
  billingAddress: DeliveryAddress;
  defaultPayment: boolean;
  sameAsShippingAddress: boolean;
  saved: boolean;
}

interface DeliveryOrderGroup {
  entries: Entry[];
  totalPriceWithTax: DeliveryCost;
}

interface Entry {
  configurationInfos: any[];
  entryNumber: number;
  maxProductOrderQuantity: number;
  product: Product;
  quantity: number;
  totalPrice: TotalPrice;
}

interface TotalPrice {
  currencyIso: string;
  value: number;
}

interface Product {
  availableForPickup: boolean;
  baseProduct: string;
  breadcrumbCategories: BreadcrumbCategory[];
  categories: any[];
  code: string;
  configurable: boolean;
  isVariant: boolean;
  manufacturer: string;
  name: string;
  purchasable: boolean;
  size: string;
  stock: Stock;
  url: string;
}

interface Stock {
  stockLevel: number;
  stockLevelStatus: string;
}

interface BreadcrumbCategory {
  code: string;
  name: string;
  url: string;
}

interface DeliveryMode {
  code: string;
  deliveryCost: DeliveryCost;
  description: string;
  name: string;
}

interface DeliveryCost {
  currencyIso: string;
  formattedValue: string;
  priceType: string;
  value: number;
}

interface DeliveryAddress {
  country: Country;
  defaultAddress: boolean;
  firstName: string;
  id: string;
  lastName: string;
  line1: string;
  line2: string;
  postalCode: string;
  titleCode: string;
  town: string;
}

interface Country {
  isocode: string;
}

export interface PaymentRedirect {
  data: PaymentData;
  method: 'POST' | 'GET';
  redirectUrl: string;
}

interface PaymentData {
  entry: { key: string; value: string }[];
}
