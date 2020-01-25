/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.order.converters.populator;

import org.apache.commons.lang.StringUtils;

import de.hybris.platform.commercefacades.storelocator.converters.populator.PointOfServicePopulator;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Hemchand
 */
public class TbsPointOfServicePopulator extends PointOfServicePopulator
{
	private final BaseStoreService baseStoreService;

	public TbsPointOfServicePopulator(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	@Override
	public void populate(final PointOfServiceModel source, final PointOfServiceData target)
	{
		super.populate(source, target);
		final BaseStoreModel baseStore = getBaseStoreService().getCurrentBaseStore();
		target.setImageUrl(source.getImageUrl()!=null ? source.getImageUrl() : (baseStore!=null ? baseStore.getDefaultStoreImageUrl() : StringUtils.EMPTY));
		target.setAmplienceSlotId(source.getAmplienceSlotId()!=null ? source.getAmplienceSlotId() : (baseStore!=null ? baseStore.getDefaultStoreAmplienceSlotId() : StringUtils.EMPTY));
		if (source.getTemporaryClosedFromDate() != null && source.getTemporaryClosedToDate() != null)
		{
			target.setTemporaryClosedFromDate(getFormattedDate(source.getTemporaryClosedFromDate()));
			target.setTemporaryClosedToDate(getFormattedDate(source.getTemporaryClosedToDate()));
		}
	}

	private String getFormattedDate(final Date temporarycloseddate)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("EEEE dd MMMM yyyy");
		return formatter.format(temporarycloseddate);
	}
	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}
}
