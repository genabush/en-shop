/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.ws.outbound.sender.impl;

import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.Map;

import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import uk.co.thebodyshop.integration.ws.outbound.sender.MessageSender;


/**
 * Created by Mario Pio Gioiosa.
 */
public class DefaultMessageSender implements MessageSender
{

	private MessageChannel receiverChannel;

	@Override
	public void sendRequest(final Map<String, String> header, final Object payload)
	{
		ServicesUtil.validateParameterNotNull(header, "header can not be null");
		Message message = createMessageFromPayload(payload);
		message = MessageBuilder.fromMessage(message).copyHeaders(header).build();
		getReceiverChannel().send(message);
	}

	@Override
	public void sendRequest(final Object payload)
	{
		final Message message = createMessageFromPayload(payload);
		getReceiverChannel().send(message);
	}

	private Message createMessageFromPayload(final Object payload)
	{
		return MessageBuilder.withPayload(payload).build();
	}

	public MessageChannel getReceiverChannel()
	{
		return receiverChannel;
	}

	public void setReceiverChannel(final MessageChannel receiverChannel)
	{
		this.receiverChannel = receiverChannel;
	}

}
