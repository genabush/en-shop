/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.populators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;

import uk.co.thebodyshop.core.model.TbsVariantProductModel;
import uk.co.thebodyshop.integration.jaxb.order.Order.Entries;
import uk.co.thebodyshop.integration.jaxb.order.Order.Entries.Entry;
import uk.co.thebodyshop.integration.jaxb.order.Order.Entries.Entry.GiftMessage;
import uk.co.thebodyshop.integration.jaxb.order.Order.Entries.Entry.LinePromotions;
import uk.co.thebodyshop.integration.jaxb.order.Order.Entries.Entry.Taxes;
import uk.co.thebodyshop.integration.jaxb.order.Order.Entries.Entry.Taxes.Tax;
import uk.co.thebodyshop.integration.jaxb.order.Promotion;

/**
 * @author prateek.goel
 */
public class PlaceOrderEntryPopulator implements Populator<OrderModel, Entries>
{

	@Override
	public void populate(final OrderModel source, final Entries target) throws ConversionException
	{
		for (final AbstractOrderEntryModel entry : source.getEntries())
		{
			target.getEntry().add(populateEntry(entry));
		}
	}

	private Entry populateEntry(final AbstractOrderEntryModel entry)
	{
		final Entry orderEntry = new Entry();
		orderEntry.setEntryNumber(entry.getEntryNumber().toString());
		orderEntry.setQuantity(entry.getQuantity().intValue());
		orderEntry.setUnitPrice(entry.getBasePrice());
		orderEntry.setLineTotal(entry.getTotalPrice());
		addProductDetails(entry, orderEntry);
		addDiscountDetails(entry, orderEntry);
		addTaxes(entry, orderEntry);
		return orderEntry;
	}

	private void addTaxes(final AbstractOrderEntryModel source, final Entry target)
	{
		final Collection<TaxValue> taxes = source.getTaxValues();
		if(CollectionUtils.isNotEmpty(taxes))
		{
			final Taxes lineTaxes = new Taxes();
			final List<Tax> taxList = new ArrayList<>();
			for(final TaxValue tax : taxes)
			{
				final Tax lineTax = new Tax();
				lineTax.setCode(tax.getCode());
				lineTax.setValue(tax.getValue());
				taxList.add(lineTax);
			}
			lineTaxes.getTax().addAll(taxList);
			target.setTaxes(lineTaxes);
		}
	}

	private void addDiscountDetails(final AbstractOrderEntryModel source, final Entry target)
	{
		final List<DiscountValue> discountList = source.getDiscountValues();
		if (CollectionUtils.isNotEmpty(discountList))
		{
			double totalDiscount = 0.00;
			final List<Promotion> promotionList = new ArrayList<>();
			for (final DiscountValue discount : discountList)
			{
				totalDiscount = totalDiscount + discount.getValue();
				final Promotion promotion = new Promotion();
				promotion.setCode(discount.getCode());
				promotion.setDiscountValue(discount.getValue());
				promotionList.add(promotion);
			}
			target.setLineDiscount(totalDiscount);
			final LinePromotions linePromotions = new LinePromotions();
			linePromotions.getPromotion().addAll(promotionList);
		}
	}

	private void addProductDetails(final AbstractOrderEntryModel source, final Entry target)
	{
		final TbsVariantProductModel variantProduct = (TbsVariantProductModel) source.getProduct();
		target.setProductType(variantProduct.getErpType());
		target.setArticleId(variantProduct.getCode());
		target.setUnit(variantProduct.getUnit().getCode());
		final AbstractOrderModel order = source.getOrder();
		if(null != order.getGiftWrapProduct())
		{
			target.setGiftWrapped(BooleanUtils.isTrue(variantProduct.getGiftWrap()));
			if (StringUtils.isNotBlank(order.getGiftMessage()))
			{
				final GiftMessage giftMessage = new GiftMessage();
				giftMessage.setFrom(order.getGiftMessageSenderName());
				giftMessage.setTo(order.getGiftMessageName());
				giftMessage.setMessage(order.getGiftMessage());
				target.setGiftMessage(giftMessage);
			}
		}

	}

}
