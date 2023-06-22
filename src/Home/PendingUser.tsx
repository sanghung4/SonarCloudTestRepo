import React, { useContext } from 'react';

import {
  Box,
  Card,
  CardContent,
  Container,
  Grid,
  Link,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { AuthContext } from 'AuthProvider';
import Loader from 'common/Loader';
import { useGetContactInfoQuery } from 'generated/graphql';
import { TimeIcon } from 'icons';

function PendingUser() {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * Context
   */
  const { profile } = useContext(AuthContext);

  /**
   * Data
   */
  const { data: contactInfoQuery, loading: contactInfoLoading } =
    useGetContactInfoQuery({
      variables: {
        userId: profile?.userId
      }
    });

  return contactInfoLoading ? (
    <Loader />
  ) : (
    <Box
      mx={isSmallScreen ? 1 : 0}
      mt={isSmallScreen ? 4 : 5}
      mb={5}
      data-testid="pendinguser-component"
    >
      <Container>
        <Grid container spacing={isSmallScreen ? 3 : 8}>
          <Grid item xs={12} md={6} sx={{ display: 'flex' }}>
            <Card sx={{ flex: 1 }}>
              <CardContent>
                <Box
                  p={5}
                  pt={4}
                  display="flex"
                  flexDirection="column"
                  justifyContent="center"
                  alignItems="center"
                  textAlign="center"
                >
                  <Typography
                    variant="h5"
                    color="primary"
                    gutterBottom
                    sx={{ fontWeight: 700 }}
                  >
                    {t('home.thankYou')}
                  </Typography>
                  <Box mb={3.5}>
                    <Typography
                      variant="h5"
                      color="primary"
                      sx={{ fontWeight: 400 }}
                    >
                      {t('home.startBrowsing')}
                    </Typography>
                  </Box>
                  <Box mb={2}>
                    <Typography variant="body1">
                      {t('common.needMoreInfo')}
                    </Typography>
                  </Box>
                  <Typography variant="h5" color="primary" gutterBottom>
                    {contactInfoQuery?.contactInfo?.isBranchInfo
                      ? t('common.branchInfo')
                      : t('common.accountAdmin')}
                  </Typography>
                  <Typography variant="body1" gutterBottom>
                    {t('common.phoneNumber')}:{' '}
                    {contactInfoQuery?.contactInfo?.phoneNumber ? (
                      <Link
                        href={`tel:${
                          contactInfoQuery!.contactInfo!.phoneNumber
                        }`}
                        sx={{
                          color: 'primary02.main'
                        }}
                      >
                        {contactInfoQuery!.contactInfo!.phoneNumber}
                      </Link>
                    ) : null}
                  </Typography>
                  {!contactInfoQuery?.contactInfo?.isBranchInfo ? (
                    <Typography variant="body1">
                      {t('common.emailAddress')}:{' '}
                      {contactInfoQuery?.contactInfo?.emailAddress ? (
                        <Link
                          href={`mailto:${
                            contactInfoQuery!.contactInfo!.emailAddress
                          }`}
                          sx={{
                            color: 'primary02.main'
                          }}
                        >
                          {contactInfoQuery!.contactInfo!.emailAddress}
                        </Link>
                      ) : null}
                    </Typography>
                  ) : null}
                </Box>
              </CardContent>
            </Card>
          </Grid>
          <Grid item xs={12} md={6} sx={{ display: 'flex' }}>
            <Card sx={{ flex: 1 }}>
              <CardContent>
                <Box
                  px={isSmallScreen ? 1 : 5}
                  pt={isSmallScreen ? 1 : 4}
                  pb={isSmallScreen ? 1 : 5}
                  display="flex"
                  flexDirection="column"
                  justifyContent="center"
                  alignItems="center"
                  textAlign="center"
                >
                  <Typography
                    variant="h4"
                    color="primary"
                    sx={{ fontWeight: 700 }}
                  >
                    {t('common.accountStatus')}:&nbsp;
                    <Box component="span" fontWeight={400}>
                      {t('common.pendingReview')}
                    </Box>
                  </Typography>
                  <Box my={3.75} component={TimeIcon} />
                  <Typography variant="body1" component="span">
                    {t('home.fullAccess')}
                  </Typography>
                </Box>
              </CardContent>
            </Card>
          </Grid>
        </Grid>
      </Container>
    </Box>
  );
}

export default PendingUser;
