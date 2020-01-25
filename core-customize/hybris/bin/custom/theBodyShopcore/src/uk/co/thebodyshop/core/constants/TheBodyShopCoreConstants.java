/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package uk.co.thebodyshop.core.constants;

/**
 * Global class for all TheBodyShopCore constants. You can add global constants for your extension into this class.
 */
public final class TheBodyShopCoreConstants extends GeneratedTheBodyShopCoreConstants
{
	public static final String EXTENSIONNAME = "theBodyShopcore";


	private TheBodyShopCoreConstants()
	{
		//empty
	}

	// implement here constants used by this extension
	public static final String QUOTE_BUYER_PROCESS = "quote-buyer-process";
	public static final String QUOTE_SALES_REP_PROCESS = "quote-salesrep-process";
	public static final String QUOTE_USER_TYPE = "QUOTE_USER_TYPE";
	public static final String QUOTE_SELLER_APPROVER_PROCESS = "quote-seller-approval-process";
	public static final String QUOTE_TO_EXPIRE_SOON_EMAIL_PROCESS = "quote-to-expire-soon-email-process";
	public static final String QUOTE_EXPIRED_EMAIL_PROCESS = "quote-expired-email-process";
	public static final String QUOTE_POST_CANCELLATION_PROCESS = "quote-post-cancellation-process";


	public static final String PRODUCT_BADGES_COUNT_MAX = "product.badges.count.max";
	public static final int PRODUCT_BADGES_COUNT_MAX_DEFAULT = 4;

	public static final String DEFAULT_MEDIA_FOLDER = "default.media.images.folder";

	public static final String AMPLIENCE_ROOT_URL = "amplience.root_url";
	public static final String AMPLIENCE_MEDIA_URL_SUFFIX = "amplience.media.url.suffix";
	public static final String AMPLIENCE_IMPORT_MEDIA_FORMATS = "amplience.media.import.media.formats";
	public static final String AMPLIENCE_IMPORT_MEDIA_FORMAT_NORMAL = "amplience.media.import.media.format.normal";
	public static final String AMPLIENCE_IMPORT_MEDIA_FORMAT_THUMBNAIL = "amplience.media.import.media.format.thumbnail";
	public static final String AMPLIENCE_MEDIA_URL_VERSION = "?v=";

	public static final String CATEGORY_FIELD_SEARCH = "category";
	public static final String PO_VALIDATION_TEXT = "pobox.validation.restricted.words.list.";

	public static final String BASE_SITE_PREFIX = "base.site.prefix";
	public static final String BASE_STORE_PREFIX = "base.store.prefix";
	public static final String BASE_STORE_POSTFIX = "base.store.postfix";

	public static interface PaymentMode
	{

		public static interface Code
		{
			public static final String GiftCard = "giftcard";

			public static final String Card = "card";
		}
	}

}
