import { useState } from 'react';

import {
  FormControl,
  FormControlLabel,
  Grid,
  RadioGroup,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { shipmentStyles } from 'Checkout/util/styles';
import { radio } from 'utils/inputTestId';
import { useCartContext } from 'providers/CartProvider';

type Props = {
  shouldShipFullOrder: boolean;
};

export default function Shipments(props: Props) {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * State
   */
  const defaultShipment = props.shouldShipFullOrder ? 'one' : 'multiple';
  const [shipments, setShipments] = useState(defaultShipment);

  /**
   * Context
   */
  const { cart, updateDelivery } = useCartContext();

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
          {t('common.delivery')}
          <Typography variant="body1" component="span" color="error">
            {'*'}
          </Typography>
        </Typography>
      </Grid>
      <Grid item xs={12} md={9}>
        <FormControl component="fieldset" sx={shipmentStyles.formControlSx}>
          <RadioGroup
            aria-label="shipments"
            name="shipments"
            value={shipments}
            onChange={handleShipmentChange}
          >
            <FormControlLabel
              value="one"
              control={radio('delivery-once-radio-button')}
              label={`${t('cart.deliverOnceLong')}`}
              sx={shipmentStyles.formLabelOneSx}
            />
            <FormControlLabel
              value="multiple"
              control={radio('delivery-multiple-radio-button')}
              label={`${t('cart.deliverMultipleLong')}`}
              sx={shipmentStyles.formLabelMultipleSx}
            />
          </RadioGroup>
        </FormControl>
      </Grid>
    </Grid>
  );

  /**
   * Callback Definitions
   */
  function handleShipmentChange(event: React.ChangeEvent<HTMLInputElement>) {
    setShipments(event.target.value);
    if (cart)
      updateDelivery?.({
        cartId: cart.id,
        shouldShipFullOrder: event.target.value === 'one'
      });
  }
}
