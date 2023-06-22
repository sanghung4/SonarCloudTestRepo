import { Dispatch, ReactNode, useContext, useState } from 'react';

import {
  Box,
  Button,
  Collapse,
  Grid,
  Skeleton,
  Typography
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { kebabCase } from 'lodash-es';

import { BranchContext } from 'providers/BranchProvider';
import { Branch } from 'generated/graphql';
import { ArrowDropDownIcon, ArrowDropUpIcon } from 'icons';
import { branchCardDisplayData } from 'Branches/util/branchCardDisplayData';
import AvailabilityChip from 'Product/AvailabilityChip';

type Props = {
  header?: String;
  branch?: Branch;
  setShippingBranch?: Dispatch<string>;
  loading?: boolean;
  availabilityLoading?: boolean;
  icon?: ReactNode;
  stock?: number;
};

export default function BranchCard(props: Props) {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();

  /**
   * State
   */
  const [moreDetails, setMoreDetails] = useState(false);

  /**
   * Context
   */
  const {
    homeBranch,
    shippingBranch,
    shippingBranchLoading,
    isLocationDistance
  } = useContext(BranchContext);
  const isHomeBranch = homeBranch?.branchId === props.branch?.branchId;
  const isShippingBranch = shippingBranch?.branchId === props.branch?.branchId;

  /**
   * Misc
   */
  const {
    branchName,
    branchAddress,
    branchCityStateZip,
    branchHours,
    branchDistance
  } = branchCardDisplayData(t, props.branch);

  const showStock = !(props.stock === undefined || props.stock === null);

  /**
   * Render
   */
  return (
    <Box py={2.5}>
      {!!props.header && (
        <Typography color="success.main">{props.header}</Typography>
      )}
      <Grid container>
        <Grid item xs={8}>
          {/* ===== Branch name ===== */}
          <Box display="flex" alignItems="bottom">
            {props.loading ? (
              <Skeleton width={200} />
            ) : (
              <>
                {!!props.icon && (
                  <Box component="span" mr={1}>
                    {props.icon}
                  </Box>
                )}
                <Typography
                  color="primary"
                  fontWeight={700}
                  data-testid={kebabCase(`${branchName}-header`)}
                >
                  {branchName}
                </Typography>
              </>
            )}
          </Box>
          {/* ===== Branch address ===== */}
          <Box>
            {props.loading ? (
              <Skeleton width={200} />
            ) : (
              <Typography data-testid="address1">{branchAddress}</Typography>
            )}
          </Box>
          {/* ===== Branch city/state/zip ===== */}
          <Box>
            {props.loading ? (
              <Skeleton width={200} />
            ) : (
              <Typography data-testid="branch-city-state-zip">
                {branchCityStateZip}
              </Typography>
            )}
          </Box>
          {/*  ===== Store details button + collapsed details ===== */}
          {props.loading ? (
            <Skeleton width={128} height={32} />
          ) : (
            !!(props.branch?.phone || props.branch?.businessHours) && (
              <>
                {/* ----- button ----- */}
                <Button
                  endIcon={
                    moreDetails ? <ArrowDropUpIcon /> : <ArrowDropDownIcon />
                  }
                  data-testid={`${props.branch?.branchId}-store-details`}
                  onClick={() => setMoreDetails(!moreDetails)}
                  color="gray"
                  iconColor="primary"
                  variant="text"
                  size="small"
                  sx={{ mt: '1rem', p: 0 }}
                >
                  {t('branch.storeDetails')}
                </Button>
                {/* ----- details ----- */}
                <Collapse in={moreDetails} sx={{ m: '0 1em' }}>
                  {/* Business Hours */}
                  {!!props.branch?.businessHours && (
                    <Box>
                      <Typography
                        variant="body2"
                        color="primary"
                        fontWeight={700}
                      >
                        {t('common.hours')}
                      </Typography>
                      {branchHours.map((eachHours, i) => (
                        <Typography variant="body2" key={i}>
                          {eachHours}
                        </Typography>
                      ))}
                    </Box>
                  )}
                  {/* Phone */}
                  {!!props.branch?.phone && (
                    <Box>
                      <Typography
                        variant="body2"
                        color="primary"
                        fontWeight={700}
                      >
                        {t('common.phone')}
                      </Typography>
                      <Typography
                        variant="body2"
                        data-testid="branch-phone-number"
                      >
                        {props.branch.phone}
                      </Typography>
                    </Box>
                  )}
                </Collapse>
              </>
            )
          )}
        </Grid>
        <Grid
          item
          container
          xs={4}
          direction="column"
          justifyContent="flex-end"
        >
          {!props.loading ? (
            <>
              {/* ===== Distance ===== */}
              {!!(
                props.branch?.distance &&
                (isLocationDistance || !isHomeBranch)
              ) && (
                <Box mb={3} textAlign="right">
                  <Typography data-testid="branch-distance">
                    {branchDistance}
                  </Typography>
                  <Typography
                    variant="caption"
                    data-testid={`${
                      isLocationDistance ? 'from-location' : 'from-branch'
                    }`}
                  >
                    {isLocationDistance
                      ? t('branch.fromLocation')
                      : t('branch.fromBranch')}
                  </Typography>
                </Box>
              )}
              {/* ===== Stock ===== */}
              {showStock && (
                <Grid item>
                  <Grid
                    xs={12}
                    container
                    alignItems="flex-end"
                    direction="column"
                  >
                    <Grid item mb={1}>
                      <AvailabilityChip
                        branch={props.branch}
                        loading={
                          shippingBranchLoading || props.availabilityLoading
                        }
                        stock={props.stock}
                      />
                    </Grid>
                  </Grid>
                </Grid>
              )}
              {/* ===== Select button ===== */}
              <Button
                data-testid={`${props.branch?.branchId}-select-branch-button`}
                onClick={() =>
                  props.setShippingBranch?.(props.branch?.branchId ?? '')
                }
                color={isShippingBranch ? 'success' : 'primary'}
                variant={isShippingBranch ? 'primary' : 'secondary'}
              >
                {isShippingBranch ? t('common.selected') : t('common.select')}
              </Button>
            </>
          ) : (
            <>
              <Box mb={3}>
                <Skeleton />
                <Skeleton />
              </Box>
              <Skeleton variant="rectangular" height={36} />
            </>
          )}
        </Grid>
      </Grid>
    </Box>
  );
}
