/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.svs.facades;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;

import uk.co.thebodyshop.integration.svs.exceptions.GiftCardException;
import uk.co.thebodyshop.integration.svs.model.GiftCardModel;
import uk.co.thebodyshop.integrations.svs.data.GiftCardResponseData;

public interface GiftCardFacade
{
	GiftCardResponseData applyGiftCardToCart(final String giftCardNumber, final String pinNumber, CartModel cart);

	GiftCardModel addGiftCard(final String giftCardNumber, final String pinNumber, CartModel cart);

	boolean cartHasGiftCard(CartModel cart);

	String authorise(String giftCardNumber, String pinNumber, CartModel cartModel, Double amountAppliedForOrder, String isocode);

	boolean reverseAuthorise(final GiftCardModel giftCard, final AbstractOrderModel orderModel);

	boolean cartContainsGiftCard(String giftCardNumber, CartModel cart);

	boolean updateGiftCardsAmounts(CartModel cartModel);

	void reapplyGiftCardsInTheCart(CartModel cart);

	void removeGiftCard(String giftCardNumber, CartModel cart);

	void removeAllGiftCard(CartModel cart);

	GiftCardModel calculateTotalsForGiftCard(CartModel cart, GiftCardModel giftCardModel) throws GiftCardException;

	void createGiftcardPaymentInfo(CartModel cart);

	Double getGiftCardBalance(final String giftCardNumber, final String pinNumber, final String currencyCode);

	boolean verifyMaximumGiftCardsPerOrderReached(final CartModel cart);
}
