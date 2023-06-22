import {
  Box,
  Image,
  TableCell,
  TableRow,
  Typography
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { Maybe, OrderLineItem } from 'generated/graphql';
import { WarningIcon } from 'icons';
import notfound from 'images/notfound.png';
import { format } from 'utils/currency';
import { itemStyles } from 'Checkout/util/styles';

type Props = {
  lineItem: OrderLineItem;
  qtyAvailable: number;
  disabled?: Maybe<boolean>;
  index?: number;
};
const { tableCellSxFn, tableCellOutOfStockSx } = itemStyles;

export default function ItemDesktop(props: Props) {
  /**
   * Props
   */
  const orderQuantity = props.lineItem.orderQuantity ?? 0;
  const notAvailable =
    !props.qtyAvailable || props.qtyAvailable < orderQuantity;

  /**
   * Custom Hooks
   */
  const { t } = useTranslation();

  /**
   * Styles
   */
  const tableCellSx = tableCellSxFn(notAvailable);

  /**
   * Render
   */
  return (
    <>
      <TableRow data-testid={`checkout-cart-item-${props.lineItem.productId}`}>
        <TableCell sx={tableCellSx}>
          <Box display="flex" alignItems="center">
            <Box height={72} width={72} flex="0 0 72px">
              <Image
                fallback={notfound}
                alt={props.lineItem.productName ?? t('common.productPicture')}
                src={props.lineItem.imageUrls?.medium ?? ''}
              />
            </Box>
            <Box display="flex" flexDirection="column" ml={3.75}>
              <Typography
                variant="caption"
                color="textSecondary"
                data-testid={`checkout-manufacturer-name-${props.index}`}
              >
                {props.lineItem.manufacturerName}
              </Typography>
              <Typography
                variant="body1"
                color="primary"
                py={0.5}
                data-testid={`checkout-product-name-${props.index}`}
              >
                {props.lineItem.productName}
              </Typography>
              <Typography
                variant="caption"
                color="textSecondary"
                data-testid={`checkout-manufacturer-number-${props.index}`}
              >
                {t('product.mfr')} {props.lineItem.manufacturerNumber}
              </Typography>
            </Box>
          </Box>
        </TableCell>
        <TableCell align="center" sx={tableCellSx}>
          <Typography
            variant="caption"
            data-testid={`checkout-unit-price-${props.index}`}
          >
            {!props.lineItem.unitPrice
              ? t('product.priceUnavailable')
              : `${format(props.lineItem.unitPrice)} ${
                  props.lineItem?.pricingUom?.toLowerCase() || t('product.each')
                }`}
          </Typography>
        </TableCell>
        <TableCell align="center" sx={tableCellSx}>
          <Box display="flex" flexDirection="column" alignItems="center">
            <Typography
              variant="caption"
              color="textSecondary"
              data-testid={`checkout-qty-available-${props.index}`}
            >
              {props.qtyAvailable}
            </Typography>
            {!!props.qtyAvailable && (
              <Typography variant="caption" color="success.main">
                {t('common.inStock')}
              </Typography>
            )}
          </Box>
        </TableCell>
        <TableCell align="center" sx={tableCellSx}>
          <Box
            display="flex"
            flexDirection="column"
            alignItems="flex-end"
            px={3}
          >
            <Typography
              variant="body1"
              color="primary"
              fontWeight={500}
              data-testid={`checkout-item-order-qty-${props.index}`}
            >
              {orderQuantity}
            </Typography>
            {!!props.lineItem.unitPrice && (
              <Typography
                variant="body1"
                color="textSecondary"
                pt={2}
                data-testid={`checkout-item-total-price-${props.index}`}
              >
                {format(orderQuantity * props.lineItem.unitPrice)}
              </Typography>
            )}
          </Box>
        </TableCell>
      </TableRow>
      {notAvailable && (
        <TableRow>
          <TableCell colSpan={5} sx={tableCellOutOfStockSx}>
            <Box display="flex" alignItems="center" color="primary02.main">
              <Box component={WarningIcon} height={16} width={16} mr={2} />
              <Typography variant="caption" color="inherit">
                {t('cart.outOfStock')}
              </Typography>
            </Box>
          </TableCell>
        </TableRow>
      )}
    </>
  );
}
