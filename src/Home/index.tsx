import React, { useContext } from 'react';

import { Box } from '@dialexa/reece-component-library';

import { AuthContext } from 'AuthProvider';
import Features from 'Home/Features';
import Landing from 'Home/Landing';
import PendingUser from 'Home/PendingUser';
import VerifyEmail from 'Register-old/VerifyEmail';
import LoginAndSupport from './LoginAndSupport';
import { useScrollToTop } from 'hooks/useScrollToTop';
import { useTranslation } from 'react-i18next';
import useDocumentTitle from 'hooks/useDocumentTitle';

function Home() {
  useScrollToTop();

  /**
   * Custom Hooks
   */
  const { t } = useTranslation();
  useDocumentTitle(t('common.plumbingHVACWaterworksSupply'));

  /**
   * Context
   */
  const { profile } = useContext(AuthContext);
  return (
    <>
      {profile?.isEmployee && !profile?.isVerified ? (
        <VerifyEmail />
      ) : !profile?.permissions?.length && profile?.userId ? (
        <PendingUser />
      ) : (
        <Box data-testid="main-content">
          <Landing />
          <Features />
          <LoginAndSupport />
        </Box>
      )}
    </>
  );
}

export default Home;
