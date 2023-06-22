import {
  ReactNode,
  createContext,
  useContext,
  useEffect,
  useMemo,
  useState,
  useCallback
} from 'react';

import { ApolloError } from '@apollo/client';
import { AlertColor, useSnackbar } from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { useHistory, useLocation } from 'react-router-dom';
import { noop, orderBy } from 'lodash-es';

import { AuthContext } from 'AuthProvider';
import {
  List,
  ListLineItem,
  ListUploadError,
  useAddAllCartItemsToNewListMutation,
  useAddAllCartItemsToExistingListsMutation,
  useCreateListMutation,
  useDeleteListMutation,
  useGetListLazyQuery,
  useToggleItemInListsMutation,
  useUpdateListMutation,
  useUploadNewListMutation,
  useUploadToListMutation,
  GetListsQuery,
  CreateListMutation,
  UpdateListMutation,
  DeleteListMutation,
  GetListQuery,
  useGetListsLazyQuery
} from 'generated/graphql';
import { useQueryParams } from 'hooks/useSearchParam';
import ListDrawer from 'Lists/ListDrawer';
import { BranchContext } from 'providers/BranchProvider';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import { removeListLineItemGap } from 'providers/utils/ListsProviderUtils';
import { ListDrawerStyled } from 'providers/utils/styled';
import { trackUploadList } from 'utils/analytics';
import decodeHTMLEntities from 'utils/decodeHTMLEntities';
import { parseError } from 'utils/errorhandling';

/**
 * CONSTS
 */
const validAddresses = [
  'cart',
  'lists',
  'order',
  'product',
  'previously-purchased-products',
  'search'
];

/**
 * Types
 */
type Props = {
  children: ReactNode;
};

type listLineItemInfo = {
  listId: string;
  listName: string;
  itemsSize: number;
};

type ListsParams = {
  name: string;
};

type ListAlert = [string, { variant?: AlertColor }];

export enum ListMode {
  CREATE,
  CHANGE,
  ADD_ITEM,
  UPLOAD
}

export type ListContextType = {
  addAlltoListId?: string;
  availableListIds: string[];
  createList: (
    name: string,
    includeProduct?: boolean,
    selectedListIds?: string[],
    setAvailableListIds?: (value: string[]) => void
  ) => Promise<void>;
  createListLoading: boolean;
  deleteList: (listId: string) => void;
  deleteListLoading: boolean;
  fileUploadError: String[];
  getListRefetchLoading: boolean;
  initiateAddAllToList: (cartId: string, availableInList: string[]) => void;
  initiateAddToList: (
    erpPartNumber: string,
    quantity: number,
    availableInList: string[]
  ) => void;
  isRenaming: boolean;
  isUpdating?: string;
  listDetailsLoading?: boolean;
  listMode: ListMode;
  listLineItemsInfo: listLineItemInfo[];
  lists: List[];
  listsLoading: boolean;
  newListName: string;
  open: boolean;
  refreshLists: () => void;
  resetListProvider: () => void;
  resetUploadState: () => void;
  selectedList?: List;
  selectedPartNumber?: string;
  selectedUploadList?: List;
  setAddAlltoListId: (cartId: string) => void;
  setAvailableListIds: (val: string[]) => void;
  setIsUpdating: (val?: string) => void;
  setItemQuantity?: (qty: number) => void;
  setListMode: (mode: ListMode) => void;
  setNewListName: (newListName: string) => void;
  setOpen: (val: boolean) => void;
  setFileUploadError: (val: string[]) => void;
  setRenaming: (val: boolean) => void;
  setSelectedList: (list?: List) => void;
  setSelectedUploadList: (list?: List) => void;
  setSelectedPartNumber: (erpPartNumber: string) => void;
  setShowAlert: (val?: [string, { variant?: AlertColor }]) => void;
  setShowUploadErrors: (val: boolean) => void;
  showAlert?: [string, { variant?: AlertColor }];
  showUploadErrors: boolean;
  toggleItemFromList?: (erpPartNumber: string, clear?: boolean) => void;
  toggleItemInLists?: (listIds: string[]) => void;
  updateList: (listName: string, listLineItems: ListLineItem[]) => void;
  updateListLoading: boolean;
  uploadErrors?: ListUploadError[];
  uploadListLoading: boolean;
  uploadNewList: (
    file: File,
    name: string,
    billToAccountId: string
  ) => Promise<void>;
  uploadToList: (file: File, listId: string) => Promise<void>;
  uploadType?: 'new' | 'existing';
};

