import { useState, useEffect, useMemo } from 'react';

import {
  Box,
  Button,
  Divider,
  Grid,
  IconButton,
  Image,
  Tooltip,
  Typography,
  Link
} from '@dialexa/reece-component-library';
import Dotdotdot from 'react-dotdotdot';
import { useTranslation } from 'react-i18next';
import { Link as RouterLink } from 'react-router-dom';
import slugify from 'react-slugify';
import { useDomainInfo } from 'hooks/useDomainInfo';

import CartLineNotesModal from 'Cart/LineNotesModal';
import { useCheckoutContext } from 'Checkout/CheckoutProvider';
import ConditionalWrapper from 'common/ConditionalWrapper';
import Loader from 'common/Loader';
import QtyInput from 'common/QtyInput';
import { LineItem } from 'generated/graphql';
import { DeleteBinIcon, EditIcon, WarningIcon } from 'icons';
import notfound from 'images/notfound.png';
import { format } from 'utils/currency';
import AddToListbutton from 'Product/AddToListButton';
import { useCartContext } from 'providers/CartProvider';

type ItemMobileProps = {
  lineItem: LineItem;
  updateItemQuantity?: (
    itemId: string,
    quantity: number,
    minIncrementQty: number,
    productName: string
  ) => void;
  handleDeleteItem?: (itemId: string) => void;
  readOnly?: boolean;
  index?: number;
  canAddToList?: boolean;
};

