import {
  Box,
  Collapse,
  FormControl,
  FormControlLabel,
  IconButton,
  RadioGroup,
  Typography,
  alpha,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { DeliveryMethodEnum, Maybe } from 'generated/graphql';
import { CloseIcon, WarningIcon } from 'icons';
import { radio } from 'utils/inputTestId';
import { useCartContext } from 'providers/CartProvider';

type ShipmentsProps = {
  isDisabled: boolean;
  deliveryMethod?: Maybe<DeliveryMethodEnum>;
  shouldShipFullOrder: string;
  setShouldShipFullOrder: (shouldShipFullOrder: string) => void;
  stockAlertOpen: boolean;
  setStockAlertOpen: (state: boolean) => void;
};

function Shipments(props: ShipmentsProps) {
  /**
   * Props
   */
  const isDelivery = props.deliveryMethod === DeliveryMethodEnum.Delivery;

  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * Context
   */
  const { cart: { id: cartId } = { id: undefined }, updateDelivery } =
    useCartContext();

  return (
    <Collapse in={props.stockAlertOpen}>
      <Box
        bgcolor={(theme) => alpha(theme.palette.error.main, 0.05)}
        borderRadius="10px"
        p={3}
        mt={3}
      >
        <Box display="flex" alignItems="center" color="error.main">
          <Box
            component={WarningIcon}
            sx={{ verticalAlign: 'middle' }}
            mr={3}
          />
          <Box flex={1} display="flex" flexDirection="column">
            <Typography variant="h5" color="inherit">
              {t('cart.outOfStockAll')}
            </Typography>
            {isDelivery && (
              <>
                <Typography variant="caption" color="textPrimary">
                  {t('cart.outOfStockContinue')}
                </Typography>
                <Box pt={isSmallScreen ? 4 : 2} pb={1} color="text.primary">
                  <Box ml={1} mr={isSmallScreen ? -3 : 0}>
                    <FormControl component="fieldset">
                      <RadioGroup
                        row={!isSmallScreen}
                        aria-label="number-of-shipments"
                        name="shouldShipFullOrder"
                        value={props.shouldShipFullOrder}
                        onChange={handleShouldShipFullOrder}
                      >
                        <Box
                          component={FormControlLabel}
                          value="one"
                          control={radio('delivery-once-radio-button')}
                          label={t('cart.deliverOnce') as string}
                          disabled={props.isDisabled}
                          pr={isSmallScreen ? 0 : 10}
                          pb={isSmallScreen ? 2 : 0}
                        />
                        <Box
                          component={FormControlLabel}
                          value="multiple"
                          control={radio('delivery-multiple-radio-button')}
                          label={t('cart.deliverMultiple') as string}
                          disabled={props.isDisabled}
                        />
                      </RadioGroup>
                    </FormControl>
                  </Box>
                </Box>
              </>
            )}
          </Box>
          <Box
            component={IconButton}
            aria-label="close"
            color="inherit"
            size="small"
            data-testid="shipment-close-button"
            onClick={() => props.setStockAlertOpen(false)}
            m={-0.375}
          >
            <CloseIcon />
          </Box>
        </Box>
      </Box>
    </Collapse>
  );

  /**
   * Callback Definitions
   */
  function handleShouldShipFullOrder(
    event: React.ChangeEvent<HTMLInputElement>
  ) {
    if (cartId) {
      const { value } = event.target;
      props.setShouldShipFullOrder(value);
      updateDelivery?.({ cartId, shouldShipFullOrder: value === 'one' });
    }
  }
}

export default Shipments;
