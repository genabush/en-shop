/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.cms.services;

import de.hybris.platform.cms2.model.pages.ContentPageModel;


public interface TbsCMSContentPageService
{
	boolean isActiveLinkAvailableforContentPage(ContentPageModel page);
}
