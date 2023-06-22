import { useContext } from 'react';

import { Button, Skeleton } from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import ItemUnavailable from 'common/ItemUnavailable';
import { ListLineItem, ProductPricing } from 'generated/graphql';
import 'Lists/ListsLineItem.scss';
import AvailabilityChip from 'Product/AvailabilityChip';
import { BranchContext } from 'providers/BranchProvider';
import { BranchIcon } from 'icons';

/**
 * Types
 */
type Props = {
  item: ListLineItem;
  loading?: boolean;
  notAvailable: boolean;
  priceData?: ProductPricing;
  priceDataLoading?: boolean;
};

/**
 * Component
 */
function ListLineItemAvailability(props: Props) {
  /**
   * Props
   */
  const { item, loading, priceData, notAvailable, priceDataLoading } = props;

  /**
   * Custom hooks
   */
  const { t } = useTranslation();

  /**
   * Context
   */
  const { setProductId, setBranchSelectOpen, shippingBranch } =
    useContext(BranchContext);

  /**
   * Misc
   */
  // Display
  const isAvailable = Boolean(!notAvailable || loading);
  // CSS Util
  const cn = (name: string) =>
    'lists-line-item__product--branch-available' + name;

  /**
   * Render
   */
  return (
    <div className={cn('--container')}>
      {/* No longer available */}
      {!isAvailable && <ItemUnavailable />}
      {/* Availability chip */}
      <div className={cn('--available-chip-box')} data-testid="stock-chip-box">
        {Boolean(!notAvailable || loading) && (
          <AvailabilityChip
            branch={shippingBranch}
            loading={Boolean(loading || priceDataLoading)}
            stock={
              shippingBranch?.isPricingOnly
                ? priceData?.totalAvailableQty
                : priceData?.branchAvailableQty
            }
          />
        )}
      </div>
      {!notAvailable && (
        <div className={cn('--location-box')}>
          {loading ? (
            <Skeleton />
          ) : (
            <Button
              color="primary"
              data-testid="check-nearby-branches-button"
              size="small"
              variant="inline"
              startIcon={<BranchIcon />}
              onClick={() => {
                setBranchSelectOpen(true);
                setProductId(item?.erpPartNumber ?? '');
              }}
            >
              {t('product.checkNearByBranches')}
            </Button>
          )}
        </div>
      )}
    </div>
  );
}

export default ListLineItemAvailability;
