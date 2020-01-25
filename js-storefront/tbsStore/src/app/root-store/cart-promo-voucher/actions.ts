import { Action } from '@ngrx/store';
import { Voucher } from '@spartacus/core';

export enum ActionTypes {
  ADD_CART_PROMO_VOUCHER = '[Cart Promo Voucher Store] Add Cart Promo Voucher',
  SET_CART_PROMO_VOUCHERS = '[Cart Promo Voucher Store] Set Cart Promo Voucher',
  UNSET_CART_PROMO_VOUCHERS = '[Cart Promo Voucher Store] Un Set Cart Promo Voucher'
}

export class AddPromoVoucherAction implements Action {
  readonly type = ActionTypes.ADD_CART_PROMO_VOUCHER;
  constructor(public payload: { voucherItem: Voucher }) {}
}
export class SetPromoVoucherAction implements Action {
  readonly type = ActionTypes.SET_CART_PROMO_VOUCHERS;
  constructor(public payload: Voucher[]) {}
}
export class UnSetPromoVoucherAction implements Action {
  readonly type = ActionTypes.UNSET_CART_PROMO_VOUCHERS;
  constructor() {}
}

export type Actions = AddPromoVoucherAction | SetPromoVoucherAction | UnSetPromoVoucherAction;
