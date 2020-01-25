/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.dynamic.handler;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import uk.co.thebodyshop.core.model.MarkDownPriceRowModel;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;

/**
 * @author Jagadeesh
 */
public class MarkDownPricesAttributeHandler<T extends MarkDownPriceRowModel, MODEL extends TbsVariantProductModel> implements DynamicAttributeHandler<Set<T>, MODEL>
{

	private FlexibleSearchService flexibleSearchService;

	@Override
	public Set<T> get(final MODEL model)
	{
		final Set<MarkDownPriceRowModel> ownAndOtherMarkDownPriceRows = addWildCardsMarkDownPriceRows(model, getOwnMarkDownPriceRows(model));
		return (Set<T>) ownAndOtherMarkDownPriceRows;
	}

	private Set<MarkDownPriceRowModel> addWildCardsMarkDownPriceRows(final TbsVariantProductModel product, final Set<MarkDownPriceRowModel> markDownPriceRows)
	{
		final Set<MarkDownPriceRowModel> list = new HashSet<>(CollectionUtils.emptyIfNull(markDownPriceRows));
		list.addAll(getWildCardsMarkDownPriceRows(product));
		return list;
	}

	private Set<MarkDownPriceRowModel> getOwnMarkDownPriceRows(final TbsVariantProductModel product)
	{
		return product.getOwnMarkDownPrices();
	}

	private Collection<MarkDownPriceRowModel> getWildCardsMarkDownPriceRows(final TbsVariantProductModel product)
	{
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("SELECT {p:").append(MarkDownPriceRowModel.PK).append("} ");
		stringBuilder.append("FROM {").append(MarkDownPriceRowModel._TYPECODE).append(" AS p}");
		stringBuilder.append(" WHERE {p:").append(MarkDownPriceRowModel.PRODUCT).append("} ");
		stringBuilder.append(" = ?").append(MarkDownPriceRowModel.PRODUCT);
		final Map<String, Object> params = new HashMap<>();
		params.put(MarkDownPriceRowModel.PRODUCT, product);
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(stringBuilder.toString());
		searchQuery.addQueryParameters(params);
		searchQuery.setResultClassList(Collections.singletonList(MarkDownPriceRowModel.class));
		final SearchResult searchResult = flexibleSearchService.search(searchQuery);
		return searchResult.getResult();
	}

	private Set<T> filteroutWildCardsMarkDownPrices(final TbsVariantProductModel product, final Set<T> markDownPriceRows)
	{
		return markDownPriceRows == null ? null : markDownPriceRows.stream().filter((pdt) -> {
			return product.equals(pdt.getProduct());
		}).collect(Collectors.toSet());
	}

	@Override
	public void set(final MODEL model, final Set<T> ts)
	{
		final Set<T> own = filteroutWildCardsMarkDownPrices(model, ts);
		model.setOwnMarkDownPrices((Set<MarkDownPriceRowModel>) own);
	}

	protected FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

}
