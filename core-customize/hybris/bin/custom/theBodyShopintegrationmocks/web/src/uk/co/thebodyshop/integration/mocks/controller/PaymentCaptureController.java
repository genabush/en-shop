/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.mocks.controller;

import javax.xml.bind.JAXBElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.mercury.ws.v1.paymentcapture.ObjectFactory;
import com.mercury.ws.v1.paymentcapture.PaymentCapture;
import com.mercury.ws.v1.paymentcapture.PaymentCaptureResponse;

import uk.co.thebodyshop.integration.mocks.response.PaymentCaptureResponseFactory;

/**
 * @author Marcin
 */
@Endpoint
public class PaymentCaptureController {

	private static final String NAMESPACE_URI = "http://paymentCapture.v1.ws.mercury.com";

	@Autowired
	private PaymentCaptureResponseFactory paymentCaptureResponseFactory;

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "paymentCapture")
	@ResponsePayload
	public JAXBElement<PaymentCaptureResponse> order(@RequestPayload final JAXBElement<PaymentCapture> paymentCapture)
	{
		final PaymentCaptureResponse response = paymentCaptureResponseFactory.createResponse(paymentCapture.getValue());
		final ObjectFactory objectFactory = new ObjectFactory();
		return objectFactory.createPaymentCaptureResponse(response);
	}
}