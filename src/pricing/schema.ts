import gql from 'graphql-tag';

export default gql`
  type TypeaheadResponse {
    meta: TypeaheadMeta!
    results: [TypeaheadResult!]!
  }

  type TypeaheadMeta {
    page: Int!
    nextPage: Int
    pageCount: Int!
    pageSize: Int!
    resultCount: Int!
    orderBy: String!
    orderDirection: String!
    entity: String!
    query: String!
  }

  type TypeaheadResult {
    id: String!
    displayName: String!
  }

  type SpecialPriceMeta {
    page: Int!
    nextPage: Int
    pageCount: Int!
    pageSize: Int!
    resultCount: Int!
    orderBy: String!
    orderDirection: String!
    customerId: String
    productId: String
    priceLine: String
  }

  type SpecialPriceResponse {
    meta: SpecialPriceMeta!
    results: [SpecialPrice!]!
  }

  type SpecialPrice {
    productId: String!
    customerId: String!
    branch: String!
    customerDisplayName: String!
    imageUrl: String
    manufacturer: String!
    displayName: String!
    manufacturerReferenceNumber: String!
    salesperson: String!
    priceLine: String!
    customerSalesQuantity: Int!
    prices: [Price!]!
    territory: String
  }

  type Price {
    type: String!
    value: Float!
    currency: String!
    displayName: String!
  }

  type UploadSpecialPriceResponse {
    successfulUploads: [SuccessfulUpload!]
    successfulCreates: [SuccessfulCreate!]
    failedUpdateSuggestions: [FailedPriceSuggestion!]
    failedCreateSuggestions: [FailedPriceSuggestion!]
  }

  type SuccessfulUpload {
    uploadedPath: String!
    uploadedName: String!
  }

  type SuccessfulCreate {
      uploadedPath: String!
      uploadedName: String!
  }

  type FailedPriceSuggestion {
    customerId: String!
    productId: String!
    branch: String!
    cmpPrice: Float!
    priceCategory: String!
    newPrice: Float!
    changeWriterDisplayName: String!
    changeWriterId: String!
  }

  type ProductPriceResponse {
    productId: String
    erpBranchNum: String
    cmp: Float
    stdCost: Float
    uom: String
    rateCardPrice: Float
    rateCardName: String
    matchedBranch: String
    matchedClass: String
    matchedGroup: String
    matrixId: String
    customerId: String
    correlationId: String
  }

  input PagingContext {
    page: Int!
    pageSize: Int!
    orderBy: String!
    orderDirection: String!
  }

  input TypeaheadInput {
    entity: String!
    query: String!
  }

  input SpecialPriceInput {
    customerId: String
    productId: String
    priceLine: String
  }

  input PriceChangeInput {
    priceChangeSuggestions: [PriceSuggestion!]!
    priceCreateSuggestions: [PriceSuggestion!]!
  }

  input PriceSuggestion {
    customerId: String!
    productId: String!
    territory: String
    branch: String!
    cmpPrice: Float!
    priceCategory: String!
    newPrice: Float!
    changeWriterDisplayName: String!
    changeWriterId: String!
  }

  input ProductPriceInput {
    productId: String!
    branch: String
    customerId: String
    userId: String
    correlationId: String
    effectiveDate: String
  }

  type Query {
    typeaheadSuggestions(input: TypeaheadInput!): TypeaheadResponse!
    paginatedTypeaheadSuggestions(input: TypeaheadInput!, pagingContext: PagingContext!): TypeaheadResponse!
    specialPrices(input: SpecialPriceInput!): SpecialPriceResponse!
    paginatedSpecialPrices(input: SpecialPriceInput!, pagingContext: PagingContext!): SpecialPriceResponse!
    priceLines(input: SpecialPriceInput!): [String!]!
    productPrices(input: ProductPriceInput!): ProductPriceResponse!
  }

  type Mutation {
    specialPrices(input: PriceChangeInput!): UploadSpecialPriceResponse
  }
`;
