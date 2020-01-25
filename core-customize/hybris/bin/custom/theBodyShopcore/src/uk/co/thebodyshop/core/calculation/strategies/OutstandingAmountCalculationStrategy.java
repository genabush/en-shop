/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.calculation.strategies;

import java.math.BigDecimal;

import de.hybris.platform.core.model.order.AbstractOrderModel;

/**
 * @author Marcin
 */
public interface OutstandingAmountCalculationStrategy
{
	public BigDecimal getOutstandingAmount(final AbstractOrderModel abstractOrderModel);

}
