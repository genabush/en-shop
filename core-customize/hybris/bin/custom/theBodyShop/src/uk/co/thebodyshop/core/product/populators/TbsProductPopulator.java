/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.product.populators;

import org.apache.commons.lang.BooleanUtils;

import de.hybris.platform.commercefacades.product.converters.populator.ProductUrlPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

/**
 * @author Marcin
 */
public class TbsProductPopulator extends ProductUrlPopulator
{
	private Populator<ProductModel, ProductData> productBasicPopulator;
	private Populator<ProductModel, ProductData> productPrimaryImagePopulator;
	private final BaseStoreService baseStoreService;

	public TbsProductPopulator(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	@Override
	public void populate(final ProductModel source, final ProductData target)
	{
		getProductBasicPopulator().populate(source, target);
		getProductPrimaryImagePopulator().populate(source, target);
		super.populate(source, target);
		final BaseStoreModel baseStoreModel = getBaseStoreService().getCurrentBaseStore();
		final boolean findInstoreEnabled = null != baseStoreModel && BooleanUtils.isTrue(baseStoreModel.getFindInStoreEnabled());
		target.setFindInStoreEnabled(findInstoreEnabled);
	}

	/**
	 * @return the productBasicPopulator
	 */
	public Populator<ProductModel, ProductData> getProductBasicPopulator()
	{
		return productBasicPopulator;
	}

	/**
	 * @param productBasicPopulator
	 *          the productBasicPopulator to set
	 */
	public void setProductBasicPopulator(final Populator<ProductModel, ProductData> productBasicPopulator)
	{
		this.productBasicPopulator = productBasicPopulator;
	}

	/**
	 * @return the productPrimaryImagePopulator
	 */
	public Populator<ProductModel, ProductData> getProductPrimaryImagePopulator()
	{
		return productPrimaryImagePopulator;
	}

	/**
	 * @param productPrimaryImagePopulator
	 *          the productPrimaryImagePopulator to set
	 */
	public void setProductPrimaryImagePopulator(final Populator<ProductModel, ProductData> productPrimaryImagePopulator)
	{
		this.productPrimaryImagePopulator = productPrimaryImagePopulator;
	}

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}
}
