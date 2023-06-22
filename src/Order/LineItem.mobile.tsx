import { useContext } from 'react';

import {
  Box,
  Button,
  CircularProgress,
  Grid,
  Image,
  Skeleton,
  Tooltip,
  Typography,
  useTheme
} from '@dialexa/reece-component-library';
import Dotdotdot from 'react-dotdotdot';
import { useTranslation } from 'react-i18next';

import { AuthContext } from 'AuthProvider';

import ConditionalWrapper from 'common/ConditionalWrapper';
import ItemUnavailable from 'common/ItemUnavailable';
import { MiscChargeIcon, WarningIcon } from 'icons';
import notfound from 'images/notfound.png';
import { SubOrderLineItemProps } from 'Order/LineItem';
import AddToListButton from 'Product/AddToListButton';
import { format } from 'utils/currency';
import { useDomainInfo } from 'hooks/useDomainInfo';
import {
  ChargeIconContainer,
  LineItemContainer,
  LineItemGridPricing,
  LineItemGridProductInfo,
  LineItemGridThumb,
  LineItemSubGrid,
  LineItemText,
  LineItemThumbContainer,
  LineItemTitleText
} from 'Order/utils/styled';
import { getUOM } from './utils/uom';
import { useCartContext } from 'providers/CartProvider';

