import React, { ChangeEvent, useState } from 'react';

import {
  Box,
  Button,
  CircularProgress,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Divider,
  FormControlLabel,
  Typography,
  IconButton,
  Radio,
  RadioGroup
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { UserInfo } from 'User';
import { DeleteIcon } from 'icons';

type Props = {
  open: boolean;
  onClose: () => void;
  onSubmit: (leftCompanyBoolean: boolean) => void;
  user: UserInfo;
  loading?: boolean;
};

function DeleteDialog(props: Props) {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();

  /**
   * State
   */

  const [leftCompanyBoolean, setLeftCompanyBoolean] = useState('false');

  /**
   * Callbacks
   */

  const handleEmployeeLeftChange = (e: ChangeEvent<HTMLInputElement>) =>
    setLeftCompanyBoolean(e.target.value);
  const handleSubmit = () => props.onSubmit(leftCompanyBoolean === 'true');

  /**
   * Static Data
   */

  const employeeLeftChoices = [
    {
      label: t('common.yes'),
      value: 'true'
    },
    {
      label: t('common.no'),
      value: 'false'
    }
  ];

  return (
    <Dialog open={props.open} maxWidth="sm" onClose={props.onClose}>
      <DialogTitle
        sx={{ backgroundColor: 'primary.main', color: 'common.white' }}
      >
        <Typography variant="h5">{t('user.deleteUser')}</Typography>
        <IconButton
          onClick={props.onClose}
          size="large"
          sx={(theme) => ({
            position: 'absolute',
            right: theme.spacing(1),
            top: theme.spacing(1)
          })}
          data-testid="delete-dialog-close-btn"
        >
          <Box component={DeleteIcon} color="common.white" />
        </IconButton>
      </DialogTitle>
      <DialogContent>
        <Box py={2} pt={4} px={3}>
          <Typography component="h5" color="primary">
            {t('user.deleteUserPrompt')}
          </Typography>
        </Box>
        <Box pb={2} px={3}>
          <Typography py={2} component="h5" color="textPrimary">
            {t('user.deleteUserLeftCompanyPrompt', {
              user: props.user.firstName || t('user.theEmployee')
            })}
          </Typography>
          <RadioGroup
            row
            aria-label="employee-left-boolean"
            name="employeeLeftBoolean"
            value={leftCompanyBoolean}
            onChange={handleEmployeeLeftChange}
          >
            {employeeLeftChoices.map((c) => (
              <FormControlLabel
                key={c.label}
                value={c.value}
                control={<Radio />}
                label={c.label}
                sx={{ p: 1, pr: 6 }}
              />
            ))}
          </RadioGroup>
        </Box>
        <Divider />
        <DialogActions sx={{ paddingTop: 2, paddingBottom: 1 }}>
          <Button
            variant="text"
            onClick={props.onClose}
            sx={{ px: 5 }}
            data-testid="delete-dialog-cancel-btn"
          >
            {t('common.cancel')}
          </Button>
          <Button
            data-testid="submit-dialog-button"
            onClick={handleSubmit}
            sx={{ px: 5 }}
          >
            {props.loading ? (
              <CircularProgress size={20} />
            ) : (
              t('common.delete')
            )}
          </Button>
        </DialogActions>
      </DialogContent>
    </Dialog>
  );
}

export default DeleteDialog;
