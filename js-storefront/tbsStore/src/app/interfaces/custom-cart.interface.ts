import { Cart, Voucher } from '@spartacus/core';
import { CollectPoint } from './collection-point.interface';

export interface CustomCart extends Cart {
  type?: string;
  giftCards: GiftCard[];
  totalGiftCardsValue: AppliedAmount;
  hasOutstandingAmount: boolean;
  outstandingAmount: AppliedAmount;
  collectionPoint?: CollectPoint;
  collectInStore?: CollectPoint;
  eligibleForCollectInStore: boolean;
  eligibleForCollectionPoint: boolean;
  eligibleForGiftWrap: boolean;
  eligibleForGiftMessage: boolean;
  deliveryPointOfService?: CollectPoint;
  eligibleForLoyalty?: boolean;
  loyaltyPoints: number;
  giftWrapPrice: AppliedAmount;
  giftMessage?: string;
  giftMessageName?: string;
  giftMessageSenderName?: string;
  giftWrapApplied: boolean;
  appliedVouchers: Voucher[];
  totalPriceWithOutDeliveryCost?: AppliedAmount;
}

export interface CustomGiftCMSBannerComponentData {
  name: string;
  giftImage: string;
  giftWrapServiceImage: string;
  giftWrapServiceMessage: string;
}

export interface GiftCard {
  applied: boolean;
  appliedAmount: AppliedAmount;
  giftCardNumber: string;
  initialBalance: AppliedAmount;
  remainingBalance: AppliedAmount;
}

export interface IFullfillmentEligibilityState {
  eligibleForCollectInStore: boolean;
  eligibleForCollectionPoint: boolean;
}

interface AppliedAmount {
  currencyIso: string;
  formattedValue: string;
  priceType: string;
  value: number;
}

export interface IGiftWrapMessage {
  giftMessageName?: string;
  giftMessageSenderName?: string;
  giftMessage?: string;
  giftWrapApplied?: boolean;
  giftWrapServiceImage?: string;
  giftWrapServiceMessage?: string;
}

// https://www.regextester.com/106421
export const EMOJI_RANGE_EXP = new RegExp(
  '(\u00a9|\u00ae|[\u2000-\u3300]|\ud83c[\ud000-\udfff]|\ud83d[\ud000-\udfff]|\ud83e[\ud000-\udfff])',
  'g'
);

/* prettier-ignore */
export const TRAILING_SPACES_EXP = new RegExp('\\s* \\ ', 'g');
/* prettier-ignore-end */

export interface IVoucherError {
  message: string;
  type: string;
}

export interface ICartVoucherState {
  appliedVouchers?: Voucher[];
}
