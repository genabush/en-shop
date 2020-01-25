/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.wishlist.impl;


import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.ConfigurablePopulator;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import org.apache.commons.collections4.CollectionUtils;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.wishlist2.enums.Wishlist2EntryPriority;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import uk.co.thebodyshop.core.product.visibility.services.ProductVisibilityService;
import uk.co.thebodyshop.core.services.TbsWishlist2Service;
import uk.co.thebodyshop.core.wishlist.StatusResponseData;
import uk.co.thebodyshop.core.wishlist.WishlistCollectionData;
import uk.co.thebodyshop.core.wishlist.WishlistData;
import uk.co.thebodyshop.core.wishlist.WishlistShareRequestData;
import uk.co.thebodyshop.core.wishlist.WishlistFacade;
import uk.co.thebodyshop.core.wishlist.GuestWishlistProductData;

/**
 * @author Jagadeesh
 */
public class DefaultWishlistFacade implements WishlistFacade
{

	private static final List<ProductOption> GUEST_WISHLIST_PRODUCT_OPTIONS = new ArrayList<>(Arrays.asList(ProductOption.BASIC,ProductOption.PRICE,ProductOption.PRICE_RANGE,ProductOption.GALLERY,ProductOption.SUMMARY,ProductOption.DESCRIPTION,ProductOption.VARIANT_FULL,ProductOption.PROMOTIONS, ProductOption.STOCK));

	private final TbsWishlist2Service tbsWishlistService;

	private final ProductService productService;

	private final Converter<Wishlist2Model, WishlistData> tbsWishlistConverter;

	private Converter<ProductModel, ProductData> productConverter;

	private ProductVisibilityService productVisibilityService;

    private ConfigurablePopulator<ProductModel, ProductData, ProductOption> productConfiguredPopulator;

	public DefaultWishlistFacade(final TbsWishlist2Service tbsWishlistService, final ProductService productService, final Converter<Wishlist2Model, WishlistData> tbsWishlistConverter,Converter<ProductModel, ProductData> productConverter,ProductVisibilityService productVisibilityService,ConfigurablePopulator<ProductModel, ProductData, ProductOption> productConfiguredPopulator)
	{
		this.tbsWishlistService = tbsWishlistService;
		this.productService = productService;
		this.tbsWishlistConverter = tbsWishlistConverter;
		this.productConverter = productConverter;
		this.productVisibilityService = productVisibilityService;
		this.productConfiguredPopulator=productConfiguredPopulator;
	}

	@Override
	public WishlistData createWishlist(final String wishlistName)
	{
		final Wishlist2Model wishlistModel = getTbsWishlistService().createWishlist(wishlistName, "");
		if (null != wishlistModel)
		{
			return populateWishListData(wishlistModel);
		}
		return null;
	}

	@Override
	public WishlistData updateWishlistById(final String wishlistId, final String wishlistName)
	{
		final Wishlist2Model wishlistModel = getTbsWishlistService().getWishlistByIdAndUser(wishlistId);
		wishlistModel.setName(wishlistName);
		getTbsWishlistService().saveWishlistModel(wishlistModel);
		return populateWishListData(wishlistModel);
	}

	@Override
	public WishlistCollectionData getAllWishlists()
	{
		final List<Wishlist2Model> wishLists = getTbsWishlistService().getWishlists();
		final WishlistCollectionData wishlistCollectionData = new WishlistCollectionData();
		if (CollectionUtils.isNotEmpty(wishLists))
		{
			final List<WishlistData> wishlistDatas = wishLists.stream().map(wishlist -> populateWishListData(wishlist)).collect(Collectors.toList());
			if (CollectionUtils.isNotEmpty(wishlistDatas))
			{
				wishlistCollectionData.setWishlists(wishlistDatas);
			}
		}
		return wishlistCollectionData;
	}

	@Override
	public WishlistData addWishListEntryWithProduct(final String wishlistId, final String productCode)
	{
		final Wishlist2Model wishlistModel = getTbsWishlistService().getWishlistByIdAndUser(wishlistId);
		final ProductModel productModel = getProductService().getProductForCode(productCode);
		if (getTbsWishlistService().checkWishlistEntryWithProdct(wishlistModel, productModel))
		{
			getTbsWishlistService().addWishlistEntry(wishlistModel, productModel, Integer.valueOf(0), Wishlist2EntryPriority.HIGHEST, "");
		}
		return populateWishListData(wishlistModel);
	}

