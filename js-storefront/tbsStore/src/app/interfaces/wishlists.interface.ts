import { ICustomProduct } from './custom-product-item.interface';

export interface Wishlists {
  wishlists?: Wishlist[];
  addFromCart: boolean;
  cartToWishlistModal: CartToWishlistModal;
}

export interface Wishlist {
  wishlistId?: string;
  wishlistName?: string;
  created?: string;
  products?: ICustomProduct[];
}

export interface WishlistNameModal {
  isModalOpen: boolean;
  isModalNameEdit: boolean;
  editWishlistName?: string | null;
  editWishlistId?: string | null;
}

export interface WishlistDeleteModal {
  isModalOpen: boolean;
  wishlistId: string | null;
}

export interface CartToWishlistModal {
  isOpen: boolean;
}
