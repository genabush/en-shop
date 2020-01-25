/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.backoffice.excel.translators;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.jalo.impex.Europe1RowTranslator;
import de.hybris.platform.impex.jalo.translators.AbstractValueTranslator;
import de.hybris.platform.impex.jalo.translators.CollectionValueTranslator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.DateRange;
import de.hybris.platform.util.StandardDateRange;

import uk.co.thebodyshop.core.jalo.MarkDownPriceMessage;
import uk.co.thebodyshop.core.jalo.MarkDownPriceRow;
import uk.co.thebodyshop.core.model.MarkDownPriceMessageModel;
import uk.co.thebodyshop.core.model.MarkDownPriceRowModel;;

/**
 * @author Jagadeesh
 */
public class MarkDownPriceTranslator extends CollectionValueTranslator
{
	private static final String FORMAT_DATE = "dd.MM.yyyy HH:mm:ss";

	private static final String CATALOGVERSION_QUERY = "SELECT {cv:" + CatalogVersionModel.PK + "} FROM {" + CatalogVersionModel._TYPECODE + " AS cv JOIN " + CatalogModel._TYPECODE + " AS c ON {cv:" + CatalogVersionModel.CATALOG + "}={c:"
			+ CatalogModel.PK
			+ "}} WHERE {c:" + CatalogModel.ID + "} = ?id AND {cv:" + CatalogVersionModel.VERSION + "} = ?version";

	private static final String MARKDOWNPRIEMESSAGE_QUERY = "SELECT {p:" + MarkDownPriceMessageModel.PK + "} FROM {" + MarkDownPriceMessageModel._TYPECODE + " AS p} WHERE {p:" + MarkDownPriceMessageModel.CODE + "} = ?code";

	private static final String PRODUCT_QUERY = "SELECT {p:" + MarkDownPriceRowModel.PK + "} FROM {" + MarkDownPriceRowModel._TYPECODE + " AS p} WHERE {p:" + MarkDownPriceRowModel.PRODUCT + "} = ?product";

	public MarkDownPriceTranslator()
	{
		super((CollectionType) TypeManager.getInstance().getType("MarkPriceRowCollectionType"), new MarkDownPriceTranslator.MarkDownPriceRowTranslator());
	}

	public MarkDownPriceTranslator(final AbstractValueTranslator elementTranslator)
	{
		super((CollectionType) TypeManager.getInstance().getType("MarkPriceRowCollectionType"), elementTranslator);
	}

	public static class MarkDownPriceRowTranslator extends Europe1RowTranslator
	{
		private final SimpleDateFormat simpleDateFormat;

		public MarkDownPriceRowTranslator()
		{
			this((SimpleDateFormat) null, (NumberFormat) null, (Locale) null);
		}

		public MarkDownPriceRowTranslator(final SimpleDateFormat dateFormat, final NumberFormat numberFormat, final Locale loc)
		{
			super(dateFormat, numberFormat, loc);
			simpleDateFormat = dateFormat;
		}

		@Override
		protected Object convertToJalo(final String valueExpr, final Item forItem)
		{
			try
			{
				final JaloSession jaloSession = Registry.getCurrentTenant().getActiveSession();
				final Product product = (Product) forItem;
				double price = 0.0D;
				Date startDate = null;
				Date endDate = null;
				String markDownMessageCode = null;
				CatalogVersion catalogVersion = null;
				MarkDownPriceMessage markDownPriceMessage = null;
				if (StringUtils.isNotEmpty(valueExpr))
				{
					final StandardDateRange dateRange = (StandardDateRange) parseDateRangeForMarkDownPrice(valueExpr);
					if (null != dateRange)
					{
						endDate = dateRange.getEnd();
						startDate = dateRange.getStart();
					}
					final String[] valueExprArray = valueExpr.split(";");
					final String productCatalogString = valueExprArray[0];
					final String[] productCatalogExprArray = productCatalogString.split(":");
					final String catalogId = productCatalogExprArray[1];
					final String catalogVersionName = productCatalogExprArray[2];
					if (StringUtils.isNotEmpty(catalogId) && StringUtils.isNotEmpty(catalogVersionName))
					{
						final Map<String, Object> catalogParams = new HashMap<>();
						catalogParams.put(CatalogModel.ID, catalogId);
						catalogParams.put(CatalogVersionModel.VERSION, catalogVersionName);
						final List<CatalogVersion> catalogList = Registry.getCurrentTenant().getActiveSession().getFlexibleSearch()
								.search(jaloSession.getSessionContext(), CATALOGVERSION_QUERY, catalogParams, Collections.singletonList(CatalogVersion.class), true, true, 0, -1).getResult();
						catalogVersion = CollectionUtils.isNotEmpty(catalogList) ? catalogList.get(0) : null;
					}
					final String priceValueString = valueExprArray[1];
					if (StringUtils.isNotEmpty(priceValueString))
					{
						price = new Double(priceValueString);
					}

					if (valueExprArray.length >= 4)
					{
						markDownMessageCode = valueExprArray[3];
						final Map<String, Object> param = new HashMap<>();
						param.put(MarkDownPriceMessageModel.CODE, markDownMessageCode);
						final List<MarkDownPriceMessage> list = Registry.getCurrentTenant().getActiveSession().getFlexibleSearch()
								.search(jaloSession.getSessionContext(), MARKDOWNPRIEMESSAGE_QUERY, param, Collections.singletonList(MarkDownPriceMessage.class), true, true, 0, -1).getResult();
						markDownPriceMessage = CollectionUtils.isNotEmpty(list) ? list.get(0) : null;
					}
				}
				final PK code = product.getPK();
				final Map<String, Object> params = new HashMap<>();
				params.put(MarkDownPriceRowModel.PRODUCT, code);
				final JaloSession session = Registry.getCurrentTenant().getActiveSession();
				final Collection<MarkDownPriceRow> markDownPrices = Registry.getCurrentTenant().getActiveSession().getFlexibleSearch()
						.search(session.getSessionContext(), PRODUCT_QUERY, params, Collections.singletonList(MarkDownPriceRow.class), true, true, 0, -1).getResult();
				final Iterator<MarkDownPriceRow> priceIterator = markDownPrices.iterator();
				MarkDownPriceRow markDownPriceRow;
				do
				{
					do
					{
						do
						{
							do
							{
								if (!priceIterator.hasNext())
								{
									return createMarkDownPriceRow(session, product, price, startDate, endDate, catalogVersion, markDownPriceMessage);
								}
								markDownPriceRow = priceIterator.next();
							}
							while (markDownPriceRow.getPriceAsPrimitive() != price);
						}
						while (markDownPriceRow.getStartTime() != startDate && !markDownPriceRow.getStartTime().equals(startDate));
					}
					while (markDownPriceRow.getEndTime() != endDate && !markDownPriceRow.getEndTime().equals(endDate));
				}
				while (markDownPriceRow.getMarkDownMessage() != markDownPriceMessage && (markDownPriceRow.getMarkDownMessage() == null || !markDownPriceRow.getMarkDownMessage().equals(markDownPriceMessage)));
				return markDownPriceRow;
			}
			catch (final JaloPriceFactoryException | IndexOutOfBoundsException ex)
			{
				ex.printStackTrace();
				return null;
			}
		}

