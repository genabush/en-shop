/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.cms.rendering.populators;

import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import uk.co.thebodyshop.cms.data.LinkComponentData;

import java.util.Objects;

/**
 * @author Lakshmi
 **/
public class CMSLinkComponentModelToDataPopulator implements Populator<CMSLinkComponentModel, LinkComponentData> {

    @Override
    public void populate(CMSLinkComponentModel source, LinkComponentData target) throws ConversionException
    {
        if(Objects.nonNull(source)) {
            target.setLinkName(source.getLinkName());
            target.setUrl(source.getUrl());
        }
    }
}
