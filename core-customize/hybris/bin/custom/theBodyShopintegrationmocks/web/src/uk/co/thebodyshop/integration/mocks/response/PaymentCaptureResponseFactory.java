/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.mocks.response;

import static uk.co.thebodyshop.integration.constants.TheBodyShopintegrationlegacyConstants.SERVICE_RESPONSE_STATUS.FAILED;

import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mercury.ws.v1.paymentcapture.HybrisResponse;
import com.mercury.ws.v1.paymentcapture.PaymentCapture;
import com.mercury.ws.v1.paymentcapture.PaymentCaptureResponse;

/**
 * @author Marcin
 */
@Component("paymentCaptureResponseFactory")
public class PaymentCaptureResponseFactory extends AbstractResponseFactory<PaymentCapture, PaymentCaptureResponse>
{
	private static final String SUCCESS = "SUCCESS";

	@Autowired
	private Predicate<PaymentCapture> paymentCaptureFailPredicate;

	@Override
	protected PaymentCaptureResponse createFailureResponse(final PaymentCapture type)
	{
		final PaymentCaptureResponse paymentCaptureResponse = createCommonResponse(type);
		paymentCaptureResponse.getReturn().setStatus(FAILED);
		return paymentCaptureResponse;
	}

	@Override
	protected PaymentCaptureResponse createSuccessResponse(final PaymentCapture type)
	{
		final PaymentCaptureResponse paymentCaptureResponse = createCommonResponse(type);
		paymentCaptureResponse.getReturn().setStatus(SUCCESS);
		return paymentCaptureResponse;
	}

	private PaymentCaptureResponse createCommonResponse(final PaymentCapture type)
	{
		final PaymentCaptureResponse paymentCaptureResponse = new PaymentCaptureResponse();
		final HybrisResponse hybrisResponse = new HybrisResponse();
		hybrisResponse.setMessageId(type.getMessageid());
		hybrisResponse.setRequestId(type.getMessageid());
		paymentCaptureResponse.setReturn(hybrisResponse);
		return paymentCaptureResponse;
	}

	@Override
	protected Predicate<PaymentCapture> getFailurePredicate()
	{
		return paymentCaptureFailPredicate;
	}
}