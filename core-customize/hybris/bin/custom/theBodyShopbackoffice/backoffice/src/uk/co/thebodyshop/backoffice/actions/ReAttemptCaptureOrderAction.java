/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.backoffice.actions;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Messagebox;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import com.hybris.cockpitng.util.notifications.NotificationService;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.processengine.BusinessProcessService;

/**
 * @author Marcin
 */
public class ReAttemptCaptureOrderAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<OrderModel, OrderModel>
{
	private static final Logger LOG = LoggerFactory.getLogger(ReAttemptCaptureOrderAction.class);

	private static final String SUCCESS = "success";

	private static final String RE_ATTEMPT_CAPTURE_EVENT = "_ReAttemptCaptureEvent";

	protected static final Object COMPLETED = "completed";

	@Resource(name = "notificationService")
	private NotificationService notificationService;

	@Resource(name = "businessProcessService")
	private BusinessProcessService businessProcessService;

	private ActionContext<OrderModel> actionContext;

	private OrderModel orderModel;

	public boolean canPerform(final ActionContext<OrderModel> actionContext)
	{
		final OrderModel order = actionContext.getData();
		return order != null && OrderStatus.PRIMARY_PAYMENT_NOT_CAPTURED.equals(order.getStatus());
	}

	@Override
	public ActionResult<OrderModel> perform(final ActionContext<OrderModel> ctx)
	{
		setOrderModel(ctx.getData());
		setActionContext(ctx);
		showMessageBox(actionContext.getData());
		return new ActionResult(SUCCESS);
	}

	protected void showMessageBox(final OrderModel orderModel)
	{
		Messagebox.show(actionContext.getLabel("reattemptcapture.confirm.msg"), actionContext.getLabel("reattemptcapture.confirm.title") + " " + orderModel.getCode(), new Messagebox.Button[]
				{ Messagebox.Button.NO, Messagebox.Button.YES }, "", this::processCapture);
	}

	protected void processCapture(final Event obj)
	{
		if (Messagebox.Button.YES.event.equals(obj.getName()))
		{
			final ConsignmentModel consignmentModel = this.orderModel.getConsignments().iterator().next();
			triggerConsignmentProcess(consignmentModel);
			getNotificationService().notifyUser("", "JustMessage", NotificationEvent.Level.SUCCESS, new Object[]
			{ actionContext.getLabel("reattemptcapture.confirm.success") });
		}
	}

	private void triggerConsignmentProcess(final ConsignmentModel consignmentModel)
	{
		businessProcessService.triggerEvent(consignmentModel.getCode() + RE_ATTEMPT_CAPTURE_EVENT);
	}

	/**
	 * @return the orderModel
	 */
	public OrderModel getOrderModel()
	{
		return orderModel;
	}

	/**
	 * @param orderModel
	 *          the orderModel to set
	 */
	public void setOrderModel(final OrderModel orderModel)
	{
		this.orderModel = orderModel;
	}

	/**
	 * @return the actionContext
	 */
	public ActionContext<OrderModel> getActionContext()
	{
		return actionContext;
	}

	/**
	 * @param actionContext
	 *          the actionContext to set
	 */
	public void setActionContext(final ActionContext<OrderModel> actionContext)
	{
		this.actionContext = actionContext;
	}

	/**
	 * @return the notificationService
	 */
	public NotificationService getNotificationService()
	{
		return notificationService;
	}

	/**
	 * @param notificationService
	 *          the notificationService to set
	 */
	public void setNotificationService(final NotificationService notificationService)
	{
		this.notificationService = notificationService;
	}
}
