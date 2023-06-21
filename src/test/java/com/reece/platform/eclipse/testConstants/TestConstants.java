package com.reece.platform.eclipse.testConstants;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.reece.platform.eclipse.model.DTO.*;
import com.reece.platform.eclipse.model.enums.OrderStatusEnum;
import com.reece.platform.eclipse.model.generated.WarehousePickTask;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestConstants {

    public final static String ENTITY_RESPONSE_ACCOUNT_ID_1 = "111111";
    public final static String ENTITY_RESPONSE_ACCOUNT_ID_2 = "222222";
    public final static String ENTITY_RESPONSE_BILL_TO_ID = "425275";
    public static final String SESSION_TOKEN = "sessionToken";

    public static ResponseEntity<String> SessionTokenResponse() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode obj = mapper.createObjectNode();
        obj.put("sessionToken", SESSION_TOKEN);
        return new ResponseEntity<>(obj.toString(), null, HttpStatus.OK);
    }

    public static String ValidSessionToken(){
        return "{\"sessionToken\": \"abcdefg\"}";
    }

    public static String ValidLogin(){
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE IDMS-XML SYSTEM \"http://www.eclipseinc.com/dtd/IDMS-XML.dtd\">\n" +
                "<IDMS-XML>\n" +
                "   <LoginSubmitResponse>\n" +
                "   <SessionId>12345</SessionId>\n" +
                "   </LoginSubmitResponse>\n" +
                "</IDMS-XML>\n";
    }

    public static String SubmitOrderResponse(OrderStatusEnum orderStatusEnum) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE IDMS-XML SYSTEM \"http://www.eclipseinc.com/dtd/IDMS-XML.dtd\">\n" +
                "<IDMS-XML>\n" +
                "   <SalesOrderSubmitResponse>\n" +
                "      <SessionID />\n" +
                "      <SalesOrder>\n" +
                "         <OrderHeader>\n" +
                "            <OrderID>S109962629</OrderID>\n" +
                "            <InvoiceNumber>0001</InvoiceNumber>\n" +
                "            <OrderStatus Code=\"" + orderStatusEnum.getCode() + "\">" + orderStatusEnum.getContent() + "</OrderStatus>\n" +
                "            <PrintStatus>N</PrintStatus>\n" +
                "            <OrderType>Open Order</OrderType>\n" +
                "            <QuoteStatus>ORDER</QuoteStatus>\n" +
                "            <PricingBranch>\n" +
                "               <Branch>\n" +
                "                  <BranchID>1003</BranchID>\n" +
                "                  <BranchName>CRRLLTN PLBG</BranchName>\n" +
                "                  <Address>\n" +
                "                     <StreetLineOne>2901 TRADE CTR</StreetLineOne>\n" +
                "                     <StreetLineTwo>Suite 150</StreetLineTwo>\n" +
                "                     <StreetLineThree />\n" +
                "                     <City>CARROLLTON</City>\n" +
                "                     <State>TX</State>\n" +
                "                     <PostalCode>75007-4652</PostalCode>\n" +
                "                     <Country />\n" +
                "                  </Address>\n" +
                "               </Branch>\n" +
                "            </PricingBranch>\n" +
                "            <ShippingBranch>\n" +
                "               <Branch>\n" +
                "                  <BranchID>1003</BranchID>\n" +
                "                  <BranchName>CRRLLTN PLBG</BranchName>\n" +
                "                  <Address>\n" +
                "                     <StreetLineOne>2901 TRADE CTR</StreetLineOne>\n" +
                "                     <StreetLineTwo>Suite 150</StreetLineTwo>\n" +
                "                     <StreetLineThree />\n" +
                "                     <City>CARROLLTON</City>\n" +
                "                     <State>TX</State>\n" +
                "                     <PostalCode>75007-4652</PostalCode>\n" +
                "                     <Country />\n" +
                "                  </Address>\n" +
                "               </Branch>\n" +
                "            </ShippingBranch>\n" +
                "            <OrderDate>\n" +
                "               <Date>03/08/2021</Date>\n" +
                "            </OrderDate>\n" +
                "            <OrderedBy>\n" +
                "               <Description>TheDude</Description>\n" +
                "            </OrderedBy>" +
                "            <RequiredDate>\n" +
                "               <Date>03/08/2021</Date>\n" +
                "            </RequiredDate>\n" +
                "            <ShipDate>\n" +
                "               <Date>03/08/2021</Date>\n" +
                "            </ShipDate>\n" +
                "            <InvoiceDueDate>\n" +
                "               <Date>04/30/2021</Date>\n" +
                "            </InvoiceDueDate>\n" +
                "            <LastUpdate>\n" +
                "               <DateTime>2021-03-08T09:20:04.000-06:00</DateTime>\n" +
                "            </LastUpdate>\n" +
                "            <BillTo>\n" +
                "               <Entity>\n" +
                "                  <EntityID>35648</EntityID>\n" +
                "                  <EntityName>HORIZON PLUMBING LTD</EntityName>\n" +
                "                  <Address>\n" +
                "                     <StreetLineOne>2706 W PIONEER PKWY</StreetLineOne>\n" +
                "                     <StreetLineTwo />\n" +
                "                     <StreetLineThree />\n" +
                "                     <City>ARLINGTON</City>\n" +
                "                     <State>TX</State>\n" +
                "                     <PostalCode>76013-5906</PostalCode>\n" +
                "                     <Country />\n" +
                "                  </Address>\n" +
                "               </Entity>\n" +
                "            </BillTo>\n" +
                "            <ShipTo>\n" +
                "               <Entity>\n" +
                "                  <EntityID>11336</EntityID>\n" +
                "                  <EntityName>HORIZON PLUMBING LTD SHOP</EntityName>\n" +
                "                  <Address>\n" +
                "                     <StreetLineOne>2706 W PIONEER PKWY</StreetLineOne>\n" +
                "                     <StreetLineTwo />\n" +
                "                     <StreetLineThree />\n" +
                "                     <City>ARLINGTON</City>\n" +
                "                     <State>TX</State>\n" +
                "                     <PostalCode>76013-5906</PostalCode>\n" +
                "                     <Country />\n" +
                "                  </Address>\n" +
                "               </Entity>\n" +
                "            </ShipTo>\n" +
                "            <ShippingInformation>\n" +
                "               <Address>\n" +
                "                  <StreetLineOne>123 Street</StreetLineOne>\n" +
                "                  <StreetLineTwo>APT 1</StreetLineTwo>\n" +
                "                  <StreetLineThree />\n" +
                "                  <City>Dallas</City>\n" +
                "                  <State>TX</State>\n" +
                "                  <PostalCode>75080</PostalCode>\n" +
                "                  <Country />\n" +
                "               </Address>\n" +
                "               <ShipVia>\n" +
                "                  <ShipViaID>WILL CALL</ShipViaID>\n" +
                "                  <Description>WC</Description>\n" +
                "                  <ValidXMLShipVia>Yes</ValidXMLShipVia>\n" +
                "               </ShipVia>\n" +
                "               <Instructions>these are instructions</Instructions>\n" +
                "            </ShippingInformation>\n" +
                "            <OutsideSalesPerson>\n" +
                "               <EclipseID>SB00040</EclipseID>\n" +
                "            </OutsideSalesPerson>\n" +
                "            <WrittenBy>\n" +
                "               <EclipseID>ECOMM</EclipseID>\n" +
                "            </WrittenBy>\n" +
                "            <Telephone>\n" +
                "               <Number>111-111-1111</Number>\n" +
                "            </Telephone>\n" +
                "            <EmailAddress>sdfkjbrasnt@test.com</EmailAddress>\n" +
                "            <Currency>US</Currency>\n" +
                "            <PaymentTerms>\n" +
                "               <Code>3%26TH</Code>\n" +
                "               <Discount>\n" +
                "                  <CashDiscount>\n" +
                "                     <Amount>0.00</Amount>\n" +
                "                     <Percentage>0.00</Percentage>\n" +
                "                     <Date>04/26/2021</Date>\n" +
                "                  </CashDiscount>\n" +
                "               </Discount>\n" +
                "               <Description>3% 26TH</Description>\n" +
                "            </PaymentTerms>\n" +
                "            <ProgressBilling>No</ProgressBilling>\n" +
                "            <SalesSource>Web Order Entry</SalesSource>\n" +
                "            <OrderSource>IDMS-XML</OrderSource>\n" +
                "         </OrderHeader>\n" +
                "         <LineItemList>\n" +
                "<LineItem>\n" +
                "                    <LineItemID>0</LineItemID>\n" +
                "                    <PartIdentifiers>\n" +
                "                        <EclipsePartNumber>42270</EclipsePartNumber>\n" +
                "                    </PartIdentifiers>\n" +
                "                    <QtyOrdered>\n" +
                "                        <Quantity UOM=\"ft\" UMQT=\"1\">500</Quantity>\n" +
                "                    </QtyOrdered>\n" +
                "<LineItemPrice>\n" +
                "                  <UnitPrice>0.230000000</UnitPrice>\n" +
                "                  <ListPrice>0.845</ListPrice>\n" +
                "                  <ExtendedPrice>115.000000000</ExtendedPrice>\n" +
                "               </LineItemPrice>" +
                "               <QtyShipped>\n" +
                "                  <Quantity UOM=\"ft\" UMQT=\"1\">500</Quantity>\n" +
                "               </QtyShipped>" +
                "                </LineItem>\n" +
                "                <LineItem>\n" +
                "                    <LineItemID>0</LineItemID>\n" +
                "                    <PartIdentifiers>\n" +
                "                        <EclipsePartNumber>42546</EclipsePartNumber>\n" +
                "                    </PartIdentifiers>\n" +
                "<LineItemPrice>\n" +
                "                  <UnitPrice>0.230000000</UnitPrice>\n" +
                "                  <ListPrice>0.845</ListPrice>\n" +
                "                  <ExtendedPrice>115.000000000</ExtendedPrice>\n" +
                "               </LineItemPrice>" +
                "               <QtyShipped>\n" +
                "                  <Quantity UOM=\"ft\" UMQT=\"1\">500</Quantity>\n" +
                "               </QtyShipped>" +
                "                    <QtyOrdered>\n" +
                "                        <Quantity UOM=\"ft\" UMQT=\"1\">100</Quantity>\n" +
                "                    </QtyOrdered>\n" +
                "                </LineItem>" +
                "         </LineItemList>\n" +
                "         <OrderTotals>\n" +
                "            <Subtotal>0.00</Subtotal>\n" +
                "            <Tax>0.00</Tax>\n" +
                "            <FederalExciseTax>0.00</FederalExciseTax>\n" +
                "            <Freight>0.00</Freight>\n" +
                "            <Handling>0.00</Handling>\n" +
                "            <Payment>\n" +
                "               <Amount>0.00</Amount>\n" +
                "            </Payment>\n" +
                "            <Discount>\n" +
                "               <CashDiscount>\n" +
                "                  <Amount>0.00</Amount>\n" +
                "                  <Percentage>0.00</Percentage>\n" +
                "                  <Date>04/26/2021</Date>\n" +
                "               </CashDiscount>\n" +
                "            </Discount>\n" +
                "            <Total>0.00</Total>\n" +
                "         </OrderTotals>\n" +
                "      </SalesOrder>\n" +
                "      <StatusResult Success=\"Yes\">\n" +
                "         <Description>Sales Order submitted successfully.</Description>\n" +
                "      </StatusResult>\n" +
                "   </SalesOrderSubmitResponse>\n" +
                "</IDMS-XML>";
    }

    public static String ProductResponse() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE IDMS-XML SYSTEM \"http://www.eclipseinc.com/dtd/IDMS-XML.dtd\">\n" +
                "<IDMS-XML>\n" +
                "    <MassProductInquiryResponse>\n" +
                "        <SessionID>J3526542259</SessionID>\n" +
                "        <ProductList>\n" +
                "            <Product>\n" +
                "                <Description>IMPNIP 2X3 BLK STL NIPP S40 (IBNKM)</Description>\n" +
                "                <AlternateDescription>IMPNIP 2X3 BLK STL NIPP S40</AlternateDescription>\n" +
                "                <PartIdentifiers>\n" +
                "                    <EclipsePartNumber>24020</EclipsePartNumber>\n" +
                "                    <UPCList>\n" +
                "                        <UPC PrimaryUPC=\"Yes\">08264710972</UPC>\n" +
                "                        <UPC PrimaryUPC=\"No\">082647109728</UPC>\n" +
                "                        <UPC PrimaryUPC=\"No\">03288816445</UPC>\n" +
                "                        <UPC PrimaryUPC=\"No\">69029113885</UPC>\n" +
                "                        <UPC PrimaryUPC=\"No\">092201274151</UPC>\n" +
                "                        <UPC PrimaryUPC=\"No\">82647109728</UPC>\n" +
                "                        <UPC PrimaryUPC=\"No\">032888100602</UPC>\n" +
                "                        <UPC PrimaryUPC=\"No\">80459595110</UPC>\n" +
                "                    </UPCList>\n" +
                "                    <CatalogNumber>IBNKM</CatalogNumber>\n" +
                "                    <ProductKeywords>GENERIC IMPORT NIPPLES IBNKM 2X3 BLK STL NIPP S40 0830037404 83NI4020030C 362380 ZNB083 588030 BLACK IMP 23BNI BN2X3 23BN BNLN IBN23 23IBN IBN 23 N 23N 3391 TPS.MERGE 2&quot; X 3&quot; BLACK NIPPLE ZNB083 2.3BN                                      MATCO NORCO BLACK BLK STEEL NIPPLES 2X3 2IN X 3IN STEELNIP STEEL NIPPLES 2NIPPLE TAX9013310 8535_LB.MERGE 2X3 BLACK NIPPLES BN23 67513501170 1400510 140 BN</ProductKeywords>\n" +
                "                    <Description>IMPNIP 2X3 BLK STL NIPP S40 (IBNKM)</Description>\n" +
                "                    <UserDefinedProductCodes>\n" +
                "                        <UserDefinedProductCode1>IBNKM</UserDefinedProductCode1>\n" +
                "                        <UserDefinedProductCode3>IMPNIP_IBNKM</UserDefinedProductCode3>\n" +
                "                        <UserDefinedProductCode4>BI53220_MATN</UserDefinedProductCode4>\n" +
                "                        <UserDefinedProductCode5>24020_WEB</UserDefinedProductCode5>\n" +
                "                        <UserDefinedProductCode9>24020</UserDefinedProductCode9>\n" +
                "                    </UserDefinedProductCodes>\n" +
                "                </PartIdentifiers>\n" +
                "                <AvailabilityList>\n" +
                "                    <BranchAvailability>\n" +
                "                        <Branch>\n" +
                "                            <BranchID>1012</BranchID>\n" +
                "                            <BranchName>AUSTIN SPRNG</BranchName>\n" +
                "                        </Branch>\n" +
                "                        <NowQuantity>\n" +
                "                            <Quantity UOM=\"ea\" UMQT=\"1\">8</Quantity>\n" +
                "                        </NowQuantity>\n" +
                "                        <EarliestMoreDate>\n" +
                "                            <Date>12/13/2022</Date>\n" +
                "                        </EarliestMoreDate>\n" +
                "                        <EarliestMoreQty>\n" +
                "                            <Quantity UOM=\"ea\" UMQT=\"1\">172</Quantity>\n" +
                "                        </EarliestMoreQty>\n" +
                "                        <PlentyDate>\n" +
                "                            <Date>01/28/2023</Date>\n" +
                "                        </PlentyDate>\n" +
                "                    </BranchAvailability>\n" +
                "                </AvailabilityList>\n" +
                "                <UOMList>\n" +
                "                    <UOM UMQT=\"1\">ea</UOM>\n" +
                "                </UOMList>\n" +
                "                <PricingUOM>\n" +
                "                    <UOM UMQT=\"1\">ea</UOM>\n" +
                "                </PricingUOM>\n" +
                "                <Pricing>\n" +
                "                    <EclipsePartNumber>24020</EclipsePartNumber>\n" +
                "                    <EntityID>6188</EntityID>\n" +
                "                    <PricingBranch>\n" +
                "                        <Branch>\n" +
                "                            <BranchID>1034</BranchID>\n" +
                "                            <BranchName>SAN ANT PLBG</BranchName>\n" +
                "                        </Branch>\n" +
                "                    </PricingBranch>\n" +
                "                    <Currency>US</Currency>\n" +
                "                    <Quantity UOM=\"ea\" UMQT=\"1\">1</Quantity>\n" +
                "                    <ListPrice>30.553</ListPrice>\n" +
                "                    <CustomerPrice>5.567000000</CustomerPrice>\n" +
                "                    <ExtendedPrice>5.567000000</ExtendedPrice>\n" +
                "                </Pricing>\n" +
                "                <Volume Unit=\"\">50.0000</Volume>\n" +
                "                <Weight Unit=\"\">0.8540</Weight>\n" +
                "                <BuyLine>DC_IMNIP</BuyLine>\n" +
                "                <PriceLine>IMPNIPP</PriceLine>\n" +
                "                <Status>Stock</Status>\n" +
                "                <IndexType>Primary</IndexType>\n" +
                "            </Product>\n" +
                "        </ProductList>\n" +
                "        <UnfilteredProductCount>1</UnfilteredProductCount>\n" +
                "        <StatusResult Success=\"Yes\">\n" +
                "            <Description>Mass Product Information retrieved successfully.</Description>\n" +
                "        </StatusResult>\n" +
                "    </MassProductInquiryResponse>\n" +
                "</IDMS-XML>";
    }

    public static String SubmitOrderPreviewResponse(OrderStatusEnum orderStatusEnum) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE IDMS-XML SYSTEM \"http://www.eclipseinc.com/dtd/IDMS-XML.dtd\">\n" +
                "<IDMS-XML>\n" +
                "   <SalesOrderSubmitPreviewResponse>\n" +
                "      <SessionID />\n" +
                "      <SalesOrder>\n" +
                "         <OrderHeader>\n" +
                "            <OrderStatus Code=\"" + orderStatusEnum.getCode() + "\">" + orderStatusEnum.getContent() + "</OrderStatus>\n" +
                "            <PrintStatus>N</PrintStatus>\n" +
                "            <OrderType>Open Order</OrderType>\n" +
                "            <QuoteStatus>ORDER</QuoteStatus>\n" +
                "            <PricingBranch>\n" +
                "               <Branch>\n" +
                "                  <BranchID>1003</BranchID>\n" +
                "                  <BranchName>CRRLLTN PLBG</BranchName>\n" +
                "                  <Address>\n" +
                "                     <StreetLineOne>2901 TRADE CTR</StreetLineOne>\n" +
                "                     <StreetLineTwo>Suite 150</StreetLineTwo>\n" +
                "                     <StreetLineThree />\n" +
                "                     <City>CARROLLTON</City>\n" +
                "                     <State>TX</State>\n" +
                "                     <PostalCode>75007-4652</PostalCode>\n" +
                "                     <Country />\n" +
                "                  </Address>\n" +
                "               </Branch>\n" +
                "            </PricingBranch>\n" +
                "            <ShippingBranch>\n" +
                "               <Branch>\n" +
                "                  <BranchID>1003</BranchID>\n" +
                "                  <BranchName>CRRLLTN PLBG</BranchName>\n" +
                "                  <Address>\n" +
                "                     <StreetLineOne>2901 TRADE CTR</StreetLineOne>\n" +
                "                     <StreetLineTwo>Suite 150</StreetLineTwo>\n" +
                "                     <StreetLineThree />\n" +
                "                     <City>CARROLLTON</City>\n" +
                "                     <State>TX</State>\n" +
                "                     <PostalCode>75007-4652</PostalCode>\n" +
                "                     <Country />\n" +
                "                  </Address>\n" +
                "               </Branch>\n" +
                "            </ShippingBranch>\n" +
                "            <OrderDate>\n" +
                "               <Date>03/08/2021</Date>\n" +
                "            </OrderDate>\n" +
                "            <OrderedBy>\n" +
                "               <Description>TheDude</Description>\n" +
                "            </OrderedBy>" +
                "            <RequiredDate>\n" +
                "               <Date>03/08/2021</Date>\n" +
                "            </RequiredDate>\n" +
                "            <ShipDate>\n" +
                "               <Date>03/08/2021</Date>\n" +
                "            </ShipDate>\n" +
                "            <InvoiceDueDate>\n" +
                "               <Date>04/30/2021</Date>\n" +
                "            </InvoiceDueDate>\n" +
                "            <LastUpdate>\n" +
                "               <DateTime>2021-03-08T09:20:04.000-06:00</DateTime>\n" +
                "            </LastUpdate>\n" +
                "            <BillTo>\n" +
                "               <Entity>\n" +
                "                  <EntityID>35648</EntityID>\n" +
                "                  <EntityName>HORIZON PLUMBING LTD</EntityName>\n" +
                "                  <Address>\n" +
                "                     <StreetLineOne>2706 W PIONEER PKWY</StreetLineOne>\n" +
                "                     <StreetLineTwo />\n" +
                "                     <StreetLineThree />\n" +
                "                     <City>ARLINGTON</City>\n" +
                "                     <State>TX</State>\n" +
                "                     <PostalCode>76013-5906</PostalCode>\n" +
                "                     <Country />\n" +
                "                  </Address>\n" +
                "               </Entity>\n" +
                "            </BillTo>\n" +
                "            <ShipTo>\n" +
                "               <Entity>\n" +
                "                  <EntityID>11336</EntityID>\n" +
                "                  <EntityName>HORIZON PLUMBING LTD SHOP</EntityName>\n" +
                "                  <Address>\n" +
                "                     <StreetLineOne>2706 W PIONEER PKWY</StreetLineOne>\n" +
                "                     <StreetLineTwo />\n" +
                "                     <StreetLineThree />\n" +
                "                     <City>ARLINGTON</City>\n" +
                "                     <State>TX</State>\n" +
                "                     <PostalCode>76013-5906</PostalCode>\n" +
                "                     <Country />\n" +
                "                  </Address>\n" +
                "               </Entity>\n" +
                "            </ShipTo>\n" +
                "            <ShippingInformation>\n" +
                "               <Address>\n" +
                "                  <StreetLineOne>123 Street</StreetLineOne>\n" +
                "                  <StreetLineTwo>APT 1</StreetLineTwo>\n" +
                "                  <StreetLineThree />\n" +
                "                  <City>Dallas</City>\n" +
                "                  <State>TX</State>\n" +
                "                  <PostalCode>75080</PostalCode>\n" +
                "                  <Country />\n" +
                "               </Address>\n" +
                "               <ShipVia>\n" +
                "                  <ShipViaID>WILL CALL</ShipViaID>\n" +
                "                  <Description>WC</Description>\n" +
                "                  <ValidXMLShipVia>Yes</ValidXMLShipVia>\n" +
                "               </ShipVia>\n" +
                "               <Instructions>these are instructions</Instructions>\n" +
                "            </ShippingInformation>\n" +
                "            <OutsideSalesPerson>\n" +
                "               <EclipseID>SB00040</EclipseID>\n" +
                "            </OutsideSalesPerson>\n" +
                "            <WrittenBy>\n" +
                "               <EclipseID>ECOMM</EclipseID>\n" +
                "            </WrittenBy>\n" +
                "            <Telephone>\n" +
                "               <Number>111-111-1111</Number>\n" +
                "            </Telephone>\n" +
                "            <EmailAddress>sdfkjbrasnt@test.com</EmailAddress>\n" +
                "            <Currency>US</Currency>\n" +
                "            <PaymentTerms>\n" +
                "               <Code>3%26TH</Code>\n" +
                "               <Discount>\n" +
                "                  <CashDiscount>\n" +
                "                     <Amount>0.00</Amount>\n" +
                "                     <Percentage>0.00</Percentage>\n" +
                "                     <Date>04/26/2021</Date>\n" +
                "                  </CashDiscount>\n" +
                "               </Discount>\n" +
                "               <Description>3% 26TH</Description>\n" +
                "            </PaymentTerms>\n" +
                "            <ProgressBilling>No</ProgressBilling>\n" +
                "            <SalesSource>Web Order Entry</SalesSource>\n" +
                "            <OrderSource>IDMS-XML</OrderSource>\n" +
                "         </OrderHeader>\n" +
                "         <LineItemList>\n" +
                "<LineItem>\n" +
                "                    <LineItemID>0</LineItemID>\n" +
                "                    <PartIdentifiers>\n" +
                "                        <EclipsePartNumber>42270</EclipsePartNumber>\n" +
                "                    </PartIdentifiers>\n" +
                "<LineItemPrice>\n" +
                "                  <UnitPrice>0.230000000</UnitPrice>\n" +
                "                  <ListPrice>0.845</ListPrice>\n" +
                "                  <ExtendedPrice>115.000000000</ExtendedPrice>\n" +
                "               </LineItemPrice>" +
                "                    <QtyOrdered>\n" +
                "                        <Quantity UOM=\"ft\" UMQT=\"1\">500</Quantity>\n" +
                "                    </QtyOrdered>\n" +
                "               <QtyShipped>\n" +
                "                  <Quantity UOM=\"ft\" UMQT=\"1\">500</Quantity>\n" +
                "               </QtyShipped>" +
                "                </LineItem>\n" +
                "                <LineItem>\n" +
                "                    <LineItemID>0</LineItemID>\n" +
                "                    <PartIdentifiers>\n" +
                "                        <EclipsePartNumber>42546</EclipsePartNumber>\n" +
                "                    </PartIdentifiers>\n" +
                "<LineItemPrice>\n" +
                "                  <UnitPrice>0.230000000</UnitPrice>\n" +
                "                  <ListPrice>0.845</ListPrice>\n" +
                "                  <ExtendedPrice>115.000000000</ExtendedPrice>\n" +
                "               </LineItemPrice>" +
                "               <QtyShipped>\n" +
                "                  <Quantity UOM=\"ft\" UMQT=\"1\">500</Quantity>\n" +
                "               </QtyShipped>" +
                "                    <QtyOrdered>\n" +
                "                        <Quantity UOM=\"ft\" UMQT=\"1\">100</Quantity>\n" +
                "                    </QtyOrdered>\n" +
                "                </LineItem>" +
                "         </LineItemList>\n" +
                "         <OrderTotals>\n" +
                "            <Subtotal>0.00</Subtotal>\n" +
                "            <Tax>0.00</Tax>\n" +
                "            <FederalExciseTax>0.00</FederalExciseTax>\n" +
                "            <Freight>0.00</Freight>\n" +
                "            <Handling>0.00</Handling>\n" +
                "            <Payment>\n" +
                "               <Amount>0.00</Amount>\n" +
                "            </Payment>\n" +
                "            <Discount>\n" +
                "               <CashDiscount>\n" +
                "                  <Amount>0.00</Amount>\n" +
                "                  <Percentage>0.00</Percentage>\n" +
                "                  <Date>04/26/2021</Date>\n" +
                "               </CashDiscount>\n" +
                "            </Discount>\n" +
                "            <Total>0.00</Total>\n" +
                "         </OrderTotals>\n" +
                "      </SalesOrder>\n" +
                "      <StatusResult Success=\"Yes\">\n" +
                "         <Description>Sales Order submitted successfully.</Description>\n" +
                "      </StatusResult>\n" +
                "   </SalesOrderSubmitPreviewResponse>\n" +
                "</IDMS-XML>";
    }

    public static String SalesOrderResponseFailure() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE IDMS-XML SYSTEM \"http://www.eclipseinc.com/dtd/IDMS-XML.dtd\">\n" +
                "<IDMS-XML>\n" +
                "  <SalesOrderInquiryResponse>\n" +
                "    <SessionID/>\n" +
                "    <StatusResult Success=\"No\">\n" +
                "      <Description>Error(s) encountered processing request.</Description>\n" +
                "      <ErrorMessageList>\n" +
                "        <ErrorMessage>\n" +
                "          <Code>527</Code>\n" +
                "          <Description>Insufficient access.</Description>\n" +
                "        </ErrorMessage>\n" +
                "      </ErrorMessageList>\n" +
                "    </StatusResult>\n" +
                "  </SalesOrderInquiryResponse>\n" +
                "</IDMS-XML>";
    }

    public static String SalesOrderResponseSuccess() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE IDMS-XML SYSTEM \"http://www.eclipseinc.com/dtd/IDMS-XML.dtd\">\n" +
                "<IDMS-XML>\n" +
                "  <SalesOrderInquiryResponse>\n" +
                "    <SessionID/>\n" +
                "    <SalesOrder>\n" +
                "      <OrderHeader>\n" +
                "        <OrderID>S108210380</OrderID>\n" +
                "        <InvoiceNumber>0001</InvoiceNumber>\n" +
                "        <OrderStatus Code=\"I\">Invoice # .001 * Direct *</OrderStatus>\n" +
                "        <PrintStatus>N</PrintStatus>\n" +
                "        <OrderType>Paid</OrderType>\n" +
                "        <CustomerPO>wr1805-w23115</CustomerPO>\n" +
                "        <CustomerReleaseNumber>1</CustomerReleaseNumber>\n" +
                "        <PricingBranch>\n" +
                "          <Branch>\n" +
                "            <BranchID>1302</BranchID>\n" +
                "            <BranchName>WINSTON-SALM</BranchName>\n" +
                "            <Address>\n" +
                "              <StreetLineOne>260 OLIVE STREET</StreetLineOne>\n" +
                "              <StreetLineTwo/>\n" +
                "              <StreetLineThree/>\n" +
                "              <City>WINSTON-SALEM</City>\n" +
                "              <State>NC</State>\n" +
                "              <PostalCode>27103-1625</PostalCode>\n" +
                "              <Country/>\n" +
                "            </Address>\n" +
                "          </Branch>\n" +
                "        </PricingBranch>\n" +
                "        <ShippingBranch>\n" +
                "          <Branch>\n" +
                "            <BranchID>1302</BranchID>\n" +
                "            <BranchName>WINSTON-SALM</BranchName>\n" +
                "            <Address>\n" +
                "              <StreetLineOne>260 OLIVE STREET</StreetLineOne>\n" +
                "              <StreetLineTwo/>\n" +
                "              <StreetLineThree/>\n" +
                "              <City>WINSTON-SALEM</City>\n" +
                "              <State>NC</State>\n" +
                "              <PostalCode>27103-1625</PostalCode>\n" +
                "              <Country/>\n" +
                "            </Address>\n" +
                "          </Branch>\n" +
                "        </ShippingBranch>\n" +
                "        <OrderDate>\n" +
                "          <Date>03/26/2020</Date>\n" +
                "        </OrderDate>\n" +
                "        <RequiredDate>\n" +
                "          <Date>03/26/2020</Date>\n" +
                "        </RequiredDate>\n" +
                "        <ShipDate>\n" +
                "          <Date>04/08/2020</Date>\n" +
                "        </ShipDate>\n" +
                "        <InvoiceDueDate>\n" +
                "          <Date>06/15/2020</Date>\n" +
                "        </InvoiceDueDate>\n" +
                "        <LastUpdate>\n" +
                "          <DateTime>2020-12-28T06:04:06.000-06:00</DateTime>\n" +
                "        </LastUpdate>\n" +
                "        <BillTo>\n" +
                "          <Entity>\n" +
                "            <EntityID>606800</EntityID>\n" +
                "            <EntityName>SPC MECHANICAL CORPORATION</EntityName>\n" +
                "            <Address>\n" +
                "              <StreetLineOne>PO BOX 3006</StreetLineOne>\n" +
                "              <StreetLineTwo/>\n" +
                "              <StreetLineThree/>\n" +
                "              <City>WILSON</City>\n" +
                "              <State>NC</State>\n" +
                "              <PostalCode>27895-3006</PostalCode>\n" +
                "              <Country/>\n" +
                "            </Address>\n" +
                "          </Entity>\n" +
                "        </BillTo>\n" +
                "        <ShipTo>\n" +
                "          <Entity>\n" +
                "            <EntityID>300964</EntityID>\n" +
                "            <EntityName>SPC MECHANICAL CORP 21436</EntityName>\n" +
                "            <Address>\n" +
                "              <StreetLineOne>FAB SHOP</StreetLineOne>\n" +
                "              <StreetLineTwo>3936 WESTPOINT BLVD</StreetLineTwo>\n" +
                "              <StreetLineThree/>\n" +
                "              <City>WINSTON-SALEM</City>\n" +
                "              <State>NC</State>\n" +
                "              <PostalCode>27103-6719</PostalCode>\n" +
                "              <Country/>\n" +
                "            </Address>\n" +
                "            <AlternateAddress>\n" +
                "              <StreetLineOne>P.O. BOX 25267</StreetLineOne>\n" +
                "              <StreetLineTwo/>\n" +
                "              <StreetLineThree/>\n" +
                "              <City>WINSTON SALEM</City>\n" +
                "              <State>NC</State>\n" +
                "              <PostalCode>27114-5267</PostalCode>\n" +
                "              <Country/>\n" +
                "            </AlternateAddress>\n" +
                "          </Entity>\n" +
                "        </ShipTo>\n" +
                "        <ShippingInformation>\n" +
                "          <Address>\n" +
                "            <StreetLineOne>FAB SHOP</StreetLineOne>\n" +
                "            <StreetLineTwo>3936 WESTPOINT BLVD</StreetLineTwo>\n" +
                "            <StreetLineThree/>\n" +
                "            <City>WINSTON-SALEM</City>\n" +
                "            <State>NC</State>\n" +
                "            <PostalCode>27103-6719</PostalCode>\n" +
                "            <Country/>\n" +
                "          </Address>\n" +
                "          <ShipVia>\n" +
                "            <ShipViaID>DIRECT</ShipViaID>\n" +
                "            <Description>DIR</Description>\n" +
                "            <ValidXMLShipVia>No</ValidXMLShipVia>\n" +
                "          </ShipVia>\n" +
                "          <FreightCharge>18.00</FreightCharge>\n" +
                "        </ShippingInformation>\n" +
                "        <OutsideSalesPerson>\n" +
                "          <EclipseID>RM04030</EclipseID>\n" +
                "        </OutsideSalesPerson>\n" +
                "        <InsideSalesPerson>\n" +
                "          <EclipseID>HOUSE</EclipseID>\n" +
                "        </InsideSalesPerson>\n" +
                "        <WrittenBy>\n" +
                "          <EclipseID>RM04030</EclipseID>\n" +
                "        </WrittenBy>\n" +
                "        <OrderedBy>\n" +
                "          <Description>jimmy sides</Description>\n" +
                "        </OrderedBy>\n" +
                "        <Telephone>\n" +
                "          <Number>336-785-9500</Number>\n" +
                "        </Telephone>\n" +
                "        <EmailAddress>karen.newman@spcmechanical.com</EmailAddress>\n" +
                "        <Currency>US</Currency>\n" +
                "        <PaymentTerms>\n" +
                "          <Code>22X10NET2X15</Code>\n" +
                "          <DiscountWrapper>\n" +
                "            <CashDiscount>\n" +
                "              <Amount>-1.21</Amount>\n" +
                "              <Percentage>1.99</Percentage>\n" +
                "              <Date>06/10/2020</Date>\n" +
                "            </CashDiscount>\n" +
                "          </DiscountWrapper>\n" +
                "          <Description>2% 2nd 10th, Net 2nd 15th</Description>\n" +
                "        </PaymentTerms>\n" +
                "        <ProgressBilling>No</ProgressBilling>\n" +
                "        <InternalNotes>***MUST HAVE VALID PO#***!!!</InternalNotes>\n" +
                "        <SalesSource>Inside</SalesSource>\n" +
                "        <OrderSource>Sales Order Entry</OrderSource>\n" +
                "      </OrderHeader>\n" +
                "      <LineItemList>\n" +
                "        <LineItem>\n" +
                "          <LineItemID>1</LineItemID>\n" +
                "          <PartIdentifiers>\n" +
                "            <EclipsePartNumber>1021819</EclipsePartNumber>\n" +
                "            <UPCList>\n" +
                "              <UPC PrimaryUPC=\"Yes\">72775309503</UPC>\n" +
                "              <UPC PrimaryUPC=\"No\">727753095038</UPC>\n" +
                "            </UPCList>\n" +
                "            <CatalogNumber>2005Y03 (-A05NB-U)</CatalogNumber>\n" +
                "            <ProductKeywords>JAY R SMITH MANUFACTURING 2005Y03 A05NBU 3 CI FLR DRN W5 NB ADJ STRNR Y 2005 WA05NB5 STRAINER WU VANDAL PROOF CAST IRON DRAIN DRAINS FLOOR</ProductKeywords>\n" +
                "            <Description>JRSMTH 3 CI FLR DRN W/5 NB ADJ STRNR 2005Y03 (-A05NB-U)</Description>\n" +
                "            <AlternateDescription>JRSMTH 3 CI FLR DRN W/5 NB ADJ STRNR</AlternateDescription>\n" +
                "            <UserDefinedProductCodes>\n" +
                "              <UserDefinedProductCode1>2005Y03 (-A05NB-U)</UserDefinedProductCode1>\n" +
                "              <UserDefinedProductCode3>JRSMTH_2005Y03(-A05NB-U)</UserDefinedProductCode3>\n" +
                "            </UserDefinedProductCodes>\n" +
                "          </PartIdentifiers>\n" +
                "          <LineItemPrice>\n" +
                "            <UnitPrice>60.682000000</UnitPrice>\n" +
                "            <ListPrice>535.000</ListPrice>\n" +
                "            <ExtendedPrice>60.682000000</ExtendedPrice>\n" +
                "          </LineItemPrice>\n" +
                "          <PriceUOM>ea</PriceUOM>\n" +
                "          <PricePerQty>1</PricePerQty>\n" +
                "          <QtyOrdered>\n" +
                "            <Quantity UOM=\"ea\" UMQT=\"1\">1</Quantity>\n" +
                "          </QtyOrdered>\n" +
                "          <QtyShipped>\n" +
                "            <Quantity UOM=\"ea\" UMQT=\"1\">1</Quantity>\n" +
                "          </QtyShipped>\n" +
                "        </LineItem>\n" +
                "        <LineItemCount>1</LineItemCount>\n" +
                "      </LineItemList>\n" +
                "      <OrderTotals>\n" +
                "        <Subtotal>60.68</Subtotal>\n" +
                "        <Tax>5.31</Tax>\n" +
                "        <FederalExciseTax>0.00</FederalExciseTax>\n" +
                "        <Freight>18.00</Freight>\n" +
                "        <Handling>0.00</Handling>\n" +
                "        <Payment>\n" +
                "          <Amount>-83.99</Amount>\n" +
                "          <Date>06/15/2020</Date>\n" +
                "        </Payment>\n" +
                "        <DiscountWrapper>\n" +
                "          <CashDiscount>\n" +
                "            <Amount>-1.21</Amount>\n" +
                "            <Percentage>1.99</Percentage>\n" +
                "            <Date>06/10/2020</Date>\n" +
                "          </CashDiscount>\n" +
                "        </DiscountWrapper>\n" +
                "        <Total>0.00</Total>\n" +
                "      </OrderTotals>\n" +
                "    </SalesOrder>\n" +
                "    <StatusResult Success=\"Yes\">\n" +
                "      <Description>Sales Order Information retrieved successfully.</Description>\n" +
                "    </StatusResult>\n" +
                "  </SalesOrderInquiryResponse>\n" +
                "</IDMS-XML>";
    }

    public static String AccountHistoryResponseFailure() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE IDMS-XML SYSTEM \"http://www.eclipseinc.com/dtd/IDMS-XML.dtd\">\n" +
                "<IDMS-XML>\n" +
                "    <AccountHistoryInquiryResponse>\n" +
                "        <SessionID></SessionID>\n" +
                "        <StatusResult Success=\"No\">\n" +
                "            <Description>Error(s) encountered processing request.</Description>\n" +
                "            <ErrorMessageList>\n" +
                "                <ErrorMessage>\n" +
                "                    <Code></Code>\n" +
                "                    <Description>EntityID could not be determined</Description>\n" +
                "                </ErrorMessage>\n" +
                "            </ErrorMessageList>\n" +
                "        </StatusResult>\n" +
                "    </AccountHistoryInquiryResponse>\n" +
                "</IDMS-XML>";
    }

    public static String OpenOrderResponseFailure() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE IDMS-XML SYSTEM \"http://www.eclipseinc.com/dtd/IDMS-XML.dtd\">\n" +
                "<IDMS-XML>\n" +
                "    <OpenOrderInquiryResponse>\n" +
                "        <SessionID></SessionID>\n" +
                "        <StatusResult Success=\"No\">\n" +
                "            <Description>Error(s) encountered processing request.</Description>\n" +
                "            <ErrorMessageList>\n" +
                "                <ErrorMessage>\n" +
                "                    <Code></Code>\n" +
                "                    <Description>EntityID could not be determined</Description>\n" +
                "                </ErrorMessage>\n" +
                "            </ErrorMessageList>\n" +
                "        </StatusResult>\n" +
                "    </OpenOrderInquiryResponse>\n" +
                "</IDMS-XML>";
    }

    public static String OpenOrderFailure() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE IDMS-XML SYSTEM \"http://www.eclipseinc.com/dtd/IDMS-XML.dtd\">\n" +
                "<IDMS-XML>\n" +
                "    <OpenOrderInquiryResponse>\n" +
                "        <SessionID></SessionID>\n" +
                "        <StatusResult Success=\"No\">\n" +
                "            <Description>Error(s) encountered processing request.</Description>\n" +
                "            <ErrorMessageList>\n" +
                "                <ErrorMessage>\n" +
                "                    <Code></Code>\n" +
                "                    <Description>EntityID could not be determined</Description>\n" +
                "                </ErrorMessage>\n" +
                "            </ErrorMessageList>\n" +
                "        </StatusResult>\n" +
                "    </OpenOrderInquiryResponse>\n" +
                "</IDMS-XML>";
    }

    public static String AccountHistoryResponseSuccess() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE IDMS-XML SYSTEM \"http://www.eclipseinc.com/dtd/IDMS-XML.dtd\">\n" +
                "<IDMS-XML>\n" +
                "   <AccountHistoryInquiryResponse>\n" +
                "       <SessionID></SessionID>\n" +
                "           <AccountHistoryItemList>\n" +
                "               <AccountHistoryItem>\n" +
                "                   <AccountRegisterID>S109945932.001</AccountRegisterID>\n" +
                "                   <OrderID>S109945932</OrderID>\n" +
                "                   <InvoiceNumber>4</InvoiceNumber>\n" +
                "                   <OrderStatus Code=\"I\">Invoice # .001 * Direct *</OrderStatus>\n" +
                "                   <PrintStatus>P</PrintStatus>\n" +
                "                   <CustomerPO>WR1805-8710</CustomerPO>\n" +
                "                   <CustomerReleaseNumber>clay</CustomerReleaseNumber>\n" +
                "                   <Description>PO# WR1805-8710</Description>\n" +
                "                   <BranchID>1303</BranchID>\n" +
                "                   <OrderDate>\n" +
                "                       <Date>01/14/2021</Date>\n" +
                "                   </OrderDate>\n" +
                "                   <PostDate>\n" +
                "                       <Date>01/18/2021</Date>\n" +
                "                   </PostDate>\n" +
                "                   <ShipDate>\n" +
                "                       <Date>01/18/2021</Date>\n" +
                "                   </ShipDate>\n" +
                "                   <ShippingInformation>\n" +
                "                       <Address>\n" +
                "                           <StreetLineOne>FAB SHOP</StreetLineOne>\n" +
                "                           <StreetLineTwo>3936 WESTPOINT BLVD</StreetLineTwo>\n" +
                "                           <StreetLineThree></StreetLineThree>\n" +
                "                           <City>WINSTON-SALEM</City>\n" +
                "                           <State>NC</State>\n" +
                "                           <PostalCode>27103-6719</PostalCode>\n" +
                "                           <Country></Country>\n" +
                "                       </Address>\n" +
                "                       <ShipVia>\n" +
                "                           <ShipViaID>OT OUR TRUCK</ShipViaID>\n" +
                "                           <Description>OT</Description>\n" +
                "                           <ValidXMLShipVia>Yes</ValidXMLShipVia>\n" +
                "                       </ShipVia>\n" +
                "                   </ShippingInformation>\n" +
                "                   <Age>Future</Age>\n" +
                "                   <DiscountWrapper>\n" +
                "                       <CashDiscount>\n" +
                "                           <Amount>-12.34</Amount>\n" +
                "                           <Percentage>-2.00</Percentage>\n" +
                "                           <Date>03/10/2021</Date>\n" +
                "                        </CashDiscount>\n" +
                "                   </DiscountWrapper>\n" +
                "                <Amount>767.21</Amount>\n" +
                "                <Subtotal>617.05</Subtotal>\n" +
                "                <Balance>3039.32</Balance>\n" +
                "                <Currency>US</Currency>\n" +
                "           </AccountHistoryItem>\n" +
                "               <AccountHistoryItem>\n" +
                "                   <AccountRegisterID>S109945932.001</AccountRegisterID>\n" +
                "                   <OrderID>C109945932</OrderID>\n" +
                "                   <InvoiceNumber>1</InvoiceNumber>\n" +
                "                   <OrderStatus Code=\"I\">Invoice # .001 * Direct *</OrderStatus>\n" +
                "                   <PrintStatus>P</PrintStatus>\n" +
                "                   <CustomerPO>WR1805-8710</CustomerPO>\n" +
                "                   <CustomerReleaseNumber>clay</CustomerReleaseNumber>\n" +
                "                   <Description>PO# WR1805-8710</Description>\n" +
                "                   <BranchID>1303</BranchID>\n" +
                "                   <OrderDate>\n" +
                "                       <Date>01/14/2021</Date>\n" +
                "                   </OrderDate>\n" +
                "                   <PostDate>\n" +
                "                       <Date>01/18/2021</Date>\n" +
                "                   </PostDate>\n" +
                "                   <ShipDate>\n" +
                "                       <Date>01/18/2021</Date>\n" +
                "                   </ShipDate>\n" +
                "                   <ShippingInformation>\n" +
                "                       <Address>\n" +
                "                           <StreetLineOne>FAB SHOP</StreetLineOne>\n" +
                "                           <StreetLineTwo>3936 WESTPOINT BLVD</StreetLineTwo>\n" +
                "                           <StreetLineThree></StreetLineThree>\n" +
                "                           <City>WINSTON-SALEM</City>\n" +
                "                           <State>NC</State>\n" +
                "                           <PostalCode>27103-6719</PostalCode>\n" +
                "                           <Country></Country>\n" +
                "                       </Address>\n" +
                "                       <ShipVia>\n" +
                "                           <ShipViaID>OT OUR TRUCK</ShipViaID>\n" +
                "                           <Description>OT</Description>\n" +
                "                           <ValidXMLShipVia>Yes</ValidXMLShipVia>\n" +
                "                       </ShipVia>\n" +
                "                   </ShippingInformation>\n" +
                "                   <Age>Future</Age>\n" +
                "                   <DiscountWrapper>\n" +
                "                       <CashDiscount>\n" +
                "                           <Amount>-12.34</Amount>\n" +
                "                           <Percentage>-2.00</Percentage>\n" +
                "                           <Date>03/10/2021</Date>\n" +
                "                        </CashDiscount>\n" +
                "                   </DiscountWrapper>\n" +
                "                <Amount>767.21</Amount>\n" +
                "                <Subtotal>617.05</Subtotal>\n" +
                "                <Balance>3039.32</Balance>\n" +
                "                <Currency>US</Currency>\n" +
                "           </AccountHistoryItem>\n" +
                "       </AccountHistoryItemList>\n" +
                "       <StatusResult Success=\"Yes\">\n" +
                "           <Description>Order History Information retrieved successfully.</Description>\n" +
                "       </StatusResult>\n" +
                "   </AccountHistoryInquiryResponse>\n" +
                "</IDMS-XML>";
    }

    public static String OpenOrderResponseSuccess() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE IDMS-XML SYSTEM \"http://www.eclipseinc.com/dtd/IDMS-XML.dtd\">\n" +
                "<IDMS-XML>\n" +
                "   <OpenOrderInquiryResponse>\n" +
                "       <SessionID></SessionID>\n" +
                "           <OpenOrderItemList>\n" +
                "               <OpenOrderItem>\n" +
                "                   <AccountRegisterID>S109945932.001</AccountRegisterID>\n" +
                "                   <OrderID>S109945932</OrderID>\n" +
                "                   <InvoiceNumber>1</InvoiceNumber>\n" +
                "                   <OrderStatus Code=\"I\">Invoice # .001 * Direct *</OrderStatus>\n" +
                "                   <PrintStatus>P</PrintStatus>\n" +
                "                   <CustomerPO>WR1805-8710</CustomerPO>\n" +
                "                   <CustomerReleaseNumber>clay</CustomerReleaseNumber>\n" +
                "                   <Description>PO# WR1805-8710</Description>\n" +
                "                   <BranchID>1303</BranchID>\n" +
                "                   <OrderDate>\n" +
                "                       <Date>01/14/2021</Date>\n" +
                "                   </OrderDate>\n" +
                "                   <PostDate>\n" +
                "                       <Date>01/18/2021</Date>\n" +
                "                   </PostDate>\n" +
                "                   <ShippingInformation>\n" +
                "                       <Address>\n" +
                "                           <StreetLineOne>FAB SHOP</StreetLineOne>\n" +
                "                           <StreetLineTwo>3936 WESTPOINT BLVD</StreetLineTwo>\n" +
                "                           <StreetLineThree></StreetLineThree>\n" +
                "                           <City>WINSTON-SALEM</City>\n" +
                "                           <State>NC</State>\n" +
                "                           <PostalCode>27103-6719</PostalCode>\n" +
                "                           <Country></Country>\n" +
                "                       </Address>\n" +
                "                       <ShipVia>\n" +
                "                           <ShipViaID>OT OUR TRUCK</ShipViaID>\n" +
                "                           <Description>OT</Description>\n" +
                "                           <ValidXMLShipVia>Yes</ValidXMLShipVia>\n" +
                "                       </ShipVia>\n" +
                "                   </ShippingInformation>\n" +
                "                   <Age>Future</Age>\n" +
                "                   <DiscountWrapper>\n" +
                "                       <CashDiscount>\n" +
                "                           <Amount>-12.34</Amount>\n" +
                "                           <Percentage>-2.00</Percentage>\n" +
                "                           <Date>03/10/2021</Date>\n" +
                "                        </CashDiscount>\n" +
                "                   </DiscountWrapper>\n" +
                "                <Amount>767.21</Amount>\n" +
                "                <Subtotal>617.05</Subtotal>\n" +
                "                <Balance>3039.32</Balance>\n" +
                "                <Currency>US</Currency>\n" +
                "           </OpenOrderItem>\n" +
                "               <OpenOrderItem>\n" +
                "                   <AccountRegisterID>S109945932.001</AccountRegisterID>\n" +
                "                   <OrderID>C109945932</OrderID>\n" +
                "                   <InvoiceNumber>1</InvoiceNumber>\n" +
                "                   <OrderStatus Code=\"I\">Invoice # .001 * Direct *</OrderStatus>\n" +
                "                   <PrintStatus>P</PrintStatus>\n" +
                "                   <CustomerPO>WR1805-8710</CustomerPO>\n" +
                "                   <CustomerReleaseNumber>clay</CustomerReleaseNumber>\n" +
                "                   <Description>PO# WR1805-8710</Description>\n" +
                "                   <BranchID>1303</BranchID>\n" +
                "                   <OrderDate>\n" +
                "                       <Date>01/14/2021</Date>\n" +
                "                   </OrderDate>\n" +
                "                   <PostDate>\n" +
                "                       <Date>01/18/2021</Date>\n" +
                "                   </PostDate>\n" +
                "                   <ShippingInformation>\n" +
                "                       <Address>\n" +
                "                           <StreetLineOne>FAB SHOP</StreetLineOne>\n" +
                "                           <StreetLineTwo>3936 WESTPOINT BLVD</StreetLineTwo>\n" +
                "                           <StreetLineThree></StreetLineThree>\n" +
                "                           <City>WINSTON-SALEM</City>\n" +
                "                           <State>NC</State>\n" +
                "                           <PostalCode>27103-6719</PostalCode>\n" +
                "                           <Country></Country>\n" +
                "                       </Address>\n" +
                "                       <ShipVia>\n" +
                "                           <ShipViaID>OT OUR TRUCK</ShipViaID>\n" +
                "                           <Description>OT</Description>\n" +
                "                           <ValidXMLShipVia>Yes</ValidXMLShipVia>\n" +
                "                       </ShipVia>\n" +
                "                   </ShippingInformation>\n" +
                "                   <Age>Future</Age>\n" +
                "                   <DiscountWrapper>\n" +
                "                       <CashDiscount>\n" +
                "                           <Amount>-12.34</Amount>\n" +
                "                           <Percentage>-2.00</Percentage>\n" +
                "                           <Date>03/10/2021</Date>\n" +
                "                        </CashDiscount>\n" +
                "                   </DiscountWrapper>\n" +
                "                <Amount>767.21</Amount>\n" +
                "                <Subtotal>617.05</Subtotal>\n" +
                "                <Balance>3039.32</Balance>\n" +
                "                <Currency>US</Currency>\n" +
                "           </OpenOrderItem>\n" +
                "       </OpenOrderItemList>\n" +
                "       <StatusResult Success=\"Yes\">\n" +
                "           <Description>Open Order Information retrieved successfully.</Description>\n" +
                "       </StatusResult>\n" +
                "   </OpenOrderInquiryResponse>\n" +
                "</IDMS-XML>";
    }

    public static String ProductResponseFailure() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE IDMS-XML SYSTEM \"http://www.eclipseinc.com/dtd/IDMS-XML.dtd\">\n" +
                "<IDMS-XML>\n" +
                "    <MassProductInquiryResponse>\n" +
                "        <SessionID></SessionID>\n" +
                "        <StatusResult Success=\"No\">\n" +
                "            <Description>Error(s) encountered processing request.</Description>\n" +
                "            <ErrorMessageList>\n" +
                "                <ErrorMessage>\n" +
                "                    <Code>554</Code>\n" +
                "                    <Description>Part Not Found: 23493208423094.</Description>\n" +
                "                </ErrorMessage>\n" +
                "            </ErrorMessageList>\n" +
                "        </StatusResult>\n" +
                "    </MassProductInquiryResponse>\n" +
                "</IDMS-XML>";
    }

    public static String ProductResponseSuccess() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE IDMS-XML SYSTEM \"http://www.eclipseinc.com/dtd/IDMS-XML.dtd\">\n" +
                "<IDMS-XML>\n" +
                "    <MassProductInquiryResponse>\n" +
                "        <SessionID></SessionID>\n" +
                "        <ProductList>" +
                "        <Product>\n" +
                "            <Description>COPFTG 2-1/2X1-1/2 COP CXC REDCR C&amp;B (CBCRCLJ)</Description>\n" +
                "            <AlternateDescription>COPFTG 2-1/2X1-1/2 COP CXC REDCR C&amp;B</AlternateDescription>\n" +
                "            <PartIdentifiers>\n" +
                "                <EclipsePartNumber>425275</EclipsePartNumber>\n" +
                "                <UPCList>\n" +
                "                    <UPC PrimaryUPC=\"Yes\">68576831986</UPC>\n" +
                "                    <UPC PrimaryUPC=\"No\">683264301030</UPC>\n" +
                "                    <UPC PrimaryUPC=\"No\">685768319863</UPC>\n" +
                "                    <UPC PrimaryUPC=\"No\">683264308039</UPC>\n" +
                "                </UPCList>\n" +
                "                <CatalogNumber>CBCRCLJ</CatalogNumber>\n" +
                "                <ProductKeywords>TSC:544P CB600LK C&amp;B 2X1-1/2 CXC RED COUP WHS.AD OXCC2X112 1390630 101-R LN140\n" +
                "                    COPPER MUELLER CELLO FITTING COP NIBCO ELKHART OXY CLEANED BAGGED FTG OXYGEN CPLG W/STOP 600R 283601\n" +
                "                    MCBCRCLJ COPFTG 2-1/2X1-1/2 (CBCRCLJ)\n" +
                "                </ProductKeywords>\n" +
                "                <Description>COPFTG 2-1/2X1-1/2 COP CXC REDCR C&amp;B (CBCRCLJ)</Description>\n" +
                "                <UserDefinedProductCodes>\n" +
                "                    <UserDefinedProductCode1>CBCRCLJ</UserDefinedProductCode1>\n" +
                "                    <UserDefinedProductCode3>COPFTG_CBCRCLJ</UserDefinedProductCode3>\n" +
                "                    <UserDefinedProductCode4>WHS.ADD_65247</UserDefinedProductCode4>\n" +
                "                    <UserDefinedProductCode5>425275_WEB</UserDefinedProductCode5>\n" +
                "                    <UserDefinedProductCode9>425275</UserDefinedProductCode9>\n" +
                "                </UserDefinedProductCodes>\n" +
                "            </PartIdentifiers>\n" +
                "            <AvailabilityList>\n" +
                "                <BranchAvailability>\n" +
                "                    <Branch>\n" +
                "                        <BranchID>1302</BranchID>\n" +
                "                        <BranchName>WINSTON-SALM</BranchName>\n" +
                "                    </Branch>\n" +
                "                    <NowQuantity>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\">0</Quantity>\n" +
                "                    </NowQuantity>\n" +
                "                    <EarliestMoreDate>\n" +
                "                        <Date></Date>\n" +
                "                    </EarliestMoreDate>\n" +
                "                    <EarliestMoreQty>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\"></Quantity>\n" +
                "                    </EarliestMoreQty>\n" +
                "                    <PlentyDate>\n" +
                "                        <Date>01/18/2021</Date>\n" +
                "                    </PlentyDate>\n" +
                "                    <StockQuantityOnOrder>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\">0</Quantity>\n" +
                "                    </StockQuantityOnOrder>\n" +
                "                    <TaggedQuantityOnOrder>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\">0</Quantity>\n" +
                "                    </TaggedQuantityOnOrder>\n" +
                "                    <ProjectedInventoryLevel>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\">0</Quantity>\n" +
                "                    </ProjectedInventoryLevel>\n" +
                "                </BranchAvailability>\n" +
                "                <BranchAvailability>\n" +
                "                    <Branch>\n" +
                "                        <BranchID>1303</BranchID>\n" +
                "                        <BranchName>RALEIGH</BranchName>\n" +
                "                    </Branch>\n" +
                "                    <NowQuantity>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\">0</Quantity>\n" +
                "                    </NowQuantity>\n" +
                "                    <EarliestMoreDate>\n" +
                "                        <Date></Date>\n" +
                "                    </EarliestMoreDate>\n" +
                "                    <EarliestMoreQty>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\"></Quantity>\n" +
                "                    </EarliestMoreQty>\n" +
                "                    <PlentyDate>\n" +
                "                        <Date>01/18/2021</Date>\n" +
                "                    </PlentyDate>\n" +
                "                    <StockQuantityOnOrder>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\">0</Quantity>\n" +
                "                    </StockQuantityOnOrder>\n" +
                "                    <TaggedQuantityOnOrder>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\">0</Quantity>\n" +
                "                    </TaggedQuantityOnOrder>\n" +
                "                    <ProjectedInventoryLevel>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\">0</Quantity>\n" +
                "                    </ProjectedInventoryLevel>\n" +
                "                </BranchAvailability>\n" +
                "                <BranchAvailability>\n" +
                "                    <Branch>\n" +
                "                        <BranchID>1304</BranchID>\n" +
                "                        <BranchName>HICKORY</BranchName>\n" +
                "                    </Branch>\n" +
                "                    <NowQuantity>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\">0</Quantity>\n" +
                "                    </NowQuantity>\n" +
                "                    <EarliestMoreDate>\n" +
                "                        <Date></Date>\n" +
                "                    </EarliestMoreDate>\n" +
                "                    <EarliestMoreQty>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\"></Quantity>\n" +
                "                    </EarliestMoreQty>\n" +
                "                    <PlentyDate>\n" +
                "                        <Date>12/10/2020</Date>\n" +
                "                    </PlentyDate>\n" +
                "                    <StockQuantityOnOrder>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\">0</Quantity>\n" +
                "                    </StockQuantityOnOrder>\n" +
                "                    <TaggedQuantityOnOrder>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\">0</Quantity>\n" +
                "                    </TaggedQuantityOnOrder>\n" +
                "                    <ProjectedInventoryLevel>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\">0</Quantity>\n" +
                "                    </ProjectedInventoryLevel>\n" +
                "                </BranchAvailability>\n" +
                "                <BranchAvailability>\n" +
                "                    <Branch>\n" +
                "                        <BranchID>1308</BranchID>\n" +
                "                        <BranchName>RALEIGH WHSE</BranchName>\n" +
                "                    </Branch>\n" +
                "                    <NowQuantity>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\">0</Quantity>\n" +
                "                    </NowQuantity>\n" +
                "                    <EarliestMoreDate>\n" +
                "                        <Date></Date>\n" +
                "                    </EarliestMoreDate>\n" +
                "                    <EarliestMoreQty>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\"></Quantity>\n" +
                "                    </EarliestMoreQty>\n" +
                "                    <PlentyDate>\n" +
                "                        <Date>12/10/2020</Date>\n" +
                "                    </PlentyDate>\n" +
                "                    <StockQuantityOnOrder>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\">0</Quantity>\n" +
                "                    </StockQuantityOnOrder>\n" +
                "                    <TaggedQuantityOnOrder>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\">0</Quantity>\n" +
                "                    </TaggedQuantityOnOrder>\n" +
                "                    <ProjectedInventoryLevel>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\">0</Quantity>\n" +
                "                    </ProjectedInventoryLevel>\n" +
                "                </BranchAvailability>\n" +
                "                <BranchAvailability>\n" +
                "                    <Branch>\n" +
                "                        <BranchID>1310</BranchID>\n" +
                "                        <BranchName>CHARLOTTE</BranchName>\n" +
                "                    </Branch>\n" +
                "                    <NowQuantity>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\">0</Quantity>\n" +
                "                    </NowQuantity>\n" +
                "                    <EarliestMoreDate>\n" +
                "                        <Date></Date>\n" +
                "                    </EarliestMoreDate>\n" +
                "                    <EarliestMoreQty>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\"></Quantity>\n" +
                "                    </EarliestMoreQty>\n" +
                "                    <PlentyDate>\n" +
                "                        <Date>01/29/2021</Date>\n" +
                "                    </PlentyDate>\n" +
                "                    <StockQuantityOnOrder>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\">0</Quantity>\n" +
                "                    </StockQuantityOnOrder>\n" +
                "                    <TaggedQuantityOnOrder>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\">0</Quantity>\n" +
                "                    </TaggedQuantityOnOrder>\n" +
                "                    <ProjectedInventoryLevel>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\">0</Quantity>\n" +
                "                    </ProjectedInventoryLevel>\n" +
                "                </BranchAvailability>\n" +
                "                <BranchAvailability>\n" +
                "                    <Branch>\n" +
                "                        <BranchID>1320</BranchID>\n" +
                "                        <BranchName>COLUMBIA</BranchName>\n" +
                "                    </Branch>\n" +
                "                    <NowQuantity>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\">0</Quantity>\n" +
                "                    </NowQuantity>\n" +
                "                    <EarliestMoreDate>\n" +
                "                        <Date></Date>\n" +
                "                    </EarliestMoreDate>\n" +
                "                    <EarliestMoreQty>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\"></Quantity>\n" +
                "                    </EarliestMoreQty>\n" +
                "                    <PlentyDate>\n" +
                "                        <Date>01/18/2021</Date>\n" +
                "                    </PlentyDate>\n" +
                "                    <StockQuantityOnOrder>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\">0</Quantity>\n" +
                "                    </StockQuantityOnOrder>\n" +
                "                    <TaggedQuantityOnOrder>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\">0</Quantity>\n" +
                "                    </TaggedQuantityOnOrder>\n" +
                "                    <ProjectedInventoryLevel>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\">0</Quantity>\n" +
                "                    </ProjectedInventoryLevel>\n" +
                "                </BranchAvailability>\n" +
                "                <BranchAvailability>\n" +
                "                    <Branch>\n" +
                "                        <BranchID>1321</BranchID>\n" +
                "                        <BranchName>CHRLSTN PLBG</BranchName>\n" +
                "                    </Branch>\n" +
                "                    <NowQuantity>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\">0</Quantity>\n" +
                "                    </NowQuantity>\n" +
                "                    <EarliestMoreDate>\n" +
                "                        <Date></Date>\n" +
                "                    </EarliestMoreDate>\n" +
                "                    <EarliestMoreQty>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\"></Quantity>\n" +
                "                    </EarliestMoreQty>\n" +
                "                    <PlentyDate>\n" +
                "                        <Date>01/18/2021</Date>\n" +
                "                    </PlentyDate>\n" +
                "                    <StockQuantityOnOrder>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\">0</Quantity>\n" +
                "                    </StockQuantityOnOrder>\n" +
                "                    <TaggedQuantityOnOrder>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\">0</Quantity>\n" +
                "                    </TaggedQuantityOnOrder>\n" +
                "                    <ProjectedInventoryLevel>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\">0</Quantity>\n" +
                "                    </ProjectedInventoryLevel>\n" +
                "                </BranchAvailability>\n" +
                "                <BranchAvailability>\n" +
                "                    <Branch>\n" +
                "                        <BranchID>1396</BranchID>\n" +
                "                        <BranchName>W-S INDUSTR</BranchName>\n" +
                "                    </Branch>\n" +
                "                    <NowQuantity>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\">0</Quantity>\n" +
                "                    </NowQuantity>\n" +
                "                    <EarliestMoreDate>\n" +
                "                        <Date></Date>\n" +
                "                    </EarliestMoreDate>\n" +
                "                    <EarliestMoreQty>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\"></Quantity>\n" +
                "                    </EarliestMoreQty>\n" +
                "                    <PlentyDate>\n" +
                "                        <Date>12/10/2020</Date>\n" +
                "                    </PlentyDate>\n" +
                "                    <StockQuantityOnOrder>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\">0</Quantity>\n" +
                "                    </StockQuantityOnOrder>\n" +
                "                    <TaggedQuantityOnOrder>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\">0</Quantity>\n" +
                "                    </TaggedQuantityOnOrder>\n" +
                "                    <ProjectedInventoryLevel>\n" +
                "                        <Quantity UOM=\"ea\" UMQT=\"1\">0</Quantity>\n" +
                "                    </ProjectedInventoryLevel>\n" +
                "                </BranchAvailability>\n" +
                "            </AvailabilityList>\n" +
                "            <UOMList>10\n" +
                "                <UOM UMQT=\"1\">ea</UOM>\n" +
                "            </UOMList>\n" +
                "            <PricingUOM>\n" +
                "                <UOM UMQT=\"1\">ea</UOM>\n" +
                "            </PricingUOM>\n" +
                "            <Pricing>\n" +
                "                <EclipsePartNumber>425275</EclipsePartNumber>\n" +
                "                <EntityID>300988</EntityID>\n" +
                "                <PricingBranch>\n" +
                "                    <Branch>\n" +
                "                        <BranchID>1302</BranchID>\n" +
                "                        <BranchName>WINSTON-SALM</BranchName>\n" +
                "                    </Branch>\n" +
                "                </PricingBranch>\n" +
                "                <Currency>US</Currency>\n" +
                "                <Quantity UOM=\"ea\" UMQT=\"1\">1</Quantity>\n" +
                "                <ListPrice>176.000</ListPrice>\n" +
                "                <CustomerPrice>32.181000000</CustomerPrice>\n" +
                "                <ExtendedPrice>32.181000000</ExtendedPrice>\n" +
                "            </Pricing>\n" +
                "            <Volume Unit=\"\">0.0000</Volume>\n" +
                "            <Weight Unit=\"\">0.3970</Weight>\n" +
                "            <BuyLine>COPFIT</BuyLine>\n" +
                "            <PriceLine>COPFIT</PriceLine>\n" +
                "            <Status>Stock</Status>\n" +
                "            <IndexType>Primary</IndexType>\n" +
                "            <RichContentList>\n" +
                "                <RichContentItem>\n" +
                "                    <Name>TS Full Image URL</Name>\n" +
                "                    <ID>70012</ID>\n" +
                "                    <Value>http://images.tradeservice.com/ProductImages/DIR100142/MUELER_W-01030-CB_LRG.jpg</Value>\n" +
                "                </RichContentItem>\n" +
                "                <RichContentItem>\n" +
                "                    <Name>TS Thumbnail URL</Name>\n" +
                "                    <ID>70050</ID>\n" +
                "                    <Value>http://images.tradeservice.com/ProductImages/DIR100143/MUELER_W-01030-CB_SML.jpg</Value>\n" +
                "                </RichContentItem>\n" +
                "            </RichContentList>\n" +
                "        </Product>\n" +
                "        </ProductList>\n" +
                "        <StatusResult Success=\"Yes\">\n" +
                "            <Description>Product Information retrieved successfully.</Description>\n" +
                "        </StatusResult>\n" +
                "    </MassProductInquiryResponse>\n" +
                "</IDMS-XML>";
    }

    public static String AccountResponseSuccess() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE IDMS-XML SYSTEM \"http://www.eclipseinc.com/dtd/IDMS-XML.dtd\">\n" +
                "<IDMS-XML>\n" +
                "    <EntityInquiryResponse>\n" +
                "        <SessionID></SessionID>\n" +
                "        <Entity>\n" +
                String.format("            <EntityID>%s</EntityID>\n", ENTITY_RESPONSE_BILL_TO_ID) +
                "            <EntityName>MTB MECHANICAL - SHOP</EntityName>\n" +
                "            <ShipToList>\n" +
                String.format("               <EntityID>%s</EntityID>\n", ENTITY_RESPONSE_ACCOUNT_ID_1) +
                String.format("               <EntityID>%s</EntityID>\n", ENTITY_RESPONSE_ACCOUNT_ID_2) +
                "            </ShipToList>" +
                "            <BillTo>\n" +
                "                <EntityID>302841</EntityID>\n" +
                "            </BillTo>\n" +
                "            <POReleaseRequired>" +
                "PO Number" +
                "</POReleaseRequired>\n" +
                "            <Branch>\n" +
                "                <BranchID>1310</BranchID>\n" +
                "                <BranchName>CHARLOTTE</BranchName>\n" +
                "            </Branch>\n" +
                "            <Address>\n" +
                "                <StreetLineOne>1201 INDUSTRIAL DR</StreetLineOne>\n" +
                "                <StreetLineTwo></StreetLineTwo>\n" +
                "                <StreetLineThree></StreetLineThree>\n" +
                "                <City>MATTHEWS</City>\n" +
                "                <State>NC</State>\n" +
                "                <PostalCode>28105-5311</PostalCode>\n" +
                "                <Country></Country>\n" +
                "            </Address>\n" +
                "            <EntityKeywords>15893MRY\n" +
                "MRY.ADD</EntityKeywords>\n" +
                "            <ContactShortList>\n" +
                "                <ContactShort>\n" +
                "                    <Description>Fax</Description>\n" +
                "                    <Telephone>\n" +
                "                        <Number></Number>\n" +
                "                    </Telephone>\n" +
                "                </ContactShort>\n" +
                "            </ContactShortList>\n" +
                "            <CustomerTypeList>\n" +
                "                <CustomerType>P_RNC</CustomerType>\n" +
                "            </CustomerTypeList>\n" +
                "            <DfltPriceClass>P_RNC30</DfltPriceClass>\n" +
                "            <EntityNoteList>\n" +
                "                <EntityNote>\n" +
                "                    <Type>WDS History</Type>\n" +
                "                    <Description></Description>\n" +
                "                </EntityNote>\n" +
                "            </EntityNoteList>\n" +
                "            <Credit Currency=\"US\" CreditLimit=\"125000.00\" CreditAvailable=\"88101.82\" OrderEntryOK=\"Yes\" Terms=\"Net 2nd 15th Prox\">\n" +
                "                <Description>Credit OK</Description>\n" +
                "            </Credit>\n" +
                "            <EmailAddressList>\n" +
                "                <EmailAddress Type=\"\" HTMLPref=\"\">jglenn@mtbmechanical.com</EmailAddress>\n" +
                "                <EmailAddress Type=\"\" HTMLPref=\"\">jtrexler@mtbmechanical.com</EmailAddress>\n" +
                "            </EmailAddressList>\n" +
                "            <WebAddress></WebAddress>\n" +
                "            <EntityRemoteData>\n" +
                "                <OrderStatusList>\n" +
                "                    <OrderStatus Code=\"B\">Bid</OrderStatus>\n" +
                "                    <OrderStatus Code=\"C\">Call When Complete</OrderStatus>\n" +
                "                </OrderStatusList>\n" +
                "                <ShipViaList>\n" +
                "                    <ShipViaID>OT OUR TRUCK</ShipViaID>\n" +
                "                    <ShipViaID>UPS GROUND</ShipViaID>\n" +
                "                    <ShipViaID>WILL CALL</ShipViaID>\n" +
                "                </ShipViaList>\n" +
                "                <DisplayProductAvailability>Show w/ Qty</DisplayProductAvailability>\n" +
                "            </EntityRemoteData>\n" +
                "            <ClassificationItemList>\n" +
                "                <ClassificationItem>\n" +
                "                    <Name>Latitude_cust</Name>\n" +
                "                    <Value>999</Value>\n" +
                "                </ClassificationItem>\n" +
                "                <ClassificationItem>\n" +
                "                    <Name>RegCreditMgr</Name>\n" +
                "                    <Value>MD04093</Value>\n" +
                "                </ClassificationItem>\n" +
                "                <ClassificationItem>\n" +
                "                    <Name>Wds_type</Name>\n" +
                "                    <Value>Plumbing Residential New Construction</Value>\n" +
                "                </ClassificationItem>\n" +
                "                <ClassificationItem>\n" +
                "                    <Name>Time_zone</Name>\n" +
                "                    <Value>ET</Value>\n" +
                "                </ClassificationItem>\n" +
                "                <ClassificationItem>\n" +
                "                    <Name>Prev_cust_sel_code</Name>\n" +
                "                    <Value>15893</Value>\n" +
                "                </ClassificationItem>\n" +
                "            </ClassificationItemList>\n" +
                "        </Entity>\n" +
                "        <StatusResult Success=\"Yes\">\n" +
                "            <Description>Entity Information retrieved successfully.</Description>\n" +
                "        </StatusResult>\n" +
                "    </EntityInquiryResponse>\n" +
                "</IDMS-XML>";
    }

    public static String AccountResponseSuccess2() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE IDMS-XML SYSTEM \"http://www.eclipseinc.com/dtd/IDMS-XML.dtd\">\n" +
                "<IDMS-XML>\n" +
                "    <EntityInquiryResponse>\n" +
                "        <SessionID></SessionID>\n" +
                "        <Entity>\n" +
                "            <EntityID>302841</EntityID>\n" +
                "            <EntityName>MDFHOP</EntityName>\n" +
                "            <BillTo>\n" +
