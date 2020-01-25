/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.ws.response.handlers;

import java.util.Optional;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.mercury.ws.v1.paymentcapture.HybrisResponse;
import com.mercury.ws.v1.paymentcapture.PaymentCapture;
import com.mercury.ws.v1.paymentcapture.PaymentCaptureResponse;

import de.hybris.platform.core.model.order.OrderModel;

import uk.co.thebodyshop.core.services.TbsOrderService;
import uk.co.thebodyshop.integration.jaxb.order.Order;

/**
 * @author Marcin
 */
public class OrderPaymentCaptureHandler extends AbstractResponseHandler<JAXBElement<PaymentCapture>, JAXBElement<PaymentCaptureResponse>>
{
	private static final Logger LOG = LoggerFactory.getLogger(OrderPaymentCaptureHandler.class);


	private final TbsOrderService tbsOrderService;

	public OrderPaymentCaptureHandler(final TbsOrderService tbsOrderService)
	{
		this.tbsOrderService = tbsOrderService;
	}

	@Override
	protected void handleSuccessFulResponse(final JAXBElement<PaymentCapture> input, final JAXBElement<PaymentCaptureResponse> output)
	{
		final PaymentCapture paymentCapture = input.getValue();
		Assert.notNull(paymentCapture, "Payment capture is required");
		final Order order = paymentCapture.getRequest();
		Assert.notNull(order, "Order can't be null");
		final String orderCode = order.getCode();
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Received status [{}] for order [{}]", Optional.ofNullable(output.getValue()).map(PaymentCaptureResponse::getReturn).map(HybrisResponse::getStatus).orElse(null), orderCode);
		}
		final OrderModel orderModel = getTbsOrderService().getOrderForCode(orderCode);
		LOG.info("Payment capture information succesfully sent to ERP system for order :: " + orderModel.getCode());
	}

	protected TbsOrderService getTbsOrderService()
	{
		return tbsOrderService;
	}
}
