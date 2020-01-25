/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.adyen.service;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.thebodyshop.integration.adyen.dao.AdyenPaymentRefusalMessageDao;
import uk.co.thebodyshop.integration.adyen.model.AdyenPaymentRefusalMessageModel;

/**
 * @author Krishna
 */
public class DefaultAdyenPaymentRefusalMessageService implements AdyenPaymentRefusalMessageService {
	private static final Logger LOG = LoggerFactory.getLogger(DefaultAdyenPaymentRefusalMessageService.class);

	private static final String GLOBAL_REFUSAL_MESSAGE = "tbs.adyen.global.refusal.message";

	private final AdyenPaymentRefusalMessageDao adyenPaymentRefusalMessageDao;

	private final ConfigurationService configurationService;

	@Override
	public String getRefusalMessage(final String refusalReasonCode) {
		final String message = StringUtils.EMPTY;

		try {
			final Integer code = Integer.valueOf(refusalReasonCode);
			final List<AdyenPaymentRefusalMessageModel> refusalMessageList = getAdyenPaymentRefusalMessageDao()
					.getRefusalMessages();

			for (final AdyenPaymentRefusalMessageModel refusalMessageModel : refusalMessageList) {
				if (CollectionUtils.isNotEmpty(refusalMessageModel.getRefusalReasonCodes())) {
					if (refusalMessageModel.getRefusalReasonCodes().contains(code)) {
						return refusalMessageModel.getMessage();
					}
				}
			}
		} catch (final Exception e) {
			LOG.warn("The refusal reason code [{}] from Adyen could not be converted into an integer",
					refusalReasonCode);
		}

		return getConfigurationService().getConfiguration().getString(GLOBAL_REFUSAL_MESSAGE);
	}

	public DefaultAdyenPaymentRefusalMessageService(final AdyenPaymentRefusalMessageDao adyenPaymentRefusalMessageDao, final ConfigurationService configurationService) {
		this.adyenPaymentRefusalMessageDao = adyenPaymentRefusalMessageDao;
		this.configurationService = configurationService;
	}

	protected AdyenPaymentRefusalMessageDao getAdyenPaymentRefusalMessageDao() {
		return adyenPaymentRefusalMessageDao;
	}

	protected ConfigurationService getConfigurationService() {
		return configurationService;
	}
}
