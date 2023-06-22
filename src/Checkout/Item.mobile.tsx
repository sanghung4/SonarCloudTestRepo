import {
  Box,
  Divider,
  Grid,
  Image,
  Typography
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { OrderLineItem } from 'generated/graphql';
import { WarningIcon } from 'icons';
import notfound from 'images/notfound.png';
import { format } from 'utils/currency';

type Props = {
  lineItem: OrderLineItem;
  qtyAvailable: number;
  index?: number;
};

export default function ItemMobile(props: Props) {
  /**
   * Props
   */
  const orderQuantity = props.lineItem.orderQuantity ?? 0;
  const unitPrice = props.lineItem.unitPrice ?? 0;

  /**
   * Custom Hooks
   */
  const { t } = useTranslation();

  /**
   * Render
   */
  return (
    <>
      <Box
        mx={1}
        px={1}
        py={3}
        position="relative"
        data-testid={`checkout-cart-item-${props.lineItem.productId}`}
      >
        <Box mb={3}>
          <Grid container alignItems="center">
            <Grid item xs={6}>
              <Box
                height={72}
                width={72}
                display="flex"
                justifyContent="center"
                alignItems="center"
              >
                <Image
                  fallback={notfound}
                  alt={props.lineItem.productName ?? t('common.productPicture')}
                  src={props.lineItem.imageUrls?.medium ?? ''}
                />
              </Box>
            </Grid>
            <Grid
              container
              item
              xs={6}
              justifyContent="flex-end"
              alignItems="center"
            >
              <Typography
                variant="body1"
                component="span"
                color="textSecondary"
                data-testid={`checkout-qty-available-${props.index}`}
              >
                {props.qtyAvailable}
              </Typography>
              {!!props.qtyAvailable && (
                <Typography
                  color="success.main"
                  display="inline-block"
                  ml={1.25}
                >
                  {t('common.inStock')}
                </Typography>
              )}
            </Grid>
          </Grid>
        </Box>
        <Grid container>
          <Grid item xs={6}>
            <Typography
              variant="caption"
              color="textSecondary"
              data-testid={`checkout-manufacturer-name-${props.index}`}
            >
              {props.lineItem.manufacturerName}
            </Typography>
            <Typography
              variant="body1"
              color="primary"
              py={0.5}
              data-testid={`checkout-product-name-${props.index}`}
            >
              {props.lineItem.productName}
            </Typography>
            <Typography
              variant="caption"
              color="textSecondary"
              data-testid={`checkout-manufacturer-number-${props.index}`}
            >
              {t('product.mfr')} {props.lineItem.manufacturerNumber}
            </Typography>
          </Grid>
          <Grid container item xs={6} justifyContent="flex-end">
            <Box mt={3}>
              <Box display="flex" justifyContent="flex-end" alignItems="center">
                <Typography variant="caption" component="span" color="primary">
                  {t('cart.orderQty')}
                </Typography>
                <Box ml={2}>
                  <Typography
                    variant="h5"
                    component="span"
                    color="primary"
                    data-testid={`checkout-item-order-qty-${props.index}`}
                  >
                    {orderQuantity}
                  </Typography>
                </Box>
              </Box>
            </Box>
          </Grid>
        </Grid>
        <Grid container pt={4}>
          <Grid item xs={6}>
            <Box pb={2}>
              <Typography variant="caption" color="primary">
                {t('common.price')}
              </Typography>
            </Box>
            <Typography
              variant="h5"
              color="textSecondary"
              fontWeight={400}
              data-testid={`checkout-unit-price-${props.index}`}
            >
              {format(unitPrice)}{' '}
              {props.lineItem?.pricingUom?.toLowerCase() || t('product.each')}
            </Typography>
          </Grid>
          <Grid container item xs={6} direction="column" alignItems="flex-end">
            <Box pb={2}>
              <Typography variant="caption" color="primary">
                {t('cart.orderTotal')}
              </Typography>
            </Box>
            <Typography
              variant="h5"
              color="textSecondary"
              fontWeight={400}
              data-testid={`checkout-item-total-price-${props.index}`}
            >
              {format(orderQuantity * unitPrice)}
            </Typography>
          </Grid>
        </Grid>
        {(!props.qtyAvailable || props.qtyAvailable < orderQuantity) && (
          <Box
            m={2}
            mt={5}
            display="flex"
            alignItems="center"
            color="primary02.main"
          >
            <Box component={WarningIcon} mr={2} />
            <Typography variant="caption" color="inherit">
              {t('cart.outOfStock')}
            </Typography>
          </Box>
        )}
      </Box>
      <Box mx={1}>
        <Divider />
      </Box>
    </>
  );
}
