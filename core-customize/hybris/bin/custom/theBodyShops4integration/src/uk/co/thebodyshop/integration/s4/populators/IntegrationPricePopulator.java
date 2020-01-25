/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.s4.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import uk.co.thebodyshop.integration.s4.model.IntegrationS4PriceModel;

/**
 * @author prateek.goel
 */
public class IntegrationPricePopulator implements Populator<IntegrationS4PriceModel, PriceRowModel>
{
	private CommonI18NService commonI18NService;

	@Override
	public void populate(final IntegrationS4PriceModel source, final PriceRowModel target)
	{
		target.setPrice(source.getPriceValue());
		target.setCurrency(getCommonI18NService().getCurrency(source.getCurrency()));
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}
}
