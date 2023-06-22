import React, { Fragment, useMemo } from 'react';

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

import LineItemDesktop from 'Quote/LineItem';
import { ProductPricing, Quote } from 'generated/graphql';
import { ItemsOrderedTypography } from './styles';

type Props = {
  loading?: boolean;
  quote?: Quote;
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
  const orderedPricing = useMemo(() => {
    return (
      props.quote?.lineItems?.map((item) => {
        return props.pricingData?.find(
          (price) => price.productId === item?.erpPartNumber
        );
      }) ?? []
    );
  }, [props.quote?.lineItems, props.pricingData]);

  return (
    <Card sx={{ height: 1 }}>
      <Box px={isSmallScreen ? 3 : 4} py={3}>
        {isSmallScreen ? (
          <ItemsOrderedTypography color="primary" variant="subtitle2">
            {props.loading ? (
              <Skeleton width={200} />
            ) : (
              `(${props?.quote?.lineItems?.length}) items ordered`
            )}
          </ItemsOrderedTypography>
        ) : (
          <Grid container spacing={2}>
            <Grid item>
              <Box
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
            <Grid item md={3} xs />
            <Grid item container md>
              <Grid item xs>
                <Typography color="primary" variant="subtitle2" align="center">
                  {t('common.price')}
                </Typography>
              </Grid>
              <Grid item xs>
                <Typography color="primary" variant="subtitle2" align="center">
                  {t('orders.orderQuantity')}
                </Typography>
              </Grid>
              <Grid item xs>
                <Typography color="primary" variant="subtitle2" align="center">
                  {t('orders.shipQuantity')}
                </Typography>
              </Grid>
              <Grid item xs>
                <Typography color="primary" variant="subtitle2" align="center">
                  {t('orders.orderTotal')}
                </Typography>
              </Grid>
            </Grid>
          </Grid>
        )}
      </Box>
      <Divider />
      <Box px={isSmallScreen ? 3 : 2}>
        {props.loading ? (
          <LineItemDesktop loading={true} />
        ) : (
          props.quote?.lineItems?.map((q, idx) => (
            <Fragment key={q.productId ?? idx}>
              <LineItemDesktop
                lineItem={q}
                uom={orderedPricing[idx]?.orderUom}
              />
              {idx + 1 < (props?.quote?.lineItems?.length ?? 0) ? (
                <Divider />
              ) : null}
            </Fragment>
          ))
        )}
      </Box>
    </Card>
  );
}

export default ProductsCard;
