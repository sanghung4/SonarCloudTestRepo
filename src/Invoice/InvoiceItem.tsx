import {
  Box,
  Grid,
  Hidden,
  Image,
  Link,
  Skeleton,
  Tooltip,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import notfound from 'images/notfound.png';
import { InvoiceProduct } from 'generated/graphql';
import { format } from 'utils/currency';
import Dotdotdot from 'react-dotdotdot';
import { Link as RouterLink } from 'react-router-dom';
import { useDomainInfo } from 'hooks/useDomainInfo';
import ConditionalWrapper from 'common/ConditionalWrapper';

type Props = {
  loading?: boolean;
  invoiceItem?: InvoiceProduct;
  isMincron?: boolean;
};

function InvoiceItem(props: Props) {
  /**
   * Props
   */
  const { loading, invoiceItem, isMincron } = props;

  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { isWaterworks } = useDomainInfo();
  const { t } = useTranslation();

  /**
   * Output
   */
  return (
    <Grid
      container
      spacing={2}
      px={isSmallScreen ? 0 : 2}
      py={2}
      data-testid={`row_${invoiceItem?.id}`}
      sx={{ breakInside: 'avoid' }}
    >
      <Grid item>
        <Box
          height={80}
          width={80}
          display="flex"
          alignItems="center"
          justifyContent="center"
        >
          {loading ? (
            <Skeleton variant="rectangular" height={80} width={80} />
          ) : (
            <Image
              alt={invoiceItem?.name ?? 'Product Picture'}
              fallback={notfound}
              src={invoiceItem?.thumb ?? t('common.productPicture')}
              data-testid="product-image"
            />
          )}
        </Box>
      </Grid>
      <Grid item container direction="column" md={3} xs>
        <Typography variant="subtitle2" data-testid="mfr-name">
          {loading ? <Skeleton width={80} /> : invoiceItem?.brand ?? ''}
        </Typography>
        <Typography color="primary" component="div">
          {loading ? (
            <Skeleton />
          ) : (
            <Typography component="div" gutterBottom data-testid="product-name">
              <ConditionalWrapper
                wrapper={(children) => (
                  <Link
                    to={`/product/${invoiceItem?.id}`}
                    component={RouterLink}
                    data-testid="product-link"
                  >
                    {children}
                  </Link>
                )}
                condition={!isWaterworks && !isMincron}
              >
                <Dotdotdot clamp={3}>
                  <Tooltip title={invoiceItem?.name ?? ''}>
                    <Typography>{invoiceItem?.name}</Typography>
                  </Tooltip>
                </Dotdotdot>
              </ConditionalWrapper>
            </Typography>
          )}
        </Typography>
        {loading ? (
          <Typography variant="subtitle2">
            <Skeleton width={120} />
          </Typography>
        ) : (
          <Box>
            <Typography variant="subtitle2" data-testid="part-number">
              {invoiceItem?.partNumber
                ? `Part# ${invoiceItem?.partNumber}`
                : ''}
            </Typography>
            <Typography variant="subtitle2" data-testid="mfr-number">
              {invoiceItem?.mfr ? `MFR# ${invoiceItem?.mfr}` : ''}
            </Typography>
          </Box>
        )}
      </Grid>
      <Grid container item md>
        <Hidden mdUp>
          <Grid item xs={3}>
            <Typography color="primary" variant="subtitle2" align="center">
              {t('common.price')}
            </Typography>
          </Grid>
          <Grid item xs={3}>
            <Typography color="primary" variant="subtitle2" align="center">
              {t('invoice.orderQuantity')}
            </Typography>
          </Grid>
          <Grid item xs={3}>
            <Typography color="primary" variant="subtitle2" align="center">
              {t('invoice.shipQuantity')}
            </Typography>
          </Grid>

          <Grid item xs={3}>
            <Box
              display="flex"
              flexDirection="column"
              height={1}
              justifyContent={'flex-start'}
            >
              <Typography color="primary" variant="subtitle2" align="center">
                {t('invoice.orderTotal')}
              </Typography>
            </Box>
          </Grid>
        </Hidden>

        <Grid item md xs={3}>
          <Typography
            variant="subtitle2"
            align="center"
            display="flex"
            justifyContent="center"
            pt={isSmallScreen ? 2 : 2.75}
            data-testid="order-price"
          >
            {loading ? (
              <Skeleton width={50} />
            ) : (
              `${format(parseFloat(invoiceItem?.price ?? '0'))} ${
                invoiceItem?.pricingUom?.toLowerCase() || t('product.each')
              }`
            )}
          </Typography>
        </Grid>
        <Grid item md xs={3}>
          <Typography
            variant="subtitle2"
            align="center"
            display="flex"
            justifyContent="center"
            pt={isSmallScreen ? 2 : 2.75}
            data-testid="order-quantity"
          >
            {loading ? (
              <Skeleton width={30} />
            ) : (
              invoiceItem?.qty?.quantityOrdered ?? '-'
            )}
          </Typography>
        </Grid>
        <Grid item md xs={3}>
          <Typography
            variant="subtitle2"
            align="center"
            display="flex"
            justifyContent="center"
            pt={isSmallScreen ? 2 : 2.75}
            data-testid="order-ship-quantity"
          >
            {loading ? (
              <Skeleton width={30} />
            ) : (
              invoiceItem?.qty?.quantityShipped ?? '-'
            )}
          </Typography>
        </Grid>

        <Grid item md xs={3}>
          <Typography
            variant="subtitle2"
            align="center"
            display="flex"
            justifyContent="center"
            pt={isSmallScreen ? 2 : 2.75}
            pl={0}
            data-testid="order-price-total"
          >
            {loading ? (
              <Skeleton width={50} />
            ) : (
              format(
                (invoiceItem?.qty?.quantityOrdered ?? 0) *
                  parseFloat(invoiceItem?.price ?? '0')
              )
            )}
          </Typography>
        </Grid>
      </Grid>
    </Grid>
  );
}

export default InvoiceItem;
