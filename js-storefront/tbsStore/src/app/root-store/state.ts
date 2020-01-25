import { ProductVariantSelectionStoreState } from './product-variant-selection';
import { CollectionPointStoreState } from './collection-point';
import { WishlistsStoreState } from './wishlists';
import { CisStoreState } from './collect-in-store';
import { CartGiftWrapStoreState } from './cart-gift-wrap';

export interface State {
  productVariantSelection: ProductVariantSelectionStoreState.State;
  collectionPointSelection: CollectionPointStoreState.State;
  cisSelection: CisStoreState.State;
  wishlistsSelection: WishlistsStoreState.State;
  cartGiftWrap: CartGiftWrapStoreState.State;
}
