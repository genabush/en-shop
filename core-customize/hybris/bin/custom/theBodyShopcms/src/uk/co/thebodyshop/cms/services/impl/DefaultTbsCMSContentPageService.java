/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.cms.services.impl;

import static java.util.Arrays.asList;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.data.PagePreviewCriteriaData;
import de.hybris.platform.cms2.enums.CmsApprovalStatus;
import de.hybris.platform.cms2.enums.CmsPageStatus;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.servicelayer.services.impl.DefaultCMSContentPageService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import uk.co.thebodyshop.cms.daos.TbsCMSContentPageDao;
import uk.co.thebodyshop.cms.enums.ContentCatalogCode;
import uk.co.thebodyshop.cms.services.TbsCMSContentPageService;
import uk.co.thebodyshop.core.catalog.services.TbsCatalogVersionService;

/**
 * @author Marcin
 */
public class DefaultTbsCMSContentPageService extends DefaultCMSContentPageService implements TbsCMSContentPageService
{
	private final String pageNotFoundLabel;

	private final TbsCatalogVersionService tbsCatalogVersionService;

	private final TbsCMSContentPageDao tbsCMSContentPageDao;

	@Override
	public ContentPageModel getPageForLabelOrIdAndMatchType(final String labelOrId, final PagePreviewCriteriaData pagePreviewCriteria, final boolean exactLabelMatch) throws CMSItemNotFoundException
	{
		final ContentPageModel contentPage = super.getPageForLabelOrIdAndMatchType(labelOrId, pagePreviewCriteria, exactLabelMatch);
		final CatalogVersionModel currentContentCatalog = getTbsCatalogVersionService().getRequestContentCatalog(super.getSessionContentCatalogVersions());
		if (getTbsCatalogVersionService().isGlobalContentCatalog(currentContentCatalog))
		{
			return contentPage;
		}
		else
		{
			return getPageForMarketCatalog(contentPage, currentContentCatalog, labelOrId, pagePreviewCriteria);
		}
	}

	private ContentPageModel getPageForMarketCatalog(final ContentPageModel contentPage, final CatalogVersionModel contentCatalog, final String label, final PagePreviewCriteriaData pagePreviewCriteria) throws CMSItemNotFoundException
	{
		if (getTbsCatalogVersionService().isGlobalContentCatalog(contentPage.getCatalogVersion()))
		{
			if (contentPage.isAllowedForAllMarkets() || (CollectionUtils.isNotEmpty(contentPage.getAllowedForContentCatalogs()) && contentPage.getAllowedForContentCatalogs().contains(ContentCatalogCode.valueOf(contentCatalog.getCatalog().getId()))))
			{
				return contentPage;
			}
			else
			{
				return super.getPageForLabel(getPageNotFoundLabel(), asList(CmsPageStatus.ACTIVE), true);
			}
		}
		else
		{
			return contentPage;
		}
	}

	@Override
	public boolean isActiveLinkAvailableforContentPage(final ContentPageModel page)
	{
		if (page.getPageStatus() == CmsPageStatus.ACTIVE && page.getApprovalStatus() == CmsApprovalStatus.APPROVED
				&& StringUtils.isNotBlank(page.getLabel())
				&& null != tbsCMSContentPageDao.findActiveCMSLinkComponent(page.getLabel(), page.getCatalogVersion()))
		{
			return true;
		}
		return false;
	}




	@Autowired
	public DefaultTbsCMSContentPageService(final String pageNotFoundLabel, final TbsCatalogVersionService tbsCatalogVersionService,
			final TbsCMSContentPageDao tbsCMSContentPageDao)
	{
		this.pageNotFoundLabel = pageNotFoundLabel;
		this.tbsCatalogVersionService = tbsCatalogVersionService;
		this.tbsCMSContentPageDao = tbsCMSContentPageDao;
	}

	public TbsCMSContentPageDao getTbsCMSContentPageDao()
	{
		return tbsCMSContentPageDao;
	}

	/**
	 * @return the pageNotFoundLabel
	 */
	protected String getPageNotFoundLabel()
	{
		return pageNotFoundLabel;
	}

	/**
	 * @return the tbsCatalogVersionService
	 */
	protected TbsCatalogVersionService getTbsCatalogVersionService()
	{
		return tbsCatalogVersionService;
	}
}
