import {
  Box,
  Button,
  Container,
  Grid,
  Hidden,
  Link,
  Typography,
  styled,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { Link as RouterLink, Redirect, useLocation } from 'react-router-dom';

import leak_bg from 'images/leak_bg.svg';
import leak from 'images/leak.svg';
import stacked_boxes from 'images/stacked_boxes.svg';
import useDocumentTitle from 'hooks/useDocumentTitle';

const Img = styled('img')(({ theme }) => ({
  [theme.breakpoints.up('sm')]: {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)'
  }
}));

export enum ErrorTypes {
  NOT_FOUND = 'NOT_FOUND',
  BRANCH_ERROR = 'BRANCH_ERROR',
  OTHER = 'OTHER' // Unused in prod but added for test coverage
}

export type ErrorState = {
  errorType?: ErrorTypes;
};

function ErrorComponent() {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();
  const { state } = useLocation<ErrorState>();
  useDocumentTitle(t('common.errorTitle'));

  const translationMap = {
    [ErrorTypes.NOT_FOUND]: {
      header: t('product.notFound'),
      body: t('product.notFoundDescription'),
      actionText: t('common.backToHome'),
      actionLink: '/'
    },
    [ErrorTypes.BRANCH_ERROR]: {
      header: t('branch.notFound'),
      body: t('branch.notFoundDescription'),
      actionText: 'Contact Support',
      actionLink: '/support'
    },
    [ErrorTypes.OTHER]: {
      header: t('common.leak'),
      body: t('common.tryGoingBack'),
      actionText: t('common.backToHome'),
      actionLink: '/'
    }
  };
  const isNotFound = state?.errorType === ErrorTypes.NOT_FOUND;
  const isBranchError = state?.errorType === ErrorTypes.BRANCH_ERROR;

  return state?.errorType ? (
    <Box
      bgcolor={isSmallScreen ? 'transparent' : 'common.white'}
      display="flex"
      flex="1"
      mx={isSmallScreen ? 6 : 0}
    >
      <Container maxWidth="md" sx={{ flex: 1, display: 'flex !important' }}>
        <Grid
          container
          spacing={isSmallScreen ? 0 : 6}
          direction={isSmallScreen ? 'column' : 'row'}
        >
          <Hidden mdDown>
            <Grid item xs={12} md={6} position="relative">
              <Img src={leak_bg} alt="leaky pipe background" />
              {isNotFound || isBranchError ? (
                <Img src={stacked_boxes} alt="stacked boxes" />
              ) : (
                <Img src={leak} alt="leaky pipe" />
              )}
            </Grid>
          </Hidden>
          <Grid
            container
            item
            xs={12}
            md={5}
            direction="column"
            justifyContent="center"
            alignItems="center"
          >
            <Hidden mdUp>
              {isNotFound || isBranchError ? (
                <Box
                  component="img"
                  src={stacked_boxes}
                  alt="stacked boxes"
                  mb={5}
                  maxWidth={170}
                  height="auto"
                />
              ) : (
                <Box component="img" src={leak} alt="leaky pipe" mb={5} />
              )}
            </Hidden>
            {!isNotFound && !isBranchError && (
              <Typography
                variant={isSmallScreen ? 'h2' : 'h1'}
                color="primary02.main"
                fontWeight={400}
              >
                {t('common.ohNo')}
              </Typography>
            )}
            <Typography
              variant={isSmallScreen ? 'h5' : 'h3'}
              data-testid="product-not-found"
              pt={isSmallScreen ? 0.625 : 0.75}
            >
              {translationMap[state.errorType].header}
            </Typography>
            <Typography
              variant={isSmallScreen ? 'body1' : 'h5'}
              align="center"
              py={isSmallScreen ? 2 : 4}
              fontWeight={400}
            >
              {translationMap[state.errorType].body}
            </Typography>
            <Grid
              container
              spacing={isSmallScreen ? 2 : 3}
              flexDirection={
                isSmallScreen || isNotFound || isBranchError
                  ? 'column-reverse'
                  : 'row'
              }
              alignItems="center"
              px={4}
              pt={isSmallScreen ? 0.5 : 0}
            >
              <Grid item xs={12} md={isNotFound ? 6 : 12} minWidth={150}>
                <Link
                  to={translationMap[state.errorType].actionLink}
                  sx={{ display: 'flex' }}
                  component={RouterLink}
                  underline="none"
                >
                  <Button
                    variant="primary"
                    fullWidth
                    data-testid="back-to-home-button"
                  >
                    {translationMap[state.errorType].actionText}
                  </Button>
                </Link>
              </Grid>
            </Grid>
          </Grid>
        </Grid>
      </Container>
    </Box>
  ) : (
    <Redirect to="/" />
  );
}

export default ErrorComponent;
