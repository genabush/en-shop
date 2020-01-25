import { createEntityAdapter, EntityAdapter, EntityState } from '@ngrx/entity';
import {
  CartToWishlistModal,
  WishlistDeleteModal,
  WishlistNameModal,
  Wishlists
} from 'src/app/interfaces/wishlists.interface';

export const featureAdapter: EntityAdapter<Wishlists> = createEntityAdapter<Wishlists>();

export interface State extends EntityState<Wishlists> {
  wishlists?: Wishlists | [];
  newWishlistName: string | null;
  nameModal: WishlistNameModal;
  deleteModal: WishlistDeleteModal;
  addFromCart: boolean;
  cartToWishlistModal: CartToWishlistModal;
}

export const initialState: State = featureAdapter.getInitialState({
  wishlists: [],
  addFromCart: false,
  cartToWishlistModal: {
    isOpen: false
  },
  newWishlistName: null,
  nameModal: {
    isModalOpen: false,
    isModalNameEdit: false
  },
  deleteModal: {
    isModalOpen: false,
    wishlistId: null
  },
  isModalNameEdit: false,
  editWishlistName: null,
  editWishlistId: null
});
