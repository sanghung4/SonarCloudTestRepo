import { Fragment } from 'react';

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
import { Invoice } from 'generated/graphql';
import InvoiceItem from './InvoiceItem';
import Link from 'common/Link';

type Props = {
  loading?: boolean;
  invoice?: Invoice;
  isMincron?: boolean;
};

function ProductsCard(props: Props) {
  /**
   * Custom hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

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
              `(${props?.invoice?.invoiceItems?.length}) items ordered`
            )}
          </Typography>
        ) : (
          <Grid container spacing={2}>
            <Grid item>
              <Box
                width={80}
                display="flex"
                alignItems="center"
                justifyContent="center"
              >
                <Typography color="primary" variant="subtitle1" align="center">
                  {t('common.product')}
                </Typography>
              </Box>
            </Grid>
            <Grid item md={3} xs />
            <Grid item container md>
              <Grid item xs>
                <Typography color="primary" variant="subtitle1" align="center">
                  {t('common.price')}
                </Typography>
              </Grid>
              <Grid item xs>
                <Typography color="primary" variant="subtitle1" align="center">
                  {t('invoice.orderQuantity')}
                </Typography>
              </Grid>
              <Grid item xs>
                <Typography color="primary" variant="subtitle1" align="center">
                  {t('invoice.shipQuantity')}
                </Typography>
              </Grid>
              <Grid item xs>
                <Typography color="primary" variant="subtitle1" align="center">
                  {t('invoice.orderTotal')}
                </Typography>
              </Grid>
            </Grid>
          </Grid>
        )}
      </Box>
      <Divider />
      <Box px={isSmallScreen ? 3 : 2}>
        {props.loading ? (
          <LineItem loading={true} />
        ) : props?.invoice?.invoiceItems?.length ? (
          props?.invoice?.invoiceItems?.map((p, idx) => (
            <Fragment key={p.id ?? idx}>
              <InvoiceItem invoiceItem={p} isMincron={props.isMincron} />
              {idx + 1 < props!.invoice!.invoiceItems!.length && <Divider />}
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
}

export default ProductsCard;
