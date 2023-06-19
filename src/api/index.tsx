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
};

export type AddToCountMutationResponse = {
  __typename?: 'AddToCountMutationResponse';
  item: LocationItem;
  message?: Maybe<Scalars['String']>;
  success: Scalars['Boolean'];
};

export type Branch = {
  __typename?: 'Branch';
  id: Scalars['String'];
  name?: Maybe<Scalars['String']>;
};

export type BranchResponse = {
  __typename?: 'BranchResponse';
  branchId: Scalars['String'];
  branchName: Scalars['String'];
};

export type BranchUsersListResponse = {
  __typename?: 'BranchUsersListResponse';
  assignedUsers?: Maybe<Array<Maybe<Scalars['String']>>>;
  branchId: Scalars['String'];
};

export type CloseTaskInput = {
  branchId: Scalars['String'];
  finalLocation?: InputMaybe<Scalars['String']>;
  invoiceId: Scalars['String'];
  orderId: Scalars['String'];
  skipInvalidLocationWarningFlag?: InputMaybe<Scalars['Boolean']>;
  skipStagedWarningFlag?: InputMaybe<Scalars['Boolean']>;
  tote: Scalars['String'];
  updateLocationOnly?: InputMaybe<Scalars['Boolean']>;
};

export type CloseTaskResponse = {
  __typename?: 'CloseTaskResponse';
  message: Scalars['String'];
  success: Scalars['Boolean'];
};

export type CompletePickInput = {
  branchId: Scalars['String'];
  cutDetail?: InputMaybe<Scalars['String']>;
  cutGroup?: InputMaybe<Scalars['String']>;
  description: Scalars['String'];
  generationId?: InputMaybe<Scalars['Int']>;
  ignoreLockToteCheck: Scalars['Boolean'];
  isLot?: InputMaybe<Scalars['String']>;
  isOverrideProduct: Scalars['Boolean'];
  isParallelCut?: InputMaybe<Scalars['Boolean']>;
  isSerial: Scalars['Boolean'];
  lineId: Scalars['Int'];
  location: Scalars['String'];
  locationType: Scalars['String'];
  lot: Scalars['String'];
  orderId?: InputMaybe<Scalars['String']>;
  pickGroup?: InputMaybe<Scalars['String']>;
  productId: Scalars['String'];
  quantity: Scalars['Int'];
  shipVia: Scalars['String'];
  splitId?: InputMaybe<Scalars['String']>;
  startPickTime: Scalars['String'];
  tote?: InputMaybe<Scalars['String']>;
  uom: Scalars['String'];
  userId: Scalars['String'];
  warehouseID: Scalars['String'];
};

export type Count = {
  __typename?: 'Count';
  branch: Branch;
  erpSystem: ErpSystem;
  id: Scalars['String'];
};

export type CountMutationResponse = {
  __typename?: 'CountMutationResponse';
  message?: Maybe<Scalars['String']>;
  success: Scalars['Boolean'];
};

export enum CountStatus {
  COMPLETE = 'COMPLETE',
  ERROR = 'ERROR',
  IN_PROGRESS = 'IN_PROGRESS',
  NOT_LOADED = 'NOT_LOADED'
}

export type CountWithStatus = {
  __typename?: 'CountWithStatus';
  branchId: Scalars['String'];
  branchName?: Maybe<Scalars['String']>;
  countId: Scalars['String'];
  createdAt?: Maybe<Scalars['String']>;
  errorMessage?: Maybe<Scalars['String']>;
  id?: Maybe<Scalars['ID']>;
  status?: Maybe<CountStatus>;
  totalProducts?: Maybe<Scalars['Int']>;
};

export type CountsInput = {
  endDate: Scalars['String'];
  startDate: Scalars['String'];
};

export type CustomerResponse = {
  __typename?: 'CustomerResponse';
  addressLine1?: Maybe<Scalars['String']>;
  addressLine2?: Maybe<Scalars['String']>;
  city?: Maybe<Scalars['String']>;
  countryCode?: Maybe<Scalars['String']>;
  homeBranch: Scalars['String'];
  id: Scalars['String'];
  isBillTo?: Maybe<Scalars['Boolean']>;
  isShipTo?: Maybe<Scalars['Boolean']>;
  name: Scalars['String'];
  postalCode?: Maybe<Scalars['String']>;
  state?: Maybe<Scalars['String']>;
};

export type CustomerSearchInput = {
  currentPage?: InputMaybe<Scalars['Int']>;
  id?: InputMaybe<Array<InputMaybe<Scalars['String']>>>;
  keyword?: InputMaybe<Scalars['String']>;
  pageSize?: InputMaybe<Scalars['Int']>;
};

export type CustomerSearchResponse = {
  __typename?: 'CustomerSearchResponse';
  metadata?: Maybe<EclipseSearchMetadata>;
  results?: Maybe<Array<Maybe<CustomerSearchResult>>>;
};

export type CustomerSearchResult = {
  __typename?: 'CustomerSearchResult';
  addressLine1?: Maybe<Scalars['String']>;
  addressLine2?: Maybe<Scalars['String']>;
  addressLine3?: Maybe<Scalars['String']>;
  addressLine4?: Maybe<Scalars['String']>;
  billToId?: Maybe<Scalars['String']>;
  city?: Maybe<Scalars['String']>;
  countryCode?: Maybe<Scalars['String']>;
  defaultPriceClass?: Maybe<Scalars['String']>;
  ediId?: Maybe<Scalars['String']>;
  id?: Maybe<Scalars['String']>;
  isBillTo?: Maybe<Scalars['Boolean']>;
  isBranchCash?: Maybe<Scalars['Boolean']>;
  isProspect?: Maybe<Scalars['Boolean']>;
  isShipTo?: Maybe<Scalars['Boolean']>;
  name?: Maybe<Scalars['String']>;
  nameIndex?: Maybe<Scalars['String']>;
  orderEntryMessage?: Maybe<Scalars['String']>;
  postalCode?: Maybe<Scalars['String']>;
  shipToLists: Array<ShipToId>;
  sortBy?: Maybe<Scalars['String']>;
  state?: Maybe<Scalars['String']>;
  updateKey?: Maybe<Scalars['String']>;
};

export type DeleteCountResponse = {
  __typename?: 'DeleteCountResponse';
  countLocationItemQuantities: Scalars['Int'];
  countLocationItems: Scalars['Int'];
  countLocations: Scalars['Int'];
  varianceCountLocationItemQuantities: Scalars['Int'];
  writeIns: Scalars['Int'];
};

export type DetailedVarianceResponse = {
  __typename?: 'DetailedVarianceResponse';
  counts?: Maybe<Array<Maybe<VarianceDetails>>>;
};

export type EclipseSearchMetadata = {
  __typename?: 'EclipseSearchMetadata';
  pageSize?: Maybe<Scalars['Int']>;
  startIndex?: Maybe<Scalars['Int']>;
  totalItems?: Maybe<Scalars['Int']>;
};

export enum ErpSystem {
  ECLIPSE = 'ECLIPSE',
  MINCRON = 'MINCRON'
}

export type ErrorResponse = {
  __typename?: 'ErrorResponse';
  code: Scalars['String'];
  details?: Maybe<Scalars['String']>;
  message: Scalars['String'];
};

export type FailedPriceSuggestion = {
  __typename?: 'FailedPriceSuggestion';
  branch: Scalars['String'];
  changeWriterDisplayName: Scalars['String'];
  changeWriterId: Scalars['String'];
  cmpPrice: Scalars['Float'];
  customerId: Scalars['String'];
  newPrice: Scalars['Float'];
  priceCategory: Scalars['String'];
  productId: Scalars['String'];
};

export type ImageUrls = {
  __typename?: 'ImageUrls';
  large?: Maybe<Scalars['String']>;
  medium?: Maybe<Scalars['String']>;
  small?: Maybe<Scalars['String']>;
  thumb?: Maybe<Scalars['String']>;
};

export type ItemInput = {
  locationId: Scalars['String'];
  productId: Scalars['String'];
  quantity?: InputMaybe<Scalars['Int']>;
};

export type KourierProduct = {
  __typename?: 'KourierProduct';
  displayField?: Maybe<Scalars['String']>;
  productId?: Maybe<Scalars['String']>;
  productImageUrl?: Maybe<Scalars['String']>;
  productNumber?: Maybe<Scalars['String']>;
  upc?: Maybe<Scalars['String']>;
};

export type LinkAccountResponse = {
  __typename?: 'LinkAccountResponse';
  message?: Maybe<Scalars['String']>;
  success: Scalars['Boolean'];
};

export type Location = {
  __typename?: 'Location';
  committed: Scalars['Boolean'];
  id: Scalars['String'];
  items: Array<LocationItem>;
  totalCounted: Scalars['Int'];
  totalProducts: Scalars['Int'];
};

export type LocationItem = {
  __typename?: 'LocationItem';
  catalogNum?: Maybe<Scalars['String']>;
  controlNum?: Maybe<Scalars['String']>;
  id?: Maybe<Scalars['ID']>;
  locationId?: Maybe<Scalars['String']>;
  prodDesc?: Maybe<Scalars['String']>;
  prodNum: Scalars['String'];
  productImageUrl?: Maybe<Scalars['String']>;
  quantity?: Maybe<Scalars['Int']>;
  sequence?: Maybe<Scalars['Int']>;
  status?: Maybe<LocationItemStatus>;
  tagNum?: Maybe<Scalars['String']>;
  uom?: Maybe<Scalars['String']>;
};

export enum LocationItemStatus {
  COMMITTED = 'COMMITTED',
  STAGED = 'STAGED',
  UNCOUNTED = 'UNCOUNTED'
}

export type LocationSummary = {
  __typename?: 'LocationSummary';
  committed: Scalars['Boolean'];
  id: Scalars['String'];
  totalCounted: Scalars['Int'];
  totalProducts: Scalars['Int'];
};

export type LocationsResponse = {
  __typename?: 'LocationsResponse';
  content: Array<LocationSummary>;
  totalCounted: Scalars['Int'];
  totalLocations: Scalars['Int'];
};

export type MetricsBranch = {
  __typename?: 'MetricsBranch';
  city?: Maybe<Scalars['String']>;
  id?: Maybe<Scalars['String']>;
  loginCount?: Maybe<Scalars['Int']>;
  state?: Maybe<Scalars['String']>;
  userCount?: Maybe<Scalars['Int']>;
};

export type MetricsChange = {
  __typename?: 'MetricsChange';
  metric: Scalars['String'];
  percentageChange?: Maybe<Scalars['String']>;
  quantity?: Maybe<Scalars['String']>;
};

