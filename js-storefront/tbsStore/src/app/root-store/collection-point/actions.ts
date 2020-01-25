import { Action } from '@ngrx/store';
import { CollectPoint } from 'src/app/interfaces/collection-point.interface';

export enum ActionTypes {
  SET_COLLECTION_POINT = '[Collection Point] Set Collection Point',
  UNSET_COLLECTION_POINT = '[Collection Point] Un Set Collection Point',
  SET_SEARCH_RESULTS = '[Collection Point] Set Collection Point Search results',
  SET_SEARCH_QUERY = '[Collection Point] Set Collection Point Search query'
}

export class SetCollectionPointAction implements Action {
  readonly type = ActionTypes.SET_COLLECTION_POINT;
  constructor(public payload: { collectionPoint: CollectPoint }) {}
}
export class UnSetCollectionPointAction implements Action {
  readonly type = ActionTypes.UNSET_COLLECTION_POINT;
  constructor(public payload: { collectionPoint: CollectPoint }) {}
}
export class SetSearchResultsAction implements Action {
  readonly type = ActionTypes.SET_SEARCH_RESULTS;
  constructor(public payload: { searchResults }) {}
}
export class SetSearchQueryAction implements Action {
  readonly type = ActionTypes.SET_SEARCH_QUERY;
  constructor(public payload: { searchQuery }) {}
}

export type Actions =
  | SetCollectionPointAction
  | UnSetCollectionPointAction
  | SetSearchResultsAction
  | SetSearchQueryAction;
