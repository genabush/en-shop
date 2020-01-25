/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Iterables;

import de.hybris.platform.acceleratorservices.dataimport.batch.util.BatchDirectoryUtils;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.exceptions.BusinessException;
import de.hybris.platform.servicelayer.model.ModelService;

import uk.co.thebodyshop.core.services.TbsOrderService;
import uk.co.thebodyshop.integration.constants.TheBodyShopintegrationlegacyConstants;

/**
 * @author vasanthramprakasam
 */
public class OrderAcknowledgementProcessor
{
	 private static final String VALUES_DELIMITER = "\\|";

	 private static final Logger LOG = LoggerFactory.getLogger(OrderAcknowledgementProcessor.class);

	 private final TbsOrderService tbsOrderService;

	 private final ModelService modelService;

	 public OrderAcknowledgementProcessor(TbsOrderService tbsOrderService, ModelService modelService)
	 {
			this.tbsOrderService = tbsOrderService;
			this.modelService = modelService;
	 }

	 public void execute(final File orderAckFile) throws IOException, BusinessException
	 {
	 	  //TODO add stock processing
			LOG.info("Starting to process order acknowledgement file: " + orderAckFile.getName());
			final BufferedReader reader = new BufferedReader(new FileReader(orderAckFile));
			String line = null;

			final List<String> errors = new ArrayList<>();
			try
			{
				 while (StringUtils.isNotBlank(line = reader.readLine()))
				 {
						final List<String> cells = Arrays.asList(line.split(VALUES_DELIMITER));
						if (cells != null && cells.size() < 4)
						{
							 errors.add("Number of cells in line is not correct, should contains at least 4 cells, data: " + cells + ". Skipping line");
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

			moveFileToArchiveFolder(orderAckFile);

			if (CollectionUtils.isNotEmpty(errors))
			{
				 LOG.info("Finished processing order acknowledgement file: " + orderAckFile.getName() + ". Some errors were encountered whilst processing this file, please check error file for more information");
			}
			else
			{
				 LOG.info("Successfully finished processing order acknowledgement file: " + orderAckFile.getName());
			}
	 }



	 private boolean processLine(final List<String> cells, final List<String> errors)
	 {
			final String orderId = cells.get(1);
			final String status = cells.get(2);
			final StringBuilder datetime = new StringBuilder(cells.get(3));
			SimpleDateFormat sdf = null;
			if (cells.size() > 4 && StringUtils.isNotBlank(cells.get(4)))
			{
				 datetime.append(cells.get(4));
				 sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			}
			else
			{
				 sdf = new SimpleDateFormat("yyyyMMdd");
			}

			Date dateObject = new Date();
			try
			{
				 dateObject = sdf.parse(datetime.toString());
			}
			catch (final ParseException e)
			{
				 LOG.error(e.getMessage(), e);
				 errors.add("Error in parsing date and time of order acknowledgement for order:" + orderId);
			}
			if (TheBodyShopintegrationlegacyConstants.ORDER_ACKNOWLEDGEMENT_CONFIRMATION_STATUS.equals(status))
			{
				 try
				 {
						final OrderModel order = getTbsOrderService().getOrderForCode(orderId);
						if (order.getConfirmationDate() == null)
						{
							 order.setConfirmationDate(dateObject);

							 if (OrderStatus.SENT_TO_SAP.equals(order.getStatus()) || OrderStatus.FRAUD_CHECKED.equals(order.getStatus()) || OrderStatus.WAIT_FRAUD_MANUAL_CHECK.equals(order.getStatus()))
							 {
									order.setStatus(OrderStatus.ORDER_CONFIRMED);
									//TODO review later when doing multiple consignments
									ConsignmentModel consignmentModel = Iterables.getFirst(order.getConsignments(),null);
									if(consignmentModel != null)
									{
										 consignmentModel.setStatus(ConsignmentStatus.RECEIVED);
										 getModelService().save(consignmentModel);
									}
									getModelService().save(order);
							 }
							 else
							 {
									errors.add("Ignoring order" + orderId + " status change as is not valid for processing confirmation");
									LOG.error("Ignoring order" + orderId + " status change as is not valid for processing confirmation");
							 }
						}
						else
						{
							 errors.add("Ignoring order" + orderId + " as order is alrady confirmed");
							 LOG.error("Ignoring order" + orderId + " as order is alrady confirmation");
						}

				 }
				 catch (final Exception e)
				 {
						errors.add("Error in processing order: " + orderId + " can not find order with this id" + e.getMessage());
				 }
			}
			else
			{
				 errors.add("Error in processing order :" + orderId + " order status is " + status);
			}
			if (errors.isEmpty())
			{
				 return true;
			}
			return false;
	 }

	 private void moveFileToArchiveFolder(final File orderAckFile)
	 {
			// Moving file to the archive directory once processed
			try
			{
				 final File archiveDir = new File(BatchDirectoryUtils.getRelativeArchiveDirectory(orderAckFile));
				 FileUtils.moveFileToDirectory(orderAckFile, archiveDir, false);
			}
			catch (final IOException e)
			{
				 LOG.error("Error moving order ack file with name: " + orderAckFile.getName() + " to the archive directory. Error is: " + e.getMessage());
			}
	 }

	 protected TbsOrderService getTbsOrderService()
	 {
			return tbsOrderService;
	 }

	 protected ModelService getModelService()
	 {
			return modelService;
	 }
}
