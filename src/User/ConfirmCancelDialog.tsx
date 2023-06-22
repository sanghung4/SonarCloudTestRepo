import React from 'react';

import {
  Button,
  Container,
  Dialog,
  DialogContent,
  Grid,
  Typography
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

type Props = {
  open: boolean;
  onClose: (save: boolean) => void;
};

function ConfirmCancelDialog(props: Props) {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();

  /**
   * Callbacks
   */
  const handleSave = () => props.onClose(true);
  const handleCancel = () => props.onClose(false);

  return (
    <Dialog open={props.open} onClose={props.onClose} maxWidth="sm" fullWidth>
      <DialogContent>
        <Container sx={{ py: 4 }}>
          <Typography
            variant="h4"
            color="primary"
            align="center"
            sx={{ pb: 4 }}
          >
            {t('common.unsavedChanges')}
          </Typography>
          <Grid container spacing={2}>
            <Grid item xs={12} md>
              <Button fullWidth onClick={handleCancel} variant="secondary">
                {t('common.withoutSaving')}
              </Button>
            </Grid>
            <Grid item xs={12} md>
              <Button fullWidth onClick={handleSave}>
                {t('common.save')}
              </Button>
            </Grid>
          </Grid>
        </Container>
      </DialogContent>
    </Dialog>
  );
}

export default ConfirmCancelDialog;
