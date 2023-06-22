import { OrderLineItem } from 'generated/graphql';
import { TFunction } from 'react-i18next';

export const getUOM = (
  isMincron: boolean = false,
  lineItem: OrderLineItem | undefined,
  t: TFunction<'translation', undefined>
) =>
  (isMincron
    ? lineItem?.pricingUom?.toLowerCase()
    : lineItem?.uom?.toLowerCase()) || t('product.each');
