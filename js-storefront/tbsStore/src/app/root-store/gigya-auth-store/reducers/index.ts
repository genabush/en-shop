/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

import { Action, ActionReducer, ActionReducerMap, combineReducers, MetaReducer } from '@ngrx/store';
import { AuthState, CLIENT_TOKEN_DATA, ClientToken, loaderReducer } from '@spartacus/core';
import { InjectionToken, Provider } from '@angular/core';
import * as fromUserTokenReducer from './user-token.reducer';
import { ActionTypes } from '../../../components/gigya-raas/store/actions/actions';

export function getReducers(): ActionReducerMap<AuthState> {
  return {
    userToken: combineReducers({ token: fromUserTokenReducer.reducer }),
    clientToken: loaderReducer<ClientToken>(CLIENT_TOKEN_DATA)
  };
}

export const reducerToken: InjectionToken<ActionReducerMap<AuthState>> = new InjectionToken<
  ActionReducerMap<AuthState>
>('AuthReducers');

export const reducerProvider: Provider = {
  provide: reducerToken,
  useFactory: getReducers
};

export function clearAuthState(reducer: ActionReducer<AuthState, Action>): ActionReducer<AuthState, Action> {
  return function(state, action) {
    if (action.type === ActionTypes.LOGOUT_USER) {
      state = {
        ...state,
        userToken: undefined
      };
    }
    return reducer(state, action);
  };
}

export const metaReducers: MetaReducer<any>[] = [clearAuthState];
