/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package uk.co.thebodyshop.cms.rendering.populators;

import de.hybris.platform.acceleratorcms.model.components.NavigationComponentModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.cmsfacades.rendering.attributeconverters.NavigationNodeToDataContentConverter;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.site.BaseSiteService;

import java.util.Map;
import java.util.function.Predicate;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author Marcin
 */
public class NavigationComponentModelToDataPopulator implements Populator<CMSItemModel, Map<String, Object>>
{
	private static final String NAVIGATION_NODE = "navigationNode";

	private final Predicate<CMSItemModel> cmsNavigationComponentTypePredicate;

	private final NavigationNodeToDataContentConverter navigationNodeToDataContentConverter;

	private final BaseSiteService baseSiteService;

	@Override
	public void populate(final CMSItemModel cmsItemModel, final Map<String, Object> stringObjectMap) throws ConversionException
	{
		if (getCmsNavigationComponentTypePredicate().test(cmsItemModel) && cmsItemModel instanceof NavigationComponentModel)
		{
			final NavigationComponentModel navigationComponent = (NavigationComponentModel) cmsItemModel;
			final CMSNavigationNodeModel navigationNode = getNavigationNodeForSiteAndCurrentBaseSite(navigationComponent);
			if (null != navigationNode)
			{
				stringObjectMap.put(NAVIGATION_NODE, navigationNodeToDataContentConverter.convert(navigationNode));
			}
		}
	}

	private CMSNavigationNodeModel getNavigationNodeForSiteAndCurrentBaseSite(final NavigationComponentModel navigationComponent)
	{
		final BaseSiteModel currentSite = baseSiteService.getCurrentBaseSite();
		if (null != currentSite && MapUtils.isNotEmpty(navigationComponent.getSiteNavigationNodeMapping()))
		{
			return navigationComponent.getSiteNavigationNodeMapping().get(currentSite);
		}
		return null;
	}

	@Autowired
	public NavigationComponentModelToDataPopulator(final Predicate<CMSItemModel> cmsNavigationComponentTypePredicate,
			final NavigationNodeToDataContentConverter navigationNodeToDataContentConverter, final BaseSiteService baseSiteService)
	{
		this.cmsNavigationComponentTypePredicate = cmsNavigationComponentTypePredicate;
		this.navigationNodeToDataContentConverter = navigationNodeToDataContentConverter;
		this.baseSiteService = baseSiteService;
	}

	/**
	 * @return the cmsNavigationComponentTypePredicate
	 */
	protected Predicate<CMSItemModel> getCmsNavigationComponentTypePredicate()
	{
		return cmsNavigationComponentTypePredicate;
	}

	/**
	 * @return the navigationNodeToDataContentConverter
	 */
	protected NavigationNodeToDataContentConverter getNavigationNodeToDataContentConverter()
	{
		return navigationNodeToDataContentConverter;
	}

	/**
	 * @return the baseSiteService
	 */
	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}
}