function ItemMobile(props: ItemMobileProps) {
  /**
   * Const
   */
  const qtyAvailable = props.lineItem.qtyAvailable ?? 0;
  const parentQty =
    props.lineItem.quantity ?? props.lineItem?.product?.minIncrementQty ?? 1;
  const lineItemId = props.lineItem.id ?? '';
  const { isWaterworks } = useDomainInfo();
  const productSlug = slugify(
    `${props.lineItem.product?.manufacturerName}-${props.lineItem.product?.name}-${props.lineItem.product?.manufacturerNumber}`
  );

  /**
   * Custom Hooks
   */
  const { t } = useTranslation();

  /**
   * State
   */
  const [loading, setLoading] = useState(false);
  const [quantity, setQuantity] = useState(parentQty);
  const [editingNotes, setEditingNotes] = useState(false);
  const [availableInList, setAvailableInList] = useState<string[]>([]);

  /**
   * Context
   */
  const { contract, deleteItem, itemLoading, lineNotes, setLineNotes } =
    useCartContext();
  const { orderedContract, orderedLineNotes } = useCheckoutContext();
  const hasContract = contract || orderedContract;
  const notAvailable =
    (!qtyAvailable || qtyAvailable < parentQty) && !hasContract;

  /**
   * Memo
   */
  const lineNote = useMemo(
    () => lineNotes?.[lineItemId] ?? orderedLineNotes?.[lineItemId] ?? '',
    [lineNotes, orderedLineNotes, lineItemId]
  );

  /**
   * Handles
   */
  const handleLineNoteChange = (data: string) => {
    if (lineItemId && data !== lineNotes[lineItemId]) {
      setLineNotes({ ...lineNotes, [lineItemId]: data });
    }
  };
  const updatedAddedToLists = (value: string[]) => {
    setAvailableInList(value);
  };

  /**
   * Effects
   */
  useEffect(handleLineItemUpdate, [parentQty]);

  useEffect(handleAvailableInList, [props.lineItem.listIds]);

  /**
   * Height
   */
  const height = hasContract && props.lineItem?.id ? 400 : 360;

  /**
   * Render
   */
  return (
    <Box height={height}>
      <Box mx={1} px={1} position="relative">
        {(loading || itemLoading === props.lineItem.id) && (
          <Loader backdrop containerProps={{ mx: -1, zIndex: 2 }} />
        )}
        <Box mb={3}>
          {props.canAddToList && (
            <Box py={1}>
              <AddToListbutton
                availableInList={availableInList}
                updatedAddedToLists={updatedAddedToLists}
                partNumber={props.lineItem?.erpPartNumber ?? ''}
                quantity={quantity}
                index={props.index}
              />
            </Box>
          )}
          <Grid container alignItems="center">
            <Grid item xs={6}>
              <Box
                height={72}
                width={72}
                display="flex"
                justifyContent="center"
                alignItems="center"
              >
                {isWaterworks ? (
                  <Image
                    fallback={notfound}
                    alt={
                      props.lineItem.product?.name ?? t('common.productPicture')
                    }
                    sx={{ margin: 'auto' }}
                    src={props.lineItem.product?.imageUrls?.medium ?? ''}
                  />
                ) : (
                  <Link
                    to={`/product/${productSlug}/MSC-${props.lineItem?.erpPartNumber}`}
                    component={RouterLink}
                    height="100%"
                    sx={{ display: 'inline-flex' }}
                  >
                    <Image
                      fallback={notfound}
                      alt={
                        props.lineItem.product?.name ??
                        t('common.productPicture')
                      }
                      sx={{ margin: 'auto' }}
                      src={props.lineItem.product?.imageUrls?.medium ?? ''}
                    />
                  </Link>
                )}
              </Box>
            </Grid>
            <Grid
              container
              item
              xs={6}
              justifyContent="flex-end"
              alignItems="center"
            >
              {!props.readOnly && (
                <IconButton
                  onClick={handleDeleteItem}
                  size="large"
                  data-testid={`remove-item-button-${props.index}`}
                >
                  <DeleteBinIcon />
                </IconButton>
              )}
            </Grid>
          </Grid>
        </Box>
        <Grid container wrap="nowrap">
          <Grid item xs>
            <Typography
              variant="caption"
              color="textSecondary"
              data-testid={`cart-item-manufacturer-name-${props.index}`}
            >
              {props.lineItem.product?.manufacturerName}
            </Typography>
            <Dotdotdot clamp={3}>
              <ConditionalWrapper
                condition={!!props.lineItem.product?.name}
                wrapper={(children) => (
                  <Tooltip
                    title={props.lineItem.product!.name!}
                    disableFocusListener
                    enterTouchDelay={0}
                  >
                    {children}
                  </Tooltip>
                )}
              >
                <Typography
                  variant="body1"
                  color="primary"
                  py={0.5}
                  data-testid={`cart-item-product-name-${props.index}`}
                >
                  {isWaterworks ? (
                    props.lineItem.product?.name
                  ) : (
                    <Link
                      to={`/product/${productSlug}/MSC-${props.lineItem?.erpPartNumber}`}
                      component={RouterLink}
                    >
                      {props.lineItem.product?.name}
                    </Link>
                  )}
                </Typography>
              </ConditionalWrapper>
            </Dotdotdot>
            <Grid container>
              {!!props.lineItem.product?.partNumber && (
                <Typography
                  display="block"
                  variant="caption"
                  color="textSecondary"
                  data-testid={`cart-item-part-number-${props.index}`}
                  mr={2}
                >
                  {t('contract.partNum')} {props.lineItem.product!.partNumber}
                </Typography>
              )}
              {!!props.lineItem.product?.manufacturerNumber && (
                <Typography
                  display="block"
                  variant="caption"
                  color="textSecondary"
                  data-testid={`cart-item-manufacturer-number-${props.index}`}
                >
                  {t('product.mfr')}{' '}
                  {props.lineItem.product!.manufacturerNumber}
                </Typography>
              )}
            </Grid>
          </Grid>
          {notAvailable && (
            <Grid item container xs="auto" alignItems="center">
              <Tooltip
                disableFocusListener
                enterTouchDelay={0}
                title={t('cart.outOfStock') as string}
              >
                <Box
                  display="block"
                  component={WarningIcon}
                  my={3}
                  color="error.main"
                  height={32}
                  width={32}
                />
              </Tooltip>
            </Grid>
          )}
          <Grid container item xs="auto" justifyContent="flex-end">
            <Box
              mt={props.readOnly ? 3 : 0}
              ml={1}
              display="flex"
              alignItems="center"
            >
              {props.readOnly ? (
                <Box
                  display="flex"
                  justifyContent="flex-end"
                  alignItems="center"
                >
                  <Typography
                    variant="caption"
                    component="span"
                    color="primary"
                  >
                    {t('cart.orderQty')}
                  </Typography>
                  <Box ml={2}>
                    <Typography
                      variant="h5"
                      component="span"
                      color="textSecondary"
                      fontWeight={400}
                      data-testid={`cart-item-order-qty-${props.index}`}
                    >
                      {props.lineItem.quantity}
                    </Typography>
                  </Box>
                </Box>
              ) : (
                <Box
                  border={1}
                  width={96}
                  borderColor={notAvailable ? 'error.main' : 'common.white'}
                >
                  <QtyInput
                    max={999999}
                    fullWidth
                    increment={props.lineItem?.product?.minIncrementQty || 1}
                    size="small"
                    value={quantity}
                    index={props.index}
                    onUpdate={handleQuantityChange}
                    buttonContainerStyle={
                      isWaterworks ? { width: '1.25rem' } : undefined
                    }
                  />
                </Box>
              )}
            </Box>
          </Grid>
        </Grid>
        {/* Line Notes (Contract - read-only) */}
        {!!(
          hasContract &&
          props.lineItem?.id &&
          props.readOnly &&
          lineNote
        ) && (
          <Tooltip title={lineNote} disableFocusListener enterTouchDelay={0}>
            <Grid container wrap="nowrap" mt={1}>
              <Grid item container xs="auto" alignContent="center">
                <Typography noWrap fontWeight={600} mr={2}>
                  {t('cart.lineNotes')}
                </Typography>
              </Grid>
              <Grid item container xs alignContent="center" overflow="hidden">
                <Typography noWrap>{lineNote}</Typography>
              </Grid>
            </Grid>
          </Tooltip>
        )}
        <Grid container pt={3} justifyContent="space-between">
          <Grid item container direction="column" xs={4} wrap="nowrap">
            <Box pb={2}>
              <Typography variant="caption" color="primary">
                {t('common.price')}
              </Typography>
            </Box>
            <Typography
              variant="h5"
              color="textSecondary"
              fontWeight={400}
              data-testid={`cart-item-unit-price-${props.index}`}
            >
              {`${format(
                props.lineItem.pricePerUnit! / (hasContract ? 1 : 100)
              )} ${props.lineItem.uom ?? t('product.each')}`}
            </Typography>
          </Grid>
          {!hasContract && ( // Remove this when we implement availableQty for release contract
            <Grid container item xs={4} direction="column" alignItems="center">
              <Box pb={2}>
                <Typography variant="caption" color="primary">
                  {t('common.availableQty')}
                </Typography>
              </Box>
              <Typography
                variant="h5"
                color="textSecondary"
                fontWeight={400}
                data-testid={`cart-item-qty-available-${props.index}`}
              >
                {props.lineItem.qtyAvailable ?? 0}
              </Typography>
              {!!qtyAvailable && (
                <Typography color="success.main">
                  {t('common.inStock')}
                </Typography>
              )}
            </Grid>
          )}
          <Grid
            container
            item
            xs={4}
            direction="column"
            alignItems="flex-end"
            wrap="nowrap"
          >
            <Box pb={2}>
              <Typography variant="caption" color="primary" noWrap>
                {t('cart.orderTotal')}
              </Typography>
            </Box>
            <Typography
              variant="h5"
              color="textSecondary"
              fontWeight={400}
              data-testid={`cart-item-total-price-${props.index}`}
              noWrap
            >
              {format(
                (props.lineItem.quantity ?? 0) *
                  (props.lineItem.pricePerUnit! / (hasContract ? 1 : 100))
              )}
            </Typography>
          </Grid>
        </Grid>
        {/* Line Notes (Contract only) */}
        {!!(hasContract && props.lineItem?.id && !props.readOnly) && (
          <>
            <Box pt={2}>
              <Grid container wrap="nowrap">
                <Grid item container xs="auto" mr={1}>
                  <Button
                    variant="inline"
                    color="primaryLight"
                    sx={{ textDecoration: 'underline', mx: 0 }}
                    onClick={handleOpenLineNoteModal}
                    data-testid={`cart-item-open-line-notes-${props.index}`}
                  >
                    {lineNote ? t('cart.lineNotes') : t('cart.enterLineNotes')}
                    <Typography
                      color="primary.main"
                      ml={1}
                      component="span"
                      lineHeight={0.5}
                    >
                      <EditIcon width={20} height={20} />
                    </Typography>
                  </Button>
                </Grid>
                <Grid item container xs alignContent="center" overflow="hidden">
                  {!!lineNote && (
                    <Tooltip title={lineNote}>
                      <Typography noWrap>{lineNote}</Typography>
                    </Tooltip>
                  )}
                </Grid>
              </Grid>
            </Box>
            <CartLineNotesModal
              open={editingNotes}
              notes={lineNote}
              onClose={handleCloseLineNoteModal}
              setLineNotes={handleLineNoteChange}
            />
          </>
        )}
      </Box>
      <Box component={Divider} mx={1} />
    </Box>
  );

  /**
   * Effect Definitions
   */
  function handleLineItemUpdate() {
    setQuantity(parentQty);
  }

  /**
   * Callback Definitions
   */
  function handleAvailableInList() {
    const listIdsAvailable = props.lineItem.listIds ?? [];
    setAvailableInList(listIdsAvailable);
  }

  function handleDeleteItem() {
    setLoading(true);
    props.handleDeleteItem?.(lineItemId);
    deleteItem(lineItemId);
  }
  function handleQuantityChange(quantity: number) {
    props.updateItemQuantity?.(
      lineItemId,
      quantity,
      props.lineItem.product?.minIncrementQty || 1,
      props.lineItem.product?.name ?? ''
    );
  }
  function handleOpenLineNoteModal() {
    setEditingNotes(true);
  }
  function handleCloseLineNoteModal() {
    setEditingNotes(false);
  }
}

export default ItemMobile;
