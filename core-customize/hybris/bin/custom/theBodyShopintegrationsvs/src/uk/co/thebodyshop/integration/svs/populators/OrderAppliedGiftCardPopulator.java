/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.svs.populators;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.thoughtworks.xstream.converters.ConversionException;

import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;

import uk.co.thebodyshop.integration.svs.model.GiftCardModel;
import uk.co.thebodyshop.integrations.svs.data.GiftCardData;

public class OrderAppliedGiftCardPopulator implements Populator<AbstractOrderModel, AbstractOrderData>
{

	@Resource(name = "priceDataFactory")
	private PriceDataFactory priceDataFactory;

	@Override
	public void populate(final AbstractOrderModel source, final AbstractOrderData target) throws ConversionException
	{
		final Set<GiftCardModel> giftCardModelList = source.getGiftCards();
		if (CollectionUtils.isNotEmpty(giftCardModelList))
		{
			final List<GiftCardData> giftCardDataList = new ArrayList<>();
			for (final GiftCardModel giftCardModel : giftCardModelList)
			{
				final GiftCardData giftCardData = new GiftCardData();
				giftCardData.setGiftCardNumber(giftCardModel.getGiftCardNumber());
				giftCardData.setInitialBalance(createPrice(source, giftCardModel.getCurrentBalance()));
				giftCardData.setAppliedAmount(createPrice(source, giftCardModel.getAmountAppliedForOrder()));

				if (giftCardModel.getCurrentBalance() - giftCardModel.getAmountAppliedForOrder() > 0.0)
				{
					giftCardData.setRemainingBalance(createPrice(source, giftCardModel.getCurrentBalance() - giftCardModel.getAmountAppliedForOrder()));
				}
				else
				{
					giftCardData.setRemainingBalance(createPrice(source, 0.0));
				}

				if (giftCardModel.getAmountAppliedForOrder() != null && giftCardModel.getAmountAppliedForOrder() > 0)
				{
					giftCardData.setApplied(true);
				}
				else
				{
					if (giftCardModel.getCurrentBalance() != null && giftCardModel.getCurrentBalance() > 0)
					{
						giftCardData.setApplied(true);
					}
					else
					{
						giftCardData.setApplied(false);
					}
				}
				giftCardDataList.add(giftCardData);
			}
			target.setGiftCards(giftCardDataList);
		}
		target.setTotalGiftCardsValue(createPrice(source, source.getAmountGiftCards()));
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

		// Get double value, handle null as zero
		final double priceValue = val != null ? val.doubleValue() : 0d;

		return priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(priceValue), currency);
	}
}
