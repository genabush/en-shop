import { Action } from '@ngrx/store';

export enum ActionTypes {
  SET_SEARCH_RESULTS = '[Store Finder] Set Search results',
  SET_SEARCH_QUERY = '[Store Finder] Set Search query',
  SET_SEARCH_LOCATION = '[Store Finder] Set Search location'
}

export class SetSearchedResultsAction implements Action {
  readonly type = ActionTypes.SET_SEARCH_RESULTS;
  constructor(public payload: { searchedResults }) {}
}
export class SetSearchedQueryAction implements Action {
  readonly type = ActionTypes.SET_SEARCH_QUERY;
  constructor(public payload: { searchedQuery }) {}
}
export class SetSearchedLocationAction implements Action {
  readonly type = ActionTypes.SET_SEARCH_LOCATION;
  constructor(public payload: { location }) {}
}

export type Actions = SetSearchedResultsAction | SetSearchedQueryAction | SetSearchedLocationAction;
