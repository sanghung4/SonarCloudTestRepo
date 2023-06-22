import { useContext, useMemo, useRef, useState, useCallback } from 'react';

import {
  Box,
  Button,
  Divider,
  Typography,
  Grid,
  FormControl,
  InputLabel,
  MenuItem,
  Select
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import BranchCard from 'Branches/BranchCard';
import { BranchContext, Divisions } from 'providers/BranchProvider';
import { MAX_PAGES, NUM_LOADING, PAGE_SIZE } from 'Branches/util/config';
import Loader from 'common/Loader';
import Sidebar from 'common/Sidebar';
import { BranchIcon, WarningIcon } from 'icons';
import { SelectChangeEvent } from '@mui/material/Select';
import { useGetProductInventoryQuery } from 'generated/graphql';
import { useCartContext } from 'providers/CartProvider';
import {
  BranchWarningContainer,
  BranchWarningTypography
} from 'Branches/util/styles';
import { MAX_BRANCH_DISTANCE } from 'Branches/util/branchCardDisplayData';

export default function BranchSidebar() {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();

  /**
   * State
   */
  const [resultCount, setResultCount] = useState(PAGE_SIZE);

  /**
   * Refs
   */
  const firstElRef = useRef<HTMLDivElement>(null);
  const lastElRef = useRef<HTMLDivElement>(null);

  /**
   * Context
   */
  const {
    branchSelectOpen,
    homeBranch,
    homeBranchLoading,
    nearbyBranches,
    nearbyBranchesLoading,
    shippingBranch,
    shippingBranchLoading,
    division,
    productId,
    setBranchSelectOpen,
    setShippingBranch,
    setDivision,
    setProductId
  } = useContext(BranchContext);
  const { itemCount } = useCartContext();

  /**
   * Data
   */
  const { data: availability, loading: availabilityLoading } =
    useGetProductInventoryQuery({
      skip: !productId,
      variables: {
        productId: productId ?? ''
      }
    });

  /**
   * Memos
   */

  const getAvailability = useCallback(getBranchProductAvailability, [
    availability,
    productId
  ]);

  const results = useMemo(resultsMemo, [
    nearbyBranches,
    shippingBranch,
    division
  ]);

  const orderedAvailability = useMemo(orderedAvailabilityMemo, [
    results,
    getAvailability
  ]);

  const filteredResults = useMemo(filteredResultsMemo, [
    productId,
    homeBranch,
    results,
    orderedAvailability
  ]);

  const slicedResults = useMemo(slicedResultsMemo, [
    filteredResults,
    resultCount
  ]);

  const showSeeMore = useMemo(seeMoreMemo, [
    productId,
    filteredResults,
    results,
    resultCount
  ]);

  /**
   * Callbacks
   */

  const handleSeeMoreClicked = () => {
    // It is impossible to execute this as falsey in tests but it is placed here for safety measures
    // istanbul ignore next
    if (MAX_PAGES * PAGE_SIZE > resultCount) {
      setResultCount(resultCount + PAGE_SIZE);
    }

    const option: ScrollIntoViewOptions = { block: 'end', behavior: 'smooth' };
    setTimeout(() => lastElRef.current?.scrollIntoView(option), 0);
  };

  function handleApplyDivision(e: SelectChangeEvent<unknown>) {
    // istanbul ignore next
    setDivision(e.target.value as Divisions);
  }

  function handleClose() {
    setBranchSelectOpen(false);
    setDivision(Divisions.NONE);
    setProductId(undefined);
  }

  /**
   * Render
   */
  return (
    <Sidebar
      on={branchSelectOpen}
      close={handleClose}
      title={t('branch.changeBranch')}
      isContentLoading={homeBranchLoading || shippingBranchLoading}
      widthOverride={512}
    >
      {itemCount > 0 && (
        <BranchWarningContainer data-testid="change-branch-warning-message">
          <Box component={WarningIcon} ml={2} mr={1} color="secondary.main" />
          <Box flex={1} ml={1}>
            <BranchWarningTypography>
              {t('branch.warningMessage')}
            </BranchWarningTypography>
          </Box>
        </BranchWarningContainer>
      )}
      <Box flex="1 1 auto" overflow="auto" px={3}>
        {shippingBranchLoading && <Loader backdrop />}
        <Box ref={firstElRef}>
          <BranchCard
            branch={homeBranch}
            header={t('branch.homeBranch')}
            loading={homeBranchLoading}
            availabilityLoading={availabilityLoading}
            stock={
              productId
                ? orderedAvailability.get(homeBranch?.branchId)
                : undefined
            }
            icon={<BranchIcon />}
            setShippingBranch={setShippingBranch}
          />
          {/* Location Types Filter */}
          <Grid item xs={8} md={12}>
            <Box py={2} width={1}>
              <FormControl variant="outlined" fullWidth>
                <InputLabel id="branch-filter-label" shrink>
                  {t('locationSearch.locationTypesLabel')}
                </InputLabel>
                <Select
                  labelId="branch-filter-label"
                  id="branch-filter-select"
                  data-testid="branch-filter-select"
                  value={division}
                  onChange={handleApplyDivision}
                  renderValue={(v) => t(`locationSearch.${v}`)}
                >
                  {Object.keys(Divisions).map((branch) => (
                    <MenuItem
                      value={Divisions[branch as keyof typeof Divisions]}
                      key={`branch-filter-select-${branch}`}
                      data-testid={`${branch}-list-item`}
                    >
                      {t(
                        `locationSearch.${
                          Divisions[branch as keyof typeof Divisions]
                        }`
                      )}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Box>
          </Grid>
        </Box>
        <Divider />
        {nearbyBranchesLoading
          ? [...new Array(NUM_LOADING).keys()].map((i) => (
              <Box key={i}>
                <BranchCard loading availabilityLoading />
                {i < NUM_LOADING - 1 && <Divider />}
              </Box>
            ))
          : slicedResults.map((branch, i) => (
              <Box
                ref={i === resultCount - 1 ? lastElRef : null}
                key={branch.branchId}
              >
                <BranchCard
                  branch={branch}
                  header={
                    !i && branch.branchId === shippingBranch?.branchId
                      ? t('branch.selectedBranch')
                      : ''
                  }
                  setShippingBranch={setShippingBranch}
                  stock={
                    productId
                      ? orderedAvailability.get(branch.branchId)
                      : undefined
                  }
                  availabilityLoading={availabilityLoading}
                  loading={false}
                />
                {i < resultCount - 1 && <Divider />}
              </Box>
            ))}
      </Box>
      {showSeeMore && (
        <Box
          visibility={
            homeBranchLoading || nearbyBranchesLoading ? 'hidden' : undefined
          }
          zIndex={homeBranchLoading || nearbyBranchesLoading ? -1 : undefined}
          boxShadow={3}
          display="flex"
          justifyContent="center"
          p={2}
        >
          <Button
            color="primaryLight"
            variant="inline"
            onClick={handleSeeMoreClicked}
            data-testid="see-more-branches-button"
          >
            {t('branch.moreBranches')}
          </Button>
        </Box>
      )}
      {!nearbyBranchesLoading && !nearbyBranches?.length && branchSelectOpen && (
        <Box py={2}>
          <Typography align="center" variant="body1" color="error">
            {t('branch.noNearbyBranches', { radius: MAX_BRANCH_DISTANCE })}
          </Typography>
        </Box>
      )}
    </Sidebar>
  );

  /**
   * Memo Defs
   */

  function resultsMemo() {
    const filteredByDivision = nearbyBranches?.filter((b) => {
      switch (division) {
        case Divisions.WATERWORKS:
          return b.isWaterworks;
        case Divisions.HVAC:
          return b.isHvac;
        case Divisions.PLUMBING:
          return b.isPlumbing;
        case Divisions.BANDK:
          return b.isBandK;
        default:
          return true;
      }
    });

    const sorted = filteredByDivision?.sort((a, b) =>
      a.branchId === shippingBranch?.branchId
        ? -1
        : b.distance ?? 0 - Number(a.distance)
    );

    return sorted ?? [];
  }

  function filteredResultsMemo() {
    const availabilityFiltered = results
      ?.filter((branch) => {
        if (productId) {
          return orderedAvailability.get(branch.branchId) !== 0;
        } else {
          return true;
        }
      })
      ?.filter((b) => b.branchId !== homeBranch?.branchId);

    return availabilityFiltered;
  }

  function slicedResultsMemo() {
    const sliced = filteredResults.slice(0, resultCount);

    return sliced;
  }

  function orderedAvailabilityMemo() {
    let orderedAvailability = new Map();

    results.forEach((branch, i) => {
      const availableOnHand = getAvailability(branch.branchId);
      orderedAvailability.set(branch.branchId, availableOnHand ?? 0);
    });

    return orderedAvailability;
  }

  function seeMoreMemo() {
    if (productId) {
      return resultCount < filteredResults.length;
    } else {
      //+1 for homeBranch
      return resultCount + 1 < results.length;
    }
  }

  /**
   * Helper Functions
   */
  function getBranchProductAvailability(branchId: string) {
    if (productId && availability) {
      const availableCount =
        availability?.productInventory.inventory.find(
          (b) => b.branchId === branchId
        )?.availableOnHand ?? 0;
      return availableCount;
    } else {
      return undefined;
    }
  }
}
