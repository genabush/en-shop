import { Action } from '@ngrx/store';
import { IGiftWrapMessage } from 'src/app/interfaces/custom-cart.interface';

export enum ActionTypes {
  SET_GIFT_WRAP_MESSAGE = '[Gift Wrap Message Store] Set Gift Wrap Message',
  UNSET_GIFT_WRAP_MESSAGE = '[Gift Wrap Message Store] Un Set Gift Wrap Message',
  SET_GIFT_WRAP_SERVICE = '[Gift Wrap Service Store] Set Gift Wrap Service',
  UNSET_GIFT_WRAP_SERVICE = '[Gift Wrap Service Store] Un Set Gift Wrap Service'
}

export class SetGiftWrapMessageAction implements Action {
  readonly type = ActionTypes.SET_GIFT_WRAP_MESSAGE;
  constructor(public payload: { giftMessage: IGiftWrapMessage }) {}
}
export class UnSetGiftWrapMessageAction implements Action {
  readonly type = ActionTypes.UNSET_GIFT_WRAP_MESSAGE;
  constructor(public payload: {}) {}
}
export class SetGiftWrapServiceAction implements Action {
  readonly type = ActionTypes.SET_GIFT_WRAP_SERVICE;
  constructor(public payload: { giftWrap: IGiftWrapMessage }) {}
}
export class UnSetGiftWrapServiceAction implements Action {
  readonly type = ActionTypes.UNSET_GIFT_WRAP_SERVICE;
  constructor(public payload: { giftWrap: IGiftWrapMessage }) {}
}

export type Actions =
  | SetGiftWrapMessageAction
  | UnSetGiftWrapMessageAction
  | SetGiftWrapServiceAction
  | UnSetGiftWrapServiceAction;
