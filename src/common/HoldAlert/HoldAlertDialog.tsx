import {
  Box,
  Dialog,
  DialogContent,
  DialogTitle,
  IconButton,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { CloseIcon } from 'icons';

type Props = {
  open: boolean;
  onClose: () => void;
};

function HoldAlertDialog(props: Props) {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * Render
   */
  return (
    <Dialog
      open={props.open}
      fullWidth
      maxWidth="sm"
      onClose={props.onClose}
      PaperProps={{
        sx: {
          width: 'calc(100% - 32px)',
          my: 4,
          mx: 2
        }
      }}
    >
      <Box
        component={DialogTitle}
        display="flex"
        justifyContent="space-between"
        alignItems="center"
        color="common.white"
        bgcolor="primary.main"
      >
        <Typography variant="h5">{t('common.warning')}</Typography>
        <IconButton
          color="inherit"
          size="small"
          onClick={props.onClose}
          data-testid="hold-alert-dialog-close"
        >
          <CloseIcon />
        </IconButton>
      </Box>
      <DialogContent>
        <Typography
          variant="body1"
          px={isSmallScreen ? 0 : 3}
          py={isSmallScreen ? 1 : 2}
          color="text.secondary"
          whiteSpace={isSmallScreen ? 'initial' : 'pre'}
        >
          {t('common.accountHoldOrdering')}
        </Typography>
      </DialogContent>
    </Dialog>
  );
}

export default HoldAlertDialog;
