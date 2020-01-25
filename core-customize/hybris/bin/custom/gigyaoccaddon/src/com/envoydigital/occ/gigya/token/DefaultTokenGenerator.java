/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package com.envoydigital.occ.gigya.token;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.gigya.gigyaservices.login.GigyaLoginService;

/**
 * @author vasanthramprakasam
 */
public class DefaultTokenGenerator implements TokenGenerator
{
	 private final ClientDetailsService clientDetailsService;

	 private final OAuth2RequestFactory oAuth2RequestFactory;

	 private final UserDetailsService userDetailsService;

	 private final AuthorizationServerTokenServices tokenService;

	 private final GigyaLoginService gigyaLoginService;

	 public DefaultTokenGenerator(ClientDetailsService clientDetailsService, OAuth2RequestFactory oAuth2RequestFactory, UserDetailsService userDetailsService, AuthorizationServerTokenServices tokenService, GigyaLoginService gigyaLoginService)
	 {
			this.clientDetailsService = clientDetailsService;
			this.oAuth2RequestFactory = oAuth2RequestFactory;
			this.userDetailsService = userDetailsService;
			this.tokenService = tokenService;
			this.gigyaLoginService = gigyaLoginService;
	 }

	 @Override
	 public Optional<OAuth2AccessToken> generateOauthTokenForGigyaUser(String gigyaUID)
	 {
			final UserModel gigyaUser = getGigyaLoginService().findCustomerByGigyaUid(gigyaUID);
			final String clientId = getClientId();
			final ClientDetails authenticatedClient = getClientDetailsService().loadClientByClientId(clientId);
			final Map<String,String> parameters = new HashMap<>();
			parameters.put("username",gigyaUser.getUid());
			parameters.put("password",null);
			parameters.put("grant_type","password");
			parameters.put("client_id",clientId);
			parameters.put("client_secret",authenticatedClient.getClientSecret());
			final TokenRequest tokenRequest = getoAuth2RequestFactory().createTokenRequest(parameters, authenticatedClient);
			final OAuth2Request storedOAuth2Request = getoAuth2RequestFactory().createOAuth2Request(authenticatedClient, tokenRequest);
			final UserDetails loadedUser = getUserDetailsService().loadUserByUsername(gigyaUser.getUid());
			final UsernamePasswordAuthenticationToken userAuth = new UsernamePasswordAuthenticationToken(loadedUser.getUsername(), "", loadedUser.getAuthorities());
			final OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(storedOAuth2Request, userAuth);
			return Optional.ofNullable(getTokenService().createAccessToken(oAuth2Authentication));
	 }

	 protected String getClientId()
	 {
			Authentication client = SecurityContextHolder.getContext().getAuthentication();
			if (!client.isAuthenticated()) {
				 throw new InsufficientAuthenticationException("The client is not authenticated.");
			} else {
				 String clientId = client.getName();
				 if (client instanceof OAuth2Authentication) {
						clientId = ((OAuth2Authentication)client).getOAuth2Request().getClientId();
				 }

				 return clientId;
			}
	 }

	 protected ClientDetailsService getClientDetailsService()
	 {
			return clientDetailsService;
	 }

	 protected OAuth2RequestFactory getoAuth2RequestFactory()
	 {
			return oAuth2RequestFactory;
	 }

	 protected UserDetailsService getUserDetailsService()
	 {
			return userDetailsService;
	 }

	 protected AuthorizationServerTokenServices getTokenService()
	 {
			return tokenService;
	 }

	 protected GigyaLoginService getGigyaLoginService()
	 {
			return gigyaLoginService;
	 }
}
