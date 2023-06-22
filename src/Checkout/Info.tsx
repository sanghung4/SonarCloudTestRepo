import { ChangeEvent, useContext, useEffect, useState } from 'react';

import {
  Box,
  Button,
  DayPicker,
  Grid,
  Hidden,
  IconButton,
  InputAdornment,
  MenuItem,
  Select,
  SelectChangeEvent,
  TextField,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { useCheckoutContext } from 'Checkout/CheckoutProvider';
import CustomAddress from 'Checkout/CustomAddress';
import DeliveryMethod from 'Checkout/DeliveryMethod';
import Heading from 'Checkout/Heading';
import Location from 'Checkout/Location';
import Shipments from 'Checkout/Shipments';
import {
  Branch,
  Delivery,
  PreferredTimeEnum,
  WillCall
} from 'generated/graphql';
import { useDomainInfo } from 'hooks/useDomainInfo';
import { CalendarIcon } from 'icons';
import { getTypedKeys } from 'utils/getTypedKeys';
import { useCartContext } from 'providers/CartProvider';
import { BranchContext } from 'providers/BranchProvider';
import { CustomAddressInput } from './util/types';
import { preferredTimeValues } from './util';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';

export default function Info() {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();
  const { isWaterworks } = useDomainInfo();
  const instructionsMax = isWaterworks ? 240 : 500;
  /**
   * Context
   */
  const { contract, contractBranch } = useCartContext();
  const {
    deliveryMethodObject,
    updateDelivery,
    deliveryData,
    setDeliveryData,
    setWillCallData,
    willCallData,
    isDelivery
  } = useCheckoutContext();
  const { shippingBranch } = useContext(BranchContext);
  const {
    selectedAccounts: { shipToErpAccount }
  } = useSelectedAccountsContext();

  const deliveryObject = deliveryMethodObject as Delivery | undefined;
  const delivery = deliveryMethodObject
    ? (deliveryMethodObject as Delivery)
    : undefined;
  const defaultAddress =
    isDelivery && deliveryObject?.address
      ? (deliveryObject.address as CustomAddressInput)
      : undefined;

  /**
   * State
   */
  const [showCustomAddress, setShowCustomAddress] = useState(false);
  const [address, setAddress] = useState<CustomAddressInput | undefined>(
    defaultAddress
  );
  const [preferredDate, setPreferredDate] = useState<Date | undefined>(
    deliveryObject?.preferredDate
  );
  const [preferredTime, setPreferredTime] = useState<PreferredTimeEnum | ''>(
    deliveryObject?.preferredTime ?? ''
  );
  const [instructions, setInstructions] = useState('');

  /**
   * Effect
   */
  useEffect(updateAddress, [
    address,
    delivery,
    deliveryMethodObject,
    isDelivery,
    setAddress,
    shippingBranch
  ]);
  useEffect(updatePreferredDate, [
    delivery?.preferredDate,
    deliveryMethodObject?.preferredDate,
    setPreferredDate
  ]);
  useEffect(updatePreferredTime, [
    delivery?.preferredTime,
    deliveryMethodObject?.preferredTime,
    setPreferredTime
  ]);
  useEffect(updateInstructions, [
    delivery,
    deliveryMethodObject,
    setInstructions
  ]);

  /**
   * Render
   */
  return (
    <>
      <Heading
        title={t(
          isDelivery ? 'cart.deliveryInformation' : 'cart.willCallInformation'
        )}
      />
      {!shippingBranch?.isPricingOnly && <DeliveryMethod />}
      {isDelivery && (
        <Shipments
          shouldShipFullOrder={
            (deliveryMethodObject as Delivery)?.shouldShipFullOrder
          }
        />
      )}
      <Grid container mb={8}>
        <Grid item xs={12} md={3}>
          <Typography
            variant="body1"
            color="textSecondary"
            pb={isSmallScreen ? 3 : 0}
          >
            {t(isDelivery ? 'cart.deliveryLocation' : 'cart.willCallLocation')}
          </Typography>
        </Grid>
        <Grid item xs={12} md={showCustomAddress ? 7 : 6}>
          {isDelivery && showCustomAddress ? (
            <CustomAddress
              onConfirm={handleAddressChange}
              onCancel={() => setShowCustomAddress(false)}
            />
          ) : (
            <>
              <Location
                location={
                  contract
                    ? isDelivery
                      ? contract?.data
                      : (contractBranch as Branch)
                    : address
                }
                includePhone={!!contract}
                data-testid="will-call-location-step-1"
              />
              {isDelivery && !contract && (
                <Box mt={1.5}>
                  <Button
                    variant="inline"
                    color="primaryLight"
                    onClick={() => setShowCustomAddress(true)}
                    data-testid="change-address-button"
                    sx={{ minWidth: 0 }}
                  >
                    {address?.custom
                      ? t('common.edit')
                      : t('cart.changeAddress')}
                  </Button>
                  {!!address?.custom && (
                    <>
                      <Typography variant="body1" component="span" mx={1}>
                        {'|'}
                      </Typography>
                      <Button
                        variant="inline"
                        color="primaryLight"
                        data-testid="restore-address"
                        onClick={() => handleAddressChange()}
                      >
                        {t('common.restoreDefault')}
                      </Button>
                    </>
                  )}
                </Box>
              )}
            </>
          )}
        </Grid>
        {!showCustomAddress && (
          <Hidden mdDown>
            <Grid item md={3} />
          </Hidden>
        )}
      </Grid>
      <Grid container mb={4}>
        <Hidden mdDown>
          <Grid item xs={12} md={3}>
            <Typography
              variant="body1"
              color="textSecondary"
              pb={isSmallScreen ? 3 : 0}
            >
              {t('cart.preferences')}
            </Typography>
          </Grid>
        </Hidden>
        <Grid item xs={12} md={6}>
          <Box width={isSmallScreen ? 1 : 0.6} mb={6}>
            <DayPicker
              onDayClick={handlePreferredDateChange}
              input={(baseProps) => (
                <TextField
                  id="preferred-date"
                  name="preferredDate"
                  label={
                    <Typography color="textSecondary">
                      {t(
                        isDelivery
                          ? 'cart.preferredDateDelivery'
                          : 'cart.preferredDateWillCall'
                      )}
                    </Typography>
                  }
                  fullWidth
                  inputProps={{ 'data-testid': 'preferred-date-input' }}
                  InputProps={{
                    endAdornment: (
                      <InputAdornment position="end">
                        <IconButton
                          color="primary"
                          size="small"
                          data-testid="preferred-date-button"
                          onClick={(e) => baseProps.onFocus?.(e as any)}
                        >
                          <CalendarIcon />
                        </IconButton>
                      </InputAdornment>
                    ),
                    ...baseProps
                  }}
                />
              )}
              inputOptions={{
                defaultSelected: preferredDate,
                fromDate: new Date()
              }}
              disabled={[{ dayOfWeek: [0, 6] }]}
            />
          </Box>
          <Box width={isSmallScreen ? 1 : 0.6} mb={6}>
            <Select
              id="preferred-time"
              name="preferredTime"
              label={
                <Typography color="textSecondary">
                  {t(
                    isDelivery
                      ? 'cart.preferredTimeDelivery'
                      : 'cart.preferredTimeWillCall'
                  )}
                </Typography>
              }
              value={preferredTime}
              onChange={handlePreferredTimeChange}
              renderValue={preferredTimeValues}
              inputProps={{ 'data-testid': 'preferred-time-input' }}
              fullWidth
            >
              {getTypedKeys(PreferredTimeEnum).map((key) => (
                <MenuItem key={key} value={PreferredTimeEnum[key]}>
                  {key === 'Asap' ? key.toUpperCase() : key}
                </MenuItem>
              ))}
            </Select>
          </Box>
          {!contract && (
            <Typography variant="h5" component="p" gutterBottom color="primary">
              {t(
                isDelivery
                  ? `cart.cutoffTimeDelivery`
                  : `cart.cutoffTimeWillCall`
              )}
            </Typography>
          )}
          {!contract && (
            <Typography variant="body1" color="textSecondary">
              {t(
                isDelivery
                  ? `cart.cutoffTimeMessageDelivery`
                  : `cart.cutoffTimeMessageWillCall`
              )}
            </Typography>
          )}
        </Grid>
        <Hidden mdDown>
          <Grid item md={3} />
        </Hidden>
      </Grid>
      <Grid container>
        <Grid item xs={12} md={3}>
          <Typography
            variant="body1"
            color="textSecondary"
            pr={isSmallScreen ? 0 : 5}
            pb={isSmallScreen ? 1 : 0}
          >
            {t(
              isDelivery
                ? `cart.deliveryInstructions`
                : `cart.willCallInstructions`
            )}
          </Typography>
        </Grid>
        <Grid item xs={12} md={6}>
          <TextField
            id="instructions"
            name="instructions"
            placeholder={t('cart.haveSpecialInstructions')}
            value={instructions}
            onChange={handleInstructionsChange}
            multiline
            rows={5}
            inputProps={{
              maxLength: instructionsMax,
              'data-testid': 'instructions-input'
            }}
            fullWidth
            helperText={
              <Box component="span" display="flex" justifyContent="flex-end">
                {`${instructions.length} / ${instructionsMax}`}
              </Box>
            }
            sx={{ mt: 0 }}
          />
        </Grid>
        <Hidden mdDown>
          <Grid item md={3} />
        </Hidden>
      </Grid>
    </>
  );

  /**
   * Effect Definitions
   */
  function updateAddress() {
    if (isDelivery && delivery) {
      setAddress(delivery.address as CustomAddressInput);
    } else if (shippingBranch && !address) {
      const address: CustomAddressInput = {
        city: shippingBranch.city ?? '',
        companyName: shippingBranch.name,
        country: '',
        custom: false,
        phoneNumber: shippingBranch.phone,
        state: shippingBranch.state ?? '',
        street1: shippingBranch.address1 ?? '',
        street2: shippingBranch.address2,
        zip: shippingBranch.zip ?? ''
      };
      setAddress(address);
    }
  }
  function updatePreferredTime() {
    if (delivery?.preferredTime) {
      setPreferredTime(delivery.preferredTime);
    }
  }
  function updatePreferredDate() {
    if (delivery?.preferredDate) {
      setPreferredDate(delivery.preferredDate);
    }
  }
  function updateInstructions() {
    const instructions = delivery
      ? (delivery.deliveryInstructions
          ? delivery.deliveryInstructions
          : (delivery as WillCall).pickupInstructions) ?? ''
      : '';
    setInstructions(instructions);
  }

  /**
   * Callback Definitions
   */
  async function handleAddressChange(newAddress?: CustomAddressInput) {
    const normalizedAddress = newAddress || {
      country: 'USA',
      custom: false,
      companyName: shipToErpAccount?.companyName,
      street1: shipToErpAccount?.street1 ?? '',
      street2: shipToErpAccount?.street2,
      city: shipToErpAccount?.city ?? '',
      state: shipToErpAccount?.state ?? '',
      zip: shipToErpAccount?.zip ?? '',
      phoneNumber: shipToErpAccount?.phoneNumber
    };
    setAddress(normalizedAddress);
    const { phoneNumber, ...addressInput } = normalizedAddress;
    await updateDelivery({
      ...deliveryData,
      phoneNumber: phoneNumber,
      address: {
        ...deliveryData.address,
        ...addressInput,
        id: deliveryData.address?.id ?? ''
      }
    });

    setShowCustomAddress(false);
  }

  function handlePreferredDateChange(preferredDate?: Date) {
    setPreferredDate(preferredDate);
    if (isDelivery) {
      setDeliveryData({ ...deliveryData, preferredDate });
    } else {
      setWillCallData({ ...willCallData, preferredDate });
    }
  }

  function handlePreferredTimeChange(event: SelectChangeEvent<unknown>) {
    const preferredTime = (event.target as HTMLInputElement)
      .value as PreferredTimeEnum;
    setPreferredTime(preferredTime);
    if (isDelivery) {
      setDeliveryData({ ...deliveryData, preferredTime });
    } else {
      setWillCallData({ ...willCallData, preferredTime });
    }
  }

  function handleInstructionsChange(event: ChangeEvent<HTMLInputElement>) {
    const deliveryInstructions = (event.target as HTMLInputElement).value;
    setInstructions(deliveryInstructions);
    if (isDelivery) {
      setDeliveryData({ ...deliveryData, deliveryInstructions });
    } else {
      const pickupInstructions = deliveryInstructions;
      setWillCallData({ ...willCallData, pickupInstructions });
    }
  }
}
