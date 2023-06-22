import React, {
  ChangeEvent,
  useEffect,
  useContext,
  useMemo,
  useRef,
  useState
} from 'react';

import {
  Alert,
  Box,
  Button,
  Collapse,
  Container,
  Divider,
  Grid,
  IconButton,
  Menu,
  MenuItem,
  Paper,
  Skeleton,
  TextField,
  Typography,
  useScreenSize,
  useSnackbar
} from '@dialexa/reece-component-library';
import { AlertTitle } from '@mui/material';
import { DragDropContext, Droppable, DropResult } from 'react-beautiful-dnd';
import { useTranslation } from 'react-i18next';
import { useHistory } from 'react-router-dom';

import Breadcrumbs from 'common/Breadcrumbs';
import Loader from 'common/Loader';
import { ListLineItem, useGetProductPricingQuery } from 'generated/graphql';
import {
  AddIcon,
  CloseIcon,
  ArrowDropDownIcon,
  CartIcon,
  FileUploadIcon,
  WarningIcon
} from 'icons';
import { ListContext, ListMode } from 'providers/ListsProvider';
import { BranchContext } from 'providers/BranchProvider';
import ListsLineItem from 'Lists/ListsLineItem';
import ListDialog from 'Lists/ListDialog';
import FeedbackButton from 'common/FeedbackButton';
import { FixedSizeList as List } from 'react-window';
import ListRow from './ListRow';
import useWindowDimensions from 'hooks/useWindowDimensions';
import useDocumentTitle from 'hooks/useDocumentTitle';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import { MAX_CART_ITEMS, useCartContext } from 'providers/CartProvider';
import ListActions from './ListActions';
import ListRenameDialog from './ListRenameDialog';
import {
  AddToCartButtonContainer,
  CartButtonContainer,
  CreateListButton,
  ListInfoCard,
  ListItemsInfoCard,
  ListsHeaderTypography,
  MyListsHeaderContainer
} from 'Lists/util/styled';
import AdvancedToolTip from 'common/AdvancedToolTip';

