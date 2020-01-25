/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.cms.uniqueidentifier;

import de.hybris.platform.cmsfacades.data.ItemData;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueIdentifierConverter;

import uk.co.thebodyshop.cms.model.SearchKeywordModel;
import uk.co.thebodyshop.core.daos.TbsSearchKeywordDao;

/**
 * @author vasanthramprakasam
 */
public class DefaultSearchKeywordUniqueIdentifierConverter implements UniqueIdentifierConverter<SearchKeywordModel>
{

	private final TbsSearchKeywordDao tbsSearchKeywordDao;

	public DefaultSearchKeywordUniqueIdentifierConverter(TbsSearchKeywordDao tbsSearchKeywordDao)
	{
		this.tbsSearchKeywordDao = tbsSearchKeywordDao;
	}

	@Override
	public String getItemType()
	{
		return SearchKeywordModel._TYPECODE;
	}

	@Override
	public ItemData convert(SearchKeywordModel itemModel)
	{
		final ItemData itemData = new ItemData();
		itemData.setItemId(itemModel.getCode());
		itemData.setItemType(itemModel.getItemtype());
		itemData.setName(itemModel.getName());
		return itemData;
	}

	@Override
	public SearchKeywordModel convert(ItemData itemData)
	{
		final String code = itemData.getItemId();
		return tbsSearchKeywordDao.findKeywordForCode(code);
	}
}
