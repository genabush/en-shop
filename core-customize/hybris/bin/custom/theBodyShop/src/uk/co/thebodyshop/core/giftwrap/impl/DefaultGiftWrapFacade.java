/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.giftwrap.impl;

import de.hybris.platform.order.CartService;

import uk.co.thebodyshop.core.giftMessage.GiftWrapMessageData;
import uk.co.thebodyshop.core.giftwrap.GiftWrapFacade;
import uk.co.thebodyshop.core.services.GiftWrapService;
import uk.co.thebodyshop.core.wishlist.StatusResponseData;

/**
 * @author Jagadeesh
 */
public class DefaultGiftWrapFacade implements GiftWrapFacade
{
	private final GiftWrapService giftWrapService;

	private final CartService cartService;

	public DefaultGiftWrapFacade(final GiftWrapService giftWrapService, final CartService cartService)
	{
		this.giftWrapService = giftWrapService;
		this.cartService = cartService;
	}

	@Override
	public StatusResponseData addGiftWrapToCart()
	{
		final StatusResponseData giftWrapResponseData = new StatusResponseData();
		giftWrapResponseData.setStatus(getGiftWrapService().addGiftWrapToCart(getCartService().getSessionCart()));
		return giftWrapResponseData;
	}

	@Override
	public StatusResponseData removeGiftWrapFromCart()
	{
		final StatusResponseData giftWrapResponseData = new StatusResponseData();
		giftWrapResponseData.setStatus(getGiftWrapService().removeGiftWrapFromCart(getCartService().getSessionCart()));
		return giftWrapResponseData;
	}

	@Override
	public StatusResponseData addGiftMessageToCart(final GiftWrapMessageData giftWrapMessageData)
	{
		final StatusResponseData giftWrapResponseData = new StatusResponseData();
		giftWrapResponseData.setStatus(getGiftWrapService().addGiftMessageToCart(getCartService().getSessionCart(), giftWrapMessageData));
		return giftWrapResponseData;
	}

	protected GiftWrapService getGiftWrapService()
	{
		return giftWrapService;
	}

	protected CartService getCartService()
	{
		return cartService;
	}
}
