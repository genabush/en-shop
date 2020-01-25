export interface PaymentMethodsResponse {
  adyenOriginKey: string;
  giftCardPayment: boolean;
  loyaltyPayment: boolean;
  creditCardPayment: boolean;
  paymentMethods: PaymentMethod[];
  savedPayments?: SavedPayments;
}

interface PaymentMethod {
  details: Detail[];
  name: string;
  type: string;
}

interface Detail {
  key: string;
  type: string;
  optional?: boolean;
  items?: Item[];
}

interface Item {
  id: string;
  name: string;
}

export interface AddGiftCardResponse {
  giftCardBalance: number;
  giftCardNumber: string;
  success: boolean;
  giftCardAppliedAmount?: any;
  errorMessage?: string;
}

export interface SavedPayments {
  payments?: SavedPaymentsItem[];
}

export interface SavedPaymentsItem {
  accountHolderName: string;
  adyenPaymentMethod: string;
  adyenSelectedReference: string;
  cardNumber: string;
  cardPaymentRequired: boolean;
  expiryMonth: string;
  expiryYear: string;
  sameAsShippingAddress: boolean;
  subscriptionId: string;
  encryptedSecurityCode?: string;
}

export interface PaymentMode {
  code: string;
  name: string;
  description: string;
}

export interface PaymentModeList {
  paymentModes: PaymentMode[];
}
