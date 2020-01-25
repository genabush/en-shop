/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.fulfilmentprocess.actions.order;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.task.RetryLaterException;

import uk.co.thebodyshop.core.services.order.TbsAcceleratorCheckoutService;
import uk.co.thebodyshop.core.stock.TbsCommerceStockService;

/**
 * @author Jagadeesh
 */
public class CancelWholeOrderAction extends AbstractSimpleDecisionAction<OrderProcessModel>
{

	private TbsCommerceStockService tbsCommerceStockService;

	private TbsAcceleratorCheckoutService tbsAcceleratorCheckoutService;

	@Override
	public Transition executeAction(final OrderProcessModel orderProcess) throws RetryLaterException, Exception
	{
		final AbstractOrderModel order = orderProcess.getOrder();
		if(null != order)
		{
			getTbsCommerceStockService().releaseStock(order);
		}
		if (getTbsAcceleratorCheckoutService().isCollectInStoreOrder(order))
		{
			getTbsAcceleratorCheckoutService().updateCisOrders(order.getDeliveryPointOfService(), false, true);
		}
		return Transition.OK;
	}

	protected TbsCommerceStockService getTbsCommerceStockService()
	{
		return tbsCommerceStockService;
	}

	public void setTbsCommerceStockService(final TbsCommerceStockService tbsCommerceStockService)
	{
		this.tbsCommerceStockService = tbsCommerceStockService;
	}

	protected TbsAcceleratorCheckoutService getTbsAcceleratorCheckoutService()
	{
		return tbsAcceleratorCheckoutService;
	}

	public void setTbsAcceleratorCheckoutService(final TbsAcceleratorCheckoutService tbsAcceleratorCheckoutService)
	{
		this.tbsAcceleratorCheckoutService = tbsAcceleratorCheckoutService;
	}

}
