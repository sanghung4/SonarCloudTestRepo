import { FormEvent, useMemo, useState, useCallback, useEffect } from 'react';

import {
  Box,
  Button,
  Card,
  IconButton,
  TextField,
  Collapse,
  Typography
} from '@dialexa/reece-component-library';
import { xor, kebabCase } from 'lodash-es';
import { useTranslation } from 'react-i18next';

import VirtualizedList from 'common/VirtualizedList';
import { List as ListType } from 'generated/graphql';
import { DeleteIcon, WarningIcon } from 'icons';
import ListDrawerItem from 'Lists/ListDrawerItem';
import {
  ListDrawerContainer,
  ListDrawerStickyBox,
  ListHardLimitAlert
} from 'Lists/util/styled';
import { useListsContext, ListMode } from 'providers/ListsProvider';
import { useCartContext } from 'providers/CartProvider';

/**
 * Config
 */
const MAX_LIST_ITEMS = 600;

/**
 * Component
 */
function ChangeList() {
  /**
   * Custom hooks
   */
  const { t } = useTranslation();

  /**
   * Context
   */
  const {
    addAlltoListId,
    availableListIds,
    setAvailableListIds,
    createList,
    listMode,
    listLineItemsInfo,
    lists,
    resetUploadState,
    selectedList,
    setAddAlltoListId,
    setOpen,
    setRenaming,
    setSelectedList,
    setSelectedUploadList,
    toggleItemInLists
  } = useListsContext();
  const { itemCount, refreshCart } = useCartContext();

  /**
   * State
   */
  const defaultSelectedIds =
    listMode === ListMode.ADD_ITEM
      ? availableListIds
      : selectedList?.id
      ? [selectedList?.id]
      : [];
  const [listSearch, setListSearch] = useState('');
  const [listToSelect, setListToSelect] = useState<ListType>();
  const [selectedListIds, setSelectedListIds] = useState(defaultSelectedIds);
  const [changedListIds, setChangedListIds] = useState<string[]>([]);
  const [listName, setListName] = useState('');
  const [alreadySelectedIds, setAlreadySelectedIds] = useState<string[]>([]);
  const [addToListError, setAddToListError] = useState<string[]>([]);

  /**
   * Memos
   */
  const drawerTitle = useMemo(drawerTitleMemo, [listMode, t]);
  const filteredLists = useMemo(filteredListMemo, [lists, listSearch]);
  const nameAlreadyExists = useMemo(nameAlreadyExistsMemo, [lists, listName]);
  const disableButton = useMemo(disableAddToListButton, [
    selectedListIds,
    listLineItemsInfo,
    addAlltoListId,
    itemCount
  ]);
  const listNames = useMemo(filterListNamesErrorMessage, [
    selectedListIds,
    listLineItemsInfo,
    addAlltoListId,
    itemCount
  ]);
  /**
   * Effects
   */
  useEffect(initAlreadySelectedIdsEffect, [
    alreadySelectedIds.length,
    listLineItemsInfo.length,
    listMode,
    selectedListIds
  ]);

  /**
   * Callbacks
   */
  const handleItemSelect = useCallback(handleItemSelectCb, [
    addToListError,
    alreadySelectedIds,
    changedListIds,
    listLineItemsInfo,
    listMode,
    selectedListIds
  ]);
  const handleSubmit = useCallback(handleSubmitCb, [
    addAlltoListId,
    createList,
    listMode,
    listName,
    listToSelect,
    nameAlreadyExists,
    refreshCart,
    resetUploadState,
    selectedList?.id,
    selectedListIds,
    setAddAlltoListId,
    setAvailableListIds,
    setOpen,
    setRenaming,
    setSelectedList,
    setSelectedUploadList,
    toggleItemInLists
  ]);
  /**
   * Render
   */
  return (
    <ListDrawerContainer>
      {/* Title */}
      <Card square sx={{ p: 2, overflow: 'visible' }}>
        <Box
          display="flex"
          justifyContent="space-between"
          alignItems="center"
          py={1}
          pl={1}
        >
          <Typography
            color="primary"
            variant="h4"
            data-testid={kebabCase(`${drawerTitle}-title`)}
          >
            {drawerTitle}
          </Typography>
          <IconButton
            disableRipple
            onClick={handleCloseDrawer}
            size="large"
            data-testid="list-drawer-close-button"
          >
            <DeleteIcon color="primary" />
          </IconButton>
        </Box>
      </Card>
      <ListDrawerStickyBox>
        <Collapse in={disableButton && listMode === ListMode.ADD_ITEM}>
          <ListHardLimitAlert
            icon={<Box component={WarningIcon} mr={2} />}
            data-testid="add-to-list-hard-limit-alert"
            action={
              <IconButton
                sx={{ padding: 0, mr: 1.5 }}
                color="inherit"
                size="small"
                data-testid="hard-limit-close-alert"
              ></IconButton>
            }
          >
            <Typography
              variant="subtitle2"
              display="inline"
              fontWeight="bold"
              data-testid="error-title"
            >
              {`${t('common.errorTitle')}: `}
            </Typography>
            {`${t('lists.listsHardLimitError')}`}
            <Typography variant="subtitle2" display="inline" fontWeight="bold">
              {listNames.join(', ')}
            </Typography>
            {`${t('lists.listsHardLimitErrorDesc')}`}
          </ListHardLimitAlert>
        </Collapse>
      </ListDrawerStickyBox>
      <Box
        component="form"
        onSubmit={handleSubmit}
        noValidate
        display="flex"
        flexDirection="column"
        flexGrow={1}
      >
        <Box display="flex" flexDirection="column" flexGrow={1} px={3} py={2}>
          {/* Text Inputs */}
          <Box mt={3} mb={5}>
            {listMode === ListMode.CHANGE || listMode === ListMode.UPLOAD ? (
              <TextField
                label={t('lists.searchList')}
                inputProps={{ 'data-testid': 'search-for-lists-input' }}
                placeholder={t('lists.enterListName')}
                fullWidth
                value={listSearch}
                onChange={(e) => setListSearch(e.target.value)}
                autoFocus
              />
            ) : (
              <TextField
                label={t('lists.createNew')}
                inputProps={{ 'data-testid': 'new-list-name-input' }}
                placeholder={t('lists.typeNewListName')}
                fullWidth
                value={listName}
                onChange={(e) => setListName(e.target.value)}
                autoFocus
                error={!!listName && nameAlreadyExists}
                helperText={
                  !!listName && nameAlreadyExists
                    ? t('lists.listExistsWarning')
                    : null
                }
              />
            )}
          </Box>
          {/* List of user's lists */}
          {listMode !== ListMode.CREATE && !!lists.length && (
            <>
              {/* Labels */}
              <Box display="flex" justifyContent="space-between">
                <Typography color="primary" data-testid="select-lists-label">
                  {t('lists.selectLists')}
                </Typography>
                <Typography data-testid="lists-count">
                  {lists.length} {t('common.list', { count: lists.length })}
                </Typography>
              </Box>
              {/* List */}
              <Box
                border={1}
                borderColor="lightGray.main"
                marginTop={1}
                marginBottom={4}
                maxHeight={400}
                boxSizing="content-box"
                data-testid="list-drawer-container"
              >
                {/* Virtualized list is used here so only visible items are rendered */}
                <VirtualizedList
                  defaultItemSize={42}
                  maxHeight={400}
                  dataArray={filteredLists}
                  renderItem={(listData) => (
                    <ListDrawerItem
                      list={listData}
                      listMode={listMode}
                      onClick={() => handleItemSelect(listData)}
                      selected={
                        listData.id === listToSelect?.id ||
                        selectedListIds.includes(listData.id)
                      }
                      key={listData.name}
                    />
                  )}
                />
              </Box>
            </>
          )}
        </Box>
        {/* Drawer Actions */}
        <Box
          component={Card}
          square
          p={2}
          display="flex"
          justifyContent="center"
          alignItems="center"
        >
          <Button
            color="primaryLight"
            data-testid="cancel-button"
            variant="inline"
            onClick={handleCloseDrawer}
            sx={{ mr: 4 }}
          >
            {t('common.cancel')}
          </Button>
          <Button
            data-testid={`${drawerTitle.split(' ').join('-')}-submit-button`}
            type="submit"
            disabled={
              (listMode === ListMode.CHANGE && !listToSelect) ||
              (!!listName && nameAlreadyExists) ||
              (listMode !== ListMode.CHANGE &&
                !changedListIds.length &&
                !listName.trim().length &&
                listMode !== ListMode.UPLOAD) ||
              !!addToListError.length ||
              (disableButton && listMode === ListMode.ADD_ITEM)
            }
          >
            {drawerTitle}
          </Button>
        </Box>
      </Box>
    </ListDrawerContainer>
  );

  /**
   * Memo defs
   */
  function drawerTitleMemo() {
    switch (listMode) {
      case ListMode.CHANGE:
        return t('lists.changeList');
      case ListMode.CREATE:
        return t('lists.createList');
      case ListMode.UPLOAD:
        return t('lists.selectList');
      default:
        return t('lists.addToList');
    }
  }

  function filteredListMemo() {
    const term = listSearch.toLowerCase();
    return lists.filter(({ name }) => name.toLowerCase().includes(term));
  }
  function nameAlreadyExistsMemo() {
    return lists.some((l) => l.name === listName);
  }
  function disableAddToListButton() {
    return selectedListIds
      ? listLineItemsInfo
          .filter((item) => selectedListIds.includes(item.listId))
          .some((item) =>
            Boolean(addAlltoListId)
              ? item.itemsSize + itemCount > MAX_LIST_ITEMS
              : item.itemsSize >= MAX_LIST_ITEMS
          )
      : false;
  }

  function filterListNamesErrorMessage() {
    const filteredList = listLineItemsInfo.filter(
      (item) =>
        selectedListIds.includes(item.listId) &&
        (Boolean(addAlltoListId)
          ? item.itemsSize + itemCount > MAX_LIST_ITEMS
          : item.itemsSize >= MAX_LIST_ITEMS)
    );
    return filteredList.map((item) => item.listName);
  }
  /**
   * Effect Def
   */
  function initAlreadySelectedIdsEffect() {
    const rightModes =
      listMode === ListMode.CREATE || listMode === ListMode.ADD_ITEM;
    const readyToSet = listLineItemsInfo.length && !alreadySelectedIds.length;
    if (readyToSet && rightModes) {
      setAlreadySelectedIds(selectedListIds);
    }
  }

  /**
   * Callback defs
   */
  function handleItemSelectCb(list: ListType) {
    if (listMode === ListMode.ADD_ITEM && listLineItemsInfo) {
      // Get number of items in the input list
      const itemCountInList =
        listLineItemsInfo.find((item) => item.listName === list.name)
          ?.itemsSize ?? 0;
      // Is the input list already in one of the selected list
      const isAlreadyInList = alreadySelectedIds.some((id) => id === list.id);
      // Is this a toggle off?
      const isTurningOff = selectedListIds.some((id) => id === list.id);

      // Update list-full error
      if (itemCountInList >= MAX_LIST_ITEMS && !isAlreadyInList) {
        if (!isTurningOff) {
          // Add to the list of errors while toggling on
          setAddToListError([...addToListError, list.name]);
        }
      }
      // Remove it if it exists while toggling off
      if (isTurningOff) {
        const newListError = addToListError.filter((err) => err !== list.name);
        setAddToListError(newListError);
      }
    }

    // Update
    switch (listMode) {
      case ListMode.CHANGE:
      case ListMode.UPLOAD:
        setSelectedListIds([list.id]);
        setListToSelect(list);
        break;
      default:
        setChangedListIds(xor(changedListIds, [list.id]));
        setSelectedListIds(xor(selectedListIds, [list.id]));
        break;
    }
  }

  function handleSubmitCb(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();

    // Submit - Change List
    if (listMode === ListMode.CHANGE) {
      if (listToSelect?.id !== selectedList?.id) {
        setSelectedList(listToSelect);
      }
      resetUploadState();
      setRenaming(false);
      setOpen(false);
      return;
    }

    // Submit - Create List
    if (listMode === ListMode.CREATE && listName && !nameAlreadyExists) {
      resetUploadState();
      createList(listName, false);
      setListName('');
      setOpen(false);
      return;
    }

    // Submit - Upload List
    if (listMode === ListMode.UPLOAD && listToSelect) {
      setSelectedUploadList(listToSelect);
      setOpen(false);
      return;
    }

    // Submit - Toggle List
    if (listMode === ListMode.ADD_ITEM) {
      if (listName) {
        createList(listName, true, selectedListIds, setAvailableListIds);
      } else {
        toggleItemInLists && toggleItemInLists(selectedListIds);
        setAvailableListIds(selectedListIds);
      }
      setOpen(false);
      Boolean(addAlltoListId) && refreshCart?.();
      setAddAlltoListId('');
      return;
    }
  }
  function handleCloseDrawer() {
    setAddAlltoListId('');
    setOpen(false);
  }
}

export default ChangeList;