export type MetricsCompletion = {
  __typename?: 'MetricsCompletion';
  branchId: Scalars['Int'];
  countId: Scalars['Int'];
  counted: Scalars['Int'];
  location: Scalars['String'];
  needToBeCounted: Scalars['Int'];
  status: Scalars['String'];
  timeEnded: Scalars['String'];
  timeStarted: Scalars['String'];
  total: Scalars['Int'];
};

export type MetricsCompletionInput = {
  branchId?: InputMaybe<Scalars['String']>;
  countId?: InputMaybe<Scalars['String']>;
  counted?: InputMaybe<Scalars['Int']>;
  location?: InputMaybe<Scalars['String']>;
  needToBeCounted?: InputMaybe<Scalars['Int']>;
  status?: InputMaybe<Scalars['String']>;
  timeEnded?: InputMaybe<Scalars['String']>;
  timeStarted?: InputMaybe<Scalars['String']>;
  total?: InputMaybe<Scalars['Int']>;
};

export type MetricsCompletionResponse = {
  __typename?: 'MetricsCompletionResponse';
  content: MetricsCompletion;
  message?: Maybe<Scalars['String']>;
  success: Scalars['Boolean'];
};

export type MetricsDivision = {
  __typename?: 'MetricsDivision';
  branchCount?: Maybe<Scalars['Int']>;
  branches?: Maybe<Array<Maybe<MetricsBranch>>>;
  division: Scalars['String'];
  loginCount?: Maybe<Scalars['Int']>;
  userCount?: Maybe<Scalars['Int']>;
};

export type MetricsDoubleRangeInput = {
  endDateWeekNew: Scalars['String'];
  endDateWeekOld: Scalars['String'];
  startDateWeekNew: Scalars['String'];
  startDateWeekOld: Scalars['String'];
};

export type MetricsLoginResponse = {
  __typename?: 'MetricsLoginResponse';
  message: Scalars['String'];
  success: Scalars['Boolean'];
};

export type MetricsOverview = {
  __typename?: 'MetricsOverview';
  response?: Maybe<Array<Maybe<MetricsChange>>>;
  type: Scalars['String'];
};

export type MetricsPercentageChange = {
  __typename?: 'MetricsPercentageChange';
  response?: Maybe<Array<Maybe<MetricsPercentageChangeDivision>>>;
  type: Scalars['String'];
};

export type MetricsPercentageChangeDivision = {
  __typename?: 'MetricsPercentageChangeDivision';
  branchChange?: Maybe<Scalars['String']>;
  division: Scalars['String'];
  loginChange?: Maybe<Scalars['String']>;
  userChange?: Maybe<Scalars['String']>;
};

export type MetricsPercentageTotal = {
  __typename?: 'MetricsPercentageTotal';
  response?: Maybe<Array<Maybe<MetricsPercentageTotalDivision>>>;
  type: Scalars['String'];
};

export type MetricsPercentageTotalDivision = {
  __typename?: 'MetricsPercentageTotalDivision';
  branchCount?: Maybe<Scalars['Int']>;
  branchPercentage?: Maybe<Scalars['Float']>;
  division: Scalars['String'];
  loginCount?: Maybe<Scalars['Int']>;
  loginPercentage?: Maybe<Scalars['Float']>;
  userCount?: Maybe<Scalars['Int']>;
  userPercentage?: Maybe<Scalars['Float']>;
};

export type MetricsSingleRangeInput = {
  endDate?: InputMaybe<Scalars['String']>;
  startDate?: InputMaybe<Scalars['String']>;
};

export type MetricsUsage = {
  __typename?: 'MetricsUsage';
  response?: Maybe<Array<Maybe<MetricsDivision>>>;
  type: Scalars['String'];
};

export type Mutation = {
  __typename?: 'Mutation';
  addCompletionMetric: MetricsCompletionResponse;
  addToCount: AddToCountMutationResponse;
  addUser: UserBranchResponse;
  assignPickTask: PickingOrder;
  closeOrder: CloseOrderResponse;
  closeTask: CloseTaskResponse;
  completeCount: CountMutationResponse;
  completeUserPick: UserPick;
  completeVarianceCount: CountMutationResponse;
  createWriteIn: WriteInMutationResponse;
  deleteCount: DeleteCountResponse;
  deleteUser: UserBranchResponse;
  loadCount: CountWithStatus;
  loadVarianceDetails: VarianceDetailsResponse;
  purgeMincronCounts: Scalars['String'];
  registerLogin: MetricsLoginResponse;
  removeCounts: Scalars['String'];
  removeUserBranch: UserBranchResponse;
  resolveWriteIn: WriteInMutationResponse;
  specialPrices?: Maybe<UploadSpecialPriceResponse>;
  splitQuantity?: Maybe<SplitQuantityResponse>;
  stagePickTask: StagePickTaskResponse;
  stagePickTotePackages: StagePickTotePackagesResponse;
  updateCount: CountMutationResponse;
  updateProductSerialNumbers: ProductSerialNumberResults;
  updateUserEmail: UserBranchResponse;
  updateVarianceCount: CountMutationResponse;
  updateWriteIn: WriteInMutationResponse;
  verifyEclipseCredentials: LinkAccountResponse;
};


export type MutationAddCompletionMetricArgs = {
  metric: MetricsCompletionInput;
};


export type MutationAddToCountArgs = {
  item: ItemInput;
};


export type MutationAddUserArgs = {
  branchId: Scalars['String'];
  userEmail: Scalars['String'];
};


export type MutationAssignPickTaskArgs = {
  input: PickingTaskInput;
};


export type MutationCloseOrderArgs = {
  input: CloseOrderInput;
};


export type MutationCloseTaskArgs = {
  input: CloseTaskInput;
};


export type MutationCompleteCountArgs = {
  locationId: Scalars['String'];
};


export type MutationCompleteUserPickArgs = {
  input: CompletePickInput;
};


export type MutationCompleteVarianceCountArgs = {
  locationId: Scalars['String'];
};


export type MutationCreateWriteInArgs = {
  writeIn: WriteInInput;
};


export type MutationDeleteCountArgs = {
  id: Scalars['ID'];
};


export type MutationDeleteUserArgs = {
  userEmail: Scalars['String'];
};


export type MutationLoadCountArgs = {
  branchId: Scalars['String'];
  countId: Scalars['String'];
};


export type MutationPurgeMincronCountsArgs = {
  input: CountsInput;
};


export type MutationRemoveCountsArgs = {
  input: RemoveCountsInput;
};


export type MutationRemoveUserBranchArgs = {
  branchId: Scalars['String'];
  userEmail: Scalars['String'];
};


export type MutationResolveWriteInArgs = {
  id: Scalars['ID'];
};


export type MutationSpecialPricesArgs = {
  input: PriceChangeInput;
};


export type MutationSplitQuantityArgs = {
  input: SplitQuantityInput;
};


export type MutationStagePickTaskArgs = {
  input: StagePickTaskInput;
};


export type MutationStagePickTotePackagesArgs = {
  input: StagePickTotePackagesInput;
};


export type MutationUpdateCountArgs = {
  item: ItemInput;
};


export type MutationUpdateProductSerialNumbersArgs = {
  input: UpdateProductSerialNumbersInput;
};


export type MutationUpdateUserEmailArgs = {
  newUserEmail: Scalars['String'];
  oldUserEmail: Scalars['String'];
};


export type MutationUpdateVarianceCountArgs = {
  item: ItemInput;
};


export type MutationUpdateWriteInArgs = {
  id: Scalars['ID'];
  writeIn: WriteInInput;
};


export type MutationVerifyEclipseCredentialsArgs = {
  password: Scalars['String'];
  username: Scalars['String'];
};

