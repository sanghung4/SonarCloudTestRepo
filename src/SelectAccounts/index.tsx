import { useContext } from 'react';
import { Typography, useScreenSize } from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { useHistory } from 'react-router-dom';

import { AuthContext } from 'AuthProvider';
import SelectAccountsForm from './SelectAccountsForm';
import {
  LogoutButton,
  PageContainer,
  SelectAccountsContainer
} from './utils/styled';
import { testIds } from 'test-utils/testIds';
import PendingUser from 'Home/PendingUser';
import useDocumentTitle from 'hooks/useDocumentTitle';
import { ArrowBack } from '@mui/icons-material';

// TODO: if you switch to an account with no shiptos it breaks the view
// TODO: check what happens if you login with a different account that does not have access to the saved accounts

function SelectAccount() {
  /**
   * Custom Hooks
   */
  const history = useHistory();
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();
  useDocumentTitle(t('common.selectAccount'));

  /**
   * Context
   */
  const { profile } = useContext(AuthContext);

  /**
   * Render
   */

  return !profile?.permissions?.length &&
    profile?.userId &&
    !profile?.isEmployee ? (
    <PendingUser />
  ) : (
    <PageContainer sx={{ alignItems: 'center', height: '100%' }}>
      <LogoutButton
        startIcon={<ArrowBack />}
        variant="text"
        onClick={handleLogoutClick}
      >
        {t('common.logOut')}
      </LogoutButton>
      <SelectAccountsContainer
        maxWidth="sm"
        data-testid={testIds.SelectAccounts.page}
      >
        <Typography
          variant={isSmallScreen ? 'h4' : 'h3'}
          component="h1"
          align="center"
          color="primary"
          marginBottom={5}
          fontWeight={700}
        >
          {t('common.shippingBilling')}
        </Typography>
        <SelectAccountsForm onContinue={handleContinue} />
      </SelectAccountsContainer>
    </PageContainer>
  );

  /**
   * Callback Defs
   */

  async function handleContinue() {
    // If the cart object exists then the user is switching accounts
    // so the cart for the newly selected account needs to be retrieved
    history.push('/');
  }

  function handleLogoutClick() {
    history.push('/logout');
  }
}

export default SelectAccount;
