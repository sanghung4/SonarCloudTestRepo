import { gql } from "@apollo/client";
import * as Apollo from "@apollo/client";
export type Maybe<T> = T | null;
export type InputMaybe<T> = Maybe<T>;
export type Exact<T extends { [key: string]: unknown }> = { [K in keyof T]: T[K] };
export type MakeOptional<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]?: Maybe<T[SubKey]> };
export type MakeMaybe<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]: Maybe<T[SubKey]> };
const defaultOptions = {} as const;
/** All built-in and custom scalars, mapped to their actual values */
export type Scalars = {
  ID: string;
  String: string;
  Boolean: boolean;
  Int: number;
  Float: number;
};

export type AddToCountMutationResponse = {
  __typename?: "AddToCountMutationResponse";
  item: LocationItem;
  message?: Maybe<Scalars["String"]>;
  success: Scalars["Boolean"];
};

export type Branch = {
  __typename?: "Branch";
  id: Scalars["String"];
  name?: Maybe<Scalars["String"]>;
};

export type BranchResponse = {
  __typename?: "BranchResponse";
  branchId: Scalars["String"];
  branchName: Scalars["String"];
};

export type BranchUsersListResponse = {
  __typename?: "BranchUsersListResponse";
  assignedUsers?: Maybe<Array<Maybe<Scalars["String"]>>>;
  branchId: Scalars["String"];
};

export type CloseTaskInput = {
  branchId: Scalars["String"];
  finalLocation?: InputMaybe<Scalars["String"]>;
  invoiceId: Scalars["String"];
  orderId: Scalars["String"];
  skipInvalidLocationWarningFlag?: InputMaybe<Scalars["Boolean"]>;
  skipStagedWarningFlag?: InputMaybe<Scalars["Boolean"]>;
  tote: Scalars["String"];
  updateLocationOnly?: InputMaybe<Scalars["Boolean"]>;
};

export type CloseTaskResponse = {
  __typename?: "CloseTaskResponse";
  message: Scalars["String"];
  success: Scalars["Boolean"];
};

export type CompletePickInput = {
  branchId: Scalars["String"];
  cutDetail?: InputMaybe<Scalars["String"]>;
  cutGroup?: InputMaybe<Scalars["String"]>;
  description: Scalars["String"];
  generationId?: InputMaybe<Scalars["Int"]>;
  ignoreLockToteCheck: Scalars["Boolean"];
  isLot?: InputMaybe<Scalars["String"]>;
  isOverrideProduct: Scalars["Boolean"];
  isParallelCut?: InputMaybe<Scalars["Boolean"]>;
  isSerial: Scalars["Boolean"];
  lineId: Scalars["Int"];
  location: Scalars["String"];
  locationType: Scalars["String"];
  lot: Scalars["String"];
  orderId?: InputMaybe<Scalars["String"]>;
  pickGroup?: InputMaybe<Scalars["String"]>;
  productId: Scalars["String"];
  quantity: Scalars["Int"];
  shipVia: Scalars["String"];
  splitId?: InputMaybe<Scalars["String"]>;
  startPickTime: Scalars["String"];
  tote?: InputMaybe<Scalars["String"]>;
  uom: Scalars["String"];
  userId: Scalars["String"];
  warehouseID: Scalars["String"];
};

export type Count = {
  __typename?: "Count";
  branch: Branch;
  erpSystem: ErpSystem;
  id: Scalars["String"];
};

export type CountMutationResponse = {
  __typename?: "CountMutationResponse";
  message?: Maybe<Scalars["String"]>;
  success: Scalars["Boolean"];
};

export enum CountStatus {
  Complete = "COMPLETE",
  Error = "ERROR",
  InProgress = "IN_PROGRESS",
  NotLoaded = "NOT_LOADED",
}

export type CountWithStatus = {
  __typename?: "CountWithStatus";
  branchId: Scalars["String"];
  branchName?: Maybe<Scalars["String"]>;
  countId: Scalars["String"];
  createdAt?: Maybe<Scalars["String"]>;
  errorMessage?: Maybe<Scalars["String"]>;
  id?: Maybe<Scalars["ID"]>;
  status?: Maybe<CountStatus>;
  totalProducts?: Maybe<Scalars["Int"]>;
};

export type CountsInput = {
  endDate: Scalars["String"];
  startDate: Scalars["String"];
};

export type CustomerResponse = {
  __typename?: "CustomerResponse";
  addressLine1?: Maybe<Scalars["String"]>;
  addressLine2?: Maybe<Scalars["String"]>;
  city?: Maybe<Scalars["String"]>;
  countryCode?: Maybe<Scalars["String"]>;
  homeBranch: Scalars["String"];
  id: Scalars["String"];
  isBillTo?: Maybe<Scalars["Boolean"]>;
  isShipTo?: Maybe<Scalars["Boolean"]>;
  name: Scalars["String"];
  postalCode?: Maybe<Scalars["String"]>;
  state?: Maybe<Scalars["String"]>;
};

export type CustomerSearchInput = {
  currentPage?: InputMaybe<Scalars["Int"]>;
  id?: InputMaybe<Array<InputMaybe<Scalars["String"]>>>;
  keyword?: InputMaybe<Scalars["String"]>;
  pageSize?: InputMaybe<Scalars["Int"]>;
};

export type CustomerSearchResponse = {
  __typename?: "CustomerSearchResponse";
  metadata?: Maybe<EclipseSearchMetadata>;
  results?: Maybe<Array<Maybe<CustomerSearchResult>>>;
};

export type CustomerSearchResult = {
  __typename?: "CustomerSearchResult";
  addressLine1?: Maybe<Scalars["String"]>;
  addressLine2?: Maybe<Scalars["String"]>;
  addressLine3?: Maybe<Scalars["String"]>;
  addressLine4?: Maybe<Scalars["String"]>;
  billToId?: Maybe<Scalars["String"]>;
  city?: Maybe<Scalars["String"]>;
  countryCode?: Maybe<Scalars["String"]>;
  defaultPriceClass?: Maybe<Scalars["String"]>;
  ediId?: Maybe<Scalars["String"]>;
  id?: Maybe<Scalars["String"]>;
  isBillTo?: Maybe<Scalars["Boolean"]>;
  isBranchCash?: Maybe<Scalars["Boolean"]>;
  isProspect?: Maybe<Scalars["Boolean"]>;
  isShipTo?: Maybe<Scalars["Boolean"]>;
  name?: Maybe<Scalars["String"]>;
  nameIndex?: Maybe<Scalars["String"]>;
  orderEntryMessage?: Maybe<Scalars["String"]>;
  postalCode?: Maybe<Scalars["String"]>;
  shipToLists: Array<ShipToId>;
  sortBy?: Maybe<Scalars["String"]>;
  state?: Maybe<Scalars["String"]>;
  updateKey?: Maybe<Scalars["String"]>;
};

export type DeleteCountResponse = {
  __typename?: "DeleteCountResponse";
  countLocationItemQuantities: Scalars["Int"];
  countLocationItems: Scalars["Int"];
  countLocations: Scalars["Int"];
  varianceCountLocationItemQuantities: Scalars["Int"];
  writeIns: Scalars["Int"];
};

export type DeleteMultipleCountResponse = {
  __typename?: "DeleteMultipleCountResponse";
  deletedCounts: Array<DeleteCountResponse>;
};

export type DetailedVarianceResponse = {
  __typename?: "DetailedVarianceResponse";
  counts?: Maybe<Array<Maybe<VarianceDetails>>>;
};

export type EclipseSearchMetadata = {
  __typename?: "EclipseSearchMetadata";
  pageSize?: Maybe<Scalars["Int"]>;
  startIndex?: Maybe<Scalars["Int"]>;
  totalItems?: Maybe<Scalars["Int"]>;
};

export enum ErpSystem {
  Eclipse = "ECLIPSE",
  Mincron = "MINCRON",
}

export type ErrorResponse = {
  __typename?: "ErrorResponse";
  code: Scalars["String"];
  details?: Maybe<Scalars["String"]>;
  message: Scalars["String"];
};

export type FailedPriceSuggestion = {
  __typename?: "FailedPriceSuggestion";
  branch: Scalars["String"];
  changeWriterDisplayName: Scalars["String"];
  changeWriterId: Scalars["String"];
  cmpPrice: Scalars["Float"];
  customerId: Scalars["String"];
  newPrice: Scalars["Float"];
  priceCategory: Scalars["String"];
  productId: Scalars["String"];
};

export type ImageUrls = {
  __typename?: "ImageUrls";
  large?: Maybe<Scalars["String"]>;
  medium?: Maybe<Scalars["String"]>;
  small?: Maybe<Scalars["String"]>;
  thumb?: Maybe<Scalars["String"]>;
};

export type ItemInput = {
  locationId: Scalars["String"];
  productId: Scalars["String"];
  quantity?: InputMaybe<Scalars["Int"]>;
};

export type KourierProduct = {
  __typename?: "KourierProduct";
  displayField?: Maybe<Scalars["String"]>;
  productId?: Maybe<Scalars["String"]>;
  productImageUrl?: Maybe<Scalars["String"]>;
  productNumber?: Maybe<Scalars["String"]>;
  upc?: Maybe<Scalars["String"]>;
};

export type LinkAccountResponse = {
  __typename?: "LinkAccountResponse";
  message?: Maybe<Scalars["String"]>;
  success: Scalars["Boolean"];
};

export type Location = {
  __typename?: "Location";
  committed: Scalars["Boolean"];
  id: Scalars["String"];
  items: Array<LocationItem>;
  totalCounted: Scalars["Int"];
  totalProducts: Scalars["Int"];
};

export type LocationItem = {
  __typename?: "LocationItem";
  catalogNum?: Maybe<Scalars["String"]>;
  controlNum?: Maybe<Scalars["String"]>;
  id?: Maybe<Scalars["ID"]>;
  locationId?: Maybe<Scalars["String"]>;
  prodDesc?: Maybe<Scalars["String"]>;
  prodNum: Scalars["String"];
  productImageUrl?: Maybe<Scalars["String"]>;
  quantity?: Maybe<Scalars["Int"]>;
  sequence?: Maybe<Scalars["Int"]>;
  status?: Maybe<LocationItemStatus>;
  tagNum?: Maybe<Scalars["String"]>;
  uom?: Maybe<Scalars["String"]>;
};

export enum LocationItemStatus {
  Committed = "COMMITTED",
  Staged = "STAGED",
  Uncounted = "UNCOUNTED",
}

