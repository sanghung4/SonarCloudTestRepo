import {
  ChangeEvent,
  useContext,
  useEffect,
  useState,
  FocusEvent,
  useMemo
} from 'react';

import {
  Box,
  Collapse,
  FormHelperText,
  Grid,
  Hidden,
  Input,
  MenuItem,
  Select,
  SelectChangeEvent,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { useCheckoutContext } from 'Checkout/CheckoutProvider';
import Heading from 'Checkout/Heading';
import Location from 'Checkout/Location';
import { PaymentInputLabelStyled } from 'Checkout/util/styles';
import CreditCardPayment from 'CreditCard';
import { DeliveryMethodEnum, PaymentMethodTypeEnum } from 'generated/graphql';
import inputTestId from 'utils/inputTestId';
import { useCartContext } from 'providers/CartProvider';
import { CreditCardContext } from 'CreditCard/CreditCardProvider';
import { checkUserPermission, Permission } from 'common/PermissionRequired';
import { AuthContext } from 'AuthProvider';
import { getKeyFromValue, getTypedKeys } from 'utils/getTypedKeys';
import { pull } from 'lodash-es';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import { useDomainInfo } from 'hooks/useDomainInfo';

export default function Payment() {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  /**
   * Context
   */
  const { activeFeatures, profile } = useContext(AuthContext);
  const { contract } = useCartContext();
  const {
    paymentData,
    poNumberError,
    setDisableContinue,
    setPaymentData,
    setPoNumberError,
    updateCart,
    deliveryMethod
  } = useCheckoutContext();
  const { setSelectedCreditCard } = useContext(CreditCardContext);
  const {
    isMincron,
    selectedAccounts: { billToErpAccount }
  } = useSelectedAccountsContext();
  const { isWaterworks } = useDomainInfo();

  /**
   * State
   */
  const [paymentMethodType, setPaymentMethodType] = useState(
    paymentData.paymentMethodType!
  );

  const [poNumber, setPoNumber] = useState(paymentData.poNumber ?? '');

  /**
   * Memo
   */
  const paymentMethodTypes = useMemo(() => {
    const canSubmitCartWithoutApproval = checkUserPermission(profile, [
      Permission.SUBMIT_CART_WITHOUT_APPROVAL
    ]);

    const types = getTypedKeys(PaymentMethodTypeEnum);

    if (deliveryMethod === DeliveryMethodEnum.Delivery) {
      pull(types, 'Payinstore');
    }

    if (
      !activeFeatures?.includes('CHECKOUT_WITH_CARD') ||
      !canSubmitCartWithoutApproval
    ) {
      pull(types, 'Creditcard');
    }

    if (billToErpAccount?.alwaysCod) {
      pull(types, 'Billtoaccount');
    }

    return types;
  }, [activeFeatures, deliveryMethod, profile, billToErpAccount]);

  /**
   * Constants
   */
  const hasValidPaymentMethod = paymentMethodTypes.includes(
    getKeyFromValue(
      PaymentMethodTypeEnum,
      paymentData.paymentMethodType ?? null
    )
  );
  const hasValidPoNumber = Boolean(
    poNumber.trim().length && paymentData.poNumber?.trim().length
  );

  /**
   * Effect
   */
  useEffect(() => {
    let isValid;
    switch (paymentMethodType) {
      case PaymentMethodTypeEnum.Billtoaccount:
      case PaymentMethodTypeEnum.Payinstore:
        //check if cart payment method is valid payment option and po number exists
        isValid = Boolean(hasValidPaymentMethod && hasValidPoNumber);
        break;
      case PaymentMethodTypeEnum.Creditcard:
        // check that payment is valid, po number exists, and credit card info exists
        isValid = Boolean(
          hasValidPaymentMethod && hasValidPoNumber && paymentData.creditCard
        );
        break;
      default:
        isValid = false;
    }
    setDisableContinue(!isValid);
  }, [
    hasValidPaymentMethod,
    hasValidPoNumber,
    paymentData.creditCard,
    paymentMethodType,
    setDisableContinue
  ]);

  useEffect(() => {
    if (paymentData.poNumber) {
      setPoNumber(paymentData.poNumber);
    }
    setPoNumberError(!hasValidPoNumber);
  }, [hasValidPoNumber, paymentData.poNumber, setPoNumber, setPoNumberError]);

  /**
   * Callbacks
   */
  function handlePaymentMethodTypeChange(event: SelectChangeEvent<unknown>) {
    const newPaymentMethodType = event.target.value as PaymentMethodTypeEnum;
    const isCreditCard =
      newPaymentMethodType === PaymentMethodTypeEnum.Creditcard;
    if (!isCreditCard) {
      setSelectedCreditCard('');
    }
    setPaymentMethodType(newPaymentMethodType);
    const creditCard = isCreditCard ? paymentData.creditCard : null;
    const newPaymentData = {
      ...paymentData,
      creditCard,
      paymentMethodType: newPaymentMethodType
    };
    setPaymentData(newPaymentData);
    updateCart(newPaymentData);
  }

  function checkNullPoNumber(val: string) {
    setPoNumberError(!val.trim().length);
  }

  function handlePoNumberChange(event: ChangeEvent<HTMLInputElement>) {
    const newPoNumber = event.target.value as string;
    setPoNumber(newPoNumber);
    checkNullPoNumber(newPoNumber);
    setPaymentData({ ...paymentData, poNumber: newPoNumber.trim() });
  }

  function handlePoNumberBlur(event: FocusEvent<HTMLInputElement>) {
    const { value } = event.target;
    checkNullPoNumber(value);
    const newPaymentData = { ...paymentData, poNumber: value.trim() };
    setPaymentData(newPaymentData);
    updateCart(newPaymentData);
  }

  /**
   * Render
   */
  return (
    <>
      <Heading title={t('common.paymentInformation')} />
      <Grid container alignItems="center">
        <Grid item xs={12} md={3}>
          <PaymentInputLabelStyled
            htmlFor="payment-method-type"
            shrink={false}
            required
          >
            <Typography component="span" variant="body1" color="textSecondary">
              {t('cart.paymentMethod')}
            </Typography>
          </PaymentInputLabelStyled>
        </Grid>
        <Grid item xs={12} md={6}>
          {!contract ? (
            <Select
              id="payment-method-type"
              name="paymentMethodType"
              value={
                paymentMethodTypes.includes(
                  getKeyFromValue(PaymentMethodTypeEnum, paymentMethodType)
                )
                  ? paymentMethodType
                  : ''
              }
              renderValue={(value) =>
                value
                  ? t(`cart.${(value as PaymentMethodTypeEnum).toLowerCase()}`)
                  : t('common.select')
              }
              displayEmpty={true}
              onChange={handlePaymentMethodTypeChange}
              inputProps={inputTestId('payment-method-input')}
              fullWidth
            >
              {paymentMethodTypes.map((key) => (
                <MenuItem key={key} value={PaymentMethodTypeEnum[key]}>
                  {t(`cart.${key.toLowerCase()}`)}
                </MenuItem>
              ))}
            </Select>
          ) : (
            <Typography variant="body1">
              {t(`cart.${paymentData.paymentMethodType?.toLowerCase()}`)}
            </Typography>
          )}
        </Grid>
      </Grid>
      <Grid container alignItems="center" pt={4}>
        <Grid item xs={12} md={3}>
          <PaymentInputLabelStyled htmlFor="po-number" shrink={false} required>
            <Typography component="span" variant="body1" color="textSecondary">
              {t('cart.poNumber')}
            </Typography>
          </PaymentInputLabelStyled>
        </Grid>
        <Grid item xs={12} md={6}>
          <Input
            id="po-number"
            name="poNumber"
            value={poNumber}
            placeholder={t('cart.enterPoNumber')}
            onChange={handlePoNumberChange}
            onBlur={handlePoNumberBlur}
            inputProps={
              isWaterworks || isMincron
                ? { 'data-testid': 'po-number-input', maxLength: 22 }
                : { 'data-testid': 'po-number-input' }
            }
            fullWidth
            error={poNumberError}
          />
        </Grid>
      </Grid>
      <Grid container>
        <Grid item xs={12} md={3}></Grid>
        <Grid item xs={12} md={6}>
          {poNumberError && (
            <FormHelperText error>
              {t('validation.poNumberRequired')}
            </FormHelperText>
          )}
          {!contract && (
            <FormHelperText>{t('cart.poNumberHelp')}</FormHelperText>
          )}
        </Grid>
      </Grid>
      <Collapse in={paymentMethodType !== PaymentMethodTypeEnum.Payinstore}>
        <Box mt={isSmallScreen ? 2 : 10}>
          {paymentMethodType === PaymentMethodTypeEnum.Billtoaccount &&
            hasValidPaymentMethod && (
              <Heading title={t('cart.billingInformation')} noMargin />
            )}
          {paymentMethodType === PaymentMethodTypeEnum.Creditcard && (
            <Heading title={t('cart.creditCardInformation')} noMargin />
          )}
        </Box>
        {paymentMethodType === PaymentMethodTypeEnum.Billtoaccount &&
        hasValidPaymentMethod ? (
          <Grid container>
            <Hidden mdDown>
              <Grid item xs={12} md={3}>
                <Typography variant="body1" color="textSecondary">
                  {t('common.billTo')}
                </Typography>
              </Grid>
            </Hidden>
            <Grid item xs={12} md={6}>
              <Location location={billToErpAccount} includePhone={!!contract} />
            </Grid>
          </Grid>
        ) : (
          paymentMethodType === PaymentMethodTypeEnum.Creditcard && (
            <CreditCardPayment />
          )
        )}
      </Collapse>
    </>
  );
}
