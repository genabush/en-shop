/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.wishlist.populators;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.converters.ConfigurablePopulator;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import org.apache.commons.collections4.CollectionUtils;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import uk.co.thebodyshop.core.wishlist.WishlistData;

/**
 * @author Jagadeesh
 */
public class TbsWishlistPopulator<SOURCE extends Wishlist2Model, TARGET extends WishlistData> implements Populator<SOURCE, TARGET>
{
	private static final List<ProductOption> WISHLIST_PRODUCT_OPTIONS = new ArrayList<>(Arrays.asList(ProductOption.BASIC,ProductOption.PRICE,ProductOption.PRICE_RANGE,ProductOption.GALLERY,ProductOption.SUMMARY,ProductOption.DESCRIPTION,ProductOption.VARIANT_FULL,ProductOption.PROMOTIONS, ProductOption.STOCK));

	private final Converter<ProductModel, ProductData> productConverter;

	private ConfigurablePopulator<ProductModel, ProductData, ProductOption> productConfiguredPopulator;

	public TbsWishlistPopulator(final Converter<ProductModel, ProductData> productConverter,ConfigurablePopulator<ProductModel, ProductData, ProductOption> productConfiguredPopulator)
	{
		this.productConverter = productConverter;
		this.productConfiguredPopulator=productConfiguredPopulator;
	}

	@Override
	public void populate(final SOURCE source, final TARGET target) throws ConversionException
	{
		target.setWishlistId(source.getId());
		target.setWishlistName(source.getName());
		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
		target.setCreated(dateFormat.format(source.getCreationtime()));
		final List<ProductData> products = source.getEntries().stream().map(entry ->populateProductInfo(entry)).collect(Collectors.toList());
		if (CollectionUtils.isNotEmpty(products))
		{
			target.setProducts(products);
		}
	}

	private  ProductData populateProductInfo(Wishlist2EntryModel entryModel)
	{
		ProductModel productModel=entryModel.getProduct();
		ProductData wishlistproductData = getProductConverter().convert(productModel);
		wishlistproductData.setWishlistCreationDate(entryModel.getCreationtime());
		getProductConfiguredPopulator().populate(productModel, wishlistproductData,WISHLIST_PRODUCT_OPTIONS);
		return wishlistproductData;
	}
	protected Converter<ProductModel, ProductData> getProductConverter()
	{
		return productConverter;
	}

	protected ConfigurablePopulator<ProductModel, ProductData, ProductOption> getProductConfiguredPopulator() {
		return productConfiguredPopulator;
	}
}
