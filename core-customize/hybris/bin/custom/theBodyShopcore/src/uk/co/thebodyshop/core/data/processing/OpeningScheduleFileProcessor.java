/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.data.processing;

import static java.util.Objects.isNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;

import de.hybris.platform.acceleratorservices.dataimport.batch.util.BatchDirectoryUtils;
import de.hybris.platform.basecommerce.enums.WeekDay;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.storelocator.model.OpeningDayModel;
import de.hybris.platform.storelocator.model.OpeningScheduleModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.storelocator.pos.PointOfServiceService;

import uk.co.thebodyshop.core.model.TbsWeekdayOpeningDayModel;
import uk.co.thebodyshop.core.services.OpeningScheduleService;

/**
 * @author Hemchand
 */
public class OpeningScheduleFileProcessor
{
	private static final Logger LOG = LoggerFactory.getLogger(OpeningScheduleFileProcessor.class);
	private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm";
	private static final String TIME_VALIDATION = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$";
	private static final String NAME_FORMAT = "-opening-schedule";
	public static final String STORE_ID = "storeID";
	private final ModelService modelService;
	private final PointOfServiceService pointOfServiceService;
	private final OpeningScheduleService tbsOpeningScheduleService;

	@ServiceActivator
	public void processFile(final File openingScheduleFile)
	{
		final String fileName = openingScheduleFile.getName();
		LOG.info("Processing the file [{}] for importing store data", fileName);
		Map<String, List<WeekDayOpeningData>> storeOpeningTimes = null;
		try (FileReader reader = new FileReader(openingScheduleFile); CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(reader);)
		{
			storeOpeningTimes = preProcess(parser.getRecords());
			for (final String storeId : storeOpeningTimes.keySet())
			{
				processOpeningSchedule(storeId, storeOpeningTimes.get(storeId));
			}
		}
		catch (final Exception e)
		{
			LOG.error("Exception importing a line from the store import file", e);
		}

		moveFileToArchiveFolder(openingScheduleFile);

		LOG.info("Successfully finished processing the file [{}]", fileName);
	}

	/**
	 * Setting list of opening timings for a store
	 */
	private Map<String, List<WeekDayOpeningData>> preProcess(final List<CSVRecord> records)
	{
		final Map<String, List<WeekDayOpeningData>> data = new HashMap<>();
		for (final CSVRecord record : records)
		{
			if (!record.get(STORE_ID).isBlank())
			{
				if (data.containsKey(record.get(STORE_ID)))
				{
					data.get(record.get(STORE_ID)).add(getWeekDayOpeningData(record));
				}
				else
				{
					final List<WeekDayOpeningData> wd = new ArrayList<>();
					wd.add(getWeekDayOpeningData(record));
					data.put(record.get(STORE_ID), wd);
				}
			}
			else
			{
				LOG.warn("StoreID is blank cannot importing the line." + record.getRecordNumber());
			}
		}
		return data;
	}

	private WeekDayOpeningData getWeekDayOpeningData(final CSVRecord record)
	{
		return new WeekDayOpeningData(record.get("dayOfWeek"), record.get("openingTime"), record.get("closingTime"));
	}

	private void processOpeningSchedule(final String storeId, final List<WeekDayOpeningData> weekDayOpeningData) throws ParseException
	{
		final PointOfServiceModel pos = getPointOfServiceService().getPointOfServiceForName(storeId);
		if (Objects.nonNull(pos))
		{

			List<OpeningDayModel> openingDayModelList = new ArrayList<>();
			final List<OpeningDayModel> updatedOpeningDayModelList = new ArrayList<>();

			final String market = pos.getBaseStore().getUid();
			OpeningScheduleModel openingSchedule = getTbsOpeningScheduleService().getOpeningSchedule(storeId + "-" + market + NAME_FORMAT);

			if (!isNull(openingSchedule))
			{
				openingDayModelList = new ArrayList<>(openingSchedule.getOpeningDays());
				for (final OpeningDayModel openingDayModel : openingDayModelList)
				{
					int count = 0;
					if (openingDayModel instanceof TbsWeekdayOpeningDayModel)
					{
						final TbsWeekdayOpeningDayModel weekdayOpeningDay = (TbsWeekdayOpeningDayModel) openingDayModel;
						for (final WeekDayOpeningData storeSchedule : weekDayOpeningData)
						{
							if (weekdayOpeningDay.getDayOfWeek().getCode().equalsIgnoreCase(storeSchedule.day))
							{
								count++;
							}
						}
						/**
						 * If the day is not found in the feed then updating it, else removing it and creating it with new timings.
						 */
						if (count == 0)
						{
							updatedOpeningDayModelList.add(openingDayModel);
						}
						else
						{
							getModelService().remove(weekdayOpeningDay);
						}
					}
				}
				updateSchedule(weekDayOpeningData, openingSchedule, updatedOpeningDayModelList);
			}
			else
			{
				openingSchedule = getModelService().create(OpeningScheduleModel.class);
				openingSchedule.setCode(storeId + "-" + market + NAME_FORMAT);
				updateSchedule(weekDayOpeningData, openingSchedule, openingDayModelList);
			}
			openingSchedule.setOpeningDays(updatedOpeningDayModelList);
			pos.setOpeningSchedule(openingSchedule);
			getModelService().saveAll(pos);
		}
		else
		{
			LOG.warn("Store does not exits with the ID :" + storeId);
		}
	}

