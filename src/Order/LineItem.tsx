import { Dispatch, useEffect, useMemo, useState } from 'react';

import { Link, useScreenSize } from '@dialexa/reece-component-library';
import { Link as RouterLink } from 'react-router-dom';

import { OrderLineItem, ProductPricing } from 'generated/graphql';
import { FN } from '@reece/global-types';
import LineItemMobile from 'Order/LineItem.mobile';
import LineItemDesktop from 'Order/LineItem.desktop';
import slugify from 'react-slugify';
import { useCartContext } from 'providers/CartProvider';

export type OrderLineItemProps = {
  loading: boolean;
  lineItem?: OrderLineItem;
  isMincron?: boolean;
  uom?: string;
  listIds?: string[];
  pricingData?: ProductPricing;
};

export type SubOrderLineItemProps = {
  availableInList: string[];
  handleReorderButtonClick: FN;
  isComment?: boolean;
  notAvailable: boolean;
  setAvailableInList: Dispatch<string[]>;
  urlWrapper: (args: JSX.Element) => JSX.Element;
} & OrderLineItemProps;

export default function LineItem(props: OrderLineItemProps) {
  /**
   * Props
   */
  const status = props?.lineItem?.status ?? '';
  const erpPartNumber = props?.lineItem?.erpPartNumber ?? '';
  const orderQuantity = props?.lineItem?.orderQuantity ?? 0;

  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();

  /**
   * State
   */
  const [availableInList, setAvailableInList] = useState<string[]>([]);

  /**
   * Memos
   */
  const notAvailable = useMemo(notAvailableMemo, [status]);
  const isComment = useMemo(isCommentMemo, [status]); // Shows for comment or misc charge

  /**
   * Context
   */
  const { addItemToCart } = useCartContext();

  const productSlug = slugify(
    `${props.lineItem?.manufacturerName}-${props.lineItem?.productName}-${props.lineItem?.manufacturerNumber}`
  );

  /**
   * Effects
   */
  useEffect(handleAvailableInList, [props.listIds]);

  /**
   * Render
   */
  const subProps: SubOrderLineItemProps = {
    ...props,
    availableInList,
    handleReorderButtonClick,
    isComment,
    notAvailable,
    setAvailableInList,
    urlWrapper
  };
  if (isSmallScreen) {
    return <LineItemMobile {...subProps} />;
  }
  return <LineItemDesktop {...subProps} />;

  /**
   * Handles
   */
  function handleReorderButtonClick() {
    addItemToCart(erpPartNumber, orderQuantity, 0, props.pricingData);
  }

  function handleAvailableInList() {
    const listIdsAvailable = props.listIds ?? [];
    setAvailableInList(listIdsAvailable);
  }

  /**
   * Memo Defs
   */
  function notAvailableMemo() {
    const notAvailableStatList = [
      'NonStock',
      'Delete',
      'Purge',
      'NonPDW',
      'Discontinued'
    ];
    return notAvailableStatList.includes(status);
  }
  function isCommentMemo() {
    const commentStatList = ['Comment', 'MiscChrg'];
    return commentStatList.includes(status);
  }

  /**
   * Misc
   */
  function urlWrapper(children: JSX.Element) {
    return (
      <Link
        to={`/product/${productSlug}/${props.lineItem?.productId ?? ''}`}
        component={RouterLink}
        data-testid="product-link"
      >
        {children}
      </Link>
    );
  }
}
