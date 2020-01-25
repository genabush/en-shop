/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.ordercancel.impl.executors;

import org.springframework.beans.factory.annotation.Autowired;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordercancel.OrderCancelException;
import de.hybris.platform.ordercancel.OrderCancelRequest;
import de.hybris.platform.ordercancel.OrderCancelRequestExecutor;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.servicelayer.model.ModelService;

/**
 * @author Jagadeesh
 */
public class TbsCancelRequestExecutor implements OrderCancelRequestExecutor
{

	private final ModelService modelService;

	@Autowired
	public TbsCancelRequestExecutor(final ModelService modelService)
	{
		super();
		this.modelService = modelService;
	}

	@Override
	public void processCancelRequest(final OrderCancelRequest orderCancelRequest, final OrderCancelRecordEntryModel arg1) throws OrderCancelException
	{
		final OrderModel order = orderCancelRequest.getOrder();
		order.setStatus(OrderStatus.CANCELLED);
		getModelService().save(order);
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

}
