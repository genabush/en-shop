/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.order.impl;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.storelocator.pos.PointOfServiceService;

import uk.co.thebodyshop.core.collection.CollectionPointData;
import uk.co.thebodyshop.core.enums.FulfillmentMethodEnum;
import uk.co.thebodyshop.core.model.CollectionPointModel;
import uk.co.thebodyshop.core.model.StoreDetailModel;
import uk.co.thebodyshop.core.services.order.TbsAcceleratorCheckoutService;

/**
 * @author prateek.goel
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultTbsCheckoutFacadeTest
{
	@InjectMocks
	private DefaultTbsCheckoutFacade defaultTbsCheckoutFacade;

	@Mock
	private Converter<CollectionPointData, CollectionPointModel> collectionPointReverseConverter;

	@Mock
	private TbsAcceleratorCheckoutService tbsAcceleratorCheckoutService;

	@Mock
	private PointOfServiceService pointOfServiceService;

	@Mock
	private Converter<PointOfServiceData, StoreDetailModel> storeDetailReverseConverter;

	@Mock
	private CartModel cart;

	@Mock
	private CartFacade cartFacade;

	@Mock
	private CartService cartService;

	@Mock
	private PointOfServiceModel pos;

	@Mock
	private UserService userService;

	@Mock
	private CustomerModel customer;

	@Mock
	private ModelService modelService;

	@Mock
	private Converter<AddressModel, AddressData> addressConverter;

	@Mock
	private CustomerAccountService customerAccountService;

	List<PointOfServiceData> posList = new ArrayList<>();

	List<AddressModel> addressList = new ArrayList<>();

	@Mock
	private AbstractOrderModel abstractOrder;

	final PointOfServiceData pointofServiceData = new PointOfServiceData();

	@Before
	public void setup()
	{
		when(pointOfServiceService.getPointOfServiceForName(Mockito.anyString())).thenReturn(pos);
		when(cartFacade.hasSessionCart()).thenReturn(Boolean.TRUE);
		when(cartService.getSessionCart()).thenReturn(cart);
		when(userService.getCurrentUser()).thenReturn(customer);
		final AddressModel userAddress = new AddressModel();
		addressList.add(userAddress);
		when(cart.getDeliveryAddress()).thenReturn(userAddress);
		when(customerAccountService.getAllAddressEntries(customer)).thenReturn(addressList);
		when(addressConverter.convert(Mockito.anyObject())).thenReturn(new AddressData());
		pointofServiceData.setName("POS1");
		posList.add(pointofServiceData);

	}

	@Test
	public void testUpdateFulfillmentMethodifDirect()
	{
		defaultTbsCheckoutFacade.updateFulfillmentMethod(FulfillmentMethodEnum.DIRECT);
	}

	@Test
	public void testCollectionPointDeliveryAddress()
	{
		when(cart.getFulfillmentMethod()).thenReturn(FulfillmentMethodEnum.COLLECTION);
		Assert.assertNotNull(defaultTbsCheckoutFacade.getCollectionDeliveryAddress(FulfillmentMethodEnum.COLLECTION));
	}

	@Test
	public void testStoreDeliveryAddress()
	{
		when(cart.getFulfillmentMethod()).thenReturn(FulfillmentMethodEnum.COLLECTINSTORE);
		Assert.assertNotNull(defaultTbsCheckoutFacade.getCollectionDeliveryAddress(FulfillmentMethodEnum.COLLECTINSTORE));
	}

	@Test
	public void testSaveCollectionPointInfo()
	{
		when(collectionPointReverseConverter.convert(Mockito.anyObject(), Mockito.anyObject())).thenReturn(new CollectionPointModel());
		defaultTbsCheckoutFacade.addCollectionPointInfo(new CollectionPointData());
	}

	@Test
	public void testConsolidatedPickupOptionsifPOSNotPresent()
	{
		Assert.assertNull(defaultTbsCheckoutFacade.getConsolidatedPickupOptions(null));
	}

	@Test
	public void testConsolidatedPickupOptionsifPOSPresent()
	{
		when(tbsAcceleratorCheckoutService.getConsolidatedPickupOptions(Mockito.anyObject(), Mockito.anyList())).thenReturn(posList);
		Assert.assertNotNull(defaultTbsCheckoutFacade.getConsolidatedPickupOptions(posList));
	}

	@Test
	public void testSaveStoreDetails()
	{
		defaultTbsCheckoutFacade.setStoreDetails(pointofServiceData);
	}

	@Test
	public void testCartNotAvailableinResetDeliveryDetails()
	{
		when(cartService.getSessionCart()).thenReturn(null);
		Assert.assertFalse(defaultTbsCheckoutFacade.resetDeliveryDetails());
	}

	@Test
	public void testCartAvailableinResetDeliveryDetails()
	{
		Assert.assertTrue(defaultTbsCheckoutFacade.resetDeliveryDetails());
	}


}
