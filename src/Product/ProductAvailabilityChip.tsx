import React, { useContext, useMemo } from 'react';

import { Skeleton, Box } from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { AuthContext } from 'AuthProvider';
import { BranchContext } from 'providers/BranchProvider';
import { DeliveryIcon, InStockIcon, QuickOrder } from 'icons';
import {
  AvailabilityInfoTypography,
  AvailabilityTypography,
  StockAvailabilityContainer
} from 'Product/util/styled';

type Props = {
  loading: boolean;
  stock?: number;
};

function ProductAvailabilityChip({ stock, loading }: Props) {
  /**
   * Custom hooks
   */
  const { t } = useTranslation();

  /**
   * Context
   */
  const { authState } = useContext(AuthContext);
  const { shippingBranchLoading, shippingBranch } = useContext(BranchContext);

  /**
   * Memo
   */
  const content = useMemo(contentMemo, [shippingBranch, stock, t]);

  // Don't render anything if the user is not logged in
  if (!authState?.isAuthenticated) return null;

  // Loading state
  if (loading || shippingBranchLoading) {
    return <Skeleton variant="rectangular" height={56} width={256} />;
  }

  return (
    <StockAvailabilityContainer bgcolor={content.backgroundColor}>
      <Box
        component={content.icon}
        color={content.iconColor}
        height={32}
        width={32}
      />
      <Box display="flex" flexDirection="column">
        <AvailabilityTypography color={content.primaryTextColor}>
          {content.primaryText}
        </AvailabilityTypography>
        <AvailabilityInfoTypography>
          {content.secondaryText}
        </AvailabilityInfoTypography>
      </Box>
    </StockAvailabilityContainer>
  );

  /**
   * Memo definitions
   */
  function contentMemo() {
    if (stock) {
      if (shippingBranch?.isPricingOnly) {
        return {
          backgroundColor: '#F3F7FE',
          icon: DeliveryIcon,
          iconColor: 'primary.main',
          primaryTextColor: 'primary.main',
          primaryText: `${stock > 999 ? '999+' : stock} ${t(
            'common.readyToShip'
          )}`,
          secondaryText: `shipping to ${shippingBranch.zip}`
        };
      }

      return {
        backgroundColor: '#ECF2E9',
        icon: InStockIcon,
        iconColor: '#407A26',
        primaryTextColor: 'success.main',
        primaryText: `${stock > 999 ? '999+' : stock} ${t('common.inStock')}`,
        secondaryText: `${t('common.at')} ${shippingBranch?.name}, ${
          shippingBranch?.city
        }`
      };
    }
    return {
      icon: QuickOrder,
      backgroundColor: 'secondary04.main',
      iconColor: 'text.primary',
      primaryTextColor: 'text.primary',
      primaryText: t('common.availableForOrder'),
      secondaryText: `${t('common.stockAvailabilityInfo')}`
    };
  }
}

export default ProductAvailabilityChip;
