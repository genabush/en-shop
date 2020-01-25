/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.backoffice.actions;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Messagebox;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.util.notifications.NotificationService;

import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.omsbackoffice.actions.order.cancel.CancelOrderAction;
import de.hybris.platform.ordercancel.OrderCancelEntry;
import de.hybris.platform.ordercancel.OrderCancelException;
import de.hybris.platform.ordercancel.OrderCancelRequest;
import de.hybris.platform.servicelayer.model.ModelService;

/**
 * @author Jagadeesh
 */
public class TbsCancelOrderAction extends CancelOrderAction
{

	private static final Logger LOGGER = LoggerFactory.getLogger(TbsCancelOrderAction.class);

	protected static final Object COMPLETED = "completed";

	@Resource(name = "notificationService")
	private NotificationService notificationService;

	@Resource(name = "modelService")
	private ModelService modelService;

	private ActionContext<OrderModel> actionContext;

	private OrderModel orderModel;

	@Override
	public ActionResult<OrderModel> perform(ActionContext<OrderModel> actionContext)
	{
		setOrderModel(actionContext.getData());
		setActionContext(actionContext);
		showMessageBox(actionContext.getData());
		return new ActionResult("success");
	}

	protected void showMessageBox(OrderModel orderModel)
	{
		Messagebox.show(actionContext.getLabel("cancelorder.confirm.msg"), actionContext.getLabel("cancelorder.confirm.title") + " " + orderModel.getCode(), new Messagebox.Button[]
		{ Messagebox.Button.NO, Messagebox.Button.YES }, "oms-widget-cancelorder-confirm-icon", this::processCancellation);
	}

	protected OrderCancelRequest buildCancelRequest()
	{
		if (orderModel != null)
		{
			final List<OrderCancelEntry> orderCancelEntries = new ArrayList();
			orderModel.getEntries().stream().forEach(entry -> {
				this.createOrderCancelEntry(orderCancelEntries, entry);
			});
			final OrderCancelRequest orderCancelRequest = new OrderCancelRequest(orderModel, orderCancelEntries);
			orderCancelRequest.setCancelReason(null);
			orderCancelRequest.setNotes("Order cancelled");
			return orderCancelRequest;
		}
		else
		{
			return null;
		}
	}

	protected void createOrderCancelEntry(List<OrderCancelEntry> orderCancelEntries, AbstractOrderEntryModel entry)
	{
		final OrderCancelEntry orderCancelEntry = new OrderCancelEntry(entry, entry.getQuantity(), "", CancelReason.CUSTOMERREQUEST);
		orderCancelEntries.add(orderCancelEntry);
	}

	protected void processCancellation(Event obj)
	{
		if (Messagebox.Button.YES.event.equals(obj.getName()))
		{
			try
			{
				getOrderCancelService().requestOrderCancel(buildCancelRequest(), getUserService().getCurrentUser());
				if (OrderStatus.CANCELLED.equals(this.orderModel.getStatus()))
				{
					getNotificationService().notifyUser("", "JustMessage", NotificationEvent.Level.SUCCESS, new Object[]
					{ actionContext.getLabel("cancelorder.confirm.success") });
				}
				else
				{
					getNotificationService().notifyUser("", "JustMessage", NotificationEvent.Level.FAILURE, new Object[]
					{ actionContext.getLabel("cancelorder.confirm.error") });
				}
			}
			catch (final OrderCancelException ex)
			{
				LOGGER.info(ex.getMessage(), ex);
				getNotificationService().notifyUser("", "JustMessage", NotificationEvent.Level.FAILURE, new Object[]
				{ actionContext.getLabel("cancelorder.confirm.error") });
			}
			sendOutput("confirmcancellation", COMPLETED);
		}
	}

	protected NotificationService getNotificationService()
	{
		return this.notificationService;
	}

	protected OrderModel getOrderModel()
	{
		return orderModel;
	}

	public void setOrderModel(OrderModel orderModel)
	{
		this.orderModel = orderModel;
	}

	protected ModelService getModelService()
	{
		return this.modelService;
	}

	protected ActionContext<OrderModel> getActionContext()
	{
		return actionContext;
	}

	public void setActionContext(ActionContext<OrderModel> actionContext)
	{
		this.actionContext = actionContext;
	}
}
