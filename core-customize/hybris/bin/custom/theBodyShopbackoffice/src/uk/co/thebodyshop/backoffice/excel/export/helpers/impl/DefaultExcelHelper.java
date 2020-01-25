/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.backoffice.excel.export.helpers.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.hybris.backoffice.excel.data.SelectedAttribute;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;
import de.hybris.platform.servicelayer.user.UserService;

import uk.co.thebodyshop.backoffice.excel.export.helpers.ExcelHelper;

/**
 * @author Marcin
 */
public class DefaultExcelHelper implements ExcelHelper
{
	private static final String PRODUCTMARKETGROUP = "productmarketgroup";

	private static final String GOBALMARKETGROUP = "globalproductmarketgroup";

	private final ConfigurationService configurationService;

	private final UserService userService;

	private final Map<String, List<String>> userGroupTypePermissionsMap;

	@Override
	public boolean getFullExportFlagForCurrentUser(final UserModel userModel)
	{
		if (userModel instanceof EmployeeModel)
		{
			return ((EmployeeModel) userModel).isFullExportEnabled();
		}
		return true;
	}

	public List<String> getExcAttributeForCurrentUser(final UserModel userModel, List<String> excludedAttributes, final String typeCode)
	{
		final boolean isMarketUser = userService.isMemberOfGroup(userModel, userService.getUserGroupForUID(PRODUCTMARKETGROUP));
		final boolean isGlobalMarketUser = userService.isMemberOfGroup(userModel, userService.getUserGroupForUID(GOBALMARKETGROUP));
		if (isMarketUser || isGlobalMarketUser)
		{
			excludedAttributes = getExcludedAttributesForTypeCode(typeCode);
		}
		return excludedAttributes;
	}

	public List<String> getExcludedAttributesForTypeCode(final String typeCode)
	{
		final String excludedAttributes = configurationService.getConfiguration().getString("backoffice.excel.export.excluded.attributes." + typeCode);
		if (StringUtils.isNotEmpty(excludedAttributes))
		{
			return Arrays.asList(excludedAttributes.split(","));
		}
		return new ArrayList<>();
	}

	public boolean isAllowedAttribute(final AttributeDescriptorModel attrDesc, final List<String> excludedAttributes)
	{
		if (CollectionUtils.isNotEmpty(excludedAttributes))
		{
			if (excludedAttributes.contains(attrDesc.getQualifier()))
			{
				return false;
			}
		}
		return true;
	}

	public void filterMarketUserAttributes(final Map<String, Set<SelectedAttribute>> attributesByType, final String typeCode)
	{
		if (MapUtils.isNotEmpty(attributesByType) && attributesByType.containsKey(typeCode))
		{
			final UserModel userModel = getUserService().getCurrentUser();
			final boolean isFullExportEnabled = getFullExportFlagForCurrentUser(userModel);
			if (!isFullExportEnabled)
			{
				List<String> excludedAttributes = new ArrayList<>();
				excludedAttributes = getExcAttributeForCurrentUser(userModel, excludedAttributes, typeCode);
				if (CollectionUtils.isNotEmpty(excludedAttributes))
				{
					final Set<SelectedAttribute> filteredAttributes = new HashSet<>();
					for (final SelectedAttribute selectedAttribute : attributesByType.get(typeCode))
					{
						if (!excludedAttributes.contains(selectedAttribute.getQualifier()))
						{
							filteredAttributes.add(selectedAttribute);
						}
					}
					attributesByType.put(typeCode, filteredAttributes);
				}
			}
		}
	}

	@Override
	public boolean hasCustomPermissionsForType(final String typeCode)
	{
		final UserModel currentUser = getUserService().getCurrentUser();
		if (getUserService().isAdminEmployee(currentUser))
		{
			return false;
		}
		final Set<String> permittedTypes = getCurrentUserTypePermissionsList(getUserService().getCurrentUser(), getUserGroupTypePermissionsMap());
		if (CollectionUtils.isNotEmpty(permittedTypes) && permittedTypes.contains(typeCode))
		{
				return true;
		}
		return false;
	}

	private Set<String> getCurrentUserTypePermissionsList(final UserModel userModel, final Map<String, List<String>> userGroupPermissions)
	{
		final Set<String> permittedTypes = new HashSet<>();
		final Set<UserGroupModel> currentUserGroups = getUserService().getAllUserGroupsForUser(userModel);
		currentUserGroups.stream().forEach(userGroup -> {
			if (userGroupPermissions.containsKey(userGroup.getUid())) {
				permittedTypes.addAll(userGroupPermissions.get(userGroup.getUid()));
			}
		});
		return permittedTypes;
	}

	public DefaultExcelHelper(final ConfigurationService configurationService, final UserService userService, final PermissionCRUDService permissionCRUDService, final Map<String, List<String>> userGroupTypePermissionsMap)
	{
		this.configurationService = configurationService;
		this.userService = userService;
		this.userGroupTypePermissionsMap = userGroupTypePermissionsMap;
	}

	/**
	 * @return the configurationService
	 */
	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @return the userService
	 */
	protected UserService getUserService()
	{
		return userService;
	}

	/**
	 * @return the userGroupTypePermissionsMap
	 */
	protected Map<String, List<String>> getUserGroupTypePermissionsMap()
	{
		return userGroupTypePermissionsMap;
	}
}
