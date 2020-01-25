/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.dynamic.handler;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.variants.model.VariantProductModel;

/**
 * @author Jagadeesh
 */
public class OrderGiftWrapEligibilityAttributeHandler implements DynamicAttributeHandler<Boolean, AbstractOrderModel>
{

	@Override
	public Boolean get(final AbstractOrderModel abstractOrderModel)
	{
		if (Objects.nonNull(abstractOrderModel.getStore()) && CollectionUtils.isNotEmpty(abstractOrderModel.getEntries()))
		{
			final BaseStoreModel store = abstractOrderModel.getStore();
			final List<ProductModel> products = abstractOrderModel.getEntries().stream().map(entry -> entry.getProduct()).filter(product -> !((VariantProductModel) product).getGiftWrap()).collect(Collectors.toList());
			if (Objects.nonNull(store.getGiftWrapProduct()) && BooleanUtils.isTrue(store.getGiftWrapEnabled()) && CollectionUtils.isEmpty(products))
			{
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	@Override
	public void set(final AbstractOrderModel abstractOrderModel, final Boolean eligibleForGiftWrap)
	{

	}

}
