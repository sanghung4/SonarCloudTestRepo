import React from 'react';

import {
  Box,
  Button,
  Container,
  Dialog,
  DialogContent,
  Grid,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { UserInfo } from 'User';

type Props = {
  open: boolean;
  onClose: (save: boolean) => void;
  user: UserInfo;
  email: string;
};

function ConfirmEmailChangeDialog(props: Props) {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();
  const { isSmallScreen } = useScreenSize();

  /**
   * Callbacks
   */
  const handleSave = () => props.onClose(true);
  const handleCancel = () => props.onClose(false);

  return (
    <Dialog open={props.open} onClose={props.onClose} maxWidth="sm" fullWidth>
      <DialogContent>
        <Container>
          <Box sx={{ pb: 3 }}>
            <Typography
              sx={{ pb: 1, color: 'primary.main' }}
              variant="h4"
              align="center"
            >
              {t('user.changeEmailConfirmation')}
            </Typography>
            <Typography
              sx={{ color: 'primary.main' }}
              variant="body1"
              align="center"
            >
              {t('user.changeEmailExplanation', {
                oldEmail: props.user.email,
                newEmail: props.email
              })}
            </Typography>
          </Box>
          <Grid container spacing={2}>
            <Grid container item xs={12} md justifyContent="flex-end">
              <Button
                onClick={handleCancel}
                variant="secondary"
                fullWidth={isSmallScreen}
              >
                {t('common.cancel')}
              </Button>
            </Grid>
            <Grid item xs={12} md>
              <Button onClick={handleSave} fullWidth={isSmallScreen}>
                {t('common.yesImSure')}
              </Button>
            </Grid>
          </Grid>
        </Container>
      </DialogContent>
    </Dialog>
  );
}

export default ConfirmEmailChangeDialog;
