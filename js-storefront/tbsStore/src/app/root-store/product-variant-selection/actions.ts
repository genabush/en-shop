import { Action } from '@ngrx/store';
import { ProductVariantSelection } from '../../models/product-variant-selection';

export enum ActionTypes {
  SET_SELECTED_VARIANT = '[Product Variant Selection] Set Selected Variant',
  UNSET_SELECTED_VARIANT = '[Product Variant Unset] Unset Selected Variant'
}

// export class LoadRequestAction implements Action {
//   readonly type = ActionTypes.LOAD_REQUEST;
// }

// export class LoadFailureAction implements Action {
//   readonly type = ActionTypes.LOAD_FAILURE;
//   constructor(public payload: { error: string }) {}
// }

// export class LoadSuccessAction implements Action {
//   readonly type = ActionTypes.LOAD_SUCCESS;
//   constructor(public payload: { items: any }) {}
// }

export class SetSelectedVariantAction implements Action {
  readonly type = ActionTypes.SET_SELECTED_VARIANT;
  constructor(public payload: { selectedProductVariant: ProductVariantSelection }) {}
}
export class UnsetSelectedVariantAction implements Action {
  readonly type = ActionTypes.UNSET_SELECTED_VARIANT;
  constructor(public payload: { selectedProductVariant: any }) {}
}

export type Actions = SetSelectedVariantAction | UnsetSelectedVariantAction;
