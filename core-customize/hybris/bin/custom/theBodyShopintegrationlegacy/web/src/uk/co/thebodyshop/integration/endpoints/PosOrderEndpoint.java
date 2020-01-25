/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.integration.endpoints;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.impersonation.ImpersonationContext;
import de.hybris.platform.commerceservices.impersonation.ImpersonationService;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.enums.PaymentStatus;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.product.UnitService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.google.common.base.Preconditions;

import uk.co.thebodyshop.core.constants.TheBodyShopCoreConstants;
import uk.co.thebodyshop.core.enums.Channel;
import uk.co.thebodyshop.core.services.TbsOrderService;
import uk.co.thebodyshop.core.services.TbsUserService;
import uk.co.thebodyshop.exceptions.PosOrderCreationException;
import uk.co.thebodyshop.exceptions.PosOrderDuplicateException;
import uk.co.thebodyshop.integration.jaxb.store.order.sales.AddressType;
import uk.co.thebodyshop.integration.jaxb.store.order.sales.Order;
import uk.co.thebodyshop.integration.jaxb.store.order.sales.Order.Entries;
import uk.co.thebodyshop.integration.jaxb.store.order.sales.Order.Entries.Entry;
import uk.co.thebodyshop.integration.jaxb.store.order.sales.Order.Payments;
import uk.co.thebodyshop.integration.jaxb.store.order.sales.Order.Payments.Payment;
import uk.co.thebodyshop.integration.jaxb.store.order.sales.Order.Taxes;
import uk.co.thebodyshop.integration.jaxb.store.order.sales.Order.Taxes.Tax;
import uk.co.thebodyshop.integration.jaxb.store.order.sales.OrderRequest;
import uk.co.thebodyshop.integration.jaxb.store.order.sales.OrderResponse;
import uk.co.thebodyshop.integration.jaxb.store.order.sales.ResponseDetail;
import uk.co.thebodyshop.integration.jaxb.store.order.sales.ResponseStatus;
import uk.co.thebodyshop.integration.jaxb.store.order.sales.ShippingAddressType;

import javax.annotation.Resource;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Profile("backend")
@Endpoint
public class PosOrderEndpoint
{

	private static final Logger LOG = Logger.getLogger(PosOrderEndpoint.class);

	private static final String TARGET_NAMESPACE = "http://thebodyshop/posOrderSchema";

	private static final String POS_CREATE_ORDER_SUCCESS = "POS_CREATE_ORDER_SUCCESS";

	private static final String POS_CREATE_ORDER_PREFIX = "POS_CREATE_ORDER";

	private static final String POS_CREATE_ORDER_REQ_VALIDATION = POS_CREATE_ORDER_PREFIX + "_VALIDATION";

	private static final String POS_CREATE_ORDER_EXIST = POS_CREATE_ORDER_PREFIX + "_EXIST";

	private static final String POS_CREATE_ORDER_NOT_FOUND = POS_CREATE_ORDER_PREFIX + "_NOT_FOUND";

	@Resource
	private BaseSiteService baseSiteService;

	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	private CommonI18NService commonI18NService;

	@Resource
	private ImpersonationService impersonationService;

	@Resource
	private TransactionTemplate transactionTemplate;

	@Resource
	private BaseStoreService baseStoreService;

	@Resource
	private ModelService modelService;

	@Resource
	private TbsOrderService tbsOrderService;

	@Resource
	private EnumerationService enumerationService;

	@Resource
	private UnitService  unitService;

	@Resource
	private ProductService productService;

	@Resource
	private TbsUserService tbsUserService;

