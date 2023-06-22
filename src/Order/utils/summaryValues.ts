import { TFunction } from 'i18next';
import { omit } from 'lodash-es';

import { Order } from 'generated/graphql';
import { checkStatus } from 'utils/statusMapping';

type SummaryValuesProps = {
  order?: Order;
  t: TFunction;
};

export default function summaryValuesLogic({ order, t }: SummaryValuesProps) {
  const address = omit(order?.shipAddress, '__typename');
  const delivery = checkStatus(order?.deliveryMethod || '');

  return [
    {
      key: t('common.shippedTo'),
      value:
        Object.values(address)
          .filter((a) => a)
          .join(', ') || '-'
    },
    {
      key: t('common.shipDate'),
      value: order?.shipDate ?? '-'
    },
    {
      key: t('common.deliveryMethod'),
      value: order?.deliveryMethod
        ? delivery
          ? t(`common.${delivery}`)
          : t('common.delivery')
        : '-'
    }
  ];
}
