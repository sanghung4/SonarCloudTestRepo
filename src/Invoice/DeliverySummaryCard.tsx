import React, { Fragment, useMemo } from 'react';
import { kebabCase } from 'lodash';
import { omit } from 'lodash-es';

import {
  Box,
  Card,
  Grid,
  Skeleton,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { Invoice } from 'generated/graphql';
import { checkStatus } from 'utils/statusMapping';
import { handleAddress } from './util';

type Props = {
  loading: boolean;
  invoice?: Invoice;
};

function DeliverySummaryCard(props: Props) {
  /**
   * Custom hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * Memo
   */
  const summaryValues = useMemo(summaryValuesMemo, [props, t]);

  return (
    <Card sx={{ p: isSmallScreen ? 3 : 4, height: 1 }}>
      <Grid container spacing={4}>
        <Grid item md={8}>
          <Box pb={3}>
            <Typography color="primary" variant="h5">
              {t('common.deliverySummary')}
            </Typography>
          </Box>
          <Grid container spacing={4}>
            {summaryValues.map((v) => (
              <Fragment key={v.key}>
                <Grid item xs={5}>
                  <Typography fontWeight={500} whiteSpace="nowrap">
                    {v.key}
                  </Typography>
                </Grid>
                <Grid item xs={7}>
                  {props.loading ? (
                    <Typography>
                      <Skeleton />
                    </Typography>
                  ) : (
                    <Typography
                      data-testid={`delivery-summary-${kebabCase(v.key)}`}
                    >
                      {v.value}
                    </Typography>
                  )}
                </Grid>
              </Fragment>
            ))}
          </Grid>
        </Grid>
      </Grid>
    </Card>
  );

  function summaryValuesMemo() {
    const address = omit(props?.invoice?.address, '__typename');

    return [
      {
        key: t('invoice.shippedTo'),
        value: handleAddress(address)
      },
      {
        key: t('common.shipDate'),
        value: props?.invoice?.shipDate ?? '-'
      },
      {
        key: t('common.deliveryMethod'),
        value: props?.invoice?.deliveryMethod
          ? checkStatus(props.invoice.deliveryMethod)
            ? t(`common.${checkStatus(props.invoice.deliveryMethod)}`)
            : t('common.delivery')
          : '-'
      }
    ];
  }
}

export default DeliverySummaryCard;
