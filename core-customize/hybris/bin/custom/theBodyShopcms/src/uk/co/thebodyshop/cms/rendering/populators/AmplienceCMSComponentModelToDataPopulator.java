/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.cms.rendering.populators;

import java.util.Map;
import java.util.function.Predicate;

import org.apache.commons.lang.StringUtils;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.session.SessionService;

/**
 * @author Marcin
 */
public class AmplienceCMSComponentModelToDataPopulator implements Populator<CMSItemModel, Map<String, Object>>
{
	private static final String AMPLIENCE_DYNAMIC_CONTENT_ROOT_URL = "tbs.amplience.dynamic.content.root.url";
	private static final String AMPLIENCE_ROOT_URL = "amplienceRootUrl";
	private static final String AMPLIENCE_PREVIEW_URL = "ampliencePreviewUrl";

	private final Predicate<CMSItemModel> cmsAmplienceCMSComponentTypePredicate;

	private final SessionService sessionService;

	private final ConfigurationService configurationService;

	@Override
	public void populate(final CMSItemModel cmsItemModel, final Map<String, Object> additionalProperties) throws ConversionException
	{
		if (getCmsAmplienceCMSComponentTypePredicate().test(cmsItemModel))
		{
			final String previewUrl = getSessionService().getAttribute(AMPLIENCE_PREVIEW_URL);
			if (StringUtils.isNotEmpty(previewUrl))
			{
				additionalProperties.put(AMPLIENCE_ROOT_URL, "https://".concat(previewUrl));
			}
			else
			{
				final String defaultUrl = getConfigurationService().getConfiguration().getString(AMPLIENCE_DYNAMIC_CONTENT_ROOT_URL);
				additionalProperties.put(AMPLIENCE_ROOT_URL, defaultUrl);
			}
		}
	}

	public AmplienceCMSComponentModelToDataPopulator(final Predicate<CMSItemModel> cmsAmplienceCMSComponentTypePredicate, final SessionService sessionService, final ConfigurationService configurationService)
	{
		this.cmsAmplienceCMSComponentTypePredicate = cmsAmplienceCMSComponentTypePredicate;
		this.sessionService = sessionService;
		this.configurationService = configurationService;
	}

	protected Predicate<CMSItemModel> getCmsAmplienceCMSComponentTypePredicate()
	{
		return cmsAmplienceCMSComponentTypePredicate;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}
}
