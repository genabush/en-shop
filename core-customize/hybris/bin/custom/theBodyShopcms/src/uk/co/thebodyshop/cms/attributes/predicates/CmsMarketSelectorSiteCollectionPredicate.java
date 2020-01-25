/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package uk.co.thebodyshop.cms.attributes.predicates;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;

import java.util.function.Predicate;


/**
 *
 */
public class CmsMarketSelectorSiteCollectionPredicate implements Predicate<AttributeDescriptorModel>
{

	@Override
	public boolean test(final AttributeDescriptorModel attributeDescriptor)
	{
		return "markets".equals(attributeDescriptor.getQualifier())
				&& "MarketSelectorCMSComponent".equals(attributeDescriptor.getEnclosingType().getCode());
	}


}
