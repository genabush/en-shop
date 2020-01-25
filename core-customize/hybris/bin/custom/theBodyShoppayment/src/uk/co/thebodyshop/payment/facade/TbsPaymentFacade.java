/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.payment.facade;

import uk.co.thebodyshop.payment.data.PaymentMethodsResponse;


/**
 * @author mahesh
 */
public interface TbsPaymentFacade
{
	/**
	 * To get list of payment methods from Adyen and add other supported payment methods per store.
	 */
	public PaymentMethodsResponse getSupportedPaymentMethods();
}