export type LocationSummary = {
  __typename?: "LocationSummary";
  committed: Scalars["Boolean"];
  id: Scalars["String"];
  totalCounted: Scalars["Int"];
  totalProducts: Scalars["Int"];
};

export type LocationsResponse = {
  __typename?: "LocationsResponse";
  content: Array<LocationSummary>;
  totalCounted: Scalars["Int"];
  totalLocations: Scalars["Int"];
};

export type MetricsBranch = {
  __typename?: "MetricsBranch";
  city?: Maybe<Scalars["String"]>;
  id?: Maybe<Scalars["String"]>;
  loginCount?: Maybe<Scalars["Int"]>;
  state?: Maybe<Scalars["String"]>;
  userCount?: Maybe<Scalars["Int"]>;
};

export type MetricsChange = {
  __typename?: "MetricsChange";
  metric: Scalars["String"];
  percentageChange?: Maybe<Scalars["String"]>;
  quantity?: Maybe<Scalars["String"]>;
};

export type MetricsCompletion = {
  __typename?: "MetricsCompletion";
  branchId: Scalars["Int"];
  countId: Scalars["Int"];
  counted: Scalars["Int"];
  location: Scalars["String"];
  needToBeCounted: Scalars["Int"];
  status: Scalars["String"];
  timeEnded: Scalars["String"];
  timeStarted: Scalars["String"];
  total: Scalars["Int"];
};

export type MetricsCompletionInput = {
  branchId?: InputMaybe<Scalars["String"]>;
  countId?: InputMaybe<Scalars["String"]>;
  counted?: InputMaybe<Scalars["Int"]>;
  location?: InputMaybe<Scalars["String"]>;
  needToBeCounted?: InputMaybe<Scalars["Int"]>;
  status?: InputMaybe<Scalars["String"]>;
  timeEnded?: InputMaybe<Scalars["String"]>;
  timeStarted?: InputMaybe<Scalars["String"]>;
  total?: InputMaybe<Scalars["Int"]>;
};

export type MetricsCompletionResponse = {
  __typename?: "MetricsCompletionResponse";
  content: MetricsCompletion;
  message?: Maybe<Scalars["String"]>;
  success: Scalars["Boolean"];
};

export type MetricsDivision = {
  __typename?: "MetricsDivision";
  branchCount?: Maybe<Scalars["Int"]>;
  branches?: Maybe<Array<Maybe<MetricsBranch>>>;
  division: Scalars["String"];
  loginCount?: Maybe<Scalars["Int"]>;
  userCount?: Maybe<Scalars["Int"]>;
};

export type MetricsDoubleRangeInput = {
  endDateWeekNew: Scalars["String"];
  endDateWeekOld: Scalars["String"];
  startDateWeekNew: Scalars["String"];
  startDateWeekOld: Scalars["String"];
};

export type MetricsLoginResponse = {
  __typename?: "MetricsLoginResponse";
  message: Scalars["String"];
  success: Scalars["Boolean"];
};

export type MetricsOverview = {
  __typename?: "MetricsOverview";
  response?: Maybe<Array<Maybe<MetricsChange>>>;
  type: Scalars["String"];
};

export type MetricsPercentageChange = {
  __typename?: "MetricsPercentageChange";
  response?: Maybe<Array<Maybe<MetricsPercentageChangeDivision>>>;
  type: Scalars["String"];
};

export type MetricsPercentageChangeDivision = {
  __typename?: "MetricsPercentageChangeDivision";
  branchChange?: Maybe<Scalars["String"]>;
  division: Scalars["String"];
  loginChange?: Maybe<Scalars["String"]>;
  userChange?: Maybe<Scalars["String"]>;
};

export type MetricsPercentageTotal = {
  __typename?: "MetricsPercentageTotal";
  response?: Maybe<Array<Maybe<MetricsPercentageTotalDivision>>>;
  type: Scalars["String"];
};

export type MetricsPercentageTotalDivision = {
  __typename?: "MetricsPercentageTotalDivision";
  branchCount?: Maybe<Scalars["Int"]>;
  branchPercentage?: Maybe<Scalars["Float"]>;
  division: Scalars["String"];
  loginCount?: Maybe<Scalars["Int"]>;
  loginPercentage?: Maybe<Scalars["Float"]>;
  userCount?: Maybe<Scalars["Int"]>;
  userPercentage?: Maybe<Scalars["Float"]>;
};

export type MetricsSingleRangeInput = {
  endDate?: InputMaybe<Scalars["String"]>;
  startDate?: InputMaybe<Scalars["String"]>;
};

export type MetricsUsage = {
  __typename?: "MetricsUsage";
  response?: Maybe<Array<Maybe<MetricsDivision>>>;
  type: Scalars["String"];
};

export type Mutation = {
  __typename?: "Mutation";
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
  purgeMincronCounts: Scalars["String"];
  registerLogin: MetricsLoginResponse;
  removeBranchCounts: DeleteMultipleCountResponse;
  removeCounts: Scalars["String"];
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
  branchId: Scalars["String"];
  userEmail: Scalars["String"];
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
  locationId: Scalars["String"];
};

export type MutationCompleteUserPickArgs = {
  input: CompletePickInput;
};

export type MutationCompleteVarianceCountArgs = {
  locationId: Scalars["String"];
};

export type MutationCreateWriteInArgs = {
  writeIn: WriteInInput;
};

export type MutationDeleteCountArgs = {
  id: Scalars["ID"];
};

export type MutationDeleteUserArgs = {
  userEmail: Scalars["String"];
};

export type MutationLoadCountArgs = {
  branchId: Scalars["String"];
  countId: Scalars["String"];
};

export type MutationPurgeMincronCountsArgs = {
  input: CountsInput;
};

export type MutationRemoveBranchCountsArgs = {
  input: RemoveBranchCountsInput;
};

export type MutationRemoveCountsArgs = {
  input: RemoveCountsInput;
};

export type MutationRemoveUserBranchArgs = {
  branchId: Scalars["String"];
  userEmail: Scalars["String"];
};

export type MutationResolveWriteInArgs = {
  id: Scalars["ID"];
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
  newUserEmail: Scalars["String"];
  oldUserEmail: Scalars["String"];
};

export type MutationUpdateVarianceCountArgs = {
  item: ItemInput;
};

export type MutationUpdateWriteInArgs = {
  id: Scalars["ID"];
  writeIn: WriteInInput;
};

export type MutationVerifyEclipseCredentialsArgs = {
  password: Scalars["String"];
  username: Scalars["String"];
};

export type NextLocationResponse = {
  __typename?: "NextLocationResponse";
  locationId: Scalars["String"];
};

export type PackageDimensions = {
  __typename?: "PackageDimensions";
  height?: Maybe<Scalars["Float"]>;
  length?: Maybe<Scalars["Float"]>;
  volume?: Maybe<Scalars["Float"]>;
  volumeUnitOfMeasure?: Maybe<Scalars["String"]>;
  weight?: Maybe<Scalars["Float"]>;
  weightUnitOfMeasure?: Maybe<Scalars["String"]>;
  width?: Maybe<Scalars["Float"]>;
};

export type PackageInput = {
  packageQuantity: Scalars["Int"];
  packageType: Scalars["String"];
};

export type Pageable = {
  __typename?: "Pageable";
  offset: Scalars["Int"];
  pageNumber: Scalars["Int"];
  pageSize: Scalars["Int"];
  paged: Scalars["Boolean"];
  sort: PageableSort;
  unpaged: Scalars["Boolean"];
};

export type PageableSort = {
  __typename?: "PageableSort";
  empty: Scalars["Boolean"];
  sorted: Scalars["Boolean"];
  unsorted: Scalars["Boolean"];
};

export type Pagination = {
  __typename?: "Pagination";
  currentPage: Scalars["Int"];
  pageSize: Scalars["Int"];
  totalItemCount: Scalars["Int"];
};

export type PagingContext = {
  orderBy: Scalars["String"];
  orderDirection: Scalars["String"];
  page: Scalars["Int"];
  pageSize: Scalars["Int"];
};

export type PickingOrder = {
  __typename?: "PickingOrder";
  assignedUserId?: Maybe<Scalars["String"]>;
  billTo?: Maybe<Scalars["Int"]>;
  branchId?: Maybe<Scalars["String"]>;
  generationId?: Maybe<Scalars["Int"]>;
  invoiceId?: Maybe<Scalars["String"]>;
  isFromMultipleZones?: Maybe<Scalars["Boolean"]>;
  orderId?: Maybe<Scalars["String"]>;
  pickCount?: Maybe<Scalars["String"]>;
  pickGroup?: Maybe<Scalars["String"]>;
  shipTo?: Maybe<Scalars["Int"]>;
  shipToName?: Maybe<Scalars["String"]>;
  shipVia?: Maybe<Scalars["String"]>;
  taskState?: Maybe<Scalars["String"]>;
  taskWeight?: Maybe<Scalars["Float"]>;
};

export type PickingOrderInput = {
  branchId: Scalars["String"];
  orderId?: InputMaybe<Scalars["String"]>;
  userId: Scalars["String"];
};

export type PickingTaskInput = {
  assignedUserId?: InputMaybe<Scalars["String"]>;
  billTo?: InputMaybe<Scalars["Int"]>;
  branchId?: InputMaybe<Scalars["String"]>;
  generationId?: InputMaybe<Scalars["Int"]>;
  invoiceId?: InputMaybe<Scalars["String"]>;
  isFromMultipleZones?: InputMaybe<Scalars["Boolean"]>;
  orderId?: InputMaybe<Scalars["String"]>;
  pickCount?: InputMaybe<Scalars["String"]>;
  pickGroup?: InputMaybe<Scalars["String"]>;
  shipTo?: InputMaybe<Scalars["Int"]>;
  shipToName?: InputMaybe<Scalars["String"]>;
  shipVia?: InputMaybe<Scalars["String"]>;
  taskState?: InputMaybe<Scalars["String"]>;
  taskWeight?: InputMaybe<Scalars["Float"]>;
};

export type Price = {
  __typename?: "Price";
  currency: Scalars["String"];
  displayName: Scalars["String"];
  type: Scalars["String"];
  value: Scalars["Float"];
};

export type PriceChangeInput = {
  priceChangeSuggestions: Array<PriceSuggestion>;
  priceCreateSuggestions: Array<PriceSuggestion>;
};

export type PriceSuggestion = {
  branch: Scalars["String"];
  changeWriterDisplayName: Scalars["String"];
  changeWriterId: Scalars["String"];
  cmpPrice: Scalars["Float"];
  customerId: Scalars["String"];
  newPrice: Scalars["Float"];
  priceCategory: Scalars["String"];
  productId: Scalars["String"];
  territory?: InputMaybe<Scalars["String"]>;
};

