/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.util.GuidKeyGenerator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import uk.co.thebodyshop.core.daos.TbsWishlist2Dao;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;

/**
 * @author Jagadeesh
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultTbsWishlist2ServiceTest
{

	private static final String WISHLIST_NAME = "testWishlist";

	private static final String WISHLIST_ID = "testWishlistID";

	@Mock
	private ModelService modelService;

	@Mock
	private UserModel userModel;

	@Mock
	private TbsWishlist2Dao tbsWishlistDao;

	@Mock
	private GuidKeyGenerator guidKeyGenerator;

	@Mock
	private UserService userService;

	@Mock
	private Wishlist2Model wishlist2Model;

	@Mock
	private ProductModel productModel;

	@Mock
	private List<Wishlist2EntryModel> entries;

	@Mock
	private List<ProductModel> products;

	@Mock
	private Wishlist2EntryModel entry;

	@InjectMocks
	private DefaultTbsWishlist2Service defaultTbsWishlist2Service;

	@Mock
	private List<Wishlist2EntryModel> wishlist2EntryModels;

	@Mock
	private TbsVariantProductModel phaseOutProduct;


	@Before
	public void setUp()
	{
		when(guidKeyGenerator.generate()).thenReturn(new String(WISHLIST_ID));
		when(wishlist2Model.getId()).thenReturn(new String(WISHLIST_ID));
		when(wishlist2Model.getEntries()).thenReturn(entries);
		when(entry.getProduct()).thenReturn(productModel);
		when(wishlist2Model.getName()).thenReturn(new String(WISHLIST_NAME));
		when(userService.getCurrentUser()).thenReturn(userModel);
		when(tbsWishlistDao.findDefaultWishlist(userModel)).thenReturn(null);
		doNothing().when(modelService).save(any(Wishlist2Model.class));
		phaseOutProduct = new TbsVariantProductModel();
	}

	@Test
	public void createDefaultWishlistTest()
	{
		final Wishlist2Model foundWishlist = defaultTbsWishlist2Service.createDefaultWishlist(userModel, WISHLIST_NAME, "");
		assertEquals("wishlist should be the same", foundWishlist.getName(), WISHLIST_NAME);
	}

	@Test
	public void createWishlistTest()
	{
		final Wishlist2Model foundWishlist = defaultTbsWishlist2Service.createWishlist(userModel, WISHLIST_NAME, "");
		assertEquals("wishlist should be the same", foundWishlist.getName(), WISHLIST_NAME);
	}

	@Test
	public void getWishlistByIdTest()
	{
		when(tbsWishlistDao.findWishlistById(WISHLIST_ID)).thenReturn(wishlist2Model);
		final Wishlist2Model foundWishlist = defaultTbsWishlist2Service.getWishlistById(WISHLIST_ID);
		assertEquals("wishlist should be the same", foundWishlist.getName(), WISHLIST_NAME);
	}

	@Test
	public void getWishlistByIdAndUserTest()
	{
		when(tbsWishlistDao.findWishlistByUserAndId(userModel, WISHLIST_ID)).thenReturn(wishlist2Model);
		final Wishlist2Model foundWishlist = defaultTbsWishlist2Service.getWishlistByIdAndUser(WISHLIST_ID);
		assertEquals("wishlist should be the same", foundWishlist.getName(), WISHLIST_NAME);
	}

	@Test
	public void replacePhaseInWithPhaseOutInWishListFailed()
	{
		given(tbsWishlistDao.getWishlistEntriesForPhaseOutProduct(null)).willReturn(Collections.emptyList());
		final List<Wishlist2EntryModel> wishllistEntries = defaultTbsWishlist2Service.getWishlistEntriesForPhaseOutProduct(null);
		assertThat(wishllistEntries.size()==0).isTrue();
	}

	@Test
	public void replacePhaseInWithPhaseOutInWishListSuccess()
	{
		given(wishlist2EntryModels.size()).willReturn(1);
		given(tbsWishlistDao.getWishlistEntriesForPhaseOutProduct(phaseOutProduct)).willReturn(wishlist2EntryModels);
		final List<Wishlist2EntryModel> wishllistEntries = defaultTbsWishlist2Service.getWishlistEntriesForPhaseOutProduct(phaseOutProduct);
		assertThat(wishllistEntries.size() == 1).isTrue();
	}
}
