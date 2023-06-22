import { ApolloError } from '@apollo/client';
import { act } from '@testing-library/react';

import { UserContextType } from 'AuthProvider';
import {
  CreateListMutation,
  DeleteListMutation,
  GetListQuery,
  GetListsQuery,
  UpdateListMutation,
  UploadNewListMutation,
  UploadToListMutation,
  useCreateListMutation,
  useDeleteListMutation,
  useGetListLazyQuery,
  useGetListsLazyQuery,
  useToggleItemInListsMutation,
  useUpdateListMutation,
  useUploadNewListMutation,
  useUploadToListMutation
} from 'generated/graphql';
import * as t from 'locales/en/translation.json';
import ListsProvider, {
  ListContextType,
  ListMode,
  useListsContext
} from 'providers/ListsProvider';
import { SelectedAccounts } from 'providers/SelectedAccountsProvider';
import {
  billToAccountId,
  CREATE_LIST_RES,
  DELETE_LIST_RES,
  GET_LISTS_EMPTY,
  GET_LISTS_RES,
  GET_LIST_RES,
  mockList,
  mockListLineItem,
  UPDATE_LIST_RES,
  UPLOAD_NEW_LIST_RES,
  UPLOAD_TO_LIST_RES
} from 'providers/tests/list.mocks';
import { render } from 'test-utils/TestWrapper';
import { trackUploadList } from 'utils/analytics';

/**
 * Types
 */
type Mocks = {
  auth?: Partial<UserContextType>;
  selectedAccounts: SelectedAccounts;
  pathname: string;
  pushAlert: jest.Mock<any, any>;
  trackUploadList: jest.Mock<any, any>;
  history: {
    push: jest.Mock<any, any>;
    replace: jest.Mock<any, any>;
  };
  useGetListsLazyQuery: {
    data: GetListsQuery;
    loading: boolean;
  };
  useGetListLazyQuery: {
    data?: GetListQuery;
    loading: boolean;
  };
  useCreateListMutation: {
    data: CreateListMutation;
    fn: jest.Mock<any, any>;
    loading: boolean;
  };
  useUpdateListMutation: {
    data: UpdateListMutation;
    fn: jest.Mock<any, any>;
    loading: boolean;
  };
  useDeleteListMutation: {
    data: DeleteListMutation;
    fn: jest.Mock<any, any>;
    loading: boolean;
  };
  useUploadNewListMutation: {
    data?: UploadNewListMutation;
    loading: boolean;
    error?: ApolloError;
  };
  useUploadToListMutation: {
    data?: UploadToListMutation;
    loading: boolean;
    error?: ApolloError;
  };
};
type MockGQLProp<R> = {
  fetchPolicy?: string;
  onCompleted?: (data: R) => void;
  onError?: (e: ApolloError) => void;
};

/**
 * Mock values
 */
const defaultMocks: Mocks = {
  history: { push: jest.fn(), replace: jest.fn() },
  pushAlert: jest.fn(),
  trackUploadList: jest.fn(),
  pathname: '/',
  selectedAccounts: {},
  useGetListsLazyQuery: { data: { ...GET_LISTS_EMPTY }, loading: false },
  useGetListLazyQuery: { loading: false },
  useCreateListMutation: {
    data: { ...CREATE_LIST_RES },
    loading: false,
    fn: jest.fn()
  },
  useUpdateListMutation: {
    data: { ...UPDATE_LIST_RES },
    loading: false,
    fn: jest.fn()
  },
  useDeleteListMutation: {
    data: { ...DELETE_LIST_RES },
    loading: false,
    fn: jest.fn()
  },
  useUploadNewListMutation: { loading: false },
  useUploadToListMutation: { loading: false }
};
let mocks = { ...defaultMocks };

/**
 * Mock methods
 */
jest.mock('@dialexa/reece-component-library', () => ({
  ...jest.requireActual('@dialexa/reece-component-library'),
  useSnackbar: () => ({ pushAlert: mocks.pushAlert })
}));
jest.mock('generated/graphql', () => ({
  ...jest.requireActual('generated/graphql'),
  useGetListsLazyQuery: jest.fn(),
  useGetListLazyQuery: jest.fn(),
  useCreateListMutation: jest.fn(),
  useUpdateListMutation: jest.fn(),
  useDeleteListMutation: jest.fn(),
  useUploadNewListMutation: jest.fn(),
  useUploadToListMutation: jest.fn(),
  useToggleItemInListsMutation: jest.fn()
}));
jest.mock('react-router-dom', () => ({
  useHistory: () => mocks.history,
  useLocation: () => ({ pathname: mocks.pathname })
}));
jest.mock('utils/analytics', () => ({
  ...jest.requireActual('utils/analytics'),
  trackUploadList: jest.fn()
}));

