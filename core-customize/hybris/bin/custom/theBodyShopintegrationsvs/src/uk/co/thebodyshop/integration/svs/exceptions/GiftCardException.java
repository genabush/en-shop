/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.svs.exceptions;


public class GiftCardException extends Exception
{
	public GiftCardException()
	{
		super();
	}

	public GiftCardException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public GiftCardException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public GiftCardException(final String message)
	{
		super(message);
	}

	public GiftCardException(final Throwable cause)
	{
		super(cause);
	}
}
