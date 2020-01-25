/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services.impl;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;

import uk.co.thebodyshop.core.daos.EmailWhenInStockDao;
import uk.co.thebodyshop.core.model.EmailWhenInStockModel;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;
import uk.co.thebodyshop.core.services.EmailWhenInStockService;
import uk.co.thebodyshop.core.services.TbsWishlist2Service;

/**
 * @author Balakrishna
 **/
@UnitTest
public class DefaultPhaseInPhaseOutProductServiceTest
{
	@InjectMocks
	private DefaultPhaseInPhaseOutProductService phaseInPhaseOutProductService;

	@Mock
	private ModelService modelService;

	@Mock
	private TbsWishlist2Service tbsWishlistService;

	@Mock
	private SearchRestrictionService searchRestrictionService;

	@Mock
	private EmailWhenInStockService emailWhenInStockService;

	@Mock
	private EmailWhenInStockDao emailWhenInStockDao;

	@Mock
	private TbsVariantProductModel phaseInProduct;

	@Mock
	private TbsVariantProductModel phaseOutProduct;

	@Mock
	private CMSSiteModel cmsSite;

	@Mock
	private EmailWhenInStockModel emailWhenInStock;

	@Mock
	private Wishlist2EntryModel wishlist2EntryModel;

	private Collection<EmailWhenInStockModel> records;

	private List<Wishlist2EntryModel> wishlist2Entries;


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		records = new ArrayList<>();
		records.add(emailWhenInStock);

		wishlist2Entries = new ArrayList<>();
		wishlist2Entries.add(wishlist2EntryModel);

		when(phaseOutProduct.getCode()).thenReturn("phaseOutCode");
		when(phaseOutProduct.getPhaseInProduct()).thenReturn(phaseInProduct);

		when(phaseInProduct.getCode()).thenReturn("phaseInProductCode");
		when(phaseInProduct.getApprovalStatus()).thenReturn(ArticleApprovalStatus.READYFORPIPO);

		when(emailWhenInStockService.findEmailWhenInStockRecords("phaseOutCode", cmsSite)).thenReturn(records);

		when(tbsWishlistService.getWishlistEntriesForPhaseOutProduct(phaseOutProduct)).thenReturn(wishlist2Entries);
	}

	@Test
	public void testWhenPhaseOutProductHasNoPhaseInProduct()
	{
		when(phaseOutProduct.getPhaseInProduct()).thenReturn(null);
		phaseInPhaseOutProductService.changePhaseInOutProductStatus(phaseOutProduct);
		verify(modelService, times(0)).save(phaseOutProduct);
		verify(modelService, times(0)).save(phaseInProduct);
	}

	@Test
	public void testWhenPhaseInProductIsNotReady()
	{
		when(phaseInProduct.getApprovalStatus()).thenReturn(ArticleApprovalStatus.READYTOBELOCALISED);
		phaseInPhaseOutProductService.changePhaseInOutProductStatus(phaseOutProduct);
		verify(modelService, times(0)).save(phaseOutProduct);
		verify(modelService, times(0)).save(phaseInProduct);
	}

	@Test
	public void testStatusChangeForPhaseOutAndInProduct()
	{
		phaseInPhaseOutProductService.changePhaseInOutProductStatus(phaseOutProduct);
		verify(phaseOutProduct).setApprovalStatus(ArticleApprovalStatus.DISCONTINUED);
		verify(phaseInProduct).setApprovalStatus(ArticleApprovalStatus.APPROVED);
		verify(modelService).save(phaseOutProduct);
		verify(modelService).save(phaseInProduct);
	}

	@Test
	public void testNoEmailWhenInStockRecordsFound()
	{
		when(emailWhenInStockService.findEmailWhenInStockRecords("phaseOutCode", cmsSite)).thenReturn(Collections.emptyList());
		phaseInPhaseOutProductService.emailStockNotificationForPhaseInOutProduct(phaseOutProduct, cmsSite);
		verify(modelService, times(0)).saveAll(Collections.emptyList());
	}

	@Test
	public void testNoEmailWhenInStockLookUpForNoPhaseInProduct()
	{
		when(phaseOutProduct.getPhaseInProduct()).thenReturn(null);
		phaseInPhaseOutProductService.emailStockNotificationForPhaseInOutProduct(phaseOutProduct, cmsSite);
		verify(emailWhenInStockService, times(0)).findEmailWhenInStockRecords("phaseOutCode", cmsSite);
	}

	@Test
	public void testEmailWhenInStockRecordUpdate()
	{
		phaseInPhaseOutProductService.emailStockNotificationForPhaseInOutProduct(phaseOutProduct, cmsSite);
		verify(emailWhenInStock).setProductCode("phaseInProductCode");
		verify(modelService).saveAll(records);
	}

	@Test
	public void testNoWishlistEntriesUpdateForNoPhaseInProduct()
	{
		when(phaseOutProduct.getPhaseInProduct()).thenReturn(null);

		phaseInPhaseOutProductService.replacePhaseOutWithPhaseInProductsInWishList(phaseOutProduct);

		verify(searchRestrictionService, times(0)).disableSearchRestrictions();
	}

	@Test
	public void testNoWishlistEntriesUpdateForNoRecordsFound()
	{
		given(tbsWishlistService.getWishlistEntriesForPhaseOutProduct(phaseOutProduct)).willReturn(Collections.emptyList());

		phaseInPhaseOutProductService.replacePhaseOutWithPhaseInProductsInWishList(phaseOutProduct);

		verify(wishlist2EntryModel, times(0)).setProduct(phaseInProduct);
	}

	@Test
	public void testWishlistEntriesFound()
	{
		phaseInPhaseOutProductService.replacePhaseOutWithPhaseInProductsInWishList(phaseOutProduct);

		verify(wishlist2EntryModel).setProduct(phaseInProduct);
	}
}
