/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

import { Action } from '@ngrx/store';

export enum ActionTypes {
  LOGOUT_USER = 'Log out User'
}

export class LogOutAction implements Action {
  readonly type = ActionTypes.LOGOUT_USER;
  constructor() {}
}

export type Actions = LogOutAction;
