import { useContext, useEffect, useState } from 'react';

import {
  FormControl,
  FormControlLabel,
  Grid,
  Hidden,
  RadioGroup,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { useCheckoutContext } from 'Checkout/CheckoutProvider';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import { AuthContext } from 'AuthProvider';
import { deliveryMethodFormControlSx } from 'Checkout/util/styles';
import { DeliveryMethodEnum } from 'generated/graphql';
import { radio } from 'utils/inputTestId';
import { useCartContext } from 'providers/CartProvider';
import { standardUserRoles } from 'User';

export default function DeliveryMethod() {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * Context
   */
  const { cart, updateCart } = useCartContext();
  const { deliveryMethod: checkoutDeliveryMethod } = useCheckoutContext();
  const { ecommUser } = useContext(AuthContext);
  const {
    selectedAccounts: { billToErpAccount }
  } = useSelectedAccountsContext();
  const isStandardAccessCashCustomer = Boolean(
    standardUserRoles.includes(ecommUser?.role?.name ?? '') &&
      billToErpAccount?.alwaysCod
  );

  /**
   * State
   */
  const [deliveryMethod, setDeliveryMethod] = useState(checkoutDeliveryMethod);

  /**
   * Effect
   */
  useEffect(
    () => setDeliveryMethod(checkoutDeliveryMethod),
    [checkoutDeliveryMethod]
  );

  /**
   * Render
   */
  return (
    <Grid container mb={8}>
      <Grid item xs={12} md={3}>
        <Typography
          variant="body1"
          color="textSecondary"
          pb={isSmallScreen ? 3 : 0}
        >
          {t('common.deliveryMethod')}
        </Typography>
      </Grid>
      <Grid item xs={12} md={6}>
        <FormControl component="fieldset" sx={deliveryMethodFormControlSx}>
          <RadioGroup
            row
            aria-label="delivery-method"
            name="deliveryMethod"
            value={deliveryMethod}
            onChange={handleDeliveryMethodChange}
          >
            <FormControlLabel
              value={DeliveryMethodEnum.Willcall}
              control={radio('will-call-radio-button-checkout')}
              label={`${t('common.willCall')}`}
              sx={{ pr: 2.75 }}
            />
            <FormControlLabel
              value={DeliveryMethodEnum.Delivery}
              control={radio('delivery-radio-button-checkout')}
              label={`${t('common.delivery')}`}
              disabled={isStandardAccessCashCustomer}
            />
          </RadioGroup>
        </FormControl>
      </Grid>
      <Hidden mdDown>
        <Grid item md={3} />
      </Hidden>
    </Grid>
  );

  /**
   * Callback Definitions
   */
  function handleDeliveryMethodChange(
    event: React.ChangeEvent<HTMLInputElement>
  ) {
    const value = event.target.value as DeliveryMethodEnum;
    setDeliveryMethod(value);
    if (cart) {
      updateCart?.(cart.id, { deliveryMethod: value });
    }
  }
}
