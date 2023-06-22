import { Order, ProductPricing } from 'generated/graphql';
import { AlertColor } from '@mui/material/Alert';

type MessageOptions = {
  variant?: AlertColor;
};
type PushAlertFn = (
  message: string,
  options?: MessageOptions | undefined
) => void;
type AddItemsToCartFn = (items: AddedItemInfo[]) => void;
type AddItemsToCartProps = {
  order?: Order;
  pushAlert: PushAlertFn;
  addItemsToCart: AddItemsToCartFn;
  pricingData: ProductPricing[];
};
export type AddedItemInfo = {
  productId: string;
  qty: number;
  minIncrementQty: number;
  pricingData?: ProductPricing;
};

export default function addItemsToCartCb({
  order,
  pushAlert,
  addItemsToCart,
  pricingData
}: AddItemsToCartProps) {
  if (order?.lineItems?.length) {
    const items: AddedItemInfo[] = order.lineItems
      .filter((i) => {
        const pricing = pricingData.filter(
          (price) => price.productId === i.erpPartNumber
        );
        return !!pricing?.[0]?.sellPrice;
      })
      .map((i) => {
        const pricing = pricingData.filter(
          (price) => price.productId === i.erpPartNumber
        );
        return {
          productId: i?.erpPartNumber ?? '',
          qty: i?.orderQuantity ?? 0,
          minIncrementQty: 1,
          pricingData: pricing[0]
        };
      });

    const badItemCount = order.lineItems.length - items.length;
    if (badItemCount > 0) {
      pushAlert(
        `${badItemCount} item${
          badItemCount > 1 ? 's' : ''
        } unable to be added to your cart`,
        { variant: 'error' }
      );
    }

    addItemsToCart(items);
  }
}
