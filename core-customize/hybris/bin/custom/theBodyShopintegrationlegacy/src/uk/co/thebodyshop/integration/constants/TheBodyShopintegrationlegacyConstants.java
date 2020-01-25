/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.constants;

@SuppressWarnings({"deprecation","squid:CallToDeprecatedMethod"})
public class TheBodyShopintegrationlegacyConstants extends GeneratedTheBodyShopintegrationlegacyConstants
{
	public static final String EXTENSIONNAME = "theBodyShopintegrationlegacy";
	
	private TheBodyShopintegrationlegacyConstants()
	{
		//empty
	}

	public interface SERVICE_RESPONSE_STATUS
	{

		String LOCKED = "LOCKED";

		String FAILED = "FAILED";
	}

	public interface RESPONSE_STATUS_INCOMING
	{
		String HANDSHAKE_STATUS_RECEIVED = "RECEIVED";

		String HANDSHAKE_STATUS_ERROR = "ERROR";
	}

	public static final String PAYLOAD_ID = "PAYLOAD_ID";

	public static final String ORDER_ACKNOWLEDGEMENT_CONFIRMATION_STATUS = "Y1";

}
