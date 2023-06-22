import {
  AccountDetailsDocument,
  AccountDetailsQuery,
  AccountDocument,
  AccountErpIdDocument,
  AccountErpIdQuery,
  AccountQuery,
  AddAllListItemsToCartDocument,
  AddAllListItemsToCartMutation,
  AddCreditCardDocument,
  AddCreditCardMutation,
  AddItemsToCartDocument,
  AddItemsToCartMutation,
  AddItemToListDocument,
  AddItemToListMutation,
  ApproveOrderDocument,
  ApproveOrderMutation,
  ApproveUserDocument,
  ApproveUserMutation,
  CartDocument,
  CartFromQuoteDocument,
  CartFromQuoteQuery,
  CartQuery,
  CartUserIdAccountIdDocument,
  CartUserIdAccountIdQuery,
  CategoriesDocument,
  CategoriesQuery,
  CheckUsersForApproverDocument,
  CheckUsersForApproverMutation,
  ContractsDocument,
  ContractsQuery,
  CreateCartDocument,
  CreateCartMutation,
  CreateListDocument,
  CreateListMutation,
  CreateUserDocument,
  CreateNewUserDocument,
  CreateNewUserMutation,
  CreateUserMutation,
  CreateNewEmployeeDocument,
  CreateNewEmployeeMutation,
  CreditCardElementInfoDocument,
  CreditCardElementInfoQuery,
  CreditCardListDocument,
  CreditCardListQuery,
  CreditCardSetupUrlDocument,
  CreditCardSetupUrlQuery,
  DeleteCartItemsDocument,
  DeleteCartItemsMutation,
  DeleteContractCartDocument,
  DeleteContractCartMutation,
  DeleteCreditCardDocument,
  DeleteCreditCardMutation,
  DeleteItemDocument,
  DeleteItemMutation,
  DeleteListDocument,
  DeleteListMutation,
  DeleteUserDocument,
  DeleteUserMutation,
  ErpAccountDocument,
  ErpAccountQuery,
  FeaturesDocument,
  FeaturesQuery,
  FindBranchesDocument,
  FindBranchesQuery,
  GetAllUnapprovedAccountRequestsDocument,
  GetAllUnapprovedAccountRequestsQuery,
  GetApprovedAccountRequestsDocument,
  GetApprovedAccountRequestsQuery,
  GetApproversDocument,
  GetBranchDocument,
  GetBranchesListDocument,
  GetBranchesListQuery,
  GetBranchQuery,
  GetContactInfoDocument,
  GetContactInfoQuery,
  GetExportListIntoCsvDocument,
  GetExportListIntoCsvQuery,
  GetHomeBranchDocument,
  GetHomeBranchQuery,
  GetInvoiceDocument,
  GetInvoiceQuery,
  GetListDocument,
  GetListQuery,
  GetListsDocument,
  GetListsQuery,
  GetOrderDocument,
  GetOrderQuery,
  GetOrdersPendingApprovalDocument,
  GetOrdersPendingApprovalQuery,
  GetPhoneTypesDocument,
  GetPhoneTypesQuery,
  GetProductDocument,
  GetProductInventoryDocument,
  GetProductInventoryQuery,
  GetProductPricingDocument,
  GetProductPricingQuery,
  GetProductQuery,
  GetRejectionReasonsDocument,
  GetRejectionReasonsQuery,
  GetSelectedErpAccountDocument,
  GetSelectedErpAccountQuery,
  GetSelectedErpAccountsDocument,
  GetSelectedErpAccountsQuery,
  GetUserInviteDocument,
  GetUserInviteQuery,
  InvitedUserEmailSentDocument,
  InvitedUserEmailSentQuery,
  InviteUserDocument,
  InviteUserMutation,
  InvoicesDocument,
  InvoicesQuery,
  InvoicesUrlDocument,
  InvoicesUrlQuery,
  OrderPendingApprovalDocument,
  OrderPendingApprovalQuery,
  OrdersDocument,
  OrdersQuery,
  PreviouslyPurchasedProductsDocument,
  PreviouslyPurchasedProductsQuery,
  ProductCategoriesDocument,
  ProductCategoriesQuery,
  QuotesDocument,
  QuotesQuery,
  RefreshCartDocument,
  RefreshCartMutation,
  RefreshContactDocument,
  RefreshContactMutation,
  RejectedAccountRequestsDocument,
  RejectedAccountRequestsQuery,
  RejectOrderDocument,
  RejectOrderMutation,
  RejectUserDocument,
  RejectUserMutation,
  ResendLegacyInviteEmailDocument,
  ResendLegacyInviteEmailMutation,
  ResendVerificationEmailDocument,
  ResendVerificationEmailMutation,
  SearchProductDocument,
  SearchProductQuery,
  SearchSuggestionDocument,
  SearchSuggestionQuery,
  SendContactFormDocument,
  SendContactFormMutation,
  SetFeatureEnabledDocument,
  SetFeatureEnabledMutation,
  SubmitContractOrderFromCartDocument,
  SubmitContractOrderFromCartQuery,
  SubmitContractOrderReviewDocument,
  SubmitContractOrderReviewQuery,
  SubmitOrderDocument,
  SubmitOrderMutation,
  SubmitOrderPreviewDocument,
  SubmitOrderPreviewMutation,
  ToggleItemInListsDocument,
  ToggleItemInListsMutation,
  UnapprovedAccountRequestsDocument,
  UnapprovedAccountRequestsQuery,
  UpdateBranchDocument,
  UpdateBranchMutation,
  UpdateCartDocument,
  UpdateCartMutation,
  UpdateDeliveryDocument,
  UpdateDeliveryMutation,
  UpdateItemQuantityDocument,
  UpdateItemQuantityMutation,
  UpdateListDocument,
  UpdateListMutation,
  UpdateUserDocument,
  UpdateUserMutation,
  UpdateUserPasswordDocument,
  UpdateUserPasswordMutation,
  UpdateWillCallBranchDocument,
  UpdateWillCallBranchMutation,
  UpdateWillCallDocument,
  UpdateWillCallMutation,
  UploadNewListDocument,
  UploadNewListMutation,
  UploadToListDocument,
  UploadToListMutation,
  VerifyAccountNewDocument,
  VerifyUserDocument,
  VerifyUserEmailDocument,
  VerifyUserEmailQuery,
  VerifyUserMutation,
  WillCallBranchesDocument,
  WillCallBranchesQuery
} from './../generated/graphql';
import { MockedResponse } from '@apollo/client/testing';
import {
  UserAccountsQuery,
  UserAccountsDocument,
  UserQuery,
  UserDocument,
  EntitySearchQuery,
  EntitySearchDocument,
  CreateJobFormDocument,
  CreateJobFormMutation,
  GetRefreshShipToAccountDocument,
  GetRefreshShipToAccountQuery,
  GetRolesDocument,
  GetRolesQuery,
  GetApproversQuery
} from 'generated/graphql';
import {
  dummyAccountLookup,
  dummyUser,
  dummyUserAccounts,
  dummyRefreshShipToAccount,
  dummyEcommAccounts,
  dummyRoles
} from './dummyData';

/******************************/
/* Utils                    */
/******************************/
const ERROR_RESPONSE = { message: 'error message', name: 'error name' };

