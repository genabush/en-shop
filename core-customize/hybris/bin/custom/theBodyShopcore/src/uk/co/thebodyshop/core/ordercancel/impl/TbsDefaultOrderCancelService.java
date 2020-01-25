/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.ordercancel.impl;

import de.hybris.platform.basecommerce.enums.OrderCancelState;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.ordercancel.CancelDecision;
import de.hybris.platform.ordercancel.OrderCancelDeniedException;
import de.hybris.platform.ordercancel.OrderCancelException;
import de.hybris.platform.ordercancel.OrderCancelRequest;
import de.hybris.platform.ordercancel.OrderCancelRequestExecutor;
import de.hybris.platform.ordercancel.impl.DefaultOrderCancelService;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;

/**
 * @author Jagadeesh
 */
public class TbsDefaultOrderCancelService extends DefaultOrderCancelService
{
	@Override
	public OrderCancelRecordEntryModel requestOrderCancel(final OrderCancelRequest orderCancelRequest, final PrincipalModel requestor) throws OrderCancelException
	{
		final CancelDecision cancelDecision = isCancelPossible(orderCancelRequest.getOrder(), requestor, orderCancelRequest.isPartialCancel(), orderCancelRequest.isPartialEntryCancel());
		if (cancelDecision.isAllowed())
		{
			final OrderCancelState currentCancelState = getStateMappingStrategy().getOrderCancelState(orderCancelRequest.getOrder());
			final OrderCancelRequestExecutor ocre = getRequestExecutorsMap().get(currentCancelState);
			if (ocre == null)
			{
				throw new IllegalStateException("Cannot find request executor for cancel state: " + currentCancelState.name());
			}
			else
			{
				ocre.processCancelRequest(orderCancelRequest, null);
				return null;
			}
		}
		else
		{
			throw new OrderCancelDeniedException(orderCancelRequest.getOrder().getCode(), cancelDecision);
		}
	}

}
