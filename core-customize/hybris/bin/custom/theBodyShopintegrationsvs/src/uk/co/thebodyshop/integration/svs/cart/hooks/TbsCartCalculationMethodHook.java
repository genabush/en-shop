/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package uk.co.thebodyshop.integration.svs.cart.hooks;

import de.hybris.platform.commerceservices.order.hook.CommerceCartCalculationMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import uk.co.thebodyshop.integration.svs.facades.GiftCardFacade;

/**
 * @author Marcin
 */
public class TbsCartCalculationMethodHook implements CommerceCartCalculationMethodHook
{
	private final GiftCardFacade giftCardFacade;

	@Override
	public void afterCalculate(final CommerceCartParameter parameter)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("CommerceCartParameter", parameter);
		if (parameter.isRecalculate())
		{
			final CartModel cartModel = parameter.getCart();
			if (cartModel == null)
			{
				throw new IllegalArgumentException("The cart model is null.");
			}
			if (getGiftCardFacade().cartHasGiftCard(cartModel))
			{
				getGiftCardFacade().updateGiftCardsAmounts(cartModel);
			}
		}
	}

	@Override
	public void beforeCalculate(final CommerceCartParameter parameter)
	{
		// Implementation not needed
	}

	public TbsCartCalculationMethodHook(final GiftCardFacade giftCardFacade)
	{
		this.giftCardFacade = giftCardFacade;
	}

	/**
	 * @return the giftCardFacade
	 */
	protected GiftCardFacade getGiftCardFacade()
	{
		return giftCardFacade;
	}
}
