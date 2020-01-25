/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.search.solrfacetsearch.filter;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.google.common.base.Joiner;

import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;


/**
 * @author vasanthramprakasam
 */
public class UserGroupFilterHandler implements SearchFilterHandler
{

	private static final String PRODUCT_RESTRICTED_GROUP_IDS_FIELD = "productRestrictedGroupIds";

	private final FieldNameProvider fieldNameProvider;

	private final UserService userService;

	public UserGroupFilterHandler(FieldNameProvider fieldNameProvider,
			UserService userService)
	{
		this.fieldNameProvider = fieldNameProvider;
		this.userService = userService;
	}

	@Override
	public void addFilterQuery(SearchQuery searchQuery, IndexedType indexedType)
	{
		final Map<String, IndexedProperty> indexedProperties = indexedType.getIndexedProperties();
		final Set<String> userGroupIds = fetchUserGroupIds();
		if (CollectionUtils.isNotEmpty(userGroupIds))
		{
			final Optional<IndexedProperty> productRestrictedGroupIdProperty = Optional.ofNullable(indexedProperties.get(
					PRODUCT_RESTRICTED_GROUP_IDS_FIELD));
			if (productRestrictedGroupIdProperty.isPresent())
			{
				userGroupIds.add("NO_RESTRICTED_GROUP");
				final String userGroupIdCondition = "(" + Joiner.on(" OR ").join(userGroupIds) + ")";
				searchQuery.addFilterRawQuery(getFieldNameProvider().getFieldName(productRestrictedGroupIdProperty.get(), null, FieldNameProvider.FieldType.INDEX) + ":" + userGroupIdCondition);
			}
		}
	}

	@Override
	public boolean canAddFilter(SearchQuery searchQuery, IndexedType indexedType)
	{
		return !indexedType.getComposedType().getCode().equals(ContentPageModel._TYPECODE);
	}

	private Set<String> fetchUserGroupIds()
	{
		final UserModel user = getUserService().getCurrentUser();
		final Set<String> userGroupIdSet = new HashSet<>();
		if (CollectionUtils.isEmpty(user.getAllGroups()))
		{
			return null;
		}
		if (CollectionUtils.isNotEmpty(user.getAllGroups()))
		{
			for (final PrincipalModel principal : user.getAllGroups())
			{
				userGroupIdSet.add(principal.getUid());
			}
		}
		userGroupIdSet.add(user.getUid());
		return userGroupIdSet;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	protected FieldNameProvider getFieldNameProvider()
	{
		return fieldNameProvider;
	}
}
