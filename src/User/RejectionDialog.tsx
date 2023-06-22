import React, { ChangeEvent, useState } from 'react';

import {
  Box,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  Divider,
  FormControlLabel,
  TextField,
  Typography,
  IconButton,
  Radio,
  RadioGroup,
  useSnackbar
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { RejectionReason } from 'generated/graphql';
import { DeleteIcon } from 'icons';
import { getRejectionReason } from 'utils/rejectionReasons';

type Props = {
  open: boolean;
  onClose: () => void;
  onSubmit: (reason: string, description: string) => void;
  rejectionReasons: RejectionReason[];
};

function RejectionDialog(props: Props) {
  /**
   * Custom Hooks
   */
  const { pushAlert } = useSnackbar();
  const { t } = useTranslation();

  /**
   * State
   */
  const [description, setDescription] = useState('');
  const [reason, setReason] = useState<string>();
  const [submitted, setSubmitted] = useState(false);
  const [disable, setDisable] = useState(false);
  /**
   * Callbacks
   */
  const handleDescriptionChange = (e: ChangeEvent<HTMLInputElement>) => {
    setDescription(e.target.value);
  };

  const handleReasonChange = (e: ChangeEvent<HTMLInputElement>) => {
    setReason(e.target.value);
  };

  const handleSubmit = async () => {
    setDisable(true);
    setSubmitted(true);

    if ((reason === 'OTHER' && description) || (reason && reason !== 'OTHER')) {
      await props.onSubmit(reason, description);
    } else {
      const error =
        reason === 'OTHER'
          ? t('user.descriptionRequired')
          : t('user.reasonRequired');
      pushAlert(error, { variant: 'warning' });
    }
    setDisable(false);
  };
  return (
    <Dialog open={props.open} maxWidth="sm" fullWidth onClose={props.onClose}>
      <Box
        sx={{
          bgcolor: 'primary.main',
          color: 'common.white'
        }}
      >
        <DialogTitle>
          <Typography variant="h5">{t('user.rejectUser')}</Typography>
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
        <Box py={2}>
          <Typography component="h5" color="primary">
            {t('user.reasonForRejection')}
          </Typography>
        </Box>
        <Box px={1} pb={2}>
          <RadioGroup
            aria-label="rejection-reason"
            name="rejectionReason"
            value={reason}
            onChange={handleReasonChange}
          >
            {props.rejectionReasons.map((r) => (
              <FormControlLabel
                key={r}
                value={r}
                control={<Radio />}
                label={t(getRejectionReason(r)) as string}
                sx={{ py: 1 }}
              />
            ))}
          </RadioGroup>
          {reason === RejectionReason.Other ? (
            <Box pt={2} px={2}>
              <TextField
                fullWidth
                placeholder={t('user.inputReasonForRejection')}
                error={
                  submitted && reason === RejectionReason.Other && !description
                }
                onChange={handleDescriptionChange}
                helperText={
                  submitted && reason === RejectionReason.Other && !description
                    ? t('user.reasonRequiredOther')
                    : null
                }
                value={description}
              />
            </Box>
          ) : null}
        </Box>
        <Divider />
        <Box display="flex" justifyContent="flex-end" pt={2} pb={1}>
          <Button onClick={handleSubmit} disabled={disable}>
            {t('common.submit')}
          </Button>
        </Box>
      </DialogContent>
    </Dialog>
  );
}

export default RejectionDialog;
