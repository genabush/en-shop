/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

export interface GigyaResponse {
  eventName: string;
  remember: boolean;
  provider: string;
  loginMode: string;
  newUser: false;
  UIDSignature: string;
  signatureTimestamp: string;
  UID: string;
  profile: {
    firstName: string;
    lastName: string;
    email: string;
    photoURL: string;
    thumbnailURL: string;
  };
  data: {};
  id_token: string;
  isGlobal: boolean;
  fullEventName: string;
  source: string;
}

export interface GigyaServerResponse {
  code: number;
  success: boolean;
  message: string;
  oauthToken?: string;
}
