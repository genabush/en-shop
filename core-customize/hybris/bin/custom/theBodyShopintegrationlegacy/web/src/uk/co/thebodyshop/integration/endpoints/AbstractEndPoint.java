/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.endpoints;

import static uk.co.thebodyshop.integration.constants.TheBodyShopintegrationlegacyConstants.RESPONSE_STATUS_INCOMING.HANDSHAKE_STATUS_RECEIVED;
import static uk.co.thebodyshop.integration.constants.TheBodyShopintegrationlegacyConstants.RESPONSE_STATUS_INCOMING.HANDSHAKE_STATUS_ERROR;


import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import uk.co.thebodyshop.integration.constants.TheBodyShopintegrationlegacyConstants;


/**
 * @author vasanthramprakasam
 */
public abstract class AbstractEndPoint<Request,Response>
{

	protected static final Logger LOG = LoggerFactory.getLogger(AbstractEndPoint.class);


	public Response queueMessageAndSendHandShakeResponse(Request request)
	{
		final Class requestClass = request.getClass();

		LOG.debug("{} received...",requestClass.getSimpleName());

		final Response response = createResponse();

		final String payloadId = new StringBuilder(getFeedHeader()).append(new SimpleDateFormat("_dd-MMM-yyyy_kkmmssS").format(new Date())).toString();
		setMessageId(response,payloadId);

		try
		{
			final JAXBContext jaxbContext = JAXBContext.newInstance(requestClass);
			final Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			final StringWriter requestXmlWriter = new StringWriter();
			jaxbMarshaller.marshal(request, requestXmlWriter);
			final String requestXml = requestXmlWriter.toString();
			final Message message = MessageBuilder
					.withPayload(requestXml).setHeader(TheBodyShopintegrationlegacyConstants.PAYLOAD_ID, payloadId).build();

			getQueueChannel().send(message);

			setStatusCode(response,HANDSHAKE_STATUS_RECEIVED);
		}
		catch (final Exception e)
		{
			LOG.error(String.format("There was a problem processing message with Payload ID [{}]",payloadId), e);
			setStatusCode(response,HANDSHAKE_STATUS_ERROR);
		}

		LOG.debug("{} processed...",requestClass.getSimpleName());

		return response;
	}

	protected abstract String getFeedHeader();

	protected abstract QueueChannel getQueueChannel();

	protected abstract Response createResponse();

	protected abstract void setMessageId(Response response,String payloadId);

	protected abstract void setStatusCode(Response response,String statusCode);

}
