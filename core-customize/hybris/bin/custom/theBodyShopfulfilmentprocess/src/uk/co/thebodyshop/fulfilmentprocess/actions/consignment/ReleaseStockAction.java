/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.fulfilmentprocess.actions.consignment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.stock.exception.InsufficientStockLevelException;
import de.hybris.platform.task.RetryLaterException;

import uk.co.thebodyshop.core.stock.TbsCommerceStockService;

/**
 * @author prateek.goel
 */
public class ReleaseStockAction extends AbstractProceduralAction<ConsignmentProcessModel>
{

	private static final Logger LOG = LoggerFactory.getLogger(ReleaseStockAction.class);

	private TbsCommerceStockService tbsCommerceStockService;

	@Override
	public void executeAction(final ConsignmentProcessModel process) throws RetryLaterException, Exception
	{
		final ConsignmentModel consignment = process.getConsignment();
		try
		{
			getTbsCommerceStockService().releaseStock(consignment);
		}
		catch (final InsufficientStockLevelException e)
		{
			LOG.error("Error in releasing stock for consignment :: " + consignment.getCode());
			throw new InsufficientStockLevelException(String.format("Error in releasing the stock"));
		}
	}

	protected TbsCommerceStockService getTbsCommerceStockService()
	{
		return tbsCommerceStockService;
	}

	public void setTbsCommerceStockService(final TbsCommerceStockService tbsCommerceStockService)
	{
		this.tbsCommerceStockService = tbsCommerceStockService;
	}

}
