import {
  Box,
  Button,
  Dialog,
  DialogContent,
  DialogTitle,
  IconButton,
  Grid,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { CloseIcon } from 'icons';
import { useCartContext } from 'providers/CartProvider';

type Props = {
  open: boolean;
  onClose: (save: boolean) => void;
};

function RemoveCartItemsDialog(props: Props) {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();
  const { isSmallScreen } = useScreenSize();

  /**
   * Callbacks
   */

  /**
   * Context
   */
  const { deleteCartItems } = useCartContext();
  const handleCancel = () => props.onClose(false);

  return (
    <Dialog open={props.open} maxWidth="sm" fullWidth onClose={props.onClose}>
      <DialogTitle>
        <Box color="primary.main">
          <Box pt={3}>
            {isSmallScreen ? (
              <Typography variant="h5" textAlign="center">
                {t('cart.removeAllFromCartDialog')}
              </Typography>
            ) : (
              <Typography variant="h5">
                {t('cart.removeAllFromCartDialog')}
              </Typography>
            )}
          </Box>
        </Box>
        <IconButton
          onClick={handleCancel}
          size="large"
          sx={(theme) => ({
            position: 'absolute',
            top: theme.spacing(3.5),
            right: theme.spacing(1)
          })}
        >
          <CloseIcon />
        </IconButton>
      </DialogTitle>
      <DialogContent>
        <Box py={2} color="primary.main">
          {isSmallScreen ? (
            <Typography component="h5" textAlign="center">
              {t('cart.removeAllFromCartDialogContent')}
            </Typography>
          ) : (
            <Typography component="h5">
              {t('cart.removeAllFromCartDialogContent')}
            </Typography>
          )}
        </Box>
        {isSmallScreen ? (
          <Box justifyContent="center">
            <Box justifyContent="center" display="flex">
              <Button
                sx={{ padding: '16px', mb: '8px' }}
                onClick={handleDeleteCart}
                data-testid="remove-all-items-dialog-confirm"
              >
                {t('cart.removeAllFromCartConfirm')}
              </Button>
            </Box>
            <Box justifyContent="center" display="flex">
              <Button
                onClick={handleCancel}
                variant="text"
                sx={{ padding: '16px' }}
              >
                {t('cart.removeAllFromCartCancel')}
              </Button>
            </Box>
          </Box>
        ) : (
          <Grid display="flex" justifyContent="right">
            <Grid paddingRight={3}>
              <Button
                onClick={handleCancel}
                variant="text"
                sx={{ padding: '16px', mr: 1 }}
                data-testid="remove-all-items-dialog-cancel"
              >
                {t('cart.removeAllFromCartCancel')}
              </Button>
            </Grid>
            <Grid>
              <Button
                onClick={handleDeleteCart}
                data-testid="remove-all-items-dialog-confirm"
                sx={{ padding: '16px', mr: 2 }}
              >
                {t('cart.removeAllFromCartConfirm')}
              </Button>
            </Grid>
          </Grid>
        )}
      </DialogContent>
    </Dialog>
  );

  function handleDeleteCart() {
    handleCancel();
    //Can't access button if cart is undefined
    deleteCartItems();
  }
}
export default RemoveCartItemsDialog;
