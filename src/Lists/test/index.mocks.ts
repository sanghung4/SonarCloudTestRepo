import {
  DeleteListDocument,
  GetListDocument,
  GetListsDocument,
  GetProductPricingDocument,
  List,
  ListLineItem,
  ToggleItemInListsDocument
} from 'generated/graphql';
import { ListContextType } from 'providers/ListsProvider';

import * as t from 'locales/en/translation.json';

const mocks = {
  lists: [
    {
      request: {
        query: GetListsDocument,
        variables: {
          billToAccountId: 'noListsAccount'
        }
      },
      result: {
        data: {
          lists: []
        }
      }
    },
    {
      request: {
        query: GetListsDocument,
        variables: {
          billToAccountId: 'testAccount'
        }
      },
      result: {
        data: {
          lists: [
            {
              billToAccountId: 'testAccount',
              id: 'list1',
              listLineItems: [
                {
                  id: 'l1_item1',
                  erpPartNumber: 'L1_I1',
                  sortOrder: 0,
                  quantity: 2
                },
                {
                  id: 'l1_item2',
                  erpPartNumber: 'L1_I2',
                  sortOrder: 1,
                  quantity: 45
                },
                {
                  id: 'l1_item3',
                  erpPartNumber: 'L1_I3',
                  sortOrder: 2,
                  quantity: 5
                }
              ],
              name: 'List One',
              __typename: 'List'
            },
            {
              billToAccountId: 'testAccount',
              id: 'list2',
              listLineItems: [
                {
                  id: 'l2_item1',
                  erpPartNumber: 'L2_I1',
                  sortOrder: 0,
                  quantity: 27
                },
                {
                  id: 'l2_item2',
                  erpPartNumber: 'L2_I2',
                  sortOrder: 1,
                  quantity: 1
                }
              ],
              name: 'List Two',
              __typename: 'List'
            }
          ]
        }
      }
    },
    {
      request: {
        query: GetListsDocument,
        variables: {
          billToAccountId: 'testAccount'
        }
      },
      result: {
        data: {
          lists: [
            {
              billToAccountId: 'testAccount',
              id: 'list1',
              listLineItems: [
                {
                  id: 'l1_item2',
                  erpPartNumber: 'L1_I2',
                  sortOrder: 1,
                  quantity: 45
                },
                {
                  id: 'l1_item3',
                  erpPartNumber: 'L1_I3',
                  sortOrder: 2,
                  quantity: 5
                }
              ],
              name: 'List One',
              __typename: 'List'
            },
            {
              billToAccountId: 'testAccount',
              id: 'list2',
              listLineItems: [
                {
                  id: 'l2_item1',
                  erpPartNumber: 'L2_I1',
                  sortOrder: 0,
                  quantity: 27
                },
                {
                  id: 'l2_item2',
                  erpPartNumber: 'L2_I2',
                  sortOrder: 1,
                  quantity: 1
                }
              ],
              name: 'List Two',
              __typename: 'List'
            }
          ]
        }
      }
    },
    {
      request: {
        query: GetProductPricingDocument,
        variables: {
          input: {
            customerId: '',
            branchId: 'any',
            productIds: ['L1_I1', 'L1_I2', 'L1_I3'],
            includeListData: false
          }
        }
      },
      result: {
        data: {
          productPricing: {
            customerId: 'testAccount',
            branch: 'any',
            products: [
              {
                productId: 'L1_I1',
                orderUom: 'ea',
                catalogId: '',
                branchAvailableQty: 2,
                totalAvailableQty: 3,
                listIds: [],
                sellPrice: 3.256
              },
              {
                productId: 'L1_I2',
                orderUom: 'ea',
                catalogId: '',
                branchAvailableQty: 2,
                totalAvailableQty: 3,
                listIds: [],
                sellPrice: 0.68
              },
              {
                productId: 'L1_I3',
                orderUom: 'ea',
                catalogId: '',
                branchAvailableQty: 2,
                totalAvailableQty: 3,
                listIds: [],
                sellPrice: 0.68
              }
            ]
          }
        }
      }
    },
    {
      request: {
        query: GetListDocument,
        variables: {
          listId: 'list1',
          userId: 'testuser',
          shipToAccountId: 'testAccount',
          branchId: 'any'
        }
      },
      result: {
        data: {
          list: {
            billToAccountId: 'testAccount',
            id: 'list1',
            listLineItems: [
              {
                id: 'l1_item1',
                listId: 'list1',
                imageUrls: {
                  thumb: 'thumb.jpg',
                  __typename: 'ImageUrls'
                },
                erpPartNumber: 'L1_I1',
                sortOrder: 0,
                quantity: 2,
                manufacturerName: 'Tesla',
                manufacturerNumber: '3',
                minIncrementQty: 1,
                name: 'Model S Plaid',
                pricePerUnit: '129990.00',
                status: 'Stock',
                stock: {
                  homeBranch: {
                    availability: 3,
                    __typename: 'StoreStock'
                  },
                  __typename: 'Stock'
                },
                __typename: 'ListLineItem'
              },
              {
                id: 'l1_item2',
                listId: 'list1',
                imageUrls: {
                  thumb: 'thumb.jpg',
                  __typename: 'ImageUrls'
                },
                erpPartNumber: 'L1_I2',
                sortOrder: 1,
                quantity: 45,
                manufacturerName: 'Nissan',
                manufacturerNumber: '280',
                minIncrementQty: 1,
                name: 'Z Coupe',
                pricePerUnit: '40000.00',
                status: 'Stock',
                stock: {
                  homeBranch: {
                    availability: 3,
                    __typename: 'StoreStock'
                  },
                  __typename: 'Stock'
                },
                __typename: 'ListLineItem'
              },
              {
                id: 'l1_item3',
                listId: 'list1',
                imageUrls: {
                  thumb: 'thumb.jpg',
                  __typename: 'ImageUrls'
                },
                erpPartNumber: 'L1_I3',
                sortOrder: 2,
                quantity: 5,
                manufacturerName: 'Ford',
                manufacturerNumber: '40',
                minIncrementQty: 1,
                name: 'Shelby GT350R',
                pricePerUnit: '74630.00',
                status: 'Stock',
                stock: {
                  homeBranch: {
                    availability: 3,
                    __typename: 'StoreStock'
                  },
                  __typename: 'Stock'
                },
                __typename: 'ListLineItem'
              }
            ],
            name: 'List One',
            __typename: 'List'
          }
        }
      }
    },
    {
      request: {
        query: GetListDocument,
        variables: {
          listId: 'list1',
          userId: 'testuser',
          shipToAccountId: 'testAccount',
          branchId: 'any'
        }
      },
      result: {
        data: {
          list: {
            billToAccountId: 'testAccount',
            id: 'list1',
            listLineItems: [
              {
                id: 'l1_item2',
                listId: 'list1',
                imageUrls: {
                  thumb: 'thumb.jpg',
                  __typename: 'ImageUrls'
                },
                erpPartNumber: 'L1_I2',
                sortOrder: 1,
                quantity: 45,
                manufacturerName: 'Nissan',
                manufacturerNumber: '280',
                minIncrementQty: 1,
                name: 'Z Coupe',
                pricePerUnit: '40000.00',
                status: 'Stock',
                stock: {
                  homeBranch: {
                    availability: 3,
                    __typename: 'StoreStock'
                  },
                  __typename: 'Stock'
                },
                __typename: 'ListLineItem'
              },
              {
                id: 'l1_item3',
                listId: 'list1',
                imageUrls: {
                  thumb: 'thumb.jpg',
                  __typename: 'ImageUrls'
                },
                erpPartNumber: 'L1_I3',
                sortOrder: 2,
                quantity: 5,
                manufacturerName: 'Ford',
                manufacturerNumber: '40',
                minIncrementQty: 1,
                name: 'Shelby GT350R',
                pricePerUnit: '74630.00',
                status: 'Stock',
                stock: {
                  homeBranch: {
                    availability: 3,
                    __typename: 'StoreStock'
                  },
                  __typename: 'Stock'
                },
                __typename: 'ListLineItem'
              }
            ],
            name: 'List One',
            __typename: 'List'
          }
        }
      }
    },
    {
      request: {
        query: ToggleItemInListsDocument,
        variables: {
          toggleItemInListsInput: {
            listIds: ['list1'],
            itemToToggle: {
              erpPartNumber: 'L1_I1'
            }
          }
        }
      },
      result: {
        data: {
          toggleItemInLists: {
            lists: [
              {
                billToAccountId: 'testAccount',
                id: 'list1',
                listLineItems: [
                  {
                    id: 'l1_item1',
                    erpPartNumber: 'L1_I1',
                    sortOrder: 0,
                    quantity: 2
                  },
                  {
                    id: 'l1_item2',
                    erpPartNumber: 'L1_I2',
                    sortOrder: 1,
                    quantity: 45
                  },
                  {
                    id: 'l1_item3',
                    erpPartNumber: 'L1_I3',
                    sortOrder: 2,
                    quantity: 5
                  }
                ],
                name: 'List One',
                __typename: 'List'
              }
            ]
          }
        }
      }
    },
    {
      request: {
        query: DeleteListDocument,
        variables: {
          listId: 'list1'
        }
      },
      result: {
        data: {
          deleteList: {
            id: 'list1',
            success: true,
            __typename: 'DeleteListResponse'
          }
        }
      }
    }
  ],
  uploadList: [
    {
      request: {
        query: GetListsDocument,
        variables: {
          billToAccountId: 'testAccount'
        }
      },
      result: {
        data: {
          lists: [
            {
              billToAccountId: 'testAccount',
              id: 'newList',
              listLineItems: [
                {
                  id: 'l1_item1',
                  erpPartNumber: 'L1_I1',
                  sortOrder: 0,
                  quantity: 2,
                  manufacturerName: 'Tesla',
                  manufacturerNumber: '3',
                  minIncrementQty: 1,
                  name: 'Model S Plaid',
                  pricePerUnit: '129990.00',
                  status: 'Stock',
                  stock: {
                    homeBranch: {
                      availability: 3
                    }
                  }
                },
                {
                  id: 'l1_item2',
                  erpPartNumber: 'L1_I2',
                  sortOrder: 1,
                  quantity: 45,
                  manufacturerName: 'Nissan',
                  manufacturerNumber: '280',
                  minIncrementQty: 1,
                  name: 'Z Coupe',
                  pricePerUnit: '40000.00',
                  status: 'Stock',
                  stock: {
                    homeBranch: {
                      availability: 3
                    }
                  }
                },
                {
                  id: 'l1_item3',
                  erpPartNumber: 'L1_I3',
                  sortOrder: 2,
                  quantity: 5,
                  manufacturerName: 'Ford',
                  manufacturerNumber: '40',
                  minIncrementQty: 1,
                  name: 'Shelby GT350R',
                  pricePerUnit: '74630.00',
                  status: 'Stock',
                  stock: {
                    homeBranch: {
                      availability: 3
                    }
                  }
                }
              ],
              name: 'New List',
              __typename: 'List'
            },
            {
              billToAccountId: 'testAccount',
              id: 'existingList',
              listLineItems: [
                {
                  id: 'l1_item1',
                  erpPartNumber: 'L1_I1',
                  sortOrder: 0,
                  quantity: 2,
                  manufacturerName: 'Tesla',
                  manufacturerNumber: '3',
                  minIncrementQty: 1,
                  name: 'Model S Plaid',
                  pricePerUnit: '129990.00',
                  status: 'Stock',
                  stock: {
                    homeBranch: {
                      availability: 3
                    }
                  }
                },
                {
                  id: 'l1_item2',
                  erpPartNumber: 'L1_I2',
                  sortOrder: 1,
                  quantity: 45,
                  manufacturerName: 'Nissan',
                  manufacturerNumber: '280',
                  minIncrementQty: 1,
                  name: 'Z Coupe',
                  pricePerUnit: '40000.00',
                  status: 'Stock',
                  stock: {
                    homeBranch: {
                      availability: 3
                    }
                  }
                },
                {
                  id: 'l1_item3',
                  erpPartNumber: 'L1_I3',
                  sortOrder: 2,
                  quantity: 10,
                  manufacturerName: 'Ford',
                  manufacturerNumber: '40',
                  minIncrementQty: 1,
                  name: 'Shelby GT350R',
                  pricePerUnit: '74630.00',
                  status: 'Stock',
                  stock: {
                    homeBranch: {
                      availability: 3
                    }
                  }
                }
              ],
              name: 'Existing List',
              __typename: 'List'
            }
          ]
        }
      }
    }
  ]
};