	@PayloadRoot(localPart = "OrderRequest", namespace = TARGET_NAMESPACE)
	public @ResponsePayload OrderResponse placeOrders(final @RequestPayload OrderRequest request)
	{
		LOG.debug("Create OrderRequest received. Total number of order received [" + request.getOrder().size() + "]");
		int successfulCount = 0;
		int failureCount = 0;
		int duplicateCount = 0;
		final OrderResponse response = new OrderResponse();
		for (final Order order : request.getOrder())
		{
			String orderResponseMessageIdPostfix = "_" + order.getCode();
			if (StringUtils.isEmpty(orderResponseMessageIdPostfix))
			{
				orderResponseMessageIdPostfix = "_";
			}
			orderResponseMessageIdPostfix += Long.valueOf(System.currentTimeMillis()).toString();
			boolean result = false;
			try
			{
				result = processOrder(order);
			}
			catch (final PosOrderDuplicateException ex)
			{
				duplicateCount++;
				final ResponseDetail createSuccuessResponse = createSuccuessResponse();
				createSuccuessResponse.setMessageId(ex.getMessageId() + orderResponseMessageIdPostfix);
				createSuccuessResponse.setMessageDesc("Order [" + order.getCode() + "] Message Detail : " + ex.getMessage());
				response.getResponseDetail().add(createSuccuessResponse);
				LOG.debug("[" + duplicateCount + "] duplicate orders");
			}
			catch (final PosOrderCreationException poce)
			{
				failureCount++;
				final ResponseDetail createFailureResponse = createFailureResponse();
				createFailureResponse.setMessageId(poce + orderResponseMessageIdPostfix);
				createFailureResponse.setMessageDesc("Order [" + order.getCode() + "] Message Detail : " + poce.getMessage());
				response.getResponseDetail().add(createFailureResponse);
				LOG.debug("[" + failureCount + "] orders got rejected", poce);
			}
			if (result)
			{
				successfulCount++;
				final ResponseDetail createSuccuessResponse = createSuccuessResponse();
				createSuccuessResponse.setMessageId(POS_CREATE_ORDER_SUCCESS + orderResponseMessageIdPostfix);
				createSuccuessResponse.setMessageDesc("Order [" + order.getCode() + "] created successful");
				response.getResponseDetail().add(createSuccuessResponse);
				LOG.debug("[" + successfulCount + "] order got created successfully and [" + failureCount + "] order got rejected");
			}
		}
		LOG.debug("Create OrderRequest finished ...");
		return response;
	}

