/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.svs.facades.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.svs.svsxml.beans.BalanceInquiryResponse;

import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import uk.co.thebodyshop.integration.svs.exceptions.GiftCardException;
import uk.co.thebodyshop.integration.svs.facades.GiftCardFacade;
import uk.co.thebodyshop.integration.svs.model.GiftCardModel;
import uk.co.thebodyshop.integration.svs.model.GiftCardPaymentInfoModel;
import uk.co.thebodyshop.integration.svs.services.SvsService;
import uk.co.thebodyshop.integrations.svs.data.GiftCardResponseData;

public class DefaultGiftCardFacade implements GiftCardFacade
{
	private static final String GIFT_CARD_ZEROBALANCE = "gift.card.zerobalance";

	private static final String GIFT_CARD_VALIDATIONISSUE = "gift.card.validationissue";

	private static final String GIFT_CARD_LIMITREACHED = "gift.card.limitreached";

	private static final String GIFT_CARD_ALREADYAPPLIED = "gift.card.alreadyapplied";

	private static final Logger LOG = Logger.getLogger(DefaultGiftCardFacade.class);

	@Resource(name = "svsService")
	private SvsService svsService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;

	@Resource(name="priceDataFactory")
	private PriceDataFactory priceDataFactory;

	@Override
	public Double getGiftCardBalance(final String giftCardNumber, final String pinNumber, final String currencyCode)
	{
		final BalanceInquiryResponse response = svsService.checkBalance(giftCardNumber, pinNumber, null, currencyCode);
		if (response != null && (SvsService.APPROVED.equalsIgnoreCase(response.getReturnCode().getReturnCode()) || SvsService.INSUFFICIENT_FUNDS.equalsIgnoreCase(response.getReturnCode().getReturnCode())))
		{
			return response.getBalanceAmount().getAmount();
		}
		else
		{
			return null;
		}
	}