export type NextLocationResponse = {
  __typename?: 'NextLocationResponse';
  locationId: Scalars['String'];
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

export type PackageInput = {
  packageQuantity: Scalars['Int'];
  packageType: Scalars['String'];
};

export type Pageable = {
  __typename?: 'Pageable';
  offset: Scalars['Int'];
  pageNumber: Scalars['Int'];
  pageSize: Scalars['Int'];
  paged: Scalars['Boolean'];
  sort: PageableSort;
  unpaged: Scalars['Boolean'];
};

export type PageableSort = {
  __typename?: 'PageableSort';
  empty: Scalars['Boolean'];
  sorted: Scalars['Boolean'];
  unsorted: Scalars['Boolean'];
};

export type Pagination = {
  __typename?: 'Pagination';
  currentPage: Scalars['Int'];
  pageSize: Scalars['Int'];
  totalItemCount: Scalars['Int'];
};

export type PagingContext = {
  orderBy: Scalars['String'];
  orderDirection: Scalars['String'];
  page: Scalars['Int'];
  pageSize: Scalars['Int'];
};

export type PickingOrder = {
  __typename?: 'PickingOrder';
  assignedUserId?: Maybe<Scalars['String']>;
  billTo?: Maybe<Scalars['Int']>;
  branchId?: Maybe<Scalars['String']>;
  generationId?: Maybe<Scalars['Int']>;
  invoiceId?: Maybe<Scalars['String']>;
  isFromMultipleZones?: Maybe<Scalars['Boolean']>;
  orderId?: Maybe<Scalars['String']>;
  pickCount?: Maybe<Scalars['String']>;
  pickGroup?: Maybe<Scalars['String']>;
  shipTo?: Maybe<Scalars['Int']>;
  shipToName?: Maybe<Scalars['String']>;
  shipVia?: Maybe<Scalars['String']>;
  taskState?: Maybe<Scalars['String']>;
  taskWeight?: Maybe<Scalars['Float']>;
};

export type PickingOrderInput = {
  branchId: Scalars['String'];
  orderId?: InputMaybe<Scalars['String']>;
  userId: Scalars['String'];
};

export type PickingTaskInput = {
  assignedUserId?: InputMaybe<Scalars['String']>;
  billTo?: InputMaybe<Scalars['Int']>;
  branchId?: InputMaybe<Scalars['String']>;
  generationId?: InputMaybe<Scalars['Int']>;
  invoiceId?: InputMaybe<Scalars['String']>;
  isFromMultipleZones?: InputMaybe<Scalars['Boolean']>;
  orderId?: InputMaybe<Scalars['String']>;
  pickCount?: InputMaybe<Scalars['String']>;
  pickGroup?: InputMaybe<Scalars['String']>;
  shipTo?: InputMaybe<Scalars['Int']>;
  shipToName?: InputMaybe<Scalars['String']>;
  shipVia?: InputMaybe<Scalars['String']>;
  taskState?: InputMaybe<Scalars['String']>;
  taskWeight?: InputMaybe<Scalars['Float']>;
};

export type Price = {
  __typename?: 'Price';
  currency: Scalars['String'];
  displayName: Scalars['String'];
  type: Scalars['String'];
  value: Scalars['Float'];
};

export type PriceChangeInput = {
  priceChangeSuggestions: Array<PriceSuggestion>;
  priceCreateSuggestions: Array<PriceSuggestion>;
};

export type PriceSuggestion = {
  branch: Scalars['String'];
  changeWriterDisplayName: Scalars['String'];
  changeWriterId: Scalars['String'];
  cmpPrice: Scalars['Float'];
  customerId: Scalars['String'];
  newPrice: Scalars['Float'];
  priceCategory: Scalars['String'];
  productId: Scalars['String'];
  territory?: InputMaybe<Scalars['String']>;
};

export type Product = {
  __typename?: 'Product';
  environmentalOptions?: Maybe<Array<Maybe<Scalars['String']>>>;
  featuresAndBenefits?: Maybe<Scalars['String']>;
  id?: Maybe<Scalars['ID']>;
  imageUrls?: Maybe<ImageUrls>;
  manufacturerName?: Maybe<Scalars['String']>;
  manufacturerNumber?: Maybe<Scalars['String']>;
  name?: Maybe<Scalars['String']>;
  packageDimensions?: Maybe<PackageDimensions>;
  price?: Maybe<Scalars['Float']>;
  productImageUrl?: Maybe<Scalars['String']>;
  productNumber?: Maybe<Scalars['String']>;
  productOverview?: Maybe<Scalars['String']>;
  productType?: Maybe<Scalars['String']>;
  seriesModelFigureNumber?: Maybe<Scalars['String']>;
  stock?: Maybe<Stock>;
  taxonomy?: Maybe<Array<Maybe<Scalars['String']>>>;
  techSpecifications?: Maybe<Array<Maybe<TechSpec>>>;
  technicalDocuments?: Maybe<Array<Maybe<TechDoc>>>;
  unspsc?: Maybe<Scalars['String']>;
  upc?: Maybe<Scalars['String']>;
};

export type ProductAttribute = {
  attributeType?: InputMaybe<Scalars['String']>;
  attributeValue?: InputMaybe<Scalars['String']>;
};

export type ProductDetails = {
  __typename?: 'ProductDetails';
  catalogNumber: Scalars['String'];
  description: Scalars['String'];
  upc: Scalars['String'];
};

export type ProductPriceInput = {
  branch?: InputMaybe<Scalars['String']>;
  correlationId?: InputMaybe<Scalars['String']>;
  customerId?: InputMaybe<Scalars['String']>;
  effectiveDate?: InputMaybe<Scalars['String']>;
  productId: Scalars['String'];
  userId?: InputMaybe<Scalars['String']>;
};

export type ProductPriceResponse = {
  __typename?: 'ProductPriceResponse';
  cmp?: Maybe<Scalars['Float']>;
  correlationId?: Maybe<Scalars['String']>;
  customerId?: Maybe<Scalars['String']>;
  erpBranchNum?: Maybe<Scalars['String']>;
  matchedBranch?: Maybe<Scalars['String']>;
  matchedClass?: Maybe<Scalars['String']>;
  matchedGroup?: Maybe<Scalars['String']>;
  matrixId?: Maybe<Scalars['String']>;
  productId?: Maybe<Scalars['String']>;
  rateCardName?: Maybe<Scalars['String']>;
  rateCardPrice?: Maybe<Scalars['Float']>;
  stdCost?: Maybe<Scalars['Float']>;
  uom?: Maybe<Scalars['String']>;
};

export type ProductSearchEclipseInput = {
  currentPage?: InputMaybe<Scalars['Int']>;
  pageSize?: InputMaybe<Scalars['Int']>;
  searchInputType: Scalars['Int'];
  searchTerm?: InputMaybe<Scalars['String']>;
  selectedAttributes?: InputMaybe<Array<InputMaybe<ProductAttribute>>>;
};

export type ProductSearchKourierInput = {
  displayName?: InputMaybe<Scalars['String']>;
  keywords: Scalars['String'];
  searchInputType?: InputMaybe<Scalars['String']>;
};

export type ProductSearchKourierResponse = {
  __typename?: 'ProductSearchKourierResponse';
  prodSearch: Array<ProductSearchKourierResult>;
};

export type ProductSearchKourierResult = {
  __typename?: 'ProductSearchKourierResult';
  errorCode?: Maybe<Scalars['String']>;
  errorMessage?: Maybe<Scalars['String']>;
  productIdCount?: Maybe<Scalars['Int']>;
  products?: Maybe<Array<Maybe<KourierProduct>>>;
};

export type ProductSearchResult = {
  __typename?: 'ProductSearchResult';
  pagination: Pagination;
  products: Array<Maybe<Product>>;
};

export type ProductSerialNumberResults = {
  __typename?: 'ProductSerialNumberResults';
  results: Array<Maybe<ProductSerialNumbers>>;
};

export type ProductSerialNumbers = {
  __typename?: 'ProductSerialNumbers';
  description?: Maybe<Scalars['String']>;
  generationId: Scalars['String'];
  invoiceId: Scalars['String'];
  location?: Maybe<Scalars['String']>;
  nonStockSerialNumbers: Array<Maybe<SerialList>>;
  orderId: Scalars['String'];
  productId: Scalars['String'];
  quantity: Scalars['Int'];
  serialList: Array<Maybe<SerialList>>;
  warehouseId: Scalars['String'];
};

export type Query = {
  __typename?: 'Query';
  branchUsersList: Array<Maybe<BranchUsersListResponse>>;
  count: Count;
  countStatus: CountWithStatus;
  counts: Array<CountWithStatus>;
  customer: CustomerResponse;
  getCustomerSearch?: Maybe<CustomerSearchResponse>;
  getProductSerialNumbers: ProductSerialNumberResults;
  location: Location;
  locations: LocationsResponse;
  nextLocation: NextLocationResponse;
  paginatedSpecialPrices: SpecialPriceResponse;
  paginatedTypeaheadSuggestions: TypeaheadResponse;
  percentageChange: MetricsPercentageChange;
  percentageTotal: MetricsPercentageTotal;
  pickingOrders: Array<Maybe<PickingOrder>>;
  priceLines: Array<Scalars['String']>;
  productDetails: ProductDetails;
  productImageUrl?: Maybe<Scalars['String']>;
  productPrices: ProductPriceResponse;
  searchProductsEclipse: ProductSearchResult;
  searchProductsKourier: ProductSearchKourierResponse;
  shippingDetails: ShippingDetailsKourierResponse;
  specialPrices: SpecialPriceResponse;
  totalOverview: MetricsOverview;
  totalUsage: MetricsUsage;
  typeaheadSuggestions: TypeaheadResponse;
  userBranch: UserResponse;
  userPicks: Array<Maybe<UserPick>>;
  validateBranch: ValidateBranchResponse;
  varianceDetails: DetailedVarianceResponse;
  varianceLocation: VarianceLocationResponse;
  varianceLocations: VarianceLocationsResponse;
  varianceNextLocation: VarianceNextLocationResponse;
  varianceSummary: VarianceSummaryResponse;
  writeIn: WriteIn;
  writeIns: WriteInsResponse;
};


export type QueryCountArgs = {
  branchId: Scalars['String'];
  id: Scalars['String'];
};


export type QueryCountStatusArgs = {
  id: Scalars['ID'];
};


export type QueryCountsArgs = {
  input: CountsInput;
};


export type QueryCustomerArgs = {
  customerId: Scalars['String'];
};


export type QueryGetCustomerSearchArgs = {
  input: CustomerSearchInput;
};


export type QueryGetProductSerialNumbersArgs = {
  warehouseId: Scalars['String'];
};


export type QueryLocationArgs = {
  id: Scalars['String'];
};


export type QueryNextLocationArgs = {
  id: Scalars['String'];
};


export type QueryPaginatedSpecialPricesArgs = {
  input: SpecialPriceInput;
  pagingContext: PagingContext;
};


export type QueryPaginatedTypeaheadSuggestionsArgs = {
  input: TypeaheadInput;
  pagingContext: PagingContext;
};


export type QueryPercentageChangeArgs = {
  input: MetricsDoubleRangeInput;
};


export type QueryPercentageTotalArgs = {
  input: MetricsSingleRangeInput;
};


export type QueryPickingOrdersArgs = {
  input: PickingOrderInput;
};


export type QueryPriceLinesArgs = {
  input: SpecialPriceInput;
};


export type QueryProductDetailsArgs = {
  productId: Scalars['String'];
};


export type QueryProductImageUrlArgs = {
  input: Scalars['String'];
};


export type QueryProductPricesArgs = {
  input: ProductPriceInput;
};


export type QuerySearchProductsEclipseArgs = {
  input: ProductSearchEclipseInput;
};


export type QuerySearchProductsKourierArgs = {
  input: ProductSearchKourierInput;
};


export type QueryShippingDetailsArgs = {
  input: ShippingDetailsKourierInput;
};


export type QuerySpecialPricesArgs = {
  input: SpecialPriceInput;
};


export type QueryTotalOverviewArgs = {
  input: MetricsDoubleRangeInput;
};


export type QueryTotalUsageArgs = {
  input: MetricsSingleRangeInput;
};


export type QueryTypeaheadSuggestionsArgs = {
  input: TypeaheadInput;
};


export type QueryUserBranchArgs = {
  username: Scalars['String'];
};


export type QueryUserPicksArgs = {
  input: PickingOrderInput;
};


export type QueryValidateBranchArgs = {
  input: ValidateBranchInput;
};


export type QueryVarianceLocationArgs = {
  id: Scalars['String'];
};


export type QueryVarianceNextLocationArgs = {
  id: Scalars['String'];
};


export type QueryWriteInArgs = {
  id: Scalars['ID'];
};


export type QueryWriteInsArgs = {
  options: WriteInsInput;
};

export type RemoveCountsInput = {
  endDate: Scalars['String'];
  erpSystemName?: InputMaybe<ErpSystem>;
};

export type SerialLineInput = {
  line: Scalars['Int'];
  serial: Scalars['String'];
};

export type SerialList = {
  __typename?: 'SerialList';
  line: Scalars['Int'];
  serial: Scalars['String'];
};

export type ShipToId = {
  __typename?: 'ShipToId';
  shipToId?: Maybe<Scalars['String']>;
};

export type ShippingDetailsKourierInput = {
  invoiceNumber: Scalars['String'];
};

export type ShippingDetailsKourierResponse = {
  __typename?: 'ShippingDetailsKourierResponse';
  shippingtext: Array<ShippingDetailsKourierResult>;
};

export type ShippingDetailsKourierResult = {
  __typename?: 'ShippingDetailsKourierResult';
  errorCode?: Maybe<Scalars['String']>;
  errorMessage?: Maybe<Scalars['String']>;
  invoiceNumber?: Maybe<Scalars['String']>;
  noBackorder: Scalars['Boolean'];
  noSort: Scalars['Boolean'];
  shippingInstructions?: Maybe<Array<Scalars['String']>>;
  status?: Maybe<Scalars['String']>;
};

export type SpecialPrice = {
  __typename?: 'SpecialPrice';
  branch: Scalars['String'];
  customerDisplayName: Scalars['String'];
  customerId: Scalars['String'];
  customerSalesQuantity: Scalars['Int'];
  displayName: Scalars['String'];
  imageUrl?: Maybe<Scalars['String']>;
  manufacturer: Scalars['String'];
  manufacturerReferenceNumber: Scalars['String'];
  priceLine: Scalars['String'];
  prices: Array<Price>;
  productId: Scalars['String'];
  salesperson: Scalars['String'];
  territory?: Maybe<Scalars['String']>;
};

export type SpecialPriceInput = {
  customerId?: InputMaybe<Scalars['String']>;
  priceLine?: InputMaybe<Scalars['String']>;
  productId?: InputMaybe<Scalars['String']>;
};

export type SpecialPriceMeta = {
  __typename?: 'SpecialPriceMeta';
  customerId?: Maybe<Scalars['String']>;
  nextPage?: Maybe<Scalars['Int']>;
  orderBy: Scalars['String'];
  orderDirection: Scalars['String'];
  page: Scalars['Int'];
  pageCount: Scalars['Int'];
  pageSize: Scalars['Int'];
  priceLine?: Maybe<Scalars['String']>;
  productId?: Maybe<Scalars['String']>;
  resultCount: Scalars['Int'];
};

export type SpecialPriceResponse = {
  __typename?: 'SpecialPriceResponse';
  meta: SpecialPriceMeta;
  results: Array<SpecialPrice>;
};

export type SplitQuantityInput = {
  pickedItemsCount: Scalars['Int'];
  product: CompletePickInput;
  serialNumbers?: InputMaybe<Array<InputMaybe<SerialLineInput>>>;
};

export type SplitQuantityResponse = {
  __typename?: 'SplitQuantityResponse';
  errorMessage?: Maybe<Scalars['String']>;
  invalidSerialNums?: Maybe<Array<Maybe<SerialList>>>;
  isSplit: Scalars['Boolean'];
  orderId: Scalars['String'];
  productId: Scalars['String'];
  successStatus: Scalars['Boolean'];
};

export type StagePickTaskInput = {
  branchId: Scalars['String'];
  invoiceId: Scalars['String'];
  location?: InputMaybe<Scalars['String']>;
  orderId: Scalars['String'];
  tote: Scalars['String'];
};

export type StagePickTaskResponse = {
  __typename?: 'StagePickTaskResponse';
  message: Scalars['String'];
  success: Scalars['Boolean'];
};

export type StagePickTotePackagesInput = {
  branchId: Scalars['String'];
  invoiceId: Scalars['String'];
  orderId: Scalars['String'];
  packageList: Array<InputMaybe<PackageInput>>;
  tote: Scalars['String'];
};

export type StagePickTotePackagesResponse = {
  __typename?: 'StagePickTotePackagesResponse';
  message: Scalars['String'];
  success: Scalars['Boolean'];
};

export type Stock = {
  __typename?: 'Stock';
  homeBranch?: Maybe<StoreStock>;
  otherBranches?: Maybe<Array<Maybe<StoreStock>>>;
};

export type StoreStock = {
  __typename?: 'StoreStock';
  address?: Maybe<Scalars['String']>;
  availability?: Maybe<Scalars['Int']>;
  branchName?: Maybe<Scalars['String']>;
};

export type SuccessfulCreate = {
  __typename?: 'SuccessfulCreate';
  uploadedName: Scalars['String'];
  uploadedPath: Scalars['String'];
};

export type SuccessfulUpload = {
  __typename?: 'SuccessfulUpload';
  uploadedName: Scalars['String'];
  uploadedPath: Scalars['String'];
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

export type TypeaheadInput = {
  entity: Scalars['String'];
  query: Scalars['String'];
};

export type TypeaheadMeta = {
  __typename?: 'TypeaheadMeta';
  entity: Scalars['String'];
  nextPage?: Maybe<Scalars['Int']>;
  orderBy: Scalars['String'];
  orderDirection: Scalars['String'];
  page: Scalars['Int'];
  pageCount: Scalars['Int'];
  pageSize: Scalars['Int'];
  query: Scalars['String'];
  resultCount: Scalars['Int'];
};

export type TypeaheadResponse = {
  __typename?: 'TypeaheadResponse';
  meta: TypeaheadMeta;
  results: Array<TypeaheadResult>;
};

export type TypeaheadResult = {
  __typename?: 'TypeaheadResult';
  displayName: Scalars['String'];
  id: Scalars['String'];
};

export type UpdateProductSerialNumbersInput = {
  branchId: Scalars['String'];
  ignoreStockCheck: Scalars['Boolean'];
  serialNumberList: Array<InputMaybe<SerialLineInput>>;
  warehouseId: Scalars['String'];
};

export type UploadSpecialPriceResponse = {
  __typename?: 'UploadSpecialPriceResponse';
  failedCreateSuggestions?: Maybe<Array<FailedPriceSuggestion>>;
  failedUpdateSuggestions?: Maybe<Array<FailedPriceSuggestion>>;
  successfulCreates?: Maybe<Array<SuccessfulCreate>>;
  successfulUploads?: Maybe<Array<SuccessfulUpload>>;
};

export type UserBranchResponse = {
  __typename?: 'UserBranchResponse';
  error?: Maybe<ErrorResponse>;
  success: Scalars['Boolean'];
};

export type UserPick = {
  __typename?: 'UserPick';
  branchId: Scalars['String'];
  cutDetail?: Maybe<Scalars['String']>;
  cutGroup?: Maybe<Scalars['String']>;
  description: Scalars['String'];
  generationId?: Maybe<Scalars['Int']>;
  isLot?: Maybe<Scalars['String']>;
  isParallelCut?: Maybe<Scalars['Boolean']>;
  isSerial: Scalars['Boolean'];
  lineId: Scalars['Int'];
  location: Scalars['String'];
  locationType: Scalars['String'];
  lot: Scalars['String'];
  orderId?: Maybe<Scalars['String']>;
  pickGroup?: Maybe<Scalars['String']>;
  productId: Scalars['String'];
  productImageUrl?: Maybe<Scalars['String']>;
  quantity: Scalars['Int'];
  shipVia: Scalars['String'];
  splitId?: Maybe<Scalars['String']>;
  tote?: Maybe<Scalars['String']>;
  uom: Scalars['String'];
  userId: Scalars['String'];
  warehouseID: Scalars['String'];
};

export type UserResponse = {
  __typename?: 'UserResponse';
  branch?: Maybe<Array<Maybe<Scalars['String']>>>;
  username: Scalars['String'];
};

export type ValidateBranchInput = {
  branchId: Scalars['String'];
};

export type ValidateBranchResponse = {
  __typename?: 'ValidateBranchResponse';
  branch?: Maybe<BranchResponse>;
  isValid: Scalars['Boolean'];
};

export type VarianceDetails = {
  __typename?: 'VarianceDetails';
  countQty?: Maybe<Scalars['Int']>;
  countedCost?: Maybe<Scalars['Float']>;
  erpProductID: Scalars['String'];
  location: Scalars['String'];
  notCountedFlag?: Maybe<Scalars['Boolean']>;
  onHandCost?: Maybe<Scalars['Float']>;
  onHandQty?: Maybe<Scalars['Int']>;
  percentDeviance?: Maybe<Scalars['Float']>;
  productDescription: Scalars['String'];
  qtyDeviance?: Maybe<Scalars['Int']>;
  recount1Qty?: Maybe<Scalars['Int']>;
  recount2Qty?: Maybe<Scalars['Int']>;
  recount3Qty?: Maybe<Scalars['Int']>;
};

export type VarianceDetailsResponse = {
  __typename?: 'VarianceDetailsResponse';
  message: Scalars['String'];
  success: Scalars['Boolean'];
};

export enum VarianceItemStatus {
  COMMITTED = 'COMMITTED',
  NONVARIANCE = 'NONVARIANCE',
  STAGED = 'STAGED',
  UNCOUNTED = 'UNCOUNTED'
}

export type VarianceLocationItem = {
  __typename?: 'VarianceLocationItem';
  catalogNum?: Maybe<Scalars['String']>;
  controlNum?: Maybe<Scalars['String']>;
  id?: Maybe<Scalars['ID']>;
  locationId?: Maybe<Scalars['String']>;
  prodDesc?: Maybe<Scalars['String']>;
  prodNum: Scalars['String'];
  productImageUrl?: Maybe<Scalars['String']>;
  quantity?: Maybe<Scalars['Int']>;
  sequence?: Maybe<Scalars['Int']>;
  status?: Maybe<LocationItemStatus>;
  tagNum?: Maybe<Scalars['String']>;
  uom?: Maybe<Scalars['String']>;
  varianceCost?: Maybe<Scalars['Float']>;
  varianceStatus?: Maybe<VarianceItemStatus>;
};

export type VarianceLocationResponse = {
  __typename?: 'VarianceLocationResponse';
  committed: Scalars['Boolean'];
  grossVarianceCost: Scalars['Float'];
  id: Scalars['String'];
  items: Array<VarianceLocationItem>;
  netVarianceCost: Scalars['Float'];
  totalCounted: Scalars['Int'];
  totalProducts: Scalars['Int'];
};

export type VarianceLocationSummary = {
  __typename?: 'VarianceLocationSummary';
  grossVarianceCost: Scalars['Float'];
  id: Scalars['String'];
  netVarianceCost: Scalars['Float'];
  totalProducts: Scalars['Int'];
};

export type VarianceLocationsResponse = {
  __typename?: 'VarianceLocationsResponse';
  content: Array<VarianceLocationSummary>;
  totalLocations: Scalars['Int'];
};

export type VarianceNextLocationResponse = {
  __typename?: 'VarianceNextLocationResponse';
  locationId: Scalars['String'];
};

export type VarianceSummaryResponse = {
  __typename?: 'VarianceSummaryResponse';
  differencePercentage?: Maybe<Scalars['Float']>;
  differenceQuantity?: Maybe<Scalars['Int']>;
  grossTotalCost?: Maybe<Scalars['String']>;
  locationQuantity?: Maybe<Scalars['Int']>;
  netTotalCost?: Maybe<Scalars['String']>;
  productQuantity?: Maybe<Scalars['Int']>;
};

export type WriteIn = {
  __typename?: 'WriteIn';
  catalogNum?: Maybe<Scalars['String']>;
  comment?: Maybe<Scalars['String']>;
  createdAt: Scalars['String'];
  createdBy: Scalars['String'];
  description: Scalars['String'];
  id: Scalars['ID'];
  locationId: Scalars['String'];
  quantity: Scalars['Int'];
  resolved: Scalars['Boolean'];
  uom: Scalars['String'];
  upcNum?: Maybe<Scalars['String']>;
  updatedAt: Scalars['String'];
  updatedBy: Scalars['String'];
};

export type WriteInInput = {
  catalogNum?: InputMaybe<Scalars['String']>;
  comment?: InputMaybe<Scalars['String']>;
  description: Scalars['String'];
  locationId: Scalars['String'];
  quantity: Scalars['Int'];
  uom: Scalars['String'];
  upcNum?: InputMaybe<Scalars['String']>;
};

export type WriteInMutationResponse = {
  __typename?: 'WriteInMutationResponse';
  content: WriteIn;
  message?: Maybe<Scalars['String']>;
  success: Scalars['Boolean'];
};

export type WriteInsInput = {
  page: Scalars['Int'];
  size: Scalars['Int'];
  sort?: InputMaybe<WriteInsSort>;
};

export type WriteInsResponse = {
  __typename?: 'WriteInsResponse';
  content: Array<WriteIn>;
  empty: Scalars['Boolean'];
  first: Scalars['Boolean'];
  last: Scalars['Boolean'];
  number: Scalars['Int'];
  numberOfElements: Scalars['Int'];
  pageable: Pageable;
  size: Scalars['Int'];
  sort: PageableSort;
  totalElements: Scalars['Int'];
  totalPages: Scalars['Int'];
};

export type WriteInsSort = {
  direction: Scalars['String'];
  property: Scalars['String'];
};

export type CloseOrderInput = {
  orderId: Scalars['String'];
  pickerId: Scalars['String'];
};

export type CloseOrderResponse = {
  __typename?: 'closeOrderResponse';
  errorCode?: Maybe<Scalars['String']>;
  errorMessage?: Maybe<Scalars['String']>;
  moreToPick: Scalars['Boolean'];
  orderId: Scalars['String'];
  orderLocked: Scalars['Boolean'];
  pickerId: Scalars['String'];
  status: Scalars['Boolean'];
  stillPicking: Scalars['Boolean'];
};

export type AddToCountMutationVariables = Exact<{
  item: ItemInput;
}>;


export type AddToCountMutation = { __typename?: 'Mutation', addToCount: { __typename?: 'AddToCountMutationResponse', success: boolean, message?: string | null | undefined, item: { __typename?: 'LocationItem', id?: string | null | undefined, prodDesc?: string | null | undefined, prodNum: string, catalogNum?: string | null | undefined, tagNum?: string | null | undefined, uom?: string | null | undefined, quantity?: number | null | undefined, sequence?: number | null | undefined, productImageUrl?: string | null | undefined } } };

export type CompleteCountMutationVariables = Exact<{
  locationId: Scalars['String'];
}>;


export type CompleteCountMutation = { __typename?: 'Mutation', completeCount: { __typename?: 'CountMutationResponse', success: boolean, message?: string | null | undefined } };

export type CompleteVarianceCountMutationVariables = Exact<{
  locationId: Scalars['String'];
}>;


export type CompleteVarianceCountMutation = { __typename?: 'Mutation', completeVarianceCount: { __typename?: 'CountMutationResponse', success: boolean, message?: string | null | undefined } };

export type GetCountQueryVariables = Exact<{
  id: Scalars['String'];
  branchId: Scalars['String'];
}>;


export type GetCountQuery = { __typename?: 'Query', count: { __typename?: 'Count', id: string, erpSystem: ErpSystem, branch: { __typename?: 'Branch', id: string, name?: string | null | undefined } } };

export type CreateWriteInMutationVariables = Exact<{
  writeIn: WriteInInput;
}>;


export type CreateWriteInMutation = { __typename?: 'Mutation', createWriteIn: { __typename?: 'WriteInMutationResponse', success: boolean, message?: string | null | undefined } };

export type LoadVarianceDetailsMutationVariables = Exact<{ [key: string]: never; }>;


export type LoadVarianceDetailsMutation = { __typename?: 'Mutation', loadVarianceDetails: { __typename?: 'VarianceDetailsResponse', success: boolean, message: string } };

export type GetLocationQueryVariables = Exact<{
  id: Scalars['String'];
}>;


export type GetLocationQuery = { __typename?: 'Query', location: { __typename?: 'Location', id: string, totalProducts: number, totalCounted: number, committed: boolean, items: Array<{ __typename?: 'LocationItem', id?: string | null | undefined, prodDesc?: string | null | undefined, prodNum: string, catalogNum?: string | null | undefined, tagNum?: string | null | undefined, uom?: string | null | undefined, quantity?: number | null | undefined, status?: LocationItemStatus | null | undefined, sequence?: number | null | undefined, productImageUrl?: string | null | undefined, controlNum?: string | null | undefined }> } };

export type GetLocationsQueryVariables = Exact<{ [key: string]: never; }>;


export type GetLocationsQuery = { __typename?: 'Query', locations: { __typename?: 'LocationsResponse', totalLocations: number, totalCounted: number, content: Array<{ __typename?: 'LocationSummary', id: string, committed: boolean, totalProducts: number, totalCounted: number }> } };

export type GetNextLocationQueryVariables = Exact<{
  id: Scalars['String'];
}>;


export type GetNextLocationQuery = { __typename?: 'Query', nextLocation: { __typename?: 'NextLocationResponse', locationId: string } };

export type ProductDetailsQueryVariables = Exact<{
  productId: Scalars['String'];
}>;


export type ProductDetailsQuery = { __typename?: 'Query', productDetails: { __typename?: 'ProductDetails', description: string, catalogNumber: string, upc: string } };

export type RegisterLoginMutationVariables = Exact<{ [key: string]: never; }>;


export type RegisterLoginMutation = { __typename?: 'Mutation', registerLogin: { __typename?: 'MetricsLoginResponse', success: boolean, message: string } };

export type ResolveWriteInMutationVariables = Exact<{
  id: Scalars['ID'];
}>;


export type ResolveWriteInMutation = { __typename?: 'Mutation', resolveWriteIn: { __typename?: 'WriteInMutationResponse', success: boolean, message?: string | null | undefined } };

export type SearchProductsEclipseQueryVariables = Exact<{
  input: ProductSearchEclipseInput;
}>;


export type SearchProductsEclipseQuery = { __typename?: 'Query', searchProductsEclipse: { __typename?: 'ProductSearchResult', pagination: { __typename?: 'Pagination', currentPage: number, totalItemCount: number }, products: Array<{ __typename?: 'Product', manufacturerName?: string | null | undefined, id?: string | null | undefined, name?: string | null | undefined, upc?: string | null | undefined, productNumber?: string | null | undefined, imageUrls?: { __typename?: 'ImageUrls', thumb?: string | null | undefined } | null | undefined } | null | undefined> } };

export type SearchProductsKourierQueryVariables = Exact<{
  input: ProductSearchKourierInput;
}>;


export type SearchProductsKourierQuery = { __typename?: 'Query', searchProductsKourier: { __typename?: 'ProductSearchKourierResponse', prodSearch: Array<{ __typename?: 'ProductSearchKourierResult', errorCode?: string | null | undefined, errorMessage?: string | null | undefined, productIdCount?: number | null | undefined, products?: Array<{ __typename?: 'KourierProduct', productId?: string | null | undefined, displayField?: string | null | undefined, productNumber?: string | null | undefined, upc?: string | null | undefined, productImageUrl?: string | null | undefined } | null | undefined> | null | undefined }> } };

export type UpdateCountMutationVariables = Exact<{
  item: ItemInput;
}>;


export type UpdateCountMutation = { __typename?: 'Mutation', updateCount: { __typename?: 'CountMutationResponse', success: boolean, message?: string | null | undefined } };

export type UpdateVarianceCountMutationVariables = Exact<{
  item: ItemInput;
}>;


export type UpdateVarianceCountMutation = { __typename?: 'Mutation', updateVarianceCount: { __typename?: 'CountMutationResponse', success: boolean, message?: string | null | undefined } };

export type UpdateWriteInMutationVariables = Exact<{
  id: Scalars['ID'];
  writeIn: WriteInInput;
}>;


export type UpdateWriteInMutation = { __typename?: 'Mutation', updateWriteIn: { __typename?: 'WriteInMutationResponse', success: boolean, message?: string | null | undefined } };

export type GetVarianceLocationQueryVariables = Exact<{
  id: Scalars['String'];
}>;


export type GetVarianceLocationQuery = { __typename?: 'Query', varianceLocation: { __typename?: 'VarianceLocationResponse', totalCounted: number, totalProducts: number, committed: boolean, id: string, items: Array<{ __typename?: 'VarianceLocationItem', id?: string | null | undefined, locationId?: string | null | undefined, prodDesc?: string | null | undefined, prodNum: string, tagNum?: string | null | undefined, catalogNum?: string | null | undefined, uom?: string | null | undefined, quantity?: number | null | undefined, status?: LocationItemStatus | null | undefined, sequence?: number | null | undefined, varianceCost?: number | null | undefined, varianceStatus?: VarianceItemStatus | null | undefined, productImageUrl?: string | null | undefined }> } };

export type GetVarianceLocationsQueryVariables = Exact<{ [key: string]: never; }>;


export type GetVarianceLocationsQuery = { __typename?: 'Query', varianceLocations: { __typename?: 'VarianceLocationsResponse', totalLocations: number, content: Array<{ __typename?: 'VarianceLocationSummary', id: string, totalProducts: number, netVarianceCost: number, grossVarianceCost: number }> } };

export type GetVarianceNextLocationQueryVariables = Exact<{
  id: Scalars['String'];
}>;


export type GetVarianceNextLocationQuery = { __typename?: 'Query', varianceNextLocation: { __typename?: 'VarianceNextLocationResponse', locationId: string } };

export type GetVarianceSummaryQueryVariables = Exact<{ [key: string]: never; }>;


export type GetVarianceSummaryQuery = { __typename?: 'Query', varianceSummary: { __typename?: 'VarianceSummaryResponse', netTotalCost?: string | null | undefined, grossTotalCost?: string | null | undefined, productQuantity?: number | null | undefined, locationQuantity?: number | null | undefined, differenceQuantity?: number | null | undefined } };

export type GetWriteInQueryVariables = Exact<{
  id: Scalars['ID'];
}>;


export type GetWriteInQuery = { __typename?: 'Query', writeIn: { __typename?: 'WriteIn', id: string, locationId: string, catalogNum?: string | null | undefined, upcNum?: string | null | undefined, description: string, uom: string, quantity: number, comment?: string | null | undefined, createdBy: string, createdAt: string, updatedBy: string, updatedAt: string, resolved: boolean } };

export type GetWriteInsQueryVariables = Exact<{
  options: WriteInsInput;
}>;


export type GetWriteInsQuery = { __typename?: 'Query', writeIns: { __typename?: 'WriteInsResponse', content: Array<{ __typename?: 'WriteIn', id: string, locationId: string, catalogNum?: string | null | undefined, upcNum?: string | null | undefined, description: string, uom: string, quantity: number, comment?: string | null | undefined, createdBy: string, createdAt: string, updatedBy: string, updatedAt: string, resolved: boolean }>, pageable: { __typename?: 'Pageable', pageNumber: number, pageSize: number, offset: number, paged: boolean, unpaged: boolean, sort: { __typename?: 'PageableSort', sorted: boolean, unsorted: boolean, empty: boolean } } } };


export const AddToCountDocument = gql`
    mutation AddToCount($item: ItemInput!) {
  addToCount(item: $item) {
    success
    message
    item {
      id
      prodDesc
      prodNum
      catalogNum
      tagNum
      uom
      quantity
      sequence
      productImageUrl
    }
  }
}
    `;
export type AddToCountMutationFn = Apollo.MutationFunction<AddToCountMutation, AddToCountMutationVariables>;

/**
 * __useAddToCountMutation__
 *
 * To run a mutation, you first call `useAddToCountMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useAddToCountMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [addToCountMutation, { data, loading, error }] = useAddToCountMutation({
 *   variables: {
 *      item: // value for 'item'
 *   },
 * });
 */
export function useAddToCountMutation(baseOptions?: Apollo.MutationHookOptions<AddToCountMutation, AddToCountMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<AddToCountMutation, AddToCountMutationVariables>(AddToCountDocument, options);
      }
export type AddToCountMutationHookResult = ReturnType<typeof useAddToCountMutation>;
export type AddToCountMutationResult = Apollo.MutationResult<AddToCountMutation>;
export type AddToCountMutationOptions = Apollo.BaseMutationOptions<AddToCountMutation, AddToCountMutationVariables>;
export const CompleteCountDocument = gql`
    mutation CompleteCount($locationId: String!) {
  completeCount(locationId: $locationId) {
    success
    message
  }
}
    `;
export type CompleteCountMutationFn = Apollo.MutationFunction<CompleteCountMutation, CompleteCountMutationVariables>;

/**
 * __useCompleteCountMutation__
 *
 * To run a mutation, you first call `useCompleteCountMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useCompleteCountMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [completeCountMutation, { data, loading, error }] = useCompleteCountMutation({
 *   variables: {
 *      locationId: // value for 'locationId'
 *   },
 * });
 */
export function useCompleteCountMutation(baseOptions?: Apollo.MutationHookOptions<CompleteCountMutation, CompleteCountMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<CompleteCountMutation, CompleteCountMutationVariables>(CompleteCountDocument, options);
      }
export type CompleteCountMutationHookResult = ReturnType<typeof useCompleteCountMutation>;
export type CompleteCountMutationResult = Apollo.MutationResult<CompleteCountMutation>;
export type CompleteCountMutationOptions = Apollo.BaseMutationOptions<CompleteCountMutation, CompleteCountMutationVariables>;
export const CompleteVarianceCountDocument = gql`
    mutation CompleteVarianceCount($locationId: String!) {
  completeVarianceCount(locationId: $locationId) {
    success
    message
  }
}
    `;
export type CompleteVarianceCountMutationFn = Apollo.MutationFunction<CompleteVarianceCountMutation, CompleteVarianceCountMutationVariables>;

/**
 * __useCompleteVarianceCountMutation__
 *
 * To run a mutation, you first call `useCompleteVarianceCountMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useCompleteVarianceCountMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [completeVarianceCountMutation, { data, loading, error }] = useCompleteVarianceCountMutation({
 *   variables: {
 *      locationId: // value for 'locationId'
 *   },
 * });
 */
export function useCompleteVarianceCountMutation(baseOptions?: Apollo.MutationHookOptions<CompleteVarianceCountMutation, CompleteVarianceCountMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<CompleteVarianceCountMutation, CompleteVarianceCountMutationVariables>(CompleteVarianceCountDocument, options);
      }
export type CompleteVarianceCountMutationHookResult = ReturnType<typeof useCompleteVarianceCountMutation>;
export type CompleteVarianceCountMutationResult = Apollo.MutationResult<CompleteVarianceCountMutation>;
export type CompleteVarianceCountMutationOptions = Apollo.BaseMutationOptions<CompleteVarianceCountMutation, CompleteVarianceCountMutationVariables>;
export const GetCountDocument = gql`
    query GetCount($id: String!, $branchId: String!) {
  count(id: $id, branchId: $branchId) {
    id
    erpSystem
    branch {
      id
      name
    }
  }
}
    `;

/**
 * __useGetCountQuery__
 *
 * To run a query within a React component, call `useGetCountQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetCountQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetCountQuery({
 *   variables: {
 *      id: // value for 'id'
 *      branchId: // value for 'branchId'
 *   },
 * });
 */
export function useGetCountQuery(baseOptions: Apollo.QueryHookOptions<GetCountQuery, GetCountQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetCountQuery, GetCountQueryVariables>(GetCountDocument, options);
      }
export function useGetCountLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetCountQuery, GetCountQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetCountQuery, GetCountQueryVariables>(GetCountDocument, options);
        }
export type GetCountQueryHookResult = ReturnType<typeof useGetCountQuery>;
export type GetCountLazyQueryHookResult = ReturnType<typeof useGetCountLazyQuery>;
export type GetCountQueryResult = Apollo.QueryResult<GetCountQuery, GetCountQueryVariables>;
export const CreateWriteInDocument = gql`
    mutation CreateWriteIn($writeIn: WriteInInput!) {
  createWriteIn(writeIn: $writeIn) {
    success
    message
  }
}
    `;
export type CreateWriteInMutationFn = Apollo.MutationFunction<CreateWriteInMutation, CreateWriteInMutationVariables>;

/**
 * __useCreateWriteInMutation__
 *
 * To run a mutation, you first call `useCreateWriteInMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useCreateWriteInMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [createWriteInMutation, { data, loading, error }] = useCreateWriteInMutation({
 *   variables: {
 *      writeIn: // value for 'writeIn'
 *   },
 * });
 */
export function useCreateWriteInMutation(baseOptions?: Apollo.MutationHookOptions<CreateWriteInMutation, CreateWriteInMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<CreateWriteInMutation, CreateWriteInMutationVariables>(CreateWriteInDocument, options);
      }
export type CreateWriteInMutationHookResult = ReturnType<typeof useCreateWriteInMutation>;
export type CreateWriteInMutationResult = Apollo.MutationResult<CreateWriteInMutation>;
export type CreateWriteInMutationOptions = Apollo.BaseMutationOptions<CreateWriteInMutation, CreateWriteInMutationVariables>;
export const LoadVarianceDetailsDocument = gql`
    mutation loadVarianceDetails {
  loadVarianceDetails {
    success
    message
  }
}
    `;
export type LoadVarianceDetailsMutationFn = Apollo.MutationFunction<LoadVarianceDetailsMutation, LoadVarianceDetailsMutationVariables>;

/**
 * __useLoadVarianceDetailsMutation__
 *
 * To run a mutation, you first call `useLoadVarianceDetailsMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useLoadVarianceDetailsMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [loadVarianceDetailsMutation, { data, loading, error }] = useLoadVarianceDetailsMutation({
 *   variables: {
 *   },
 * });
 */
export function useLoadVarianceDetailsMutation(baseOptions?: Apollo.MutationHookOptions<LoadVarianceDetailsMutation, LoadVarianceDetailsMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<LoadVarianceDetailsMutation, LoadVarianceDetailsMutationVariables>(LoadVarianceDetailsDocument, options);
      }
export type LoadVarianceDetailsMutationHookResult = ReturnType<typeof useLoadVarianceDetailsMutation>;
export type LoadVarianceDetailsMutationResult = Apollo.MutationResult<LoadVarianceDetailsMutation>;
export type LoadVarianceDetailsMutationOptions = Apollo.BaseMutationOptions<LoadVarianceDetailsMutation, LoadVarianceDetailsMutationVariables>;
export const GetLocationDocument = gql`
    query GetLocation($id: String!) {
  location(id: $id) {
    id
    totalProducts
    totalCounted
    committed
    items {
      id
      prodDesc
      prodNum
      catalogNum
      tagNum
      uom
      quantity
      status
      sequence
      productImageUrl
      controlNum
    }
  }
}
    `;

/**
 * __useGetLocationQuery__
 *
 * To run a query within a React component, call `useGetLocationQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetLocationQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetLocationQuery({
 *   variables: {
 *      id: // value for 'id'
 *   },
 * });
 */
export function useGetLocationQuery(baseOptions: Apollo.QueryHookOptions<GetLocationQuery, GetLocationQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetLocationQuery, GetLocationQueryVariables>(GetLocationDocument, options);
      }
export function useGetLocationLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetLocationQuery, GetLocationQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetLocationQuery, GetLocationQueryVariables>(GetLocationDocument, options);
        }
