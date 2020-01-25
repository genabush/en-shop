/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.backoffice.widgets.notificationarea;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zul.Div;

import com.hybris.backoffice.widgets.notificationarea.NotificationController;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent.Level;
import com.hybris.cockpitng.config.notification.jaxb.Notification;
import com.hybris.cockpitng.config.notification.jaxb.NotificationDefaults;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectDeletionException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectSavingException;


public class TbsNotificationController extends NotificationController
{
	@Override
	protected void addMessageComponent(final Div container, final NotificationEvent notificationEvent,
			final Notification notificationConfig, final NotificationDefaults notificationDefaults)
	{
		if (null != notificationEvent
				&& null != notificationEvent.getReferencedObjects() && notificationEvent.getReferencedObjects().length > 0
				&& notificationEvent.getLevel() == Level.FAILURE)
		{
			updateFailureMessage(notificationEvent, notificationConfig);
		}
		super.addMessageComponent(container, notificationEvent, notificationConfig, notificationDefaults);
	}

	private void updateFailureMessage(final NotificationEvent notificationEvent, final Notification notificationConfig)
	{
		for (final Object object : notificationEvent.getReferencedObjects())
		{
			if (object instanceof HashMap && StringUtils.containsIgnoreCase(notificationConfig.getEventType(), "RemoveObject"))
			{
				final HashMap failureHashMap = (HashMap) object;
				updateNotificationConfigMessage(notificationConfig, failureHashMap);
				if (isCustomMsg(notificationConfig.getMessage()))
				{
					break;
				}
			}
			if (object instanceof ObjectSavingException)
			{
				updateNotificationConfigMessage(notificationConfig, (ObjectSavingException) object);
				if (isCustomMsg(notificationConfig.getMessage()))
				{
					break;
				}
			}
			if (object instanceof Map && StringUtils.containsIgnoreCase(notificationConfig.getEventType(), "UpdateObject"))
			{
				final Map failureMap = (Map) object;
				if (MapUtils.isNotEmpty(failureMap) && failureMap.values().iterator().next() instanceof ObjectSavingException)
				{
					updateNotificationConfigMessage(notificationConfig, (ObjectSavingException) failureMap.values().iterator().next());
				}
				if (isCustomMsg(notificationConfig.getMessage()))
				{
					break;
				}
			}
		}
	}

	private void updateNotificationConfigMessage(final Notification notificationConfig, final ObjectSavingException exception)
	{
		if (null != exception.getCause() && isCustomMsg(exception.getCause().getMessage()))
		{
			final String[] message = exception.getCause().getMessage().split(":");
			notificationConfig.setMessage(message[1]);
		}
		else if (StringUtils.containsIgnoreCase(notificationConfig.getEventType(), "UpdateObject"))
		{
			notificationConfig.setMessage("user.notification.items.unable_to_update");
		}
		else
		{
			notificationConfig.setMessage("user.notification.items.unable_to_create");
		}
	}

	private boolean isCustomMsg(final String msg)
	{
		return StringUtils.containsIgnoreCase(msg, "custom.notallowed.notification.msg");
	}

	private void updateNotificationConfigMessage(final Notification notificationConfig, final HashMap failureHashMap)
	{
		for (final Object deletionObject : failureHashMap.values())
		{
			if (deletionObject instanceof ObjectDeletionException)
			{
				final ObjectDeletionException exception = (ObjectDeletionException) deletionObject;
				if (null != exception.getCause() && isCustomMsg(exception.getCause().getMessage()))
				{
					final String[] message = exception.getCause().getMessage().split(":");
					notificationConfig.setMessage(message[1]);
					break;
				}
				else
				{
					notificationConfig.setMessage("user.notification.items.unable_to_delete");
				}

			}
		}
	}

}
