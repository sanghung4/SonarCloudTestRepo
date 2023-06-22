import { Dispatch, useCallback, useMemo } from 'react';

import {
  Box,
  Button,
  CircularProgress,
  Drawer,
  IconButton,
  Typography,
  alpha,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import useSearchQueryParams from 'Search/util/useSearchQueryParams';
import { getSelectedFiltersArray } from 'Search/util/searchFiltersUtil';
import { AggregationResults, Maybe } from 'generated/graphql';
import { CloseIcon } from 'icons';
import SearchFiltersContent from './SearchFiltersContent';

/**
 * Types
 */
export type Props = {
  loading: boolean;
  count: number;
  filters?: Maybe<AggregationResults>;
  filtersOpen: boolean;
  onShowHide: Dispatch<boolean>;
};

export default function SearchFilters(props: Props) {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();
  const [params, setParams] = useSearchQueryParams();
  const { filters = [], criteria: searchTerm } = params;

  /**
   * Memos
   */
  const selectedFilters = useMemo(selectedFiltersMemo, [filters]);

  /**
   * Callbacks
   */
  const handleClear = useCallback(handleClearCb, [searchTerm, setParams]);

  return isSmallScreen ? (
    <Drawer
      PaperProps={{ sx: { bottom: 0 } }}
      anchor="top"
      open={props.filtersOpen}
      onClose={() => props.onShowHide(false)}
    >
      <Box display="flex" flexDirection="column" minHeight={1}>
        <Box
          px={3}
          pt={5.5}
          pb={1.5}
          display="flex"
          alignItems="center"
          sx={{
            borderBottom: 1,
            borderBottomColor: 'lighterGray.main'
          }}
        >
          <Box flex="1" />
          <Typography
            variant="h5"
            component="span"
            color="primary"
            sx={{ fontWeight: 700 }}
          >
            {t('common.filter')}
          </Typography>
          <Box display="flex" flex="1" justifyContent="flex-end">
            <IconButton
              onClick={() => props.onShowHide(false)}
              size="large"
              sx={{ p: 0 }}
            >
              <CloseIcon data-testId="close-icon" />
            </IconButton>
          </Box>
        </Box>
        <Box flex="1" overflow="auto">
          <SearchFiltersContent
            loading={props.loading}
            filters={props.filters}
            onClear={handleClear}
          />
        </Box>
        <Box
          display="flex"
          p={4}
          sx={(theme) => ({
            borderTop: 1,
            borderTopColor: 'lightGray.main',
            boxShadow: `0 -2px 4px 0 ${alpha(theme.palette.common.black, 0.15)}`
          })}
        >
          <Button
            data-testId="search-filter-button"
            onClick={() => props.onShowHide(false)}
            sx={{ flex: 1 }}
          >
            {`${t('search.showResults')} (`}
            {props.loading ? (
              <CircularProgress
                size="1em"
                sx={{
                  display: 'flex',
                  my: 0,
                  mx: 0.5,
                  '& .MuiCircularProgressReece-bottom': {
                    color: 'primary.main'
                  }
                }}
              />
            ) : (
              props.count
            )}
            {')'}
          </Button>
          {selectedFilters.length > 0 ? (
            <Button variant="text" onClick={handleClear} sx={{ ml: 3 }}>
              {t('common.clear')}
            </Button>
          ) : null}
        </Box>
      </Box>
    </Drawer>
  ) : (
    <SearchFiltersContent
      loading={props.loading}
      filters={props.filters}
      onClear={handleClear}
    />
  );

  /**
   * Memo defs
   */
  function selectedFiltersMemo() {
    return getSelectedFiltersArray(filters);
  }

  /**
   * Callback defs
   */
  function handleClearCb() {
    setParams({
      criteria: searchTerm
    });
  }
}
