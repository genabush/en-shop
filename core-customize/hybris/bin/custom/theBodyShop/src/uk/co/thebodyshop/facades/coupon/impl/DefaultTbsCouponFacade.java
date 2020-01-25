/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.facades.coupon.impl;

import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.couponfacades.facades.impl.DefaultCouponFacade;
import de.hybris.platform.store.BaseStoreModel;

import uk.co.thebodyshop.facades.coupon.TbsCouponFacade;

/**
 * @author Jagadeesh
 */
public class DefaultTbsCouponFacade extends DefaultCouponFacade implements TbsCouponFacade
{
	@Override
	public boolean checkMaximumCouponAllowedForCart()
	{
		final CartModel cart = getCartService().getSessionCart();
		final BaseStoreModel baseStore = cart.getStore();
		if (CollectionUtils.isEmpty(cart.getAppliedCouponCodes()) || Objects.nonNull(baseStore) && cart.getAppliedCouponCodes().size() < baseStore.getMaximumCouponsAllowed())
		{
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
}