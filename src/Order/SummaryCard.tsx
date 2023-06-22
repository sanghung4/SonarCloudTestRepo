import { useCallback, useMemo } from 'react';

import {
  Box,
  Button,
  Card,
  CircularProgress,
  Grid,
  Divider,
  Paper,
  Skeleton,
  Typography,
  alpha,
  useSnackbar,
  useTheme
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { Order, ProductPricing } from 'generated/graphql';
import addItemsToCartCb from 'Order/utils/addItemsToCartCb';
import { format } from 'utils/currency';
import { useCartContext } from 'providers/CartProvider';

type Props = {
  loading?: boolean;
  order?: Order;
  pricingData: ProductPricing[];
};

function SummaryCard(props: Props) {
  /**
   * Custom hooks
   */
  const { t } = useTranslation();
  const theme = useTheme();
  const { pushAlert } = useSnackbar();

  /**
   * Context
   */
  const { addItemsToCart, cartLoading } = useCartContext();

  /**
   * Memo
   */
  const hasItemsAvailableForReorder = useMemo(hasItemsAvailableForReorderMemo, [
    props
  ]);

  /**
   * Callbacks
   */
  const handleAddItemsToCart = useCallback(handleAddItemsToCartCb, [
    addItemsToCart,
    props.order,
    pushAlert,
    props.pricingData
  ]);

  return (
    <Card sx={{ px: 2, py: 3 }}>
      <Box pb={3} px={3}>
        <Typography color="primary" variant="h5">
          {t('cart.orderSummary')}
        </Typography>
      </Box>
      <Paper
        elevation={0}
        sx={{
          px: 2,
          py: 3,
          bgcolor: (theme) => alpha(theme.palette.primary02.main, 0.05)
        }}
      >
        <Grid container px={1}>
          <Grid item xs={6}>
            <Typography color="primary" fontWeight={500}>
              {t('cart.subtotal')}
            </Typography>
          </Grid>
          <Grid item xs={6}>
            <Typography
              color="primary"
              align="right"
              data-testid="order-summary-subtotal"
            >
              {props.loading ? (
                <Box display="flex" justifyContent="flex-end" component="span">
                  <Skeleton width={80} />
                </Box>
              ) : (
                format(props.order?.subTotal ?? 0)
              )}
            </Typography>
          </Grid>
          <Grid item xs={6}>
            <Typography color="primary">{t('common.tax')}</Typography>
          </Grid>
          <Grid item xs={6}>
            <Typography
              color="primary"
              align="right"
              data-testid="order-summary-tax"
            >
              {props.loading ? (
                <Box display="flex" justifyContent="flex-end" component="span">
                  <Skeleton width={80} />
                </Box>
              ) : (
                format(props.order?.tax ?? 0)
              )}
            </Typography>
          </Grid>
        </Grid>
        <Box my={2}>
          <Divider />
        </Box>
        <Grid container spacing={2} px={1}>
          <Grid item xs={6}>
            <Typography color="primary" fontWeight={500}>
              {t('orders.orderTotal')}
            </Typography>
          </Grid>
          <Grid item xs={6}>
            <Typography
              color="primary"
              align="right"
              display="flex"
              justifyContent="flex-end"
              data-testid="order-summary-order-total"
            >
              {props.loading ? (
                <Skeleton width={80} />
              ) : (
                format(
                  props.order?.orderTotal ??
                    (props.order?.subTotal ?? 0) + (props.order?.tax ?? 0)
                )
              )}
            </Typography>
          </Grid>
          <Grid item xs={12}>
            <Button
              disabled={
                cartLoading || props.loading || !hasItemsAvailableForReorder
              }
              variant="primary"
              size="small"
              fullWidth
              onClick={handleAddItemsToCart}
              data-testid="add-all-to-cart-button"
            >
              {(cartLoading || props.loading) && (
                <Box display="inline-block" mr={1}>
                  <CircularProgress size={theme.spacing(2)} />
                </Box>
              )}
              {t('orders.addAll')}
            </Button>
          </Grid>
        </Grid>
      </Paper>
    </Card>
  );

  function handleAddItemsToCartCb() {
    addItemsToCartCb({
      order: props.order,
      pushAlert,
      addItemsToCart,
      pricingData: props.pricingData
    });
  }

  function hasItemsAvailableForReorderMemo() {
    if (props.order?.lineItems?.length) {
      const items = props.order.lineItems.filter((i) => {
        const pricing = props.pricingData.filter(
          (price) => price.productId === i.erpPartNumber
        );
        return !!pricing?.[0]?.sellPrice;
      });
      return items.length > 0;
    }
  }
}

export default SummaryCard;
