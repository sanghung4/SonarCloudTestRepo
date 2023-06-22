import { useContext } from 'react';

import {
  Box,
  Button,
  Divider,
  Grid,
  Hidden,
  Image,
  Skeleton,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import QtyInput from 'common/QtyInput';
import { ContractContext } from 'Contract/ContractProvider';
import { ContractProduct } from 'generated/graphql';
import notfound from 'images/notfound.png';
import { format } from 'utils/currency';
import { formatNonNegativeFieldsData } from 'utils/formatNonNegativeFields';

type Props = {
  sequence: string;
  data?: ContractProduct;
  loading: boolean;
};

export default function ContractLineItem(props: Props) {
  /**
   * Custom hooks
   */
  const { t } = useTranslation();
  const { isSmallScreen } = useScreenSize();

  /**
   * Context
   */
  const { qtyInputMap, setModal, setQtyInputMap } = useContext(ContractContext);

  /**
   * Output
   */
  return (
    <Box
      data-testid={`contract-line-item-container-${props.data?.sequenceNumber}`}
    >
      <Hidden mdUp>
        <Box mx={1} mt={1}>
          {props.loading ? (
            <Skeleton width={24} height={24} />
          ) : (
            <Typography
              color="mediumGray.main"
              fontWeight={500}
              lineHeight="16px"
              component="div"
              data-testid={`sequence-number-${props.data?.id}`}
            >
              {props.sequence}
            </Typography>
          )}
        </Box>
      </Hidden>
      <Grid
        container
        alignItems="center"
        flexDirection="row"
        columnSpacing={2}
        px={isSmallScreen ? 1 : 4}
        py={isSmallScreen ? 0.5 : 1}
        height={isSmallScreen ? 246 : 118}
        id={`row_${props.data?.id}`}
        data-testid={`row_${props.data?.id}`}
      >
        <Grid item xs={isSmallScreen ? 3 : 2}>
          <Grid container columns={4} alignItems="center">
            <Hidden mdDown>
              <Grid item xs={1}>
                {props.loading ? (
                  <Skeleton width={24} height={isSmallScreen ? 20 : 24} />
                ) : (
                  <Typography
                    color="mediumGray.main"
                    fontWeight={500}
                    component="div"
                    data-testid="sequence-number"
                  >
                    {props.sequence}
                  </Typography>
                )}
              </Grid>
            </Hidden>
            <Grid
              item
              container
              justifyContent="center"
              alignItems="center"
              xs={isSmallScreen ? 4 : 3}
              height={92}
            >
              {props.loading ? (
                <Skeleton height="100%" variant="rectangular" />
              ) : (
                <Image
                  alt={t('common.productPicture')}
                  fallback={notfound}
                  src={props.data?.thumb ?? notfound}
                  data-testid="product-picture"
                />
              )}
            </Grid>
          </Grid>
        </Grid>
        <Grid item xs={isSmallScreen ? 9 : 4}>
          {props.loading ? (
            <>
              <Skeleton height={24} />
              <Skeleton height={32} />
            </>
          ) : (
            <>
              <Typography
                fontSize={12}
                color="mediumGray.main"
                component="div"
                noWrap
                data-testid="mfr-name"
              >
                {props.data?.brand}
              </Typography>
              <Typography
                color="primary.main"
                component="div"
                noWrap
                data-testid="product-name"
              >
                {props.data?.name}
              </Typography>
            </>
          )}
          <Grid container columnSpacing={2} flexWrap="wrap">
            {/* ---------- Part Number ---------- */}
            <Grid item xs="auto">
              {props.data?.partNumber ? (
                <Typography
                  fontSize={12}
                  color="mediumGray.main"
                  component="div"
                >
                  {t('contract.partNum')}{' '}
                  <Typography
                    fontSize={12}
                    color="mediumGray.main"
                    component="div"
                    display="inline"
                    data-testid="part-number"
                  >
                    {props.data?.partNumber}
                  </Typography>
                </Typography>
              ) : null}
            </Grid>
            {/* ---------- MFR Number ---------- */}
            <Hidden mdDown>
              <Grid item xs="auto">
                {props.loading ? (
                  <Skeleton height={24} />
                ) : props.data?.mfr ? (
                  <Typography
                    fontSize={12}
                    color="mediumGray.main"
                    component="div"
                  >
                    {t('contract.mfrNum')}{' '}
                    <Typography
                      fontSize={12}
                      color="mediumGray.main"
                      component="div"
                      display="inline"
                      data-testid="mfr-number"
                    >
                      {' '}
                      {props.data?.mfr}
                    </Typography>
                  </Typography>
                ) : null}
              </Grid>
            </Hidden>
          </Grid>
          <Hidden mdDown>
            <Button
              variant="text"
              size="small"
              color="primaryLight"
              className="noprint"
              disabled={props.loading}
              sx={{ fontSize: 16, textDecoration: 'underline' }}
              onClick={handleOpenDetails}
              data-testid={`contract-line-item-button${props.sequence}`}
            >
              {t('contract.showDetails')}
            </Button>
          </Hidden>
        </Grid>
        <Grid item xs={isSmallScreen ? 12 : 6}>
          <Hidden mdUp>
            <Grid
              container
              columns={isSmallScreen ? 9 : 8}
              columnSpacing={0.5}
              alignItems="stretch"
              mt={1}
            >
              <Grid
                item
                container
                xs={2}
                flexDirection="column"
                justifyContent="flex-end"
              >
                <Typography
                  color={props.loading ? 'lightgray' : 'primary'}
                  fontSize={14}
                  lineHeight={1.2}
                  component="div"
                  align="center"
                >
                  {t('contract.price')}
                </Typography>
              </Grid>
              <Grid
                item
                container
                xs={2}
                flexDirection="column"
                justifyContent="flex-end"
              >
                <Typography
                  color={props.loading ? 'lightgray' : 'primary'}
                  fontSize={14}
                  lineHeight={1.2}
                  component="div"
                  align="center"
                >
                  {t('contract.contract')}
                </Typography>
              </Grid>
              <Grid
                item
                container
                xs={2}
                flexDirection="column"
                justifyContent="flex-end"
              >
                <Typography
                  color={props.loading ? 'lightgray' : 'primary'}
                  fontSize={14}
                  lineHeight={1.2}
                  component="div"
                  align="center"
                >
                  {t('contract.released')}
                </Typography>
              </Grid>
              <Grid
                item
                container
                xs={3}
                flexDirection="column"
                justifyContent="flex-end"
              >
                <Typography
                  color={props.loading ? 'lightgray' : 'primary'}
                  fontSize={14}
                  lineHeight={1.2}
                  component="div"
                  align="center"
                >
                  {t('contract.qtyToRelease')}
                </Typography>
              </Grid>
            </Grid>
          </Hidden>
          <Grid
            container
            columns={isSmallScreen ? 9 : 10}
            columnSpacing={isSmallScreen ? 0.5 : 2}
            alignItems="center"
          >
            <Grid item xs={2}>
              {props.loading ? (
                <Skeleton height={36} variant="rectangular" />
              ) : (
                <Typography
                  color="mediumGray.main"
                  component="div"
                  fontSize={isSmallScreen ? 14 : 16}
                  lineHeight={1.2}
                  align="center"
                  data-testid="product-price"
                >
                  {format(props.data?.netPrice ?? 0)}{' '}
                  {props.data?.pricingUom?.toLowerCase() || t('product.each')}
                </Typography>
              )}
            </Grid>
            <Grid item xs={2}>
              {props.loading ? (
                <Skeleton height={36} variant="rectangular" />
              ) : (
                <Typography
                  color="mediumGray.main"
                  component="div"
                  fontSize={isSmallScreen ? 14 : 16}
                  align="center"
                  lineHeight={1.2}
                  data-testid="contract-quantity"
                >
                  {formatNonNegativeFieldsData(
                    props.data?.qty?.quantityOrdered
                  )}
                </Typography>
              )}
            </Grid>
            <Grid item xs={2}>
              {props.loading ? (
                <Skeleton height={36} variant="rectangular" />
              ) : (
                <Typography
                  color="mediumGray.main"
                  fontSize={isSmallScreen ? 14 : 16}
                  component="div"
                  align="center"
                  lineHeight={1.2}
                  data-testid="released-quantity"
                >
                  {formatNonNegativeFieldsData(
                    props.data?.qty?.quantityReleasedToDate
                  )}
                </Typography>
              )}
            </Grid>
            <Hidden mdDown>
              <Grid item xs={2} width={24}>
                {props.loading ? (
                  <Skeleton height={36} variant="rectangular" />
                ) : (
                  <Typography
                    color="mediumGray.main"
                    fontSize={16}
                    component="div"
                    align="center"
                    lineHeight={1.2}
                    data-testid="shipped-to-date"
                  >
                    {formatNonNegativeFieldsData(
                      props.data?.qty?.quantityShipped
                    )}
                  </Typography>
                )}
              </Grid>
            </Hidden>
            <Grid item container xs={3} md={2} justifyContent="center">
              <Grid item width="100%" maxWidth={100}>
                {props.loading ? (
                  <Skeleton height={36} variant="rectangular" />
                ) : (
                  <QtyInput
                    onUpdate={handleQtyInputChange}
                    max={999999}
                    allowZero
                    noDebounce
                    value={parseInt(
                      qtyInputMap[props.sequence.toString()] || '0'
                    )}
                    fullWidth
                    size="small"
                    buttonContainerStyle={{ width: '1.25rem' }}
                    sync
                    data-testid={`contract-qty-input${props.sequence}`}
                  />
                )}
              </Grid>
            </Grid>
          </Grid>
        </Grid>
        <Hidden mdUp>
          <Button
            variant="text"
            color="primaryLight"
            disabled={props.loading}
            sx={{ fontSize: 16, textDecoration: 'underline' }}
            onClick={handleOpenDetails}
            data-testid="contract-line-item-button"
          >
            {t('contract.showDetails')}
          </Button>
        </Hidden>
      </Grid>
      <Box component={Divider} mx={isSmallScreen ? 0 : 2} />
    </Box>
  );

  /**
   * Event Handles
   */
  function handleOpenDetails() {
    setModal(props.data);
  }
  function handleQtyInputChange(value: number) {
    setQtyInputMap({ ...qtyInputMap, [`${props.sequence}`]: `${value}` });
  }
}
