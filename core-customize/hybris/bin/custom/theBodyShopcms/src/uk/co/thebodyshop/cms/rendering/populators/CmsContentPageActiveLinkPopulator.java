/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.cms.rendering.populators;

import java.util.Map;

import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import uk.co.thebodyshop.cms.services.TbsCMSContentPageService;


public class CmsContentPageActiveLinkPopulator implements Populator<ItemModel, Map<String, Object>>
{
	private static final String ACTIVE_LINK_FLAG = "activeLink";

	private TbsCMSContentPageService tbsCMSContentPageService;

	@Override
	public void populate(final ItemModel source, final Map<String, Object> target) throws ConversionException
	{
		if (target != null && source instanceof ContentPageModel) {
			final ContentPageModel page = (ContentPageModel) source;
			target.putIfAbsent(ACTIVE_LINK_FLAG, tbsCMSContentPageService.isActiveLinkAvailableforContentPage(page));
		}

	}

	protected TbsCMSContentPageService getTbsCMSContentPageService()
	{
		return tbsCMSContentPageService;
	}

	public void setTbsCMSContentPageService(TbsCMSContentPageService tbsCMSContentPageService)
	{
		this.tbsCMSContentPageService = tbsCMSContentPageService;
	}

}
