import { Fragment, useMemo } from 'react';

import {
  Box,
  Card,
  Divider,
  Grid,
  Skeleton,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import LineItem from 'Order/LineItem';
import { Order, ProductPricing } from 'generated/graphql';
import Link from 'common/Link';

type Props = {
  loading?: boolean;
  order?: Order;
  isMincron?: boolean;
  pricingData: ProductPricing[];
};

function ProductsCard(props: Props) {
  /**
   * Custom hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * Memos
   */
  const orderedPricing = useMemo(OrderedPricingMemo, [
    props.order?.lineItems,
    props.pricingData
  ]);

  return (
    <Card
      sx={{
        height: 1,
        '& .MuiTypography-subtitle2': {
          fontWeight: 400,
          fontSize: (theme) => theme.spacing(1.5)
        }
      }}
    >
      <Box px={isSmallScreen ? 3 : 4} py={3}>
        {isSmallScreen ? (
          <Typography
            color="primary"
            variant="subtitle2"
            data-testid="items-ordered"
          >
            {props.loading ? (
              <Skeleton width={200} />
            ) : (
              `(${props?.order?.lineItems?.length}) items ordered`
            )}
          </Typography>
        ) : (
          <Grid container spacing={2}>
            <Grid item className="printGridWidth_15">
              <Box
                className="printGridWidth_90"
                width={80}
                display="flex"
                alignItems="center"
                justifyContent="center"
              >
                <Typography color="primary" variant="subtitle2" align="center">
                  {t('common.product')}
                </Typography>
              </Box>
            </Grid>
            <Grid className="printGridWidth_25" item md={3} xs />
            <Grid
              className="printGridWidth_60 printGridBlock"
              item
              container
              md
            >
              <Grid item xs className="printGridWidth_25_inline">
                <Typography color="primary" variant="subtitle2" align="center">
                  {t('common.price')}
                </Typography>
              </Grid>
              <Grid item xs className="printGridWidth_25_inline">
                <Typography color="primary" variant="subtitle2" align="center">
                  {t('orders.orderQuantity')}
                </Typography>
              </Grid>
              <Grid item xs className="printGridWidth_25_inline">
                <Typography color="primary" variant="subtitle2" align="center">
                  {t('orders.shipQuantity')}
                </Typography>
              </Grid>
              <Grid item xs className="printGridWidth_25_inline">
                <Typography color="primary" variant="subtitle2" align="center">
                  {t('orders.orderTotal')}
                </Typography>
              </Grid>
              <Grid item xs></Grid>
            </Grid>
          </Grid>
        )}
      </Box>
      <Divider />
      <Box px={isSmallScreen ? 3 : 2}>
        {props.loading ? (
          <LineItem loading={true} />
        ) : props?.order?.lineItems?.length ? (
          props?.order?.lineItems?.map((p, idx) => (
            <Fragment key={p.productId ?? idx}>
              <LineItem
                loading={false}
                lineItem={p}
                isMincron={props.isMincron}
                uom={orderedPricing[idx]?.orderUom ?? t('product.each')}
                listIds={orderedPricing[idx]?.listIds ?? []}
                pricingData={orderedPricing[idx]}
              />
              {idx + 1 < props!.order!.lineItems!.length && <Divider />}
            </Fragment>
          ))
        ) : (
          <Box px={3} py={8}>
            <Typography
              variant="h4"
              textAlign="center"
              color="secondary03.main"
            >
              {t('orders.noItemsWaterworks')}
            </Typography>
            <Typography
              variant="h5"
              textAlign="center"
              color="secondary03.main"
            >
              {t('orders.noOrdersContactMessage')}
              <Link to="/support" underline="always">
                {t('orders.noOrdersContactBranch')}
              </Link>
            </Typography>
          </Box>
        )}
      </Box>
    </Card>
  );

  function OrderedPricingMemo() {
    if (props.order?.lineItems) {
      const orderedPricing = props.order.lineItems.map((item) => {
        return props.pricingData.find(
          (price) => price.productId === item?.erpPartNumber
        );
      });

      return orderedPricing;
    }
    return [];
  }
}

export default ProductsCard;
