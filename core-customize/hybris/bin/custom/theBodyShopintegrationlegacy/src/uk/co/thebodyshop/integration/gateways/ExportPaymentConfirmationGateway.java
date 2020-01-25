/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.gateways;

import javax.xml.bind.JAXBElement;

import com.mercury.ws.v1.paymentcapture.PaymentCapture;
import com.mercury.ws.v1.paymentcapture.PaymentCaptureResponse;

/**
 * @author Marcin
 */
public interface ExportPaymentConfirmationGateway
{
	JAXBElement<PaymentCaptureResponse> exportPaymentConfirmation(JAXBElement<PaymentCapture> exportPaymentConfirmation);
}
