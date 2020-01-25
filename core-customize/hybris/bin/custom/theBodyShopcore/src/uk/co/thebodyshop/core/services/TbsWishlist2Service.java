/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.wishlist2.Wishlist2Service;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;

import java.util.List;

/**
 * @author Jagadeesh
 */
public interface TbsWishlist2Service extends Wishlist2Service
{
	/**
	 * This method returns Wishlist2Model by wishlist id and current user
	 *
	 * @param id
	 * @return Wishlist2Model
	 */
	public Wishlist2Model getWishlistByIdAndUser(final String id);

	/**
	 * This method used to save the wishlist
	 *
	 * @param wishlist2Model
	 */
	public void saveWishlistModel(final Wishlist2Model wishlist2Model);

	/**
	 * This method used to remove the wishlist
	 *
	 * @param wishlist2Model
	 */
	public void removeWishlistModel(final Wishlist2Model wishlist2Model);

	/**
	 * This method used to check the whether we already have product in wishlist entry
	 *
	 * @param wishlist2Model
	 * @param productModel
	 * @return boolean
	 */
	public boolean checkWishlistEntryWithProdct(final Wishlist2Model wishlist2Model, final ProductModel productModel);

	/**
	 * This method returns Wishlist2Model by wishlist id
	 *
	 * @param id
	 * @return Wishlist2Model
	 */
	public Wishlist2Model getWishlistById(final String id);

	/**
	 * This method used to save wishlist to current user
	 * 
	 * @param wishlist2Model
	 */
	public void saveWishlistToCurrentUser(final Wishlist2Model wishlist2Model);

	List<Wishlist2EntryModel> getWishlistEntriesForPhaseOutProduct(TbsVariantProductModel phaseOutProduct);
}
