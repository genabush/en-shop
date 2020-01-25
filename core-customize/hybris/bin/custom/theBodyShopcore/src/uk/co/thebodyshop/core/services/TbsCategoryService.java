/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services;

import java.util.List;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;

public interface TbsCategoryService extends CategoryService {

	CategoryModel getCategoryForSolrQuery(SolrSearchQueryData searchQueryData);

	List<CategoryModel> getBreadcrumbCategoriesInReverseOrderForCategory(final CategoryModel category);

}