/******************************/
/* Variables                  */
/******************************/
const variables = {
  userAccountsQuery: { userId: 'testuser' },
  userQuery: { userId: 'testuser' },
  entitySearchQuery: { accountId: '12345' },
  getRefreshShipToAccount: {
    billToAccountId: '507ee1d7-4f6b-4ebd-af79-a06d3dfeefce'
  },
  getApprovers: { billToAccountId: '12345' }, // TODO: add dummy variables
  createJobForm: {
    jobFormInput: {
      bonding: {
        bondNumber: '',
        city: '',
        phoneNumber: '',
        postalCode: '',
        state: '',
        streetLineOne: '',
        streetLineTwo: '',
        suretyName: ''
      },
      generalContractor: {
        city: 'value',
        generalContractor: 'value',
        phoneNumber: '(123) 456-7890',
        postalCode: '12345',
        state: 'AL',
        streetLineOne: 'address',
        streetLineTwo: ''
      },
      job: {
        customerName: 'mock userAccounts name 0',
        customerNumber: '12345',
        email: 'testuser@email.com',
        phoneNumber: '(123) 456-7890',
        userName: 'testuser@email.com'
      },
      lender: {
        city: '',
        lenderName: '',
        loanNumber: '',
        phoneNumber: '',
        postalCode: '',
        state: '',
        streetLineOne: '',
        streetLineTwo: ''
      },
      owner: {
        city: 'value',
        ownerName: 'value',
        phoneNumber: '',
        postalCode: '12345',
        state: 'AL',
        streetLineOne: 'address',
        streetLineTwo: ''
      },
      project: {
        city: 'value',
        estimatedProjectAmount: 123,
        jobName: 'value',
        lotNoAndTrack: '',
        postalCode: '12345',
        state: 'AL',
        streetLineOne: 'address',
        streetLineTwo: '',
        taxable: false
      }
    }
  },
  getProductQuery: { id: '' }, // TODO: add dummy variables
  getProductPricing: { input: '' }, // TODO: add dummy variables
  getproductInventory: { productId: '' }, // TODO: add dummy variables
  cartQuery: {
    id: '',
    userId: '',
    shipToAccountId: '',
    includeProducts: false
  }, // TODO: add dummy variables
  cartFromQuote: {
    userId: '',
    shipToAccountId: '',
    quoteId: '',
    branchId: ''
  }, // TODO: add dummy variables
  cartUserIdAccountId: {
    userId: '',
    accountId: '',
    includeProducts: ''
  }, // TODO: add dummy variables
  erpAccount: {
    id: '',
    brand: ''
  }, // TODO: add dummy variables
  willCallBranches: { shipToAccountId: '' }, // TODO: add dummy variables
  createCart: { userId: '', shipToAccountId: '', branchId: '' }, // TODO: add dummy variables
  addItemsToCart: { cartId: '', addItemsInput: '' }, // TODO: add dummy variables
  addAllListItemsToCart: { cartId: '', listId: '', addAllToCartUserInput: '' }, // TODO: add dummy variables
  deleteItem: { cartId: '', itemId: '', userId: '', shipToAccountId: '' }, // TODO: add dummy variables
  updateCart: { cartId: '', cart: '' }, // TODO: add dummy variables
  refreshCart: {
    cartId: '',
    userId: '',
    shipToAccountId: ''
  }, // TODO: add dummy variables
  deleteCartItems: { cartId: '' }, // TODO: add dummy variables
  updateDelivery: { deliveryInfo: '' }, // TODO: add dummy variables
  updateWillCall: { willCallInfo: '' },
  updateWillCallBranch: {
    cartId: '',
    branchId: '',
    userId: '',
    shipToAccountId: ''
  }, // TODO: add dummy variables
  updateItemQuantity: {
    cartId: '',
    itemId: '',
    quantity: '',
    minIncrementQty: '',
    userId: '',
    shipToAccountId: ''
  }, // TODO: add dummy variables
  submitOrderPreview: {
    cartId: '',
    userId: '',
    shipToAccountId: '',
    billToAccountId: ''
  }, // TODO: add dummy variables
  submitOrder: {
    cartId: '',
    userId: '',
    shipToAccountId: '',
    billToAccountId: ''
  }, // TODO: add dummy variables
  getOrder: {
    accountId: '',
    orderId: '',
    userId: '',
    invoiceNumber: '',
    orderStatus: ''
  }, // TODO: add dummy variables
  orders: { accountId: '', startDate: '', endDate: '', erpName: '' }, // TODO: add dummy variables
  invitedUserEmailSent: { email: 'example@email.com' },
  resendLegacyInviteEmail: { legacyUserEmail: 'example@email.com' }, // TODO: add dummy variables
  getList: {
    listId: '',
    userId: '',
    shipToAccountId: '',
    branchId: ''
  }, // TODO: add dummy variables
  getLists: { billToAccountId: '' }, // TODO: add dummy variables
  getExportListIntoCSV: {
    listId: '',
    userId: '',
    shipToAccountId: '',
    branchId: ''
  }, // TODO: add dummy variables
  createList: { createListInput: '' }, // TODO: add dummy variables
  deleteList: { listId: '' }, // TODO: add dummy variables
  UpdateList: { updateListInput: '' }, // TODO: add dummy variables
  uploadNewList: { file: '', name: '', billToAccountId: '' }, // TODO: add dummy variables
  uploadToList: { file: '', listId: '' }, // TODO: add dummy variables
  addItemToList: { addItemToListInput: '' }, // TODO: add dummy variables
  toggleItemInLists: { toggleItemsInListInput: '' }, // TODO: add dummy variables
  getInvoice: { $accountId: '', invoiceNumber: '' }, // TODO: add dummy variables
  invoices: { accountId: '', erpName: '' }, // TODO: add dummy variables
  invoicesUrl: { accountId: '', invoiceNumbers: '' }, // TODO: add dummy variables
  getBranch: { branchId: '' }, // TODO: add dummy variables
  findBranches: { branchSearch: '' }, // TODO: add dummy variables
  getSelectedErpAccount: { accountId: '', brand: '' }, // TODO: add dummy variables
  getSelectedErpAccounts: { billToId: '', shipToId: '', brand: '' }, // TODO: add dummy variables
  quote: { accountId: '', quoteId: '', userId: '' }, // TODO: add dummy variables
  approveQuoteInput: { approveQuoteInput: '' }, // TODO: add dummy variables
  rejectQuote: {
    userId: '',
    shipToAccountId: '',
    quoteId: ''
  }, // TODO: add dummy variables
  getHomeBranch: { shipToAccountId: '' }, // TODO: add dummy variables
  sendContactForm: { contactFormInput: '' }, // TODO: add dummy variables
  submitContractOrderReview: { orderReview: '' }, // TODO: add dummy variables
  deleteContractCart: {
    application: '',
    accountId: '',
    userId: '',
    shoppingCartId: '',
    branchNumber: ''
  }, // TODO: add dummy variables
  submitContractOrderFromCart: {
    contractOrderSubmit: '',
    application: '',
    accountId: '',
    userId: '',
    shoppingCartId: ''
  }, // TODO: add dummy variables
  quotes: { accountId: '' }, // TODO: add dummy variables
  user: { userId: '' }, // TODO: add dummy variables
  userAccounts: { userId: '' }, // TODO: add dummy variables
  updateUserPassword: { updateUserPasswordInput: '' }, // TODO: add dummy variables
  searchSuggestion: {
    term: '',
    engine: '',
    size: '',
    userId: '',
    shipToAccountId: '',
    erpSystem: '',
    state: ''
  }, // TODO: add dummy variables
  contracts: {
    erpAccountId: '',
    pageNumber: '',
    searchFilter: '',
    fromDate: '',
    toDate: '',
    sortOrder: '',
    sortDirection: ''
  }, // TODO: add dummy variables
  updateUser: { updateUserInput: '' }, // TODO: add dummy variables
  approveUser: { user: '' }, // TODO: add dummy variables
  accountErpId: { accountId: '', brand: '' }, // TODO: add dummy variables
  inviteUser: { inviteUserInput: '' }, // TODO: add dummy variables
  deleteUser: {
    deleteUserInput: { accountId: '', userId: '', userLeftCompany: true }
  },
  checkUsersForApprover: { checkUsersForApproverInput: '' }, // TODO: add dummy variables
  categories: { erp: '' }, // TODO: add dummy variables
  productCategories: { engine: '' }, // TODO: add dummy variables
  searchProduct: { productSearch: '', userId: '', shipToAccountId: '' }, // TODO: add dummy variables
  setFeatureEnabled: { featureId: '', setFeatureEnabledInput: '' }, // TODO: add dummy variables
  refreshContact: { emails: '' }, // TODO: add dummy variables
  previouslyPurchasedProducts: {
    ecommShipToId: '',
    userId: '',
    currentPage: '',
    pageSize: ''
  }, // TODO: add dummy variables
  creditCardSetupUrl: { accountId: '', cardHolderInput: '' }, // TODO: add dummy variables
  creditCardElementInfo: {
    accountId: '',
    elementSetupId: ''
  }, // TODO: add dummy variables
  creditCardList: { accountId: '' }, // TODO: add dummy variables
  addCreditCard: { accountId: '', creditCard: '' }, // TODO: add dummy variables
  deleteCreditCard: { accountId: '', creditCard: '' }, // TODO: add dummy variables
  unapprovedAccountRequests: { accountId: '' }, // TODO: add dummy variables
  getApprovedAccountRequests: { accountId: '' }, // TODO: add dummy variables
  rejectedAccountRequests: { accountId: '' }, // TODO: add dummy variables
  orderPendingApproval: { orderId: '' }, // TODO: add dummy variables
  approveOrder: {
    cartId: '',
    userId: '',
    shipToAccountId: '',
    billToAccountId: ''
  }, // TODO: add dummy variables
  rejectOrder: {
    cartId: '',
    userId: '',
    shipToAccountId: '',
    rejectOrderInfo: ''
  }, // TODO: add dummy variables
  account: { accountId: '', brand: '' }, // TODO: add dummy variables
  accountDetails: { accountId: '', brand: '' }, // TODO: add dummy variables
  getContactInfo: { userId: '' }, // TODO: add dummy variables
  getUserInvite: { inviteId: '' }, // TODO: add dummy variables
  createUser: { user: '', inviteId: '' }, // TODO: add dummy variables
  createNewUser: { user: '', inviteId: '' }, // TODO: add dummy variables
  createNewEmployee: { employe: '', inviteId: '' }, // TODO: add dummy variables
  verifyUser: { verificationToken: '' }, // TODO: add dummy variables
  resendVerificationEmail: { $userId: '', $isWaterworksSubdomain: '' }, // TODO: add dummy variables
  updateBranch: { input: '' },
  rejectUser: { rejectUserInput: '' },
  verifyUserEmail: { email: 'example@email.com' },
  verifyAccount: { accountNumber: '', zipCode: '', brand: '' }
};

