/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.adyen.service;


/**
 * @author Krishna
 */
public interface AdyenPaymentRefusalMessageService
{
	public String getRefusalMessage(String refusalReasonCode);
}
