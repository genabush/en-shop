/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.endpoints;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.mercury.ws.v1.orderupdate.CCOrder;
import com.mercury.ws.v1.orderupdate.OrderUpdateRequest;
import com.mercury.ws.v1.orderupdate.OrderUpdateResponse;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.model.ModelService;

import uk.co.thebodyshop.core.services.TbsOrderService;

@Endpoint
public class OrderStatusUpdateEndpoint
{
	 private static final Logger LOG = Logger.getLogger(OrderStatusUpdateEndpoint.class);

	 private static final String ORDER_STATUS_EVENT_SUFFIX = "_OrderStatusUpdatedEvent";

	 private static final String WAIT_STATUS_NODE = "waitForStatusUpdate";

	 private static final String TARGET_NAMESPACE = "http://thebodyshop/orderStatusUpdateSchema";

	 private static final String CONSIGNMENT_NOT_IN_WAIT_NODE = "Consignment not in wait node for order [%s]";

	 private static final String NO_CONSIGNMENT_PROCESS_FOUND = "NO consignment process found for order [%s]";

	 private static final String RECEIVED = "RECEIVED";

	 @Resource
	 private ModelService modelService;

	 @Resource
	 private EnumerationService enumerationService;

	 @Resource
	 private TbsOrderService tbsOrderService;

	 @Resource
	 private BusinessProcessService businessProcessService;

	 @Resource
	 private Predicate<ConsignmentModel> clickAndCollectConsignmentPredicate;

	 @Resource
	 private Map<OrderStatus,ConsignmentStatus> orderStatusConsignmentStatusMapping;

	 @Resource
	 private Map<String,String> posHybrisOrderStatusMapping;

	 @PayloadRoot(localPart = "OrderUpdateRequest", namespace = TARGET_NAMESPACE)
	 public @ResponsePayload OrderUpdateResponse orderStatusUpdateService(final @RequestPayload OrderUpdateRequest request)
	 {
			LOG.debug("Create OrderUpdateRequest received. Total number of order received [" + request.getOrder().size() + "]");
			final String payloadId = "ORDERSTATUS_UPDATE_FEED" + new SimpleDateFormat("_dd-MMM-yyyy_kkmmssS").format(new Date());
			final OrderUpdateResponse response = new OrderUpdateResponse();
			response.setMessageId(payloadId);
			StringBuilder statusCodeBuilder = new StringBuilder("");
			final List<OrderModel> orderModels = new ArrayList<>();
			for (final CCOrder order : request.getOrder())
			{
				 try
				 {
						String orderResponse = processOrder(order, orderModels);
						if (StringUtils.isNotBlank(orderResponse))
						{
							 statusCodeBuilder.append(orderResponse).append(",");
						}
				 }
				 catch (final SystemException se)
				 {
						LOG.debug(String.format("Could not update status for order %s", order.getOrderNumber()), se);
						statusCodeBuilder.append("Order [").append(order.getOrderNumber()).append("] does not exist,");
				 }
			}
			modelService.saveAll(orderModels);
			LOG.debug(String.format("Update Order status finished ... updated [%d] orders", orderModels.size()));

			String statusCode = statusCodeBuilder.toString();
			if (StringUtils.isEmpty(statusCode))
			{
				 statusCode = RECEIVED;
			}
			else
			{
				 statusCode = statusCode.substring(0, statusCode.length() - 1);
			}
			response.setStatusCode(statusCode);
			return response;
	 }

	 private String processOrder(final CCOrder order, final List<OrderModel> orderModels) throws SystemException
	 {
			Preconditions.checkNotNull(order);

			final OrderModel orderModel = tbsOrderService.getOrderForCode(order.getOrderNumber());

			final String orderStatusString = posHybrisOrderStatusMapping.get(order.getStatus());

			if (orderStatusString == null)
			{
				 return String.format("No order status mapping %s exists for order %s", order.getStatus(), order.getOrderNumber());
			}

			final OrderStatus orderStatus = enumerationService.getEnumerationValue(OrderStatus.class, orderStatusString);

			if (orderStatus == null)
			{
				 return String.format("No order status %s exists for order %s", order.getStatus(), order.getOrderNumber());
			}

			orderModel.setStatus(orderStatus);

			orderModels.add(orderModel);

			return processConsignments(orderModel,order,orderStatus);
	 }

	 private String processConsignments(OrderModel orderModel,CCOrder order,OrderStatus orderStatus)
	 {
	 	 final Optional<ConsignmentModel> clickAndCollectConsignment = orderModel.getConsignments().stream().filter(cons -> clickAndCollectConsignmentPredicate.test(cons))
				 .findFirst();

	 	 if (clickAndCollectConsignment.isPresent())
		 {
		 	 final ConsignmentStatus consignmentStatus = orderStatusConsignmentStatusMapping.get(orderStatus);
				if (consignmentStatus == null)
				{
					 return String.format("No consignment status mapping exists for orderstatus %s on order %s", order.getStatus(), order.getOrderNumber());
				}
		 	 final ConsignmentModel consignmentModel = clickAndCollectConsignment.get();
		 	 consignmentModel.setStatus(consignmentStatus);
			 modelService.save(consignmentModel);
		 	 return triggerConsignmentProcess(consignmentModel,orderModel);
		 }

	 	 return "No consignment found";
	 }

	 private String triggerConsignmentProcess(ConsignmentModel consignmentModel,OrderModel orderModel)
	 {
			final ConsignmentProcessModel consignmentProcess = Iterables.getFirst(consignmentModel.getConsignmentProcesses(),null);

			if (consignmentProcess != null)
			{
				 final boolean onWaitNode = consignmentProcess.getCurrentTasks().stream().filter(task -> task.getAction().equals(WAIT_STATUS_NODE))
						 .findFirst().isPresent();

				 if(onWaitNode)
				 {
				 	 businessProcessService.triggerEvent(consignmentProcess.getConsignment().getCode() + ORDER_STATUS_EVENT_SUFFIX);
				 	 return StringUtils.EMPTY;
				 }
				 else
				 {
				 	 final String error = String.format(CONSIGNMENT_NOT_IN_WAIT_NODE,orderModel.getCode());
				 	 LOG.error(error);
				 	 return error;
				 }
			}
			else
			{
				 final String error = String.format(NO_CONSIGNMENT_PROCESS_FOUND,orderModel.getCode());
				 LOG.error(error);
				 return error;
			}
	 }

}
