import React from 'react';

import { Box, Typography } from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { InfoIcon } from 'icons';

function ItemUnavailable() {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();

  return (
    <Typography
      variant="caption"
      sx={{
        color: 'primary02.main',
        display: 'flex',
        alignItems: 'center',
        pt: 1,
        left: -1
      }}
      data-testid="product-unavailable"
    >
      <Box component={InfoIcon} sx={{ color: 'primary02.main', pr: 1 }} />
      {t('orders.notAvailable')}
    </Typography>
  );
}

export default ItemUnavailable;
