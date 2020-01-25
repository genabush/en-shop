/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package uk.co.thebodyshop.cms.rendering.populators;

import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.cmsfacades.data.NavigationNodeData;
import de.hybris.platform.cmsfacades.rendering.populators.NavigationNodeModelToDataRenderingPopulator;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import uk.co.thebodyshop.cms.data.LinkComponentData;

import java.util.Objects;

/**
 * @author Marcin
 */
public class TbsNavigationNodeModelToDataRenderingPopulator extends NavigationNodeModelToDataRenderingPopulator
{
	private Converter<CMSLinkComponentModel, LinkComponentData> cmsLinkComponentModelToDataRenderingConverter;

	@Override
	public void populate(final CMSNavigationNodeModel source, final NavigationNodeData target)
	{
		super.populate(source, target);
		target.setAmplienceId(source.getAmplienceId());
		target.setEnabledForMegaNav(Boolean.toString(source.isEnabledForMegaNav()));
		target.setEnabledForMobileNav(Boolean.toString(source.isEnabledForMobileNav()));
		target.setColumnBreak(Boolean.toString(source.isColumnBreak()));
		if(Objects.nonNull(source.getViewAllLink())) {
			target.setViewAllLink(getCmsLinkComponentModelToDataRenderingConverter().convert(source.getViewAllLink()));
		}
	}

	public TbsNavigationNodeModelToDataRenderingPopulator(Converter<CMSLinkComponentModel, LinkComponentData> cmsLinkComponentModelToDataRenderingConverter) {
		this.cmsLinkComponentModelToDataRenderingConverter = cmsLinkComponentModelToDataRenderingConverter;
	}

	protected Converter<CMSLinkComponentModel, LinkComponentData> getCmsLinkComponentModelToDataRenderingConverter() {
		return cmsLinkComponentModelToDataRenderingConverter;
	}
}