export type GetLocationQueryHookResult = ReturnType<typeof useGetLocationQuery>;
export type GetLocationLazyQueryHookResult = ReturnType<typeof useGetLocationLazyQuery>;
export type GetLocationQueryResult = Apollo.QueryResult<GetLocationQuery, GetLocationQueryVariables>;
export const GetLocationsDocument = gql`
    query GetLocations {
  locations {
    totalLocations
    totalCounted
    content {
      id
      committed
      totalProducts
      totalCounted
    }
  }
}
    `;

/**
 * __useGetLocationsQuery__
 *
 * To run a query within a React component, call `useGetLocationsQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetLocationsQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetLocationsQuery({
 *   variables: {
 *   },
 * });
 */
export function useGetLocationsQuery(baseOptions?: Apollo.QueryHookOptions<GetLocationsQuery, GetLocationsQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetLocationsQuery, GetLocationsQueryVariables>(GetLocationsDocument, options);
      }
export function useGetLocationsLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetLocationsQuery, GetLocationsQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetLocationsQuery, GetLocationsQueryVariables>(GetLocationsDocument, options);
        }
export type GetLocationsQueryHookResult = ReturnType<typeof useGetLocationsQuery>;
export type GetLocationsLazyQueryHookResult = ReturnType<typeof useGetLocationsLazyQuery>;
export type GetLocationsQueryResult = Apollo.QueryResult<GetLocationsQuery, GetLocationsQueryVariables>;
export const GetNextLocationDocument = gql`
    query GetNextLocation($id: String!) {
  nextLocation(id: $id) {
    locationId
  }
}
    `;

