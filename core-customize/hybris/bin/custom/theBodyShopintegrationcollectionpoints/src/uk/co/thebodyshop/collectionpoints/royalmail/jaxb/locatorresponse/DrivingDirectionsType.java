//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.04.21 at 04:55:09 PM BST 
//


package uk.co.thebodyshop.collectionpoints.royalmail.jaxb.locatorresponse;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DrivingDirectionsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DrivingDirectionsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Directions" type="{}DirectionsType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="TotalDistance" type="{}TotalDistanceType"/>
 *         &lt;element name="TotalTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DrivingDirectionsType", propOrder = {
    "directions",
    "totalDistance",
    "totalTime"
})
public class DrivingDirectionsType {

    @XmlElement(name = "Directions")
    protected List<DirectionsType> directions;
    @XmlElement(name = "TotalDistance", required = true)
    protected TotalDistanceType totalDistance;
    @XmlElement(name = "TotalTime", required = true)
    protected String totalTime;

    /**
     * Gets the value of the directions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the directions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDirections().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DirectionsType }
     * 
     * 
     */
    public List<DirectionsType> getDirections() {
        if (directions == null) {
            directions = new ArrayList<DirectionsType>();
        }
        return this.directions;
    }

    /**
     * Gets the value of the totalDistance property.
     * 
     * @return
     *     possible object is
     *     {@link TotalDistanceType }
     *     
     */
    public TotalDistanceType getTotalDistance() {
        return totalDistance;
    }

    /**
     * Sets the value of the totalDistance property.
     * 
     * @param value
     *     allowed object is
     *     {@link TotalDistanceType }
     *     
     */
    public void setTotalDistance(TotalDistanceType value) {
        this.totalDistance = value;
    }

    /**
     * Gets the value of the totalTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalTime() {
        return totalTime;
    }

    /**
     * Sets the value of the totalTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalTime(String value) {
        this.totalTime = value;
    }

}
