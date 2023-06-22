import { CSSProperties, useEffect, useState } from 'react';

import {
  Box,
  Divider,
  Grid,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { FixedSizeList } from 'react-window';

import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';

import { useCheckoutContext } from 'Checkout/CheckoutProvider';
import ItemDesktop from 'Cart/Item.desktop';
import ItemMobile from 'Cart/Item.mobile';
import Loader from 'common/Loader';
import { Cart } from 'generated/graphql';
import { useCartContext } from 'providers/CartProvider';
import VirtualizedList from 'common/VirtualizedList';

type Props = {
  cart: Cart;
  updateItemQuantity?: (
    itemId: string,
    quantity: number,
    minIncrementQty: number,
    productName: string
  ) => void;
  readOnly?: boolean;
};
type FixedSizeListProps = {
  index: number;
  style: CSSProperties;
};

function CartList(props: Props) {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();
  const { isMincron } = useSelectedAccountsContext();

  /**
   * States
   */
  // Optimistic temp data for products (this will be updated instantly before GQL mutation response)
  const [productsData, setProductData] = useState(props.cart.products || []);
  const productCount = productsData.length;

  /**
   * Context
   */
  const { cartLoading, contract } = useCartContext();
  const { orderedContract, tempCartItems } = useCheckoutContext();

  /**
   * Effects
   */
  // Apply cart product if it exists
  useEffect(() => {
    if (props.cart.products) {
      setProductData(props.cart.products);
    } else {
      setProductData([]);
    }
  }, [props.cart.products]);

  /**
   * callbacks
   */
  const handleQuantityUpdate = (
    itemId: string,
    quantity: number,
    minIncrementQty: number,
    productName: string
  ) => {
    if (!props.cart.products) {
      return;
    }
    // First, update the item qty in the optimistic temp data
    const newProductData = productsData.map((item) =>
      item.id === itemId ? { ...item, quantity } : item
    );
    setProductData(newProductData);
    // Then call the API
    props.updateItemQuantity?.(itemId, quantity, minIncrementQty, productName);
  };
  const handleDeleteItem = (itemId: string) => {
    // Remove the item from optimistic temp data before the API response
    const newProductData = productsData.filter((item) => item.id !== itemId);
    setProductData(newProductData);
  };

  return isSmallScreen ? (
    // MOBILE LIST
    <>
      {((!contract && !orderedContract) || !tempCartItems.length) && (
        <>
          <Box px={2} py={3}>
            <Typography variant="caption" color="primary">
              ({productCount}) {t('cart.itemInCart', { count: productCount })}
            </Typography>
          </Box>
          <Divider />
        </>
      )}
      {props.cart.products && productCount ? (
        <FixedSizeList
          height={Math.min(800, 360 * productCount)}
          width="100%"
          overscanCount={2}
          itemSize={contract || orderedContract ? 400 : 360}
          itemCount={productCount}
          style={{ flex: 1, overflow: 'hidden auto' }}
        >
          {({ index, style }: FixedSizeListProps) => {
            const product = productsData[index];
            return (
              <Box style={style} data-testid={`cart-list-item-${index}`}>
                <ItemMobile
                  lineItem={product ?? {}}
                  updateItemQuantity={handleQuantityUpdate}
                  handleDeleteItem={handleDeleteItem}
                  key={product?.id || index}
                  readOnly={props.readOnly}
                  index={index}
                  canAddToList={!isMincron && !contract}
                />
              </Box>
            );
          }}
        </FixedSizeList>
      ) : (
        <Box display="flex" justifyContent="center" mt={9}>
          <Typography variant="h4">{t('cart.noItems')}</Typography>
        </Box>
      )}
    </>
  ) : (
    // DESKTOP LIST
    <Grid container flexDirection="column">
      {/* Head */}
      <Grid item container my={2} wrap="nowrap">
        {!isMincron && !contract && (
          <Grid item xs={1} md={1.7} pl={1} textAlign="left">
            <Typography color="primary.main" variant="body2">
              {t('common.addToList')}
            </Typography>
          </Grid>
        )}
        <Grid
          item
          xs={3}
          md={!isMincron && !contract ? 5 : 5.5}
          pl={!isMincron && !contract ? 0 : 5}
          textAlign="left"
        >
          <Typography color="primary.main" variant="body2">
            {t('common.product')}
          </Typography>
        </Grid>
        <Grid item xs={1} md={2} textAlign="center" pl={4}>
          <Typography color="primary.main" textAlign="center" variant="body2">
            {t('common.price')}
          </Typography>
        </Grid>
        {/* istanbul ignore next */}
        {!contract &&
          !orderedContract && ( // Remove this when we implement availableQty for release contract
            <Grid item xs={1} md={2} textAlign="center">
              <Typography color="primary.main" variant="body2">
                {t('common.availableQty')}
              </Typography>
            </Grid>
          )}
        <Grid
          item
          xs={1}
          md={!isMincron && !contract ? 2 : 2.3}
          pl={!isMincron && !contract ? 0 : 2}
          textAlign="center"
        >
          <Typography color="primary.main" variant="body2">
            {t('common.qty')}
          </Typography>
        </Grid>
        <Grid
          item
          xs={1}
          md={!isMincron && !contract ? 2 : 2.2}
          textAlign="center"
        >
          <Typography color="primary.main" variant="body2">
            {t('common.orderTotal')}
          </Typography>
        </Grid>
        {!props.readOnly && (
          <Grid item xs={1} md={!isMincron && !contract ? 1 : 0.75} />
        )}
      </Grid>
      <Box component={Divider} width="100%" />
      {/* List */}
      <Grid item container className="printGridBlock">
        {props.cart?.products && productCount ? (
          <VirtualizedList
            defaultItemSize={178}
            maxHeight={Math.min(
              640,
              productCount === 1 ? 180 : 150 * productCount
            )}
            width={'100%'}
            dataArray={productsData}
            renderItem={(products, idx) => (
              <Box
                className="printBreakPage printPositionRelative"
                data-testid={`cart-list-item-${idx}`}
              >
                <ItemDesktop
                  lineItem={products ?? {}}
                  updateItemQuantity={handleQuantityUpdate}
                  handleDeleteItem={handleDeleteItem}
                  key={products.product?.id || idx}
                  readOnly={props.readOnly}
                  index={idx}
                  canAddToList={!isMincron && !contract}
                />
              </Box>
            )}
          />
        ) : (
          <Grid item container justifyContent="center" my={9}>
            <Typography variant="h4">
              {cartLoading ? <Loader size="flex" /> : t('cart.noItems')}
            </Typography>
          </Grid>
        )}
      </Grid>
    </Grid>
  );
}

export default CartList;
