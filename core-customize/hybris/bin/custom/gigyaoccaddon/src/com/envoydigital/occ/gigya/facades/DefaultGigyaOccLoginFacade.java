/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package com.envoydigital.occ.gigya.facades;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gigya.socialize.GSKeyNotFoundException;
import com.gigya.socialize.GSObject;
import com.gigya.socialize.GSResponse;

import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.gigya.gigyafacades.login.impl.DefaultGigyaLoginFacade;
import de.hybris.platform.gigya.gigyaservices.api.exception.GigyaApiException;
import de.hybris.platform.gigya.gigyaservices.constants.GigyaservicesConstants;
import de.hybris.platform.gigya.gigyaservices.data.GigyaUserObject;
import de.hybris.platform.gigya.gigyaservices.model.GigyaConfigModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import uk.co.thebodyshop.core.customer.strategy.CustomerEmailStrategy;

/**
 * @author vasanthramprakasam
 */
public class DefaultGigyaOccLoginFacade extends DefaultGigyaLoginFacade implements GigyaOccLoginFacade
{
	 private static final Logger LOG = LoggerFactory.getLogger(DefaultGigyaOccLoginFacade.class);

	 private CMSSiteService cmsSiteService;

	 private CustomerEmailStrategy customerEmailStrategy;

	 @Override
	 public void logOutUserFromGigya(String userId, GigyaConfigModel gigyaConfig)
	 {
	 	  try
			{
				 final UserModel user = getUserService().getCurrentUser();
				 if(user instanceof CustomerModel)
				 {
						final CustomerModel customer = (CustomerModel)user;
						final String gigyaUserId = customer.getGyUID();
						if (StringUtils.isNotBlank(gigyaUserId) && getGigyaLoginService().findCustomerByGigyaUid(gigyaUserId) != null)
						{
							 getGigyaLoginService().notifyGigyaOfLogout(gigyaConfig, gigyaUserId);
						}
				 }
			}
	 	  catch (UnknownIdentifierException uie)
			{
				 LOG.error("Customer with guid [{}] not found",userId);
			}
	 }

	 @Override
	 public UserModel createNewCustomer(GigyaConfigModel gigyaConfig, String uid) throws DuplicateUidException
	 {
			final GigyaUserObject gigyaUserObject = getGigyaLoginService().fetchGigyaInfo(gigyaConfig, uid);

			if (gigyaConfig == null)
			{
				 return null;
			}

			if (StringUtils.isEmpty(gigyaUserObject.getEmail()))
			{
				 throw new GigyaApiException("Gigya User does not have an email address");
			}

			if (getGigyaLoginService().findCustomerByGigyaUid(uid) != null)
			{
				 throw new DuplicateUidException("User with Gigya UID: " + uid + " already exists.");
			}

			final CustomerModel gigyaUser = getModelService().create(CustomerModel.class);
			gigyaUser.setGyIsOriginGigya(true);
			gigyaUser.setName(getCustomerNameStrategy().getName(gigyaUserObject.getFirstName(), gigyaUserObject.getLastName()));
			gigyaUser.setUid(getCustomerEmailStrategy().getEmail(gigyaUserObject.getEmail(),getCmsSiteService().getCurrentSite()));
			gigyaUser.setOriginalUid(gigyaUserObject.getEmail());
			gigyaUser.setGyUID(uid);
			gigyaUser.setGyApiKey(gigyaConfig.getGigyaApiKey());
			gigyaUser.setSessionLanguage(getCommonI18NService().getCurrentLanguage());
			gigyaUser.setSessionCurrency(getCommonI18NService().getCurrentCurrency());
			gigyaUser.setCustomerID(UUID.randomUUID().toString());
			getCustomerAccountService().register(gigyaUser, null);
			scheduleDataSyncFromCDCToCommerce(gigyaUser);

			return gigyaUser;
	 }

	 @Override
	 public void updateUser(final GigyaConfigModel gigyaConfig, final UserModel user) throws GSKeyNotFoundException
	 {
			// Update mandatory info i.e. UID, name and then update based on mappings
			final ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

			if (user instanceof CustomerModel)
			{
				 final CustomerModel gigyaUser = (CustomerModel) user;
				 final Map<String, Object> params = new LinkedHashMap<>();
				 params.put("UID", gigyaUser.getGyUID());
				 params.put("include", "loginIDs,emails,profile,data");

				 final GSObject gigyaParams = convertMapToGsObject(mapper, params);

				 final GSResponse gsResponse = getGigyaService().callRawGigyaApiWithConfigAndObject("accounts.getAccountInfo", gigyaParams,
						 gigyaConfig, GigyaservicesConstants.MAX_RETRIES, GigyaservicesConstants.TRY_NUM);

				 if (gsResponse.hasData() && gsResponse.getData().get("profile") != null)
				 {
						final GSObject profile = (GSObject) gsResponse.getData().get("profile");
						final String emailId = profile.getString("email");

						gigyaUser.setName(getCustomerNameStrategy().getName(profile.getString("firstName"), profile.getString("lastName")));

						// Checks if email ID is updated in the gigya profile, if yes that needs to be updated in commerce as well
						if (!StringUtils.equals(getCustomerEmailStrategy().getContactEmail(gigyaUser), emailId))
						{
							 gigyaUser.setUid(getCustomerEmailStrategy().getEmail(emailId,getCmsSiteService().getCurrentSite()));
							 getSessionService().setAttribute("emailUpdated", true);
						}
				 }
				 gigyaUser.setGyIsOriginGigya(true);

				 getModelService().save(gigyaUser);

				 // Task to synchronize data between gigya and commerce when gigya user logs in
				 scheduleDataSyncFromCDCToCommerce(gigyaUser);
			}
	 }

	 public CMSSiteService getCmsSiteService()
	 {
			return cmsSiteService;
	 }

	 public void setCmsSiteService(CMSSiteService cmsSiteService)
	 {
			this.cmsSiteService = cmsSiteService;
	 }

	 public CustomerEmailStrategy getCustomerEmailStrategy()
	 {
			return customerEmailStrategy;
	 }

	 public void setCustomerEmailStrategy(CustomerEmailStrategy customerEmailStrategy)
	 {
			this.customerEmailStrategy = customerEmailStrategy;
	 }
}
