/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.languages.impl;


import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.co.thebodyshop.core.constants.YcommercewebservicesConstants;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@UnitTest
public class DefaultTbsStoreSessionFacadeTest {

    public static final String EN = "en";
    public static final String EN_CA = "en_CA";
    public static final String FR_CA = "fr_CA";
    private TbsDefaultStoreSessionFacade defaultTbsStoreSessionFacade;


    @Mock
    private BaseStoreService baseStoreService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        defaultTbsStoreSessionFacade = new TbsDefaultStoreSessionFacade();
        defaultTbsStoreSessionFacade.setBaseStoreService(baseStoreService);
        setLanguageConverter(defaultTbsStoreSessionFacade);
    }

    public void setLanguageConverter(TbsDefaultStoreSessionFacade defaultTbsStoreSessionFacade) {
        AbstractPopulatingConverter<LanguageModel, LanguageData> languageConverter = new AbstractPopulatingConverter<>();
        languageConverter.setTargetClass(LanguageData.class);
        languageConverter.setPopulators(Arrays.asList(new TestLanguagePopulator()));
        defaultTbsStoreSessionFacade.setLanguageConverter(languageConverter);
    }

    @Test
    public void shouldReturnGlobalLanguageForGlobalStore() {
        given(baseStoreService.getCurrentBaseStore()).willReturn(getTestBaseStoreWithLanguages(YcommercewebservicesConstants.TBS_GLOBAL_STORE));
        Collection<LanguageData> resultLanguages = defaultTbsStoreSessionFacade.getFrontendDisplayLanguages();
        assertThat(resultLanguages != null).isTrue();
        assertThat(resultLanguages.size() == 1).isTrue();
        LanguageData language = resultLanguages.iterator().next();
        assertThat(StringUtils.equals(language.getIsocode(), EN)).isTrue();
    }

    @Test
    public void shouldReturnCanadianLanguageForCanadianStore() {
        given(baseStoreService.getCurrentBaseStore()).willReturn(getTestBaseStoreWithLanguages(YcommercewebservicesConstants.TBS_CA_STORE));
        Collection<LanguageData> resultLanguages = defaultTbsStoreSessionFacade.getFrontendDisplayLanguages();
        assertThat(resultLanguages != null).isTrue();
        assertThat(resultLanguages.size() == 2).isTrue();
        assertThat(CollectionUtils.isNotEmpty(resultLanguages.stream().filter(language -> StringUtils.equals(language.getIsocode(), EN_CA)).collect(Collectors.toList()))).isTrue();
        assertThat(CollectionUtils.isNotEmpty(resultLanguages.stream().filter(language -> StringUtils.equals(language.getIsocode(), FR_CA)).collect(Collectors.toList()))).isTrue();
        assertThat(CollectionUtils.isEmpty(resultLanguages.stream().filter(language -> StringUtils.equals(language.getIsocode(), EN)).collect(Collectors.toList()))).isTrue();
    }

    @Test
    public void shouldReturnEmptyCollection() {
        given(baseStoreService.getCurrentBaseStore()).willReturn(getTestBaseStoreWithoutLanguages(YcommercewebservicesConstants.TBS_CA_STORE));
        Collection<LanguageData> resultLanguages = defaultTbsStoreSessionFacade.getFrontendDisplayLanguages();
        assertThat(resultLanguages != null).isTrue();
        assertThat(resultLanguages.size() == 0).isTrue();
    }

    private BaseStoreModel getTestBaseStoreWithLanguages(String baseStoreUid) {
        BaseStoreModel baseStore = createBaseStore(baseStoreUid);
        baseStore.setLanguages(getLanguagesForBaseStore());
        return baseStore;
    }

    private BaseStoreModel getTestBaseStoreWithoutLanguages(String baseStoreUid) {
        BaseStoreModel baseStore = createBaseStore(baseStoreUid);
        baseStore.setLanguages(Collections.EMPTY_SET);
        return baseStore;
    }

    private BaseStoreModel createBaseStore(String baseStoreUid) {
        BaseStoreModel baseStore = new BaseStoreModel();
        baseStore.setUid(baseStoreUid);
        return baseStore;
    }

    private Set<LanguageModel> getLanguagesForBaseStore() {
        Set<LanguageModel> languages = new HashSet<>();
        languages.add(getLanguageForISOCode(EN));
        languages.add(getLanguageForISOCode(EN_CA));
        languages.add(getLanguageForISOCode(FR_CA));
        return languages;
    }

    private LanguageModel getLanguageForISOCode(String isoCode) {
        LanguageModel language = new LanguageModel();
        language.setIsocode(isoCode);
        return language;
    }

    class TestLanguagePopulator implements Populator<LanguageModel, LanguageData> {

        @Override
        public void populate(LanguageModel languageModel, LanguageData languageData) throws ConversionException {
            languageData.setIsocode(languageModel.getIsocode());
        }
    }

}