export type Product = {
  __typename?: "Product";
  environmentalOptions?: Maybe<Array<Maybe<Scalars["String"]>>>;
  featuresAndBenefits?: Maybe<Scalars["String"]>;
  id?: Maybe<Scalars["ID"]>;
  imageUrls?: Maybe<ImageUrls>;
  manufacturerName?: Maybe<Scalars["String"]>;
  manufacturerNumber?: Maybe<Scalars["String"]>;
  name?: Maybe<Scalars["String"]>;
  packageDimensions?: Maybe<PackageDimensions>;
  price?: Maybe<Scalars["Float"]>;
  productImageUrl?: Maybe<Scalars["String"]>;
  productNumber?: Maybe<Scalars["String"]>;
  productOverview?: Maybe<Scalars["String"]>;
  productType?: Maybe<Scalars["String"]>;
  seriesModelFigureNumber?: Maybe<Scalars["String"]>;
  stock?: Maybe<Stock>;
  taxonomy?: Maybe<Array<Maybe<Scalars["String"]>>>;
  techSpecifications?: Maybe<Array<Maybe<TechSpec>>>;
  technicalDocuments?: Maybe<Array<Maybe<TechDoc>>>;
  unspsc?: Maybe<Scalars["String"]>;
  upc?: Maybe<Scalars["String"]>;
};

export type ProductAttribute = {
  attributeType?: InputMaybe<Scalars["String"]>;
  attributeValue?: InputMaybe<Scalars["String"]>;
};

export type ProductDetails = {
  __typename?: "ProductDetails";
  catalogNumber: Scalars["String"];
  description: Scalars["String"];
  upc: Scalars["String"];
};

export type ProductPriceInput = {
  branch?: InputMaybe<Scalars["String"]>;
  correlationId?: InputMaybe<Scalars["String"]>;
  customerId?: InputMaybe<Scalars["String"]>;
  effectiveDate?: InputMaybe<Scalars["String"]>;
  productId: Scalars["String"];
  userId?: InputMaybe<Scalars["String"]>;
};

export type ProductPriceResponse = {
  __typename?: "ProductPriceResponse";
  cmp?: Maybe<Scalars["Float"]>;
  correlationId?: Maybe<Scalars["String"]>;
  customerId?: Maybe<Scalars["String"]>;
  erpBranchNum?: Maybe<Scalars["String"]>;
  matchedBranch?: Maybe<Scalars["String"]>;
  matchedClass?: Maybe<Scalars["String"]>;
  matchedGroup?: Maybe<Scalars["String"]>;
  matrixId?: Maybe<Scalars["String"]>;
  productId?: Maybe<Scalars["String"]>;
  rateCardName?: Maybe<Scalars["String"]>;
  rateCardPrice?: Maybe<Scalars["Float"]>;
  stdCost?: Maybe<Scalars["Float"]>;
  uom?: Maybe<Scalars["String"]>;
};

export type ProductSearchEclipseInput = {
  currentPage?: InputMaybe<Scalars["Int"]>;
  pageSize?: InputMaybe<Scalars["Int"]>;
  searchInputType: Scalars["Int"];
  searchTerm?: InputMaybe<Scalars["String"]>;
  selectedAttributes?: InputMaybe<Array<InputMaybe<ProductAttribute>>>;
};

export type ProductSearchKourierInput = {
  displayName?: InputMaybe<Scalars["String"]>;
  keywords: Scalars["String"];
  searchInputType?: InputMaybe<Scalars["String"]>;
};

export type ProductSearchKourierResponse = {
  __typename?: "ProductSearchKourierResponse";
  prodSearch: Array<ProductSearchKourierResult>;
};

export type ProductSearchKourierResult = {
  __typename?: "ProductSearchKourierResult";
  errorCode?: Maybe<Scalars["String"]>;
  errorMessage?: Maybe<Scalars["String"]>;
  productIdCount?: Maybe<Scalars["Int"]>;
  products?: Maybe<Array<Maybe<KourierProduct>>>;
};

export type ProductSearchResult = {
  __typename?: "ProductSearchResult";
  pagination: Pagination;
  products: Array<Maybe<Product>>;
};

export type ProductSerialNumberResults = {
  __typename?: "ProductSerialNumberResults";
  results: Array<Maybe<ProductSerialNumbers>>;
};

export type ProductSerialNumbers = {
  __typename?: "ProductSerialNumbers";
  description?: Maybe<Scalars["String"]>;
  generationId: Scalars["String"];
  invoiceId: Scalars["String"];
  location?: Maybe<Scalars["String"]>;
  nonStockSerialNumbers: Array<Maybe<SerialList>>;
  orderId: Scalars["String"];
  productId: Scalars["String"];
  quantity: Scalars["Int"];
  serialList: Array<Maybe<SerialList>>;
  warehouseId: Scalars["String"];
};

export type Query = {
  __typename?: "Query";
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
  priceLines: Array<Scalars["String"]>;
  productDetails: ProductDetails;
  productImageUrl?: Maybe<Scalars["String"]>;
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
  branchId: Scalars["String"];
  id: Scalars["String"];
};

export type QueryCountStatusArgs = {
  id: Scalars["ID"];
};

export type QueryCountsArgs = {
  input: CountsInput;
};

export type QueryCustomerArgs = {
  customerId: Scalars["String"];
};

export type QueryGetCustomerSearchArgs = {
  input: CustomerSearchInput;
};

export type QueryGetProductSerialNumbersArgs = {
  warehouseId: Scalars["String"];
};

export type QueryLocationArgs = {
  id: Scalars["String"];
};

export type QueryNextLocationArgs = {
  id: Scalars["String"];
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
  productId: Scalars["String"];
};

export type QueryProductImageUrlArgs = {
  input: Scalars["String"];
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
  username: Scalars["String"];
};

export type QueryUserPicksArgs = {
  input: PickingOrderInput;
};

export type QueryValidateBranchArgs = {
  input: ValidateBranchInput;
};

export type QueryVarianceLocationArgs = {
  id: Scalars["String"];
};

export type QueryVarianceNextLocationArgs = {
  id: Scalars["String"];
};

export type QueryWriteInArgs = {
  id: Scalars["ID"];
};

export type QueryWriteInsArgs = {
  options: WriteInsInput;
};

export type RemoveBranchCountsInput = {
  branchId: Scalars["String"];
  countId: Scalars["String"];
  createdAt?: InputMaybe<Scalars["String"]>;
};

export type RemoveCountsInput = {
  endDate: Scalars["String"];
  erpSystemName?: InputMaybe<ErpSystem>;
};

export type SerialLineInput = {
  line: Scalars["Int"];
  serial: Scalars["String"];
};

export type SerialList = {
  __typename?: "SerialList";
  line: Scalars["Int"];
  serial: Scalars["String"];
};

export type ShipToId = {
  __typename?: "ShipToId";
  shipToId?: Maybe<Scalars["String"]>;
};

export type ShippingDetailsKourierInput = {
  invoiceNumber: Scalars["String"];
};

export type ShippingDetailsKourierResponse = {
  __typename?: "ShippingDetailsKourierResponse";
  shippingtext: Array<ShippingDetailsKourierResult>;
};

export type ShippingDetailsKourierResult = {
  __typename?: "ShippingDetailsKourierResult";
  errorCode?: Maybe<Scalars["String"]>;
  errorMessage?: Maybe<Scalars["String"]>;
  invoiceNumber?: Maybe<Scalars["String"]>;
  noBackorder: Scalars["Boolean"];
  noSort: Scalars["Boolean"];
  shippingInstructions?: Maybe<Array<Scalars["String"]>>;
  status?: Maybe<Scalars["String"]>;
};

export type SpecialPrice = {
  __typename?: "SpecialPrice";
  branch: Scalars["String"];
  customerDisplayName: Scalars["String"];
  customerId: Scalars["String"];
  customerSalesQuantity: Scalars["Int"];
  displayName: Scalars["String"];
  imageUrl?: Maybe<Scalars["String"]>;
  manufacturer: Scalars["String"];
  manufacturerReferenceNumber: Scalars["String"];
  priceLine: Scalars["String"];
  prices: Array<Price>;
  productId: Scalars["String"];
  salesperson: Scalars["String"];
  territory?: Maybe<Scalars["String"]>;
};

export type SpecialPriceInput = {
  customerId?: InputMaybe<Scalars["String"]>;
  priceLine?: InputMaybe<Scalars["String"]>;
  productId?: InputMaybe<Scalars["String"]>;
};

export type SpecialPriceMeta = {
  __typename?: "SpecialPriceMeta";
  customerId?: Maybe<Scalars["String"]>;
  nextPage?: Maybe<Scalars["Int"]>;
  orderBy: Scalars["String"];
  orderDirection: Scalars["String"];
  page: Scalars["Int"];
  pageCount: Scalars["Int"];
  pageSize: Scalars["Int"];
  priceLine?: Maybe<Scalars["String"]>;
  productId?: Maybe<Scalars["String"]>;
  resultCount: Scalars["Int"];
};

export type SpecialPriceResponse = {
  __typename?: "SpecialPriceResponse";
  meta: SpecialPriceMeta;
  results: Array<SpecialPrice>;
};

export type SplitQuantityInput = {
  pickedItemsCount: Scalars["Int"];
  product: CompletePickInput;
  serialNumbers?: InputMaybe<Array<InputMaybe<SerialLineInput>>>;
};

export type SplitQuantityResponse = {
  __typename?: "SplitQuantityResponse";
  errorMessage?: Maybe<Scalars["String"]>;
  invalidSerialNums?: Maybe<Array<Maybe<SerialList>>>;
  isSplit: Scalars["Boolean"];
  orderId: Scalars["String"];
  productId: Scalars["String"];
  successStatus: Scalars["Boolean"];
};

export type StagePickTaskInput = {
  branchId: Scalars["String"];
  invoiceId: Scalars["String"];
  location?: InputMaybe<Scalars["String"]>;
  orderId: Scalars["String"];
  tote: Scalars["String"];
};

export type StagePickTaskResponse = {
  __typename?: "StagePickTaskResponse";
  message: Scalars["String"];
  success: Scalars["Boolean"];
};

export type StagePickTotePackagesInput = {
  branchId: Scalars["String"];
  invoiceId: Scalars["String"];
  orderId: Scalars["String"];
  packageList: Array<InputMaybe<PackageInput>>;
  tote: Scalars["String"];
};

export type StagePickTotePackagesResponse = {
  __typename?: "StagePickTotePackagesResponse";
  message: Scalars["String"];
  success: Scalars["Boolean"];
};