export default function LineItemMobile(props: SubOrderLineItemProps) {
  /**
   * Props
   */
  const {
    availableInList,
    handleReorderButtonClick,
    isComment,
    isMincron,
    lineItem,
    loading,
    notAvailable,
    setAvailableInList,
    urlWrapper
  } = props;

  // Line Item stuff
  const {
    backOrderedQuantity,
    erpPartNumber,
    imageUrls,
    lineComments,
    lineNumber,
    manufacturerName,
    manufacturerNumber,
    orderQuantity,
    productId,
    productName,
    productOrderTotal,
    shipQuantity,
    status,
    unitPrice
  } = lineItem || {};

  /**
   * Custom hooks
   */
  const { isWaterworks } = useDomainInfo();
  const theme = useTheme();
  const { t } = useTranslation();

  /**
   * Context
   */
  const { authState } = useContext(AuthContext);
  const { cartLoading } = useCartContext();

  /**
   * Render
   */
  return (
    <Box
      data-testid={
        lineNumber ? `row_${productId}_line_${lineNumber}` : `row_${productId}`
      }
    >
      <LineItemContainer>
        <LineItemGridThumb item>
          <LineItemThumbContainer>
            {loading ? (
              <Skeleton variant="rectangular" height={80} width={80} />
            ) : isComment ? (
              <ChargeIconContainer>
                <MiscChargeIcon />
              </ChargeIconContainer>
            ) : (
              <Image
                alt={productName ?? t('common.productPicture')}
                fallback={notfound}
                src={imageUrls?.thumb ?? t('common.productPicture')}
                data-testid="product-image"
              />
            )}
          </LineItemThumbContainer>
        </LineItemGridThumb>
        <LineItemGridProductInfo xs>
          {!isComment && !notAvailable && (
            <Typography variant="subtitle2" data-testid="mfr-name">
              {loading ? <Skeleton width={80} /> : manufacturerName ?? ''}
              {!loading && !!backOrderedQuantity && (
                <Box
                  data-testid="back-order-warning-icon"
                  component={WarningIcon}
                  height={20}
                  width={20}
                  ml={0.5}
                  style={{ verticalAlign: 'text-bottom', marginBottom: -2 }}
                  color="secondary.main"
                />
              )}
            </Typography>
          )}
          <Typography color="primary" component="div">
            {loading ? (
              <Skeleton />
            ) : isComment ? (
              <Box pt={3.5} component="span">
                {/* NOTE: status is always truthy because isComment requires status.  */}
                {t(`orders.${status!.toLowerCase()}`)}
              </Box>
            ) : !notAvailable ? (
              <Typography component="div" gutterBottom>
                <ConditionalWrapper
                  condition={!isWaterworks && !isMincron}
                  wrapper={urlWrapper}
                >
                  <Dotdotdot clamp={3}>
                    <Tooltip title={productName ?? ''}>
                      <Typography>{productName}</Typography>
                    </Tooltip>
                  </Dotdotdot>
                </ConditionalWrapper>
              </Typography>
            ) : (
              productName
            )}
          </Typography>
          {!isComment &&
            (loading ? (
              <Typography variant="subtitle2">
                <Skeleton width={120} />
              </Typography>
            ) : (
              <>
                {isMincron && (
                  <Typography variant="subtitle2" data-testid="part-number">
                    {erpPartNumber
                      ? `${t('contract.partNum')} ${erpPartNumber}`
                      : ''}
                  </Typography>
                )}
                <Typography variant="subtitle2" data-testid="mfr-number">
                  {manufacturerNumber
                    ? `${t('product.mfr')} ${manufacturerNumber}`
                    : ''}
                </Typography>
              </>
            ))}
        </LineItemGridProductInfo>
      </LineItemContainer>
      {!!(lineComments && isMincron) && (
        <Tooltip title={lineComments} disableFocusListener enterTouchDelay={0}>
          <Grid container wrap="nowrap" mb={2}>
            <Grid item xs="auto" display="flex" flexWrap="nowrap">
              <Box display="inline" displayPrint="none" width={96} />
              <Typography noWrap fontWeight={600} mr={2}>
                {t('cart.lineNotes')}
              </Typography>
            </Grid>
            <Grid item xs display="grid" overflow="hidden">
              {loading ? (
                <Skeleton width={30} />
              ) : (
                <Typography noWrap data-testid="order-line-comment">
                  {lineComments}
                </Typography>
              )}
            </Grid>
          </Grid>
        </Tooltip>
      )}
      <LineItemContainer>
        <LineItemGridPricing container item md>
          {isComment ? (
            <Grid container item xs={9} alignItems="center" pr={1.5}>
              <Typography variant="body1">{productName}</Typography>
            </Grid>
          ) : (
            <>
              <Grid item xs={3}>
                <LineItemTitleText>{t('common.price')}</LineItemTitleText>
              </Grid>
              <Grid item xs={3}>
                <LineItemTitleText>
                  {t('orders.orderQuantity')}
                </LineItemTitleText>
              </Grid>
              <Grid item xs={3}>
                <LineItemTitleText>
                  {t('orders.shipQuantity')}
                </LineItemTitleText>
              </Grid>
            </>
          )}
          <Grid item xs={3}>
            <Box
              display="flex"
              flexDirection="column"
              height={1}
              justifyContent={isComment ? 'flex-end' : 'flex-start'}
            >
              <Typography color="primary" variant="subtitle2" align="center">
                {t('orders.orderTotal')}
              </Typography>
              {!!isComment &&
                (loading ? (
                  <Skeleton width={50} />
                ) : (
                  <Typography variant="subtitle2" align="center" pt={1}>
                    {format(productOrderTotal ?? 0)}
                  </Typography>
                ))}
            </Box>
          </Grid>
          {isComment ? (
            <Grid item container xs={9} alignItems="center" />
          ) : (
            <>
              <LineItemSubGrid>
                <LineItemText data-testid="order-price">
                  {loading ? (
                    <Skeleton width={50} />
                  ) : (
                    `${format(unitPrice ?? 0)}`
                  )}
                </LineItemText>
              </LineItemSubGrid>
              <LineItemSubGrid>
                <LineItemText data-testid="order-quantity">
                  {loading ? (
                    <Skeleton width={30} />
                  ) : orderQuantity !== null ? (
                    `${orderQuantity} ${getUOM(isMincron, lineItem, t)}`
                  ) : (
                    0
                  )}
                </LineItemText>
              </LineItemSubGrid>
              <LineItemSubGrid>
                <LineItemText data-testid="order-ship-quantity">
                  {loading ? (
                    <Skeleton width={30} />
                  ) : shipQuantity !== null ? (
                    `${shipQuantity} ${getUOM(isMincron, lineItem, t)}`
                  ) : (
                    0
                  )}
                </LineItemText>
              </LineItemSubGrid>
            </>
          )}
          {!isComment && (
            <LineItemSubGrid>
              <LineItemText data-testid="order-price-total">
                {loading ? (
                  <Skeleton width={50} />
                ) : (
                  format(productOrderTotal ?? 0)
                )}
              </LineItemText>
            </LineItemSubGrid>
          )}
          <Grid
            item
            xs={12}
            pb={2}
            mb={2}
            flex={1}
            display="flex"
            flexDirection="column"
            pt={2}
          >
            {loading ? (
              <Skeleton variant="rectangular" height={24} />
            ) : (
              !isComment && (
                <Button
                  fullWidth
                  size="small"
                  disabled={cartLoading || !props.pricingData?.sellPrice}
                  onClick={handleReorderButtonClick}
                  data-testid="reorder-button"
                >
                  {cartLoading && (
                    <Box display="inline-block" mr={1}>
                      <CircularProgress size={theme.spacing(2)} />
                    </Box>
                  )}
                  {t('orders.reorder')}
                </Button>
              )
            )}
            {!isMincron && (
              <Box pb={1.5} mt={2}>
                {loading ? (
                  <Skeleton width={100} height={24} />
                ) : (
                  authState?.isAuthenticated && (
                    <AddToListButton
                      availableInList={availableInList}
                      updatedAddedToLists={setAvailableInList}
                      partNumber={erpPartNumber ?? ''}
                      quantity={orderQuantity ?? 0}
                    />
                  )
                )}
              </Box>
            )}
          </Grid>
        </LineItemGridPricing>
      </LineItemContainer>
      {notAvailable && !isComment && (
        <Grid item xs={12}>
          <ItemUnavailable />
        </Grid>
      )}
    </Box>
  );
}
