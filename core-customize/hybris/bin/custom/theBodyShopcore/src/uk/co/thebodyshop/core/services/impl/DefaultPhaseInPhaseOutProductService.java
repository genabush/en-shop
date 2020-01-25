/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services.impl;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;

import uk.co.thebodyshop.core.model.EmailWhenInStockModel;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;
import uk.co.thebodyshop.core.services.EmailWhenInStockService;
import uk.co.thebodyshop.core.services.PhaseInPhaseOutProductService;
import uk.co.thebodyshop.core.services.TbsWishlist2Service;

/**
 * @author Balakrishna
 **/
public class DefaultPhaseInPhaseOutProductService implements PhaseInPhaseOutProductService
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultPhaseInPhaseOutProductService.class);

	private final ModelService modelService;

	private final EmailWhenInStockService emailWhenInStockService;

	private final TbsWishlist2Service tbsWishlistService;

	private final SearchRestrictionService searchRestrictionService;

	public DefaultPhaseInPhaseOutProductService(final ModelService modelService, final EmailWhenInStockService emailWhenInStockService, final TbsWishlist2Service tbsWishlistService, final SearchRestrictionService searchRestrictionService)
	{
		this.modelService = modelService;
		this.emailWhenInStockService = emailWhenInStockService;
		this.tbsWishlistService = tbsWishlistService;
		this.searchRestrictionService = searchRestrictionService;
	}

	@Override
	public boolean changePhaseInOutProductStatus(final TbsVariantProductModel phaseOutProduct)
	{
		if (phaseOutProduct.getPhaseInProduct() != null)
		{
			final TbsVariantProductModel phaseInProduct = phaseOutProduct.getPhaseInProduct();

			if (ArticleApprovalStatus.READYFORPIPO.equals(phaseInProduct.getApprovalStatus()))
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Updating phase out product [{}] and phase in product [{}]", phaseOutProduct.getCode(), phaseInProduct.getCode());
				}

				phaseOutProduct.setApprovalStatus(ArticleApprovalStatus.DISCONTINUED);
				phaseInProduct.setApprovalStatus(ArticleApprovalStatus.APPROVED);

				getModelService().save(phaseOutProduct);
				getModelService().save(phaseInProduct);

				return true;
			}
		}

		return false;
	}

	@Override
	public void emailStockNotificationForPhaseInOutProduct(final TbsVariantProductModel phaseOutProduct, final CMSSiteModel cmsSite)
	{
		final TbsVariantProductModel phaseInProduct = phaseOutProduct.getPhaseInProduct();

		if (phaseOutProduct.getPhaseInProduct() != null)
		{
			final Collection<EmailWhenInStockModel> emailWhenInStockRecords = getEmailWhenInStockService().findEmailWhenInStockRecords(phaseOutProduct.getCode(), cmsSite);

			if (CollectionUtils.isNotEmpty(emailWhenInStockRecords))
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Updating [{}] email records for the phase out product [{}]", emailWhenInStockRecords.size(), phaseOutProduct.getCode());
				}

				for (final EmailWhenInStockModel record : emailWhenInStockRecords)
				{
					record.setProductCode(phaseInProduct.getCode());
				}
				getModelService().saveAll(emailWhenInStockRecords);
			}
		}
	}

	@Override
	public void replacePhaseOutWithPhaseInProductsInWishList(final TbsVariantProductModel phaseOutProduct)
	{
		final TbsVariantProductModel phaseInProduct = phaseOutProduct.getPhaseInProduct();

		if (phaseOutProduct.getPhaseInProduct() != null)
		{
			getSearchRestrictionService().disableSearchRestrictions();

			final List<Wishlist2EntryModel> entries = getTbsWishlistService().getWishlistEntriesForPhaseOutProduct(phaseOutProduct);

			if (LOG.isDebugEnabled())
			{
				LOG.debug("Updating [{}] wishlist entries for the phase out product [{}]", entries.size(), phaseOutProduct.getCode());
			}

			entries.stream().forEach(wishlist2Entry -> {
				wishlist2Entry.setProduct(phaseInProduct);
			});

			getModelService().saveAll(entries);

			getSearchRestrictionService().enableSearchRestrictions();
		}
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected EmailWhenInStockService getEmailWhenInStockService()
	{
		return emailWhenInStockService;
	}

	protected TbsWishlist2Service getTbsWishlistService()
	{
		return tbsWishlistService;
	}

	protected SearchRestrictionService getSearchRestrictionService()
	{
		return searchRestrictionService;
	}
}