/**
 * __useGetNextLocationQuery__
 *
 * To run a query within a React component, call `useGetNextLocationQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetNextLocationQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetNextLocationQuery({
 *   variables: {
 *      id: // value for 'id'
 *   },
 * });
 */
export function useGetNextLocationQuery(baseOptions: Apollo.QueryHookOptions<GetNextLocationQuery, GetNextLocationQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetNextLocationQuery, GetNextLocationQueryVariables>(GetNextLocationDocument, options);
      }
export function useGetNextLocationLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetNextLocationQuery, GetNextLocationQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetNextLocationQuery, GetNextLocationQueryVariables>(GetNextLocationDocument, options);
        }
export type GetNextLocationQueryHookResult = ReturnType<typeof useGetNextLocationQuery>;
export type GetNextLocationLazyQueryHookResult = ReturnType<typeof useGetNextLocationLazyQuery>;
export type GetNextLocationQueryResult = Apollo.QueryResult<GetNextLocationQuery, GetNextLocationQueryVariables>;
export const ProductDetailsDocument = gql`
    query ProductDetails($productId: String!) {
  productDetails(productId: $productId) {
    description
    catalogNumber
    upc
  }
}
    `;

/**
 * __useProductDetailsQuery__
 *
 * To run a query within a React component, call `useProductDetailsQuery` and pass it any options that fit your needs.
 * When your component renders, `useProductDetailsQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useProductDetailsQuery({
 *   variables: {
 *      productId: // value for 'productId'
 *   },
 * });
 */
