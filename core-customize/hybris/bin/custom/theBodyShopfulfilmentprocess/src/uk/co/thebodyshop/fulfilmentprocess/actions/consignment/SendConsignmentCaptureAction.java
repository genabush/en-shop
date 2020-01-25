/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.fulfilmentprocess.actions.consignment;

import javax.xml.bind.JAXBElement;

import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.mercury.ws.v1.paymentcapture.ObjectFactory;
import com.mercury.ws.v1.paymentcapture.PaymentCapture;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.task.RetryLaterException;

import uk.co.thebodyshop.core.services.order.TbsAcceleratorCheckoutService;
import uk.co.thebodyshop.core.stock.TbsCommerceStockService;
import uk.co.thebodyshop.fulfilmentprocess.actions.AbstractLimitedRetryAction;
import uk.co.thebodyshop.integration.actions.SendCheckResponseAction;
import uk.co.thebodyshop.integration.jaxb.order.Order;

/**
 * @author Marcin
 */
public class SendConsignmentCaptureAction extends AbstractLimitedRetryAction<ConsignmentProcessModel>
{
	private static final Logger LOG = LoggerFactory.getLogger(SendConsignmentCaptureAction.class);

	private final SendCheckResponseAction<JAXBElement<PaymentCapture>> orderSendCaptureAction;

	private final Converter<OrderModel, Order> orderSendPaymentConverter;

	public SendConsignmentCaptureAction(final SendCheckResponseAction<JAXBElement<PaymentCapture>> orderSendCaptureAction, final Converter<OrderModel, Order> orderSendPaymentConverter, final TbsCommerceStockService tbsCommerceStockService,
			final TbsAcceleratorCheckoutService tbsAcceleratorCheckoutService)
	{
		this.orderSendCaptureAction = orderSendCaptureAction;
		this.orderSendPaymentConverter = orderSendPaymentConverter;
	}

	@Override
	public void executeAction(final ConsignmentProcessModel consignmentProcessModel) throws RetryLaterException
	{
		final OrderModel order = consignmentProcessModel.getParentProcess().getOrder();
		Assert.notNull(order,"Order cannot be null");
		LOG.info("Sending capture information to ERP system for order :: " + order.getCode());
		final ObjectFactory objectFactory = new ObjectFactory();
		final PaymentCapture paymentCapture = objectFactory.createPaymentCapture();
		paymentCapture.setRequest(getOrderSendPaymentConverter().convert(order));
		final JAXBElement<PaymentCapture> paymentCaptureRequest = objectFactory.createPaymentCapture(paymentCapture);
		boolean success;
		try
		{
			success =  getOrderSendCaptureAction().sendToGatewayAndCheckResponse(paymentCaptureRequest);
		}
		catch (final Exception e)
		{
			throw new RetryLaterException(String.format("Received error while sending order", order.getCode()),e);
		}
		if (BooleanUtils.isFalse(success))
		{
			throw new RetryLaterException(String.format("Received error response for order", order.getCode()));
		}
	}

	/**
	 * @return the orderSendCaptureAction
	 */
	protected SendCheckResponseAction<JAXBElement<PaymentCapture>> getOrderSendCaptureAction()
	{
		return orderSendCaptureAction;
	}

	/**
	 * @return the orderPaymentConverter
	 */
	protected Converter<OrderModel, Order> getOrderSendPaymentConverter()
	{
		return orderSendPaymentConverter;
	}
}
