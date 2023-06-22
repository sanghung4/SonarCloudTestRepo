import React, { useState } from 'react';

import {
  Box,
  Button,
  CircularProgress,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Divider,
  Grid,
  TextField,
  Typography,
  IconButton,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { DeleteIcon } from 'icons';

type Props = {
  open: boolean;
  onClose: () => void;
  onSubmit: (reason: string) => void;
  loading?: boolean;
};

function RejectOrderDialog(props: Props) {
  /**
   * Custom Hooks
   */

  const { t } = useTranslation();
  const { isSmallScreen } = useScreenSize();

  /**
   * State
   */
  const [reason, setReason] = useState('');

  /**
   * Callbacks
   */
  const handleSubmit = () => {
    props.onSubmit(reason);
  };

  return (
    <Dialog
      open={props.open}
      fullScreen={isSmallScreen}
      maxWidth="sm"
      fullWidth
      onClose={props.onClose}
    >
      <Box
        sx={{
          bgcolor: 'primary.main',
          color: 'common.white'
        }}
      >
        <DialogTitle>
          <Typography variant="h5">
            {t('purchaseApprovals.rejectOrder')}
          </Typography>
          <IconButton
            onClick={props.onClose}
            size="large"
            sx={(theme) => ({
              position: 'absolute',
              top: theme.spacing(1),
              right: theme.spacing(1)
            })}
          >
            <Box component={DeleteIcon} sx={{ color: 'common.white' }} />
          </IconButton>
        </DialogTitle>
      </Box>
      <DialogContent>
        <Box>
          <Box py={2} px={2}>
            <Typography variant="h4" color="primary">
              {t('purchaseApprovals.rejectConfirmation')}
            </Typography>
          </Box>
          <Box pb={2} px={2}>
            <TextField
              multiline
              rows={3}
              fullWidth
              label={t('purchaseApprovals.rejectReasonLabel')}
              value={reason}
              onChange={(e) => setReason(e.target.value)}
            />
          </Box>
        </Box>
      </DialogContent>
      <Divider />
      <DialogActions>
        <Grid item xs={12} md={4}>
          <Button variant="text" onClick={props.onClose} fullWidth>
            {t('common.cancel')}
          </Button>
        </Grid>
        <Grid item xs={12} md={4}>
          <Button onClick={handleSubmit} fullWidth>
            {props.loading ? (
              <CircularProgress size={20} />
            ) : (
              t('common.submit')
            )}
          </Button>
        </Grid>
      </DialogActions>
    </Dialog>
  );
}

export default RejectOrderDialog;
