/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.services.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.co.thebodyshop.cms.model.SearchKeywordModel;
import uk.co.thebodyshop.core.daos.TbsSearchKeywordDao;
import uk.co.thebodyshop.core.keywords.data.SearchKeyWordData;

import java.util.Collections;

/**
 * @author vasanthramprakasam
 */
@UnitTest
public class DefaultSearchKeywordServiceTest
{
    public static final String EXPECTED_CODE = "code";
    private DefaultSearchKeywordService searchKeywordService;

    @Mock
    private TbsSearchKeywordDao tbsSearchKeywordDao;

    @Mock
    private Converter<SearchKeywordModel, SearchKeyWordData> searchKeyWordDataConverter;

    @Mock
    private SearchKeywordModel searchKeywordModel;

    @Mock
    private SearchKeyWordData searchKeyWordData;

    @Mock
    private SearchResult searchResult;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        searchKeywordService = new DefaultSearchKeywordService(tbsSearchKeywordDao,searchKeyWordDataConverter);
    }

    @Test
    public void testGetSingleSearchKeyword() throws CMSItemNotFoundException
    {
        when(tbsSearchKeywordDao.findKeywordForCode(EXPECTED_CODE)).thenReturn(searchKeywordModel);
        when(searchKeyWordDataConverter.convert(any(SearchKeywordModel.class))).thenReturn(searchKeyWordData);
        searchKeywordService.getSearchKeyWordForCode(EXPECTED_CODE);
        verify(tbsSearchKeywordDao,times(1)).findKeywordForCode(EXPECTED_CODE);
        verify(searchKeyWordDataConverter,times(1)).convert(searchKeywordModel);
    }

    @Test(expected = CMSItemNotFoundException.class)
    public void testGetNonExistingSingleKeyword() throws CMSItemNotFoundException
    {
        when(tbsSearchKeywordDao.findKeywordForCode(EXPECTED_CODE)).thenThrow(new ModelNotFoundException("mnf"));
        searchKeywordService.getSearchKeyWordForCode(EXPECTED_CODE);
        verify(tbsSearchKeywordDao,times(1)).findKeywordForCode(EXPECTED_CODE);
        verify(searchKeyWordDataConverter,never()).convert(searchKeywordModel);
    }

    @Test
    public void testGetSearchTermsMatchingTerm()
    {
        PageableData pageableData = new PageableData();
        pageableData.setCurrentPage(0);
        pageableData.setPageSize(10);
        when(searchResult.getResult()).thenReturn(Collections.singletonList(searchKeywordModel));
        when(searchResult.getTotalCount()).thenReturn(1);
        when(searchResult.getRequestedCount()).thenReturn(10);
        when(searchResult.getRequestedStart()).thenReturn(0);
        when(tbsSearchKeywordDao.findAllKeywordsMatchingCode(EXPECTED_CODE,pageableData)).thenReturn(searchResult);
        searchKeywordService.getSearchKeywordsMatchingTerm(EXPECTED_CODE,pageableData);
        verify(tbsSearchKeywordDao,times(1)).findAllKeywordsMatchingCode(EXPECTED_CODE,pageableData);
        verify(searchKeyWordDataConverter,times(1)).convert(searchKeywordModel);
    }


}
