/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import de.hybris.platform.commerceservices.price.CommercePriceService;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.variants.model.VariantProductModel;

import uk.co.thebodyshop.core.giftMessage.GiftWrapMessageData;
import uk.co.thebodyshop.core.model.ServiceProductModel;
import uk.co.thebodyshop.core.services.GiftWrapService;

/**
 * @author Jagadeesh
 */
public class DefaultGiftWrapService implements GiftWrapService
{
	private static final Logger LOG = Logger.getLogger(DefaultGiftWrapService.class);

	private final ModelService modelService;

	private final CommercePriceService commercePriceService;

	public DefaultGiftWrapService(final ModelService modelService, final CommercePriceService commercePriceService)
	{
		this.modelService = modelService;
		this.commercePriceService = commercePriceService;
	}

	@Override
	public boolean addGiftWrapToCart(final CartModel cart)
	{
		try
		{
			if (Objects.nonNull(cart) && cart.getEligibleForGiftWrap())
			{
				final BaseStoreModel store = cart.getStore();
				final ServiceProductModel giftWrapProduct = store.getGiftWrapProduct();

				final PriceInformation info = getCommercePriceService().getWebPriceForProduct(giftWrapProduct);
				if (info != null && info.getPrice() != null)
				{
					cart.setGiftWrapPrice(info.getPrice().getValue());
				}

				cart.setGiftWrapProduct(giftWrapProduct);
				cart.setCalculated(Boolean.FALSE);
				getModelService().save(cart);
				return true;
			}
		}
		catch (final Exception e)
		{
			LOG.error("Error occured while saving gift wrap to cart", e);
		}
		return false;
	}

	@Override
	public boolean removeGiftWrapFromCart(final CartModel cart)
	{
		try
		{
			if (Objects.nonNull(cart))
			{
				cart.setGiftWrapProduct(null);
				cart.setGiftWrapPrice(new Double(0));
				cart.setCalculated(Boolean.FALSE);
				getModelService().save(cart);
				return true;
			}
		}
		catch (final Exception e)
		{
			LOG.error("Error occured while removing gift wrap to cart", e);
		}
		return false;
	}

	@Override
	public boolean addGiftMessageToCart(final CartModel cart, final GiftWrapMessageData giftWrapMessageData)
	{
		try
		{
			if (Objects.nonNull(cart))
			{
				cart.setGiftMessageName(giftWrapMessageData.getGiftMessageName());
				cart.setGiftMessageSenderName(giftWrapMessageData.getGiftMessageSenderName());
				cart.setGiftMessage(giftWrapMessageData.getGiftMessage());
				getModelService().save(cart);
				return true;
			}
		}
		catch (final Exception e)
		{
			LOG.error("Error occured while adding/updating gift message to cart", e);
		}
		return false;
	}

	@Override
	public boolean checkProductsOnCartEligibleForGiftWrap(final CartModel cartModel)
	{
		if (CollectionUtils.isNotEmpty(cartModel.getEntries()))
		{
			final List<ProductModel> products = cartModel.getEntries().stream().map(entry -> entry.getProduct()).filter(product -> !((VariantProductModel) product).getGiftWrap()).collect(Collectors.toList());
			if (CollectionUtils.isNotEmpty(products))
			{
				return false;
			}
		}
		return true;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected CommercePriceService getCommercePriceService()
	{
		return commercePriceService;
	}
}
