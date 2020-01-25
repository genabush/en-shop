import { Actions, ActionTypes } from './actions';
import { initialState } from './state';

export function featureReducer(state = initialState, action: Actions) {
  switch (action.type) {
    case ActionTypes.ADD_CART_PROMO_VOUCHER: {
      if (state.appliedVouchers.length > 0) {
        return {
          ...state,
          appliedVouchers: [state.appliedVouchers.concat(action.payload.voucherItem)]
        };
      } else {
        const temp = {
          ...state,
          appliedVouchers: [action.payload.voucherItem]
        };
        return temp;
      }
    }

    case ActionTypes.SET_CART_PROMO_VOUCHERS: {
      return {
        ...state,
        appliedVouchers: action.payload
      };
    }

    case ActionTypes.UNSET_CART_PROMO_VOUCHERS: {
      return {
        ...state,
        appliedVouchers: []
      };
    }

    default: {
      return state;
    }
  }
}
