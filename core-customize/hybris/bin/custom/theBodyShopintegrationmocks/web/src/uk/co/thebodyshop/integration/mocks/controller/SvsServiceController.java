/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package uk.co.thebodyshop.integration.mocks.controller;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.svs.svsxml.beans.Amount;
import com.svs.svsxml.beans.Card;
import com.svs.svsxml.beans.ReturnCode;
import com.svs.svsxml.service.BalanceInquiry;
import com.svs.svsxml.service.BalanceInquiryResponse;
import com.svs.svsxml.service.ObjectFactory;
import com.svs.svsxml.service.PreAuth;
import com.svs.svsxml.service.PreAuthComplete;
import com.svs.svsxml.service.PreAuthCompleteResponse;
import com.svs.svsxml.service.PreAuthResponse;

import uk.co.thebodyshop.integration.svs.services.SvsService;

/**
 * @author Marcin
 */
@Endpoint
public class SvsServiceController
{
	private static final String NAMESPACE_URI = "http://service.svsxml.svs.com";

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "balanceInquiry")
	@ResponsePayload
	public BalanceInquiryResponse balanceInquiry(@RequestPayload
			final BalanceInquiry balanceInquiry)
	{
		final ObjectFactory objectFactory = new ObjectFactory();
		final BalanceInquiryResponse balanceInquiryResponse = objectFactory.createBalanceInquiryResponse();
		final com.svs.svsxml.beans.ObjectFactory beansobjectFactory = new com.svs.svsxml.beans.ObjectFactory();
		final com.svs.svsxml.beans.BalanceInquiryResponse balanceInquiryResponseReturn = getCheckBalanceStubResponse(beansobjectFactory, balanceInquiry.getRequest().getCard().getCardNumber(), balanceInquiry.getRequest().getCard().getPinNumber(),
				balanceInquiry.getRequest().getAmount().getCurrency(), balanceInquiry.getRequest().getTransactionID());
		balanceInquiryResponse.setBalanceInquiryReturn(balanceInquiryResponseReturn);
		return balanceInquiryResponse;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "preAuth")
	@ResponsePayload
	public PreAuthResponse preAuth(@RequestPayload
			final PreAuth preAuth)
	{
		final ObjectFactory objectFactory = new ObjectFactory();
		final PreAuthResponse preAuthResponse = objectFactory.createPreAuthResponse();
		final com.svs.svsxml.beans.ObjectFactory beansobjectFactory = new com.svs.svsxml.beans.ObjectFactory();
		final com.svs.svsxml.beans.PreAuthResponse preAuthResponseReturn = getStubAuthResponse(beansobjectFactory, preAuth.getRequest().getCard().getCardNumber(), preAuth.getRequest().getCard().getPinNumber(),
				preAuth.getRequest().getRequestedAmount().getAmount(), preAuth.getRequest().getRequestedAmount().getCurrency(), preAuth.getRequest().getTransactionID());
		preAuthResponse.setPreAuthReturn(preAuthResponseReturn);
		return preAuthResponse;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "preAuthComplete")
	@ResponsePayload
	public PreAuthCompleteResponse preAuthComplete(@RequestPayload
			final PreAuthComplete preAuthComplete)
	{
		final ObjectFactory objectFactory = new ObjectFactory();
		final PreAuthCompleteResponse preAuthCompleteResponse = objectFactory.createPreAuthCompleteResponse();
		final com.svs.svsxml.beans.ObjectFactory beansobjectFactory = new com.svs.svsxml.beans.ObjectFactory();
		final com.svs.svsxml.beans.PreAuthCompleteResponse preAuthCompleteResponseReturn = getStubAuthCompleteResponse(beansobjectFactory, preAuthComplete.getRequest().getCard().getCardNumber(),
				preAuthComplete.getRequest().getTransactionAmount().getAmount(), preAuthComplete.getRequest().getTransactionAmount().getCurrency(), preAuthComplete.getRequest().getTransactionID());
		preAuthCompleteResponse.setPreAuthCompleteReturn(preAuthCompleteResponseReturn);
		return preAuthCompleteResponse;
	}

	private com.svs.svsxml.beans.BalanceInquiryResponse getCheckBalanceStubResponse(final com.svs.svsxml.beans.ObjectFactory objectFactory, final String giftCardNumber, final String pinNumber, final String currencyCode, final String transactionId)
	{
		final com.svs.svsxml.beans.BalanceInquiryResponse balanceInquiryResponse = objectFactory.createBalanceInquiryResponse();
		if (pinNumber.startsWith("2"))
		{
			balanceInquiryResponse.setBalanceAmount(getAmount(objectFactory, currencyCode, 0.0));
			balanceInquiryResponse.setReturnCode(getReturnCode(objectFactory, SvsService.INSUFFICIENT_FUNDS, SvsService.INSUFFICIENT_FUNDS));
		}
		else if (pinNumber.startsWith("3"))
		{
			return null;
		}
		else
		{
			balanceInquiryResponse.setBalanceAmount(getAmount(objectFactory, currencyCode, getAmountFromPin(pinNumber)));
			balanceInquiryResponse.setReturnCode(getReturnCode(objectFactory, SvsService.APPROVED, SvsService.APPROVED));
		}
		balanceInquiryResponse.setTransactionID(transactionId);
		return balanceInquiryResponse;
	}

	private ReturnCode getReturnCode(final com.svs.svsxml.beans.ObjectFactory objectFactory, final String code, final String description)
	{
		final ReturnCode returnCode = objectFactory.createReturnCode();
		returnCode.setReturnCode(code);
		returnCode.setReturnDescription(description);
		return returnCode;
	}

	private double getAmountFromPin(final String pinNumber)
	{
		final double amount = Double.valueOf(pinNumber.substring(1));
		return amount;
	}

	private Amount getAmount(final com.svs.svsxml.beans.ObjectFactory objectFactory, final String currencyCode, final double value)
	{
		final Amount amount = objectFactory.createAmount();
		amount.setAmount(value);
		amount.setCurrency(currencyCode);
		return amount;
	}

	private com.svs.svsxml.beans.PreAuthResponse getStubAuthResponse(final com.svs.svsxml.beans.ObjectFactory objectFactory, final String cardNumber, final String pinNumber, final double cardAmount, final String currencyIsocode,
			final String transactionId)
	{
		final com.svs.svsxml.beans.PreAuthResponse authResponse = objectFactory.createPreAuthResponse();
		if (pinNumber.equals("9999"))
		{
			authResponse.setReturnCode(getReturnCode(objectFactory, SvsService.INSUFFICIENT_FUNDS, SvsService.INSUFFICIENT_FUNDS));
		}
		else
		{
			authResponse.setReturnCode(getReturnCode(objectFactory, SvsService.APPROVED, SvsService.APPROVED));
		}
		final Card card = new Card();
		card.setCardNumber(cardNumber);
		authResponse.setCard(card);
		authResponse.setApprovedAmount(getAmount(objectFactory, currencyIsocode, cardAmount));
		authResponse.setTransactionID(transactionId);
		authResponse.setAuthorizationCode(transactionId + cardNumber + SvsService.APPROVED);
		return authResponse;
	}

	private com.svs.svsxml.beans.PreAuthCompleteResponse getStubAuthCompleteResponse(final com.svs.svsxml.beans.ObjectFactory objectFactory, final String cardNumber, final double approvedAmount, final String currencyIsocode, final String transactionId)
	{
		final com.svs.svsxml.beans.PreAuthCompleteResponse authResponse = objectFactory.createPreAuthCompleteResponse();
		if (cardNumber.equals("9999999999999999998"))
		{
			authResponse.setReturnCode(getReturnCode(objectFactory, SvsService.INSUFFICIENT_FUNDS, SvsService.INSUFFICIENT_FUNDS));
		}
		else
		{
			authResponse.setReturnCode(getReturnCode(objectFactory, SvsService.APPROVED, SvsService.APPROVED));
		}
		final Card card = new Card();
		card.setCardNumber(cardNumber);
		authResponse.setCard(card);
		authResponse.setApprovedAmount(getAmount(objectFactory, currencyIsocode, approvedAmount));
		authResponse.setTransactionID(transactionId);
		authResponse.setAuthorizationCode(transactionId + cardNumber + SvsService.APPROVED);
		return authResponse;
	}
}
