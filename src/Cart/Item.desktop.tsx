import { useEffect, useMemo, useState } from 'react';

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
import './styles.scss';
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

type Props = {
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

function ItemDesktop(props: Props) {
  /**
   * Consts
   */
  const qtyAvailable = props.lineItem.qtyAvailable ?? 0;
  const qty = props.lineItem.quantity ?? 0;
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
  const [editingNotes, setEditingNotes] = useState(false);
  const [quantity, setQuantity] = useState(
    props.lineItem?.quantity ?? props.lineItem?.product?.minIncrementQty ?? 1
  );
  const [availableInList, setAvailableInList] = useState<string[]>([]);

  /**
   * Context
   */
  const { contract, deleteItem, itemLoading, lineNotes, setLineNotes } =
    useCartContext();
  const { orderedContract, orderedLineNotes } = useCheckoutContext();
  const hasContract = !!(contract || orderedContract);
  const notAvailable = (!qtyAvailable || qtyAvailable < qty) && !hasContract;

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
  useEffect(handleLineItemUpdate, [props.lineItem]);

  useEffect(handleAvailableInList, [props.lineItem.listIds]);

  /**
   * Render
   */
  return (
    <Box pt={2} width="100%">
      <Grid
        container
        wrap="nowrap"
        alignContent="center"
        alignItems="center"
        className="cart-list"
      >
        {props.canAddToList && (
          <Grid item xs={1} md={1}>
            <AddToListbutton
              availableInList={availableInList}
              routePath={window.location.pathname}
              updatedAddedToLists={updatedAddedToLists}
              partNumber={props.lineItem?.erpPartNumber ?? ''}
              quantity={quantity}
              index={props.index}
            />
          </Grid>
        )}
        {/* Image */}
        <Grid
          className="cart-list__product--image"
          item
          container
          xs={1}
          md={2.3}
          height={72}
          px={2}
          justifyContent="center"
          alignItems="center"
        >
          {isWaterworks ? (
            <Image
              fallback={notfound}
              alt={props.lineItem.product?.name ?? t('common.productPicture')}
              src={props.lineItem.product?.imageUrls?.medium ?? ''}
              sx={{ objectFit: 'contain' }}
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
                alt={props.lineItem.product?.name ?? t('common.productPicture')}
                src={props.lineItem.product?.imageUrls?.medium ?? ''}
                sx={{ objectFit: 'contain' }}
              />
            </Link>
          )}
        </Grid>
        {/* Product details */}
        <Grid item xs={4} md={5.7} pl={1}>
          <Box display="flex" flexDirection="column">
            <Typography
              variant="caption"
              color="textSecondary"
              data-testid={`cart-item-manufacturer-name-${props.index}`}
            >
              {props.lineItem.product?.manufacturerName}
            </Typography>
            <Dotdotdot clamp={2}>
              <ConditionalWrapper
                condition={!!props.lineItem.product?.name}
                wrapper={(children) => (
                  <Tooltip title={props.lineItem.product!.name!}>
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
            <Box>
              {!!props.lineItem.product?.partNumber && (
                <Typography
                  display="inline"
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
                  display="inline"
                  variant="caption"
                  color="textSecondary"
                  data-testid={`cart-item-manufacturer-number-${props.index}`}
                >
                  {t('product.mfr')}{' '}
                  {props.lineItem.product!.manufacturerNumber}
                </Typography>
              )}
            </Box>
          </Box>
        </Grid>
        {/* Price per unit */}
        <Grid item xs={1} md={2} textAlign="center">
          <Typography
            variant="caption"
            data-testid={`cart-item-unit-price-${props.index}`}
          >
            {!props.lineItem.pricePerUnit
              ? t('product.priceUnavailable')
              : `${format(
                  props.lineItem.pricePerUnit! / (hasContract ? 1 : 100)
                )} ${props.lineItem.uom ?? t('product.each')}`}
          </Typography>
        </Grid>
        {/* Quantity available */}
        {!hasContract && ( // Remove this when we implement availableQty for release contract
          <Grid item xs={1} md={2} textAlign="center">
            <Box display="flex" flexDirection="column" alignItems="center">
              <Typography
                variant="caption"
                color="textSecondary"
                data-testid={`cart-item-qty-available-${props.index}`}
              >
                {qtyAvailable}
              </Typography>
              {!!qtyAvailable && (
                <Typography variant="caption" color="success.main">
                  {t('common.inStock')}
                </Typography>
              )}
            </Box>
          </Grid>
        )}
        {/* QTY Input */}
        <Grid
          item
          xs={4}
          md={3}
          pl={2}
          className="cart-list__product--quantity"
          textAlign="center"
        >
          <Box
            display="flex"
            flexDirection="column"
            alignItems={props.readOnly ? 'flex-end' : 'center'}
            px={props.readOnly ? 3 : 0}
          >
            {props.readOnly ? (
              <Grid container justifyContent="center">
                <Grid item container xs="auto" alignItems="center">
                  <Typography
                    variant="caption"
                    data-testid={`cart-item-order-qty-${props.index}`}
                  >
                    {props.lineItem.quantity}
                  </Typography>
                </Grid>
              </Grid>
            ) : (
              <Grid
                container
                justifyContent="center"
                alignItems="center"
                flexWrap="nowrap"
              >
                {notAvailable && (
                  <Grid item container xs="auto" alignItems="center">
                    <Tooltip title={t('cart.outOfStock') as string}>
                      <Box
                        component={WarningIcon}
                        mr={1}
                        data-testid={`cart-item-out-of-stock-warning-${props.index}`}
                        color="error.main"
                        height={24}
                        width={24}
                      />
                    </Tooltip>
                  </Grid>
                )}
                <Grid
                  item
                  border={1}
                  borderColor={notAvailable ? 'error.main' : 'common.white'}
                  width="70%"
                >
                  <QtyInput
                    max={999999}
                    value={quantity}
                    fullWidth
                    size="small"
                    increment={props.lineItem?.product?.minIncrementQty || 1}
                    onUpdate={handleQuantityChange}
                    index={props.index}
                    noDebounce={!!hasContract}
                    buttonContainerStyle={
                      isWaterworks ? { width: '1.25rem' } : undefined
                    }
                  />
                </Grid>
              </Grid>
            )}
          </Box>
        </Grid>
        {/* Order Total */}
        <Grid item xs={2} md={3} textAlign="center">
          <Typography
            variant="caption"
            data-testid={`cart-item-total-price-${props.index}`}
          >
            {!props.lineItem.pricePerUnit
              ? t('product.priceUnavailable')
              : format(
                  (props.lineItem.pricePerUnit! / (hasContract ? 1 : 100)) *
                    quantity
                )}
          </Typography>
        </Grid>
        {/* Delete icon */}
        {!props.readOnly && (
          <Grid item xs={1} md={1}>
            <IconButton
              onClick={handleDeleteItem}
              data-testid={`remove-item-button-${props.index}`}
              size="large"
              disabled={loading || itemLoading === lineItemId}
            >
              <DeleteBinIcon />
            </IconButton>
          </Grid>
        )}
        {(loading || itemLoading === lineItemId) && (
          <Loader
            backdrop
            size="parent"
            containerProps={{
              position: 'absolute',
              left: 0,
              right: 0,
              width: '100%',
              height: '100%',
              zIndex: 2 // so this does not goes above the qty change modal
            }}
          />
        )}
      </Grid>
      <Grid
        pb={2}
        container
        wrap="nowrap"
        alignContent="center"
        alignItems="center"
        className="cart-list"
      >
        <Grid item xs={1} md={1.8}></Grid>
        <Grid item xs={11} md={10.2}>
          <Box>
            {/* Line Notes (Contract only) */}
            {!!(hasContract && props.lineItem?.id) && (
              <Grid item>
                <Box>
                  <Grid container wrap="nowrap">
                    {!props.readOnly ? (
                      <Grid item container xs="auto" mr={1}>
                        <Button
                          variant="inline"
                          color="primaryLight"
                          sx={{ textDecoration: 'underline', mx: 0 }}
                          onClick={handleOpenLineNoteModal}
                          data-testid={`cart-item-open-line-notes-${props.index}`}
                        >
                          {lineNote
                            ? t('cart.lineNotes')
                            : t('cart.enterLineNotes')}
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
                    ) : (
                      !!lineNote && (
                        <Grid item container xs="auto" alignContent="center">
                          <Typography noWrap fontWeight={600} mr={2} ml={1}>
                            {t('cart.lineNotes')}
                          </Typography>
                        </Grid>
                      )
                    )}
                    <Grid
                      item
                      container
                      xs={10}
                      alignContent="center"
                      overflow="hidden"
                      textOverflow="ellipsis"
                    >
                      {!!lineNote && (
                        <Tooltip title={lineNote}>
                          <Typography
                            noWrap
                            data-testid={`cart-line-note-${props.index}`}
                          >
                            {lineNote}
                          </Typography>
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
              </Grid>
            )}
          </Box>
        </Grid>
      </Grid>
      <Box component={Divider} width="100%" />
    </Box>
  );

  /**
   * Effect Definitions
   */
  function handleLineItemUpdate() {
    setQuantity(
      props.lineItem.quantity ?? props.lineItem?.product?.minIncrementQty ?? 1
    );
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

export default ItemDesktop;
