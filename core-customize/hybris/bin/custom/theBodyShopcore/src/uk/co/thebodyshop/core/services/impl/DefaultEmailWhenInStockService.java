/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services.impl;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.daos.ProductDao;
import de.hybris.platform.servicelayer.model.ModelService;

import uk.co.thebodyshop.core.daos.EmailWhenInStockDao;
import uk.co.thebodyshop.core.emailWhenInStock.ws.data.EmailWhenInStockData;
import uk.co.thebodyshop.core.emailWhenInStock.ws.data.EmailWhenInStockResultData;
import uk.co.thebodyshop.core.model.EmailWhenInStockModel;
import uk.co.thebodyshop.core.services.EmailWhenInStockService;

/**
 * @author Kirshna
 */
public class DefaultEmailWhenInStockService implements EmailWhenInStockService
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultEmailWhenInStockService.class);

	private final EmailWhenInStockDao emailWhenInStockDao;
	private final ModelService modelService;
	private final CMSSiteService cmsSiteService;
	private final ProductDao productDao;

	@Override
	public EmailWhenInStockResultData findEmailWhenInStock(final EmailWhenInStockData emailWhenInStockData)
	{
		final EmailWhenInStockResultData data = new EmailWhenInStockResultData();

		final String productCode = emailWhenInStockData.getProductCode();

		final List<ProductModel> products = getProductDao().findProductsByCode(productCode);

		if (CollectionUtils.isEmpty(products))
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Could not find product [{}] when looking up email in stock record", productCode);
			}

			data.setSuccess(Boolean.FALSE);
		}
		else
		{
			data.setSuccess(Boolean.TRUE);

			final String emailId = emailWhenInStockData.getEmailId();
			final CMSSiteModel currentSite = getCmsSiteService().getCurrentSite();
			final EmailWhenInStockModel existingRecord = getEmailWhenInStockDao().getEmailWhenInStock(emailId, productCode, currentSite);

			if (existingRecord == null)
			{
				createEmailWhenInStock(emailWhenInStockData, currentSite);
			}
		}

		return data;
	}

	@Override
	public Collection<EmailWhenInStockModel> findEmailWhenInStockRecords(final String productCode, final CMSSiteModel baseSite) {
		return getEmailWhenInStockDao().getEmailWhenInStockRecordsByProductAndSite(productCode,baseSite);
	}

	public void createEmailWhenInStock(final EmailWhenInStockData emailWhenInStockData, final CMSSiteModel cmsSiteModel)
	{
		final EmailWhenInStockModel model = getModelService().create(EmailWhenInStockModel.class);
		model.setEmail(emailWhenInStockData.getEmailId());
		model.setProductCode(emailWhenInStockData.getProductCode());
		model.setBaseSite(cmsSiteModel);
		getModelService().save(model);
	}

	public DefaultEmailWhenInStockService(final EmailWhenInStockDao emailWhenInStockDao, final ModelService modelService, final CMSSiteService cmsSiteService, final ProductDao productDao)
	{
		this.emailWhenInStockDao = emailWhenInStockDao;
		this.modelService = modelService;
		this.cmsSiteService = cmsSiteService;
		this.productDao = productDao;
	}

	protected EmailWhenInStockDao getEmailWhenInStockDao()
	{
		return emailWhenInStockDao;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected CMSSiteService getCmsSiteService()
	{
		return cmsSiteService;
	}

	protected ProductDao getProductDao()
	{
		return productDao;
	}
}
