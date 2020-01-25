/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.cms.types.predicates;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;

import java.util.function.Predicate;


public class CmsContentPagePredicate implements Predicate<CMSItemModel>
{
	@Override
	public boolean test(final CMSItemModel cmsItemModel)
	{
		return ContentPageModel.class.isInstance(cmsItemModel);
	}
}
