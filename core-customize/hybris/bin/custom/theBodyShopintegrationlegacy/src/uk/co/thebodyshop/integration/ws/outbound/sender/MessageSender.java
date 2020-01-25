/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.ws.outbound.sender;

import java.util.Map;

/**
 * Created by Mario Pio Gioiosa
 */
public interface MessageSender
{
    void sendRequest(Map<String, String> header, Object payload);

    void sendRequest(Object payload);
}
