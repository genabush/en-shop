/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package uk.co.thebodyshop.payment.exceptions;

/**
 * 
 */
public class PaymentMethodException extends RuntimeException
{

	/**
	 * 
	 * @param message
	 */
	public PaymentMethodException(final String message)
	{
		super(message);
	}

}
