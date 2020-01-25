/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services.impl;

import static uk.co.thebodyshop.core.constants.TheBodyShopCoreConstants.CATEGORY_FIELD_SEARCH;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.impl.DefaultCategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;

import uk.co.thebodyshop.core.services.TbsCategoryService;

public class DefaultTbsCategoryService extends DefaultCategoryService implements TbsCategoryService {

	@Override
	public CategoryModel getCategoryForSolrQuery(final SolrSearchQueryData searchQueryData) {
		CategoryModel result = null;
		if (searchQueryData.getFilterTerms() != null && searchQueryData.getFilterTerms().size() > 0 )
		{
			final Optional<String> categoryCodeOpt = searchQueryData.getFilterTerms().stream().filter(solrSearchQueryTermData -> StringUtils.equals(solrSearchQueryTermData.getKey(),
					CATEGORY_FIELD_SEARCH)).map(SolrSearchQueryTermData::getValue).findFirst();
			if(categoryCodeOpt.isPresent()) {
				final String categoryCode = categoryCodeOpt.get();
				result = getCategoryForCode(categoryCode);
			}
		}
		return result;
	}

	public List<CategoryModel> getBreadcrumbCategoriesInReverseOrderForCategory(final CategoryModel category)
	{
		final List<CategoryModel> categories = getSupercategoriesForCategory(category, new LinkedList<CategoryModel>());
		Collections.reverse(categories);
		return categories;
	}

	private List<CategoryModel> getSupercategoriesForCategory(final CategoryModel currentCategory, final LinkedList<CategoryModel> categories)
	{
		if (!(currentCategory instanceof ClassificationClassModel))
		{
			categories.add(currentCategory);
		}
		if (CollectionUtils.isNotEmpty(currentCategory.getSupercategories()))
		{
			getSupercategoriesForCategory(currentCategory.getSupercategories().iterator().next(), categories);
		}
		return categories;
	}
}
