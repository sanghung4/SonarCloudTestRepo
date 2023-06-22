import {
  Box,
  Button,
  Dialog,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { useHistory } from 'react-router-dom';

import { ModalContent } from './utils/styled';
import SelectAccountsForm from 'SelectAccounts/SelectAccountsForm';
import { ArrowBack } from '@mui/icons-material';

/**
 * Types
 */
type SelectAccountsModalProps = {
  open: boolean;
};

/**
 * Component
 */
function SelectAccountsModal(props: SelectAccountsModalProps) {
  /**
   * Custom Hooks
   */
  const history = useHistory();
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * Render
   */
  return (
    <Dialog
      open={props.open}
      fullWidth={!isSmallScreen}
      fullScreen={isSmallScreen}
    >
      <Box paddingTop={2}>
        <Button
          startIcon={<ArrowBack />}
          variant="text"
          onClick={handleLogoutClick}
        >
          {t('common.logOut')}
        </Button>
        <ModalContent>
          <Typography variant="h5" textAlign="center" marginBottom={6}>
            {t('selectAccounts.selectToContinue')}
          </Typography>
          <SelectAccountsForm />
        </ModalContent>
      </Box>
    </Dialog>
  );

  /**
   * Callback Definitions
   */
  function handleLogoutClick() {
    history.push('/logout');
  }
}

export default SelectAccountsModal;
