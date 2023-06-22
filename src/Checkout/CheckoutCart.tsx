import { Fragment, useMemo } from 'react';

import {
  Box,
  Divider,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { useCheckoutContext } from 'Checkout/CheckoutProvider';
import ItemDesktop from 'Checkout/Item.desktop';
import ItemMobile from 'Checkout/Item.mobile';
import { HeaderCell } from 'Checkout/util/styles';
import { LineItem } from 'generated/graphql';

export default function CheckoutCart() {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * Context
   */
  const {
    orderData: { lineItems },
    tempCartItems
  } = useCheckoutContext();

  /**
   * Memo
   */
  const qtyAvailable = useMemo(qtyAvailableMemo, [tempCartItems]);

  /** Render */
  return isSmallScreen ? (
    <>
      <Box px={2} py={3}>
        <Typography variant="caption" color="primary">
          ({lineItems.length}){' '}
          {t('cart.itemInCart', { count: lineItems.length })}
        </Typography>
      </Box>
      <Divider />
      {lineItems && lineItems.length > 0 ? (
        lineItems.map((lineItem, i) => (
          <Fragment key={lineItem.productId}>
            {!!lineItem?.erpPartNumber && (
              <ItemMobile
                lineItem={lineItem}
                qtyAvailable={qtyAvailable[lineItem.erpPartNumber]}
                index={i}
              />
            )}
          </Fragment>
        ))
      ) : (
        <Box display="flex" justifyContent="center" mt={9}>
          <Typography variant="h4">{t('cart.noItems')}</Typography>
        </Box>
      )}
    </>
  ) : (
    <Box ml={-4} width={(theme) => `calc(100% + ${theme.spacing(4)})`}>
      <TableContainer>
        <Table aria-label={t('common.cart')}>
          <TableHead>
            <TableRow>
              <HeaderCell>{t('common.product')}</HeaderCell>
              <HeaderCell align="center">{t('common.price')}</HeaderCell>
              <HeaderCell align="center">{t('common.availableQty')}</HeaderCell>
              <HeaderCell align="right">{t('common.orderTotal')}</HeaderCell>
            </TableRow>
          </TableHead>
          <TableBody sx={{ position: 'relative' }}>
            {lineItems.length > 0 ? (
              lineItems.map((lineItem, i) => (
                <Fragment key={lineItem.productId}>
                  {!!lineItem?.erpPartNumber && (
                    <ItemDesktop
                      lineItem={lineItem}
                      qtyAvailable={qtyAvailable[lineItem.erpPartNumber] ?? 0}
                      index={i}
                    />
                  )}
                </Fragment>
              ))
            ) : (
              <TableRow>
                <TableCell colSpan={5} sx={{ borderBottom: 0 }}>
                  <Box display="flex" justifyContent="center" mt={9}>
                    <Typography variant="h4">{t('cart.noItems')}</Typography>
                  </Box>
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );

  /**
   * Memo Functions
   */
  function qtyAvailableMemo() {
    const reducerLogic = (obj: Record<string, number>, product: LineItem) => {
      if (product?.erpPartNumber) {
        obj[product.erpPartNumber] = product?.qtyAvailable ?? 0;
      }
      return obj;
    };
    return tempCartItems.reduce<Record<string, number>>(reducerLogic, {});
  }
}