	private boolean processOrder(final Order order)
	{
		final BaseSiteModel baseSite = baseSiteService.getBaseSiteForUID(TheBodyShopCoreConstants.BASE_SITE_PREFIX + order.getMarket().toLowerCase());

		if (baseSite == null || StringUtils.isBlank(baseSite.getUid()))
		{
			LOG.debug("No BaseSite/BaseSite Id found for market [" + order.getMarket() + "].");
			throw new PosOrderCreationException(POS_CREATE_ORDER_REQ_VALIDATION, "No BaseSite/BaseSite Id found for market [" + order.getMarket() + "].");
		}

		String customerId = null;
		CustomerModel customer = null;
		if (order.getCustomer() != null && StringUtils.isNotBlank(order.getCustomer().getCustomerID()))
		{
			customer = tbsUserService.getCustomerByCustomerIdAndSite(order.getCustomer().getCustomerID(), (CMSSiteModel) baseSite);
			customerId = order.getCustomer().getCustomerID();
		}
		else if (StringUtils.isNotBlank(order.getLYBCCardNumber()))
		{
			customer = tbsUserService.getCustomerByLoyaltyCardNumber(order.getLYBCCardNumber());
		}

		if (null == customer)
		{
			LOG.debug("No Customer found for the request with order code : [" + order.getCode() + "].");
			throw new PosOrderCreationException(POS_CREATE_ORDER_REQ_VALIDATION, "No Customer found for the request with customer id : [" + customerId + "], LYBCCardNumber : [" + order.getLYBCCardNumber() + "] .");
		}

		final CatalogVersionModel onlineCatalog = getCatalogVersion(((CMSSiteModel) baseSite).getDefaultCatalog().getId(), ((CMSSiteModel) baseSite).getDefaultCatalog().getVersion());

		final ImpersonationContext ctx = new ImpersonationContext();

		ctx.setUser(customer);
		ctx.setCurrency(commonI18NService.getCurrency(order.getCurrency().toString()));
		ctx.setCatalogVersions(Arrays.asList(onlineCatalog));
		ctx.setSite(baseSite);
		return impersonationService.executeInContext(ctx, new ImpersonationService.Executor<Boolean, PosOrderCreationException>()
		{

			@Override
			public Boolean execute() throws ImpersonationService.Nothing
			{
				Preconditions.checkNotNull(order);

				return transactionTemplate.execute(new TransactionCallback<Boolean>()
				{

					@Override
					public Boolean doInTransaction(final TransactionStatus status)
					{
						final BaseSiteModel baseSite = baseSiteService.getBaseSiteForUID(TheBodyShopCoreConstants.BASE_SITE_PREFIX + order.getMarket().toLowerCase());
						final BaseStoreModel baseStoreModel = baseStoreService.getBaseStoreForUid(TheBodyShopCoreConstants.BASE_STORE_PREFIX + order.getMarket().toUpperCase() + TheBodyShopCoreConstants.BASE_STORE_POSTFIX);

						CustomerModel customer = null;
						if (order.getCustomer() != null && StringUtils.isNotBlank(order.getCustomer().getCustomerID()))
						{
							customer = tbsUserService.getCustomerByCustomerIdAndSite(order.getCustomer().getCustomerID(), (CMSSiteModel) baseSite);
						}
						else if (StringUtils.isNotBlank(order.getLYBCCardNumber()))
						{
							customer = tbsUserService.getCustomerByLoyaltyCardNumber(order.getLYBCCardNumber());
						}

						final CatalogVersionModel onlineCatalog = getCatalogVersion(((CMSSiteModel) baseSite).getDefaultCatalog().getId(), ((CMSSiteModel) baseSite).getDefaultCatalog().getVersion());
						final CurrencyModel currency = commonI18NService.getCurrency(order.getCurrency().toString());
						final OrderModel orderModel = populateOrder(order, baseSite, baseStoreModel, customer, currency);
						if (order.getEntries() != null)
						{
							final Entries entries = order.getEntries();
							final List<Entry> entry = entries.getEntry();
							if (CollectionUtils.isNotEmpty(entry))
							{
								for (final Entry each : entry)
								{
									populateOrderEntry(onlineCatalog, orderModel, each);
								}
							}
						}
						orderModel.setCalculated(Boolean.TRUE);
						modelService.save(orderModel);
						return Boolean.TRUE;
					}
				});
			}
		});
	}

	private OrderModel populateOrder(final Order order, final BaseSiteModel baseSite, final BaseStoreModel baseStoreModel, final CustomerModel customer, final CurrencyModel currency) throws PosOrderCreationException
	{
		OrderModel existingOrder = null;
		try
		{
			existingOrder = tbsOrderService.getOrderForCode(order.getCode());
		}
		catch (final SystemException e)
		{ // do nothing : this is check to make sure that order doesn't exist with the incoming code
			// if it does then do not create one and error this order
			LOG.info("Expected - No order found with order code : " + order.getCode());
		}

		if (existingOrder == null)
		{
			final OrderModel orderModel = modelService.create(OrderModel.class);
			orderModel.setCode(order.getCode());
			orderModel.setStore(baseStoreModel);
			orderModel.setUser(customer);
			orderModel.setSite(baseSite);
			final XMLGregorianCalendar date = order.getDate();
			if (null != date)
			{
				orderModel.setDate(date.toGregorianCalendar().getTime());
			}
			orderModel.setCurrency(currency);

			if (null != order.getStatus())
			{
				try
				{
					orderModel.setStatus(enumerationService.getEnumerationValue(OrderStatus.class, order.getStatus().toString()));
				}
				catch (final Exception e)
				{
					throw new PosOrderCreationException(POS_CREATE_ORDER_REQ_VALIDATION, e.getMessage(), e);
				}
			}
			if (null != order.getChannel())
			{
				try
				{
					orderModel.setSalesApplication(enumerationService.getEnumerationValue(SalesApplication.class, order.getChannel()));
				}
				catch (final Exception e)
				{
					if (order.getChannel().equalsIgnoreCase(Channel.POS.getCode()))
					{
						orderModel.setSalesApplication(SalesApplication.STORE);
					}
					else
					{
						throw new PosOrderCreationException(POS_CREATE_ORDER_REQ_VALIDATION, e.getMessage(), e);
					}
				}
			}

			orderModel.setAgentId(order.getAgentID());
			orderModel.setStoreId(order.getStoreID());

			populateTaxes(order, orderModel);
			populatePayment(order, orderModel, currency);
			populatePaymentAddress(order.getBillingAddress(), orderModel, customer);
			populateShippingAddress(order.getShippingAddress(), orderModel, customer);

			if (order.getTotalDiscount() >= 0)
			{
				orderModel.setTotalDiscounts(Double.valueOf(order.getTotalDiscount()));
			}

			orderModel.setNet(Boolean.valueOf(baseStoreModel.isNet()));

			if (baseStoreModel.isNet())
			{
				orderModel.setTotalPrice(Double.valueOf(order.getTotalNet()));
			}
			else
			{
				orderModel.setTotalPrice(Double.valueOf(order.getTotalGross()));
			}

			try
			{
				modelService.save(orderModel);
			}
			catch (ModelSavingException ex)
			{

				LOG.error(ex.getMessage(), ex);
				throw new PosOrderCreationException(POS_CREATE_ORDER_REQ_VALIDATION, ex.getMessage(), ex);
			}
			return orderModel;
		}
		else
		{
			LOG.debug("An existing order found with order code : " + order.getCode());
			throw new PosOrderDuplicateException(POS_CREATE_ORDER_EXIST, "An existing order found with order code : " + order.getCode());
		}

	}

