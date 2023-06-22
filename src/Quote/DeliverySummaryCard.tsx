import React, { Fragment, useMemo } from 'react';

import {
  Box,
  Card,
  Grid,
  Skeleton,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { Quote } from 'generated/graphql';
import { checkStatus } from 'utils/statusMapping';

type Props = {
  loading: boolean;
  quote?: Quote;
};

function DeliverySummaryCard(props: Props) {
  /**
   * Custom Hooks
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
        <Grid item md={6}>
          <Box pb={3}>
            <Typography color="primary" variant="h5">
              {t('common.deliverySummary')}
            </Typography>
          </Box>
          <Grid container spacing={2}>
            {summaryValues.map((v) => (
              <Fragment key={v.key}>
                <Grid item xs={5}>
                  <Typography sx={{ fontWeight: 500 }}>{v.key}</Typography>
                </Grid>
                <Grid item xs={7}>
                  <Typography>
                    {props.loading ? <Skeleton /> : v.value}
                  </Typography>
                </Grid>
              </Fragment>
            ))}
          </Grid>
        </Grid>
        <Grid item md={6}>
          <Box pb={3}>
            <Typography color="primary" variant="h5">
              {t('common.specialInstructions')}
            </Typography>
          </Box>
          <Typography>
            {props.loading ? (
              <>
                <Skeleton />
                <Skeleton width={120} />
              </>
            ) : (
              props.quote?.specialInstructions ?? t('common.na')
            )}
          </Typography>
        </Grid>
      </Grid>
    </Card>
  );

  function summaryValuesMemo() {
    let address = {
      ...props.quote?.shipAddress,
      __typename: undefined
    };

    return [
      {
        key: t('common.shippedTo'),
        value:
          Object.values(address)
            .filter((a) => a)
            .join(', ') ?? '-'
      },
      {
        key: t('common.shipDate'),
        value: props.quote?.shipDate ?? '-'
      },
      {
        key: t('common.deliveryMethod'),
        value: props.quote?.deliveryMethod
          ? checkStatus(props.quote.deliveryMethod)
            ? t(`common.${checkStatus(props.quote.deliveryMethod)}`)
            : t('common.delivery')
          : '-'
      }
    ];
  }
}

export default DeliverySummaryCard;
