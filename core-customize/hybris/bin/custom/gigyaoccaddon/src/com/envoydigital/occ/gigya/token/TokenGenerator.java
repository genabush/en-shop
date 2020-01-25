/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package com.envoydigital.occ.gigya.token;

import java.util.Optional;

import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * @author vasanthramprakasam
 */
public interface TokenGenerator
{
	 Optional<OAuth2AccessToken> generateOauthTokenForGigyaUser(String gigyaUID);
}
