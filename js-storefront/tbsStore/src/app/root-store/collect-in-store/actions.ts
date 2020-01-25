import { Action } from '@ngrx/store';
import { CollectPoint } from 'src/app/interfaces/collection-point.interface';

export enum ActionTypes {
  SET_COLLECTION_POINT = '[Collect in Store] Set Collect in Store',
  UNSET_COLLECTION_POINT = '[Collect in Store] Un Set Collect in Store',
  SET_SEARCH_RESULTS = '[Collect in Store] Set Collect in Store Search results',
  SET_SEARCH_QUERY = '[Collect in Store] Set Collect in Store Search query'
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
