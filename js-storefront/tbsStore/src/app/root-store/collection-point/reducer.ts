import { Actions, ActionTypes } from './actions';
import { initialState } from './state';

export function featureReducer(state = initialState, action: Actions) {
  switch (action.type) {
    case ActionTypes.SET_COLLECTION_POINT: {
      return {
        ...state,
        selected: action.payload.collectionPoint
      };
    }
    case ActionTypes.UNSET_COLLECTION_POINT: {
      return {
        ...state,
        selected: action.payload.collectionPoint
      };
    }
    case ActionTypes.SET_SEARCH_RESULTS: {
      return {
        ...state,
        searchResults: action.payload.searchResults
      };
    }
    case ActionTypes.SET_SEARCH_QUERY: {
      return {
        ...state,
        searchQuery: action.payload.searchQuery
      };
    }

    default: {
      return state;
    }
  }
}
