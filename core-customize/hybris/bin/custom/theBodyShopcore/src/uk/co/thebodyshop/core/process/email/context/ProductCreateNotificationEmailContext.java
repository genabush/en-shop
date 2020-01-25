/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.process.email.context;

import com.google.common.base.Joiner;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import uk.co.thebodyshop.core.model.ProductCreateNotificationProcessModel;

/**
 * @author prateek.goel
 */
public class ProductCreateNotificationEmailContext extends AbstractEmailContext<ProductCreateNotificationProcessModel>
{
	private static final String ENVIRONMENT_NAME = "environmentName";
	private static final String ENVIRONMENT_KEY = "tbs.environment.name";
	private static final String CATALOG_ID = "catalogId";
	private static final String SEMI_COLON = ";";
	private static final String PRODUCT_CODES = "productCodes";

	private ConfigurationService configurationService;

	@Override
	public void init(final ProductCreateNotificationProcessModel productCreateNotificationProcesModel, final EmailPageModel emailPageModel)
	{
		super.init(productCreateNotificationProcesModel, emailPageModel);
		put(EMAIL, Joiner.on(SEMI_COLON).join(productCreateNotificationProcesModel.getEmailList()));
		put(PRODUCT_CODES, productCreateNotificationProcesModel.getProductCodes());
		put(CATALOG_ID, productCreateNotificationProcesModel.getCatalogId());
		put(ENVIRONMENT_NAME, getConfigurationService().getConfiguration().getString(ENVIRONMENT_KEY));
	}

	@Override
	protected BaseSiteModel getSite(final ProductCreateNotificationProcessModel businessProcessModel)
	{
		return businessProcessModel.getSite();
	}

	@Override
	protected CustomerModel getCustomer(final ProductCreateNotificationProcessModel businessProcessModel)
	{
		return null;
	}

	@Override
	protected LanguageModel getEmailLanguage(final ProductCreateNotificationProcessModel businessProcessModel)
	{
		return businessProcessModel.getLanguage();
	}

	protected static String getCatalogId()
	{
		return CATALOG_ID;
	}

	protected static String getProductCodes()
	{
		return PRODUCT_CODES;
	}

	@Override
	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Override
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

}
