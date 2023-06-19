import { GraphQLResolveInfo } from 'graphql';
export type Maybe<T> = T | null;
export type Exact<T extends { [key: string]: unknown }> = { [K in keyof T]: T[K] };
export type MakeOptional<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]?: Maybe<T[SubKey]> };
export type MakeMaybe<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]: Maybe<T[SubKey]> };
export type RequireFields<T, K extends keyof T> = { [X in Exclude<keyof T, K>]?: T[X] } & { [P in K]-?: NonNullable<T[P]> };
/** All built-in and custom scalars, mapped to their actual values */
export type Scalars = {
  ID: string;
  String: string;
  Boolean: boolean;
  Int: number;
  Float: number;
};

export type LinkAccountResponse = {
  __typename?: 'LinkAccountResponse';
  success: Scalars['Boolean'];
  message?: Maybe<Scalars['String']>;
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
  removeBranchCounts: DeleteMultipleCountResponse;
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


export type MutationRemoveBranchCountsArgs = {
  input: RemoveBranchCountsInput;
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
  oldUserEmail: Scalars['String'];
  newUserEmail: Scalars['String'];
};


export type MutationUpdateVarianceCountArgs = {
  item: ItemInput;
};


export type MutationUpdateWriteInArgs = {
  id: Scalars['ID'];
  writeIn: WriteInInput;
};


export type MutationVerifyEclipseCredentialsArgs = {
  username: Scalars['String'];
  password: Scalars['String'];
};

export type CustomerSearchInput = {
  id?: Maybe<Array<Maybe<Scalars['String']>>>;
  keyword?: Maybe<Scalars['String']>;
  pageSize?: Maybe<Scalars['Int']>;
  currentPage?: Maybe<Scalars['Int']>;
};

export type CustomerSearchResponse = {
  __typename?: 'CustomerSearchResponse';
  metadata?: Maybe<EclipseSearchMetadata>;
  results?: Maybe<Array<Maybe<CustomerSearchResult>>>;
};

export type EclipseSearchMetadata = {
  __typename?: 'EclipseSearchMetadata';
  startIndex?: Maybe<Scalars['Int']>;
  pageSize?: Maybe<Scalars['Int']>;
  totalItems?: Maybe<Scalars['Int']>;
};

export type CustomerSearchResult = {
  __typename?: 'CustomerSearchResult';
  name?: Maybe<Scalars['String']>;
  addressLine1?: Maybe<Scalars['String']>;
  addressLine2?: Maybe<Scalars['String']>;
  addressLine3?: Maybe<Scalars['String']>;
  addressLine4?: Maybe<Scalars['String']>;
  city?: Maybe<Scalars['String']>;
  state?: Maybe<Scalars['String']>;
  postalCode?: Maybe<Scalars['String']>;
  countryCode?: Maybe<Scalars['String']>;
  isBillTo?: Maybe<Scalars['Boolean']>;
  isShipTo?: Maybe<Scalars['Boolean']>;
  isBranchCash?: Maybe<Scalars['Boolean']>;
  isProspect?: Maybe<Scalars['Boolean']>;
  sortBy?: Maybe<Scalars['String']>;
  nameIndex?: Maybe<Scalars['String']>;
  billToId?: Maybe<Scalars['String']>;
  defaultPriceClass?: Maybe<Scalars['String']>;
  ediId?: Maybe<Scalars['String']>;
  orderEntryMessage?: Maybe<Scalars['String']>;
  updateKey?: Maybe<Scalars['String']>;
  shipToLists: Array<ShipToId>;
  id?: Maybe<Scalars['String']>;
};

export type ShipToId = {
  __typename?: 'ShipToId';
  shipToId?: Maybe<Scalars['String']>;
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
  id: Scalars['String'];
  branchId: Scalars['String'];
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

export type CustomerResponse = {
  __typename?: 'CustomerResponse';
  id: Scalars['String'];
  homeBranch: Scalars['String'];
  name: Scalars['String'];
  isBillTo?: Maybe<Scalars['Boolean']>;
  isShipTo?: Maybe<Scalars['Boolean']>;
  addressLine1?: Maybe<Scalars['String']>;
  addressLine2?: Maybe<Scalars['String']>;
  city?: Maybe<Scalars['String']>;
  state?: Maybe<Scalars['String']>;
  postalCode?: Maybe<Scalars['String']>;
  countryCode?: Maybe<Scalars['String']>;
};

export enum ErpSystem {
  MINCRON = 'MINCRON',
  ECLIPSE = 'ECLIPSE'
}

export enum LocationItemStatus {
  UNCOUNTED = 'UNCOUNTED',
  STAGED = 'STAGED',
  COMMITTED = 'COMMITTED'
}

export enum CountStatus {
  NOT_LOADED = 'NOT_LOADED',
  IN_PROGRESS = 'IN_PROGRESS',
  COMPLETE = 'COMPLETE',
  ERROR = 'ERROR'
}

export type Count = {
  __typename?: 'Count';
  id: Scalars['String'];
  branch: Branch;
  erpSystem: ErpSystem;
};

export type CountWithStatus = {
  __typename?: 'CountWithStatus';
  id?: Maybe<Scalars['ID']>;
  branchId: Scalars['String'];
  countId: Scalars['String'];
  branchName?: Maybe<Scalars['String']>;
  status?: Maybe<CountStatus>;
  errorMessage?: Maybe<Scalars['String']>;
  totalProducts?: Maybe<Scalars['Int']>;
  createdAt?: Maybe<Scalars['String']>;
};

export type Branch = {
  __typename?: 'Branch';
  id: Scalars['String'];
  name?: Maybe<Scalars['String']>;
};

export type Location = {
  __typename?: 'Location';
  id: Scalars['String'];
  totalProducts: Scalars['Int'];
  totalCounted: Scalars['Int'];
  committed: Scalars['Boolean'];
  items: Array<LocationItem>;
};

export type LocationItem = {
  __typename?: 'LocationItem';
  id?: Maybe<Scalars['ID']>;
  locationId?: Maybe<Scalars['String']>;
  prodDesc?: Maybe<Scalars['String']>;
  prodNum: Scalars['String'];
  tagNum?: Maybe<Scalars['String']>;
  catalogNum?: Maybe<Scalars['String']>;
  uom?: Maybe<Scalars['String']>;
  quantity?: Maybe<Scalars['Int']>;
  status?: Maybe<LocationItemStatus>;
  sequence?: Maybe<Scalars['Int']>;
  productImageUrl?: Maybe<Scalars['String']>;
  controlNum?: Maybe<Scalars['String']>;
};

export type LocationSummary = {
  __typename?: 'LocationSummary';
  id: Scalars['String'];
  committed: Scalars['Boolean'];
  totalProducts: Scalars['Int'];
  totalCounted: Scalars['Int'];
};

export type LocationsResponse = {
  __typename?: 'LocationsResponse';
  totalLocations: Scalars['Int'];
  totalCounted: Scalars['Int'];
  content: Array<LocationSummary>;
};

export type NextLocationResponse = {
  __typename?: 'NextLocationResponse';
  locationId: Scalars['String'];
};

export type WriteIn = {
  __typename?: 'WriteIn';
  id: Scalars['ID'];
  locationId: Scalars['String'];
  catalogNum?: Maybe<Scalars['String']>;
  upcNum?: Maybe<Scalars['String']>;
  description: Scalars['String'];
  uom: Scalars['String'];
  quantity: Scalars['Int'];
  comment?: Maybe<Scalars['String']>;
  createdBy: Scalars['String'];
  createdAt: Scalars['String'];
  updatedBy: Scalars['String'];
  updatedAt: Scalars['String'];
  resolved: Scalars['Boolean'];
};

export type MetricsCompletion = {
  __typename?: 'MetricsCompletion';
  status: Scalars['String'];
  total: Scalars['Int'];
  needToBeCounted: Scalars['Int'];
  counted: Scalars['Int'];
  timeStarted: Scalars['String'];
  timeEnded: Scalars['String'];
  countId: Scalars['Int'];
  location: Scalars['String'];
  branchId: Scalars['Int'];
};

export type PageableSort = {
  __typename?: 'PageableSort';
  sorted: Scalars['Boolean'];
  unsorted: Scalars['Boolean'];
  empty: Scalars['Boolean'];
};

export type Pageable = {
  __typename?: 'Pageable';
  sort: PageableSort;
  pageNumber: Scalars['Int'];
  pageSize: Scalars['Int'];
  offset: Scalars['Int'];
  paged: Scalars['Boolean'];
  unpaged: Scalars['Boolean'];
};

export type WriteInsResponse = {
  __typename?: 'WriteInsResponse';
  content: Array<WriteIn>;
  pageable: Pageable;
  last: Scalars['Boolean'];
  totalPages: Scalars['Int'];
  totalElements: Scalars['Int'];
  first: Scalars['Boolean'];
  numberOfElements: Scalars['Int'];
  number: Scalars['Int'];
  size: Scalars['Int'];
  sort: PageableSort;
  empty: Scalars['Boolean'];
};

export type WriteInMutationResponse = {
  __typename?: 'WriteInMutationResponse';
  success: Scalars['Boolean'];
  message?: Maybe<Scalars['String']>;
  content: WriteIn;
};

export type MetricsCompletionResponse = {
  __typename?: 'MetricsCompletionResponse';
  success: Scalars['Boolean'];
  message?: Maybe<Scalars['String']>;
  content: MetricsCompletion;
};

export type AddToCountMutationResponse = {
  __typename?: 'AddToCountMutationResponse';
  success: Scalars['Boolean'];
  message?: Maybe<Scalars['String']>;
  item: LocationItem;
};

export type DeleteMultipleCountResponse = {
  __typename?: 'DeleteMultipleCountResponse';
  deletedCounts: Array<DeleteCountResponse>;
};

export type RemoveCountsInput = {
  erpSystemName?: Maybe<ErpSystem>;
  endDate: Scalars['String'];
};

export type DeleteCountResponse = {
  __typename?: 'DeleteCountResponse';
  countLocations: Scalars['Int'];
  countLocationItems: Scalars['Int'];
  countLocationItemQuantities: Scalars['Int'];
  varianceCountLocationItemQuantities: Scalars['Int'];
  writeIns: Scalars['Int'];
};

export type CountMutationResponse = {
  __typename?: 'CountMutationResponse';
  success: Scalars['Boolean'];
  message?: Maybe<Scalars['String']>;
};

export type MetricsCompletionInput = {
  status?: Maybe<Scalars['String']>;
  total?: Maybe<Scalars['Int']>;
  needToBeCounted?: Maybe<Scalars['Int']>;
  counted?: Maybe<Scalars['Int']>;
  timeStarted?: Maybe<Scalars['String']>;
  timeEnded?: Maybe<Scalars['String']>;
  countId?: Maybe<Scalars['String']>;
  location?: Maybe<Scalars['String']>;
  branchId?: Maybe<Scalars['String']>;
};

export type ItemInput = {
  productId: Scalars['String'];
  locationId: Scalars['String'];
  quantity?: Maybe<Scalars['Int']>;
};

export type WriteInsSort = {
  property: Scalars['String'];
  direction: Scalars['String'];
};

export type WriteInsInput = {
  sort?: Maybe<WriteInsSort>;
  page: Scalars['Int'];
  size: Scalars['Int'];
};

export type WriteInInput = {
  locationId: Scalars['String'];
  catalogNum?: Maybe<Scalars['String']>;
  upcNum?: Maybe<Scalars['String']>;
  description: Scalars['String'];
  uom: Scalars['String'];
  quantity: Scalars['Int'];
  comment?: Maybe<Scalars['String']>;
};

export type CountsInput = {
  startDate: Scalars['String'];
  endDate: Scalars['String'];
};

export type RemoveBranchCountsInput = {
  branchId: Scalars['String'];
  countId: Scalars['String'];
};

export type MetricsBranch = {
  __typename?: 'MetricsBranch';
  id?: Maybe<Scalars['String']>;
  city?: Maybe<Scalars['String']>;
  state?: Maybe<Scalars['String']>;
  userCount?: Maybe<Scalars['Int']>;
  loginCount?: Maybe<Scalars['Int']>;
};

export type MetricsChange = {
  __typename?: 'MetricsChange';
  metric: Scalars['String'];
  quantity?: Maybe<Scalars['String']>;
  percentageChange?: Maybe<Scalars['String']>;
};

export type MetricsDivision = {
  __typename?: 'MetricsDivision';
  division: Scalars['String'];
  userCount?: Maybe<Scalars['Int']>;
  loginCount?: Maybe<Scalars['Int']>;
  branchCount?: Maybe<Scalars['Int']>;
  branches?: Maybe<Array<Maybe<MetricsBranch>>>;
};

export type MetricsOverview = {
  __typename?: 'MetricsOverview';
  type: Scalars['String'];
  response?: Maybe<Array<Maybe<MetricsChange>>>;
};

export type MetricsPercentageChangeDivision = {
  __typename?: 'MetricsPercentageChangeDivision';
  division: Scalars['String'];
  userChange?: Maybe<Scalars['String']>;
  loginChange?: Maybe<Scalars['String']>;
  branchChange?: Maybe<Scalars['String']>;
};

export type MetricsPercentageChange = {
  __typename?: 'MetricsPercentageChange';
  type: Scalars['String'];
  response?: Maybe<Array<Maybe<MetricsPercentageChangeDivision>>>;
};

export type MetricsPercentageTotalDivision = {
  __typename?: 'MetricsPercentageTotalDivision';
  division: Scalars['String'];
  userCount?: Maybe<Scalars['Int']>;
  userPercentage?: Maybe<Scalars['Float']>;
  loginCount?: Maybe<Scalars['Int']>;
  loginPercentage?: Maybe<Scalars['Float']>;
  branchCount?: Maybe<Scalars['Int']>;
  branchPercentage?: Maybe<Scalars['Float']>;
};

export type MetricsPercentageTotal = {
  __typename?: 'MetricsPercentageTotal';
  type: Scalars['String'];
  response?: Maybe<Array<Maybe<MetricsPercentageTotalDivision>>>;
};

export type MetricsLoginResponse = {
  __typename?: 'MetricsLoginResponse';
  success: Scalars['Boolean'];
  message: Scalars['String'];
};

export type MetricsUsage = {
  __typename?: 'MetricsUsage';
  type: Scalars['String'];
  response?: Maybe<Array<Maybe<MetricsDivision>>>;
};

export type MetricsSingleRangeInput = {
  startDate?: Maybe<Scalars['String']>;
  endDate?: Maybe<Scalars['String']>;
};

export type MetricsDoubleRangeInput = {
  startDateWeekOld: Scalars['String'];
  endDateWeekOld: Scalars['String'];
  startDateWeekNew: Scalars['String'];
  endDateWeekNew: Scalars['String'];
};

export type PickingOrder = {
  __typename?: 'PickingOrder';
  orderId?: Maybe<Scalars['String']>;
  generationId?: Maybe<Scalars['Int']>;
  invoiceId?: Maybe<Scalars['String']>;
  branchId?: Maybe<Scalars['String']>;
  pickGroup?: Maybe<Scalars['String']>;
  assignedUserId?: Maybe<Scalars['String']>;
  billTo?: Maybe<Scalars['Int']>;
  shipTo?: Maybe<Scalars['Int']>;
  shipToName?: Maybe<Scalars['String']>;
  pickCount?: Maybe<Scalars['String']>;
  shipVia?: Maybe<Scalars['String']>;
  isFromMultipleZones?: Maybe<Scalars['Boolean']>;
  taskWeight?: Maybe<Scalars['Float']>;
  taskState?: Maybe<Scalars['String']>;
};

export type UserPick = {
  __typename?: 'UserPick';
  productId: Scalars['String'];
  productImageUrl?: Maybe<Scalars['String']>;
  description: Scalars['String'];
  quantity: Scalars['Int'];
  uom: Scalars['String'];
  locationType: Scalars['String'];
  location: Scalars['String'];
  lot: Scalars['String'];
  splitId?: Maybe<Scalars['String']>;
  orderId?: Maybe<Scalars['String']>;
  generationId?: Maybe<Scalars['Int']>;
  lineId: Scalars['Int'];
  shipVia: Scalars['String'];
  tote?: Maybe<Scalars['String']>;
  userId: Scalars['String'];
  branchId: Scalars['String'];
  cutDetail?: Maybe<Scalars['String']>;
  cutGroup?: Maybe<Scalars['String']>;
  isParallelCut?: Maybe<Scalars['Boolean']>;
  warehouseID: Scalars['String'];
  isLot?: Maybe<Scalars['String']>;
  isSerial: Scalars['Boolean'];
  pickGroup?: Maybe<Scalars['String']>;
};

export type PickingOrderInput = {
  branchId: Scalars['String'];
  userId: Scalars['String'];
  orderId?: Maybe<Scalars['String']>;
};

export type PickingTaskInput = {
  orderId?: Maybe<Scalars['String']>;
  generationId?: Maybe<Scalars['Int']>;
  invoiceId?: Maybe<Scalars['String']>;
  branchId?: Maybe<Scalars['String']>;
  pickGroup?: Maybe<Scalars['String']>;
  assignedUserId?: Maybe<Scalars['String']>;
  billTo?: Maybe<Scalars['Int']>;
  shipTo?: Maybe<Scalars['Int']>;
  shipToName?: Maybe<Scalars['String']>;
  pickCount?: Maybe<Scalars['String']>;
  shipVia?: Maybe<Scalars['String']>;
  isFromMultipleZones?: Maybe<Scalars['Boolean']>;
  taskWeight?: Maybe<Scalars['Float']>;
  taskState?: Maybe<Scalars['String']>;
};

export type CompletePickInput = {
  productId: Scalars['String'];
  description: Scalars['String'];
  quantity: Scalars['Int'];
  uom: Scalars['String'];
  locationType: Scalars['String'];
  location: Scalars['String'];
  lot: Scalars['String'];
  splitId?: Maybe<Scalars['String']>;
  orderId?: Maybe<Scalars['String']>;
  generationId?: Maybe<Scalars['Int']>;
  lineId: Scalars['Int'];
  shipVia: Scalars['String'];
  tote?: Maybe<Scalars['String']>;
  userId: Scalars['String'];
  branchId: Scalars['String'];
  cutDetail?: Maybe<Scalars['String']>;
  cutGroup?: Maybe<Scalars['String']>;
  isParallelCut?: Maybe<Scalars['Boolean']>;
  warehouseID: Scalars['String'];
  isLot?: Maybe<Scalars['String']>;
  isSerial: Scalars['Boolean'];
  pickGroup?: Maybe<Scalars['String']>;
  isOverrideProduct: Scalars['Boolean'];
  startPickTime: Scalars['String'];
  ignoreLockToteCheck: Scalars['Boolean'];
};

export type ProductSerialNumberResults = {
  __typename?: 'ProductSerialNumberResults';
  results: Array<Maybe<ProductSerialNumbers>>;
};

export type ProductSerialNumbers = {
  __typename?: 'ProductSerialNumbers';
  productId: Scalars['String'];
  orderId: Scalars['String'];
  generationId: Scalars['String'];
  invoiceId: Scalars['String'];
  quantity: Scalars['Int'];
  description?: Maybe<Scalars['String']>;
  location?: Maybe<Scalars['String']>;
  warehouseId: Scalars['String'];
  serialList: Array<Maybe<SerialList>>;
  nonStockSerialNumbers: Array<Maybe<SerialList>>;
};

export type SerialList = {
  __typename?: 'SerialList';
  line: Scalars['Int'];
  serial: Scalars['String'];
};

export type StagePickTaskResponse = {
  __typename?: 'StagePickTaskResponse';
  success: Scalars['Boolean'];
  message: Scalars['String'];
};

export type StagePickTotePackagesResponse = {
  __typename?: 'StagePickTotePackagesResponse';
  success: Scalars['Boolean'];
  message: Scalars['String'];
};

export type CloseTaskResponse = {
  __typename?: 'CloseTaskResponse';
  success: Scalars['Boolean'];
  message: Scalars['String'];
};

export type UpdateProductSerialNumbersInput = {
  branchId: Scalars['String'];
  warehouseId: Scalars['String'];
  serialNumberList: Array<Maybe<SerialLineInput>>;
  ignoreStockCheck: Scalars['Boolean'];
};

export type SerialLineInput = {
  line: Scalars['Int'];
  serial: Scalars['String'];
};

export type StagePickTaskInput = {
  orderId: Scalars['String'];
  invoiceId: Scalars['String'];
  branchId: Scalars['String'];
  tote: Scalars['String'];
  location?: Maybe<Scalars['String']>;
};

export type StagePickTotePackagesInput = {
  orderId: Scalars['String'];
  invoiceId: Scalars['String'];
  branchId: Scalars['String'];
  tote: Scalars['String'];
  packageList: Array<Maybe<PackageInput>>;
};

export type PackageInput = {
  packageType: Scalars['String'];
  packageQuantity: Scalars['Int'];
};

export type CloseTaskInput = {
  orderId: Scalars['String'];
  invoiceId: Scalars['String'];
  branchId: Scalars['String'];
  finalLocation?: Maybe<Scalars['String']>;
  tote: Scalars['String'];
  skipStagedWarningFlag?: Maybe<Scalars['Boolean']>;
  skipInvalidLocationWarningFlag?: Maybe<Scalars['Boolean']>;
  updateLocationOnly?: Maybe<Scalars['Boolean']>;
};

export type SplitQuantityInput = {
  product: CompletePickInput;
  serialNumbers?: Maybe<Array<Maybe<SerialLineInput>>>;
  pickedItemsCount: Scalars['Int'];
};

export type SplitQuantityResponse = {
  __typename?: 'SplitQuantityResponse';
  productId: Scalars['String'];
  isSplit: Scalars['Boolean'];
  orderId: Scalars['String'];
  invalidSerialNums?: Maybe<Array<Maybe<SerialList>>>;
  successStatus: Scalars['Boolean'];
  errorMessage?: Maybe<Scalars['String']>;
};

export type CloseOrderInput = {
  orderId: Scalars['String'];
  pickerId: Scalars['String'];
};

export type CloseOrderResponse = {
  __typename?: 'closeOrderResponse';
  status: Scalars['Boolean'];
  orderId: Scalars['String'];
  pickerId: Scalars['String'];
  errorCode?: Maybe<Scalars['String']>;
  errorMessage?: Maybe<Scalars['String']>;
  orderLocked: Scalars['Boolean'];
  moreToPick: Scalars['Boolean'];
  stillPicking: Scalars['Boolean'];
};

export type ShippingDetailsKourierResponse = {
  __typename?: 'ShippingDetailsKourierResponse';
  shippingtext: Array<ShippingDetailsKourierResult>;
};

export type ShippingDetailsKourierResult = {
  __typename?: 'ShippingDetailsKourierResult';
  invoiceNumber?: Maybe<Scalars['String']>;
  status?: Maybe<Scalars['String']>;
  errorCode?: Maybe<Scalars['String']>;
  errorMessage?: Maybe<Scalars['String']>;
  shippingInstructions?: Maybe<Array<Scalars['String']>>;
  noBackorder: Scalars['Boolean'];
  noSort: Scalars['Boolean'];
};

export type ShippingDetailsKourierInput = {
  invoiceNumber: Scalars['String'];
};

export type ValidateBranchInput = {
  branchId: Scalars['String'];
};

export type ValidateBranchResponse = {
  __typename?: 'ValidateBranchResponse';
  isValid: Scalars['Boolean'];
  branch?: Maybe<BranchResponse>;
};

export type BranchResponse = {
  __typename?: 'BranchResponse';
  branchId: Scalars['String'];
  branchName: Scalars['String'];
};

export type TypeaheadResponse = {
  __typename?: 'TypeaheadResponse';
  meta: TypeaheadMeta;
  results: Array<TypeaheadResult>;
};

export type TypeaheadMeta = {
  __typename?: 'TypeaheadMeta';
  page: Scalars['Int'];
  nextPage?: Maybe<Scalars['Int']>;
  pageCount: Scalars['Int'];
  pageSize: Scalars['Int'];
  resultCount: Scalars['Int'];
  orderBy: Scalars['String'];
  orderDirection: Scalars['String'];
  entity: Scalars['String'];
  query: Scalars['String'];
};

export type TypeaheadResult = {
  __typename?: 'TypeaheadResult';
  id: Scalars['String'];
  displayName: Scalars['String'];
};

export type SpecialPriceMeta = {
  __typename?: 'SpecialPriceMeta';
  page: Scalars['Int'];
  nextPage?: Maybe<Scalars['Int']>;
  pageCount: Scalars['Int'];
  pageSize: Scalars['Int'];
  resultCount: Scalars['Int'];
  orderBy: Scalars['String'];
  orderDirection: Scalars['String'];
  customerId?: Maybe<Scalars['String']>;
  productId?: Maybe<Scalars['String']>;
  priceLine?: Maybe<Scalars['String']>;
};

export type SpecialPriceResponse = {
  __typename?: 'SpecialPriceResponse';
  meta: SpecialPriceMeta;
  results: Array<SpecialPrice>;
};

export type SpecialPrice = {
  __typename?: 'SpecialPrice';
  productId: Scalars['String'];
  customerId: Scalars['String'];
  branch: Scalars['String'];
  customerDisplayName: Scalars['String'];
  imageUrl?: Maybe<Scalars['String']>;
  manufacturer: Scalars['String'];
  displayName: Scalars['String'];
  manufacturerReferenceNumber: Scalars['String'];
  salesperson: Scalars['String'];
  priceLine: Scalars['String'];
  customerSalesQuantity: Scalars['Int'];
  prices: Array<Price>;
  territory?: Maybe<Scalars['String']>;
};

export type Price = {
  __typename?: 'Price';
  type: Scalars['String'];
  value: Scalars['Float'];
  currency: Scalars['String'];
  displayName: Scalars['String'];
};

export type UploadSpecialPriceResponse = {
  __typename?: 'UploadSpecialPriceResponse';
  successfulUploads?: Maybe<Array<SuccessfulUpload>>;
  successfulCreates?: Maybe<Array<SuccessfulCreate>>;
  failedUpdateSuggestions?: Maybe<Array<FailedPriceSuggestion>>;
  failedCreateSuggestions?: Maybe<Array<FailedPriceSuggestion>>;
};

export type SuccessfulUpload = {
  __typename?: 'SuccessfulUpload';
  uploadedPath: Scalars['String'];
  uploadedName: Scalars['String'];
};

export type SuccessfulCreate = {
  __typename?: 'SuccessfulCreate';
  uploadedPath: Scalars['String'];
  uploadedName: Scalars['String'];
};

export type FailedPriceSuggestion = {
  __typename?: 'FailedPriceSuggestion';
  customerId: Scalars['String'];
  productId: Scalars['String'];
  branch: Scalars['String'];
  cmpPrice: Scalars['Float'];
  priceCategory: Scalars['String'];
  newPrice: Scalars['Float'];
  changeWriterDisplayName: Scalars['String'];
  changeWriterId: Scalars['String'];
};

export type ProductPriceResponse = {
  __typename?: 'ProductPriceResponse';
  productId?: Maybe<Scalars['String']>;
  erpBranchNum?: Maybe<Scalars['String']>;
  cmp?: Maybe<Scalars['Float']>;
  stdCost?: Maybe<Scalars['Float']>;
  uom?: Maybe<Scalars['String']>;
  rateCardPrice?: Maybe<Scalars['Float']>;
  rateCardName?: Maybe<Scalars['String']>;
  matchedBranch?: Maybe<Scalars['String']>;
  matchedClass?: Maybe<Scalars['String']>;
  matchedGroup?: Maybe<Scalars['String']>;
  matrixId?: Maybe<Scalars['String']>;
  customerId?: Maybe<Scalars['String']>;
  correlationId?: Maybe<Scalars['String']>;
};

export type PagingContext = {
  page: Scalars['Int'];
  pageSize: Scalars['Int'];
  orderBy: Scalars['String'];
  orderDirection: Scalars['String'];
};

export type TypeaheadInput = {
  entity: Scalars['String'];
  query: Scalars['String'];
};

export type SpecialPriceInput = {
  customerId?: Maybe<Scalars['String']>;
  productId?: Maybe<Scalars['String']>;
  priceLine?: Maybe<Scalars['String']>;
};

export type PriceChangeInput = {
  priceChangeSuggestions: Array<PriceSuggestion>;
  priceCreateSuggestions: Array<PriceSuggestion>;
};

export type PriceSuggestion = {
  customerId: Scalars['String'];
  productId: Scalars['String'];
  territory?: Maybe<Scalars['String']>;
  branch: Scalars['String'];
  cmpPrice: Scalars['Float'];
  priceCategory: Scalars['String'];
  newPrice: Scalars['Float'];
  changeWriterDisplayName: Scalars['String'];
  changeWriterId: Scalars['String'];
};

export type ProductPriceInput = {
  productId: Scalars['String'];
  branch?: Maybe<Scalars['String']>;
  customerId?: Maybe<Scalars['String']>;
  userId?: Maybe<Scalars['String']>;
  correlationId?: Maybe<Scalars['String']>;
  effectiveDate?: Maybe<Scalars['String']>;
};

export type StoreStock = {
  __typename?: 'StoreStock';
  branchName?: Maybe<Scalars['String']>;
  address?: Maybe<Scalars['String']>;
  availability?: Maybe<Scalars['Int']>;
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

export type ImageUrls = {
  __typename?: 'ImageUrls';
  thumb?: Maybe<Scalars['String']>;
  small?: Maybe<Scalars['String']>;
  medium?: Maybe<Scalars['String']>;
  large?: Maybe<Scalars['String']>;
};

export type PackageDimensions = {
  __typename?: 'PackageDimensions';
  height?: Maybe<Scalars['Float']>;
  length?: Maybe<Scalars['Float']>;
  volume?: Maybe<Scalars['Float']>;
  volumeUnitOfMeasure?: Maybe<Scalars['String']>;
  width?: Maybe<Scalars['Float']>;
  weight?: Maybe<Scalars['Float']>;
  weightUnitOfMeasure?: Maybe<Scalars['String']>;
};

export type Stock = {
  __typename?: 'Stock';
  homeBranch?: Maybe<StoreStock>;
  otherBranches?: Maybe<Array<Maybe<StoreStock>>>;
};

export type ProductSearchResult = {
  __typename?: 'ProductSearchResult';
  pagination: Pagination;
  products: Array<Maybe<Product>>;
};

export type Product = {
  __typename?: 'Product';
  id?: Maybe<Scalars['ID']>;
  name?: Maybe<Scalars['String']>;
  productNumber?: Maybe<Scalars['String']>;
  productType?: Maybe<Scalars['String']>;
  taxonomy?: Maybe<Array<Maybe<Scalars['String']>>>;
  manufacturerName?: Maybe<Scalars['String']>;
  manufacturerNumber?: Maybe<Scalars['String']>;
  price?: Maybe<Scalars['Float']>;
  stock?: Maybe<Stock>;
  technicalDocuments?: Maybe<Array<Maybe<TechDoc>>>;
  environmentalOptions?: Maybe<Array<Maybe<Scalars['String']>>>;
  upc?: Maybe<Scalars['String']>;
  unspsc?: Maybe<Scalars['String']>;
  seriesModelFigureNumber?: Maybe<Scalars['String']>;
  productOverview?: Maybe<Scalars['String']>;
  featuresAndBenefits?: Maybe<Scalars['String']>;
  techSpecifications?: Maybe<Array<Maybe<TechSpec>>>;
  imageUrls?: Maybe<ImageUrls>;
  packageDimensions?: Maybe<PackageDimensions>;
  productImageUrl?: Maybe<Scalars['String']>;
};

export type Pagination = {
  __typename?: 'Pagination';
  pageSize: Scalars['Int'];
  currentPage: Scalars['Int'];
  totalItemCount: Scalars['Int'];
};

export type ProductSearchKourierResult = {
  __typename?: 'ProductSearchKourierResult';
  errorCode?: Maybe<Scalars['String']>;
  errorMessage?: Maybe<Scalars['String']>;
  productIdCount?: Maybe<Scalars['Int']>;
  products?: Maybe<Array<Maybe<KourierProduct>>>;
};

export type KourierProduct = {
  __typename?: 'KourierProduct';
  productId?: Maybe<Scalars['String']>;
  displayField?: Maybe<Scalars['String']>;
  productNumber?: Maybe<Scalars['String']>;
  upc?: Maybe<Scalars['String']>;
  productImageUrl?: Maybe<Scalars['String']>;
};

export type ProductSearchKourierResponse = {
  __typename?: 'ProductSearchKourierResponse';
  prodSearch: Array<ProductSearchKourierResult>;
};

export type ProductSearchKourierInput = {
  keywords: Scalars['String'];
  displayName?: Maybe<Scalars['String']>;
  searchInputType?: Maybe<Scalars['String']>;
};

export type ProductSearchEclipseInput = {
  searchTerm?: Maybe<Scalars['String']>;
  pageSize?: Maybe<Scalars['Int']>;
  searchInputType: Scalars['Int'];
  currentPage?: Maybe<Scalars['Int']>;
  selectedAttributes?: Maybe<Array<Maybe<ProductAttribute>>>;
};

export type ProductAttribute = {
  attributeType?: Maybe<Scalars['String']>;
  attributeValue?: Maybe<Scalars['String']>;
};

export type ProductDetails = {
  __typename?: 'ProductDetails';
  description: Scalars['String'];
  catalogNumber: Scalars['String'];
  upc: Scalars['String'];
};

export type UserResponse = {
  __typename?: 'UserResponse';
  username: Scalars['String'];
  branch?: Maybe<Array<Maybe<Scalars['String']>>>;
};

export type BranchUsersListResponse = {
  __typename?: 'BranchUsersListResponse';
  branchId: Scalars['String'];
  assignedUsers?: Maybe<Array<Maybe<Scalars['String']>>>;
};

export type ErrorResponse = {
  __typename?: 'ErrorResponse';
  code: Scalars['String'];
  message: Scalars['String'];
  details?: Maybe<Scalars['String']>;
};

export type UserBranchResponse = {
  __typename?: 'UserBranchResponse';
  success: Scalars['Boolean'];
  error?: Maybe<ErrorResponse>;
};

export enum VarianceItemStatus {
  NONVARIANCE = 'NONVARIANCE',
  UNCOUNTED = 'UNCOUNTED',
  STAGED = 'STAGED',
  COMMITTED = 'COMMITTED'
}

export type VarianceSummaryResponse = {
  __typename?: 'VarianceSummaryResponse';
  netTotalCost?: Maybe<Scalars['String']>;
  grossTotalCost?: Maybe<Scalars['String']>;
  locationQuantity?: Maybe<Scalars['Int']>;
  productQuantity?: Maybe<Scalars['Int']>;
  differenceQuantity?: Maybe<Scalars['Int']>;
  differencePercentage?: Maybe<Scalars['Float']>;
};

export type VarianceDetailsResponse = {
  __typename?: 'VarianceDetailsResponse';
  success: Scalars['Boolean'];
  message: Scalars['String'];
};

export type VarianceLocationResponse = {
  __typename?: 'VarianceLocationResponse';
  id: Scalars['String'];
  totalProducts: Scalars['Int'];
  totalCounted: Scalars['Int'];
  committed: Scalars['Boolean'];
  netVarianceCost: Scalars['Float'];
  grossVarianceCost: Scalars['Float'];
  items: Array<VarianceLocationItem>;
};

export type VarianceLocationItem = {
  __typename?: 'VarianceLocationItem';
  id?: Maybe<Scalars['ID']>;
  locationId?: Maybe<Scalars['String']>;
  prodDesc?: Maybe<Scalars['String']>;
  prodNum: Scalars['String'];
  tagNum?: Maybe<Scalars['String']>;
  catalogNum?: Maybe<Scalars['String']>;
  uom?: Maybe<Scalars['String']>;
  quantity?: Maybe<Scalars['Int']>;
  status?: Maybe<LocationItemStatus>;
  sequence?: Maybe<Scalars['Int']>;
  varianceCost?: Maybe<Scalars['Float']>;
  varianceStatus?: Maybe<VarianceItemStatus>;
  productImageUrl?: Maybe<Scalars['String']>;
};

export type VarianceNextLocationResponse = {
  __typename?: 'VarianceNextLocationResponse';
  locationId: Scalars['String'];
};

export type VarianceLocationsResponse = {
  __typename?: 'VarianceLocationsResponse';
  totalLocations: Scalars['Int'];
  content: Array<VarianceLocationSummary>;
};

export type VarianceLocationSummary = {
  __typename?: 'VarianceLocationSummary';
  id: Scalars['String'];
  totalProducts: Scalars['Int'];
  netVarianceCost: Scalars['Float'];
  grossVarianceCost: Scalars['Float'];
};

export type DetailedVarianceResponse = {
  __typename?: 'DetailedVarianceResponse';
  counts?: Maybe<Array<Maybe<VarianceDetails>>>;
};

export type VarianceDetails = {
  __typename?: 'VarianceDetails';
  location: Scalars['String'];
  erpProductID: Scalars['String'];
  productDescription: Scalars['String'];
  countQty?: Maybe<Scalars['Int']>;
  onHandQty?: Maybe<Scalars['Int']>;
  qtyDeviance?: Maybe<Scalars['Int']>;
  onHandCost?: Maybe<Scalars['Float']>;
  percentDeviance?: Maybe<Scalars['Float']>;
  countedCost?: Maybe<Scalars['Float']>;
  notCountedFlag?: Maybe<Scalars['Boolean']>;
  recount1Qty?: Maybe<Scalars['Int']>;
  recount2Qty?: Maybe<Scalars['Int']>;
  recount3Qty?: Maybe<Scalars['Int']>;
};

export type WithIndex<TObject> = TObject & Record<string, any>;
export type ResolversObject<TObject> = WithIndex<TObject>;

export type ResolverTypeWrapper<T> = Promise<T> | T;


export type LegacyStitchingResolver<TResult, TParent, TContext, TArgs> = {
  fragment: string;
  resolve: ResolverFn<TResult, TParent, TContext, TArgs>;
};

export type NewStitchingResolver<TResult, TParent, TContext, TArgs> = {
  selectionSet: string;
  resolve: ResolverFn<TResult, TParent, TContext, TArgs>;
};
export type StitchingResolver<TResult, TParent, TContext, TArgs> = LegacyStitchingResolver<TResult, TParent, TContext, TArgs> | NewStitchingResolver<TResult, TParent, TContext, TArgs>;
export type Resolver<TResult, TParent = {}, TContext = {}, TArgs = {}> =
  | ResolverFn<TResult, TParent, TContext, TArgs>
  | StitchingResolver<TResult, TParent, TContext, TArgs>;

export type ResolverFn<TResult, TParent, TContext, TArgs> = (
  parent: TParent,
  args: TArgs,
  context: TContext,
  info: GraphQLResolveInfo
) => Promise<TResult> | TResult;

export type SubscriptionSubscribeFn<TResult, TParent, TContext, TArgs> = (
  parent: TParent,
  args: TArgs,
  context: TContext,
  info: GraphQLResolveInfo
) => AsyncIterator<TResult> | Promise<AsyncIterator<TResult>>;

export type SubscriptionResolveFn<TResult, TParent, TContext, TArgs> = (
  parent: TParent,
  args: TArgs,
  context: TContext,
  info: GraphQLResolveInfo
) => TResult | Promise<TResult>;

export interface SubscriptionSubscriberObject<TResult, TKey extends string, TParent, TContext, TArgs> {
  subscribe: SubscriptionSubscribeFn<{ [key in TKey]: TResult }, TParent, TContext, TArgs>;
  resolve?: SubscriptionResolveFn<TResult, { [key in TKey]: TResult }, TContext, TArgs>;
}

export interface SubscriptionResolverObject<TResult, TParent, TContext, TArgs> {
  subscribe: SubscriptionSubscribeFn<any, TParent, TContext, TArgs>;
  resolve: SubscriptionResolveFn<TResult, any, TContext, TArgs>;
}

export type SubscriptionObject<TResult, TKey extends string, TParent, TContext, TArgs> =
  | SubscriptionSubscriberObject<TResult, TKey, TParent, TContext, TArgs>
  | SubscriptionResolverObject<TResult, TParent, TContext, TArgs>;

export type SubscriptionResolver<TResult, TKey extends string, TParent = {}, TContext = {}, TArgs = {}> =
  | ((...args: any[]) => SubscriptionObject<TResult, TKey, TParent, TContext, TArgs>)
  | SubscriptionObject<TResult, TKey, TParent, TContext, TArgs>;

export type TypeResolveFn<TTypes, TParent = {}, TContext = {}> = (
  parent: TParent,
  context: TContext,
  info: GraphQLResolveInfo
) => Maybe<TTypes> | Promise<Maybe<TTypes>>;

export type IsTypeOfResolverFn<T = {}, TContext = {}> = (obj: T, context: TContext, info: GraphQLResolveInfo) => boolean | Promise<boolean>;

export type NextResolverFn<T> = () => Promise<T>;

export type DirectiveResolverFn<TResult = {}, TParent = {}, TContext = {}, TArgs = {}> = (
  next: NextResolverFn<TResult>,
  parent: TParent,
  args: TArgs,
  context: TContext,
  info: GraphQLResolveInfo
) => TResult | Promise<TResult>;

/** Mapping between all available schema types and the resolvers types */
export type ResolversTypes = ResolversObject<{
  LinkAccountResponse: ResolverTypeWrapper<LinkAccountResponse>;
  Boolean: ResolverTypeWrapper<Scalars['Boolean']>;
  String: ResolverTypeWrapper<Scalars['String']>;
  Mutation: ResolverTypeWrapper<{}>;
  ID: ResolverTypeWrapper<Scalars['ID']>;
  CustomerSearchInput: CustomerSearchInput;
  Int: ResolverTypeWrapper<Scalars['Int']>;
  CustomerSearchResponse: ResolverTypeWrapper<CustomerSearchResponse>;
  EclipseSearchMetadata: ResolverTypeWrapper<EclipseSearchMetadata>;
  CustomerSearchResult: ResolverTypeWrapper<CustomerSearchResult>;
  ShipToId: ResolverTypeWrapper<ShipToId>;
  Query: ResolverTypeWrapper<{}>;
  CustomerResponse: ResolverTypeWrapper<CustomerResponse>;
  ErpSystem: ErpSystem;
  LocationItemStatus: LocationItemStatus;
  CountStatus: CountStatus;
  Count: ResolverTypeWrapper<Count>;
  CountWithStatus: ResolverTypeWrapper<CountWithStatus>;
  Branch: ResolverTypeWrapper<Branch>;
  Location: ResolverTypeWrapper<Location>;
  LocationItem: ResolverTypeWrapper<LocationItem>;
  LocationSummary: ResolverTypeWrapper<LocationSummary>;
  LocationsResponse: ResolverTypeWrapper<LocationsResponse>;
  NextLocationResponse: ResolverTypeWrapper<NextLocationResponse>;
  WriteIn: ResolverTypeWrapper<WriteIn>;
  MetricsCompletion: ResolverTypeWrapper<MetricsCompletion>;
  PageableSort: ResolverTypeWrapper<PageableSort>;
  Pageable: ResolverTypeWrapper<Pageable>;
  WriteInsResponse: ResolverTypeWrapper<WriteInsResponse>;
  WriteInMutationResponse: ResolverTypeWrapper<WriteInMutationResponse>;
  MetricsCompletionResponse: ResolverTypeWrapper<MetricsCompletionResponse>;
  AddToCountMutationResponse: ResolverTypeWrapper<AddToCountMutationResponse>;
  DeleteMultipleCountResponse: ResolverTypeWrapper<DeleteMultipleCountResponse>;
  RemoveCountsInput: RemoveCountsInput;
  DeleteCountResponse: ResolverTypeWrapper<DeleteCountResponse>;
  CountMutationResponse: ResolverTypeWrapper<CountMutationResponse>;
  MetricsCompletionInput: MetricsCompletionInput;
  ItemInput: ItemInput;
  WriteInsSort: WriteInsSort;
  WriteInsInput: WriteInsInput;
  WriteInInput: WriteInInput;
  CountsInput: CountsInput;
  RemoveBranchCountsInput: RemoveBranchCountsInput;
  MetricsBranch: ResolverTypeWrapper<MetricsBranch>;
  MetricsChange: ResolverTypeWrapper<MetricsChange>;
  MetricsDivision: ResolverTypeWrapper<MetricsDivision>;
  MetricsOverview: ResolverTypeWrapper<MetricsOverview>;
  MetricsPercentageChangeDivision: ResolverTypeWrapper<MetricsPercentageChangeDivision>;
  MetricsPercentageChange: ResolverTypeWrapper<MetricsPercentageChange>;
  MetricsPercentageTotalDivision: ResolverTypeWrapper<MetricsPercentageTotalDivision>;
  Float: ResolverTypeWrapper<Scalars['Float']>;
  MetricsPercentageTotal: ResolverTypeWrapper<MetricsPercentageTotal>;
  MetricsLoginResponse: ResolverTypeWrapper<MetricsLoginResponse>;
  MetricsUsage: ResolverTypeWrapper<MetricsUsage>;
  MetricsSingleRangeInput: MetricsSingleRangeInput;
  MetricsDoubleRangeInput: MetricsDoubleRangeInput;
  PickingOrder: ResolverTypeWrapper<PickingOrder>;
  UserPick: ResolverTypeWrapper<UserPick>;
  PickingOrderInput: PickingOrderInput;
  PickingTaskInput: PickingTaskInput;
  CompletePickInput: CompletePickInput;
  ProductSerialNumberResults: ResolverTypeWrapper<ProductSerialNumberResults>;
  ProductSerialNumbers: ResolverTypeWrapper<ProductSerialNumbers>;
  SerialList: ResolverTypeWrapper<SerialList>;
  StagePickTaskResponse: ResolverTypeWrapper<StagePickTaskResponse>;
  StagePickTotePackagesResponse: ResolverTypeWrapper<StagePickTotePackagesResponse>;
  CloseTaskResponse: ResolverTypeWrapper<CloseTaskResponse>;
  UpdateProductSerialNumbersInput: UpdateProductSerialNumbersInput;
  SerialLineInput: SerialLineInput;
  StagePickTaskInput: StagePickTaskInput;
  StagePickTotePackagesInput: StagePickTotePackagesInput;
  PackageInput: PackageInput;
  CloseTaskInput: CloseTaskInput;
  SplitQuantityInput: SplitQuantityInput;
  SplitQuantityResponse: ResolverTypeWrapper<SplitQuantityResponse>;
  closeOrderInput: CloseOrderInput;
  closeOrderResponse: ResolverTypeWrapper<CloseOrderResponse>;
  ShippingDetailsKourierResponse: ResolverTypeWrapper<ShippingDetailsKourierResponse>;
  ShippingDetailsKourierResult: ResolverTypeWrapper<ShippingDetailsKourierResult>;
  ShippingDetailsKourierInput: ShippingDetailsKourierInput;
  ValidateBranchInput: ValidateBranchInput;
  ValidateBranchResponse: ResolverTypeWrapper<ValidateBranchResponse>;
  BranchResponse: ResolverTypeWrapper<BranchResponse>;
  TypeaheadResponse: ResolverTypeWrapper<TypeaheadResponse>;
  TypeaheadMeta: ResolverTypeWrapper<TypeaheadMeta>;
  TypeaheadResult: ResolverTypeWrapper<TypeaheadResult>;
  SpecialPriceMeta: ResolverTypeWrapper<SpecialPriceMeta>;
  SpecialPriceResponse: ResolverTypeWrapper<SpecialPriceResponse>;
  SpecialPrice: ResolverTypeWrapper<SpecialPrice>;
  Price: ResolverTypeWrapper<Price>;
  UploadSpecialPriceResponse: ResolverTypeWrapper<UploadSpecialPriceResponse>;
  SuccessfulUpload: ResolverTypeWrapper<SuccessfulUpload>;
  SuccessfulCreate: ResolverTypeWrapper<SuccessfulCreate>;
  FailedPriceSuggestion: ResolverTypeWrapper<FailedPriceSuggestion>;
  ProductPriceResponse: ResolverTypeWrapper<ProductPriceResponse>;
  PagingContext: PagingContext;
  TypeaheadInput: TypeaheadInput;
  SpecialPriceInput: SpecialPriceInput;
  PriceChangeInput: PriceChangeInput;
  PriceSuggestion: PriceSuggestion;
  ProductPriceInput: ProductPriceInput;
  StoreStock: ResolverTypeWrapper<StoreStock>;
  TechDoc: ResolverTypeWrapper<TechDoc>;
  TechSpec: ResolverTypeWrapper<TechSpec>;
  ImageUrls: ResolverTypeWrapper<ImageUrls>;
  PackageDimensions: ResolverTypeWrapper<PackageDimensions>;
  Stock: ResolverTypeWrapper<Stock>;
  ProductSearchResult: ResolverTypeWrapper<ProductSearchResult>;
  Product: ResolverTypeWrapper<Product>;
  Pagination: ResolverTypeWrapper<Pagination>;
  ProductSearchKourierResult: ResolverTypeWrapper<ProductSearchKourierResult>;
  KourierProduct: ResolverTypeWrapper<KourierProduct>;
  ProductSearchKourierResponse: ResolverTypeWrapper<ProductSearchKourierResponse>;
  ProductSearchKourierInput: ProductSearchKourierInput;
  ProductSearchEclipseInput: ProductSearchEclipseInput;
  ProductAttribute: ProductAttribute;
  ProductDetails: ResolverTypeWrapper<ProductDetails>;
  UserResponse: ResolverTypeWrapper<UserResponse>;
  BranchUsersListResponse: ResolverTypeWrapper<BranchUsersListResponse>;
  ErrorResponse: ResolverTypeWrapper<ErrorResponse>;
  UserBranchResponse: ResolverTypeWrapper<UserBranchResponse>;
  VarianceItemStatus: VarianceItemStatus;
  VarianceSummaryResponse: ResolverTypeWrapper<VarianceSummaryResponse>;
  VarianceDetailsResponse: ResolverTypeWrapper<VarianceDetailsResponse>;
  VarianceLocationResponse: ResolverTypeWrapper<VarianceLocationResponse>;
  VarianceLocationItem: ResolverTypeWrapper<VarianceLocationItem>;
  VarianceNextLocationResponse: ResolverTypeWrapper<VarianceNextLocationResponse>;
  VarianceLocationsResponse: ResolverTypeWrapper<VarianceLocationsResponse>;
  VarianceLocationSummary: ResolverTypeWrapper<VarianceLocationSummary>;
  DetailedVarianceResponse: ResolverTypeWrapper<DetailedVarianceResponse>;
  VarianceDetails: ResolverTypeWrapper<VarianceDetails>;
}>;

/** Mapping between all available schema types and the resolvers parents */
export type ResolversParentTypes = ResolversObject<{
  LinkAccountResponse: LinkAccountResponse;
  Boolean: Scalars['Boolean'];
  String: Scalars['String'];
  Mutation: {};
  ID: Scalars['ID'];
  CustomerSearchInput: CustomerSearchInput;
  Int: Scalars['Int'];
  CustomerSearchResponse: CustomerSearchResponse;
  EclipseSearchMetadata: EclipseSearchMetadata;
  CustomerSearchResult: CustomerSearchResult;
  ShipToId: ShipToId;
  Query: {};
  CustomerResponse: CustomerResponse;
  Count: Count;
  CountWithStatus: CountWithStatus;
  Branch: Branch;
  Location: Location;
  LocationItem: LocationItem;
  LocationSummary: LocationSummary;
  LocationsResponse: LocationsResponse;
  NextLocationResponse: NextLocationResponse;
  WriteIn: WriteIn;
  MetricsCompletion: MetricsCompletion;
  PageableSort: PageableSort;
  Pageable: Pageable;
  WriteInsResponse: WriteInsResponse;
  WriteInMutationResponse: WriteInMutationResponse;
  MetricsCompletionResponse: MetricsCompletionResponse;
  AddToCountMutationResponse: AddToCountMutationResponse;
  DeleteMultipleCountResponse: DeleteMultipleCountResponse;
  RemoveCountsInput: RemoveCountsInput;
  DeleteCountResponse: DeleteCountResponse;
  CountMutationResponse: CountMutationResponse;
  MetricsCompletionInput: MetricsCompletionInput;
  ItemInput: ItemInput;
  WriteInsSort: WriteInsSort;
  WriteInsInput: WriteInsInput;
  WriteInInput: WriteInInput;
  CountsInput: CountsInput;
  RemoveBranchCountsInput: RemoveBranchCountsInput;
  MetricsBranch: MetricsBranch;
  MetricsChange: MetricsChange;
  MetricsDivision: MetricsDivision;
  MetricsOverview: MetricsOverview;
  MetricsPercentageChangeDivision: MetricsPercentageChangeDivision;
  MetricsPercentageChange: MetricsPercentageChange;
  MetricsPercentageTotalDivision: MetricsPercentageTotalDivision;
  Float: Scalars['Float'];
  MetricsPercentageTotal: MetricsPercentageTotal;
  MetricsLoginResponse: MetricsLoginResponse;
  MetricsUsage: MetricsUsage;
  MetricsSingleRangeInput: MetricsSingleRangeInput;
  MetricsDoubleRangeInput: MetricsDoubleRangeInput;
  PickingOrder: PickingOrder;
  UserPick: UserPick;
  PickingOrderInput: PickingOrderInput;
  PickingTaskInput: PickingTaskInput;
  CompletePickInput: CompletePickInput;
  ProductSerialNumberResults: ProductSerialNumberResults;
  ProductSerialNumbers: ProductSerialNumbers;
  SerialList: SerialList;
  StagePickTaskResponse: StagePickTaskResponse;
  StagePickTotePackagesResponse: StagePickTotePackagesResponse;
  CloseTaskResponse: CloseTaskResponse;
  UpdateProductSerialNumbersInput: UpdateProductSerialNumbersInput;
  SerialLineInput: SerialLineInput;
  StagePickTaskInput: StagePickTaskInput;
  StagePickTotePackagesInput: StagePickTotePackagesInput;
  PackageInput: PackageInput;
  CloseTaskInput: CloseTaskInput;
  SplitQuantityInput: SplitQuantityInput;
  SplitQuantityResponse: SplitQuantityResponse;
  closeOrderInput: CloseOrderInput;
  closeOrderResponse: CloseOrderResponse;
  ShippingDetailsKourierResponse: ShippingDetailsKourierResponse;
  ShippingDetailsKourierResult: ShippingDetailsKourierResult;
  ShippingDetailsKourierInput: ShippingDetailsKourierInput;
  ValidateBranchInput: ValidateBranchInput;
  ValidateBranchResponse: ValidateBranchResponse;
  BranchResponse: BranchResponse;
  TypeaheadResponse: TypeaheadResponse;
  TypeaheadMeta: TypeaheadMeta;
  TypeaheadResult: TypeaheadResult;
  SpecialPriceMeta: SpecialPriceMeta;
  SpecialPriceResponse: SpecialPriceResponse;
  SpecialPrice: SpecialPrice;
  Price: Price;
  UploadSpecialPriceResponse: UploadSpecialPriceResponse;
  SuccessfulUpload: SuccessfulUpload;
  SuccessfulCreate: SuccessfulCreate;
  FailedPriceSuggestion: FailedPriceSuggestion;
  ProductPriceResponse: ProductPriceResponse;
  PagingContext: PagingContext;
  TypeaheadInput: TypeaheadInput;
  SpecialPriceInput: SpecialPriceInput;
  PriceChangeInput: PriceChangeInput;
  PriceSuggestion: PriceSuggestion;
  ProductPriceInput: ProductPriceInput;
  StoreStock: StoreStock;
  TechDoc: TechDoc;
  TechSpec: TechSpec;
  ImageUrls: ImageUrls;
  PackageDimensions: PackageDimensions;
  Stock: Stock;
  ProductSearchResult: ProductSearchResult;
  Product: Product;
  Pagination: Pagination;
  ProductSearchKourierResult: ProductSearchKourierResult;
  KourierProduct: KourierProduct;
  ProductSearchKourierResponse: ProductSearchKourierResponse;
  ProductSearchKourierInput: ProductSearchKourierInput;
  ProductSearchEclipseInput: ProductSearchEclipseInput;
  ProductAttribute: ProductAttribute;
  ProductDetails: ProductDetails;
  UserResponse: UserResponse;
  BranchUsersListResponse: BranchUsersListResponse;
  ErrorResponse: ErrorResponse;
  UserBranchResponse: UserBranchResponse;
  VarianceSummaryResponse: VarianceSummaryResponse;
  VarianceDetailsResponse: VarianceDetailsResponse;
  VarianceLocationResponse: VarianceLocationResponse;
  VarianceLocationItem: VarianceLocationItem;
  VarianceNextLocationResponse: VarianceNextLocationResponse;
  VarianceLocationsResponse: VarianceLocationsResponse;
  VarianceLocationSummary: VarianceLocationSummary;
  DetailedVarianceResponse: DetailedVarianceResponse;
  VarianceDetails: VarianceDetails;
}>;

export type LinkAccountResponseResolvers<ContextType = any, ParentType extends ResolversParentTypes['LinkAccountResponse'] = ResolversParentTypes['LinkAccountResponse']> = ResolversObject<{
  success?: Resolver<ResolversTypes['Boolean'], ParentType, ContextType>;
  message?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type MutationResolvers<ContextType = any, ParentType extends ResolversParentTypes['Mutation'] = ResolversParentTypes['Mutation']> = ResolversObject<{
  addCompletionMetric?: Resolver<ResolversTypes['MetricsCompletionResponse'], ParentType, ContextType, RequireFields<MutationAddCompletionMetricArgs, 'metric'>>;
  addToCount?: Resolver<ResolversTypes['AddToCountMutationResponse'], ParentType, ContextType, RequireFields<MutationAddToCountArgs, 'item'>>;
  addUser?: Resolver<ResolversTypes['UserBranchResponse'], ParentType, ContextType, RequireFields<MutationAddUserArgs, 'branchId' | 'userEmail'>>;
  assignPickTask?: Resolver<ResolversTypes['PickingOrder'], ParentType, ContextType, RequireFields<MutationAssignPickTaskArgs, 'input'>>;
  closeOrder?: Resolver<ResolversTypes['closeOrderResponse'], ParentType, ContextType, RequireFields<MutationCloseOrderArgs, 'input'>>;
  closeTask?: Resolver<ResolversTypes['CloseTaskResponse'], ParentType, ContextType, RequireFields<MutationCloseTaskArgs, 'input'>>;
  completeCount?: Resolver<ResolversTypes['CountMutationResponse'], ParentType, ContextType, RequireFields<MutationCompleteCountArgs, 'locationId'>>;
  completeUserPick?: Resolver<ResolversTypes['UserPick'], ParentType, ContextType, RequireFields<MutationCompleteUserPickArgs, 'input'>>;
  completeVarianceCount?: Resolver<ResolversTypes['CountMutationResponse'], ParentType, ContextType, RequireFields<MutationCompleteVarianceCountArgs, 'locationId'>>;
  createWriteIn?: Resolver<ResolversTypes['WriteInMutationResponse'], ParentType, ContextType, RequireFields<MutationCreateWriteInArgs, 'writeIn'>>;
  deleteCount?: Resolver<ResolversTypes['DeleteCountResponse'], ParentType, ContextType, RequireFields<MutationDeleteCountArgs, 'id'>>;
  deleteUser?: Resolver<ResolversTypes['UserBranchResponse'], ParentType, ContextType, RequireFields<MutationDeleteUserArgs, 'userEmail'>>;
  loadCount?: Resolver<ResolversTypes['CountWithStatus'], ParentType, ContextType, RequireFields<MutationLoadCountArgs, 'branchId' | 'countId'>>;
  loadVarianceDetails?: Resolver<ResolversTypes['VarianceDetailsResponse'], ParentType, ContextType>;
  purgeMincronCounts?: Resolver<ResolversTypes['String'], ParentType, ContextType, RequireFields<MutationPurgeMincronCountsArgs, 'input'>>;
  registerLogin?: Resolver<ResolversTypes['MetricsLoginResponse'], ParentType, ContextType>;
  removeBranchCounts?: Resolver<ResolversTypes['DeleteMultipleCountResponse'], ParentType, ContextType, RequireFields<MutationRemoveBranchCountsArgs, 'input'>>;
  removeCounts?: Resolver<ResolversTypes['String'], ParentType, ContextType, RequireFields<MutationRemoveCountsArgs, 'input'>>;
  removeUserBranch?: Resolver<ResolversTypes['UserBranchResponse'], ParentType, ContextType, RequireFields<MutationRemoveUserBranchArgs, 'branchId' | 'userEmail'>>;
  resolveWriteIn?: Resolver<ResolversTypes['WriteInMutationResponse'], ParentType, ContextType, RequireFields<MutationResolveWriteInArgs, 'id'>>;
  specialPrices?: Resolver<Maybe<ResolversTypes['UploadSpecialPriceResponse']>, ParentType, ContextType, RequireFields<MutationSpecialPricesArgs, 'input'>>;
  splitQuantity?: Resolver<Maybe<ResolversTypes['SplitQuantityResponse']>, ParentType, ContextType, RequireFields<MutationSplitQuantityArgs, 'input'>>;
  stagePickTask?: Resolver<ResolversTypes['StagePickTaskResponse'], ParentType, ContextType, RequireFields<MutationStagePickTaskArgs, 'input'>>;
  stagePickTotePackages?: Resolver<ResolversTypes['StagePickTotePackagesResponse'], ParentType, ContextType, RequireFields<MutationStagePickTotePackagesArgs, 'input'>>;
  updateCount?: Resolver<ResolversTypes['CountMutationResponse'], ParentType, ContextType, RequireFields<MutationUpdateCountArgs, 'item'>>;
  updateProductSerialNumbers?: Resolver<ResolversTypes['ProductSerialNumberResults'], ParentType, ContextType, RequireFields<MutationUpdateProductSerialNumbersArgs, 'input'>>;
  updateUserEmail?: Resolver<ResolversTypes['UserBranchResponse'], ParentType, ContextType, RequireFields<MutationUpdateUserEmailArgs, 'oldUserEmail' | 'newUserEmail'>>;
  updateVarianceCount?: Resolver<ResolversTypes['CountMutationResponse'], ParentType, ContextType, RequireFields<MutationUpdateVarianceCountArgs, 'item'>>;
  updateWriteIn?: Resolver<ResolversTypes['WriteInMutationResponse'], ParentType, ContextType, RequireFields<MutationUpdateWriteInArgs, 'id' | 'writeIn'>>;
  verifyEclipseCredentials?: Resolver<ResolversTypes['LinkAccountResponse'], ParentType, ContextType, RequireFields<MutationVerifyEclipseCredentialsArgs, 'username' | 'password'>>;
}>;

export type CustomerSearchResponseResolvers<ContextType = any, ParentType extends ResolversParentTypes['CustomerSearchResponse'] = ResolversParentTypes['CustomerSearchResponse']> = ResolversObject<{
  metadata?: Resolver<Maybe<ResolversTypes['EclipseSearchMetadata']>, ParentType, ContextType>;
  results?: Resolver<Maybe<Array<Maybe<ResolversTypes['CustomerSearchResult']>>>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type EclipseSearchMetadataResolvers<ContextType = any, ParentType extends ResolversParentTypes['EclipseSearchMetadata'] = ResolversParentTypes['EclipseSearchMetadata']> = ResolversObject<{
  startIndex?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  pageSize?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  totalItems?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type CustomerSearchResultResolvers<ContextType = any, ParentType extends ResolversParentTypes['CustomerSearchResult'] = ResolversParentTypes['CustomerSearchResult']> = ResolversObject<{
  name?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  addressLine1?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  addressLine2?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  addressLine3?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  addressLine4?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  city?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  state?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  postalCode?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  countryCode?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  isBillTo?: Resolver<Maybe<ResolversTypes['Boolean']>, ParentType, ContextType>;
  isShipTo?: Resolver<Maybe<ResolversTypes['Boolean']>, ParentType, ContextType>;
  isBranchCash?: Resolver<Maybe<ResolversTypes['Boolean']>, ParentType, ContextType>;
  isProspect?: Resolver<Maybe<ResolversTypes['Boolean']>, ParentType, ContextType>;
  sortBy?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  nameIndex?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  billToId?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  defaultPriceClass?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  ediId?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  orderEntryMessage?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  updateKey?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  shipToLists?: Resolver<Array<ResolversTypes['ShipToId']>, ParentType, ContextType>;
  id?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type ShipToIdResolvers<ContextType = any, ParentType extends ResolversParentTypes['ShipToId'] = ResolversParentTypes['ShipToId']> = ResolversObject<{
  shipToId?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type QueryResolvers<ContextType = any, ParentType extends ResolversParentTypes['Query'] = ResolversParentTypes['Query']> = ResolversObject<{
  branchUsersList?: Resolver<Array<Maybe<ResolversTypes['BranchUsersListResponse']>>, ParentType, ContextType>;
  count?: Resolver<ResolversTypes['Count'], ParentType, ContextType, RequireFields<QueryCountArgs, 'id' | 'branchId'>>;
  countStatus?: Resolver<ResolversTypes['CountWithStatus'], ParentType, ContextType, RequireFields<QueryCountStatusArgs, 'id'>>;
  counts?: Resolver<Array<ResolversTypes['CountWithStatus']>, ParentType, ContextType, RequireFields<QueryCountsArgs, 'input'>>;
  customer?: Resolver<ResolversTypes['CustomerResponse'], ParentType, ContextType, RequireFields<QueryCustomerArgs, 'customerId'>>;
  getCustomerSearch?: Resolver<Maybe<ResolversTypes['CustomerSearchResponse']>, ParentType, ContextType, RequireFields<QueryGetCustomerSearchArgs, 'input'>>;
  getProductSerialNumbers?: Resolver<ResolversTypes['ProductSerialNumberResults'], ParentType, ContextType, RequireFields<QueryGetProductSerialNumbersArgs, 'warehouseId'>>;
  location?: Resolver<ResolversTypes['Location'], ParentType, ContextType, RequireFields<QueryLocationArgs, 'id'>>;
  locations?: Resolver<ResolversTypes['LocationsResponse'], ParentType, ContextType>;
  nextLocation?: Resolver<ResolversTypes['NextLocationResponse'], ParentType, ContextType, RequireFields<QueryNextLocationArgs, 'id'>>;
  paginatedSpecialPrices?: Resolver<ResolversTypes['SpecialPriceResponse'], ParentType, ContextType, RequireFields<QueryPaginatedSpecialPricesArgs, 'input' | 'pagingContext'>>;
  paginatedTypeaheadSuggestions?: Resolver<ResolversTypes['TypeaheadResponse'], ParentType, ContextType, RequireFields<QueryPaginatedTypeaheadSuggestionsArgs, 'input' | 'pagingContext'>>;
  percentageChange?: Resolver<ResolversTypes['MetricsPercentageChange'], ParentType, ContextType, RequireFields<QueryPercentageChangeArgs, 'input'>>;
  percentageTotal?: Resolver<ResolversTypes['MetricsPercentageTotal'], ParentType, ContextType, RequireFields<QueryPercentageTotalArgs, 'input'>>;
  pickingOrders?: Resolver<Array<Maybe<ResolversTypes['PickingOrder']>>, ParentType, ContextType, RequireFields<QueryPickingOrdersArgs, 'input'>>;
  priceLines?: Resolver<Array<ResolversTypes['String']>, ParentType, ContextType, RequireFields<QueryPriceLinesArgs, 'input'>>;
  productDetails?: Resolver<ResolversTypes['ProductDetails'], ParentType, ContextType, RequireFields<QueryProductDetailsArgs, 'productId'>>;
  productImageUrl?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType, RequireFields<QueryProductImageUrlArgs, 'input'>>;
  productPrices?: Resolver<ResolversTypes['ProductPriceResponse'], ParentType, ContextType, RequireFields<QueryProductPricesArgs, 'input'>>;
  searchProductsEclipse?: Resolver<ResolversTypes['ProductSearchResult'], ParentType, ContextType, RequireFields<QuerySearchProductsEclipseArgs, 'input'>>;
  searchProductsKourier?: Resolver<ResolversTypes['ProductSearchKourierResponse'], ParentType, ContextType, RequireFields<QuerySearchProductsKourierArgs, 'input'>>;
  shippingDetails?: Resolver<ResolversTypes['ShippingDetailsKourierResponse'], ParentType, ContextType, RequireFields<QueryShippingDetailsArgs, 'input'>>;
  specialPrices?: Resolver<ResolversTypes['SpecialPriceResponse'], ParentType, ContextType, RequireFields<QuerySpecialPricesArgs, 'input'>>;
  totalOverview?: Resolver<ResolversTypes['MetricsOverview'], ParentType, ContextType, RequireFields<QueryTotalOverviewArgs, 'input'>>;
  totalUsage?: Resolver<ResolversTypes['MetricsUsage'], ParentType, ContextType, RequireFields<QueryTotalUsageArgs, 'input'>>;
  typeaheadSuggestions?: Resolver<ResolversTypes['TypeaheadResponse'], ParentType, ContextType, RequireFields<QueryTypeaheadSuggestionsArgs, 'input'>>;
  userBranch?: Resolver<ResolversTypes['UserResponse'], ParentType, ContextType, RequireFields<QueryUserBranchArgs, 'username'>>;
  userPicks?: Resolver<Array<Maybe<ResolversTypes['UserPick']>>, ParentType, ContextType, RequireFields<QueryUserPicksArgs, 'input'>>;
  validateBranch?: Resolver<ResolversTypes['ValidateBranchResponse'], ParentType, ContextType, RequireFields<QueryValidateBranchArgs, 'input'>>;
  varianceDetails?: Resolver<ResolversTypes['DetailedVarianceResponse'], ParentType, ContextType>;
  varianceLocation?: Resolver<ResolversTypes['VarianceLocationResponse'], ParentType, ContextType, RequireFields<QueryVarianceLocationArgs, 'id'>>;
  varianceLocations?: Resolver<ResolversTypes['VarianceLocationsResponse'], ParentType, ContextType>;
  varianceNextLocation?: Resolver<ResolversTypes['VarianceNextLocationResponse'], ParentType, ContextType, RequireFields<QueryVarianceNextLocationArgs, 'id'>>;
  varianceSummary?: Resolver<ResolversTypes['VarianceSummaryResponse'], ParentType, ContextType>;
  writeIn?: Resolver<ResolversTypes['WriteIn'], ParentType, ContextType, RequireFields<QueryWriteInArgs, 'id'>>;
  writeIns?: Resolver<ResolversTypes['WriteInsResponse'], ParentType, ContextType, RequireFields<QueryWriteInsArgs, 'options'>>;
}>;

export type CustomerResponseResolvers<ContextType = any, ParentType extends ResolversParentTypes['CustomerResponse'] = ResolversParentTypes['CustomerResponse']> = ResolversObject<{
  id?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  homeBranch?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  name?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  isBillTo?: Resolver<Maybe<ResolversTypes['Boolean']>, ParentType, ContextType>;
  isShipTo?: Resolver<Maybe<ResolversTypes['Boolean']>, ParentType, ContextType>;
  addressLine1?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  addressLine2?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  city?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  state?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  postalCode?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  countryCode?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type CountResolvers<ContextType = any, ParentType extends ResolversParentTypes['Count'] = ResolversParentTypes['Count']> = ResolversObject<{
  id?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  branch?: Resolver<ResolversTypes['Branch'], ParentType, ContextType>;
  erpSystem?: Resolver<ResolversTypes['ErpSystem'], ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type CountWithStatusResolvers<ContextType = any, ParentType extends ResolversParentTypes['CountWithStatus'] = ResolversParentTypes['CountWithStatus']> = ResolversObject<{
  id?: Resolver<Maybe<ResolversTypes['ID']>, ParentType, ContextType>;
  branchId?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  countId?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  branchName?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  status?: Resolver<Maybe<ResolversTypes['CountStatus']>, ParentType, ContextType>;
  errorMessage?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  totalProducts?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  createdAt?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type BranchResolvers<ContextType = any, ParentType extends ResolversParentTypes['Branch'] = ResolversParentTypes['Branch']> = ResolversObject<{
  id?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  name?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type LocationResolvers<ContextType = any, ParentType extends ResolversParentTypes['Location'] = ResolversParentTypes['Location']> = ResolversObject<{
  id?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  totalProducts?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  totalCounted?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  committed?: Resolver<ResolversTypes['Boolean'], ParentType, ContextType>;
  items?: Resolver<Array<ResolversTypes['LocationItem']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type LocationItemResolvers<ContextType = any, ParentType extends ResolversParentTypes['LocationItem'] = ResolversParentTypes['LocationItem']> = ResolversObject<{
  id?: Resolver<Maybe<ResolversTypes['ID']>, ParentType, ContextType>;
  locationId?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  prodDesc?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  prodNum?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  tagNum?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  catalogNum?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  uom?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  quantity?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  status?: Resolver<Maybe<ResolversTypes['LocationItemStatus']>, ParentType, ContextType>;
  sequence?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  productImageUrl?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  controlNum?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type LocationSummaryResolvers<ContextType = any, ParentType extends ResolversParentTypes['LocationSummary'] = ResolversParentTypes['LocationSummary']> = ResolversObject<{
  id?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  committed?: Resolver<ResolversTypes['Boolean'], ParentType, ContextType>;
  totalProducts?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  totalCounted?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type LocationsResponseResolvers<ContextType = any, ParentType extends ResolversParentTypes['LocationsResponse'] = ResolversParentTypes['LocationsResponse']> = ResolversObject<{
  totalLocations?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  totalCounted?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  content?: Resolver<Array<ResolversTypes['LocationSummary']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type NextLocationResponseResolvers<ContextType = any, ParentType extends ResolversParentTypes['NextLocationResponse'] = ResolversParentTypes['NextLocationResponse']> = ResolversObject<{
  locationId?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type WriteInResolvers<ContextType = any, ParentType extends ResolversParentTypes['WriteIn'] = ResolversParentTypes['WriteIn']> = ResolversObject<{
  id?: Resolver<ResolversTypes['ID'], ParentType, ContextType>;
  locationId?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  catalogNum?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  upcNum?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  description?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  uom?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  quantity?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  comment?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  createdBy?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  createdAt?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  updatedBy?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  updatedAt?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  resolved?: Resolver<ResolversTypes['Boolean'], ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type MetricsCompletionResolvers<ContextType = any, ParentType extends ResolversParentTypes['MetricsCompletion'] = ResolversParentTypes['MetricsCompletion']> = ResolversObject<{
  status?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  total?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  needToBeCounted?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  counted?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  timeStarted?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  timeEnded?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  countId?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  location?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  branchId?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type PageableSortResolvers<ContextType = any, ParentType extends ResolversParentTypes['PageableSort'] = ResolversParentTypes['PageableSort']> = ResolversObject<{
  sorted?: Resolver<ResolversTypes['Boolean'], ParentType, ContextType>;
  unsorted?: Resolver<ResolversTypes['Boolean'], ParentType, ContextType>;
  empty?: Resolver<ResolversTypes['Boolean'], ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type PageableResolvers<ContextType = any, ParentType extends ResolversParentTypes['Pageable'] = ResolversParentTypes['Pageable']> = ResolversObject<{
  sort?: Resolver<ResolversTypes['PageableSort'], ParentType, ContextType>;
  pageNumber?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  pageSize?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  offset?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  paged?: Resolver<ResolversTypes['Boolean'], ParentType, ContextType>;
  unpaged?: Resolver<ResolversTypes['Boolean'], ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type WriteInsResponseResolvers<ContextType = any, ParentType extends ResolversParentTypes['WriteInsResponse'] = ResolversParentTypes['WriteInsResponse']> = ResolversObject<{
  content?: Resolver<Array<ResolversTypes['WriteIn']>, ParentType, ContextType>;
  pageable?: Resolver<ResolversTypes['Pageable'], ParentType, ContextType>;
  last?: Resolver<ResolversTypes['Boolean'], ParentType, ContextType>;
  totalPages?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  totalElements?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  first?: Resolver<ResolversTypes['Boolean'], ParentType, ContextType>;
  numberOfElements?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  number?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  size?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  sort?: Resolver<ResolversTypes['PageableSort'], ParentType, ContextType>;
  empty?: Resolver<ResolversTypes['Boolean'], ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type WriteInMutationResponseResolvers<ContextType = any, ParentType extends ResolversParentTypes['WriteInMutationResponse'] = ResolversParentTypes['WriteInMutationResponse']> = ResolversObject<{
  success?: Resolver<ResolversTypes['Boolean'], ParentType, ContextType>;
  message?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  content?: Resolver<ResolversTypes['WriteIn'], ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type MetricsCompletionResponseResolvers<ContextType = any, ParentType extends ResolversParentTypes['MetricsCompletionResponse'] = ResolversParentTypes['MetricsCompletionResponse']> = ResolversObject<{
  success?: Resolver<ResolversTypes['Boolean'], ParentType, ContextType>;
  message?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  content?: Resolver<ResolversTypes['MetricsCompletion'], ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type AddToCountMutationResponseResolvers<ContextType = any, ParentType extends ResolversParentTypes['AddToCountMutationResponse'] = ResolversParentTypes['AddToCountMutationResponse']> = ResolversObject<{
  success?: Resolver<ResolversTypes['Boolean'], ParentType, ContextType>;
  message?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  item?: Resolver<ResolversTypes['LocationItem'], ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type DeleteMultipleCountResponseResolvers<ContextType = any, ParentType extends ResolversParentTypes['DeleteMultipleCountResponse'] = ResolversParentTypes['DeleteMultipleCountResponse']> = ResolversObject<{
  deletedCounts?: Resolver<Array<ResolversTypes['DeleteCountResponse']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type DeleteCountResponseResolvers<ContextType = any, ParentType extends ResolversParentTypes['DeleteCountResponse'] = ResolversParentTypes['DeleteCountResponse']> = ResolversObject<{
  countLocations?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  countLocationItems?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  countLocationItemQuantities?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  varianceCountLocationItemQuantities?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  writeIns?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type CountMutationResponseResolvers<ContextType = any, ParentType extends ResolversParentTypes['CountMutationResponse'] = ResolversParentTypes['CountMutationResponse']> = ResolversObject<{
  success?: Resolver<ResolversTypes['Boolean'], ParentType, ContextType>;
  message?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type MetricsBranchResolvers<ContextType = any, ParentType extends ResolversParentTypes['MetricsBranch'] = ResolversParentTypes['MetricsBranch']> = ResolversObject<{
  id?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  city?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  state?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  userCount?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  loginCount?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type MetricsChangeResolvers<ContextType = any, ParentType extends ResolversParentTypes['MetricsChange'] = ResolversParentTypes['MetricsChange']> = ResolversObject<{
  metric?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  quantity?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  percentageChange?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type MetricsDivisionResolvers<ContextType = any, ParentType extends ResolversParentTypes['MetricsDivision'] = ResolversParentTypes['MetricsDivision']> = ResolversObject<{
  division?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  userCount?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  loginCount?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  branchCount?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  branches?: Resolver<Maybe<Array<Maybe<ResolversTypes['MetricsBranch']>>>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type MetricsOverviewResolvers<ContextType = any, ParentType extends ResolversParentTypes['MetricsOverview'] = ResolversParentTypes['MetricsOverview']> = ResolversObject<{
  type?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  response?: Resolver<Maybe<Array<Maybe<ResolversTypes['MetricsChange']>>>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type MetricsPercentageChangeDivisionResolvers<ContextType = any, ParentType extends ResolversParentTypes['MetricsPercentageChangeDivision'] = ResolversParentTypes['MetricsPercentageChangeDivision']> = ResolversObject<{
  division?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  userChange?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  loginChange?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  branchChange?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type MetricsPercentageChangeResolvers<ContextType = any, ParentType extends ResolversParentTypes['MetricsPercentageChange'] = ResolversParentTypes['MetricsPercentageChange']> = ResolversObject<{
  type?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  response?: Resolver<Maybe<Array<Maybe<ResolversTypes['MetricsPercentageChangeDivision']>>>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type MetricsPercentageTotalDivisionResolvers<ContextType = any, ParentType extends ResolversParentTypes['MetricsPercentageTotalDivision'] = ResolversParentTypes['MetricsPercentageTotalDivision']> = ResolversObject<{
  division?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  userCount?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  userPercentage?: Resolver<Maybe<ResolversTypes['Float']>, ParentType, ContextType>;
  loginCount?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  loginPercentage?: Resolver<Maybe<ResolversTypes['Float']>, ParentType, ContextType>;
  branchCount?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  branchPercentage?: Resolver<Maybe<ResolversTypes['Float']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type MetricsPercentageTotalResolvers<ContextType = any, ParentType extends ResolversParentTypes['MetricsPercentageTotal'] = ResolversParentTypes['MetricsPercentageTotal']> = ResolversObject<{
  type?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  response?: Resolver<Maybe<Array<Maybe<ResolversTypes['MetricsPercentageTotalDivision']>>>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type MetricsLoginResponseResolvers<ContextType = any, ParentType extends ResolversParentTypes['MetricsLoginResponse'] = ResolversParentTypes['MetricsLoginResponse']> = ResolversObject<{
  success?: Resolver<ResolversTypes['Boolean'], ParentType, ContextType>;
  message?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type MetricsUsageResolvers<ContextType = any, ParentType extends ResolversParentTypes['MetricsUsage'] = ResolversParentTypes['MetricsUsage']> = ResolversObject<{
  type?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  response?: Resolver<Maybe<Array<Maybe<ResolversTypes['MetricsDivision']>>>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type PickingOrderResolvers<ContextType = any, ParentType extends ResolversParentTypes['PickingOrder'] = ResolversParentTypes['PickingOrder']> = ResolversObject<{
  orderId?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  generationId?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  invoiceId?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  branchId?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  pickGroup?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  assignedUserId?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  billTo?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  shipTo?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  shipToName?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  pickCount?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  shipVia?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  isFromMultipleZones?: Resolver<Maybe<ResolversTypes['Boolean']>, ParentType, ContextType>;
  taskWeight?: Resolver<Maybe<ResolversTypes['Float']>, ParentType, ContextType>;
  taskState?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type UserPickResolvers<ContextType = any, ParentType extends ResolversParentTypes['UserPick'] = ResolversParentTypes['UserPick']> = ResolversObject<{
  productId?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  productImageUrl?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  description?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  quantity?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  uom?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  locationType?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  location?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  lot?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  splitId?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  orderId?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  generationId?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  lineId?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  shipVia?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  tote?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  userId?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  branchId?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  cutDetail?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  cutGroup?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  isParallelCut?: Resolver<Maybe<ResolversTypes['Boolean']>, ParentType, ContextType>;
  warehouseID?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  isLot?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  isSerial?: Resolver<ResolversTypes['Boolean'], ParentType, ContextType>;
  pickGroup?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type ProductSerialNumberResultsResolvers<ContextType = any, ParentType extends ResolversParentTypes['ProductSerialNumberResults'] = ResolversParentTypes['ProductSerialNumberResults']> = ResolversObject<{
  results?: Resolver<Array<Maybe<ResolversTypes['ProductSerialNumbers']>>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type ProductSerialNumbersResolvers<ContextType = any, ParentType extends ResolversParentTypes['ProductSerialNumbers'] = ResolversParentTypes['ProductSerialNumbers']> = ResolversObject<{
  productId?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  orderId?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  generationId?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  invoiceId?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  quantity?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  description?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  location?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  warehouseId?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  serialList?: Resolver<Array<Maybe<ResolversTypes['SerialList']>>, ParentType, ContextType>;
  nonStockSerialNumbers?: Resolver<Array<Maybe<ResolversTypes['SerialList']>>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type SerialListResolvers<ContextType = any, ParentType extends ResolversParentTypes['SerialList'] = ResolversParentTypes['SerialList']> = ResolversObject<{
  line?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  serial?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type StagePickTaskResponseResolvers<ContextType = any, ParentType extends ResolversParentTypes['StagePickTaskResponse'] = ResolversParentTypes['StagePickTaskResponse']> = ResolversObject<{
  success?: Resolver<ResolversTypes['Boolean'], ParentType, ContextType>;
  message?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type StagePickTotePackagesResponseResolvers<ContextType = any, ParentType extends ResolversParentTypes['StagePickTotePackagesResponse'] = ResolversParentTypes['StagePickTotePackagesResponse']> = ResolversObject<{
  success?: Resolver<ResolversTypes['Boolean'], ParentType, ContextType>;
  message?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type CloseTaskResponseResolvers<ContextType = any, ParentType extends ResolversParentTypes['CloseTaskResponse'] = ResolversParentTypes['CloseTaskResponse']> = ResolversObject<{
  success?: Resolver<ResolversTypes['Boolean'], ParentType, ContextType>;
  message?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type SplitQuantityResponseResolvers<ContextType = any, ParentType extends ResolversParentTypes['SplitQuantityResponse'] = ResolversParentTypes['SplitQuantityResponse']> = ResolversObject<{
  productId?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  isSplit?: Resolver<ResolversTypes['Boolean'], ParentType, ContextType>;
  orderId?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  invalidSerialNums?: Resolver<Maybe<Array<Maybe<ResolversTypes['SerialList']>>>, ParentType, ContextType>;
  successStatus?: Resolver<ResolversTypes['Boolean'], ParentType, ContextType>;
  errorMessage?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type CloseOrderResponseResolvers<ContextType = any, ParentType extends ResolversParentTypes['closeOrderResponse'] = ResolversParentTypes['closeOrderResponse']> = ResolversObject<{
  status?: Resolver<ResolversTypes['Boolean'], ParentType, ContextType>;
  orderId?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  pickerId?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  errorCode?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  errorMessage?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  orderLocked?: Resolver<ResolversTypes['Boolean'], ParentType, ContextType>;
  moreToPick?: Resolver<ResolversTypes['Boolean'], ParentType, ContextType>;
  stillPicking?: Resolver<ResolversTypes['Boolean'], ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type ShippingDetailsKourierResponseResolvers<ContextType = any, ParentType extends ResolversParentTypes['ShippingDetailsKourierResponse'] = ResolversParentTypes['ShippingDetailsKourierResponse']> = ResolversObject<{
  shippingtext?: Resolver<Array<ResolversTypes['ShippingDetailsKourierResult']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type ShippingDetailsKourierResultResolvers<ContextType = any, ParentType extends ResolversParentTypes['ShippingDetailsKourierResult'] = ResolversParentTypes['ShippingDetailsKourierResult']> = ResolversObject<{
  invoiceNumber?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  status?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  errorCode?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  errorMessage?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  shippingInstructions?: Resolver<Maybe<Array<ResolversTypes['String']>>, ParentType, ContextType>;
  noBackorder?: Resolver<ResolversTypes['Boolean'], ParentType, ContextType>;
  noSort?: Resolver<ResolversTypes['Boolean'], ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type ValidateBranchResponseResolvers<ContextType = any, ParentType extends ResolversParentTypes['ValidateBranchResponse'] = ResolversParentTypes['ValidateBranchResponse']> = ResolversObject<{
  isValid?: Resolver<ResolversTypes['Boolean'], ParentType, ContextType>;
  branch?: Resolver<Maybe<ResolversTypes['BranchResponse']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type BranchResponseResolvers<ContextType = any, ParentType extends ResolversParentTypes['BranchResponse'] = ResolversParentTypes['BranchResponse']> = ResolversObject<{
  branchId?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  branchName?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type TypeaheadResponseResolvers<ContextType = any, ParentType extends ResolversParentTypes['TypeaheadResponse'] = ResolversParentTypes['TypeaheadResponse']> = ResolversObject<{
  meta?: Resolver<ResolversTypes['TypeaheadMeta'], ParentType, ContextType>;
  results?: Resolver<Array<ResolversTypes['TypeaheadResult']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type TypeaheadMetaResolvers<ContextType = any, ParentType extends ResolversParentTypes['TypeaheadMeta'] = ResolversParentTypes['TypeaheadMeta']> = ResolversObject<{
  page?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  nextPage?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  pageCount?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  pageSize?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  resultCount?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  orderBy?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  orderDirection?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  entity?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  query?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type TypeaheadResultResolvers<ContextType = any, ParentType extends ResolversParentTypes['TypeaheadResult'] = ResolversParentTypes['TypeaheadResult']> = ResolversObject<{
  id?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  displayName?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type SpecialPriceMetaResolvers<ContextType = any, ParentType extends ResolversParentTypes['SpecialPriceMeta'] = ResolversParentTypes['SpecialPriceMeta']> = ResolversObject<{
  page?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  nextPage?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  pageCount?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  pageSize?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  resultCount?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  orderBy?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  orderDirection?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  customerId?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  productId?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  priceLine?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type SpecialPriceResponseResolvers<ContextType = any, ParentType extends ResolversParentTypes['SpecialPriceResponse'] = ResolversParentTypes['SpecialPriceResponse']> = ResolversObject<{
  meta?: Resolver<ResolversTypes['SpecialPriceMeta'], ParentType, ContextType>;
  results?: Resolver<Array<ResolversTypes['SpecialPrice']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type SpecialPriceResolvers<ContextType = any, ParentType extends ResolversParentTypes['SpecialPrice'] = ResolversParentTypes['SpecialPrice']> = ResolversObject<{
  productId?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  customerId?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  branch?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  customerDisplayName?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  imageUrl?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  manufacturer?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  displayName?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  manufacturerReferenceNumber?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  salesperson?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  priceLine?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  customerSalesQuantity?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  prices?: Resolver<Array<ResolversTypes['Price']>, ParentType, ContextType>;
  territory?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type PriceResolvers<ContextType = any, ParentType extends ResolversParentTypes['Price'] = ResolversParentTypes['Price']> = ResolversObject<{
  type?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  value?: Resolver<ResolversTypes['Float'], ParentType, ContextType>;
  currency?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  displayName?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type UploadSpecialPriceResponseResolvers<ContextType = any, ParentType extends ResolversParentTypes['UploadSpecialPriceResponse'] = ResolversParentTypes['UploadSpecialPriceResponse']> = ResolversObject<{
  successfulUploads?: Resolver<Maybe<Array<ResolversTypes['SuccessfulUpload']>>, ParentType, ContextType>;
  successfulCreates?: Resolver<Maybe<Array<ResolversTypes['SuccessfulCreate']>>, ParentType, ContextType>;
  failedUpdateSuggestions?: Resolver<Maybe<Array<ResolversTypes['FailedPriceSuggestion']>>, ParentType, ContextType>;
  failedCreateSuggestions?: Resolver<Maybe<Array<ResolversTypes['FailedPriceSuggestion']>>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type SuccessfulUploadResolvers<ContextType = any, ParentType extends ResolversParentTypes['SuccessfulUpload'] = ResolversParentTypes['SuccessfulUpload']> = ResolversObject<{
  uploadedPath?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  uploadedName?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type SuccessfulCreateResolvers<ContextType = any, ParentType extends ResolversParentTypes['SuccessfulCreate'] = ResolversParentTypes['SuccessfulCreate']> = ResolversObject<{
  uploadedPath?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  uploadedName?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type FailedPriceSuggestionResolvers<ContextType = any, ParentType extends ResolversParentTypes['FailedPriceSuggestion'] = ResolversParentTypes['FailedPriceSuggestion']> = ResolversObject<{
  customerId?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  productId?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  branch?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  cmpPrice?: Resolver<ResolversTypes['Float'], ParentType, ContextType>;
  priceCategory?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  newPrice?: Resolver<ResolversTypes['Float'], ParentType, ContextType>;
  changeWriterDisplayName?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  changeWriterId?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type ProductPriceResponseResolvers<ContextType = any, ParentType extends ResolversParentTypes['ProductPriceResponse'] = ResolversParentTypes['ProductPriceResponse']> = ResolversObject<{
  productId?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  erpBranchNum?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  cmp?: Resolver<Maybe<ResolversTypes['Float']>, ParentType, ContextType>;
  stdCost?: Resolver<Maybe<ResolversTypes['Float']>, ParentType, ContextType>;
  uom?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  rateCardPrice?: Resolver<Maybe<ResolversTypes['Float']>, ParentType, ContextType>;
  rateCardName?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  matchedBranch?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  matchedClass?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  matchedGroup?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  matrixId?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  customerId?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  correlationId?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type StoreStockResolvers<ContextType = any, ParentType extends ResolversParentTypes['StoreStock'] = ResolversParentTypes['StoreStock']> = ResolversObject<{
  branchName?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  address?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  availability?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type TechDocResolvers<ContextType = any, ParentType extends ResolversParentTypes['TechDoc'] = ResolversParentTypes['TechDoc']> = ResolversObject<{
  name?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  url?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type TechSpecResolvers<ContextType = any, ParentType extends ResolversParentTypes['TechSpec'] = ResolversParentTypes['TechSpec']> = ResolversObject<{
  name?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  value?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type ImageUrlsResolvers<ContextType = any, ParentType extends ResolversParentTypes['ImageUrls'] = ResolversParentTypes['ImageUrls']> = ResolversObject<{
  thumb?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  small?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  medium?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  large?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type PackageDimensionsResolvers<ContextType = any, ParentType extends ResolversParentTypes['PackageDimensions'] = ResolversParentTypes['PackageDimensions']> = ResolversObject<{
  height?: Resolver<Maybe<ResolversTypes['Float']>, ParentType, ContextType>;
  length?: Resolver<Maybe<ResolversTypes['Float']>, ParentType, ContextType>;
  volume?: Resolver<Maybe<ResolversTypes['Float']>, ParentType, ContextType>;
  volumeUnitOfMeasure?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  width?: Resolver<Maybe<ResolversTypes['Float']>, ParentType, ContextType>;
  weight?: Resolver<Maybe<ResolversTypes['Float']>, ParentType, ContextType>;
  weightUnitOfMeasure?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type StockResolvers<ContextType = any, ParentType extends ResolversParentTypes['Stock'] = ResolversParentTypes['Stock']> = ResolversObject<{
  homeBranch?: Resolver<Maybe<ResolversTypes['StoreStock']>, ParentType, ContextType>;
  otherBranches?: Resolver<Maybe<Array<Maybe<ResolversTypes['StoreStock']>>>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type ProductSearchResultResolvers<ContextType = any, ParentType extends ResolversParentTypes['ProductSearchResult'] = ResolversParentTypes['ProductSearchResult']> = ResolversObject<{
  pagination?: Resolver<ResolversTypes['Pagination'], ParentType, ContextType>;
  products?: Resolver<Array<Maybe<ResolversTypes['Product']>>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type ProductResolvers<ContextType = any, ParentType extends ResolversParentTypes['Product'] = ResolversParentTypes['Product']> = ResolversObject<{
  id?: Resolver<Maybe<ResolversTypes['ID']>, ParentType, ContextType>;
  name?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  productNumber?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  productType?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  taxonomy?: Resolver<Maybe<Array<Maybe<ResolversTypes['String']>>>, ParentType, ContextType>;
  manufacturerName?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  manufacturerNumber?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  price?: Resolver<Maybe<ResolversTypes['Float']>, ParentType, ContextType>;
  stock?: Resolver<Maybe<ResolversTypes['Stock']>, ParentType, ContextType>;
  technicalDocuments?: Resolver<Maybe<Array<Maybe<ResolversTypes['TechDoc']>>>, ParentType, ContextType>;
  environmentalOptions?: Resolver<Maybe<Array<Maybe<ResolversTypes['String']>>>, ParentType, ContextType>;
  upc?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  unspsc?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  seriesModelFigureNumber?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  productOverview?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  featuresAndBenefits?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  techSpecifications?: Resolver<Maybe<Array<Maybe<ResolversTypes['TechSpec']>>>, ParentType, ContextType>;
  imageUrls?: Resolver<Maybe<ResolversTypes['ImageUrls']>, ParentType, ContextType>;
  packageDimensions?: Resolver<Maybe<ResolversTypes['PackageDimensions']>, ParentType, ContextType>;
  productImageUrl?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type PaginationResolvers<ContextType = any, ParentType extends ResolversParentTypes['Pagination'] = ResolversParentTypes['Pagination']> = ResolversObject<{
  pageSize?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  currentPage?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  totalItemCount?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type ProductSearchKourierResultResolvers<ContextType = any, ParentType extends ResolversParentTypes['ProductSearchKourierResult'] = ResolversParentTypes['ProductSearchKourierResult']> = ResolversObject<{
  errorCode?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  errorMessage?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  productIdCount?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  products?: Resolver<Maybe<Array<Maybe<ResolversTypes['KourierProduct']>>>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type KourierProductResolvers<ContextType = any, ParentType extends ResolversParentTypes['KourierProduct'] = ResolversParentTypes['KourierProduct']> = ResolversObject<{
  productId?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  displayField?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  productNumber?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  upc?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  productImageUrl?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type ProductSearchKourierResponseResolvers<ContextType = any, ParentType extends ResolversParentTypes['ProductSearchKourierResponse'] = ResolversParentTypes['ProductSearchKourierResponse']> = ResolversObject<{
  prodSearch?: Resolver<Array<ResolversTypes['ProductSearchKourierResult']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type ProductDetailsResolvers<ContextType = any, ParentType extends ResolversParentTypes['ProductDetails'] = ResolversParentTypes['ProductDetails']> = ResolversObject<{
  description?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  catalogNumber?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  upc?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type UserResponseResolvers<ContextType = any, ParentType extends ResolversParentTypes['UserResponse'] = ResolversParentTypes['UserResponse']> = ResolversObject<{
  username?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  branch?: Resolver<Maybe<Array<Maybe<ResolversTypes['String']>>>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type BranchUsersListResponseResolvers<ContextType = any, ParentType extends ResolversParentTypes['BranchUsersListResponse'] = ResolversParentTypes['BranchUsersListResponse']> = ResolversObject<{
  branchId?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  assignedUsers?: Resolver<Maybe<Array<Maybe<ResolversTypes['String']>>>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type ErrorResponseResolvers<ContextType = any, ParentType extends ResolversParentTypes['ErrorResponse'] = ResolversParentTypes['ErrorResponse']> = ResolversObject<{
  code?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  message?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  details?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type UserBranchResponseResolvers<ContextType = any, ParentType extends ResolversParentTypes['UserBranchResponse'] = ResolversParentTypes['UserBranchResponse']> = ResolversObject<{
  success?: Resolver<ResolversTypes['Boolean'], ParentType, ContextType>;
  error?: Resolver<Maybe<ResolversTypes['ErrorResponse']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type VarianceSummaryResponseResolvers<ContextType = any, ParentType extends ResolversParentTypes['VarianceSummaryResponse'] = ResolversParentTypes['VarianceSummaryResponse']> = ResolversObject<{
  netTotalCost?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  grossTotalCost?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  locationQuantity?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  productQuantity?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  differenceQuantity?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  differencePercentage?: Resolver<Maybe<ResolversTypes['Float']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type VarianceDetailsResponseResolvers<ContextType = any, ParentType extends ResolversParentTypes['VarianceDetailsResponse'] = ResolversParentTypes['VarianceDetailsResponse']> = ResolversObject<{
  success?: Resolver<ResolversTypes['Boolean'], ParentType, ContextType>;
  message?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type VarianceLocationResponseResolvers<ContextType = any, ParentType extends ResolversParentTypes['VarianceLocationResponse'] = ResolversParentTypes['VarianceLocationResponse']> = ResolversObject<{
  id?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  totalProducts?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  totalCounted?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  committed?: Resolver<ResolversTypes['Boolean'], ParentType, ContextType>;
  netVarianceCost?: Resolver<ResolversTypes['Float'], ParentType, ContextType>;
  grossVarianceCost?: Resolver<ResolversTypes['Float'], ParentType, ContextType>;
  items?: Resolver<Array<ResolversTypes['VarianceLocationItem']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type VarianceLocationItemResolvers<ContextType = any, ParentType extends ResolversParentTypes['VarianceLocationItem'] = ResolversParentTypes['VarianceLocationItem']> = ResolversObject<{
  id?: Resolver<Maybe<ResolversTypes['ID']>, ParentType, ContextType>;
  locationId?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  prodDesc?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  prodNum?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  tagNum?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  catalogNum?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  uom?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  quantity?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  status?: Resolver<Maybe<ResolversTypes['LocationItemStatus']>, ParentType, ContextType>;
  sequence?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  varianceCost?: Resolver<Maybe<ResolversTypes['Float']>, ParentType, ContextType>;
  varianceStatus?: Resolver<Maybe<ResolversTypes['VarianceItemStatus']>, ParentType, ContextType>;
  productImageUrl?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type VarianceNextLocationResponseResolvers<ContextType = any, ParentType extends ResolversParentTypes['VarianceNextLocationResponse'] = ResolversParentTypes['VarianceNextLocationResponse']> = ResolversObject<{
  locationId?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type VarianceLocationsResponseResolvers<ContextType = any, ParentType extends ResolversParentTypes['VarianceLocationsResponse'] = ResolversParentTypes['VarianceLocationsResponse']> = ResolversObject<{
  totalLocations?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  content?: Resolver<Array<ResolversTypes['VarianceLocationSummary']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type VarianceLocationSummaryResolvers<ContextType = any, ParentType extends ResolversParentTypes['VarianceLocationSummary'] = ResolversParentTypes['VarianceLocationSummary']> = ResolversObject<{
  id?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  totalProducts?: Resolver<ResolversTypes['Int'], ParentType, ContextType>;
  netVarianceCost?: Resolver<ResolversTypes['Float'], ParentType, ContextType>;
  grossVarianceCost?: Resolver<ResolversTypes['Float'], ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type DetailedVarianceResponseResolvers<ContextType = any, ParentType extends ResolversParentTypes['DetailedVarianceResponse'] = ResolversParentTypes['DetailedVarianceResponse']> = ResolversObject<{
  counts?: Resolver<Maybe<Array<Maybe<ResolversTypes['VarianceDetails']>>>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type VarianceDetailsResolvers<ContextType = any, ParentType extends ResolversParentTypes['VarianceDetails'] = ResolversParentTypes['VarianceDetails']> = ResolversObject<{
  location?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  erpProductID?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  productDescription?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  countQty?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  onHandQty?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  qtyDeviance?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  onHandCost?: Resolver<Maybe<ResolversTypes['Float']>, ParentType, ContextType>;
  percentDeviance?: Resolver<Maybe<ResolversTypes['Float']>, ParentType, ContextType>;
  countedCost?: Resolver<Maybe<ResolversTypes['Float']>, ParentType, ContextType>;
  notCountedFlag?: Resolver<Maybe<ResolversTypes['Boolean']>, ParentType, ContextType>;
  recount1Qty?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  recount2Qty?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  recount3Qty?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type Resolvers<ContextType = any> = ResolversObject<{
  LinkAccountResponse?: LinkAccountResponseResolvers<ContextType>;
  Mutation?: MutationResolvers<ContextType>;
  CustomerSearchResponse?: CustomerSearchResponseResolvers<ContextType>;
  EclipseSearchMetadata?: EclipseSearchMetadataResolvers<ContextType>;
  CustomerSearchResult?: CustomerSearchResultResolvers<ContextType>;
  ShipToId?: ShipToIdResolvers<ContextType>;
  Query?: QueryResolvers<ContextType>;
  CustomerResponse?: CustomerResponseResolvers<ContextType>;
  Count?: CountResolvers<ContextType>;
  CountWithStatus?: CountWithStatusResolvers<ContextType>;
  Branch?: BranchResolvers<ContextType>;
  Location?: LocationResolvers<ContextType>;
  LocationItem?: LocationItemResolvers<ContextType>;
  LocationSummary?: LocationSummaryResolvers<ContextType>;
  LocationsResponse?: LocationsResponseResolvers<ContextType>;
  NextLocationResponse?: NextLocationResponseResolvers<ContextType>;
  WriteIn?: WriteInResolvers<ContextType>;
  MetricsCompletion?: MetricsCompletionResolvers<ContextType>;
  PageableSort?: PageableSortResolvers<ContextType>;
  Pageable?: PageableResolvers<ContextType>;
  WriteInsResponse?: WriteInsResponseResolvers<ContextType>;
  WriteInMutationResponse?: WriteInMutationResponseResolvers<ContextType>;
  MetricsCompletionResponse?: MetricsCompletionResponseResolvers<ContextType>;
  AddToCountMutationResponse?: AddToCountMutationResponseResolvers<ContextType>;
  DeleteMultipleCountResponse?: DeleteMultipleCountResponseResolvers<ContextType>;
  DeleteCountResponse?: DeleteCountResponseResolvers<ContextType>;
  CountMutationResponse?: CountMutationResponseResolvers<ContextType>;
  MetricsBranch?: MetricsBranchResolvers<ContextType>;
  MetricsChange?: MetricsChangeResolvers<ContextType>;
  MetricsDivision?: MetricsDivisionResolvers<ContextType>;
  MetricsOverview?: MetricsOverviewResolvers<ContextType>;
  MetricsPercentageChangeDivision?: MetricsPercentageChangeDivisionResolvers<ContextType>;
  MetricsPercentageChange?: MetricsPercentageChangeResolvers<ContextType>;
  MetricsPercentageTotalDivision?: MetricsPercentageTotalDivisionResolvers<ContextType>;
  MetricsPercentageTotal?: MetricsPercentageTotalResolvers<ContextType>;
  MetricsLoginResponse?: MetricsLoginResponseResolvers<ContextType>;
  MetricsUsage?: MetricsUsageResolvers<ContextType>;
  PickingOrder?: PickingOrderResolvers<ContextType>;
  UserPick?: UserPickResolvers<ContextType>;
  ProductSerialNumberResults?: ProductSerialNumberResultsResolvers<ContextType>;
  ProductSerialNumbers?: ProductSerialNumbersResolvers<ContextType>;
  SerialList?: SerialListResolvers<ContextType>;
  StagePickTaskResponse?: StagePickTaskResponseResolvers<ContextType>;
  StagePickTotePackagesResponse?: StagePickTotePackagesResponseResolvers<ContextType>;
  CloseTaskResponse?: CloseTaskResponseResolvers<ContextType>;
  SplitQuantityResponse?: SplitQuantityResponseResolvers<ContextType>;
  closeOrderResponse?: CloseOrderResponseResolvers<ContextType>;
  ShippingDetailsKourierResponse?: ShippingDetailsKourierResponseResolvers<ContextType>;
  ShippingDetailsKourierResult?: ShippingDetailsKourierResultResolvers<ContextType>;
  ValidateBranchResponse?: ValidateBranchResponseResolvers<ContextType>;
  BranchResponse?: BranchResponseResolvers<ContextType>;
  TypeaheadResponse?: TypeaheadResponseResolvers<ContextType>;
  TypeaheadMeta?: TypeaheadMetaResolvers<ContextType>;
  TypeaheadResult?: TypeaheadResultResolvers<ContextType>;
  SpecialPriceMeta?: SpecialPriceMetaResolvers<ContextType>;
  SpecialPriceResponse?: SpecialPriceResponseResolvers<ContextType>;
  SpecialPrice?: SpecialPriceResolvers<ContextType>;
  Price?: PriceResolvers<ContextType>;
  UploadSpecialPriceResponse?: UploadSpecialPriceResponseResolvers<ContextType>;
  SuccessfulUpload?: SuccessfulUploadResolvers<ContextType>;
  SuccessfulCreate?: SuccessfulCreateResolvers<ContextType>;
  FailedPriceSuggestion?: FailedPriceSuggestionResolvers<ContextType>;
  ProductPriceResponse?: ProductPriceResponseResolvers<ContextType>;
  StoreStock?: StoreStockResolvers<ContextType>;
  TechDoc?: TechDocResolvers<ContextType>;
  TechSpec?: TechSpecResolvers<ContextType>;
  ImageUrls?: ImageUrlsResolvers<ContextType>;
  PackageDimensions?: PackageDimensionsResolvers<ContextType>;
  Stock?: StockResolvers<ContextType>;
  ProductSearchResult?: ProductSearchResultResolvers<ContextType>;
  Product?: ProductResolvers<ContextType>;
  Pagination?: PaginationResolvers<ContextType>;
  ProductSearchKourierResult?: ProductSearchKourierResultResolvers<ContextType>;
  KourierProduct?: KourierProductResolvers<ContextType>;
  ProductSearchKourierResponse?: ProductSearchKourierResponseResolvers<ContextType>;
  ProductDetails?: ProductDetailsResolvers<ContextType>;
  UserResponse?: UserResponseResolvers<ContextType>;
  BranchUsersListResponse?: BranchUsersListResponseResolvers<ContextType>;
  ErrorResponse?: ErrorResponseResolvers<ContextType>;
  UserBranchResponse?: UserBranchResponseResolvers<ContextType>;
  VarianceSummaryResponse?: VarianceSummaryResponseResolvers<ContextType>;
  VarianceDetailsResponse?: VarianceDetailsResponseResolvers<ContextType>;
  VarianceLocationResponse?: VarianceLocationResponseResolvers<ContextType>;
  VarianceLocationItem?: VarianceLocationItemResolvers<ContextType>;
  VarianceNextLocationResponse?: VarianceNextLocationResponseResolvers<ContextType>;
  VarianceLocationsResponse?: VarianceLocationsResponseResolvers<ContextType>;
  VarianceLocationSummary?: VarianceLocationSummaryResolvers<ContextType>;
  DetailedVarianceResponse?: DetailedVarianceResponseResolvers<ContextType>;
  VarianceDetails?: VarianceDetailsResolvers<ContextType>;
}>;


/**
 * @deprecated
 * Use "Resolvers" root object instead. If you wish to get "IResolvers", add "typesPrefix: I" to your config.
 */
export type IResolvers<ContextType = any> = Resolvers<ContextType>;
