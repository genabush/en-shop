/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import de.hybris.platform.commerceservices.util.GuidKeyGenerator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.wishlist2.enums.Wishlist2EntryPriority;
import de.hybris.platform.wishlist2.impl.DefaultWishlist2Service;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import uk.co.thebodyshop.core.daos.TbsWishlist2Dao;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;
import uk.co.thebodyshop.core.services.TbsWishlist2Service;

/**
 * @author Jagadeesh
 */
public class DefaultTbsWishlist2Service extends DefaultWishlist2Service implements TbsWishlist2Service
{

	private final GuidKeyGenerator guidKeyGenerator;

	private final TbsWishlist2Dao tbsWishlistDao;

	private final UserService userService;

	private final ModelService modelService;

	public DefaultTbsWishlist2Service(final GuidKeyGenerator guidKeyGenerator, final TbsWishlist2Dao tbsWishlistDao, final UserService userService, final ModelService modelService)
	{
		this.guidKeyGenerator = guidKeyGenerator;
		this.tbsWishlistDao = tbsWishlistDao;
		this.userService = userService;
		this.modelService = modelService;
	}

	@Override
	protected UserModel getCurrentUser()
	{
		return getUserService().getCurrentUser();
	}

	@Override
	public boolean hasDefaultWishlist()
	{
		return hasDefaultWishlist(this.getCurrentUser());
	}

	@Override
	public boolean hasDefaultWishlist(final UserModel user)
	{
		return getTbsWishlistDao().findDefaultWishlist(user) != null;
	}

	@Override
	public Wishlist2Model createDefaultWishlist(final UserModel user, final String name, final String description)
	{
		if (hasDefaultWishlist())
		{
			throw new SystemException("An default wishlist for the user <" + user.getName() + "> already exists");
		}
		else
		{
			return createWishlistWithId(user, name, description, Boolean.TRUE);
		}
	}

	@Override
	public Wishlist2Model createWishlist(final UserModel user, final String name, final String description)
	{
		return createWishlistWithId(user, name, description, Boolean.FALSE);
	}

	@Override
	public Wishlist2Model getWishlistByIdAndUser(final String id)
	{
		final Wishlist2Model wishlistModel = getTbsWishlistDao().findWishlistByUserAndId(getCurrentUser(), id);
		if (null == wishlistModel)
		{
			throw new SystemException("No wishlist found for the current user with wishlistId:" + id);
		}
		return wishlistModel;
	}

	@Override
	public Wishlist2Model getWishlistById(final String id)
	{
		final Wishlist2Model wishlistModel = getTbsWishlistDao().findWishlistById(id);
		if (null == wishlistModel)
		{
			throw new SystemException("No wishlist found for the current user with wishlistId:" + id);
		}
		return wishlistModel;
	}

	@Override
	public void saveWishlistModel(final Wishlist2Model wishlist2Model)
	{
		getModelService().save(wishlist2Model);
	}

	@Override
	public void removeWishlistModel(final Wishlist2Model wishlist2Model)
	{
		getModelService().remove(wishlist2Model);
	}

	@Override
	public void saveWishlistToCurrentUser(final Wishlist2Model wishlist2Model)
	{
		final Wishlist2Model wishlistCopy = createWishlist(getCurrentUser(), wishlist2Model.getName(), "");
		if (CollectionUtils.isNotEmpty(wishlist2Model.getEntries()))
		{
			final List<ProductModel> products = wishlist2Model.getEntries().stream().map(entry -> entry.getProduct()).collect(Collectors.toList());
			for (final ProductModel productModel : products)
			{
				addWishlistEntry(wishlistCopy, productModel, Integer.valueOf(0), Wishlist2EntryPriority.HIGHEST, "");
			}
		}
	}

	@Override
	public List<Wishlist2EntryModel> getWishlistEntriesForPhaseOutProduct(TbsVariantProductModel phaseOutProduct) {
		return getTbsWishlistDao().getWishlistEntriesForPhaseOutProduct(phaseOutProduct);
	}

	@Override
	public boolean checkWishlistEntryWithProdct(final Wishlist2Model wishlist2Model, final ProductModel productModel)
	{
		final List<Wishlist2EntryModel> entries = getTbsWishlistDao().findWishlistEntryByProduct(productModel, wishlist2Model);
		if (entries.isEmpty())
		{
			return true;
		}
		throw new SystemException("Product with code: " + productModel.getCode() + " is already in wishlist");
	}

	private Wishlist2Model createWishlistWithId(final UserModel user, final String name, final String description, final Boolean defaultWL)
	{
		final Wishlist2Model wishlist = new Wishlist2Model();
		wishlist.setId(getGuidKeyGenerator().generate().toString());
		wishlist.setName(name);
		wishlist.setDescription(description);
		wishlist.setDefault(defaultWL);
		wishlist.setUser(user);
		wishlist.setCreationtime(new Date());
		if (saveWishlist(user))
		{
			getModelService().save(wishlist);
		}
		return wishlist;
	}



	protected GuidKeyGenerator getGuidKeyGenerator()
	{
		return guidKeyGenerator;
	}

	protected TbsWishlist2Dao getTbsWishlistDao()
	{
		return tbsWishlistDao;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	@Override
	protected ModelService getModelService()
	{
		return modelService;
	}
}