export type Stock = {
  __typename?: "Stock";
  homeBranch?: Maybe<StoreStock>;
  otherBranches?: Maybe<Array<Maybe<StoreStock>>>;
};

export type StoreStock = {
  __typename?: "StoreStock";
  address?: Maybe<Scalars["String"]>;
  availability?: Maybe<Scalars["Int"]>;
  branchName?: Maybe<Scalars["String"]>;
};

export type SuccessfulCreate = {
  __typename?: "SuccessfulCreate";
  uploadedName: Scalars["String"];
  uploadedPath: Scalars["String"];
};

export type SuccessfulUpload = {
  __typename?: "SuccessfulUpload";
  uploadedName: Scalars["String"];
  uploadedPath: Scalars["String"];
};

export type TechDoc = {
  __typename?: "TechDoc";
  name?: Maybe<Scalars["String"]>;
  url?: Maybe<Scalars["String"]>;
};

export type TechSpec = {
  __typename?: "TechSpec";
  name?: Maybe<Scalars["String"]>;
  value?: Maybe<Scalars["String"]>;
};

export type TypeaheadInput = {
  entity: Scalars["String"];
  query: Scalars["String"];
};

export type TypeaheadMeta = {
  __typename?: "TypeaheadMeta";
  entity: Scalars["String"];
  nextPage?: Maybe<Scalars["Int"]>;
  orderBy: Scalars["String"];
  orderDirection: Scalars["String"];
  page: Scalars["Int"];
  pageCount: Scalars["Int"];
  pageSize: Scalars["Int"];
  query: Scalars["String"];
  resultCount: Scalars["Int"];
};

export type TypeaheadResponse = {
  __typename?: "TypeaheadResponse";
  meta: TypeaheadMeta;
  results: Array<TypeaheadResult>;
};

export type TypeaheadResult = {
  __typename?: "TypeaheadResult";
  displayName: Scalars["String"];
  id: Scalars["String"];
};

export type UpdateProductSerialNumbersInput = {
  branchId: Scalars["String"];
  ignoreStockCheck: Scalars["Boolean"];
  serialNumberList: Array<InputMaybe<SerialLineInput>>;
  warehouseId: Scalars["String"];
};

export type UploadSpecialPriceResponse = {
  __typename?: "UploadSpecialPriceResponse";
  failedCreateSuggestions?: Maybe<Array<FailedPriceSuggestion>>;
  failedUpdateSuggestions?: Maybe<Array<FailedPriceSuggestion>>;
  successfulCreates?: Maybe<Array<SuccessfulCreate>>;
  successfulUploads?: Maybe<Array<SuccessfulUpload>>;
};

export type UserBranchResponse = {
  __typename?: "UserBranchResponse";
  error?: Maybe<ErrorResponse>;
  success: Scalars["Boolean"];
};

export type UserPick = {
  __typename?: "UserPick";
  branchId: Scalars["String"];
  cutDetail?: Maybe<Scalars["String"]>;
  cutGroup?: Maybe<Scalars["String"]>;
  description: Scalars["String"];
  generationId?: Maybe<Scalars["Int"]>;
  isLot?: Maybe<Scalars["String"]>;
  isParallelCut?: Maybe<Scalars["Boolean"]>;
  isSerial: Scalars["Boolean"];
  lineId: Scalars["Int"];
  location: Scalars["String"];
  locationType: Scalars["String"];
  lot: Scalars["String"];
  orderId?: Maybe<Scalars["String"]>;
  pickGroup?: Maybe<Scalars["String"]>;
  productId: Scalars["String"];
  productImageUrl?: Maybe<Scalars["String"]>;
  quantity: Scalars["Int"];
  shipVia: Scalars["String"];
  splitId?: Maybe<Scalars["String"]>;
  tote?: Maybe<Scalars["String"]>;
  uom: Scalars["String"];
  userId: Scalars["String"];
  warehouseID: Scalars["String"];
};

export type UserResponse = {
  __typename?: "UserResponse";
  branch?: Maybe<Array<Maybe<Scalars["String"]>>>;
  username: Scalars["String"];
};

export type ValidateBranchInput = {
  branchId: Scalars["String"];
};

export type ValidateBranchResponse = {
  __typename?: "ValidateBranchResponse";
  branch?: Maybe<BranchResponse>;
  isValid: Scalars["Boolean"];
};

export type VarianceDetails = {
  __typename?: "VarianceDetails";
  countQty?: Maybe<Scalars["Int"]>;
  countedCost?: Maybe<Scalars["Float"]>;
  erpProductID: Scalars["String"];
  location: Scalars["String"];
  notCountedFlag?: Maybe<Scalars["Boolean"]>;
  onHandCost?: Maybe<Scalars["Float"]>;
  onHandQty?: Maybe<Scalars["Int"]>;
  percentDeviance?: Maybe<Scalars["Float"]>;
  productDescription: Scalars["String"];
  qtyDeviance?: Maybe<Scalars["Int"]>;
  recount1Qty?: Maybe<Scalars["Int"]>;
  recount2Qty?: Maybe<Scalars["Int"]>;
  recount3Qty?: Maybe<Scalars["Int"]>;
};

export type VarianceDetailsResponse = {
  __typename?: "VarianceDetailsResponse";
  message: Scalars["String"];
  success: Scalars["Boolean"];
};

export enum VarianceItemStatus {
  Committed = "COMMITTED",
  Nonvariance = "NONVARIANCE",
  Staged = "STAGED",
  Uncounted = "UNCOUNTED",
}

export type VarianceLocationItem = {
  __typename?: "VarianceLocationItem";
  catalogNum?: Maybe<Scalars["String"]>;
  id?: Maybe<Scalars["ID"]>;
  locationId?: Maybe<Scalars["String"]>;
  prodDesc?: Maybe<Scalars["String"]>;
  prodNum: Scalars["String"];
  productImageUrl?: Maybe<Scalars["String"]>;
  quantity?: Maybe<Scalars["Int"]>;
  sequence?: Maybe<Scalars["Int"]>;
  status?: Maybe<LocationItemStatus>;
  tagNum?: Maybe<Scalars["String"]>;
  uom?: Maybe<Scalars["String"]>;
  varianceCost?: Maybe<Scalars["Float"]>;
  varianceStatus?: Maybe<VarianceItemStatus>;
};

export type VarianceLocationResponse = {
  __typename?: "VarianceLocationResponse";
  committed: Scalars["Boolean"];
  grossVarianceCost: Scalars["Float"];
  id: Scalars["String"];
  items: Array<VarianceLocationItem>;
  netVarianceCost: Scalars["Float"];
  totalCounted: Scalars["Int"];
  totalProducts: Scalars["Int"];
};

export type VarianceLocationSummary = {
  __typename?: "VarianceLocationSummary";
  grossVarianceCost: Scalars["Float"];
  id: Scalars["String"];
  netVarianceCost: Scalars["Float"];
  totalProducts: Scalars["Int"];
};

export type VarianceLocationsResponse = {
  __typename?: "VarianceLocationsResponse";
  content: Array<VarianceLocationSummary>;
  totalLocations: Scalars["Int"];
};

export type VarianceNextLocationResponse = {
  __typename?: "VarianceNextLocationResponse";
  locationId: Scalars["String"];
};

export type VarianceSummaryResponse = {
  __typename?: "VarianceSummaryResponse";
  differencePercentage?: Maybe<Scalars["Float"]>;
  differenceQuantity?: Maybe<Scalars["Int"]>;
  grossTotalCost?: Maybe<Scalars["String"]>;
  locationQuantity?: Maybe<Scalars["Int"]>;
  netTotalCost?: Maybe<Scalars["String"]>;
  productQuantity?: Maybe<Scalars["Int"]>;
};

export type WriteIn = {
  __typename?: "WriteIn";
  catalogNum?: Maybe<Scalars["String"]>;
  comment?: Maybe<Scalars["String"]>;
  createdAt: Scalars["String"];
  createdBy: Scalars["String"];
  description: Scalars["String"];
  id: Scalars["ID"];
  locationId: Scalars["String"];
  quantity: Scalars["Int"];
  resolved: Scalars["Boolean"];
  uom: Scalars["String"];
  upcNum?: Maybe<Scalars["String"]>;
  updatedAt: Scalars["String"];
  updatedBy: Scalars["String"];
};

export type WriteInInput = {
  catalogNum?: InputMaybe<Scalars["String"]>;
  comment?: InputMaybe<Scalars["String"]>;
  description: Scalars["String"];
  locationId: Scalars["String"];
  quantity: Scalars["Int"];
  uom: Scalars["String"];
  upcNum?: InputMaybe<Scalars["String"]>;
};

export type WriteInMutationResponse = {
  __typename?: "WriteInMutationResponse";
  content: WriteIn;
  message?: Maybe<Scalars["String"]>;
  success: Scalars["Boolean"];
};

export type WriteInsInput = {
  page: Scalars["Int"];
  size: Scalars["Int"];
  sort?: InputMaybe<WriteInsSort>;
};

export type WriteInsResponse = {
  __typename?: "WriteInsResponse";
  content: Array<WriteIn>;
  empty: Scalars["Boolean"];
  first: Scalars["Boolean"];
  last: Scalars["Boolean"];
  number: Scalars["Int"];
  numberOfElements: Scalars["Int"];
  pageable: Pageable;
  size: Scalars["Int"];
  sort: PageableSort;
  totalElements: Scalars["Int"];
  totalPages: Scalars["Int"];
};

export type WriteInsSort = {
  direction: Scalars["String"];
  property: Scalars["String"];
};

export type CloseOrderInput = {
  orderId: Scalars["String"];
  pickerId: Scalars["String"];
};

export type CloseOrderResponse = {
  __typename?: "closeOrderResponse";
  errorCode?: Maybe<Scalars["String"]>;
  errorMessage?: Maybe<Scalars["String"]>;
  moreToPick: Scalars["Boolean"];
  orderId: Scalars["String"];
  orderLocked: Scalars["Boolean"];
  pickerId: Scalars["String"];
  status: Scalars["Boolean"];
  stillPicking: Scalars["Boolean"];
};

export type PurgeMincronCountsMutationVariables = Exact<{
  input: CountsInput;
}>;

export type PurgeMincronCountsMutation = { __typename?: "Mutation"; purgeMincronCounts: string };

export type AddUserMutationVariables = Exact<{
  userEmail: Scalars["String"];
  branchId: Scalars["String"];
}>;