//                "                <EntityID>302841</EntityID>\n" +
                "            </BillTo>\n" +
                "            <Branch>\n" +
                "                <BranchID>1310</BranchID>\n" +
                "                <BranchName>CHARLOTTE</BranchName>\n" +
                "            </Branch>\n" +
                "            <Address>\n" +
                "                <StreetLineOne>1201 INDUSTRIAL DR</StreetLineOne>\n" +
                "                <StreetLineTwo></StreetLineTwo>\n" +
                "                <StreetLineThree></StreetLineThree>\n" +
                "                <City>MATTHEWS</City>\n" +
                "                <State>NC</State>\n" +
                "                <PostalCode>28105-5311</PostalCode>\n" +
                "                <Country></Country>\n" +
                "            </Address>\n" +
                "            <EntityKeywords>15893MRY\n" +
                "MRY.ADD</EntityKeywords>\n" +
                "            <ContactShortList>\n" +
                "                <ContactShort>\n" +
                "                    <Description>Fax</Description>\n" +
                "                    <Telephone>\n" +
                "                        <Number></Number>\n" +
                "                    </Telephone>\n" +
                "                </ContactShort>\n" +
                "            </ContactShortList>\n" +
                "            <CustomerTypeList>\n" +
                "                <CustomerType>P_RNC</CustomerType>\n" +
                "            </CustomerTypeList>\n" +
                "            <DfltPriceClass>P_RNC30</DfltPriceClass>\n" +
                "            <EntityNoteList>\n" +
                "                <EntityNote>\n" +
                "                    <Type>WDS History</Type>\n" +
                "                    <Description></Description>\n" +
                "                </EntityNote>\n" +
                "            </EntityNoteList>\n" +
                "            <Credit Currency=\"US\" CreditLimit=\"125000.00\" CreditAvailable=\"88101.82\" OrderEntryOK=\"Yes\" Terms=\"Net 2nd 15th Prox\">\n" +
                "                <Description>Credit OK</Description>\n" +
                "            </Credit>\n" +
                "            <EmailAddressList>\n" +
                "                <EmailAddress Type=\"\" HTMLPref=\"\">jglenn@mtbmechanical.com</EmailAddress>\n" +
                "                <EmailAddress Type=\"\" HTMLPref=\"\">jtrexler@mtbmechanical.com</EmailAddress>\n" +
                "            </EmailAddressList>\n" +
                "            <WebAddress></WebAddress>\n" +
                "            <EntityRemoteData>\n" +
                "                <OrderStatusList>\n" +
                "                    <OrderStatus Code=\"B\">Bid</OrderStatus>\n" +
                "                    <OrderStatus Code=\"C\">Call When Complete</OrderStatus>\n" +
                "                </OrderStatusList>\n" +
                "                <ShipViaList>\n" +
                "                    <ShipViaID>OT OUR TRUCK</ShipViaID>\n" +
                "                    <ShipViaID>UPS GROUND</ShipViaID>\n" +
                "                    <ShipViaID>WILL CALL</ShipViaID>\n" +
                "                </ShipViaList>\n" +
                "                <DisplayProductAvailability>Show w/ Qty</DisplayProductAvailability>\n" +
                "            </EntityRemoteData>\n" +
                "            <ClassificationItemList>\n" +
                "                <ClassificationItem>\n" +
                "                    <Name>Latitude_cust</Name>\n" +
                "                    <Value>999</Value>\n" +
                "                </ClassificationItem>\n" +
                "                <ClassificationItem>\n" +
                "                    <Name>RegCreditMgr</Name>\n" +
                "                    <Value>MD04093</Value>\n" +
                "                </ClassificationItem>\n" +
                "                <ClassificationItem>\n" +
                "                    <Name>Wds_type</Name>\n" +
                "                    <Value>Plumbing Residential New Construction</Value>\n" +
                "                </ClassificationItem>\n" +
                "                <ClassificationItem>\n" +
                "                    <Name>Time_zone</Name>\n" +
                "                    <Value>ET</Value>\n" +
                "                </ClassificationItem>\n" +
                "                <ClassificationItem>\n" +
                "                    <Name>Prev_cust_sel_code</Name>\n" +
                "                    <Value>15893</Value>\n" +
                "                </ClassificationItem>\n" +
                "            </ClassificationItemList>\n" +
                "        </Entity>\n" +
                "        <StatusResult Success=\"Yes\">\n" +
                "            <Description>Entity Information retrieved successfully.</Description>\n" +
                "        </StatusResult>\n" +
                "    </EntityInquiryResponse>\n" +
                "</IDMS-XML>";
    }

    public static String AccountResponseSuccess3() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE IDMS-XML SYSTEM \"http://www.eclipseinc.com/dtd/IDMS-XML.dtd\">\n" +
                "<IDMS-XML>\n" +
                "    <EntityInquiryResponse>\n" +
                "        <SessionID></SessionID>\n" +
                "        <Entity>\n" +
                "            <EntityID>1111111</EntityID>\n" +
                "            <EntityName>asdf</EntityName>\n" +
                "            <BillTo>\n" +
