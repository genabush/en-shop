/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.category.daos.CategoryDao;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@UnitTest
public class DefaultTbsCategoryServiceTest {

    public static final String KEY_CATEGORY = "category";
    public static final String VALUE_TEST_CATEGORY = "testCategory";
    public static final String KEY_OTHER_FACET = "otherfacet";
    public static final String VALUE_OTHER_FACET = "otherFacet";

    private CategoryDao categoryDao;
    private DefaultTbsCategoryService defaultTbsCategoryService;



    @Before
    public void setUp() {
        categoryDao = mock(CategoryDao.class);
        defaultTbsCategoryService = new DefaultTbsCategoryService();
        defaultTbsCategoryService.setCategoryDao(categoryDao);
    }

    @Test
    public void shouldInvokeGetCategoryCodeCorrectly() {
        SolrSearchQueryData searchQuery = createSearchQueryWithCategory();
        when(categoryDao.findCategoriesByCode(anyString())).thenReturn(getDummyCategories());
        defaultTbsCategoryService.getCategoryForSolrQuery(searchQuery);
        verify(categoryDao, times(1)).findCategoriesByCode(VALUE_TEST_CATEGORY);
        verify(categoryDao, times(0)).findCategoriesByCode(VALUE_OTHER_FACET);
    }

    @Test
    public void shouldNotInvokeGetCategoryCode_NotEmptyTerms() {
        SolrSearchQueryData searchQuery = createSearchQueryWithoutCategory();
        defaultTbsCategoryService.getCategoryForSolrQuery(searchQuery);
        verify(categoryDao, times(0)).findCategoriesByCode(VALUE_OTHER_FACET);
    }

    @Test
    public void shouldNotInvokeGetCategoryCode_EmptyTerms() {
        SolrSearchQueryData searchQuery = new SolrSearchQueryData();
        defaultTbsCategoryService.getCategoryForSolrQuery(searchQuery);
        verify(categoryDao, times(0)).findCategoriesByCode(anyString());
    }

    private SolrSearchQueryData createSearchQueryWithCategory() {
        List<SolrSearchQueryTermData> searchTerms = new ArrayList<>();
        searchTerms.add(createCategorySearchTerm(KEY_CATEGORY, VALUE_TEST_CATEGORY));
        searchTerms.add(createCategorySearchTerm(KEY_OTHER_FACET, VALUE_OTHER_FACET));
        SolrSearchQueryData searchQuery = new SolrSearchQueryData();
        searchQuery.setFilterTerms(searchTerms);
        return searchQuery;
    }

    private SolrSearchQueryData createSearchQueryWithoutCategory() {
        List<SolrSearchQueryTermData> searchTerms = new ArrayList<>();
        searchTerms.add(createCategorySearchTerm(KEY_OTHER_FACET, VALUE_OTHER_FACET));
        SolrSearchQueryData searchQuery = new SolrSearchQueryData();
        searchQuery.setFilterTerms(searchTerms);
        return searchQuery;
    }

    private SolrSearchQueryTermData createCategorySearchTerm(String key, String value) {
        SolrSearchQueryTermData searchTerm = new SolrSearchQueryTermData();
        searchTerm.setKey(key);
        searchTerm.setValue(value);
        return searchTerm;
    }

    private List<CategoryModel> getDummyCategories() {
        List<CategoryModel> categories = new ArrayList<>();
        CategoryModel category = new CategoryModel();
        category.setCode(VALUE_TEST_CATEGORY);
        categories.add(category);
        return categories;
    }

}