	@Override
	public WishlistData getWishlistById(final String wishlistId)
	{
		final Wishlist2Model wishlistModel = getTbsWishlistService().getWishlistById(wishlistId);
		return populateWishListData(wishlistModel);
	}

	@Override
	public StatusResponseData removeWishlistById(final String wishlistId)
	{
		final Wishlist2Model wishlistModel = getTbsWishlistService().getWishlistByIdAndUser(wishlistId);
		final StatusResponseData wishlistResponseData = new StatusResponseData();
		wishlistResponseData.setStatus(null != wishlistModel ? Boolean.TRUE : Boolean.FALSE);
		if (null != wishlistModel)
		{
			getTbsWishlistService().removeWishlistModel(wishlistModel);
		}
		return wishlistResponseData;
	}

	@Override
	public WishlistData removeWishlistEntryByProduct(final String wishlistId, final String productCode)
	{
		final Wishlist2Model wishlistModel = getTbsWishlistService().getWishlistByIdAndUser(wishlistId);
		final ProductModel productModel = getProductService().getProductForCode(productCode);
		getTbsWishlistService().removeWishlistEntryForProduct(productModel, wishlistModel);
		return populateWishListData(wishlistModel);
	}

	@Override
	public StatusResponseData shareWishlist(final WishlistShareRequestData wishlistShareRequestData)
	{
		final StatusResponseData wishlistResponseData = new StatusResponseData();
		wishlistResponseData.setStatus(null != wishlistShareRequestData.getWishlistId() && null != wishlistShareRequestData.getRecipientEmail() ? Boolean.TRUE : Boolean.FALSE);
		return wishlistResponseData;
	}

	@Override
	public StatusResponseData saveWishlistToCurrentUser(final String wishlistId)
	{
		final Wishlist2Model wishlistModel = getTbsWishlistService().getWishlistById(wishlistId);
		final StatusResponseData wishlistResponseData = new StatusResponseData();
		getTbsWishlistService().saveWishlistToCurrentUser(wishlistModel);
		wishlistResponseData.setStatus(null != wishlistModel ? Boolean.TRUE : Boolean.FALSE);
		return wishlistResponseData;
	}

	@Override
	public GuestWishlistProductData getGuestWishlistProductData(List<String> productCodes, CatalogVersionModel catalogVersion)
	{
		ServicesUtil.validateParameterNotNull(productCodes,"product codes should not be empty");
		ServicesUtil.validateParameterNotNull(catalogVersion,"catalogVersion should not be empty");
		Map<Boolean, List<String>> visibilityProductsList = productCodes.stream().collect(Collectors.partitioningBy(code -> !getProductVisibilityService().getVisibiltyInfo(code, catalogVersion).isVisible()));
		List<String> unavailableProducts = visibilityProductsList.getOrDefault(true, Collections.EMPTY_LIST);
		List<String> availableProducts=visibilityProductsList.getOrDefault(false, Collections.EMPTY_LIST);
		List<ProductData> productDataList = availableProducts.stream().map(productCode -> {
            ProductModel productModel = getProductService().getProductForCode(catalogVersion, productCode);
            ProductData productData=getProductConverter().convert(productModel);
            getProductConfiguredPopulator().populate(productModel, productData,GUEST_WISHLIST_PRODUCT_OPTIONS);
            return productData;
        }).collect(Collectors.toList());
		GuestWishlistProductData guestWishlistProductData=new GuestWishlistProductData();
		guestWishlistProductData.setProducts(productDataList);
		guestWishlistProductData.setUnavailableProductCodes(unavailableProducts);
		return guestWishlistProductData;
	}

	private WishlistData populateWishListData(final Wishlist2Model wishlistModel)
	{
		return getTbsWishlistConverter().convert(wishlistModel);
	}

	protected TbsWishlist2Service getTbsWishlistService()
	{
		return tbsWishlistService;
	}

	protected ProductService getProductService()
	{
		return productService;
	}

	protected Converter<Wishlist2Model, WishlistData> getTbsWishlistConverter()
	{
		return tbsWishlistConverter;
	}

	protected Converter<ProductModel, ProductData> getProductConverter() {
		return productConverter;
	}

	protected ProductVisibilityService getProductVisibilityService() {
		return productVisibilityService;
	}

    public ConfigurablePopulator<ProductModel, ProductData, ProductOption> getProductConfiguredPopulator() {
        return productConfiguredPopulator;
    }
}
