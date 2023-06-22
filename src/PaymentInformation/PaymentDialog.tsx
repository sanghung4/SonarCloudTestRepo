import { useContext } from 'react';

import {
  Box,
  Button,
  Dialog,
  DialogContent,
  Grid,
  IconButton,
  Typography,
  TypographyProps
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import CreditCardListItem from 'CreditCard/CreditCardListItem';
import { CloseIcon } from 'icons';
import { CreditCard } from 'generated/graphql';
import { CreditCardContext } from 'CreditCard/CreditCardProvider';

export type PaymentDialogProps = {
  open: boolean;
  onClose: () => void;
  handleDelete: () => void;
  cardToDelete?: CreditCard;
};

function PaymentDialog(props: PaymentDialogProps) {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();

  /**
   * Context
   */
  const { expiredCreditCards } = useContext(CreditCardContext);

  /**
   * Render
   */
  return (
    <Dialog open={props.open} onClose={props.onClose} maxWidth="xs" fullWidth>
      <Grid container justifyContent="flex-end">
        <IconButton onClick={props.onClose} size="large">
          <CloseIcon />
        </IconButton>
      </Grid>
      <DialogContent>
        <Box mb={2}>
          <Typography variant="h5" color="textPrimary" align="center" p={2}>
            {t('creditCard.confirmDeletePrompt')}
          </Typography>
          <Grid container justifyContent="center" p={2} pb={4}>
            {Boolean(props.cardToDelete) && (
              <CreditCardListItem
                creditCard={props.cardToDelete!}
                expired={
                  expiredCreditCards[
                    props.cardToDelete!.elementPaymentAccountId
                  ]
                }
                TypographyProps={
                  { 'data-testid': 'card-to-delete' } as TypographyProps
                }
                hideDate
                hideExpired
              />
            )}
          </Grid>
          <Grid container spacing={2}>
            <Box width="100%">
              <Button
                fullWidth
                onClick={() => {
                  props.handleDelete();
                  props.onClose();
                }}
                data-testid="confirm-delete-button"
              >
                {t('creditCard.confirmDelete')}
              </Button>
            </Box>
            <Box width="100%" mt={2} mb={3}>
              <Button
                fullWidth
                onClick={props.onClose}
                variant="secondary"
                data-testid="cancel-button"
              >
                {t('common.cancel')}
              </Button>
            </Box>
          </Grid>
        </Box>
      </DialogContent>
    </Dialog>
  );
}

export default PaymentDialog;
