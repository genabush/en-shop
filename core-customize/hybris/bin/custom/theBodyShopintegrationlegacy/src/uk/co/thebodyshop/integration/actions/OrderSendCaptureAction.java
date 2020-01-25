/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.actions;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mercury.ws.v1.paymentcapture.PaymentCapture;
import com.mercury.ws.v1.paymentcapture.PaymentCaptureResponse;

import uk.co.thebodyshop.integration.gateways.ExportPaymentConfirmationGateway;
import uk.co.thebodyshop.integration.utils.XmlUtils;
import uk.co.thebodyshop.integration.ws.response.handlers.ResponseHandler;

/**
 * @author Marcin
 */
public class OrderSendCaptureAction implements SendCheckResponseAction<JAXBElement<PaymentCapture>>
{
	private static final Logger LOG = LoggerFactory.getLogger(OrderSendCaptureAction.class);

	private final ExportPaymentConfirmationGateway exportPaymentConfirmationGateway;

	private final ResponseHandler<JAXBElement<PaymentCapture>, JAXBElement<PaymentCaptureResponse>> orderPaymentCaptureHandler;

	public OrderSendCaptureAction(final ExportPaymentConfirmationGateway exportPaymentConfirmationGateway, final ResponseHandler<JAXBElement<PaymentCapture>, JAXBElement<PaymentCaptureResponse>> orderPaymentCaptureHandler)
	{
		this.exportPaymentConfirmationGateway = exportPaymentConfirmationGateway;
		this.orderPaymentCaptureHandler = orderPaymentCaptureHandler;
	}

	@Override
	public boolean sendToGatewayAndCheckResponse(final JAXBElement<PaymentCapture> input)
	{
		final JAXBElement<PaymentCaptureResponse> output = getExportPaymentConfirmationGateway().exportPaymentConfirmation(input);
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Order Payment Capture Response received is \n {}", XmlUtils.toXml(output));
		}
		return getOrderPaymentCaptureHandler().handleResponse(input, output);
	}

	/**
	 * @return the exportPaymentConfirmationGateway
	 */
	protected ExportPaymentConfirmationGateway getExportPaymentConfirmationGateway()
	{
		return exportPaymentConfirmationGateway;
	}

	/**
	 * @return the orderPaymentCaptureHandler
	 */
	public ResponseHandler<JAXBElement<PaymentCapture>, JAXBElement<PaymentCaptureResponse>> getOrderPaymentCaptureHandler()
	{
		return orderPaymentCaptureHandler;
	}
}
