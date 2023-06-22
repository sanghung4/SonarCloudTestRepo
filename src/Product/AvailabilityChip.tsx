import { useContext, useMemo } from 'react';

import { useTranslation } from 'react-i18next';

import { AuthContext } from 'AuthProvider';
import { BranchContext } from 'providers/BranchProvider';
import { DeliveryIcon, InStockIcon, QuickOrder } from 'icons';
import InfoChip from 'common/InfoChip';
import { Branch } from 'generated/graphql';

type Props = {
  loading?: boolean;
  index?: number;
  branch?: Branch;
  stock?: number;
};

function AvailabilityChip(props: Props) {
  /**
   * Props
   */
  const { stock, loading, branch, index } = props;

  /**
   * Custom hooks
   */
  const { t } = useTranslation();

  /**
   * Context
   */
  const { authState } = useContext(AuthContext);
  const { shippingBranchLoading } = useContext(BranchContext);

  /**
   * Memo
   */
  const chipDisplay = useMemo(chipDisplayMemo, [
    stock,
    branch?.isPricingOnly,
    t
  ]);

  // Don't render anything if the user is not logged in
  if (!authState?.isAuthenticated) {
    return null;
  }

  return (
    <InfoChip
      testId={
        index === undefined
          ? 'availability-stock-chip'
          : `availability-stock-chip-${index}`
      }
      loading={loading || shippingBranchLoading}
      {...chipDisplay}
    />
  );

  /**
   * Memo definitions
   */
  function chipDisplayMemo() {
    // If there is stock
    if (stock) {
      const numberAvailable = stock > 999 ? '999+' : stock;

      // if the branch is EHG
      if (branch?.isPricingOnly) {
        return {
          text: `${numberAvailable} ${t('common.readyToShip')}`,
          icon: <DeliveryIcon />,
          color: 'primary',
          contained: true
        };
      }

      // if the branch is NOT EHG
      return {
        text: `${numberAvailable} ${t('common.inStock')}`,
        icon: <InStockIcon />,
        color: 'success',
        contained: true
      };
    }

    // No stock is undefined or 0
    return {
      text: t('common.availableForOrder'),
      icon: <QuickOrder />
    };
  }
}

export default AvailabilityChip;
