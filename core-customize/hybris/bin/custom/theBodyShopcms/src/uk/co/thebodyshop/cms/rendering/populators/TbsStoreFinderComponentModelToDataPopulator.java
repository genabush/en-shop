/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.cms.rendering.populators;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import org.apache.commons.lang.StringUtils;
import uk.co.thebodyshop.cms.model.components.TbsStoreFinderComponentModel;

import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author Balakrishna
 **/
public class TbsStoreFinderComponentModelToDataPopulator implements Populator<CMSItemModel, Map<String, Object>> {
    private static final String IMAGE_URL = "imageUrl";

    private Predicate<CMSItemModel> cmsTbsStoreFinderComponentTypePredicate;

    private I18NService i18NService;

    public TbsStoreFinderComponentModelToDataPopulator(Predicate<CMSItemModel> cmsTbsStoreFinderComponentTypePredicate, I18NService i18NService) {
        this.cmsTbsStoreFinderComponentTypePredicate = cmsTbsStoreFinderComponentTypePredicate;
        this.i18NService = i18NService;
    }

    @Override
    public void populate(CMSItemModel cmsItemModel, Map<String, Object> additionalProperties) throws ConversionException {
        if (getCmsTbsStoreFinderComponentTypePredicate().test(cmsItemModel)) {
            TbsStoreFinderComponentModel tbsStoreFinderComponentModel = (TbsStoreFinderComponentModel) cmsItemModel;
            Locale currentLocal = getI18NService().getCurrentLocale();
            String localizedImageUrl = tbsStoreFinderComponentModel.getImageUrl(currentLocal);
            if(StringUtils.isNotEmpty(localizedImageUrl))
            {
                additionalProperties.put(IMAGE_URL, localizedImageUrl);
            }
        }
    }

    public Predicate<CMSItemModel> getCmsTbsStoreFinderComponentTypePredicate() {
        return cmsTbsStoreFinderComponentTypePredicate;
    }

    protected I18NService getI18NService() {
        return i18NService;
    }
}