	@Override
	public GiftCardResponseData applyGiftCardToCart(final String giftCardNumber, final String pinNumber, final CartModel cart)
	{
		if (cartContainsGiftCard(giftCardNumber, cart))
		{
			return getErrorResponse(giftCardNumber, null, GIFT_CARD_ALREADYAPPLIED);
		}

		if (verifyMaximumGiftCardsPerOrderReached(cart))
		{
			return getErrorResponse(giftCardNumber, null, GIFT_CARD_LIMITREACHED);
		}

		final GiftCardModel giftCardModel = addGiftCard(giftCardNumber, pinNumber, cart);
		if (null == giftCardModel)
		{
			return getErrorResponse(giftCardNumber, null, GIFT_CARD_VALIDATIONISSUE);
		}
		if (isZeroBalanceGiftCard(giftCardModel.getCurrentBalance()))
		{
			return getErrorResponse(giftCardNumber, null, GIFT_CARD_ZEROBALANCE);
		}
		final GiftCardResponseData giftCardData = new GiftCardResponseData();
		giftCardData.setGiftCardNumber(giftCardNumber);
		giftCardData.setSuccess(true);
		try
		{
			calculateTotalsForGiftCard(cart, giftCardModel);
			final Double giftCardBalance = getGiftCardBalance(giftCardNumber, pinNumber, Objects.nonNull(cart.getCurrency()) ? cart.getCurrency().getIsocode() : "GBP");
			final PriceData priceData = priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(giftCardBalance), cart.getCurrency());
			giftCardData.setGiftCardBalance(priceData.getFormattedValue());
			giftCardData.setGiftCardAppliedAmount(giftCardModel.getAmountAppliedForOrder());

			return giftCardData;
		}
		catch (final GiftCardException gce)
		{
			giftCardData.setSuccess(false);
			giftCardData.setErrorMessage(gce.getMessage());
			return giftCardData;
		}
	}

	private GiftCardResponseData getErrorResponse(final String giftCardNumber, final Double giftCardBalance, final String errorMessage)
	{
		final GiftCardResponseData giftCardResponseData = new GiftCardResponseData();
		giftCardResponseData.setGiftCardNumber(giftCardNumber);
		giftCardResponseData.setGiftCardBalance(String.valueOf(giftCardBalance));
		giftCardResponseData.setSuccess(false);
		giftCardResponseData.setErrorMessage(errorMessage);
		return giftCardResponseData;
	}

	@Override
	public GiftCardModel addGiftCard(final String giftCardNumber, final String pinNumber, final CartModel cart)
	{
		final BalanceInquiryResponse response = svsService.checkBalance(giftCardNumber, pinNumber, null, cart.getCurrency().getIsocode());
		if (response != null && (SvsService.APPROVED.equalsIgnoreCase(response.getReturnCode().getReturnCode()) || SvsService.INSUFFICIENT_FUNDS.equalsIgnoreCase(response.getReturnCode().getReturnCode())) && response.getBalanceAmount().getAmount() >= 0)
		{
			final GiftCardModel giftCardModel = modelService.create(GiftCardModel.class);
			giftCardModel.setGiftCardNumber(giftCardNumber.trim());
			giftCardModel.setPinNumber(pinNumber);
			giftCardModel.setCurrentBalance(response.getBalanceAmount().getAmount());
			giftCardModel.setTransactionID(response.getTransactionID());
			if (isZeroBalanceGiftCard(giftCardModel.getCurrentBalance()))
			{
				return giftCardModel;
			}
			final List<AbstractOrderModel> orders = new ArrayList<>();
			orders.add(cart);
			giftCardModel.setOrders(orders);

			modelService.save(giftCardModel);
			return giftCardModel;
		}
		return null;
	}

	private boolean isZeroBalanceGiftCard(final double balanceAmount)
	{
		return BigDecimal.valueOf(balanceAmount).compareTo(BigDecimal.ZERO) == 0;
	}

	@Override
	public boolean cartHasGiftCard(final CartModel cart)
	{
		return CollectionUtils.isNotEmpty(cart.getGiftCards());
	}

	@Override
	public String authorise(final String giftCardNumber, final String pinNumber, final CartModel cartModel, final Double amountAppliedForOrder, final String isocode)
	{
		return svsService.authorise(giftCardNumber, pinNumber, cartModel, amountAppliedForOrder, isocode);
	}

	@Override
	public boolean reverseAuthorise(final GiftCardModel giftCard, final AbstractOrderModel orderModel)
	{
		final boolean useGiftCardStub = configurationService.getConfiguration().getBoolean("gift.card.stub.enabled", false);
		if (useGiftCardStub)
		{
			return true;
		}
		else
		{
			return svsService.reverseAuthorise(giftCard, orderModel);
		}
	}

	@Override
	public boolean cartContainsGiftCard(final String giftCardNumber, final CartModel cart)
	{
		final Set<GiftCardModel> giftCards = cart.getGiftCards();

		if (CollectionUtils.isEmpty(giftCards))
		{
			return false;
		}

		for (final GiftCardModel cards : giftCards)
		{
			if (StringUtils.equalsIgnoreCase(giftCardNumber, cards.getGiftCardNumber()))
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean updateGiftCardsAmounts(final CartModel cartModel)
	{
		final Set<GiftCardModel> giftCards = new HashSet<>(cartModel.getGiftCards());
		final Set<GiftCardModel> newGiftCards = new HashSet<>();
		modelService.refresh(cartModel);
		cartModel.setGiftCards(null);
		int numGiftCardsApplied = 0;

		if (CollectionUtils.isNotEmpty(giftCards))
		{
			for (final GiftCardModel card : giftCards)
			{
				GiftCardModel giftCard;
				try
				{
					giftCard = calculateTotalsForGiftCard(cartModel, card);
				}
				catch (final GiftCardException e)
				{
					// Gift Card is null, skip this card
					LOG.error("There's an error reapplying the gift card applied previously in the cart", e);
					continue;
				}

				if (checkIfCardApplied(giftCard))
				{
					newGiftCards.add(giftCard);
					cartModel.setGiftCards(newGiftCards);
					numGiftCardsApplied++;
				}
			}
		}
		modelService.save(cartModel);
		if (giftCards.isEmpty())
		{
			return true;
		}
		else
		{
			return numGiftCardsApplied > 0;
		}
	}

	private boolean checkIfCardApplied(final GiftCardModel giftCard)
	{
		return giftCard.getAmountAppliedForOrder() != null && giftCard.getAmountAppliedForOrder() > 0.0;
	}

	private boolean checkIfInsufficientBalance(final GiftCardModel giftCard)
	{
		return (giftCard.getAmountAppliedForOrder() != null && 0.0 == giftCard.getAmountAppliedForOrder()) && giftCard.getCurrentBalance() > 0.0;
	}

	@Override
	public void reapplyGiftCardsInTheCart(final CartModel cart)
	{

		if (CollectionUtils.isNotEmpty(cart.getGiftCards()))
		{
			for (final GiftCardModel giftCard : cart.getGiftCards())
			{
				if (giftCard.getCurrentBalance() > 0)
				{
					try
					{
						calculateTotalsForGiftCard(cart, giftCard);
					}
					catch (final GiftCardException e)
					{
						// Gift card is null so it cannot be applied, skip to the next one
						LOG.error("There's an error reapplying the gift card applied previously in the cart", e);
						continue;
					}
					// Update the gift card in the cart
					giftCard.setCurrentBalance(giftCard.getCurrentBalance());
					giftCard.setAmountAppliedForOrder(giftCard.getAmountAppliedForOrder());
					modelService.save(giftCard);
				}
			}
		}
	}

	@Override
	public void removeAllGiftCard(final CartModel cart)
	{
		cart.setGiftCards(null);
		modelService.save(cart);
	}

	@Override
	public void removeGiftCard(final String giftCardNumber, final CartModel cart)
	{
		final Set<GiftCardModel> giftCardModelList = new HashSet<>();
		final Set<GiftCardModel> giftCards = cart.getGiftCards();

		if (CollectionUtils.isNotEmpty(giftCards))
		{
			Double cartTotal = (Boolean.TRUE.equals(cart.getNet())) ? cart.getTotalPrice() + cart.getTotalTax() : cart.getTotalPrice();
			for (final GiftCardModel giftCardModel : giftCards)
			{
				if (!giftCardModel.getGiftCardNumber().equalsIgnoreCase(giftCardNumber))
				{
					if (giftCardModel.getCurrentBalance() >= cartTotal)
					{
						giftCardModel.setAmountAppliedForOrder(cartTotal);
						cartTotal = Double.valueOf(0.0);
					}
					else
					{
						giftCardModel.setAmountAppliedForOrder(giftCardModel.getCurrentBalance());
						cartTotal = cartTotal - giftCardModel.getCurrentBalance();
					}
					modelService.save(giftCardModel);
					giftCardModelList.add(giftCardModel);
				}
			}
		}

		cart.setGiftCards(giftCardModelList);
		modelService.save(cart);
	}

	@Override
	public GiftCardModel calculateTotalsForGiftCard(final CartModel cart, final GiftCardModel giftCardModel) throws GiftCardException
	{
		if (giftCardModel == null)
		{
			throw new GiftCardException();
		}

		double tax = 0d;
		if (BooleanUtils.isTrue(cart.getNet()))
		{
			tax = cart.getTotalTax() != null ? cart.getTotalTax() : 0;
		}

		double totalPrice = (new BigDecimal(cart.getTotalPrice()).add(new BigDecimal(tax))).doubleValue();

		// Remove the cost of any gift cards which have already applied
		final double giftCardsAmount = cart.getAmountGiftCards();
		final double loyaltyoVuchersAmount = cart.getLoyaltyVoucherDiscount();
		totalPrice = ((new BigDecimal(totalPrice)).subtract(new BigDecimal(giftCardsAmount + loyaltyoVuchersAmount))).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();

		if (giftCardModel.getCurrentBalance() > 0)
		{
			if (totalPrice < giftCardModel.getCurrentBalance())
			{
				giftCardModel.setAmountAppliedForOrder(totalPrice);
			}
			else
			{
				giftCardModel.setAmountAppliedForOrder(giftCardModel.getCurrentBalance());
			}
		}
		else
		{
			giftCardModel.setAmountAppliedForOrder(0.0);
		}

		modelService.save(giftCardModel);

		return giftCardModel;
	}

	@Override
	public void createGiftcardPaymentInfo(final CartModel cart)
	{
		if (cart.getPaymentInfo() == null && CollectionUtils.isNotEmpty(cart.getGiftCards()) && cart.getAmountGiftCards().doubleValue() == calculateTotalWithTax(cart))
		{
			final GiftCardPaymentInfoModel giftcardPaymentInfo = modelService.create(GiftCardPaymentInfoModel.class);
			giftcardPaymentInfo.setCode(cart.getCode());
			giftcardPaymentInfo.setUser(cart.getUser());
			cart.setPaymentInfo(giftcardPaymentInfo);
			modelService.save(giftcardPaymentInfo);
			modelService.save(cart);
		}
	}

	private double calculateTotalWithTax(final CartModel cart)
	{
		if (cart == null)
		{
			throw new IllegalArgumentException("source order must not be null");
		}
		double totalPrice = cart.getTotalPrice() == null ? 0.0d : cart.getTotalPrice().doubleValue();

		// Add the taxes to the total price if the cart is net; if the total was null taxes should be null as well
		if (Boolean.TRUE.equals(cart.getNet()) && totalPrice != 0.0d)
		{
			totalPrice += cart.getTotalTax() == null ? 0.0d : cart.getTotalTax().doubleValue();
		}
		totalPrice = totalPrice - cart.getLoyaltyVoucherDiscount();
		return round(BigDecimal.valueOf(totalPrice)).doubleValue();
	}

	private BigDecimal round(final BigDecimal amount)
	{
		return amount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	}

	@Override
	public boolean verifyMaximumGiftCardsPerOrderReached(final CartModel cart)
	{
		boolean maximumGiftCardsReached = false;

		final BaseStoreModel currentBaseStore = baseStoreService.getCurrentBaseStore();
		final int maxGiftCardNumberAllowed = currentBaseStore.getMaximumSvsGiftCardPerOrder();
		int giftCardsInCart = 0;

		if (CollectionUtils.isNotEmpty(cart.getGiftCards()))
		{
			for (final GiftCardModel card : cart.getGiftCards())
			{
				// If the card has no money on it we do not want to consider it as a used card
				if (card.getAmountAppliedForOrder() != null && card.getAmountAppliedForOrder() > 0.00)
				{
					giftCardsInCart++;
				}
			}
		}

		if (maxGiftCardNumberAllowed <= giftCardsInCart)
		{
			maximumGiftCardsReached = true;
		}

		return maximumGiftCardsReached;
	}
}
