/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.loyalty.populators;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import uk.co.thebodyshop.loyalty.model.LoyaltyVoucherModel;
import uk.co.thebodyshop.loyalty.service.LoyaltyService;

/**
 * @author Krishna
 */
public class LoyaltyPopulator implements Populator<AbstractOrderModel, AbstractOrderData>
{
	private final BaseStoreService baseStoreService;

	private final LoyaltyService loyaltyService;
	
	private final PriceDataFactory priceDataFactory;
	
	private final UserService userService;

	@Override
	public void populate(final AbstractOrderModel source, final AbstractOrderData target) throws ConversionException
	{
		if (null != source.getBonusPoints())
		{
			target.setLoyaltyPoints(source.getBonusPoints());
		}
		target.setEligibleForLoyalty(checkEligibilityForLoyalty());
		target.setLoyaltyVoucherDiscount(createPrice(source, source.getLoyaltyVoucherDiscount()));
	}

	private boolean checkEligibilityForLoyalty()
	{
		final BaseStoreModel baseStore = getBaseStoreService().getCurrentBaseStore();
		final CustomerModel customer = (CustomerModel) getUserService().getCurrentUser();
		final List<LoyaltyVoucherModel> loyaltyVouchers = getLoyaltyService().getLoyaltyVouchers(customer);
		if (CollectionUtils.isNotEmpty(loyaltyVouchers) && baseStore.getLybcEnabled())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private PriceData createPrice(final AbstractOrderModel source, final Double val)
	{
		if (source == null)
		{
			throw new IllegalArgumentException("source order must not be null");
		}

		final CurrencyModel currency = source.getCurrency();
		if (currency == null)
		{
			throw new IllegalArgumentException("source order currency must not be null");
		}

		final double priceValue = val != null ? val.doubleValue() : 0d;

		return priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(priceValue), currency);
	}

	public LoyaltyPopulator(final BaseStoreService baseStoreService, final LoyaltyService loyaltyService, final PriceDataFactory priceDataFactory, final UserService userService)
	{
		this.baseStoreService = baseStoreService;
		this.loyaltyService = loyaltyService;
		this.priceDataFactory = priceDataFactory;
		this.userService = userService;
	}

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	protected LoyaltyService getLoyaltyService()
	{
		return loyaltyService;
	}

	protected PriceDataFactory getPriceDataFactory()
	{
		return priceDataFactory;
	}

	protected UserService getUserService()
	{
		return userService;
	}
}
