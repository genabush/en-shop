/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.v2.filter;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import de.hybris.platform.cms2.misc.CMSFilter;
import de.hybris.platform.cms2.model.preview.CMSPreviewTicketModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPreviewService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;

import uk.co.thebodyshop.core.integration.amplience.AmplienceDynamicContentGateway;

/**
 * @author j.wong
 */
public class PreviewDataFilter extends AbstractUrlMatchingFilter
{
	private static final Logger LOG = Logger.getLogger(PreviewDataFilter.class);
	private static final String AMPLIENCE_PREVIEW_URL = "ampliencePreviewUrl";

	private final SessionService sessionService;
	private final ConfigurationService configurationService;
	private final CMSPreviewService cmsPreviewService;
	private final AmplienceDynamicContentGateway amplienceDynamicContentGateway;

	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException
	{
		final String previewTicketId = getSessionService().getAttribute(CMSFilter.PREVIEW_TICKET_ID_PARAM);
		if (StringUtils.isNotEmpty(previewTicketId))
		{
			final PreviewDataModel previewData = getPreviewDataModel(previewTicketId);

			if (Objects.nonNull(previewData))
			{
				if (null != previewData.getTime())
				{
					final String generatedPreviewUrl = generatePreviewUrl(previewData.getTime());

					if (StringUtils.isNotEmpty(generatedPreviewUrl))
					{
						getSessionService().setAttribute(AMPLIENCE_PREVIEW_URL, generatedPreviewUrl);
					}
				}
			}
		}

		filterChain.doFilter(request, response);
	}

	private String generatePreviewUrl(Date previewDateTime)
	{
		final String formattedUrl = generateFormattedUrl(Long.toString(previewDateTime.getTime()));

		String previewUrl = null;

		try
		{
			previewUrl = getAmplienceDynamicContentGateway().getDynamicContent(formattedUrl);
		}
		catch (final Exception e)
		{
			LOG.error("Could not generate an Amplience preview URL", e);
		}

		return previewUrl;
	}

	private String generateFormattedUrl(String epochTime)
	{
		final String previewStagingDomain = getConfigurationService().getConfiguration().getString("tbs.cms.amplience.preview.staging.domain");
		final String virtualStagingMicroService = getConfigurationService().getConfiguration().getString("tbs.cms.amplience.virtual.staging.micro.service");

		final StringBuilder url = new StringBuilder();

		url.append(virtualStagingMicroService).append("/").append(previewStagingDomain).append("?timestamp=").append(epochTime);

		return url.toString();
	}

	private PreviewDataModel getPreviewDataModel(final String previewTicketId)
	{
		final CMSPreviewTicketModel previewTicket = getCmsPreviewService().getPreviewTicket(previewTicketId);
		return previewTicket != null ? previewTicket.getPreviewData() : null;
	}

	public PreviewDataFilter(final AmplienceDynamicContentGateway amplienceDynamicContentGateway, final ConfigurationService configurationService, final SessionService sessionService, final CMSPreviewService cmsPreviewService)
	{
		this.configurationService = configurationService;
		this.sessionService = sessionService;
		this.cmsPreviewService = cmsPreviewService;
		this.amplienceDynamicContentGateway = amplienceDynamicContentGateway;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	protected CMSPreviewService getCmsPreviewService()
	{
		return cmsPreviewService;
	}

	protected AmplienceDynamicContentGateway getAmplienceDynamicContentGateway()
	{
		return amplienceDynamicContentGateway;
	}
}
