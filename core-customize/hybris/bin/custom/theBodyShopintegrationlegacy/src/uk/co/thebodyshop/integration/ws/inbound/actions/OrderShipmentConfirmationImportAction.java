/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.ws.inbound.actions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Iterables;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import uk.co.thebodyshop.core.services.TbsOrderService;
import uk.co.thebodyshop.core.strategies.ConsignmentStatusStrategy;
import uk.co.thebodyshop.integration.jaxb.order.shipmentconfirmation.OrderShipmentConfirmation;
import uk.co.thebodyshop.integration.jaxb.order.shipmentconfirmation.OrderShipmentConfirmationRequest;


/**
 * @author vasanthramprakasam
 */
public class OrderShipmentConfirmationImportAction extends AbstractFeedImportAction<OrderShipmentConfirmationRequest>
{

	private static final String DATE_FORMAT = "yyyyMMdd";

	private static final String TIME_FORMAT = "HHmmss";

	private static final String SHIP_EVENT = "_ConsignmentShippedEvent";

	private final TbsOrderService tbsOrderService;

	private final List<OrderStatus> orderStatusesForShipment;

	private final ConsignmentStatusStrategy consignmentStatusStrategy;

	private final BusinessProcessService businessProcessService;

	private final Predicate<ConsignmentModel> clickAndCollectConsignmentPredicate;

	public OrderShipmentConfirmationImportAction(final TbsOrderService tbsOrderService, final List<OrderStatus> orderStatusesForShipment, final ConsignmentStatusStrategy consignmentStatusStrategy, final BusinessProcessService businessProcessService,
			final Predicate<ConsignmentModel> clickAndCollectConsignmentPredicate)
	{
		this.tbsOrderService = tbsOrderService;
		this.orderStatusesForShipment = orderStatusesForShipment;
		this.consignmentStatusStrategy = consignmentStatusStrategy;
		this.businessProcessService = businessProcessService;
		this.clickAndCollectConsignmentPredicate = clickAndCollectConsignmentPredicate;
	}

	//TODO check all existing logic from legacy tbsdataprocessing extension
	@Override
	protected void executeInternal(final OrderShipmentConfirmationRequest feed) throws Exception
	{
		for(final OrderShipmentConfirmation payload : feed.getOrderShipmentConfirmation())
		{
			final String orderCode = payload.getCode();
			try
			{
				final OrderModel order = getTbsOrderService().getOrderForCode(orderCode);

				if (getOrderStatusesForShipment().contains(order.getStatus()))
				{
					//get the first consignment for now TODO review this
					final ConsignmentModel consignmentModel = Iterables.getFirst(order.getConsignments(), null);

					if (consignmentModel == null)
					{
						LOG.error("No consignment found on order");
						return;
					}

					for (final OrderShipmentConfirmation.Entries.Entry entry : payload.getEntries().getEntry())
					{
						updateConsignmentEntry(entry, consignmentModel);
					}

					final ConsignmentStatus status = getConsignmentStatusStrategy().calculateConsignmentStatus(consignmentModel);
					consignmentModel.setStatus(status);
					consignmentModel.setCarrier(payload.getCarrier());
					//consignmentModel.setCarrierService(payload.getCarrierService());
					consignmentModel.setTrackingID(payload.getTrackingID());
					updateDeliveryDate(payload, consignmentModel);
					getModelService().save(consignmentModel);
					setOrderStatus(consignmentModel,order);
					triggerConsignmentProcess(consignmentModel);
				}
				else
				{
					LOG.error("Ignoring order {} status is not valid for processing", orderCode);
				}
			}
			catch (final ModelNotFoundException e)
			{
				LOG.warn("The order [{}] was not found", orderCode);
			}
		}
	}

	private void setOrderStatus(final ConsignmentModel consignmentModel,final OrderModel orderModel)
	{
		final boolean clickAndCollectOrder = getClickAndCollectConsignmentPredicate().test(consignmentModel);
		if (clickAndCollectOrder)
		{
			orderModel.setStatus(getOrderStatusForClickAndCollect(consignmentModel));
		}
		else
		{
			orderModel.setStatus(getOrderStatusForDirectFullfilment(consignmentModel));
		}
		getModelService().save(orderModel);
	}

	private OrderStatus getOrderStatusForDirectFullfilment(final ConsignmentModel consignmentModel)
	{
		if (consignmentModel.getStatus().equals(ConsignmentStatus.SHIPPED))
		{
			return OrderStatus.SHIPPED;
		}
		else if (consignmentModel.getStatus().equals(ConsignmentStatus.PART_SHIPPED))
		{
			return OrderStatus.PART_SHIPPED;
		}
		else
		{
			return OrderStatus.NOT_SHIPPED;
		}
	}

