/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.cms.rendering.populators;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
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
public class TbsAbstractPageModelToDataPopulator implements Populator<AbstractPageModel, Map<String, Object>> {
    private static final String PAGE_TYPE = "pageType";

    @Override
    public void populate(AbstractPageModel abstractPageModel, Map<String, Object> additionalProperties) throws ConversionException
    {
            String pageType = abstractPageModel.getPageType();
            if(StringUtils.isNotEmpty(pageType))
            {
                additionalProperties.put(PAGE_TYPE, pageType);
            }
    }

}