export function useProductDetailsQuery(baseOptions: Apollo.QueryHookOptions<ProductDetailsQuery, ProductDetailsQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<ProductDetailsQuery, ProductDetailsQueryVariables>(ProductDetailsDocument, options);
      }
export function useProductDetailsLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<ProductDetailsQuery, ProductDetailsQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<ProductDetailsQuery, ProductDetailsQueryVariables>(ProductDetailsDocument, options);
        }
export type ProductDetailsQueryHookResult = ReturnType<typeof useProductDetailsQuery>;
export type ProductDetailsLazyQueryHookResult = ReturnType<typeof useProductDetailsLazyQuery>;
export type ProductDetailsQueryResult = Apollo.QueryResult<ProductDetailsQuery, ProductDetailsQueryVariables>;
export const RegisterLoginDocument = gql`
    mutation RegisterLogin {
  registerLogin {
    success
    message
  }
}
    `;
export type RegisterLoginMutationFn = Apollo.MutationFunction<RegisterLoginMutation, RegisterLoginMutationVariables>;

/**
 * __useRegisterLoginMutation__
 *
 * To run a mutation, you first call `useRegisterLoginMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useRegisterLoginMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [registerLoginMutation, { data, loading, error }] = useRegisterLoginMutation({
 *   variables: {
 *   },
 * });
 */
