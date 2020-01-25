/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.loyalty.populators;

import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import uk.co.thebodyshop.loyalty.data.LoyaltyVoucherData;
import uk.co.thebodyshop.loyalty.model.LoyaltyVoucherModel;

/**
 * @author Krishna
 */
public class LoyaltyVoucherPopulator implements Populator<LoyaltyVoucherModel, LoyaltyVoucherData>
{
	private final CartService cartService;

	@Override
	public void populate(final LoyaltyVoucherModel source, final LoyaltyVoucherData target) throws ConversionException
	{
		target.setType(source.getName());
		target.setValue(source.getValue());
		target.setExpiryDate(source.getExpiryDate());
		target.setVoucherId(source.getVoucherCode());

		final CartModel cart = getCartService().getSessionCart();
		if (Objects.nonNull(cart))
		{
			final Set<LoyaltyVoucherModel> appliedLoyaltyVouchers = cart.getAppliedLoyaltyVouchers();
			if (CollectionUtils.isNotEmpty(appliedLoyaltyVouchers))
			{
				if (appliedLoyaltyVouchers.contains(source))
				{
					target.setApplied(Boolean.TRUE);
				}
			}
		}
	}

	public LoyaltyVoucherPopulator(final CartService cartService)
	{
		this.cartService = cartService;
	}

	protected CartService getCartService()
	{
		return cartService;
	}
}