export const EMPTY_LIST_MOCK = {
  VALID_LIST: {
    availableListIds: ['3147d508-8398-4a31-8f38-e451863a60b0'],
    lists: [
      {
        id: '3147d508-8398-4a31-8f38-e451863a60b0',
        name: 'Copper Clamps V2',
        billToAccountId: '507ee1d7-4f6b-4ebd-af79-a06d3dfeefce',
        listLineItems: [] as ListLineItem[],
        __typename: 'List'
      }
    ],
    selectedList: {
      id: '3147d508-8398-4a31-8f38-e451863a60b0',
      name: 'Copper Clamps V2',
      billToAccountId: '507ee1d7-4f6b-4ebd-af79-a06d3dfeefce',
      listLineItems: [] as ListLineItem[],
      __typename: 'List'
    }
  } as ListContextType
};

export const LIST_PROVIDER_MOCKS = {
  VALID_LIST: {
    availableListIds: [
      '1bd15d80-fb49-4410-965a-0f212374ac3f',
      '3e1bbf45-25c3-4876-a30d-c8adff1c7fa3'
    ],
    lists: [
      {
        id: '3147d508-8398-4a31-8f38-e451863a60b0',
        name: 'Copper Clamps V2',
        billToAccountId: '507ee1d7-4f6b-4ebd-af79-a06d3dfeefce',
        listLineItems: [
          {
            id: '1bd15d80-fb49-4410-965a-0f212374ac3f',
            erpPartNumber: '33860',
            sortOrder: 1,
            quantity: 2,
            __typename: 'ListLineItem'
          },
          {
            id: '3e1bbf45-25c3-4876-a30d-c8adff1c7fa3',
            erpPartNumber: '263101',
            sortOrder: 2,
            quantity: 1,
            __typename: 'ListLineItem'
          },
          {
            id: '4dd39af0-a60f-4788-a846-bdf90ab63bdf',
            erpPartNumber: '31747',
            sortOrder: 0,
            quantity: 1,
            __typename: 'ListLineItem'
          }
        ],
        __typename: 'List'
      },
      {
        id: '57c6ae7e-9195-4363-9cfc-668a00d66f27',
        name: 'Copper Stuff',
        billToAccountId: '507ee1d7-4f6b-4ebd-af79-a06d3dfeefce',
        listLineItems: [
          {
            id: '85e45f31-07cf-4cdc-8221-764aea26255b',
            erpPartNumber: '39707',
            sortOrder: 0,
            quantity: 1000,
            __typename: 'ListLineItem'
          }
        ],
        __typename: 'List'
      },
      {
        id: '102cca54-bcb6-4c22-9983-4a43b1330753',
        name: 'And Another One Gone',
        billToAccountId: '507ee1d7-4f6b-4ebd-af79-a06d3dfeefce',
        listLineItems: [
          {
            id: 'fd965094-02d5-4e8f-b4b5-bfef3a2dbf71',
            erpPartNumber: '31747',
            sortOrder: 4,
            quantity: 1,
            __typename: 'ListLineItem'
          },
          {
            id: '193f7647-1f8a-44d2-bc5f-3ce7654921c7',
            erpPartNumber: '30243',
            sortOrder: 7,
            quantity: 1,
            __typename: 'ListLineItem'
          },
          {
            id: '7d35d4f7-ba1a-4551-a54a-dc7c52900ee6',
            erpPartNumber: '48370',
            sortOrder: 10,
            quantity: 1,
            __typename: 'ListLineItem'
          },
          {
            id: '3bcf476f-8dff-45b3-aca1-21c361ed96d3',
            erpPartNumber: '153479',
            sortOrder: 5,
            quantity: 1,
            __typename: 'ListLineItem'
          },
          {
            id: '33229d36-7a48-4a62-9f83-cfa765117dfc',
            erpPartNumber: '92329',
            sortOrder: 11,
            quantity: 5,
            __typename: 'ListLineItem'
          },
          {
            id: 'eb5013e5-8d77-4331-b702-14420d610b97',
            erpPartNumber: '43704',
            sortOrder: 3,
            quantity: 1,
            __typename: 'ListLineItem'
          },
          {
            id: '66d829c8-00d6-42a8-b4b1-b0a3b7e07155',
            erpPartNumber: '153482',
            sortOrder: 6,
            quantity: 1,
            __typename: 'ListLineItem'
          },
          {
            id: '9a762780-c62e-4260-9437-c250520d6782',
            erpPartNumber: '31742',
            sortOrder: 8,
            quantity: 1,
            __typename: 'ListLineItem'
          },
          {
            id: '5b26bdfa-6add-4422-bfeb-47e830643c99',
            erpPartNumber: '48374',
            sortOrder: 2,
            quantity: 4130,
            __typename: 'ListLineItem'
          },
          {
            id: '4a7610c2-c66c-4393-83e8-ff174a5d0b6f',
            erpPartNumber: '55491',
            sortOrder: 9,
            quantity: 1,
            __typename: 'ListLineItem'
          }
        ],
        __typename: 'List'
      },
      {
        id: 'd5a3d95e-2a33-4cd0-b822-8a49908602f3',
        name: 'Hey I\'m Gonna Get "You" Too/',
        billToAccountId: '507ee1d7-4f6b-4ebd-af79-a06d3dfeefce',
        listLineItems: [
          {
            id: 'c7bb389d-ae9c-4d1a-9ccd-e79778d9c44b',
            erpPartNumber: '257708',
            sortOrder: 0,
            quantity: 1,
            __typename: 'ListLineItem'
          }
        ],
        __typename: 'List'
      },
      {
        id: 'fc607d14-545b-4323-9344-8cc2a5532d4d',
        name: 'test154',
        billToAccountId: '507ee1d7-4f6b-4ebd-af79-a06d3dfeefce',
        listLineItems: [
          {
            id: '37949fef-d7eb-4847-aee0-a09001d446d5',
            erpPartNumber: '1528305',
            sortOrder: 9,
            quantity: 1,
            __typename: 'ListLineItem'
          },
          {
            id: 'c8c28520-a9aa-4dc2-9acd-b11e3f4cd88b',
            erpPartNumber: '99603',
            sortOrder: 0,
            quantity: 2,
            __typename: 'ListLineItem'
          },
          {
            id: '676436b3-1655-439e-9c45-129f17547380',
            erpPartNumber: '25697',
            sortOrder: 5,
            quantity: 1,
            __typename: 'ListLineItem'
          },
          {
            id: 'c8c4ecfe-aac7-4ed1-bd3c-1f9bd29c29e0',
            erpPartNumber: '102563',
            sortOrder: 8,
            quantity: 10,
            __typename: 'ListLineItem'
          },
          {
            id: 'abdad809-7456-476d-a832-08c5f8a8674f',
            erpPartNumber: '36290',
            sortOrder: 4,
            quantity: 7,
            __typename: 'ListLineItem'
          },
          {
            id: '4ab37d39-a3b6-4454-92da-d10ec0aedb10',
            erpPartNumber: '99375',
            sortOrder: 2,
            quantity: 1,
            __typename: 'ListLineItem'
          },
          {
            id: '81b3eede-813b-4c09-97c3-468d0dffe7e1',
            erpPartNumber: '79639',
            sortOrder: 6,
            quantity: 1,
            __typename: 'ListLineItem'
          },
          {
            id: '70f2a3c6-349f-4183-857e-ec6dac66a6fb',
            erpPartNumber: '103219',
            sortOrder: 7,
            quantity: 1,
            __typename: 'ListLineItem'
          },
          {
            id: 'd487baf3-9e60-4273-94f4-98dbfa3619b3',
            erpPartNumber: '46409',
            sortOrder: 1,
            quantity: 14,
            __typename: 'ListLineItem'
          },
          {
            id: '1a9cffb2-32ee-4c96-a310-0fe73259074f',
            erpPartNumber: '1454098',
            sortOrder: 3,
            quantity: 1,
            __typename: 'ListLineItem'
          }
        ],
        __typename: 'List'
      },
      {
        id: 'bfacd82f-3c61-42fd-a341-5cee94aa49a6',
        name: 'New list',
        billToAccountId: '507ee1d7-4f6b-4ebd-af79-a06d3dfeefce',
        listLineItems: [],
        __typename: 'List'
      },
      {
        id: 'c9e75b50-d887-4dad-8070-fbbf0591903b',
        name: 'Another One',
        billToAccountId: '507ee1d7-4f6b-4ebd-af79-a06d3dfeefce',
        listLineItems: [
          {
            id: '1e83e7bc-0694-434d-a2d0-f3e4c4eda417',
            erpPartNumber: '39419',
            sortOrder: 9,
            quantity: 1,
            __typename: 'ListLineItem'
          },
          {
            id: 'd0102f25-4f1c-4daf-8422-0bfc12a4e6de',
            erpPartNumber: '22069',
            sortOrder: 7,
            quantity: 1,
            __typename: 'ListLineItem'
          },
          {
            id: 'bb1ad894-9cce-458a-b488-b5f22cfba3a0',
            erpPartNumber: '22059',
            sortOrder: 6,
            quantity: 1,
            __typename: 'ListLineItem'
          },
          {
            id: 'a64eafa9-9243-431d-9b37-03385751fd46',
            erpPartNumber: '22067',
            sortOrder: 5,
            quantity: 142,
            __typename: 'ListLineItem'
          },
          {
            id: '540207cc-dc89-45da-8d17-d734c80a9e4d',
            erpPartNumber: '101962',
            sortOrder: 4,
            quantity: 1,
            __typename: 'ListLineItem'
          },
          {
            id: '6315d424-5d23-4f85-9132-1dab3ce80324',
            erpPartNumber: '90906',
            sortOrder: 0,
            quantity: 9996,
            __typename: 'ListLineItem'
          },
          {
            id: 'e5d8e05c-27a1-4208-8a24-ecbf00457fd2',
            erpPartNumber: '42056',
            sortOrder: 1,
            quantity: 40,
            __typename: 'ListLineItem'
          },
          {
            id: 'be7953ff-10d7-433c-922c-38888946bfb6',
            erpPartNumber: '505644',
            sortOrder: 8,
            quantity: 1,
            __typename: 'ListLineItem'
          },
          {
            id: 'b3e5f300-0544-422a-90f6-db1934173026',
            erpPartNumber: '39363',
            sortOrder: 2,
            quantity: 80,
            __typename: 'ListLineItem'
          },
          {
            id: '7dead00d-dd6c-4a75-8142-468402872eef',
            erpPartNumber: '40562',
            sortOrder: 3,
            quantity: 7854,
            __typename: 'ListLineItem'
          }
        ],
        __typename: 'List'
      },
      {
        id: '13a2cc34-5a42-46b3-b05f-77ddf829dfbb',
        name: 'Test',
        billToAccountId: '507ee1d7-4f6b-4ebd-af79-a06d3dfeefce',
        listLineItems: [
          {
            id: '57969aa0-aec0-45ce-976b-a5b06732d332',
            erpPartNumber: '1528305',
            sortOrder: 1,
            quantity: 1,
            __typename: 'ListLineItem'
          },
          {
            id: '6f81856a-7bbb-4a4c-973c-026b9ee6767c',
            erpPartNumber: '21962',
            sortOrder: 0,
            quantity: 1,
            __typename: 'ListLineItem'
          }
        ],
        __typename: 'List'
      },
      {
        id: 'ce622196-a4ff-4378-8d4e-0000ad175fc8',
        name: 'better name',
        billToAccountId: '507ee1d7-4f6b-4ebd-af79-a06d3dfeefce',
        listLineItems: [],
        __typename: 'List'
      },
      {
        id: '92336f83-2527-4bf6-b37f-3cc03e9d096f',
        name: 'JacobsList',
        billToAccountId: '507ee1d7-4f6b-4ebd-af79-a06d3dfeefce',
        listLineItems: [
          {
            id: '7460a7ac-b2aa-498c-8156-32cd10cf8e86',
            erpPartNumber: '39378',
            sortOrder: 7,
            quantity: 10,
            __typename: 'ListLineItem'
          },
          {
            id: '0a73bf9b-8430-4509-a727-fbd1f4606e51',
            erpPartNumber: '39355',
            sortOrder: 0,
            quantity: 10,
            __typename: 'ListLineItem'
          },
          {
            id: 'bc2b1c93-e300-4020-8a40-8f41ba982736',
            erpPartNumber: '24484',
            sortOrder: 2,
            quantity: 50,
            __typename: 'ListLineItem'
          },
          {
            id: 'c1245728-dd17-4083-b067-aa3fcec74827',
            erpPartNumber: '40544',
            sortOrder: 1,
            quantity: 1,
            __typename: 'ListLineItem'
          },
          {
            id: '5a5c7d2f-fac2-4f56-b6a7-1114cf913147',
            erpPartNumber: '40548',
            sortOrder: 5,
            quantity: 1,
            __typename: 'ListLineItem'
          },
          {
            id: '205dd71d-8232-4e2f-8214-d20000d97d2f',
            erpPartNumber: '39363',
            sortOrder: 6,
            quantity: 10,
            __typename: 'ListLineItem'
          },
          {
            id: '8f828bc5-0e3a-4f3a-a03a-d69ea61f6d52',
            erpPartNumber: '43254',
            sortOrder: 4,
            quantity: 1,
            __typename: 'ListLineItem'
          },
          {
            id: '7b693fc9-b70d-480d-97f0-1f2d8a3f9b22',
            erpPartNumber: '58066',
            sortOrder: 3,
            quantity: 500,
            __typename: 'ListLineItem'
          }
        ],
        __typename: 'List'
      },
      {
        id: '5f1070b0-42e5-4ba8-ae5b-dcc8c5ef21c2',
        name: 'JAcobs',
        billToAccountId: '507ee1d7-4f6b-4ebd-af79-a06d3dfeefce',
        listLineItems: [
          {
            id: '5d584c16-acc4-487c-8279-82fe1694eae5',
            erpPartNumber: '39363',
            sortOrder: 4,
            quantity: 10,
            __typename: 'ListLineItem'
          },
          {
            id: '300a816d-5f27-4260-81fb-322ce8b37ed7',
            erpPartNumber: '40548',
            sortOrder: 5,
            quantity: 1,
            __typename: 'ListLineItem'
          },
          {
            id: '7903dfd7-e5e1-4fad-92b6-e51d265514a7',
            erpPartNumber: '39355',
            sortOrder: 2,
            quantity: 10,
            __typename: 'ListLineItem'
          },
          {
            id: '1ddb1a23-11bb-41c8-85a4-242533d0ff30',
            erpPartNumber: '43254',
            sortOrder: 3,
            quantity: 1,
            __typename: 'ListLineItem'
          },
          {
            id: 'edfdc470-4b40-4217-9479-0bf2973d24e5',
            erpPartNumber: '58066',
            sortOrder: 6,
            quantity: 500,
            __typename: 'ListLineItem'
          },
          {
            id: 'a1a55edf-a5be-404b-b6da-e8707be553af',
            erpPartNumber: '24484',
            sortOrder: 7,
            quantity: 50,
            __typename: 'ListLineItem'
          },
          {
            id: '4490661d-e470-4268-b9f0-569353047db4',
            erpPartNumber: '39378',
            sortOrder: 0,
            quantity: 10,
            __typename: 'ListLineItem'
          },
          {
            id: '97abd090-c2de-457e-9840-3a2754dad8cf',
            erpPartNumber: '40544',
            sortOrder: 1,
            quantity: 1,
            __typename: 'ListLineItem'
          }
        ],
        __typename: 'List'
      }
    ],
    selectedList: {
      id: '3147d508-8398-4a31-8f38-e451863a60b0',
      name: 'Copper Clamps V2',
      billToAccountId: '507ee1d7-4f6b-4ebd-af79-a06d3dfeefce',
      listLineItems: [
        {
          id: '1bd15d80-fb49-4410-965a-0f212374ac3f',
          erpPartNumber: '33860',
          quantity: 2,
          minIncrementQty: 0,
          listId: '3147d508-8398-4a31-8f38-e451863a60b0',
          sortOrder: 1,
          name: '1/2" Wrot Copper Cap',
          imageUrls: {
            thumb:
              'http://images.tradeservice.com/ProductImages/DIR100103/MUELER_W-07004_SML.jpg',
            __typename: 'ImageUrls'
          },
          techSpecs: [
            {
              name: 'material',
              value: 'Copper',
              __typename: 'TechSpec'
            },
            {
              name: 'Applicable Standard',
              value: 'ASME B16.22, ANSI/NSF 61 G',
              __typename: 'TechSpec'
            },
            {
              name: 'Application',
              value: 'Ground Potable Water Supply System',
              __typename: 'TechSpec'
            },
            {
              name: 'End Connection',
              value: 'Female Soldered',
              __typename: 'TechSpec'
            },
            {
              name: 'Material',
              value: 'Copper',
              __typename: 'TechSpec'
            },
            {
              name: 'Nominal Size',
              value: '1/2"',
              __typename: 'TechSpec'
            },
            {
              name: 'Process',
              value: 'Wrot',
              __typename: 'TechSpec'
            },
            {
              name: 'Type',
              value: 'Round Head',
              __typename: 'TechSpec'
            }
          ],
          manufacturerName: 'Generic',
          manufacturerNumber: 'CCAPD',
          pricePerUnit: 43,
          status: 'Stock',
          stock: {
            homeBranch: {
              availability: 62,
              __typename: 'StoreStock'
            },
            __typename: 'Stock'
          },
          __typename: 'ListLineItem'
        },
        {
          id: '3e1bbf45-25c3-4876-a30d-c8adff1c7fa3',
          erpPartNumber: '263101',
          quantity: 1,
          minIncrementQty: 0,
          listId: '3147d508-8398-4a31-8f38-e451863a60b0',
          sortOrder: 2,
          name: '4" x 4" FPT x Copper Sweat Copper Dielectric Union',
          imageUrls: {
            thumb:
              'http://images.tradeservice.com/ProductImages/DIR100148/JONESS_D06400_SML.jpg',
            __typename: 'ImageUrls'
          },
          techSpecs: [
            {
              name: 'End Connection',
              value: 'Female Threaded x Copper Sweat',
              __typename: 'TechSpec'
            },
            {
              name: 'Material',
              value: 'Copper',
              __typename: 'TechSpec'
            },
            {
              name: 'Pressure Rating',
              value: '200 PSI',
              __typename: 'TechSpec'
            },
            {
              name: 'Size A',
              value: '4 Inch',
              __typename: 'TechSpec'
            },
            {
              name: 'Size B',
              value: '4 Inch',
              __typename: 'TechSpec'
            },
            {
              name: 'Temperature Rating',
              value: '200 Deg F',
              __typename: 'TechSpec'
            },
            {
              name: 'Type',
              value: 'Dielectric',
              __typename: 'TechSpec'
            }
          ],
          manufacturerName: 'Jones Stephens',
          manufacturerNumber: 'D06400',
          pricePerUnit: 55513,
          status: 'Stock',
          stock: {
            homeBranch: {
              availability: 0,
              __typename: 'StoreStock'
            },
            __typename: 'Stock'
          },
          __typename: 'ListLineItem'
        },
        {
          id: '4dd39af0-a60f-4788-a846-bdf90ab63bdf',
          erpPartNumber: '31747',
          quantity: 1,
          minIncrementQty: 0,
          listId: '3147d508-8398-4a31-8f38-e451863a60b0',
          sortOrder: 0,
          name: '3/4" Wrot Copper Reducing Coupling',
          imageUrls: {
            thumb:
              'http://images.tradeservice.com/ProductImages/DIR100103/MUELER_WB01130_SML.jpg',
            __typename: 'ImageUrls'
          },
          techSpecs: [
            {
              name: 'material',
              value: 'Copper',
              __typename: 'TechSpec'
            },
            {
              name: 'Applicable Standard',
              value: 'ASME B16.22/B1.20.1, NSF 61G',
              __typename: 'TechSpec'
            },
            {
              name: 'Application',
              value: 'Potable Water',
              __typename: 'TechSpec'
            },
            {
              name: 'End Connection',
              value: 'Female Soldered x Male Threaded',
              __typename: 'TechSpec'
            },
            {
              name: 'Material',
              value: 'Copper',
              __typename: 'TechSpec'
            },
            {
              name: 'Material Specification',
              value: 'ASTM B75',
              __typename: 'TechSpec'
            },
            {
              name: 'Process',
              value: 'Wrot',
              __typename: 'TechSpec'
            },
            {
              name: 'Size A',
              value: '3/4"',
              __typename: 'TechSpec'
            },
            {
              name: 'Size B',
              value: '3/4"',
              __typename: 'TechSpec'
            },
            {
              name: 'Thickness',
              value: '0.033"',
              __typename: 'TechSpec'
            },
            {
              name: 'Type',
              value: 'Male, Straight',
              __typename: 'TechSpec'
            }
          ],
          manufacturerName: 'Generic',
          manufacturerNumber: 'CMAF',
          pricePerUnit: 205,
          status: 'Stock',
          stock: {
            homeBranch: {
              availability: 0,
              __typename: 'StoreStock'
            },
            __typename: 'Stock'
          },
          __typename: 'ListLineItem'
        }
      ],
      __typename: 'List'
    }
  } as ListContextType,
  UPLOAD_NEW_LIST: {
    lists: [
      {
        id: 'newList',
        name: 'New List',
        billToAccountId: 'testAccount',
        listLineItems: [
          {
            id: 'l1_item1',
            erpPartNumber: 'L1_I1',
            sortOrder: 0,
            quantity: 2,
            __typename: 'ListLineItem'
          },
          {
            id: 'l1_item2',
            erpPartNumber: 'L1_I2',
            sortOrder: 1,
            quantity: 45,
            __typename: 'ListLineItem'
          },
          {
            id: 'l1_item3',
            erpPartNumber: 'L1_I3',
            sortOrder: 2,
            quantity: 5,
            __typename: 'ListLineItem'
          }
        ],
        __typename: 'List'
      },
      {
        id: 'existingList',
        name: 'Existing List',
        billToAccountId: 'testAccount',
        listLineItems: [
          {
            id: 'l1_item1',
            erpPartNumber: 'L1_I1',
            sortOrder: 0,
            quantity: 2,
            __typename: 'ListLineItem'
          },
          {
            id: 'l1_item2',
            erpPartNumber: 'L1_I2',
            sortOrder: 1,
            quantity: 45,
            __typename: 'ListLineItem'
          },
          {
            id: 'l1_item3',
            erpPartNumber: 'L1_I3',
            sortOrder: 2,
            quantity: 10,
            __typename: 'ListLineItem'
          }
        ],
        __typename: 'List'
      }
    ],
    selectedList: {
      id: 'newList',
      name: 'New List',
      billToAccountId: 'testAccount',
      listLineItems: [
        {
          id: 'l1_item1',
          erpPartNumber: 'L1_I1',
          sortOrder: 0,
          quantity: 2,
          __typename: 'ListLineItem'
        },
        {
          id: 'l1_item2',
          erpPartNumber: 'L1_I2',
          sortOrder: 1,
          quantity: 45,
          __typename: 'ListLineItem'
        },
        {
          id: 'l1_item3',
          erpPartNumber: 'L1_I3',
          sortOrder: 2,
          quantity: 5,
          __typename: 'ListLineItem'
        }
      ],
      __typename: 'List'
    },
    setSelectedList: (_: List) => {},
    setShowAlert: (_: [any, any]) => {},
    showAlert: [
      t.lists.createNewSuccess.replace('{{name}}', 'New List'),
      {
        variant: 'success'
      }
    ],
    uploadErrors: [
      {
        partNumber: 'P40FCP',
        description: '2" x 20\' PVC SCH 40 Foam Cor',
        manufacturerName: 'Generic',
        quantity: 60,
        __typename: 'ListUploadError'
      },
      {
        partNumber: 'FC4365',
        description: '3" x 2" Grooved x Grooved Cop',
        manufacturerName: 'Victaulic',
        quantity: 5,
        __typename: 'ListUploadError'
      },
      {
        partNumber: '541-CPK2',
        description: '1.25" Top Plate Connector',
        manufacturerName: 'Sioux Chief',
        quantity: 4,
        __typename: 'ListUploadError'
      },
      {
        partNumber: '12345',
        description: 'Copper T',
        manufacturerName: 'Streamline',
        quantity: 2,
        __typename: 'ListUploadError'
      }
    ],
    uploadType: 'new'
  } as ListContextType,
  UPLOAD_TO_LIST: {
    lists: [
      {
        id: 'newList',
        name: 'New List',
        billToAccountId: 'testAccount',
        listLineItems: [
          {
            id: 'l1_item1',
            erpPartNumber: 'L1_I1',
            sortOrder: 0,
            quantity: 2,
            __typename: 'ListLineItem'
          },
          {
            id: 'l1_item2',
            erpPartNumber: 'L1_I2',
            sortOrder: 1,
            quantity: 45,
            __typename: 'ListLineItem'
          },
          {
            id: 'l1_item3',
            erpPartNumber: 'L1_I3',
            sortOrder: 2,
            quantity: 5,
            __typename: 'ListLineItem'
          }
        ],
        __typename: 'List'
      },
      {
        id: 'existingList',
        name: 'Existing List',
        billToAccountId: 'testAccount',
        listLineItems: [
          {
            id: 'l1_item1',
            erpPartNumber: 'L1_I1',
            sortOrder: 0,
            quantity: 2,
            __typename: 'ListLineItem'
          },
          {
            id: 'l1_item2',
            erpPartNumber: 'L1_I2',
            sortOrder: 1,
            quantity: 45,
            __typename: 'ListLineItem'
          },
          {
            id: 'l1_item3',
            erpPartNumber: 'L1_I3',
            sortOrder: 2,
            quantity: 10,
            __typename: 'ListLineItem'
          }
        ],
        __typename: 'List'
      }
    ],
    selectedList: {
      id: 'existingList',
      name: 'Existing List',
      billToAccountId: 'testAccount',
      listLineItems: [
        {
          id: 'l1_item1',
          erpPartNumber: 'L1_I1',
          sortOrder: 0,
          quantity: 2,
          __typename: 'ListLineItem'
        },
        {
          id: 'l1_item2',
          erpPartNumber: 'L1_I2',
          sortOrder: 1,
          quantity: 45,
          __typename: 'ListLineItem'
        },
        {
          id: 'l1_item3',
          erpPartNumber: 'L1_I3',
          sortOrder: 2,
          quantity: 10,
          __typename: 'ListLineItem'
        }
      ],
      __typename: 'List'
    },
    setSelectedList: (_: List) => {},
    setShowAlert: (_: [any, any]) => {},
    showAlert: [
      t.lists.uploadSuccess_one.replace('{{count}}', '1'),
      {
        variant: 'success'
      }
    ],
    uploadErrors: [
      {
        partNumber: 'L1_I3',
        manufacturerName: 'Ford',
        quantity: 5,
        description: 'Shelby GT350R'
      }
    ],
    uploadType: 'existing'
  } as ListContextType
};

export const COPPER_CLAMPS_ADD_TO_CART = [
  {
    productId: '31747',
    qty: 1,
    minIncrementQty: 0,
    qtyAvailable: 550,
    uom: 'ea',
    pricePerUnit: 3.256
  },
  {
    productId: '33860',
    qty: 2,
    minIncrementQty: 0,
    qtyAvailable: 95,
    uom: 'ea',
    pricePerUnit: 0.68
  },
  {
    productId: '263101',
    qty: 1,
    minIncrementQty: 0,
    qtyAvailable: 95,
    uom: 'ea',
    pricePerUnit: 0.68
  }
];

export const ADD_ALL_LIST_TO_CART = '3147d508-8398-4a31-8f38-e451863a60b0';

export default mocks;
