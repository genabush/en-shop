/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.loyalty.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.user.UserService;

import uk.co.thebodyshop.loyalty.dao.LoyaltyCardDao;
import uk.co.thebodyshop.loyalty.enums.BenefitStatus;
import uk.co.thebodyshop.loyalty.model.LoyaltyVoucherModel;
import uk.co.thebodyshop.loyalty.service.impl.DefaultLoyaltyService;

/**
 * @author Krishna
 */
@UnitTest
public class DefaultLoyaltyServiceTest
{
	private static final String VOUCHER_CODE = "27844052002111472964";
	@InjectMocks
	private DefaultLoyaltyService loyaltyService;

	@Mock
	private UserService userService;

	@Mock
	private LoyaltyCardDao loyaltyCardDao;

	@Mock
	private CustomerModel currentUser;

	@Mock
	private LoyaltyVoucherModel loyaltyVoucherModel;

	private List<LoyaltyVoucherModel> loyaltyVouchers;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		when(userService.getCurrentUser()).thenReturn(currentUser);
		loyaltyVouchers = new ArrayList<LoyaltyVoucherModel>();
		loyaltyVouchers.add(loyaltyVoucherModel);
	}

	@Test
	public void testgetLoyaltyVouchersWhenResultsFound()
	{
		when(loyaltyCardDao.getLoyaltyVouchers(currentUser)).thenReturn(loyaltyVouchers);
		final List<LoyaltyVoucherModel> loyaltyVouchers = loyaltyService.getLoyaltyVouchers(currentUser);
		assertThat(loyaltyVouchers != null).isTrue();
	}

	@Test
	public void testgetLoyaltyVouchersWhenResultsNotFound()
	{
		when(loyaltyCardDao.getLoyaltyVouchers(currentUser)).thenReturn(null);
		final List<LoyaltyVoucherModel> loyaltyVouchers = loyaltyService.getLoyaltyVouchers(currentUser);
		assertThat(loyaltyVouchers == null).isTrue();
	}

	@Test
	public void testIfVoucherExistsUserLoyaltyVoucherForCode()
	{
		when(loyaltyCardDao.getUserLoyaltyVoucherForCode(VOUCHER_CODE, currentUser)).thenReturn(loyaltyVoucherModel);
		final LoyaltyVoucherModel loyaltyVoucher = loyaltyService.getUserLoyaltyVoucherForCode(VOUCHER_CODE, currentUser);
		assertThat(loyaltyVoucher != null).isTrue();
	}

	@Test
	public void testIfVoucherNotExistsUserLoyaltyVoucherForCode()
	{
		when(loyaltyCardDao.getUserLoyaltyVoucherForCode(VOUCHER_CODE, currentUser)).thenReturn(null);
		final LoyaltyVoucherModel loyaltyVoucher = loyaltyService.getUserLoyaltyVoucherForCode(VOUCHER_CODE, currentUser);
		assertThat(loyaltyVoucher == null).isTrue();
	}
	
	@Test
	public void testIfVoucherExistsLoyaltyVoucherForCode()
	{
		when(loyaltyCardDao.getLoyaltyVoucherForCode(VOUCHER_CODE)).thenReturn(loyaltyVoucherModel);
		final LoyaltyVoucherModel loyaltyVoucher = loyaltyService.getLoyaltyVoucherForCode(VOUCHER_CODE);
		assertThat(loyaltyVoucher != null).isTrue();
	}

	@Test
	public void testIfVoucherNotExistsLoyaltyVoucherForCode()
	{
		when(loyaltyCardDao.getLoyaltyVoucherForCode(VOUCHER_CODE)).thenReturn(null);
		final LoyaltyVoucherModel loyaltyVoucher = loyaltyService.getLoyaltyVoucherForCode(VOUCHER_CODE);
		assertThat(loyaltyVoucher == null).isTrue();
	}
}
