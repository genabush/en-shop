/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.stock.processor;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.stock.exception.InsufficientStockLevelException;

/**
 * @author prateek.goel
 */
public class EmptyProcessStrategy implements StockProcessStrategy {

	@Override
	public void reserve(final AbstractOrderModel abstractOrderModel)
	{
		// No action

	}

	@Override
	public void release(final AbstractOrderModel order)
	{
		// No action

	}

	@Override
	public void release(final ConsignmentModel consignment) throws InsufficientStockLevelException
	{
		// No action
	}

	@Override
	public void fulfill(final ConsignmentModel consignment, final boolean reset)
	{
		// No action

	}

	@Override
	public boolean applicable(final AbstractOrderModel abstractOrderModel)
	{
		return false;
	}

}
