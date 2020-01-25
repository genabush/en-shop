/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.deliveryrestrictions.manager.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.variants.model.VariantProductModel;

import uk.co.thebodyshop.core.deliveryrestrictions.error.ProductDeliveryRestrictionError;
import uk.co.thebodyshop.core.deliveryrestrictions.handlers.DeliveryRestrictionHandler;
import uk.co.thebodyshop.core.deliveryrestrictions.manager.RestrictionsManager;
import uk.co.thebodyshop.core.model.deliveryrestrictions.DeliveryModeDeliveryRestrictionModel;
import uk.co.thebodyshop.core.model.deliveryrestrictions.ProductDeliveryRestrictionModel;

/**
 * @author prateek.goel
 */
public class DefaultRestrictionsManager implements RestrictionsManager
{

	private DeliveryRestrictionHandler deliveryModeDeliveryRestrictionHandler;

	private DeliveryRestrictionHandler productDeliveryRestrictionHandler;

	@Override
	public List<DeliveryModeModel> handleDeliveryModeRestrictions(final AddressModel address, final List<DeliveryModeModel> deliveryModes)
	{
		final List<DeliveryModeModel> restrictedDeliveryModes = new ArrayList<>();
		for (final DeliveryModeModel deliveryMode : deliveryModes)
		{
			final Set<DeliveryModeDeliveryRestrictionModel> deliveryRestrictions = deliveryMode.getDeliveryRestrictions();
			if (CollectionUtils.isNotEmpty(deliveryRestrictions) && getDeliveryModeDeliveryRestrictionHandler().isDeliveryModeRestricted(address, deliveryRestrictions))
			{
				restrictedDeliveryModes.add(deliveryMode);
			}
		}
		return restrictedDeliveryModes;
	}

	@Override
	public ProductDeliveryRestrictionError handleProductDeliveryModeRestrictions(final AddressModel address, final CartModel cart)
	{
		final List<String> restrictedProducts = new ArrayList<>();
		final ProductDeliveryRestrictionError productDeliveryRestrictionError = new ProductDeliveryRestrictionError();
		if (null != address)
		{
			for (final AbstractOrderEntryModel entry : cart.getEntries())
			{
				final ProductModel product = entry.getProduct();
				if (product instanceof VariantProductModel)
				{
					final Collection<ProductDeliveryRestrictionModel> restrictions = ((VariantProductModel) product).getDeliveryRestrictions();
					if (CollectionUtils.isNotEmpty(restrictions) && getProductDeliveryRestrictionHandler().isDeliveryModeRestricted(address, restrictions))
					{
						restrictedProducts.add(product.getName());
					}
				}
			}
		}
		productDeliveryRestrictionError.setRestrictedProducts(restrictedProducts);
		return productDeliveryRestrictionError;
	}

	protected DeliveryRestrictionHandler getDeliveryModeDeliveryRestrictionHandler()
	{
		return deliveryModeDeliveryRestrictionHandler;
	}

	public void setDeliveryModeDeliveryRestrictionHandler(final DeliveryRestrictionHandler deliveryModeDeliveryRestrictionHandler)
	{
		this.deliveryModeDeliveryRestrictionHandler = deliveryModeDeliveryRestrictionHandler;
	}

	protected DeliveryRestrictionHandler getProductDeliveryRestrictionHandler()
	{
		return productDeliveryRestrictionHandler;
	}

	public void setProductDeliveryRestrictionHandler(final DeliveryRestrictionHandler productDeliveryRestrictionHandler)
	{
		this.productDeliveryRestrictionHandler = productDeliveryRestrictionHandler;
	}

}