/**
 * Context
 */
// istanbul ignore next
const promiseNoop = <T,>() => new Promise<T>(noop);
const defaultListContext: ListContextType = {
  addAlltoListId: '',
  availableListIds: [],
  createList: promiseNoop,
  createListLoading: false,
  deleteList: noop,
  deleteListLoading: false,
  fileUploadError: [],
  getListRefetchLoading: false,
  initiateAddAllToList: noop,
  initiateAddToList: noop,
  isRenaming: false,
  listMode: ListMode.CREATE,
  lists: [],
  listLineItemsInfo: [],
  listsLoading: false,
  newListName: '',
  open: false,
  refreshLists: noop,
  resetListProvider: noop,
  resetUploadState: noop,
  selectedPartNumber: '',
  setAddAlltoListId: noop,
  setAvailableListIds: noop,
  setFileUploadError: noop,
  setIsUpdating: noop,
  setListMode: noop,
  setNewListName: noop,
  setOpen: noop,
  setRenaming: noop,
  setSelectedList: noop,
  setSelectedUploadList: noop,
  setSelectedPartNumber: noop,
  setShowAlert: noop,
  setShowUploadErrors: noop,
  showUploadErrors: false,
  updateList: noop,
  updateListLoading: false,
  uploadListLoading: false,
  uploadNewList: promiseNoop,
  uploadToList: promiseNoop
};
export const ListContext = createContext<ListContextType>(defaultListContext);
//
/**
 * Component
 */
