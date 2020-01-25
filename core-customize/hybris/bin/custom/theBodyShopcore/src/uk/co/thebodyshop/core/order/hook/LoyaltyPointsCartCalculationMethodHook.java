/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.order.hook;

import org.apache.commons.collections.CollectionUtils;

import de.hybris.platform.commerceservices.order.hook.CommerceCartCalculationMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import uk.co.thebodyshop.core.loyalty.points.calculation.strategies.LoyaltyPointsCalculationStrategy;

/**
 * @author Krishna
 */
public class LoyaltyPointsCartCalculationMethodHook implements CommerceCartCalculationMethodHook
{
	private final ModelService modelService;
	private final LoyaltyPointsCalculationStrategy loyaltyPointsCalculationStrategy;
	private final BaseStoreService baseStoreService;

	@Override
	public void afterCalculate(final CommerceCartParameter parameter)
	{
		calculate(parameter);
	}

	@Override
	public void beforeCalculate(final CommerceCartParameter parameter)
	{
		// do nothing

	}

	private void calculate(final CommerceCartParameter parameter)
	{
		final CartModel cart = parameter.getCart();
		if (CollectionUtils.isNotEmpty(cart.getEntries()))
		{
			final BaseStoreModel baseStore = getBaseStoreService().getCurrentBaseStore();
			final Integer bonusPoints = getLoyaltyPointsCalculationStrategy().calculate(cart, baseStore);
			if (bonusPoints != null)
			{
				cart.setBonusPoints(bonusPoints);
				getModelService().save(cart);
			}
		}
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected LoyaltyPointsCalculationStrategy getLoyaltyPointsCalculationStrategy()
	{
		return loyaltyPointsCalculationStrategy;
	}

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	public LoyaltyPointsCartCalculationMethodHook(final ModelService modelService, final LoyaltyPointsCalculationStrategy loyaltyPointsCalculationStrategy, final BaseStoreService baseStoreService)
	{
		this.modelService = modelService;
		this.loyaltyPointsCalculationStrategy = loyaltyPointsCalculationStrategy;
		this.baseStoreService = baseStoreService;
	}
}
