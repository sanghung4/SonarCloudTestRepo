import {
  Box,
  Button,
  Dialog,
  DialogContent,
  Typography
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { WarningIcon } from 'icons';
import { useCartContext } from 'providers/CartProvider';

type ItemsRemovedDialogProps = {
  open: boolean;
  onClose: () => void;
};

function ItemsRemovedDialog(props: ItemsRemovedDialogProps) {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();
  const { cart } = useCartContext();

  return (
    <Dialog open={props.open} maxWidth="sm" fullWidth onClose={props.onClose}>
      <Box color="primary.main">
        <Box p={3} display="flex" flexDirection="row">
          <Box mr={1}>
            <WarningIcon />
          </Box>
          <Typography variant="h5">{t('cart.importantMessage')}</Typography>
        </Box>
      </Box>
      <DialogContent>
        <Box py={2} color="primary.main">
          <Typography component="h5">{t('cart.itemsUnavailable')}</Typography>
        </Box>
        <Box ml={6}>
          {cart?.removedProducts?.map((item, i) => (
            <Typography variant="subtitle1" key={i}>
              {i + 1}. {item}
            </Typography>
          ))}
        </Box>

        <Box display="flex" justifyContent="center" pt={2} pb={1}>
          <Button
            onClick={props.onClose}
            data-testid="item-removed-dialog-close"
          >
            {t('common.close')}
          </Button>
        </Box>
      </DialogContent>
    </Dialog>
  );
}

export default ItemsRemovedDialog;