function ListsProvider(props: Props) {
  /**
   * Custom hooks
   */
  const { pushAlert } = useSnackbar();
  const { t } = useTranslation();
  const history = useHistory();
  const { pathname } = useLocation();
  const [queryParams, setQueryParams] = useQueryParams<ListsParams>(
    { arrayKeys: [] },
    true
  );

  /**
   * Context
   */
  const { authState, user, profile } = useContext(AuthContext);
  const { shippingBranch, shippingBranchLoading } = useContext(BranchContext);
  const { selectedAccounts } = useSelectedAccountsContext();
  const billToAccountId = selectedAccounts.billTo?.id ?? '';
  const shipToAccountId = selectedAccounts.shipTo?.id ?? '';

  /**
   * State
   */
  const [addAlltoListId, setAddAlltoListId] = useState<string>('');
  const [availableListIds, setAvailableListIds] = useState<string[]>([]);
  const [fileUploadError, setFileUploadError] = useState<string[]>([]);
  const [getListRefetchLoading, setGetListRefetchLoading] = useState(false);
  const [isRenaming, setRenaming] = useState(false);
  const [isUpdating, setIsUpdating] = useState<string>();
  const [itemQuantity, setItemQuantity] = useState<number>();
  const [listMode, setListMode] = useState(defaultListContext.listMode);
  const [lists, setLists] = useState<List[]>([]);
  const [open, setOpen] = useState(false);
  const [selectedList, setSelectedList] = useState<List>();
  const [selectedPartNumber, setSelectedPartNumber] = useState<string>();
  const [selectedUploadList, setSelectedUploadList] = useState<List>();
  const [showAlert, setShowAlert] = useState<ListAlert>();
  const [showUploadErrors, setShowUploadErrors] = useState(false);
  const [affectsSelectedList, setAffectsSelectedList] = useState(false);
  const [uploadErrors, setUploadErrors] = useState<ListUploadError[]>();
  const [uploadType, setUploadType] = useState<'new' | 'existing'>();
  const [newListName, setNewListName] = useState<string>('');

  /**
   * Data
   */
  // 🟣 LISTS (ALL)
  const [getLists, { loading: listsLoading }] = useGetListsLazyQuery({
    fetchPolicy: 'no-cache',
    onCompleted: getListsCompleted
  });
  // 🟣 LIST (SELECTED)
  const [getList, { loading: listLoading }] = useGetListLazyQuery({
    fetchPolicy: 'no-cache',
    onCompleted: getListCompleted
  });
  // 🟣 CREATE LIST
  const [createListMutation, { loading: createListLoading }] =
    useCreateListMutation({ onCompleted: createListCompleted });
  // 🟣 UPDATE LIST
  const [updateListMutation, { loading: updateListLoading }] =
    useUpdateListMutation({ onCompleted: updateListCompleted });
  // 🟣 DELETE LIST
  const [deleteListMutation, { loading: deleteListLoading }] =
    useDeleteListMutation({ onCompleted: deleteListCompleted });
  // 🟣 UPLOAD NEW LIST
  const [
    uploadNewListMutation,
    { loading: uploadNewListLoading, reset: uploadNewListReset }
  ] = useUploadNewListMutation({
    refetchQueries: ['GetLists'],
    awaitRefetchQueries: true,
    onError: handleUploadError
  });
  // 🟣 UPLOAD TO EXISTING LIST
  const [
    uploadToListMutation,
    { loading: uploadToListLoading, reset: uploadToListReset }
  ] = useUploadToListMutation({
    refetchQueries: ['GetLists'],
    awaitRefetchQueries: true,
    onError: handleUploadError
  });
  // 🟣 TOGGLE ITEM IN LIST
  const [toggleItem] = useToggleItemInListsMutation({
    onCompleted: toggleItemsInListsCompleted
  });
  // 🟣 ADD CART TO LIST
  const [addCartToNewListMutation] = useAddAllCartItemsToNewListMutation({
    onCompleted: closeDrawer
  });
  // 🟣 ADD CART TO EXISTING LISTS
  const [addAllCartItemsToExistingLists] =
    useAddAllCartItemsToExistingListsMutation({
      onCompleted: toggleItemsInListsCompleted
    });

  /**
   * Memos
   */
  const listLineItemsInfo = useMemo(listLineItemsInfoMemo, [lists]);
  const isOnValidAddress = useMemo(
    () => validAddresses.includes(pathname.split('/')[1]),
    [pathname]
  );
  const listDetailsLoading =
    listLoading ||
    shippingBranchLoading ||
    (!selectedList?.id && Boolean(lists.length)) ||
    !shippingBranch?.branchId;
  const uploadListLoading =
    listLoading ||
    getListRefetchLoading ||
    uploadNewListLoading ||
    uploadToListLoading;

  /**
   * Effects
   */
  // eslint-disable-next-line react-hooks/exhaustive-deps
  useEffect(initListsEffect, [billToAccountId, isOnValidAddress, pathname]);
  // eslint-disable-next-line react-hooks/exhaustive-deps
  useEffect(syncQueryParamsEffect, [pathname, selectedList]);
  // eslint-disable-next-line react-hooks/exhaustive-deps
  useEffect(handleListDataChangedEffect, [
    queryParams,
    selectedList,
    lists.length
  ]);

  /**
   * Callbacks
   */
  // 🟤 CB - toggleItemFromList
  // eslint-disable-next-line react-hooks/exhaustive-deps
  const toggleItemFromList = useCallback(toggleItemFromListCb, [
    availableListIds,
    selectedList
  ]);

  // 🟤 CB - toggleItemInLists
  const toggleItemInLists = (listIds: string[]) => {
    if (Boolean(addAlltoListId)) {
      if (selectedList && listIds.includes(selectedList.id)) {
        setAffectsSelectedList(true);
      }
      const variables = { listIds, cartId: addAlltoListId };
      addAllCartItemsToExistingLists({ variables });
      return;
    }
    // Exit when there's nothing to toggle
    if (!selectedPartNumber) {
      return;
    }

    // Use to re-call `getList` if `listIds` contains if from `selectedList`
    if (selectedList && listIds.includes(selectedList.id)) {
      setAffectsSelectedList(true);
    }

    // Prepare and make request
    setIsUpdating(selectedPartNumber);
    const itemToToggle = {
      erpPartNumber: selectedPartNumber,
      quantity: itemQuantity
    };
    const variables = {
      toggleItemInListsInput: { listIds, itemToToggle, billToAccountId }
    };
    toggleItem({ variables });
  };

  // 🟤 CB - initiateAddAllToList
  const initiateAddAllToList = (cartId: string, availableInList: string[]) => {
    refreshLists();
    setOpen(true);
    setSelectedPartNumber(undefined);
    setAddAlltoListId(cartId);
    setListMode(ListMode.ADD_ITEM);
    setAvailableListIds(availableInList);
  };

  // 🟤 CB - initiateAddToList
  const initiateAddToList = (
    erpPartNumber: string,
    quantity: number,
    availableInList: string[]
  ) => {
    refreshLists();
    setOpen(true);
    setListMode(ListMode.ADD_ITEM);
    setAddAlltoListId('');
    setSelectedPartNumber(erpPartNumber);
    setItemQuantity(quantity);
    setAvailableListIds(availableInList);
  };

  // 🟤 CB - resetUploadState
  const resetUploadState = () => {
    setShowUploadErrors(false);
    setUploadErrors(undefined);
    setUploadType(undefined);
    setFileUploadError([]);
  };

  // 🟤 CB - setSelectedListProvider
  const setSelectedListProvider = (list?: List) => {
    setSelectedList(list);
    list?.id && getListById(list.id);
  };

  // 🟤 CB - refreshLists
  const refreshLists = () => {
    if (billToAccountId) {
      getLists({ variables: { billToAccountId } });
    }
  };

  // 🟤 CB - resetListProvider
  const resetListProvider = () => {
    setLists([]);
    setSelectedList(undefined);
    setAvailableListIds([]);
    resetUploadState();
    setGetListRefetchLoading(false);
    setRenaming(false);
    setIsUpdating(undefined);
    setItemQuantity(undefined);
    setListMode(defaultListContext.listMode);
    closeDrawer();
    setSelectedPartNumber(undefined);
    setShowAlert(undefined);
    setAffectsSelectedList(false);
    setNewListName('');
    setAddAlltoListId('');
  };

  /**
   * Render
   */
  return (
    <ListContext.Provider
      value={{
        addAlltoListId,
        availableListIds,
        createList,
        createListLoading,
        deleteList,
        deleteListLoading,
        fileUploadError,
        getListRefetchLoading,
        initiateAddAllToList,
        initiateAddToList,
        isRenaming,
        isUpdating,
        listDetailsLoading,
        listMode,
        listLineItemsInfo,
        lists,
        listsLoading,
        newListName,
        open,
        refreshLists,
        resetListProvider,
        resetUploadState,
        selectedList,
        selectedPartNumber,
        selectedUploadList,
        setAddAlltoListId,
        setAvailableListIds,
        setFileUploadError,
        setItemQuantity,
        setIsUpdating,
        setListMode,
        setNewListName,
        setOpen,
        setRenaming,
        setSelectedList: setSelectedListProvider,
        setSelectedUploadList,
        setSelectedPartNumber,
        setShowAlert,
        setShowUploadErrors,
        showAlert,
        showUploadErrors,
        toggleItemFromList,
        toggleItemInLists,
        updateList,
        updateListLoading,
        uploadErrors,
        uploadListLoading,
        uploadNewList,
        uploadToList,
        uploadType
      }}
    >
      <ListDrawerStyled open={open} onClose={closeDrawer} anchor="right">
        <ListDrawer />
      </ListDrawerStyled>
      {props.children}
    </ListContext.Provider>
  );

  /**
   * GQL Mutations Callbacks
   */
  // 🟠 - createList
  async function createList(
    name: string,
    includeProduct?: boolean,
    selectedListIds?: string[],
    setAvailableListIds?: (value: string[]) => void
  ) {
    if (Boolean(addAlltoListId)) {
      const variables = {
        name,
        accountId: billToAccountId,
        cartId: addAlltoListId
      };

      // Add cart to new list
      const result = await addCartToNewListMutation({ variables });

      // Update lists
      const createdListId = result.data?.addAllCartItemsToNewList;
      const cartListIds = selectedListIds ?? [];
      cartListIds.length && toggleItemInLists(cartListIds);
      createdListId && setAvailableListIds?.([...cartListIds, createdListId]);

      const message = t('lists.createNewSuccess', { name: name });
      pushAlert(decodeHTMLEntities(message));
      const newListHelper = {
        id: createdListId!,
        name: name,
        billToAccountId,
        listLineItems: []
      };
      const newLists = [...lists, newListHelper];
      const newOrderedList = orderBy(newLists, ({ name }) =>
        name.toLowerCase()
      );
      setLists(newOrderedList);
      setSelectedList(newListHelper);
      return;
    }
    // Init variables
    const newItem = {
      erpPartNumber: selectedPartNumber ?? '',
      quantity: itemQuantity,
      sortOrder: 0
    };
    const listLineItems = includeProduct ? [newItem] : undefined;
    const variables = {
      createListInput: { name, billToAccountId, listLineItems }
    };

    // Create list
    const result = await createListMutation({ variables });

    // Update lists
    const createdListId = result.data?.createList.id;
    const selectedIds = selectedListIds ?? [];
    const listIds = [...selectedIds, createdListId!];
    toggleItemInLists(listIds);
    setAvailableListIds?.(listIds);
  }

  // 🟠 - updateList
  function updateList(listName: string, listLineItems: ListLineItem[]) {
    // Exit if `selectedList` is undefined
    if (!selectedList) {
      return;
    }

    // Update the selected list with the new data
    const newList = { ...selectedList, name: listName, listLineItems };
    const adjustedList = removeListLineItemGap(newList);
    setSelectedList(adjustedList);

    // Prepare the variables
    const trimmedLineItemData = listLineItems.map(
      ({ id, listId, erpPartNumber, sortOrder, quantity }) => ({
        id,
        listId,
        erpPartNumber: erpPartNumber ?? '',
        sortOrder,
        quantity
      })
    );
    const variables = {
      updateListInput: {
        id: selectedList.id,
        name: listName,
        billToAccountId,
        listLineItems: trimmedLineItemData
      }
    };

    // Call to update list
    return updateListMutation({ variables });
  }

  // 🟠 - deleteList
  function deleteList(listId: string) {
    return deleteListMutation({ variables: { listId } });
  }

  // 🟠 - uploadNewList
  async function uploadNewList(
    file: File,
    name: string,
    billToAccountId: string
  ) {
    // Start upload list
    const uploadRes = await uploadNewListMutation({
      variables: { file, name, billToAccountId }
    });
    const uploadedNewList = uploadRes.data?.uploadNewList;

    // Exit when there's no data
    if (!uploadedNewList) {
      return;
    }

    // Init new list procedure
    setGetListRefetchLoading(true);

    // Grab the uploaded list data
    const { data } = await getListById(uploadedNewList.listId);
    const successMessage = t('lists.createNewSuccess', { name });
    const urlName = encodeURIComponent(data?.list.name ?? '');

    // Apply new list as selected list
    setGetListRefetchLoading(false);
    setNewListName('');
    setSelectedList(data?.list);
    setShowUploadErrors(Boolean(uploadedNewList.errors.length));
    setUploadErrors(uploadedNewList.errors);
    setUploadType('new');
    setShowAlert([decodeHTMLEntities(successMessage), { variant: 'success' }]);
    uploadNewListReset();
    trackUploadList({
      billTo: billToAccountId,
      user: authState?.isAuthenticated ? user?.email : null,
      listName: name
    });
    history.push(`/lists?name=${urlName}`);
  }

  // 🟠 - uploadToList
  async function uploadToList(file: File, listId: string) {
    // Start upload list
    const uploadRes = await uploadToListMutation({
      variables: { file, listId }
    });
    const uploadedList = uploadRes.data?.uploadToList;

    // Exit when there's no data
    if (!uploadedList) {
      return;
    }

    // Init new list procedure
    setGetListRefetchLoading(true);

    // Grab the uploaded list data
    const { data } = await getListById(uploadedList.listId);
    const successMessage = t('lists.uploadSuccess', {
      count: uploadedList.successfulRowCount
    });

    // Apply new list as selected list
    setGetListRefetchLoading(false);
    setSelectedList(data?.list);
    setShowUploadErrors(Boolean(uploadedList.errors.length));
    setUploadErrors(uploadedList.errors);
    setUploadType('existing');
    setShowAlert([successMessage, { variant: 'success' }]);
    uploadToListReset();
    trackUploadList({
      billTo: billToAccountId,
      user: authState?.isAuthenticated ? user?.email : null,
      listName: data?.list.name
    });
    history.push(`/lists?name=${encodeURIComponent(data?.list.name ?? '')}`);
  }

  /**
   * GQL Complete Defs
   */
  // 🟢 - get lists
  function getListsCompleted(res: GetListsQuery) {
    // Prepare the new list, exit if it's empty
    const newList = res.lists as List[];
    if (!newList.length) {
      return;
    }
    const newOrderedList = orderBy(newList, ({ name }) => name.toLowerCase());

    // update the `lists` state
    setLists(newOrderedList);

    // Find the listID from lists by using the query param name
    const foundListId =
      newOrderedList.find(({ name }) => name === queryParams.name)?.id ??
      newOrderedList[0]?.id;

    // Call `getList` to populate the current list's data
    getListById(selectedList?.id ?? foundListId);
  }

  // 🟢 - get selected list
  function getListCompleted(res?: GetListQuery) {
    if (!res) {
      return;
    }
    // Extract and prepare list data
    const newSelectedList = res.list as List;
    const adjustedSelectedList = removeListLineItemGap(newSelectedList);

    // Check if the list is the same, update the selected list if it's not the same
    const sameList =
      selectedList &&
      // istanbul ignore next
      adjustedSelectedList.id === selectedList.id;
    const emptyList = !selectedList?.listLineItems.length;
    const differentSize =
      selectedList?.listLineItems.length !==
      adjustedSelectedList.listLineItems.length;
    // istanbul ignore next (test coverage of no value)
    if (!sameList || emptyList || differentSize) {
      setSelectedList(adjustedSelectedList);
    }

    // Reset
    setIsUpdating(undefined);
  }

  // 🟢 - create list
  function createListCompleted({ createList }: CreateListMutation) {
    // Toast message
    const message = t('lists.createNewSuccess', { name: createList.name });
    pushAlert(decodeHTMLEntities(message));

    // Update lists
    const newListHelper = { ...createList, listLineItems: [] };
    const newLists = [...lists, newListHelper];
    const newOrderedList = orderBy(newLists, ({ name }) => name.toLowerCase());
    setLists(newOrderedList);

    // Update selectedList (listLineItems has to be empty)
    setSelectedList(newListHelper);

    // Reset
    closeDrawer();
  }

  // 🟢 - update list
  function updateListCompleted({ updateList }: UpdateListMutation) {
    // Reset
    setIsUpdating(undefined);
    setRenaming(false);

    // Don't update if it's not renaming because the original updateList function already handled that
    if (!isRenaming || !selectedList) {
      return;
    }

    // Rename `selectList`
    const renamedSelectedList = { ...selectedList, name: updateList.name };
    setSelectedList(renamedSelectedList);

    // Rename `lists`
    const listIndex = lists.findIndex(({ name }) => name === selectedList.name);
    if (listIndex === -1) {
      // Quit if the affect list isn't found
      return;
    }
    const renameLists = [...lists];
    renameLists[listIndex] = { ...renamedSelectedList, listLineItems: [] };
    const sortedList = orderBy(renameLists, ({ name }) => name.toLowerCase());
    setLists(sortedList);

    // Toast Message
    pushAlert(t('lists.renameListSuccess'));
  }

  // 🟢 - delete list
  function deleteListCompleted({ deleteList }: DeleteListMutation) {
    // Toast Message
    const message = t('lists.listDeleted', { name: selectedList?.name });
    pushAlert(decodeHTMLEntities(message), { variant: 'info' });

    // Reset
    resetUploadState();

    // Update lists by filter out the deleted list
    const newLists = lists.filter(({ id }) => id !== deleteList?.id);
    setLists(newLists);

    // Update the selected list
    setSelectedList(newLists[0]);
    getListById(newLists[0]?.id);
  }

  // 🟢 - toggle item
  function toggleItemsInListsCompleted() {
    // Reset
    setIsUpdating(undefined);

    // When toggling affected list...
    // istanbul ignore next (impossible to cover due to React's nature of setState)
    if (selectedList && affectsSelectedList) {
      // ...Clear `listLineItems` so after toggle...
      setSelectedList({ ...selectedList, listLineItems: [] });
      // ...It will call `getList` to re-populate
      getListById(selectedList.id);
      // Reset the affected list helper state
      setAffectsSelectedList(false);
    }
  }

  /**
   * GQL Error defs
   */
  // 🔴 - upload error
  function handleUploadError(e: ApolloError) {
    return setFileUploadError(parseError(e));
  }

  /**
   * Memo Defs
   */
  // 🔵 - listLineItemInfo
  function listLineItemsInfoMemo() {
    return lists.map((list) => ({
      listId: list.id,
      listName: list.name,
      itemsSize: list.listLineItemsSize ?? 0
    }));
  }

  /**
   * Effect Defs
   */
  // 🟡 - init lists
  function initListsEffect() {
    if (isOnValidAddress && billToAccountId) {
      // istanbul ignore next (impossible to cover since even with setState actions)
      if (!lists.length) {
        getLists({ variables: { billToAccountId } });
      }
      // istanbul ignore next (impossible to cover since even with setState actions)
      if (!listLoading && selectedList) {
        getListById(selectedList.id);
      }
    }
  }

  // 🟡 - query param
  function syncQueryParamsEffect() {
    if (
      selectedList?.name &&
      pathname === '/lists' &&
      queryParams.name !== selectedList.name
    ) {
      setQueryParams({ name: selectedList.name });
    }
  }

  // 🟡 - list data change
  function handleListDataChangedEffect() {
    if (!selectedList && lists.length) {
      const matchingList = lists.find(({ name }) => name === queryParams.name);
      setSelectedList(matchingList ?? lists[0]);
    }
  }

  /**
   * Callback Def
   */
  // 🟤 - Callback
  function toggleItemFromListCb(erpPartNumber: string, clearList?: boolean) {
    // Exit when there's no part/selectedList to update
    if (!erpPartNumber || !selectedList) {
      return;
    }

    // Clear `listLineItems` so after toggle. It will call `getList` to repopulate
    if (clearList) {
      setSelectedList({ ...selectedList, listLineItems: [] });
    }

    // Init update
    setIsUpdating(erpPartNumber);
    const variables = {
      toggleItemInListsInput: {
        listIds: [selectedList.id],
        itemToToggle: { erpPartNumber }
      }
    };

    // Make call
    setAffectsSelectedList(false);
    toggleItem({ variables });
  }

  /**
   * Util
   */
  // 🔣 util - getListById
  function getListById(id: string) {
    const variables = {
      listId: id,
      userId: profile?.userId ?? '',
      shipToAccountId,
      branchId: shippingBranch?.branchId ?? '',
      erpAccountId: selectedAccounts.billToErpAccount?.erpAccountId
    };
    return getList({ variables });
  }
  // 🔣 util - close drawer
  function closeDrawer() {
    setAddAlltoListId('');
    setOpen(false);
  }
}

export const useListsContext = () => useContext(ListContext);

export default ListsProvider;
