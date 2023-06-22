import { Fragment, useMemo } from 'react';

import {
  Card,
  Grid,
  Tooltip,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import CreditCardListItem from 'CreditCard/CreditCardListItem';
import { Maybe, Order } from 'generated/graphql';
import OrderInfoToolTip from 'Order/utils/OrderInfoToolTip';

// TODO: optionals?
type Props = {
  loading: boolean;
  order?: Order;
};

type ValuesType = {
  key: string;
  value: JSX.Element | Maybe<string> | undefined;
  testIdKey: string;
};

function InfoCard(props: Props) {
  /**
   * Custom hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * Memo
   */
  const values = useMemo(valuesMemo, [props, t]);

  return (
    <Card
      sx={{
        p: isSmallScreen ? 3 : 4,
        height: 1
      }}
    >
      <Typography
        color="primary"
        variant="h5"
        pb={3}
        data-testid="order-info-header"
      >
        {t('common.orderInformation')}
      </Typography>
      <Grid container spacing={2}>
        {values.map((v) => (
          <Fragment key={v.key}>
            <Grid item xs={4}>
              <Typography fontWeight={500}>{v.key}</Typography>
            </Grid>
            <Grid item xs={8}>
              {typeof v.value === 'string' ? (
                <Tooltip
                  title={
                    // Reason why this is ignored because Tooltip is impossible to capture in snapshots
                    // istanbul ignore next
                    v.value || '-'
                  }
                >
                  <OrderInfoToolTip
                    loading={props.loading}
                    key={v.key}
                    value={v.value}
                    testIdKey={v.testIdKey}
                  />
                </Tooltip>
              ) : (
                v.value
              )}
            </Grid>
          </Fragment>
        ))}
      </Grid>
    </Card>
  );

  function valuesMemo() {
    const returnValue: ValuesType[] = [
      {
        key: t('common.jobName'),
        value: props.order?.shipToName,
        testIdKey: t('common.jobName')
      },
      {
        key: t('common.orderDate'),
        value: props.order?.orderDate,
        testIdKey: t('common.orderDate')
      },
      {
        key: t('common.orderedBy'),
        value: props?.order?.orderedBy,
        testIdKey: t('common.orderedBy')
      },
      {
        key: t('common.poNumber'),
        value: props?.order?.customerPO,
        testIdKey: t('common.poNumber')
      }
    ];

    if (props?.order?.creditCard) {
      returnValue.push({
        key: t('common.payment'),
        value: (
          <CreditCardListItem
            creditCard={props.order.creditCard}
            expired={false}
            hideType
            hideExpired
            noEmphasis
          />
        ),
        testIdKey: t('common.payment')
      });
    }

    return returnValue;
  }
}

export default InfoCard;
