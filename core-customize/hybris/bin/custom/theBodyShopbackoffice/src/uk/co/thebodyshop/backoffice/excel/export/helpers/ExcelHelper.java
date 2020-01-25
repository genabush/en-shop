/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.backoffice.excel.export.helpers;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hybris.backoffice.excel.data.SelectedAttribute;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.user.UserModel;

/**
 * @author Marcin
 */
public interface ExcelHelper
{
	public boolean getFullExportFlagForCurrentUser(final UserModel userModel);

	public List<String> getExcAttributeForCurrentUser(final UserModel userModel, List<String> excludedAttributes, final String typeCode);

	public List<String> getExcludedAttributesForTypeCode(final String typeCode);

	public boolean isAllowedAttribute(final AttributeDescriptorModel attrDesc, List<String> excludedAttributes);

	public void filterMarketUserAttributes(final Map<String, Set<SelectedAttribute>> attributesByType, final String typeCode);

	public boolean hasCustomPermissionsForType(final String typeCode);
}