export function useRegisterLoginMutation(baseOptions?: Apollo.MutationHookOptions<RegisterLoginMutation, RegisterLoginMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<RegisterLoginMutation, RegisterLoginMutationVariables>(RegisterLoginDocument, options);
      }
export type RegisterLoginMutationHookResult = ReturnType<typeof useRegisterLoginMutation>;
export type RegisterLoginMutationResult = Apollo.MutationResult<RegisterLoginMutation>;
export type RegisterLoginMutationOptions = Apollo.BaseMutationOptions<RegisterLoginMutation, RegisterLoginMutationVariables>;
export const ResolveWriteInDocument = gql`
    mutation ResolveWriteIn($id: ID!) {
  resolveWriteIn(id: $id) {
    success
    message
  }
}
    `;
export type ResolveWriteInMutationFn = Apollo.MutationFunction<ResolveWriteInMutation, ResolveWriteInMutationVariables>;

/**
 * __useResolveWriteInMutation__
 *
 * To run a mutation, you first call `useResolveWriteInMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useResolveWriteInMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [resolveWriteInMutation, { data, loading, error }] = useResolveWriteInMutation({
 *   variables: {
 *      id: // value for 'id'
 *   },
 * });
 */
export function useResolveWriteInMutation(baseOptions?: Apollo.MutationHookOptions<ResolveWriteInMutation, ResolveWriteInMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<ResolveWriteInMutation, ResolveWriteInMutationVariables>(ResolveWriteInDocument, options);
      }
export type ResolveWriteInMutationHookResult = ReturnType<typeof useResolveWriteInMutation>;
export type ResolveWriteInMutationResult = Apollo.MutationResult<ResolveWriteInMutation>;
export type ResolveWriteInMutationOptions = Apollo.BaseMutationOptions<ResolveWriteInMutation, ResolveWriteInMutationVariables>;
export const SearchProductsEclipseDocument = gql`
    query searchProductsEclipse($input: ProductSearchEclipseInput!) {
  searchProductsEclipse(input: $input) {
    pagination {
      currentPage
      totalItemCount
    }
    products {
      manufacturerName
      id
      name
      upc
      productNumber
      imageUrls {
        thumb
      }
    }
  }
}
    `;

/**
 * __useSearchProductsEclipseQuery__
 *
 * To run a query within a React component, call `useSearchProductsEclipseQuery` and pass it any options that fit your needs.
 * When your component renders, `useSearchProductsEclipseQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useSearchProductsEclipseQuery({
 *   variables: {
 *      input: // value for 'input'
 *   },
 * });
 */
export function useSearchProductsEclipseQuery(baseOptions: Apollo.QueryHookOptions<SearchProductsEclipseQuery, SearchProductsEclipseQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<SearchProductsEclipseQuery, SearchProductsEclipseQueryVariables>(SearchProductsEclipseDocument, options);
      }
export function useSearchProductsEclipseLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<SearchProductsEclipseQuery, SearchProductsEclipseQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<SearchProductsEclipseQuery, SearchProductsEclipseQueryVariables>(SearchProductsEclipseDocument, options);
        }
export type SearchProductsEclipseQueryHookResult = ReturnType<typeof useSearchProductsEclipseQuery>;
export type SearchProductsEclipseLazyQueryHookResult = ReturnType<typeof useSearchProductsEclipseLazyQuery>;
export type SearchProductsEclipseQueryResult = Apollo.QueryResult<SearchProductsEclipseQuery, SearchProductsEclipseQueryVariables>;
export const SearchProductsKourierDocument = gql`
    query searchProductsKourier($input: ProductSearchKourierInput!) {
  searchProductsKourier(input: $input) {
    prodSearch {
      errorCode
      errorMessage
      productIdCount
      products {
        productId
        displayField
        productNumber
        upc
        productImageUrl
      }
    }
  }
}
    `;

/**
 * __useSearchProductsKourierQuery__
 *
 * To run a query within a React component, call `useSearchProductsKourierQuery` and pass it any options that fit your needs.
 * When your component renders, `useSearchProductsKourierQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useSearchProductsKourierQuery({
 *   variables: {
 *      input: // value for 'input'
 *   },
 * });
 */
export function useSearchProductsKourierQuery(baseOptions: Apollo.QueryHookOptions<SearchProductsKourierQuery, SearchProductsKourierQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<SearchProductsKourierQuery, SearchProductsKourierQueryVariables>(SearchProductsKourierDocument, options);
      }
export function useSearchProductsKourierLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<SearchProductsKourierQuery, SearchProductsKourierQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<SearchProductsKourierQuery, SearchProductsKourierQueryVariables>(SearchProductsKourierDocument, options);
        }
export type SearchProductsKourierQueryHookResult = ReturnType<typeof useSearchProductsKourierQuery>;
export type SearchProductsKourierLazyQueryHookResult = ReturnType<typeof useSearchProductsKourierLazyQuery>;
export type SearchProductsKourierQueryResult = Apollo.QueryResult<SearchProductsKourierQuery, SearchProductsKourierQueryVariables>;
export const UpdateCountDocument = gql`
    mutation UpdateCount($item: ItemInput!) {
  updateCount(item: $item) {
    success
    message
  }
}
    `;
export type UpdateCountMutationFn = Apollo.MutationFunction<UpdateCountMutation, UpdateCountMutationVariables>;

/**
 * __useUpdateCountMutation__
 *
 * To run a mutation, you first call `useUpdateCountMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useUpdateCountMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [updateCountMutation, { data, loading, error }] = useUpdateCountMutation({
 *   variables: {
 *      item: // value for 'item'
 *   },
 * });
 */
export function useUpdateCountMutation(baseOptions?: Apollo.MutationHookOptions<UpdateCountMutation, UpdateCountMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<UpdateCountMutation, UpdateCountMutationVariables>(UpdateCountDocument, options);
      }
export type UpdateCountMutationHookResult = ReturnType<typeof useUpdateCountMutation>;
export type UpdateCountMutationResult = Apollo.MutationResult<UpdateCountMutation>;
export type UpdateCountMutationOptions = Apollo.BaseMutationOptions<UpdateCountMutation, UpdateCountMutationVariables>;
export const UpdateVarianceCountDocument = gql`
    mutation UpdateVarianceCount($item: ItemInput!) {
  updateVarianceCount(item: $item) {
    success
    message
  }
}
    `;
export type UpdateVarianceCountMutationFn = Apollo.MutationFunction<UpdateVarianceCountMutation, UpdateVarianceCountMutationVariables>;

/**
 * __useUpdateVarianceCountMutation__
 *
 * To run a mutation, you first call `useUpdateVarianceCountMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useUpdateVarianceCountMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [updateVarianceCountMutation, { data, loading, error }] = useUpdateVarianceCountMutation({
 *   variables: {
 *      item: // value for 'item'
 *   },
 * });
 */
