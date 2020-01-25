/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.deliveryrestrictions.handlers.impl;

import org.apache.commons.lang.StringUtils;

import uk.co.thebodyshop.core.deliveryrestrictions.handlers.FieldRestrictionsHandler;
import uk.co.thebodyshop.core.model.deliveryrestrictions.DeliveryRestrictionModel;

/**
 * @author prateek.goel
 */
public abstract class AbstractFieldDeliveryRestrictionHandler<T extends DeliveryRestrictionModel> implements FieldRestrictionsHandler<T>
{

	protected boolean equalsTrimIgnoreCase(final String s1, final String s2)
	{
		return StringUtils.equalsIgnoreCase(StringUtils.trimToEmpty(s1), StringUtils.trimToEmpty(s2));
	}
}
