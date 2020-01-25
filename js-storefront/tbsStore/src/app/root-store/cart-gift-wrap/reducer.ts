import { Actions, ActionTypes } from './actions';
import { initialState } from './state';

export function featureReducer(state = initialState, action: Actions) {
  switch (action.type) {
    case ActionTypes.SET_GIFT_WRAP_MESSAGE: {
      return {
        ...state,
        giftMessage: action.payload.giftMessage.giftMessage,
        giftMessageSenderName: action.payload.giftMessage.giftMessageSenderName,
        giftMessageName: action.payload.giftMessage.giftMessageName
      };
    }
    case ActionTypes.UNSET_GIFT_WRAP_MESSAGE: {
      return {
        ...state,
        giftMessage: undefined,
        giftMessageSenderName: undefined,
        giftMessageName: undefined
      };
    }
    case ActionTypes.SET_GIFT_WRAP_SERVICE: {
      return {
        ...state,
        giftWrapApplied: true,
        giftWrapServiceImage: action.payload.giftWrap.giftWrapServiceImage,
        giftWrapServiceMessage: action.payload.giftWrap.giftWrapServiceMessage
      };
    }
    case ActionTypes.UNSET_GIFT_WRAP_SERVICE: {
      return {
        ...state,
        giftWrapApplied: false,
        giftWrapServiceImage: action.payload.giftWrap.giftWrapServiceImage,
        giftWrapServiceMessage: action.payload.giftWrap.giftWrapServiceMessage
      };
    }

    default: {
      return state;
    }
  }
}
