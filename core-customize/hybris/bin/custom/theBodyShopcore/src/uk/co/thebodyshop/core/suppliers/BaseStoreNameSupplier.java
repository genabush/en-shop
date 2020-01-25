/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.suppliers;


import java.util.function.Supplier;

import de.hybris.platform.store.services.BaseStoreService;

/**
 * @author vasanthramprakasam
 */
public class BaseStoreNameSupplier implements Supplier<String>
{

	 private final BaseStoreService baseStoreService;

	 public BaseStoreNameSupplier(BaseStoreService baseStoreService)
	 {
			this.baseStoreService = baseStoreService;
	 }

	 @Override
	 public String get()
	 {
			return getBaseStoreService().getCurrentBaseStore().getName();
	 }

	 public BaseStoreService getBaseStoreService()
	 {
			return baseStoreService;
	 }
}
