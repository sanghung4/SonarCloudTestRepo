import { mockBranch } from 'Branches/tests/mocks';
import {
  CreateListMutation,
  DeleteListMutation,
  GetListQuery,
  GetListsQuery,
  List,
  ListLineItem,
  UpdateListMutation,
  UploadNewListMutation,
  UploadToListMutation
} from 'generated/graphql';

/**
 * Config
 */
export const billToAccountId = 'testAccount';
export const shipToAccountId = 'testAccount';
export const branchId = mockBranch.branchId;
export const userId = 'testUser';

/**
 * Dummy Data
 */
export const mockLists: List[] = [
  {
    id: 'e558613d-36a3-449d-9295-558c616ef6a4',
    name: 'test test 2',
    billToAccountId: '507ee1d7-4f6b-4ebd-af79-a06d3dfeefce',
    listLineItems: [],
    __typename: 'List'
  },
  {
    id: 'b1d6d1e6-9eff-4421-9e10-add607db0b30',
    name: 'test12',
    billToAccountId: '507ee1d7-4f6b-4ebd-af79-a06d3dfeefce',
    listLineItems: [],
    listLineItemsSize: 0,
    __typename: 'List'
  },
  {
    id: '0200c581-5e18-4d7a-bfad-8bb1b3389bd1',
    name: 'test3234',
    billToAccountId: '507ee1d7-4f6b-4ebd-af79-a06d3dfeefce',
    listLineItems: [],
    listLineItemsSize: 0,
    __typename: 'List'
  }
];

export const mockList: List = {
  ...mockLists[0],
  listLineItems: [
    {
      id: '71d792bd-4480-4c30-8216-a65e6360842a',
      erpPartNumber: '54321',
      quantity: 3,
      minIncrementQty: 0,
      listId: 'd01fea35-7047-4643-9b97-0d20a54ef59e',
      sortOrder: 1,
      name: 'MOEN RGHIN M-PACT POSI 1H TS IPS 2510',
      imageUrls: {
        thumb:
          'https://ecomm-prod-product-img-store.s3.amazonaws.com/Moen/S/2510.jpg',
        __typename: 'ImageUrls'
      },
      manufacturerName: 'Moen',
      manufacturerNumber: '2510',
      status: null,
      __typename: 'ListLineItem'
    },
    {
      id: '12204ca5-a915-43ef-b5b9-25cbc73f3040',
      erpPartNumber: '48374',
      quantity: 20,
      minIncrementQty: 10,
      listId: 'd01fea35-7047-4643-9b97-0d20a54ef59e',
      sortOrder: 2,
      name: '2" X 10\' PVC SCH 40 Foam Core Pipe - Plain End',
      imageUrls: {
        thumb:
          'http://images.tradeservice.com/ProductImages/DIR100103/CHRPIP_PVC-10002_SML.jpg',
        __typename: 'ImageUrls'
      },
      manufacturerName: 'Generic',
      manufacturerNumber: 'P40FCPK10',
      status: null,
      __typename: 'ListLineItem'
    },
    {
      id: 'aff14786-12a8-4c95-9368-bc84fed5a3a7',
      erpPartNumber: '1610060',
      quantity: 7,
      minIncrementQty: 0,
      listId: 'd01fea35-7047-4643-9b97-0d20a54ef59e',
      sortOrder: 0,
      name: '3/4 X 15 CPVC Pipe',
      imageUrls: {
        thumb:
          'http://images.tradeservice.com/ProductImages/DIR100115/VICORP_34PIPE_SML.jpg',
        __typename: 'ImageUrls'
      },
      manufacturerName: 'Viking Group',
      manufacturerNumber: '34PIPE',
      status: null,
      __typename: 'ListLineItem'
    }
  ]
};

export const mockListLineItem: ListLineItem = { ...mockList.listLineItems[0] };

/**
 * GQL mocks
 */
export const GET_LISTS_EMPTY: GetListsQuery = { lists: [] };
export const GET_LISTS_RES: GetListsQuery = { lists: [...mockLists] };
export const GET_LIST_RES: GetListQuery = { list: { ...mockList } };
export const CREATE_LIST_RES: CreateListMutation = {
  createList: { ...mockList }
};
export const UPDATE_LIST_RES: UpdateListMutation = {
  updateList: { ...mockList }
};
export const DELETE_LIST_RES: DeleteListMutation = {
  deleteList: { id: mockList.id, success: true }
};
export const UPLOAD_NEW_LIST_RES: UploadNewListMutation = {
  uploadNewList: {
    __typename: 'ListUploadResponse',
    successfulRowCount: 3,
    listId: mockList.id,
    errors: []
  }
};
export const UPLOAD_TO_LIST_RES: UploadToListMutation = {
  uploadToList: {
    __typename: 'ListUploadResponse',
    successfulRowCount: 3,
    listId: mockList.id,
    errors: []
  }
};
