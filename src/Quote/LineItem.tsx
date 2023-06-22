import React from 'react';

import {
  Box,
  Grid,
  Hidden,
  Image,
  Skeleton,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import notfound from 'images/notfound.png';
import { QuoteLineItem } from 'generated/graphql';
import { format } from 'utils/currency';

type Props = {
  loading?: boolean;
  lineItem?: QuoteLineItem;
  uom?: string;
};

function LineItemDesktop(props: Props) {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();
  const { isSmallScreen } = useScreenSize();

  return (
    <Grid container spacing={2} sx={{ px: isSmallScreen ? 0 : 2, py: 2 }}>
      <Grid item>
        <Box
          height={80}
          width={80}
          display="flex"
          alignItems="center"
          justifyContent="center"
        >
          {props.loading ? (
            <Skeleton variant="rectangular" height={80} width={80} />
          ) : (
            <Image
              alt={props?.lineItem?.productName ?? t('common.productPicture')}
              fallback={notfound}
              src={
                props?.lineItem?.imageUrls?.thumb ?? t('common.productPicture')
              }
            />
          )}
        </Box>
      </Grid>
      <Grid item container direction="column" md={3} xs>
        <Typography variant="subtitle2">
          {props.loading ? (
            <Skeleton width={80} />
          ) : (
            props?.lineItem?.manufacturerName ?? '-'
          )}
        </Typography>
        <Typography color="primary" component="div">
          {props.loading ? <Skeleton /> : props?.lineItem?.productName}
        </Typography>
        <Typography variant="subtitle2">
          {props.loading ? (
            <Skeleton width={120} />
          ) : (
            `${t('product.mfr')} ${props?.lineItem?.manufacturerNumber ?? '-'}`
          )}
        </Typography>
      </Grid>

      <Grid item container xs={isSmallScreen ? 12 : true}>
        <Hidden mdUp>
          <Grid item xs={3}>
            <Typography color="primary" variant="subtitle2" align="center">
              {t('common.price')}
            </Typography>
          </Grid>
          <Grid item xs={3}>
            <Typography color="primary" variant="subtitle2" align="center">
              {t('orders.orderQuantity')}
            </Typography>
          </Grid>
          <Grid item xs={3}>
            <Typography color="primary" variant="subtitle2" align="center">
              {t('orders.shipQuantity')}
            </Typography>
          </Grid>
          <Grid item xs={3}>
            <Box display="flex" flexDirection="column" height={1}>
              <Typography color="primary" variant="subtitle2" align="center">
                {t('orders.orderTotal')}
              </Typography>
            </Box>
          </Grid>
        </Hidden>
        <Grid item md xs={3}>
          <Typography
            variant="subtitle2"
            align="center"
            sx={{
              pt: isSmallScreen ? 2 : 2.75,
              display: 'flex',
              justifyContent: 'center'
            }}
          >
            {props.loading ? (
              <Skeleton width={50} />
            ) : (
              `${format(props?.lineItem?.unitPrice ?? 0)} ${
                props?.uom ?? t('product.each')
              }`
            )}
          </Typography>
        </Grid>
        <Grid item md xs={3}>
          <Typography
            variant="subtitle2"
            align="center"
            sx={{
              pt: isSmallScreen ? 2 : 2.75,
              display: 'flex',
              justifyContent: 'center'
            }}
          >
            {props.loading ? (
              <Skeleton width={30} />
            ) : (
              props?.lineItem?.orderQuantity ?? '-'
            )}
          </Typography>
        </Grid>
        <Grid item md xs={3}>
          <Typography
            variant="subtitle2"
            align="center"
            sx={{
              pt: isSmallScreen ? 2 : 2.75,
              display: 'flex',
              justifyContent: 'center'
            }}
          >
            {props.loading ? (
              <Skeleton width={30} />
            ) : (
              props?.lineItem?.shipQuantity ?? '-'
            )}
          </Typography>
        </Grid>
        <Grid item md xs={3}>
          <Typography
            variant="subtitle2"
            align="center"
            sx={{
              pt: isSmallScreen ? 2 : 2.75,
              display: 'flex',
              justifyContent: 'center'
            }}
          >
            {props.loading ? (
              <Skeleton width={50} />
            ) : (
              format(props?.lineItem?.productOrderTotal ?? 0)
            )}
          </Typography>
        </Grid>
      </Grid>
    </Grid>
  );
}

export default LineItemDesktop;