/**
 * Setup
 */
function setup(mocks: Mocks) {
  // Init output
  let listContext = {} as ListContextType;

  // Create a dummy component to call the context
  function Component() {
    Object.assign(listContext, useListsContext());
    return null;
  }

  // Render the dummy component wrapped with ListsProvider
  render(
    <ListsProvider>
      <Component />
    </ListsProvider>,
    {
      authConfig: mocks.auth,
      selectedAccountsConfig: { selectedAccounts: mocks.selectedAccounts }
    }
  );
  return listContext;
}

/**
 * MAIN TEST
 */
describe('providers/ListProvider', () => {
  // ⚪ Reset mocks
  afterEach(() => {
    mocks = { ...defaultMocks };
  });

  // 🔵 MOCK GQL/custom hooks (these are in the same declaration order as the Provider)
  beforeEach(() => {
    // 🔵 Hook - useGetListsLazyQuery
    (useGetListsLazyQuery as jest.Mock).mockImplementation(
      (param: MockGQLProp<GetListsQuery>) => {
        const { data, loading } = mocks.useGetListsLazyQuery;
        const call = jest.fn(() => param.onCompleted?.(data));
        return [call, { loading }];
      }
    );
    // 🔵 Hook - useGetListLazyQuery
    (useGetListLazyQuery as jest.Mock).mockImplementation(
      (param: MockGQLProp<GetListQuery | undefined>) => {
        const { data, loading } = mocks.useGetListLazyQuery;
        const call = jest.fn(() => {
          param.onCompleted?.(data);
          return { data };
        });
        return [call, { loading }];
      }
    );
    // 🔵 Hook - useCreateListMutation
    (useCreateListMutation as jest.Mock).mockImplementation(
      (param: MockGQLProp<CreateListMutation>) => {
        const { data, loading, fn } = mocks.useCreateListMutation;
        const call = fn.mockImplementation(() => {
          param.onCompleted?.(data);
          return { data };
        });
        return [call, { loading }];
      }
    );
    // 🔵 Hook - useUpdateListMutation
    (useUpdateListMutation as jest.Mock).mockImplementation(
      (param: MockGQLProp<UpdateListMutation>) => {
        const { data, loading, fn } = mocks.useUpdateListMutation;
        const call = fn.mockImplementation(() => {
          param.onCompleted?.(data);
          return { data };
        });
        return [call, { loading }];
      }
    );
    // 🔵 Hook - useDeleteListMutation
    (useDeleteListMutation as jest.Mock).mockImplementation(
      (param: MockGQLProp<DeleteListMutation>) => {
        const { data, loading, fn } = mocks.useDeleteListMutation;
        const call = fn.mockImplementation(() => {
          param.onCompleted?.(data);
          return { data };
        });
        return [call, { loading }];
      }
    );
    // 🔵 Hook - useUploadNewListMutation
    (useUploadNewListMutation as jest.Mock).mockImplementation(
      (param: MockGQLProp<UploadNewListMutation | undefined>) => {
        const { data, loading, error } = mocks.useUploadNewListMutation;
        const call = jest.fn(() => {
          if (error) {
            param.onError?.(error);
          }
          return { data };
        });
        return [call, { loading, reset: jest.fn() }];
      }
    );
    // 🔵 Hook - useUploadToListMutation
    (useUploadToListMutation as jest.Mock).mockImplementation(
      (param: MockGQLProp<UploadToListMutation | undefined>) => {
        const { data, loading, error } = mocks.useUploadToListMutation;
        const call = jest.fn(() => {
          if (error) {
            param.onError?.(error);
          }
          return { data };
        });
        return [call, { loading, reset: jest.fn() }];
      }
    );
    // 🔵 Hook - useToggleItemInListsMutation
    (useToggleItemInListsMutation as jest.Mock).mockImplementation(
      (param: MockGQLProp<void>) => [jest.fn(() => param.onCompleted?.())]
    );
    // 🔵 Method - trackUploadList
    (trackUploadList as jest.Mock).mockImplementation((...args) =>
      mocks.trackUploadList(...args)
    );
  });

  // 🟢 1 - Default
  it('Expect to match snapshot under default render condition', () => {
    // act
    const context = setup(mocks);

    // assert
    expect(context).toMatchSnapshot();
  });

  // 🟢 2 - useEffect: initListsEffect > getLists > empty
  it('Expect `lists` to be empty', () => {
    // arrange
    mocks.pathname = '/lists';
    mocks.selectedAccounts = { billTo: { id: billToAccountId } };

    // act
    const context = setup(mocks);

    // assert
    expect(context.lists.length).toBe(0);
  });

  // 🟢 3 - useEffect: initListsEffect > getLists > full
  it('Expect `lists` populated from the response to match the original data length', () => {
    // arrange
    mocks.pathname = '/lists';
    mocks.selectedAccounts = { billTo: { id: billToAccountId } };
    mocks.useGetListsLazyQuery.data = { ...GET_LISTS_RES };

    // act
    const context = setup(mocks);

    // assert
    const expectedLength = GET_LISTS_RES.lists.length;
    expect(context.lists.length).toBe(expectedLength);
  });

  // 🟢 4 - useEffect: initListsEffect > getLists > getSelectedList
  it('Expect `selectedList` populated from the response to match the original data length', () => {
    // arrange
    mocks.pathname = '/lists';
    mocks.selectedAccounts = { billTo: { id: billToAccountId } };
    mocks.useGetListsLazyQuery.data = { ...GET_LISTS_RES };
    mocks.useGetListLazyQuery.data = { ...GET_LIST_RES };

    // act
    const context = setup(mocks);

    // assert
    const expectedLength = GET_LIST_RES.list.listLineItems.length;
    expect(context.selectedList?.listLineItems.length).toBe(expectedLength);
  });

  // 🟢 5 - toggleItemInLists > nothing
  it('Expect matching context when `toggleItemFromList` is called with no selectedList', () => {
    // arrange
    mocks.pathname = '/lists';
    mocks.selectedAccounts = { billTo: { id: billToAccountId } };
    mocks.useGetListsLazyQuery.data = { ...GET_LISTS_EMPTY };

    // act
    const context = setup(mocks);
    const oldContext = { ...context };
    act(() => {
      context.toggleItemFromList?.('test');
    });

    // assert
    expect(context).toMatchObject(oldContext);
  });

  // 🟢 6 - toggleItemInLists > toggleItem > setSelectedPartNumber
  it('Expect reset selectedPartNumber when `toggleItemFromList` is called with selectedList', () => {
    // arrange
    mocks.pathname = '/lists';
    mocks.selectedAccounts = { billTo: { id: billToAccountId } };
    mocks.useGetListsLazyQuery.data = { ...GET_LISTS_RES };
    mocks.useGetListLazyQuery.data = { ...GET_LIST_RES };

    // act 1
    const context = setup(mocks);
    act(() => {
      context.setSelectedPartNumber('test-part-num');
    });
    // assert 1
    expect(context.selectedPartNumber).toBeTruthy();

    // act 2
    act(() => {
      context.toggleItemFromList?.('test');
    });
    // assert 2
    expect(context.selectedList?.listLineItems.length).toBeTruthy();
  });

  // 🟢 7 - toggleItemInLists > toggleItem (clearList)
  it('Expect blank selectedList line item when `toggleItemFromList` is called with clearList', () => {
    // arrange
    mocks.pathname = '/lists';
    mocks.selectedAccounts = { billTo: { id: billToAccountId } };
    mocks.useGetListsLazyQuery.data = { ...GET_LISTS_RES };
    mocks.useGetListLazyQuery.data = { ...GET_LIST_RES };

    // act 1
    const context = setup(mocks);
    // assert 1
    expect(context.selectedList?.listLineItems.length).toBeTruthy();

    // act 2
    act(() => {
      context.toggleItemFromList?.('test', true);
    });
    // assert 2
    expect(context.selectedList?.listLineItems.length).toBeFalsy();
  });

  // 🟢 8 - toggleItemInLists > nothing
  it('Expect matching context when `toggleItemInLists` is called with no selectedPartNumber', () => {
    // arrange
    mocks.pathname = '/lists';
    mocks.selectedAccounts = { billTo: { id: billToAccountId } };
    mocks.useGetListsLazyQuery.data = { ...GET_LISTS_EMPTY };

    // act
    const context = setup(mocks);
    const oldContext = { ...context };
    act(() => {
      context.toggleItemInLists?.([]);
    });

    // assert
    expect(context).toMatchObject(oldContext);
  });

  // 🟢 9 - toggleItemInLists > setIsUpdating
  it('Expect undefined isUpdating when `toggleItemInLists` is called with selectedPartNumber', () => {
    // arrange
    mocks.pathname = '/lists';
    mocks.selectedAccounts = { billTo: { id: billToAccountId } };
    mocks.useGetListsLazyQuery.data = { ...GET_LISTS_RES };

    // act
    const context = setup(mocks);
    act(() => {
      context.setSelectedPartNumber('test');
    });
    act(() => {
      context.toggleItemInLists?.(['']);
    });

    // assert
    expect(context.isUpdating).toBeUndefined();
  });

  // 🟢 10 - toggleItemInLists > setIsUpdating (match)
  it('Expect undefined isUpdating when `toggleItemInLists` is called with selectedPartNumber with matching selected listId', () => {
    // arrange
    mocks.pathname = '/lists';
    mocks.selectedAccounts = { billTo: { id: billToAccountId } };
    mocks.useGetListsLazyQuery.data = { ...GET_LISTS_RES };
    mocks.useGetListLazyQuery.data = { ...GET_LIST_RES };

    // act
    const context = setup(mocks);
    act(() => {
      context.setSelectedPartNumber('test');
    });
    act(() => {
      context.toggleItemInLists?.([GET_LIST_RES.list.id]);
    });

    // assert
    expect(context.isUpdating).toBeUndefined();
  });

  // 🟢 11 - initiateAddToList
  it('Expect updated context values with `initiateAddToList`', () => {
    // act
    const context = setup(mocks);
    act(() => {
      context.initiateAddToList('part-number', 1, []);
    });

    // assert
    expect(context.open).toBeTruthy();
    expect(context.selectedPartNumber).toBeTruthy();
    expect(context.listMode).toBe(ListMode.ADD_ITEM);
  });

  // 🟢 12 - resetUploadState
  it('Expect updated context values with `resetUploadState`', () => {
    // act 1
    const context = setup(mocks);
    act(() => {
      context.setShowUploadErrors(true);
      context.setFileUploadError(['Test']);
    });
    // asssert 1
    expect(context.showUploadErrors).toBeTruthy();
    expect(context.fileUploadError.length).toBeTruthy();

    // act 2
    act(() => {
      context.resetUploadState();
    });
    // assert 2
    expect(context.showUploadErrors).toBeFalsy();
    expect(context.fileUploadError.length).toBeFalsy();
  });

  // 🟢 13 - setSelectedList
  it('Expect truthy selectedList with `setSelectedList`', () => {
    // arrange
    mocks.auth = { profile: undefined };
    mocks.useGetListLazyQuery.data = undefined;

    // act
    const context = setup(mocks);
    act(() => {
      context.setSelectedList({ ...mockList });
    });

    // assert
    expect(context.selectedList).toBeTruthy();
  });

  // 🟢 14 - setSelectedList (nothing)
  it('Expect falsey lists and selectedList with `resetListProvider`', () => {
    // arrange
    mocks.pathname = '/lists';
    mocks.selectedAccounts = { billTo: { id: billToAccountId } };
    mocks.useGetListsLazyQuery.data = { ...GET_LISTS_RES };
    mocks.useGetListLazyQuery.data = { ...GET_LIST_RES };

    // act 1
    const context = setup(mocks);
    // assert 1
    expect(context.lists.length).toBeTruthy();
    expect(context.selectedList).toBeTruthy();

    // act 2
    act(() => {
      context.resetListProvider();
    });
    // assert 2
    expect(context.lists.length).toBeFalsy();
    expect(context.selectedList).toBeFalsy();
  });

  // 🟢 15 - createList (basic)
  it('Expect setAvailableListIds to be called with undefined with `createList`', async () => {
    // arrange
    const setAvailableListIds = jest.fn();

    // act
    const context = setup(mocks);
    await act(async () => {
      await context.createList(
        'test',
        undefined,
        undefined,
        setAvailableListIds
      );
    });

    // assert
    expect(setAvailableListIds).toBeCalledWith([mockList.id]);
  });

  // 🟢 16 - createList (include product)
  it('Expect setAvailableListIds to be called with undefined with `createList` including product', async () => {
    // arrange
    const setAvailableListIds = jest.fn();

    // act
    const context = setup(mocks);
    await act(async () => {
      await context.createList('test', true, undefined, setAvailableListIds);
    });

    // assert
    expect(setAvailableListIds).toBeCalledWith([mockList.id]);
  });

  // 🟢 17 - updateList (nothing)
  it('Expect updateListMutation not to be called with `updateList` when selectedList is undefined', () => {
    // act
    const context = setup(mocks);
    act(() => {
      context.updateList('', []);
    });

    // assert
    expect(mocks.useUpdateListMutation.fn).not.toBeCalled();
  });

  // 🟢 18 - updateList
  it('Expect updateListMutation to be called with `updateList`', () => {
    // arrange
    mocks.pathname = '/lists';
    mocks.selectedAccounts = { billTo: { id: billToAccountId } };
    mocks.useGetListsLazyQuery.data = { ...GET_LISTS_RES };
    mocks.useGetListLazyQuery.data = { ...GET_LIST_RES };

    // act
    const context = setup(mocks);
    act(() => {
      context.updateList('', [
        { ...mockListLineItem, erpPartNumber: undefined }
      ]);
    });

    // assert
    expect(mocks.useUpdateListMutation.fn).toBeCalled();
  });

  // 🟢 19 - updateList (rename)
  it('Expect pushAlert to be called with `updateList` with renaming', () => {
    // arrange
    mocks.pathname = '/lists';
    mocks.selectedAccounts = { billTo: { id: billToAccountId } };
    mocks.useGetListsLazyQuery.data = { ...GET_LISTS_RES };
    mocks.useGetListLazyQuery.data = { ...GET_LIST_RES };

    // act
    const context = setup(mocks);
    act(() => {
      context.setSelectedList({ ...mockList });
      context.setIsUpdating('test');
      context.setRenaming(true);
    });
    act(() => {
      context.updateList('', [
        { ...mockListLineItem, erpPartNumber: undefined }
      ]);
    });

    // assert
    expect(mocks.pushAlert).toBeCalledWith(t.lists.renameListSuccess);
  });

  // 🟢 20 - updateList (rename none)
  it("Expect pushAlert NOT to be called with `updateList` with renaming but lists doesn't have the list", () => {
    // arrange
    mocks.pathname = '/lists';
    mocks.selectedAccounts = { billTo: { id: billToAccountId } };
    mocks.useGetListsLazyQuery.data = { ...GET_LISTS_EMPTY };
    mocks.useGetListLazyQuery.data = { ...GET_LIST_RES };

    // act
    const context = setup(mocks);
    act(() => {
      context.setSelectedList({ ...mockList });
      context.setIsUpdating('test');
      context.setRenaming(true);
    });
    act(() => {
      context.updateList('', [
        { ...mockListLineItem, erpPartNumber: undefined }
      ]);
    });

    // assert
    expect(mocks.pushAlert).not.toBeCalled();
  });

  // 🟢 21 - deleteList
  it('Expect deleteListMutation to be called with `deleteLIst`', () => {
    // arrange
    mocks.pathname = '/lists';
    mocks.selectedAccounts = { billTo: { id: billToAccountId } };
    mocks.useGetListsLazyQuery.data = { ...GET_LISTS_RES };
    mocks.useGetListLazyQuery.data = { ...GET_LIST_RES };

    // act
    const context = setup(mocks);
    act(() => {
      context.deleteList(GET_LIST_RES.list.id);
    });

    // assert
    expect(mocks.useDeleteListMutation.fn).toBeCalled();
  });

  // 🟢 22 - uploadNewList (empty)
  it('Expect selected functions not to be called when `uploadNewList` GQL mutations is returning blank', () => {
    // arrange
    const mockFile = new File([], 'test-file');

    // act
    const context = setup(mocks);
    act(() => {
      context.uploadNewList(mockFile, '', '');
    });

    // assert
    expect(mocks.history.push).not.toBeCalled();
    expect(mocks.trackUploadList).not.toBeCalled();
  });

  // 🟢 23 - uploadNewList
  it('Expect selected functions to be called with `uploadNewList`', async () => {
    // arrange
    const mockFile = new File([], 'test-file');
    mocks.useUploadNewListMutation.data = { ...UPLOAD_NEW_LIST_RES };
    mocks.useGetListLazyQuery.data = { ...GET_LIST_RES };

    // act
    const context = setup(mocks);
    await act(async () => {
      await context.uploadNewList(mockFile, '', '');
    });

    // assert
    const urlName = encodeURIComponent(mockList.name);
    expect(mocks.history.push).toBeCalledWith(`/lists?name=${urlName}`);
    expect(mocks.trackUploadList).toBeCalledWith({
      billTo: '',
      user: undefined,
      listName: ''
    });
  });

  // 🟢 24 - uploadNewList (alt)
  it('Expect selected functions to be called with different data from `uploadNewList`', async () => {
    // arrange
    const mockFile = new File([], 'test-file');
    mocks.auth = { authState: { isAuthenticated: false } };
    mocks.useUploadNewListMutation.data = { ...UPLOAD_NEW_LIST_RES };
    mocks.useGetListLazyQuery.data = undefined;

    // act
    const context = setup(mocks);
    await act(async () => {
      await context.uploadNewList(mockFile, '', '');
    });

    // assert
    expect(mocks.history.push).toBeCalledWith('/lists?name=');
    expect(mocks.trackUploadList).toBeCalledWith({
      billTo: '',
      user: null,
      listName: ''
    });
  });

  // 🟢 25 - uploadNewList (error)
  it('Expect truthy fileUploadError with from `uploadNewList` when res is error', async () => {
    // arrange
    const mockFile = new File([], 'test-file');
    mocks.auth = { authState: { isAuthenticated: false } };
    mocks.useUploadNewListMutation.data = { ...UPLOAD_NEW_LIST_RES };
    mocks.useUploadNewListMutation.error = {
      ...new ApolloError({}),
      message: '["test"]'
    };
    mocks.useGetListLazyQuery.data = undefined;

    // act
    const context = setup(mocks);
    await act(async () => {
      await context.uploadNewList(mockFile, '', '');
    });

    // assert
    expect(context.fileUploadError.length).toBeTruthy();
  });

  // 🟢 26 - uploadToList (empty)
  it('Expect selected functions not to be called when `uploadToList` GQL mutations is returning blank', () => {
    // arrange
    const mockFile = new File([], 'test-file');

    // act
    const context = setup(mocks);
    act(() => {
      context.uploadToList(mockFile, '');
    });

    // assert
    expect(mocks.history.push).not.toBeCalled();
    expect(mocks.trackUploadList).not.toBeCalled();
  });

  // 🟢 27 - uploadToList
  it('Expect selected functions to be called with `uploadToList`', async () => {
    // arrange
    const mockFile = new File([], 'test-file');
    mocks.useUploadToListMutation.data = { ...UPLOAD_TO_LIST_RES };
    mocks.useGetListLazyQuery.data = { ...GET_LIST_RES };

    // act
    const context = setup(mocks);
    await act(async () => {
      await context.uploadToList(mockFile, '');
    });

    // assert
    const urlName = encodeURIComponent(mockList.name);
    expect(mocks.history.push).toBeCalledWith(`/lists?name=${urlName}`);
    expect(mocks.trackUploadList).toBeCalledWith({
      billTo: '',
      user: undefined,
      listName: mockList.name
    });
  });

  // 🟢 28 - uploadToList (alt)
  it('Expect selected functions to be called with different data from `uploadToList`', async () => {
    // arrange
    const mockFile = new File([], 'test-file');
    mocks.auth = { authState: { isAuthenticated: false } };
    mocks.useUploadToListMutation.data = { ...UPLOAD_TO_LIST_RES };
    mocks.useGetListLazyQuery.data = undefined;

    // act
    const context = setup(mocks);
    await act(async () => {
      await context.uploadToList(mockFile, '');
    });

    // assert
    expect(mocks.history.push).toBeCalledWith('/lists?name=');
    expect(mocks.trackUploadList).toBeCalledWith({
      billTo: '',
      user: null,
      listName: undefined
    });
  });
  // 🟢 29 - initiateAddAllToList
  it('Expect updated context values with `initiateAddAllToList`', () => {
    mocks.selectedAccounts = { billTo: { id: billToAccountId } };
    mocks.useGetListsLazyQuery.data = { ...GET_LISTS_RES };
    const context = setup(mocks);
    act(() => {
      context.initiateAddAllToList('1234', []);
    });
    expect(context.open).toBeTruthy();
    expect(context.listMode).toBe(ListMode.ADD_ITEM);
  });

  // 🟢 30 - refreshLists
  it('Expect updated context values with Lists after calling `refreshLists`', () => {
    mocks.selectedAccounts = { billTo: { id: billToAccountId } };
    mocks.useGetListsLazyQuery.data = { ...GET_LISTS_RES };
    const context = setup(mocks);
    act(() => {
      context.refreshLists();
    });
    expect(context.lists.length).toBe(GET_LISTS_RES.lists.length);
  });
});