		private MarkDownPriceRow createMarkDownPriceRow(final JaloSession session, final Product product, final double price, final Date startDate, final Date endDate, final CatalogVersion catalogVersion, final MarkDownPriceMessage markDownPriceMessage)
				throws JaloPriceFactoryException
		{
			MarkDownPriceRow result = null;
			try
			{
				result = (MarkDownPriceRow) ComposedType.newInstance(session.getSessionContext(), MarkDownPriceRow.class, new Object[]
						{ "product", product, "price", price, "startTime", startDate, "endTime", endDate, "catalogVersion", catalogVersion, "markDownMessage", markDownPriceMessage });
				return result;
			}
			catch (final JaloGenericCreationException jgce)
			{
				Throwable cause = jgce.getCause();
				if (cause == null)
				{
					cause = jgce;
				}
				if (cause instanceof RuntimeException)
				{
					throw (RuntimeException) cause;
				}
				else if (cause instanceof JaloPriceFactoryException)
				{
					throw (JaloPriceFactoryException) cause;
				}
				else
				{
					throw new JaloSystemException(cause);
				}
			}
			catch (final JaloAbstractTypeException jate)
			{
				throw new JaloSystemException(jate);
			}
			catch (final JaloItemNotFoundException jine)
			{
				throw new JaloSystemException(jine);
			}
		}

		protected DateRange parseDateRangeForMarkDownPrice(final String valueExpr)
		{
			final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_DATE);
			DateRange dateRange = null;
			final int startPosition = valueExpr.indexOf(91);
			final int endPosition = valueExpr.indexOf(93);
			boolean dateRangeIsWelldefined = true;
			if (startPosition != -1 || endPosition != -1)
			{
				dateRangeIsWelldefined = false;
				if (endPosition > startPosition)
				{
					final String dateRangeExpr = valueExpr.substring(startPosition + 1, endPosition);
					final int sepPos = dateRangeExpr.indexOf("to");
					if (sepPos != -1)
					{
						try
						{
							final String start = dateRangeExpr.substring(0, sepPos).trim();
							final String end = dateRangeExpr.substring(sepPos + 2, dateRangeExpr.length()).trim();
							final Date startDate = transformStartDate(simpleDateFormat.parse(start));
							final Date endDate = transformEndDate(simpleDateFormat.parse(end));
							dateRange = new StandardDateRange(startDate, endDate);
							dateRangeIsWelldefined = true;
						}
						catch (final ParseException var12)
						{
							var12.printStackTrace();
						}
					}
				}
			}

			if (!dateRangeIsWelldefined)
			{
				throw new JaloInvalidParameterException("Invalid daterange definition!", 123);
			}
			else
			{
				return dateRange;
			}
		}

		@Override
		protected String convertToString(final Object value)
		{
			final MarkDownPriceRowModel priceRow = (MarkDownPriceRowModel) value;
			final ProductModel product = priceRow.getProduct();
			final StringBuilder text = new StringBuilder();
			text.append(priceRow.getProduct().getCode()).append(" ");
			text.append(product.getCatalogVersion().getCatalog().getId()).append(" ");
			text.append(product.getCatalogVersion().getVersion()).append(" ");
			text.append(priceRow.getPrice()).append(" ");
			text.append(priceRow.getMarkDownMessage().getCode()).append(" ");
			if (priceRow.getStartTime() != null && priceRow.getEndTime() != null)
			{
				text.append('[');
				final DateFormat dateformat = this.getDateFormat();
				synchronized (dateformat)
				{
					text.append(dateformat.format(priceRow.getStartTime()));
				}
				text.append(",");
				synchronized (dateformat)
				{
					text.append(dateformat.format(priceRow.getEndTime()));
				}
				text.append(']');
			}
			return text.toString();
		}
	}

}
