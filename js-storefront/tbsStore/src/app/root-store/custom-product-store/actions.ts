import { Action } from '@ngrx/store';

export enum ActionTypes {
  LOAD_REQUEST = '[Product] Load Request',
  LOAD_FAILURE = '[Product] Load Failure',
  LOAD_SUCCESS = '[Product] Load Success'
}

export class LoadRequestAction implements Action {
  readonly type = ActionTypes.LOAD_REQUEST;
}

export class LoadFailureAction implements Action {
  readonly type = ActionTypes.LOAD_FAILURE;
  constructor(public payload: { error: string }) {}
}

export class LoadSuccessAction implements Action {
  readonly type = ActionTypes.LOAD_SUCCESS;
  constructor(public payload: { items: any }) {}
}

export type Actions = LoadRequestAction | LoadFailureAction | LoadSuccessAction;
