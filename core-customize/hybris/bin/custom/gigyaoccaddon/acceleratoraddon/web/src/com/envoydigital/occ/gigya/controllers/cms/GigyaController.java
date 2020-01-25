/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package com.envoydigital.occ.gigya.controllers.cms;

import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.lang.BooleanUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.envoydigital.occ.gigya.data.GigyaLoginResponseWsDTO;
import com.envoydigital.occ.gigya.facades.GigyaOccLoginFacade;
import com.envoydigital.occ.gigya.token.TokenGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.gigya.gigyafacades.login.GigyaLoginFacade;
import de.hybris.platform.gigya.gigyaservices.data.GigyaJsOnLoginInfo;
import de.hybris.platform.gigya.gigyaservices.login.GigyaLoginService;
import de.hybris.platform.servicelayer.session.SessionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author vasanthramprakasam
 */
@Controller
@ApiVersion("v2")
@Api(tags = "Gigya")
@RequestMapping(value = "/{baseSiteId}/gigya-raas")
public class GigyaController
{

	 private static final Logger LOG = Logger.getLogger(GigyaController.class);

	 @Resource(name = "cmsSiteService")
	 private CMSSiteService cmsSiteService;

	 @Resource
	 private GigyaOccLoginFacade gigyaLoginFacade;

	 @Resource
	 private TokenGenerator tokenGenerator;

	 @Resource
	 private SessionService sessionService;

	 @Secured({"ROLE_CLIENT","ROLE_TRUSTED_CLIENT"})
	 @ResponseBody
	 @RequestMapping(value = "/login", method = RequestMethod.POST)
	 @ApiOperation(value = "Post Gigya login", notes = "Post login with gigya data", nickname = "postGigyaLoginData")
	 public GigyaLoginResponseWsDTO raasLogin(@RequestBody final GigyaJsOnLoginInfo jsInfo)
	 {
			final GigyaLoginResponseWsDTO gigyaResponse = new GigyaLoginResponseWsDTO();

			final CMSSiteModel cmsSite = cmsSiteService.getCurrentSite();
			final boolean userProcessed = gigyaLoginFacade.processGigyaLogin(jsInfo, cmsSite.getGigyaConfig());

			if (userProcessed)
			{
				 gigyaResponse.setCode(0);
				 gigyaResponse.setSuccess(true);
				 gigyaResponse.setMessage("User successfully logged in.");
				 generateTokenAndSetOnResponse(jsInfo, gigyaResponse);
				 return gigyaResponse;
			}
			else
			{
				 gigyaResponse.setCode(500);
				 gigyaResponse.setSuccess(false);
				 gigyaResponse.setMessage("Error logging in user. Please contact support.");
				 return gigyaResponse;
			}
	 }

	 @Secured({ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	 @ResponseBody
	 @RequestMapping(value = "/profile", method = RequestMethod.POST)
	 public GigyaLoginResponseWsDTO updateRaasProfile(@RequestBody final GigyaJsOnLoginInfo jsInfo)
	 {
			final GigyaLoginResponseWsDTO gigyaResponse = new GigyaLoginResponseWsDTO();
			final CMSSiteModel cmsSite = cmsSiteService.getCurrentSite();
			final boolean profileProcessed = gigyaLoginFacade.processGigyaProfileUpdate(jsInfo, cmsSite.getGigyaConfig());
			if (profileProcessed)
			{
				 if (BooleanUtils.isTrue(sessionService.getAttribute("emailUpdated")))
				 {
						generateTokenAndSetOnResponse(jsInfo, gigyaResponse);
				 }
				 gigyaResponse.setCode(0);
				 gigyaResponse.setSuccess(true);
				 gigyaResponse.setMessage("User profile updated successfully.");
				 return gigyaResponse;
			}
			else
			{
				 gigyaResponse.setCode(500);
				 gigyaResponse.setSuccess(false);
				 gigyaResponse.setMessage("Error updating user profile. Please contact support.");
				 return gigyaResponse;
			}
	 }

	 @Secured({ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	 @RequestMapping(value = "/users/{userId}/logout", method = RequestMethod.POST)
	 @ResponseStatus(HttpStatus.OK)
	 public void logOutUserFromGigya(@PathVariable String userId)
	 {
			final CMSSiteModel cmsSite = cmsSiteService.getCurrentSite();
			gigyaLoginFacade.logOutUserFromGigya(userId,cmsSite.getGigyaConfig());
	 }

	 private void generateTokenAndSetOnResponse(@RequestBody GigyaJsOnLoginInfo jsInfo, GigyaLoginResponseWsDTO gigyaResponse)
	 {
			final ObjectMapper objectMapper = new ObjectMapper();
			try
			{
				 Optional<OAuth2AccessToken> token = tokenGenerator.generateOauthTokenForGigyaUser(jsInfo.getUID());
				 if (token.isPresent())
				 {
						gigyaResponse.setOauthToken(objectMapper.writeValueAsString(token.get()));
				 }
				 else
				 {
						LOG.error("Can't get oauth token");
				 }
			}
			catch (JsonProcessingException jpe)
			{
				 LOG.error("Error writing oauth token");
			}
	 }
}
