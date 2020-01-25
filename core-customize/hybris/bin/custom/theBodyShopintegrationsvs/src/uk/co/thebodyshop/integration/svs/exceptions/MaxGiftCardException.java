/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.svs.exceptions;

public class MaxGiftCardException extends GiftCardException
{

	private final int maximumGiftCards;

	public MaxGiftCardException(final int maximumGiftCards)
	{
		this.maximumGiftCards = maximumGiftCards;
	}

	public int getMaximumGiftCards()
	{
		return maximumGiftCards;
	}

}
