/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.wishlist;

import de.hybris.platform.catalog.model.CatalogVersionModel;

import java.util.List;

/**
 * @author Jagadeesh
 */
public interface WishlistFacade
{
	/**
	 * This method used to create the wishlist by given name
	 *
	 * @param wishlistName
	 * @return WishlistData
	 */
	public WishlistData createWishlist(final String wishlistName);

	/**
	 * This method used to update th wishlist name by id
	 *
	 * @param wishlistId
	 * @param wishlistName
	 * @return WishlistData
	 */
	public WishlistData updateWishlistById(final String wishlistId, final String wishlistName);

	/**
	 * This method used to get all the wishlists of loged in user
	 *
	 * @return WishlistCollectionData
	 */
	public WishlistCollectionData getAllWishlists();

	/**
	 * This method used add product entry to wishlist
	 *
	 * @param wishlistId
	 * @param productCode
	 * @return WishlistData
	 */
	public WishlistData addWishListEntryWithProduct(final String wishlistId, final String productCode);

	/**
	 * This method used to get the wishlist by id
	 *
	 * @param wishlistId
	 * @return WishlistData
	 */
	public WishlistData getWishlistById(final String wishlistId);

	/**
	 * This method used to remove the wishlist by id
	 *
	 * @param wishlistId
	 * @return WishlistResponseData
	 */
	public StatusResponseData removeWishlistById(final String wishlistId);

	/**
	 * This method used to remove the product entry from wishlist
	 *
	 * @param wishlistId
	 * @param productCode
	 * @return WishlistData
	 */
	public WishlistData removeWishlistEntryByProduct(final String wishlistId, final String productCode);

	/**
	 * This method used to share the wishlist by id
	 *
	 * @param wishlistShareRequestData
	 * @return WishlistResponseData
	 */
	public StatusResponseData shareWishlist(final WishlistShareRequestData wishlistShareRequestData);

	/**
	 * This method used to save wishlist to current user
	 *
	 * @param wishlistId
	 * @return
	 */
	public StatusResponseData saveWishlistToCurrentUser(final String wishlistId);


	/**
	 * This method used to get Guest wishlist product datas
	 *
	 * @param productCodes
	 * @return GuestWishlistProductData
	 */
	GuestWishlistProductData getGuestWishlistProductData(List<String> productCodes, CatalogVersionModel catalogVersion);
}
