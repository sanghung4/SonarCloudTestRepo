import React, { useContext } from 'react';

import {
  Button,
  Dialog,
  DialogContent,
  Grid,
  IconButton,
  Typography
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { ListContext } from 'providers/ListsProvider';
import { CloseIcon } from 'icons';
import decodeHTMLEntities from 'utils/decodeHTMLEntities';

type Props = {
  open: boolean;
  onClose: () => void;
  handleDelete: () => void;
};

function ListDialog(props: Props) {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();

  /**
   * Context
   */
  const { selectedList } = useContext(ListContext);

  return (
    <Dialog
      open={props.open}
      onClose={props.onClose}
      maxWidth="xs"
      data-testid="list-dialog-popup"
    >
      <Grid container justifyContent="flex-end">
        <IconButton
          onClick={props.onClose}
          size="large"
          data-testid="close-button"
        >
          <CloseIcon />
        </IconButton>
      </Grid>
      <DialogContent>
        <Typography
          variant="h5"
          color="primary"
          align="center"
          sx={{ pb: 2 }}
          data-testid="delete-list-confirmation-text"
        >
          {decodeHTMLEntities(
            t('lists.deleteListConfirmation', {
              name: selectedList?.name ?? ''
            })
          )}
        </Typography>
        <Typography
          variant="body1"
          color="primary"
          align="center"
          sx={{ pb: 3 }}
          data-testid="delete-list-confirmation-subtext"
        >
          {decodeHTMLEntities(
            t('lists.deleteListConfirmationSub', {
              name: selectedList?.name ?? ''
            })
          )}
        </Typography>
        <Button
          fullWidth
          data-testid="yes-button"
          onClick={() => {
            props.handleDelete();
            props.onClose();
          }}
        >
          {t('common.yes')}
        </Button>
        <Button
          fullWidth
          data-testid="cancel-button"
          onClick={() => {
            props.onClose();
          }}
          variant="secondary"
          sx={{ mt: 2 }}
        >
          {t('common.cancel')}
        </Button>
      </DialogContent>
    </Dialog>
  );
}

export default ListDialog;
