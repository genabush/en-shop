/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

import { UserToken, AuthActions } from '@spartacus/core';
import { UpdateEmailSuccessAction } from '@spartacus/core/src/user/store/actions/update-email.action';

export const initialState: UserToken = <UserToken>{};

export function reducer(
  state = initialState,
  action: AuthActions.UserTokenAction | UpdateEmailSuccessAction
): UserToken {
  switch (action.type) {
    case AuthActions.LOAD_USER_TOKEN:
    case AuthActions.REFRESH_USER_TOKEN: {
      return {
        ...state
      };
    }

    case AuthActions.LOAD_USER_TOKEN_SUCCESS:
    case AuthActions.REFRESH_USER_TOKEN_SUCCESS: {
      return {
        ...state,
        ...action.payload
      };
    }

    case AuthActions.LOAD_USER_TOKEN_FAIL:
    case AuthActions.REFRESH_USER_TOKEN_FAIL: {
      return {
        ...state
      };
    }
  }
  return state;
}
