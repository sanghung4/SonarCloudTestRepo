package com.reece.platform.eclipse.model.XML.EntityResponse;

import com.reece.platform.eclipse.model.XML.common.Address;
import com.reece.platform.eclipse.model.XML.common.BillTo;
import com.reece.platform.eclipse.model.XML.common.Branch;
import com.reece.platform.eclipse.model.XML.common.EmailAddressList;
import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "CustomerTypeList", "EntityName", "ShipToList", "Address", "EntityKeywords", "EntityNoteList", "ClassificationItemList", "DfltPriceClass", "ContactShortList", "EntityID", "EmailAddressList", "Credit", "CreditCardList", "Branch", "BillTo", "WebAddress", "EntityRemoteData", "POReleaseRequired" })
public class Entity {

//    public Entity(String entityName) {
//        this.setEntityName(entityName);
//    }

    @XmlElement(name = "CustomerTypeList")
    private CustomerTypeList CustomerTypeList;

    @XmlElement(name = "EntityName")
    private String EntityName;

    @XmlElement(name = "ShipToList")
    private ShipToList ShipToList;

    @XmlElement(name = "Address")
    private Address Address;

    @XmlElement(name = "EntityKeywords")
    private String EntityKeywords;

    @XmlElement(name = "EntityNoteList")
    private EntityNoteList EntityNoteList;

    @XmlElement(name = "ClassificationItemList")
    private ClassificationItemList ClassificationItemList;

    @XmlElement(name = "DfltPriceClass")
    private String DfltPriceClass;

    @XmlElement(name = "ContactShortList")
    private ContactShortList ContactShortList;

    @XmlElement(name = "EntityID")
    private String EntityID;

    @XmlElement(name = "EmailAddressList")
    private EmailAddressList EmailAddressList;

    @XmlElement(name = "Credit")
    private Credit Credit;

    @XmlElement(name = "CreditCardList")
    private CreditCardList CreditCardList;

    @XmlElement(name = "Branch")
    private Branch Branch;

    @XmlElement(name = "BillTo")
    private BillTo BillTo;

    @XmlElement(name = "WebAddress")
    private String WebAddress;

    @XmlElement(name = "EntityRemoteData")
    private EntityRemoteData EntityRemoteData;

    @XmlElement(name = "POReleaseRequired")
    private String POReleaseRequired;

    @Override
    public String toString()
    {
        return "ClassPojo [CustomerTypeList = "+CustomerTypeList+", EntityName = "+EntityName+", Address = "+Address+", EntityKeywords = "+EntityKeywords+", EntityNoteList = "+EntityNoteList+", ClassificationItemList = "+ClassificationItemList+", DfltPriceClass = "+DfltPriceClass+", ContactShortList = "+ContactShortList+", EntityID = "+EntityID+", EmailAddressList = "+EmailAddressList+", Credit = "+Credit+", CreditCardList = "+CreditCardList+",  Branch = "+Branch+", BillTo = "+BillTo+", WebAddress = "+WebAddress+", EntityRemoteData = "+EntityRemoteData+"]";
    }
}