//                "                <EntityID>302841</EntityID>\n" +
                "            </BillTo>\n" +
                "            <Branch>\n" +
                "                <BranchID>1310</BranchID>\n" +
                "                <BranchName>CHARLOTTE</BranchName>\n" +
                "            </Branch>\n" +
                "            <Address>\n" +
                "                <StreetLineOne>1201 INDUSTRIAL DR</StreetLineOne>\n" +
                "                <StreetLineTwo></StreetLineTwo>\n" +
                "                <StreetLineThree></StreetLineThree>\n" +
                "                <City>MATTHEWS</City>\n" +
                "                <State>NC</State>\n" +
                "                <PostalCode>28105-5311</PostalCode>\n" +
                "                <Country></Country>\n" +
                "            </Address>\n" +
                "            <EntityKeywords>15893MRY\n" +
                "MRY.ADD</EntityKeywords>\n" +
                "            <ContactShortList>\n" +
                "                <ContactShort>\n" +
                "                    <Description>Fax</Description>\n" +
                "                    <Telephone>\n" +
                "                        <Number></Number>\n" +
                "                    </Telephone>\n" +
                "                </ContactShort>\n" +
                "            </ContactShortList>\n" +
                "            <CustomerTypeList>\n" +
                "                <CustomerType>P_RNC</CustomerType>\n" +
                "            </CustomerTypeList>\n" +
                "            <DfltPriceClass>P_RNC30</DfltPriceClass>\n" +
                "            <EntityNoteList>\n" +
                "                <EntityNote>\n" +
                "                    <Type>WDS History</Type>\n" +
                "                    <Description></Description>\n" +
                "                </EntityNote>\n" +
                "            </EntityNoteList>\n" +
                "            <Credit Currency=\"US\" CreditLimit=\"125000.00\" CreditAvailable=\"88101.82\" OrderEntryOK=\"Yes\" Terms=\"Net 2nd 15th Prox\">\n" +
                "                <Description>Credit OK</Description>\n" +
                "            </Credit>\n" +
                "            <EmailAddressList>\n" +
                "                <EmailAddress Type=\"\" HTMLPref=\"\">jglenn@mtbmechanical.com</EmailAddress>\n" +
                "                <EmailAddress Type=\"\" HTMLPref=\"\">jtrexler@mtbmechanical.com</EmailAddress>\n" +
                "            </EmailAddressList>\n" +
                "            <WebAddress></WebAddress>\n" +
                "            <EntityRemoteData>\n" +
                "                <OrderStatusList>\n" +
                "                    <OrderStatus Code=\"B\">Bid</OrderStatus>\n" +
                "                    <OrderStatus Code=\"C\">Call When Complete</OrderStatus>\n" +
                "                </OrderStatusList>\n" +
                "                <ShipViaList>\n" +
                "                    <ShipViaID>OT OUR TRUCK</ShipViaID>\n" +
                "                    <ShipViaID>UPS GROUND</ShipViaID>\n" +
                "                    <ShipViaID>WILL CALL</ShipViaID>\n" +
                "                </ShipViaList>\n" +
                "                <DisplayProductAvailability>Show w/ Qty</DisplayProductAvailability>\n" +
                "            </EntityRemoteData>\n" +
                "            <ClassificationItemList>\n" +
                "                <ClassificationItem>\n" +
                "                    <Name>Latitude_cust</Name>\n" +
                "                    <Value>999</Value>\n" +
                "                </ClassificationItem>\n" +
                "                <ClassificationItem>\n" +
                "                    <Name>RegCreditMgr</Name>\n" +
                "                    <Value>MD04093</Value>\n" +
                "                </ClassificationItem>\n" +
                "                <ClassificationItem>\n" +
                "                    <Name>Wds_type</Name>\n" +
                "                    <Value>Plumbing Residential New Construction</Value>\n" +
                "                </ClassificationItem>\n" +
                "                <ClassificationItem>\n" +
                "                    <Name>Time_zone</Name>\n" +
                "                    <Value>ET</Value>\n" +
                "                </ClassificationItem>\n" +
                "                <ClassificationItem>\n" +
                "                    <Name>Prev_cust_sel_code</Name>\n" +
                "                    <Value>15893</Value>\n" +
                "                </ClassificationItem>\n" +
                "            </ClassificationItemList>\n" +
                "        </Entity>\n" +
                "        <StatusResult Success=\"Yes\">\n" +
                "            <Description>Entity Information retrieved successfully.</Description>\n" +
                "        </StatusResult>\n" +
                "    </EntityInquiryResponse>\n" +
                "</IDMS-XML>";
        return xml;
    }

    public static String AccountResponseSuccess4() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE IDMS-XML SYSTEM \"http://www.eclipseinc.com/dtd/IDMS-XML.dtd\">\n" +
                "<IDMS-XML>\n" +
                "    <EntityInquiryResponse>\n" +
                "        <SessionID></SessionID>\n" +
                "        <Entity>\n" +
                "            <EntityID>2222222</EntityID>\n" +
                "            <EntityName>fdsa</EntityName>\n" +
                "            <BillTo>\n" +
