/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.languages;

import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;

import java.util.Collection;

public interface TbsStoreSessionFacade extends StoreSessionFacade {

    Collection<LanguageData> getFrontendDisplayLanguages();

}
