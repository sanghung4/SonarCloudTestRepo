import { ChangeEvent, Dispatch, useState } from 'react';

import {
  Box,
  Button,
  DialogContent,
  DialogTitle,
  Grid,
  Hidden,
  IconButton,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { CloseIcon } from 'icons';
import { LineNotesModal, LineNotesTextfield } from 'Cart/util/styled';

/**
 * Types
 */
type Props = {
  notes?: string;
  open: boolean;
  setLineNotes: Dispatch<string>;
  onClose: () => void;
};
type E = ChangeEvent<HTMLInputElement | HTMLTextAreaElement>;

/**
 * Config
 */
const maxLength = 200;

export default function CartLineNotesModal(props: Props) {
  /**
   * Custom hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * State
   */
  const [notes, setNotes] = useState<string>(props.notes ?? '');

  /**
   * Handles
   */
  const handleChangeValue = (e: E) => {
    setNotes(e.target.value);
  };

  /**
   * Render
   */
  return (
    <LineNotesModal
      open={props.open}
      onClose={handleClose}
      maxWidth="sm"
      fullWidth
      PaperProps={{ square: true }}
    >
      <DialogTitle>
        <Hidden mdDown>
          <Grid container>
            <Grid item xs>
              <Typography variant="h5" align="left">
                {t('cart.enterLineNotes')}
              </Typography>
            </Grid>
            <Grid item xs="auto">
              <Box
                component={IconButton}
                onClick={handleClose}
                size="small"
                data-testid="cart-line-notes-modal-close-button"
              >
                <CloseIcon />
              </Box>
            </Grid>
          </Grid>
        </Hidden>
        <Hidden mdUp>
          <Grid container flexDirection="column">
            <Grid item container justifyContent="flex-end">
              <Box
                component={IconButton}
                onClick={props.onClose}
                size="small"
                data-testid="line-notes-close-button"
              >
                <CloseIcon />
              </Box>
            </Grid>
            <Grid item container justifyContent="center">
              <Typography variant="h5" align="left" fontSize={24}>
                {t('cart.enterLineNotes')}
              </Typography>
            </Grid>
          </Grid>
        </Hidden>
      </DialogTitle>
      <DialogContent>
        <Typography>{t('cart.lineNotes')}</Typography>
        <LineNotesTextfield
          multiline
          rows={isSmallScreen ? 4 : 3}
          placeholder={t('cart.enterLineNotes')}
          value={notes}
          onChange={handleChangeValue}
          fullWidth
          inputProps={{
            maxLength,
            'data-testid': 'line-notes-modal-textfield'
          }}
          helperText={
            <Box component="span" display="flex" justifyContent="flex-end">
              {`${notes.length} / ${maxLength}`}
            </Box>
          }
        />
        <Grid container spacing={2} mt={3} justifyContent="flex-end">
          {!!notes && (
            <Grid item xs="auto">
              <Button
                size="large"
                variant="secondary"
                onClick={handleClearNotes}
                data-testid="line-notes-modal-clear"
              >
                {t('common.clear')}
              </Button>
            </Grid>
          )}
          <Grid item xs="auto">
            <Button
              size="large"
              onClick={handleSetLineNotes}
              data-testid="line-notes-modal-save"
            >
              {t('common.save')}
            </Button>
          </Grid>
        </Grid>
      </DialogContent>
    </LineNotesModal>
  );

  /**
   * Handles
   */
  function handleSetLineNotes() {
    props.onClose();
    props.setLineNotes(notes);
  }
  function handleClose() {
    props.onClose();
    setNotes(props.notes ?? '');
  }
  function handleClearNotes() {
    setNotes('');
  }
}