	private void populatePaymentAddress(final AddressType billingAddress, final OrderModel orderModel, final CustomerModel customer)
	{
		if (null != billingAddress)
		{
			final AddressModel address = modelService.create(AddressModel.class);
			address.setOwner(customer);
			address.setLine1(billingAddress.getAddressLine1());
			address.setLine2(billingAddress.getAddressLine2());
			address.setTown(billingAddress.getCity());
			address.setCountry(commonI18NService.getCountry(billingAddress.getCountry()));
			address.setPostalcode(billingAddress.getPostCode());
			modelService.save(address);
			orderModel.setPaymentAddress(address);
		}

	}

	private void populateShippingAddress(final ShippingAddressType shippingAddress, final OrderModel orderModel, final CustomerModel customer)
	{
		if (null != shippingAddress)
		{
			final AddressModel address = modelService.create(AddressModel.class);
			address.setOwner(customer);
			address.setLine1(shippingAddress.getAddressLine1());
			address.setLine2(shippingAddress.getAddressLine2());
			address.setTown(shippingAddress.getCity());
			address.setCountry(commonI18NService.getCountry(shippingAddress.getCountry()));
			address.setPostalcode(shippingAddress.getPostCode());
			address.setFirstname(shippingAddress.getFirstName());
			address.setLastname(shippingAddress.getLastName());
			address.setEmail(shippingAddress.getEmailAddress());
			address.setPhone1(shippingAddress.getPhoneNumber());
			address.setCellphone(shippingAddress.getSMSNumber());
			modelService.save(address);
			orderModel.setDeliveryAddress(address);
		}

	}

	private void populateOrderEntry(final CatalogVersionModel onlineCatalog, final OrderModel orderModel, final Entry each)
	{
		final OrderEntryModel orderEntry = modelService.create(OrderEntryModel.class);
		orderEntry.setOrder(orderModel);
		ProductModel productForCode = null;
		try
		{
			productForCode = productService.getProductForCode(onlineCatalog, each.getArticleId());
		}
		catch (final Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
			throw new PosOrderCreationException(POS_CREATE_ORDER_NOT_FOUND, ex.getMessage());
		}
		orderEntry.setProduct(productForCode);
		orderEntry.setQuantity(Long.valueOf(String.valueOf(each.getQuantity())));
		orderEntry.setCalculated(Boolean.TRUE);
		orderEntry.setEntryNumber(Integer.valueOf(each.getEntryNumber()));
		orderEntry.setUnit(getUnitModel(each));
		orderEntry.setTotalPrice(each.getLineTotal());
		if (each.getUnitPrice() >= 0)
		{
			orderEntry.setBasePrice(Double.valueOf(each.getUnitPrice()));
		}

		modelService.save(orderEntry);
	}