//                "                <EntityID>302841</EntityID>\n" +
                "            </BillTo>\n" +
                "            <Branch>\n" +
                "                <BranchID>1310</BranchID>\n" +
                "                <BranchName>CHARLOTTE</BranchName>\n" +
                "            </Branch>\n" +
                "            <Address>\n" +
                "                <StreetLineOne>1201 INDUSTRIAL DR</StreetLineOne>\n" +
                "                <StreetLineTwo></StreetLineTwo>\n" +
                "                <StreetLineThree></StreetLineThree>\n" +
                "                <City>MATTHEWS</City>\n" +
                "                <State>NC</State>\n" +
                "                <PostalCode>28105-5311</PostalCode>\n" +
                "                <Country></Country>\n" +
                "            </Address>\n" +
                "            <EntityKeywords>15893MRY\n" +
                "MRY.ADD</EntityKeywords>\n" +
                "            <ContactShortList>\n" +
                "                <ContactShort>\n" +
                "                    <Description>Fax</Description>\n" +
                "                    <Telephone>\n" +
                "                        <Number></Number>\n" +
                "                    </Telephone>\n" +
                "                </ContactShort>\n" +
                "            </ContactShortList>\n" +
                "            <CustomerTypeList>\n" +
                "                <CustomerType>P_RNC</CustomerType>\n" +
                "            </CustomerTypeList>\n" +
                "            <DfltPriceClass>P_RNC30</DfltPriceClass>\n" +
                "            <EntityNoteList>\n" +
                "                <EntityNote>\n" +
                "                    <Type>WDS History</Type>\n" +
                "                    <Description></Description>\n" +
                "                </EntityNote>\n" +
                "            </EntityNoteList>\n" +
                "            <Credit Currency=\"US\" CreditLimit=\"125000.00\" CreditAvailable=\"88101.82\" OrderEntryOK=\"Yes\" Terms=\"Net 2nd 15th Prox\">\n" +
                "                <Description>Credit OK</Description>\n" +
                "            </Credit>\n" +
                "            <EmailAddressList>\n" +
                "                <EmailAddress Type=\"\" HTMLPref=\"\">jglenn@mtbmechanical.com</EmailAddress>\n" +
                "                <EmailAddress Type=\"\" HTMLPref=\"\">jtrexler@mtbmechanical.com</EmailAddress>\n" +
                "            </EmailAddressList>\n" +
                "            <WebAddress></WebAddress>\n" +
                "            <EntityRemoteData>\n" +
                "                <OrderStatusList>\n" +
                "                    <OrderStatus Code=\"B\">Bid</OrderStatus>\n" +
                "                    <OrderStatus Code=\"C\">Call When Complete</OrderStatus>\n" +
                "                </OrderStatusList>\n" +
                "                <ShipViaList>\n" +
                "                    <ShipViaID>OT OUR TRUCK</ShipViaID>\n" +
                "                    <ShipViaID>UPS GROUND</ShipViaID>\n" +
                "                    <ShipViaID>WILL CALL</ShipViaID>\n" +
                "                </ShipViaList>\n" +
                "                <DisplayProductAvailability>Show w/ Qty</DisplayProductAvailability>\n" +
                "            </EntityRemoteData>\n" +
                "            <ClassificationItemList>\n" +
                "                <ClassificationItem>\n" +
                "                    <Name>Latitude_cust</Name>\n" +
                "                    <Value>999</Value>\n" +
                "                </ClassificationItem>\n" +
                "                <ClassificationItem>\n" +
                "                    <Name>RegCreditMgr</Name>\n" +
                "                    <Value>MD04093</Value>\n" +
                "                </ClassificationItem>\n" +
                "                <ClassificationItem>\n" +
                "                    <Name>Wds_type</Name>\n" +
                "                    <Value>Plumbing Residential New Construction</Value>\n" +
                "                </ClassificationItem>\n" +
                "                <ClassificationItem>\n" +
                "                    <Name>Time_zone</Name>\n" +
                "                    <Value>ET</Value>\n" +
                "                </ClassificationItem>\n" +
                "                <ClassificationItem>\n" +
                "                    <Name>Prev_cust_sel_code</Name>\n" +
                "                    <Value>15893</Value>\n" +
                "                </ClassificationItem>\n" +
                "            </ClassificationItemList>\n" +
                "        </Entity>\n" +
                "        <StatusResult Success=\"Yes\">\n" +
                "            <Description>Entity Information retrieved successfully.</Description>\n" +
                "        </StatusResult>\n" +
                "    </EntityInquiryResponse>\n" +
                "</IDMS-XML>";
        return xml;
    }

    public static String AccountResponseFailure() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE IDMS-XML SYSTEM \"http://www.eclipseinc.com/dtd/IDMS-XML.dtd\">\n" +
                "<IDMS-XML>\n" +
                "    <EntityInquiryResponse>\n" +
                "        <SessionID></SessionID>\n" +
                "        <StatusResult Success=\"No\">\n" +
                "            <Description>Error(s) encountered processing request.</Description>\n" +
                "            <ErrorMessageList>\n" +
                "                <ErrorMessage>\n" +
                "                    <Code>554</Code>\n" +
                "                    <Description>Part Not Found: 23493208423094.</Description>\n" +
                "                </ErrorMessage>\n" +
                "            </ErrorMessageList>\n" +
                "        </StatusResult>\n" +
                "    </EntityInquiryResponse>\n" +
                "</IDMS-XML>";
        return xml;
    }

    public static String ContactInquirySuccess() {
        String responseXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE IDMS-XML SYSTEM \"http://www.eclipseinc.com/dtd/IDMS-XML.dtd\"><IDMS-XML> <ContactInquiryResponse> <SessionID></SessionID> <Contact> <ContactID>90009</ContactID> <EntityID>425275</EntityID> <ContactName> <FirstName>Johnz</FirstName> <MiddleName>Lz</MiddleName> <LastName>Doe</LastName> <Salutation>Mr</Salutation> </ContactName> <ClassificationList> </ClassificationList> <UseEntityAddress>No</UseEntityAddress> <Address> <StreetLineOne>7355 Poston Way</StreetLineOne> <StreetLineTwo></StreetLineTwo> <StreetLineThree></StreetLineThree> <City>BOULDER</City> <State>CO</State> <PostalCode>80301-2312</PostalCode> <Country></Country> </Address> <TelephoneList> <Telephone> <Number>303-938-8801 x1136</Number> <Code>Work</Code> <Description>Direct to Cubicle</Description> </Telephone> <Telephone> <Number>303-938-8921</Number> <Code>Fax</Code> <Description>Fax Machine at work</Description> </Telephone> <Telephone> <Number>303-475-1032</Number> <Code>Cell</Code> <Description>Cell Phone</Description> </Telephone> </TelephoneList> <Title>SW Engineer 1</Title> <EmailAddressList> <EmailAddress Type=\"Business\" HTMLPref=\"HTML\">john_doe@activant.com</EmailAddress> <EmailAddress Type=\"Extra\" HTMLPref=\"Plain\">johnd@activant.com</EmailAddress> </EmailAddressList> <WebAddress>http://http://distribution.activant.com/</WebAddress> <IsSuperuser>Yes</IsSuperuser> <Login> <LoginID>ECLIPSE</LoginID> <Password>TEST</Password> </Login> <HideAccountInquiry>No</HideAccountInquiry> <HideOrderHistory>No</HideOrderHistory> <IsCreditDecisionMaker>No</IsCreditDecisionMaker> <PreventModification>No</PreventModification> <CreditLimit>Unlimited</CreditLimit> <EnableJobManagementLogin>No</EnableJobManagementLogin> <AccountManagerList> </AccountManagerList> </Contact> <StatusResult Success=\"Yes\"> <Description>Contact Information retrieved successfully.</Description> </StatusResult> </ContactInquiryResponse></IDMS-XML>";
        return responseXml;
    }

    public static String ContactInquiryFailure() {
        String responseXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE IDMS-XML SYSTEM \"http://www.eclipseinc.com/dtd/IDMS-XML.dtd\"><IDMS-XML> <ContactInquiryResponse> <SessionID></SessionID> <StatusResult Success=\"No\"> <Description>Error(s) encountered processing request.</Description> <ErrorMessageList> <ErrorMessage> <Code>562</Code> <Description>Read Fail: CONTACT .</Description> </ErrorMessage> </ErrorMessageList> </StatusResult> </ContactInquiryResponse></IDMS-XML>";
        return responseXml;
    }

    public static String ContactNewSubmitResponseSuccess() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE IDMS-XML SYSTEM \"http://www.eclipseinc.com/dtd/IDMS-XML.dtd\">\n" +
                "<IDMS-XML>\n" +
                "  <ContactNewSubmitResponse>\n" +
                "    <SessionID/>\n" +
                "    <Contact>\n" +
                "      <ContactID>89967</ContactID>\n" +
                "      <EntityID/>\n" +
                "      <ContactName>\n" +
                "        <FirstName>John2</FirstName>\n" +
                "        <LastName>Doe</LastName>\n" +
                "      </ContactName>\n" +
                "      <ClassificationList> </ClassificationList>\n" +
                "      <UseEntityAddress>Yes</UseEntityAddress>\n" +
                "      <Address>\n" +
                "        <StreetLineOne/>\n" +
                "        <StreetLineTwo/>\n" +
                "        <StreetLineThree/>\n" +
                "        <City/>\n" +
                "        <State/>\n" +
                "        <PostalCode/>\n" +
                "        <Country/>\n" +
                "      </Address>\n" +
                "      <TelephoneList>\n" +
                "        <Telephone>\n" +
                "          <Number>555-555-5555</Number>\n" +
                "        </Telephone>\n" +
                "      </TelephoneList>\n" +
                "      <Title/>\n" +
                "      <EmailAddressList>\n" +
                "        <EmailAddress Type=\"\" HTMLPref=\"\">seth+reece@dialexa.com</EmailAddress>\n" +
                "      </EmailAddressList>\n" +
                "      <WebAddress/>\n" +
                "      <IsSuperuser>No</IsSuperuser>\n" +
                "      <Login>\n" +
                "        <LoginID>login</LoginID>\n" +
                "        <Password>Password1</Password>\n" +
                "      </Login>\n" +
                "      <HideAccountInquiry>No</HideAccountInquiry>\n" +
                "      <HideOrderHistory>No</HideOrderHistory>\n" +
                "      <IsCreditDecisionMaker>No</IsCreditDecisionMaker>\n" +
                "      <PreventModification>No</PreventModification>\n" +
                "      <CreditLimit>Unlimited</CreditLimit>\n" +
                "      <EnableJobManagementLogin>No</EnableJobManagementLogin>\n" +
                "      <AccountManagerList> </AccountManagerList>\n" +
                "    </Contact>\n" +
                "    <StatusResult Success=\"Yes\">\n" +
                "      <Description>Contact information created successfully.</Description>\n" +
                "    </StatusResult>\n" +
                "  </ContactNewSubmitResponse>\n" +
                "</IDMS-XML>";
    }

    public static String ContactNewSubmitResponseFailure() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE IDMS-XML SYSTEM \"http://www.eclipseinc.com/dtd/IDMS-XML.dtd\">\n" +
                "<IDMS-XML>\n" +
                "  <ContactNewSubmitResponse>\n" +
                "    <SessionID/>\n" +
                "    <Contact>\n" +
                "      <ContactID>89967</ContactID>\n" +
                "      <EntityID/>\n" +
                "      <ContactName>\n" +
                "        <FirstName>John2</FirstName>\n" +
                "        <LastName>Doe</LastName>\n" +
                "      </ContactName>\n" +
                "      <ClassificationList> </ClassificationList>\n" +
                "      <UseEntityAddress>Yes</UseEntityAddress>\n" +
                "      <Address>\n" +
                "        <StreetLineOne/>\n" +
                "        <StreetLineTwo/>\n" +
                "        <StreetLineThree/>\n" +
                "        <City/>\n" +
                "        <State/>\n" +
                "        <PostalCode/>\n" +
                "        <Country/>\n" +
                "      </Address>\n" +
                "      <TelephoneList>\n" +
                "        <Telephone>\n" +
                "          <Number>555-555-5555</Number>\n" +
                "        </Telephone>\n" +
                "      </TelephoneList>\n" +
                "      <Title/>\n" +
                "      <EmailAddressList>\n" +
                "        <EmailAddress Type=\"\" HTMLPref=\"\">seth+reece@dialexa.com</EmailAddress>\n" +
                "      </EmailAddressList>\n" +
                "      <WebAddress/>\n" +
                "      <IsSuperuser>No</IsSuperuser>\n" +
                "      <Login>\n" +
                "        <LoginID>login</LoginID>\n" +
                "        <Password>Password1</Password>\n" +
                "      </Login>\n" +
                "      <HideAccountInquiry>No</HideAccountInquiry>\n" +
                "      <HideOrderHistory>No</HideOrderHistory>\n" +
                "      <IsCreditDecisionMaker>No</IsCreditDecisionMaker>\n" +
                "      <PreventModification>No</PreventModification>\n" +
                "      <CreditLimit>Unlimited</CreditLimit>\n" +
                "      <EnableJobManagementLogin>No</EnableJobManagementLogin>\n" +
                "      <AccountManagerList> </AccountManagerList>\n" +
                "    </Contact>\n" +
                "    <StatusResult Success=\"No\">\n" +
                "      <Description>Contact information created successfully.</Description>\n" +
                "    </StatusResult>\n" +
                "  </ContactNewSubmitResponse>\n" +
                "</IDMS-XML>";
    }

    public static CreateContactRequestDTO CreateContactRequest() {
        CreateContactRequestDTO createContactRequestDTO = new CreateContactRequestDTO();
        createContactRequestDTO.setEmail("test@test.com");
        createContactRequestDTO.setFirstName("john");
        createContactRequestDTO.setLastName("doe");
        createContactRequestDTO.setTelephone("555-555-5555");
        return createContactRequestDTO;

    }

    public static UpdateContactRequestDTO CreateUpdateContactRequest() {
        UpdateContactRequestDTO updateContactRequestDTO = new UpdateContactRequestDTO();
        updateContactRequestDTO.setEmail("soenthadsrlgfhjk");
        updateContactRequestDTO.setFirstName("k");
        updateContactRequestDTO.setLastName("asdf");
        updateContactRequestDTO.setPhoneNumber("1231234523");
        return updateContactRequestDTO;
    }

    public static String UpdateContactResponseSuccess() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE IDMS-XML SYSTEM \"http://www.eclipseinc.com/dtd/IDMS-XML.dtd\">\n" +
                "<IDMS-XML>\n" +
                "  <ContactUpdateSubmitResponse>\n" +
                "    <SessionID/>\n" +
                "    <Contact>\n" +
                "      <ContactID>90893</ContactID>\n" +
                "      <EntityID>425275</EntityID>\n" +
                "      <ContactName>\n" +
                "        <FirstName>Johnz</FirstName>\n" +
                "        <MiddleName>L</MiddleName>\n" +
                "        <LastName>Doe</LastName>\n" +
                "        <Salutation>Mr</Salutation>\n" +
                "      </ContactName>\n" +
                "      <ClassificationList> </ClassificationList>\n" +
                "      <UseEntityAddress>No</UseEntityAddress>\n" +
                "      <Address>\n" +
                "        <StreetLineOne>7355 Poston Way</StreetLineOne>\n" +
                "        <StreetLineTwo/>\n" +
                "        <StreetLineThree/>\n" +
                "        <City>BOULDER</City>\n" +
                "        <State>CO</State>\n" +
                "        <PostalCode>80301-2312</PostalCode>\n" +
                "        <Country/>\n" +
                "      </Address>\n" +
                "      <TelephoneList>\n" +
                "        <Telephone>\n" +
                "          <Number>303-938-8801 x1136</Number>\n" +
                "          <Code>Work</Code>\n" +
                "          <Description>Direct to Cubicle</Description>\n" +
                "        </Telephone>\n" +
                "        <Telephone>\n" +
                "          <Number>303-938-8921</Number>\n" +
                "          <Code>Fax</Code>\n" +
                "          <Description>Fax Machine at work</Description>\n" +
                "        </Telephone>\n" +
                "        <Telephone>\n" +
                "          <Number>303-475-1032</Number>\n" +
                "          <Code>Cell</Code>\n" +
                "          <Description>Cell Phone</Description>\n" +
                "        </Telephone>\n" +
                "      </TelephoneList>\n" +
                "      <Title>SW Engineer 1</Title>\n" +
                "      <EmailAddressList>\n" +
                "        <EmailAddress Type=\"Business\" HTMLPref=\"HTML\">john_doe@activant.com</EmailAddress>\n" +
                "        <EmailAddress Type=\"Extra\" HTMLPref=\"Plain\">johnd@activant.com</EmailAddress>\n" +
                "      </EmailAddressList>\n" +
                "      <WebAddress>http://http://distribution.activant.com/</WebAddress>\n" +
                "      <IsSuperuser>Yes</IsSuperuser>\n" +
                "      <Login>\n" +
                "        <LoginID>ECLIPSE</LoginID>\n" +
                "        <Password>TEST</Password>\n" +
                "      </Login>\n" +
                "      <HideAccountInquiry>No</HideAccountInquiry>\n" +
                "      <HideOrderHistory>No</HideOrderHistory>\n" +
                "      <IsCreditDecisionMaker>No</IsCreditDecisionMaker>\n" +
                "      <PreventModification>No</PreventModification>\n" +
                "      <CreditLimit>Unlimited</CreditLimit>\n" +
                "      <EnableJobManagementLogin>No</EnableJobManagementLogin>\n" +
                "      <AccountManagerList> </AccountManagerList>\n" +
                "    </Contact>\n" +
                "    <StatusResult Success=\"Yes\">\n" +
                "      <Description>Contact information updated successfully.</Description>\n" +
                "    </StatusResult>\n" +
                "  </ContactUpdateSubmitResponse>\n" +
                "</IDMS-XML>";
    }

    public static String GetContactResponseFailure() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE IDMS-XML SYSTEM \"http://www.eclipseinc.com/dtd/IDMS-XML.dtd\">\n" +
                "<IDMS-XML>\n" +
                "  <ContactInquiryResponse>\n" +
                "    <SessionID/>\n" +
                "    <StatusResult Success=\"No\">\n" +
                "      <Description>Error(s) encountered processing request.</Description>\n" +
                "      <ErrorMessageList>\n" +
                "        <ErrorMessage>\n" +
                "          <Code>533</Code>\n" +
                "          <Description>Login: ECLIPSE already taken. Please choose a different login.</Description>\n" +
                "        </ErrorMessage>\n" +
                "      </ErrorMessageList>\n" +
                "    </StatusResult>\n" +
                "  </ContactInquiryResponse>\n" +
                "</IDMS-XML>";
    }

    public static String UpdateContactResponseFailure() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE IDMS-XML SYSTEM \"http://www.eclipseinc.com/dtd/IDMS-XML.dtd\">\n" +
                "<IDMS-XML>\n" +
                "  <ContactUpdateSubmitResponse>\n" +
                "    <SessionID/>\n" +
                "    <StatusResult Success=\"No\">\n" +
                "      <Description>Error(s) encountered processing request.</Description>\n" +
                "      <ErrorMessageList>\n" +
                "        <ErrorMessage>\n" +
                "          <Code>533</Code>\n" +
                "          <Description>Login: ECLIPSE already taken. Please choose a different login.</Description>\n" +
                "        </ErrorMessage>\n" +
                "      </ErrorMessageList>\n" +
                "    </StatusResult>\n" +
                "  </ContactUpdateSubmitResponse>\n" +
                "</IDMS-XML>";
    }

    public static String MassSalesInquiryResponseSuccess() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE IDMS-XML SYSTEM \"http://www.eclipseinc.com/dtd/IDMS-XML.dtd\">\n" +
                "<IDMS-XML>\n" +
                "  <MassSalesOrderInquiryResponse>\n" +
                "    <SessionID/>\n" +
                "    <SalesOrderList>\n" +
                "      <SalesOrder>\n" +
                "        <OrderHeader>\n" +
                "          <OrderID>S110436695</OrderID>\n" +
                "          <InvoiceNumber>0001</InvoiceNumber>\n" +
                "          <OrderStatus Code=\"C\">Call When Complete</OrderStatus>\n" +
                "          <PrintStatus>N</PrintStatus>\n" +
                "          <OrderType>Open Order</OrderType>\n" +
                "          <QuoteStatus>ORDER</QuoteStatus>\n" +
                "          <CustomerPO>3</CustomerPO>\n" +
                "          <PricingBranch>\n" +
                "            <Branch>\n" +
                "              <BranchID>1003</BranchID>\n" +
                "              <BranchName>CRRLLTN PLBG</BranchName>\n" +
                "            </Branch>\n" +
                "          </PricingBranch>\n" +
                "          <ShippingBranch>\n" +
                "            <Branch>\n" +
                "              <BranchID>1003</BranchID>\n" +
                "              <BranchName>CRRLLTN PLBG</BranchName>\n" +
                "            </Branch>\n" +
                "          </ShippingBranch>\n" +
                "          <OrderDate>\n" +
                "            <Date>04/22/2021</Date>\n" +
                "          </OrderDate>\n" +
                "          <RequiredDate>\n" +
                "            <Date>04/23/2021</Date>\n" +
                "          </RequiredDate>\n" +
                "          <ShipDate>\n" +
                "            <Date>04/23/2021</Date>\n" +
                "          </ShipDate>\n" +
                "          <LastUpdate>\n" +
                "            <DateTime>2021-04-22T16:30:55.000-05:00</DateTime>\n" +
                "          </LastUpdate>\n" +
                "          <BillTo>\n" +
                "            <Entity>\n" +
                "              <EntityID>35648</EntityID>\n" +
                "              <EntityName>HORIZON PLUMBING LTD</EntityName>\n" +
                "            </Entity>\n" +
                "          </BillTo>\n" +
                "          <ShipTo>\n" +
                "            <Entity>\n" +
                "              <EntityID>11336</EntityID>\n" +
                "              <EntityName>HORIZON PLUMBING LTD SHOP</EntityName>\n" +
                "            </Entity>\n" +
                "          </ShipTo>\n" +
                "          <ShippingInformation>\n" +
                "            <Address>\n" +
                "              <StreetLineOne>Dialexa</StreetLineOne>\n" +
                "              <StreetLineTwo>2200 Commerce St</StreetLineTwo>\n" +
                "              <StreetLineThree/>\n" +
                "              <City>Dallas</City>\n" +
                "              <State>TX</State>\n" +
                "              <PostalCode>75201</PostalCode>\n" +
                "              <Country/>\n" +
                "            </Address>\n" +
                "            <ShipVia>\n" +
                "              <ShipViaID>OT OUR TRUCK</ShipViaID>\n" +
                "              <Description>OT</Description>\n" +
                "              <ValidXMLShipVia>Yes</ValidXMLShipVia>\n" +
                "            </ShipVia>\n" +
                "            <Instructions>As a matter of fact, I do!</Instructions>\n" +
                "          </ShippingInformation>\n" +
                "          <EmailAddress>james+test20@dialexa.com</EmailAddress>\n" +
                "          <ProgressBilling>No</ProgressBilling>\n" +
                "          <SalesSource>Web Order Entry</SalesSource>\n" +
                "          <OrderSource>IDMS-XML</OrderSource>\n" +
                "        </OrderHeader>\n" +
                "        <LineItemList>\n" +
                "          <LineItemCount>1</LineItemCount>\n" +
                "        </LineItemList>\n" +
                "        <OrderTotals>\n" +
                "          <Subtotal>15.99</Subtotal>\n" +
                "          <Total>44.37</Total>\n" +
                "        </OrderTotals>\n" +
                "      </SalesOrder>\n" +
                "    </SalesOrderList>\n" +
                "    <StatusResult Success=\"Yes\">\n" +
                "      <Description>Mass Sales Order Information retrieved successfully.</Description>\n" +
                "    </StatusResult>\n" +
                "  </MassSalesOrderInquiryResponse>\n" +
                "</IDMS-XML>";
    }

    public static String MassSalesInquiryResponseFailure() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE IDMS-XML SYSTEM \"http://www.eclipseinc.com/dtd/IDMS-XML.dtd\">\n" +
                "<IDMS-XML>\n" +
                "  <MassSalesOrderInquiryResponse>\n" +
                "    <SessionID/>\n" +
                "    <StatusResult Success=\"No\">\n" +
                "      <Description>Error(s) encountered processing request.</Description>\n" +
                "      <ErrorMessageList>\n" +
                "        <ErrorMessage>\n" +
                "          <Code>527</Code>\n" +
                "          <Description>Insufficient access.</Description>\n" +
                "        </ErrorMessage>\n" +
                "      </ErrorMessageList>\n" +
                "    </StatusResult>\n" +
                "  </MassSalesOrderInquiryResponse>\n" +
                "</IDMS-XML>";
    }

    public static String AccountInquiryResponseSuccess() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE IDMS-XML SYSTEM \"http://www.eclipseinc.com/dtd/IDMS-XML.dtd\">\n" +
                "<IDMS-XML>\n" +
                "  <AccountInquiryResponse>\n" +
                "    <SessionID/>\n" +
                "    <AccountInquirySummary>\n" +
                "      <EntityStartDate>\n" +
                "        <Date>06/23/2008</Date>\n" +
                "      </EntityStartDate>\n" +
                "      <AsOfDate>\n" +
                "        <Date>04/22/2021</Date>\n" +
                "      </AsOfDate>\n" +
                "      <Future>530.44</Future>\n" +
                "      <Current>745973.49</Current>\n" +
                "      <Thirty>534601.93</Thirty>\n" +
                "      <Sixty>8027.58</Sixty>\n" +
                "      <Ninety>8801.03</Ninety>\n" +
                "      <OneTwenty>3061.20</OneTwenty>\n" +
                "      <ARTotal>1300977.67</ARTotal>\n" +
                "      <ARDeposits>-18.00</ARDeposits>\n" +
                "      <AROrders>475869.55</AROrders>\n" +
                "      <MTDSales>489.98</MTDSales>\n" +
                "      <YTDSales>1755422.88</YTDSales>\n" +
                "      <SixMonthAverage>981930.68</SixMonthAverage>\n" +
                "      <SixMonthHigh>1519516.54</SixMonthHigh>\n" +
                "      <PaymentDays>42</PaymentDays>\n" +
                "      <ARTerms>3% 26TH</ARTerms>\n" +
                "      <ARCreditLimit>1000001.00</ARCreditLimit>\n" +
                "      <ARCreditAvail>-288201.94</ARCreditAvail>\n" +
                "      <LastSale>\n" +
                "        <Amount>17.31</Amount>\n" +
                "        <Date>04/22/21</Date>\n" +
                "      </LastSale>\n" +
                "      <LastPayment>\n" +
                "        <Amount>42.75</Amount>\n" +
                "        <Date>04/09/21</Date>\n" +
                "      </LastPayment>\n" +
                "      <Currency>US</Currency>\n" +
                "    </AccountInquirySummary>\n" +
                "    <AccountInquiryItemList>\n" +
                "      <AccountInquiryItem>\n" +
                "        <AccountRegisterID>S108411768.001</AccountRegisterID>\n" +
                "        <BranchID>1003</BranchID>\n" +
                "        <Transaction>\n" +
                "          <Amount>1683.19</Amount>\n" +
                "          <Date>05/08/2020</Date>\n" +
                "        </Transaction>\n" +
                "        <Payment>\n" +
                "          <Amount>1684.13</Amount>\n" +
                "          <Date>08/31/2020</Date>\n" +
                "        </Payment>\n" +
                "        <Balance>-0.94</Balance>\n" +
                "        <Age>Over120</Age>\n" +
                "        <CustomerPO>475615</CustomerPO>\n" +
                "        <Currency>US</Currency>\n" +
                "        <ShippingInformation>\n" +
                "          <EntityName>AMERICAN LEGEND/CANYON FALLS</EntityName>\n" +
                "          <Address>\n" +
                "            <StreetLineOne>1100 DAYLILY DR</StreetLineOne>\n" +
                "            <StreetLineTwo/>\n" +
                "            <StreetLineThree/>\n" +
                "            <City>NORTHLAKE</City>\n" +
                "            <State>TX</State>\n" +
                "            <PostalCode>76226</PostalCode>\n" +
                "            <Country/>\n" +
                "          </Address>\n" +
                "          <ShipVia>\n" +
                "            <ShipViaID>OT OUR TRUCK</ShipViaID>\n" +
                "            <Description>OT</Description>\n" +
                "            <ValidXMLShipVia>Yes</ValidXMLShipVia>\n" +
                "          </ShipVia>\n" +
                "          <Instructions>DEL TO JOB FIRST AM FRIDAY 5/8/20\n" +
                "\n" +
                "TRAVIS 817/630/9673</Instructions>\n" +
                "        </ShippingInformation>\n" +
                "      </AccountInquiryItem>\n" +
                "      </AccountInquiryItemList>\n" +
                "    <StatusResult Success=\"Yes\">\n" +
                "      <Description>Account Inquiry Information retrieved successfully.</Description>\n" +
                "    </StatusResult>\n" +
                "  </AccountInquiryResponse>\n" +
                "</IDMS-XML>";
    }

    public static String AccountInquiryResponseFailure() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE IDMS-XML SYSTEM \"http://www.eclipseinc.com/dtd/IDMS-XML.dtd\">\n" +
                "<IDMS-XML>\n" +
                "  <AccountInquiryResponse>\n" +
                "    <SessionID/>\n" +
                "    <StatusResult Success=\"No\">\n" +
                "      <Description>Error(s) encountered processing request.</Description>\n" +
                "      <ErrorMessageList>\n" +
                "        <ErrorMessage>\n" +
                "          <Code>527</Code>\n" +
                "          <Description>Insufficient access.</Description>\n" +
                "        </ErrorMessage>\n" +
                "      </ErrorMessageList>\n" +
                "    </StatusResult>\n" +
                "  </AccountInquiryResponse>\n" +
                "</IDMS-XML>";
    }

    public static String GetCreditCardList() {
        String successXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE IDMS-XML SYSTEM \"http://www.eclipseinc.com/dtd/IDMS-XML.dtd\">\n" +
                "<IDMS-XML>\n" +
                "<EntityInquiryResponse>\n" +
                "<SessionID></SessionID>\n" +
                "<Entity>\n" +
                "<CreditCardList>\n" +
                "<CreditCard>\n" +
                "<CreditCardType>Other</CreditCardType>\n" +
                "<CreditCardNumber>4444</CreditCardNumber>\n" +
                "<ExpirationDate>\n" +
                "<Date>02/01/2022</Date>\n" +
                "</ExpirationDate>\n" +
                "<CardHolder>Morshed John</CardHolder>\n" +
                "<StreetAddress>5813 Marsh rail</StreetAddress>\n" +
                "<PostalCode>76208</PostalCode>\n" +
                "<ElementPaymentAccountId>411FFEB0-95C1-412D-83ED-479B3821D39D</ElementPaymentAccountId>\n" +
                "</CreditCard>\n" +
                "</CreditCardList>\n" +
                "</Entity>\n" +
                "<StatusResult Success=\"Yes\">\n" +
                "<Description>Entity Information retrieved successfully.</Description>\n" +
                "</StatusResult>\n" +
                "</EntityInquiryResponse>\n" +
                "</IDMS-XML>";
        return successXML;
    }
    public static String GetCreditUpdateFailure() {
        String updateFailure = "<IDMS-XML>\n" +
                "    <EntityUpdateSubmitResponse>\n" +
                "        <SessionID></SessionID>\n" +
                "        <StatusResult Success=\"No\">\n" +
                "            <Description>Error(s) encountered processing request.</Description>\n" +
                "            <ErrorMessageList>\n" +
                "                <ErrorMessage>\n" +
                "                    <Code>824</Code>\n" +
                "                    <Description>Failed to query Element account F9AFA9E8-51C5-4F38-9EF2-7D57014543A5: PaymentAccount record(s) not found</Description>\n" +
                "                </ErrorMessage>\n" +
                "            </ErrorMessageList>\n" +
                "        </StatusResult>\n" +
                "    </EntityUpdateSubmitResponse>\n" +
                "</IDMS-XML>";
        return updateFailure;
    }

    public static String CreditCardSetupURLSuccess() {
        String setUpUrlXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE IDMS-XML SYSTEM \"http://www.eclipseinc.com/dtd/IDMS-XML.dtd\">\n" +
                "<IDMS-XML>\n" +
                "    <ElementAccountSetupResponse>\n" +
                "        <SessionID></SessionID>\n" +
                "        <ElementSetupUrl>https://certtransaction.hostedpayments.com/?TransactionSetupID=83B5AA85-0E57-4439-A564-AFE933C1DB1F</ElementSetupUrl>\n" +
                "        <ElementSetupId>83B5AA85-0E57-4439-A564-AFE933C1DB1F</ElementSetupId>\n" +
                "        <StatusResult Success=\"Yes\">\n" +
                "            <Description>Element PASS Account session setup successfully.</Description>\n" +
                "        </StatusResult>\n" +
                "    </ElementAccountSetupResponse>\n" +
                "</IDMS-XML>";
        return setUpUrlXML;
    }
    public static String CreditCardSetupURLFailure() {
        String setUpUrlXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE IDMS-XML SYSTEM \"http://www.eclipseinc.com/dtd/IDMS-XML.dtd\">\n" +
                "<IDMS-XML>\n" +
                "    <ElementAccountSetupResponse>\n" +
                "        <SessionID></SessionID>\n" +
                "        <StatusResult Success=\"No\">\n" +
                "            <Description>Error(s) encountered processing request.</Description>\n" +
                "            <ErrorMessageList>\n" +
                "                <ErrorMessage>\n" +
                "                    <Code>828</Code>\n" +
                "                    <Description>CardHolder is required for the ElementAccountSetupData element but not found.</Description>\n" +
                "                </ErrorMessage>\n" +
                "            </ErrorMessageList>\n" +
                "        </StatusResult>\n" +
                "    </ElementAccountSetupResponse>\n" +
                "</IDMS-XML>";
        return setUpUrlXML;
    }

    public static String GetCreditCardElementInfoSuccess() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE IDMS-XML SYSTEM \"http://www.eclipseinc.com/dtd/IDMS-XML.dtd\">\n" +
                "<IDMS-XML>\n" +
                "    <ElementSetupQueryResponse>\n" +
                "        <SessionID></SessionID>\n" +
                "        <CreditCard>\n" +
                "            <CreditCardType>Visa</CreditCardType>\n" +
                "            <CreditCardNumber>1111</CreditCardNumber>\n" +
                "            <ExpirationDate>\n" +
                "                <Date>01/31/2023</Date>\n" +
                "            </ExpirationDate>\n" +
                "            <CardHolder>Susan Grant</CardHolder>\n" +
                "            <StreetAddress>5813 Marsh rail</StreetAddress>\n" +
                "            <PostalCode>76208</PostalCode>\n" +
                "            <ElementPaymentAccountId>E5343299-A5D1-4687-88DA-961F9FA7629F</ElementPaymentAccountId>\n" +
                "        </CreditCard>\n" +
                "        <StatusResult Success=\"Yes\">\n" +
                "            <Description>Element PASS Account session setup successfully.</Description>\n" +
                "        </StatusResult>\n" +
                "    </ElementSetupQueryResponse>\n" +
                "</IDMS-XML>";
    }

    public static String GetCreditCardElementInfoFailure() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE IDMS-XML SYSTEM \"http://www.eclipseinc.com/dtd/IDMS-XML.dtd\">\n" +
                "<IDMS-XML>\n" +
                "    <ElementSetupQueryResponse>\n" +
                "        <SessionID></SessionID>\n" +
                "        <StatusResult Success=\"No\">\n" +
                "            <Description>Error(s) encountered processing request.</Description>\n" +
                "            <ErrorMessageList>\n" +
                "                <ErrorMessage>\n" +
                "                    <Code>830</Code>\n" +
                "                    <Description>Failed to locate payment account for Element session: PaymentAccount record(s) not found</Description>\n" +
                "                </ErrorMessage>\n" +
                "            </ErrorMessageList>\n" +
                "        </StatusResult>\n" +
                "    </ElementSetupQueryResponse>\n" +
                "</IDMS-XML>";
    }

    public static String updateEntityInquiryForCreditCardSuccess() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE IDMS-XML SYSTEM \"http://www.eclipseinc.com/dtd/IDMS-XML.dtd\">\n" +
                "<IDMS-XML>\n" +
                "    <EntityInquiryResponse>\n" +
                "        <SessionID></SessionID>\n" +
                "        <Entity>\n" +
                "            <CreditCardList>\n" +
                "                <CreditCard>\n" +
                "                    <CreditCardType>Visa</CreditCardType>\n" +
                "                    <CreditCardNumber>1111</CreditCardNumber>\n" +
                "                    <ExpirationDate>\n" +
                "                        <Date>02/01/2024</Date>\n" +
                "                    </ExpirationDate>\n" +
                "                    <CardHolder>Mike John</CardHolder>\n" +
                "                    <StreetAddress>5813 Marsh Rail Dr.</StreetAddress>\n" +
                "                    <PostalCode>76208</PostalCode>\n" +
                "                    <ElementPaymentAccountId>25905A3A-1A7A-4015-A437-E636ED4D49D1</ElementPaymentAccountId>\n" +
                "                </CreditCard>\n" +
                "            </CreditCardList>\n" +
                "        </Entity>\n" +
                "        <StatusResult Success=\"Yes\">\n" +
                "            <Description>Entity Information retrieved successfully.</Description>\n" +
                "        </StatusResult>\n" +
                "    </EntityInquiryResponse>\n" +
                "</IDMS-XML>";
    }

    public static ResponseEntity<PickingTasksResponseDTO> GetPickingTasksInfoSuccess() {
        PickingTasksResponseDTO responseDto = new PickingTasksResponseDTO();
        WarehousePickTask task = new WarehousePickTask();
        task.setOrderId("1234");
        task.setBillTo(1);
        responseDto.setResults(List.of(task));
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    public static ProductSerialNumbersResponseDTO GetSerialNumbersResponseSuccess() throws JsonProcessingException {
        String response = "{\n" +
                "  \"results\": [\n" +
                "    {\n" +
                "      \"productId\": 529,\n" +
                "      \"orderId\": \"S112228791\",\n" +
                "      \"generationId\": 1,\n" +
                "      \"invoiceId\": \"001\",\n" +
                "      \"quantity\": \"2\",\n" +
                "      \"description\": \"AOSMTH NG 98 GAL 75K COMM HTR BT-100\",\n" +
                "      \"location\": \"T09B10\",\n" +
                "      \"warehouseId\": \"S~T09B10~S112228791.1.1.0~I\",\n" +
                "      \"serialList\": [\n" +
                "        { \n" +
                "            \"line\": \"0\", \n" +
                "            \"serial\": \"12345\"\n" +
                "        },\n" +
                "        { \n" +
                "            \"line\": \"1\", \n" +
                "            \"serial\": \"54321\"\n" +
                "        }\n" +
                "          \n" +
                "      ],\n" +
                "      \"nonStockSerialNumbers\": []\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response, ProductSerialNumbersResponseDTO.class);
    }

    public static AccountInquiryResponseDTO GetAccountInquirySuccess() {
        AccountInquiryResponseDTO response = new AccountInquiryResponseDTO();
        response.setBucketThirty(10.16);
        response.setBucketSixty(20.21);
        response.setBucketNinety(40.24);
        response.setBucketOneTwenty(98.54);
        response.setTotalAmt(201.65);
        response.setCurrentAmt(0.10);
        response.setTotalPastDue(198.20);
        response.setInvoices(new ArrayList<>());
        return response;
    }

    public static String ReorderPadInquiryResponse() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE IDMS-XML SYSTEM \"http://www.eclipseinc.com/dtd/IDMS-XML.dtd\">\n" +
                "<IDMS-XML>\n" +
                " <ReorderPadInquiryResponse>\n" +
                " <SessionID>J1676462045</SessionID>\n" +
                " <ReorderPadList>\n" +
                " <ReorderPadItem>\n" +
                " <Product>\n" +
                " <Description>EU Class: General Ledger, 2 Days</Description>\n" +
                " <PartIdentifiers>\n" +
                " <EclipsePartNumber>122027</EclipsePartNumber>\n" +
                "<ProductKeywords>GENLED classesGENLED</ProductKeywords>\n" +
                "<Description>EU Class: General Ledger, 2 Days</Description>\n" +
                " </PartIdentifiers>\n" +
                " <UOMList>\n" +
                " <UOM UMQT=\"1\">dy</UOM>\n" +
                " </UOMList>\n" +
                " <Volume Unit=\"EA\">0.0000</Volume>\n" +
                " <Weight Unit=\"lbs\">0.0000</Weight>\n" +
                " </Product>\n" +
                " <Quantity UOM=\"dy\" UMQT=\"1\">104</Quantity>\n" +
                " </ReorderPadItem>\n" +
                " <ReorderPadItem>\n" +
                " <Product>\n" +
                " <Description>UFO Registration</Description>\n" +
                " <PartIdentifiers>\n" +
                " <EclipsePartNumber>504274</EclipsePartNumber>\n" +
                " <Description>UFO Registration</Description>\n" +
                " </PartIdentifiers>\n" +
                " <UOMList>\n" +
                " <UOM UMQT=\"1\">ea</UOM>\n" +
                " </UOMList>\n" +
                " <Volume Unit=\"EA\">0.0000</Volume>\n" +
                " <Weight Unit=\"lbs\">0.0000</Weight>\n" +
                " </Product>\n" +
                " <Quantity UOM=\"ea\" UMQT=\"1\">37</Quantity>\n" +
                " </ReorderPadItem>\n" +
                " <StatusResult Success=\"Yes\">\n" +
                " <Description>ReorderPad information retrieved \n" +
                "successfully.</Description>\n" +
                " </StatusResult>\n" +
                "  </ReorderPadInquiryResponse>\n" +
                "</IDMS-XML>";
    }
}