// mockNameTypeState
// ex: mockUserAccountsQuerySuccess
// ex: mockCreateJobFormMutationError

/******************************/
/* Queries - Success          */
/******************************/
export const mockUserAccountsQuerySuccess: MockedResponse<UserAccountsQuery> = {
  request: {
    query: UserAccountsDocument,
    variables: variables.userAccountsQuery
  },
  result: { data: { userAccounts: dummyUserAccounts } }
};

export const mockUserQuerySuccess: MockedResponse<UserQuery> = {
  request: { query: UserDocument, variables: variables.userQuery },
  result: {
    data: {
      user: dummyUser
    }
  }
};

export const mockEntitySearchQuerySuccess: MockedResponse<EntitySearchQuery> = {
  request: {
    query: EntitySearchDocument,
    variables: variables.entitySearchQuery
  },
  result: {
    data: {
      entitySearch: dummyAccountLookup
    }
  }
};

export const mockProductQuerySuccess: MockedResponse<GetProductQuery> = {
  request: {
    query: GetProductDocument,
    variables: variables.getProductQuery
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockProductPricingQuerySuccess: MockedResponse<GetProductPricingQuery> =
  {
    request: {
      query: GetProductPricingDocument,
      variables: variables.getProductPricing
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockProductInventorySuccess: MockedResponse<GetProductInventoryQuery> =
  {
    request: {
      query: GetProductInventoryDocument,
      variables: variables.getproductInventory
    },
    result: {
      // TODO: add dummy data
    }
  };

export const USER_ACCOUNTS_SUCCESS: MockedResponse<UserAccountsQuery> = {
  request: {
    query: UserAccountsDocument,
    variables: {
      userId: 'testuser'
    }
  },
  result: {
    data: {
      userAccounts: dummyEcommAccounts
    }
  }
};

export const REFRESH_SHIP_TO_ACCOUNT_SUCCESS: MockedResponse<GetRefreshShipToAccountQuery> =
  {
    request: {
      query: GetRefreshShipToAccountDocument,
      variables: variables.getRefreshShipToAccount
    },
    result: {
      data: {
        refreshShipToAccount: dummyRefreshShipToAccount
      }
    }
  };

export const mockGetRolesQuerySuccess: MockedResponse<GetRolesQuery> = {
  request: { query: GetRolesDocument },
  result: {
    data: {
      roles: dummyRoles
    }
  }
};

export const mockGetApproversQuerySuccess: MockedResponse<GetApproversQuery> = {
  request: {
    query: GetApproversDocument,
    variables: variables.getApprovers
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockCartQuerySuccess: MockedResponse<CartQuery> = {
  request: {
    query: CartDocument,
    variables: variables.cartQuery
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockCartFromQuoteSuccess: MockedResponse<CartFromQuoteQuery> = {
  request: {
    query: CartFromQuoteDocument,
    variables: variables.cartFromQuote
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockCartUserIdAccountSuccess: MockedResponse<CartUserIdAccountIdQuery> =
  {
    request: {
      query: CartUserIdAccountIdDocument,
      variables: variables.cartUserIdAccountId
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockErpAccountSuccess: MockedResponse<ErpAccountQuery> = {
  request: {
    query: ErpAccountDocument,
    variables: variables.erpAccount
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockWillCallBranchesSuccess: MockedResponse<WillCallBranchesQuery> =
  {
    request: {
      query: WillCallBranchesDocument,
      variables: variables.willCallBranches
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockGetOrderSuccess: MockedResponse<GetOrderQuery> = {
  request: {
    query: GetOrderDocument,
    variables: variables.getOrder
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockOrdersSuccess: MockedResponse<OrdersQuery> = {
  request: {
    query: OrdersDocument,
    variables: variables.orders
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockInvitedUserEmailSentSuccess: MockedResponse<InvitedUserEmailSentQuery> =
  {
    request: {
      query: InvitedUserEmailSentDocument,
      variables: variables.invitedUserEmailSent
    },
    result: {
      data: { invitedUserEmailSent: false }
    }
  };

export const mockGetListSuccess: MockedResponse<GetListQuery> = {
  request: {
    query: GetListDocument,
    variables: variables.getList
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockGetListsSuccess: MockedResponse<GetListsQuery> = {
  request: {
    query: GetListsDocument,
    variables: variables.getLists
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockGetExportListIntoCSVSuccess: MockedResponse<GetExportListIntoCsvQuery> =
  {
    request: {
      query: GetExportListIntoCsvDocument,
      variables: variables.getExportListIntoCSV
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockGetBranchSuccess: MockedResponse<GetBranchQuery> = {
  request: {
    query: GetBranchDocument,
    variables: variables.getBranch
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockFindBranchesSuccess: MockedResponse<FindBranchesQuery> = {
  request: {
    query: FindBranchesDocument,
    variables: variables.findBranches
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockUserSuccess: MockedResponse<UserQuery> = {
  request: {
    query: UserDocument,
    variables: variables.user
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockUserAccountsSuccess: MockedResponse<UserAccountsQuery> = {
  request: {
    query: UserAccountsDocument,
    variables: variables.userAccounts
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockGetSelectedErpAccountSuccess: MockedResponse<GetSelectedErpAccountQuery> =
  {
    request: {
      query: GetSelectedErpAccountDocument,
      variables: variables.getSelectedErpAccount
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockGetSelectedErpAccountsSuccess: MockedResponse<GetSelectedErpAccountsQuery> =
  {
    request: {
      query: GetSelectedErpAccountsDocument,
      variables: variables.getSelectedErpAccounts
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockQuotesSuccess: MockedResponse<QuotesQuery> = {
  request: {
    query: QuotesDocument,
    variables: variables.quotes
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockGetHomeBranchSuccess: MockedResponse<GetHomeBranchQuery> = {
  request: {
    query: GetHomeBranchDocument,
    variables: variables.getHomeBranch
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockSubmitContractOrderReviewSuccess: MockedResponse<SubmitContractOrderReviewQuery> =
  {
    request: {
      query: SubmitContractOrderReviewDocument,
      variables: variables.submitContractOrderReview
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockSubmitContractOrderFromCartSuccess: MockedResponse<SubmitContractOrderFromCartQuery> =
  {
    request: {
      query: SubmitContractOrderFromCartDocument,
      variables: variables.submitContractOrderFromCart
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockInvoicesSuccess: MockedResponse<InvoicesQuery> = {
  request: {
    query: InvoicesDocument,
    variables: variables.invoices
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockInvoicesUrlSuccess: MockedResponse<InvoicesUrlQuery> = {
  request: {
    query: InvoicesUrlDocument,
    variables: variables.invoicesUrl
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockGetInvoiceSuccess: MockedResponse<GetInvoiceQuery> = {
  request: {
    query: GetInvoiceDocument,
    variables: variables.getInvoice
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockSearchSuggestionSuccess: MockedResponse<SearchSuggestionQuery> =
  {
    request: {
      query: SearchSuggestionDocument,
      variables: variables.searchSuggestion
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockGetRejectionReasonsSuccess: MockedResponse<GetRejectionReasonsQuery> =
  {
    request: {
      query: GetRejectionReasonsDocument //no variables
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockContractsSuccess: MockedResponse<ContractsQuery> = {
  request: {
    query: ContractsDocument,
    variables: variables.contracts
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockGetAllUnapprovedAccountRequestsSuccess: MockedResponse<GetAllUnapprovedAccountRequestsQuery> =
  {
    request: {
      query: GetAllUnapprovedAccountRequestsDocument //no variables
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockAccountErpIdSuccess: MockedResponse<AccountErpIdQuery> = {
  request: {
    query: AccountErpIdDocument,
    variables: variables.accountErpId
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockCategoriesSuccess: MockedResponse<CategoriesQuery> = {
  request: {
    query: CategoriesDocument,
    variables: variables.categories
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockProductCategoriesSuccess: MockedResponse<ProductCategoriesQuery> =
  {
    request: {
      query: ProductCategoriesDocument,
      variables: variables.productCategories
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockSearchProductSuccess: MockedResponse<SearchProductQuery> = {
  request: {
    query: SearchProductDocument,
    variables: variables.searchProduct
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockFeaturesSuccess: MockedResponse<FeaturesQuery> = {
  request: {
    query: FeaturesDocument //no variables
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockPreviouslyPurchasedProductsSuccess: MockedResponse<PreviouslyPurchasedProductsQuery> =
  {
    request: {
      query: PreviouslyPurchasedProductsDocument,
      variables: variables.previouslyPurchasedProducts
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockCreditCardSetupUrlSuccess: MockedResponse<CreditCardSetupUrlQuery> =
  {
    request: {
      query: CreditCardSetupUrlDocument,
      variables: variables.creditCardSetupUrl
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockCreditCardElementInfoSuccess: MockedResponse<CreditCardElementInfoQuery> =
  {
    request: {
      query: CreditCardElementInfoDocument,
      variables: variables.creditCardElementInfo
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockCreditCardListSuccess: MockedResponse<CreditCardListQuery> = {
  request: {
    query: CreditCardListDocument,
    variables: variables.creditCardList
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockUnapprovedAccountRequestsSuccess: MockedResponse<UnapprovedAccountRequestsQuery> =
  {
    request: {
      query: UnapprovedAccountRequestsDocument,
      variables: variables.unapprovedAccountRequests
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockGetApprovedAccountRequestsSuccess: MockedResponse<GetApprovedAccountRequestsQuery> =
  {
    request: {
      query: GetApprovedAccountRequestsDocument,
      variables: variables.getApprovedAccountRequests
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockRejectedAccountRequestsSuccess: MockedResponse<RejectedAccountRequestsQuery> =
  {
    request: {
      query: RejectedAccountRequestsDocument,
      variables: variables.rejectedAccountRequests
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockGetOrdersPendingApprovalSuccess: MockedResponse<GetOrdersPendingApprovalQuery> =
  {
    request: {
      query: GetOrdersPendingApprovalDocument //no variables
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockOrderPendingApprovalSuccess: MockedResponse<OrderPendingApprovalQuery> =
  {
    request: {
      query: OrderPendingApprovalDocument,
      variables: variables.orderPendingApproval
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockAccountSuccess: MockedResponse<AccountQuery> = {
  request: {
    query: AccountDocument,
    variables: variables.account
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockAccountDetailsSuccess: MockedResponse<AccountDetailsQuery> = {
  request: {
    query: AccountDetailsDocument,
    variables: variables.accountDetails
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockGetContactInfoSuccess: MockedResponse<GetContactInfoQuery> = {
  request: {
    query: GetContactInfoDocument,
    variables: variables.getContactInfo
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockGetPhoneTypesSuccess: MockedResponse<GetPhoneTypesQuery> = {
  request: {
    query: GetPhoneTypesDocument // no variables
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockGetUserInviteSuccess: MockedResponse<GetUserInviteQuery> = {
  request: {
    query: GetUserInviteDocument,
    variables: variables.getUserInvite
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockGetBranchesListSuccess: MockedResponse<GetBranchesListQuery> =
  {
    request: {
      query: GetBranchesListDocument //no argument
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockVerifyUserEmailSuccess: MockedResponse<VerifyUserEmailQuery> =
  {
    request: {
      query: VerifyUserEmailDocument,
      variables: variables.verifyUserEmail
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockVerifyAccountSuccess: MockedResponse<VerifyUserEmailQuery> = {
  request: {
    query: VerifyAccountNewDocument,
    variables: variables.verifyAccount
  },
  result: {
    // TODO: add dummy data
  }
};

/******************************/
/* Queries - Errors           */
/******************************/
export const mockEntitySearchQueryError: MockedResponse<EntitySearchQuery> = {
  request: {
    query: EntitySearchDocument,
    variables: variables.entitySearchQuery
  },
  error: ERROR_RESPONSE
};

export const mockGetRolesQueryError: MockedResponse<GetRolesQuery> = {
  request: { query: GetRolesDocument },
  error: ERROR_RESPONSE
};

export const mockGetApproversQueryError: MockedResponse<GetApproversQuery> = {
  request: {
    query: GetApproversDocument,
    variables: variables.getApprovers
  },
  error: ERROR_RESPONSE
};

export const mockProductQueryError: MockedResponse<GetProductQuery> = {
  request: {
    query: GetProductDocument,
    variables: variables.getProductQuery
  },
  error: ERROR_RESPONSE
};

export const mockProductPricingQueryError: MockedResponse<GetProductPricingQuery> =
  {
    request: {
      query: GetProductPricingDocument,
      variables: variables.getProductPricing
    },
    error: ERROR_RESPONSE
  };

export const mockProductInventoryError: MockedResponse<GetProductInventoryQuery> =
  {
    request: {
      query: GetProductInventoryDocument,
      variables: variables.getproductInventory
    },
    error: ERROR_RESPONSE
  };

export const mockCartQueryError: MockedResponse<CartQuery> = {
  request: {
    query: CartDocument,
    variables: variables.cartQuery
  },
  error: ERROR_RESPONSE
};

export const mockCartFromQuoteError: MockedResponse<CartFromQuoteQuery> = {
  request: {
    query: CartFromQuoteDocument,
    variables: variables.cartFromQuote
  },
  error: ERROR_RESPONSE
};

export const mockCartUserIdAccountError: MockedResponse<CartUserIdAccountIdQuery> =
  {
    request: {
      query: CartUserIdAccountIdDocument,
      variables: variables.cartUserIdAccountId
    },
    error: ERROR_RESPONSE
  };

export const mockErpAccountError: MockedResponse<ErpAccountQuery> = {
  request: {
    query: ErpAccountDocument,
    variables: variables.erpAccount
  },
  error: ERROR_RESPONSE
};

export const mockWillCallBranchesError: MockedResponse<WillCallBranchesQuery> =
  {
    request: {
      query: WillCallBranchesDocument,
      variables: variables.willCallBranches
    },
    error: ERROR_RESPONSE
  };

export const mockGetOrderError: MockedResponse<GetOrderQuery> = {
  request: {
    query: GetOrderDocument,
    variables: variables.getOrder
  },
  error: ERROR_RESPONSE
};

export const mockOrdersError: MockedResponse<OrdersQuery> = {
  request: {
    query: OrdersDocument,
    variables: variables.orders
  },
  error: ERROR_RESPONSE
};

export const mockInvitedUserEmailSentError: MockedResponse<InvitedUserEmailSentQuery> =
  {
    request: {
      query: InvitedUserEmailSentDocument,
      variables: variables.invitedUserEmailSent
    },
    error: ERROR_RESPONSE
  };

export const mockGetListError: MockedResponse<GetListQuery> = {
  request: {
    query: GetListDocument,
    variables: variables.getList
  },
  error: ERROR_RESPONSE
};

export const mockGetListsError: MockedResponse<GetListsQuery> = {
  request: {
    query: GetListsDocument,
    variables: variables.getLists
  },
  error: ERROR_RESPONSE
};

export const mockGetExportListIntoCSVError: MockedResponse<GetExportListIntoCsvQuery> =
  {
    request: {
      query: GetExportListIntoCsvDocument,
      variables: variables.getExportListIntoCSV
    },
    error: ERROR_RESPONSE
  };

export const mockGetBranchError: MockedResponse<GetBranchQuery> = {
  request: {
    query: GetBranchDocument,
    variables: variables.getBranch
  },
  error: ERROR_RESPONSE
};

export const mockFindBranchesError: MockedResponse<FindBranchesQuery> = {
  request: {
    query: FindBranchesDocument,
    variables: variables.findBranches
  },
  error: ERROR_RESPONSE
};

export const mockUserError: MockedResponse<UserQuery> = {
  request: {
    query: UserDocument,
    variables: variables.user
  },
  error: ERROR_RESPONSE
};

export const mockUserAccountsError: MockedResponse<UserAccountsQuery> = {
  request: {
    query: UserAccountsDocument,
    variables: variables.userAccounts
  },
  error: ERROR_RESPONSE
};

export const mockGetSelectedErpAccountError: MockedResponse<GetSelectedErpAccountQuery> =
  {
    request: {
      query: GetSelectedErpAccountDocument,
      variables: variables.getSelectedErpAccount
    },
    error: ERROR_RESPONSE
  };

export const mockGetSelectedErpAccountsError: MockedResponse<GetSelectedErpAccountsQuery> =
  {
    request: {
      query: GetSelectedErpAccountsDocument,
      variables: variables.getSelectedErpAccounts
    },
    error: ERROR_RESPONSE
  };

export const mockQuotesError: MockedResponse<QuotesQuery> = {
  request: {
    query: QuotesDocument,
    variables: variables.quotes
  },
  error: ERROR_RESPONSE
};

export const mockGetHomeBranchError: MockedResponse<GetHomeBranchQuery> = {
  request: {
    query: GetHomeBranchDocument,
    variables: variables.getHomeBranch
  },
  error: ERROR_RESPONSE
};

export const mockSubmitContractOrderReviewError: MockedResponse<SubmitContractOrderReviewQuery> =
  {
    request: {
      query: SubmitContractOrderReviewDocument,
      variables: variables.submitContractOrderReview
    },
    error: ERROR_RESPONSE
  };

export const mockSubmitContractOrderFromCartError: MockedResponse<SubmitContractOrderFromCartQuery> =
  {
    request: {
      query: SubmitContractOrderFromCartDocument,
      variables: variables.submitContractOrderFromCart
    },
    error: ERROR_RESPONSE
  };

export const mockInvoicesError: MockedResponse<InvoicesQuery> = {
  request: {
    query: InvoicesDocument,
    variables: variables.invoices
  },
  error: ERROR_RESPONSE
};

export const mockInvoicesUrlError: MockedResponse<InvoicesUrlQuery> = {
  request: {
    query: InvoicesUrlDocument,
    variables: variables.invoicesUrl
  },
  error: ERROR_RESPONSE
};

export const mockGetInvoiceError: MockedResponse<GetInvoiceQuery> = {
  request: {
    query: GetInvoiceDocument,
    variables: variables.getInvoice
  },
  error: ERROR_RESPONSE
};

export const mockSearchSuggestionError: MockedResponse<SearchSuggestionQuery> =
  {
    request: {
      query: SearchSuggestionDocument,
      variables: variables.searchSuggestion
    },
    error: ERROR_RESPONSE
  };

export const mockGetRejectionReasonsError: MockedResponse<GetRejectionReasonsQuery> =
  {
    request: {
      query: GetRejectionReasonsDocument //no variables
    },
    error: ERROR_RESPONSE
  };

export const mockContractsError: MockedResponse<ContractsQuery> = {
  request: {
    query: ContractsDocument,
    variables: variables.contracts
  },
  error: ERROR_RESPONSE
};

export const mockGetAllUnapprovedAccountRequestsError: MockedResponse<GetAllUnapprovedAccountRequestsQuery> =
  {
    request: {
      query: GetAllUnapprovedAccountRequestsDocument //no variables
    },
    error: ERROR_RESPONSE
  };

export const mockAccountErpIdError: MockedResponse<AccountErpIdQuery> = {
  request: {
    query: AccountErpIdDocument,
    variables: variables.accountErpId
  },
  error: ERROR_RESPONSE
};

export const mockCategoriesError: MockedResponse<CategoriesQuery> = {
  request: {
    query: CategoriesDocument,
    variables: variables.categories
  },
  error: ERROR_RESPONSE
};

export const mockProductCategoriesError: MockedResponse<ProductCategoriesQuery> =
  {
    request: {
      query: ProductCategoriesDocument,
      variables: variables.productCategories
    },
    error: ERROR_RESPONSE
  };

export const mockSearchProductError: MockedResponse<SearchProductQuery> = {
  request: {
    query: SearchProductDocument,
    variables: variables.searchProduct
  },
  error: ERROR_RESPONSE
};

export const mockFeaturesError: MockedResponse<FeaturesQuery> = {
  request: {
    query: FeaturesDocument //no variables
  },
  error: ERROR_RESPONSE
};

export const mockPreviouslyPurchasedProductsError: MockedResponse<PreviouslyPurchasedProductsQuery> =
  {
    request: {
      query: PreviouslyPurchasedProductsDocument,
      variables: variables.previouslyPurchasedProducts
    },
    error: ERROR_RESPONSE
  };

export const mockCreditCardSetupUrlError: MockedResponse<CreditCardSetupUrlQuery> =
  {
    request: {
      query: CreditCardSetupUrlDocument,
      variables: variables.creditCardSetupUrl
    },
    error: ERROR_RESPONSE
  };

export const mockCreditCardElementInfoError: MockedResponse<CreditCardElementInfoQuery> =
  {
    request: {
      query: CreditCardElementInfoDocument,
      variables: variables.creditCardElementInfo
    },
    error: ERROR_RESPONSE
  };

export const mockCreditCardListError: MockedResponse<CreditCardListQuery> = {
  request: {
    query: CreditCardListDocument,
    variables: variables.creditCardList
  },
  error: ERROR_RESPONSE
};

export const mockUnapprovedAccountRequestsError: MockedResponse<UnapprovedAccountRequestsQuery> =
  {
    request: {
      query: UnapprovedAccountRequestsDocument,
      variables: variables.unapprovedAccountRequests
    },
    error: ERROR_RESPONSE
  };

export const mockGetApprovedAccountRequestsError: MockedResponse<GetApprovedAccountRequestsQuery> =
  {
    request: {
      query: GetApprovedAccountRequestsDocument,
      variables: variables.getApprovedAccountRequests
    },
    error: ERROR_RESPONSE
  };

export const mockRejectedAccountRequestsError: MockedResponse<RejectedAccountRequestsQuery> =
  {
    request: {
      query: RejectedAccountRequestsDocument,
      variables: variables.rejectedAccountRequests
    },
    error: ERROR_RESPONSE
  };

export const mockGetOrdersPendingApprovalError: MockedResponse<GetOrdersPendingApprovalQuery> =
  {
    request: {
      query: GetOrdersPendingApprovalDocument //no variables
    },
    error: ERROR_RESPONSE
  };

export const mockOrderPendingApprovalError: MockedResponse<OrderPendingApprovalQuery> =
  {
    request: {
      query: OrderPendingApprovalDocument,
      variables: variables.orderPendingApproval
    },
    error: ERROR_RESPONSE
  };

export const mockAccountError: MockedResponse<AccountQuery> = {
  request: {
    query: AccountDocument,
    variables: variables.account
  },
  error: ERROR_RESPONSE
};

export const mockAccountDetailsError: MockedResponse<AccountDetailsQuery> = {
  request: {
    query: AccountDetailsDocument,
    variables: variables.accountDetails
  },
  error: ERROR_RESPONSE
};

export const mockGetContactInfoError: MockedResponse<GetContactInfoQuery> = {
  request: {
    query: GetContactInfoDocument,
    variables: variables.getContactInfo
  },
  error: ERROR_RESPONSE
};

export const mockGetPhoneTypesError: MockedResponse<GetPhoneTypesQuery> = {
  request: {
    query: GetPhoneTypesDocument // no variables
  },
  error: ERROR_RESPONSE
};

export const mockGetUserInviteError: MockedResponse<GetUserInviteQuery> = {
  request: {
    query: GetUserInviteDocument,
    variables: variables.getUserInvite
  },
  error: ERROR_RESPONSE
};

export const mockGetBranchesListError: MockedResponse<GetBranchesListQuery> = {
  request: {
    query: GetBranchesListDocument //no argument
  },
  error: ERROR_RESPONSE
};

export const mockVerifyUserEmailError: MockedResponse<VerifyUserEmailQuery> = {
  request: {
    query: VerifyUserEmailDocument,
    variables: variables.verifyUserEmail
  },
  error: ERROR_RESPONSE
};

export const mockVerifyAccountError: MockedResponse<VerifyUserEmailQuery> = {
  request: {
    query: VerifyAccountNewDocument,
    variables: variables.verifyAccount
  },
  error: ERROR_RESPONSE
};

/******************************/
/* Mutations - Success        */
/******************************/
export const mockCreateJobFormMutationSuccess: MockedResponse<CreateJobFormMutation> =
  {
    request: {
      query: CreateJobFormDocument,
      variables: variables.createJobForm
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockCreateCartMutationSuccess: MockedResponse<CreateCartMutation> =
  {
    request: {
      query: CreateCartDocument,
      variables: variables.createCart
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockAddItemsToCartMutationSuccess: MockedResponse<AddItemsToCartMutation> =
  {
    request: {
      query: AddItemsToCartDocument,
      variables: variables.addItemsToCart
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockAddAllListItemsToCartSuccess: MockedResponse<AddAllListItemsToCartMutation> =
  {
    request: {
      query: AddAllListItemsToCartDocument,
      variables: variables.addItemsToCart
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockDeleteItemSuccess: MockedResponse<DeleteItemMutation> = {
  request: {
    query: DeleteItemDocument,
    variables: variables.deleteItem
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockUpdateCartSuccess: MockedResponse<UpdateCartMutation> = {
  request: {
    query: UpdateCartDocument,
    variables: variables.updateCart
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockRefreshCartSuccess: MockedResponse<RefreshCartMutation> = {
  request: {
    query: RefreshCartDocument,
    variables: variables.refreshCart
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockDeleteCartItemsSuccess: MockedResponse<DeleteCartItemsMutation> =
  {
    request: {
      query: DeleteCartItemsDocument,
      variables: variables.deleteCartItems
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockUpdateDeliverySuccess: MockedResponse<UpdateDeliveryMutation> =
  {
    request: {
      query: UpdateDeliveryDocument,
      variables: variables.updateDelivery
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockUpdateWillCallSuccess: MockedResponse<UpdateWillCallMutation> =
  {
    request: {
      query: UpdateWillCallDocument,
      variables: variables.updateDelivery
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockUpdateWillCallBranchSuccess: MockedResponse<UpdateWillCallBranchMutation> =
  {
    request: {
      query: UpdateWillCallBranchDocument,
      variables: variables.updateWillCallBranch
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockUpdateItemQuantitySuccess: MockedResponse<UpdateItemQuantityMutation> =
  {
    request: {
      query: UpdateItemQuantityDocument,
      variables: variables.updateItemQuantity
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockSubmitOrderPreviewSuccess: MockedResponse<SubmitOrderPreviewMutation> =
  {
    request: {
      query: SubmitOrderPreviewDocument,
      variables: variables.submitOrderPreview
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockSubmitOrderSuccess: MockedResponse<SubmitOrderMutation> = {
  request: {
    query: SubmitOrderDocument,
    variables: variables.submitOrder
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockResendLegacyInviteEmailSuccess: MockedResponse<ResendLegacyInviteEmailMutation> =
  {
    request: {
      query: ResendLegacyInviteEmailDocument,
      variables: variables.resendLegacyInviteEmail
    },
    result: {
      data: { resendLegacyInviteEmail: 'example@email.com' }
      // TODO: add dummy data
    }
  };

export const mockCreateListSuccess: MockedResponse<CreateListMutation> = {
  request: {
    query: CreateListDocument,
    variables: variables.createList
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockDeleteListSuccess: MockedResponse<DeleteListMutation> = {
  request: {
    query: DeleteListDocument,
    variables: variables.deleteList
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockUpdateListSuccess: MockedResponse<UpdateListMutation> = {
  request: {
    query: UpdateListDocument,
    variables: variables.UpdateList
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockUploadNewListSuccess: MockedResponse<UploadNewListMutation> = {
  request: {
    query: UploadNewListDocument,
    variables: variables.uploadNewList
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockUploadToListSuccess: MockedResponse<UploadToListMutation> = {
  request: {
    query: UploadToListDocument,
    variables: variables.uploadToList
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockAddItemToListSuccess: MockedResponse<AddItemToListMutation> = {
  request: {
    query: AddItemToListDocument,
    variables: variables.addItemToList
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockToggleItemInListsSuccess: MockedResponse<ToggleItemInListsMutation> =
  {
    request: {
      query: ToggleItemInListsDocument,
      variables: variables.toggleItemInLists
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockUpdateUserPasswordSuccess: MockedResponse<UpdateUserPasswordMutation> =
  {
    request: {
      query: UpdateUserPasswordDocument,
      variables: variables.updateUserPassword
    },
    result: {
      // TODO: add dummy data
    }
  };
export const mockSendContactFormSuccess: MockedResponse<SendContactFormMutation> =
  {
    request: {
      query: SendContactFormDocument,
      variables: variables.sendContactForm
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockDeleteContractCartSuccess: MockedResponse<DeleteContractCartMutation> =
  {
    request: {
      query: DeleteContractCartDocument,
      variables: variables.deleteContractCart
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockUpdateUserSuccess: MockedResponse<UpdateUserMutation> = {
  request: {
    query: UpdateUserDocument,
    variables: variables.updateUser
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockApproveUserSuccess: MockedResponse<ApproveUserMutation> = {
  request: {
    query: ApproveUserDocument,
    variables: variables.approveUser
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockDeleteUserSuccess: MockedResponse<DeleteUserMutation> = {
  request: {
    query: DeleteUserDocument,
    variables: variables.deleteUser
  },
  result: {
    data: { deleteUser: true }
    // TODO: add dummy data
  }
};

export const mockCheckUsersForApproverSuccess: MockedResponse<CheckUsersForApproverMutation> =
  {
    request: {
      query: CheckUsersForApproverDocument,
      variables: variables.checkUsersForApprover
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockSetFeatureEnabledSuccess: MockedResponse<SetFeatureEnabledMutation> =
  {
    request: {
      query: SetFeatureEnabledDocument,
      variables: variables.setFeatureEnabled
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockRefreshContactSuccess: MockedResponse<RefreshContactMutation> =
  {
    request: {
      query: RefreshContactDocument,
      variables: variables.refreshContact
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockAddCreditCardSuccess: MockedResponse<AddCreditCardMutation> = {
  request: {
    query: AddCreditCardDocument,
    variables: variables.addCreditCard
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockDeleteCreditCardSuccess: MockedResponse<DeleteCreditCardMutation> =
  {
    request: {
      query: DeleteCreditCardDocument,
      variables: variables.deleteCreditCard
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockApproveOrderSuccess: MockedResponse<ApproveOrderMutation> = {
  request: {
    query: ApproveOrderDocument,
    variables: variables.approveOrder
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockRejectOrderSuccess: MockedResponse<RejectOrderMutation> = {
  request: {
    query: RejectOrderDocument,
    variables: variables.rejectOrder
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockCreateUserSuccess: MockedResponse<CreateUserMutation> = {
  request: {
    query: CreateUserDocument,
    variables: variables.createUser
  },
  result: {
    // TODO: add dummy data
  }
};
export const mockCreateNewUserSuccess: MockedResponse<CreateNewUserMutation> = {
  request: {
    query: CreateNewUserDocument,
    variables: variables.createNewUser
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockCreateNewEmployeeSuccess: MockedResponse<CreateNewEmployeeMutation> =
  {
    request: {
      query: CreateNewEmployeeDocument,
      variables: variables.createNewEmployee
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockVerifyUserSuccess: MockedResponse<VerifyUserMutation> = {
  request: {
    query: VerifyUserDocument,
    variables: variables.verifyUser
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockResendVerificationEmailSuccess: MockedResponse<ResendVerificationEmailMutation> =
  {
    request: {
      query: ResendVerificationEmailDocument,
      variables: variables.resendVerificationEmail
    },
    result: {
      // TODO: add dummy data
    }
  };

export const mockUpdateBranchSuccess: MockedResponse<UpdateBranchMutation> = {
  request: {
    query: UpdateBranchDocument,
    variables: variables.updateBranch
  },
  result: {
    // TODO: add dummy data
  }
};

export const mockRejectUserSuccess: MockedResponse<RejectUserMutation> = {
  request: {
    query: RejectUserDocument,
    variables: variables.rejectUser
  },
  result: {
    // TODO: add dummy data
  }
};

/******************************/
/* Mutations - Errors         */
/******************************/
export const mockCreateJobFormMutationError: MockedResponse<CreateJobFormMutation> =
  {
    request: {
      query: CreateJobFormDocument,
      variables: variables.createJobForm
    },
    error: ERROR_RESPONSE
  };

export const mockCreateCartMutationError: MockedResponse<CreateCartMutation> = {
  request: {
    query: CreateCartDocument,
    variables: variables.createCart
  },
  error: ERROR_RESPONSE
};

export const mockAddItemsToCartMutationError: MockedResponse<AddItemsToCartMutation> =
  {
    request: {
      query: AddItemsToCartDocument,
      variables: variables.addItemsToCart
    },
    error: ERROR_RESPONSE
  };

export const mockAddAllListItemsToCartError: MockedResponse<AddAllListItemsToCartMutation> =
  {
    request: {
      query: AddAllListItemsToCartDocument,
      variables: variables.addItemsToCart
    },
    error: ERROR_RESPONSE
  };

export const mockDeleteItemError: MockedResponse<DeleteItemMutation> = {
  request: {
    query: DeleteItemDocument,
    variables: variables.deleteItem
  },
  error: ERROR_RESPONSE
};

export const mockUpdateCartError: MockedResponse<UpdateCartMutation> = {
  request: {
    query: UpdateCartDocument,
    variables: variables.updateCart
  },
  error: ERROR_RESPONSE
};

export const mockRefreshCartError: MockedResponse<RefreshCartMutation> = {
  request: {
    query: RefreshCartDocument,
    variables: variables.refreshCart
  },
  error: ERROR_RESPONSE
};

export const mockDeleteCartItemsError: MockedResponse<DeleteCartItemsMutation> =
  {
    request: {
      query: DeleteCartItemsDocument,
      variables: variables.deleteCartItems
    },
    error: ERROR_RESPONSE
  };

export const mockUpdateDeliveryError: MockedResponse<UpdateDeliveryMutation> = {
  request: {
    query: UpdateDeliveryDocument,
    variables: variables.updateDelivery
  },
  error: ERROR_RESPONSE
};

export const mockUpdateWillCallError: MockedResponse<UpdateWillCallMutation> = {
  request: {
    query: UpdateWillCallDocument,
    variables: variables.updateDelivery
  },
  error: ERROR_RESPONSE
};

export const mockUpdateWillCallBranchError: MockedResponse<UpdateWillCallBranchMutation> =
  {
    request: {
      query: UpdateWillCallBranchDocument,
      variables: variables.updateWillCallBranch
    },
    error: ERROR_RESPONSE
  };

export const mockUpdateItemQuantityError: MockedResponse<UpdateItemQuantityMutation> =
  {
    request: {
      query: UpdateItemQuantityDocument,
      variables: variables.updateItemQuantity
    },
    error: ERROR_RESPONSE
  };

export const mockSubmitOrderPreviewError: MockedResponse<SubmitOrderPreviewMutation> =
  {
    request: {
      query: SubmitOrderPreviewDocument,
      variables: variables.submitOrderPreview
    },
    error: ERROR_RESPONSE
  };

export const mockSubmitOrderError: MockedResponse<SubmitOrderMutation> = {
  request: {
    query: SubmitOrderDocument,
    variables: variables.submitOrder
  },
  error: ERROR_RESPONSE
};

export const mockResendLegacyInviteEmailError: MockedResponse<ResendLegacyInviteEmailMutation> =
  {
    request: {
      query: ResendLegacyInviteEmailDocument,
      variables: variables.resendLegacyInviteEmail
    },
    error: ERROR_RESPONSE
  };

export const mockCreateListError: MockedResponse<CreateListMutation> = {
  request: {
    query: CreateListDocument,
    variables: variables.createList
  },
  error: ERROR_RESPONSE
};

export const mockDeleteListError: MockedResponse<DeleteListMutation> = {
  request: {
    query: DeleteListDocument,
    variables: variables.deleteList
  },
  error: ERROR_RESPONSE
};

export const mockUpdateListError: MockedResponse<UpdateListMutation> = {
  request: {
    query: UpdateListDocument,
    variables: variables.UpdateList
  },
  error: ERROR_RESPONSE
};

export const mockUploadNewListError: MockedResponse<UploadNewListMutation> = {
  request: {
    query: UploadNewListDocument,
    variables: variables.uploadNewList
  },
  error: ERROR_RESPONSE
};

export const mockUploadToListError: MockedResponse<UploadToListMutation> = {
  request: {
    query: UploadToListDocument,
    variables: variables.uploadToList
  },
  error: ERROR_RESPONSE
};

export const mockAddItemToListError: MockedResponse<AddItemToListMutation> = {
  request: {
    query: AddItemToListDocument,
    variables: variables.addItemToList
  },
  error: ERROR_RESPONSE
};

export const mockToggleItemInListsError: MockedResponse<ToggleItemInListsMutation> =
  {
    request: {
      query: ToggleItemInListsDocument,
      variables: variables.toggleItemInLists
    },
    error: ERROR_RESPONSE
  };
export const mockUpdateUserPasswordError: MockedResponse<UpdateUserPasswordMutation> =
  {
    request: {
      query: UpdateUserPasswordDocument,
      variables: variables.updateUserPassword
    },
    error: ERROR_RESPONSE
  };
export const mockSendContactFormError: MockedResponse<SendContactFormMutation> =
  {
    request: {
      query: SendContactFormDocument,
      variables: variables.sendContactForm
    },
    error: ERROR_RESPONSE
  };

export const mockDeleteContractCartError: MockedResponse<DeleteContractCartMutation> =
  {
    request: {
      query: DeleteContractCartDocument,
      variables: variables.deleteContractCart
    },
    error: ERROR_RESPONSE
  };

export const mockUpdateUserError: MockedResponse<UpdateUserMutation> = {
  request: {
    query: UpdateUserDocument,
    variables: variables.updateUser
  },
  error: ERROR_RESPONSE
};

export const mockApproveUserError: MockedResponse<ApproveUserMutation> = {
  request: {
    query: ApproveUserDocument,
    variables: variables.approveUser
  },
  error: ERROR_RESPONSE
};

export const mockInviteUserError: MockedResponse<InviteUserMutation> = {
  request: {
    query: InviteUserDocument,
    variables: variables.inviteUser
  },
  error: ERROR_RESPONSE
};

export const mockDeleteUserError: MockedResponse<DeleteUserMutation> = {
  request: {
    query: DeleteUserDocument,
    variables: variables.deleteUser
  },
  error: ERROR_RESPONSE
};

export const mockCheckUsersForApproverError: MockedResponse<CheckUsersForApproverMutation> =
  {
    request: {
      query: CheckUsersForApproverDocument,
      variables: variables.checkUsersForApprover
    },
    error: ERROR_RESPONSE
  };

export const mockSetFeatureEnabledError: MockedResponse<SetFeatureEnabledMutation> =
  {
    request: {
      query: SetFeatureEnabledDocument,
      variables: variables.setFeatureEnabled
    },
    error: ERROR_RESPONSE
  };

export const mockAddCreditCardError: MockedResponse<AddCreditCardMutation> = {
  request: {
    query: AddCreditCardDocument,
    variables: variables.addCreditCard
  },
  error: ERROR_RESPONSE
};

export const mockDeleteCreditCardError: MockedResponse<DeleteCreditCardMutation> =
  {
    request: {
      query: DeleteCreditCardDocument,
      variables: variables.deleteCreditCard
    },
    error: ERROR_RESPONSE
  };

export const mockApproveOrderError: MockedResponse<ApproveOrderMutation> = {
  request: {
    query: ApproveOrderDocument,
    variables: variables.approveOrder
  },
  error: ERROR_RESPONSE
};

export const mockRejectOrderError: MockedResponse<RejectOrderMutation> = {
  request: {
    query: RejectOrderDocument,
    variables: variables.rejectOrder
  },
  error: ERROR_RESPONSE
};

export const mockCreateUserError: MockedResponse<CreateUserMutation> = {
  request: {
    query: CreateUserDocument,
    variables: variables.createUser
  },
  error: ERROR_RESPONSE
};

export const mockCreateNewUserError: MockedResponse<CreateNewUserMutation> = {
  request: {
    query: CreateNewUserDocument,
    variables: variables.createNewUser
  },
  error: ERROR_RESPONSE
};

export const mockCreateNewEmployeeError: MockedResponse<CreateNewEmployeeMutation> =
  {
    request: {
      query: CreateNewEmployeeDocument,
      variables: variables.createNewEmployee
    },
    error: ERROR_RESPONSE
  };

export const mockVerifyUserError: MockedResponse<VerifyUserMutation> = {
  request: {
    query: VerifyUserDocument,
    variables: variables.verifyUser
  },
  error: ERROR_RESPONSE
};

export const mockResendVerificationEmailError: MockedResponse<ResendVerificationEmailMutation> =
  {
    request: {
      query: ResendVerificationEmailDocument,
      variables: variables.resendVerificationEmail
    },
    error: ERROR_RESPONSE
  };

export const mockUpdateBranchError: MockedResponse<UpdateBranchMutation> = {
  request: {
    query: UpdateBranchDocument,
    variables: variables.updateBranch
  },
  error: ERROR_RESPONSE
};

export const mockRejectUserError: MockedResponse<RejectUserMutation> = {
  request: {
    query: RejectUserDocument,
    variables: variables.rejectUser
  },
  error: ERROR_RESPONSE
};
