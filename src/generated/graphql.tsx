import { gql } from '@apollo/client';
import * as Apollo from '@apollo/client';
export type Maybe<T> = T | null;
export type InputMaybe<T> = Maybe<T>;
export type Exact<T extends { [key: string]: unknown }> = { [K in keyof T]: T[K] };
export type MakeOptional<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]?: Maybe<T[SubKey]> };
export type MakeMaybe<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]: Maybe<T[SubKey]> };
const defaultOptions =  {}
/** All built-in and custom scalars, mapped to their actual values */
export type Scalars = {
  ID: string;
  String: string;
  Boolean: boolean;
  Int: number;
  Float: number;
  /** Date custom scalar type */
  Date: any;
  /** The `Upload` scalar type represents a file upload. */
  Upload: any;
};

export type Account = {
  __typename?: 'Account';
  accountNumber?: Maybe<Scalars['String']>;
  companyAddress1?: Maybe<Scalars['String']>;
  companyAddress2?: Maybe<Scalars['String']>;
  companyAddressCity?: Maybe<Scalars['String']>;
  companyAddressState?: Maybe<Scalars['String']>;
  companyAddressZip?: Maybe<Scalars['String']>;
  companyEmail?: Maybe<Scalars['String']>;
  companyName?: Maybe<Scalars['String']>;
  companyPhoneNumber?: Maybe<Scalars['String']>;
};

export type AccountInformation = {
  __typename?: 'AccountInformation';
  branch?: Maybe<ContractAddress>;
  shipToAddress?: Maybe<ContractAddress>;
};

export type AccountInput = {
  accountNumber?: InputMaybe<Scalars['String']>;
  companyAddress1?: InputMaybe<Scalars['String']>;
  companyAddress2?: InputMaybe<Scalars['String']>;
  companyAddressCity?: InputMaybe<Scalars['String']>;
  companyAddressState?: InputMaybe<Scalars['String']>;
  companyAddressZip?: InputMaybe<Scalars['String']>;
  companyEmail?: InputMaybe<Scalars['String']>;
  companyName?: InputMaybe<Scalars['String']>;
  companyPhoneNumber?: InputMaybe<Scalars['String']>;
  erp?: InputMaybe<ErpSystemEnum>;
  erpId?: InputMaybe<Scalars['ID']>;
};

export type AccountInvoice = {
  __typename?: 'AccountInvoice';
  bucketFuture: Scalars['Float'];
  bucketNinety: Scalars['Float'];
  bucketOneTwenty: Scalars['Float'];
  bucketSixty: Scalars['Float'];
  bucketThirty: Scalars['Float'];
  currentAmt: Scalars['Float'];
  invoices: Array<Invoice>;
  totalAmt: Scalars['Float'];
  totalPastDue: Scalars['Float'];
};

export type AccountLookUp = {
  __typename?: 'AccountLookUp';
  branchId?: Maybe<Scalars['String']>;
  companyName?: Maybe<Scalars['String']>;
  erpAccountId?: Maybe<Scalars['String']>;
  erpName?: Maybe<Scalars['String']>;
  isBillTo?: Maybe<Scalars['Boolean']>;
};

export type AddAllToCartUserInput = {
  itemInfoList: Array<ItemInfo>;
  shipToAccountId: Scalars['ID'];
  userId: Scalars['ID'];
};

export type AddItemToListInput = {
  erpPartNumber: Scalars['String'];
  listId: Scalars['ID'];
  quantity?: InputMaybe<Scalars['Int']>;
};

export type AddItemsInput = {
  customerNumber?: InputMaybe<Scalars['String']>;
  itemInfoList: Array<ItemInfo>;
  shipToAccountId: Scalars['ID'];
  userId: Scalars['ID'];
};

export type Address = {
  __typename?: 'Address';
  city?: Maybe<Scalars['String']>;
  companyName?: Maybe<Scalars['String']>;
  country?: Maybe<Scalars['String']>;
  custom: Scalars['Boolean'];
  id: Scalars['ID'];
  state?: Maybe<Scalars['String']>;
  street1?: Maybe<Scalars['String']>;
  street2?: Maybe<Scalars['String']>;
  zip?: Maybe<Scalars['String']>;
};

export type AddressInput = {
  city: Scalars['String'];
  companyName?: InputMaybe<Scalars['String']>;
  country: Scalars['String'];
  custom: Scalars['Boolean'];
  state: Scalars['String'];
  street1: Scalars['String'];
  street2?: InputMaybe<Scalars['String']>;
  zip: Scalars['String'];
};

export type AggregationItem = {
  __typename?: 'AggregationItem';
  count?: Maybe<Scalars['Int']>;
  value?: Maybe<Scalars['String']>;
};

export type AggregationResults = {
  __typename?: 'AggregationResults';
  brands?: Maybe<Array<AggregationItem>>;
  btu?: Maybe<Array<AggregationItem>>;
  capacity?: Maybe<Array<AggregationItem>>;
  category?: Maybe<Array<AggregationItem>>;
  colorFinish?: Maybe<Array<AggregationItem>>;
  depth?: Maybe<Array<AggregationItem>>;
  environmentalOptions?: Maybe<Array<AggregationItem>>;
  flowRate?: Maybe<Array<AggregationItem>>;
  height?: Maybe<Array<AggregationItem>>;
  inStockLocation?: Maybe<Array<AggregationItem>>;
  inletSize?: Maybe<Array<AggregationItem>>;
  length?: Maybe<Array<AggregationItem>>;
  lines?: Maybe<Array<AggregationItem>>;
  material?: Maybe<Array<AggregationItem>>;
  pressureRating?: Maybe<Array<AggregationItem>>;
  productTypes?: Maybe<Array<AggregationItem>>;
  size?: Maybe<Array<AggregationItem>>;
  temperatureRating?: Maybe<Array<AggregationItem>>;
  tonnage?: Maybe<Array<AggregationItem>>;
  voltage?: Maybe<Array<AggregationItem>>;
  wattage?: Maybe<Array<AggregationItem>>;
  width?: Maybe<Array<AggregationItem>>;
};

export type ApproveQuoteAddressInput = {
  city: Scalars['String'];
  country?: InputMaybe<Scalars['String']>;
  postalCode: Scalars['String'];
  state: Scalars['String'];
  streetLineOne: Scalars['String'];
  streetLineThree?: InputMaybe<Scalars['String']>;
  streetLineTwo?: InputMaybe<Scalars['String']>;
};

export type ApproveQuoteInput = {
  address?: InputMaybe<ApproveQuoteAddressInput>;
  billToEntityId: Scalars['ID'];
  branchId: Scalars['ID'];
  creditCard?: InputMaybe<CreditCardInput>;
  instructions?: InputMaybe<Scalars['String']>;
  isDelivery: Scalars['Boolean'];
  orderedBy: Scalars['String'];
  phoneNumber?: InputMaybe<Scalars['String']>;
  poNumber: Scalars['String'];
  preferredDate?: InputMaybe<Scalars['Date']>;
  preferredTime?: InputMaybe<PreferredTimeEnum>;
  quoteId: Scalars['ID'];
  shipToEntityId: Scalars['ID'];
  shouldShipFullOrder: Scalars['Boolean'];
  userId: Scalars['ID'];
};

export type ApproveUserInput = {
  approverId?: InputMaybe<Scalars['ID']>;
  userId?: InputMaybe<Scalars['String']>;
  userRoleId?: InputMaybe<Scalars['String']>;
};

export type ApprovedUser = {
  __typename?: 'ApprovedUser';
  approverId?: Maybe<Scalars['ID']>;
  contactUpdatedAt?: Maybe<Scalars['String']>;
  contactUpdatedBy?: Maybe<Scalars['String']>;
  email?: Maybe<Scalars['String']>;
  firstName?: Maybe<Scalars['String']>;
  id?: Maybe<Scalars['String']>;
  lastName?: Maybe<Scalars['String']>;
  phoneNumber?: Maybe<Scalars['String']>;
  phoneType: PhoneType;
  role?: Maybe<Role>;
};

export type Approver = {
  __typename?: 'Approver';
  email?: Maybe<Scalars['String']>;
  firstName?: Maybe<Scalars['String']>;
  id: Scalars['ID'];
  lastName?: Maybe<Scalars['String']>;
};

export type Bonding = {
  bondNumber?: InputMaybe<Scalars['String']>;
  city?: InputMaybe<Scalars['String']>;
  phoneNumber?: InputMaybe<Scalars['String']>;
  postalCode?: InputMaybe<Scalars['String']>;
  state?: InputMaybe<Scalars['String']>;
  streetLineOne?: InputMaybe<Scalars['String']>;
  streetLineThree?: InputMaybe<Scalars['String']>;
  streetLineTwo?: InputMaybe<Scalars['String']>;
  suretyName?: InputMaybe<Scalars['String']>;
};

export type Branch = {
  __typename?: 'Branch';
  actingBranchManager?: Maybe<Scalars['String']>;
  actingBranchManagerEmail?: Maybe<Scalars['String']>;
  actingBranchManagerPhone?: Maybe<Scalars['String']>;
  address1?: Maybe<Scalars['String']>;
  address2?: Maybe<Scalars['String']>;
  branchId: Scalars['String'];
  businessHours?: Maybe<Scalars['String']>;
  city?: Maybe<Scalars['String']>;
  distance?: Maybe<Scalars['Float']>;
  entityId?: Maybe<Scalars['String']>;
  erpSystemName?: Maybe<ErpSystemEnum>;
  id: Scalars['ID'];
  isActive: Scalars['Boolean'];
  isAvailableInStoreFinder: Scalars['Boolean'];
  isBandK: Scalars['Boolean'];
  isHvac: Scalars['Boolean'];
  isPlumbing: Scalars['Boolean'];
  isPricingOnly: Scalars['Boolean'];
  isShoppable: Scalars['Boolean'];
  isWaterworks: Scalars['Boolean'];
  latitude: Scalars['Float'];
  longitude: Scalars['Float'];
  name?: Maybe<Scalars['String']>;
  phone?: Maybe<Scalars['String']>;
  state?: Maybe<Scalars['String']>;
  website?: Maybe<Scalars['String']>;
  workdayId?: Maybe<Scalars['String']>;
  workdayLocationName?: Maybe<Scalars['String']>;
  zip?: Maybe<Scalars['String']>;
};

export type BranchDetails = {
  __typename?: 'BranchDetails';
  branchNumber: Scalars['String'];
};

export type BranchOrderInfo = {
  __typename?: 'BranchOrderInfo';
  branchName?: Maybe<Scalars['String']>;
  city?: Maybe<Scalars['String']>;
  country?: Maybe<Scalars['String']>;
  postalCode?: Maybe<Scalars['String']>;
  state?: Maybe<Scalars['String']>;
  streetLineOne?: Maybe<Scalars['String']>;
  streetLineThree?: Maybe<Scalars['String']>;
  streetLineTwo?: Maybe<Scalars['String']>;
};

export type BranchSearch = {
  branchSearchRadius?: InputMaybe<MileRadiusEnum>;
  count?: InputMaybe<Scalars['Int']>;
  isShoppable?: InputMaybe<Scalars['Boolean']>;
  isStoreFinder?: InputMaybe<Scalars['Boolean']>;
  latitude?: InputMaybe<Scalars['Float']>;
  longitude?: InputMaybe<Scalars['Float']>;
  territory?: InputMaybe<Scalars['String']>;
};

export type BranchSearchResult = {
  __typename?: 'BranchSearchResult';
  branches: Array<Branch>;
  latitude: Scalars['Float'];
  longitude: Scalars['Float'];
};

export type CardHolderInput = {
  cardHolder: Scalars['String'];
  postalCode: Scalars['String'];
  returnUrl: Scalars['String'];
  streetAddress: Scalars['String'];
};

export type Cart = {
  __typename?: 'Cart';
  approvalState?: Maybe<Scalars['ID']>;
  approverId?: Maybe<Scalars['ID']>;
  creditCard?: Maybe<CreditCard>;
  delivery: Delivery;
  deliveryMethod?: Maybe<DeliveryMethodEnum>;
  erpSystemName: ErpSystemEnum;
  id: Scalars['ID'];
  ownerId?: Maybe<Scalars['ID']>;
  paymentMethodType?: Maybe<PaymentMethodTypeEnum>;
  poNumber?: Maybe<Scalars['String']>;
  pricingBranchId?: Maybe<Scalars['String']>;
  products?: Maybe<Array<LineItem>>;
  rejectionReason?: Maybe<Scalars['String']>;
  removedProducts?: Maybe<Array<Scalars['String']>>;
  shipToId?: Maybe<Scalars['ID']>;
  shippingBranchId?: Maybe<Scalars['String']>;
  shippingHandling?: Maybe<Scalars['Int']>;
  subtotal?: Maybe<Scalars['Int']>;
  tax?: Maybe<Scalars['Int']>;
  total?: Maybe<Scalars['Int']>;
  willCall: WillCall;
};

export type CartInput = {
  approvalState?: InputMaybe<Scalars['ID']>;
  approverId?: InputMaybe<Scalars['ID']>;
  creditCard?: InputMaybe<CreditCardInput>;
  delivery?: InputMaybe<DeliveryInput>;
  deliveryMethod?: InputMaybe<DeliveryMethodEnum>;
  id?: InputMaybe<Scalars['ID']>;
  ownerId?: InputMaybe<Scalars['ID']>;
  paymentMethodType?: InputMaybe<PaymentMethodTypeEnum>;
  poNumber?: InputMaybe<Scalars['String']>;
  pricingBranchId?: InputMaybe<Scalars['String']>;
  rejectionReason?: InputMaybe<Scalars['String']>;
  shipToId?: InputMaybe<Scalars['ID']>;
  shippingBranchId?: InputMaybe<Scalars['String']>;
  shippingHandling?: InputMaybe<Scalars['Int']>;
  subtotal?: InputMaybe<Scalars['Int']>;
  tax?: InputMaybe<Scalars['Int']>;
  total?: InputMaybe<Scalars['Int']>;
  willCall?: InputMaybe<WillCallInput>;
};

export type CartLineItemInput = {
  branch?: InputMaybe<BranchDetailInput>;
  description?: InputMaybe<Scalars['String']>;
  lineComment?: InputMaybe<Scalars['String']>;
  lineNumber?: InputMaybe<Scalars['Int']>;
  netPrice?: InputMaybe<Scalars['String']>;
  productNumber?: InputMaybe<Scalars['String']>;
  quantityOrdered?: InputMaybe<Scalars['String']>;
  taxable?: InputMaybe<Scalars['String']>;
  unitPrice?: InputMaybe<Scalars['String']>;
};

export type Categories = {
  __typename?: 'Categories';
  eclipseCategories?: Maybe<Scalars['String']>;
  mincronCategories?: Maybe<Scalars['String']>;
};

export type Category = {
  __typename?: 'Category';
  children?: Maybe<Array<Category>>;
  name: Scalars['String'];
};

export type CheckUsersForApproverInput = {
  accountId: Scalars['ID'];
  userId: Scalars['ID'];
};

export type ContactFormInput = {
  email: Scalars['String'];
  firstName: Scalars['String'];
  lastName: Scalars['String'];
  message: Scalars['String'];
  phoneNumber?: InputMaybe<Scalars['String']>;
  topic: Scalars['String'];
  zip: Scalars['String'];
};

export type ContactInfo = {
  __typename?: 'ContactInfo';
  emailAddress?: Maybe<Scalars['String']>;
  isBranchInfo?: Maybe<Scalars['Boolean']>;
  phoneNumber?: Maybe<Scalars['String']>;
};

export type Contract = {
  __typename?: 'Contract';
  contractDate?: Maybe<Scalars['String']>;
  contractNumber?: Maybe<Scalars['String']>;
  description?: Maybe<Scalars['String']>;
  firstReleaseDate?: Maybe<Scalars['String']>;
  jobName?: Maybe<Scalars['String']>;
  jobNumber?: Maybe<Scalars['String']>;
  lastReleaseDate?: Maybe<Scalars['String']>;
  promiseDate?: Maybe<Scalars['String']>;
  purchaseOrderNumber?: Maybe<Scalars['String']>;
};

export type ContractAddItemToCartInput = {
  items?: InputMaybe<Array<CartLineItemInput>>;
};

export type ContractAddress = {
  __typename?: 'ContractAddress';
  address1?: Maybe<Scalars['String']>;
  address2?: Maybe<Scalars['String']>;
  address3?: Maybe<Scalars['String']>;
  branchName?: Maybe<Scalars['String']>;
  branchNumber?: Maybe<Scalars['String']>;
  city?: Maybe<Scalars['String']>;
  country?: Maybe<Scalars['String']>;
  county?: Maybe<Scalars['String']>;
  entityId?: Maybe<Scalars['String']>;
  state?: Maybe<Scalars['String']>;
  taxJurisdiction?: Maybe<Scalars['String']>;
  zip?: Maybe<Scalars['String']>;
};

export type ContractCartItem = {
  __typename?: 'ContractCartItem';
  description?: Maybe<Scalars['String']>;
  displayOnly?: Maybe<Scalars['String']>;
  extendedPrice?: Maybe<Scalars['String']>;
  lineComments?: Maybe<Scalars['String']>;
  lineNumber?: Maybe<Scalars['String']>;
  netPrice?: Maybe<Scalars['String']>;
  orderLineItemTypeCode?: Maybe<Scalars['String']>;
  orderNumber?: Maybe<Scalars['String']>;
  productNumber?: Maybe<Scalars['String']>;
  quantityBackOrdered?: Maybe<Scalars['String']>;
  quantityOrdered?: Maybe<Scalars['String']>;
  quantityReleasedToDate?: Maybe<Scalars['String']>;
  quantityShipped?: Maybe<Scalars['String']>;
  unitPrice?: Maybe<Scalars['String']>;
  uom?: Maybe<Scalars['String']>;
};

export type ContractDates = {
  __typename?: 'ContractDates';
  contractDate?: Maybe<Scalars['String']>;
  firstReleaseDate?: Maybe<Scalars['String']>;
  lastReleaseDate?: Maybe<Scalars['String']>;
  promisedDate?: Maybe<Scalars['String']>;
};

export type ContractDetails = {
  __typename?: 'ContractDetails';
  accountInformation?: Maybe<AccountInformation>;
  contractDates?: Maybe<ContractDates>;
  contractDescription?: Maybe<Scalars['String']>;
  contractNumber?: Maybe<Scalars['String']>;
  contractProducts?: Maybe<Array<ContractProduct>>;
  contractSummary?: Maybe<ContractSummary>;
  customerInfo?: Maybe<CustomerInfo>;
  jobName?: Maybe<Scalars['String']>;
  purchaseOrderNumber?: Maybe<Scalars['String']>;
};

export type ContractOrderDetail = {
  __typename?: 'ContractOrderDetail';
  allowCreditCardFlag?: Maybe<Scalars['String']>;
  branchName?: Maybe<Scalars['String']>;
  branchNumber?: Maybe<Scalars['String']>;
  contractNumber?: Maybe<Scalars['String']>;
  creditCardAuthAmount?: Maybe<Scalars['String']>;
  creditCardMessage?: Maybe<Scalars['String']>;
  creditCardTypes?: Maybe<Array<Scalars['String']>>;
  customerName?: Maybe<Scalars['String']>;
  editable?: Maybe<Scalars['String']>;
  enteredBy?: Maybe<Scalars['String']>;
  freightCode?: Maybe<Scalars['String']>;
  gstHstAmount?: Maybe<Scalars['String']>;
  gstHstUseCode?: Maybe<Scalars['String']>;
  invoiceNumber?: Maybe<Scalars['String']>;
  itemList?: Maybe<ItemPage>;
  jobName?: Maybe<Scalars['String']>;
  jobNumber?: Maybe<Scalars['String']>;
  merchantId?: Maybe<Scalars['String']>;
  orderBy?: Maybe<Scalars['String']>;
  orderComments?: Maybe<Array<Scalars['String']>>;
  orderDate?: Maybe<Scalars['String']>;
  orderDescription?: Maybe<Scalars['String']>;
  orderNumber?: Maybe<Scalars['String']>;
  orderQuoteCreditDebitCode?: Maybe<Scalars['String']>;
  orderType?: Maybe<Scalars['String']>;
  otherCharges?: Maybe<Scalars['String']>;
  paidByCC?: Maybe<Scalars['String']>;
  paidStatus?: Maybe<Scalars['String']>;
  phoneNumber?: Maybe<Scalars['String']>;
  promiseDate?: Maybe<Scalars['String']>;
  purchaseOrderNumber?: Maybe<Scalars['String']>;
  shipDate?: Maybe<Scalars['String']>;
  shipHandleAmount?: Maybe<Scalars['String']>;
  shipToAddress?: Maybe<ContractAddress>;
  shipVia?: Maybe<Scalars['String']>;
  shipmentMethod?: Maybe<Scalars['String']>;
  shipmentMethodSysConfig?: Maybe<Scalars['String']>;
  shoppingCartId?: Maybe<Scalars['String']>;
  specialInstructions?: Maybe<Array<Scalars['String']>>;
  status?: Maybe<Scalars['String']>;
  subTotal?: Maybe<Scalars['String']>;
  subTotalWithBackOrder?: Maybe<Scalars['String']>;
  taxAmount?: Maybe<Scalars['String']>;
  taxable?: Maybe<Scalars['String']>;
  totalAmount?: Maybe<Scalars['String']>;
  trackingNumber?: Maybe<Scalars['String']>;
  trackingURL?: Maybe<Scalars['String']>;
};

export type ContractOrderSubmitInput = {
  contractName: Scalars['String'];
  contractNumber: Scalars['String'];
  deliveryMethod: Scalars['String'];
  jobName?: InputMaybe<Scalars['String']>;
  jobNumber?: InputMaybe<Scalars['String']>;
  lineItems: Array<OrderLineItemResponseDto>;
  orderComments?: InputMaybe<Scalars['String']>;
  orderDate: Scalars['String'];
  orderTotal?: InputMaybe<Scalars['String']>;
  phoneNumber?: InputMaybe<Scalars['String']>;
  poNumber?: InputMaybe<Scalars['String']>;
  promiseDate?: InputMaybe<Scalars['String']>;
  shipBranchNumber?: InputMaybe<Scalars['String']>;
  shipCode?: InputMaybe<Scalars['String']>;
  shipDescription?: InputMaybe<Scalars['String']>;
  shipHandleAmount?: InputMaybe<Scalars['String']>;
  shipMethod?: InputMaybe<Scalars['String']>;
  shipToAddress?: InputMaybe<ShippingAddressInput>;
  shipToId: Scalars['ID'];
  spInstructions?: InputMaybe<Scalars['String']>;
  subTotal?: InputMaybe<Scalars['String']>;
  taxAmount?: InputMaybe<Scalars['String']>;
};

export type ContractProduct = {
  __typename?: 'ContractProduct';
  brand?: Maybe<Scalars['String']>;
  displayOnly?: Maybe<Scalars['Boolean']>;
  id?: Maybe<Scalars['String']>;
  lineComments?: Maybe<Scalars['String']>;
  mfr?: Maybe<Scalars['String']>;
  name?: Maybe<Scalars['String']>;
  netPrice?: Maybe<Scalars['Float']>;
  partNumber?: Maybe<Scalars['String']>;
  pricingUom?: Maybe<Scalars['String']>;
  qty?: Maybe<ContractProductQty>;
  sequenceNumber?: Maybe<Scalars['String']>;
  techSpecifications?: Maybe<Array<TechSpec>>;
  technicalDocuments?: Maybe<Array<TechDoc>>;
  thumb?: Maybe<Scalars['String']>;
  unitPrice?: Maybe<Scalars['Float']>;
};

export type ContractProductQty = {
  __typename?: 'ContractProductQty';
  quantityOrdered?: Maybe<Scalars['Int']>;
  quantityReleasedToDate?: Maybe<Scalars['Int']>;
  quantityShipped?: Maybe<Scalars['Int']>;
};

export type ContractSummary = {
  __typename?: 'ContractSummary';
  firstShipmentDate?: Maybe<Scalars['String']>;
  invoicedToDateAmount?: Maybe<Scalars['String']>;
  lastShipmentDate?: Maybe<Scalars['String']>;
  otherCharges?: Maybe<Scalars['String']>;
  subTotal?: Maybe<Scalars['String']>;
  taxAmount?: Maybe<Scalars['String']>;
  totalAmount?: Maybe<Scalars['String']>;
};

export type CreateCartRequestInput = {
  accountId?: InputMaybe<Scalars['String']>;
  application?: InputMaybe<Scalars['String']>;
  branchNumber?: InputMaybe<Scalars['String']>;
  contractNumber?: InputMaybe<Scalars['String']>;
  jobName?: InputMaybe<Scalars['String']>;
  jobNumber?: InputMaybe<Scalars['String']>;
  rePrice?: InputMaybe<Scalars['String']>;
  shipmentDetail?: InputMaybe<ShipmentDetailInput>;
  shoppingCartId?: InputMaybe<Scalars['String']>;
  userId?: InputMaybe<Scalars['String']>;
};

export type CreateEmployeeInput = {
  brand: Scalars['String'];
  email: Scalars['String'];
  firstName: Scalars['String'];
  lastName: Scalars['String'];
  password: Scalars['String'];
  phoneNumber: Scalars['String'];
  phoneType: PhoneType;
  ppAccepted: Scalars['Boolean'];
  tosAccepted: Scalars['Boolean'];
};

export type CreateListInput = {
  billToAccountId: Scalars['ID'];
  listLineItems?: InputMaybe<Array<ListLineItemInput>>;
  name: Scalars['String'];
};

export type CreateUserInput = {
  accountNumber: Scalars['String'];
  brand: Scalars['String'];
  email: Scalars['String'];
  firstName: Scalars['String'];
  lastName: Scalars['String'];
  password: Scalars['String'];
  phoneNumber: Scalars['String'];
  phoneType: PhoneType;
  ppAccepted: Scalars['Boolean'];
  tosAccepted: Scalars['Boolean'];
  zipcode: Scalars['String'];
};

export type CreateUserResponse = {
  __typename?: 'CreateUserResponse';
  approverId?: Maybe<Scalars['ID']>;
  email: Scalars['String'];
  firstName: Scalars['String'];
  id: Scalars['ID'];
  lastName: Scalars['String'];
  phoneNumber: Scalars['String'];
  roleId: Scalars['ID'];
};

export type CreditCard = {
  __typename?: 'CreditCard';
  cardHolder: Scalars['String'];
  creditCardNumber: Scalars['String'];
  creditCardType: Scalars['String'];
  elementPaymentAccountId: Scalars['ID'];
  expirationDate: DateWrapper;
  postalCode: Scalars['String'];
  streetAddress: Scalars['String'];
};

export type CreditCardElementInfoResponse = {
  __typename?: 'CreditCardElementInfoResponse';
  creditCard: CreditCard;
};

export type CreditCardInput = {
  cardHolder: Scalars['String'];
  creditCardNumber: Scalars['String'];
  creditCardType: Scalars['String'];
  elementPaymentAccountId: Scalars['ID'];
  expirationDate: DateWrapperInput;
  postalCode: Scalars['String'];
  streetAddress: Scalars['String'];
};

export type CreditCardList = {
  __typename?: 'CreditCardList';
  creditCard?: Maybe<Array<CreditCard>>;
};

export type CreditCardListResponse = {
  __typename?: 'CreditCardListResponse';
  creditCardList?: Maybe<CreditCardList>;
};

export type CreditCardResponse = {
  __typename?: 'CreditCardResponse';
  creditCardList: CreditCardList;
  statusResult: StatusResult;
};

export type CreditCardSetupUrl = {
  __typename?: 'CreditCardSetupUrl';
  elementSetupId: Scalars['String'];
  elementSetupUrl: Scalars['String'];
};

export type CustomerInfo = {
  __typename?: 'CustomerInfo';
  customerNumber?: Maybe<Scalars['String']>;
  enteredBy?: Maybe<Scalars['String']>;
  jobNumber?: Maybe<Scalars['String']>;
};

export type CustomerPartNumber = {
  __typename?: 'CustomerPartNumber';
  customer: Scalars['String'];
  partNumbers: Array<Scalars['String']>;
};

export type DateWrapper = {
  __typename?: 'DateWrapper';
  date: Scalars['String'];
};

export type DateWrapperInput = {
  date: Scalars['String'];
};

export type DeleteListResponse = {
  __typename?: 'DeleteListResponse';
  id: Scalars['ID'];
  success: Scalars['Boolean'];
};

export type DeleteUserInput = {
  accountId: Scalars['ID'];
  userId: Scalars['ID'];
  userLeftCompany: Scalars['Boolean'];
};

export type Delivery = {
  __typename?: 'Delivery';
  address?: Maybe<Address>;
  deliveryInstructions?: Maybe<Scalars['String']>;
  id: Scalars['ID'];
  phoneNumber?: Maybe<Scalars['String']>;
  preferredDate?: Maybe<Scalars['Date']>;
  preferredTime?: Maybe<PreferredTimeEnum>;
  shipToId?: Maybe<Scalars['ID']>;
  shouldShipFullOrder: Scalars['Boolean'];
};

export type DeliveryInput = {
  address?: InputMaybe<AddressInput>;
  cartId: Scalars['ID'];
  deliveryInstructions?: InputMaybe<Scalars['String']>;
  phoneNumber?: InputMaybe<Scalars['String']>;
  preferredDate?: InputMaybe<Scalars['Date']>;
  preferredTime?: InputMaybe<PreferredTimeEnum>;
  shipTo?: InputMaybe<Scalars['ID']>;
  shouldShipFullOrder: Scalars['Boolean'];
};

export enum DeliveryMethodEnum {
  Delivery = 'DELIVERY',
  Willcall = 'WILLCALL'
}

export enum DivisionEnum {
  Phvac = 'PHVAC',
  Waterworks = 'WATERWORKS'
}

export type EclipseAddress = {
  __typename?: 'EclipseAddress';
  city?: Maybe<Scalars['String']>;
  country?: Maybe<Scalars['String']>;
  postalCode?: Maybe<Scalars['String']>;
  state?: Maybe<Scalars['String']>;
  streetLineOne?: Maybe<Scalars['String']>;
  streetLineThree?: Maybe<Scalars['String']>;
  streetLineTwo?: Maybe<Scalars['String']>;
};

export type EcommAccount = {
  __typename?: 'EcommAccount';
  branchAddress?: Maybe<Scalars['String']>;
  branchId?: Maybe<Scalars['String']>;
  divisionEnum?: Maybe<DivisionEnum>;
  erpAccountId?: Maybe<Scalars['String']>;
  erpSystemName?: Maybe<ErpSystemEnum>;
  id?: Maybe<Scalars['String']>;
  name?: Maybe<Scalars['String']>;
  shipTos?: Maybe<Array<EcommAccount>>;
};

export type EmailValidationResponse = {
  __typename?: 'EmailValidationResponse';
  isEmployee: Scalars['Boolean'];
  isValid: Scalars['Boolean'];
};

export type ErpAccount = {
  __typename?: 'ErpAccount';
  alwaysCod?: Maybe<Scalars['Boolean']>;
  billToFlag?: Maybe<Scalars['Boolean']>;
  branchId?: Maybe<Scalars['String']>;
  city?: Maybe<Scalars['String']>;
  companyName?: Maybe<Scalars['String']>;
  creditHold?: Maybe<Scalars['Boolean']>;
  email?: Maybe<Array<Scalars['String']>>;
  erp?: Maybe<ErpSystemEnum>;
  erpAccountId?: Maybe<Scalars['String']>;
  /** @deprecated Field is deprecated! Use erp instead */
  erpId?: Maybe<Scalars['ID']>;
  /** @deprecated Field is deprecated! Use erp instead */
  erpName?: Maybe<Scalars['String']>;
  phoneNumber?: Maybe<Scalars['String']>;
  poReleaseRequired?: Maybe<Scalars['String']>;
  state?: Maybe<Scalars['String']>;
  street1?: Maybe<Scalars['String']>;
  street2?: Maybe<Scalars['String']>;
  territory?: Maybe<Scalars['String']>;
  zip?: Maybe<Scalars['String']>;
};

export enum ErpSystemEnum {
  Eclipse = 'ECLIPSE',
  Mincron = 'MINCRON'
}

export type ErpUserInformation = {
  __typename?: 'ErpUserInformation';
  customerNumber?: Maybe<Scalars['String']>;
  erpAccountId?: Maybe<Scalars['String']>;
  erpSystemName?: Maybe<ErpSystemEnum>;
  name?: Maybe<Scalars['String']>;
  password?: Maybe<Scalars['String']>;
  userId?: Maybe<Scalars['String']>;
};

export type ExportListLineItem = {
  __typename?: 'ExportListLineItem';
  description?: Maybe<Scalars['String']>;
  mfrName?: Maybe<Scalars['String']>;
  mfrNumber?: Maybe<Scalars['String']>;
  partNumber: Scalars['String'];
  price?: Maybe<Scalars['Int']>;
  quantity: Scalars['Int'];
};

export type ExportListResponse = {
  __typename?: 'ExportListResponse';
  listLineItems: Array<ExportListLineItem>;
};

export type Feature = {
  __typename?: 'Feature';
  id: Scalars['ID'];
  isEnabled: Scalars['Boolean'];
  name: Scalars['String'];
};

export type GeneralContractor = {
  city?: InputMaybe<Scalars['String']>;
  generalContractor?: InputMaybe<Scalars['String']>;
  phoneNumber?: InputMaybe<Scalars['String']>;
  postalCode?: InputMaybe<Scalars['String']>;
  state?: InputMaybe<Scalars['String']>;
  streetLineOne?: InputMaybe<Scalars['String']>;
  streetLineThree?: InputMaybe<Scalars['String']>;
  streetLineTwo?: InputMaybe<Scalars['String']>;
};

export type ImageUrls = {
  __typename?: 'ImageUrls';
  large?: Maybe<Scalars['String']>;
  medium?: Maybe<Scalars['String']>;
  small?: Maybe<Scalars['String']>;
  thumb?: Maybe<Scalars['String']>;
};

export type InviteOwnerInput = {
  email: Scalars['String'];
  erp?: InputMaybe<ErpSystemEnum>;
  erpAccountId: Scalars['ID'];
  erpId?: InputMaybe<Scalars['ID']>;
  firstName: Scalars['String'];
  lastName: Scalars['String'];
  userRoleId: Scalars['ID'];
};

export type InviteUserInput = {
  approverId?: InputMaybe<Scalars['ID']>;
  billToAccountId: Scalars['ID'];
  email: Scalars['String'];
  erp: ErpSystemEnum;
  erpAccountId: Scalars['String'];
  firstName: Scalars['String'];
  lastName: Scalars['String'];
  userRoleId: Scalars['ID'];
};

export type InvitedUser = {
  __typename?: 'InvitedUser';
  approverId?: Maybe<Scalars['ID']>;
  billToAccountId?: Maybe<Scalars['ID']>;
  completed: Scalars['Boolean'];
  email: Scalars['String'];
  erpAccountId: Scalars['String'];
  erpSystemName: ErpSystemEnum;
  firstName: Scalars['String'];
  id: Scalars['ID'];
  lastName: Scalars['String'];
  userRoleId: Scalars['ID'];
};

export type Invoice = {
  __typename?: 'Invoice';
  address?: Maybe<EclipseAddress>;
  age?: Maybe<Scalars['String']>;
  contractNumber?: Maybe<Scalars['String']>;
  customerPo?: Maybe<Scalars['String']>;
  deliveryMethod?: Maybe<Scalars['String']>;
  dueDate: Scalars['String'];
  invoiceDate: Scalars['String'];
  invoiceItems?: Maybe<Array<InvoiceProduct>>;
  invoiceNumber: Scalars['String'];
  invoiceUrl?: Maybe<Scalars['String']>;
  jobName?: Maybe<Scalars['String']>;
  jobNumber?: Maybe<Scalars['String']>;
  openBalance: Scalars['String'];
  originalAmt: Scalars['String'];
  otherCharges?: Maybe<Scalars['String']>;
  paidToDate?: Maybe<Scalars['String']>;
  shipDate?: Maybe<Scalars['String']>;
  status: Scalars['String'];
  subtotal?: Maybe<Scalars['String']>;
  tax?: Maybe<Scalars['String']>;
  terms?: Maybe<Scalars['String']>;
};

export type InvoiceProduct = {
  __typename?: 'InvoiceProduct';
  brand?: Maybe<Scalars['String']>;
  id?: Maybe<Scalars['String']>;
  mfr?: Maybe<Scalars['String']>;
  name?: Maybe<Scalars['String']>;
  partNumber?: Maybe<Scalars['String']>;
  price?: Maybe<Scalars['String']>;
  pricingUom?: Maybe<Scalars['String']>;
  qty?: Maybe<InvoiceProductQty>;
  thumb?: Maybe<Scalars['String']>;
};

export type InvoiceProductQty = {
  __typename?: 'InvoiceProductQty';
  quantityOrdered?: Maybe<Scalars['Int']>;
  quantityShipped?: Maybe<Scalars['Int']>;
};

export type ItemAvailability = {
  __typename?: 'ItemAvailability';
  items: Array<Maybe<LineItemResponse>>;
};

export type ItemDetailsInput = {
  accountId: Scalars['String'];
  application: Scalars['String'];
  itemDTO: ItemWrapper;
  userId: Scalars['String'];
};

export type ItemInfo = {
  minIncrementQty: Scalars['Int'];
  pricePerUnit: Scalars['Float'];
  productId: Scalars['ID'];
  qty: Scalars['Int'];
  qtyAvailable: Scalars['Int'];
  uom: Scalars['String'];
};

export type ItemPage = {
  __typename?: 'ItemPage';
  items: Array<ContractCartItem>;
  preStartRow: Scalars['Int'];
  rowsReturned: Scalars['Int'];
  totalRows: Scalars['Int'];
};

export type ItemWrapper = {
  items: Array<InputMaybe<LineItemRequest>>;
};

export type Job = {
  customerName: Scalars['String'];
  customerNumber: Scalars['String'];
  email: Scalars['String'];
  phoneNumber: Scalars['String'];
  userName: Scalars['String'];
};

export type JobFormInput = {
  bonding?: InputMaybe<Bonding>;
  generalContractor?: InputMaybe<GeneralContractor>;
  job: Job;
  lender?: InputMaybe<Lender>;
  owner?: InputMaybe<Owner>;
  project: Project;
};

export type LastOrder = {
  __typename?: 'LastOrder';
  lastDate?: Maybe<Scalars['String']>;
  lastQuantity?: Maybe<Scalars['String']>;
};

export type Lender = {
  city?: InputMaybe<Scalars['String']>;
  lenderName?: InputMaybe<Scalars['String']>;
  loanNumber?: InputMaybe<Scalars['String']>;
  phoneNumber?: InputMaybe<Scalars['String']>;
  postalCode?: InputMaybe<Scalars['String']>;
  state?: InputMaybe<Scalars['String']>;
  streetLineOne?: InputMaybe<Scalars['String']>;
  streetLineThree?: InputMaybe<Scalars['String']>;
  streetLineTwo?: InputMaybe<Scalars['String']>;
};

export type LineItem = {
  __typename?: 'LineItem';
  cartId?: Maybe<Scalars['ID']>;
  customerPartNumber?: Maybe<Scalars['String']>;
  erpPartNumber?: Maybe<Scalars['String']>;
  id?: Maybe<Scalars['ID']>;
  listIds?: Maybe<Array<Scalars['String']>>;
  priceLastUpdatedAt?: Maybe<Scalars['Date']>;
  pricePerUnit?: Maybe<Scalars['Int']>;
  product?: Maybe<Product>;
  qtyAvailable?: Maybe<Scalars['Int']>;
  qtyAvailableLastUpdatedAt?: Maybe<Scalars['Date']>;
  quantity?: Maybe<Scalars['Int']>;
  uom?: Maybe<Scalars['String']>;
};

export type LineItemRequest = {
  branch: BranchDetailInput;
  contractNumber?: InputMaybe<Scalars['String']>;
  jobNumber?: InputMaybe<Scalars['String']>;
  productNumber: Scalars['String'];
};

export type LineItemResponse = {
  __typename?: 'LineItemResponse';
  branch?: Maybe<BranchDetails>;
  productNumber: Scalars['String'];
  quantityAvailableHomeBranch: Scalars['String'];
};

export type List = {
  __typename?: 'List';
  billToAccountId: Scalars['ID'];
  id: Scalars['ID'];
  listLineItems: Array<ListLineItem>;
  listLineItemsSize?: Maybe<Scalars['Int']>;
  name: Scalars['String'];
};

export type ListItemToToggle = {
  erpPartNumber: Scalars['String'];
  quantity?: InputMaybe<Scalars['Int']>;
};

export type ListLineItem = {
  __typename?: 'ListLineItem';
  customerPartNumber?: Maybe<Array<Scalars['String']>>;
  erpPartNumber?: Maybe<Scalars['String']>;
  id: Scalars['ID'];
  imageUrls?: Maybe<ImageUrls>;
  listId: Scalars['ID'];
  manufacturerName?: Maybe<Scalars['String']>;
  manufacturerNumber?: Maybe<Scalars['String']>;
  minIncrementQty?: Maybe<Scalars['Int']>;
  name?: Maybe<Scalars['String']>;
  pricePerUnit?: Maybe<Scalars['Int']>;
  quantity?: Maybe<Scalars['Int']>;
  sortOrder?: Maybe<Scalars['Int']>;
  status?: Maybe<Scalars['String']>;
  stock?: Maybe<Stock>;
  techSpecs?: Maybe<Array<TechSpec>>;
};

export type ListLineItemInput = {
  erpPartNumber: Scalars['String'];
  quantity?: InputMaybe<Scalars['Int']>;
  sortOrder?: InputMaybe<Scalars['Int']>;
};

export type ListUploadError = {
  __typename?: 'ListUploadError';
  description?: Maybe<Scalars['String']>;
  manufacturerName?: Maybe<Scalars['String']>;
  partNumber?: Maybe<Scalars['String']>;
  quantity?: Maybe<Scalars['Int']>;
};

export type ListUploadResponse = {
  __typename?: 'ListUploadResponse';
  errors: Array<ListUploadError>;
  listId: Scalars['ID'];
  successfulRowCount: Scalars['Int'];
};

export enum MileRadiusEnum {
  Miles_25 = 'MILES_25',
  Miles_50 = 'MILES_50',
  Miles_100 = 'MILES_100',
  Miles_200 = 'MILES_200',
  Miles_400 = 'MILES_400'
}

export type Mutation = {
  __typename?: 'Mutation';
  addAllCartItemsToExistingLists: Array<Maybe<Scalars['ID']>>;
  addAllCartItemsToNewList: Scalars['ID'];
  addAllListItemsToCart: Cart;
  addCreditCard: CreditCardResponse;
  addItemToList: Scalars['ID'];
  addItemsToCart: Cart;
  approveOrder: OrderResponse;
  approveQuote: OrderResponse;
  approveUser: Scalars['String'];
  checkUsersForApprover?: Maybe<UsersOfApprover>;
  confirmNewEmployee: Scalars['Boolean'];
  createCart: Scalars['ID'];
  createDelivery: Cart;
  createJobForm: Scalars['String'];
  createList: List;
  createNewEmployee: CreateUserResponse;
  createNewUser: CreateUserResponse;
  createUser: UserRequest;
  createWillCall: Cart;
  deleteCartItems: Cart;
  deleteContractCart: Scalars['String'];
  deleteCreditCard: Scalars['ID'];
  deleteCreditCardFromCart: Cart;
  deleteItem: Cart;
  deleteList: DeleteListResponse;
  deleteUser: Scalars['Boolean'];
  inviteOwner: InvitedUser;
  inviteUser: InvitedUser;
  refreshCart: Cart;
  refreshContact: Scalars['String'];
  rejectOrder: Scalars['String'];
  rejectQuote?: Maybe<Scalars['String']>;
  rejectUser: Scalars['String'];
  resendConfirmationEmail: Scalars['String'];
  resendLegacyInviteEmail: Scalars['String'];
  resendVerificationEmail: Scalars['String'];
  sendContactForm: Scalars['String'];
  setFeatureEnabled: Scalars['Boolean'];
  submitOrder: OrderResponse;
  submitOrderPreview: OrderPreviewResponse;
  toggleItemInLists?: Maybe<ToggleItemsResponse>;
  updateBranch: Branch;
  updateCart: Cart;
  updateDelivery: Cart;
  updateItemQuantity: UpdateItemQtyResponse;
  updateList: List;
  updateUser: User;
  updateUserPassword: Scalars['Boolean'];
  updateWillCall: Cart;
  updateWillCallBranch: Cart;
  uploadNewList?: Maybe<ListUploadResponse>;
  uploadToList?: Maybe<ListUploadResponse>;
  verifyUser: Scalars['Boolean'];
};


export type MutationAddAllCartItemsToExistingListsArgs = {
  cartId: Scalars['ID'];
  listIds: Array<Scalars['String']>;
};


export type MutationAddAllCartItemsToNewListArgs = {
  accountId: Scalars['ID'];
  cartId: Scalars['ID'];
  name: Scalars['String'];
};


export type MutationAddAllListItemsToCartArgs = {
  addAllToCartUserInput: AddAllToCartUserInput;
  cartId: Scalars['ID'];
  listId: Scalars['ID'];
};


export type MutationAddCreditCardArgs = {
  accountId: Scalars['String'];
  creditCard: CreditCardInput;
};


export type MutationAddItemToListArgs = {
  addItemToListInput: AddItemToListInput;
};


export type MutationAddItemsToCartArgs = {
  addItemsInput: AddItemsInput;
  cartId: Scalars['ID'];
};


export type MutationApproveOrderArgs = {
  billToAccountId: Scalars['ID'];
  cartId: Scalars['ID'];
  shipToAccountId: Scalars['ID'];
  userId: Scalars['ID'];
};


export type MutationApproveQuoteArgs = {
  approveQuoteInput: ApproveQuoteInput;
};


export type MutationApproveUserArgs = {
  user: ApproveUserInput;
};


export type MutationCheckUsersForApproverArgs = {
  checkUsersForApproverInput: CheckUsersForApproverInput;
};


export type MutationConfirmNewEmployeeArgs = {
  id: Scalars['ID'];
};


export type MutationCreateCartArgs = {
  branchId: Scalars['ID'];
  shipToAccountId: Scalars['ID'];
  userId: Scalars['ID'];
};


export type MutationCreateDeliveryArgs = {
  deliveryInfo: DeliveryInput;
};


export type MutationCreateJobFormArgs = {
  file?: InputMaybe<Scalars['Upload']>;
  jobFormInput: JobFormInput;
};


export type MutationCreateListArgs = {
  createListInput: CreateListInput;
};


export type MutationCreateNewEmployeeArgs = {
  employee: CreateEmployeeInput;
};


export type MutationCreateNewUserArgs = {
  inviteId?: InputMaybe<Scalars['ID']>;
  user: CreateUserInput;
};


export type MutationCreateUserArgs = {
  inviteId?: InputMaybe<Scalars['ID']>;
  user: UserInput;
};


export type MutationCreateWillCallArgs = {
  willCallInfo: WillCallInput;
};


export type MutationDeleteCartItemsArgs = {
  cartId: Scalars['ID'];
};


export type MutationDeleteContractCartArgs = {
  accountId: Scalars['String'];
  application: Scalars['String'];
  branchNumber: Scalars['String'];
  shoppingCartId: Scalars['String'];
  userId: Scalars['String'];
};


export type MutationDeleteCreditCardArgs = {
  accountId: Scalars['String'];
  creditCardId: Scalars['ID'];
};


export type MutationDeleteCreditCardFromCartArgs = {
  cartId: Scalars['ID'];
};


export type MutationDeleteItemArgs = {
  cartId: Scalars['ID'];
  itemId: Scalars['ID'];
  shipToAccountId: Scalars['ID'];
  userId: Scalars['ID'];
};


export type MutationDeleteListArgs = {
  listId: Scalars['ID'];
};


export type MutationDeleteUserArgs = {
  deleteUserInput: DeleteUserInput;
};


export type MutationInviteOwnerArgs = {
  inviteOwnerInput: InviteOwnerInput;
};


export type MutationInviteUserArgs = {
  inviteUserInput: InviteUserInput;
};


export type MutationRefreshCartArgs = {
  cartId: Scalars['ID'];
  shipToAccountId: Scalars['ID'];
  userId: Scalars['ID'];
};


export type MutationRefreshContactArgs = {
  emails: Array<Scalars['String']>;
};


export type MutationRejectOrderArgs = {
  cartId: Scalars['ID'];
  rejectOrderInfo: RejectOrderInput;
  shipToAccountId: Scalars['ID'];
  userId: Scalars['ID'];
};


export type MutationRejectQuoteArgs = {
  quoteId: Scalars['ID'];
  shipToAccountId: Scalars['ID'];
  userId: Scalars['ID'];
};


export type MutationRejectUserArgs = {
  rejectUserInput: RejectUserInput;
};


export type MutationResendConfirmationEmailArgs = {
  id: Scalars['ID'];
};


export type MutationResendLegacyInviteEmailArgs = {
  legacyUserEmail: Scalars['String'];
};


export type MutationResendVerificationEmailArgs = {
  isWaterworksSubdomain?: InputMaybe<Scalars['Boolean']>;
  userId: Scalars['ID'];
};


export type MutationSendContactFormArgs = {
  contactFormInput: ContactFormInput;
};


export type MutationSetFeatureEnabledArgs = {
  featureId?: InputMaybe<Scalars['String']>;
  setFeatureEnabledInput: SetFeatureEnabledInput;
};


export type MutationSubmitOrderArgs = {
  billToAccountId: Scalars['ID'];
  cartId: Scalars['ID'];
  shipToAccountId: Scalars['ID'];
  userId: Scalars['ID'];
};


export type MutationSubmitOrderPreviewArgs = {
  billToAccountId: Scalars['ID'];
  cartId: Scalars['ID'];
  shipToAccountId: Scalars['ID'];
  userId: Scalars['ID'];
};


export type MutationToggleItemInListsArgs = {
  toggleItemsInListInput: ToggleItemsInListInput;
};


export type MutationUpdateBranchArgs = {
  input: UpdateBranchInput;
};


export type MutationUpdateCartArgs = {
  cart: CartInput;
  cartId: Scalars['ID'];
};


export type MutationUpdateDeliveryArgs = {
  deliveryInfo: DeliveryInput;
};


export type MutationUpdateItemQuantityArgs = {
  cartId: Scalars['ID'];
  customerNumber?: InputMaybe<Scalars['String']>;
  itemId: Scalars['ID'];
  minIncrementQty: Scalars['Int'];
  quantity: Scalars['Int'];
  shipToAccountId: Scalars['ID'];
  userId: Scalars['ID'];
};


export type MutationUpdateListArgs = {
  updateListInput: UpdateListInput;
};


export type MutationUpdateUserArgs = {
  updateUserInput: UpdateUserInput;
};


export type MutationUpdateUserPasswordArgs = {
  updateUserPasswordInput: UpdateUserPasswordInput;
};


export type MutationUpdateWillCallArgs = {
  willCallInfo: WillCallInput;
};


export type MutationUpdateWillCallBranchArgs = {
  branchId: Scalars['ID'];
  cartId: Scalars['ID'];
  shipToAccountId: Scalars['ID'];
  userId: Scalars['ID'];
};


export type MutationUploadNewListArgs = {
  billToAccountId: Scalars['ID'];
  file: Scalars['Upload'];
  name: Scalars['String'];
};


export type MutationUploadToListArgs = {
  file: Scalars['Upload'];
  listId: Scalars['ID'];
};


export type MutationVerifyUserArgs = {
  verificationToken: Scalars['ID'];
};

export type Order = {
  __typename?: 'Order';
  amount?: Maybe<Scalars['String']>;
  billToName?: Maybe<Scalars['String']>;
  contractNumber?: Maybe<Scalars['String']>;
  creditCard?: Maybe<CreditCard>;
  customerPO?: Maybe<Scalars['String']>;
  deliveryMethod?: Maybe<Scalars['String']>;
  invoiceNumber?: Maybe<Scalars['String']>;
  jobNumber?: Maybe<Scalars['String']>;
  lineItems?: Maybe<Array<OrderLineItem>>;
  orderDate?: Maybe<Scalars['String']>;
  orderNumber?: Maybe<Scalars['String']>;
  orderStatus?: Maybe<Scalars['String']>;
  orderTotal?: Maybe<Scalars['Float']>;
  orderedBy?: Maybe<Scalars['String']>;
  shipAddress?: Maybe<OrderAddress>;
  shipDate?: Maybe<Scalars['String']>;
  shipToId?: Maybe<Scalars['String']>;
  shipToName?: Maybe<Scalars['String']>;
  specialInstructions?: Maybe<Scalars['String']>;
  subTotal?: Maybe<Scalars['Float']>;
  tax?: Maybe<Scalars['Float']>;
  terms?: Maybe<Scalars['String']>;
  webStatus?: Maybe<Scalars['String']>;
};

export type OrderAddress = {
  __typename?: 'OrderAddress';
  city?: Maybe<Scalars['String']>;
  country?: Maybe<Scalars['String']>;
  postalCode?: Maybe<Scalars['String']>;
  state?: Maybe<Scalars['String']>;
  streetLineOne?: Maybe<Scalars['String']>;
  streetLineThree?: Maybe<Scalars['String']>;
  streetLineTwo?: Maybe<Scalars['String']>;
};

export type OrderLineItem = {
  __typename?: 'OrderLineItem';
  backOrderedQuantity?: Maybe<Scalars['Int']>;
  erpPartNumber?: Maybe<Scalars['String']>;
  imageUrls?: Maybe<ImageUrls>;
  lineComments?: Maybe<Scalars['String']>;
  lineNumber?: Maybe<Scalars['String']>;
  manufacturerName?: Maybe<Scalars['String']>;
  manufacturerNumber?: Maybe<Scalars['String']>;
  orderQuantity?: Maybe<Scalars['Int']>;
  pricingUom?: Maybe<Scalars['String']>;
  productId?: Maybe<Scalars['ID']>;
  productName?: Maybe<Scalars['String']>;
  productOrderTotal?: Maybe<Scalars['Float']>;
  shipQuantity?: Maybe<Scalars['Int']>;
  status?: Maybe<Scalars['String']>;
  unitPrice?: Maybe<Scalars['Float']>;
  uom?: Maybe<Scalars['String']>;
};

export type OrderLineItemResponseDto = {
  orderQuantity?: InputMaybe<Scalars['Int']>;
  productName?: InputMaybe<Scalars['String']>;
  unitPrice?: InputMaybe<Scalars['Float']>;
};

export type OrderPendingApproval = {
  __typename?: 'OrderPendingApproval';
  cartId?: Maybe<Scalars['String']>;
  orderId?: Maybe<Scalars['String']>;
  orderTotal?: Maybe<Scalars['Int']>;
  purchaseOrderNumber?: Maybe<Scalars['String']>;
  submissionDate?: Maybe<Scalars['String']>;
  submittedByName?: Maybe<Scalars['String']>;
};

export type OrderPreviewResponse = {
  __typename?: 'OrderPreviewResponse';
  orderTotal: Scalars['String'];
  subTotal: Scalars['String'];
  tax: Scalars['String'];
};

export type OrderResponse = {
  __typename?: 'OrderResponse';
  amount?: Maybe<Scalars['String']>;
  billToName?: Maybe<Scalars['String']>;
  branchInfo?: Maybe<BranchOrderInfo>;
  creditCard?: Maybe<CreditCard>;
  customerPO?: Maybe<Scalars['String']>;
  deliveryMethod?: Maybe<Scalars['String']>;
  email?: Maybe<Scalars['String']>;
  invoiceNumber?: Maybe<Scalars['String']>;
  lineItems: Array<OrderLineItem>;
  orderDate?: Maybe<Scalars['String']>;
  orderNumber?: Maybe<Scalars['String']>;
  orderStatus?: Maybe<Scalars['String']>;
  orderTotal?: Maybe<Scalars['Float']>;
  orderedBy?: Maybe<Scalars['String']>;
  shipAddress?: Maybe<EclipseAddress>;
  shipDate?: Maybe<Scalars['String']>;
  shipToName?: Maybe<Scalars['String']>;
  specialInstructions?: Maybe<Scalars['String']>;
  subTotal?: Maybe<Scalars['Float']>;
  tax?: Maybe<Scalars['Float']>;
  webStatus?: Maybe<Scalars['String']>;
};

export type OrderReviewInput = {
  addItemsToCart?: InputMaybe<ContractAddItemToCartInput>;
  createCartRequest?: InputMaybe<CreateCartRequestInput>;
};

export type OrdersResponse = {
  __typename?: 'OrdersResponse';
  orders?: Maybe<Array<Order>>;
  pagination?: Maybe<Pagination>;
};

export type Owner = {
  city?: InputMaybe<Scalars['String']>;
  ownerName?: InputMaybe<Scalars['String']>;
  phoneNumber?: InputMaybe<Scalars['String']>;
  postalCode?: InputMaybe<Scalars['String']>;
  state?: InputMaybe<Scalars['String']>;
  streetLineOne?: InputMaybe<Scalars['String']>;
  streetLineThree?: InputMaybe<Scalars['String']>;
  streetLineTwo?: InputMaybe<Scalars['String']>;
};

export type PackageDimensions = {
  __typename?: 'PackageDimensions';
  height?: Maybe<Scalars['Float']>;
  length?: Maybe<Scalars['Float']>;
  volume?: Maybe<Scalars['Float']>;
  volumeUnitOfMeasure?: Maybe<Scalars['String']>;
  weight?: Maybe<Scalars['Float']>;
  weightUnitOfMeasure?: Maybe<Scalars['String']>;
  width?: Maybe<Scalars['Float']>;
};

export type Page = {
  __typename?: 'Page';
  results: Array<Contract>;
  rowsReturned: Scalars['Int'];
  startRow: Scalars['Int'];
  totalRows: Scalars['Int'];
};

export type Pagination = {
  __typename?: 'Pagination';
  currentPage?: Maybe<Scalars['Int']>;
  pageSize?: Maybe<Scalars['Int']>;
  totalItemCount?: Maybe<Scalars['Int']>;
};

export enum PaymentMethodTypeEnum {
  Billtoaccount = 'BILLTOACCOUNT',
  Creditcard = 'CREDITCARD',
  Payinstore = 'PAYINSTORE'
}

export enum PhoneType {
  Home = 'HOME',
  Mobile = 'MOBILE',
  Office = 'OFFICE'
}

export enum PreferredTimeEnum {
  Afternoon = 'AFTERNOON',
  Asap = 'ASAP',
  Midday = 'MIDDAY',
  Morning = 'MORNING'
}

export type PreviouslyPurchasedProduct = {
  __typename?: 'PreviouslyPurchasedProduct';
  lastOrder?: Maybe<LastOrder>;
  product?: Maybe<Product>;
  quantity?: Maybe<Quantity>;
};

export type PreviouslyPurchasedProductResponse = {
  __typename?: 'PreviouslyPurchasedProductResponse';
  pagination?: Maybe<Pagination>;
  products?: Maybe<Array<PreviouslyPurchasedProduct>>;
};

export type PricingAndAvailability = {
  __typename?: 'PricingAndAvailability';
  description: Scalars['String'];
  listIds?: Maybe<Array<Scalars['String']>>;
  partNumber: Scalars['String'];
  price: Scalars['Float'];
  status: Scalars['String'];
  stock: Stock;
  uom: Scalars['String'];
};

export type Product = {
  __typename?: 'Product';
  categories?: Maybe<Array<Scalars['String']>>;
  cmp?: Maybe<Scalars['Float']>;
  customerPartNumber?: Maybe<Array<Scalars['String']>>;
  customerPartNumbers?: Maybe<Array<Maybe<CustomerPartNumber>>>;
  environmentalOptions?: Maybe<Array<Scalars['String']>>;
  erp?: Maybe<Scalars['String']>;
  featuresAndBenefits?: Maybe<Scalars['String']>;
  id: Scalars['ID'];
  imageUrls?: Maybe<ImageUrls>;
  manufacturerName?: Maybe<Scalars['String']>;
  manufacturerNumber?: Maybe<Scalars['String']>;
  minIncrementQty?: Maybe<Scalars['Int']>;
  name?: Maybe<Scalars['String']>;
  packageDimensions?: Maybe<PackageDimensions>;
  partNumber?: Maybe<Scalars['String']>;
  price?: Maybe<Scalars['Float']>;
  productOverview?: Maybe<Scalars['String']>;
  productType?: Maybe<Scalars['String']>;
  seriesModelFigureNumber?: Maybe<Scalars['String']>;
  status?: Maybe<Scalars['String']>;
  stock?: Maybe<Stock>;
  taxonomy?: Maybe<Array<Scalars['String']>>;
  techSpecifications?: Maybe<Array<TechSpec>>;
  technicalDocuments?: Maybe<Array<TechDoc>>;
  unspsc?: Maybe<Scalars['String']>;
  upc?: Maybe<Scalars['String']>;
};

export type ProductAttribute = {
  attributeType?: InputMaybe<Scalars['String']>;
  attributeValue?: InputMaybe<Scalars['String']>;
};

export type ProductAttributeOutput = {
  __typename?: 'ProductAttributeOutput';
  attributeType?: Maybe<Scalars['String']>;
  attributeValue?: Maybe<Scalars['String']>;
};

export type ProductCategories = {
  __typename?: 'ProductCategories';
  categories?: Maybe<Array<Category>>;
};

export type ProductInput = {
  customerNumber?: InputMaybe<Scalars['String']>;
  productId: Scalars['String'];
};

export type ProductInventory = {
  __typename?: 'ProductInventory';
  availableOnHand: Scalars['Int'];
  branchId: Scalars['String'];
  branchName: Scalars['String'];
};

export type ProductInventoryResponse = {
  __typename?: 'ProductInventoryResponse';
  inventory: Array<ProductInventory>;
  productDescription: Scalars['String'];
  productId: Scalars['String'];
};

export type ProductPricing = {
  __typename?: 'ProductPricing';
  branchAvailableQty: Scalars['Int'];
  catalogId: Scalars['String'];
  listIds?: Maybe<Array<Scalars['String']>>;
  orderUom: Scalars['String'];
  productId: Scalars['String'];
  sellPrice: Scalars['Float'];
  totalAvailableQty: Scalars['Int'];
};

export type ProductPricingInput = {
  branchId: Scalars['String'];
  customerId: Scalars['String'];
  includeListData: Scalars['Boolean'];
  productIds: Array<Scalars['String']>;
};

export type ProductPricingResponse = {
  __typename?: 'ProductPricingResponse';
  branch: Scalars['String'];
  customerId: Scalars['String'];
  products: Array<ProductPricing>;
};

export type ProductResponse = {
  __typename?: 'ProductResponse';
  id: Scalars['String'];
  name?: Maybe<Scalars['String']>;
};

export type ProductSearchInput = {
  billToId?: InputMaybe<Scalars['String']>;
  categories?: InputMaybe<Array<Scalars['String']>>;
  categoryLevel?: InputMaybe<Scalars['Int']>;
  customerNumber?: InputMaybe<Scalars['String']>;
  engine: Scalars['String'];
  filters?: InputMaybe<Array<Scalars['String']>>;
  page: Scalars['Int'];
  searchTerm: Scalars['String'];
  size: Scalars['Int'];
  state?: InputMaybe<Scalars['String']>;
};

export type ProductSearchResult = {
  __typename?: 'ProductSearchResult';
  aggregates?: Maybe<AggregationResults>;
  categoryLevel: Scalars['Int'];
  pagination?: Maybe<Pagination>;
  products?: Maybe<Array<Product>>;
  selectedAttributes?: Maybe<Array<ProductAttributeOutput>>;
  selectedCategories?: Maybe<Array<ProductAttributeOutput>>;
};

export type Project = {
  city: Scalars['String'];
  estimatedProjectAmount: Scalars['Float'];
  jobName: Scalars['String'];
  lotNoAndTrack?: InputMaybe<Scalars['String']>;
  postalCode: Scalars['String'];
  state: Scalars['String'];
  streetLineOne: Scalars['String'];
  streetLineThree?: InputMaybe<Scalars['String']>;
  streetLineTwo?: InputMaybe<Scalars['String']>;
  taxable: Scalars['Boolean'];
};

export type Quantity = {
  __typename?: 'Quantity';
  quantity?: Maybe<Scalars['String']>;
  umqt?: Maybe<Scalars['String']>;
  uom?: Maybe<Scalars['String']>;
};

export type Query = {
  __typename?: 'Query';
  account: Array<ErpAccount>;
  accountUsers?: Maybe<Array<ApprovedUser>>;
  accounts?: Maybe<User>;
  allProducts: Array<ProductResponse>;
  allUnapprovedAccountRequests: Array<User>;
  approvers: Array<Approver>;
  branch: Branch;
  branchSearch: BranchSearchResult;
  branches: Array<Branch>;
  cart: Cart;
  cartFromQuote: Cart;
  cartUserIdAccountId: Cart;
  categories: Categories;
  contactInfo?: Maybe<ContactInfo>;
  contract: ContractDetails;
  contracts: Page;
  creditCardElementInfo: CreditCardElementInfoResponse;
  creditCardList?: Maybe<CreditCardListResponse>;
  creditCardSetupUrl: CreditCardSetupUrl;
  entitySearch?: Maybe<AccountLookUp>;
  erpUserInformation?: Maybe<ErpUserInformation>;
  exportListToCSV: ExportListResponse;
  features?: Maybe<Array<Feature>>;
  homeBranch: Branch;
  invitedUserEmailSent: Scalars['Boolean'];
  invoice: Invoice;
  invoices: AccountInvoice;
  invoicesUrl: Scalars['String'];
  list: List;
  lists: Array<List>;
  order: Order;
  orderPendingApproval: OrderPendingApproval;
  orders: OrdersResponse;
  ordersPendingApproval: Array<OrderPendingApproval>;
  phoneTypes: Array<PhoneType>;
  previouslyPurchasedProducts: PreviouslyPurchasedProductResponse;
  /** @deprecated Use productPricing instead */
  pricingAndAvailability: Array<PricingAndAvailability>;
  product?: Maybe<Product>;
  productCategories?: Maybe<ProductCategories>;
  productDetails: ItemAvailability;
  productInventory: ProductInventoryResponse;
  productPricing: ProductPricingResponse;
  quote: Quote;
  quotes: Array<Quote>;
  refreshShipToAccount: Array<ShipToAccount>;
  rejectedAccountRequests?: Maybe<Array<User>>;
  rejectionReasons?: Maybe<Array<RejectionReason>>;
  roles: Array<Role>;
  searchProduct?: Maybe<ProductSearchResult>;
  searchSuggestion: SearchSuggestionResult;
  services?: Maybe<Array<Service>>;
  submitContractOrderFromCart: Scalars['String'];
  submitContractOrderReview: ContractOrderDetail;
  unapprovedAccountRequests?: Maybe<Array<User>>;
  user?: Maybe<ApprovedUser>;
  userAccounts?: Maybe<Array<EcommAccount>>;
  userInvite: InvitedUser;
  verifyAccount: VerifyAccountResponse;
  verifyAccountNew: VerifyAccountResponse;
  verifyUserEmail: EmailValidationResponse;
  willCallBranches: Array<Branch>;
};


export type QueryAccountArgs = {
  accountId: Scalars['String'];
  brand: Scalars['String'];
};


export type QueryAccountUsersArgs = {
  accountId?: InputMaybe<Scalars['String']>;
};


export type QueryApproversArgs = {
  billToAccountId: Scalars['ID'];
};


export type QueryBranchArgs = {
  branchId: Scalars['ID'];
};


export type QueryBranchSearchArgs = {
  branchSearch: BranchSearch;
};


export type QueryCartArgs = {
  id: Scalars['ID'];
  includeProducts?: InputMaybe<Scalars['Boolean']>;
  shipToAccountId: Scalars['ID'];
  userId: Scalars['ID'];
};


export type QueryCartFromQuoteArgs = {
  branchId: Scalars['ID'];
  quoteId: Scalars['ID'];
  shipToAccountId: Scalars['ID'];
  userId: Scalars['ID'];
};


export type QueryCartUserIdAccountIdArgs = {
  accountId: Scalars['ID'];
  includeProducts?: InputMaybe<Scalars['Boolean']>;
  userId: Scalars['ID'];
};


export type QueryCategoriesArgs = {
  erp?: InputMaybe<Scalars['String']>;
};


export type QueryContactInfoArgs = {
  userId?: InputMaybe<Scalars['String']>;
};


export type QueryContractArgs = {
  contractNumber: Scalars['String'];
  erpAccountId: Scalars['String'];
};


export type QueryContractsArgs = {
  erpAccountId: Scalars['String'];
  fromDate?: InputMaybe<Scalars['String']>;
  pageNumber: Scalars['String'];
  searchFilter?: InputMaybe<Scalars['String']>;
  sortDirection?: InputMaybe<Scalars['String']>;
  sortOrder?: InputMaybe<Scalars['String']>;
  toDate?: InputMaybe<Scalars['String']>;
};


export type QueryCreditCardElementInfoArgs = {
  accountId: Scalars['String'];
  elementSetupId: Scalars['String'];
};


export type QueryCreditCardListArgs = {
  accountId: Scalars['String'];
};


export type QueryCreditCardSetupUrlArgs = {
  accountId: Scalars['String'];
  cardHolderInput: CardHolderInput;
};


export type QueryEntitySearchArgs = {
  accountId?: InputMaybe<Scalars['String']>;
};


export type QueryErpUserInformationArgs = {
  shipToAccountId?: InputMaybe<Scalars['String']>;
  userId?: InputMaybe<Scalars['String']>;
};


export type QueryExportListToCsvArgs = {
  branchId: Scalars['ID'];
  listId: Scalars['ID'];
  shipToAccountId: Scalars['ID'];
  userId: Scalars['ID'];
};


export type QueryHomeBranchArgs = {
  shipToAccountId: Scalars['String'];
};


export type QueryInvitedUserEmailSentArgs = {
  email?: InputMaybe<Scalars['String']>;
};


export type QueryInvoiceArgs = {
  accountId: Scalars['String'];
  invoiceNumber: Scalars['String'];
};


export type QueryInvoicesArgs = {
  accountId: Scalars['String'];
  endDate?: InputMaybe<Scalars['Date']>;
  erpName: Scalars['String'];
  invoiceStatus?: InputMaybe<Scalars['String']>;
  shipTo?: InputMaybe<Scalars['String']>;
  startDate?: InputMaybe<Scalars['Date']>;
};


export type QueryInvoicesUrlArgs = {
  accountId: Scalars['String'];
  invoiceNumbers: Array<Scalars['String']>;
};


export type QueryListArgs = {
  branchId: Scalars['ID'];
  erpAccountId?: InputMaybe<Scalars['ID']>;
  listId: Scalars['ID'];
  shipToAccountId: Scalars['ID'];
  userId: Scalars['ID'];
};


export type QueryListsArgs = {
  billToAccountId: Scalars['ID'];
};


export type QueryOrderArgs = {
  accountId: Scalars['ID'];
  invoiceNumber?: InputMaybe<Scalars['ID']>;
  orderId: Scalars['ID'];
  orderStatus?: InputMaybe<Scalars['String']>;
  userId: Scalars['ID'];
};


export type QueryOrderPendingApprovalArgs = {
  orderId: Scalars['String'];
};


export type QueryOrdersArgs = {
  accountId: Scalars['ID'];
  endDate?: InputMaybe<Scalars['String']>;
  erpName: Scalars['String'];
  startDate?: InputMaybe<Scalars['String']>;
};


export type QueryPreviouslyPurchasedProductsArgs = {
  currentPage?: InputMaybe<Scalars['Int']>;
  customerNumber?: InputMaybe<Scalars['String']>;
  ecommShipToId: Scalars['ID'];
  pageSize: Scalars['Int'];
  userId: Scalars['ID'];
};


export type QueryPricingAndAvailabilityArgs = {
  partNumbers: Array<Scalars['String']>;
  shipToAccountId: Scalars['ID'];
  userId: Scalars['ID'];
};


export type QueryProductArgs = {
  productInput: ProductInput;
};


export type QueryProductCategoriesArgs = {
  engine: Scalars['String'];
};


export type QueryProductDetailsArgs = {
  itemDetails?: InputMaybe<ItemDetailsInput>;
};


export type QueryProductInventoryArgs = {
  productId: Scalars['String'];
};


export type QueryProductPricingArgs = {
  input: ProductPricingInput;
};


export type QueryQuoteArgs = {
  accountId: Scalars['ID'];
  quoteId: Scalars['ID'];
  userId: Scalars['ID'];
};


export type QueryQuotesArgs = {
  accountId: Scalars['ID'];
  endDate?: InputMaybe<Scalars['String']>;
  startDate?: InputMaybe<Scalars['String']>;
};


export type QueryRefreshShipToAccountArgs = {
  billToAccountId: Scalars['String'];
};


export type QueryRejectedAccountRequestsArgs = {
  accountId: Scalars['String'];
};


export type QuerySearchProductArgs = {
  productSearch?: InputMaybe<ProductSearchInput>;
  shipToAccountId?: InputMaybe<Scalars['ID']>;
  userId?: InputMaybe<Scalars['ID']>;
};


export type QuerySearchSuggestionArgs = {
  engine: Scalars['String'];
  erpSystem?: InputMaybe<Scalars['String']>;
  shipToAccountId?: InputMaybe<Scalars['ID']>;
  size?: InputMaybe<Scalars['Int']>;
  state?: InputMaybe<Scalars['String']>;
  term: Scalars['String'];
  userId?: InputMaybe<Scalars['ID']>;
};


export type QuerySubmitContractOrderFromCartArgs = {
  accountId: Scalars['String'];
  application: Scalars['String'];
  contractOrderSubmit?: InputMaybe<ContractOrderSubmitInput>;
  shoppingCartId: Scalars['String'];
  userId: Scalars['String'];
};


export type QuerySubmitContractOrderReviewArgs = {
  orderReview?: InputMaybe<OrderReviewInput>;
};


export type QueryUnapprovedAccountRequestsArgs = {
  accountId?: InputMaybe<Scalars['String']>;
};


export type QueryUserArgs = {
  userId?: InputMaybe<Scalars['String']>;
};


export type QueryUserAccountsArgs = {
  userId?: InputMaybe<Scalars['String']>;
};


export type QueryUserInviteArgs = {
  inviteId: Scalars['ID'];
};


export type QueryVerifyAccountArgs = {
  input: VerifyAccountInput;
};


export type QueryVerifyAccountNewArgs = {
  input: VerifyAccountInput;
};


export type QueryVerifyUserEmailArgs = {
  email: Scalars['String'];
};


export type QueryWillCallBranchesArgs = {
  shipToAccountId: Scalars['String'];
};

export type Quote = {
  __typename?: 'Quote';
  amount?: Maybe<Scalars['String']>;
  bidExpireDate: Scalars['String'];
  billToName: Scalars['String'];
  branchInfo?: Maybe<BranchOrderInfo>;
  customerPO?: Maybe<Scalars['String']>;
  deliveryMethod?: Maybe<Scalars['String']>;
  email?: Maybe<Scalars['String']>;
  invoiceDueDate?: Maybe<Scalars['String']>;
  invoiceNumber?: Maybe<Scalars['String']>;
  lineItems?: Maybe<Array<QuoteLineItem>>;
  orderDate?: Maybe<Scalars['String']>;
  orderNumber: Scalars['String'];
  orderStatus: Scalars['String'];
  orderTotal: Scalars['Float'];
  orderedBy?: Maybe<Scalars['String']>;
  requiredDate: Scalars['String'];
  shipAddress: QuoteAddress;
  shipDate?: Maybe<Scalars['String']>;
  shipToId?: Maybe<Scalars['String']>;
  shipToName?: Maybe<Scalars['String']>;
  specialInstructions?: Maybe<Scalars['String']>;
  subTotal: Scalars['Float'];
  tax: Scalars['Float'];
  webStatus: Scalars['String'];
};

export type QuoteAddress = {
  __typename?: 'QuoteAddress';
  city?: Maybe<Scalars['String']>;
  country?: Maybe<Scalars['String']>;
  postalCode?: Maybe<Scalars['String']>;
  state?: Maybe<Scalars['String']>;
  streetLineOne?: Maybe<Scalars['String']>;
  streetLineThree?: Maybe<Scalars['String']>;
  streetLineTwo?: Maybe<Scalars['String']>;
};

export type QuoteLineItem = {
  __typename?: 'QuoteLineItem';
  erpPartNumber?: Maybe<Scalars['String']>;
  imageUrls?: Maybe<ImageUrls>;
  manufacturerName?: Maybe<Scalars['String']>;
  manufacturerNumber?: Maybe<Scalars['String']>;
  orderQuantity?: Maybe<Scalars['Int']>;
  productId?: Maybe<Scalars['ID']>;
  productName?: Maybe<Scalars['String']>;
  productOrderTotal?: Maybe<Scalars['Float']>;
  shipQuantity?: Maybe<Scalars['Int']>;
  status?: Maybe<Scalars['String']>;
  unitPrice?: Maybe<Scalars['Float']>;
};

export type RejectOrderInput = {
  rejectionReason?: InputMaybe<Scalars['String']>;
};

export type RejectUserInput = {
  accountRequestId?: InputMaybe<Scalars['ID']>;
  customRejectionReason?: InputMaybe<Scalars['String']>;
  rejectionReasonType?: InputMaybe<Scalars['String']>;
};

export enum RejectionReason {
  NotAuthorized = 'NOT_AUTHORIZED',
  NotACompanyMember = 'NOT_A_COMPANY_MEMBER',
  Other = 'OTHER'
}

export type Role = {
  __typename?: 'Role';
  description?: Maybe<Scalars['String']>;
  id?: Maybe<Scalars['String']>;
  name?: Maybe<Scalars['String']>;
};

export type SearchSuggestionResult = {
  __typename?: 'SearchSuggestionResult';
  suggestions: Array<Scalars['String']>;
  topCategories: Array<AggregationItem>;
  topProducts: Array<Product>;
};

export enum ServerStatus {
  Down = 'DOWN',
  Up = 'UP'
}

export type Service = {
  __typename?: 'Service';
  name?: Maybe<Scalars['String']>;
  status?: Maybe<ServerStatus>;
};

export type SetFeatureEnabledInput = {
  isEnabled: Scalars['Boolean'];
};

export type ShipToAccount = {
  __typename?: 'ShipToAccount';
  id?: Maybe<Scalars['String']>;
  name?: Maybe<Scalars['String']>;
};

export type StatusResult = {
  __typename?: 'StatusResult';
  description: Scalars['String'];
  success: Scalars['String'];
};

export type Stock = {
  __typename?: 'Stock';
  homeBranch?: Maybe<StoreStock>;
  otherBranches?: Maybe<Array<StoreStock>>;
};

export type StoreStock = {
  __typename?: 'StoreStock';
  address1?: Maybe<Scalars['String']>;
  address2?: Maybe<Scalars['String']>;
  availability?: Maybe<Scalars['Int']>;
  branchId?: Maybe<Scalars['String']>;
  branchName?: Maybe<Scalars['String']>;
  city?: Maybe<Scalars['String']>;
  distanceToBranch?: Maybe<Scalars['String']>;
  phone?: Maybe<Scalars['String']>;
  state?: Maybe<Scalars['String']>;
  zip?: Maybe<Scalars['String']>;
};

export type TechDoc = {
  __typename?: 'TechDoc';
  name?: Maybe<Scalars['String']>;
  url?: Maybe<Scalars['String']>;
};

export type TechSpec = {
  __typename?: 'TechSpec';
  name?: Maybe<Scalars['String']>;
  value?: Maybe<Scalars['String']>;
};

export type ToggleItemsInListInput = {
  billToAccountId?: InputMaybe<Scalars['ID']>;
  itemToToggle?: InputMaybe<ListItemToToggle>;
  listIds: Array<Scalars['ID']>;
};

export type ToggleItemsResponse = {
  __typename?: 'ToggleItemsResponse';
  lists: Array<List>;
};

export type UpdateBranchInput = {
  id: Scalars['ID'];
  isActive: Scalars['Boolean'];
  isAvailableInStoreFinder: Scalars['Boolean'];
  isPricingOnly: Scalars['Boolean'];
  isShoppable: Scalars['Boolean'];
};

export type UpdateItemQtyResponse = {
  __typename?: 'UpdateItemQtyResponse';
  product: LineItem;
  subtotal?: Maybe<Scalars['Int']>;
};

export type UpdateListInput = {
  billToAccountId: Scalars['ID'];
  id: Scalars['ID'];
  listLineItems?: InputMaybe<Array<UpdateListLineItemInput>>;
  name: Scalars['String'];
};

export type UpdateListLineItemInput = {
  erpPartNumber: Scalars['String'];
  id: Scalars['ID'];
  listId: Scalars['ID'];
  quantity?: InputMaybe<Scalars['Int']>;
  sortOrder?: InputMaybe<Scalars['Int']>;
};

export type UpdateUserInput = {
  accountId?: InputMaybe<Scalars['String']>;
  approverId?: InputMaybe<Scalars['ID']>;
  email?: InputMaybe<Scalars['String']>;
  firstName?: InputMaybe<Scalars['String']>;
  lastName?: InputMaybe<Scalars['String']>;
  phoneNumber?: InputMaybe<Scalars['String']>;
  phoneTypeId?: InputMaybe<Scalars['ID']>;
  roleId?: InputMaybe<Scalars['ID']>;
  userId?: InputMaybe<Scalars['String']>;
};

export type UpdateUserPasswordInput = {
  newUserPassword: Scalars['String'];
  oldUserPassword: Scalars['String'];
  userId: Scalars['ID'];
};

export type User = {
  __typename?: 'User';
  accountInfo?: Maybe<Account>;
  accountNumber?: Maybe<Scalars['String']>;
  approverId?: Maybe<Scalars['ID']>;
  branchId?: Maybe<Scalars['String']>;
  companyName?: Maybe<Scalars['String']>;
  createdAt?: Maybe<Scalars['String']>;
  customRejectionReason?: Maybe<Scalars['String']>;
  customerCategory?: Maybe<Scalars['String']>;
  email?: Maybe<Scalars['String']>;
  firstName?: Maybe<Scalars['String']>;
  id?: Maybe<Scalars['String']>;
  lastName?: Maybe<Scalars['String']>;
  password?: Maybe<Scalars['String']>;
  phoneNumber?: Maybe<Scalars['String']>;
  phoneType: PhoneType;
  ppAcceptedAt?: Maybe<Scalars['String']>;
  preferredLocationId?: Maybe<Scalars['String']>;
  rejectedAt?: Maybe<Scalars['String']>;
  rejectedBy?: Maybe<Scalars['String']>;
  rejectionReason?: Maybe<RejectionReason>;
  roleId?: Maybe<Scalars['ID']>;
  tosAcceptedAt?: Maybe<Scalars['String']>;
  userContactTitle?: Maybe<Scalars['String']>;
};

export type UserCartInfo = {
  erpSystemName?: InputMaybe<ErpSystemEnum>;
  shipToAccountId?: InputMaybe<Scalars['ID']>;
  shippingBranchId?: InputMaybe<Scalars['String']>;
  userId?: InputMaybe<Scalars['ID']>;
};

export type UserInfo = {
  __typename?: 'UserInfo';
  email: Scalars['String'];
  firstName: Scalars['String'];
  lastName: Scalars['String'];
};

export type UserInput = {
  accountInfo?: InputMaybe<AccountInput>;
  branchId?: InputMaybe<Scalars['String']>;
  customerCategory?: InputMaybe<Scalars['String']>;
  email?: InputMaybe<Scalars['String']>;
  firstName?: InputMaybe<Scalars['String']>;
  isEmployee?: InputMaybe<Scalars['Boolean']>;
  isWaterworksSubdomain?: InputMaybe<Scalars['Boolean']>;
  lastName?: InputMaybe<Scalars['String']>;
  password?: InputMaybe<Scalars['String']>;
  phoneNumber?: InputMaybe<Scalars['String']>;
  phoneTypeId?: InputMaybe<Scalars['ID']>;
  ppAccepted?: InputMaybe<Scalars['Boolean']>;
  preferredLocationId?: InputMaybe<Scalars['String']>;
  tosAccepted?: InputMaybe<Scalars['Boolean']>;
  userContactTitle?: InputMaybe<Scalars['String']>;
};

export type UserRequest = {
  __typename?: 'UserRequest';
  id?: Maybe<Scalars['String']>;
  isEmployee?: Maybe<Scalars['Boolean']>;
  isVerified?: Maybe<Scalars['Boolean']>;
};

export type UsersOfApprover = {
  __typename?: 'UsersOfApprover';
  users: Array<UserInfo>;
};

export type VerifyAccountInput = {
  accountNumber: Scalars['String'];
  brand: Scalars['String'];
  zipcode: Scalars['String'];
};

export type VerifyAccountResponse = {
  __typename?: 'VerifyAccountResponse';
  accountName: Scalars['String'];
  isTradeAccount: Scalars['Boolean'];
};

export type WillCall = {
  __typename?: 'WillCall';
  branchId?: Maybe<Scalars['String']>;
  id: Scalars['ID'];
  pickupInstructions?: Maybe<Scalars['String']>;
  preferredDate?: Maybe<Scalars['Date']>;
  preferredTime?: Maybe<PreferredTimeEnum>;
};

export type WillCallInput = {
  branchId?: InputMaybe<Scalars['String']>;
  cartId: Scalars['ID'];
  pickupInstructions?: InputMaybe<Scalars['String']>;
  preferredDate?: InputMaybe<Scalars['Date']>;
  preferredTime?: InputMaybe<PreferredTimeEnum>;
};

export type BranchDetailInput = {
  branchNumber?: InputMaybe<Scalars['String']>;
};

export type ShipmentDetailInput = {
  shipMethod?: InputMaybe<Scalars['String']>;
  shippingAddress1?: InputMaybe<Scalars['String']>;
  shippingAddress2?: InputMaybe<Scalars['String']>;
  shippingAddress3?: InputMaybe<Scalars['String']>;
  shippingCity?: InputMaybe<Scalars['String']>;
  shippingCountry?: InputMaybe<Scalars['String']>;
  shippingState?: InputMaybe<Scalars['String']>;
  shippingTaxJurisdiction?: InputMaybe<Scalars['String']>;
  shippingZip?: InputMaybe<Scalars['String']>;
};

export type ShippingAddressInput = {
  address1?: InputMaybe<Scalars['String']>;
  address2?: InputMaybe<Scalars['String']>;
  address3?: InputMaybe<Scalars['String']>;
  city?: InputMaybe<Scalars['String']>;
  country?: InputMaybe<Scalars['String']>;
  state?: InputMaybe<Scalars['String']>;
  taxJurisdiction?: InputMaybe<Scalars['String']>;
  zip?: InputMaybe<Scalars['String']>;
};

export type UserQueryVariables = Exact<{
  userId?: InputMaybe<Scalars['String']>;
}>;


export type UserQuery = { __typename?: 'Query', user?: { __typename?: 'ApprovedUser', id?: string | null | undefined, email?: string | null | undefined, firstName?: string | null | undefined, lastName?: string | null | undefined, phoneNumber?: string | null | undefined, phoneType: PhoneType, role?: { __typename?: 'Role', id?: string | null | undefined, name?: string | null | undefined, description?: string | null | undefined } | null | undefined } | null | undefined };

export type UserAccountsQueryVariables = Exact<{
  userId?: InputMaybe<Scalars['String']>;
}>;


export type UserAccountsQuery = { __typename?: 'Query', userAccounts?: Array<{ __typename?: 'EcommAccount', id?: string | null | undefined, name?: string | null | undefined, erpAccountId?: string | null | undefined, erpSystemName?: ErpSystemEnum | null | undefined, divisionEnum?: DivisionEnum | null | undefined, branchId?: string | null | undefined, branchAddress?: string | null | undefined, shipTos?: Array<{ __typename?: 'EcommAccount', id?: string | null | undefined, name?: string | null | undefined, erpAccountId?: string | null | undefined, erpSystemName?: ErpSystemEnum | null | undefined, divisionEnum?: DivisionEnum | null | undefined, branchId?: string | null | undefined, branchAddress?: string | null | undefined }> | null | undefined }> | null | undefined };

export type UpdateUserPasswordMutationVariables = Exact<{
  updateUserPasswordInput: UpdateUserPasswordInput;
}>;


export type UpdateUserPasswordMutation = { __typename?: 'Mutation', updateUserPassword: boolean };

export type BranchListItemFragment = { __typename?: 'Branch', branchId: string, name?: string | null | undefined, address1?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, isActive: boolean, isAvailableInStoreFinder: boolean, isShoppable: boolean, isPricingOnly: boolean, isPlumbing: boolean, isWaterworks: boolean, isHvac: boolean, isBandK: boolean, id: string };

export type GetBranchesListQueryVariables = Exact<{ [key: string]: never; }>;


export type GetBranchesListQuery = { __typename?: 'Query', branches: Array<{ __typename?: 'Branch', branchId: string, name?: string | null | undefined, address1?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, isActive: boolean, isAvailableInStoreFinder: boolean, isShoppable: boolean, isPricingOnly: boolean, isPlumbing: boolean, isWaterworks: boolean, isHvac: boolean, isBandK: boolean, id: string }> };

export type UpdateBranchMutationVariables = Exact<{
  input: UpdateBranchInput;
}>;


export type UpdateBranchMutation = { __typename?: 'Mutation', updateBranch: { __typename?: 'Branch', branchId: string, name?: string | null | undefined, address1?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, isActive: boolean, isAvailableInStoreFinder: boolean, isShoppable: boolean, isPricingOnly: boolean, isPlumbing: boolean, isWaterworks: boolean, isHvac: boolean, isBandK: boolean, id: string } };

export type CartQueryVariables = Exact<{
  id: Scalars['ID'];
  userId: Scalars['ID'];
  shipToAccountId: Scalars['ID'];
  includeProducts?: InputMaybe<Scalars['Boolean']>;
}>;


export type CartQuery = { __typename?: 'Query', cart: { __typename?: 'Cart', id: string, ownerId?: string | null | undefined, approverId?: string | null | undefined, shipToId?: string | null | undefined, poNumber?: string | null | undefined, pricingBranchId?: string | null | undefined, shippingBranchId?: string | null | undefined, paymentMethodType?: PaymentMethodTypeEnum | null | undefined, approvalState?: string | null | undefined, rejectionReason?: string | null | undefined, removedProducts?: Array<string> | null | undefined, subtotal?: number | null | undefined, tax?: number | null | undefined, shippingHandling?: number | null | undefined, total?: number | null | undefined, deliveryMethod?: DeliveryMethodEnum | null | undefined, erpSystemName: ErpSystemEnum, creditCard?: { __typename?: 'CreditCard', creditCardType: string, creditCardNumber: string, cardHolder: string, streetAddress: string, postalCode: string, elementPaymentAccountId: string, expirationDate: { __typename?: 'DateWrapper', date: string } } | null | undefined, delivery: { __typename?: 'Delivery', id: string, shipToId?: string | null | undefined, deliveryInstructions?: string | null | undefined, preferredDate?: any | null | undefined, preferredTime?: PreferredTimeEnum | null | undefined, shouldShipFullOrder: boolean, phoneNumber?: string | null | undefined, address?: { __typename?: 'Address', id: string, companyName?: string | null | undefined, street1?: string | null | undefined, street2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, country?: string | null | undefined, custom: boolean } | null | undefined }, willCall: { __typename?: 'WillCall', id: string, preferredDate?: any | null | undefined, preferredTime?: PreferredTimeEnum | null | undefined, branchId?: string | null | undefined, pickupInstructions?: string | null | undefined }, products?: Array<{ __typename?: 'LineItem', id?: string | null | undefined, cartId?: string | null | undefined, customerPartNumber?: string | null | undefined, pricePerUnit?: number | null | undefined, uom?: string | null | undefined, priceLastUpdatedAt?: any | null | undefined, erpPartNumber?: string | null | undefined, quantity?: number | null | undefined, qtyAvailableLastUpdatedAt?: any | null | undefined, qtyAvailable?: number | null | undefined, listIds?: Array<string> | null | undefined, product?: { __typename?: 'Product', id: string, partNumber?: string | null | undefined, name?: string | null | undefined, manufacturerName?: string | null | undefined, manufacturerNumber?: string | null | undefined, price?: number | null | undefined, cmp?: number | null | undefined, taxonomy?: Array<string> | null | undefined, categories?: Array<string> | null | undefined, environmentalOptions?: Array<string> | null | undefined, upc?: string | null | undefined, unspsc?: string | null | undefined, seriesModelFigureNumber?: string | null | undefined, productOverview?: string | null | undefined, featuresAndBenefits?: string | null | undefined, minIncrementQty?: number | null | undefined, erp?: string | null | undefined, customerPartNumber?: Array<string> | null | undefined, stock?: { __typename?: 'Stock', homeBranch?: { __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined } | null | undefined, otherBranches?: Array<{ __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined }> | null | undefined } | null | undefined, technicalDocuments?: Array<{ __typename?: 'TechDoc', name?: string | null | undefined, url?: string | null | undefined }> | null | undefined, techSpecifications?: Array<{ __typename?: 'TechSpec', name?: string | null | undefined, value?: string | null | undefined }> | null | undefined, imageUrls?: { __typename?: 'ImageUrls', thumb?: string | null | undefined, small?: string | null | undefined, medium?: string | null | undefined, large?: string | null | undefined } | null | undefined, packageDimensions?: { __typename?: 'PackageDimensions', height?: number | null | undefined, weight?: number | null | undefined, length?: number | null | undefined, volume?: number | null | undefined, width?: number | null | undefined, volumeUnitOfMeasure?: string | null | undefined, weightUnitOfMeasure?: string | null | undefined } | null | undefined } | null | undefined }> | null | undefined } };

export type CartFromQuoteQueryVariables = Exact<{
  userId: Scalars['ID'];
  shipToAccountId: Scalars['ID'];
  quoteId: Scalars['ID'];
  branchId: Scalars['ID'];
}>;


export type CartFromQuoteQuery = { __typename?: 'Query', cartFromQuote: { __typename?: 'Cart', id: string, ownerId?: string | null | undefined, approverId?: string | null | undefined, shipToId?: string | null | undefined, poNumber?: string | null | undefined, pricingBranchId?: string | null | undefined, shippingBranchId?: string | null | undefined, paymentMethodType?: PaymentMethodTypeEnum | null | undefined, approvalState?: string | null | undefined, rejectionReason?: string | null | undefined, removedProducts?: Array<string> | null | undefined, subtotal?: number | null | undefined, tax?: number | null | undefined, shippingHandling?: number | null | undefined, total?: number | null | undefined, deliveryMethod?: DeliveryMethodEnum | null | undefined, erpSystemName: ErpSystemEnum, creditCard?: { __typename?: 'CreditCard', creditCardType: string, creditCardNumber: string, cardHolder: string, streetAddress: string, postalCode: string, elementPaymentAccountId: string, expirationDate: { __typename?: 'DateWrapper', date: string } } | null | undefined, delivery: { __typename?: 'Delivery', id: string, shipToId?: string | null | undefined, deliveryInstructions?: string | null | undefined, preferredDate?: any | null | undefined, preferredTime?: PreferredTimeEnum | null | undefined, shouldShipFullOrder: boolean, phoneNumber?: string | null | undefined, address?: { __typename?: 'Address', id: string, companyName?: string | null | undefined, street1?: string | null | undefined, street2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, country?: string | null | undefined, custom: boolean } | null | undefined }, willCall: { __typename?: 'WillCall', id: string, preferredDate?: any | null | undefined, preferredTime?: PreferredTimeEnum | null | undefined, branchId?: string | null | undefined, pickupInstructions?: string | null | undefined }, products?: Array<{ __typename?: 'LineItem', id?: string | null | undefined, cartId?: string | null | undefined, customerPartNumber?: string | null | undefined, pricePerUnit?: number | null | undefined, uom?: string | null | undefined, priceLastUpdatedAt?: any | null | undefined, erpPartNumber?: string | null | undefined, quantity?: number | null | undefined, qtyAvailableLastUpdatedAt?: any | null | undefined, qtyAvailable?: number | null | undefined, listIds?: Array<string> | null | undefined, product?: { __typename?: 'Product', id: string, partNumber?: string | null | undefined, name?: string | null | undefined, manufacturerName?: string | null | undefined, manufacturerNumber?: string | null | undefined, price?: number | null | undefined, cmp?: number | null | undefined, taxonomy?: Array<string> | null | undefined, categories?: Array<string> | null | undefined, environmentalOptions?: Array<string> | null | undefined, upc?: string | null | undefined, unspsc?: string | null | undefined, seriesModelFigureNumber?: string | null | undefined, productOverview?: string | null | undefined, featuresAndBenefits?: string | null | undefined, minIncrementQty?: number | null | undefined, erp?: string | null | undefined, customerPartNumber?: Array<string> | null | undefined, stock?: { __typename?: 'Stock', homeBranch?: { __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined } | null | undefined, otherBranches?: Array<{ __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined }> | null | undefined } | null | undefined, technicalDocuments?: Array<{ __typename?: 'TechDoc', name?: string | null | undefined, url?: string | null | undefined }> | null | undefined, techSpecifications?: Array<{ __typename?: 'TechSpec', name?: string | null | undefined, value?: string | null | undefined }> | null | undefined, imageUrls?: { __typename?: 'ImageUrls', thumb?: string | null | undefined, small?: string | null | undefined, medium?: string | null | undefined, large?: string | null | undefined } | null | undefined, packageDimensions?: { __typename?: 'PackageDimensions', height?: number | null | undefined, weight?: number | null | undefined, length?: number | null | undefined, volume?: number | null | undefined, width?: number | null | undefined, volumeUnitOfMeasure?: string | null | undefined, weightUnitOfMeasure?: string | null | undefined } | null | undefined } | null | undefined }> | null | undefined } };

export type CartUserIdAccountIdQueryVariables = Exact<{
  userId: Scalars['ID'];
  accountId: Scalars['ID'];
  includeProducts?: InputMaybe<Scalars['Boolean']>;
}>;


export type CartUserIdAccountIdQuery = { __typename?: 'Query', cartUserIdAccountId: { __typename?: 'Cart', id: string, ownerId?: string | null | undefined, approverId?: string | null | undefined, shipToId?: string | null | undefined, poNumber?: string | null | undefined, pricingBranchId?: string | null | undefined, shippingBranchId?: string | null | undefined, paymentMethodType?: PaymentMethodTypeEnum | null | undefined, approvalState?: string | null | undefined, rejectionReason?: string | null | undefined, removedProducts?: Array<string> | null | undefined, subtotal?: number | null | undefined, tax?: number | null | undefined, shippingHandling?: number | null | undefined, total?: number | null | undefined, deliveryMethod?: DeliveryMethodEnum | null | undefined, erpSystemName: ErpSystemEnum, creditCard?: { __typename?: 'CreditCard', creditCardType: string, creditCardNumber: string, cardHolder: string, streetAddress: string, postalCode: string, elementPaymentAccountId: string, expirationDate: { __typename?: 'DateWrapper', date: string } } | null | undefined, delivery: { __typename?: 'Delivery', id: string, shipToId?: string | null | undefined, deliveryInstructions?: string | null | undefined, preferredDate?: any | null | undefined, preferredTime?: PreferredTimeEnum | null | undefined, shouldShipFullOrder: boolean, phoneNumber?: string | null | undefined, address?: { __typename?: 'Address', id: string, companyName?: string | null | undefined, street1?: string | null | undefined, street2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, country?: string | null | undefined, custom: boolean } | null | undefined }, willCall: { __typename?: 'WillCall', id: string, preferredDate?: any | null | undefined, preferredTime?: PreferredTimeEnum | null | undefined, branchId?: string | null | undefined, pickupInstructions?: string | null | undefined }, products?: Array<{ __typename?: 'LineItem', id?: string | null | undefined, cartId?: string | null | undefined, customerPartNumber?: string | null | undefined, pricePerUnit?: number | null | undefined, uom?: string | null | undefined, priceLastUpdatedAt?: any | null | undefined, erpPartNumber?: string | null | undefined, quantity?: number | null | undefined, qtyAvailableLastUpdatedAt?: any | null | undefined, qtyAvailable?: number | null | undefined, listIds?: Array<string> | null | undefined, product?: { __typename?: 'Product', id: string, partNumber?: string | null | undefined, name?: string | null | undefined, manufacturerName?: string | null | undefined, manufacturerNumber?: string | null | undefined, price?: number | null | undefined, cmp?: number | null | undefined, taxonomy?: Array<string> | null | undefined, categories?: Array<string> | null | undefined, environmentalOptions?: Array<string> | null | undefined, upc?: string | null | undefined, unspsc?: string | null | undefined, seriesModelFigureNumber?: string | null | undefined, productOverview?: string | null | undefined, featuresAndBenefits?: string | null | undefined, minIncrementQty?: number | null | undefined, erp?: string | null | undefined, customerPartNumber?: Array<string> | null | undefined, stock?: { __typename?: 'Stock', homeBranch?: { __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined } | null | undefined, otherBranches?: Array<{ __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined }> | null | undefined } | null | undefined, technicalDocuments?: Array<{ __typename?: 'TechDoc', name?: string | null | undefined, url?: string | null | undefined }> | null | undefined, techSpecifications?: Array<{ __typename?: 'TechSpec', name?: string | null | undefined, value?: string | null | undefined }> | null | undefined, imageUrls?: { __typename?: 'ImageUrls', thumb?: string | null | undefined, small?: string | null | undefined, medium?: string | null | undefined, large?: string | null | undefined } | null | undefined, packageDimensions?: { __typename?: 'PackageDimensions', height?: number | null | undefined, weight?: number | null | undefined, length?: number | null | undefined, volume?: number | null | undefined, width?: number | null | undefined, volumeUnitOfMeasure?: string | null | undefined, weightUnitOfMeasure?: string | null | undefined } | null | undefined } | null | undefined }> | null | undefined } };

export type ErpAccountQueryVariables = Exact<{
  accountId: Scalars['String'];
  brand: Scalars['String'];
}>;


export type ErpAccountQuery = { __typename?: 'Query', account: Array<{ __typename?: 'ErpAccount', alwaysCod?: boolean | null | undefined, branchId?: string | null | undefined, city?: string | null | undefined, companyName?: string | null | undefined, email?: Array<string> | null | undefined, erpAccountId?: string | null | undefined, erp?: ErpSystemEnum | null | undefined, erpName?: string | null | undefined, phoneNumber?: string | null | undefined, state?: string | null | undefined, street1?: string | null | undefined, street2?: string | null | undefined, zip?: string | null | undefined, poReleaseRequired?: string | null | undefined, creditHold?: boolean | null | undefined, territory?: string | null | undefined }> };

export type WillCallBranchesQueryVariables = Exact<{
  shipToAccountId: Scalars['String'];
}>;


export type WillCallBranchesQuery = { __typename?: 'Query', willCallBranches: Array<{ __typename?: 'Branch', branchId: string, name?: string | null | undefined, entityId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, latitude: number, longitude: number, isPlumbing: boolean, isWaterworks: boolean, isHvac: boolean, isBandK: boolean }> };

export type CreateCartMutationVariables = Exact<{
  userId: Scalars['ID'];
  shipToAccountId: Scalars['ID'];
  branchId: Scalars['ID'];
}>;


export type CreateCartMutation = { __typename?: 'Mutation', createCart: string };

export type AddItemsToCartMutationVariables = Exact<{
  cartId: Scalars['ID'];
  addItemsInput: AddItemsInput;
}>;


export type AddItemsToCartMutation = { __typename?: 'Mutation', addItemsToCart: { __typename?: 'Cart', id: string, ownerId?: string | null | undefined, approverId?: string | null | undefined, shipToId?: string | null | undefined, poNumber?: string | null | undefined, pricingBranchId?: string | null | undefined, shippingBranchId?: string | null | undefined, paymentMethodType?: PaymentMethodTypeEnum | null | undefined, approvalState?: string | null | undefined, rejectionReason?: string | null | undefined, removedProducts?: Array<string> | null | undefined, subtotal?: number | null | undefined, tax?: number | null | undefined, shippingHandling?: number | null | undefined, total?: number | null | undefined, deliveryMethod?: DeliveryMethodEnum | null | undefined, erpSystemName: ErpSystemEnum, creditCard?: { __typename?: 'CreditCard', creditCardType: string, creditCardNumber: string, cardHolder: string, streetAddress: string, postalCode: string, elementPaymentAccountId: string, expirationDate: { __typename?: 'DateWrapper', date: string } } | null | undefined, delivery: { __typename?: 'Delivery', id: string, shipToId?: string | null | undefined, deliveryInstructions?: string | null | undefined, preferredDate?: any | null | undefined, preferredTime?: PreferredTimeEnum | null | undefined, shouldShipFullOrder: boolean, phoneNumber?: string | null | undefined, address?: { __typename?: 'Address', id: string, companyName?: string | null | undefined, street1?: string | null | undefined, street2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, country?: string | null | undefined, custom: boolean } | null | undefined }, willCall: { __typename?: 'WillCall', id: string, preferredDate?: any | null | undefined, preferredTime?: PreferredTimeEnum | null | undefined, branchId?: string | null | undefined, pickupInstructions?: string | null | undefined }, products?: Array<{ __typename?: 'LineItem', id?: string | null | undefined, cartId?: string | null | undefined, customerPartNumber?: string | null | undefined, pricePerUnit?: number | null | undefined, uom?: string | null | undefined, priceLastUpdatedAt?: any | null | undefined, erpPartNumber?: string | null | undefined, quantity?: number | null | undefined, qtyAvailableLastUpdatedAt?: any | null | undefined, qtyAvailable?: number | null | undefined, listIds?: Array<string> | null | undefined, product?: { __typename?: 'Product', id: string, partNumber?: string | null | undefined, name?: string | null | undefined, manufacturerName?: string | null | undefined, manufacturerNumber?: string | null | undefined, price?: number | null | undefined, cmp?: number | null | undefined, taxonomy?: Array<string> | null | undefined, categories?: Array<string> | null | undefined, environmentalOptions?: Array<string> | null | undefined, upc?: string | null | undefined, unspsc?: string | null | undefined, seriesModelFigureNumber?: string | null | undefined, productOverview?: string | null | undefined, featuresAndBenefits?: string | null | undefined, minIncrementQty?: number | null | undefined, erp?: string | null | undefined, customerPartNumber?: Array<string> | null | undefined, stock?: { __typename?: 'Stock', homeBranch?: { __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined } | null | undefined, otherBranches?: Array<{ __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined }> | null | undefined } | null | undefined, technicalDocuments?: Array<{ __typename?: 'TechDoc', name?: string | null | undefined, url?: string | null | undefined }> | null | undefined, techSpecifications?: Array<{ __typename?: 'TechSpec', name?: string | null | undefined, value?: string | null | undefined }> | null | undefined, imageUrls?: { __typename?: 'ImageUrls', thumb?: string | null | undefined, small?: string | null | undefined, medium?: string | null | undefined, large?: string | null | undefined } | null | undefined, packageDimensions?: { __typename?: 'PackageDimensions', height?: number | null | undefined, weight?: number | null | undefined, length?: number | null | undefined, volume?: number | null | undefined, width?: number | null | undefined, volumeUnitOfMeasure?: string | null | undefined, weightUnitOfMeasure?: string | null | undefined } | null | undefined } | null | undefined }> | null | undefined } };

export type AddAllListItemsToCartMutationVariables = Exact<{
  cartId: Scalars['ID'];
  listId: Scalars['ID'];
  addAllToCartUserInput: AddAllToCartUserInput;
}>;


export type AddAllListItemsToCartMutation = { __typename?: 'Mutation', addAllListItemsToCart: { __typename?: 'Cart', id: string, ownerId?: string | null | undefined, approverId?: string | null | undefined, shipToId?: string | null | undefined, poNumber?: string | null | undefined, pricingBranchId?: string | null | undefined, shippingBranchId?: string | null | undefined, paymentMethodType?: PaymentMethodTypeEnum | null | undefined, approvalState?: string | null | undefined, rejectionReason?: string | null | undefined, removedProducts?: Array<string> | null | undefined, subtotal?: number | null | undefined, tax?: number | null | undefined, shippingHandling?: number | null | undefined, total?: number | null | undefined, deliveryMethod?: DeliveryMethodEnum | null | undefined, erpSystemName: ErpSystemEnum, creditCard?: { __typename?: 'CreditCard', creditCardType: string, creditCardNumber: string, cardHolder: string, streetAddress: string, postalCode: string, elementPaymentAccountId: string, expirationDate: { __typename?: 'DateWrapper', date: string } } | null | undefined, delivery: { __typename?: 'Delivery', id: string, shipToId?: string | null | undefined, deliveryInstructions?: string | null | undefined, preferredDate?: any | null | undefined, preferredTime?: PreferredTimeEnum | null | undefined, shouldShipFullOrder: boolean, phoneNumber?: string | null | undefined, address?: { __typename?: 'Address', id: string, companyName?: string | null | undefined, street1?: string | null | undefined, street2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, country?: string | null | undefined, custom: boolean } | null | undefined }, willCall: { __typename?: 'WillCall', id: string, preferredDate?: any | null | undefined, preferredTime?: PreferredTimeEnum | null | undefined, branchId?: string | null | undefined, pickupInstructions?: string | null | undefined }, products?: Array<{ __typename?: 'LineItem', id?: string | null | undefined, cartId?: string | null | undefined, customerPartNumber?: string | null | undefined, pricePerUnit?: number | null | undefined, uom?: string | null | undefined, priceLastUpdatedAt?: any | null | undefined, erpPartNumber?: string | null | undefined, quantity?: number | null | undefined, qtyAvailableLastUpdatedAt?: any | null | undefined, qtyAvailable?: number | null | undefined, listIds?: Array<string> | null | undefined, product?: { __typename?: 'Product', id: string, partNumber?: string | null | undefined, name?: string | null | undefined, manufacturerName?: string | null | undefined, manufacturerNumber?: string | null | undefined, price?: number | null | undefined, cmp?: number | null | undefined, taxonomy?: Array<string> | null | undefined, categories?: Array<string> | null | undefined, environmentalOptions?: Array<string> | null | undefined, upc?: string | null | undefined, unspsc?: string | null | undefined, seriesModelFigureNumber?: string | null | undefined, productOverview?: string | null | undefined, featuresAndBenefits?: string | null | undefined, minIncrementQty?: number | null | undefined, erp?: string | null | undefined, customerPartNumber?: Array<string> | null | undefined, stock?: { __typename?: 'Stock', homeBranch?: { __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined } | null | undefined, otherBranches?: Array<{ __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined }> | null | undefined } | null | undefined, technicalDocuments?: Array<{ __typename?: 'TechDoc', name?: string | null | undefined, url?: string | null | undefined }> | null | undefined, techSpecifications?: Array<{ __typename?: 'TechSpec', name?: string | null | undefined, value?: string | null | undefined }> | null | undefined, imageUrls?: { __typename?: 'ImageUrls', thumb?: string | null | undefined, small?: string | null | undefined, medium?: string | null | undefined, large?: string | null | undefined } | null | undefined, packageDimensions?: { __typename?: 'PackageDimensions', height?: number | null | undefined, weight?: number | null | undefined, length?: number | null | undefined, volume?: number | null | undefined, width?: number | null | undefined, volumeUnitOfMeasure?: string | null | undefined, weightUnitOfMeasure?: string | null | undefined } | null | undefined } | null | undefined }> | null | undefined } };

export type DeleteItemMutationVariables = Exact<{
  cartId: Scalars['ID'];
  itemId: Scalars['ID'];
  userId: Scalars['ID'];
  shipToAccountId: Scalars['ID'];
}>;


export type DeleteItemMutation = { __typename?: 'Mutation', deleteItem: { __typename?: 'Cart', id: string, ownerId?: string | null | undefined, approverId?: string | null | undefined, shipToId?: string | null | undefined, poNumber?: string | null | undefined, pricingBranchId?: string | null | undefined, shippingBranchId?: string | null | undefined, paymentMethodType?: PaymentMethodTypeEnum | null | undefined, approvalState?: string | null | undefined, rejectionReason?: string | null | undefined, removedProducts?: Array<string> | null | undefined, subtotal?: number | null | undefined, tax?: number | null | undefined, shippingHandling?: number | null | undefined, total?: number | null | undefined, deliveryMethod?: DeliveryMethodEnum | null | undefined, erpSystemName: ErpSystemEnum, creditCard?: { __typename?: 'CreditCard', creditCardType: string, creditCardNumber: string, cardHolder: string, streetAddress: string, postalCode: string, elementPaymentAccountId: string, expirationDate: { __typename?: 'DateWrapper', date: string } } | null | undefined, delivery: { __typename?: 'Delivery', id: string, shipToId?: string | null | undefined, deliveryInstructions?: string | null | undefined, preferredDate?: any | null | undefined, preferredTime?: PreferredTimeEnum | null | undefined, shouldShipFullOrder: boolean, phoneNumber?: string | null | undefined, address?: { __typename?: 'Address', id: string, companyName?: string | null | undefined, street1?: string | null | undefined, street2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, country?: string | null | undefined, custom: boolean } | null | undefined }, willCall: { __typename?: 'WillCall', id: string, preferredDate?: any | null | undefined, preferredTime?: PreferredTimeEnum | null | undefined, branchId?: string | null | undefined, pickupInstructions?: string | null | undefined }, products?: Array<{ __typename?: 'LineItem', id?: string | null | undefined, cartId?: string | null | undefined, customerPartNumber?: string | null | undefined, pricePerUnit?: number | null | undefined, uom?: string | null | undefined, priceLastUpdatedAt?: any | null | undefined, erpPartNumber?: string | null | undefined, quantity?: number | null | undefined, qtyAvailableLastUpdatedAt?: any | null | undefined, qtyAvailable?: number | null | undefined, listIds?: Array<string> | null | undefined, product?: { __typename?: 'Product', id: string, partNumber?: string | null | undefined, name?: string | null | undefined, manufacturerName?: string | null | undefined, manufacturerNumber?: string | null | undefined, price?: number | null | undefined, cmp?: number | null | undefined, taxonomy?: Array<string> | null | undefined, categories?: Array<string> | null | undefined, environmentalOptions?: Array<string> | null | undefined, upc?: string | null | undefined, unspsc?: string | null | undefined, seriesModelFigureNumber?: string | null | undefined, productOverview?: string | null | undefined, featuresAndBenefits?: string | null | undefined, minIncrementQty?: number | null | undefined, erp?: string | null | undefined, customerPartNumber?: Array<string> | null | undefined, stock?: { __typename?: 'Stock', homeBranch?: { __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined } | null | undefined, otherBranches?: Array<{ __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined }> | null | undefined } | null | undefined, technicalDocuments?: Array<{ __typename?: 'TechDoc', name?: string | null | undefined, url?: string | null | undefined }> | null | undefined, techSpecifications?: Array<{ __typename?: 'TechSpec', name?: string | null | undefined, value?: string | null | undefined }> | null | undefined, imageUrls?: { __typename?: 'ImageUrls', thumb?: string | null | undefined, small?: string | null | undefined, medium?: string | null | undefined, large?: string | null | undefined } | null | undefined, packageDimensions?: { __typename?: 'PackageDimensions', height?: number | null | undefined, weight?: number | null | undefined, length?: number | null | undefined, volume?: number | null | undefined, width?: number | null | undefined, volumeUnitOfMeasure?: string | null | undefined, weightUnitOfMeasure?: string | null | undefined } | null | undefined } | null | undefined }> | null | undefined } };

export type UpdateCartMutationVariables = Exact<{
  cartId: Scalars['ID'];
  cart: CartInput;
}>;


export type UpdateCartMutation = { __typename?: 'Mutation', updateCart: { __typename?: 'Cart', id: string, ownerId?: string | null | undefined, approverId?: string | null | undefined, shipToId?: string | null | undefined, poNumber?: string | null | undefined, pricingBranchId?: string | null | undefined, shippingBranchId?: string | null | undefined, paymentMethodType?: PaymentMethodTypeEnum | null | undefined, approvalState?: string | null | undefined, rejectionReason?: string | null | undefined, removedProducts?: Array<string> | null | undefined, subtotal?: number | null | undefined, tax?: number | null | undefined, shippingHandling?: number | null | undefined, total?: number | null | undefined, deliveryMethod?: DeliveryMethodEnum | null | undefined, erpSystemName: ErpSystemEnum, creditCard?: { __typename?: 'CreditCard', creditCardType: string, creditCardNumber: string, cardHolder: string, streetAddress: string, postalCode: string, elementPaymentAccountId: string, expirationDate: { __typename?: 'DateWrapper', date: string } } | null | undefined, delivery: { __typename?: 'Delivery', id: string, shipToId?: string | null | undefined, deliveryInstructions?: string | null | undefined, preferredDate?: any | null | undefined, preferredTime?: PreferredTimeEnum | null | undefined, shouldShipFullOrder: boolean, phoneNumber?: string | null | undefined, address?: { __typename?: 'Address', id: string, companyName?: string | null | undefined, street1?: string | null | undefined, street2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, country?: string | null | undefined, custom: boolean } | null | undefined }, willCall: { __typename?: 'WillCall', id: string, preferredDate?: any | null | undefined, preferredTime?: PreferredTimeEnum | null | undefined, branchId?: string | null | undefined, pickupInstructions?: string | null | undefined }, products?: Array<{ __typename?: 'LineItem', id?: string | null | undefined, cartId?: string | null | undefined, customerPartNumber?: string | null | undefined, pricePerUnit?: number | null | undefined, uom?: string | null | undefined, priceLastUpdatedAt?: any | null | undefined, erpPartNumber?: string | null | undefined, quantity?: number | null | undefined, qtyAvailableLastUpdatedAt?: any | null | undefined, qtyAvailable?: number | null | undefined, listIds?: Array<string> | null | undefined, product?: { __typename?: 'Product', id: string, partNumber?: string | null | undefined, name?: string | null | undefined, manufacturerName?: string | null | undefined, manufacturerNumber?: string | null | undefined, price?: number | null | undefined, cmp?: number | null | undefined, taxonomy?: Array<string> | null | undefined, categories?: Array<string> | null | undefined, environmentalOptions?: Array<string> | null | undefined, upc?: string | null | undefined, unspsc?: string | null | undefined, seriesModelFigureNumber?: string | null | undefined, productOverview?: string | null | undefined, featuresAndBenefits?: string | null | undefined, minIncrementQty?: number | null | undefined, erp?: string | null | undefined, customerPartNumber?: Array<string> | null | undefined, stock?: { __typename?: 'Stock', homeBranch?: { __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined } | null | undefined, otherBranches?: Array<{ __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined }> | null | undefined } | null | undefined, technicalDocuments?: Array<{ __typename?: 'TechDoc', name?: string | null | undefined, url?: string | null | undefined }> | null | undefined, techSpecifications?: Array<{ __typename?: 'TechSpec', name?: string | null | undefined, value?: string | null | undefined }> | null | undefined, imageUrls?: { __typename?: 'ImageUrls', thumb?: string | null | undefined, small?: string | null | undefined, medium?: string | null | undefined, large?: string | null | undefined } | null | undefined, packageDimensions?: { __typename?: 'PackageDimensions', height?: number | null | undefined, weight?: number | null | undefined, length?: number | null | undefined, volume?: number | null | undefined, width?: number | null | undefined, volumeUnitOfMeasure?: string | null | undefined, weightUnitOfMeasure?: string | null | undefined } | null | undefined } | null | undefined }> | null | undefined } };

export type RefreshCartMutationVariables = Exact<{
  cartId: Scalars['ID'];
  userId: Scalars['ID'];
  shipToAccountId: Scalars['ID'];
}>;


export type RefreshCartMutation = { __typename?: 'Mutation', refreshCart: { __typename?: 'Cart', id: string, ownerId?: string | null | undefined, approverId?: string | null | undefined, shipToId?: string | null | undefined, poNumber?: string | null | undefined, pricingBranchId?: string | null | undefined, shippingBranchId?: string | null | undefined, paymentMethodType?: PaymentMethodTypeEnum | null | undefined, approvalState?: string | null | undefined, rejectionReason?: string | null | undefined, removedProducts?: Array<string> | null | undefined, subtotal?: number | null | undefined, tax?: number | null | undefined, shippingHandling?: number | null | undefined, total?: number | null | undefined, deliveryMethod?: DeliveryMethodEnum | null | undefined, erpSystemName: ErpSystemEnum, creditCard?: { __typename?: 'CreditCard', creditCardType: string, creditCardNumber: string, cardHolder: string, streetAddress: string, postalCode: string, elementPaymentAccountId: string, expirationDate: { __typename?: 'DateWrapper', date: string } } | null | undefined, delivery: { __typename?: 'Delivery', id: string, shipToId?: string | null | undefined, deliveryInstructions?: string | null | undefined, preferredDate?: any | null | undefined, preferredTime?: PreferredTimeEnum | null | undefined, shouldShipFullOrder: boolean, phoneNumber?: string | null | undefined, address?: { __typename?: 'Address', id: string, companyName?: string | null | undefined, street1?: string | null | undefined, street2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, country?: string | null | undefined, custom: boolean } | null | undefined }, willCall: { __typename?: 'WillCall', id: string, preferredDate?: any | null | undefined, preferredTime?: PreferredTimeEnum | null | undefined, branchId?: string | null | undefined, pickupInstructions?: string | null | undefined }, products?: Array<{ __typename?: 'LineItem', id?: string | null | undefined, cartId?: string | null | undefined, customerPartNumber?: string | null | undefined, pricePerUnit?: number | null | undefined, uom?: string | null | undefined, priceLastUpdatedAt?: any | null | undefined, erpPartNumber?: string | null | undefined, quantity?: number | null | undefined, qtyAvailableLastUpdatedAt?: any | null | undefined, qtyAvailable?: number | null | undefined, listIds?: Array<string> | null | undefined, product?: { __typename?: 'Product', id: string, partNumber?: string | null | undefined, name?: string | null | undefined, manufacturerName?: string | null | undefined, manufacturerNumber?: string | null | undefined, price?: number | null | undefined, cmp?: number | null | undefined, taxonomy?: Array<string> | null | undefined, categories?: Array<string> | null | undefined, environmentalOptions?: Array<string> | null | undefined, upc?: string | null | undefined, unspsc?: string | null | undefined, seriesModelFigureNumber?: string | null | undefined, productOverview?: string | null | undefined, featuresAndBenefits?: string | null | undefined, minIncrementQty?: number | null | undefined, erp?: string | null | undefined, customerPartNumber?: Array<string> | null | undefined, stock?: { __typename?: 'Stock', homeBranch?: { __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined } | null | undefined, otherBranches?: Array<{ __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined }> | null | undefined } | null | undefined, technicalDocuments?: Array<{ __typename?: 'TechDoc', name?: string | null | undefined, url?: string | null | undefined }> | null | undefined, techSpecifications?: Array<{ __typename?: 'TechSpec', name?: string | null | undefined, value?: string | null | undefined }> | null | undefined, imageUrls?: { __typename?: 'ImageUrls', thumb?: string | null | undefined, small?: string | null | undefined, medium?: string | null | undefined, large?: string | null | undefined } | null | undefined, packageDimensions?: { __typename?: 'PackageDimensions', height?: number | null | undefined, weight?: number | null | undefined, length?: number | null | undefined, volume?: number | null | undefined, width?: number | null | undefined, volumeUnitOfMeasure?: string | null | undefined, weightUnitOfMeasure?: string | null | undefined } | null | undefined } | null | undefined }> | null | undefined } };

export type DeleteCartItemsMutationVariables = Exact<{
  cartId: Scalars['ID'];
}>;


export type DeleteCartItemsMutation = { __typename?: 'Mutation', deleteCartItems: { __typename?: 'Cart', id: string, ownerId?: string | null | undefined, approverId?: string | null | undefined, shipToId?: string | null | undefined, poNumber?: string | null | undefined, pricingBranchId?: string | null | undefined, shippingBranchId?: string | null | undefined, paymentMethodType?: PaymentMethodTypeEnum | null | undefined, approvalState?: string | null | undefined, rejectionReason?: string | null | undefined, removedProducts?: Array<string> | null | undefined, subtotal?: number | null | undefined, tax?: number | null | undefined, shippingHandling?: number | null | undefined, total?: number | null | undefined, deliveryMethod?: DeliveryMethodEnum | null | undefined, erpSystemName: ErpSystemEnum, creditCard?: { __typename?: 'CreditCard', creditCardType: string, creditCardNumber: string, cardHolder: string, streetAddress: string, postalCode: string, elementPaymentAccountId: string, expirationDate: { __typename?: 'DateWrapper', date: string } } | null | undefined, delivery: { __typename?: 'Delivery', id: string, shipToId?: string | null | undefined, deliveryInstructions?: string | null | undefined, preferredDate?: any | null | undefined, preferredTime?: PreferredTimeEnum | null | undefined, shouldShipFullOrder: boolean, phoneNumber?: string | null | undefined, address?: { __typename?: 'Address', id: string, companyName?: string | null | undefined, street1?: string | null | undefined, street2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, country?: string | null | undefined, custom: boolean } | null | undefined }, willCall: { __typename?: 'WillCall', id: string, preferredDate?: any | null | undefined, preferredTime?: PreferredTimeEnum | null | undefined, branchId?: string | null | undefined, pickupInstructions?: string | null | undefined }, products?: Array<{ __typename?: 'LineItem', id?: string | null | undefined, cartId?: string | null | undefined, customerPartNumber?: string | null | undefined, pricePerUnit?: number | null | undefined, uom?: string | null | undefined, priceLastUpdatedAt?: any | null | undefined, erpPartNumber?: string | null | undefined, quantity?: number | null | undefined, qtyAvailableLastUpdatedAt?: any | null | undefined, qtyAvailable?: number | null | undefined, listIds?: Array<string> | null | undefined, product?: { __typename?: 'Product', id: string, partNumber?: string | null | undefined, name?: string | null | undefined, manufacturerName?: string | null | undefined, manufacturerNumber?: string | null | undefined, price?: number | null | undefined, cmp?: number | null | undefined, taxonomy?: Array<string> | null | undefined, categories?: Array<string> | null | undefined, environmentalOptions?: Array<string> | null | undefined, upc?: string | null | undefined, unspsc?: string | null | undefined, seriesModelFigureNumber?: string | null | undefined, productOverview?: string | null | undefined, featuresAndBenefits?: string | null | undefined, minIncrementQty?: number | null | undefined, erp?: string | null | undefined, customerPartNumber?: Array<string> | null | undefined, stock?: { __typename?: 'Stock', homeBranch?: { __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined } | null | undefined, otherBranches?: Array<{ __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined }> | null | undefined } | null | undefined, technicalDocuments?: Array<{ __typename?: 'TechDoc', name?: string | null | undefined, url?: string | null | undefined }> | null | undefined, techSpecifications?: Array<{ __typename?: 'TechSpec', name?: string | null | undefined, value?: string | null | undefined }> | null | undefined, imageUrls?: { __typename?: 'ImageUrls', thumb?: string | null | undefined, small?: string | null | undefined, medium?: string | null | undefined, large?: string | null | undefined } | null | undefined, packageDimensions?: { __typename?: 'PackageDimensions', height?: number | null | undefined, weight?: number | null | undefined, length?: number | null | undefined, volume?: number | null | undefined, width?: number | null | undefined, volumeUnitOfMeasure?: string | null | undefined, weightUnitOfMeasure?: string | null | undefined } | null | undefined } | null | undefined }> | null | undefined } };

export type UpdateDeliveryMutationVariables = Exact<{
  deliveryInfo: DeliveryInput;
}>;


export type UpdateDeliveryMutation = { __typename?: 'Mutation', updateDelivery: { __typename?: 'Cart', id: string, ownerId?: string | null | undefined, approverId?: string | null | undefined, shipToId?: string | null | undefined, poNumber?: string | null | undefined, pricingBranchId?: string | null | undefined, shippingBranchId?: string | null | undefined, paymentMethodType?: PaymentMethodTypeEnum | null | undefined, approvalState?: string | null | undefined, rejectionReason?: string | null | undefined, removedProducts?: Array<string> | null | undefined, subtotal?: number | null | undefined, tax?: number | null | undefined, shippingHandling?: number | null | undefined, total?: number | null | undefined, deliveryMethod?: DeliveryMethodEnum | null | undefined, erpSystemName: ErpSystemEnum, creditCard?: { __typename?: 'CreditCard', creditCardType: string, creditCardNumber: string, cardHolder: string, streetAddress: string, postalCode: string, elementPaymentAccountId: string, expirationDate: { __typename?: 'DateWrapper', date: string } } | null | undefined, delivery: { __typename?: 'Delivery', id: string, shipToId?: string | null | undefined, deliveryInstructions?: string | null | undefined, preferredDate?: any | null | undefined, preferredTime?: PreferredTimeEnum | null | undefined, shouldShipFullOrder: boolean, phoneNumber?: string | null | undefined, address?: { __typename?: 'Address', id: string, companyName?: string | null | undefined, street1?: string | null | undefined, street2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, country?: string | null | undefined, custom: boolean } | null | undefined }, willCall: { __typename?: 'WillCall', id: string, preferredDate?: any | null | undefined, preferredTime?: PreferredTimeEnum | null | undefined, branchId?: string | null | undefined, pickupInstructions?: string | null | undefined }, products?: Array<{ __typename?: 'LineItem', id?: string | null | undefined, cartId?: string | null | undefined, customerPartNumber?: string | null | undefined, pricePerUnit?: number | null | undefined, uom?: string | null | undefined, priceLastUpdatedAt?: any | null | undefined, erpPartNumber?: string | null | undefined, quantity?: number | null | undefined, qtyAvailableLastUpdatedAt?: any | null | undefined, qtyAvailable?: number | null | undefined, listIds?: Array<string> | null | undefined, product?: { __typename?: 'Product', id: string, partNumber?: string | null | undefined, name?: string | null | undefined, manufacturerName?: string | null | undefined, manufacturerNumber?: string | null | undefined, price?: number | null | undefined, cmp?: number | null | undefined, taxonomy?: Array<string> | null | undefined, categories?: Array<string> | null | undefined, environmentalOptions?: Array<string> | null | undefined, upc?: string | null | undefined, unspsc?: string | null | undefined, seriesModelFigureNumber?: string | null | undefined, productOverview?: string | null | undefined, featuresAndBenefits?: string | null | undefined, minIncrementQty?: number | null | undefined, erp?: string | null | undefined, customerPartNumber?: Array<string> | null | undefined, stock?: { __typename?: 'Stock', homeBranch?: { __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined } | null | undefined, otherBranches?: Array<{ __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined }> | null | undefined } | null | undefined, technicalDocuments?: Array<{ __typename?: 'TechDoc', name?: string | null | undefined, url?: string | null | undefined }> | null | undefined, techSpecifications?: Array<{ __typename?: 'TechSpec', name?: string | null | undefined, value?: string | null | undefined }> | null | undefined, imageUrls?: { __typename?: 'ImageUrls', thumb?: string | null | undefined, small?: string | null | undefined, medium?: string | null | undefined, large?: string | null | undefined } | null | undefined, packageDimensions?: { __typename?: 'PackageDimensions', height?: number | null | undefined, weight?: number | null | undefined, length?: number | null | undefined, volume?: number | null | undefined, width?: number | null | undefined, volumeUnitOfMeasure?: string | null | undefined, weightUnitOfMeasure?: string | null | undefined } | null | undefined } | null | undefined }> | null | undefined } };

export type UpdateWillCallMutationVariables = Exact<{
  willCallInfo: WillCallInput;
}>;


export type UpdateWillCallMutation = { __typename?: 'Mutation', updateWillCall: { __typename?: 'Cart', id: string, ownerId?: string | null | undefined, approverId?: string | null | undefined, shipToId?: string | null | undefined, poNumber?: string | null | undefined, pricingBranchId?: string | null | undefined, shippingBranchId?: string | null | undefined, paymentMethodType?: PaymentMethodTypeEnum | null | undefined, approvalState?: string | null | undefined, rejectionReason?: string | null | undefined, removedProducts?: Array<string> | null | undefined, subtotal?: number | null | undefined, tax?: number | null | undefined, shippingHandling?: number | null | undefined, total?: number | null | undefined, deliveryMethod?: DeliveryMethodEnum | null | undefined, erpSystemName: ErpSystemEnum, creditCard?: { __typename?: 'CreditCard', creditCardType: string, creditCardNumber: string, cardHolder: string, streetAddress: string, postalCode: string, elementPaymentAccountId: string, expirationDate: { __typename?: 'DateWrapper', date: string } } | null | undefined, delivery: { __typename?: 'Delivery', id: string, shipToId?: string | null | undefined, deliveryInstructions?: string | null | undefined, preferredDate?: any | null | undefined, preferredTime?: PreferredTimeEnum | null | undefined, shouldShipFullOrder: boolean, phoneNumber?: string | null | undefined, address?: { __typename?: 'Address', id: string, companyName?: string | null | undefined, street1?: string | null | undefined, street2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, country?: string | null | undefined, custom: boolean } | null | undefined }, willCall: { __typename?: 'WillCall', id: string, preferredDate?: any | null | undefined, preferredTime?: PreferredTimeEnum | null | undefined, branchId?: string | null | undefined, pickupInstructions?: string | null | undefined }, products?: Array<{ __typename?: 'LineItem', id?: string | null | undefined, cartId?: string | null | undefined, customerPartNumber?: string | null | undefined, pricePerUnit?: number | null | undefined, uom?: string | null | undefined, priceLastUpdatedAt?: any | null | undefined, erpPartNumber?: string | null | undefined, quantity?: number | null | undefined, qtyAvailableLastUpdatedAt?: any | null | undefined, qtyAvailable?: number | null | undefined, listIds?: Array<string> | null | undefined, product?: { __typename?: 'Product', id: string, partNumber?: string | null | undefined, name?: string | null | undefined, manufacturerName?: string | null | undefined, manufacturerNumber?: string | null | undefined, price?: number | null | undefined, cmp?: number | null | undefined, taxonomy?: Array<string> | null | undefined, categories?: Array<string> | null | undefined, environmentalOptions?: Array<string> | null | undefined, upc?: string | null | undefined, unspsc?: string | null | undefined, seriesModelFigureNumber?: string | null | undefined, productOverview?: string | null | undefined, featuresAndBenefits?: string | null | undefined, minIncrementQty?: number | null | undefined, erp?: string | null | undefined, customerPartNumber?: Array<string> | null | undefined, stock?: { __typename?: 'Stock', homeBranch?: { __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined } | null | undefined, otherBranches?: Array<{ __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined }> | null | undefined } | null | undefined, technicalDocuments?: Array<{ __typename?: 'TechDoc', name?: string | null | undefined, url?: string | null | undefined }> | null | undefined, techSpecifications?: Array<{ __typename?: 'TechSpec', name?: string | null | undefined, value?: string | null | undefined }> | null | undefined, imageUrls?: { __typename?: 'ImageUrls', thumb?: string | null | undefined, small?: string | null | undefined, medium?: string | null | undefined, large?: string | null | undefined } | null | undefined, packageDimensions?: { __typename?: 'PackageDimensions', height?: number | null | undefined, weight?: number | null | undefined, length?: number | null | undefined, volume?: number | null | undefined, width?: number | null | undefined, volumeUnitOfMeasure?: string | null | undefined, weightUnitOfMeasure?: string | null | undefined } | null | undefined } | null | undefined }> | null | undefined } };

export type UpdateWillCallBranchMutationVariables = Exact<{
  cartId: Scalars['ID'];
  branchId: Scalars['ID'];
  userId: Scalars['ID'];
  shipToAccountId: Scalars['ID'];
}>;


export type UpdateWillCallBranchMutation = { __typename?: 'Mutation', updateWillCallBranch: { __typename?: 'Cart', id: string, ownerId?: string | null | undefined, approverId?: string | null | undefined, shipToId?: string | null | undefined, poNumber?: string | null | undefined, pricingBranchId?: string | null | undefined, shippingBranchId?: string | null | undefined, paymentMethodType?: PaymentMethodTypeEnum | null | undefined, approvalState?: string | null | undefined, rejectionReason?: string | null | undefined, removedProducts?: Array<string> | null | undefined, subtotal?: number | null | undefined, tax?: number | null | undefined, shippingHandling?: number | null | undefined, total?: number | null | undefined, deliveryMethod?: DeliveryMethodEnum | null | undefined, erpSystemName: ErpSystemEnum, creditCard?: { __typename?: 'CreditCard', creditCardType: string, creditCardNumber: string, cardHolder: string, streetAddress: string, postalCode: string, elementPaymentAccountId: string, expirationDate: { __typename?: 'DateWrapper', date: string } } | null | undefined, delivery: { __typename?: 'Delivery', id: string, shipToId?: string | null | undefined, deliveryInstructions?: string | null | undefined, preferredDate?: any | null | undefined, preferredTime?: PreferredTimeEnum | null | undefined, shouldShipFullOrder: boolean, phoneNumber?: string | null | undefined, address?: { __typename?: 'Address', id: string, companyName?: string | null | undefined, street1?: string | null | undefined, street2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, country?: string | null | undefined, custom: boolean } | null | undefined }, willCall: { __typename?: 'WillCall', id: string, preferredDate?: any | null | undefined, preferredTime?: PreferredTimeEnum | null | undefined, branchId?: string | null | undefined, pickupInstructions?: string | null | undefined }, products?: Array<{ __typename?: 'LineItem', id?: string | null | undefined, cartId?: string | null | undefined, customerPartNumber?: string | null | undefined, pricePerUnit?: number | null | undefined, uom?: string | null | undefined, priceLastUpdatedAt?: any | null | undefined, erpPartNumber?: string | null | undefined, quantity?: number | null | undefined, qtyAvailableLastUpdatedAt?: any | null | undefined, qtyAvailable?: number | null | undefined, listIds?: Array<string> | null | undefined, product?: { __typename?: 'Product', id: string, partNumber?: string | null | undefined, name?: string | null | undefined, manufacturerName?: string | null | undefined, manufacturerNumber?: string | null | undefined, price?: number | null | undefined, cmp?: number | null | undefined, taxonomy?: Array<string> | null | undefined, categories?: Array<string> | null | undefined, environmentalOptions?: Array<string> | null | undefined, upc?: string | null | undefined, unspsc?: string | null | undefined, seriesModelFigureNumber?: string | null | undefined, productOverview?: string | null | undefined, featuresAndBenefits?: string | null | undefined, minIncrementQty?: number | null | undefined, erp?: string | null | undefined, customerPartNumber?: Array<string> | null | undefined, stock?: { __typename?: 'Stock', homeBranch?: { __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined } | null | undefined, otherBranches?: Array<{ __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined }> | null | undefined } | null | undefined, technicalDocuments?: Array<{ __typename?: 'TechDoc', name?: string | null | undefined, url?: string | null | undefined }> | null | undefined, techSpecifications?: Array<{ __typename?: 'TechSpec', name?: string | null | undefined, value?: string | null | undefined }> | null | undefined, imageUrls?: { __typename?: 'ImageUrls', thumb?: string | null | undefined, small?: string | null | undefined, medium?: string | null | undefined, large?: string | null | undefined } | null | undefined, packageDimensions?: { __typename?: 'PackageDimensions', height?: number | null | undefined, weight?: number | null | undefined, length?: number | null | undefined, volume?: number | null | undefined, width?: number | null | undefined, volumeUnitOfMeasure?: string | null | undefined, weightUnitOfMeasure?: string | null | undefined } | null | undefined } | null | undefined }> | null | undefined } };

export type UpdateItemQuantityMutationVariables = Exact<{
  cartId: Scalars['ID'];
  itemId: Scalars['ID'];
  quantity: Scalars['Int'];
  minIncrementQty: Scalars['Int'];
  userId: Scalars['ID'];
  shipToAccountId: Scalars['ID'];
  customerNumber?: InputMaybe<Scalars['String']>;
}>;


export type UpdateItemQuantityMutation = { __typename?: 'Mutation', updateItemQuantity: { __typename?: 'UpdateItemQtyResponse', subtotal?: number | null | undefined, product: { __typename?: 'LineItem', id?: string | null | undefined, cartId?: string | null | undefined, customerPartNumber?: string | null | undefined, pricePerUnit?: number | null | undefined, uom?: string | null | undefined, priceLastUpdatedAt?: any | null | undefined, erpPartNumber?: string | null | undefined, quantity?: number | null | undefined, qtyAvailableLastUpdatedAt?: any | null | undefined, qtyAvailable?: number | null | undefined, listIds?: Array<string> | null | undefined, product?: { __typename?: 'Product', id: string, partNumber?: string | null | undefined, name?: string | null | undefined, manufacturerName?: string | null | undefined, manufacturerNumber?: string | null | undefined, price?: number | null | undefined, cmp?: number | null | undefined, taxonomy?: Array<string> | null | undefined, categories?: Array<string> | null | undefined, environmentalOptions?: Array<string> | null | undefined, upc?: string | null | undefined, unspsc?: string | null | undefined, seriesModelFigureNumber?: string | null | undefined, productOverview?: string | null | undefined, featuresAndBenefits?: string | null | undefined, minIncrementQty?: number | null | undefined, erp?: string | null | undefined, customerPartNumber?: Array<string> | null | undefined, stock?: { __typename?: 'Stock', homeBranch?: { __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined } | null | undefined, otherBranches?: Array<{ __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined }> | null | undefined } | null | undefined, technicalDocuments?: Array<{ __typename?: 'TechDoc', name?: string | null | undefined, url?: string | null | undefined }> | null | undefined, techSpecifications?: Array<{ __typename?: 'TechSpec', name?: string | null | undefined, value?: string | null | undefined }> | null | undefined, imageUrls?: { __typename?: 'ImageUrls', thumb?: string | null | undefined, small?: string | null | undefined, medium?: string | null | undefined, large?: string | null | undefined } | null | undefined, packageDimensions?: { __typename?: 'PackageDimensions', height?: number | null | undefined, weight?: number | null | undefined, length?: number | null | undefined, volume?: number | null | undefined, width?: number | null | undefined, volumeUnitOfMeasure?: string | null | undefined, weightUnitOfMeasure?: string | null | undefined } | null | undefined } | null | undefined } } };

export type SubmitOrderPreviewMutationVariables = Exact<{
  cartId: Scalars['ID'];
  userId: Scalars['ID'];
  shipToAccountId: Scalars['ID'];
  billToAccountId: Scalars['ID'];
}>;


export type SubmitOrderPreviewMutation = { __typename?: 'Mutation', submitOrderPreview: { __typename?: 'OrderPreviewResponse', subTotal: string, tax: string, orderTotal: string } };

export type SubmitOrderMutationVariables = Exact<{
  cartId: Scalars['ID'];
  userId: Scalars['ID'];
  shipToAccountId: Scalars['ID'];
  billToAccountId: Scalars['ID'];
}>;


export type SubmitOrderMutation = { __typename?: 'Mutation', submitOrder: { __typename?: 'OrderResponse', orderNumber?: string | null | undefined, orderStatus?: string | null | undefined, webStatus?: string | null | undefined, shipDate?: string | null | undefined, customerPO?: string | null | undefined, orderDate?: string | null | undefined, amount?: string | null | undefined, invoiceNumber?: string | null | undefined, billToName?: string | null | undefined, shipToName?: string | null | undefined, orderedBy?: string | null | undefined, email?: string | null | undefined, deliveryMethod?: string | null | undefined, specialInstructions?: string | null | undefined, subTotal?: number | null | undefined, tax?: number | null | undefined, orderTotal?: number | null | undefined, shipAddress?: { __typename?: 'EclipseAddress', streetLineOne?: string | null | undefined, streetLineTwo?: string | null | undefined, streetLineThree?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, postalCode?: string | null | undefined, country?: string | null | undefined } | null | undefined, creditCard?: { __typename?: 'CreditCard', creditCardType: string, creditCardNumber: string, cardHolder: string, streetAddress: string, postalCode: string, elementPaymentAccountId: string, expirationDate: { __typename?: 'DateWrapper', date: string } } | null | undefined, branchInfo?: { __typename?: 'BranchOrderInfo', branchName?: string | null | undefined, streetLineOne?: string | null | undefined, streetLineTwo?: string | null | undefined, streetLineThree?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, postalCode?: string | null | undefined, country?: string | null | undefined } | null | undefined, lineItems: Array<{ __typename?: 'OrderLineItem', unitPrice?: number | null | undefined, erpPartNumber?: string | null | undefined, orderQuantity?: number | null | undefined, shipQuantity?: number | null | undefined, uom?: string | null | undefined, productOrderTotal?: number | null | undefined, manufacturerName?: string | null | undefined, manufacturerNumber?: string | null | undefined, productName?: string | null | undefined, productId?: string | null | undefined, imageUrls?: { __typename?: 'ImageUrls', thumb?: string | null | undefined, small?: string | null | undefined, medium?: string | null | undefined, large?: string | null | undefined } | null | undefined }> } };

export type DeleteCreditCardFromCartMutationVariables = Exact<{
  cartId: Scalars['ID'];
}>;


export type DeleteCreditCardFromCartMutation = { __typename?: 'Mutation', deleteCreditCardFromCart: { __typename?: 'Cart', id: string, ownerId?: string | null | undefined, approverId?: string | null | undefined, shipToId?: string | null | undefined, poNumber?: string | null | undefined, pricingBranchId?: string | null | undefined, shippingBranchId?: string | null | undefined, paymentMethodType?: PaymentMethodTypeEnum | null | undefined, approvalState?: string | null | undefined, rejectionReason?: string | null | undefined, removedProducts?: Array<string> | null | undefined, subtotal?: number | null | undefined, tax?: number | null | undefined, shippingHandling?: number | null | undefined, total?: number | null | undefined, deliveryMethod?: DeliveryMethodEnum | null | undefined, erpSystemName: ErpSystemEnum, creditCard?: { __typename?: 'CreditCard', creditCardType: string, creditCardNumber: string, cardHolder: string, streetAddress: string, postalCode: string, elementPaymentAccountId: string, expirationDate: { __typename?: 'DateWrapper', date: string } } | null | undefined, delivery: { __typename?: 'Delivery', id: string, shipToId?: string | null | undefined, deliveryInstructions?: string | null | undefined, preferredDate?: any | null | undefined, preferredTime?: PreferredTimeEnum | null | undefined, shouldShipFullOrder: boolean, phoneNumber?: string | null | undefined, address?: { __typename?: 'Address', id: string, companyName?: string | null | undefined, street1?: string | null | undefined, street2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, country?: string | null | undefined, custom: boolean } | null | undefined }, willCall: { __typename?: 'WillCall', id: string, preferredDate?: any | null | undefined, preferredTime?: PreferredTimeEnum | null | undefined, branchId?: string | null | undefined, pickupInstructions?: string | null | undefined }, products?: Array<{ __typename?: 'LineItem', id?: string | null | undefined, cartId?: string | null | undefined, customerPartNumber?: string | null | undefined, pricePerUnit?: number | null | undefined, uom?: string | null | undefined, priceLastUpdatedAt?: any | null | undefined, erpPartNumber?: string | null | undefined, quantity?: number | null | undefined, qtyAvailableLastUpdatedAt?: any | null | undefined, qtyAvailable?: number | null | undefined, listIds?: Array<string> | null | undefined, product?: { __typename?: 'Product', id: string, partNumber?: string | null | undefined, name?: string | null | undefined, manufacturerName?: string | null | undefined, manufacturerNumber?: string | null | undefined, price?: number | null | undefined, cmp?: number | null | undefined, taxonomy?: Array<string> | null | undefined, categories?: Array<string> | null | undefined, environmentalOptions?: Array<string> | null | undefined, upc?: string | null | undefined, unspsc?: string | null | undefined, seriesModelFigureNumber?: string | null | undefined, productOverview?: string | null | undefined, featuresAndBenefits?: string | null | undefined, minIncrementQty?: number | null | undefined, erp?: string | null | undefined, customerPartNumber?: Array<string> | null | undefined, stock?: { __typename?: 'Stock', homeBranch?: { __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined } | null | undefined, otherBranches?: Array<{ __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined }> | null | undefined } | null | undefined, technicalDocuments?: Array<{ __typename?: 'TechDoc', name?: string | null | undefined, url?: string | null | undefined }> | null | undefined, techSpecifications?: Array<{ __typename?: 'TechSpec', name?: string | null | undefined, value?: string | null | undefined }> | null | undefined, imageUrls?: { __typename?: 'ImageUrls', thumb?: string | null | undefined, small?: string | null | undefined, medium?: string | null | undefined, large?: string | null | undefined } | null | undefined, packageDimensions?: { __typename?: 'PackageDimensions', height?: number | null | undefined, weight?: number | null | undefined, length?: number | null | undefined, volume?: number | null | undefined, width?: number | null | undefined, volumeUnitOfMeasure?: string | null | undefined, weightUnitOfMeasure?: string | null | undefined } | null | undefined } | null | undefined }> | null | undefined } };

export type BranchOrderInfoFragment = { __typename?: 'BranchOrderInfo', branchName?: string | null | undefined, streetLineOne?: string | null | undefined, streetLineTwo?: string | null | undefined, streetLineThree?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, postalCode?: string | null | undefined, country?: string | null | undefined };

export type SubmitOrderResponseFragment = { __typename?: 'OrderResponse', orderNumber?: string | null | undefined, orderStatus?: string | null | undefined, webStatus?: string | null | undefined, shipDate?: string | null | undefined, customerPO?: string | null | undefined, orderDate?: string | null | undefined, amount?: string | null | undefined, invoiceNumber?: string | null | undefined, billToName?: string | null | undefined, shipToName?: string | null | undefined, orderedBy?: string | null | undefined, email?: string | null | undefined, deliveryMethod?: string | null | undefined, specialInstructions?: string | null | undefined, subTotal?: number | null | undefined, tax?: number | null | undefined, orderTotal?: number | null | undefined, shipAddress?: { __typename?: 'EclipseAddress', streetLineOne?: string | null | undefined, streetLineTwo?: string | null | undefined, streetLineThree?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, postalCode?: string | null | undefined, country?: string | null | undefined } | null | undefined, creditCard?: { __typename?: 'CreditCard', creditCardType: string, creditCardNumber: string, cardHolder: string, streetAddress: string, postalCode: string, elementPaymentAccountId: string, expirationDate: { __typename?: 'DateWrapper', date: string } } | null | undefined, branchInfo?: { __typename?: 'BranchOrderInfo', branchName?: string | null | undefined, streetLineOne?: string | null | undefined, streetLineTwo?: string | null | undefined, streetLineThree?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, postalCode?: string | null | undefined, country?: string | null | undefined } | null | undefined, lineItems: Array<{ __typename?: 'OrderLineItem', unitPrice?: number | null | undefined, erpPartNumber?: string | null | undefined, orderQuantity?: number | null | undefined, shipQuantity?: number | null | undefined, uom?: string | null | undefined, productOrderTotal?: number | null | undefined, manufacturerName?: string | null | undefined, manufacturerNumber?: string | null | undefined, productName?: string | null | undefined, productId?: string | null | undefined, imageUrls?: { __typename?: 'ImageUrls', thumb?: string | null | undefined, small?: string | null | undefined, medium?: string | null | undefined, large?: string | null | undefined } | null | undefined }> };

export type UpdateItemQtyResponseFragment = { __typename?: 'UpdateItemQtyResponse', subtotal?: number | null | undefined, product: { __typename?: 'LineItem', id?: string | null | undefined, cartId?: string | null | undefined, customerPartNumber?: string | null | undefined, pricePerUnit?: number | null | undefined, uom?: string | null | undefined, priceLastUpdatedAt?: any | null | undefined, erpPartNumber?: string | null | undefined, quantity?: number | null | undefined, qtyAvailableLastUpdatedAt?: any | null | undefined, qtyAvailable?: number | null | undefined, listIds?: Array<string> | null | undefined, product?: { __typename?: 'Product', id: string, partNumber?: string | null | undefined, name?: string | null | undefined, manufacturerName?: string | null | undefined, manufacturerNumber?: string | null | undefined, price?: number | null | undefined, cmp?: number | null | undefined, taxonomy?: Array<string> | null | undefined, categories?: Array<string> | null | undefined, environmentalOptions?: Array<string> | null | undefined, upc?: string | null | undefined, unspsc?: string | null | undefined, seriesModelFigureNumber?: string | null | undefined, productOverview?: string | null | undefined, featuresAndBenefits?: string | null | undefined, minIncrementQty?: number | null | undefined, erp?: string | null | undefined, customerPartNumber?: Array<string> | null | undefined, stock?: { __typename?: 'Stock', homeBranch?: { __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined } | null | undefined, otherBranches?: Array<{ __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined }> | null | undefined } | null | undefined, technicalDocuments?: Array<{ __typename?: 'TechDoc', name?: string | null | undefined, url?: string | null | undefined }> | null | undefined, techSpecifications?: Array<{ __typename?: 'TechSpec', name?: string | null | undefined, value?: string | null | undefined }> | null | undefined, imageUrls?: { __typename?: 'ImageUrls', thumb?: string | null | undefined, small?: string | null | undefined, medium?: string | null | undefined, large?: string | null | undefined } | null | undefined, packageDimensions?: { __typename?: 'PackageDimensions', height?: number | null | undefined, weight?: number | null | undefined, length?: number | null | undefined, volume?: number | null | undefined, width?: number | null | undefined, volumeUnitOfMeasure?: string | null | undefined, weightUnitOfMeasure?: string | null | undefined } | null | undefined } | null | undefined } };

export type CartFragment = { __typename?: 'Cart', id: string, ownerId?: string | null | undefined, approverId?: string | null | undefined, shipToId?: string | null | undefined, poNumber?: string | null | undefined, pricingBranchId?: string | null | undefined, shippingBranchId?: string | null | undefined, paymentMethodType?: PaymentMethodTypeEnum | null | undefined, approvalState?: string | null | undefined, rejectionReason?: string | null | undefined, removedProducts?: Array<string> | null | undefined, subtotal?: number | null | undefined, tax?: number | null | undefined, shippingHandling?: number | null | undefined, total?: number | null | undefined, deliveryMethod?: DeliveryMethodEnum | null | undefined, erpSystemName: ErpSystemEnum, creditCard?: { __typename?: 'CreditCard', creditCardType: string, creditCardNumber: string, cardHolder: string, streetAddress: string, postalCode: string, elementPaymentAccountId: string, expirationDate: { __typename?: 'DateWrapper', date: string } } | null | undefined, delivery: { __typename?: 'Delivery', id: string, shipToId?: string | null | undefined, deliveryInstructions?: string | null | undefined, preferredDate?: any | null | undefined, preferredTime?: PreferredTimeEnum | null | undefined, shouldShipFullOrder: boolean, phoneNumber?: string | null | undefined, address?: { __typename?: 'Address', id: string, companyName?: string | null | undefined, street1?: string | null | undefined, street2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, country?: string | null | undefined, custom: boolean } | null | undefined }, willCall: { __typename?: 'WillCall', id: string, preferredDate?: any | null | undefined, preferredTime?: PreferredTimeEnum | null | undefined, branchId?: string | null | undefined, pickupInstructions?: string | null | undefined }, products?: Array<{ __typename?: 'LineItem', id?: string | null | undefined, cartId?: string | null | undefined, customerPartNumber?: string | null | undefined, pricePerUnit?: number | null | undefined, uom?: string | null | undefined, priceLastUpdatedAt?: any | null | undefined, erpPartNumber?: string | null | undefined, quantity?: number | null | undefined, qtyAvailableLastUpdatedAt?: any | null | undefined, qtyAvailable?: number | null | undefined, listIds?: Array<string> | null | undefined, product?: { __typename?: 'Product', id: string, partNumber?: string | null | undefined, name?: string | null | undefined, manufacturerName?: string | null | undefined, manufacturerNumber?: string | null | undefined, price?: number | null | undefined, cmp?: number | null | undefined, taxonomy?: Array<string> | null | undefined, categories?: Array<string> | null | undefined, environmentalOptions?: Array<string> | null | undefined, upc?: string | null | undefined, unspsc?: string | null | undefined, seriesModelFigureNumber?: string | null | undefined, productOverview?: string | null | undefined, featuresAndBenefits?: string | null | undefined, minIncrementQty?: number | null | undefined, erp?: string | null | undefined, customerPartNumber?: Array<string> | null | undefined, stock?: { __typename?: 'Stock', homeBranch?: { __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined } | null | undefined, otherBranches?: Array<{ __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined }> | null | undefined } | null | undefined, technicalDocuments?: Array<{ __typename?: 'TechDoc', name?: string | null | undefined, url?: string | null | undefined }> | null | undefined, techSpecifications?: Array<{ __typename?: 'TechSpec', name?: string | null | undefined, value?: string | null | undefined }> | null | undefined, imageUrls?: { __typename?: 'ImageUrls', thumb?: string | null | undefined, small?: string | null | undefined, medium?: string | null | undefined, large?: string | null | undefined } | null | undefined, packageDimensions?: { __typename?: 'PackageDimensions', height?: number | null | undefined, weight?: number | null | undefined, length?: number | null | undefined, volume?: number | null | undefined, width?: number | null | undefined, volumeUnitOfMeasure?: string | null | undefined, weightUnitOfMeasure?: string | null | undefined } | null | undefined } | null | undefined }> | null | undefined };

export type ErpAccountFragment = { __typename?: 'ErpAccount', alwaysCod?: boolean | null | undefined, branchId?: string | null | undefined, city?: string | null | undefined, companyName?: string | null | undefined, email?: Array<string> | null | undefined, erpAccountId?: string | null | undefined, erp?: ErpSystemEnum | null | undefined, erpName?: string | null | undefined, phoneNumber?: string | null | undefined, state?: string | null | undefined, street1?: string | null | undefined, street2?: string | null | undefined, zip?: string | null | undefined, poReleaseRequired?: string | null | undefined, creditHold?: boolean | null | undefined, territory?: string | null | undefined };

export type CategoriesQueryVariables = Exact<{
  erp?: InputMaybe<Scalars['String']>;
}>;


export type CategoriesQuery = { __typename?: 'Query', categories: { __typename?: 'Categories', mincronCategories?: string | null | undefined, eclipseCategories?: string | null | undefined } };

export type ProductCategoriesQueryVariables = Exact<{
  engine: Scalars['String'];
}>;


export type ProductCategoriesQuery = { __typename?: 'Query', productCategories?: { __typename?: 'ProductCategories', categories?: Array<{ __typename?: 'Category', name: string, children?: Array<{ __typename?: 'Category', name: string, children?: Array<{ __typename?: 'Category', name: string }> | null | undefined }> | null | undefined }> | null | undefined } | null | undefined };

export type SubmitContractOrderReviewQueryVariables = Exact<{
  orderReview?: InputMaybe<OrderReviewInput>;
}>;


export type SubmitContractOrderReviewQuery = { __typename?: 'Query', submitContractOrderReview: { __typename?: 'ContractOrderDetail', taxAmount?: string | null | undefined, totalAmount?: string | null | undefined, subTotal?: string | null | undefined, otherCharges?: string | null | undefined, orderType?: string | null | undefined, gstHstUseCode?: string | null | undefined, promiseDate?: string | null | undefined, orderNumber?: string | null | undefined, orderComments?: Array<string> | null | undefined, subTotalWithBackOrder?: string | null | undefined, trackingURL?: string | null | undefined, creditCardMessage?: string | null | undefined, orderBy?: string | null | undefined, gstHstAmount?: string | null | undefined, orderDescription?: string | null | undefined, enteredBy?: string | null | undefined, shipmentMethod?: string | null | undefined, paidStatus?: string | null | undefined, shipVia?: string | null | undefined, allowCreditCardFlag?: string | null | undefined, merchantId?: string | null | undefined, creditCardTypes?: Array<string> | null | undefined, invoiceNumber?: string | null | undefined, freightCode?: string | null | undefined, trackingNumber?: string | null | undefined, jobName?: string | null | undefined, taxable?: string | null | undefined, branchNumber?: string | null | undefined, editable?: string | null | undefined, contractNumber?: string | null | undefined, branchName?: string | null | undefined, shipDate?: string | null | undefined, shipHandleAmount?: string | null | undefined, paidByCC?: string | null | undefined, customerName?: string | null | undefined, phoneNumber?: string | null | undefined, creditCardAuthAmount?: string | null | undefined, specialInstructions?: Array<string> | null | undefined, purchaseOrderNumber?: string | null | undefined, shipmentMethodSysConfig?: string | null | undefined, orderQuoteCreditDebitCode?: string | null | undefined, orderDate?: string | null | undefined, jobNumber?: string | null | undefined, status?: string | null | undefined, shoppingCartId?: string | null | undefined, shipToAddress?: { __typename?: 'ContractAddress', entityId?: string | null | undefined, branchNumber?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, address3?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, county?: string | null | undefined, country?: string | null | undefined, taxJurisdiction?: string | null | undefined } | null | undefined, itemList?: { __typename?: 'ItemPage', items: Array<{ __typename?: 'ContractCartItem', orderNumber?: string | null | undefined, displayOnly?: string | null | undefined, productNumber?: string | null | undefined, description?: string | null | undefined, uom?: string | null | undefined, lineComments?: string | null | undefined, quantityOrdered?: string | null | undefined, quantityReleasedToDate?: string | null | undefined, quantityBackOrdered?: string | null | undefined, quantityShipped?: string | null | undefined, unitPrice?: string | null | undefined, netPrice?: string | null | undefined, extendedPrice?: string | null | undefined, lineNumber?: string | null | undefined, orderLineItemTypeCode?: string | null | undefined }> } | null | undefined } };

export type ContractOrderDetailFragment = { __typename?: 'ContractOrderDetail', taxAmount?: string | null | undefined, totalAmount?: string | null | undefined, subTotal?: string | null | undefined, otherCharges?: string | null | undefined, orderType?: string | null | undefined, gstHstUseCode?: string | null | undefined, promiseDate?: string | null | undefined, orderNumber?: string | null | undefined, orderComments?: Array<string> | null | undefined, subTotalWithBackOrder?: string | null | undefined, trackingURL?: string | null | undefined, creditCardMessage?: string | null | undefined, orderBy?: string | null | undefined, gstHstAmount?: string | null | undefined, orderDescription?: string | null | undefined, enteredBy?: string | null | undefined, shipmentMethod?: string | null | undefined, paidStatus?: string | null | undefined, shipVia?: string | null | undefined, allowCreditCardFlag?: string | null | undefined, merchantId?: string | null | undefined, creditCardTypes?: Array<string> | null | undefined, invoiceNumber?: string | null | undefined, freightCode?: string | null | undefined, trackingNumber?: string | null | undefined, jobName?: string | null | undefined, taxable?: string | null | undefined, branchNumber?: string | null | undefined, editable?: string | null | undefined, contractNumber?: string | null | undefined, branchName?: string | null | undefined, shipDate?: string | null | undefined, shipHandleAmount?: string | null | undefined, paidByCC?: string | null | undefined, customerName?: string | null | undefined, phoneNumber?: string | null | undefined, creditCardAuthAmount?: string | null | undefined, specialInstructions?: Array<string> | null | undefined, purchaseOrderNumber?: string | null | undefined, shipmentMethodSysConfig?: string | null | undefined, orderQuoteCreditDebitCode?: string | null | undefined, orderDate?: string | null | undefined, jobNumber?: string | null | undefined, status?: string | null | undefined, shoppingCartId?: string | null | undefined, shipToAddress?: { __typename?: 'ContractAddress', entityId?: string | null | undefined, branchNumber?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, address3?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, county?: string | null | undefined, country?: string | null | undefined, taxJurisdiction?: string | null | undefined } | null | undefined, itemList?: { __typename?: 'ItemPage', items: Array<{ __typename?: 'ContractCartItem', orderNumber?: string | null | undefined, displayOnly?: string | null | undefined, productNumber?: string | null | undefined, description?: string | null | undefined, uom?: string | null | undefined, lineComments?: string | null | undefined, quantityOrdered?: string | null | undefined, quantityReleasedToDate?: string | null | undefined, quantityBackOrdered?: string | null | undefined, quantityShipped?: string | null | undefined, unitPrice?: string | null | undefined, netPrice?: string | null | undefined, extendedPrice?: string | null | undefined, lineNumber?: string | null | undefined, orderLineItemTypeCode?: string | null | undefined }> } | null | undefined };

export type ItemPageFragment = { __typename?: 'ItemPage', items: Array<{ __typename?: 'ContractCartItem', orderNumber?: string | null | undefined, displayOnly?: string | null | undefined, productNumber?: string | null | undefined, description?: string | null | undefined, uom?: string | null | undefined, lineComments?: string | null | undefined, quantityOrdered?: string | null | undefined, quantityReleasedToDate?: string | null | undefined, quantityBackOrdered?: string | null | undefined, quantityShipped?: string | null | undefined, unitPrice?: string | null | undefined, netPrice?: string | null | undefined, extendedPrice?: string | null | undefined, lineNumber?: string | null | undefined, orderLineItemTypeCode?: string | null | undefined }> };

export type DeleteContractCartMutationVariables = Exact<{
  application: Scalars['String'];
  accountId: Scalars['String'];
  userId: Scalars['String'];
  shoppingCartId: Scalars['String'];
  branchNumber: Scalars['String'];
}>;


export type DeleteContractCartMutation = { __typename?: 'Mutation', deleteContractCart: string };

export type SubmitContractOrderFromCartQueryVariables = Exact<{
  contractOrderSubmit?: InputMaybe<ContractOrderSubmitInput>;
  application: Scalars['String'];
  accountId: Scalars['String'];
  userId: Scalars['String'];
  shoppingCartId: Scalars['String'];
}>;


export type SubmitContractOrderFromCartQuery = { __typename?: 'Query', submitContractOrderFromCart: string };

export type ContractDetailsFragment = { __typename?: 'ContractDetails', contractNumber?: string | null | undefined, contractDescription?: string | null | undefined, jobName?: string | null | undefined, purchaseOrderNumber?: string | null | undefined, accountInformation?: { __typename?: 'AccountInformation', shipToAddress?: { __typename?: 'ContractAddress', entityId?: string | null | undefined, branchNumber?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, address3?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, county?: string | null | undefined, country?: string | null | undefined, taxJurisdiction?: string | null | undefined } | null | undefined, branch?: { __typename?: 'ContractAddress', entityId?: string | null | undefined, branchNumber?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, address3?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, county?: string | null | undefined, country?: string | null | undefined, taxJurisdiction?: string | null | undefined } | null | undefined } | null | undefined, contractDates?: { __typename?: 'ContractDates', promisedDate?: string | null | undefined, contractDate?: string | null | undefined, firstReleaseDate?: string | null | undefined, lastReleaseDate?: string | null | undefined } | null | undefined, customerInfo?: { __typename?: 'CustomerInfo', customerNumber?: string | null | undefined, jobNumber?: string | null | undefined, enteredBy?: string | null | undefined } | null | undefined, contractSummary?: { __typename?: 'ContractSummary', subTotal?: string | null | undefined, taxAmount?: string | null | undefined, otherCharges?: string | null | undefined, invoicedToDateAmount?: string | null | undefined, totalAmount?: string | null | undefined, firstShipmentDate?: string | null | undefined, lastShipmentDate?: string | null | undefined } | null | undefined, contractProducts?: Array<{ __typename?: 'ContractProduct', id?: string | null | undefined, brand?: string | null | undefined, name?: string | null | undefined, lineComments?: string | null | undefined, partNumber?: string | null | undefined, sequenceNumber?: string | null | undefined, mfr?: string | null | undefined, thumb?: string | null | undefined, netPrice?: number | null | undefined, unitPrice?: number | null | undefined, pricingUom?: string | null | undefined, displayOnly?: boolean | null | undefined, qty?: { __typename?: 'ContractProductQty', quantityOrdered?: number | null | undefined, quantityReleasedToDate?: number | null | undefined, quantityShipped?: number | null | undefined } | null | undefined, technicalDocuments?: Array<{ __typename?: 'TechDoc', name?: string | null | undefined, url?: string | null | undefined }> | null | undefined, techSpecifications?: Array<{ __typename?: 'TechSpec', name?: string | null | undefined, value?: string | null | undefined }> | null | undefined }> | null | undefined };

export type AccountInformationFragment = { __typename?: 'AccountInformation', shipToAddress?: { __typename?: 'ContractAddress', entityId?: string | null | undefined, branchNumber?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, address3?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, county?: string | null | undefined, country?: string | null | undefined, taxJurisdiction?: string | null | undefined } | null | undefined, branch?: { __typename?: 'ContractAddress', entityId?: string | null | undefined, branchNumber?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, address3?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, county?: string | null | undefined, country?: string | null | undefined, taxJurisdiction?: string | null | undefined } | null | undefined };

export type ContractAddressFragment = { __typename?: 'ContractAddress', entityId?: string | null | undefined, branchNumber?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, address3?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, county?: string | null | undefined, country?: string | null | undefined, taxJurisdiction?: string | null | undefined };

export type ContractSummaryFragment = { __typename?: 'ContractSummary', subTotal?: string | null | undefined, taxAmount?: string | null | undefined, otherCharges?: string | null | undefined, invoicedToDateAmount?: string | null | undefined, totalAmount?: string | null | undefined, firstShipmentDate?: string | null | undefined, lastShipmentDate?: string | null | undefined };

export type ContractDatesFragment = { __typename?: 'ContractDates', promisedDate?: string | null | undefined, contractDate?: string | null | undefined, firstReleaseDate?: string | null | undefined, lastReleaseDate?: string | null | undefined };

export type CustomerInfoFragment = { __typename?: 'CustomerInfo', customerNumber?: string | null | undefined, jobNumber?: string | null | undefined, enteredBy?: string | null | undefined };

export type ContractProductFragment = { __typename?: 'ContractProduct', id?: string | null | undefined, brand?: string | null | undefined, name?: string | null | undefined, lineComments?: string | null | undefined, partNumber?: string | null | undefined, sequenceNumber?: string | null | undefined, mfr?: string | null | undefined, thumb?: string | null | undefined, netPrice?: number | null | undefined, unitPrice?: number | null | undefined, pricingUom?: string | null | undefined, displayOnly?: boolean | null | undefined, qty?: { __typename?: 'ContractProductQty', quantityOrdered?: number | null | undefined, quantityReleasedToDate?: number | null | undefined, quantityShipped?: number | null | undefined } | null | undefined, technicalDocuments?: Array<{ __typename?: 'TechDoc', name?: string | null | undefined, url?: string | null | undefined }> | null | undefined, techSpecifications?: Array<{ __typename?: 'TechSpec', name?: string | null | undefined, value?: string | null | undefined }> | null | undefined };

export type GetContractDetailsQueryVariables = Exact<{
  contractNumber: Scalars['String'];
  erpAccountId: Scalars['String'];
}>;


export type GetContractDetailsQuery = { __typename?: 'Query', contract: { __typename?: 'ContractDetails', contractNumber?: string | null | undefined, contractDescription?: string | null | undefined, jobName?: string | null | undefined, purchaseOrderNumber?: string | null | undefined, accountInformation?: { __typename?: 'AccountInformation', shipToAddress?: { __typename?: 'ContractAddress', entityId?: string | null | undefined, branchNumber?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, address3?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, county?: string | null | undefined, country?: string | null | undefined, taxJurisdiction?: string | null | undefined } | null | undefined, branch?: { __typename?: 'ContractAddress', entityId?: string | null | undefined, branchNumber?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, address3?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, county?: string | null | undefined, country?: string | null | undefined, taxJurisdiction?: string | null | undefined } | null | undefined } | null | undefined, contractDates?: { __typename?: 'ContractDates', promisedDate?: string | null | undefined, contractDate?: string | null | undefined, firstReleaseDate?: string | null | undefined, lastReleaseDate?: string | null | undefined } | null | undefined, customerInfo?: { __typename?: 'CustomerInfo', customerNumber?: string | null | undefined, jobNumber?: string | null | undefined, enteredBy?: string | null | undefined } | null | undefined, contractSummary?: { __typename?: 'ContractSummary', subTotal?: string | null | undefined, taxAmount?: string | null | undefined, otherCharges?: string | null | undefined, invoicedToDateAmount?: string | null | undefined, totalAmount?: string | null | undefined, firstShipmentDate?: string | null | undefined, lastShipmentDate?: string | null | undefined } | null | undefined, contractProducts?: Array<{ __typename?: 'ContractProduct', id?: string | null | undefined, brand?: string | null | undefined, name?: string | null | undefined, lineComments?: string | null | undefined, partNumber?: string | null | undefined, sequenceNumber?: string | null | undefined, mfr?: string | null | undefined, thumb?: string | null | undefined, netPrice?: number | null | undefined, unitPrice?: number | null | undefined, pricingUom?: string | null | undefined, displayOnly?: boolean | null | undefined, qty?: { __typename?: 'ContractProductQty', quantityOrdered?: number | null | undefined, quantityReleasedToDate?: number | null | undefined, quantityShipped?: number | null | undefined } | null | undefined, technicalDocuments?: Array<{ __typename?: 'TechDoc', name?: string | null | undefined, url?: string | null | undefined }> | null | undefined, techSpecifications?: Array<{ __typename?: 'TechSpec', name?: string | null | undefined, value?: string | null | undefined }> | null | undefined }> | null | undefined } };

export type ContractsQueryVariables = Exact<{
  erpAccountId: Scalars['String'];
  pageNumber: Scalars['String'];
  searchFilter?: InputMaybe<Scalars['String']>;
  fromDate?: InputMaybe<Scalars['String']>;
  toDate?: InputMaybe<Scalars['String']>;
  sortOrder?: InputMaybe<Scalars['String']>;
  sortDirection?: InputMaybe<Scalars['String']>;
}>;


export type ContractsQuery = { __typename?: 'Query', contracts: { __typename?: 'Page', rowsReturned: number, totalRows: number, startRow: number, results: Array<{ __typename?: 'Contract', contractNumber?: string | null | undefined, description?: string | null | undefined, contractDate?: string | null | undefined, promiseDate?: string | null | undefined, firstReleaseDate?: string | null | undefined, lastReleaseDate?: string | null | undefined, jobNumber?: string | null | undefined, jobName?: string | null | undefined, purchaseOrderNumber?: string | null | undefined }> } };

export type CreditCardSetupUrlQueryVariables = Exact<{
  accountId: Scalars['String'];
  cardHolderInput: CardHolderInput;
}>;


export type CreditCardSetupUrlQuery = { __typename?: 'Query', creditCardSetupUrl: { __typename?: 'CreditCardSetupUrl', elementSetupUrl: string, elementSetupId: string } };

export type CreditCardElementInfoQueryVariables = Exact<{
  accountId: Scalars['String'];
  elementSetupId: Scalars['String'];
}>;


export type CreditCardElementInfoQuery = { __typename?: 'Query', creditCardElementInfo: { __typename?: 'CreditCardElementInfoResponse', creditCard: { __typename?: 'CreditCard', creditCardType: string, creditCardNumber: string, cardHolder: string, streetAddress: string, postalCode: string, elementPaymentAccountId: string, expirationDate: { __typename?: 'DateWrapper', date: string } } } };

export type CreditCardListQueryVariables = Exact<{
  accountId: Scalars['String'];
}>;


export type CreditCardListQuery = { __typename?: 'Query', creditCardList?: { __typename?: 'CreditCardListResponse', creditCardList?: { __typename?: 'CreditCardList', creditCard?: Array<{ __typename?: 'CreditCard', creditCardType: string, creditCardNumber: string, cardHolder: string, streetAddress: string, postalCode: string, elementPaymentAccountId: string, expirationDate: { __typename?: 'DateWrapper', date: string } }> | null | undefined } | null | undefined } | null | undefined };

export type AddCreditCardMutationVariables = Exact<{
  accountId: Scalars['String'];
  creditCard: CreditCardInput;
}>;


export type AddCreditCardMutation = { __typename?: 'Mutation', addCreditCard: { __typename?: 'CreditCardResponse', statusResult: { __typename?: 'StatusResult', success: string, description: string }, creditCardList: { __typename?: 'CreditCardList', creditCard?: Array<{ __typename?: 'CreditCard', creditCardType: string, creditCardNumber: string, cardHolder: string, streetAddress: string, postalCode: string, elementPaymentAccountId: string, expirationDate: { __typename?: 'DateWrapper', date: string } }> | null | undefined } } };

export type DeleteCreditCardMutationVariables = Exact<{
  accountId: Scalars['String'];
  creditCardId: Scalars['ID'];
}>;


export type DeleteCreditCardMutation = { __typename?: 'Mutation', deleteCreditCard: string };

export type CreditCardFragment = { __typename?: 'CreditCard', creditCardType: string, creditCardNumber: string, cardHolder: string, streetAddress: string, postalCode: string, elementPaymentAccountId: string, expirationDate: { __typename?: 'DateWrapper', date: string } };

export type GetAllUnapprovedAccountRequestsQueryVariables = Exact<{ [key: string]: never; }>;


export type GetAllUnapprovedAccountRequestsQuery = { __typename?: 'Query', allUnapprovedAccountRequests: Array<{ __typename?: 'User', id?: string | null | undefined, email?: string | null | undefined, firstName?: string | null | undefined, lastName?: string | null | undefined, branchId?: string | null | undefined, phoneNumber?: string | null | undefined, phoneType: PhoneType, createdAt?: string | null | undefined, companyName?: string | null | undefined, accountNumber?: string | null | undefined }> };

export type FeaturesQueryVariables = Exact<{ [key: string]: never; }>;


export type FeaturesQuery = { __typename?: 'Query', features?: Array<{ __typename?: 'Feature', id: string, name: string, isEnabled: boolean }> | null | undefined };

export type SetFeatureEnabledMutationVariables = Exact<{
  featureId: Scalars['String'];
  setFeatureEnabledInput: SetFeatureEnabledInput;
}>;


export type SetFeatureEnabledMutation = { __typename?: 'Mutation', setFeatureEnabled: boolean };

export type AccountErpIdQueryVariables = Exact<{
  accountId: Scalars['String'];
  brand: Scalars['String'];
}>;


export type AccountErpIdQuery = { __typename?: 'Query', account: Array<{ __typename?: 'ErpAccount', erpAccountId?: string | null | undefined }> };

export type InviteUserMutationVariables = Exact<{
  inviteUserInput: InviteUserInput;
}>;


export type InviteUserMutation = { __typename?: 'Mutation', inviteUser: { __typename?: 'InvitedUser', firstName: string, lastName: string, email: string, userRoleId: string, approverId?: string | null | undefined } };

export type GetInvoiceQueryVariables = Exact<{
  accountId: Scalars['String'];
  invoiceNumber: Scalars['String'];
}>;


export type GetInvoiceQuery = { __typename?: 'Query', invoice: { __typename?: 'Invoice', invoiceNumber: string, status: string, terms?: string | null | undefined, customerPo?: string | null | undefined, invoiceDate: string, dueDate: string, originalAmt: string, openBalance: string, jobNumber?: string | null | undefined, jobName?: string | null | undefined, shipDate?: string | null | undefined, deliveryMethod?: string | null | undefined, subtotal?: string | null | undefined, tax?: string | null | undefined, otherCharges?: string | null | undefined, paidToDate?: string | null | undefined, address?: { __typename?: 'EclipseAddress', streetLineOne?: string | null | undefined, streetLineTwo?: string | null | undefined, streetLineThree?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, postalCode?: string | null | undefined, country?: string | null | undefined } | null | undefined, invoiceItems?: Array<{ __typename?: 'InvoiceProduct', id?: string | null | undefined, brand?: string | null | undefined, name?: string | null | undefined, partNumber?: string | null | undefined, mfr?: string | null | undefined, thumb?: string | null | undefined, price?: string | null | undefined, pricingUom?: string | null | undefined, qty?: { __typename?: 'InvoiceProductQty', quantityOrdered?: number | null | undefined, quantityShipped?: number | null | undefined } | null | undefined }> | null | undefined } };

export type InvoicesQueryVariables = Exact<{
  accountId: Scalars['String'];
  erpName: Scalars['String'];
}>;


export type InvoicesQuery = { __typename?: 'Query', invoices: { __typename?: 'AccountInvoice', bucketThirty: number, bucketSixty: number, bucketNinety: number, bucketOneTwenty: number, bucketFuture: number, currentAmt: number, totalAmt: number, totalPastDue: number, invoices: Array<{ __typename?: 'Invoice', invoiceNumber: string, status: string, customerPo?: string | null | undefined, invoiceDate: string, originalAmt: string, openBalance: string, age?: string | null | undefined, jobNumber?: string | null | undefined, jobName?: string | null | undefined, invoiceUrl?: string | null | undefined }> } };

export type InvoicesUrlQueryVariables = Exact<{
  accountId: Scalars['String'];
  invoiceNumbers: Array<Scalars['String']> | Scalars['String'];
}>;


export type InvoicesUrlQuery = { __typename?: 'Query', invoicesUrl: string };

export type CreateJobFormMutationVariables = Exact<{
  jobFormInput: JobFormInput;
  file?: InputMaybe<Scalars['Upload']>;
}>;


export type CreateJobFormMutation = { __typename?: 'Mutation', createJobForm: string };

export type EntitySearchQueryVariables = Exact<{
  accountId: Scalars['String'];
}>;


export type EntitySearchQuery = { __typename?: 'Query', entitySearch?: { __typename?: 'AccountLookUp', isBillTo?: boolean | null | undefined, erpName?: string | null | undefined, branchId?: string | null | undefined, companyName?: string | null | undefined, erpAccountId?: string | null | undefined } | null | undefined };

export type GetListQueryVariables = Exact<{
  listId: Scalars['ID'];
  userId: Scalars['ID'];
  shipToAccountId: Scalars['ID'];
  branchId: Scalars['ID'];
  erpAccountId?: InputMaybe<Scalars['ID']>;
}>;


export type GetListQuery = { __typename?: 'Query', list: { __typename?: 'List', id: string, name: string, billToAccountId: string, listLineItems: Array<{ __typename?: 'ListLineItem', id: string, erpPartNumber?: string | null | undefined, quantity?: number | null | undefined, minIncrementQty?: number | null | undefined, listId: string, sortOrder?: number | null | undefined, name?: string | null | undefined, manufacturerName?: string | null | undefined, manufacturerNumber?: string | null | undefined, status?: string | null | undefined, customerPartNumber?: Array<string> | null | undefined, imageUrls?: { __typename?: 'ImageUrls', thumb?: string | null | undefined } | null | undefined }> } };

export type GetListsQueryVariables = Exact<{
  billToAccountId: Scalars['ID'];
}>;


export type GetListsQuery = { __typename?: 'Query', lists: Array<{ __typename?: 'List', id: string, name: string, billToAccountId: string, listLineItemsSize?: number | null | undefined, listLineItems: Array<{ __typename?: 'ListLineItem', id: string, erpPartNumber?: string | null | undefined, sortOrder?: number | null | undefined, quantity?: number | null | undefined }> }> };

export type GetExportListIntoCsvQueryVariables = Exact<{
  listId: Scalars['ID'];
  userId: Scalars['ID'];
  shipToAccountId: Scalars['ID'];
  branchId: Scalars['ID'];
}>;


export type GetExportListIntoCsvQuery = { __typename?: 'Query', exportListToCSV: { __typename?: 'ExportListResponse', listLineItems: Array<{ __typename?: 'ExportListLineItem', partNumber: string, description?: string | null | undefined, mfrName?: string | null | undefined, quantity: number, price?: number | null | undefined, mfrNumber?: string | null | undefined }> } };

export type CreateListMutationVariables = Exact<{
  createListInput: CreateListInput;
}>;


export type CreateListMutation = { __typename?: 'Mutation', createList: { __typename?: 'List', id: string, name: string, billToAccountId: string, listLineItems: Array<{ __typename?: 'ListLineItem', id: string, erpPartNumber?: string | null | undefined, quantity?: number | null | undefined, minIncrementQty?: number | null | undefined, listId: string, sortOrder?: number | null | undefined, name?: string | null | undefined, manufacturerName?: string | null | undefined, manufacturerNumber?: string | null | undefined, status?: string | null | undefined, customerPartNumber?: Array<string> | null | undefined, imageUrls?: { __typename?: 'ImageUrls', thumb?: string | null | undefined } | null | undefined }> } };

export type DeleteListMutationVariables = Exact<{
  listId: Scalars['ID'];
}>;


export type DeleteListMutation = { __typename?: 'Mutation', deleteList: { __typename?: 'DeleteListResponse', id: string, success: boolean } };

export type UpdateListMutationVariables = Exact<{
  updateListInput: UpdateListInput;
}>;


export type UpdateListMutation = { __typename?: 'Mutation', updateList: { __typename?: 'List', id: string, name: string, billToAccountId: string, listLineItems: Array<{ __typename?: 'ListLineItem', id: string, erpPartNumber?: string | null | undefined, quantity?: number | null | undefined, minIncrementQty?: number | null | undefined, listId: string, sortOrder?: number | null | undefined, name?: string | null | undefined, manufacturerName?: string | null | undefined, manufacturerNumber?: string | null | undefined, status?: string | null | undefined, customerPartNumber?: Array<string> | null | undefined, imageUrls?: { __typename?: 'ImageUrls', thumb?: string | null | undefined } | null | undefined }> } };

export type UploadNewListMutationVariables = Exact<{
  file: Scalars['Upload'];
  name: Scalars['String'];
  billToAccountId: Scalars['ID'];
}>;


export type UploadNewListMutation = { __typename?: 'Mutation', uploadNewList?: { __typename?: 'ListUploadResponse', successfulRowCount: number, listId: string, errors: Array<{ __typename?: 'ListUploadError', partNumber?: string | null | undefined, description?: string | null | undefined, manufacturerName?: string | null | undefined, quantity?: number | null | undefined }> } | null | undefined };

export type UploadToListMutationVariables = Exact<{
  file: Scalars['Upload'];
  listId: Scalars['ID'];
}>;


export type UploadToListMutation = { __typename?: 'Mutation', uploadToList?: { __typename?: 'ListUploadResponse', successfulRowCount: number, listId: string, errors: Array<{ __typename?: 'ListUploadError', partNumber?: string | null | undefined, description?: string | null | undefined, manufacturerName?: string | null | undefined, quantity?: number | null | undefined }> } | null | undefined };

export type AddItemToListMutationVariables = Exact<{
  addItemToListInput: AddItemToListInput;
}>;


export type AddItemToListMutation = { __typename?: 'Mutation', addItemToList: string };

export type ToggleItemInListsMutationVariables = Exact<{
  toggleItemInListsInput: ToggleItemsInListInput;
}>;


export type ToggleItemInListsMutation = { __typename?: 'Mutation', toggleItemInLists?: { __typename?: 'ToggleItemsResponse', lists: Array<{ __typename?: 'List', id: string, name: string, billToAccountId: string, listLineItemsSize?: number | null | undefined, listLineItems: Array<{ __typename?: 'ListLineItem', id: string, erpPartNumber?: string | null | undefined, sortOrder?: number | null | undefined, quantity?: number | null | undefined }> }> } | null | undefined };

export type AddAllCartItemsToNewListMutationVariables = Exact<{
  accountId: Scalars['ID'];
  cartId: Scalars['ID'];
  name: Scalars['String'];
}>;


export type AddAllCartItemsToNewListMutation = { __typename?: 'Mutation', addAllCartItemsToNewList: string };

export type AddAllCartItemsToExistingListsMutationVariables = Exact<{
  listIds: Array<Scalars['String']> | Scalars['String'];
  cartId: Scalars['ID'];
}>;


export type AddAllCartItemsToExistingListsMutation = { __typename?: 'Mutation', addAllCartItemsToExistingLists: Array<string | null | undefined> };

export type BasicListFragment = { __typename?: 'List', id: string, name: string, billToAccountId: string, listLineItemsSize?: number | null | undefined, listLineItems: Array<{ __typename?: 'ListLineItem', id: string, erpPartNumber?: string | null | undefined, sortOrder?: number | null | undefined, quantity?: number | null | undefined }> };

export type ListFragment = { __typename?: 'List', id: string, name: string, billToAccountId: string, listLineItems: Array<{ __typename?: 'ListLineItem', id: string, erpPartNumber?: string | null | undefined, quantity?: number | null | undefined, minIncrementQty?: number | null | undefined, listId: string, sortOrder?: number | null | undefined, name?: string | null | undefined, manufacturerName?: string | null | undefined, manufacturerNumber?: string | null | undefined, status?: string | null | undefined, customerPartNumber?: Array<string> | null | undefined, imageUrls?: { __typename?: 'ImageUrls', thumb?: string | null | undefined } | null | undefined }> };

export type ListUploadResponseFragment = { __typename?: 'ListUploadResponse', successfulRowCount: number, listId: string, errors: Array<{ __typename?: 'ListUploadError', partNumber?: string | null | undefined, description?: string | null | undefined, manufacturerName?: string | null | undefined, quantity?: number | null | undefined }> };

export type ExportListResponseFragment = { __typename?: 'ExportListResponse', listLineItems: Array<{ __typename?: 'ExportListLineItem', partNumber: string, description?: string | null | undefined, mfrName?: string | null | undefined, quantity: number, price?: number | null | undefined, mfrNumber?: string | null | undefined }> };

export type BranchFragment = { __typename?: 'Branch', branchId: string, name?: string | null | undefined, entityId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distance?: number | null | undefined, erpSystemName?: ErpSystemEnum | null | undefined, website?: string | null | undefined, workdayId?: string | null | undefined, workdayLocationName?: string | null | undefined, actingBranchManager?: string | null | undefined, actingBranchManagerPhone?: string | null | undefined, actingBranchManagerEmail?: string | null | undefined, businessHours?: string | null | undefined, latitude: number, longitude: number, isPlumbing: boolean, isWaterworks: boolean, isHvac: boolean, isBandK: boolean, isPricingOnly: boolean };

export type GetBranchQueryVariables = Exact<{
  branchId: Scalars['ID'];
}>;


export type GetBranchQuery = { __typename?: 'Query', branch: { __typename?: 'Branch', branchId: string, name?: string | null | undefined, entityId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distance?: number | null | undefined, erpSystemName?: ErpSystemEnum | null | undefined, website?: string | null | undefined, workdayId?: string | null | undefined, workdayLocationName?: string | null | undefined, actingBranchManager?: string | null | undefined, actingBranchManagerPhone?: string | null | undefined, actingBranchManagerEmail?: string | null | undefined, businessHours?: string | null | undefined, latitude: number, longitude: number, isPlumbing: boolean, isWaterworks: boolean, isHvac: boolean, isBandK: boolean, isPricingOnly: boolean } };

export type FindBranchesQueryVariables = Exact<{
  branchSearch: BranchSearch;
}>;


export type FindBranchesQuery = { __typename?: 'Query', branchSearch: { __typename?: 'BranchSearchResult', latitude: number, longitude: number, branches: Array<{ __typename?: 'Branch', branchId: string, name?: string | null | undefined, entityId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distance?: number | null | undefined, erpSystemName?: ErpSystemEnum | null | undefined, website?: string | null | undefined, workdayId?: string | null | undefined, workdayLocationName?: string | null | undefined, actingBranchManager?: string | null | undefined, actingBranchManagerPhone?: string | null | undefined, actingBranchManagerEmail?: string | null | undefined, businessHours?: string | null | undefined, latitude: number, longitude: number, isPlumbing: boolean, isWaterworks: boolean, isHvac: boolean, isBandK: boolean, isPricingOnly: boolean }> } };

export type ResendLegacyInviteEmailMutationVariables = Exact<{
  legacyUserEmail: Scalars['String'];
}>;


export type ResendLegacyInviteEmailMutation = { __typename?: 'Mutation', resendLegacyInviteEmail: string };

export type InvitedUserEmailSentQueryVariables = Exact<{
  email: Scalars['String'];
}>;


export type InvitedUserEmailSentQuery = { __typename?: 'Query', invitedUserEmailSent: boolean };

export type GetOrderQueryVariables = Exact<{
  accountId: Scalars['ID'];
  orderId: Scalars['ID'];
  userId: Scalars['ID'];
  invoiceNumber?: InputMaybe<Scalars['ID']>;
  orderStatus?: InputMaybe<Scalars['String']>;
}>;


export type GetOrderQuery = { __typename?: 'Query', order: { __typename?: 'Order', amount?: string | null | undefined, shipToName?: string | null | undefined, customerPO?: string | null | undefined, deliveryMethod?: string | null | undefined, invoiceNumber?: string | null | undefined, orderDate?: string | null | undefined, orderNumber?: string | null | undefined, orderStatus?: string | null | undefined, orderTotal?: number | null | undefined, orderedBy?: string | null | undefined, shipDate?: string | null | undefined, specialInstructions?: string | null | undefined, subTotal?: number | null | undefined, tax?: number | null | undefined, webStatus?: string | null | undefined, lineItems?: Array<{ __typename?: 'OrderLineItem', uom?: string | null | undefined, pricingUom?: string | null | undefined, manufacturerName?: string | null | undefined, manufacturerNumber?: string | null | undefined, erpPartNumber?: string | null | undefined, orderQuantity?: number | null | undefined, backOrderedQuantity?: number | null | undefined, productId?: string | null | undefined, productName?: string | null | undefined, productOrderTotal?: number | null | undefined, shipQuantity?: number | null | undefined, status?: string | null | undefined, unitPrice?: number | null | undefined, lineNumber?: string | null | undefined, lineComments?: string | null | undefined, imageUrls?: { __typename?: 'ImageUrls', thumb?: string | null | undefined } | null | undefined }> | null | undefined, creditCard?: { __typename?: 'CreditCard', creditCardType: string, creditCardNumber: string, cardHolder: string, streetAddress: string, postalCode: string, elementPaymentAccountId: string, expirationDate: { __typename?: 'DateWrapper', date: string } } | null | undefined, shipAddress?: { __typename?: 'OrderAddress', streetLineOne?: string | null | undefined, streetLineTwo?: string | null | undefined, streetLineThree?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, postalCode?: string | null | undefined, country?: string | null | undefined } | null | undefined } };

export type OrdersQueryVariables = Exact<{
  accountId: Scalars['ID'];
  startDate?: InputMaybe<Scalars['String']>;
  endDate?: InputMaybe<Scalars['String']>;
  erpName: Scalars['String'];
}>;


export type OrdersQuery = { __typename?: 'Query', orders: { __typename?: 'OrdersResponse', orders?: Array<{ __typename?: 'Order', orderNumber?: string | null | undefined, orderStatus?: string | null | undefined, shipDate?: string | null | undefined, customerPO?: string | null | undefined, orderDate?: string | null | undefined, invoiceNumber?: string | null | undefined, webStatus?: string | null | undefined, amount?: string | null | undefined, shipToName?: string | null | undefined, jobNumber?: string | null | undefined, orderTotal?: number | null | undefined, shipAddress?: { __typename?: 'OrderAddress', streetLineOne?: string | null | undefined, streetLineTwo?: string | null | undefined, streetLineThree?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, postalCode?: string | null | undefined, country?: string | null | undefined } | null | undefined }> | null | undefined, pagination?: { __typename?: 'Pagination', totalItemCount?: number | null | undefined } | null | undefined } };

export type PreviouslyPurchasedProductsQueryVariables = Exact<{
  ecommShipToId: Scalars['ID'];
  userId: Scalars['ID'];
  currentPage?: InputMaybe<Scalars['Int']>;
  pageSize: Scalars['Int'];
  customerNumber?: InputMaybe<Scalars['String']>;
}>;


export type PreviouslyPurchasedProductsQuery = { __typename?: 'Query', previouslyPurchasedProducts: { __typename?: 'PreviouslyPurchasedProductResponse', products?: Array<{ __typename?: 'PreviouslyPurchasedProduct', product?: { __typename?: 'Product', id: string, partNumber?: string | null | undefined, name?: string | null | undefined, price?: number | null | undefined, upc?: string | null | undefined, manufacturerName?: string | null | undefined, manufacturerNumber?: string | null | undefined, minIncrementQty?: number | null | undefined, status?: string | null | undefined, customerPartNumber?: Array<string> | null | undefined, stock?: { __typename?: 'Stock', homeBranch?: { __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, availability?: number | null | undefined } | null | undefined, otherBranches?: Array<{ __typename?: 'StoreStock', branchId?: string | null | undefined, availability?: number | null | undefined }> | null | undefined } | null | undefined, imageUrls?: { __typename?: 'ImageUrls', thumb?: string | null | undefined } | null | undefined } | null | undefined, quantity?: { __typename?: 'Quantity', uom?: string | null | undefined, umqt?: string | null | undefined, quantity?: string | null | undefined } | null | undefined, lastOrder?: { __typename?: 'LastOrder', lastDate?: string | null | undefined, lastQuantity?: string | null | undefined } | null | undefined }> | null | undefined, pagination?: { __typename?: 'Pagination', pageSize?: number | null | undefined, currentPage?: number | null | undefined, totalItemCount?: number | null | undefined } | null | undefined } };

export type GetProductQueryVariables = Exact<{
  productInput: ProductInput;
}>;


export type GetProductQuery = { __typename?: 'Query', product?: { __typename?: 'Product', id: string, partNumber?: string | null | undefined, name?: string | null | undefined, manufacturerName?: string | null | undefined, manufacturerNumber?: string | null | undefined, price?: number | null | undefined, cmp?: number | null | undefined, taxonomy?: Array<string> | null | undefined, categories?: Array<string> | null | undefined, environmentalOptions?: Array<string> | null | undefined, upc?: string | null | undefined, unspsc?: string | null | undefined, seriesModelFigureNumber?: string | null | undefined, productOverview?: string | null | undefined, featuresAndBenefits?: string | null | undefined, minIncrementQty?: number | null | undefined, erp?: string | null | undefined, customerPartNumber?: Array<string> | null | undefined, stock?: { __typename?: 'Stock', homeBranch?: { __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined } | null | undefined, otherBranches?: Array<{ __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined }> | null | undefined } | null | undefined, technicalDocuments?: Array<{ __typename?: 'TechDoc', name?: string | null | undefined, url?: string | null | undefined }> | null | undefined, techSpecifications?: Array<{ __typename?: 'TechSpec', name?: string | null | undefined, value?: string | null | undefined }> | null | undefined, imageUrls?: { __typename?: 'ImageUrls', thumb?: string | null | undefined, small?: string | null | undefined, medium?: string | null | undefined, large?: string | null | undefined } | null | undefined, packageDimensions?: { __typename?: 'PackageDimensions', height?: number | null | undefined, weight?: number | null | undefined, length?: number | null | undefined, volume?: number | null | undefined, width?: number | null | undefined, volumeUnitOfMeasure?: string | null | undefined, weightUnitOfMeasure?: string | null | undefined } | null | undefined } | null | undefined };

export type GetProductPricingQueryVariables = Exact<{
  input: ProductPricingInput;
}>;


export type GetProductPricingQuery = { __typename?: 'Query', productPricing: { __typename?: 'ProductPricingResponse', customerId: string, branch: string, products: Array<{ __typename?: 'ProductPricing', productId: string, catalogId: string, branchAvailableQty: number, totalAvailableQty: number, listIds?: Array<string> | null | undefined, sellPrice: number, orderUom: string }> } };

export type GetProductInventoryQueryVariables = Exact<{
  productId: Scalars['String'];
}>;


export type GetProductInventoryQuery = { __typename?: 'Query', productInventory: { __typename?: 'ProductInventoryResponse', productId: string, productDescription: string, inventory: Array<{ __typename?: 'ProductInventory', branchId: string, availableOnHand: number }> } };

export type BranchInfoFragment = { __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined };

export type ProductFragment = { __typename?: 'Product', id: string, partNumber?: string | null | undefined, name?: string | null | undefined, manufacturerName?: string | null | undefined, manufacturerNumber?: string | null | undefined, price?: number | null | undefined, cmp?: number | null | undefined, taxonomy?: Array<string> | null | undefined, categories?: Array<string> | null | undefined, environmentalOptions?: Array<string> | null | undefined, upc?: string | null | undefined, unspsc?: string | null | undefined, seriesModelFigureNumber?: string | null | undefined, productOverview?: string | null | undefined, featuresAndBenefits?: string | null | undefined, minIncrementQty?: number | null | undefined, erp?: string | null | undefined, customerPartNumber?: Array<string> | null | undefined, stock?: { __typename?: 'Stock', homeBranch?: { __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined } | null | undefined, otherBranches?: Array<{ __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined }> | null | undefined } | null | undefined, technicalDocuments?: Array<{ __typename?: 'TechDoc', name?: string | null | undefined, url?: string | null | undefined }> | null | undefined, techSpecifications?: Array<{ __typename?: 'TechSpec', name?: string | null | undefined, value?: string | null | undefined }> | null | undefined, imageUrls?: { __typename?: 'ImageUrls', thumb?: string | null | undefined, small?: string | null | undefined, medium?: string | null | undefined, large?: string | null | undefined } | null | undefined, packageDimensions?: { __typename?: 'PackageDimensions', height?: number | null | undefined, weight?: number | null | undefined, length?: number | null | undefined, volume?: number | null | undefined, width?: number | null | undefined, volumeUnitOfMeasure?: string | null | undefined, weightUnitOfMeasure?: string | null | undefined } | null | undefined };

export type GetOrdersPendingApprovalQueryVariables = Exact<{ [key: string]: never; }>;


export type GetOrdersPendingApprovalQuery = { __typename?: 'Query', ordersPendingApproval: Array<{ __typename?: 'OrderPendingApproval', orderId?: string | null | undefined, purchaseOrderNumber?: string | null | undefined, submissionDate?: string | null | undefined, submittedByName?: string | null | undefined, orderTotal?: number | null | undefined }> };

export type OrderPendingApprovalQueryVariables = Exact<{
  orderId: Scalars['String'];
}>;


export type OrderPendingApprovalQuery = { __typename?: 'Query', orderPendingApproval: { __typename?: 'OrderPendingApproval', orderId?: string | null | undefined, purchaseOrderNumber?: string | null | undefined, submissionDate?: string | null | undefined, submittedByName?: string | null | undefined, orderTotal?: number | null | undefined, cartId?: string | null | undefined } };

export type ApproveOrderMutationVariables = Exact<{
  cartId: Scalars['ID'];
  userId: Scalars['ID'];
  shipToAccountId: Scalars['ID'];
  billToAccountId: Scalars['ID'];
}>;


export type ApproveOrderMutation = { __typename?: 'Mutation', approveOrder: { __typename?: 'OrderResponse', orderNumber?: string | null | undefined, orderStatus?: string | null | undefined, webStatus?: string | null | undefined, shipDate?: string | null | undefined, customerPO?: string | null | undefined, orderDate?: string | null | undefined, amount?: string | null | undefined, invoiceNumber?: string | null | undefined, billToName?: string | null | undefined, shipToName?: string | null | undefined, orderedBy?: string | null | undefined, email?: string | null | undefined, deliveryMethod?: string | null | undefined, specialInstructions?: string | null | undefined, subTotal?: number | null | undefined, tax?: number | null | undefined, orderTotal?: number | null | undefined, shipAddress?: { __typename?: 'EclipseAddress', streetLineOne?: string | null | undefined, streetLineTwo?: string | null | undefined, streetLineThree?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, postalCode?: string | null | undefined, country?: string | null | undefined } | null | undefined, branchInfo?: { __typename?: 'BranchOrderInfo', branchName?: string | null | undefined, streetLineOne?: string | null | undefined, streetLineTwo?: string | null | undefined, streetLineThree?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, postalCode?: string | null | undefined, country?: string | null | undefined } | null | undefined, lineItems: Array<{ __typename?: 'OrderLineItem', unitPrice?: number | null | undefined, erpPartNumber?: string | null | undefined, orderQuantity?: number | null | undefined, shipQuantity?: number | null | undefined, productOrderTotal?: number | null | undefined, manufacturerName?: string | null | undefined, manufacturerNumber?: string | null | undefined, productName?: string | null | undefined, productId?: string | null | undefined, imageUrls?: { __typename?: 'ImageUrls', thumb?: string | null | undefined, small?: string | null | undefined, medium?: string | null | undefined, large?: string | null | undefined } | null | undefined }> } };

export type RejectOrderMutationVariables = Exact<{
  cartId: Scalars['ID'];
  userId: Scalars['ID'];
  shipToAccountId: Scalars['ID'];
  rejectOrderInfo: RejectOrderInput;
}>;


export type RejectOrderMutation = { __typename?: 'Mutation', rejectOrder: string };

export type QuoteQueryVariables = Exact<{
  accountId: Scalars['ID'];
  quoteId: Scalars['ID'];
  userId: Scalars['ID'];
}>;


export type QuoteQuery = { __typename?: 'Query', quote: { __typename?: 'Quote', amount?: string | null | undefined, deliveryMethod?: string | null | undefined, email?: string | null | undefined, shipDate?: string | null | undefined, specialInstructions?: string | null | undefined, subTotal: number, tax: number, bidExpireDate: string, billToName: string, customerPO?: string | null | undefined, invoiceDueDate?: string | null | undefined, invoiceNumber?: string | null | undefined, orderDate?: string | null | undefined, orderedBy?: string | null | undefined, orderNumber: string, orderStatus: string, orderTotal: number, requiredDate: string, shipToId?: string | null | undefined, shipToName?: string | null | undefined, webStatus: string, branchInfo?: { __typename?: 'BranchOrderInfo', branchName?: string | null | undefined, streetLineOne?: string | null | undefined, streetLineTwo?: string | null | undefined, streetLineThree?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, postalCode?: string | null | undefined, country?: string | null | undefined } | null | undefined, lineItems?: Array<{ __typename?: 'QuoteLineItem', manufacturerName?: string | null | undefined, manufacturerNumber?: string | null | undefined, erpPartNumber?: string | null | undefined, orderQuantity?: number | null | undefined, productId?: string | null | undefined, productName?: string | null | undefined, productOrderTotal?: number | null | undefined, shipQuantity?: number | null | undefined, status?: string | null | undefined, unitPrice?: number | null | undefined, imageUrls?: { __typename?: 'ImageUrls', thumb?: string | null | undefined } | null | undefined }> | null | undefined, shipAddress: { __typename?: 'QuoteAddress', streetLineOne?: string | null | undefined, streetLineTwo?: string | null | undefined, streetLineThree?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, postalCode?: string | null | undefined, country?: string | null | undefined } } };

export type ApproveQuoteMutationVariables = Exact<{
  approveQuoteInput: ApproveQuoteInput;
}>;


export type ApproveQuoteMutation = { __typename?: 'Mutation', approveQuote: { __typename?: 'OrderResponse', orderNumber?: string | null | undefined, orderStatus?: string | null | undefined, webStatus?: string | null | undefined, shipDate?: string | null | undefined, customerPO?: string | null | undefined, orderDate?: string | null | undefined, amount?: string | null | undefined, invoiceNumber?: string | null | undefined, billToName?: string | null | undefined, shipToName?: string | null | undefined, orderedBy?: string | null | undefined, email?: string | null | undefined, deliveryMethod?: string | null | undefined, specialInstructions?: string | null | undefined, subTotal?: number | null | undefined, tax?: number | null | undefined, orderTotal?: number | null | undefined, shipAddress?: { __typename?: 'EclipseAddress', streetLineOne?: string | null | undefined, streetLineTwo?: string | null | undefined, streetLineThree?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, postalCode?: string | null | undefined, country?: string | null | undefined } | null | undefined, creditCard?: { __typename?: 'CreditCard', creditCardType: string, creditCardNumber: string, cardHolder: string, streetAddress: string, postalCode: string, elementPaymentAccountId: string, expirationDate: { __typename?: 'DateWrapper', date: string } } | null | undefined, branchInfo?: { __typename?: 'BranchOrderInfo', branchName?: string | null | undefined, streetLineOne?: string | null | undefined, streetLineTwo?: string | null | undefined, streetLineThree?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, postalCode?: string | null | undefined, country?: string | null | undefined } | null | undefined, lineItems: Array<{ __typename?: 'OrderLineItem', unitPrice?: number | null | undefined, erpPartNumber?: string | null | undefined, orderQuantity?: number | null | undefined, shipQuantity?: number | null | undefined, uom?: string | null | undefined, productOrderTotal?: number | null | undefined, manufacturerName?: string | null | undefined, manufacturerNumber?: string | null | undefined, productName?: string | null | undefined, productId?: string | null | undefined, imageUrls?: { __typename?: 'ImageUrls', thumb?: string | null | undefined, small?: string | null | undefined, medium?: string | null | undefined, large?: string | null | undefined } | null | undefined }> } };

export type RejectQuoteMutationVariables = Exact<{
  userId: Scalars['ID'];
  shipToAccountId: Scalars['ID'];
  quoteId: Scalars['ID'];
}>;


export type RejectQuoteMutation = { __typename?: 'Mutation', rejectQuote?: string | null | undefined };

export type QuoteFragment = { __typename?: 'Quote', bidExpireDate: string, billToName: string, customerPO?: string | null | undefined, invoiceDueDate?: string | null | undefined, invoiceNumber?: string | null | undefined, orderDate?: string | null | undefined, orderedBy?: string | null | undefined, orderNumber: string, orderStatus: string, orderTotal: number, requiredDate: string, shipToId?: string | null | undefined, shipToName?: string | null | undefined, webStatus: string };

export type QuotesQueryVariables = Exact<{
  accountId: Scalars['ID'];
}>;


export type QuotesQuery = { __typename?: 'Query', quotes: Array<{ __typename?: 'Quote', bidExpireDate: string, billToName: string, customerPO?: string | null | undefined, invoiceDueDate?: string | null | undefined, invoiceNumber?: string | null | undefined, orderDate?: string | null | undefined, orderedBy?: string | null | undefined, orderNumber: string, orderStatus: string, orderTotal: number, requiredDate: string, shipToId?: string | null | undefined, shipToName?: string | null | undefined, webStatus: string }> };

export type AccountQueryVariables = Exact<{
  accountId: Scalars['String'];
  brand: Scalars['String'];
}>;


export type AccountQuery = { __typename?: 'Query', account: Array<{ __typename?: 'ErpAccount', branchId?: string | null | undefined, erpAccountId?: string | null | undefined, erpName?: string | null | undefined, companyName?: string | null | undefined, billToFlag?: boolean | null | undefined }> };

export type AccountDetailsQueryVariables = Exact<{
  accountId: Scalars['String'];
  brand: Scalars['String'];
}>;


export type AccountDetailsQuery = { __typename?: 'Query', account: Array<{ __typename?: 'ErpAccount', companyName?: string | null | undefined, street1?: string | null | undefined, street2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, erpName?: string | null | undefined }> };

export type GetContactInfoQueryVariables = Exact<{
  userId?: InputMaybe<Scalars['String']>;
}>;


export type GetContactInfoQuery = { __typename?: 'Query', contactInfo?: { __typename?: 'ContactInfo', isBranchInfo?: boolean | null | undefined, phoneNumber?: string | null | undefined, emailAddress?: string | null | undefined } | null | undefined };

export type GetPhoneTypesQueryVariables = Exact<{ [key: string]: never; }>;


export type GetPhoneTypesQuery = { __typename?: 'Query', phoneTypes: Array<PhoneType> };

export type GetUserInviteQueryVariables = Exact<{
  inviteId: Scalars['ID'];
}>;


export type GetUserInviteQuery = { __typename?: 'Query', userInvite: { __typename?: 'InvitedUser', id: string, email: string, firstName: string, lastName: string, userRoleId: string, approverId?: string | null | undefined, erpAccountId: string, billToAccountId?: string | null | undefined, erpSystemName: ErpSystemEnum, completed: boolean } };

export type CreateUserMutationVariables = Exact<{
  user: UserInput;
  inviteId?: InputMaybe<Scalars['ID']>;
}>;


export type CreateUserMutation = { __typename?: 'Mutation', createUser: { __typename?: 'UserRequest', id?: string | null | undefined, isEmployee?: boolean | null | undefined, isVerified?: boolean | null | undefined } };

export type VerifyUserMutationVariables = Exact<{
  verificationToken: Scalars['ID'];
}>;


export type VerifyUserMutation = { __typename?: 'Mutation', verifyUser: boolean };

export type ResendVerificationEmailMutationVariables = Exact<{
  userId: Scalars['ID'];
  isWaterworksSubdomain?: InputMaybe<Scalars['Boolean']>;
}>;


export type ResendVerificationEmailMutation = { __typename?: 'Mutation', resendVerificationEmail: string };

export type CreateNewEmployeeMutationVariables = Exact<{
  employee: CreateEmployeeInput;
}>;


export type CreateNewEmployeeMutation = { __typename?: 'Mutation', createNewEmployee: { __typename?: 'CreateUserResponse', id: string, firstName: string, lastName: string, email: string, phoneNumber: string, roleId: string, approverId?: string | null | undefined } };

export type CreateNewUserMutationVariables = Exact<{
  user: CreateUserInput;
  inviteId?: InputMaybe<Scalars['ID']>;
}>;


export type CreateNewUserMutation = { __typename?: 'Mutation', createNewUser: { __typename?: 'CreateUserResponse', id: string, firstName: string, lastName: string, email: string, phoneNumber: string, roleId: string, approverId?: string | null | undefined } };

export type VerifyAccountNewQueryVariables = Exact<{
  input: VerifyAccountInput;
}>;


export type VerifyAccountNewQuery = { __typename?: 'Query', verifyAccountNew: { __typename?: 'VerifyAccountResponse', accountName: string, isTradeAccount: boolean } };

export type VerifyUserEmailQueryVariables = Exact<{
  email: Scalars['String'];
}>;


export type VerifyUserEmailQuery = { __typename?: 'Query', verifyUserEmail: { __typename?: 'EmailValidationResponse', isValid: boolean, isEmployee: boolean } };

export type SearchProductQueryVariables = Exact<{
  productSearch?: InputMaybe<ProductSearchInput>;
  userId?: InputMaybe<Scalars['ID']>;
  shipToAccountId?: InputMaybe<Scalars['ID']>;
}>;


export type SearchProductQuery = { __typename?: 'Query', searchProduct?: { __typename?: 'ProductSearchResult', pagination?: { __typename?: 'Pagination', currentPage?: number | null | undefined, pageSize?: number | null | undefined, totalItemCount?: number | null | undefined } | null | undefined, products?: Array<{ __typename?: 'Product', id: string, partNumber?: string | null | undefined, name?: string | null | undefined, manufacturerName?: string | null | undefined, manufacturerNumber?: string | null | undefined, price?: number | null | undefined, cmp?: number | null | undefined, taxonomy?: Array<string> | null | undefined, categories?: Array<string> | null | undefined, environmentalOptions?: Array<string> | null | undefined, upc?: string | null | undefined, unspsc?: string | null | undefined, seriesModelFigureNumber?: string | null | undefined, productOverview?: string | null | undefined, featuresAndBenefits?: string | null | undefined, minIncrementQty?: number | null | undefined, erp?: string | null | undefined, customerPartNumber?: Array<string> | null | undefined, stock?: { __typename?: 'Stock', homeBranch?: { __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined } | null | undefined, otherBranches?: Array<{ __typename?: 'StoreStock', branchName?: string | null | undefined, branchId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, distanceToBranch?: string | null | undefined, availability?: number | null | undefined }> | null | undefined } | null | undefined, technicalDocuments?: Array<{ __typename?: 'TechDoc', name?: string | null | undefined, url?: string | null | undefined }> | null | undefined, techSpecifications?: Array<{ __typename?: 'TechSpec', name?: string | null | undefined, value?: string | null | undefined }> | null | undefined, imageUrls?: { __typename?: 'ImageUrls', thumb?: string | null | undefined, small?: string | null | undefined, medium?: string | null | undefined, large?: string | null | undefined } | null | undefined, packageDimensions?: { __typename?: 'PackageDimensions', height?: number | null | undefined, weight?: number | null | undefined, length?: number | null | undefined, volume?: number | null | undefined, width?: number | null | undefined, volumeUnitOfMeasure?: string | null | undefined, weightUnitOfMeasure?: string | null | undefined } | null | undefined }> | null | undefined, aggregates?: { __typename?: 'AggregationResults', inStockLocation?: Array<{ __typename?: 'AggregationItem', value?: string | null | undefined, count?: number | null | undefined }> | null | undefined, category?: Array<{ __typename?: 'AggregationItem', value?: string | null | undefined, count?: number | null | undefined }> | null | undefined, productTypes?: Array<{ __typename?: 'AggregationItem', value?: string | null | undefined, count?: number | null | undefined }> | null | undefined, brands?: Array<{ __typename?: 'AggregationItem', value?: string | null | undefined, count?: number | null | undefined }> | null | undefined, lines?: Array<{ __typename?: 'AggregationItem', value?: string | null | undefined, count?: number | null | undefined }> | null | undefined, flowRate?: Array<{ __typename?: 'AggregationItem', value?: string | null | undefined, count?: number | null | undefined }> | null | undefined, environmentalOptions?: Array<{ __typename?: 'AggregationItem', value?: string | null | undefined, count?: number | null | undefined }> | null | undefined, material?: Array<{ __typename?: 'AggregationItem', value?: string | null | undefined, count?: number | null | undefined }> | null | undefined, colorFinish?: Array<{ __typename?: 'AggregationItem', value?: string | null | undefined, count?: number | null | undefined }> | null | undefined, size?: Array<{ __typename?: 'AggregationItem', value?: string | null | undefined, count?: number | null | undefined }> | null | undefined, length?: Array<{ __typename?: 'AggregationItem', value?: string | null | undefined, count?: number | null | undefined }> | null | undefined, width?: Array<{ __typename?: 'AggregationItem', value?: string | null | undefined, count?: number | null | undefined }> | null | undefined, height?: Array<{ __typename?: 'AggregationItem', value?: string | null | undefined, count?: number | null | undefined }> | null | undefined, depth?: Array<{ __typename?: 'AggregationItem', value?: string | null | undefined, count?: number | null | undefined }> | null | undefined, voltage?: Array<{ __typename?: 'AggregationItem', value?: string | null | undefined, count?: number | null | undefined }> | null | undefined, tonnage?: Array<{ __typename?: 'AggregationItem', value?: string | null | undefined, count?: number | null | undefined }> | null | undefined, btu?: Array<{ __typename?: 'AggregationItem', value?: string | null | undefined, count?: number | null | undefined }> | null | undefined, pressureRating?: Array<{ __typename?: 'AggregationItem', value?: string | null | undefined, count?: number | null | undefined }> | null | undefined, temperatureRating?: Array<{ __typename?: 'AggregationItem', value?: string | null | undefined, count?: number | null | undefined }> | null | undefined, inletSize?: Array<{ __typename?: 'AggregationItem', value?: string | null | undefined, count?: number | null | undefined }> | null | undefined, capacity?: Array<{ __typename?: 'AggregationItem', value?: string | null | undefined, count?: number | null | undefined }> | null | undefined, wattage?: Array<{ __typename?: 'AggregationItem', value?: string | null | undefined, count?: number | null | undefined }> | null | undefined } | null | undefined, selectedAttributes?: Array<{ __typename?: 'ProductAttributeOutput', attributeType?: string | null | undefined, attributeValue?: string | null | undefined }> | null | undefined } | null | undefined };

export type SearchSuggestionQueryVariables = Exact<{
  term: Scalars['String'];
  engine: Scalars['String'];
  size?: InputMaybe<Scalars['Int']>;
  userId?: InputMaybe<Scalars['ID']>;
  shipToAccountId?: InputMaybe<Scalars['ID']>;
  erpSystem?: InputMaybe<Scalars['String']>;
  state?: InputMaybe<Scalars['String']>;
}>;


export type SearchSuggestionQuery = { __typename?: 'Query', searchSuggestion: { __typename?: 'SearchSuggestionResult', suggestions: Array<string>, topCategories: Array<{ __typename?: 'AggregationItem', value?: string | null | undefined, count?: number | null | undefined }>, topProducts: Array<{ __typename?: 'Product', id: string, name?: string | null | undefined, manufacturerName?: string | null | undefined, manufacturerNumber?: string | null | undefined, imageUrls?: { __typename?: 'ImageUrls', thumb?: string | null | undefined } | null | undefined }> } };

export type GetRefreshShipToAccountQueryVariables = Exact<{
  billToAccountId: Scalars['String'];
}>;


export type GetRefreshShipToAccountQuery = { __typename?: 'Query', refreshShipToAccount: Array<{ __typename?: 'ShipToAccount', id?: string | null | undefined, name?: string | null | undefined }> };

export type GetHomeBranchQueryVariables = Exact<{
  shipToAccountId: Scalars['String'];
}>;


export type GetHomeBranchQuery = { __typename?: 'Query', homeBranch: { __typename?: 'Branch', branchId: string, name?: string | null | undefined, entityId?: string | null | undefined, address1?: string | null | undefined, address2?: string | null | undefined, city?: string | null | undefined, state?: string | null | undefined, zip?: string | null | undefined, phone?: string | null | undefined, longitude: number, latitude: number } };

export type SendContactFormMutationVariables = Exact<{
  contactFormInput: ContactFormInput;
}>;


export type SendContactFormMutation = { __typename?: 'Mutation', sendContactForm: string };

export type RefreshContactMutationVariables = Exact<{
  emails: Array<Scalars['String']> | Scalars['String'];
}>;


export type RefreshContactMutation = { __typename?: 'Mutation', refreshContact: string };

export type ApproveUserMutationVariables = Exact<{
  user: ApproveUserInput;
}>;


export type ApproveUserMutation = { __typename?: 'Mutation', approveUser: string };

export type DeleteUserMutationVariables = Exact<{
  deleteUserInput: DeleteUserInput;
}>;


export type DeleteUserMutation = { __typename?: 'Mutation', deleteUser: boolean };

export type CheckUsersForApproverMutationVariables = Exact<{
  checkUsersForApproverInput: CheckUsersForApproverInput;
}>;


export type CheckUsersForApproverMutation = { __typename?: 'Mutation', checkUsersForApprover?: { __typename?: 'UsersOfApprover', users: Array<{ __typename?: 'UserInfo', firstName: string, lastName: string, email: string }> } | null | undefined };

export type GetRejectionReasonsQueryVariables = Exact<{ [key: string]: never; }>;


export type GetRejectionReasonsQuery = { __typename?: 'Query', rejectionReasons?: Array<RejectionReason> | null | undefined };

export type RejectUserMutationVariables = Exact<{
  rejectUserInput: RejectUserInput;
}>;


export type RejectUserMutation = { __typename?: 'Mutation', rejectUser: string };

export type GetRolesQueryVariables = Exact<{ [key: string]: never; }>;


export type GetRolesQuery = { __typename?: 'Query', roles: Array<{ __typename?: 'Role', id?: string | null | undefined, name?: string | null | undefined, description?: string | null | undefined }> };

export type GetApproversQueryVariables = Exact<{
  billToAccountId: Scalars['ID'];
}>;


export type GetApproversQuery = { __typename?: 'Query', approvers: Array<{ __typename?: 'Approver', id: string, firstName?: string | null | undefined, lastName?: string | null | undefined, email?: string | null | undefined }> };

export type UpdateUserMutationVariables = Exact<{
  updateUserInput: UpdateUserInput;
}>;


export type UpdateUserMutation = { __typename?: 'Mutation', updateUser: { __typename?: 'User', id?: string | null | undefined } };

export type UnapprovedAccountRequestsQueryVariables = Exact<{
  accountId: Scalars['String'];
}>;


export type UnapprovedAccountRequestsQuery = { __typename?: 'Query', unapprovedAccountRequests?: Array<{ __typename?: 'User', id?: string | null | undefined, email?: string | null | undefined, firstName?: string | null | undefined, lastName?: string | null | undefined, companyName?: string | null | undefined, phoneNumber?: string | null | undefined, roleId?: string | null | undefined, phoneType: PhoneType, createdAt?: string | null | undefined }> | null | undefined };

export type GetApprovedAccountRequestsQueryVariables = Exact<{
  accountId?: InputMaybe<Scalars['String']>;
}>;


export type GetApprovedAccountRequestsQuery = { __typename?: 'Query', accountUsers?: Array<{ __typename?: 'ApprovedUser', id?: string | null | undefined, email?: string | null | undefined, firstName?: string | null | undefined, lastName?: string | null | undefined, phoneNumber?: string | null | undefined, contactUpdatedAt?: string | null | undefined, contactUpdatedBy?: string | null | undefined, approverId?: string | null | undefined, phoneType: PhoneType, role?: { __typename?: 'Role', id?: string | null | undefined, name?: string | null | undefined } | null | undefined }> | null | undefined };

export type RejectedAccountRequestsQueryVariables = Exact<{
  accountId: Scalars['String'];
}>;


export type RejectedAccountRequestsQuery = { __typename?: 'Query', rejectedAccountRequests?: Array<{ __typename?: 'User', id?: string | null | undefined, email?: string | null | undefined, firstName?: string | null | undefined, lastName?: string | null | undefined, companyName?: string | null | undefined, phoneNumber?: string | null | undefined, phoneType: PhoneType, rejectionReason?: RejectionReason | null | undefined, rejectedAt?: string | null | undefined, rejectedBy?: string | null | undefined }> | null | undefined };

export type GetSelectedErpAccountQueryVariables = Exact<{
  accountId: Scalars['String'];
  brand: Scalars['String'];
}>;


export type GetSelectedErpAccountQuery = { __typename?: 'Query', account: Array<{ __typename?: 'ErpAccount', alwaysCod?: boolean | null | undefined, branchId?: string | null | undefined, city?: string | null | undefined, companyName?: string | null | undefined, email?: Array<string> | null | undefined, erpAccountId?: string | null | undefined, erp?: ErpSystemEnum | null | undefined, erpName?: string | null | undefined, phoneNumber?: string | null | undefined, state?: string | null | undefined, street1?: string | null | undefined, street2?: string | null | undefined, zip?: string | null | undefined, poReleaseRequired?: string | null | undefined, creditHold?: boolean | null | undefined, territory?: string | null | undefined }> };

export type GetSelectedErpAccountsQueryVariables = Exact<{
  billToId: Scalars['String'];
  shipToId: Scalars['String'];
  brand: Scalars['String'];
}>;


export type GetSelectedErpAccountsQuery = { __typename?: 'Query', billToAccount: Array<{ __typename?: 'ErpAccount', alwaysCod?: boolean | null | undefined, branchId?: string | null | undefined, city?: string | null | undefined, companyName?: string | null | undefined, email?: Array<string> | null | undefined, erpAccountId?: string | null | undefined, erp?: ErpSystemEnum | null | undefined, erpName?: string | null | undefined, phoneNumber?: string | null | undefined, state?: string | null | undefined, street1?: string | null | undefined, street2?: string | null | undefined, zip?: string | null | undefined, poReleaseRequired?: string | null | undefined, creditHold?: boolean | null | undefined, territory?: string | null | undefined }>, shipToAccount: Array<{ __typename?: 'ErpAccount', alwaysCod?: boolean | null | undefined, branchId?: string | null | undefined, city?: string | null | undefined, companyName?: string | null | undefined, email?: Array<string> | null | undefined, erpAccountId?: string | null | undefined, erp?: ErpSystemEnum | null | undefined, erpName?: string | null | undefined, phoneNumber?: string | null | undefined, state?: string | null | undefined, street1?: string | null | undefined, street2?: string | null | undefined, zip?: string | null | undefined, poReleaseRequired?: string | null | undefined, creditHold?: boolean | null | undefined, territory?: string | null | undefined }> };

export const BranchListItemFragmentDoc = gql`
    fragment BranchListItem on Branch {
  branchId
  name
  address1
  city
  state
  zip
  isActive
  isAvailableInStoreFinder
  isShoppable
  isPricingOnly
  isPlumbing
  isWaterworks
  isHvac
  isBandK
  id
}
    `;
export const CreditCardFragmentDoc = gql`
    fragment CreditCard on CreditCard {
  creditCardType
  creditCardNumber
  expirationDate {
    date
  }
  cardHolder
  streetAddress
  postalCode
  elementPaymentAccountId
}
    `;
export const BranchOrderInfoFragmentDoc = gql`
    fragment BranchOrderInfo on BranchOrderInfo {
  branchName
  streetLineOne
  streetLineTwo
  streetLineThree
  city
  state
  postalCode
  country
}
    `;
export const SubmitOrderResponseFragmentDoc = gql`
    fragment SubmitOrderResponse on OrderResponse {
  orderNumber
  orderStatus
  webStatus
  shipDate
  customerPO
  orderDate
  amount
  invoiceNumber
  billToName
  shipToName
  orderedBy
  shipAddress {
    streetLineOne
    streetLineTwo
    streetLineThree
    city
    state
    postalCode
    country
  }
  email
  creditCard {
    ...CreditCard
  }
  branchInfo {
    ...BranchOrderInfo
  }
  deliveryMethod
  specialInstructions
  subTotal
  tax
  orderTotal
  lineItems {
    unitPrice
    erpPartNumber
    orderQuantity
    shipQuantity
    uom
    productOrderTotal
    imageUrls {
      thumb
      small
      medium
      large
    }
    manufacturerName
    manufacturerNumber
    productName
    productId
  }
}
    ${CreditCardFragmentDoc}
${BranchOrderInfoFragmentDoc}`;
export const BranchInfoFragmentDoc = gql`
    fragment BranchInfo on StoreStock {
  branchName
  branchId
  address1
  address2
  city
  state
  zip
  phone
  distanceToBranch
  availability
}
    `;
export const ProductFragmentDoc = gql`
    fragment Product on Product {
  id
  partNumber
  name
  manufacturerName
  manufacturerNumber
  price
  cmp
  stock {
    homeBranch {
      ...BranchInfo
    }
    otherBranches {
      ...BranchInfo
    }
  }
  taxonomy
  categories
  technicalDocuments {
    name
    url
  }
  environmentalOptions
  upc
  unspsc
  seriesModelFigureNumber
  productOverview
  featuresAndBenefits
  techSpecifications {
    name
    value
  }
  imageUrls {
    thumb
    small
    medium
    large
  }
  packageDimensions {
    height
    weight
    length
    volume
    width
    volumeUnitOfMeasure
    weightUnitOfMeasure
  }
  minIncrementQty
  erp
  customerPartNumber
}
    ${BranchInfoFragmentDoc}`;
export const UpdateItemQtyResponseFragmentDoc = gql`
    fragment UpdateItemQtyResponse on UpdateItemQtyResponse {
  subtotal
  product {
    id
    cartId
    customerPartNumber
    pricePerUnit
    uom
    priceLastUpdatedAt
    erpPartNumber
    quantity
    qtyAvailableLastUpdatedAt
    qtyAvailable
    product {
      ...Product
    }
    listIds
  }
}
    ${ProductFragmentDoc}`;
export const CartFragmentDoc = gql`
    fragment Cart on Cart {
  id
  ownerId
  approverId
  shipToId
  poNumber
  pricingBranchId
  shippingBranchId
  paymentMethodType
  creditCard {
    ...CreditCard
  }
  approvalState
  rejectionReason
  removedProducts
  subtotal
  tax
  shippingHandling
  total
  deliveryMethod
  delivery {
    id
    shipToId
    address {
      id
      companyName
      street1
      street2
      city
      state
      zip
      country
      custom
    }
    deliveryInstructions
    preferredDate
    preferredTime
    shouldShipFullOrder
    phoneNumber
  }
  willCall {
    id
    preferredDate
    preferredTime
    branchId
    pickupInstructions
  }
  erpSystemName
  products {
    id
    cartId
    customerPartNumber
    pricePerUnit
    uom
    priceLastUpdatedAt
    erpPartNumber
    quantity
    qtyAvailableLastUpdatedAt
    qtyAvailable
    product {
      ...Product
    }
    listIds
  }
}
    ${CreditCardFragmentDoc}
${ProductFragmentDoc}`;
export const ErpAccountFragmentDoc = gql`
    fragment ErpAccount on ErpAccount {
  alwaysCod
  branchId
  city
  companyName
  email
  erpAccountId
  erp
  erpName
  phoneNumber
  state
  street1
  street2
  zip
  poReleaseRequired
  creditHold
  territory
}
    `;
export const ContractAddressFragmentDoc = gql`
    fragment contractAddress on ContractAddress {
  entityId
  branchNumber
  address1
  address2
  address3
  city
  state
  zip
  county
  country
  taxJurisdiction
}
    `;
export const ItemPageFragmentDoc = gql`
    fragment ItemPage on ItemPage {
  items {
    orderNumber
    displayOnly
    productNumber
    description
    uom
    lineComments
    quantityOrdered
    quantityReleasedToDate
    quantityBackOrdered
    quantityShipped
    unitPrice
    netPrice
    extendedPrice
    lineNumber
    orderLineItemTypeCode
  }
}
    `;
export const ContractOrderDetailFragmentDoc = gql`
    fragment ContractOrderDetail on ContractOrderDetail {
  shipToAddress {
    ...contractAddress
  }
  itemList {
    ...ItemPage
  }
  taxAmount
  totalAmount
  subTotal
  otherCharges
  orderType
  gstHstUseCode
  promiseDate
  orderNumber
  orderComments
  subTotalWithBackOrder
  trackingURL
  creditCardMessage
  orderBy
  gstHstAmount
  orderDescription
  enteredBy
  shipmentMethod
  paidStatus
  shipVia
  allowCreditCardFlag
  merchantId
  creditCardTypes
  invoiceNumber
  freightCode
  trackingNumber
  jobName
  taxable
  branchNumber
  editable
  contractNumber
  branchName
  shipDate
  shipHandleAmount
  paidByCC
  customerName
  phoneNumber
  creditCardAuthAmount
  specialInstructions
  purchaseOrderNumber
  shipmentMethodSysConfig
  orderQuoteCreditDebitCode
  orderDate
  jobNumber
  status
  shoppingCartId
}
    ${ContractAddressFragmentDoc}
${ItemPageFragmentDoc}`;
export const AccountInformationFragmentDoc = gql`
    fragment accountInformation on AccountInformation {
  shipToAddress {
    ...contractAddress
  }
  branch {
    ...contractAddress
  }
}
    ${ContractAddressFragmentDoc}`;
export const ContractDatesFragmentDoc = gql`
    fragment contractDates on ContractDates {
  promisedDate
  contractDate
  firstReleaseDate
  lastReleaseDate
}
    `;
export const CustomerInfoFragmentDoc = gql`
    fragment customerInfo on CustomerInfo {
  customerNumber
  jobNumber
  enteredBy
}
    `;
export const ContractSummaryFragmentDoc = gql`
    fragment contractSummary on ContractSummary {
  subTotal
  taxAmount
  otherCharges
  invoicedToDateAmount
  totalAmount
  firstShipmentDate
  lastShipmentDate
}
    `;
export const ContractProductFragmentDoc = gql`
    fragment contractProduct on ContractProduct {
  id
  brand
  name
  lineComments
  partNumber
  sequenceNumber
  mfr
  thumb
  netPrice
  unitPrice
  pricingUom
  qty {
    quantityOrdered
    quantityReleasedToDate
    quantityShipped
  }
  technicalDocuments {
    name
    url
  }
  techSpecifications {
    name
    value
  }
  displayOnly
}
    `;
export const ContractDetailsFragmentDoc = gql`
    fragment contractDetails on ContractDetails {
  contractNumber
  contractDescription
  jobName
  purchaseOrderNumber
  accountInformation {
    ...accountInformation
  }
  contractDates {
    ...contractDates
  }
  customerInfo {
    ...customerInfo
  }
  contractSummary {
    ...contractSummary
  }
  contractProducts {
    ...contractProduct
  }
}
    ${AccountInformationFragmentDoc}
${ContractDatesFragmentDoc}
${CustomerInfoFragmentDoc}
${ContractSummaryFragmentDoc}
${ContractProductFragmentDoc}`;
export const BasicListFragmentDoc = gql`
    fragment BasicList on List {
  id
  name
  billToAccountId
  listLineItems {
    id
    erpPartNumber
    sortOrder
    quantity
  }
  listLineItemsSize
}
    `;
export const ListFragmentDoc = gql`
    fragment List on List {
  id
  name
  billToAccountId
  listLineItems {
    id
    erpPartNumber
    quantity
    minIncrementQty
    listId
    sortOrder
    name
    imageUrls {
      thumb
    }
    manufacturerName
    manufacturerNumber
    status
    customerPartNumber
  }
}
    `;
export const ListUploadResponseFragmentDoc = gql`
    fragment ListUploadResponse on ListUploadResponse {
  errors {
    partNumber
    description
    manufacturerName
    quantity
  }
  successfulRowCount
  listId
}
    `;
export const ExportListResponseFragmentDoc = gql`
    fragment ExportListResponse on ExportListResponse {
  listLineItems {
    partNumber
    description
    mfrName
    quantity
    price
    mfrNumber
  }
}
    `;
export const BranchFragmentDoc = gql`
    fragment Branch on Branch {
  branchId
  name
  entityId
  address1
  address2
  city
  state
  zip
  phone
  distance
  erpSystemName
  website
  workdayId
  workdayLocationName
  actingBranchManager
  actingBranchManagerPhone
  actingBranchManagerEmail
  businessHours
  latitude
  longitude
  isPlumbing
  isWaterworks
  isHvac
  isBandK
  isPricingOnly
}
    `;
export const QuoteFragmentDoc = gql`
    fragment Quote on Quote {
  bidExpireDate
  billToName
  customerPO
  invoiceDueDate
  invoiceNumber
  orderDate
  orderedBy
  orderNumber
  orderStatus
  orderTotal
  requiredDate
  shipToId
  shipToName
  webStatus
}
    `;
export const UserDocument = gql`
    query user($userId: String) {
  user(userId: $userId) {
    id
    email
    firstName
    lastName
    phoneNumber
    role {
      id
      name
      description
    }
    phoneType
  }
}
    `;

/**
 * __useUserQuery__
 *
 * To run a query within a React component, call `useUserQuery` and pass it any options that fit your needs.
 * When your component renders, `useUserQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useUserQuery({
 *   variables: {
 *      userId: // value for 'userId'
 *   },
 * });
 */
export function useUserQuery(baseOptions?: Apollo.QueryHookOptions<UserQuery, UserQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<UserQuery, UserQueryVariables>(UserDocument, options);
      }
export function useUserLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<UserQuery, UserQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<UserQuery, UserQueryVariables>(UserDocument, options);
        }
export type UserQueryHookResult = ReturnType<typeof useUserQuery>;
export type UserLazyQueryHookResult = ReturnType<typeof useUserLazyQuery>;
export type UserQueryResult = Apollo.QueryResult<UserQuery, UserQueryVariables>;
export const UserAccountsDocument = gql`
    query UserAccounts($userId: String) {
  userAccounts(userId: $userId) {
    id
    name
    erpAccountId
    erpSystemName
    shipTos {
      id
      name
      erpAccountId
      erpSystemName
      divisionEnum
      branchId
      branchAddress
    }
    erpSystemName
    divisionEnum
    branchId
    branchAddress
  }
}
    `;

/**
 * __useUserAccountsQuery__
 *
 * To run a query within a React component, call `useUserAccountsQuery` and pass it any options that fit your needs.
 * When your component renders, `useUserAccountsQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useUserAccountsQuery({
 *   variables: {
 *      userId: // value for 'userId'
 *   },
 * });
 */
export function useUserAccountsQuery(baseOptions?: Apollo.QueryHookOptions<UserAccountsQuery, UserAccountsQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<UserAccountsQuery, UserAccountsQueryVariables>(UserAccountsDocument, options);
      }
export function useUserAccountsLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<UserAccountsQuery, UserAccountsQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<UserAccountsQuery, UserAccountsQueryVariables>(UserAccountsDocument, options);
        }
export type UserAccountsQueryHookResult = ReturnType<typeof useUserAccountsQuery>;
export type UserAccountsLazyQueryHookResult = ReturnType<typeof useUserAccountsLazyQuery>;
export type UserAccountsQueryResult = Apollo.QueryResult<UserAccountsQuery, UserAccountsQueryVariables>;
export const UpdateUserPasswordDocument = gql`
    mutation updateUserPassword($updateUserPasswordInput: UpdateUserPasswordInput!) {
  updateUserPassword(updateUserPasswordInput: $updateUserPasswordInput)
}
    `;
export type UpdateUserPasswordMutationFn = Apollo.MutationFunction<UpdateUserPasswordMutation, UpdateUserPasswordMutationVariables>;

/**
 * __useUpdateUserPasswordMutation__
 *
 * To run a mutation, you first call `useUpdateUserPasswordMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useUpdateUserPasswordMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [updateUserPasswordMutation, { data, loading, error }] = useUpdateUserPasswordMutation({
 *   variables: {
 *      updateUserPasswordInput: // value for 'updateUserPasswordInput'
 *   },
 * });
 */
export function useUpdateUserPasswordMutation(baseOptions?: Apollo.MutationHookOptions<UpdateUserPasswordMutation, UpdateUserPasswordMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<UpdateUserPasswordMutation, UpdateUserPasswordMutationVariables>(UpdateUserPasswordDocument, options);
      }
export type UpdateUserPasswordMutationHookResult = ReturnType<typeof useUpdateUserPasswordMutation>;
export type UpdateUserPasswordMutationResult = Apollo.MutationResult<UpdateUserPasswordMutation>;
export type UpdateUserPasswordMutationOptions = Apollo.BaseMutationOptions<UpdateUserPasswordMutation, UpdateUserPasswordMutationVariables>;
export const GetBranchesListDocument = gql`
    query GetBranchesList {
  branches {
    ...BranchListItem
  }
}
    ${BranchListItemFragmentDoc}`;

/**
 * __useGetBranchesListQuery__
 *
 * To run a query within a React component, call `useGetBranchesListQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetBranchesListQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetBranchesListQuery({
 *   variables: {
 *   },
 * });
 */
export function useGetBranchesListQuery(baseOptions?: Apollo.QueryHookOptions<GetBranchesListQuery, GetBranchesListQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetBranchesListQuery, GetBranchesListQueryVariables>(GetBranchesListDocument, options);
      }
export function useGetBranchesListLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetBranchesListQuery, GetBranchesListQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetBranchesListQuery, GetBranchesListQueryVariables>(GetBranchesListDocument, options);
        }
export type GetBranchesListQueryHookResult = ReturnType<typeof useGetBranchesListQuery>;
export type GetBranchesListLazyQueryHookResult = ReturnType<typeof useGetBranchesListLazyQuery>;
export type GetBranchesListQueryResult = Apollo.QueryResult<GetBranchesListQuery, GetBranchesListQueryVariables>;
export const UpdateBranchDocument = gql`
    mutation UpdateBranch($input: UpdateBranchInput!) {
  updateBranch(input: $input) {
    ...BranchListItem
  }
}
    ${BranchListItemFragmentDoc}`;
export type UpdateBranchMutationFn = Apollo.MutationFunction<UpdateBranchMutation, UpdateBranchMutationVariables>;

/**
 * __useUpdateBranchMutation__
 *
 * To run a mutation, you first call `useUpdateBranchMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useUpdateBranchMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [updateBranchMutation, { data, loading, error }] = useUpdateBranchMutation({
 *   variables: {
 *      input: // value for 'input'
 *   },
 * });
 */
export function useUpdateBranchMutation(baseOptions?: Apollo.MutationHookOptions<UpdateBranchMutation, UpdateBranchMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<UpdateBranchMutation, UpdateBranchMutationVariables>(UpdateBranchDocument, options);
      }
export type UpdateBranchMutationHookResult = ReturnType<typeof useUpdateBranchMutation>;
export type UpdateBranchMutationResult = Apollo.MutationResult<UpdateBranchMutation>;
export type UpdateBranchMutationOptions = Apollo.BaseMutationOptions<UpdateBranchMutation, UpdateBranchMutationVariables>;
export const CartDocument = gql`
    query Cart($id: ID!, $userId: ID!, $shipToAccountId: ID!, $includeProducts: Boolean) {
  cart(
    id: $id
    userId: $userId
    shipToAccountId: $shipToAccountId
    includeProducts: $includeProducts
  ) {
    ...Cart
  }
}
    ${CartFragmentDoc}`;

/**
 * __useCartQuery__
 *
 * To run a query within a React component, call `useCartQuery` and pass it any options that fit your needs.
 * When your component renders, `useCartQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useCartQuery({
 *   variables: {
 *      id: // value for 'id'
 *      userId: // value for 'userId'
 *      shipToAccountId: // value for 'shipToAccountId'
 *      includeProducts: // value for 'includeProducts'
 *   },
 * });
 */
export function useCartQuery(baseOptions: Apollo.QueryHookOptions<CartQuery, CartQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<CartQuery, CartQueryVariables>(CartDocument, options);
      }
export function useCartLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<CartQuery, CartQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<CartQuery, CartQueryVariables>(CartDocument, options);
        }
export type CartQueryHookResult = ReturnType<typeof useCartQuery>;
export type CartLazyQueryHookResult = ReturnType<typeof useCartLazyQuery>;
export type CartQueryResult = Apollo.QueryResult<CartQuery, CartQueryVariables>;
export const CartFromQuoteDocument = gql`
    query CartFromQuote($userId: ID!, $shipToAccountId: ID!, $quoteId: ID!, $branchId: ID!) {
  cartFromQuote(
    userId: $userId
    shipToAccountId: $shipToAccountId
    quoteId: $quoteId
    branchId: $branchId
  ) {
    ...Cart
  }
}
    ${CartFragmentDoc}`;

/**
 * __useCartFromQuoteQuery__
 *
 * To run a query within a React component, call `useCartFromQuoteQuery` and pass it any options that fit your needs.
 * When your component renders, `useCartFromQuoteQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useCartFromQuoteQuery({
 *   variables: {
 *      userId: // value for 'userId'
 *      shipToAccountId: // value for 'shipToAccountId'
 *      quoteId: // value for 'quoteId'
 *      branchId: // value for 'branchId'
 *   },
 * });
 */
export function useCartFromQuoteQuery(baseOptions: Apollo.QueryHookOptions<CartFromQuoteQuery, CartFromQuoteQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<CartFromQuoteQuery, CartFromQuoteQueryVariables>(CartFromQuoteDocument, options);
      }
export function useCartFromQuoteLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<CartFromQuoteQuery, CartFromQuoteQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<CartFromQuoteQuery, CartFromQuoteQueryVariables>(CartFromQuoteDocument, options);
        }
export type CartFromQuoteQueryHookResult = ReturnType<typeof useCartFromQuoteQuery>;
export type CartFromQuoteLazyQueryHookResult = ReturnType<typeof useCartFromQuoteLazyQuery>;
export type CartFromQuoteQueryResult = Apollo.QueryResult<CartFromQuoteQuery, CartFromQuoteQueryVariables>;
export const CartUserIdAccountIdDocument = gql`
    query CartUserIdAccountId($userId: ID!, $accountId: ID!, $includeProducts: Boolean) {
  cartUserIdAccountId(
    userId: $userId
    accountId: $accountId
    includeProducts: $includeProducts
  ) {
    ...Cart
  }
}
    ${CartFragmentDoc}`;

/**
 * __useCartUserIdAccountIdQuery__
 *
 * To run a query within a React component, call `useCartUserIdAccountIdQuery` and pass it any options that fit your needs.
 * When your component renders, `useCartUserIdAccountIdQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useCartUserIdAccountIdQuery({
 *   variables: {
 *      userId: // value for 'userId'
 *      accountId: // value for 'accountId'
 *      includeProducts: // value for 'includeProducts'
 *   },
 * });
 */
export function useCartUserIdAccountIdQuery(baseOptions: Apollo.QueryHookOptions<CartUserIdAccountIdQuery, CartUserIdAccountIdQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<CartUserIdAccountIdQuery, CartUserIdAccountIdQueryVariables>(CartUserIdAccountIdDocument, options);
      }
export function useCartUserIdAccountIdLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<CartUserIdAccountIdQuery, CartUserIdAccountIdQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<CartUserIdAccountIdQuery, CartUserIdAccountIdQueryVariables>(CartUserIdAccountIdDocument, options);
        }
export type CartUserIdAccountIdQueryHookResult = ReturnType<typeof useCartUserIdAccountIdQuery>;
export type CartUserIdAccountIdLazyQueryHookResult = ReturnType<typeof useCartUserIdAccountIdLazyQuery>;
export type CartUserIdAccountIdQueryResult = Apollo.QueryResult<CartUserIdAccountIdQuery, CartUserIdAccountIdQueryVariables>;
export const ErpAccountDocument = gql`
    query ErpAccount($accountId: String!, $brand: String!) {
  account(accountId: $accountId, brand: $brand) {
    ...ErpAccount
  }
}
    ${ErpAccountFragmentDoc}`;

/**
 * __useErpAccountQuery__
 *
 * To run a query within a React component, call `useErpAccountQuery` and pass it any options that fit your needs.
 * When your component renders, `useErpAccountQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useErpAccountQuery({
 *   variables: {
 *      accountId: // value for 'accountId'
 *      brand: // value for 'brand'
 *   },
 * });
 */
export function useErpAccountQuery(baseOptions: Apollo.QueryHookOptions<ErpAccountQuery, ErpAccountQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<ErpAccountQuery, ErpAccountQueryVariables>(ErpAccountDocument, options);
      }
export function useErpAccountLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<ErpAccountQuery, ErpAccountQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<ErpAccountQuery, ErpAccountQueryVariables>(ErpAccountDocument, options);
        }
export type ErpAccountQueryHookResult = ReturnType<typeof useErpAccountQuery>;
export type ErpAccountLazyQueryHookResult = ReturnType<typeof useErpAccountLazyQuery>;
export type ErpAccountQueryResult = Apollo.QueryResult<ErpAccountQuery, ErpAccountQueryVariables>;
export const WillCallBranchesDocument = gql`
    query WillCallBranches($shipToAccountId: String!) {
  willCallBranches(shipToAccountId: $shipToAccountId) {
    branchId
    name
    entityId
    address1
    address2
    city
    state
    zip
    phone
    latitude
    longitude
    isPlumbing
    isWaterworks
    isHvac
    isBandK
  }
}
    `;

/**
 * __useWillCallBranchesQuery__
 *
 * To run a query within a React component, call `useWillCallBranchesQuery` and pass it any options that fit your needs.
 * When your component renders, `useWillCallBranchesQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useWillCallBranchesQuery({
 *   variables: {
 *      shipToAccountId: // value for 'shipToAccountId'
 *   },
 * });
 */
export function useWillCallBranchesQuery(baseOptions: Apollo.QueryHookOptions<WillCallBranchesQuery, WillCallBranchesQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<WillCallBranchesQuery, WillCallBranchesQueryVariables>(WillCallBranchesDocument, options);
      }
export function useWillCallBranchesLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<WillCallBranchesQuery, WillCallBranchesQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<WillCallBranchesQuery, WillCallBranchesQueryVariables>(WillCallBranchesDocument, options);
        }
export type WillCallBranchesQueryHookResult = ReturnType<typeof useWillCallBranchesQuery>;
export type WillCallBranchesLazyQueryHookResult = ReturnType<typeof useWillCallBranchesLazyQuery>;
export type WillCallBranchesQueryResult = Apollo.QueryResult<WillCallBranchesQuery, WillCallBranchesQueryVariables>;
export const CreateCartDocument = gql`
    mutation CreateCart($userId: ID!, $shipToAccountId: ID!, $branchId: ID!) {
  createCart(
    userId: $userId
    shipToAccountId: $shipToAccountId
    branchId: $branchId
  )
}
    `;
export type CreateCartMutationFn = Apollo.MutationFunction<CreateCartMutation, CreateCartMutationVariables>;

/**
 * __useCreateCartMutation__
 *
 * To run a mutation, you first call `useCreateCartMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useCreateCartMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [createCartMutation, { data, loading, error }] = useCreateCartMutation({
 *   variables: {
 *      userId: // value for 'userId'
 *      shipToAccountId: // value for 'shipToAccountId'
 *      branchId: // value for 'branchId'
 *   },
 * });
 */
export function useCreateCartMutation(baseOptions?: Apollo.MutationHookOptions<CreateCartMutation, CreateCartMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<CreateCartMutation, CreateCartMutationVariables>(CreateCartDocument, options);
      }
export type CreateCartMutationHookResult = ReturnType<typeof useCreateCartMutation>;
export type CreateCartMutationResult = Apollo.MutationResult<CreateCartMutation>;
export type CreateCartMutationOptions = Apollo.BaseMutationOptions<CreateCartMutation, CreateCartMutationVariables>;
export const AddItemsToCartDocument = gql`
    mutation AddItemsToCart($cartId: ID!, $addItemsInput: AddItemsInput!) {
  addItemsToCart(cartId: $cartId, addItemsInput: $addItemsInput) {
    ...Cart
  }
}
    ${CartFragmentDoc}`;
export type AddItemsToCartMutationFn = Apollo.MutationFunction<AddItemsToCartMutation, AddItemsToCartMutationVariables>;

/**
 * __useAddItemsToCartMutation__
 *
 * To run a mutation, you first call `useAddItemsToCartMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useAddItemsToCartMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [addItemsToCartMutation, { data, loading, error }] = useAddItemsToCartMutation({
 *   variables: {
 *      cartId: // value for 'cartId'
 *      addItemsInput: // value for 'addItemsInput'
 *   },
 * });
 */
export function useAddItemsToCartMutation(baseOptions?: Apollo.MutationHookOptions<AddItemsToCartMutation, AddItemsToCartMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<AddItemsToCartMutation, AddItemsToCartMutationVariables>(AddItemsToCartDocument, options);
      }
export type AddItemsToCartMutationHookResult = ReturnType<typeof useAddItemsToCartMutation>;
export type AddItemsToCartMutationResult = Apollo.MutationResult<AddItemsToCartMutation>;
export type AddItemsToCartMutationOptions = Apollo.BaseMutationOptions<AddItemsToCartMutation, AddItemsToCartMutationVariables>;
export const AddAllListItemsToCartDocument = gql`
    mutation AddAllListItemsToCart($cartId: ID!, $listId: ID!, $addAllToCartUserInput: AddAllToCartUserInput!) {
  addAllListItemsToCart(
    cartId: $cartId
    listId: $listId
    addAllToCartUserInput: $addAllToCartUserInput
  ) {
    ...Cart
  }
}
    ${CartFragmentDoc}`;
export type AddAllListItemsToCartMutationFn = Apollo.MutationFunction<AddAllListItemsToCartMutation, AddAllListItemsToCartMutationVariables>;

/**
 * __useAddAllListItemsToCartMutation__
 *
 * To run a mutation, you first call `useAddAllListItemsToCartMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useAddAllListItemsToCartMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [addAllListItemsToCartMutation, { data, loading, error }] = useAddAllListItemsToCartMutation({
 *   variables: {
 *      cartId: // value for 'cartId'
 *      listId: // value for 'listId'
 *      addAllToCartUserInput: // value for 'addAllToCartUserInput'
 *   },
 * });
 */
export function useAddAllListItemsToCartMutation(baseOptions?: Apollo.MutationHookOptions<AddAllListItemsToCartMutation, AddAllListItemsToCartMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<AddAllListItemsToCartMutation, AddAllListItemsToCartMutationVariables>(AddAllListItemsToCartDocument, options);
      }
export type AddAllListItemsToCartMutationHookResult = ReturnType<typeof useAddAllListItemsToCartMutation>;
export type AddAllListItemsToCartMutationResult = Apollo.MutationResult<AddAllListItemsToCartMutation>;
export type AddAllListItemsToCartMutationOptions = Apollo.BaseMutationOptions<AddAllListItemsToCartMutation, AddAllListItemsToCartMutationVariables>;
export const DeleteItemDocument = gql`
    mutation DeleteItem($cartId: ID!, $itemId: ID!, $userId: ID!, $shipToAccountId: ID!) {
  deleteItem(
    cartId: $cartId
    itemId: $itemId
    userId: $userId
    shipToAccountId: $shipToAccountId
  ) {
    ...Cart
  }
}
    ${CartFragmentDoc}`;
export type DeleteItemMutationFn = Apollo.MutationFunction<DeleteItemMutation, DeleteItemMutationVariables>;

/**
 * __useDeleteItemMutation__
 *
 * To run a mutation, you first call `useDeleteItemMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useDeleteItemMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [deleteItemMutation, { data, loading, error }] = useDeleteItemMutation({
 *   variables: {
 *      cartId: // value for 'cartId'
 *      itemId: // value for 'itemId'
 *      userId: // value for 'userId'
 *      shipToAccountId: // value for 'shipToAccountId'
 *   },
 * });
 */
export function useDeleteItemMutation(baseOptions?: Apollo.MutationHookOptions<DeleteItemMutation, DeleteItemMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<DeleteItemMutation, DeleteItemMutationVariables>(DeleteItemDocument, options);
      }
export type DeleteItemMutationHookResult = ReturnType<typeof useDeleteItemMutation>;
export type DeleteItemMutationResult = Apollo.MutationResult<DeleteItemMutation>;
export type DeleteItemMutationOptions = Apollo.BaseMutationOptions<DeleteItemMutation, DeleteItemMutationVariables>;
export const UpdateCartDocument = gql`
    mutation UpdateCart($cartId: ID!, $cart: CartInput!) {
  updateCart(cartId: $cartId, cart: $cart) {
    ...Cart
  }
}
    ${CartFragmentDoc}`;
export type UpdateCartMutationFn = Apollo.MutationFunction<UpdateCartMutation, UpdateCartMutationVariables>;

/**
 * __useUpdateCartMutation__
 *
 * To run a mutation, you first call `useUpdateCartMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useUpdateCartMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [updateCartMutation, { data, loading, error }] = useUpdateCartMutation({
 *   variables: {
 *      cartId: // value for 'cartId'
 *      cart: // value for 'cart'
 *   },
 * });
 */
export function useUpdateCartMutation(baseOptions?: Apollo.MutationHookOptions<UpdateCartMutation, UpdateCartMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<UpdateCartMutation, UpdateCartMutationVariables>(UpdateCartDocument, options);
      }
export type UpdateCartMutationHookResult = ReturnType<typeof useUpdateCartMutation>;
export type UpdateCartMutationResult = Apollo.MutationResult<UpdateCartMutation>;
export type UpdateCartMutationOptions = Apollo.BaseMutationOptions<UpdateCartMutation, UpdateCartMutationVariables>;
export const RefreshCartDocument = gql`
    mutation RefreshCart($cartId: ID!, $userId: ID!, $shipToAccountId: ID!) {
  refreshCart(cartId: $cartId, userId: $userId, shipToAccountId: $shipToAccountId) {
    ...Cart
  }
}
    ${CartFragmentDoc}`;
export type RefreshCartMutationFn = Apollo.MutationFunction<RefreshCartMutation, RefreshCartMutationVariables>;

/**
 * __useRefreshCartMutation__
 *
 * To run a mutation, you first call `useRefreshCartMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useRefreshCartMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [refreshCartMutation, { data, loading, error }] = useRefreshCartMutation({
 *   variables: {
 *      cartId: // value for 'cartId'
 *      userId: // value for 'userId'
 *      shipToAccountId: // value for 'shipToAccountId'
 *   },
 * });
 */
export function useRefreshCartMutation(baseOptions?: Apollo.MutationHookOptions<RefreshCartMutation, RefreshCartMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<RefreshCartMutation, RefreshCartMutationVariables>(RefreshCartDocument, options);
      }
export type RefreshCartMutationHookResult = ReturnType<typeof useRefreshCartMutation>;
export type RefreshCartMutationResult = Apollo.MutationResult<RefreshCartMutation>;
export type RefreshCartMutationOptions = Apollo.BaseMutationOptions<RefreshCartMutation, RefreshCartMutationVariables>;
export const DeleteCartItemsDocument = gql`
    mutation DeleteCartItems($cartId: ID!) {
  deleteCartItems(cartId: $cartId) {
    ...Cart
  }
}
    ${CartFragmentDoc}`;
export type DeleteCartItemsMutationFn = Apollo.MutationFunction<DeleteCartItemsMutation, DeleteCartItemsMutationVariables>;

/**
 * __useDeleteCartItemsMutation__
 *
 * To run a mutation, you first call `useDeleteCartItemsMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useDeleteCartItemsMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [deleteCartItemsMutation, { data, loading, error }] = useDeleteCartItemsMutation({
 *   variables: {
 *      cartId: // value for 'cartId'
 *   },
 * });
 */
export function useDeleteCartItemsMutation(baseOptions?: Apollo.MutationHookOptions<DeleteCartItemsMutation, DeleteCartItemsMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<DeleteCartItemsMutation, DeleteCartItemsMutationVariables>(DeleteCartItemsDocument, options);
      }
export type DeleteCartItemsMutationHookResult = ReturnType<typeof useDeleteCartItemsMutation>;
export type DeleteCartItemsMutationResult = Apollo.MutationResult<DeleteCartItemsMutation>;
export type DeleteCartItemsMutationOptions = Apollo.BaseMutationOptions<DeleteCartItemsMutation, DeleteCartItemsMutationVariables>;
export const UpdateDeliveryDocument = gql`
    mutation UpdateDelivery($deliveryInfo: DeliveryInput!) {
  updateDelivery(deliveryInfo: $deliveryInfo) {
    ...Cart
  }
}
    ${CartFragmentDoc}`;
export type UpdateDeliveryMutationFn = Apollo.MutationFunction<UpdateDeliveryMutation, UpdateDeliveryMutationVariables>;

/**
 * __useUpdateDeliveryMutation__
 *
 * To run a mutation, you first call `useUpdateDeliveryMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useUpdateDeliveryMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [updateDeliveryMutation, { data, loading, error }] = useUpdateDeliveryMutation({
 *   variables: {
 *      deliveryInfo: // value for 'deliveryInfo'
 *   },
 * });
 */
export function useUpdateDeliveryMutation(baseOptions?: Apollo.MutationHookOptions<UpdateDeliveryMutation, UpdateDeliveryMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<UpdateDeliveryMutation, UpdateDeliveryMutationVariables>(UpdateDeliveryDocument, options);
      }
export type UpdateDeliveryMutationHookResult = ReturnType<typeof useUpdateDeliveryMutation>;
export type UpdateDeliveryMutationResult = Apollo.MutationResult<UpdateDeliveryMutation>;
export type UpdateDeliveryMutationOptions = Apollo.BaseMutationOptions<UpdateDeliveryMutation, UpdateDeliveryMutationVariables>;
export const UpdateWillCallDocument = gql`
    mutation UpdateWillCall($willCallInfo: WillCallInput!) {
  updateWillCall(willCallInfo: $willCallInfo) {
    ...Cart
  }
}
    ${CartFragmentDoc}`;
export type UpdateWillCallMutationFn = Apollo.MutationFunction<UpdateWillCallMutation, UpdateWillCallMutationVariables>;

/**
 * __useUpdateWillCallMutation__
 *
 * To run a mutation, you first call `useUpdateWillCallMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useUpdateWillCallMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [updateWillCallMutation, { data, loading, error }] = useUpdateWillCallMutation({
 *   variables: {
 *      willCallInfo: // value for 'willCallInfo'
 *   },
 * });
 */
export function useUpdateWillCallMutation(baseOptions?: Apollo.MutationHookOptions<UpdateWillCallMutation, UpdateWillCallMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<UpdateWillCallMutation, UpdateWillCallMutationVariables>(UpdateWillCallDocument, options);
      }
export type UpdateWillCallMutationHookResult = ReturnType<typeof useUpdateWillCallMutation>;
export type UpdateWillCallMutationResult = Apollo.MutationResult<UpdateWillCallMutation>;
export type UpdateWillCallMutationOptions = Apollo.BaseMutationOptions<UpdateWillCallMutation, UpdateWillCallMutationVariables>;
export const UpdateWillCallBranchDocument = gql`
    mutation UpdateWillCallBranch($cartId: ID!, $branchId: ID!, $userId: ID!, $shipToAccountId: ID!) {
  updateWillCallBranch(
    cartId: $cartId
    branchId: $branchId
    userId: $userId
    shipToAccountId: $shipToAccountId
  ) {
    ...Cart
  }
}
    ${CartFragmentDoc}`;
export type UpdateWillCallBranchMutationFn = Apollo.MutationFunction<UpdateWillCallBranchMutation, UpdateWillCallBranchMutationVariables>;

/**
 * __useUpdateWillCallBranchMutation__
 *
 * To run a mutation, you first call `useUpdateWillCallBranchMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useUpdateWillCallBranchMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [updateWillCallBranchMutation, { data, loading, error }] = useUpdateWillCallBranchMutation({
 *   variables: {
 *      cartId: // value for 'cartId'
 *      branchId: // value for 'branchId'
 *      userId: // value for 'userId'
 *      shipToAccountId: // value for 'shipToAccountId'
 *   },
 * });
 */
export function useUpdateWillCallBranchMutation(baseOptions?: Apollo.MutationHookOptions<UpdateWillCallBranchMutation, UpdateWillCallBranchMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<UpdateWillCallBranchMutation, UpdateWillCallBranchMutationVariables>(UpdateWillCallBranchDocument, options);
      }
export type UpdateWillCallBranchMutationHookResult = ReturnType<typeof useUpdateWillCallBranchMutation>;
export type UpdateWillCallBranchMutationResult = Apollo.MutationResult<UpdateWillCallBranchMutation>;
export type UpdateWillCallBranchMutationOptions = Apollo.BaseMutationOptions<UpdateWillCallBranchMutation, UpdateWillCallBranchMutationVariables>;
export const UpdateItemQuantityDocument = gql`
    mutation UpdateItemQuantity($cartId: ID!, $itemId: ID!, $quantity: Int!, $minIncrementQty: Int!, $userId: ID!, $shipToAccountId: ID!, $customerNumber: String) {
  updateItemQuantity(
    cartId: $cartId
    itemId: $itemId
    quantity: $quantity
    minIncrementQty: $minIncrementQty
    userId: $userId
    shipToAccountId: $shipToAccountId
    customerNumber: $customerNumber
  ) {
    ...UpdateItemQtyResponse
  }
}
    ${UpdateItemQtyResponseFragmentDoc}`;
export type UpdateItemQuantityMutationFn = Apollo.MutationFunction<UpdateItemQuantityMutation, UpdateItemQuantityMutationVariables>;

/**
 * __useUpdateItemQuantityMutation__
 *
 * To run a mutation, you first call `useUpdateItemQuantityMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useUpdateItemQuantityMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [updateItemQuantityMutation, { data, loading, error }] = useUpdateItemQuantityMutation({
 *   variables: {
 *      cartId: // value for 'cartId'
 *      itemId: // value for 'itemId'
 *      quantity: // value for 'quantity'
 *      minIncrementQty: // value for 'minIncrementQty'
 *      userId: // value for 'userId'
 *      shipToAccountId: // value for 'shipToAccountId'
 *      customerNumber: // value for 'customerNumber'
 *   },
 * });
 */
export function useUpdateItemQuantityMutation(baseOptions?: Apollo.MutationHookOptions<UpdateItemQuantityMutation, UpdateItemQuantityMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<UpdateItemQuantityMutation, UpdateItemQuantityMutationVariables>(UpdateItemQuantityDocument, options);
      }
export type UpdateItemQuantityMutationHookResult = ReturnType<typeof useUpdateItemQuantityMutation>;
export type UpdateItemQuantityMutationResult = Apollo.MutationResult<UpdateItemQuantityMutation>;
export type UpdateItemQuantityMutationOptions = Apollo.BaseMutationOptions<UpdateItemQuantityMutation, UpdateItemQuantityMutationVariables>;
export const SubmitOrderPreviewDocument = gql`
    mutation SubmitOrderPreview($cartId: ID!, $userId: ID!, $shipToAccountId: ID!, $billToAccountId: ID!) {
  submitOrderPreview(
    cartId: $cartId
    userId: $userId
    shipToAccountId: $shipToAccountId
    billToAccountId: $billToAccountId
  ) {
    subTotal
    tax
    orderTotal
  }
}
    `;
export type SubmitOrderPreviewMutationFn = Apollo.MutationFunction<SubmitOrderPreviewMutation, SubmitOrderPreviewMutationVariables>;

/**
 * __useSubmitOrderPreviewMutation__
 *
 * To run a mutation, you first call `useSubmitOrderPreviewMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useSubmitOrderPreviewMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [submitOrderPreviewMutation, { data, loading, error }] = useSubmitOrderPreviewMutation({
 *   variables: {
 *      cartId: // value for 'cartId'
 *      userId: // value for 'userId'
 *      shipToAccountId: // value for 'shipToAccountId'
 *      billToAccountId: // value for 'billToAccountId'
 *   },
 * });
 */
export function useSubmitOrderPreviewMutation(baseOptions?: Apollo.MutationHookOptions<SubmitOrderPreviewMutation, SubmitOrderPreviewMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<SubmitOrderPreviewMutation, SubmitOrderPreviewMutationVariables>(SubmitOrderPreviewDocument, options);
      }
export type SubmitOrderPreviewMutationHookResult = ReturnType<typeof useSubmitOrderPreviewMutation>;
export type SubmitOrderPreviewMutationResult = Apollo.MutationResult<SubmitOrderPreviewMutation>;
export type SubmitOrderPreviewMutationOptions = Apollo.BaseMutationOptions<SubmitOrderPreviewMutation, SubmitOrderPreviewMutationVariables>;
export const SubmitOrderDocument = gql`
    mutation SubmitOrder($cartId: ID!, $userId: ID!, $shipToAccountId: ID!, $billToAccountId: ID!) {
  submitOrder(
    cartId: $cartId
    userId: $userId
    shipToAccountId: $shipToAccountId
    billToAccountId: $billToAccountId
  ) {
    ...SubmitOrderResponse
  }
}
    ${SubmitOrderResponseFragmentDoc}`;
export type SubmitOrderMutationFn = Apollo.MutationFunction<SubmitOrderMutation, SubmitOrderMutationVariables>;

/**
 * __useSubmitOrderMutation__
 *
 * To run a mutation, you first call `useSubmitOrderMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useSubmitOrderMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [submitOrderMutation, { data, loading, error }] = useSubmitOrderMutation({
 *   variables: {
 *      cartId: // value for 'cartId'
 *      userId: // value for 'userId'
 *      shipToAccountId: // value for 'shipToAccountId'
 *      billToAccountId: // value for 'billToAccountId'
 *   },
 * });
 */
export function useSubmitOrderMutation(baseOptions?: Apollo.MutationHookOptions<SubmitOrderMutation, SubmitOrderMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<SubmitOrderMutation, SubmitOrderMutationVariables>(SubmitOrderDocument, options);
      }
export type SubmitOrderMutationHookResult = ReturnType<typeof useSubmitOrderMutation>;
export type SubmitOrderMutationResult = Apollo.MutationResult<SubmitOrderMutation>;
export type SubmitOrderMutationOptions = Apollo.BaseMutationOptions<SubmitOrderMutation, SubmitOrderMutationVariables>;
export const DeleteCreditCardFromCartDocument = gql`
    mutation DeleteCreditCardFromCart($cartId: ID!) {
  deleteCreditCardFromCart(cartId: $cartId) {
    ...Cart
  }
}
    ${CartFragmentDoc}`;
export type DeleteCreditCardFromCartMutationFn = Apollo.MutationFunction<DeleteCreditCardFromCartMutation, DeleteCreditCardFromCartMutationVariables>;

/**
 * __useDeleteCreditCardFromCartMutation__
 *
 * To run a mutation, you first call `useDeleteCreditCardFromCartMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useDeleteCreditCardFromCartMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [deleteCreditCardFromCartMutation, { data, loading, error }] = useDeleteCreditCardFromCartMutation({
 *   variables: {
 *      cartId: // value for 'cartId'
 *   },
 * });
 */
export function useDeleteCreditCardFromCartMutation(baseOptions?: Apollo.MutationHookOptions<DeleteCreditCardFromCartMutation, DeleteCreditCardFromCartMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<DeleteCreditCardFromCartMutation, DeleteCreditCardFromCartMutationVariables>(DeleteCreditCardFromCartDocument, options);
      }
export type DeleteCreditCardFromCartMutationHookResult = ReturnType<typeof useDeleteCreditCardFromCartMutation>;
export type DeleteCreditCardFromCartMutationResult = Apollo.MutationResult<DeleteCreditCardFromCartMutation>;
export type DeleteCreditCardFromCartMutationOptions = Apollo.BaseMutationOptions<DeleteCreditCardFromCartMutation, DeleteCreditCardFromCartMutationVariables>;
export const CategoriesDocument = gql`
    query Categories($erp: String) {
  categories(erp: $erp) {
    mincronCategories
    eclipseCategories
  }
}
    `;

/**
 * __useCategoriesQuery__
 *
 * To run a query within a React component, call `useCategoriesQuery` and pass it any options that fit your needs.
 * When your component renders, `useCategoriesQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useCategoriesQuery({
 *   variables: {
 *      erp: // value for 'erp'
 *   },
 * });
 */
export function useCategoriesQuery(baseOptions?: Apollo.QueryHookOptions<CategoriesQuery, CategoriesQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<CategoriesQuery, CategoriesQueryVariables>(CategoriesDocument, options);
      }
export function useCategoriesLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<CategoriesQuery, CategoriesQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<CategoriesQuery, CategoriesQueryVariables>(CategoriesDocument, options);
        }
export type CategoriesQueryHookResult = ReturnType<typeof useCategoriesQuery>;
export type CategoriesLazyQueryHookResult = ReturnType<typeof useCategoriesLazyQuery>;
export type CategoriesQueryResult = Apollo.QueryResult<CategoriesQuery, CategoriesQueryVariables>;
export const ProductCategoriesDocument = gql`
    query ProductCategories($engine: String!) {
  productCategories(engine: $engine) {
    categories {
      name
      children {
        name
        children {
          name
        }
      }
    }
  }
}
    `;

/**
 * __useProductCategoriesQuery__
 *
 * To run a query within a React component, call `useProductCategoriesQuery` and pass it any options that fit your needs.
 * When your component renders, `useProductCategoriesQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useProductCategoriesQuery({
 *   variables: {
 *      engine: // value for 'engine'
 *   },
 * });
 */
export function useProductCategoriesQuery(baseOptions: Apollo.QueryHookOptions<ProductCategoriesQuery, ProductCategoriesQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<ProductCategoriesQuery, ProductCategoriesQueryVariables>(ProductCategoriesDocument, options);
      }
export function useProductCategoriesLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<ProductCategoriesQuery, ProductCategoriesQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<ProductCategoriesQuery, ProductCategoriesQueryVariables>(ProductCategoriesDocument, options);
        }
export type ProductCategoriesQueryHookResult = ReturnType<typeof useProductCategoriesQuery>;
export type ProductCategoriesLazyQueryHookResult = ReturnType<typeof useProductCategoriesLazyQuery>;
export type ProductCategoriesQueryResult = Apollo.QueryResult<ProductCategoriesQuery, ProductCategoriesQueryVariables>;
export const SubmitContractOrderReviewDocument = gql`
    query submitContractOrderReview($orderReview: OrderReviewInput) {
  submitContractOrderReview(orderReview: $orderReview) {
    ...ContractOrderDetail
  }
}
    ${ContractOrderDetailFragmentDoc}`;

/**
 * __useSubmitContractOrderReviewQuery__
 *
 * To run a query within a React component, call `useSubmitContractOrderReviewQuery` and pass it any options that fit your needs.
 * When your component renders, `useSubmitContractOrderReviewQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useSubmitContractOrderReviewQuery({
 *   variables: {
 *      orderReview: // value for 'orderReview'
 *   },
 * });
 */
export function useSubmitContractOrderReviewQuery(baseOptions?: Apollo.QueryHookOptions<SubmitContractOrderReviewQuery, SubmitContractOrderReviewQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<SubmitContractOrderReviewQuery, SubmitContractOrderReviewQueryVariables>(SubmitContractOrderReviewDocument, options);
      }
export function useSubmitContractOrderReviewLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<SubmitContractOrderReviewQuery, SubmitContractOrderReviewQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<SubmitContractOrderReviewQuery, SubmitContractOrderReviewQueryVariables>(SubmitContractOrderReviewDocument, options);
        }
export type SubmitContractOrderReviewQueryHookResult = ReturnType<typeof useSubmitContractOrderReviewQuery>;
export type SubmitContractOrderReviewLazyQueryHookResult = ReturnType<typeof useSubmitContractOrderReviewLazyQuery>;
export type SubmitContractOrderReviewQueryResult = Apollo.QueryResult<SubmitContractOrderReviewQuery, SubmitContractOrderReviewQueryVariables>;
export const DeleteContractCartDocument = gql`
    mutation deleteContractCart($application: String!, $accountId: String!, $userId: String!, $shoppingCartId: String!, $branchNumber: String!) {
  deleteContractCart(
    application: $application
    accountId: $accountId
    userId: $userId
    shoppingCartId: $shoppingCartId
    branchNumber: $branchNumber
  )
}
    `;
export type DeleteContractCartMutationFn = Apollo.MutationFunction<DeleteContractCartMutation, DeleteContractCartMutationVariables>;

/**
 * __useDeleteContractCartMutation__
 *
 * To run a mutation, you first call `useDeleteContractCartMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useDeleteContractCartMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [deleteContractCartMutation, { data, loading, error }] = useDeleteContractCartMutation({
 *   variables: {
 *      application: // value for 'application'
 *      accountId: // value for 'accountId'
 *      userId: // value for 'userId'
 *      shoppingCartId: // value for 'shoppingCartId'
 *      branchNumber: // value for 'branchNumber'
 *   },
 * });
 */
export function useDeleteContractCartMutation(baseOptions?: Apollo.MutationHookOptions<DeleteContractCartMutation, DeleteContractCartMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<DeleteContractCartMutation, DeleteContractCartMutationVariables>(DeleteContractCartDocument, options);
      }
export type DeleteContractCartMutationHookResult = ReturnType<typeof useDeleteContractCartMutation>;
export type DeleteContractCartMutationResult = Apollo.MutationResult<DeleteContractCartMutation>;
export type DeleteContractCartMutationOptions = Apollo.BaseMutationOptions<DeleteContractCartMutation, DeleteContractCartMutationVariables>;
export const SubmitContractOrderFromCartDocument = gql`
    query submitContractOrderFromCart($contractOrderSubmit: ContractOrderSubmitInput, $application: String!, $accountId: String!, $userId: String!, $shoppingCartId: String!) {
  submitContractOrderFromCart(
    contractOrderSubmit: $contractOrderSubmit
    application: $application
    accountId: $accountId
    userId: $userId
    shoppingCartId: $shoppingCartId
  )
}
    `;

/**
 * __useSubmitContractOrderFromCartQuery__
 *
 * To run a query within a React component, call `useSubmitContractOrderFromCartQuery` and pass it any options that fit your needs.
 * When your component renders, `useSubmitContractOrderFromCartQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useSubmitContractOrderFromCartQuery({
 *   variables: {
 *      contractOrderSubmit: // value for 'contractOrderSubmit'
 *      application: // value for 'application'
 *      accountId: // value for 'accountId'
 *      userId: // value for 'userId'
 *      shoppingCartId: // value for 'shoppingCartId'
 *   },
 * });
 */
export function useSubmitContractOrderFromCartQuery(baseOptions: Apollo.QueryHookOptions<SubmitContractOrderFromCartQuery, SubmitContractOrderFromCartQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<SubmitContractOrderFromCartQuery, SubmitContractOrderFromCartQueryVariables>(SubmitContractOrderFromCartDocument, options);
      }
export function useSubmitContractOrderFromCartLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<SubmitContractOrderFromCartQuery, SubmitContractOrderFromCartQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<SubmitContractOrderFromCartQuery, SubmitContractOrderFromCartQueryVariables>(SubmitContractOrderFromCartDocument, options);
        }
export type SubmitContractOrderFromCartQueryHookResult = ReturnType<typeof useSubmitContractOrderFromCartQuery>;
export type SubmitContractOrderFromCartLazyQueryHookResult = ReturnType<typeof useSubmitContractOrderFromCartLazyQuery>;
export type SubmitContractOrderFromCartQueryResult = Apollo.QueryResult<SubmitContractOrderFromCartQuery, SubmitContractOrderFromCartQueryVariables>;
export const GetContractDetailsDocument = gql`
    query GetContractDetails($contractNumber: String!, $erpAccountId: String!) {
  contract(contractNumber: $contractNumber, erpAccountId: $erpAccountId) {
    ...contractDetails
  }
}
    ${ContractDetailsFragmentDoc}`;

/**
 * __useGetContractDetailsQuery__
 *
 * To run a query within a React component, call `useGetContractDetailsQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetContractDetailsQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetContractDetailsQuery({
 *   variables: {
 *      contractNumber: // value for 'contractNumber'
 *      erpAccountId: // value for 'erpAccountId'
 *   },
 * });
 */
export function useGetContractDetailsQuery(baseOptions: Apollo.QueryHookOptions<GetContractDetailsQuery, GetContractDetailsQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetContractDetailsQuery, GetContractDetailsQueryVariables>(GetContractDetailsDocument, options);
      }
export function useGetContractDetailsLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetContractDetailsQuery, GetContractDetailsQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetContractDetailsQuery, GetContractDetailsQueryVariables>(GetContractDetailsDocument, options);
        }
export type GetContractDetailsQueryHookResult = ReturnType<typeof useGetContractDetailsQuery>;
export type GetContractDetailsLazyQueryHookResult = ReturnType<typeof useGetContractDetailsLazyQuery>;
export type GetContractDetailsQueryResult = Apollo.QueryResult<GetContractDetailsQuery, GetContractDetailsQueryVariables>;
export const ContractsDocument = gql`
    query Contracts($erpAccountId: String!, $pageNumber: String!, $searchFilter: String, $fromDate: String, $toDate: String, $sortOrder: String, $sortDirection: String) {
  contracts(
    erpAccountId: $erpAccountId
    pageNumber: $pageNumber
    searchFilter: $searchFilter
    fromDate: $fromDate
    toDate: $toDate
    sortOrder: $sortOrder
    sortDirection: $sortDirection
  ) {
    rowsReturned
    totalRows
    startRow
    results {
      contractNumber
      description
      contractDate
      promiseDate
      firstReleaseDate
      lastReleaseDate
      jobNumber
      jobName
      purchaseOrderNumber
    }
  }
}
    `;

/**
 * __useContractsQuery__
 *
 * To run a query within a React component, call `useContractsQuery` and pass it any options that fit your needs.
 * When your component renders, `useContractsQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useContractsQuery({
 *   variables: {
 *      erpAccountId: // value for 'erpAccountId'
 *      pageNumber: // value for 'pageNumber'
 *      searchFilter: // value for 'searchFilter'
 *      fromDate: // value for 'fromDate'
 *      toDate: // value for 'toDate'
 *      sortOrder: // value for 'sortOrder'
 *      sortDirection: // value for 'sortDirection'
 *   },
 * });
 */
export function useContractsQuery(baseOptions: Apollo.QueryHookOptions<ContractsQuery, ContractsQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<ContractsQuery, ContractsQueryVariables>(ContractsDocument, options);
      }
export function useContractsLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<ContractsQuery, ContractsQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<ContractsQuery, ContractsQueryVariables>(ContractsDocument, options);
        }
export type ContractsQueryHookResult = ReturnType<typeof useContractsQuery>;
export type ContractsLazyQueryHookResult = ReturnType<typeof useContractsLazyQuery>;
export type ContractsQueryResult = Apollo.QueryResult<ContractsQuery, ContractsQueryVariables>;
export const CreditCardSetupUrlDocument = gql`
    query CreditCardSetupUrl($accountId: String!, $cardHolderInput: CardHolderInput!) {
  creditCardSetupUrl(accountId: $accountId, cardHolderInput: $cardHolderInput) {
    elementSetupUrl
    elementSetupId
  }
}
    `;

/**
 * __useCreditCardSetupUrlQuery__
 *
 * To run a query within a React component, call `useCreditCardSetupUrlQuery` and pass it any options that fit your needs.
 * When your component renders, `useCreditCardSetupUrlQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useCreditCardSetupUrlQuery({
 *   variables: {
 *      accountId: // value for 'accountId'
 *      cardHolderInput: // value for 'cardHolderInput'
 *   },
 * });
 */
export function useCreditCardSetupUrlQuery(baseOptions: Apollo.QueryHookOptions<CreditCardSetupUrlQuery, CreditCardSetupUrlQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<CreditCardSetupUrlQuery, CreditCardSetupUrlQueryVariables>(CreditCardSetupUrlDocument, options);
      }
export function useCreditCardSetupUrlLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<CreditCardSetupUrlQuery, CreditCardSetupUrlQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<CreditCardSetupUrlQuery, CreditCardSetupUrlQueryVariables>(CreditCardSetupUrlDocument, options);
        }
export type CreditCardSetupUrlQueryHookResult = ReturnType<typeof useCreditCardSetupUrlQuery>;
export type CreditCardSetupUrlLazyQueryHookResult = ReturnType<typeof useCreditCardSetupUrlLazyQuery>;
export type CreditCardSetupUrlQueryResult = Apollo.QueryResult<CreditCardSetupUrlQuery, CreditCardSetupUrlQueryVariables>;
export const CreditCardElementInfoDocument = gql`
    query CreditCardElementInfo($accountId: String!, $elementSetupId: String!) {
  creditCardElementInfo(accountId: $accountId, elementSetupId: $elementSetupId) {
    creditCard {
      ...CreditCard
    }
  }
}
    ${CreditCardFragmentDoc}`;

/**
 * __useCreditCardElementInfoQuery__
 *
 * To run a query within a React component, call `useCreditCardElementInfoQuery` and pass it any options that fit your needs.
 * When your component renders, `useCreditCardElementInfoQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useCreditCardElementInfoQuery({
 *   variables: {
 *      accountId: // value for 'accountId'
 *      elementSetupId: // value for 'elementSetupId'
 *   },
 * });
 */
export function useCreditCardElementInfoQuery(baseOptions: Apollo.QueryHookOptions<CreditCardElementInfoQuery, CreditCardElementInfoQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<CreditCardElementInfoQuery, CreditCardElementInfoQueryVariables>(CreditCardElementInfoDocument, options);
      }
export function useCreditCardElementInfoLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<CreditCardElementInfoQuery, CreditCardElementInfoQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<CreditCardElementInfoQuery, CreditCardElementInfoQueryVariables>(CreditCardElementInfoDocument, options);
        }
export type CreditCardElementInfoQueryHookResult = ReturnType<typeof useCreditCardElementInfoQuery>;
export type CreditCardElementInfoLazyQueryHookResult = ReturnType<typeof useCreditCardElementInfoLazyQuery>;
export type CreditCardElementInfoQueryResult = Apollo.QueryResult<CreditCardElementInfoQuery, CreditCardElementInfoQueryVariables>;
export const CreditCardListDocument = gql`
    query CreditCardList($accountId: String!) {
  creditCardList(accountId: $accountId) {
    creditCardList {
      creditCard {
        ...CreditCard
      }
    }
  }
}
    ${CreditCardFragmentDoc}`;

/**
 * __useCreditCardListQuery__
 *
 * To run a query within a React component, call `useCreditCardListQuery` and pass it any options that fit your needs.
 * When your component renders, `useCreditCardListQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useCreditCardListQuery({
 *   variables: {
 *      accountId: // value for 'accountId'
 *   },
 * });
 */
export function useCreditCardListQuery(baseOptions: Apollo.QueryHookOptions<CreditCardListQuery, CreditCardListQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<CreditCardListQuery, CreditCardListQueryVariables>(CreditCardListDocument, options);
      }
export function useCreditCardListLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<CreditCardListQuery, CreditCardListQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<CreditCardListQuery, CreditCardListQueryVariables>(CreditCardListDocument, options);
        }
export type CreditCardListQueryHookResult = ReturnType<typeof useCreditCardListQuery>;
export type CreditCardListLazyQueryHookResult = ReturnType<typeof useCreditCardListLazyQuery>;
export type CreditCardListQueryResult = Apollo.QueryResult<CreditCardListQuery, CreditCardListQueryVariables>;
export const AddCreditCardDocument = gql`
    mutation AddCreditCard($accountId: String!, $creditCard: CreditCardInput!) {
  addCreditCard(accountId: $accountId, creditCard: $creditCard) {
    statusResult {
      success
      description
    }
    creditCardList {
      creditCard {
        ...CreditCard
      }
    }
  }
}
    ${CreditCardFragmentDoc}`;
export type AddCreditCardMutationFn = Apollo.MutationFunction<AddCreditCardMutation, AddCreditCardMutationVariables>;

/**
 * __useAddCreditCardMutation__
 *
 * To run a mutation, you first call `useAddCreditCardMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useAddCreditCardMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [addCreditCardMutation, { data, loading, error }] = useAddCreditCardMutation({
 *   variables: {
 *      accountId: // value for 'accountId'
 *      creditCard: // value for 'creditCard'
 *   },
 * });
 */
export function useAddCreditCardMutation(baseOptions?: Apollo.MutationHookOptions<AddCreditCardMutation, AddCreditCardMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<AddCreditCardMutation, AddCreditCardMutationVariables>(AddCreditCardDocument, options);
      }
export type AddCreditCardMutationHookResult = ReturnType<typeof useAddCreditCardMutation>;
export type AddCreditCardMutationResult = Apollo.MutationResult<AddCreditCardMutation>;
export type AddCreditCardMutationOptions = Apollo.BaseMutationOptions<AddCreditCardMutation, AddCreditCardMutationVariables>;
export const DeleteCreditCardDocument = gql`
    mutation DeleteCreditCard($accountId: String!, $creditCardId: ID!) {
  deleteCreditCard(accountId: $accountId, creditCardId: $creditCardId)
}
    `;
export type DeleteCreditCardMutationFn = Apollo.MutationFunction<DeleteCreditCardMutation, DeleteCreditCardMutationVariables>;

/**
 * __useDeleteCreditCardMutation__
 *
 * To run a mutation, you first call `useDeleteCreditCardMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useDeleteCreditCardMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [deleteCreditCardMutation, { data, loading, error }] = useDeleteCreditCardMutation({
 *   variables: {
 *      accountId: // value for 'accountId'
 *      creditCardId: // value for 'creditCardId'
 *   },
 * });
 */
export function useDeleteCreditCardMutation(baseOptions?: Apollo.MutationHookOptions<DeleteCreditCardMutation, DeleteCreditCardMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<DeleteCreditCardMutation, DeleteCreditCardMutationVariables>(DeleteCreditCardDocument, options);
      }
export type DeleteCreditCardMutationHookResult = ReturnType<typeof useDeleteCreditCardMutation>;
export type DeleteCreditCardMutationResult = Apollo.MutationResult<DeleteCreditCardMutation>;
export type DeleteCreditCardMutationOptions = Apollo.BaseMutationOptions<DeleteCreditCardMutation, DeleteCreditCardMutationVariables>;
export const GetAllUnapprovedAccountRequestsDocument = gql`
    query getAllUnapprovedAccountRequests {
  allUnapprovedAccountRequests {
    id
    email
    firstName
    lastName
    branchId
    phoneNumber
    phoneType
    createdAt
    companyName
    accountNumber
  }
}
    `;

/**
 * __useGetAllUnapprovedAccountRequestsQuery__
 *
 * To run a query within a React component, call `useGetAllUnapprovedAccountRequestsQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetAllUnapprovedAccountRequestsQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetAllUnapprovedAccountRequestsQuery({
 *   variables: {
 *   },
 * });
 */
export function useGetAllUnapprovedAccountRequestsQuery(baseOptions?: Apollo.QueryHookOptions<GetAllUnapprovedAccountRequestsQuery, GetAllUnapprovedAccountRequestsQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetAllUnapprovedAccountRequestsQuery, GetAllUnapprovedAccountRequestsQueryVariables>(GetAllUnapprovedAccountRequestsDocument, options);
      }
export function useGetAllUnapprovedAccountRequestsLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetAllUnapprovedAccountRequestsQuery, GetAllUnapprovedAccountRequestsQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetAllUnapprovedAccountRequestsQuery, GetAllUnapprovedAccountRequestsQueryVariables>(GetAllUnapprovedAccountRequestsDocument, options);
        }
export type GetAllUnapprovedAccountRequestsQueryHookResult = ReturnType<typeof useGetAllUnapprovedAccountRequestsQuery>;
export type GetAllUnapprovedAccountRequestsLazyQueryHookResult = ReturnType<typeof useGetAllUnapprovedAccountRequestsLazyQuery>;
export type GetAllUnapprovedAccountRequestsQueryResult = Apollo.QueryResult<GetAllUnapprovedAccountRequestsQuery, GetAllUnapprovedAccountRequestsQueryVariables>;
export const FeaturesDocument = gql`
    query features {
  features {
    id
    name
    isEnabled
  }
}
    `;

/**
 * __useFeaturesQuery__
 *
 * To run a query within a React component, call `useFeaturesQuery` and pass it any options that fit your needs.
 * When your component renders, `useFeaturesQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useFeaturesQuery({
 *   variables: {
 *   },
 * });
 */
export function useFeaturesQuery(baseOptions?: Apollo.QueryHookOptions<FeaturesQuery, FeaturesQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<FeaturesQuery, FeaturesQueryVariables>(FeaturesDocument, options);
      }
export function useFeaturesLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<FeaturesQuery, FeaturesQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<FeaturesQuery, FeaturesQueryVariables>(FeaturesDocument, options);
        }
export type FeaturesQueryHookResult = ReturnType<typeof useFeaturesQuery>;
export type FeaturesLazyQueryHookResult = ReturnType<typeof useFeaturesLazyQuery>;
export type FeaturesQueryResult = Apollo.QueryResult<FeaturesQuery, FeaturesQueryVariables>;
export const SetFeatureEnabledDocument = gql`
    mutation SetFeatureEnabled($featureId: String!, $setFeatureEnabledInput: SetFeatureEnabledInput!) {
  setFeatureEnabled(
    featureId: $featureId
    setFeatureEnabledInput: $setFeatureEnabledInput
  )
}
    `;
export type SetFeatureEnabledMutationFn = Apollo.MutationFunction<SetFeatureEnabledMutation, SetFeatureEnabledMutationVariables>;

/**
 * __useSetFeatureEnabledMutation__
 *
 * To run a mutation, you first call `useSetFeatureEnabledMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useSetFeatureEnabledMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [setFeatureEnabledMutation, { data, loading, error }] = useSetFeatureEnabledMutation({
 *   variables: {
 *      featureId: // value for 'featureId'
 *      setFeatureEnabledInput: // value for 'setFeatureEnabledInput'
 *   },
 * });
 */
export function useSetFeatureEnabledMutation(baseOptions?: Apollo.MutationHookOptions<SetFeatureEnabledMutation, SetFeatureEnabledMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<SetFeatureEnabledMutation, SetFeatureEnabledMutationVariables>(SetFeatureEnabledDocument, options);
      }
export type SetFeatureEnabledMutationHookResult = ReturnType<typeof useSetFeatureEnabledMutation>;
export type SetFeatureEnabledMutationResult = Apollo.MutationResult<SetFeatureEnabledMutation>;
export type SetFeatureEnabledMutationOptions = Apollo.BaseMutationOptions<SetFeatureEnabledMutation, SetFeatureEnabledMutationVariables>;
export const AccountErpIdDocument = gql`
    query AccountErpId($accountId: String!, $brand: String!) {
  account(accountId: $accountId, brand: $brand) {
    erpAccountId
  }
}
    `;

/**
 * __useAccountErpIdQuery__
 *
 * To run a query within a React component, call `useAccountErpIdQuery` and pass it any options that fit your needs.
 * When your component renders, `useAccountErpIdQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useAccountErpIdQuery({
 *   variables: {
 *      accountId: // value for 'accountId'
 *      brand: // value for 'brand'
 *   },
 * });
 */
export function useAccountErpIdQuery(baseOptions: Apollo.QueryHookOptions<AccountErpIdQuery, AccountErpIdQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<AccountErpIdQuery, AccountErpIdQueryVariables>(AccountErpIdDocument, options);
      }
export function useAccountErpIdLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<AccountErpIdQuery, AccountErpIdQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<AccountErpIdQuery, AccountErpIdQueryVariables>(AccountErpIdDocument, options);
        }
export type AccountErpIdQueryHookResult = ReturnType<typeof useAccountErpIdQuery>;
export type AccountErpIdLazyQueryHookResult = ReturnType<typeof useAccountErpIdLazyQuery>;
export type AccountErpIdQueryResult = Apollo.QueryResult<AccountErpIdQuery, AccountErpIdQueryVariables>;
export const InviteUserDocument = gql`
    mutation InviteUser($inviteUserInput: InviteUserInput!) {
  inviteUser(inviteUserInput: $inviteUserInput) {
    firstName
    lastName
    email
    userRoleId
    approverId
  }
}
    `;
export type InviteUserMutationFn = Apollo.MutationFunction<InviteUserMutation, InviteUserMutationVariables>;

/**
 * __useInviteUserMutation__
 *
 * To run a mutation, you first call `useInviteUserMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useInviteUserMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [inviteUserMutation, { data, loading, error }] = useInviteUserMutation({
 *   variables: {
 *      inviteUserInput: // value for 'inviteUserInput'
 *   },
 * });
 */
export function useInviteUserMutation(baseOptions?: Apollo.MutationHookOptions<InviteUserMutation, InviteUserMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<InviteUserMutation, InviteUserMutationVariables>(InviteUserDocument, options);
      }
export type InviteUserMutationHookResult = ReturnType<typeof useInviteUserMutation>;
export type InviteUserMutationResult = Apollo.MutationResult<InviteUserMutation>;
export type InviteUserMutationOptions = Apollo.BaseMutationOptions<InviteUserMutation, InviteUserMutationVariables>;
export const GetInvoiceDocument = gql`
    query getInvoice($accountId: String!, $invoiceNumber: String!) {
  invoice(accountId: $accountId, invoiceNumber: $invoiceNumber) {
    invoiceNumber
    status
    terms
    customerPo
    invoiceDate
    dueDate
    originalAmt
    openBalance
    jobNumber
    jobName
    address {
      streetLineOne
      streetLineTwo
      streetLineThree
      city
      state
      postalCode
      country
    }
    shipDate
    deliveryMethod
    subtotal
    tax
    otherCharges
    paidToDate
    invoiceItems {
      id
      brand
      name
      partNumber
      mfr
      thumb
      price
      pricingUom
      qty {
        quantityOrdered
        quantityShipped
      }
    }
  }
}
    `;

/**
 * __useGetInvoiceQuery__
 *
 * To run a query within a React component, call `useGetInvoiceQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetInvoiceQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetInvoiceQuery({
 *   variables: {
 *      accountId: // value for 'accountId'
 *      invoiceNumber: // value for 'invoiceNumber'
 *   },
 * });
 */
export function useGetInvoiceQuery(baseOptions: Apollo.QueryHookOptions<GetInvoiceQuery, GetInvoiceQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetInvoiceQuery, GetInvoiceQueryVariables>(GetInvoiceDocument, options);
      }
export function useGetInvoiceLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetInvoiceQuery, GetInvoiceQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetInvoiceQuery, GetInvoiceQueryVariables>(GetInvoiceDocument, options);
        }
export type GetInvoiceQueryHookResult = ReturnType<typeof useGetInvoiceQuery>;
export type GetInvoiceLazyQueryHookResult = ReturnType<typeof useGetInvoiceLazyQuery>;
export type GetInvoiceQueryResult = Apollo.QueryResult<GetInvoiceQuery, GetInvoiceQueryVariables>;
export const InvoicesDocument = gql`
    query Invoices($accountId: String!, $erpName: String!) {
  invoices(accountId: $accountId, erpName: $erpName) {
    bucketThirty
    bucketSixty
    bucketNinety
    bucketOneTwenty
    bucketFuture
    currentAmt
    totalAmt
    totalPastDue
    invoices {
      invoiceNumber
      status
      customerPo
      invoiceDate
      originalAmt
      openBalance
      age
      jobNumber
      jobName
      invoiceUrl
    }
  }
}
    `;

/**
 * __useInvoicesQuery__
 *
 * To run a query within a React component, call `useInvoicesQuery` and pass it any options that fit your needs.
 * When your component renders, `useInvoicesQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useInvoicesQuery({
 *   variables: {
 *      accountId: // value for 'accountId'
 *      erpName: // value for 'erpName'
 *   },
 * });
 */
export function useInvoicesQuery(baseOptions: Apollo.QueryHookOptions<InvoicesQuery, InvoicesQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<InvoicesQuery, InvoicesQueryVariables>(InvoicesDocument, options);
      }
export function useInvoicesLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<InvoicesQuery, InvoicesQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<InvoicesQuery, InvoicesQueryVariables>(InvoicesDocument, options);
        }
export type InvoicesQueryHookResult = ReturnType<typeof useInvoicesQuery>;
export type InvoicesLazyQueryHookResult = ReturnType<typeof useInvoicesLazyQuery>;
export type InvoicesQueryResult = Apollo.QueryResult<InvoicesQuery, InvoicesQueryVariables>;
export const InvoicesUrlDocument = gql`
    query InvoicesUrl($accountId: String!, $invoiceNumbers: [String!]!) {
  invoicesUrl(accountId: $accountId, invoiceNumbers: $invoiceNumbers)
}
    `;

/**
 * __useInvoicesUrlQuery__
 *
 * To run a query within a React component, call `useInvoicesUrlQuery` and pass it any options that fit your needs.
 * When your component renders, `useInvoicesUrlQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useInvoicesUrlQuery({
 *   variables: {
 *      accountId: // value for 'accountId'
 *      invoiceNumbers: // value for 'invoiceNumbers'
 *   },
 * });
 */
export function useInvoicesUrlQuery(baseOptions: Apollo.QueryHookOptions<InvoicesUrlQuery, InvoicesUrlQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<InvoicesUrlQuery, InvoicesUrlQueryVariables>(InvoicesUrlDocument, options);
      }
export function useInvoicesUrlLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<InvoicesUrlQuery, InvoicesUrlQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<InvoicesUrlQuery, InvoicesUrlQueryVariables>(InvoicesUrlDocument, options);
        }
export type InvoicesUrlQueryHookResult = ReturnType<typeof useInvoicesUrlQuery>;
export type InvoicesUrlLazyQueryHookResult = ReturnType<typeof useInvoicesUrlLazyQuery>;
export type InvoicesUrlQueryResult = Apollo.QueryResult<InvoicesUrlQuery, InvoicesUrlQueryVariables>;
export const CreateJobFormDocument = gql`
    mutation createJobForm($jobFormInput: JobFormInput!, $file: Upload) {
  createJobForm(jobFormInput: $jobFormInput, file: $file)
}
    `;
export type CreateJobFormMutationFn = Apollo.MutationFunction<CreateJobFormMutation, CreateJobFormMutationVariables>;

/**
 * __useCreateJobFormMutation__
 *
 * To run a mutation, you first call `useCreateJobFormMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useCreateJobFormMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [createJobFormMutation, { data, loading, error }] = useCreateJobFormMutation({
 *   variables: {
 *      jobFormInput: // value for 'jobFormInput'
 *      file: // value for 'file'
 *   },
 * });
 */
export function useCreateJobFormMutation(baseOptions?: Apollo.MutationHookOptions<CreateJobFormMutation, CreateJobFormMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<CreateJobFormMutation, CreateJobFormMutationVariables>(CreateJobFormDocument, options);
      }
export type CreateJobFormMutationHookResult = ReturnType<typeof useCreateJobFormMutation>;
export type CreateJobFormMutationResult = Apollo.MutationResult<CreateJobFormMutation>;
export type CreateJobFormMutationOptions = Apollo.BaseMutationOptions<CreateJobFormMutation, CreateJobFormMutationVariables>;
export const EntitySearchDocument = gql`
    query entitySearch($accountId: String!) {
  entitySearch(accountId: $accountId) {
    isBillTo
    erpName
    branchId
    companyName
    erpAccountId
  }
}
    `;

/**
 * __useEntitySearchQuery__
 *
 * To run a query within a React component, call `useEntitySearchQuery` and pass it any options that fit your needs.
 * When your component renders, `useEntitySearchQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useEntitySearchQuery({
 *   variables: {
 *      accountId: // value for 'accountId'
 *   },
 * });
 */
export function useEntitySearchQuery(baseOptions: Apollo.QueryHookOptions<EntitySearchQuery, EntitySearchQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<EntitySearchQuery, EntitySearchQueryVariables>(EntitySearchDocument, options);
      }
export function useEntitySearchLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<EntitySearchQuery, EntitySearchQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<EntitySearchQuery, EntitySearchQueryVariables>(EntitySearchDocument, options);
        }
export type EntitySearchQueryHookResult = ReturnType<typeof useEntitySearchQuery>;
export type EntitySearchLazyQueryHookResult = ReturnType<typeof useEntitySearchLazyQuery>;
export type EntitySearchQueryResult = Apollo.QueryResult<EntitySearchQuery, EntitySearchQueryVariables>;
export const GetListDocument = gql`
    query GetList($listId: ID!, $userId: ID!, $shipToAccountId: ID!, $branchId: ID!, $erpAccountId: ID) {
  list(
    listId: $listId
    userId: $userId
    shipToAccountId: $shipToAccountId
    branchId: $branchId
    erpAccountId: $erpAccountId
  ) {
    ...List
  }
}
    ${ListFragmentDoc}`;

/**
 * __useGetListQuery__
 *
 * To run a query within a React component, call `useGetListQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetListQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetListQuery({
 *   variables: {
 *      listId: // value for 'listId'
 *      userId: // value for 'userId'
 *      shipToAccountId: // value for 'shipToAccountId'
 *      branchId: // value for 'branchId'
 *      erpAccountId: // value for 'erpAccountId'
 *   },
 * });
 */
export function useGetListQuery(baseOptions: Apollo.QueryHookOptions<GetListQuery, GetListQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetListQuery, GetListQueryVariables>(GetListDocument, options);
      }
export function useGetListLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetListQuery, GetListQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetListQuery, GetListQueryVariables>(GetListDocument, options);
        }
export type GetListQueryHookResult = ReturnType<typeof useGetListQuery>;
export type GetListLazyQueryHookResult = ReturnType<typeof useGetListLazyQuery>;
export type GetListQueryResult = Apollo.QueryResult<GetListQuery, GetListQueryVariables>;
export const GetListsDocument = gql`
    query GetLists($billToAccountId: ID!) {
  lists(billToAccountId: $billToAccountId) {
    ...BasicList
  }
}
    ${BasicListFragmentDoc}`;

/**
 * __useGetListsQuery__
 *
 * To run a query within a React component, call `useGetListsQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetListsQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetListsQuery({
 *   variables: {
 *      billToAccountId: // value for 'billToAccountId'
 *   },
 * });
 */
export function useGetListsQuery(baseOptions: Apollo.QueryHookOptions<GetListsQuery, GetListsQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetListsQuery, GetListsQueryVariables>(GetListsDocument, options);
      }
export function useGetListsLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetListsQuery, GetListsQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetListsQuery, GetListsQueryVariables>(GetListsDocument, options);
        }
export type GetListsQueryHookResult = ReturnType<typeof useGetListsQuery>;
export type GetListsLazyQueryHookResult = ReturnType<typeof useGetListsLazyQuery>;
export type GetListsQueryResult = Apollo.QueryResult<GetListsQuery, GetListsQueryVariables>;
export const GetExportListIntoCsvDocument = gql`
    query getExportListIntoCSV($listId: ID!, $userId: ID!, $shipToAccountId: ID!, $branchId: ID!) {
  exportListToCSV(
    listId: $listId
    userId: $userId
    shipToAccountId: $shipToAccountId
    branchId: $branchId
  ) {
    ...ExportListResponse
  }
}
    ${ExportListResponseFragmentDoc}`;

/**
 * __useGetExportListIntoCsvQuery__
 *
 * To run a query within a React component, call `useGetExportListIntoCsvQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetExportListIntoCsvQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetExportListIntoCsvQuery({
 *   variables: {
 *      listId: // value for 'listId'
 *      userId: // value for 'userId'
 *      shipToAccountId: // value for 'shipToAccountId'
 *      branchId: // value for 'branchId'
 *   },
 * });
 */
export function useGetExportListIntoCsvQuery(baseOptions: Apollo.QueryHookOptions<GetExportListIntoCsvQuery, GetExportListIntoCsvQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetExportListIntoCsvQuery, GetExportListIntoCsvQueryVariables>(GetExportListIntoCsvDocument, options);
      }
export function useGetExportListIntoCsvLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetExportListIntoCsvQuery, GetExportListIntoCsvQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetExportListIntoCsvQuery, GetExportListIntoCsvQueryVariables>(GetExportListIntoCsvDocument, options);
        }
export type GetExportListIntoCsvQueryHookResult = ReturnType<typeof useGetExportListIntoCsvQuery>;
export type GetExportListIntoCsvLazyQueryHookResult = ReturnType<typeof useGetExportListIntoCsvLazyQuery>;
export type GetExportListIntoCsvQueryResult = Apollo.QueryResult<GetExportListIntoCsvQuery, GetExportListIntoCsvQueryVariables>;
export const CreateListDocument = gql`
    mutation CreateList($createListInput: CreateListInput!) {
  createList(createListInput: $createListInput) {
    ...List
  }
}
    ${ListFragmentDoc}`;
export type CreateListMutationFn = Apollo.MutationFunction<CreateListMutation, CreateListMutationVariables>;

/**
 * __useCreateListMutation__
 *
 * To run a mutation, you first call `useCreateListMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useCreateListMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [createListMutation, { data, loading, error }] = useCreateListMutation({
 *   variables: {
 *      createListInput: // value for 'createListInput'
 *   },
 * });
 */
export function useCreateListMutation(baseOptions?: Apollo.MutationHookOptions<CreateListMutation, CreateListMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<CreateListMutation, CreateListMutationVariables>(CreateListDocument, options);
      }
export type CreateListMutationHookResult = ReturnType<typeof useCreateListMutation>;
export type CreateListMutationResult = Apollo.MutationResult<CreateListMutation>;
export type CreateListMutationOptions = Apollo.BaseMutationOptions<CreateListMutation, CreateListMutationVariables>;
export const DeleteListDocument = gql`
    mutation DeleteList($listId: ID!) {
  deleteList(listId: $listId) {
    id
    success
  }
}
    `;
export type DeleteListMutationFn = Apollo.MutationFunction<DeleteListMutation, DeleteListMutationVariables>;

/**
 * __useDeleteListMutation__
 *
 * To run a mutation, you first call `useDeleteListMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useDeleteListMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [deleteListMutation, { data, loading, error }] = useDeleteListMutation({
 *   variables: {
 *      listId: // value for 'listId'
 *   },
 * });
 */
export function useDeleteListMutation(baseOptions?: Apollo.MutationHookOptions<DeleteListMutation, DeleteListMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<DeleteListMutation, DeleteListMutationVariables>(DeleteListDocument, options);
      }
export type DeleteListMutationHookResult = ReturnType<typeof useDeleteListMutation>;
export type DeleteListMutationResult = Apollo.MutationResult<DeleteListMutation>;
export type DeleteListMutationOptions = Apollo.BaseMutationOptions<DeleteListMutation, DeleteListMutationVariables>;
export const UpdateListDocument = gql`
    mutation UpdateList($updateListInput: UpdateListInput!) {
  updateList(updateListInput: $updateListInput) {
    ...List
  }
}
    ${ListFragmentDoc}`;
export type UpdateListMutationFn = Apollo.MutationFunction<UpdateListMutation, UpdateListMutationVariables>;

/**
 * __useUpdateListMutation__
 *
 * To run a mutation, you first call `useUpdateListMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useUpdateListMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [updateListMutation, { data, loading, error }] = useUpdateListMutation({
 *   variables: {
 *      updateListInput: // value for 'updateListInput'
 *   },
 * });
 */
export function useUpdateListMutation(baseOptions?: Apollo.MutationHookOptions<UpdateListMutation, UpdateListMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<UpdateListMutation, UpdateListMutationVariables>(UpdateListDocument, options);
      }
export type UpdateListMutationHookResult = ReturnType<typeof useUpdateListMutation>;
export type UpdateListMutationResult = Apollo.MutationResult<UpdateListMutation>;
export type UpdateListMutationOptions = Apollo.BaseMutationOptions<UpdateListMutation, UpdateListMutationVariables>;
export const UploadNewListDocument = gql`
    mutation UploadNewList($file: Upload!, $name: String!, $billToAccountId: ID!) {
  uploadNewList(file: $file, name: $name, billToAccountId: $billToAccountId) {
    ...ListUploadResponse
  }
}
    ${ListUploadResponseFragmentDoc}`;
export type UploadNewListMutationFn = Apollo.MutationFunction<UploadNewListMutation, UploadNewListMutationVariables>;

/**
 * __useUploadNewListMutation__
 *
 * To run a mutation, you first call `useUploadNewListMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useUploadNewListMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [uploadNewListMutation, { data, loading, error }] = useUploadNewListMutation({
 *   variables: {
 *      file: // value for 'file'
 *      name: // value for 'name'
 *      billToAccountId: // value for 'billToAccountId'
 *   },
 * });
 */
export function useUploadNewListMutation(baseOptions?: Apollo.MutationHookOptions<UploadNewListMutation, UploadNewListMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<UploadNewListMutation, UploadNewListMutationVariables>(UploadNewListDocument, options);
      }
export type UploadNewListMutationHookResult = ReturnType<typeof useUploadNewListMutation>;
export type UploadNewListMutationResult = Apollo.MutationResult<UploadNewListMutation>;
export type UploadNewListMutationOptions = Apollo.BaseMutationOptions<UploadNewListMutation, UploadNewListMutationVariables>;
export const UploadToListDocument = gql`
    mutation UploadToList($file: Upload!, $listId: ID!) {
  uploadToList(file: $file, listId: $listId) {
    ...ListUploadResponse
  }
}
    ${ListUploadResponseFragmentDoc}`;
export type UploadToListMutationFn = Apollo.MutationFunction<UploadToListMutation, UploadToListMutationVariables>;

/**
 * __useUploadToListMutation__
 *
 * To run a mutation, you first call `useUploadToListMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useUploadToListMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [uploadToListMutation, { data, loading, error }] = useUploadToListMutation({
 *   variables: {
 *      file: // value for 'file'
 *      listId: // value for 'listId'
 *   },
 * });
 */
export function useUploadToListMutation(baseOptions?: Apollo.MutationHookOptions<UploadToListMutation, UploadToListMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<UploadToListMutation, UploadToListMutationVariables>(UploadToListDocument, options);
      }
export type UploadToListMutationHookResult = ReturnType<typeof useUploadToListMutation>;
export type UploadToListMutationResult = Apollo.MutationResult<UploadToListMutation>;
export type UploadToListMutationOptions = Apollo.BaseMutationOptions<UploadToListMutation, UploadToListMutationVariables>;
export const AddItemToListDocument = gql`
    mutation AddItemToList($addItemToListInput: AddItemToListInput!) {
  addItemToList(addItemToListInput: $addItemToListInput)
}
    `;
export type AddItemToListMutationFn = Apollo.MutationFunction<AddItemToListMutation, AddItemToListMutationVariables>;

/**
 * __useAddItemToListMutation__
 *
 * To run a mutation, you first call `useAddItemToListMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useAddItemToListMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [addItemToListMutation, { data, loading, error }] = useAddItemToListMutation({
 *   variables: {
 *      addItemToListInput: // value for 'addItemToListInput'
 *   },
 * });
 */
export function useAddItemToListMutation(baseOptions?: Apollo.MutationHookOptions<AddItemToListMutation, AddItemToListMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<AddItemToListMutation, AddItemToListMutationVariables>(AddItemToListDocument, options);
      }
export type AddItemToListMutationHookResult = ReturnType<typeof useAddItemToListMutation>;
export type AddItemToListMutationResult = Apollo.MutationResult<AddItemToListMutation>;
export type AddItemToListMutationOptions = Apollo.BaseMutationOptions<AddItemToListMutation, AddItemToListMutationVariables>;
export const ToggleItemInListsDocument = gql`
    mutation ToggleItemInLists($toggleItemInListsInput: ToggleItemsInListInput!) {
  toggleItemInLists(toggleItemsInListInput: $toggleItemInListsInput) {
    lists {
      ...BasicList
    }
  }
}
    ${BasicListFragmentDoc}`;
export type ToggleItemInListsMutationFn = Apollo.MutationFunction<ToggleItemInListsMutation, ToggleItemInListsMutationVariables>;

/**
 * __useToggleItemInListsMutation__
 *
 * To run a mutation, you first call `useToggleItemInListsMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useToggleItemInListsMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [toggleItemInListsMutation, { data, loading, error }] = useToggleItemInListsMutation({
 *   variables: {
 *      toggleItemInListsInput: // value for 'toggleItemInListsInput'
 *   },
 * });
 */
export function useToggleItemInListsMutation(baseOptions?: Apollo.MutationHookOptions<ToggleItemInListsMutation, ToggleItemInListsMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<ToggleItemInListsMutation, ToggleItemInListsMutationVariables>(ToggleItemInListsDocument, options);
      }
export type ToggleItemInListsMutationHookResult = ReturnType<typeof useToggleItemInListsMutation>;
export type ToggleItemInListsMutationResult = Apollo.MutationResult<ToggleItemInListsMutation>;
export type ToggleItemInListsMutationOptions = Apollo.BaseMutationOptions<ToggleItemInListsMutation, ToggleItemInListsMutationVariables>;
export const AddAllCartItemsToNewListDocument = gql`
    mutation addAllCartItemsToNewList($accountId: ID!, $cartId: ID!, $name: String!) {
  addAllCartItemsToNewList(accountId: $accountId, cartId: $cartId, name: $name)
}
    `;
export type AddAllCartItemsToNewListMutationFn = Apollo.MutationFunction<AddAllCartItemsToNewListMutation, AddAllCartItemsToNewListMutationVariables>;

/**
 * __useAddAllCartItemsToNewListMutation__
 *
 * To run a mutation, you first call `useAddAllCartItemsToNewListMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useAddAllCartItemsToNewListMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [addAllCartItemsToNewListMutation, { data, loading, error }] = useAddAllCartItemsToNewListMutation({
 *   variables: {
 *      accountId: // value for 'accountId'
 *      cartId: // value for 'cartId'
 *      name: // value for 'name'
 *   },
 * });
 */
export function useAddAllCartItemsToNewListMutation(baseOptions?: Apollo.MutationHookOptions<AddAllCartItemsToNewListMutation, AddAllCartItemsToNewListMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<AddAllCartItemsToNewListMutation, AddAllCartItemsToNewListMutationVariables>(AddAllCartItemsToNewListDocument, options);
      }
export type AddAllCartItemsToNewListMutationHookResult = ReturnType<typeof useAddAllCartItemsToNewListMutation>;
export type AddAllCartItemsToNewListMutationResult = Apollo.MutationResult<AddAllCartItemsToNewListMutation>;
export type AddAllCartItemsToNewListMutationOptions = Apollo.BaseMutationOptions<AddAllCartItemsToNewListMutation, AddAllCartItemsToNewListMutationVariables>;
export const AddAllCartItemsToExistingListsDocument = gql`
    mutation addAllCartItemsToExistingLists($listIds: [String!]!, $cartId: ID!) {
  addAllCartItemsToExistingLists(listIds: $listIds, cartId: $cartId)
}
    `;
export type AddAllCartItemsToExistingListsMutationFn = Apollo.MutationFunction<AddAllCartItemsToExistingListsMutation, AddAllCartItemsToExistingListsMutationVariables>;

/**
 * __useAddAllCartItemsToExistingListsMutation__
 *
 * To run a mutation, you first call `useAddAllCartItemsToExistingListsMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useAddAllCartItemsToExistingListsMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [addAllCartItemsToExistingListsMutation, { data, loading, error }] = useAddAllCartItemsToExistingListsMutation({
 *   variables: {
 *      listIds: // value for 'listIds'
 *      cartId: // value for 'cartId'
 *   },
 * });
 */
export function useAddAllCartItemsToExistingListsMutation(baseOptions?: Apollo.MutationHookOptions<AddAllCartItemsToExistingListsMutation, AddAllCartItemsToExistingListsMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<AddAllCartItemsToExistingListsMutation, AddAllCartItemsToExistingListsMutationVariables>(AddAllCartItemsToExistingListsDocument, options);
      }
export type AddAllCartItemsToExistingListsMutationHookResult = ReturnType<typeof useAddAllCartItemsToExistingListsMutation>;
export type AddAllCartItemsToExistingListsMutationResult = Apollo.MutationResult<AddAllCartItemsToExistingListsMutation>;
export type AddAllCartItemsToExistingListsMutationOptions = Apollo.BaseMutationOptions<AddAllCartItemsToExistingListsMutation, AddAllCartItemsToExistingListsMutationVariables>;
export const GetBranchDocument = gql`
    query getBranch($branchId: ID!) {
  branch(branchId: $branchId) {
    ...Branch
  }
}
    ${BranchFragmentDoc}`;

/**
 * __useGetBranchQuery__
 *
 * To run a query within a React component, call `useGetBranchQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetBranchQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetBranchQuery({
 *   variables: {
 *      branchId: // value for 'branchId'
 *   },
 * });
 */
export function useGetBranchQuery(baseOptions: Apollo.QueryHookOptions<GetBranchQuery, GetBranchQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetBranchQuery, GetBranchQueryVariables>(GetBranchDocument, options);
      }
export function useGetBranchLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetBranchQuery, GetBranchQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetBranchQuery, GetBranchQueryVariables>(GetBranchDocument, options);
        }
export type GetBranchQueryHookResult = ReturnType<typeof useGetBranchQuery>;
export type GetBranchLazyQueryHookResult = ReturnType<typeof useGetBranchLazyQuery>;
export type GetBranchQueryResult = Apollo.QueryResult<GetBranchQuery, GetBranchQueryVariables>;
export const FindBranchesDocument = gql`
    query findBranches($branchSearch: BranchSearch!) {
  branchSearch(branchSearch: $branchSearch) {
    latitude
    longitude
    branches {
      ...Branch
    }
  }
}
    ${BranchFragmentDoc}`;

/**
 * __useFindBranchesQuery__
 *
 * To run a query within a React component, call `useFindBranchesQuery` and pass it any options that fit your needs.
 * When your component renders, `useFindBranchesQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useFindBranchesQuery({
 *   variables: {
 *      branchSearch: // value for 'branchSearch'
 *   },
 * });
 */
export function useFindBranchesQuery(baseOptions: Apollo.QueryHookOptions<FindBranchesQuery, FindBranchesQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<FindBranchesQuery, FindBranchesQueryVariables>(FindBranchesDocument, options);
      }
export function useFindBranchesLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<FindBranchesQuery, FindBranchesQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<FindBranchesQuery, FindBranchesQueryVariables>(FindBranchesDocument, options);
        }
export type FindBranchesQueryHookResult = ReturnType<typeof useFindBranchesQuery>;
export type FindBranchesLazyQueryHookResult = ReturnType<typeof useFindBranchesLazyQuery>;
export type FindBranchesQueryResult = Apollo.QueryResult<FindBranchesQuery, FindBranchesQueryVariables>;
export const ResendLegacyInviteEmailDocument = gql`
    mutation ResendLegacyInviteEmail($legacyUserEmail: String!) {
  resendLegacyInviteEmail(legacyUserEmail: $legacyUserEmail)
}
    `;
export type ResendLegacyInviteEmailMutationFn = Apollo.MutationFunction<ResendLegacyInviteEmailMutation, ResendLegacyInviteEmailMutationVariables>;

/**
 * __useResendLegacyInviteEmailMutation__
 *
 * To run a mutation, you first call `useResendLegacyInviteEmailMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useResendLegacyInviteEmailMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [resendLegacyInviteEmailMutation, { data, loading, error }] = useResendLegacyInviteEmailMutation({
 *   variables: {
 *      legacyUserEmail: // value for 'legacyUserEmail'
 *   },
 * });
 */
export function useResendLegacyInviteEmailMutation(baseOptions?: Apollo.MutationHookOptions<ResendLegacyInviteEmailMutation, ResendLegacyInviteEmailMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<ResendLegacyInviteEmailMutation, ResendLegacyInviteEmailMutationVariables>(ResendLegacyInviteEmailDocument, options);
      }
export type ResendLegacyInviteEmailMutationHookResult = ReturnType<typeof useResendLegacyInviteEmailMutation>;
export type ResendLegacyInviteEmailMutationResult = Apollo.MutationResult<ResendLegacyInviteEmailMutation>;
export type ResendLegacyInviteEmailMutationOptions = Apollo.BaseMutationOptions<ResendLegacyInviteEmailMutation, ResendLegacyInviteEmailMutationVariables>;
export const InvitedUserEmailSentDocument = gql`
    query InvitedUserEmailSent($email: String!) {
  invitedUserEmailSent(email: $email)
}
    `;

/**
 * __useInvitedUserEmailSentQuery__
 *
 * To run a query within a React component, call `useInvitedUserEmailSentQuery` and pass it any options that fit your needs.
 * When your component renders, `useInvitedUserEmailSentQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useInvitedUserEmailSentQuery({
 *   variables: {
 *      email: // value for 'email'
 *   },
 * });
 */
export function useInvitedUserEmailSentQuery(baseOptions: Apollo.QueryHookOptions<InvitedUserEmailSentQuery, InvitedUserEmailSentQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<InvitedUserEmailSentQuery, InvitedUserEmailSentQueryVariables>(InvitedUserEmailSentDocument, options);
      }
export function useInvitedUserEmailSentLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<InvitedUserEmailSentQuery, InvitedUserEmailSentQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<InvitedUserEmailSentQuery, InvitedUserEmailSentQueryVariables>(InvitedUserEmailSentDocument, options);
        }
export type InvitedUserEmailSentQueryHookResult = ReturnType<typeof useInvitedUserEmailSentQuery>;
export type InvitedUserEmailSentLazyQueryHookResult = ReturnType<typeof useInvitedUserEmailSentLazyQuery>;
export type InvitedUserEmailSentQueryResult = Apollo.QueryResult<InvitedUserEmailSentQuery, InvitedUserEmailSentQueryVariables>;
export const GetOrderDocument = gql`
    query getOrder($accountId: ID!, $orderId: ID!, $userId: ID!, $invoiceNumber: ID, $orderStatus: String) {
  order(
    accountId: $accountId
    orderId: $orderId
    userId: $userId
    invoiceNumber: $invoiceNumber
    orderStatus: $orderStatus
  ) {
    amount
    shipToName
    customerPO
    deliveryMethod
    invoiceNumber
    lineItems {
      uom
      pricingUom
      imageUrls {
        thumb
      }
      manufacturerName
      manufacturerNumber
      erpPartNumber
      orderQuantity
      backOrderedQuantity
      productId
      productName
      productOrderTotal
      shipQuantity
      status
      unitPrice
      lineNumber
      lineComments
    }
    orderDate
    orderNumber
    orderStatus
    orderTotal
    orderedBy
    creditCard {
      ...CreditCard
    }
    shipAddress {
      streetLineOne
      streetLineTwo
      streetLineThree
      city
      state
      postalCode
      country
    }
    shipDate
    specialInstructions
    subTotal
    tax
    webStatus
  }
}
    ${CreditCardFragmentDoc}`;

/**
 * __useGetOrderQuery__
 *
 * To run a query within a React component, call `useGetOrderQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetOrderQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetOrderQuery({
 *   variables: {
 *      accountId: // value for 'accountId'
 *      orderId: // value for 'orderId'
 *      userId: // value for 'userId'
 *      invoiceNumber: // value for 'invoiceNumber'
 *      orderStatus: // value for 'orderStatus'
 *   },
 * });
 */
export function useGetOrderQuery(baseOptions: Apollo.QueryHookOptions<GetOrderQuery, GetOrderQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetOrderQuery, GetOrderQueryVariables>(GetOrderDocument, options);
      }
export function useGetOrderLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetOrderQuery, GetOrderQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetOrderQuery, GetOrderQueryVariables>(GetOrderDocument, options);
        }
export type GetOrderQueryHookResult = ReturnType<typeof useGetOrderQuery>;
export type GetOrderLazyQueryHookResult = ReturnType<typeof useGetOrderLazyQuery>;
export type GetOrderQueryResult = Apollo.QueryResult<GetOrderQuery, GetOrderQueryVariables>;
export const OrdersDocument = gql`
    query Orders($accountId: ID!, $startDate: String, $endDate: String, $erpName: String!) {
  orders(
    accountId: $accountId
    startDate: $startDate
    endDate: $endDate
    erpName: $erpName
  ) {
    orders {
      orderNumber
      orderStatus
      shipDate
      customerPO
      orderDate
      invoiceNumber
      webStatus
      shipAddress {
        streetLineOne
        streetLineTwo
        streetLineThree
        city
        state
        postalCode
        country
      }
      amount
      shipToName
      jobNumber
      orderTotal
    }
    pagination {
      totalItemCount
    }
  }
}
    `;

/**
 * __useOrdersQuery__
 *
 * To run a query within a React component, call `useOrdersQuery` and pass it any options that fit your needs.
 * When your component renders, `useOrdersQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useOrdersQuery({
 *   variables: {
 *      accountId: // value for 'accountId'
 *      startDate: // value for 'startDate'
 *      endDate: // value for 'endDate'
 *      erpName: // value for 'erpName'
 *   },
 * });
 */
export function useOrdersQuery(baseOptions: Apollo.QueryHookOptions<OrdersQuery, OrdersQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<OrdersQuery, OrdersQueryVariables>(OrdersDocument, options);
      }
export function useOrdersLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<OrdersQuery, OrdersQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<OrdersQuery, OrdersQueryVariables>(OrdersDocument, options);
        }
export type OrdersQueryHookResult = ReturnType<typeof useOrdersQuery>;
export type OrdersLazyQueryHookResult = ReturnType<typeof useOrdersLazyQuery>;
export type OrdersQueryResult = Apollo.QueryResult<OrdersQuery, OrdersQueryVariables>;
export const PreviouslyPurchasedProductsDocument = gql`
    query previouslyPurchasedProducts($ecommShipToId: ID!, $userId: ID!, $currentPage: Int, $pageSize: Int!, $customerNumber: String) {
  previouslyPurchasedProducts(
    ecommShipToId: $ecommShipToId
    userId: $userId
    currentPage: $currentPage
    pageSize: $pageSize
    customerNumber: $customerNumber
  ) {
    products {
      product {
        id
        partNumber
        name
        price
        upc
        manufacturerName
        manufacturerNumber
        minIncrementQty
        status
        customerPartNumber
        stock {
          homeBranch {
            branchName
            branchId
            availability
          }
          otherBranches {
            branchId
            availability
          }
        }
        imageUrls {
          thumb
        }
      }
      quantity {
        uom
        umqt
        quantity
      }
      lastOrder {
        lastDate
        lastQuantity
      }
    }
    pagination {
      pageSize
      currentPage
      totalItemCount
    }
  }
}
    `;

/**
 * __usePreviouslyPurchasedProductsQuery__
 *
 * To run a query within a React component, call `usePreviouslyPurchasedProductsQuery` and pass it any options that fit your needs.
 * When your component renders, `usePreviouslyPurchasedProductsQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = usePreviouslyPurchasedProductsQuery({
 *   variables: {
 *      ecommShipToId: // value for 'ecommShipToId'
 *      userId: // value for 'userId'
 *      currentPage: // value for 'currentPage'
 *      pageSize: // value for 'pageSize'
 *      customerNumber: // value for 'customerNumber'
 *   },
 * });
 */
export function usePreviouslyPurchasedProductsQuery(baseOptions: Apollo.QueryHookOptions<PreviouslyPurchasedProductsQuery, PreviouslyPurchasedProductsQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<PreviouslyPurchasedProductsQuery, PreviouslyPurchasedProductsQueryVariables>(PreviouslyPurchasedProductsDocument, options);
      }
export function usePreviouslyPurchasedProductsLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<PreviouslyPurchasedProductsQuery, PreviouslyPurchasedProductsQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<PreviouslyPurchasedProductsQuery, PreviouslyPurchasedProductsQueryVariables>(PreviouslyPurchasedProductsDocument, options);
        }
export type PreviouslyPurchasedProductsQueryHookResult = ReturnType<typeof usePreviouslyPurchasedProductsQuery>;
export type PreviouslyPurchasedProductsLazyQueryHookResult = ReturnType<typeof usePreviouslyPurchasedProductsLazyQuery>;
export type PreviouslyPurchasedProductsQueryResult = Apollo.QueryResult<PreviouslyPurchasedProductsQuery, PreviouslyPurchasedProductsQueryVariables>;
export const GetProductDocument = gql`
    query GetProduct($productInput: ProductInput!) {
  product(productInput: $productInput) {
    ...Product
  }
}
    ${ProductFragmentDoc}`;

/**
 * __useGetProductQuery__
 *
 * To run a query within a React component, call `useGetProductQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetProductQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetProductQuery({
 *   variables: {
 *      productInput: // value for 'productInput'
 *   },
 * });
 */
export function useGetProductQuery(baseOptions: Apollo.QueryHookOptions<GetProductQuery, GetProductQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetProductQuery, GetProductQueryVariables>(GetProductDocument, options);
      }
export function useGetProductLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetProductQuery, GetProductQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetProductQuery, GetProductQueryVariables>(GetProductDocument, options);
        }
export type GetProductQueryHookResult = ReturnType<typeof useGetProductQuery>;
export type GetProductLazyQueryHookResult = ReturnType<typeof useGetProductLazyQuery>;
export type GetProductQueryResult = Apollo.QueryResult<GetProductQuery, GetProductQueryVariables>;
export const GetProductPricingDocument = gql`
    query GetProductPricing($input: ProductPricingInput!) {
  productPricing(input: $input) {
    customerId
    branch
    products {
      productId
      catalogId
      branchAvailableQty
      totalAvailableQty
      listIds
      sellPrice
      orderUom
    }
  }
}
    `;

/**
 * __useGetProductPricingQuery__
 *
 * To run a query within a React component, call `useGetProductPricingQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetProductPricingQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetProductPricingQuery({
 *   variables: {
 *      input: // value for 'input'
 *   },
 * });
 */
export function useGetProductPricingQuery(baseOptions: Apollo.QueryHookOptions<GetProductPricingQuery, GetProductPricingQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetProductPricingQuery, GetProductPricingQueryVariables>(GetProductPricingDocument, options);
      }
export function useGetProductPricingLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetProductPricingQuery, GetProductPricingQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetProductPricingQuery, GetProductPricingQueryVariables>(GetProductPricingDocument, options);
        }
export type GetProductPricingQueryHookResult = ReturnType<typeof useGetProductPricingQuery>;
export type GetProductPricingLazyQueryHookResult = ReturnType<typeof useGetProductPricingLazyQuery>;
export type GetProductPricingQueryResult = Apollo.QueryResult<GetProductPricingQuery, GetProductPricingQueryVariables>;
export const GetProductInventoryDocument = gql`
    query GetProductInventory($productId: String!) {
  productInventory(productId: $productId) {
    productId
    productDescription
    inventory {
      branchId
      availableOnHand
    }
  }
}
    `;

/**
 * __useGetProductInventoryQuery__
 *
 * To run a query within a React component, call `useGetProductInventoryQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetProductInventoryQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetProductInventoryQuery({
 *   variables: {
 *      productId: // value for 'productId'
 *   },
 * });
 */
export function useGetProductInventoryQuery(baseOptions: Apollo.QueryHookOptions<GetProductInventoryQuery, GetProductInventoryQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetProductInventoryQuery, GetProductInventoryQueryVariables>(GetProductInventoryDocument, options);
      }
export function useGetProductInventoryLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetProductInventoryQuery, GetProductInventoryQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetProductInventoryQuery, GetProductInventoryQueryVariables>(GetProductInventoryDocument, options);
        }
export type GetProductInventoryQueryHookResult = ReturnType<typeof useGetProductInventoryQuery>;
export type GetProductInventoryLazyQueryHookResult = ReturnType<typeof useGetProductInventoryLazyQuery>;
export type GetProductInventoryQueryResult = Apollo.QueryResult<GetProductInventoryQuery, GetProductInventoryQueryVariables>;
export const GetOrdersPendingApprovalDocument = gql`
    query GetOrdersPendingApproval {
  ordersPendingApproval {
    orderId
    purchaseOrderNumber
    submissionDate
    submittedByName
    orderTotal
  }
}
    `;

/**
 * __useGetOrdersPendingApprovalQuery__
 *
 * To run a query within a React component, call `useGetOrdersPendingApprovalQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetOrdersPendingApprovalQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetOrdersPendingApprovalQuery({
 *   variables: {
 *   },
 * });
 */
export function useGetOrdersPendingApprovalQuery(baseOptions?: Apollo.QueryHookOptions<GetOrdersPendingApprovalQuery, GetOrdersPendingApprovalQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetOrdersPendingApprovalQuery, GetOrdersPendingApprovalQueryVariables>(GetOrdersPendingApprovalDocument, options);
      }
export function useGetOrdersPendingApprovalLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetOrdersPendingApprovalQuery, GetOrdersPendingApprovalQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetOrdersPendingApprovalQuery, GetOrdersPendingApprovalQueryVariables>(GetOrdersPendingApprovalDocument, options);
        }
export type GetOrdersPendingApprovalQueryHookResult = ReturnType<typeof useGetOrdersPendingApprovalQuery>;
export type GetOrdersPendingApprovalLazyQueryHookResult = ReturnType<typeof useGetOrdersPendingApprovalLazyQuery>;
export type GetOrdersPendingApprovalQueryResult = Apollo.QueryResult<GetOrdersPendingApprovalQuery, GetOrdersPendingApprovalQueryVariables>;
export const OrderPendingApprovalDocument = gql`
    query orderPendingApproval($orderId: String!) {
  orderPendingApproval(orderId: $orderId) {
    orderId
    purchaseOrderNumber
    submissionDate
    submittedByName
    orderTotal
    cartId
  }
}
    `;

/**
 * __useOrderPendingApprovalQuery__
 *
 * To run a query within a React component, call `useOrderPendingApprovalQuery` and pass it any options that fit your needs.
 * When your component renders, `useOrderPendingApprovalQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useOrderPendingApprovalQuery({
 *   variables: {
 *      orderId: // value for 'orderId'
 *   },
 * });
 */
export function useOrderPendingApprovalQuery(baseOptions: Apollo.QueryHookOptions<OrderPendingApprovalQuery, OrderPendingApprovalQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<OrderPendingApprovalQuery, OrderPendingApprovalQueryVariables>(OrderPendingApprovalDocument, options);
      }
export function useOrderPendingApprovalLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<OrderPendingApprovalQuery, OrderPendingApprovalQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<OrderPendingApprovalQuery, OrderPendingApprovalQueryVariables>(OrderPendingApprovalDocument, options);
        }
export type OrderPendingApprovalQueryHookResult = ReturnType<typeof useOrderPendingApprovalQuery>;
export type OrderPendingApprovalLazyQueryHookResult = ReturnType<typeof useOrderPendingApprovalLazyQuery>;
export type OrderPendingApprovalQueryResult = Apollo.QueryResult<OrderPendingApprovalQuery, OrderPendingApprovalQueryVariables>;
export const ApproveOrderDocument = gql`
    mutation ApproveOrder($cartId: ID!, $userId: ID!, $shipToAccountId: ID!, $billToAccountId: ID!) {
  approveOrder(
    cartId: $cartId
    userId: $userId
    shipToAccountId: $shipToAccountId
    billToAccountId: $billToAccountId
  ) {
    orderNumber
    orderStatus
    webStatus
    shipDate
    customerPO
    orderDate
    amount
    invoiceNumber
    billToName
    shipToName
    orderedBy
    shipAddress {
      streetLineOne
      streetLineTwo
      streetLineThree
      city
      state
      postalCode
      country
    }
    email
    branchInfo {
      ...BranchOrderInfo
    }
    deliveryMethod
    specialInstructions
    subTotal
    tax
    orderTotal
    lineItems {
      unitPrice
      erpPartNumber
      orderQuantity
      shipQuantity
      productOrderTotal
      imageUrls {
        thumb
        small
        medium
        large
      }
      manufacturerName
      manufacturerNumber
      productName
      productId
    }
  }
}
    ${BranchOrderInfoFragmentDoc}`;
export type ApproveOrderMutationFn = Apollo.MutationFunction<ApproveOrderMutation, ApproveOrderMutationVariables>;

/**
 * __useApproveOrderMutation__
 *
 * To run a mutation, you first call `useApproveOrderMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useApproveOrderMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [approveOrderMutation, { data, loading, error }] = useApproveOrderMutation({
 *   variables: {
 *      cartId: // value for 'cartId'
 *      userId: // value for 'userId'
 *      shipToAccountId: // value for 'shipToAccountId'
 *      billToAccountId: // value for 'billToAccountId'
 *   },
 * });
 */
export function useApproveOrderMutation(baseOptions?: Apollo.MutationHookOptions<ApproveOrderMutation, ApproveOrderMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<ApproveOrderMutation, ApproveOrderMutationVariables>(ApproveOrderDocument, options);
      }
export type ApproveOrderMutationHookResult = ReturnType<typeof useApproveOrderMutation>;
export type ApproveOrderMutationResult = Apollo.MutationResult<ApproveOrderMutation>;
export type ApproveOrderMutationOptions = Apollo.BaseMutationOptions<ApproveOrderMutation, ApproveOrderMutationVariables>;
export const RejectOrderDocument = gql`
    mutation RejectOrder($cartId: ID!, $userId: ID!, $shipToAccountId: ID!, $rejectOrderInfo: RejectOrderInput!) {
  rejectOrder(
    cartId: $cartId
    userId: $userId
    shipToAccountId: $shipToAccountId
    rejectOrderInfo: $rejectOrderInfo
  )
}
    `;
export type RejectOrderMutationFn = Apollo.MutationFunction<RejectOrderMutation, RejectOrderMutationVariables>;

/**
 * __useRejectOrderMutation__
 *
 * To run a mutation, you first call `useRejectOrderMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useRejectOrderMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [rejectOrderMutation, { data, loading, error }] = useRejectOrderMutation({
 *   variables: {
 *      cartId: // value for 'cartId'
 *      userId: // value for 'userId'
 *      shipToAccountId: // value for 'shipToAccountId'
 *      rejectOrderInfo: // value for 'rejectOrderInfo'
 *   },
 * });
 */
export function useRejectOrderMutation(baseOptions?: Apollo.MutationHookOptions<RejectOrderMutation, RejectOrderMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<RejectOrderMutation, RejectOrderMutationVariables>(RejectOrderDocument, options);
      }
export type RejectOrderMutationHookResult = ReturnType<typeof useRejectOrderMutation>;
export type RejectOrderMutationResult = Apollo.MutationResult<RejectOrderMutation>;
export type RejectOrderMutationOptions = Apollo.BaseMutationOptions<RejectOrderMutation, RejectOrderMutationVariables>;
export const QuoteDocument = gql`
    query Quote($accountId: ID!, $quoteId: ID!, $userId: ID!) {
  quote(accountId: $accountId, quoteId: $quoteId, userId: $userId) {
    ...Quote
    amount
    branchInfo {
      ...BranchOrderInfo
    }
    deliveryMethod
    email
    lineItems {
      imageUrls {
        thumb
      }
      manufacturerName
      manufacturerNumber
      erpPartNumber
      orderQuantity
      productId
      productName
      productOrderTotal
      shipQuantity
      status
      unitPrice
    }
    shipAddress {
      streetLineOne
      streetLineTwo
      streetLineThree
      city
      state
      postalCode
      country
    }
    shipDate
    specialInstructions
    subTotal
    tax
  }
}
    ${QuoteFragmentDoc}
${BranchOrderInfoFragmentDoc}`;

/**
 * __useQuoteQuery__
 *
 * To run a query within a React component, call `useQuoteQuery` and pass it any options that fit your needs.
 * When your component renders, `useQuoteQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useQuoteQuery({
 *   variables: {
 *      accountId: // value for 'accountId'
 *      quoteId: // value for 'quoteId'
 *      userId: // value for 'userId'
 *   },
 * });
 */
export function useQuoteQuery(baseOptions: Apollo.QueryHookOptions<QuoteQuery, QuoteQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<QuoteQuery, QuoteQueryVariables>(QuoteDocument, options);
      }
export function useQuoteLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<QuoteQuery, QuoteQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<QuoteQuery, QuoteQueryVariables>(QuoteDocument, options);
        }
export type QuoteQueryHookResult = ReturnType<typeof useQuoteQuery>;
export type QuoteLazyQueryHookResult = ReturnType<typeof useQuoteLazyQuery>;
export type QuoteQueryResult = Apollo.QueryResult<QuoteQuery, QuoteQueryVariables>;
export const ApproveQuoteDocument = gql`
    mutation ApproveQuote($approveQuoteInput: ApproveQuoteInput!) {
  approveQuote(approveQuoteInput: $approveQuoteInput) {
    ...SubmitOrderResponse
  }
}
    ${SubmitOrderResponseFragmentDoc}`;
export type ApproveQuoteMutationFn = Apollo.MutationFunction<ApproveQuoteMutation, ApproveQuoteMutationVariables>;

/**
 * __useApproveQuoteMutation__
 *
 * To run a mutation, you first call `useApproveQuoteMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useApproveQuoteMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [approveQuoteMutation, { data, loading, error }] = useApproveQuoteMutation({
 *   variables: {
 *      approveQuoteInput: // value for 'approveQuoteInput'
 *   },
 * });
 */
export function useApproveQuoteMutation(baseOptions?: Apollo.MutationHookOptions<ApproveQuoteMutation, ApproveQuoteMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<ApproveQuoteMutation, ApproveQuoteMutationVariables>(ApproveQuoteDocument, options);
      }
export type ApproveQuoteMutationHookResult = ReturnType<typeof useApproveQuoteMutation>;
export type ApproveQuoteMutationResult = Apollo.MutationResult<ApproveQuoteMutation>;
export type ApproveQuoteMutationOptions = Apollo.BaseMutationOptions<ApproveQuoteMutation, ApproveQuoteMutationVariables>;
export const RejectQuoteDocument = gql`
    mutation RejectQuote($userId: ID!, $shipToAccountId: ID!, $quoteId: ID!) {
  rejectQuote(
    userId: $userId
    shipToAccountId: $shipToAccountId
    quoteId: $quoteId
  )
}
    `;
export type RejectQuoteMutationFn = Apollo.MutationFunction<RejectQuoteMutation, RejectQuoteMutationVariables>;

/**
 * __useRejectQuoteMutation__
 *
 * To run a mutation, you first call `useRejectQuoteMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useRejectQuoteMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [rejectQuoteMutation, { data, loading, error }] = useRejectQuoteMutation({
 *   variables: {
 *      userId: // value for 'userId'
 *      shipToAccountId: // value for 'shipToAccountId'
 *      quoteId: // value for 'quoteId'
 *   },
 * });
 */
export function useRejectQuoteMutation(baseOptions?: Apollo.MutationHookOptions<RejectQuoteMutation, RejectQuoteMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<RejectQuoteMutation, RejectQuoteMutationVariables>(RejectQuoteDocument, options);
      }
export type RejectQuoteMutationHookResult = ReturnType<typeof useRejectQuoteMutation>;
export type RejectQuoteMutationResult = Apollo.MutationResult<RejectQuoteMutation>;
export type RejectQuoteMutationOptions = Apollo.BaseMutationOptions<RejectQuoteMutation, RejectQuoteMutationVariables>;
export const QuotesDocument = gql`
    query Quotes($accountId: ID!) {
  quotes(accountId: $accountId) {
    ...Quote
  }
}
    ${QuoteFragmentDoc}`;

/**
 * __useQuotesQuery__
 *
 * To run a query within a React component, call `useQuotesQuery` and pass it any options that fit your needs.
 * When your component renders, `useQuotesQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useQuotesQuery({
 *   variables: {
 *      accountId: // value for 'accountId'
 *   },
 * });
 */
export function useQuotesQuery(baseOptions: Apollo.QueryHookOptions<QuotesQuery, QuotesQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<QuotesQuery, QuotesQueryVariables>(QuotesDocument, options);
      }
export function useQuotesLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<QuotesQuery, QuotesQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<QuotesQuery, QuotesQueryVariables>(QuotesDocument, options);
        }
export type QuotesQueryHookResult = ReturnType<typeof useQuotesQuery>;
export type QuotesLazyQueryHookResult = ReturnType<typeof useQuotesLazyQuery>;
export type QuotesQueryResult = Apollo.QueryResult<QuotesQuery, QuotesQueryVariables>;
export const AccountDocument = gql`
    query Account($accountId: String!, $brand: String!) {
  account(accountId: $accountId, brand: $brand) {
    branchId
    erpAccountId
    erpName
    companyName
    billToFlag
  }
}
    `;

/**
 * __useAccountQuery__
 *
 * To run a query within a React component, call `useAccountQuery` and pass it any options that fit your needs.
 * When your component renders, `useAccountQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useAccountQuery({
 *   variables: {
 *      accountId: // value for 'accountId'
 *      brand: // value for 'brand'
 *   },
 * });
 */
export function useAccountQuery(baseOptions: Apollo.QueryHookOptions<AccountQuery, AccountQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<AccountQuery, AccountQueryVariables>(AccountDocument, options);
      }
export function useAccountLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<AccountQuery, AccountQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<AccountQuery, AccountQueryVariables>(AccountDocument, options);
        }
export type AccountQueryHookResult = ReturnType<typeof useAccountQuery>;
export type AccountLazyQueryHookResult = ReturnType<typeof useAccountLazyQuery>;
export type AccountQueryResult = Apollo.QueryResult<AccountQuery, AccountQueryVariables>;
export const AccountDetailsDocument = gql`
    query AccountDetails($accountId: String!, $brand: String!) {
  account(accountId: $accountId, brand: $brand) {
    companyName
    street1
    street2
    city
    state
    zip
    erpName
  }
}
    `;

/**
 * __useAccountDetailsQuery__
 *
 * To run a query within a React component, call `useAccountDetailsQuery` and pass it any options that fit your needs.
 * When your component renders, `useAccountDetailsQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useAccountDetailsQuery({
 *   variables: {
 *      accountId: // value for 'accountId'
 *      brand: // value for 'brand'
 *   },
 * });
 */
export function useAccountDetailsQuery(baseOptions: Apollo.QueryHookOptions<AccountDetailsQuery, AccountDetailsQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<AccountDetailsQuery, AccountDetailsQueryVariables>(AccountDetailsDocument, options);
      }
export function useAccountDetailsLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<AccountDetailsQuery, AccountDetailsQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<AccountDetailsQuery, AccountDetailsQueryVariables>(AccountDetailsDocument, options);
        }
export type AccountDetailsQueryHookResult = ReturnType<typeof useAccountDetailsQuery>;
export type AccountDetailsLazyQueryHookResult = ReturnType<typeof useAccountDetailsLazyQuery>;
export type AccountDetailsQueryResult = Apollo.QueryResult<AccountDetailsQuery, AccountDetailsQueryVariables>;
export const GetContactInfoDocument = gql`
    query getContactInfo($userId: String) {
  contactInfo(userId: $userId) {
    isBranchInfo
    phoneNumber
    emailAddress
  }
}
    `;

/**
 * __useGetContactInfoQuery__
 *
 * To run a query within a React component, call `useGetContactInfoQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetContactInfoQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetContactInfoQuery({
 *   variables: {
 *      userId: // value for 'userId'
 *   },
 * });
 */
export function useGetContactInfoQuery(baseOptions?: Apollo.QueryHookOptions<GetContactInfoQuery, GetContactInfoQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetContactInfoQuery, GetContactInfoQueryVariables>(GetContactInfoDocument, options);
      }
export function useGetContactInfoLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetContactInfoQuery, GetContactInfoQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetContactInfoQuery, GetContactInfoQueryVariables>(GetContactInfoDocument, options);
        }
export type GetContactInfoQueryHookResult = ReturnType<typeof useGetContactInfoQuery>;
export type GetContactInfoLazyQueryHookResult = ReturnType<typeof useGetContactInfoLazyQuery>;
export type GetContactInfoQueryResult = Apollo.QueryResult<GetContactInfoQuery, GetContactInfoQueryVariables>;
export const GetPhoneTypesDocument = gql`
    query GetPhoneTypes {
  phoneTypes
}
    `;

/**
 * __useGetPhoneTypesQuery__
 *
 * To run a query within a React component, call `useGetPhoneTypesQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetPhoneTypesQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetPhoneTypesQuery({
 *   variables: {
 *   },
 * });
 */
export function useGetPhoneTypesQuery(baseOptions?: Apollo.QueryHookOptions<GetPhoneTypesQuery, GetPhoneTypesQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetPhoneTypesQuery, GetPhoneTypesQueryVariables>(GetPhoneTypesDocument, options);
      }
export function useGetPhoneTypesLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetPhoneTypesQuery, GetPhoneTypesQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetPhoneTypesQuery, GetPhoneTypesQueryVariables>(GetPhoneTypesDocument, options);
        }
export type GetPhoneTypesQueryHookResult = ReturnType<typeof useGetPhoneTypesQuery>;
export type GetPhoneTypesLazyQueryHookResult = ReturnType<typeof useGetPhoneTypesLazyQuery>;
export type GetPhoneTypesQueryResult = Apollo.QueryResult<GetPhoneTypesQuery, GetPhoneTypesQueryVariables>;
export const GetUserInviteDocument = gql`
    query GetUserInvite($inviteId: ID!) {
  userInvite(inviteId: $inviteId) {
    id
    email
    firstName
    lastName
    userRoleId
    approverId
    erpAccountId
    billToAccountId
    erpSystemName
    completed
  }
}
    `;

/**
 * __useGetUserInviteQuery__
 *
 * To run a query within a React component, call `useGetUserInviteQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetUserInviteQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetUserInviteQuery({
 *   variables: {
 *      inviteId: // value for 'inviteId'
 *   },
 * });
 */
export function useGetUserInviteQuery(baseOptions: Apollo.QueryHookOptions<GetUserInviteQuery, GetUserInviteQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetUserInviteQuery, GetUserInviteQueryVariables>(GetUserInviteDocument, options);
      }
export function useGetUserInviteLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetUserInviteQuery, GetUserInviteQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetUserInviteQuery, GetUserInviteQueryVariables>(GetUserInviteDocument, options);
        }
export type GetUserInviteQueryHookResult = ReturnType<typeof useGetUserInviteQuery>;
export type GetUserInviteLazyQueryHookResult = ReturnType<typeof useGetUserInviteLazyQuery>;
export type GetUserInviteQueryResult = Apollo.QueryResult<GetUserInviteQuery, GetUserInviteQueryVariables>;
export const CreateUserDocument = gql`
    mutation CreateUser($user: UserInput!, $inviteId: ID) {
  createUser(user: $user, inviteId: $inviteId) {
    id
    isEmployee
    isVerified
  }
}
    `;
export type CreateUserMutationFn = Apollo.MutationFunction<CreateUserMutation, CreateUserMutationVariables>;

/**
 * __useCreateUserMutation__
 *
 * To run a mutation, you first call `useCreateUserMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useCreateUserMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [createUserMutation, { data, loading, error }] = useCreateUserMutation({
 *   variables: {
 *      user: // value for 'user'
 *      inviteId: // value for 'inviteId'
 *   },
 * });
 */
export function useCreateUserMutation(baseOptions?: Apollo.MutationHookOptions<CreateUserMutation, CreateUserMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<CreateUserMutation, CreateUserMutationVariables>(CreateUserDocument, options);
      }
export type CreateUserMutationHookResult = ReturnType<typeof useCreateUserMutation>;
export type CreateUserMutationResult = Apollo.MutationResult<CreateUserMutation>;
export type CreateUserMutationOptions = Apollo.BaseMutationOptions<CreateUserMutation, CreateUserMutationVariables>;
export const VerifyUserDocument = gql`
    mutation verifyUser($verificationToken: ID!) {
  verifyUser(verificationToken: $verificationToken)
}
    `;
export type VerifyUserMutationFn = Apollo.MutationFunction<VerifyUserMutation, VerifyUserMutationVariables>;

/**
 * __useVerifyUserMutation__
 *
 * To run a mutation, you first call `useVerifyUserMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useVerifyUserMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [verifyUserMutation, { data, loading, error }] = useVerifyUserMutation({
 *   variables: {
 *      verificationToken: // value for 'verificationToken'
 *   },
 * });
 */
export function useVerifyUserMutation(baseOptions?: Apollo.MutationHookOptions<VerifyUserMutation, VerifyUserMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<VerifyUserMutation, VerifyUserMutationVariables>(VerifyUserDocument, options);
      }
export type VerifyUserMutationHookResult = ReturnType<typeof useVerifyUserMutation>;
export type VerifyUserMutationResult = Apollo.MutationResult<VerifyUserMutation>;
export type VerifyUserMutationOptions = Apollo.BaseMutationOptions<VerifyUserMutation, VerifyUserMutationVariables>;
export const ResendVerificationEmailDocument = gql`
    mutation resendVerificationEmail($userId: ID!, $isWaterworksSubdomain: Boolean) {
  resendVerificationEmail(
    userId: $userId
    isWaterworksSubdomain: $isWaterworksSubdomain
  )
}
    `;
export type ResendVerificationEmailMutationFn = Apollo.MutationFunction<ResendVerificationEmailMutation, ResendVerificationEmailMutationVariables>;

/**
 * __useResendVerificationEmailMutation__
 *
 * To run a mutation, you first call `useResendVerificationEmailMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useResendVerificationEmailMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [resendVerificationEmailMutation, { data, loading, error }] = useResendVerificationEmailMutation({
 *   variables: {
 *      userId: // value for 'userId'
 *      isWaterworksSubdomain: // value for 'isWaterworksSubdomain'
 *   },
 * });
 */
export function useResendVerificationEmailMutation(baseOptions?: Apollo.MutationHookOptions<ResendVerificationEmailMutation, ResendVerificationEmailMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<ResendVerificationEmailMutation, ResendVerificationEmailMutationVariables>(ResendVerificationEmailDocument, options);
      }
export type ResendVerificationEmailMutationHookResult = ReturnType<typeof useResendVerificationEmailMutation>;
export type ResendVerificationEmailMutationResult = Apollo.MutationResult<ResendVerificationEmailMutation>;
export type ResendVerificationEmailMutationOptions = Apollo.BaseMutationOptions<ResendVerificationEmailMutation, ResendVerificationEmailMutationVariables>;
export const CreateNewEmployeeDocument = gql`
    mutation CreateNewEmployee($employee: CreateEmployeeInput!) {
  createNewEmployee(employee: $employee) {
    id
    firstName
    lastName
    email
    phoneNumber
    roleId
    approverId
  }
}
    `;
export type CreateNewEmployeeMutationFn = Apollo.MutationFunction<CreateNewEmployeeMutation, CreateNewEmployeeMutationVariables>;

/**
 * __useCreateNewEmployeeMutation__
 *
 * To run a mutation, you first call `useCreateNewEmployeeMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useCreateNewEmployeeMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [createNewEmployeeMutation, { data, loading, error }] = useCreateNewEmployeeMutation({
 *   variables: {
 *      employee: // value for 'employee'
 *   },
 * });
 */
export function useCreateNewEmployeeMutation(baseOptions?: Apollo.MutationHookOptions<CreateNewEmployeeMutation, CreateNewEmployeeMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<CreateNewEmployeeMutation, CreateNewEmployeeMutationVariables>(CreateNewEmployeeDocument, options);
      }
export type CreateNewEmployeeMutationHookResult = ReturnType<typeof useCreateNewEmployeeMutation>;
export type CreateNewEmployeeMutationResult = Apollo.MutationResult<CreateNewEmployeeMutation>;
export type CreateNewEmployeeMutationOptions = Apollo.BaseMutationOptions<CreateNewEmployeeMutation, CreateNewEmployeeMutationVariables>;
export const CreateNewUserDocument = gql`
    mutation CreateNewUser($user: CreateUserInput!, $inviteId: ID) {
  createNewUser(user: $user, inviteId: $inviteId) {
    id
    firstName
    lastName
    email
    phoneNumber
    roleId
    approverId
  }
}
    `;
export type CreateNewUserMutationFn = Apollo.MutationFunction<CreateNewUserMutation, CreateNewUserMutationVariables>;

/**
 * __useCreateNewUserMutation__
 *
 * To run a mutation, you first call `useCreateNewUserMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useCreateNewUserMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [createNewUserMutation, { data, loading, error }] = useCreateNewUserMutation({
 *   variables: {
 *      user: // value for 'user'
 *      inviteId: // value for 'inviteId'
 *   },
 * });
 */
export function useCreateNewUserMutation(baseOptions?: Apollo.MutationHookOptions<CreateNewUserMutation, CreateNewUserMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<CreateNewUserMutation, CreateNewUserMutationVariables>(CreateNewUserDocument, options);
      }
export type CreateNewUserMutationHookResult = ReturnType<typeof useCreateNewUserMutation>;
export type CreateNewUserMutationResult = Apollo.MutationResult<CreateNewUserMutation>;
export type CreateNewUserMutationOptions = Apollo.BaseMutationOptions<CreateNewUserMutation, CreateNewUserMutationVariables>;
export const VerifyAccountNewDocument = gql`
    query VerifyAccountNew($input: VerifyAccountInput!) {
  verifyAccountNew(input: $input) {
    accountName
    isTradeAccount
  }
}
    `;

/**
 * __useVerifyAccountNewQuery__
 *
 * To run a query within a React component, call `useVerifyAccountNewQuery` and pass it any options that fit your needs.
 * When your component renders, `useVerifyAccountNewQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useVerifyAccountNewQuery({
 *   variables: {
 *      input: // value for 'input'
 *   },
 * });
 */
export function useVerifyAccountNewQuery(baseOptions: Apollo.QueryHookOptions<VerifyAccountNewQuery, VerifyAccountNewQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<VerifyAccountNewQuery, VerifyAccountNewQueryVariables>(VerifyAccountNewDocument, options);
      }
export function useVerifyAccountNewLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<VerifyAccountNewQuery, VerifyAccountNewQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<VerifyAccountNewQuery, VerifyAccountNewQueryVariables>(VerifyAccountNewDocument, options);
        }
export type VerifyAccountNewQueryHookResult = ReturnType<typeof useVerifyAccountNewQuery>;
export type VerifyAccountNewLazyQueryHookResult = ReturnType<typeof useVerifyAccountNewLazyQuery>;
export type VerifyAccountNewQueryResult = Apollo.QueryResult<VerifyAccountNewQuery, VerifyAccountNewQueryVariables>;
export const VerifyUserEmailDocument = gql`
    query VerifyUserEmail($email: String!) {
  verifyUserEmail(email: $email) {
    isValid
    isEmployee
  }
}
    `;

/**
 * __useVerifyUserEmailQuery__
 *
 * To run a query within a React component, call `useVerifyUserEmailQuery` and pass it any options that fit your needs.
 * When your component renders, `useVerifyUserEmailQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useVerifyUserEmailQuery({
 *   variables: {
 *      email: // value for 'email'
 *   },
 * });
 */
export function useVerifyUserEmailQuery(baseOptions: Apollo.QueryHookOptions<VerifyUserEmailQuery, VerifyUserEmailQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<VerifyUserEmailQuery, VerifyUserEmailQueryVariables>(VerifyUserEmailDocument, options);
      }
export function useVerifyUserEmailLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<VerifyUserEmailQuery, VerifyUserEmailQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<VerifyUserEmailQuery, VerifyUserEmailQueryVariables>(VerifyUserEmailDocument, options);
        }
export type VerifyUserEmailQueryHookResult = ReturnType<typeof useVerifyUserEmailQuery>;
export type VerifyUserEmailLazyQueryHookResult = ReturnType<typeof useVerifyUserEmailLazyQuery>;
export type VerifyUserEmailQueryResult = Apollo.QueryResult<VerifyUserEmailQuery, VerifyUserEmailQueryVariables>;
export const SearchProductDocument = gql`
    query SearchProduct($productSearch: ProductSearchInput, $userId: ID, $shipToAccountId: ID) {
  searchProduct(
    productSearch: $productSearch
    userId: $userId
    shipToAccountId: $shipToAccountId
  ) {
    pagination {
      currentPage
      pageSize
      totalItemCount
    }
    products {
      ...Product
    }
    aggregates {
      inStockLocation {
        value
        count
      }
      category {
        value
        count
      }
      productTypes {
        value
        count
      }
      brands {
        value
        count
      }
      lines {
        value
        count
      }
      flowRate {
        value
        count
      }
      environmentalOptions {
        value
        count
      }
      material {
        value
        count
      }
      colorFinish {
        value
        count
      }
      size {
        value
        count
      }
      length {
        value
        count
      }
      width {
        value
        count
      }
      height {
        value
        count
      }
      depth {
        value
        count
      }
      voltage {
        value
        count
      }
      tonnage {
        value
        count
      }
      btu {
        value
        count
      }
      pressureRating {
        value
        count
      }
      temperatureRating {
        value
        count
      }
      inletSize {
        value
        count
      }
      capacity {
        value
        count
      }
      wattage {
        value
        count
      }
    }
    selectedAttributes {
      attributeType
      attributeValue
    }
  }
}
    ${ProductFragmentDoc}`;

/**
 * __useSearchProductQuery__
 *
 * To run a query within a React component, call `useSearchProductQuery` and pass it any options that fit your needs.
 * When your component renders, `useSearchProductQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useSearchProductQuery({
 *   variables: {
 *      productSearch: // value for 'productSearch'
 *      userId: // value for 'userId'
 *      shipToAccountId: // value for 'shipToAccountId'
 *   },
 * });
 */
export function useSearchProductQuery(baseOptions?: Apollo.QueryHookOptions<SearchProductQuery, SearchProductQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<SearchProductQuery, SearchProductQueryVariables>(SearchProductDocument, options);
      }
export function useSearchProductLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<SearchProductQuery, SearchProductQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<SearchProductQuery, SearchProductQueryVariables>(SearchProductDocument, options);
        }
export type SearchProductQueryHookResult = ReturnType<typeof useSearchProductQuery>;
export type SearchProductLazyQueryHookResult = ReturnType<typeof useSearchProductLazyQuery>;
export type SearchProductQueryResult = Apollo.QueryResult<SearchProductQuery, SearchProductQueryVariables>;
export const SearchSuggestionDocument = gql`
    query searchSuggestion($term: String!, $engine: String!, $size: Int, $userId: ID, $shipToAccountId: ID, $erpSystem: String, $state: String) {
  searchSuggestion(
    term: $term
    engine: $engine
    size: $size
    userId: $userId
    shipToAccountId: $shipToAccountId
    erpSystem: $erpSystem
    state: $state
  ) {
    suggestions
    topCategories {
      value
      count
    }
    topProducts {
      id
      name
      manufacturerName
      manufacturerNumber
      imageUrls {
        thumb
      }
    }
  }
}
    `;

/**
 * __useSearchSuggestionQuery__
 *
 * To run a query within a React component, call `useSearchSuggestionQuery` and pass it any options that fit your needs.
 * When your component renders, `useSearchSuggestionQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useSearchSuggestionQuery({
 *   variables: {
 *      term: // value for 'term'
 *      engine: // value for 'engine'
 *      size: // value for 'size'
 *      userId: // value for 'userId'
 *      shipToAccountId: // value for 'shipToAccountId'
 *      erpSystem: // value for 'erpSystem'
 *      state: // value for 'state'
 *   },
 * });
 */
export function useSearchSuggestionQuery(baseOptions: Apollo.QueryHookOptions<SearchSuggestionQuery, SearchSuggestionQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<SearchSuggestionQuery, SearchSuggestionQueryVariables>(SearchSuggestionDocument, options);
      }
export function useSearchSuggestionLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<SearchSuggestionQuery, SearchSuggestionQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<SearchSuggestionQuery, SearchSuggestionQueryVariables>(SearchSuggestionDocument, options);
        }
export type SearchSuggestionQueryHookResult = ReturnType<typeof useSearchSuggestionQuery>;
export type SearchSuggestionLazyQueryHookResult = ReturnType<typeof useSearchSuggestionLazyQuery>;
export type SearchSuggestionQueryResult = Apollo.QueryResult<SearchSuggestionQuery, SearchSuggestionQueryVariables>;
export const GetRefreshShipToAccountDocument = gql`
    query getRefreshShipToAccount($billToAccountId: String!) {
  refreshShipToAccount(billToAccountId: $billToAccountId) {
    id
    name
  }
}
    `;

/**
 * __useGetRefreshShipToAccountQuery__
 *
 * To run a query within a React component, call `useGetRefreshShipToAccountQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetRefreshShipToAccountQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetRefreshShipToAccountQuery({
 *   variables: {
 *      billToAccountId: // value for 'billToAccountId'
 *   },
 * });
 */
export function useGetRefreshShipToAccountQuery(baseOptions: Apollo.QueryHookOptions<GetRefreshShipToAccountQuery, GetRefreshShipToAccountQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetRefreshShipToAccountQuery, GetRefreshShipToAccountQueryVariables>(GetRefreshShipToAccountDocument, options);
      }
export function useGetRefreshShipToAccountLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetRefreshShipToAccountQuery, GetRefreshShipToAccountQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetRefreshShipToAccountQuery, GetRefreshShipToAccountQueryVariables>(GetRefreshShipToAccountDocument, options);
        }
export type GetRefreshShipToAccountQueryHookResult = ReturnType<typeof useGetRefreshShipToAccountQuery>;
export type GetRefreshShipToAccountLazyQueryHookResult = ReturnType<typeof useGetRefreshShipToAccountLazyQuery>;
export type GetRefreshShipToAccountQueryResult = Apollo.QueryResult<GetRefreshShipToAccountQuery, GetRefreshShipToAccountQueryVariables>;
export const GetHomeBranchDocument = gql`
    query getHomeBranch($shipToAccountId: String!) {
  homeBranch(shipToAccountId: $shipToAccountId) {
    branchId
    name
    entityId
    address1
    address2
    city
    state
    zip
    phone
    longitude
    latitude
  }
}
    `;

/**
 * __useGetHomeBranchQuery__
 *
 * To run a query within a React component, call `useGetHomeBranchQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetHomeBranchQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetHomeBranchQuery({
 *   variables: {
 *      shipToAccountId: // value for 'shipToAccountId'
 *   },
 * });
 */
export function useGetHomeBranchQuery(baseOptions: Apollo.QueryHookOptions<GetHomeBranchQuery, GetHomeBranchQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetHomeBranchQuery, GetHomeBranchQueryVariables>(GetHomeBranchDocument, options);
      }
export function useGetHomeBranchLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetHomeBranchQuery, GetHomeBranchQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetHomeBranchQuery, GetHomeBranchQueryVariables>(GetHomeBranchDocument, options);
        }
export type GetHomeBranchQueryHookResult = ReturnType<typeof useGetHomeBranchQuery>;
export type GetHomeBranchLazyQueryHookResult = ReturnType<typeof useGetHomeBranchLazyQuery>;
export type GetHomeBranchQueryResult = Apollo.QueryResult<GetHomeBranchQuery, GetHomeBranchQueryVariables>;
export const SendContactFormDocument = gql`
    mutation sendContactForm($contactFormInput: ContactFormInput!) {
  sendContactForm(contactFormInput: $contactFormInput)
}
    `;
export type SendContactFormMutationFn = Apollo.MutationFunction<SendContactFormMutation, SendContactFormMutationVariables>;

/**
 * __useSendContactFormMutation__
 *
 * To run a mutation, you first call `useSendContactFormMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useSendContactFormMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [sendContactFormMutation, { data, loading, error }] = useSendContactFormMutation({
 *   variables: {
 *      contactFormInput: // value for 'contactFormInput'
 *   },
 * });
 */
export function useSendContactFormMutation(baseOptions?: Apollo.MutationHookOptions<SendContactFormMutation, SendContactFormMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<SendContactFormMutation, SendContactFormMutationVariables>(SendContactFormDocument, options);
      }
export type SendContactFormMutationHookResult = ReturnType<typeof useSendContactFormMutation>;
export type SendContactFormMutationResult = Apollo.MutationResult<SendContactFormMutation>;
export type SendContactFormMutationOptions = Apollo.BaseMutationOptions<SendContactFormMutation, SendContactFormMutationVariables>;
export const RefreshContactDocument = gql`
    mutation refreshContact($emails: [String!]!) {
  refreshContact(emails: $emails)
}
    `;
export type RefreshContactMutationFn = Apollo.MutationFunction<RefreshContactMutation, RefreshContactMutationVariables>;

/**
 * __useRefreshContactMutation__
 *
 * To run a mutation, you first call `useRefreshContactMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useRefreshContactMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [refreshContactMutation, { data, loading, error }] = useRefreshContactMutation({
 *   variables: {
 *      emails: // value for 'emails'
 *   },
 * });
 */
export function useRefreshContactMutation(baseOptions?: Apollo.MutationHookOptions<RefreshContactMutation, RefreshContactMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<RefreshContactMutation, RefreshContactMutationVariables>(RefreshContactDocument, options);
      }
export type RefreshContactMutationHookResult = ReturnType<typeof useRefreshContactMutation>;
export type RefreshContactMutationResult = Apollo.MutationResult<RefreshContactMutation>;
export type RefreshContactMutationOptions = Apollo.BaseMutationOptions<RefreshContactMutation, RefreshContactMutationVariables>;
export const ApproveUserDocument = gql`
    mutation ApproveUser($user: ApproveUserInput!) {
  approveUser(user: $user)
}
    `;
export type ApproveUserMutationFn = Apollo.MutationFunction<ApproveUserMutation, ApproveUserMutationVariables>;

/**
 * __useApproveUserMutation__
 *
 * To run a mutation, you first call `useApproveUserMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useApproveUserMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [approveUserMutation, { data, loading, error }] = useApproveUserMutation({
 *   variables: {
 *      user: // value for 'user'
 *   },
 * });
 */
export function useApproveUserMutation(baseOptions?: Apollo.MutationHookOptions<ApproveUserMutation, ApproveUserMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<ApproveUserMutation, ApproveUserMutationVariables>(ApproveUserDocument, options);
      }
export type ApproveUserMutationHookResult = ReturnType<typeof useApproveUserMutation>;
export type ApproveUserMutationResult = Apollo.MutationResult<ApproveUserMutation>;
export type ApproveUserMutationOptions = Apollo.BaseMutationOptions<ApproveUserMutation, ApproveUserMutationVariables>;
export const DeleteUserDocument = gql`
    mutation DeleteUser($deleteUserInput: DeleteUserInput!) {
  deleteUser(deleteUserInput: $deleteUserInput)
}
    `;
export type DeleteUserMutationFn = Apollo.MutationFunction<DeleteUserMutation, DeleteUserMutationVariables>;

/**
 * __useDeleteUserMutation__
 *
 * To run a mutation, you first call `useDeleteUserMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useDeleteUserMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [deleteUserMutation, { data, loading, error }] = useDeleteUserMutation({
 *   variables: {
 *      deleteUserInput: // value for 'deleteUserInput'
 *   },
 * });
 */
export function useDeleteUserMutation(baseOptions?: Apollo.MutationHookOptions<DeleteUserMutation, DeleteUserMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<DeleteUserMutation, DeleteUserMutationVariables>(DeleteUserDocument, options);
      }
export type DeleteUserMutationHookResult = ReturnType<typeof useDeleteUserMutation>;
export type DeleteUserMutationResult = Apollo.MutationResult<DeleteUserMutation>;
export type DeleteUserMutationOptions = Apollo.BaseMutationOptions<DeleteUserMutation, DeleteUserMutationVariables>;
export const CheckUsersForApproverDocument = gql`
    mutation checkUsersForApprover($checkUsersForApproverInput: CheckUsersForApproverInput!) {
  checkUsersForApprover(checkUsersForApproverInput: $checkUsersForApproverInput) {
    users {
      firstName
      lastName
      email
    }
  }
}
    `;
export type CheckUsersForApproverMutationFn = Apollo.MutationFunction<CheckUsersForApproverMutation, CheckUsersForApproverMutationVariables>;

/**
 * __useCheckUsersForApproverMutation__
 *
 * To run a mutation, you first call `useCheckUsersForApproverMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useCheckUsersForApproverMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [checkUsersForApproverMutation, { data, loading, error }] = useCheckUsersForApproverMutation({
 *   variables: {
 *      checkUsersForApproverInput: // value for 'checkUsersForApproverInput'
 *   },
 * });
 */
export function useCheckUsersForApproverMutation(baseOptions?: Apollo.MutationHookOptions<CheckUsersForApproverMutation, CheckUsersForApproverMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<CheckUsersForApproverMutation, CheckUsersForApproverMutationVariables>(CheckUsersForApproverDocument, options);
      }
export type CheckUsersForApproverMutationHookResult = ReturnType<typeof useCheckUsersForApproverMutation>;
export type CheckUsersForApproverMutationResult = Apollo.MutationResult<CheckUsersForApproverMutation>;
export type CheckUsersForApproverMutationOptions = Apollo.BaseMutationOptions<CheckUsersForApproverMutation, CheckUsersForApproverMutationVariables>;
export const GetRejectionReasonsDocument = gql`
    query getRejectionReasons {
  rejectionReasons
}
    `;

/**
 * __useGetRejectionReasonsQuery__
 *
 * To run a query within a React component, call `useGetRejectionReasonsQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetRejectionReasonsQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetRejectionReasonsQuery({
 *   variables: {
 *   },
 * });
 */
export function useGetRejectionReasonsQuery(baseOptions?: Apollo.QueryHookOptions<GetRejectionReasonsQuery, GetRejectionReasonsQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetRejectionReasonsQuery, GetRejectionReasonsQueryVariables>(GetRejectionReasonsDocument, options);
      }
export function useGetRejectionReasonsLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetRejectionReasonsQuery, GetRejectionReasonsQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetRejectionReasonsQuery, GetRejectionReasonsQueryVariables>(GetRejectionReasonsDocument, options);
        }
export type GetRejectionReasonsQueryHookResult = ReturnType<typeof useGetRejectionReasonsQuery>;
export type GetRejectionReasonsLazyQueryHookResult = ReturnType<typeof useGetRejectionReasonsLazyQuery>;
export type GetRejectionReasonsQueryResult = Apollo.QueryResult<GetRejectionReasonsQuery, GetRejectionReasonsQueryVariables>;
export const RejectUserDocument = gql`
    mutation RejectUser($rejectUserInput: RejectUserInput!) {
  rejectUser(rejectUserInput: $rejectUserInput)
}
    `;
export type RejectUserMutationFn = Apollo.MutationFunction<RejectUserMutation, RejectUserMutationVariables>;

/**
 * __useRejectUserMutation__
 *
 * To run a mutation, you first call `useRejectUserMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useRejectUserMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [rejectUserMutation, { data, loading, error }] = useRejectUserMutation({
 *   variables: {
 *      rejectUserInput: // value for 'rejectUserInput'
 *   },
 * });
 */
export function useRejectUserMutation(baseOptions?: Apollo.MutationHookOptions<RejectUserMutation, RejectUserMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<RejectUserMutation, RejectUserMutationVariables>(RejectUserDocument, options);
      }
export type RejectUserMutationHookResult = ReturnType<typeof useRejectUserMutation>;
export type RejectUserMutationResult = Apollo.MutationResult<RejectUserMutation>;
export type RejectUserMutationOptions = Apollo.BaseMutationOptions<RejectUserMutation, RejectUserMutationVariables>;
export const GetRolesDocument = gql`
    query GetRoles {
  roles {
    id
    name
    description
  }
}
    `;

/**
 * __useGetRolesQuery__
 *
 * To run a query within a React component, call `useGetRolesQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetRolesQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetRolesQuery({
 *   variables: {
 *   },
 * });
 */
export function useGetRolesQuery(baseOptions?: Apollo.QueryHookOptions<GetRolesQuery, GetRolesQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetRolesQuery, GetRolesQueryVariables>(GetRolesDocument, options);
      }
export function useGetRolesLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetRolesQuery, GetRolesQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetRolesQuery, GetRolesQueryVariables>(GetRolesDocument, options);
        }
export type GetRolesQueryHookResult = ReturnType<typeof useGetRolesQuery>;
export type GetRolesLazyQueryHookResult = ReturnType<typeof useGetRolesLazyQuery>;
export type GetRolesQueryResult = Apollo.QueryResult<GetRolesQuery, GetRolesQueryVariables>;
export const GetApproversDocument = gql`
    query GetApprovers($billToAccountId: ID!) {
  approvers(billToAccountId: $billToAccountId) {
    id
    firstName
    lastName
    email
  }
}
    `;

/**
 * __useGetApproversQuery__
 *
 * To run a query within a React component, call `useGetApproversQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetApproversQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetApproversQuery({
 *   variables: {
 *      billToAccountId: // value for 'billToAccountId'
 *   },
 * });
 */
export function useGetApproversQuery(baseOptions: Apollo.QueryHookOptions<GetApproversQuery, GetApproversQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetApproversQuery, GetApproversQueryVariables>(GetApproversDocument, options);
      }
export function useGetApproversLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetApproversQuery, GetApproversQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetApproversQuery, GetApproversQueryVariables>(GetApproversDocument, options);
        }
export type GetApproversQueryHookResult = ReturnType<typeof useGetApproversQuery>;
export type GetApproversLazyQueryHookResult = ReturnType<typeof useGetApproversLazyQuery>;
export type GetApproversQueryResult = Apollo.QueryResult<GetApproversQuery, GetApproversQueryVariables>;
export const UpdateUserDocument = gql`
    mutation UpdateUser($updateUserInput: UpdateUserInput!) {
  updateUser(updateUserInput: $updateUserInput) {
    id
  }
}
    `;
export type UpdateUserMutationFn = Apollo.MutationFunction<UpdateUserMutation, UpdateUserMutationVariables>;

/**
 * __useUpdateUserMutation__
 *
 * To run a mutation, you first call `useUpdateUserMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useUpdateUserMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [updateUserMutation, { data, loading, error }] = useUpdateUserMutation({
 *   variables: {
 *      updateUserInput: // value for 'updateUserInput'
 *   },
 * });
 */
export function useUpdateUserMutation(baseOptions?: Apollo.MutationHookOptions<UpdateUserMutation, UpdateUserMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<UpdateUserMutation, UpdateUserMutationVariables>(UpdateUserDocument, options);
      }
export type UpdateUserMutationHookResult = ReturnType<typeof useUpdateUserMutation>;
export type UpdateUserMutationResult = Apollo.MutationResult<UpdateUserMutation>;
export type UpdateUserMutationOptions = Apollo.BaseMutationOptions<UpdateUserMutation, UpdateUserMutationVariables>;
export const UnapprovedAccountRequestsDocument = gql`
    query unapprovedAccountRequests($accountId: String!) {
  unapprovedAccountRequests(accountId: $accountId) {
    id
    email
    firstName
    lastName
    companyName
    phoneNumber
    roleId
    phoneType
    createdAt
  }
}
    `;

/**
 * __useUnapprovedAccountRequestsQuery__
 *
 * To run a query within a React component, call `useUnapprovedAccountRequestsQuery` and pass it any options that fit your needs.
 * When your component renders, `useUnapprovedAccountRequestsQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useUnapprovedAccountRequestsQuery({
 *   variables: {
 *      accountId: // value for 'accountId'
 *   },
 * });
 */
export function useUnapprovedAccountRequestsQuery(baseOptions: Apollo.QueryHookOptions<UnapprovedAccountRequestsQuery, UnapprovedAccountRequestsQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<UnapprovedAccountRequestsQuery, UnapprovedAccountRequestsQueryVariables>(UnapprovedAccountRequestsDocument, options);
      }
export function useUnapprovedAccountRequestsLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<UnapprovedAccountRequestsQuery, UnapprovedAccountRequestsQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<UnapprovedAccountRequestsQuery, UnapprovedAccountRequestsQueryVariables>(UnapprovedAccountRequestsDocument, options);
        }
export type UnapprovedAccountRequestsQueryHookResult = ReturnType<typeof useUnapprovedAccountRequestsQuery>;
export type UnapprovedAccountRequestsLazyQueryHookResult = ReturnType<typeof useUnapprovedAccountRequestsLazyQuery>;
export type UnapprovedAccountRequestsQueryResult = Apollo.QueryResult<UnapprovedAccountRequestsQuery, UnapprovedAccountRequestsQueryVariables>;
export const GetApprovedAccountRequestsDocument = gql`
    query getApprovedAccountRequests($accountId: String) {
  accountUsers(accountId: $accountId) {
    id
    email
    firstName
    lastName
    phoneNumber
    contactUpdatedAt
    contactUpdatedBy
    role {
      id
      name
    }
    approverId
    phoneType
  }
}
    `;

/**
 * __useGetApprovedAccountRequestsQuery__
 *
 * To run a query within a React component, call `useGetApprovedAccountRequestsQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetApprovedAccountRequestsQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetApprovedAccountRequestsQuery({
 *   variables: {
 *      accountId: // value for 'accountId'
 *   },
 * });
 */
export function useGetApprovedAccountRequestsQuery(baseOptions?: Apollo.QueryHookOptions<GetApprovedAccountRequestsQuery, GetApprovedAccountRequestsQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetApprovedAccountRequestsQuery, GetApprovedAccountRequestsQueryVariables>(GetApprovedAccountRequestsDocument, options);
      }
export function useGetApprovedAccountRequestsLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetApprovedAccountRequestsQuery, GetApprovedAccountRequestsQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetApprovedAccountRequestsQuery, GetApprovedAccountRequestsQueryVariables>(GetApprovedAccountRequestsDocument, options);
        }
export type GetApprovedAccountRequestsQueryHookResult = ReturnType<typeof useGetApprovedAccountRequestsQuery>;
export type GetApprovedAccountRequestsLazyQueryHookResult = ReturnType<typeof useGetApprovedAccountRequestsLazyQuery>;
export type GetApprovedAccountRequestsQueryResult = Apollo.QueryResult<GetApprovedAccountRequestsQuery, GetApprovedAccountRequestsQueryVariables>;
export const RejectedAccountRequestsDocument = gql`
    query rejectedAccountRequests($accountId: String!) {
  rejectedAccountRequests(accountId: $accountId) {
    id
    email
    firstName
    lastName
    companyName
    phoneNumber
    phoneType
    rejectionReason
    rejectedAt
    rejectedBy
  }
}
    `;

/**
 * __useRejectedAccountRequestsQuery__
 *
 * To run a query within a React component, call `useRejectedAccountRequestsQuery` and pass it any options that fit your needs.
 * When your component renders, `useRejectedAccountRequestsQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useRejectedAccountRequestsQuery({
 *   variables: {
 *      accountId: // value for 'accountId'
 *   },
 * });
 */
export function useRejectedAccountRequestsQuery(baseOptions: Apollo.QueryHookOptions<RejectedAccountRequestsQuery, RejectedAccountRequestsQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<RejectedAccountRequestsQuery, RejectedAccountRequestsQueryVariables>(RejectedAccountRequestsDocument, options);
      }
export function useRejectedAccountRequestsLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<RejectedAccountRequestsQuery, RejectedAccountRequestsQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<RejectedAccountRequestsQuery, RejectedAccountRequestsQueryVariables>(RejectedAccountRequestsDocument, options);
        }
export type RejectedAccountRequestsQueryHookResult = ReturnType<typeof useRejectedAccountRequestsQuery>;
export type RejectedAccountRequestsLazyQueryHookResult = ReturnType<typeof useRejectedAccountRequestsLazyQuery>;
export type RejectedAccountRequestsQueryResult = Apollo.QueryResult<RejectedAccountRequestsQuery, RejectedAccountRequestsQueryVariables>;
export const GetSelectedErpAccountDocument = gql`
    query GetSelectedErpAccount($accountId: String!, $brand: String!) {
  account(accountId: $accountId, brand: $brand) {
    ...ErpAccount
  }
}
    ${ErpAccountFragmentDoc}`;

/**
 * __useGetSelectedErpAccountQuery__
 *
 * To run a query within a React component, call `useGetSelectedErpAccountQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetSelectedErpAccountQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetSelectedErpAccountQuery({
 *   variables: {
 *      accountId: // value for 'accountId'
 *      brand: // value for 'brand'
 *   },
 * });
 */
export function useGetSelectedErpAccountQuery(baseOptions: Apollo.QueryHookOptions<GetSelectedErpAccountQuery, GetSelectedErpAccountQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetSelectedErpAccountQuery, GetSelectedErpAccountQueryVariables>(GetSelectedErpAccountDocument, options);
      }
export function useGetSelectedErpAccountLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetSelectedErpAccountQuery, GetSelectedErpAccountQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetSelectedErpAccountQuery, GetSelectedErpAccountQueryVariables>(GetSelectedErpAccountDocument, options);
        }
export type GetSelectedErpAccountQueryHookResult = ReturnType<typeof useGetSelectedErpAccountQuery>;
export type GetSelectedErpAccountLazyQueryHookResult = ReturnType<typeof useGetSelectedErpAccountLazyQuery>;
export type GetSelectedErpAccountQueryResult = Apollo.QueryResult<GetSelectedErpAccountQuery, GetSelectedErpAccountQueryVariables>;
export const GetSelectedErpAccountsDocument = gql`
    query GetSelectedErpAccounts($billToId: String!, $shipToId: String!, $brand: String!) {
  billToAccount: account(accountId: $billToId, brand: $brand) {
    ...ErpAccount
  }
  shipToAccount: account(accountId: $shipToId, brand: $brand) {
    ...ErpAccount
  }
}
    ${ErpAccountFragmentDoc}`;

/**
 * __useGetSelectedErpAccountsQuery__
 *
 * To run a query within a React component, call `useGetSelectedErpAccountsQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetSelectedErpAccountsQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetSelectedErpAccountsQuery({
 *   variables: {
 *      billToId: // value for 'billToId'
 *      shipToId: // value for 'shipToId'
 *      brand: // value for 'brand'
 *   },
 * });
 */
export function useGetSelectedErpAccountsQuery(baseOptions: Apollo.QueryHookOptions<GetSelectedErpAccountsQuery, GetSelectedErpAccountsQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetSelectedErpAccountsQuery, GetSelectedErpAccountsQueryVariables>(GetSelectedErpAccountsDocument, options);
      }
export function useGetSelectedErpAccountsLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetSelectedErpAccountsQuery, GetSelectedErpAccountsQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetSelectedErpAccountsQuery, GetSelectedErpAccountsQueryVariables>(GetSelectedErpAccountsDocument, options);
        }
export type GetSelectedErpAccountsQueryHookResult = ReturnType<typeof useGetSelectedErpAccountsQuery>;
export type GetSelectedErpAccountsLazyQueryHookResult = ReturnType<typeof useGetSelectedErpAccountsLazyQuery>;
export type GetSelectedErpAccountsQueryResult = Apollo.QueryResult<GetSelectedErpAccountsQuery, GetSelectedErpAccountsQueryVariables>;