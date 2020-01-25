import { Actions, ActionTypes } from './actions';
import { featureAdapter, initialState } from './state';
import { ProductVariantSelection } from 'src/app/models';

export function featureReducer(state = initialState, action: Actions): ProductVariantSelection {
  switch (action.type) {
    case ActionTypes.SET_SELECTED_VARIANT: {
      return {
        ...state,
        selected: action.payload.selectedProductVariant.selected
      };
    }
    case ActionTypes.UNSET_SELECTED_VARIANT: {
      return {
        ...state,
        selected: action.payload.selectedProductVariant.selected
      };
    }

    default: {
      return state;
    }
  }
}
