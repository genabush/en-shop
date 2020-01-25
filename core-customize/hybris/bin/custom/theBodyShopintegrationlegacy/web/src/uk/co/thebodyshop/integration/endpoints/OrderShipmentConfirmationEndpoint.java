/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.endpoints;

import javax.annotation.Resource;

import org.springframework.integration.channel.QueueChannel;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import uk.co.thebodyshop.integration.jaxb.order.shipmentconfirmation.OrderShipmentConfirmationRequest;
import uk.co.thebodyshop.integration.jaxb.order.shipmentconfirmation.OrderShipmentConfirmationResponse;


@Endpoint
public class OrderShipmentConfirmationEndpoint extends AbstractEndPoint<OrderShipmentConfirmationRequest,OrderShipmentConfirmationResponse>
{
	private static final String TARGET_NAMESPACE = "http://thebodyshop/orderShipmentConfirmationSchema";

	@Resource(name = "orderShipmentConfirmationXmlProcessingChannel")
	protected QueueChannel messageChannel;

	private static final String FEED_HEADER = "ORDER_SHIPMENT_CONFIRMATION_FEED";

	@PayloadRoot(localPart = "OrderShipmentConfirmationRequest", namespace = TARGET_NAMESPACE)
	public @ResponsePayload OrderShipmentConfirmationResponse orderShipmentConfirmationService(
			final @RequestPayload OrderShipmentConfirmationRequest orderShipmentConfirmationRequest)
	{
		return queueMessageAndSendHandShakeResponse(orderShipmentConfirmationRequest);
	}

	@Override
	protected OrderShipmentConfirmationResponse createResponse()
	{
		return new OrderShipmentConfirmationResponse();
	}

	@Override
	protected void setMessageId(OrderShipmentConfirmationResponse orderShipmentConfirmationResponse, String payloadId)
	{
		orderShipmentConfirmationResponse.setMessageId(payloadId);
	}

	@Override
	protected void setStatusCode(OrderShipmentConfirmationResponse orderShipmentConfirmationResponse, String statusCode)
	{
		orderShipmentConfirmationResponse.setStatusCode(statusCode);
	}

	@Override
	protected String getFeedHeader()
	{
		return FEED_HEADER;
	}

	@Override
	protected QueueChannel getQueueChannel()
	{
		return messageChannel;
	}
}
