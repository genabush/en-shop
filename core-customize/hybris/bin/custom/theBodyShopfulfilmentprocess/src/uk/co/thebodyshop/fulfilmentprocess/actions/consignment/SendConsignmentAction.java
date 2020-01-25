/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.fulfilmentprocess.actions.consignment;

import java.util.function.Predicate;

import javax.xml.bind.JAXBElement;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.util.Assert;

import com.mercury.ws.v1.placeorder.ObjectFactory;
import com.mercury.ws.v1.placeorder.PlaceOrder;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.task.RetryLaterException;

import uk.co.thebodyshop.core.enums.ConsignmentEntryStatus;
import uk.co.thebodyshop.fulfilmentprocess.actions.AbstractLimitedRetryAction;
import uk.co.thebodyshop.integration.actions.SendCheckResponseAction;
import uk.co.thebodyshop.integration.jaxb.order.Order;

/**
 * @author vasanthramprakasam
 */
public class SendConsignmentAction extends AbstractLimitedRetryAction<ConsignmentProcessModel>
{

	private final SendCheckResponseAction<JAXBElement<PlaceOrder>> orderSendResponseAction;

	private final Converter<OrderModel, Order> placeOrderConverter;

	private final Predicate<ConsignmentModel> clickAndCollectConsignmentPredicate;

	public SendConsignmentAction(
			final SendCheckResponseAction<JAXBElement<PlaceOrder>> orderSendResponseAction,
			final Converter<OrderModel, Order> placeOrderConverter, final Predicate<ConsignmentModel> clickAndCollectConsignmentPredicate)
	{
		this.orderSendResponseAction = orderSendResponseAction;
		this.placeOrderConverter = placeOrderConverter;
		this.clickAndCollectConsignmentPredicate = clickAndCollectConsignmentPredicate;
	}

	@Override
	public void executeAction(final ConsignmentProcessModel consignmentProcessModel) throws RetryLaterException
	{
		final OrderModel order = consignmentProcessModel.getParentProcess().getOrder();
		Assert.notNull(order,"Order cannot be null");
		final ObjectFactory objectFactory = new ObjectFactory();
		final PlaceOrder placeOrder = objectFactory.createPlaceOrder();
		placeOrder.setRequest(getPlaceOrderConverter().convert(order));
		final JAXBElement<PlaceOrder> placeOrderRequest = objectFactory.createPlaceOrder(placeOrder);
		boolean success;
		try
		{
			success = getOrderSendResponseAction().sendToGatewayAndCheckResponse(placeOrderRequest);
		}
		catch (final Exception e)
		{
			throw new RetryLaterException(String.format("Received error while sending order", order.getCode()),e);
		}
		if (BooleanUtils.isFalse(success))
		{
			throw new RetryLaterException(String.format("Received error response for order", order.getCode()));
		}
		else
		{
			final ConsignmentModel consignment = consignmentProcessModel.getConsignment();
			if (getClickAndCollectConsignmentPredicate().test(consignment))
			{
				processClickAndCollectOrder(order, consignment);
			}
			else
			{
				processConsignment(consignment);
			}
		}
	}

	private void processConsignment(final ConsignmentModel consignment)
	{
		processConsignmentEntries(consignment);
		consignment.setStatus(ConsignmentStatus.SENT);
		getModelService().save(consignment);
	}

	private void processClickAndCollectOrder(final OrderModel order, final ConsignmentModel consignment)
	{
		processConsignmentEntries(consignment);
		consignment.setStatus(ConsignmentStatus.RECEIVED);
		getModelService().save(consignment);
	}

	private void processConsignmentEntries(final ConsignmentModel consignment)
	{
		 for (final ConsignmentEntryModel consignmentEntry : consignment.getConsignmentEntries())
		 {
				consignmentEntry.setStatus(ConsignmentEntryStatus.READY_FOR_PICKING);
				getModelService().save(consignmentEntry);
		 }
	}

	protected SendCheckResponseAction<JAXBElement<PlaceOrder>> getOrderSendResponseAction()
	{
		return orderSendResponseAction;
	}

	protected Converter<OrderModel, Order> getPlaceOrderConverter()
	{
		return placeOrderConverter;
	}

	/**
	 * @return the clickAndCollectConsignmentPredicate
	 */
	protected Predicate<ConsignmentModel> getClickAndCollectConsignmentPredicate()
	{
		return clickAndCollectConsignmentPredicate;
	}
}
