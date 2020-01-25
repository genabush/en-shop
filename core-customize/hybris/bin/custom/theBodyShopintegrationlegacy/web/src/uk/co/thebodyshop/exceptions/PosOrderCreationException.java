/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.exceptions;

public class PosOrderCreationException extends RuntimeException
{

    private final String messageId;

    public PosOrderCreationException(final String messageId, final String msg)
    {
        super(msg);
        this.messageId = messageId;

    }

    public PosOrderCreationException(final String messageId, final String msg, final Throwable t)
    {
        super(msg, t);
        this.messageId = messageId;
    }

    public String getMessageId()
    {
        return messageId;
    }
}
