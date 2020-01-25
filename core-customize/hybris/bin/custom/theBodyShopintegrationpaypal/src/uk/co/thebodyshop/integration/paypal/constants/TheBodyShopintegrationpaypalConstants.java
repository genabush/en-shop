/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.paypal.constants;

/**
 * Global class for all TheBodyShoppaypal constants. You can add global constants for your extension into this class.
 */
public final class TheBodyShopintegrationpaypalConstants extends GeneratedTheBodyShopintegrationpaypalConstants
{
	public static final String EXTENSIONNAME = "theBodyShopintegrationpaypal";

	public static final String PAYPAL_PAYMENT = "PAYPAL";

	private TheBodyShopintegrationpaypalConstants()
	{
		//empty to avoid instantiating this constant class
	}

	public interface PayPalIntent
	{
		 String AUTHORIZE = "AUTHORIZE";
		 String CAPTURE = "CAPTURE";
	}

	public interface PaypalLandingPage
	{
		 String LOGIN = "LOGIN";
		 String BILLING = "BILLING";
	}

	public interface ShippingPreference
	{
		 String GET_FROM_FILE = "GET_FROM_FILE";
		 String NO_SHIPPING = "NO_SHIPPING";
		 String SET_PROVIDED_ADDRESS = "SET_PROVIDED_ADDRESS";
	}

	// implement here constants used by this extension

	public static final String PLATFORM_LOGO_CODE = "theBodyShoppaypalPlatformLogo";
}
