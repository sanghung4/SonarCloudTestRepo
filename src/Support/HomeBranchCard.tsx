import React from 'react';

import {
  Box,
  Card,
  CardHeader,
  Link,
  Skeleton,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { useGetHomeBranchQuery } from 'generated/graphql';
import homeBranchImg from 'images/home-branch.svg';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';

function HomeBranchCard() {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * Context
   */
  const { selectedAccounts } = useSelectedAccountsContext();

  /**
   * Data
   */
  const { data: homeBranchQuery, loading: homeBranchLoading } =
    useGetHomeBranchQuery({
      variables: {
        shipToAccountId: selectedAccounts?.shipTo?.id ?? ''
      }
    });

  return !homeBranchQuery?.homeBranch && homeBranchLoading ? (
    <Box
      width={isSmallScreen ? '21.4rem' : '32.4rem'}
      height={isSmallScreen ? '14.6rem' : '8.4rem'}
    >
      <Card>
        <Box
          display="flex"
          sx={
            isSmallScreen
              ? { flexDirection: 'column', alignItems: 'center' }
              : {}
          }
        >
          <Box p={3}>
            <Skeleton variant="rectangular" height={80} width={80} />
          </Box>
          <Box
            display={isSmallScreen ? 'flex' : ''}
            sx={
              isSmallScreen
                ? { flexDirection: 'column', alignItems: 'center' }
                : {}
            }
          >
            <Typography variant="h2" component="span">
              <Skeleton width={250} />
            </Typography>

            <Typography variant="h4" component="span">
              <Skeleton width={200} />
            </Typography>

            <Typography variant="h4" component="span">
              <Skeleton width={200} />
            </Typography>
          </Box>
        </Box>
      </Card>
    </Box>
  ) : (
    <Card sx={{ borderRadius: isSmallScreen ? '.5rem' : undefined }}>
      <Box p={3} display={isSmallScreen ? 'block' : 'flex'}>
        <Box>
          <Box
            component="img"
            alt="HomeBranch"
            src={homeBranchImg}
            sx={{
              mx: 'auto',
              display: 'block'
            }}
          />
        </Box>
        <Box>
          <Box mt={-2} mb={-1.5}>
            <CardHeader
              align={isSmallScreen ? 'center' : ''}
              title={homeBranchQuery?.homeBranch?.name ?? ''}
              titleTypographyProps={{
                variant: 'h4',
                color: 'primary'
              }}
            />
          </Box>
          <Box px={3} textAlign={isSmallScreen ? 'center' : undefined}>
            <Typography variant="body2" color="primary">
              {homeBranchQuery?.homeBranch?.address1 ?? ''}{' '}
              {homeBranchQuery?.homeBranch?.address2 ?? ''}{' '}
              {homeBranchQuery?.homeBranch?.city ?? ''},{' '}
              {homeBranchQuery?.homeBranch?.state ?? ''}{' '}
              {homeBranchQuery?.homeBranch?.zip ?? ''}
            </Typography>
          </Box>
          {homeBranchQuery?.homeBranch?.phone ? (
            <Box
              px={3}
              justifyContent={isSmallScreen ? 'center' : ''}
              display="flex"
              mt={1.5}
            >
              <Typography
                variant="body2"
                color="textSecondary"
                component="span"
              >
                {t('common.phoneNumber')}:{' '}
              </Typography>
              <Link
                href={`tel:${homeBranchQuery?.homeBranch?.phone}`}
                color="primary02.main"
                sx={{ ml: 0.5, textDecoration: 'none' }}
              >
                {homeBranchQuery?.homeBranch?.phone}
              </Link>
            </Box>
          ) : null}
        </Box>
      </Box>
    </Card>
  );
}

export default HomeBranchCard;