export type AddUserMutation = {
  __typename?: "Mutation";
  addUser: {
    __typename?: "UserBranchResponse";
    success: boolean;
    error?: {
      __typename?: "ErrorResponse";
      code: string;
      message: string;
      details?: string | null;
    } | null;
  };
};

export type RemoveUserBranchMutationVariables = Exact<{
  userEmail: Scalars["String"];
  branchId: Scalars["String"];
}>;

export type RemoveUserBranchMutation = {
  __typename?: "Mutation";
  removeUserBranch: {
    __typename?: "UserBranchResponse";
    success: boolean;
    error?: {
      __typename?: "ErrorResponse";
      code: string;
      message: string;
      details?: string | null;
    } | null;
  };
};

export type UpdateUserEmailMutationVariables = Exact<{
  oldUserEmail: Scalars["String"];
  newUserEmail: Scalars["String"];
}>;

export type UpdateUserEmailMutation = {
  __typename?: "Mutation";
  updateUserEmail: {
    __typename?: "UserBranchResponse";
    success: boolean;
    error?: {
      __typename?: "ErrorResponse";
      code: string;
      message: string;
      details?: string | null;
    } | null;
  };
};

export type DeleteUserMutationVariables = Exact<{
  userEmail: Scalars["String"];
}>;

export type DeleteUserMutation = {
  __typename?: "Mutation";
  deleteUser: {
    __typename?: "UserBranchResponse";
    success: boolean;
    error?: {
      __typename?: "ErrorResponse";
      code: string;
      message: string;
      details?: string | null;
    } | null;
  };
};

export type SetSpecialPricesMutationVariables = Exact<{
  input: PriceChangeInput;
}>;

export type SetSpecialPricesMutation = {
  __typename?: "Mutation";
  specialPrices?: {
    __typename?: "UploadSpecialPriceResponse";
    successfulUploads?: Array<{
      __typename?: "SuccessfulUpload";
      uploadedPath: string;
      uploadedName: string;
    }> | null;
  } | null;
};

export type LoadCountMutationVariables = Exact<{
  branchId: Scalars["String"];
  countId: Scalars["String"];
}>;

export type LoadCountMutation = {
  __typename?: "Mutation";
  loadCount: {
    __typename?: "CountWithStatus";
    id?: string | null;
    branchId: string;
    countId: string;
    branchName?: string | null;
    status?: CountStatus | null;
    errorMessage?: string | null;
    createdAt?: string | null;
    totalProducts?: number | null;
  };
};

export type RemoveBranchCountsMutationVariables = Exact<{
  input: RemoveBranchCountsInput;
}>;

export type RemoveBranchCountsMutation = {
  __typename?: "Mutation";
  removeBranchCounts: {
    __typename?: "DeleteMultipleCountResponse";
    deletedCounts: Array<{
      __typename?: "DeleteCountResponse";
      countLocations: number;
      countLocationItems: number;
      varianceCountLocationItemQuantities: number;
      countLocationItemQuantities: number;
      writeIns: number;
    }>;
  };
};

export type GetCountsQueryVariables = Exact<{
  input: CountsInput;
}>;

export type GetCountsQuery = {
  __typename?: "Query";
  counts: Array<{
    __typename?: "CountWithStatus";
    branchId: string;
    branchName?: string | null;
    countId: string;
    status?: CountStatus | null;
    createdAt?: string | null;
    totalProducts?: number | null;
    errorMessage?: string | null;
  }>;
};

export type GetCountStatusQueryVariables = Exact<{
  id: Scalars["ID"];
}>;

export type GetCountStatusQuery = {
  __typename?: "Query";
  countStatus: {
    __typename?: "CountWithStatus";
    id?: string | null;
    branchId: string;
    branchName?: string | null;
    countId: string;
    status?: CountStatus | null;
    createdAt?: string | null;
    totalProducts?: number | null;
    errorMessage?: string | null;
  };
};

export type GetPercentageTotalQueryVariables = Exact<{
  input: MetricsSingleRangeInput;
}>;

export type GetPercentageTotalQuery = {
  __typename?: "Query";
  percentageTotal: {
    __typename?: "MetricsPercentageTotal";
    response?: Array<{
      __typename?: "MetricsPercentageTotalDivision";
      division: string;
      userCount?: number | null;
      userPercentage?: number | null;
      loginCount?: number | null;
      loginPercentage?: number | null;
      branchCount?: number | null;
      branchPercentage?: number | null;
    } | null> | null;
  };
};

export type GetPercentageChangeQueryVariables = Exact<{
  input: MetricsDoubleRangeInput;
}>;

export type GetPercentageChangeQuery = {
  __typename?: "Query";
  percentageChange: {
    __typename?: "MetricsPercentageChange";
    response?: Array<{
      __typename?: "MetricsPercentageChangeDivision";
      division: string;
      userChange?: string | null;
      loginChange?: string | null;
      branchChange?: string | null;
    } | null> | null;
  };
};

export type GetTotalUsageQueryVariables = Exact<{
  input: MetricsSingleRangeInput;
}>;

export type GetTotalUsageQuery = {
  __typename?: "Query";
  totalUsage: {
    __typename?: "MetricsUsage";
    response?: Array<{
      __typename?: "MetricsDivision";
      division: string;
      userCount?: number | null;
      loginCount?: number | null;
      branchCount?: number | null;
      branches?: Array<{
        __typename?: "MetricsBranch";
        id?: string | null;
        city?: string | null;
        state?: string | null;
        userCount?: number | null;
        loginCount?: number | null;
      } | null> | null;
    } | null> | null;
  };
};

export type GetTotalOverviewQueryVariables = Exact<{
  input: MetricsDoubleRangeInput;
}>;

export type GetTotalOverviewQuery = {
  __typename?: "Query";
  totalOverview: {
    __typename?: "MetricsOverview";
    response?: Array<{
      __typename?: "MetricsChange";
      metric: string;
      quantity?: string | null;
      percentageChange?: string | null;
    } | null> | null;
  };
};

export type GetPaginatedSpecialPricesQueryVariables = Exact<{
  input: SpecialPriceInput;
  pagingContext: PagingContext;
}>;

export type GetPaginatedSpecialPricesQuery = {
  __typename?: "Query";
  paginatedSpecialPrices: {
    __typename?: "SpecialPriceResponse";
    meta: {
      __typename?: "SpecialPriceMeta";
      page: number;
      nextPage?: number | null;
      pageCount: number;
      resultCount: number;
      pageSize: number;
      orderBy: string;
      orderDirection: string;
      productId?: string | null;
      customerId?: string | null;
    };
    results: Array<{
      __typename?: "SpecialPrice";
      productId: string;
      customerId: string;
      branch: string;
      displayName: string;
      customerDisplayName: string;
      imageUrl?: string | null;
      manufacturer: string;
      manufacturerReferenceNumber: string;
      salesperson: string;
      priceLine: string;
      customerSalesQuantity: number;
      territory?: string | null;
      prices: Array<{
        __typename?: "Price";
        type: string;
        value: number;
        currency: string;
        displayName: string;
      }>;
    }>;
  };
};

export type GetPriceLinesQueryVariables = Exact<{
  input: SpecialPriceInput;
}>;

export type GetPriceLinesQuery = { __typename?: "Query"; priceLines: Array<string> };

export type SearchProductsEclipseQueryVariables = Exact<{
  input: ProductSearchEclipseInput;
}>;

export type SearchProductsEclipseQuery = {
  __typename?: "Query";
  searchProductsEclipse: {
    __typename?: "ProductSearchResult";
    pagination: { __typename?: "Pagination"; currentPage: number; totalItemCount: number };
    products: Array<{
      __typename?: "Product";
      manufacturerName?: string | null;
      id?: string | null;
      name?: string | null;
      upc?: string | null;
      productNumber?: string | null;
      imageUrls?: { __typename?: "ImageUrls"; thumb?: string | null } | null;
    } | null>;
  };
};

export type GetProductPricesQueryVariables = Exact<{
  input: ProductPriceInput;
}>;

export type GetProductPricesQuery = {
  __typename?: "Query";
  productPrices: {
    __typename?: "ProductPriceResponse";
    productId?: string | null;
    erpBranchNum?: string | null;
    cmp?: number | null;
    stdCost?: number | null;
    uom?: string | null;
    rateCardPrice?: number | null;
    rateCardName?: string | null;
    matchedBranch?: string | null;
    matchedClass?: string | null;
    matchedGroup?: string | null;
    matrixId?: string | null;
    customerId?: string | null;
    correlationId?: string | null;
  };
};

export type GetCustomerByIdQueryVariables = Exact<{
  input: Scalars["String"];
}>;

export type GetCustomerByIdQuery = {
  __typename?: "Query";
  customer: {
    __typename?: "CustomerResponse";
    id: string;
    homeBranch: string;
    name: string;
    isBillTo?: boolean | null;
    isShipTo?: boolean | null;
    addressLine1?: string | null;
    addressLine2?: string | null;
    city?: string | null;
    state?: string | null;
    postalCode?: string | null;
    countryCode?: string | null;
  };
};

export type SearchProductsKourierQueryVariables = Exact<{
  input: ProductSearchKourierInput;
}>;

export type SearchProductsKourierQuery = {
  __typename?: "Query";
  searchProductsKourier: {
    __typename?: "ProductSearchKourierResponse";
    prodSearch: Array<{
      __typename?: "ProductSearchKourierResult";
      errorCode?: string | null;
      errorMessage?: string | null;
      productIdCount?: number | null;
      products?: Array<{
        __typename?: "KourierProduct";
        productId?: string | null;
        displayField?: string | null;
      } | null> | null;
    }>;
  };
};

export type BranchUsersListQueryVariables = Exact<{ [key: string]: never }>;

export type BranchUsersListQuery = {
  __typename?: "Query";
  branchUsersList: Array<{
    __typename?: "BranchUsersListResponse";
    branchId: string;
    assignedUsers?: Array<string | null> | null;
  } | null>;
};

export type GetUserBranchQueryVariables = Exact<{
  input: Scalars["String"];
}>;

export type GetUserBranchQuery = {
  __typename?: "Query";
  userBranch: {
    __typename?: "UserResponse";
    username: string;
    branch?: Array<string | null> | null;
  };
};

export const PurgeMincronCountsDocument = gql`
  mutation purgeMincronCounts($input: CountsInput!) {
    purgeMincronCounts(input: $input)
  }
`;
export type PurgeMincronCountsMutationFn = Apollo.MutationFunction<
  PurgeMincronCountsMutation,
  PurgeMincronCountsMutationVariables
>;