	private void updateSchedule(final List<WeekDayOpeningData> weekDayOpeningData, final OpeningScheduleModel openingSchedule, final List<OpeningDayModel> updateOpeningDayModelList) throws ParseException
	{
		for (final WeekDayOpeningData weekDayOpening : weekDayOpeningData)
		{
			boolean dayofWeek = false;
			final TbsWeekdayOpeningDayModel tbsWeekdayOpeningDayModel = getModelService().create(TbsWeekdayOpeningDayModel.class);
			for (final WeekDay day : WeekDay.values())
			{
				if ((day.toString().equalsIgnoreCase(weekDayOpening.day)))
				{
					dayofWeek = true;
					tbsWeekdayOpeningDayModel.setDayOfWeek(day);
				}
			}
			if (getDateandTime(weekDayOpening.openingTime) != null && getDateandTime(weekDayOpening.closingTime) != null)
			{
				tbsWeekdayOpeningDayModel.setStoreOpeningTime(getDateandTime(weekDayOpening.openingTime));
				tbsWeekdayOpeningDayModel.setStoreClosingTime(getDateandTime(weekDayOpening.closingTime));
				if (dayofWeek)
				{
					tbsWeekdayOpeningDayModel.setOpeningSchedule(openingSchedule);
					getModelService().save(tbsWeekdayOpeningDayModel);
					updateOpeningDayModelList.add(tbsWeekdayOpeningDayModel);
				}
			}
		}
	}

	private void moveFileToArchiveFolder(final File openingScheduleFile)
	{
		// Moving file to the archive directory once processed
		try
		{
			final File archiveDir = new File(BatchDirectoryUtils.getRelativeArchiveDirectory(openingScheduleFile));
			FileUtils.moveFileToDirectory(openingScheduleFile, archiveDir, false);
		}
		catch (final IOException e)
		{
			LOG.error("Could not move the openingSchedule file [{}] to the archive directory", openingScheduleFile.getName());
			LOG.error("Exception occured moving the openingSchedule file to the archive directory", e);
		}
	}

	private Date getDateandTime(final String date) throws ParseException
	{
		Date storetimings = null;
		if (Pattern.matches(TIME_VALIDATION, date))
		{
			final SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);
			final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd");
			final Date presentdate = new Date();
			storetimings = dateFormat.parse(formatter.format(presentdate) + " " + date);
		}
		else
		{
			LOG.warn("Given date [{}] is not in the expected format [{}]", date, TIME_FORMAT);
		}
		return storetimings;
	}

	private class WeekDayOpeningData
	{
		String day;
		String openingTime;
		String closingTime;

		public WeekDayOpeningData(final String day, final String openingTime, final String closingTime)
		{
			this.day = day;
			this.openingTime = openingTime;
			this.closingTime = closingTime;
		}
	}

	public OpeningScheduleFileProcessor(final ModelService modelService, final PointOfServiceService pointOfServiceService, final OpeningScheduleService tbsOpeningScheduleService)
	{
		this.modelService = modelService;
		this.pointOfServiceService = pointOfServiceService;
		this.tbsOpeningScheduleService = tbsOpeningScheduleService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected PointOfServiceService getPointOfServiceService()
	{
		return pointOfServiceService;
	}

	protected OpeningScheduleService getTbsOpeningScheduleService()
	{
		return tbsOpeningScheduleService;
	}
}
