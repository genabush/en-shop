/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.svs.order.dynamicattributes;

import java.math.BigDecimal;

import org.apache.commons.collections.CollectionUtils;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;

import uk.co.thebodyshop.integration.svs.model.GiftCardModel;

public class AmountGiftCardsForOrderAttributeHandler implements DynamicAttributeHandler<Double, AbstractOrderModel>
{

	@Override
	public Double get(final AbstractOrderModel order)
	{
		BigDecimal total = BigDecimal.valueOf(0.0);

		if (CollectionUtils.isNotEmpty(order.getGiftCards()))
		{
			for (final GiftCardModel giftCard : order.getGiftCards())
			{
				if (giftCard.getAmountAppliedForOrder() != null)
				{
					total = total.add(BigDecimal.valueOf(giftCard.getAmountAppliedForOrder()));
				}
			}
		}
		return total.doubleValue();
	}

	@Override
	public void set(final AbstractOrderModel model, final Double value)
	{
		throw new UnsupportedOperationException();
	}
}
