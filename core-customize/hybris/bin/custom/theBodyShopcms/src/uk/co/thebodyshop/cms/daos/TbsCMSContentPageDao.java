/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.cms.daos;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;


public interface TbsCMSContentPageDao
{
	CMSLinkComponentModel findActiveCMSLinkComponent(String url, CatalogVersionModel catalogVersion);
}
