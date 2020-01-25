/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.loyalty.data.processing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.integration.annotation.ServiceActivator;

import de.hybris.platform.acceleratorservices.dataimport.batch.util.BatchDirectoryUtils;
import de.hybris.platform.commerceservices.customer.CustomerService;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.BusinessException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.voucher.VoucherService;
import de.hybris.platform.voucher.model.PromotionVoucherModel;

import uk.co.thebodyshop.loyalty.enums.BenefitStatus;
import uk.co.thebodyshop.loyalty.model.LoyaltyCardModel;
import uk.co.thebodyshop.loyalty.model.LoyaltyVoucherModel;
import uk.co.thebodyshop.loyalty.service.LoyaltyService;

/**
 * @author Krishna
 */
public class LoyaltyFileProcessor
{
	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd";

	private static final String VOUCHER_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private static final String DELIMITER = "\\|";

	private static final String EXPIRE_TIME = " 23:59:59";

	private static final Logger LOG = Logger.getLogger(LoyaltyFileProcessor.class);

	private final ModelService modelService;

	private final CustomerService customerService;

	private final TimeService timeService;

	private final ConfigurationService configurationService;

	private final LoyaltyService loyaltyService;
	
	private static enum LOYALTY_FILE_HEADERS
	{
		CUSTOMERID, POINTSBALANCE, PROGRAMME, MEMBER_GROUP, MEMBER_LAST_UPDATE, MEMBER_STATUS, MEMBER_TIER, MEMBERSHIP_EXPIRY_DATE, BENEFITTYPE, DENEFIT_STATUS, BENEFITVALUE, CODE, REASON, EXPIRYDATE
	}

	private static enum BenefitTypeEnum
	{

		PROMOTION("PROMOTION"), LYB_VOUCHER("LYB-VOUCHER"), VOUCHER("VOUCHER");

		private final String value;

		private BenefitTypeEnum(final String value)
		{
			this.value = value;
		}

		public String getValue()
		{
			return value;
		}

		@Override
		public String toString()
		{
			return value;
		}

		public static String valuesString()
		{
			final StringBuilder builder = new StringBuilder();
			for (final BenefitTypeEnum val : values())
			{
				builder.append(val.getValue());
				builder.append(" ");
			}
			return builder.toString();
		}

	}

	@ServiceActivator
	public void uploadStockFile(final File loyaltyFile) throws IOException, BusinessException
	{
		LOG.info("Starting to process  file: " + loyaltyFile.getName());
		setSessionParameters();

		final BufferedReader reader = new BufferedReader(new FileReader(loyaltyFile));
		String line = null;

		final List<String> errors = new ArrayList<>();

		try
		{
			while (StringUtils.isNotBlank(line = reader.readLine()))
			{
				final List<String> cells = Arrays.asList(line.split(DELIMITER));
				if (cells != null && cells.size() < 2)
				{
					errors.add("Number of cells in line is not correct, should contains at least 2 cells, data: " + cells + ". Skipping line");
				}
				else
				{
					processLine(cells, errors);
				}
			}
		}
		finally
		{
			reader.close();
		}

		if (CollectionUtils.isNotEmpty(errors))
		{
			errors.forEach(error -> LOG.error(error));
		}

		moveFileToArchiveFolder(loyaltyFile);

		if (CollectionUtils.isNotEmpty(errors))
		{
			LOG.info("Finished processing loyalty file: " + loyaltyFile.getName() + ". Some errors were encountered whilst processing this file, please check error file for more information");
		}
		else
		{
			LOG.info("Successfully finished processing loyalty file: " + loyaltyFile.getName());
		}

	}


