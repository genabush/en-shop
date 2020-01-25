/**
 *
 */
package uk.co.thebodyshop.core.factories;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import org.junit.Before;
import org.junit.Test;
import uk.co.thebodyshop.core.enums.CurrencySymbolPosition;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;


/**
 * @author Krishna
 *
 */

@UnitTest
public class TbsPriceDataFactoryTest
{

	public static final String ISOCODE_EN = "en";
	public static final String ISOCODE_GBP = "GBP";
	public static final String ISOCODE_DE = "de";
	public static final String ISOCODE_EUR = "EUR";
	private CommerceCommonI18NService commerceCommonI18NService;
	private CommonI18NService commonI18NService;
	private BaseStoreService baseStoreService;

	private TbsPriceDataFactory tbsPriceDataFactory;


	@Before
	public void setUp()
	{
		commerceCommonI18NService = mock(CommerceCommonI18NService.class);
		commonI18NService = mock(CommonI18NService.class);
		baseStoreService = mock(BaseStoreService.class);
		tbsPriceDataFactory = new TbsPriceDataFactory(baseStoreService);
		tbsPriceDataFactory.setCommerceCommonI18NService(commerceCommonI18NService);
		tbsPriceDataFactory.setCommonI18NService(commonI18NService);

	}

	@Test
	public void shouldSetCurrencySymbolBeforeValue() {
		when(commonI18NService.getCurrentLanguage()).thenReturn(getTestLanguageModel(ISOCODE_EN));
		when(commerceCommonI18NService.getLocaleForLanguage(any(LanguageModel.class))).thenReturn(Locale.ENGLISH);
		when(baseStoreService.getCurrentBaseStore()).thenReturn(getTestBaseStore(ISOCODE_EN, CurrencySymbolPosition.BEFORE_PRICE));
		PriceData price = tbsPriceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(20.5d), getTestCurrency(ISOCODE_GBP, "£" ));
		assertEquals("£20.50", price.getFormattedValue());
		assertEquals(ISOCODE_GBP, price.getCurrencyIso());
	}

	@Test
	public void shouldSetCurrencySymbolAfterValue() {
		when(commonI18NService.getCurrentLanguage()).thenReturn(getTestLanguageModel(ISOCODE_DE));
		when(commerceCommonI18NService.getLocaleForLanguage(any(LanguageModel.class))).thenReturn(Locale.GERMAN);
		when(baseStoreService.getCurrentBaseStore()).thenReturn(getTestBaseStore(ISOCODE_DE, CurrencySymbolPosition.AFTER_PRICE));
		PriceData price = tbsPriceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(20.5d), getTestCurrency(ISOCODE_EUR, "€" ));
		assertEquals("20,50 €", price.getFormattedValue());
		assertEquals(ISOCODE_EUR, price.getCurrencyIso());
	}

	private LanguageModel getTestLanguageModel(String languageIso) {
		LanguageModel languageModel = new LanguageModel();
		languageModel.setIsocode(languageIso);
		return languageModel;
	}

	private BaseStoreModel getTestBaseStore(String languageIso, CurrencySymbolPosition currencySymbolPosition) {
		BaseStoreModel baseStore = new BaseStoreModel();
		Map<String, CurrencySymbolPosition> currencySymbolPositionMap = new HashMap<>();
		currencySymbolPositionMap.put(languageIso, currencySymbolPosition);
		baseStore.setCurrencySymbolPositionMap(currencySymbolPositionMap);
		return baseStore;
	}

	private CurrencyModel getTestCurrency(String isoCode, String symbol) {
		CurrencyModel currency = new CurrencyModel();
		currency.setIsocode(isoCode);
		currency.setSymbol(symbol);
		currency.setDigits(2);
		return currency;
	}

}
