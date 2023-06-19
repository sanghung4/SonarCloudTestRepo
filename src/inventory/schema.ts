import gql from 'graphql-tag';

export default gql`
  enum ErpSystem {
    MINCRON
    ECLIPSE
  }

  enum LocationItemStatus {
    UNCOUNTED
    STAGED
    COMMITTED
  }

  enum CountStatus {
    NOT_LOADED
    IN_PROGRESS
    COMPLETE
    ERROR
  }

  type Count {
    id: String!
    branch: Branch!
    erpSystem: ErpSystem!
  }

  type CountWithStatus {
    id: ID
    branchId: String!
    countId: String!
    branchName: String
    status: CountStatus
    errorMessage: String
    totalProducts: Int
    createdAt: String
  }

  type Branch {
    id: String!
    name: String
  }

  type Location {
    id: String!
    totalProducts: Int!
    totalCounted: Int!
    committed: Boolean!
    items: [LocationItem!]!
  }

  type LocationItem {
    id: ID
    locationId: String
    prodDesc: String
    prodNum: String!
    tagNum: String
    catalogNum: String
    uom: String
    quantity: Int
    status: LocationItemStatus
    sequence: Int
    productImageUrl: String
    controlNum: String
  }

  type LocationSummary {
    id: String!
    committed: Boolean!
    totalProducts: Int!
    totalCounted: Int!
  }

  type LocationsResponse {
    totalLocations: Int!
    totalCounted: Int!
    content: [LocationSummary!]!
  }

  type NextLocationResponse {
    locationId: String!
  }

  type WriteIn {
    id: ID!
    locationId: String!
    catalogNum: String
    upcNum: String
    description: String!
    uom: String!
    quantity: Int!
    comment: String
    createdBy: String!
    createdAt: String!
    updatedBy: String!
    updatedAt: String!
    resolved: Boolean!
  }

  type MetricsCompletion{
    status : String!
    total: Int!
    needToBeCounted: Int!
    counted: Int!
    timeStarted: String!
    timeEnded: String!
    countId : Int!
    location: String!
    branchId: Int!
  }

  type PageableSort {
    sorted: Boolean!
    unsorted: Boolean!
    empty: Boolean!
  }

  type Pageable {
    sort: PageableSort!
    pageNumber: Int!
    pageSize: Int!
    offset: Int!
    paged: Boolean!
    unpaged: Boolean!
  }

  type WriteInsResponse {
    content: [WriteIn!]!
    pageable: Pageable!
    last: Boolean!
    totalPages: Int!
    totalElements: Int!
    first: Boolean!
    numberOfElements: Int!
    number: Int!
    size: Int!
    sort: PageableSort!
    empty: Boolean!
  }

  type WriteInMutationResponse {
    success: Boolean!
    message: String
    content: WriteIn!
  }

  type MetricsCompletionResponse {
    success: Boolean!
    message: String
    content: MetricsCompletion!
  }

  type AddToCountMutationResponse {
    success: Boolean!
    message: String
    item: LocationItem!
  }

  type DeleteMultipleCountResponse {
    deletedCounts: [DeleteCountResponse!]!
  }

  input RemoveCountsInput {
    erpSystemName: ErpSystem
    endDate: String!
  }

  type DeleteCountResponse {
    countLocations: Int!
    countLocationItems: Int!
    countLocationItemQuantities: Int!
    varianceCountLocationItemQuantities: Int!
    writeIns: Int!
  }

  type CountMutationResponse {
    success: Boolean!
    message: String
  }

  input MetricsCompletionInput{
    status : String
    total: Int
    needToBeCounted: Int
    counted: Int
    timeStarted: String
    timeEnded: String
    countId : String
    location: String
    branchId: String
  }

  input ItemInput {
    productId: String!
    locationId: String!
    quantity: Int
  }

  input WriteInsSort {
    property: String!
    direction: String!
  }

  input WriteInsInput {
    sort: WriteInsSort
    page: Int!
    size: Int!
  }

  input WriteInInput {
    locationId: String!
    catalogNum: String
    upcNum: String
    description: String!
    uom: String!
    quantity: Int!
    comment: String
  }

  input CountsInput {
    startDate: String!
    endDate: String!
  }

  input RemoveBranchCountsInput {
    branchId: String!
    countId: String!
  }

  type Query {
    count(id: String!, branchId: String!): Count!
    countStatus(id: ID!): CountWithStatus!
    counts(input: CountsInput!): [CountWithStatus!]!
    locations: LocationsResponse!
    location(id: String!): Location!
    nextLocation(id: String!): NextLocationResponse!
    writeIns(options: WriteInsInput!): WriteInsResponse!
    writeIn(id: ID!): WriteIn!
  }

  type Mutation {
    addToCount(item: ItemInput!): AddToCountMutationResponse!
    updateCount(item: ItemInput!): CountMutationResponse!
    completeCount(locationId: String!): CountMutationResponse!
    deleteCount(id: ID!): DeleteCountResponse!
    createWriteIn(writeIn: WriteInInput!): WriteInMutationResponse!
    updateWriteIn(id: ID!, writeIn: WriteInInput!): WriteInMutationResponse!
    resolveWriteIn(id: ID!): WriteInMutationResponse!
    addCompletionMetric(metric: MetricsCompletionInput!): MetricsCompletionResponse!
    loadCount(branchId: String!, countId: String!): CountWithStatus!
    purgeMincronCounts(input: CountsInput!): String!
    removeCounts(input: RemoveCountsInput!): String!
    removeBranchCounts(input: RemoveBranchCountsInput!): DeleteMultipleCountResponse!
  }
`;
