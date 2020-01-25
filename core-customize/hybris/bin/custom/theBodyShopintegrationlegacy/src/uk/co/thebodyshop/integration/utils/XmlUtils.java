/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */

package uk.co.thebodyshop.integration.utils;

import java.io.ByteArrayOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * @author vasanthramprakasam
 */
public final class XmlUtils
{

	private static final Logger LOG = Logger.getLogger(XmlUtils.class);

	public static String toXml(JAXBElement element)
	{
		try
		{
			JAXBContext jc = JAXBContext.newInstance(element.getValue().getClass());
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			marshaller.marshal(element, byteArrayOutputStream);
			return byteArrayOutputStream.toString();
		}
		catch (Exception e)
		{
			LOG.error(e);
		}
		return StringUtils.EMPTY;
	}

}
