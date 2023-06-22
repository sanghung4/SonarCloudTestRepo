import { Fragment, useMemo } from 'react';
import { kebabCase } from 'lodash';

import {
  Box,
  Card,
  Grid,
  Skeleton,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { Order } from 'generated/graphql';
import summaryValuesLogic from './utils/summaryValues';

type Props = {
  loading: boolean;
  order?: Order;
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
                  <Typography fontWeight={500}>{v.key}</Typography>
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
        <Grid item md={6}>
          <Box pb={3}>
            <Typography color="primary" variant="h5">
              {t('common.specialInstructions')}
            </Typography>
          </Box>
          <Typography data-testid={'handling-instructions'}>
            {props.loading ? (
              <>
                <Skeleton />
                <Skeleton width={120} />
              </>
            ) : (
              props?.order?.specialInstructions ?? t('common.na')
            )}
          </Typography>
        </Grid>
      </Grid>
    </Card>
  );

  function summaryValuesMemo() {
    return summaryValuesLogic({ order: props.order, t });
  }
}

export default DeliverySummaryCard;
