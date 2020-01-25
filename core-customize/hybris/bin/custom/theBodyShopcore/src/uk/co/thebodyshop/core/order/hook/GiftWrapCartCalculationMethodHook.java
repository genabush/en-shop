/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.order.hook;

import de.hybris.platform.commerceservices.order.hook.CommerceCartCalculationMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartModel;

import uk.co.thebodyshop.core.services.GiftWrapService;

/**
 * @author Jagadeesh
 */
public class GiftWrapCartCalculationMethodHook implements CommerceCartCalculationMethodHook
{
	private final GiftWrapService giftWrapService;

	public GiftWrapCartCalculationMethodHook(final GiftWrapService giftWrapService)
	{
		this.giftWrapService = giftWrapService;
	}

	@Override
	public void afterCalculate(final CommerceCartParameter parameter)
	{
		// do nothing
	}

	@Override
	public void beforeCalculate(final CommerceCartParameter parameter)
	{
		final CartModel cartModel = parameter.getCart();
		final boolean eligibleGiftWrapOnCart = getGiftWrapService().checkProductsOnCartEligibleForGiftWrap(cartModel);
		if (!eligibleGiftWrapOnCart && cartModel.getGiftWrapPrice() != 0)
		{
			getGiftWrapService().removeGiftWrapFromCart(cartModel);
		}
	}

	protected GiftWrapService getGiftWrapService()
	{
		return giftWrapService;
	}
}