	private boolean processLine(final List<String> cells, final List<String> errors)
	{
		try
		{
			final String customerId = cells.get(0);
			final String pointsAsString = cells.get(1);
			String programme = null;
			String benefitType = null;
			String benefitValueAsString = null;
			String code = null;
			String reason = null;
			String expiryDateAsString = null;
			String lybExpiryDateAsString = null;
			String benefitStatus = null;
			String memberLastUpdateDateAsString = null;
			String memberStatus = null;
			String memberTier = null;
			String memberGroup = null;

			try
			{
				programme = cells.get(2);
				memberGroup = cells.get(3);
				memberLastUpdateDateAsString = cells.get(4);
				memberStatus = cells.get(5);
				memberTier = cells.get(6);
				lybExpiryDateAsString = cells.get(7);
				benefitType = cells.get(8);
				benefitStatus = cells.get(9);
				benefitValueAsString = cells.get(10);
				code = cells.get(11);
				reason = cells.get(12);
				expiryDateAsString = cells.get(13);

			}
			catch (final IndexOutOfBoundsException e)
			{
				// These are not mandatory fields so do nothing...
				LOG.debug(e.getMessage(), e);
			}

			UserModel user = null;
			int points = 0;
			double benefitValue = 0;
			Date benefitExpiryDate = null;
			Date lybMemberExpiryDate = null;
			try
			{
				user = getCustomerService().getCustomerByCustomerId(customerId);
			}
			catch (final IllegalArgumentException e)
			{
				LOG.debug(e.getMessage(), e);
				return errors.add("No " + LOYALTY_FILE_HEADERS.CUSTOMERID + " field provided for line " + cells + ", Skipping line ...");
			}
			catch (final UnknownIdentifierException e)
			{
				LOG.debug(e.getMessage(), e);
				return errors.add("No " + LOYALTY_FILE_HEADERS.CUSTOMERID + " with id [" + customerId + "] for line " + cells + ", Skipping line ...");
			}
			catch (final AmbiguousIdentifierException e)
			{
				LOG.debug(e.getMessage(), e);
				return errors.add("More than one customer with id [" + customerId + "] for line " + cells + ", Skipping line ...");
			}

			try
			{
				points = Integer.parseInt(pointsAsString);

				if (points < 0)
				{
					return errors.add("Specified points for User with " + LOYALTY_FILE_HEADERS.CUSTOMERID + " [" + customerId + "] is below 0, Skipping line ...");
				}
			}
			catch (final NumberFormatException e)
			{
				LOG.debug(e.getMessage(), e);
				return errors.add("Number format exception when tring to convert field " + LOYALTY_FILE_HEADERS.POINTSBALANCE + ", value was [" + pointsAsString + "] for line " + cells + ", Skipping line ...");
			}

			if (!(user instanceof CustomerModel))
			{
				return errors.add("User with " + LOYALTY_FILE_HEADERS.CUSTOMERID + " [" + customerId + "] is not of type customer, Skipping line ...");
			}

			// set loyalty points
			final CustomerModel customer = (CustomerModel) user;
			customer.setPointsBalance(Integer.valueOf(points));
			modelService.save(user);

			if (StringUtils.isBlank(programme))
			{
				// Only a points update line
				return true;
			}

			if (!programmeExistsAgainstUserLoyaltyCard(customer, programme))
			{
				return errors.add(LOYALTY_FILE_HEADERS.PROGRAMME + " [" + programme + "] does not exist against users loyalty cards, Skipping voucher/promotion creation... CustomerId: " + customerId);
			}

			final boolean isMemberExpirySuccessfulRequest = false;

			// update membership - start
			if (StringUtils.isNotBlank(lybExpiryDateAsString))
			{
				final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
				try
				{
					lybMemberExpiryDate = dateFormat.parse(lybExpiryDateAsString);
				}
				catch (final ParseException e)
				{
					LOG.debug(e.getMessage(), e);
					errors.add("Could not parse give date [" + lybExpiryDateAsString + "] for line " + cells + ", format should be [" + dateFormat.toPattern() + ", Skipping customer update... CustomerId: " + customerId);
				}
				if (lybMemberExpiryDate != null)
				{
					customer.setMemberExpiryDate(lybMemberExpiryDate);
					modelService.save(customer);
				}
			}

			manageTierParameters(memberLastUpdateDateAsString, memberStatus, memberTier, customer, cells, errors);

			// update membership - end

			final List<String> voucherUpdateErrors = new ArrayList<>();

			if (StringUtils.isBlank(benefitType))
			{
				voucherUpdateErrors.add(LOYALTY_FILE_HEADERS.BENEFITTYPE + " is empty " + cells + ", Skipping voucher/promotion creation... CustomerId: " + customerId);
			}
			if (!isBenefitTypeValid(benefitType))
			{
				voucherUpdateErrors.add(LOYALTY_FILE_HEADERS.BENEFITTYPE + " does not match one of the following values  " + BenefitTypeEnum.valuesString() + ", Skipping voucher/promotion creation... CustomerId: " + customerId);
			}

			if (StringUtils.isNotBlank(code) && StringUtils.isBlank(reason))
			{
				voucherUpdateErrors.add(LOYALTY_FILE_HEADERS.CODE + " is not empty but " + LOYALTY_FILE_HEADERS.REASON + " is empty " + cells + ", Skipping voucher/promotion creation... CustomerId: " + customerId);
			}

			if (StringUtils.isBlank(benefitValueAsString))
			{
				benefitValueAsString = "0";
			}

			try
			{
				benefitValue = Double.parseDouble(benefitValueAsString);
				if (benefitValue < 0)
				{
					voucherUpdateErrors.add("Specified " + LOYALTY_FILE_HEADERS.BENEFITVALUE + " cannot be less than 0: " + cells + ", Skipping voucher/promotion creation... CustomerId: " + customerId);
				}
			}
			catch (final NumberFormatException e)
			{
				LOG.debug(e.getMessage(), e);
				voucherUpdateErrors
						.add("Number format exception when tring to convert field " + LOYALTY_FILE_HEADERS.BENEFITVALUE + ", value was [" + benefitValueAsString + "] for line " + cells + ", Skipping voucher/promotion creation... CustomerId: " + customerId);
			}

			if (StringUtils.isNotBlank(expiryDateAsString))
			{
				final SimpleDateFormat dateFormat = new SimpleDateFormat(VOUCHER_DATE_TIME_FORMAT);
				try
				{
					benefitExpiryDate = dateFormat.parse(expiryDateAsString + EXPIRE_TIME);
				}
				catch (final ParseException e)
				{
					LOG.debug(e.getMessage(), e);
					voucherUpdateErrors.add("Could not parse give date [" + expiryDateAsString + "] for line " + cells + ", format should be [" + dateFormat.toPattern() + ", Skipping voucher/promotion creation... CustomerId: " + customerId);
				}
			}
			else
			{
				benefitExpiryDate = getDefaultExpiryDate();
			}

			if (CollectionUtils.isNotEmpty(voucherUpdateErrors))
			{
				if (isMemberExpirySuccessfulRequest)
				{
					return true;
				}
				else
				{
					errors.addAll(voucherUpdateErrors);
					return false;
				}
			}

			if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(reason) && StringUtils.isNotBlank(benefitType))
			{

				createVoucher(customer, code, reason, benefitValue, benefitExpiryDate, errors, cells, memberGroup, benefitStatus, benefitType);
			}

		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
			return errors.add("Unknown error when processing line " + cells + ", Error: [" + e.getMessage() + "]. Skipping line ...");
		}
		return true;
	}

	private boolean createVoucher(final CustomerModel customer, final String code, final String reason, final double benefitValue, final Date expiryDate, final List<String> errors, final List<String> cells, final String memberGroup,
			final String benefitStatus, final String benefitType)
	{

		LoyaltyVoucherModel loyaltyVoucher = getLoyaltyService().getLoyaltyVoucherForCode(code);
		LoyaltyVoucherModel existingVoucher = null;
		if (null != loyaltyVoucher)
		{
				if (loyaltyVoucher.getCustomer() != null && loyaltyVoucher.getCustomer().equals(customer))
				{
					existingVoucher = loyaltyVoucher;
				}
		}
		if (existingVoucher == null)
		{
			final LoyaltyVoucherModel voucher = modelService.create(LoyaltyVoucherModel.class);
			voucher.setCurrency(customer.getSessionCurrency());
			voucher.setCode(code + "_" + customer.getUid().replace("#", ""));
			voucher.setCustomer(customer);
			voucher.setDescription(reason, Locale.ENGLISH);
			voucher.setExpiryDate(expiryDate);
			voucher.setName(reason, Locale.ENGLISH);
			voucher.setValue(Double.valueOf(benefitValue));
			voucher.setVoucherCode(code);
			voucher.setStatus(BenefitStatus.ACTIVE);
			if (StringUtils.isNotBlank(benefitStatus))
			{
				final BenefitStatus benefitStatusObject = BenefitStatus.valueOf(benefitStatus);
				if (benefitStatusObject != null)
				{
					voucher.setStatus(benefitStatusObject);
				}
				else
				{
					errors.add("Invalid benefit status [" + benefitStatus + "] for voucher [" + code + "], setting status to _BLANK_");
				}
			}
			modelService.save(voucher);
		}
    else
    {
        if (StringUtils.isNotBlank(benefitType) && (benefitType.equals(BenefitTypeEnum.LYB_VOUCHER.getValue()) != existingVoucher.isLybVoucher()))
        {
            errors.add("Invalid benefit type [" + benefitType + "] for voucher [" + code + "], voucher will not be updated.");
            return false;
        }

        existingVoucher.setDescription(reason, Locale.ENGLISH);
        existingVoucher.setExpiryDate(expiryDate);
        existingVoucher.setName(reason, Locale.ENGLISH);
        existingVoucher.setValue(Double.valueOf(benefitValue));
        existingVoucher.setCurrency(customer.getSessionCurrency());
        modelService.save(existingVoucher);
    }

		return true;
	}

	private boolean programmeExistsAgainstUserLoyaltyCard(final CustomerModel customer, final String programme)
	{
		final LoyaltyCardModel card = customer.getDefaultLoyaltyCard();
		if (card == null)
		{
			return false;
		}

		if (card.getLoyaltyMembership() != null && programme.equalsIgnoreCase(card.getLoyaltyMembership().getProgrammeId()))
		{
			return true;
		}

		return false;
	}

	private boolean manageTierParameters(final String memberLastUpdateDateAsString, final String memberStatus, final String memberTier, final CustomerModel customer, final List<String> cells, final List<String> errors)
	{
		Date memberLastUpdateDate = null;
		if (atLeastOneEmpty(memberLastUpdateDateAsString, memberStatus, memberTier))
		{
			LOG.debug("Line ignored since bsStringUtils.atLeastOneEmpty(memberLastUpdateDateAsString,memberStatus,memberTier)");
		}
		else
		{
			final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
			try
			{
				memberLastUpdateDate = dateFormat.parse(memberLastUpdateDateAsString);
			}
			catch (final ParseException e)
			{
				LOG.debug(e.getMessage(), e);
				return errors.add("Could not parse give date [" + memberLastUpdateDateAsString + "] for line " + cells + ", format should be [" + dateFormat.toPattern() + ", Skipping customer update...");
			}

			customer.setMemberLastUpdateDate(memberLastUpdateDate);
			customer.setMemberTier(memberTier);
			customer.setMemberStatus(memberStatus);
			modelService.save(customer);
		}
		return true;
	}

	private boolean atLeastOneEmpty(final String... list)
	{
		if (list == null)
		{
			return false;
		}

		for (final String a : list)
		{
			if (StringUtils.isEmpty(a))
			{
				return true;
			}
		}

		return false;
	}

	public boolean isBenefitTypeValid(final String s)
	{
		for (final BenefitTypeEnum bt : BenefitTypeEnum.values())
		{
			if (bt.getValue().equals(s))
			{
				return true;
			}
		}
		return false;
	}

	private Date getDefaultExpiryDate()
	{
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(timeService.getCurrentTime());
		calendar.add(Calendar.DATE, getConfigurationService().getConfiguration().getInt("loyalty.feed.default.expiry.date.plus.days"));
		return calendar.getTime();
	}

	private void moveFileToArchiveFolder(final File loyaltyFile)
	{
		// Moving file to the archive directory once processed
		try
		{
			final File archiveDir = new File(BatchDirectoryUtils.getRelativeArchiveDirectory(loyaltyFile));
			FileUtils.moveFileToDirectory(loyaltyFile, archiveDir, false);
		}
		catch (final IOException e)
		{
			LOG.error("Error moving loyalty file with name: " + loyaltyFile.getName() + " to the archive directory. Error is: " + e.getMessage(), e);
		}
	}
	
	private void setSessionParameters()
  {
      // Activating master tenant
      Registry.activateMasterTenant();
  }
	

	public LoyaltyFileProcessor(final ModelService modelService,final CustomerService customerService, final TimeService timeService, final ConfigurationService configurationService, final LoyaltyService loyaltyService)
	{
		this.modelService = modelService;
		this.customerService = customerService;
		this.timeService = timeService;
		this.configurationService = configurationService;
		this.loyaltyService = loyaltyService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected TimeService getTimeService()
	{
		return timeService;
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	protected CustomerService getCustomerService()
	{
		return customerService;
	}
	
	protected LoyaltyService getLoyaltyService()
	{
		return loyaltyService;
	}
}
