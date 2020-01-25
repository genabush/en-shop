/**
 *
 */
package uk.co.thebodyshop.core.marketselector.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.co.thebodyshop.core.daos.TbsMarketSelectorDao;
import uk.co.thebodyshop.core.model.MarketSelectorSiteModel;
import uk.co.thebodyshop.core.services.MarketSelectorService;
import uk.co.thebodyshop.core.services.impl.DefaultMarketSelectorService;


/**
 * @author Marcin
 *
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultTbsMarketSelectorServiceTest
{

	private MarketSelectorService tbsMarketSelectorService;

	@Mock
	private TbsMarketSelectorDao tbsMarketSelectorDao;

	@Before
	public void setUp()
	{
		tbsMarketSelectorService = new DefaultMarketSelectorService(tbsMarketSelectorDao);
	}

	@Test
	public void testServicecallWithEmptyResults()
	{
		final List<MarketSelectorSiteModel> emptyList = new ArrayList<MarketSelectorSiteModel>();
		given(tbsMarketSelectorDao.findAllEnabledSites()).willReturn(emptyList);
		final List<MarketSelectorSiteModel> marketSelectorList = tbsMarketSelectorService.getAllEnabledSites();
		assertThat(marketSelectorList.isEmpty()).isTrue();
	}

	@Test
	public void testServicecallWithNonEmptyResults()
	{
		final List<MarketSelectorSiteModel> siteSelectorList = new ArrayList<MarketSelectorSiteModel>();
		siteSelectorList.add(new MarketSelectorSiteModel());
		given(tbsMarketSelectorDao.findAllEnabledSites()).willReturn(siteSelectorList);
		final List<MarketSelectorSiteModel> marketSelectorList = tbsMarketSelectorService.getAllEnabledSites();
		assertThat(marketSelectorList.size() == 1).isTrue();

	}
}