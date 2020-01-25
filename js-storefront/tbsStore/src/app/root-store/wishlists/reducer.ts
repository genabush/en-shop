import { Actions, ActionTypes } from './actions';
import { initialState } from './state';

export function featureReducer(state = initialState, action: Actions) {
  switch (action.type) {
    case ActionTypes.SET_WISHLISTS: {
      return {
        ...state,
        wishlists: action.payload.wishlists
      };
    }

    case ActionTypes.OPEN_WISHLIST_NAME_MODAL: {
      return {
        ...state,
        nameModal: {
          isModalOpen: action.payload.nameModal.isModalOpen,
          isModalNameEdit: action.payload.nameModal.isModalNameEdit
        }
      };
    }

    case ActionTypes.CLOSE_WISHLIST_NAME_MODAL: {
      return {
        ...state,
        nameModal: {
          isModalOpen: false
        }
      };
    }

    case ActionTypes.UPDATE_WISHLIST_NAME: {
      return {
        ...state,
        newWishlistName: action.payload.newWishlistName
      };
    }

    case ActionTypes.ADD_FROM_CART: {
      return {
        ...state,
        addFromCart: true
      };
    }

    case ActionTypes.RESET_ADD_FROM_CART: {
      return {
        ...state,
        addFromCart: false
      };
    }

    case ActionTypes.OPEN_WISHLIST_DELETE_MODAL: {
      return {
        ...state,
        deleteModal: {
          wishlistId: action.payload.deleteModal.wishlistId,
          isModalOpen: true
        }
      };
    }

    case ActionTypes.OPEN_ADD_FROM_CART_MODAL: {
      return {
        ...state,
        cartToWishlistModal: {
          isOpen: true
        }
      };
    }

    case ActionTypes.CLOSE_WISHLIST_DELETE_MODAL: {
      return {
        ...state,
        deleteModal: {
          isModalOpen: false
        }
      };
    }

    case ActionTypes.CLOSE_ADD_FROM_CART_MODAL: {
      return {
        ...state,
        cartToWishlistModal: {
          isOpen: false
        }
      };
    }

    default: {
      return state;
    }
  }
}
