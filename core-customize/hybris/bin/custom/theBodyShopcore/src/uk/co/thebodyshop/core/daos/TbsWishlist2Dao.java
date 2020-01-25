/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.daos;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.wishlist2.impl.daos.Wishlist2Dao;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;

import java.util.List;

/**
 * @author Jagadeesh
 */
public interface TbsWishlist2Dao extends Wishlist2Dao
{
	/**
	 * This method used to get the wishlist by User and Id
	 *
	 * @param user
	 * @param id
	 * @return Wishlist2Model
	 */
	public Wishlist2Model findWishlistByUserAndId(final UserModel user, final String id);

	/**
	 * This method used to get the wishlist by Id
	 *
	 * @param user
	 * @return Wishlist2Model
	 */
	public Wishlist2Model findWishlistById(final String id);

	/*
	 * This method used to get Wishlist Entries for phaseOut Product
	 * @return List<Wishlist2EntryModel>
	 */
	List<Wishlist2EntryModel> getWishlistEntriesForPhaseOutProduct(TbsVariantProductModel phaseOutProduct);
}
