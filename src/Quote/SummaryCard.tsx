import {
  Box,
  Button,
  Card,
  Grid,
  Divider,
  Paper,
  Skeleton,
  Typography,
  alpha
} from '@dialexa/reece-component-library';
import { useHistory } from 'react-router-dom';
import { useTranslation } from 'react-i18next';

import { format } from 'utils/currency';
import { AddressInput, DeliveryMethodEnum, Quote } from 'generated/graphql';
import { checkStatus } from 'utils/statusMapping';
import { useCartContext } from 'providers/CartProvider';

type Props = {
  loading?: boolean;
  quote?: Quote;
  onReject: () => void;
};

function SummaryCard(props: Props) {
  /**
   * Custom hooks
   */
  const { t } = useTranslation();
  const history = useHistory();

  /**
   * Context
   */
  const {
    cart,
    previousCart,
    setQuoteId,
    setQuoteShipToId,
    setQuoteData,
    updateCartFromQuote
  } = useCartContext();

  /**
   * Callbacks
   */
  const approveOrder = () => {
    if (props.quote) {
      setQuoteId(props.quote.orderNumber);
      setQuoteShipToId(props.quote.shipToId ?? '');
      setQuoteData(props.quote);

      // Pass the address and delivery method to cart
      const isDelivery =
        checkStatus(props.quote?.deliveryMethod ?? '') === 'delivery';
      updateCartFromQuote?.({
        deliveryMethod: isDelivery
          ? DeliveryMethodEnum.Delivery
          : DeliveryMethodEnum.Willcall,
        delivery: {
          ...cart?.delivery,
          cartId: cart?.id ?? '',
          shouldShipFullOrder: !!cart?.delivery?.shouldShipFullOrder,
          address: isDelivery
            ? {
                custom: true,
                companyName: props.quote?.shipToName ?? '',
                street1: props.quote?.shipAddress?.streetLineOne ?? '',
                street2: props.quote?.shipAddress?.streetLineTwo ?? '',
                city: props.quote?.shipAddress?.city ?? '',
                state: props.quote?.shipAddress?.state ?? '',
                zip: props.quote?.shipAddress?.postalCode ?? '',
                country: props.quote?.shipAddress?.country ?? ''
              }
            : ((previousCart?.delivery?.address ?? cart?.delivery?.address) as
                | AddressInput
                | undefined)
        }
      });
      history.push('/checkout');
    }
  };

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
        <Grid container sx={{ px: 1 }}>
          <Grid item xs={6}>
            <Typography color="primary" sx={{ fontWeight: 500 }}>
              {t('cart.subtotal')}
            </Typography>
          </Grid>
          <Grid item xs={6}>
            <Typography
              data-testid="quote-sub-total"
              color="primary"
              align="right"
            >
              {props.loading ? (
                <Box display="flex" justifyContent="flex-end" component="span">
                  <Skeleton width={80} />
                </Box>
              ) : (
                format(props?.quote?.subTotal ?? 0)
              )}
            </Typography>
          </Grid>
          <Grid item xs={6}>
            <Typography color="primary">{t('common.tax')}</Typography>
          </Grid>
          <Grid item xs={6}>
            <Typography color="primary" align="right">
              {props.loading ? (
                <Box display="flex" justifyContent="flex-end" component="span">
                  <Skeleton width={80} />
                </Box>
              ) : (
                format(props?.quote?.tax ?? 0)
              )}
            </Typography>
          </Grid>
        </Grid>
        <Box my={2}>
          <Divider />
        </Box>
        <Grid container spacing={2} sx={{ px: 1 }}>
          <Grid item xs={6}>
            <Typography color="primary" sx={{ fontWeight: 500 }}>
              {t('orders.orderTotal')}
            </Typography>
          </Grid>
          <Grid item xs={6}>
            <Typography color="primary" align="right">
              {props.loading ? (
                <Box display="flex" justifyContent="flex-end" component="span">
                  <Skeleton width={80} />
                </Box>
              ) : (
                format(props?.quote?.orderTotal ?? 0)
              )}
            </Typography>
          </Grid>
        </Grid>
      </Paper>
      <Box pt={2}>
        <Button
          fullWidth
          onClick={approveOrder}
          data-testid="approve-quote-button"
        >
          {t('quotes.approveQuote')}
        </Button>
      </Box>
      <Box pt={2}>
        <Button
          fullWidth
          variant="inline"
          color="primaryLight"
          onClick={props.onReject}
          data-testid="decline-quote-button"
        >
          {t('quotes.rejectQuote')}
        </Button>
      </Box>
    </Card>
  );
}

export default SummaryCard;
