import { useContext } from 'react';

import {
  Button,
  Grid,
  Hidden,
  TextField,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { ContractContext } from 'Contract/ContractProvider';
import { VALUE_OVER_10MIL } from 'Cart/util';
import { valueOfReleasingContract } from './util';

type Props = {
  hasSearch?: boolean;
  count: number;
  searchApplied: string;
  setSearchApplied: (search: string) => void;
  testId: string;
};

export default function ContractProductListControls(props: Props) {
  /**
   * Custom hooks
   */
  const { t } = useTranslation();
  const { isSmallScreen } = useScreenSize();

  /**
   * Context
   */
  const {
    handleQtyClear,
    handleSearchInputChange,
    qtyInputMap,
    search,
    setSearch,
    handleReleaseAll,
    handleReleaseOver10mil,
    setIsReviewReady,
    contractData
  } = useContext(ContractContext);
  const itemsToRelease = Object.keys(qtyInputMap).length;

  return (
    <Grid container px={isSmallScreen ? 2 : 4} py={isSmallScreen ? 1 : 3}>
      {props.hasSearch ? (
        <Grid item container xs={isSmallScreen ? 12 : 6} flexDirection="column">
          <Grid item>
            <Typography
              component="h5"
              fontWeight={500}
              fontSize={isSmallScreen ? 16 : 20}
            >
              {t('contract.searchProducts')}
            </Typography>
          </Grid>
          <Grid
            item
            container
            alignItems="center"
            spacing={isSmallScreen ? 0 : 2}
          >
            <Grid item xs={isSmallScreen ? 12 : true}>
              <TextField
                fullWidth
                value={search}
                placeholder={t('contract.searchPlaceholder')}
                onChange={handleSearchInputChange}
                inputProps={{
                  'data-testid': `contract-search-input-${props.testId}`
                }}
                sx={{ mt: 0 }}
              />
            </Grid>
            <Grid
              item
              container
              xs={isSmallScreen ? 12 : 'auto'}
              flexDirection="row"
              alignItems="center"
              justifyContent={isSmallScreen ? 'flex-end' : 'flex-start'}
            >
              <Grid item>
                <Button
                  variant="primary"
                  disabled={!search}
                  onClick={handleSearch}
                  data-testid={`contract-search-button-${props.testId}`}
                >
                  {t('common.search')}
                </Button>
              </Grid>
              <Grid item>
                <Button
                  color="primaryLight"
                  variant="inline"
                  onClick={handleSearchReset}
                  data-testid={`contract-searchclear-button-${props.testId}`}
                  disabled={!search && !props.searchApplied}
                >
                  {t('common.clear')}
                </Button>
              </Grid>
            </Grid>
          </Grid>
        </Grid>
      ) : (
        <Grid item xs />
      )}
      <Grid item container xs={isSmallScreen ? 12 : 6}>
        <Grid
          item
          container
          justifyContent={isSmallScreen ? 'center' : 'flex-end'}
          alignItems="center"
          spacing={isSmallScreen ? 1 : 2}
          height={isSmallScreen ? 176 : undefined}
          pt={4}
        >
          <Hidden mdDown>
            <Grid item>
              <Button
                variant="text"
                fullWidth={isSmallScreen}
                disabled={!props.count}
                onClick={handleReleaseAll}
                data-testid={`contract-releaseall-button-desktop-${props.testId}`}
                color="primaryLight"
                sx={{ textDecoration: 'underline' }}
              >
                {t('contract.releaseAll', { count: props.count })}
              </Button>
            </Grid>
          </Hidden>
          <Grid item xs="auto">
            <Button
              variant="secondary"
              disabled={!itemsToRelease}
              onClick={handleQtyClear}
              data-testid={`contract-clearqtys-button-${props.testId}`}
              sx={{ whiteSpace: 'nowrap' }}
            >
              {t('contract.clearQtys')}
            </Button>
          </Grid>
          <Grid item xs="auto">
            <Button
              fullWidth={isSmallScreen}
              disabled={!itemsToRelease}
              onClick={handleReleaseToCart}
              data-testid={`contract-releasetocart-button-${props.testId}`}
              sx={{ whiteSpace: 'nowrap' }}
            >
              {t('contract.releaseToCart')}
              {!!itemsToRelease && ` (${itemsToRelease})`}
            </Button>
          </Grid>
          <Hidden mdUp>
            <Grid item container xs={12} justifyContent="center">
              <Button
                variant="text"
                onClick={handleReleaseAll}
                disabled={!props.count}
                data-testid={`contract-releaseall-button-mobile-${props.testId}`}
                color="primaryLight"
                sx={{ textDecoration: 'underline' }}
              >
                {t('contract.releaseAll', { count: props.count })}
              </Button>
            </Grid>
          </Hidden>
        </Grid>
      </Grid>
    </Grid>
  );

  /**
   * Handle Event
   */
  function handleSearch() {
    props.setSearchApplied(search);
  }
  function handleSearchReset() {
    setSearch('');
    props.setSearchApplied('');
  }
  function handleReleaseToCart() {
    const value = valueOfReleasingContract(contractData, qtyInputMap);
    if (value > VALUE_OVER_10MIL) {
      handleReleaseOver10mil();
      return;
    }
    setIsReviewReady(true);
  }
}
