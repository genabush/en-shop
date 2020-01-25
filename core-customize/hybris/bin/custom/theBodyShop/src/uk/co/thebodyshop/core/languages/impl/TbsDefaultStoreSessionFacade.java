/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.languages.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.commercefacades.storesession.impl.DefaultStoreSessionFacade;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.store.BaseStoreModel;

import uk.co.thebodyshop.core.constants.YcommercewebservicesConstants;
import uk.co.thebodyshop.core.languages.TbsStoreSessionFacade;

public class TbsDefaultStoreSessionFacade extends DefaultStoreSessionFacade implements TbsStoreSessionFacade
{
	@Override
	public Collection<LanguageData> getFrontendDisplayLanguages()
	{
		final BaseStoreModel store = getBaseStoreService().getCurrentBaseStore();
		final Collection<LanguageModel> storeLanguages = store.getLanguages();
		Collection<LanguageModel> displayStoreLanguages;
		if (storeLanguages != null)
		{
			if (StringUtils.equals(store.getUid(), YcommercewebservicesConstants.TBS_GLOBAL_STORE))
			{
				displayStoreLanguages = storeLanguages.stream().filter(language -> StringUtils.equals(language.getIsocode(), YcommercewebservicesConstants.ISO_CODE_GLOBAL_LANGUAGE)).collect(Collectors.toList());
			}
			else
			{
				displayStoreLanguages = storeLanguages.stream().filter(language -> !StringUtils.equals(language.getIsocode(), YcommercewebservicesConstants.ISO_CODE_GLOBAL_LANGUAGE)).collect(Collectors.toList());
			}
		}
		else
		{
			displayStoreLanguages = Collections.EMPTY_LIST;
		}
		return Converters.convertAll(displayStoreLanguages, getLanguageConverter());
	}
}
