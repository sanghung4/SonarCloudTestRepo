import React, { useContext, useEffect, useMemo, useState } from 'react';

import {
  Box,
  Breadcrumbs,
  Button,
  Container,
  Grid,
  Hidden,
  Link,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { useHistory } from 'react-router-dom';

import { AuthContext } from 'AuthProvider';
import Loader from 'common/Loader';
import {
  ErpAccount,
  useAccountDetailsQuery,
  useUserQuery
} from 'generated/graphql';
import { ChevronLeftIcon, ChevronRightIcon } from 'icons';
import ViewAccount from 'Account/ViewAccount';
import ViewCompanyInfo from 'Account/ViewCompanyInfo';
import EditAccount from 'Account/EditAccount';
import { useDomainInfo } from 'hooks/useDomainInfo';
import useDocumentTitle from 'hooks/useDocumentTitle';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';

function User() {
  /**
   * Custom Hooks
   */
  const history = useHistory();
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();
  const { brand } = useDomainInfo();
  useDocumentTitle(t('common.accountManagement'));

  /**
   * Context
   */
  const { profile } = useContext(AuthContext);
  const { selectedAccounts } = useSelectedAccountsContext();

  /**
   * Data fetching
   */
  const { data: userQuery, loading: userLoading } = useUserQuery({
    variables: {
      userId: profile?.userId
    }
  });
  const { data: accountQuery, loading: accountLoading } =
    useAccountDetailsQuery({
      variables: {
        accountId: selectedAccounts?.billTo?.id ?? '',
        brand
      }
    });

  /**
   * State
   */
  const [isEditing, setIsEditing] = useState(false);
  const [userInfo, setUserInfo] = useState({
    firstName: '',
    lastName: '',
    phoneNumber: '',
    phoneTypeId: '',
    email: '',
    role: '',
    id: ''
  });

  /**
   * Callbacks
   */
  const handleEditClicked = () => setIsEditing(true);
  const handleFinishEditing = (values?: any) => {
    values && setUserInfo({ ...userInfo, ...values });
    setIsEditing(false);
  };

  const companyInfo = useMemo(accountMemo, [
    accountQuery,
    selectedAccounts.erpSystemName
  ]);

  /**
   * Effects
   */
  useEffect(() => {
    if (userQuery?.user) {
      setUserInfo({
        firstName: userQuery.user.firstName!,
        lastName: userQuery.user.lastName!,
        phoneNumber: userQuery.user.phoneNumber!,
        phoneTypeId: userQuery.user.phoneType!,
        email: userQuery.user.email!,
        role: userQuery.user.role?.name!,
        id: userQuery.user.id!
      });
    }
  }, [userQuery]);

  return userLoading ||
    accountLoading ||
    !userQuery?.user ||
    !accountQuery?.account ||
    !accountQuery?.account[0] ? (
    <Loader />
  ) : (
    <Box mb={5}>
      <Container maxWidth="lg" disableGutters={isSmallScreen}>
        <Grid container item spacing={4}>
          <Hidden mdUp>
            <Grid item xs={12}>
              <Button
                sx={{ mt: 1 }}
                startIcon={<ChevronLeftIcon />}
                variant="inline"
                onClick={onBackToHome}
              >
                {t('common.backToHome')}
              </Button>
            </Grid>
          </Hidden>
          <Hidden mdDown>
            <Grid item xs={12}>
              <Breadcrumbs
                aria-label="breadcrumb"
                separator={<ChevronRightIcon />}
              >
                <Link color="primary" href="/">
                  {t('common.home')}
                </Link>
                <Typography color="inherit" variant="caption">
                  {t('common.accountInfo')}
                </Typography>
              </Breadcrumbs>
            </Grid>
          </Hidden>
          <Grid item xs={12}>
            <Box mb={2} pl={isSmallScreen ? 3 : 0}>
              <Typography color="textPrimary" variant="h4">
                {`${userInfo.firstName} ${userInfo.lastName}`}
              </Typography>
            </Box>
          </Grid>
          <Grid item xs={12} md={6}>
            {isEditing ? (
              <EditAccount user={userInfo} onFinished={handleFinishEditing} />
            ) : (
              <ViewAccount user={userInfo} onEditClicked={handleEditClicked} />
            )}
          </Grid>
          <Grid item xs={12} md={6}>
            <ViewCompanyInfo company={companyInfo} />
          </Grid>
        </Grid>
      </Container>
    </Box>
  );

  function onBackToHome() {
    history.push('/');
  }

  function accountMemo(): ErpAccount {
    const erpSystemName = selectedAccounts.erpSystemName;
    if (accountQuery?.account && erpSystemName) {
      const erpAccount = accountQuery?.account?.filter(
        (i) => i?.erpName === erpSystemName
      )[0];
      return erpAccount as ErpAccount;
    }
    return [] as ErpAccount;
  }
}

export default User;
