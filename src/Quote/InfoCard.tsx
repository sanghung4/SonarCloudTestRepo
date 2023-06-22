import React, { Fragment, useMemo } from 'react';

import {
  Card,
  Grid,
  Skeleton,
  Tooltip,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { Quote } from 'generated/graphql';

type Props = {
  loading: boolean;
  quote?: Quote;
};

function InfoCard(props: Props) {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * Memo
   */
  const values = useMemo(valuesMemo, [props, t]);

  return (
    <Card sx={{ p: isSmallScreen ? 3 : 4, height: 1 }}>
      <Typography color="primary" variant="h5" sx={{ pb: 3 }}>
        {t('common.quoteInformation')}
      </Typography>
      <Grid container spacing={2}>
        {values.map((v) => (
          <Fragment key={v.key}>
            <Grid item xs={5}>
              <Typography sx={{ fontWeight: 500 }}>{v.key}</Typography>
            </Grid>
            <Grid item xs={7}>
              <Tooltip title={v.value ?? '-'}>
                <Typography noWrap>
                  {props.loading ? <Skeleton /> : v.value ?? '-'}
                </Typography>
              </Tooltip>
            </Grid>
          </Fragment>
        ))}
      </Grid>
    </Card>
  );

  function valuesMemo() {
    return [
      {
        key: t('common.jobName'),
        value: props.quote?.shipToName
      },
      {
        key: t('quotes.requestedDate'),
        value: props.quote?.orderDate
      },
      // Looks to always be returning 'null' for now
      // {
      //   key: t('quotes.requestedBy'),
      //   value: props.quote?.orderedBy
      // },
      {
        key: t('common.poNumber'),
        value: props.quote?.customerPO
      }
    ];
  }
}

export default InfoCard;