/**
 * __usePurgeMincronCountsMutation__
 *
 * To run a mutation, you first call `usePurgeMincronCountsMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `usePurgeMincronCountsMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [purgeMincronCountsMutation, { data, loading, error }] = usePurgeMincronCountsMutation({
 *   variables: {
 *      input: // value for 'input'
 *   },
 * });
 */
export function usePurgeMincronCountsMutation(
  baseOptions?: Apollo.MutationHookOptions<
    PurgeMincronCountsMutation,
    PurgeMincronCountsMutationVariables
  >
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useMutation<PurgeMincronCountsMutation, PurgeMincronCountsMutationVariables>(
    PurgeMincronCountsDocument,
    options
  );
}
export type PurgeMincronCountsMutationHookResult = ReturnType<typeof usePurgeMincronCountsMutation>;
export type PurgeMincronCountsMutationResult = Apollo.MutationResult<PurgeMincronCountsMutation>;
export type PurgeMincronCountsMutationOptions = Apollo.BaseMutationOptions<
  PurgeMincronCountsMutation,
  PurgeMincronCountsMutationVariables
>;
export const AddUserDocument = gql`
  mutation addUser($userEmail: String!, $branchId: String!) {
    addUser(userEmail: $userEmail, branchId: $branchId) {
      success
      error {
        code
        message
        details
      }
    }
  }
`;
export type AddUserMutationFn = Apollo.MutationFunction<AddUserMutation, AddUserMutationVariables>;

/**
 * __useAddUserMutation__
 *
 * To run a mutation, you first call `useAddUserMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useAddUserMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [addUserMutation, { data, loading, error }] = useAddUserMutation({
 *   variables: {
 *      userEmail: // value for 'userEmail'
 *      branchId: // value for 'branchId'
 *   },
 * });
 */
export function useAddUserMutation(
  baseOptions?: Apollo.MutationHookOptions<AddUserMutation, AddUserMutationVariables>
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useMutation<AddUserMutation, AddUserMutationVariables>(AddUserDocument, options);
}
export type AddUserMutationHookResult = ReturnType<typeof useAddUserMutation>;
export type AddUserMutationResult = Apollo.MutationResult<AddUserMutation>;
export type AddUserMutationOptions = Apollo.BaseMutationOptions<
  AddUserMutation,
  AddUserMutationVariables
>;
export const RemoveUserBranchDocument = gql`
  mutation removeUserBranch($userEmail: String!, $branchId: String!) {
    removeUserBranch(userEmail: $userEmail, branchId: $branchId) {
      success
      error {
        code
        message
        details
      }
    }
  }
`;
export type RemoveUserBranchMutationFn = Apollo.MutationFunction<
  RemoveUserBranchMutation,
  RemoveUserBranchMutationVariables
>;

/**
 * __useRemoveUserBranchMutation__
 *
 * To run a mutation, you first call `useRemoveUserBranchMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useRemoveUserBranchMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [removeUserBranchMutation, { data, loading, error }] = useRemoveUserBranchMutation({
 *   variables: {
 *      userEmail: // value for 'userEmail'
 *      branchId: // value for 'branchId'
 *   },
 * });
 */
export function useRemoveUserBranchMutation(
  baseOptions?: Apollo.MutationHookOptions<
    RemoveUserBranchMutation,
    RemoveUserBranchMutationVariables
  >
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useMutation<RemoveUserBranchMutation, RemoveUserBranchMutationVariables>(
    RemoveUserBranchDocument,
    options
  );
}
export type RemoveUserBranchMutationHookResult = ReturnType<typeof useRemoveUserBranchMutation>;
export type RemoveUserBranchMutationResult = Apollo.MutationResult<RemoveUserBranchMutation>;
export type RemoveUserBranchMutationOptions = Apollo.BaseMutationOptions<
  RemoveUserBranchMutation,
  RemoveUserBranchMutationVariables
>;
export const UpdateUserEmailDocument = gql`
  mutation updateUserEmail($oldUserEmail: String!, $newUserEmail: String!) {
    updateUserEmail(oldUserEmail: $oldUserEmail, newUserEmail: $newUserEmail) {
      success
      error {
        code
        message
        details
      }
    }
  }
`;
export type UpdateUserEmailMutationFn = Apollo.MutationFunction<
  UpdateUserEmailMutation,
  UpdateUserEmailMutationVariables
>;

/**
 * __useUpdateUserEmailMutation__
 *
 * To run a mutation, you first call `useUpdateUserEmailMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useUpdateUserEmailMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [updateUserEmailMutation, { data, loading, error }] = useUpdateUserEmailMutation({
 *   variables: {
 *      oldUserEmail: // value for 'oldUserEmail'
 *      newUserEmail: // value for 'newUserEmail'
 *   },
 * });
 */
export function useUpdateUserEmailMutation(
  baseOptions?: Apollo.MutationHookOptions<
    UpdateUserEmailMutation,
    UpdateUserEmailMutationVariables
  >
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useMutation<UpdateUserEmailMutation, UpdateUserEmailMutationVariables>(
    UpdateUserEmailDocument,
    options
  );
}
export type UpdateUserEmailMutationHookResult = ReturnType<typeof useUpdateUserEmailMutation>;
export type UpdateUserEmailMutationResult = Apollo.MutationResult<UpdateUserEmailMutation>;
export type UpdateUserEmailMutationOptions = Apollo.BaseMutationOptions<
  UpdateUserEmailMutation,
  UpdateUserEmailMutationVariables
>;
export const DeleteUserDocument = gql`
  mutation deleteUser($userEmail: String!) {
    deleteUser(userEmail: $userEmail) {
      success
      error {
        code
        message
        details
      }
    }
  }
`;
export type DeleteUserMutationFn = Apollo.MutationFunction<
  DeleteUserMutation,
  DeleteUserMutationVariables
>;

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
 *      userEmail: // value for 'userEmail'
 *   },
 * });
 */
export function useDeleteUserMutation(
  baseOptions?: Apollo.MutationHookOptions<DeleteUserMutation, DeleteUserMutationVariables>
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useMutation<DeleteUserMutation, DeleteUserMutationVariables>(
    DeleteUserDocument,
    options
  );
}
export type DeleteUserMutationHookResult = ReturnType<typeof useDeleteUserMutation>;
export type DeleteUserMutationResult = Apollo.MutationResult<DeleteUserMutation>;
export type DeleteUserMutationOptions = Apollo.BaseMutationOptions<
  DeleteUserMutation,
  DeleteUserMutationVariables
>;
export const SetSpecialPricesDocument = gql`
  mutation SetSpecialPrices($input: PriceChangeInput!) {
    specialPrices(input: $input) {
      successfulUploads {
        uploadedPath
        uploadedName
      }
    }
  }
`;
export type SetSpecialPricesMutationFn = Apollo.MutationFunction<
  SetSpecialPricesMutation,
  SetSpecialPricesMutationVariables
>;

/**
 * __useSetSpecialPricesMutation__
 *
 * To run a mutation, you first call `useSetSpecialPricesMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useSetSpecialPricesMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [setSpecialPricesMutation, { data, loading, error }] = useSetSpecialPricesMutation({
 *   variables: {
 *      input: // value for 'input'
 *   },
 * });
 */
export function useSetSpecialPricesMutation(
  baseOptions?: Apollo.MutationHookOptions<
    SetSpecialPricesMutation,
    SetSpecialPricesMutationVariables
  >
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useMutation<SetSpecialPricesMutation, SetSpecialPricesMutationVariables>(
    SetSpecialPricesDocument,
    options
  );
}
export type SetSpecialPricesMutationHookResult = ReturnType<typeof useSetSpecialPricesMutation>;
export type SetSpecialPricesMutationResult = Apollo.MutationResult<SetSpecialPricesMutation>;
export type SetSpecialPricesMutationOptions = Apollo.BaseMutationOptions<
  SetSpecialPricesMutation,
  SetSpecialPricesMutationVariables
>;
export const LoadCountDocument = gql`
  mutation LoadCount($branchId: String!, $countId: String!) {
    loadCount(branchId: $branchId, countId: $countId) {
      id
      branchId
      countId
      branchName
      status
      errorMessage
      createdAt
      totalProducts
    }
  }
`;
export type LoadCountMutationFn = Apollo.MutationFunction<
  LoadCountMutation,
  LoadCountMutationVariables
>;

/**
 * __useLoadCountMutation__
 *
 * To run a mutation, you first call `useLoadCountMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useLoadCountMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [loadCountMutation, { data, loading, error }] = useLoadCountMutation({
 *   variables: {
 *      branchId: // value for 'branchId'
 *      countId: // value for 'countId'
 *   },
 * });
 */
export function useLoadCountMutation(
  baseOptions?: Apollo.MutationHookOptions<LoadCountMutation, LoadCountMutationVariables>
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useMutation<LoadCountMutation, LoadCountMutationVariables>(
    LoadCountDocument,
    options
  );
}
export type LoadCountMutationHookResult = ReturnType<typeof useLoadCountMutation>;
export type LoadCountMutationResult = Apollo.MutationResult<LoadCountMutation>;
export type LoadCountMutationOptions = Apollo.BaseMutationOptions<
  LoadCountMutation,
  LoadCountMutationVariables
>;
export const RemoveBranchCountsDocument = gql`
  mutation removeBranchCounts($input: RemoveBranchCountsInput!) {
    removeBranchCounts(input: $input) {
      deletedCounts {
        countLocations
        countLocationItems
        varianceCountLocationItemQuantities
        countLocationItemQuantities
        writeIns
      }
    }
  }
`;
export type RemoveBranchCountsMutationFn = Apollo.MutationFunction<
  RemoveBranchCountsMutation,
  RemoveBranchCountsMutationVariables
>;

/**
 * __useRemoveBranchCountsMutation__
 *
 * To run a mutation, you first call `useRemoveBranchCountsMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useRemoveBranchCountsMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [removeBranchCountsMutation, { data, loading, error }] = useRemoveBranchCountsMutation({
 *   variables: {
 *      input: // value for 'input'
 *   },
 * });
 */
export function useRemoveBranchCountsMutation(
  baseOptions?: Apollo.MutationHookOptions<
    RemoveBranchCountsMutation,
    RemoveBranchCountsMutationVariables
  >
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useMutation<RemoveBranchCountsMutation, RemoveBranchCountsMutationVariables>(
    RemoveBranchCountsDocument,
    options
  );
}
export type RemoveBranchCountsMutationHookResult = ReturnType<typeof useRemoveBranchCountsMutation>;
export type RemoveBranchCountsMutationResult = Apollo.MutationResult<RemoveBranchCountsMutation>;
export type RemoveBranchCountsMutationOptions = Apollo.BaseMutationOptions<
  RemoveBranchCountsMutation,
  RemoveBranchCountsMutationVariables
