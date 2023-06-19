import gql from 'graphql-tag';

export default gql`

type StoreStock {
    branchName: String
    address: String
    availability: Int
  }
  type TechDoc {
    name: String
    url: String
  }
  type TechSpec {
    name: String
    value: String
  }
  type ImageUrls {
    thumb: String
    small: String
    medium: String
    large: String
  }
  type PackageDimensions {
    height: Float
    length: Float
    volume: Float
    volumeUnitOfMeasure: String
    width: Float
    weight: Float
    weightUnitOfMeasure: String
  }
  type Stock {
    homeBranch: StoreStock
    otherBranches: [StoreStock]
  }
  type ProductSearchResult {
    pagination: Pagination!
    products: [Product]!
  }
  type Product {
    id: ID
    name: String
    productNumber: String
    productType: String
    taxonomy: [String]
    manufacturerName: String
    manufacturerNumber: String
    price: Float
    stock: Stock
    technicalDocuments: [TechDoc]
    environmentalOptions: [String]
    upc: String
    unspsc: String
    seriesModelFigureNumber: String
    productOverview: String
    featuresAndBenefits: String
    techSpecifications: [TechSpec]
    imageUrls: ImageUrls
    packageDimensions: PackageDimensions
    productImageUrl: String
  }
  type Pagination {
    pageSize: Int!
    currentPage: Int!
    totalItemCount: Int!
  }
  
  type ProductSearchKourierResult {
    errorCode: String
    errorMessage: String
    productIdCount: Int
    products: [KourierProduct]
  }

  type KourierProduct {
    productId: String
    displayField: String
    productNumber: String
    upc: String
    productImageUrl: String

  }

  type ProductSearchKourierResponse {
    prodSearch: [ProductSearchKourierResult!]!
  }

  input ProductSearchKourierInput {
    keywords: String!
    displayName: String
    searchInputType: String
  }

  input ProductSearchEclipseInput {
    searchTerm: String
    pageSize: Int
    searchInputType: Int!
    currentPage: Int
    selectedAttributes: [ProductAttribute]
  }
  input ProductAttribute {
    attributeType: String
    attributeValue: String
  }

  type ProductDetails {
    description: String!
    catalogNumber: String!
    upc: String!
  }

  type Query {
    productDetails(productId: String!): ProductDetails!
    searchProductsEclipse(input: ProductSearchEclipseInput!): ProductSearchResult!
    searchProductsKourier(input: ProductSearchKourierInput!): ProductSearchKourierResponse!
  }
`;
