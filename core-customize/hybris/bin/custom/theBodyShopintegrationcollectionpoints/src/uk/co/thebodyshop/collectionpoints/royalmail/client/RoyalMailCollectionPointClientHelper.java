/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.collectionpoints.royalmail.client;

import de.hybris.platform.util.Config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import uk.co.thebodyshop.collectionpoints.royalmail.client.exception.XmlMarshellingException;
import uk.co.thebodyshop.collectionpoints.royalmail.data.CollectionPointRequestData;
import uk.co.thebodyshop.collectionpoints.royalmail.jaxb.getlcdeliverylocationsrequest.GetLCDeliveryLocationsRequest;
import uk.co.thebodyshop.collectionpoints.royalmail.jaxb.getlcdeliverylocationsresponse.GetLCDeliveryLocationsResponse;
import uk.co.thebodyshop.collectionpoints.royalmail.jaxb.lcdeliverylocations.GeoSpatialPosition;
import uk.co.thebodyshop.collectionpoints.royalmail.jaxb.lcdeliverylocations.IdentificationStructure;
import uk.co.thebodyshop.collectionpoints.royalmail.jaxb.lcdeliverylocations.IntegrationHeader;
import uk.co.thebodyshop.collectionpoints.royalmail.jaxb.lcdeliverylocations.ObjectFactory;
import uk.co.thebodyshop.collectionpoints.royalmail.jaxb.lcdeliverylocations.ReferenceDataType;
import uk.co.thebodyshop.collectionpoints.royalmail.jaxb.lcdeliverylocations.SystemNameType;

/**
 * Created by alexjollands on 21/09/2016.
 */
public class RoyalMailCollectionPointClientHelper
{

    private static final BigDecimal SEARCH_RADIUS = new BigDecimal(Config.getString("collection.point.royalmail.service.searchradius", "99"));

    private static final BigDecimal LOCAL_LOCATION = new BigDecimal(1);

    private static final String GEO_DETIC_SYSTEM_CODE = "WGS84";

    private static final String XML_VERSION_1_0 = "<?xml version=\"1.0\"?>";

    private static final String SOAP_WRAPPER_TOP = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Header></SOAP-ENV:Header><SOAP-ENV:Body>";

    private static final String SOAP_WRAPPER_BOTTOM = "</SOAP-ENV:Body></SOAP-ENV:Envelope>";

    private static final String SOAP_RESPONSE_TOP = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Body>";

    private static final String SOAP_RESPONSE_BOTTOM = "</soapenv:Body></soapenv:Envelope>";

    private static final Logger LOG = Logger.getLogger(RoyalMailCollectionPointClientHelper.class);

    @Resource(name = "collectionPointJaxb2Marshaller")
    private Jaxb2Marshaller jaxb2Marshaller;

    @Resource(name = "collectionPointJaxb2MarshallerResponse")
    private Jaxb2Marshaller jaxb2MarshallerResponse;

    public String createRequest(final CollectionPointRequestData requestData)
    {
        StringWriter strWriter = null;
        String request = null;
        try
        {
            final Marshaller requestMarshaller = jaxb2Marshaller.getJaxbContext().createMarshaller();
            requestMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            final GetLCDeliveryLocationsRequest deliveryLocationsRequest = prepareRequest(requestData);
            strWriter = new StringWriter();
            strWriter.append(XML_VERSION_1_0);
            strWriter.append(SOAP_WRAPPER_TOP);
            requestMarshaller.marshal(deliveryLocationsRequest, strWriter);
            strWriter.append(SOAP_WRAPPER_BOTTOM);
            strWriter.flush();
            strWriter.close();
            request = strWriter.getBuffer().toString();
        }
        catch (final JAXBException e)
        {
            LOG.error("Error while marshalling ", e);
            throw new XmlMarshellingException("Error while marshalling", e);
        }
        catch (final IOException e)
        {
            throw new RuntimeException("The StringWriter has not closed properly", e);
        }
        finally
        {
            try
            {
                if (strWriter != null)
                {
                    strWriter.close();
                    strWriter = null;
                }
            }
            catch (final Exception e)
            {
                throw new RuntimeException("StringWriter has not closed properly", e);
            }
        }
        return request;
    }

