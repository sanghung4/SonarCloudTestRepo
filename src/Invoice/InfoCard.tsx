import React, { Fragment, useMemo } from 'react';
import { kebabCase } from 'lodash';

import {
  Card,
  Grid,
  Skeleton,
  Typography,
  useScreenSize,
  Link
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { Maybe, Invoice } from 'generated/graphql';
import ConditionalWrapper from 'common/ConditionalWrapper';

type Props = {
  loading: boolean;
  invoice?: Invoice;
};

type ValuesType = {
  key: string;
  value: JSX.Element | Maybe<string> | undefined;
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
        sx={{ pb: 3 }}
        data-testid="invoice-info-header"
      >
        {t('invoice.invoiceInfo')}
      </Typography>
      <Grid container spacing={2}>
        {values.map((v) => (
          <Fragment key={v.key}>
            <Grid item xs={4}>
              <Typography sx={{ fontWeight: 500 }}>{v.key}</Typography>
            </Grid>
            <Grid item xs={8}>
              <ConditionalWrapper
                condition={
                  typeof v.value === 'string' &&
                  v.key === t('common.orderNumber')
                }
                wrapper={(children) => (
                  <Link
                    href={`/order/${v.value}/?orderStatus=INVOICED`}
                    sx={{
                      textDecoration: 'none',
                      color: 'primary02.main'
                    }}
                  >
                    {children}
                  </Link>
                )}
              >
                <Typography
                  noWrap
                  data-testid={`invoice-info-${kebabCase(v.key)}`}
                >
                  {props.loading ? <Skeleton /> : v.value ?? '-'}
                </Typography>
              </ConditionalWrapper>
            </Grid>
          </Fragment>
        ))}
      </Grid>
    </Card>
  );

  function valuesMemo() {
    const returnValue: ValuesType[] = [
      {
        key: t('invoices.invoiceDate'),
        value: props.invoice?.invoiceDate
      },
      {
        key: t('invoices.dueDate'),
        value: props.invoice?.dueDate
      },
      {
        key: t('common.orderNumber'),
        value: props.invoice?.invoiceNumber
      },
      {
        key: t('common.poNumber'),
        value: props.invoice?.customerPo
      },
      {
        key: t('invoices.jobName'),
        value: props?.invoice?.jobName
      },
      {
        key: t('invoices.jobNumber'),
        value: props?.invoice?.jobNumber
      }
    ];

    return returnValue;
  }
}

export default InfoCard;