	private OrderStatus getOrderStatusForClickAndCollect(final ConsignmentModel consignmentModel)
	{
		if (consignmentModel.getStatus().equals(ConsignmentStatus.PICKUP_COMPLETE)) {
			return OrderStatus.PICKUP_COMPLETE;
		} else if (consignmentModel.getStatus().equals(ConsignmentStatus.PART_PICKED)) {
			return OrderStatus.PART_PICKED;
		} else {
			return OrderStatus.NOT_PICKED;
		}
	}

	private void triggerConsignmentProcess(final ConsignmentModel consignmentModel)
	{
		getBusinessProcessService().triggerEvent(consignmentModel.getCode()+SHIP_EVENT);
	}

	private void updateDeliveryDate(final OrderShipmentConfirmation payload, final ConsignmentModel consignmentModel)
	{
		final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		final SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
		final String shippingdate = payload.getShippingDate();
		final String shippingTime = payload.getShippingTime();
		Date shippingdateObject = null;
		Date shippingTimeObject = null;
		try
		{
			if (StringUtils.isNotBlank(shippingdate))
			{
				shippingdateObject = dateFormat.parse(shippingdate);
			}
			if (StringUtils.isNotBlank(shippingTime))
			{
				shippingTimeObject = timeFormat.parse(shippingTime);
			}
		}
		catch (final ParseException e)
		{
			LOG.error("Cannot parse shipping date", e);
		}
		if (shippingdateObject != null)
		{
			final Calendar shipmentCal = Calendar.getInstance();
			shipmentCal.setTime(shippingdateObject);

			if (shippingTimeObject != null)
			{
				shipmentCal.set(Calendar.HOUR_OF_DAY, shippingTimeObject.getHours());
				shipmentCal.set(Calendar.MINUTE, shippingTimeObject.getMinutes());
				shipmentCal.set(Calendar.SECOND, shippingTimeObject.getSeconds());
			}
			else
			{
				shipmentCal.set(Calendar.HOUR_OF_DAY, 0);
				shipmentCal.set(Calendar.MINUTE, 0);
				shipmentCal.set(Calendar.SECOND, 0);
			}

			consignmentModel.setShippingDate(shipmentCal.getTime());
		}
	}


	private void updateConsignmentEntry(final OrderShipmentConfirmation.Entries.Entry entry, final ConsignmentModel consignmentModel)
	{
		for (final ConsignmentEntryModel entryModel : consignmentModel.getConsignmentEntries())
		{
			if (isProductSame(entryModel, entry))
			{
				if (isShipmentCheckRequired(entryModel))
				{
					entryModel.setShippedQuantity((long)entry.getQuantityDispatched());
				}
				else
				{
					entryModel.setShippedQuantity(entryModel.getOrderEntry().getQuantity());
				}
				entryModel.setStatus(getConsignmentStatusStrategy().calculateConsignmentEntryStatus(entryModel));
				getModelService().save(entryModel);
			}
		}
	}

	private boolean isShipmentCheckRequired(final ConsignmentEntryModel entry)
	{
		//TODO add this check later
		//return !(entry.getProduct() instanceof ServiceProductModel);
		return true;
	}

	private boolean isProductSame(final ConsignmentEntryModel entryModel, final OrderShipmentConfirmation.Entries.Entry entry)
	{
		//TODO add this check later
		//		if (StringUtils.isNotBlank(gtin))
		//		{
		//			final VariantProductModel variant = (VariantProductModel) entry.getProduct();
		//			return variant.getGtin().equals(gtin) && entry.getEntryNumber().toString().equals(entryNumber);
		//		}
		return entryModel.getOrderEntry().getProduct().getCode().equals(entry.getArticleId()) && entryModel.getOrderEntry().getEntryNumber().toString().equals(entry.getEntryNumber());
	}

	protected TbsOrderService getTbsOrderService()
	{
		return tbsOrderService;
	}

	protected List<OrderStatus> getOrderStatusesForShipment()
	{
		return orderStatusesForShipment;
	}

	protected ConsignmentStatusStrategy getConsignmentStatusStrategy()
	{
		return consignmentStatusStrategy;
	}

	protected BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	protected Predicate<ConsignmentModel> getClickAndCollectConsignmentPredicate()
	{
		return clickAndCollectConsignmentPredicate;
	}
}
