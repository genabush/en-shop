/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.cms.interceptors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;

import uk.co.thebodyshop.cms.services.TbsCMSContentPageService;


public class CmsContentPageRemoveInterceptor implements RemoveInterceptor<ContentPageModel>
{
	private static final Logger LOG = Logger.getLogger(CmsContentPageRemoveInterceptor.class);

	private final TbsCMSContentPageService tbsCMSContentPageService;

	@Override
	public void onRemove(final ContentPageModel contentPageModel, final InterceptorContext interceptorContext) throws InterceptorException
	{
		if (tbsCMSContentPageService.isActiveLinkAvailableforContentPage(contentPageModel))
		{
			throw new InterceptorException("contentpage.custom.notallowed.notification.msg");
		}
	}

	@Autowired
	public CmsContentPageRemoveInterceptor(final TbsCMSContentPageService tbsCMSContentPageService)
	{
		this.tbsCMSContentPageService = tbsCMSContentPageService;
	}

	public TbsCMSContentPageService getTbsCMSContentPageService()
	{
		return tbsCMSContentPageService;
	}

}
