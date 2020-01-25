import { Action } from '@ngrx/store';
import { Wishlists } from '../../interfaces/wishlists.interface';

export enum ActionTypes {
  SET_WISHLISTS = '[Wishlists] Set Wishlists',
  OPEN_WISHLIST_NAME_MODAL = '[Wishlists] Open Wishlists Name Modal Edit',
  CLOSE_WISHLIST_NAME_MODAL = '[Wishlists] Close Wishlists Name Modal',
  OPEN_WISHLIST_DELETE_MODAL = '[Wishlists] Open Wishlists Delete Modal',
  CLOSE_WISHLIST_DELETE_MODAL = '[Wishlists] Close Wishlists Delete Modal',
  UPDATE_WISHLIST_NAME = '[Wishlists] Update New Wishlist Name',
  ADD_FROM_CART = '[Wishlists] Add Products From Cart',
  RESET_ADD_FROM_CART = '[Wishlists] Reset Add Products From Cart',
  OPEN_ADD_FROM_CART_MODAL = '[Wishlists] Open Cart to Wishlist Modal',
  CLOSE_ADD_FROM_CART_MODAL = '[Wishlists] Close Cart to Wishlist Modal'
}

export class SetWishlistsAction implements Action {
  readonly type = ActionTypes.SET_WISHLISTS;
  constructor(public payload: { wishlists: Wishlists }) {}
}

export class UpdateWishlistName implements Action {
  readonly type = ActionTypes.UPDATE_WISHLIST_NAME;
  constructor(public payload: { newWishlistName: string }) {}
}

export class OpenWishlistNameModalAction implements Action {
  readonly type = ActionTypes.OPEN_WISHLIST_NAME_MODAL;
  constructor(
    public payload: {
      nameModal: { isModalOpen: boolean; isModalNameEdit: boolean };
    }
  ) {}
}

export class CloseWishlistNameModalAction implements Action {
  readonly type = ActionTypes.CLOSE_WISHLIST_NAME_MODAL;
  constructor(public payload: { nameModal: { isModalOpen: boolean } }) {}
}

export class AddFromCartAction implements Action {
  readonly type = ActionTypes.ADD_FROM_CART;
  constructor() {}
}

export class ResetAddFromCartAction implements Action {
  readonly type = ActionTypes.RESET_ADD_FROM_CART;
  constructor() {}
}

export class OpenWishlistDeleteModalAction implements Action {
  readonly type = ActionTypes.OPEN_WISHLIST_DELETE_MODAL;
  constructor(public payload: { deleteModal: { isModalOpen: boolean; wishlistId: string } }) {}
}

export class CloseWishlistDeleteModalAction implements Action {
  readonly type = ActionTypes.CLOSE_WISHLIST_DELETE_MODAL;
  constructor(public payload: { deleteModal: { isModalOpen: boolean } }) {}
}
export class OpenAddFromCartModalAction implements Action {
  readonly type = ActionTypes.OPEN_ADD_FROM_CART_MODAL;
  constructor() {}
}

export class CloseAddFromCartModalAction implements Action {
  readonly type = ActionTypes.CLOSE_ADD_FROM_CART_MODAL;
  constructor() {}
}

export type Actions =
  | SetWishlistsAction
  | OpenWishlistNameModalAction
  | CloseWishlistNameModalAction
  | UpdateWishlistName
  | AddFromCartAction
  | OpenWishlistDeleteModalAction
  | CloseWishlistDeleteModalAction
  | OpenAddFromCartModalAction
  | CloseAddFromCartModalAction
  | ResetAddFromCartAction;
