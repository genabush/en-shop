/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services;

import de.hybris.platform.core.model.order.CartModel;

import uk.co.thebodyshop.core.giftMessage.GiftWrapMessageData;

/**
 * @author Jagadeesh
 */
public interface GiftWrapService
{
	/**
	 * This method used to add the gift wrap to the cart
	 *
	 * @return boolean
	 */
	public boolean addGiftWrapToCart(final CartModel cartModel);

	/**
	 * This method used to remove the gift wrap to the cart
	 *
	 * @return boolean
	 */
	public boolean removeGiftWrapFromCart(final CartModel cartModel);

	/**
	 * This method used to add the gift wrap message to the cart
	 *
	 * @param giftMessageName,giftMessage
	 * @return boolean
	 */
	public boolean addGiftMessageToCart(final CartModel cartModel, final GiftWrapMessageData giftWrapMessageData);

	/**
	 * This method used to check products are gift wrap eligible on the cart
	 *
	 * @param cartModel
	 * @return boolean
	 */
	public boolean checkProductsOnCartEligibleForGiftWrap(final CartModel cartModel);
}
