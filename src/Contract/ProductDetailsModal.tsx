import { useEffect, useState } from 'react';

import {
  Box,
  Button,
  Container,
  Dialog,
  DialogContent,
  DialogTitle,
  Grid,
  IconButton,
  Image,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import ContractMoreDetailsDesktop from 'Contract/ProductMoreDetailsDesktop';
import ContractMoreDetailsMobile from 'Contract/ProductMoreDetailsMobile';
import { ContractProduct } from 'generated/graphql';
import { CloseIcon } from 'icons';
import notfound from 'images/notfound.png';

type Props = {
  product?: ContractProduct;
  onClose: () => void;
};

export default function ContractProductDetailsModal(props: Props) {
  /**
   * Custom hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * State
   */
  // This ensures that the data is still there when the modal is closing,
  // While closing, props.product is set as undefined so we need to keep that there to
  // prevent display bug while the modal is on its closing animation
  const [product, setProduct] = useState<ContractProduct | undefined>();

  /**
   * Effect
   */
  useEffect(productEffect, [props.product]);

  /**
   * Render
   */
  return (
    <Dialog
      open={!!props.product}
      onClose={props.onClose}
      maxWidth="md"
      fullWidth
      fullScreen={isSmallScreen}
    >
      <DialogTitle>
        <IconButton
          onClick={props.onClose}
          size="large"
          sx={(theme) => ({
            position: 'absolute',
            top: theme.spacing(1),
            right: isSmallScreen ? 1 : theme.spacing(3)
          })}
          data-testid="contract-product-modal-close-button-top"
        >
          <CloseIcon />
        </IconButton>
        <Box pl={isSmallScreen ? 0 : 3}>
          <Typography
            variant="h5"
            align="left"
            data-testid="product-details-header"
          >
            {t('contract.productDetails')}
          </Typography>
        </Box>
      </DialogTitle>
      <DialogContent>
        <Box
          component={Container}
          pt={2}
          pb={2}
          data-testid="contract-product-details-container"
        >
          <Grid container columnSpacing={2}>
            <Grid
              item
              container
              justifyContent="center"
              alignItems="center"
              xs={isSmallScreen ? 4 : 3}
              height={92}
            >
              <Image
                alt={t('common.productPicture')}
                fallback={notfound}
                src={product?.thumb ?? ''}
                data-testid="product-picture"
              />
            </Grid>
            <Grid item xs={isSmallScreen ? 8 : 9}>
              <Typography
                fontSize={12}
                color="mediumGray.main"
                component="div"
                noWrap
                data-testid="mfr-name"
              >
                {product?.brand}
              </Typography>
              <Typography
                color="primary.main"
                component="div"
                data-testid="product-name"
              >
                {product?.name}
              </Typography>
              <Grid
                container
                columnSpacing={2}
                flexDirection={isSmallScreen ? 'column' : 'row'}
              >
                {product?.partNumber ? (
                  <Grid item xs="auto">
                    <Typography
                      fontSize={12}
                      color="mediumGray.main"
                      component="div"
                      noWrap
                    >
                      {t('contract.partNum')}{' '}
                      <Typography
                        fontSize={12}
                        color="mediumGray.main"
                        component="div"
                        noWrap
                        display="inline"
                        data-testid="part-number"
                      >
                        {product?.partNumber}
                      </Typography>
                    </Typography>
                  </Grid>
                ) : null}
                {product?.mfr ? (
                  <Grid item xs="auto">
                    <Typography
                      fontSize={12}
                      color="mediumGray.main"
                      component="div"
                      noWrap
                    >
                      {t('contract.mfrNum')}{' '}
                      <Typography
                        fontSize={12}
                        color="mediumGray.main"
                        component="div"
                        noWrap
                        display="inline"
                        data-testid="mfr-number"
                      >
                        {product?.mfr}
                      </Typography>
                    </Typography>
                  </Grid>
                ) : null}
              </Grid>
            </Grid>
          </Grid>
          {isSmallScreen ? (
            <ContractMoreDetailsMobile product={product} />
          ) : (
            <ContractMoreDetailsDesktop product={product} />
          )}
          <Grid
            container
            justifyContent={isSmallScreen ? 'center' : 'flex-end'}
          >
            <Grid item>
              <Button
                onClick={props.onClose}
                size="large"
                data-testid="contract-product-modal-close-button-bottom"
              >
                {t('contract.closeDetails')}
              </Button>
            </Grid>
          </Grid>
        </Box>
      </DialogContent>
    </Dialog>
  );

  /**
   * Memo Def
   */
  function productEffect() {
    if (props.product) {
      setProduct(props.product);
    }
  }
}