function Lists() {
  /**
   * Custom hooks
   */
  const history = useHistory();
  const { isSmallScreen } = useScreenSize();
  const { pushAlert } = useSnackbar();
  const { t } = useTranslation();
  const { height } = useWindowDimensions();
  useDocumentTitle(t('common.lists'));

  /**
   * Context
   */
  const {
    availableListIds,
    deleteList,
    isUpdating,
    listDetailsLoading,
    lists,
    listsLoading,
    selectedList,
    setAvailableListIds,
    setOpen,
    setIsUpdating,
    setListMode,
    setSelectedList,
    setShowAlert,
    setShowUploadErrors,
    showAlert,
    showUploadErrors,
    updateList,
    uploadErrors,
    uploadType
  } = useContext(ListContext);

  const { addAllListItemsToCart, disableAddToCart, itemCount } =
    useCartContext();

  const { shippingBranch } = useContext(BranchContext);
  const { selectedAccounts } = useSelectedAccountsContext();

  const willListOverflow =
    itemCount + (selectedList?.listLineItems?.length || 0) > MAX_CART_ITEMS;

  /**
   * State
   */
  const [listName, setListName] = useState('');
  const [searchValue, setSearchValue] = useState('');
  const [appliedSearchValue, setAppliedSearchValue] = useState('');
  const [isReordered, setIsReordered] = useState(false);
  const [isDialogOpen, setDialogOpen] = useState(false);
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);

  /**
   * Ref
   */
  const uploadErrorsRef = useRef<HTMLDivElement>(null);

  /**
   * Data
   */
  const partNumbers = useMemo(partNumbersMemo, [selectedList]);
  const open = Boolean(anchorEl);

  const { data: pricingData, loading: pricingLoading } =
    useGetProductPricingQuery({
      fetchPolicy: 'no-cache',
      skip: partNumbers.length === 0,
      variables: {
        input: {
          customerId: selectedAccounts.billTo?.erpAccountId ?? '',
          branchId: shippingBranch?.branchId ?? '',
          productIds: partNumbers,
          includeListData: false
        }
      }
    });

  /**
   * Memo
   */
  const lineItemResults = useMemo(listLineItemMemo, [
    selectedList,
    appliedSearchValue,
    isReordered
  ]);

  const nameAlreadyExists = useMemo(nameExistsMemo, [
    lists,
    listName,
    selectedList
  ]);

  const orderedPricing = useMemo(() => {
    return lineItemResults.map((item) => {
      return pricingData?.productPricing.products?.find(
        (p) => p.productId === item?.erpPartNumber
      );
    });
  }, [lineItemResults, pricingData]);

  /**
   * Effects
   */
  useEffect(updateNameWhenSelectedListChanges, [
    lists,
    selectedList,
    setListName
  ]);
  useEffect(handleShowAlert, [pushAlert, setShowAlert, showAlert]);
  useEffect(scrollToUploadErrors, [showUploadErrors]);

  /**
   * Callbacks
   */
  const createList = () => {
    setOpen(true);
    setListMode(ListMode.CREATE);
  };

  const changeList = () => {
    setOpen(true);
    setListMode(ListMode.CHANGE);
  };

  const handleDelete = () => {
    if (selectedList) {
      const listIds = availableListIds.filter(
        (listId) => listId !== selectedList.id
      );
      setAvailableListIds(listIds);
      deleteList(selectedList.id);
    }
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  return (
    <>
      <Breadcrumbs pageTitle={t('common.myLists')} />
      <Container
        sx={{
          '@media print': {
            maxWidth: 'unset',
            px: 0,
            mx: 0
          }
        }}
      >
        {((listsLoading && !isUpdating) || listDetailsLoading) && (
          <Loader backdrop />
        )}
        <Grid>
          <Grid
            item
            pl={1}
            xs={12}
            container
            justifyContent="space-between"
            alignItems="center"
            spacing={1}
          >
            <MyListsHeaderContainer>
              <ListsHeaderTypography variant="h5" data-testid="my-lists-header">
                {t('common.myLists')}
                {` (${lists.length})`}
              </ListsHeaderTypography>
            </MyListsHeaderContainer>
            <Grid>
              <CreateListButton
                variant="inline"
                startIcon={<AddIcon style={{ color: 'white' }} />}
                data-testid="create-new-list-button"
                aria-controls={open ? 'list-menu' : undefined}
                aria-haspopup="true"
                aria-expanded={open ? 'true' : undefined}
                onClick={(event) => setAnchorEl(event.currentTarget)}
              >
                <Typography color="primary.contrastText">
                  {t('lists.createList')}
                </Typography>
              </CreateListButton>
              <Menu
                id="list-menu"
                anchorEl={anchorEl}
                open={open}
                onClose={handleClose}
                MenuListProps={{
                  'aria-labelledby': 'create-new-list-button'
                }}
              >
                <MenuItem onClick={handleClose}>
                  <Grid pt={1} item md="auto" xs={6}>
                    <Button
                      variant="inline"
                      data-testid="create-list-button"
                      startIcon={<AddIcon />}
                      onClick={createList}
                    >
                      {t('lists.createEmptyList')}
                    </Button>
                  </Grid>
                </MenuItem>
                <MenuItem onClick={handleClose}>
                  <Grid pb={1} item md="auto" xs={6}>
                    <Button
                      variant="inline"
                      startIcon={<FileUploadIcon />}
                      onClick={() => history.push('/lists/upload')}
                      data-testid="list-file-upload"
                    >
                      {t('lists.uploadListFromFile')}
                    </Button>
                  </Grid>
                </MenuItem>
              </Menu>
            </Grid>
          </Grid>
          <ListInfoCard square>
            <Grid container>
              <Grid container item md={true}>
                {/* Labels */}
                <Grid
                  item
                  container
                  alignItems="center"
                  xs={12}
                  justifyContent="space-between"
                >
                  {!listName &&
                  !selectedList?.listLineItems.length &&
                  Boolean(listDetailsLoading) ? (
                    <Skeleton variant="rectangular" height={24} width={260} />
                  ) : (
                    <Grid item>
                      <Button
                        variant="inline"
                        endIcon={<ArrowDropDownIcon />}
                        disabled={lists.length <= 1}
                        data-testid="change-list-button"
                        onClick={changeList}
                        sx={{
                          fontSize: '25px',
                          py: 1.5,
                          px: 0
                        }}
                      >
                        {`${t('common.list_one')} ${listName} ${t(
                          'lists.listDetails',
                          { count: selectedList?.listLineItems.length ?? 0 }
                        )}`}
                      </Button>
                    </Grid>
                  )}
                  <ListActions
                    pricingData={pricingData}
                    pricingLoading={pricingLoading}
                    setDialogOpen={setDialogOpen}
                  />
                </Grid>
              </Grid>
            </Grid>
          </ListInfoCard>
        </Grid>
        <ListItemsInfoCard square>
          {uploadErrors ? (
            <Box
              mx={isSmallScreen ? 3 : 6}
              mt={isSmallScreen ? 2 : 5}
              mb={isSmallScreen ? 5 : 2}
              displayPrint="none"
              ref={uploadErrorsRef}
            >
              <Collapse in={showUploadErrors}>
                <Alert
                  icon={<Box component={WarningIcon} sx={{ mr: 1 }} />}
                  action={
                    <IconButton
                      onClick={() => setShowUploadErrors(false)}
                      sx={{
                        color: 'info.main',
                        mt: -0.5
                      }}
                    >
                      <CloseIcon />
                    </IconButton>
                  }
                  severity="info"
                  sx={{
                    px: isSmallScreen ? 2 : 5,
                    py: isSmallScreen ? 2 : 3,
                    color: 'info.main'
                  }}
                  data-testid="list-upload-errors"
                >
                  <AlertTitle
                    sx={(theme) => ({
                      ...theme.typography.h5
                    })}
                  >
                    {t(
                      uploadType === 'new'
                        ? 'lists.uploadError'
                        : 'lists.duplicateError',
                      {
                        count: uploadErrors?.length
                      }
                    )}
                  </AlertTitle>
                  <Box
                    component="ul"
                    sx={{
                      mx: isSmallScreen ? -4 : 0,
                      paddingInlineStart: (theme) =>
                        theme.spacing(isSmallScreen ? 4 : 2.5)
                    }}
                  >
                    {uploadErrors?.map((error, i) => (
                      <Box
                        component="li"
                        sx={(theme) => ({
                          color: 'text.primary',
                          ...theme.typography.body1
                        })}
                        key={i}
                      >{`Part #: ${error.partNumber}, Desc: ${error.description}, MFR: ${error.manufacturerName}, QTY: ${error.quantity}`}</Box>
                    ))}
                  </Box>
                </Alert>
              </Collapse>
            </Box>
          ) : null}
          {!Boolean(lists?.length) ||
          !selectedList ||
          !Boolean(selectedList?.listLineItems?.length) ? (
            <Box
              px={4}
              py={isSmallScreen ? 6 : 15}
              display="flex"
              flexDirection="column"
              justifyContent="center"
              alignItems={isSmallScreen ? 'stretch' : 'center'}
            >
              <Typography
                variant="h4"
                align="center"
                sx={{
                  pb: isSmallScreen ? 3 : 4,
                  color: 'secondary03.main'
                }}
                data-testid="list-info"
              >
                {listsLoading || (listDetailsLoading && lists.length) ? (
                  <Skeleton width={200} />
                ) : !lists.length ? (
                  t('lists.noLists')
                ) : (
                  t('lists.listEmpty')
                )}
              </Typography>
              <Typography
                variant="h6"
                sx={{
                  color: 'secondary03.main'
                }}
              >
                {listsLoading || (listDetailsLoading && lists.length) ? (
                  <>
                    <Skeleton width={300} />
                    <Skeleton width={200} />
                  </>
                ) : !Boolean(lists.length) ? (
                  t('lists.startCreateNew')
                ) : (
                  t('lists.searchProducts')
                )}
              </Typography>
            </Box>
          ) : (
            <>
              <Divider sx={{ displayPrint: 'none' }} />
              <Box
                px={isSmallScreen ? 3 : 6}
                py={isSmallScreen ? 2 : 3}
                displayPrint="none"
              >
                <Grid container spacing={isSmallScreen ? 2 : undefined}>
                  <Grid width={400} mt={0.5} item>
                    <TextField
                      InputProps={{ sx: { height: 40 } }}
                      label={t('lists.searchLabel')}
                      data-testid="search-lists-input"
                      placeholder={t('lists.searchPlaceholder')}
                      value={searchValue}
                      onChange={(e: ChangeEvent<HTMLInputElement>) =>
                        setSearchValue(e.target.value)
                      }
                      fullWidth
                    />
                  </Grid>
                  <Grid
                    item
                    container
                    xs={12}
                    md={3}
                    justifyContent="space-between"
                    sx={{
                      pb: isSmallScreen ? 1 : 2.5,
                      mt: isSmallScreen ? 0 : 4.5
                    }}
                  >
                    <Grid
                      item
                      container
                      justifyContent="flex-start"
                      alignItems="center"
                    >
                      <Grid ml={isSmallScreen ? 0 : 3} item md="auto">
                        <Button
                          variant="secondary"
                          onClick={() => setAppliedSearchValue(searchValue)}
                          data-testid="view-search-in-list-results"
                        >
                          {t('common.search')}
                        </Button>
                      </Grid>
                      <Grid ml={isSmallScreen ? 0 : 2} item xs="auto" md="auto">
                        <Button
                          variant="inline"
                          onClick={() => {
                            setSearchValue('');
                            setAppliedSearchValue('');
                          }}
                          data-testid="clear-search-in-list-results"
                        >
                          {t('common.clear')}
                        </Button>
                      </Grid>
                    </Grid>
                  </Grid>
                  <CartButtonContainer container>
                    <AddToCartButtonContainer item>
                      <AdvancedToolTip
                        title="Warning"
                        text={t('cart.maxLimitToolTip')}
                        icon={<WarningIcon />}
                        placement={isSmallScreen ? 'bottom' : 'left'}
                        disabled={disableAddToCart || willListOverflow}
                      >
                        <FeedbackButton
                          startIcon={<CartIcon />}
                          onClick={handleAddAllToCart}
                          disabled={
                            !lists.length ||
                            listsLoading ||
                            pricingLoading ||
                            Boolean(appliedSearchValue) ||
                            disableAddToCart ||
                            willListOverflow
                          }
                          value={t('common.addAll')}
                          valueDone={t('common.addedAll')}
                          testId="lists-add-all-to-cart"
                          fullWidth
                        />
                      </AdvancedToolTip>
                    </AddToCartButtonContainer>
                  </CartButtonContainer>
                  <Grid
                    item
                    xs={12}
                    alignItems="center"
                    justifyContent="center"
                    mt={
                      (disableAddToCart || willListOverflow) && isSmallScreen
                        ? 18
                        : 0
                    }
                  >
                    <Typography
                      color="textSecondary"
                      variant="caption"
                      data-testid={`${
                        isSmallScreen ? 'tipMobile' : 'tipDesktop'
                      }`}
                    >
                      {t(`lists.tipDesktop`)}
                    </Typography>
                  </Grid>
                </Grid>
              </Box>
              <Divider sx={{ displayPrint: 'none' }} />
              <DragDropContext onDragEnd={onDragEnd}>
                <Droppable
                  droppableId="droppable"
                  mode="virtual"
                  renderClone={(provided, snapshot, rubric) => (
                    <Paper
                      key={`line-item-${rubric.source.index}`}
                      elevation={snapshot.isDragging ? 8 : 0}
                      ref={provided.innerRef}
                      {...provided.draggableProps}
                      {...provided.dragHandleProps}
                      sx={{
                        outline: 'none'
                      }}
                    >
                      <ListsLineItem
                        item={lineItemResults[rubric.source.index] as any}
                        loading={false}
                        priceData={orderedPricing[rubric.source.index]}
                        priceDataLoading={false}
                      />
                    </Paper>
                  )}
                >
                  {(provided) => (
                    <div data-testid="list-line-items-container">
                      <List
                        height={height - 300}
                        itemCount={lineItemResults.length}
                        itemSize={isSmallScreen ? 300 : 164}
                        width="100%"
                        innerRef={provided.innerRef}
                        itemData={{
                          lineItemResults,
                          isDragDisabled: Boolean(
                            searchValue ||
                              appliedSearchValue ||
                              listDetailsLoading
                          ),
                          loading: listDetailsLoading && !isUpdating,
                          orderedPricing,
                          priceDataLoading: pricingLoading,
                          updateItem: updateListLineItem,
                          branchId: shippingBranch?.branchId
                        }}
                      >
                        {ListRow}
                      </List>
                    </div>
                  )}
                </Droppable>
              </DragDropContext>
            </>
          )}
        </ListItemsInfoCard>
        <Grid height={isSmallScreen ? 130 : 224} />
      </Container>
      <ListDialog
        open={isDialogOpen}
        onClose={() => setDialogOpen(false)}
        handleDelete={handleDelete}
      />
      <ListRenameDialog
        listName={listName}
        setListName={setListName}
        nameAlreadyExists={nameAlreadyExists}
      />
    </>
  );

  function handleAddAllToCart() {
    if (!lineItemResults.length) {
      return;
    }

    if (selectedList?.id && pricingData) {
      let listItemsInfo = lineItemResults.map((lineItem) => {
        const { erpPartNumber, quantity } = lineItem;
        const productPrice = pricingData.productPricing?.products.filter(
          (data) => data.productId === erpPartNumber
        );
        const qtyAvailable = shippingBranch?.isPricingOnly
          ? productPrice[0].totalAvailableQty ?? 0
          : productPrice[0].branchAvailableQty ?? 0;
        const itemInfoList = {
          qty: quantity ?? 0,
          productId: erpPartNumber ?? '',
          minIncrementQty: 0,
          qtyAvailable,
          uom: productPrice[0].orderUom ?? t('product.each'),
          pricePerUnit: productPrice[0].sellPrice ?? 0
        };
        return itemInfoList;
      });
      const listId = selectedList.id;
      addAllListItemsToCart(listId, listItemsInfo);
    }
  }

  function onDragEnd(result: DropResult) {
    if (!selectedList || !result.destination || !result.source) {
      pushAlert(t('lists.sortFail'), {
        variant: 'warning'
      });
      return;
    }
    if (result.destination.index === result.source.index) {
      return;
    }

    const newList = { ...selectedList };
    newList.listLineItems = reorder(
      result.source.index,
      result.destination.index
    );

    setIsUpdating(
      newList.listLineItems[result.destination.index].erpPartNumber ?? undefined
    );
    setSelectedList(newList);
    setIsReordered(true);
    updateList(selectedList.name, newList.listLineItems);
  }

  function reorder(startIndex: number, endIndex: number) {
    if (!selectedList) {
      return [] as ListLineItem[];
    }

    let reordered = [...lineItemResults];
    const [removed] = reordered.splice(startIndex, 1);
    reordered.splice(endIndex, 0, removed);

    // Apply sort order for each of the list line item
    const result = reordered.map((l, i) => {
      const clone = { ...l };
      clone.sortOrder = i;
      return clone as ListLineItem;
    });
    return result;
  }

  function updateListLineItem(item: ListLineItem) {
    if (!selectedList || !item.erpPartNumber) {
      return;
    }
    let listLineItemHelper = selectedList.listLineItems.filter(
      (unchanged) => unchanged.erpPartNumber !== item.erpPartNumber
    );
    const newList = {
      ...selectedList,
      listLineItems: [...listLineItemHelper, item]
    };
    setSelectedList(newList);
    setIsUpdating(item.erpPartNumber);
    updateList(selectedList.name, newList.listLineItems);
  }

  /**
   * Memo Defs
   */
  function listLineItemMemo() {
    const lowerCasedSearchValue = appliedSearchValue.toLowerCase();
    if (isReordered) {
      setIsReordered(false);
    }

    // Take a copy of the raw list and then sort it
    const rawList = [...(selectedList?.listLineItems || [])] as ListLineItem[];
    const sortedList =
      rawList.length > 1
        ? rawList.sort((a, b) => (a?.sortOrder || 0) - (b?.sortOrder || 0))
        : rawList;

    // Apply search filter if needed
    return appliedSearchValue && selectedList
      ? sortedList.filter(
          (item) =>
            item.manufacturerName
              ?.toLowerCase()
              .includes(lowerCasedSearchValue) ||
            item.manufacturerNumber
              ?.toLowerCase()
              .includes(lowerCasedSearchValue) ||
            item.name?.toLowerCase().includes(lowerCasedSearchValue) ||
            `msc-${item.erpPartNumber}`
              ?.toLowerCase()
              .includes(lowerCasedSearchValue)
        )
      : sortedList;
  }

  function partNumbersMemo() {
    if (!selectedList?.listLineItems.length) {
      return [];
    }
    //Sorting so that update doesnt trigger refetch
    const partNumbers = selectedList!
      .listLineItems!.map((item) => item.erpPartNumber ?? '')
      .sort((a, b) => a.localeCompare(b));
    return partNumbers;
  }

  function nameExistsMemo() {
    return lists
      .filter((l) => l.name !== selectedList?.name)
      .some((l) => l.name === listName);
  }

  /**
   * Effect defs
   */
  function updateNameWhenSelectedListChanges() {
    setListName(selectedList?.name ?? '');
  }

  function handleShowAlert() {
    if (showAlert) {
      pushAlert(...showAlert);
      setShowAlert(undefined);
    }
  }

  function scrollToUploadErrors() {
    if (showUploadErrors && uploadErrorsRef.current) {
      uploadErrorsRef.current.scrollIntoView({
        behavior: 'smooth',
        block: 'start',
        inline: 'nearest'
      });
    }
  }
}

export default Lists;
