import { Order } from '@spartacus/core';

export interface CustomOrder extends Order {
  loyaltyPoints?: number;
}
