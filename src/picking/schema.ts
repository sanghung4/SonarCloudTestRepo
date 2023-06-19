import gql from 'graphql-tag';

export default gql`
  type PickingOrder {
    orderId: String
    generationId: Int
    invoiceId: String
    branchId: String
    pickGroup: String
    assignedUserId: String
    billTo: Int
    shipTo: Int
    shipToName: String
    pickCount: String
    shipVia: String
    isFromMultipleZones: Boolean
    taskWeight: Float
    taskState: String
  }

  type UserPick {
    productId: String!
    productImageUrl: String
    description: String!
    quantity: Int!
    uom: String!
    locationType: String!
    location: String!
    lot: String!
    splitId: String
    orderId: String
    generationId: Int
    lineId: Int!
    shipVia: String!
    tote: String
    userId: String!
    branchId: String!
    cutDetail: String
    cutGroup: String
    isParallelCut: Boolean
    warehouseID: String!
    isLot: String
    isSerial: Boolean!
    pickGroup: String
  }

  input PickingOrderInput {
    branchId: String!
    userId: String!
    orderId: String
  }

  input PickingTaskInput {
    orderId: String
    generationId: Int
    invoiceId: String
    branchId: String
    pickGroup: String
    assignedUserId: String
    billTo: Int
    shipTo: Int
    shipToName: String
    pickCount: String
    shipVia: String
    isFromMultipleZones: Boolean
    taskWeight: Float
    taskState: String
  }

  input CompletePickInput {
    productId: String!
    description: String!
    quantity: Int!
    uom: String!
    locationType: String!
    location: String!
    lot: String!
    splitId: String
    orderId: String
    generationId: Int
    lineId: Int!
    shipVia: String!
    tote: String
    userId: String!
    branchId: String!
    cutDetail: String
    cutGroup: String
    isParallelCut: Boolean
    warehouseID: String!
    isLot: String
    isSerial: Boolean!
    pickGroup: String
    isOverrideProduct: Boolean!
    startPickTime: String!
    ignoreLockToteCheck: Boolean!
  }

  type ProductSerialNumberResults {
    results: [ProductSerialNumbers]!
  }

  type ProductSerialNumbers {
    productId: String!
    orderId: String!
    generationId: String!
    invoiceId: String!
    quantity: Int!
    description: String
    location: String
    warehouseId: String!
    serialList: [SerialList]!
    nonStockSerialNumbers: [SerialList]!
  }

  type SerialList {
    line: Int!
    serial: String!
  }

  type StagePickTaskResponse {
    success: Boolean!
    message: String!
  }

  type StagePickTotePackagesResponse {
    success: Boolean!
    message: String!
  }

  type CloseTaskResponse {
    success: Boolean!
    message: String!
  }

  input UpdateProductSerialNumbersInput {
    branchId: String!
    warehouseId: String!
    serialNumberList: [SerialLineInput]!
    ignoreStockCheck: Boolean!
  }

  input SerialLineInput {
    line: Int!
    serial: String!
  }

  input StagePickTaskInput {
    orderId: String!
    invoiceId: String!
    branchId: String!
    tote: String!
    location: String
  }

  input StagePickTotePackagesInput {
    orderId: String!
    invoiceId: String!
    branchId: String!
    tote: String!
    packageList: [PackageInput]!
  }

  input PackageInput {
    packageType: String!
    packageQuantity: Int!
  }

  input CloseTaskInput {
    orderId: String!
    invoiceId: String!
    branchId: String!
    finalLocation: String
    tote: String!
    skipStagedWarningFlag: Boolean
    skipInvalidLocationWarningFlag: Boolean
    updateLocationOnly: Boolean
  }

  input SplitQuantityInput {
    product: CompletePickInput!
    serialNumbers: [SerialLineInput]
    pickedItemsCount: Int!
  }

  type SplitQuantityResponse {
    productId: String!
    isSplit: Boolean!
    orderId: String!
    invalidSerialNums: [SerialList]
    successStatus: Boolean!
    errorMessage: String
  }

  input closeOrderInput {
    orderId: String!
    pickerId: String!
  }

  type closeOrderResponse {
    status: Boolean!
    orderId: String!
    pickerId: String!
    errorCode: String
    errorMessage: String
    orderLocked: Boolean!
    moreToPick: Boolean!
    stillPicking: Boolean!
  }

  type ShippingDetailsKourierResponse{
    shippingtext: [ShippingDetailsKourierResult!]!
  }

  type ShippingDetailsKourierResult{
    invoiceNumber: String
    status: String
    errorCode: String
    errorMessage: String
    shippingInstructions: [String!]
    noBackorder: Boolean!
    noSort: Boolean!
  }

  input ShippingDetailsKourierInput {
    invoiceNumber: String!
  }

  input ValidateBranchInput {
    branchId: String!
  }

  type ValidateBranchResponse {
    isValid: Boolean!
    branch: BranchResponse
  }

  type BranchResponse {
    branchId: String!
    branchName: String!
  }

  type Query {
    pickingOrders(input: PickingOrderInput!): [PickingOrder]!
    userPicks(input: PickingOrderInput!): [UserPick]!
    getProductSerialNumbers(warehouseId: String!): ProductSerialNumberResults!
    productImageUrl(input: String!): String
    shippingDetails(input: ShippingDetailsKourierInput!): ShippingDetailsKourierResponse!
    validateBranch(input: ValidateBranchInput!): ValidateBranchResponse!
  }

  type Mutation {
    assignPickTask(input: PickingTaskInput!): PickingOrder!
    completeUserPick(input: CompletePickInput!): UserPick!
    updateProductSerialNumbers(input: UpdateProductSerialNumbersInput!): ProductSerialNumberResults!
    stagePickTask(input: StagePickTaskInput!): StagePickTaskResponse!
    stagePickTotePackages(input: StagePickTotePackagesInput!): StagePickTotePackagesResponse!
    splitQuantity(input: SplitQuantityInput!): SplitQuantityResponse
    closeTask(input: CloseTaskInput!): CloseTaskResponse!
    closeOrder(input: closeOrderInput!): closeOrderResponse!
  }
`;
