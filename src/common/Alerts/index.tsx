import {
  Button,
  Dialog,
  DialogContent,
  IconButton,
  Box,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';

import decodeHTMLEntities from 'utils/decodeHTMLEntities';
import { CloseIcon } from 'icons';

type Props = {
  title: string;
  message: string;
  confirmBtnTitle?: string;
  onConfirm?: () => void;
  onCancel?: () => void;
  cancelBtnTitle?: string;
  open: boolean;
};

export default function CommonAlert(props: Props) {
  const {
    title,
    message,
    confirmBtnTitle,
    cancelBtnTitle,
    onConfirm,
    onCancel,
    open
  } = props;

  const { isSmallScreen } = useScreenSize();

  return (
    <Dialog open={open} onClose={onCancel} maxWidth="sm" fullWidth>
      <Box display="flex" justifyContent="space-between">
        <Typography variant="h5" color="primary" mt={2} ml={3}>
          {decodeHTMLEntities(title)}
        </Typography>
        <IconButton onClick={onCancel} size="large">
          <CloseIcon />
        </IconButton>
      </Box>
      <DialogContent>
        <Typography variant="body1" color="primary" pb={3}>
          {decodeHTMLEntities(message)}
        </Typography>

        <Box
          display="flex"
          justifyContent="flex-end"
          pt={2}
          pb={1}
          flexDirection={isSmallScreen ? 'column-reverse' : 'row'}
        >
          <Button
            variant="text"
            data-testid="alert-cancel-button"
            onClick={onCancel}
            color="primaryLight"
            sx={{ mr: 2, textDecoration: 'underline' }}
          >
            {cancelBtnTitle}
          </Button>
          {!!confirmBtnTitle && (
            <Button
              onClick={onConfirm}
              variant="primary"
              data-testid="alert-confirm-button"
            >
              {confirmBtnTitle}
            </Button>
          )}
        </Box>
      </DialogContent>
    </Dialog>
  );
}