>;
export const GetCountsDocument = gql`
  query GetCounts($input: CountsInput!) {
    counts(input: $input) {
      branchId
      branchName
      countId
      status
      createdAt
      totalProducts
      errorMessage
    }
  }
`;

/**
 * __useGetCountsQuery__
 *
 * To run a query within a React component, call `useGetCountsQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetCountsQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetCountsQuery({
 *   variables: {
 *      input: // value for 'input'
 *   },
 * });
 */
export function useGetCountsQuery(
  baseOptions: Apollo.QueryHookOptions<GetCountsQuery, GetCountsQueryVariables>
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useQuery<GetCountsQuery, GetCountsQueryVariables>(GetCountsDocument, options);
}
export function useGetCountsLazyQuery(
  baseOptions?: Apollo.LazyQueryHookOptions<GetCountsQuery, GetCountsQueryVariables>
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useLazyQuery<GetCountsQuery, GetCountsQueryVariables>(GetCountsDocument, options);
}
export type GetCountsQueryHookResult = ReturnType<typeof useGetCountsQuery>;
export type GetCountsLazyQueryHookResult = ReturnType<typeof useGetCountsLazyQuery>;
export type GetCountsQueryResult = Apollo.QueryResult<GetCountsQuery, GetCountsQueryVariables>;
export const GetCountStatusDocument = gql`
  query GetCountStatus($id: ID!) {
    countStatus(id: $id) {
      id
      branchId
      branchName
      countId
      status
      createdAt
      totalProducts
      errorMessage
    }
  }
`;

/**
 * __useGetCountStatusQuery__
 *
 * To run a query within a React component, call `useGetCountStatusQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetCountStatusQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetCountStatusQuery({
 *   variables: {
 *      id: // value for 'id'
 *   },
 * });
 */
export function useGetCountStatusQuery(
  baseOptions: Apollo.QueryHookOptions<GetCountStatusQuery, GetCountStatusQueryVariables>
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useQuery<GetCountStatusQuery, GetCountStatusQueryVariables>(
    GetCountStatusDocument,
    options
  );
}
export function useGetCountStatusLazyQuery(
  baseOptions?: Apollo.LazyQueryHookOptions<GetCountStatusQuery, GetCountStatusQueryVariables>
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useLazyQuery<GetCountStatusQuery, GetCountStatusQueryVariables>(
    GetCountStatusDocument,
    options
  );
}
export type GetCountStatusQueryHookResult = ReturnType<typeof useGetCountStatusQuery>;
export type GetCountStatusLazyQueryHookResult = ReturnType<typeof useGetCountStatusLazyQuery>;
export type GetCountStatusQueryResult = Apollo.QueryResult<
  GetCountStatusQuery,
  GetCountStatusQueryVariables
>;
export const GetPercentageTotalDocument = gql`
  query GetPercentageTotal($input: MetricsSingleRangeInput!) {
    percentageTotal(input: $input) {
      response {
        division
        userCount
        userPercentage
        loginCount
        loginPercentage
        branchCount
        branchPercentage
      }
    }
  }
`;

/**
 * __useGetPercentageTotalQuery__
 *
 * To run a query within a React component, call `useGetPercentageTotalQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetPercentageTotalQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetPercentageTotalQuery({
 *   variables: {
 *      input: // value for 'input'
 *   },
 * });
 */
export function useGetPercentageTotalQuery(
  baseOptions: Apollo.QueryHookOptions<GetPercentageTotalQuery, GetPercentageTotalQueryVariables>
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useQuery<GetPercentageTotalQuery, GetPercentageTotalQueryVariables>(
    GetPercentageTotalDocument,
    options
  );
}
export function useGetPercentageTotalLazyQuery(
  baseOptions?: Apollo.LazyQueryHookOptions<
    GetPercentageTotalQuery,
    GetPercentageTotalQueryVariables
  >
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useLazyQuery<GetPercentageTotalQuery, GetPercentageTotalQueryVariables>(
    GetPercentageTotalDocument,
    options
  );
}
export type GetPercentageTotalQueryHookResult = ReturnType<typeof useGetPercentageTotalQuery>;
export type GetPercentageTotalLazyQueryHookResult = ReturnType<
  typeof useGetPercentageTotalLazyQuery
>;
export type GetPercentageTotalQueryResult = Apollo.QueryResult<
  GetPercentageTotalQuery,
  GetPercentageTotalQueryVariables
>;
export const GetPercentageChangeDocument = gql`
  query GetPercentageChange($input: MetricsDoubleRangeInput!) {
    percentageChange(input: $input) {
      response {
        division
        userChange
        loginChange
        branchChange
      }
    }
  }
`;

/**
 * __useGetPercentageChangeQuery__
 *
 * To run a query within a React component, call `useGetPercentageChangeQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetPercentageChangeQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetPercentageChangeQuery({
 *   variables: {
 *      input: // value for 'input'
 *   },
 * });
 */
export function useGetPercentageChangeQuery(
  baseOptions: Apollo.QueryHookOptions<GetPercentageChangeQuery, GetPercentageChangeQueryVariables>
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useQuery<GetPercentageChangeQuery, GetPercentageChangeQueryVariables>(
    GetPercentageChangeDocument,
    options
  );
}
export function useGetPercentageChangeLazyQuery(
  baseOptions?: Apollo.LazyQueryHookOptions<
    GetPercentageChangeQuery,
    GetPercentageChangeQueryVariables
  >
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useLazyQuery<GetPercentageChangeQuery, GetPercentageChangeQueryVariables>(
    GetPercentageChangeDocument,
    options
  );
}
export type GetPercentageChangeQueryHookResult = ReturnType<typeof useGetPercentageChangeQuery>;
export type GetPercentageChangeLazyQueryHookResult = ReturnType<
  typeof useGetPercentageChangeLazyQuery
>;
export type GetPercentageChangeQueryResult = Apollo.QueryResult<
  GetPercentageChangeQuery,
  GetPercentageChangeQueryVariables
>;
export const GetTotalUsageDocument = gql`
  query GetTotalUsage($input: MetricsSingleRangeInput!) {
    totalUsage(input: $input) {
      response {
        division
        userCount
        loginCount
        branchCount
        branches {
          id
          city
          state
          userCount
          loginCount
        }
      }
    }
  }
`;

/**
 * __useGetTotalUsageQuery__
 *
 * To run a query within a React component, call `useGetTotalUsageQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetTotalUsageQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetTotalUsageQuery({
 *   variables: {
 *      input: // value for 'input'
 *   },
 * });
 */
export function useGetTotalUsageQuery(
  baseOptions: Apollo.QueryHookOptions<GetTotalUsageQuery, GetTotalUsageQueryVariables>
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useQuery<GetTotalUsageQuery, GetTotalUsageQueryVariables>(
    GetTotalUsageDocument,
    options
  );
}
export function useGetTotalUsageLazyQuery(
  baseOptions?: Apollo.LazyQueryHookOptions<GetTotalUsageQuery, GetTotalUsageQueryVariables>
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useLazyQuery<GetTotalUsageQuery, GetTotalUsageQueryVariables>(
    GetTotalUsageDocument,
    options
  );
}
export type GetTotalUsageQueryHookResult = ReturnType<typeof useGetTotalUsageQuery>;
export type GetTotalUsageLazyQueryHookResult = ReturnType<typeof useGetTotalUsageLazyQuery>;
export type GetTotalUsageQueryResult = Apollo.QueryResult<
  GetTotalUsageQuery,
  GetTotalUsageQueryVariables
>;
export const GetTotalOverviewDocument = gql`
  query GetTotalOverview($input: MetricsDoubleRangeInput!) {
    totalOverview(input: $input) {
      response {
        metric
        quantity
        percentageChange
      }
    }
  }
`;

/**
 * __useGetTotalOverviewQuery__
 *
 * To run a query within a React component, call `useGetTotalOverviewQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetTotalOverviewQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetTotalOverviewQuery({
 *   variables: {
 *      input: // value for 'input'
 *   },
 * });
 */
export function useGetTotalOverviewQuery(
  baseOptions: Apollo.QueryHookOptions<GetTotalOverviewQuery, GetTotalOverviewQueryVariables>
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useQuery<GetTotalOverviewQuery, GetTotalOverviewQueryVariables>(
    GetTotalOverviewDocument,
    options
  );
}
export function useGetTotalOverviewLazyQuery(
  baseOptions?: Apollo.LazyQueryHookOptions<GetTotalOverviewQuery, GetTotalOverviewQueryVariables>
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useLazyQuery<GetTotalOverviewQuery, GetTotalOverviewQueryVariables>(
    GetTotalOverviewDocument,
    options
  );
}
export type GetTotalOverviewQueryHookResult = ReturnType<typeof useGetTotalOverviewQuery>;
export type GetTotalOverviewLazyQueryHookResult = ReturnType<typeof useGetTotalOverviewLazyQuery>;
export type GetTotalOverviewQueryResult = Apollo.QueryResult<
  GetTotalOverviewQuery,
  GetTotalOverviewQueryVariables
>;
export const GetPaginatedSpecialPricesDocument = gql`
  query GetPaginatedSpecialPrices($input: SpecialPriceInput!, $pagingContext: PagingContext!) {
    paginatedSpecialPrices(input: $input, pagingContext: $pagingContext) {
      meta {
        page
        nextPage
        pageCount
        resultCount
        pageSize
        orderBy
        orderDirection
        productId
        customerId
      }
      results {
        productId
        customerId
        branch
        displayName
        customerDisplayName
        imageUrl
        manufacturer
        displayName
        manufacturerReferenceNumber
        salesperson
        priceLine
        customerSalesQuantity
        territory
        prices {
          type
          value
          currency
          displayName
        }
      }
    }
  }
`;

/**
 * __useGetPaginatedSpecialPricesQuery__
 *
 * To run a query within a React component, call `useGetPaginatedSpecialPricesQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetPaginatedSpecialPricesQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetPaginatedSpecialPricesQuery({
 *   variables: {
 *      input: // value for 'input'
 *      pagingContext: // value for 'pagingContext'
 *   },
 * });
 */
