import { useContext, useState, useEffect } from 'react';
import {
  Accordion,
  AccordionDetails,
  AccordionSummary,
  Skeleton,
  Typography
} from '@dialexa/reece-component-library';
import { findIndex, isEqual } from 'lodash-es';
import { ArrowDropDownIcon } from 'icons';

import SearchSubfilter from './SearchSubfilter';
import { camelToString } from 'utils/strings';
import {
  AggregationResults,
  Maybe,
  ProductAttribute,
  AggregationItem
} from 'generated/graphql';
import VirtualizedList from 'common/VirtualizedList';
import { BranchContext } from 'providers/BranchProvider';
import { useTranslation } from 'react-i18next';

type Props = {
  filter: keyof AggregationResults;
  subfilters: Maybe<AggregationItem>[];
  loading: boolean;
  skeletonTotal: number;
  selectedFilters: {
    attributeType: string;
    attributeValue: string;
  }[];
  handleToggle: (changedFilter: ProductAttribute) => void;
};

function SearchFilterList(props: Props) {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();

  /**
   * Context
   */
  const { shippingBranch, shippingBranchLoading } = useContext(BranchContext);

  /**
   * State
   */
  const [expanded, setExpanded] = useState<string[]>([]);
  const [isExpanded, setIsExpanded] = useState<boolean>(false);
  const stockDisabled =
    !!shippingBranch?.isPricingOnly && props.filter === 'inStockLocation';

  /**
   * Effects
   */
  useEffect(() => {
    let filteredItems: string[] = [];
    const filterOptions = [
      'brand',
      'environmentalOption',
      'productType',
      'line'
    ];
    if (props.selectedFilters.length > 0) {
      filteredItems = props.selectedFilters.map(
        (filteredItem) => filteredItem.attributeType
      );
      const updatedFilteredItems = filteredItems.map((item) => {
        if (filterOptions.includes(item)) {
          return item.concat('s');
        }
        return item;
      });
      setExpanded(updatedFilteredItems);
    } else {
      setExpanded(['inStockLocation']);
    }
  }, [props.selectedFilters]);

  /**
   * Data
   */
  const stockText =
    props.filter === 'inStockLocation'
      ? `${t('search.inStockAt')} ${shippingBranch?.name ?? ''}`
      : '';

  /**
   * Render
   */
  return (
    <Accordion
      key={props.filter}
      type="filter"
      onChange={(_event, isExpanded) => setIsExpanded(isExpanded)}
      expanded={expanded.includes(props.filter) || isExpanded}
      data-testid={`search-filter-list-${props.filter}`}
    >
      <AccordionSummary
        expandIcon={<ArrowDropDownIcon />}
        aria-controls="filter-content"
        id="filter-header"
      >
        {stockDisabled ? (
          <Typography color="primary03.main">
            {camelToString(props.filter)}
          </Typography>
        ) : (
          <Typography>{camelToString(props.filter)}</Typography>
        )}
      </AccordionSummary>
      <AccordionDetails
        sx={{
          flexDirection: 'column',
          px: 1,
          pb: 3
        }}
      >
        {props.loading &&
          Array(props.skeletonTotal).map((_, i) => (
            <Skeleton
              key={i}
              variant="rectangular"
              width="100%"
              height={32}
              sx={{ mb: 1 }}
            />
          ))}

        {!props.loading && props.subfilters && (
          <VirtualizedList
            defaultItemSize={40}
            maxHeight={320}
            dataArray={props.subfilters}
            renderItem={(subfilter, idx) =>
              !!subfilter && (
                <SearchSubfilter
                  filter={props.filter}
                  subfilter={subfilter}
                  checked={getChecked(idx)}
                  handleToggle={props.handleToggle}
                  loading={shippingBranchLoading}
                  stockText={stockText}
                  filterDisabled={stockDisabled}
                />
              )
            }
          />
        )}
      </AccordionDetails>
    </Accordion>
  );

  /**
   * Callback defs
   */
  function getChecked(index: number) {
    return (
      findIndex(props.selectedFilters, (currentFilter) =>
        isEqual(currentFilter, {
          attributeType: props.filter.replace(/s$/, ''),
          attributeValue: props.subfilters?.[index]?.value
        })
      ) !== -1
    );
  }
}

export default SearchFilterList;
