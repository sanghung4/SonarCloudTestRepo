import { useMemo, useCallback, useContext } from 'react';
import {
  Box,
  Button,
  Chip,
  Collapse,
  Hidden,
  Skeleton,
  Typography
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { CloseIcon } from 'icons';
import useSearchQueryParams from './util/useSearchQueryParams';
import {
  handleToggleLogics,
  getSelectedFiltersArray
} from './util/searchFiltersUtil';
import { ProductAttribute, Maybe, AggregationResults } from 'generated/graphql';
import { getTypedKeys } from 'utils/getTypedKeys';
import { BranchContext } from 'providers/BranchProvider';
import SearchFilterList from './SearchFilterList';
import { AuthContext } from 'AuthProvider';

type Props = {
  filters?: Maybe<AggregationResults>;
  loading: boolean;
  onClear: () => void;
};

function SearchFiltersContent(props: Props) {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();
  const [params, setParams] = useSearchQueryParams();
  const { categories = [], filters = [] } = params;

  /**
   * Context
   */
  const { shippingBranch } = useContext(BranchContext);
  const { profile } = useContext(AuthContext);

  /**
   * Memos
   */
  const selectedFilters = useMemo(selectedFiltersMemo, [filters]);

  /**
   * Callbacks
   */
  const handleToggle = useCallback(handleToggleCb, [
    categories,
    params,
    selectedFilters,
    setParams
  ]);

  /**
   * Render
   */
  return (
    <Box display="flex" flexDirection="column" width="1">
      <Hidden mdUp>
        <Collapse in={filters.length > 0}>
          <Box pt={1} pb={2} overflow="hidden">
            <Typography
              color="primary"
              variant="body1"
              fontWeight={500}
              py={1}
              px={4}
            >
              {t('common.filters')}
            </Typography>
            <Box display="flex" overflow="auto hidden" px={3}>
              {selectedFilters.map((selectedFilter) => (
                <Box key={selectedFilter.attributeValue} p={1}>
                  <Chip
                    avatar={<CloseIcon />}
                    onClick={() =>
                      handleToggle(selectedFilter as ProductAttribute)
                    }
                    label={`${t(`search.${selectedFilter.attributeType}`)}: ${
                      selectedFilter.attributeType === 'inStockLocation'
                        ? shippingBranch?.name
                        : selectedFilter?.attributeValue
                    }`}
                    styleVariant="stroke"
                  />
                </Box>
              ))}
              <Box
                sx={{ width: (theme) => theme.spacing(3) }}
                flex="1 0 auto"
              />
            </Box>
          </Box>
        </Collapse>
      </Hidden>
      <Hidden mdDown>
        <Collapse in={!!selectedFilters.length}>
          <Box
            px={5}
            py={3}
            display="flex"
            flexDirection="column"
            borderTop={1}
            borderColor="secondary03.main"
          >
            <Box
              pb={1}
              color="primary.main"
              fontWeight={500}
              letterSpacing="0.005em"
              display="flex"
              justifyContent="space-between"
              alignItems="center"
              sx={(theme) => ({
                fontSize: theme.typography.pxToRem(14),
                lineHeight: theme.typography.pxToRem(22)
              })}
            >
              {t('search.selectedItem', {
                count: selectedFilters.length + categories.length
              })}
              <Button
                variant="inline"
                color="primaryLight"
                onClick={props.onClear}
              >
                <Typography variant="caption">
                  {t('search.clearAll')}
                </Typography>
              </Button>
            </Box>
            {selectedFilters.map((selectedFilter) => (
              <Box
                key={selectedFilter?.attributeValue}
                px={0.5}
                py={1}
                display="flex"
                alignItems="center"
                onClick={() => handleToggle(selectedFilter as ProductAttribute)}
                sx={{ cursor: 'pointer' }}
              >
                <Box
                  component={CloseIcon}
                  mr={1.5}
                  height={16}
                  width={16}
                  color="primary.main"
                />
                <Typography variant="caption">
                  {`${t(`search.${selectedFilter?.attributeType}`)}: ${
                    selectedFilter.attributeType === 'inStockLocation'
                      ? shippingBranch?.name
                      : selectedFilter?.attributeValue
                  }`}
                </Typography>
              </Box>
            ))}
          </Box>
        </Collapse>
      </Hidden>
      {!props.filters &&
        Array(2).map((_, i) => (
          <Skeleton
            key={i}
            variant="rectangular"
            width="100%"
            height={56}
            sx={{ mb: 0.5 }}
          />
        ))}
      {props.filters &&
        getTypedKeys(props.filters)
          .filter((f) => props.filters?.[f]?.length)
          .map((filter) => {
            const subfilters =
              filter === '__typename'
                ? undefined
                : props.filters?.[filter] ?? undefined;

            if (
              !(filter === 'category' && categories.length === 3) &&
              !(filter === 'inStockLocation' && !profile?.userId) &&
              subfilters
            ) {
              return (
                <SearchFilterList
                  filter={filter}
                  subfilters={subfilters}
                  loading={props.loading}
                  skeletonTotal={categories.length ?? 0 ? 1 : 3}
                  selectedFilters={selectedFilters}
                  handleToggle={handleToggle}
                  key={filter}
                />
              );
            }

            return null;
          })}
    </Box>
  );

  /**
   * Memo Definitions
   */
  function selectedFiltersMemo() {
    return getSelectedFiltersArray(filters);
  }

  /**
   * Callback Definitions
   */
  function handleToggleCb(changedFilter: ProductAttribute) {
    handleToggleLogics({
      categories,
      changedFilter,
      params,
      selectedFilters,
      setParams
    });
  }
}

export default SearchFiltersContent;