export function useUpdateVarianceCountMutation(baseOptions?: Apollo.MutationHookOptions<UpdateVarianceCountMutation, UpdateVarianceCountMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<UpdateVarianceCountMutation, UpdateVarianceCountMutationVariables>(UpdateVarianceCountDocument, options);
      }
export type UpdateVarianceCountMutationHookResult = ReturnType<typeof useUpdateVarianceCountMutation>;
export type UpdateVarianceCountMutationResult = Apollo.MutationResult<UpdateVarianceCountMutation>;
export type UpdateVarianceCountMutationOptions = Apollo.BaseMutationOptions<UpdateVarianceCountMutation, UpdateVarianceCountMutationVariables>;
export const UpdateWriteInDocument = gql`
    mutation UpdateWriteIn($id: ID!, $writeIn: WriteInInput!) {
  updateWriteIn(id: $id, writeIn: $writeIn) {
    success
    message
  }
}
    `;
export type UpdateWriteInMutationFn = Apollo.MutationFunction<UpdateWriteInMutation, UpdateWriteInMutationVariables>;

/**
 * __useUpdateWriteInMutation__
 *
 * To run a mutation, you first call `useUpdateWriteInMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useUpdateWriteInMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [updateWriteInMutation, { data, loading, error }] = useUpdateWriteInMutation({
 *   variables: {
 *      id: // value for 'id'
 *      writeIn: // value for 'writeIn'
 *   },
 * });
 */
export function useUpdateWriteInMutation(baseOptions?: Apollo.MutationHookOptions<UpdateWriteInMutation, UpdateWriteInMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<UpdateWriteInMutation, UpdateWriteInMutationVariables>(UpdateWriteInDocument, options);
      }
export type UpdateWriteInMutationHookResult = ReturnType<typeof useUpdateWriteInMutation>;
export type UpdateWriteInMutationResult = Apollo.MutationResult<UpdateWriteInMutation>;
export type UpdateWriteInMutationOptions = Apollo.BaseMutationOptions<UpdateWriteInMutation, UpdateWriteInMutationVariables>;
export const GetVarianceLocationDocument = gql`
    query GetVarianceLocation($id: String!) {
  varianceLocation(id: $id) {
    totalCounted
    totalProducts
    committed
    id
    items {
      id
      locationId
      prodDesc
      prodNum
      tagNum
      catalogNum
      uom
      quantity
      status
      sequence
      varianceCost
      varianceStatus
      productImageUrl
    }
  }
}
    `;

/**
 * __useGetVarianceLocationQuery__
 *
 * To run a query within a React component, call `useGetVarianceLocationQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetVarianceLocationQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetVarianceLocationQuery({
 *   variables: {
 *      id: // value for 'id'
 *   },
 * });
 */
export function useGetVarianceLocationQuery(baseOptions: Apollo.QueryHookOptions<GetVarianceLocationQuery, GetVarianceLocationQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetVarianceLocationQuery, GetVarianceLocationQueryVariables>(GetVarianceLocationDocument, options);
      }
export function useGetVarianceLocationLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetVarianceLocationQuery, GetVarianceLocationQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetVarianceLocationQuery, GetVarianceLocationQueryVariables>(GetVarianceLocationDocument, options);
        }
export type GetVarianceLocationQueryHookResult = ReturnType<typeof useGetVarianceLocationQuery>;
export type GetVarianceLocationLazyQueryHookResult = ReturnType<typeof useGetVarianceLocationLazyQuery>;
export type GetVarianceLocationQueryResult = Apollo.QueryResult<GetVarianceLocationQuery, GetVarianceLocationQueryVariables>;
export const GetVarianceLocationsDocument = gql`
    query GetVarianceLocations {
  varianceLocations {
    totalLocations
    content {
      id
      totalProducts
      netVarianceCost
      grossVarianceCost
    }
  }
}
    `;

/**
 * __useGetVarianceLocationsQuery__
 *
 * To run a query within a React component, call `useGetVarianceLocationsQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetVarianceLocationsQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetVarianceLocationsQuery({
 *   variables: {
 *   },
 * });
 */
export function useGetVarianceLocationsQuery(baseOptions?: Apollo.QueryHookOptions<GetVarianceLocationsQuery, GetVarianceLocationsQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetVarianceLocationsQuery, GetVarianceLocationsQueryVariables>(GetVarianceLocationsDocument, options);
      }
export function useGetVarianceLocationsLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetVarianceLocationsQuery, GetVarianceLocationsQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetVarianceLocationsQuery, GetVarianceLocationsQueryVariables>(GetVarianceLocationsDocument, options);
        }
export type GetVarianceLocationsQueryHookResult = ReturnType<typeof useGetVarianceLocationsQuery>;
export type GetVarianceLocationsLazyQueryHookResult = ReturnType<typeof useGetVarianceLocationsLazyQuery>;
export type GetVarianceLocationsQueryResult = Apollo.QueryResult<GetVarianceLocationsQuery, GetVarianceLocationsQueryVariables>;
export const GetVarianceNextLocationDocument = gql`
    query GetVarianceNextLocation($id: String!) {
  varianceNextLocation(id: $id) {
    locationId
  }
}
    `;

/**
 * __useGetVarianceNextLocationQuery__
 *
 * To run a query within a React component, call `useGetVarianceNextLocationQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetVarianceNextLocationQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetVarianceNextLocationQuery({
 *   variables: {
 *      id: // value for 'id'
 *   },
 * });
 */
export function useGetVarianceNextLocationQuery(baseOptions: Apollo.QueryHookOptions<GetVarianceNextLocationQuery, GetVarianceNextLocationQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetVarianceNextLocationQuery, GetVarianceNextLocationQueryVariables>(GetVarianceNextLocationDocument, options);
      }
export function useGetVarianceNextLocationLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetVarianceNextLocationQuery, GetVarianceNextLocationQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetVarianceNextLocationQuery, GetVarianceNextLocationQueryVariables>(GetVarianceNextLocationDocument, options);
        }
export type GetVarianceNextLocationQueryHookResult = ReturnType<typeof useGetVarianceNextLocationQuery>;
export type GetVarianceNextLocationLazyQueryHookResult = ReturnType<typeof useGetVarianceNextLocationLazyQuery>;
export type GetVarianceNextLocationQueryResult = Apollo.QueryResult<GetVarianceNextLocationQuery, GetVarianceNextLocationQueryVariables>;
export const GetVarianceSummaryDocument = gql`
    query GetVarianceSummary {
  varianceSummary {
    netTotalCost
    grossTotalCost
    productQuantity
    locationQuantity
    differenceQuantity
  }
}
    `;

/**
 * __useGetVarianceSummaryQuery__
 *
 * To run a query within a React component, call `useGetVarianceSummaryQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetVarianceSummaryQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetVarianceSummaryQuery({
 *   variables: {
 *   },
 * });
 */
export function useGetVarianceSummaryQuery(baseOptions?: Apollo.QueryHookOptions<GetVarianceSummaryQuery, GetVarianceSummaryQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetVarianceSummaryQuery, GetVarianceSummaryQueryVariables>(GetVarianceSummaryDocument, options);
      }
export function useGetVarianceSummaryLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetVarianceSummaryQuery, GetVarianceSummaryQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetVarianceSummaryQuery, GetVarianceSummaryQueryVariables>(GetVarianceSummaryDocument, options);
        }
export type GetVarianceSummaryQueryHookResult = ReturnType<typeof useGetVarianceSummaryQuery>;
export type GetVarianceSummaryLazyQueryHookResult = ReturnType<typeof useGetVarianceSummaryLazyQuery>;
export type GetVarianceSummaryQueryResult = Apollo.QueryResult<GetVarianceSummaryQuery, GetVarianceSummaryQueryVariables>;
export const GetWriteInDocument = gql`
    query GetWriteIn($id: ID!) {
  writeIn(id: $id) {
    id
    locationId
    catalogNum
    upcNum
    description
    uom
    quantity
    comment
    createdBy
    createdAt
    updatedBy
    updatedAt
    resolved
  }
}
    `;

/**
 * __useGetWriteInQuery__
 *
 * To run a query within a React component, call `useGetWriteInQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetWriteInQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetWriteInQuery({
 *   variables: {
 *      id: // value for 'id'
 *   },
 * });
 */
export function useGetWriteInQuery(baseOptions: Apollo.QueryHookOptions<GetWriteInQuery, GetWriteInQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetWriteInQuery, GetWriteInQueryVariables>(GetWriteInDocument, options);
      }
export function useGetWriteInLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetWriteInQuery, GetWriteInQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetWriteInQuery, GetWriteInQueryVariables>(GetWriteInDocument, options);
        }
export type GetWriteInQueryHookResult = ReturnType<typeof useGetWriteInQuery>;
export type GetWriteInLazyQueryHookResult = ReturnType<typeof useGetWriteInLazyQuery>;
export type GetWriteInQueryResult = Apollo.QueryResult<GetWriteInQuery, GetWriteInQueryVariables>;
export const GetWriteInsDocument = gql`
    query GetWriteIns($options: WriteInsInput!) {
  writeIns(options: $options) {
    content {
      id
      locationId
      catalogNum
      upcNum
      description
      uom
      quantity
      comment
      createdBy
      createdAt
      updatedBy
      updatedAt
      resolved
    }
    pageable {
      sort {
        sorted
        unsorted
        empty
      }
      pageNumber
      pageSize
      offset
      paged
      unpaged
    }
  }
}
    `;

/**
 * __useGetWriteInsQuery__
 *
 * To run a query within a React component, call `useGetWriteInsQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetWriteInsQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetWriteInsQuery({
 *   variables: {
 *      options: // value for 'options'
 *   },
 * });
 */
export function useGetWriteInsQuery(baseOptions: Apollo.QueryHookOptions<GetWriteInsQuery, GetWriteInsQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetWriteInsQuery, GetWriteInsQueryVariables>(GetWriteInsDocument, options);
      }
export function useGetWriteInsLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetWriteInsQuery, GetWriteInsQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetWriteInsQuery, GetWriteInsQueryVariables>(GetWriteInsDocument, options);
        }
export type GetWriteInsQueryHookResult = ReturnType<typeof useGetWriteInsQuery>;
export type GetWriteInsLazyQueryHookResult = ReturnType<typeof useGetWriteInsLazyQuery>;
export type GetWriteInsQueryResult = Apollo.QueryResult<GetWriteInsQuery, GetWriteInsQueryVariables>;