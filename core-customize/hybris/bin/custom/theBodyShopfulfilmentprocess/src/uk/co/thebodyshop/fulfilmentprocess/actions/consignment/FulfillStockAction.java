/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.fulfilmentprocess.actions.consignment;

import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.task.RetryLaterException;

import uk.co.thebodyshop.core.stock.TbsCommerceStockService;

/**
 * @author vasanthramprakasam
 */
public class FulfillStockAction extends AbstractProceduralAction<ConsignmentProcessModel>
{
	private final TbsCommerceStockService tbsCommerceStockService;

	public FulfillStockAction(final TbsCommerceStockService tbsCommerceStockService)
	{
		this.tbsCommerceStockService = tbsCommerceStockService;
	}

	@Override
	public void executeAction(final ConsignmentProcessModel consignmentProcessModel) throws RetryLaterException, Exception
	{
		getTbsCommerceStockService().fulfillStock(consignmentProcessModel.getConsignment(), false);
	}

	protected TbsCommerceStockService getTbsCommerceStockService()
	{
		return tbsCommerceStockService;
	}
}
