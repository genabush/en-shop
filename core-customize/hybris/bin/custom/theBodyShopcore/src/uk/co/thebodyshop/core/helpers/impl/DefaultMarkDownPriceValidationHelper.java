/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.helpers.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.store.BaseStoreModel;

import uk.co.thebodyshop.core.helpers.MarkDownPriceValidationHelper;
import uk.co.thebodyshop.core.model.MarkDownPriceRowModel;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;
import uk.co.thebodyshop.core.price.TbsCommercePriceService;
import uk.co.thebodyshop.core.services.UserPriceGroupService;

/**
 * @author Marcin
 */
public class DefaultMarkDownPriceValidationHelper implements MarkDownPriceValidationHelper
{
	private final SessionService sessionService;

	private final CMSSiteService cmsSiteService;

	private final UserPriceGroupService userPriceGroupService;

	private final TbsCommercePriceService commercePriceService;

	private final CommonI18NService commonI18NService;

	public DefaultMarkDownPriceValidationHelper(final SessionService sessionService, final CMSSiteService cmsSiteService, final UserPriceGroupService userPriceGroupService, final TbsCommercePriceService commercePriceService,
			final CommonI18NService commonI18NService)
	{
		this.sessionService = sessionService;
		this.cmsSiteService = cmsSiteService;
		this.userPriceGroupService = userPriceGroupService;
		this.commercePriceService = commercePriceService;
		this.commonI18NService = commonI18NService;
	}

	@Override
	public boolean hasValidPrice(final MarkDownPriceRowModel priceRow)
	{
		if (!(priceRow.getPrice() > 0))
		{
			return false;
		}
		if (null != priceRow.getProduct() && priceRow.getProduct() instanceof TbsVariantProductModel && null != priceRow.getProduct().getCatalogVersion())
		{
			final Double basePrice = getCurrentBasePrice(priceRow.getProduct());
			if (null != basePrice && priceRow.getPrice() > basePrice)
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean hasValidDates(final MarkDownPriceRowModel priceRow, final boolean checkStartDate)
	{
		if (null == priceRow.getStartTime() || null == priceRow.getEndTime())
		{
			return false;
		}
		if (priceRow.getEndTime().before(priceRow.getStartTime()))
		{
			return false;
		}
		if (checkStartDate && !isStartDateUnique(priceRow))
		{
			return false;
		}
		return true;
	}

	@Override
	public boolean isStartDateUnique(final MarkDownPriceRowModel priceRow)
	{
		if (null != priceRow.getProduct() && CollectionUtils.isNotEmpty(priceRow.getProduct().getMarkDownPrices()))
		{
			final List<MarkDownPriceRowModel> sameMarkDownPriceList = priceRow.getProduct().getMarkDownPrices().stream().filter(price -> !(price.getPk().equals(priceRow.getPk())) && price.getStartTime().equals(priceRow.getStartTime()))
					.collect(Collectors.toList());
			if (CollectionUtils.isNotEmpty(sameMarkDownPriceList))
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public String getMarkDownPriceRowsErrors(final Set<MarkDownPriceRowModel> markDownPrices)
	{
		final StringBuilder markDownPriceRowsErrors = new StringBuilder();
		for (final MarkDownPriceRowModel priceRow : markDownPrices)
		{
			if (!hasValidPrice(priceRow))
			{
				addMarkDownPriceError(markDownPriceRowsErrors, "Invalid or missing price value found for priceRow with PK :: " + priceRow.getPk());
			}
			if (!hasValidDates(priceRow, false))
			{
				addMarkDownPriceError(markDownPriceRowsErrors, "Invalid or missing online/offline dates found for priceRow with PK :: " + priceRow.getPk());
			}
		}
		return markDownPriceRowsErrors.toString();
	}

	private void addMarkDownPriceError(final StringBuilder markDownPriceRowsErrors, final String errorMessage)
	{
		if (markDownPriceRowsErrors.toString().isEmpty())
		{
			markDownPriceRowsErrors.append(errorMessage);
		}
		else
		{
			markDownPriceRowsErrors.append(", " + errorMessage);
		}
	}

	@Override
	public Double getCurrentBasePrice(final TbsVariantProductModel variantModel)
	{
		final List<PriceInformation> prices = new ArrayList<>();
		getSessionService().executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public void executeWithoutResult()
			{
				final BaseStoreModel baseStore = variantModel.getCatalogVersion().getCatalog().getBaseStores().iterator().next();
				final BaseSiteModel baseSite = baseStore.getCmsSites().iterator().next();
				getCmsSiteService().setCurrentSite((CMSSiteModel) baseSite);
				getCommonI18NService().setCurrentCurrency(baseStore.getDefaultCurrency());
				getUserPriceGroupService().setUserPriceGroupForSite(baseSite);
				prices.add(getCommercePriceService().getBasePriceForProduct(variantModel));
			}
		});
		final PriceInformation info = prices.iterator().next();
		if (null != info && null != info.getValue())
		{
			return info.getValue().getValue();
		}
		return null;
	}

	/**
	 * @return the sessionService
	 */
	protected SessionService getSessionService()
	{
		return sessionService;
	}

	/**
	 * @return the cmsSiteService
	 */
	protected CMSSiteService getCmsSiteService()
	{
		return cmsSiteService;
	}

	/**
	 * @return the userPriceGroupService
	 */
	protected UserPriceGroupService getUserPriceGroupService()
	{
		return userPriceGroupService;
	}

	/**
	 * @return the commercePriceService
	 */
	protected TbsCommercePriceService getCommercePriceService()
	{
		return commercePriceService;
	}

	/**
	 * @return the commonI18NService
	 */
	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}
}
