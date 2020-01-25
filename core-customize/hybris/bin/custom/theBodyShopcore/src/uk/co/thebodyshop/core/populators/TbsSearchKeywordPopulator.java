/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.core.populators;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import de.hybris.platform.cmsfacades.common.populator.LocalizedPopulator;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import uk.co.thebodyshop.cms.model.SearchKeywordModel;
import uk.co.thebodyshop.core.keywords.data.SearchKeyWordData;

/**
 * @author vasanthramprakasam
 */
public class TbsSearchKeywordPopulator implements Populator<SearchKeywordModel, SearchKeyWordData>
{

	private final LocalizedPopulator localizedPopulator;

	public TbsSearchKeywordPopulator(final LocalizedPopulator localizedPopulator)
	{
		this.localizedPopulator = localizedPopulator;
	}

	@Override
	public void populate(final SearchKeywordModel searchKeywordModel, final SearchKeyWordData searchKeyWordData) throws ConversionException
	{
		searchKeyWordData.setCode(searchKeywordModel.getCode());
		final Map<String, String> nameMap = Optional.ofNullable(searchKeyWordData.getName()).orElseGet(() -> getNewNameMap(searchKeyWordData));
		getLocalizedPopulator().populate( //
				(locale, value) -> nameMap.put(getLocalizedPopulator().getLanguage(locale), value), //
				(locale) -> searchKeywordModel.getName(locale));
	}

	protected Map<String, String> getNewNameMap(final SearchKeyWordData target)
	{
		target.setName(new LinkedHashMap<>());
		return target.getName();
	}

	protected LocalizedPopulator getLocalizedPopulator()
	{
		return localizedPopulator;
	}
}
