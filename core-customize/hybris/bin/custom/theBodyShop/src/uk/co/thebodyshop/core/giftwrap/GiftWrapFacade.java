/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.giftwrap;

import uk.co.thebodyshop.core.giftMessage.GiftWrapMessageData;
import uk.co.thebodyshop.core.wishlist.StatusResponseData;

/**
 * @author Jagadeesh
 */
public interface GiftWrapFacade
{

	/**
	 * This method used to add the gift wrap to the cart
	 *
	 * @return StatusResponseData
	 */
	public StatusResponseData addGiftWrapToCart();

	/**
	 * This method used to remove the gift wrap to the cart
	 *
	 * @return StatusResponseData
	 */
	public StatusResponseData removeGiftWrapFromCart();

	/**
	 * This method used to add and edit the gift wrap message to the cart
	 *
	 * @param giftMessageName,giftMessage
	 * @return StatusResponseData
	 */
	public StatusResponseData addGiftMessageToCart(final GiftWrapMessageData giftWrapMessageData);

}
