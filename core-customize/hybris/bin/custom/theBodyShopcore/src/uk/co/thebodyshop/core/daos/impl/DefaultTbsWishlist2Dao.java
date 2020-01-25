/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.daos.impl;

import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import org.apache.log4j.Logger;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.wishlist2.impl.daos.impl.DefaultWishlist2Dao;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import uk.co.thebodyshop.core.daos.TbsWishlist2Dao;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;

import java.util.List;

/**
 * @author Jagadeesh
 */
public class DefaultTbsWishlist2Dao extends DefaultWishlist2Dao implements TbsWishlist2Dao
{

	private static final Logger LOG = Logger.getLogger(DefaultTbsWishlist2Dao.class);

	private static final String FIND_ALL_WISHLIST_ENTRIES_BY_PHASE_OUT_PRODUCT_QUERY = "SELECT {"+ Wishlist2EntryModel.PK +"}" + "FROM {"+ Wishlist2EntryModel._TYPECODE +"} WHERE"+"{ "+Wishlist2EntryModel.PRODUCT+" } = ?phaseOutProduct";

	@Override
	public Wishlist2Model findWishlistByUserAndId(final UserModel user, final String id)
	{
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {Wishlist2} WHERE {user} = ?user AND {id} = ?id");
		fQuery.addQueryParameter("user", user);
		fQuery.addQueryParameter("id", id);
		final SearchResult<Wishlist2Model> result = search(fQuery);
		if (result.getCount() > 1)
		{
			LOG.warn("More than one wishlist defined for user " + user.getName() + "with this id. Returning first!");
		}
		return result.getCount() > 0 ? (Wishlist2Model) result.getResult().iterator().next() : null;
	}

	@Override
	public Wishlist2Model findWishlistById(final String id)
	{
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {Wishlist2} WHERE {id} = ?id");
		fQuery.addQueryParameter("id", id);
		final SearchResult<Wishlist2Model> result = search(fQuery);
		if (result.getCount() > 1)
		{
			LOG.warn("More than one wishlist found for id :" + id);
		}
		return result.getCount() > 0 ? (Wishlist2Model) result.getResult().iterator().next() : null;
	}

	@Override
	public List<Wishlist2EntryModel> getWishlistEntriesForPhaseOutProduct(TbsVariantProductModel phaseOutProduct) {
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery ( FIND_ALL_WISHLIST_ENTRIES_BY_PHASE_OUT_PRODUCT_QUERY );
		fQuery.addQueryParameter("phaseOutProduct", phaseOutProduct);
		final SearchResult<Wishlist2EntryModel> result = search ( fQuery );
		return result.getResult ();
	}
}