export function useGetPaginatedSpecialPricesQuery(
  baseOptions: Apollo.QueryHookOptions<
    GetPaginatedSpecialPricesQuery,
    GetPaginatedSpecialPricesQueryVariables
  >
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useQuery<GetPaginatedSpecialPricesQuery, GetPaginatedSpecialPricesQueryVariables>(
    GetPaginatedSpecialPricesDocument,
    options
  );
}
export function useGetPaginatedSpecialPricesLazyQuery(
  baseOptions?: Apollo.LazyQueryHookOptions<
    GetPaginatedSpecialPricesQuery,
    GetPaginatedSpecialPricesQueryVariables
  >
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useLazyQuery<
    GetPaginatedSpecialPricesQuery,
    GetPaginatedSpecialPricesQueryVariables
  >(GetPaginatedSpecialPricesDocument, options);
}
export type GetPaginatedSpecialPricesQueryHookResult = ReturnType<
  typeof useGetPaginatedSpecialPricesQuery
>;
export type GetPaginatedSpecialPricesLazyQueryHookResult = ReturnType<
  typeof useGetPaginatedSpecialPricesLazyQuery
>;
export type GetPaginatedSpecialPricesQueryResult = Apollo.QueryResult<
  GetPaginatedSpecialPricesQuery,
  GetPaginatedSpecialPricesQueryVariables
>;
export const GetPriceLinesDocument = gql`
  query GetPriceLines($input: SpecialPriceInput!) {
    priceLines(input: $input)
  }
`;

/**
 * __useGetPriceLinesQuery__
 *
 * To run a query within a React component, call `useGetPriceLinesQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetPriceLinesQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetPriceLinesQuery({
 *   variables: {
 *      input: // value for 'input'
 *   },
 * });
 */
export function useGetPriceLinesQuery(
  baseOptions: Apollo.QueryHookOptions<GetPriceLinesQuery, GetPriceLinesQueryVariables>
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useQuery<GetPriceLinesQuery, GetPriceLinesQueryVariables>(
    GetPriceLinesDocument,
    options
  );
}
export function useGetPriceLinesLazyQuery(
  baseOptions?: Apollo.LazyQueryHookOptions<GetPriceLinesQuery, GetPriceLinesQueryVariables>
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useLazyQuery<GetPriceLinesQuery, GetPriceLinesQueryVariables>(
    GetPriceLinesDocument,
    options
  );
}
export type GetPriceLinesQueryHookResult = ReturnType<typeof useGetPriceLinesQuery>;
export type GetPriceLinesLazyQueryHookResult = ReturnType<typeof useGetPriceLinesLazyQuery>;
export type GetPriceLinesQueryResult = Apollo.QueryResult<
  GetPriceLinesQuery,
  GetPriceLinesQueryVariables
>;
export const SearchProductsEclipseDocument = gql`
  query SearchProductsEclipse($input: ProductSearchEclipseInput!) {
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
export function useSearchProductsEclipseQuery(
  baseOptions: Apollo.QueryHookOptions<
    SearchProductsEclipseQuery,
    SearchProductsEclipseQueryVariables
  >
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useQuery<SearchProductsEclipseQuery, SearchProductsEclipseQueryVariables>(
    SearchProductsEclipseDocument,
    options
  );
}
export function useSearchProductsEclipseLazyQuery(
  baseOptions?: Apollo.LazyQueryHookOptions<
    SearchProductsEclipseQuery,
    SearchProductsEclipseQueryVariables
  >
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useLazyQuery<SearchProductsEclipseQuery, SearchProductsEclipseQueryVariables>(
    SearchProductsEclipseDocument,
    options
  );
}
export type SearchProductsEclipseQueryHookResult = ReturnType<typeof useSearchProductsEclipseQuery>;
export type SearchProductsEclipseLazyQueryHookResult = ReturnType<
  typeof useSearchProductsEclipseLazyQuery
>;
export type SearchProductsEclipseQueryResult = Apollo.QueryResult<
  SearchProductsEclipseQuery,
  SearchProductsEclipseQueryVariables
>;
export const GetProductPricesDocument = gql`
  query GetProductPrices($input: ProductPriceInput!) {
    productPrices(input: $input) {
      productId
      erpBranchNum
      cmp
      stdCost
      uom
      rateCardPrice
      rateCardName
      matchedBranch
      matchedClass
      matchedGroup
      matrixId
      customerId
      correlationId
    }
  }
`;

/**
 * __useGetProductPricesQuery__
 *
 * To run a query within a React component, call `useGetProductPricesQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetProductPricesQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetProductPricesQuery({
 *   variables: {
 *      input: // value for 'input'
 *   },
 * });
 */
export function useGetProductPricesQuery(
  baseOptions: Apollo.QueryHookOptions<GetProductPricesQuery, GetProductPricesQueryVariables>
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useQuery<GetProductPricesQuery, GetProductPricesQueryVariables>(
    GetProductPricesDocument,
    options
  );
}
export function useGetProductPricesLazyQuery(
  baseOptions?: Apollo.LazyQueryHookOptions<GetProductPricesQuery, GetProductPricesQueryVariables>
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useLazyQuery<GetProductPricesQuery, GetProductPricesQueryVariables>(
    GetProductPricesDocument,
    options
  );
}
export type GetProductPricesQueryHookResult = ReturnType<typeof useGetProductPricesQuery>;
export type GetProductPricesLazyQueryHookResult = ReturnType<typeof useGetProductPricesLazyQuery>;
export type GetProductPricesQueryResult = Apollo.QueryResult<
  GetProductPricesQuery,
  GetProductPricesQueryVariables
>;
export const GetCustomerByIdDocument = gql`
  query GetCustomerById($input: String!) {
    customer(customerId: $input) {
      id
      homeBranch
      name
      isBillTo
      isShipTo
      addressLine1
      addressLine2
      city
      state
      postalCode
      countryCode
    }
  }
`;

/**
 * __useGetCustomerByIdQuery__
 *
 * To run a query within a React component, call `useGetCustomerByIdQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetCustomerByIdQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetCustomerByIdQuery({
 *   variables: {
 *      input: // value for 'input'
 *   },
 * });
 */
export function useGetCustomerByIdQuery(
  baseOptions: Apollo.QueryHookOptions<GetCustomerByIdQuery, GetCustomerByIdQueryVariables>
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useQuery<GetCustomerByIdQuery, GetCustomerByIdQueryVariables>(
    GetCustomerByIdDocument,
    options
  );
}
export function useGetCustomerByIdLazyQuery(
  baseOptions?: Apollo.LazyQueryHookOptions<GetCustomerByIdQuery, GetCustomerByIdQueryVariables>
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useLazyQuery<GetCustomerByIdQuery, GetCustomerByIdQueryVariables>(
    GetCustomerByIdDocument,
    options
  );
}
export type GetCustomerByIdQueryHookResult = ReturnType<typeof useGetCustomerByIdQuery>;
export type GetCustomerByIdLazyQueryHookResult = ReturnType<typeof useGetCustomerByIdLazyQuery>;
export type GetCustomerByIdQueryResult = Apollo.QueryResult<
  GetCustomerByIdQuery,
  GetCustomerByIdQueryVariables
>;
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
export function useSearchProductsKourierQuery(
  baseOptions: Apollo.QueryHookOptions<
    SearchProductsKourierQuery,
    SearchProductsKourierQueryVariables
  >
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useQuery<SearchProductsKourierQuery, SearchProductsKourierQueryVariables>(
    SearchProductsKourierDocument,
    options
  );
}
export function useSearchProductsKourierLazyQuery(
  baseOptions?: Apollo.LazyQueryHookOptions<
    SearchProductsKourierQuery,
    SearchProductsKourierQueryVariables
  >
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useLazyQuery<SearchProductsKourierQuery, SearchProductsKourierQueryVariables>(
    SearchProductsKourierDocument,
    options
  );
}
export type SearchProductsKourierQueryHookResult = ReturnType<typeof useSearchProductsKourierQuery>;
export type SearchProductsKourierLazyQueryHookResult = ReturnType<
  typeof useSearchProductsKourierLazyQuery
>;
export type SearchProductsKourierQueryResult = Apollo.QueryResult<
  SearchProductsKourierQuery,
  SearchProductsKourierQueryVariables
>;
export const BranchUsersListDocument = gql`
  query branchUsersList {
    branchUsersList {
      branchId
      assignedUsers
    }
  }
`;

/**
 * __useBranchUsersListQuery__
 *
 * To run a query within a React component, call `useBranchUsersListQuery` and pass it any options that fit your needs.
 * When your component renders, `useBranchUsersListQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useBranchUsersListQuery({
 *   variables: {
 *   },
 * });
 */
export function useBranchUsersListQuery(
  baseOptions?: Apollo.QueryHookOptions<BranchUsersListQuery, BranchUsersListQueryVariables>
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useQuery<BranchUsersListQuery, BranchUsersListQueryVariables>(
    BranchUsersListDocument,
    options
  );
}
export function useBranchUsersListLazyQuery(
  baseOptions?: Apollo.LazyQueryHookOptions<BranchUsersListQuery, BranchUsersListQueryVariables>
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useLazyQuery<BranchUsersListQuery, BranchUsersListQueryVariables>(
    BranchUsersListDocument,
    options
  );
}
export type BranchUsersListQueryHookResult = ReturnType<typeof useBranchUsersListQuery>;
export type BranchUsersListLazyQueryHookResult = ReturnType<typeof useBranchUsersListLazyQuery>;
export type BranchUsersListQueryResult = Apollo.QueryResult<
  BranchUsersListQuery,
  BranchUsersListQueryVariables
>;
export const GetUserBranchDocument = gql`
  query getUserBranch($input: String!) {
    userBranch(username: $input) {
      username
      branch
    }
  }
`;

/**
 * __useGetUserBranchQuery__
 *
 * To run a query within a React component, call `useGetUserBranchQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetUserBranchQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetUserBranchQuery({
 *   variables: {
 *      input: // value for 'input'
 *   },
 * });
 */
export function useGetUserBranchQuery(
  baseOptions: Apollo.QueryHookOptions<GetUserBranchQuery, GetUserBranchQueryVariables>
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useQuery<GetUserBranchQuery, GetUserBranchQueryVariables>(
    GetUserBranchDocument,
    options
  );
}
export function useGetUserBranchLazyQuery(
  baseOptions?: Apollo.LazyQueryHookOptions<GetUserBranchQuery, GetUserBranchQueryVariables>
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useLazyQuery<GetUserBranchQuery, GetUserBranchQueryVariables>(
    GetUserBranchDocument,
    options
  );
}
export type GetUserBranchQueryHookResult = ReturnType<typeof useGetUserBranchQuery>;
export type GetUserBranchLazyQueryHookResult = ReturnType<typeof useGetUserBranchLazyQuery>;
export type GetUserBranchQueryResult = Apollo.QueryResult<
  GetUserBranchQuery,
  GetUserBranchQueryVariables
>;
