import {
  Button,
  Container,
  Dialog,
  IconButton
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import 'common/QtyInput/styles.scss';
import { DeleteIcon } from 'icons';

/**
 * Types
 */
type Props = {
  minQty: number;
  adjustedQty: number;
  open: boolean;
  productName: string;
  onClose: () => void;
  onConfirm: () => void;
};

/**
 * Constants
 */
const cn = 'minimum-quantity-dialog__';

/**
 * Component
 */
function MinimumQuantityDialog(props: Props) {
  /**
   * Custom hooks
   */
  const { t } = useTranslation();

  /**
   * Render
   */
  return (
    <Dialog open={props.open} onClose={props.onClose} maxWidth="sm" fullWidth>
      <div className={cn + 'dialog-content'}>
        <div className={cn + 'content-container'}>
          <h5 className={cn + 'title'}>
            {t('minimumQuantity.minimumSellQuantity')}
          </h5>
          <IconButton
            onClick={props.onClose}
            size="large"
            data-testid="qty-dialog-close-button"
            className={cn + 'icon-button'}
          >
            <DeleteIcon className={cn + 'icon-delete'} />
          </IconButton>
        </div>
      </div>
      <div className={cn + 'dialog-actions'}>
        <Container>
          <div className={cn + 'actions-container'}>
            <div>
              <span className={cn + 'body-text'}>
                {t('minimumQuantity.product')}
                <span className={cn + 'product-name'}>{props.productName}</span>
                {t('minimumQuantity.minQty', { minQty: props.minQty })}
              </span>
            </div>
            <div className={cn + 'adjusted-qty-container'}>
              <h5 className={cn + 'adjusted-qty-text'}>
                {t('minimumQuantity.orderQuantity')}
                {': '}
                {props.adjustedQty}
              </h5>
            </div>
          </div>
          <div className={cn + 'button-container'}>
            <Button
              variant="primary"
              onClick={props.onConfirm}
              data-testid="qty-dialog-confirm-button"
              className={cn + '__button'}
            >
              {t('common.ok')}
            </Button>
          </div>
        </Container>
      </div>
    </Dialog>
  );
}

export default MinimumQuantityDialog;
