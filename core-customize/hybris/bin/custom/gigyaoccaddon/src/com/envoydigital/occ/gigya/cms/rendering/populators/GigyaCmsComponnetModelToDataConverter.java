/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package com.envoydigital.occ.gigya.cms.rendering.populators;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.envoydigital.occ.gigya.data.GigyaRaasData;
import com.envoydigital.occ.gigya.gigyaoccaddon.model.GigyaRaasComponentModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.hybris.platform.cms2.common.functions.Converter;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

/**
 * @author vasanthramprakasam
 */
public class GigyaCmsComponnetModelToDataConverter implements Converter<GigyaRaasComponentModel, GigyaRaasData>
{

	 private static final Logger LOG = LoggerFactory.getLogger(GigyaCmsComponnetModelToDataConverter.class);

	 private final UserService userService;

	 public GigyaCmsComponnetModelToDataConverter(UserService userService)
	 {
			this.userService = userService;
	 }

	 @Override
	 public GigyaRaasData convert(GigyaRaasComponentModel component)
	 {
			GigyaRaasData gigyaRaasData = new GigyaRaasData();
			final HashMap<String, Object> raasConfig = new HashMap<>();
			raasConfig.put("screenSet", component.getScreenSet());
			raasConfig.put("startScreen", component.getStartScreen());
			raasConfig.put("profileEdit", component.getProfileEdit());
			raasConfig.put("sessionExpiration", Config.getInt("default.session.timeout", 3600));

			final ObjectMapper mapper = new ObjectMapper();

			if (component.getEmbed() == Boolean.TRUE)
			{
				 raasConfig.put("containerID", component.getContainerID());
			}

			if (StringUtils.isNotBlank(component.getAdvancedConfiguration()))
			{
				 Map<String, Object> advConfig = new HashMap<>();
				 try
				 {
						advConfig = mapper.readValue(component.getAdvancedConfiguration(), HashMap.class);
						raasConfig.putAll(advConfig);
				 }
				 catch (final IOException e)
				 {
						LOG.error("Exception in converting json string to map" + e);
				 }
			}

			final boolean isAnonymousUser = userService.isAnonymousUser(userService.getCurrentUser());
			try
			{
				 gigyaRaasData.setId(component.getUid().replaceAll("[^A-Za-z0-9]", ""));
				 gigyaRaasData.setRaasConfig( mapper.writeValueAsString(raasConfig));

				 final Boolean show;
				 if (isAnonymousUser)
				 {
						show = component.getShowAnonymous();
				 }
				 else
				 {
						show = component.getShowLoggedIn();
				 }
				 gigyaRaasData.setShow(show);
				 gigyaRaasData.setProfileEdit(component.getProfileEdit());
			}
			catch (final IOException e)
			{
				 LOG.error("Exception in converting map to json string" + e);
			}
			gigyaRaasData.setAuthenticated(!isAnonymousUser);
			return gigyaRaasData;
	 }
}