    private GetLCDeliveryLocationsRequest prepareRequest(final CollectionPointRequestData requestData)
    {
        final ObjectFactory requestObjectFactory = new ObjectFactory();
        final GetLCDeliveryLocationsRequest request = requestObjectFactory.createGetLCDeliveryLocationsRequest();
        populateRequest(request, requestData);
        return request;
    }

    private void populateRequest(final GetLCDeliveryLocationsRequest request, final CollectionPointRequestData requestData)
    {
		configureRequestCredentials(request, requestData.getUsername(), requestData.getPassword());
		roundLatLongValues(requestData);
		final GeoSpatialPosition coordinates = new GeoSpatialPosition();
		coordinates.setGeoDeticSystem(getGeoDeticSystem());
		coordinates.setLatitude(BigDecimal.valueOf(requestData.getLatitude()));
		coordinates.setLongitude(BigDecimal.valueOf(requestData.getLongitude()));
		request.setSearchPosition(coordinates);
		request.setRadius(SEARCH_RADIUS);
		request.setEstimatedDeliveryDate(getEstimatedDeliveryDate());
    }

    private void roundLatLongValues(final CollectionPointRequestData requestData)
    {
        requestData.setLatitude(Math.round(requestData.getLatitude() * 10000000d) / 10000000d);
        requestData.setLongitude(Math.round(requestData.getLongitude() * 10000000d) / 10000000d);
    }

    private SystemNameType getGeoDeticSystem()
    {
        final SystemNameType snt = new SystemNameType();
        final ReferenceDataType rdt = new ReferenceDataType();
        rdt.setCode(GEO_DETIC_SYSTEM_CODE);
        snt.setSystemNameCode(rdt);
        return snt;
    }

    private void configureRequestCredentials(final GetLCDeliveryLocationsRequest request, final String username, final String password)
    {
        final IntegrationHeader header = new IntegrationHeader();
        final IdentificationStructure id = new IdentificationStructure();
        id.setApplicationId(username);
        id.setTransactionId(UUID.randomUUID().toString().replaceAll("-", ""));
        header.setIdentification(id);
        request.setIntegrationHeader(header);
    }

    public GetLCDeliveryLocationsResponse createResponse(final String responseString)
    {
        final String cleanXMLResponse = cleanupResponseString(responseString);
        Unmarshaller unmarshaller;
        Object objResponse = null;
        try
        {
            unmarshaller = jaxb2MarshallerResponse.getJaxbContext().createUnmarshaller();
            final ByteArrayInputStream input = new ByteArrayInputStream(cleanXMLResponse.getBytes());
            objResponse = unmarshaller.unmarshal(input);
        }
        catch (final JAXBException e)
        {
            LOG.error("An error occured whilst unmarshalling.", e);
            throw new XmlMarshellingException("An error occured whilst unmarshalling.", e);
        }
        if (objResponse instanceof GetLCDeliveryLocationsResponse)
        {
            return (GetLCDeliveryLocationsResponse) objResponse;
        }
        else
        {
            throw new RuntimeException("The response is not of type GetLCDeliveryLocationsResponse.");
        }
    }

    private String cleanupResponseString(String responseString)
    {
        responseString = responseString.replace(SOAP_RESPONSE_TOP, "");
        responseString = responseString.replace(SOAP_RESPONSE_BOTTOM, "");
        return responseString;
    }

    // Returns an XMLGregorianCalendar with tomorrow's date
    private XMLGregorianCalendar getEstimatedDeliveryDate()
    {
        XMLGregorianCalendar calendar = null;
        final Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, 1);
        final String formatStyle = "yyyy-MM-dd";
        final DateFormat format = new SimpleDateFormat(formatStyle);
        try
        {
            calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(format.format(c.getTime()));
        }
        catch (final DatatypeConfigurationException exception)
        {
            LOG.debug(exception);
        }
        return calendar;
    }

}
