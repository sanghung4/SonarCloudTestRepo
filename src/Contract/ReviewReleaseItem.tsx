import { useContext } from 'react';

import {
  Box,
  Typography,
  Grid,
  useScreenSize,
  Image,
  Divider
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import QtyInput from 'common/QtyInput';
import { ContractContext } from 'Contract/ContractProvider';
import { ContractProduct } from 'generated/graphql';
import notfound from 'images/notfound.png';

type props = {
  data?: ContractProduct;
  loading?: boolean;
  sequence: string;
  idx: number;
};

export default function ReviewReleaseItem(props: props) {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * Context
   */
  const { qtyInputMap, setQtyInputMap } = useContext(ContractContext);

  /**
   * Render
   */
  return (
    <Box
      data-testid={`contract-line-item-container-${props.data?.sequenceNumber}`}
    >
      <Grid
        container
        alignItems="center"
        direction="row"
        px={1}
        py={isSmallScreen ? 0.5 : 1}
        id={`row_${props.data?.id}`}
        data-testid={`row_${props.data?.id}`}
        spacing={1}
      >
        <Grid item md={2} sm={2} xs={2}>
          <Grid container alignItems="flex-start" flexDirection="column">
            <Grid item xs={3}>
              <Typography
                color="mediumGray.main"
                fontSize={16}
                component="div"
                data-testid="sequence-number"
              >
                {props.sequence}
              </Typography>
            </Grid>
            <Grid
              item
              justifyContent="center"
              alignItems="center"
              height={60}
              width={60}
            >
              <Image
                alt={t('common.productPicture')}
                src={props.data?.thumb ?? ''}
                data-testid="product-picture"
                fallback={notfound}
              />
            </Grid>
          </Grid>
        </Grid>
        <Grid item md={7} sm={8} xs={7} flexWrap="nowrap">
          <Typography
            fontSize={13}
            color="mediumGray.main"
            component="div"
            data-testid="contract-product-brand"
          >
            {props.data?.brand}
          </Typography>
          <Typography
            color="primary.main"
            component="div"
            data-testid="contract-product-name"
            fontSize={16}
          >
            {props.data?.name}
          </Typography>
          <Grid container flexWrap="nowrap" columnSpacing={1}>
            <Grid item>
              {props.data?.partNumber ? (
                <Typography
                  fontSize={12}
                  color="mediumGray.main"
                  component="div"
                  data-testid="part-number"
                >
                  {`${t('contract.partNum')} ${props.data?.partNumber}`}
                </Typography>
              ) : null}
            </Grid>
            <Grid item>
              {props.data?.mfr ? (
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
                    {props.data?.mfr}
                  </Typography>
                </Typography>
              ) : null}
            </Grid>
          </Grid>
        </Grid>
        <Grid item md={3} sm={2} xs={3}>
          <Grid container justifyContent="flex-end">
            <QtyInput
              onUpdate={handleQtyUpdate}
              max={999999}
              noDebounce
              size="small"
              fullWidth
              buttonContainerStyle={{ width: '1.25rem' }}
              value={parseInt(qtyInputMap[props.sequence.toString()] || '0')}
              sync
              data-testid={`review-contract-qty-input${props.sequence}`}
            />
          </Grid>
        </Grid>
      </Grid>
      <Box component={Divider} mx={isSmallScreen ? 0 : 2} />
    </Box>
  );

  function handleQtyUpdate(value: number) {
    setQtyInputMap({
      ...qtyInputMap,
      [`${props.sequence}`]: `${value}`
    });
  }
}
