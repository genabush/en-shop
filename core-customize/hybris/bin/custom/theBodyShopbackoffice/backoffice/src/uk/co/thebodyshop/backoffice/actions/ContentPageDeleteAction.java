/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.backoffice.actions;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;

import java.util.Collection;

import javax.annotation.Resource;

import org.zkoss.util.resource.Labels;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.delete.DeleteAction;

import uk.co.thebodyshop.cms.services.TbsCMSContentPageService;


public class ContentPageDeleteAction extends DeleteAction
{

	@Resource(name = "cmsContentPageService")
	private TbsCMSContentPageService tbsCMSContentPageService;

	private static final String CONTENT_PAGE_CONFIRMATION_MESSAGE = "hmc.action.contentpage.confirmation.message";

	@Override
	public boolean needsConfirmation(final ActionContext<Object> ctx)
	{
		boolean needConfirmation = false;
		if (isActiveLinkAvailableforPage(ctx.getData()))
		{
			needConfirmation = false;
		}
		else if (isPageCollection(ctx.getData()))
		{
			final Collection selectedItems = (Collection) ctx.getData();
			for (final Object selectItem : selectedItems)
			{
				if (!isActiveLinkAvailableforPage(selectItem))
				{
					needConfirmation = true;
					break;
				}
			}
		}
		else
		{
			needConfirmation = true;
		}
		return needConfirmation;
	}

	@Override
	public String getConfirmationMessage(final ActionContext<Object> ctx)
	{
		if (ctx.getData() instanceof ContentPageModel || isPageCollection(ctx.getData()))
		{
			return Labels.getLabel(CONTENT_PAGE_CONFIRMATION_MESSAGE);
		}
		else
		{
			return super.getConfirmationMessage(ctx);
		}
	}

	private boolean isActiveLinkAvailableforPage(final Object data)
	{
		if(data instanceof ContentPageModel)
		{
			final ContentPageModel page = (ContentPageModel) data;
			return tbsCMSContentPageService.isActiveLinkAvailableforContentPage((ContentPageModel) data);
		}
		return false;
	}

	private boolean isPageCollection(final Object data)
	{
		if (data instanceof Collection)
		{
			final Collection selectedItems = (Collection) data;
			return selectedItems.iterator().next() instanceof AbstractPageModel;
		}
		return false;
	}
}
