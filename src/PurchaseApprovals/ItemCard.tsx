import React from 'react';

import {
  Box,
  Button,
  Grid,
  Link,
  Typography
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { ChevronRightIcon } from 'icons';
import { OrderItem } from 'PurchaseApprovals';

type Props = {
  order: OrderItem;
  onRowClick: (row: OrderItem) => void;
  rejectedUser?: boolean;
};

function ItemCard(props: Props) {
  /**
   * Custom hooks
   */
  const { t } = useTranslation();

  return (
    <Box
      borderBottom={1}
      py={2}
      width={1}
      sx={{ borderColor: 'secondary03.main' }}
    >
      <Grid container alignItems="flex-end">
        <Grid item>
          <Box pb={1}>
            <Typography
              sx={{
                fontWeight: 500,
                color: 'primary.main'
              }}
            >
              {t('common.orderNumber')}
            </Typography>
            <Link sx={{ color: 'primary02.main' }}>{props.order.orderId}</Link>
          </Box>
          <Typography
            sx={{
              fontWeight: 500,
              color: 'primary.main'
            }}
          >
            {t('common.submittedBy')}
          </Typography>
          {`${props.order.submittedByName}`}
        </Grid>
        <Grid
          item
          sx={{ display: 'flex', justifyContent: 'flex-end', flexGrow: 1 }}
        >
          <Button
            onClick={() => props?.onRowClick(props.order)}
            endIcon={<ChevronRightIcon />}
            variant="text"
            data-testid={`${props.order.orderId}-view-order-button`}
            sx={{ p: 0 }}
          >
            {t('common.viewOrder')}
          </Button>
        </Grid>
      </Grid>
    </Box>
  );
}

export default ItemCard;
