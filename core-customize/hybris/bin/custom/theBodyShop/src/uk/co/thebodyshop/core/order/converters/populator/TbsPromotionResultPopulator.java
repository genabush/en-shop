/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.order.converters.populator;

import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;

import de.hybris.platform.commercefacades.order.converters.populator.PromotionResultPopulator;
import de.hybris.platform.commercefacades.product.data.PromotionResultData;
import de.hybris.platform.promotionengineservices.model.RuleBasedOrderEntryAdjustActionModel;
import de.hybris.platform.promotions.model.PromotionResultModel;

/**
 * @author Jagadeesh
 */
public class TbsPromotionResultPopulator extends PromotionResultPopulator
{
	@Override
	public void populate(final PromotionResultModel source, final PromotionResultData target)
	{
		super.populate(source, target);
		final double discountAmount = source.getActions().stream().filter(action -> action instanceof RuleBasedOrderEntryAdjustActionModel).mapToDouble(action -> ((RuleBasedOrderEntryAdjustActionModel) action).getAmount().doubleValue()).sum();
		final Optional<Optional<String>> coupons = source.getActions().stream().filter(action -> action instanceof RuleBasedOrderEntryAdjustActionModel && CollectionUtils.isNotEmpty(((RuleBasedOrderEntryAdjustActionModel) action).getUsedCouponCodes()))
				.map(action -> (RuleBasedOrderEntryAdjustActionModel) action).map(action -> action.getUsedCouponCodes().stream().findFirst()).findFirst();
		if (coupons.isPresent())
		{
			final Optional<String> couponCode = coupons.get();
			if (couponCode.isPresent())
			{
				target.setAppliedVoucher(couponCode.get());
			}
		}
		target.setDiscountAmount(discountAmount);
	}
}
