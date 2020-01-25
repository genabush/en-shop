import { Actions, ActionTypes } from './actions';
import { initialState } from './state';

export function featureReducer(state = initialState, action: Actions) {
  switch (action.type) {
    case ActionTypes.SET_SEARCH_RESULTS: {
      return {
        ...state,
        searchedResults: action.payload.searchedResults
      };
    }
    case ActionTypes.SET_SEARCH_QUERY: {
      return {
        ...state,
        searchedQuery: action.payload.searchedQuery
      };
    }
    case ActionTypes.SET_SEARCH_LOCATION: {
      return {
        ...state,
        searchedLocation: action.payload.location
      };
    }

    default: {
      return state;
    }
  }
}
