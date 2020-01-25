/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.cms.rendering.populators;

import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.cmsfacades.data.NavigationNodeData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import org.apache.commons.lang.StringUtils;

/**
 * @author Balakrishna
 **/
public class CMSNavigationNodeModelToDataPopulator implements Populator<CMSNavigationNodeModel, NavigationNodeData> {

    @Override
    public void populate(CMSNavigationNodeModel cmsNavigationNodeModel, NavigationNodeData navigationNodeData) throws ConversionException
    {
        String icon=cmsNavigationNodeModel.getIcon();
        if(StringUtils.isNotEmpty(icon))
        {
            navigationNodeData.setIcon(icon);
        }
    }
}
