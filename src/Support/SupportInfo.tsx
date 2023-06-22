import React, { useContext } from 'react';

import {
  Grid,
  Typography,
  useScreenSize,
  Link
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { useGetHomeBranchQuery } from 'generated/graphql';
import { BranchContext } from 'providers/BranchProvider';
import { AuthContext } from 'AuthProvider';

import { ContactPhoneIcon, ContactLocationIcon } from 'icons';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';

const SupportInfo = () => {
  const { t } = useTranslation();
  const { isSmallScreen } = useScreenSize();
  const { homeBranch } = useContext(BranchContext);
  const { authState } = useContext(AuthContext);

  /**
   * Context
   */
  const { selectedAccounts } = useSelectedAccountsContext();

  const { data: homeBranchQuery } = useGetHomeBranchQuery({
    variables: {
      shipToAccountId: selectedAccounts?.shipTo?.id ?? ''
    }
  });

  const hours = homeBranch?.businessHours?.split(';');
  const firstHours = hours ? hours[0] : '';
  const secondHours = hours ? hours[1] : '';

  return (
    <Grid
      container
      flexDirection="column"
      alignItems="flex-end"
      my={5}
      px={isSmallScreen ? 2 : 0}
    >
      <Grid item container justifyContent="center" width="100%" pb={5}>
        <ContactPhoneIcon />
        <Grid item xs={9} pl={2}>
          <Typography variant="h6" color="primary">
            {t('support.eCommerceTitle')}
          </Typography>
          <Grid container pt={1} flexDirection="column">
            <Grid item>
              <Typography component="span" fontWeight={500}>
                {t('support.email')}
              </Typography>
              <Link>
                <Typography component="span">
                  {t('support.eCommerceEmail')}
                </Typography>
              </Link>
            </Grid>
            <Grid item>
              <Typography component="span" fontWeight={500}>
                {t('support.phone')}
              </Typography>
              <Typography component="span">
                {t('support.eCommerceNumber')}
              </Typography>
            </Grid>
            <Grid item>
              <Typography component="span" fontWeight={500}>
                {t('support.hours')}
              </Typography>
              <Typography component="span">
                {t('support.eCommerceHours')}
              </Typography>
            </Grid>
          </Grid>
        </Grid>
      </Grid>
      {authState?.isAuthenticated && (
        <Grid item container sx={{ width: '100%' }} justifyContent="center">
          <ContactLocationIcon />
          <Grid item xs={9} paddingLeft={2}>
            <Typography variant="h6" color="primary">
              {homeBranchQuery?.homeBranch?.name ?? ''}
            </Typography>
            <Grid container item paddingTop={1} flexDirection="column">
              <Typography>
                {homeBranchQuery?.homeBranch?.address1 ?? ''}{' '}
                {homeBranchQuery?.homeBranch?.address2 ?? ''}{' '}
                {homeBranchQuery?.homeBranch?.city ?? ''},{' '}
                {homeBranchQuery?.homeBranch?.state ?? ''}{' '}
                {homeBranchQuery?.homeBranch?.zip ?? ''}
              </Typography>

              <Grid>
                <Typography component="span" fontWeight={500}>
                  {t('support.phone')}
                </Typography>
                <Typography component="span">
                  {' '}
                  {homeBranchQuery?.homeBranch?.phone}
                </Typography>
              </Grid>
              <Grid container flexDirection="row" columnSpacing={1}>
                <Grid item>
                  <Typography fontWeight={500}>{t('support.hours')}</Typography>
                </Grid>
                <Grid item>
                  <Typography>{firstHours}</Typography>
                  <Typography>{secondHours}</Typography>
                </Grid>
              </Grid>
            </Grid>
          </Grid>
        </Grid>
      )}
    </Grid>
  );
};

export default SupportInfo;