	protected void populateTaxes(final Order order, final OrderModel orderModel)
	{
		final Taxes taxes = order.getTaxes();
		if (null != taxes)
		{
			final List<Tax> taxList = taxes.getTax();
			if (CollectionUtils.isNotEmpty(taxList))
			{
				double taxValue = 0.0d;
				for (final Tax tax : taxList)
				{
					taxValue += tax.getValue();
				}
				orderModel.setTotalTax(Double.valueOf(taxValue));
			}
		}
	}

	protected void populatePayment(final Order order, final OrderModel orderModel, final CurrencyModel currency)
	{
		final Payments payments = order.getPayments();
		if (null != payments)
		{
			final List<Payment> listOfPayment = payments.getPayment();

			if (CollectionUtils.isNotEmpty(listOfPayment))
			{
				final PaymentTransactionModel paymentTransaction = modelService.create(PaymentTransactionModel.class);
				final List<PaymentTransactionEntryModel> listOfPaymentTransactionModel = new ArrayList<PaymentTransactionEntryModel>(listOfPayment.size());
				double paymentTotal = 0.0d;

				for (final Payment payment : listOfPayment)
				{
					final PaymentTransactionEntryModel entry = modelService.create(PaymentTransactionEntryModel.class);
					entry.setType(enumerationService.getEnumerationValue(PaymentTransactionType.class, payment.getType()));
					entry.setTransactionStatus(payment.getStatus());
					entry.setCode(payment.getID());
					if (payment.getAmount() >= 0)
					{
						entry.setAmount(new BigDecimal(payment.getAmount()));
					}
					entry.setCurrency(currency);
					final XMLGregorianCalendar transactionDate = payment.getTransactionDate();
					if (null != transactionDate)
					{
						entry.setTime(transactionDate.toGregorianCalendar().getTime());
					}
					listOfPaymentTransactionModel.add(entry);
					paymentTotal += payment.getAmount();
				}
				paymentTransaction.setEntries(listOfPaymentTransactionModel);
				paymentTransaction.setCurrency(currency);
				paymentTransaction.setOrder(orderModel);
				orderModel.setPaymentCost(Double.valueOf(paymentTotal));
				orderModel.setPaymentStatus(PaymentStatus.PAID);// as this is store order, assume it is paid
				orderModel.setPaymentTransactions(Arrays.asList(paymentTransaction));
			}
		}
	}

	private UnitModel getUnitModel(final Entry payload)
	{
		UnitModel unitModel = null;
		if (StringUtils.isNotEmpty(payload.getUnit()))
		{
			try
			{
				unitModel = unitService.getUnitForCode(payload.getUnit());

			}
			catch (final Exception re)
			{
				final String errorMessage = "Unit of Measure [" + payload.getUnit() + "] received for product with code [" + payload.getArticleId() + "] is not present in the system.";
				LOG.error(errorMessage + re.getMessage(), re);
				throw new PosOrderCreationException(POS_CREATE_ORDER_REQ_VALIDATION, errorMessage);
			}
		}
		return unitModel;
	}


	private CatalogVersionModel getCatalogVersion(final String catalogId, final String version)
	{
		try
		{
			return catalogVersionService.getCatalogVersion(catalogId, version);
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new PosOrderCreationException(POS_CREATE_ORDER_REQ_VALIDATION, e.getMessage());
		}

	}

	private ResponseDetail createFailureResponse()
	{
		final ResponseDetail responseDetail = new ResponseDetail();
		responseDetail.setStatus(ResponseStatus.FAILURE);
		return responseDetail;
	}

	private ResponseDetail createSuccuessResponse()
	{
		final ResponseDetail responseDetail = new ResponseDetail();
		responseDetail.setStatus(ResponseStatus.SUCCESS);
		return responseDetail;
	}
}
