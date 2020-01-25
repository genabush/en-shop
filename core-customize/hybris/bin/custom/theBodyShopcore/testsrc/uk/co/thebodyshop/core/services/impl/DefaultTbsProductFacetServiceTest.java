/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import org.junit.Before;
import org.junit.Test;
import uk.co.thebodyshop.core.daos.TbsProductFacetDao;
import uk.co.thebodyshop.core.model.FinishFacetModel;
import uk.co.thebodyshop.core.model.TbsProductFacetModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@UnitTest
public class DefaultTbsProductFacetServiceTest {

    private static final String CODE_TEST_FINISH_FACET = "testFinishFacet";

    private TbsProductFacetDao tbsProductFacetDao;
    private DefaultProductFacetService defaultTbsProductFacetService;

    @Before
    public void setUp() {
        tbsProductFacetDao = mock(TbsProductFacetDao.class);
        defaultTbsProductFacetService = new DefaultProductFacetService(tbsProductFacetDao);
    }

    @Test
    public void shouldReturnOneTbsProductFacet() {
        when(tbsProductFacetDao.findFacetsByTypeAndCode(FinishFacetModel._TYPECODE, CODE_TEST_FINISH_FACET)).thenReturn(getDummyFinishFacets());
        TbsProductFacetModel tbsProductFacet = defaultTbsProductFacetService.getFacetByTypeAndCode(FinishFacetModel._TYPECODE, CODE_TEST_FINISH_FACET);
        assertNotNull(tbsProductFacet);
        assertEquals(tbsProductFacet.getCode(), CODE_TEST_FINISH_FACET);
    }

    @Test
    public void shouldReturnNull() {
        when(tbsProductFacetDao.findFacetsByTypeAndCode(anyString(), anyString())).thenReturn(Collections.EMPTY_LIST);
        TbsProductFacetModel tbsProductFacet = defaultTbsProductFacetService.getFacetByTypeAndCode(FinishFacetModel._TYPECODE, CODE_TEST_FINISH_FACET);
        assertNull(tbsProductFacet);
    }

    private List<TbsProductFacetModel> getDummyFinishFacets() {
        List<TbsProductFacetModel> dummyFacets = new ArrayList<>();
        FinishFacetModel finishFacet = new FinishFacetModel();
        finishFacet.setCode(CODE_TEST_FINISH_FACET);
        dummyFacets.add(finishFacet);
        return dummyFacets;
    }

}
