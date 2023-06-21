package com.reece.platform.eclipse.model.XML.common;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@NoArgsConstructor
@XmlType(propOrder = {"streetLineOne", "streetLineTwo", "streetLineThree", "city", "state", "postalCode", "country"})
public class Address {

    public Address(Address address) {
        this.streetLineOne = address.getStreetLineOne();
        this.streetLineTwo = address.getStreetLineTwo();
        this.streetLineThree = address.getStreetLineThree();
        this.city = address.getCity();
        this.state = address.getState();
        this.postalCode = address.getPostalCode();
        this.country = address.getCountry();
    }

    @XmlElement(name = "StreetLineOne")
    private String streetLineOne;

    @XmlElement(name = "StreetLineTwo")
    private String streetLineTwo;

    @XmlElement(name = "StreetLineThree")
    private String streetLineThree;

    @XmlElement(name = "City")
    private String city;

    @XmlElement(name = "State")
    private String state;

    @XmlElement(name = "PostalCode")
    private String postalCode;

    @XmlElement(name = "Country")
    private String country;
}
