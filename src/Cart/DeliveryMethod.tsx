import { useContext, useState, useEffect, useMemo } from 'react';

import {
  Box,
  Button,
  CircularProgress,
  FormControl,
  FormControlLabel,
  RadioGroup,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { AuthContext } from 'AuthProvider';

import { DeliveryMethodEnum, Maybe } from 'generated/graphql';
import { BranchContext } from 'providers/BranchProvider';
import { useCartContext } from 'providers/CartProvider';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import { radio } from 'utils/inputTestId';
import { standardUserRoles } from 'User';

type DeliveryMethodProps = {
  isDisabled: boolean;
  deliveryMethod?: Maybe<DeliveryMethodEnum>;
};

function DeliveryMethod(props: DeliveryMethodProps) {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();
  const {
    selectedAccounts: { billToErpAccount }
  } = useSelectedAccountsContext();
  const { ecommUser } = useContext(AuthContext);

  /**
   * State
   */
  const [deliveryMethod, setDeliveryMethod] = useState(props.deliveryMethod);

  /**
   * Context
   */
  const { cart, contract, updateCart } = useCartContext();
  const { shippingBranch, shippingBranchLoading, setBranchSelectOpen } =
    useContext(BranchContext);
  const cartId = cart?.id;
  const contractData = contract?.data;
  const contractAddress =
    contractData?.accountInformation?.shipToAddress?.address1;
  const isWillCall = props.deliveryMethod === DeliveryMethodEnum.Willcall;
  const isDelivery = props.deliveryMethod === DeliveryMethodEnum.Delivery;
  const isPricingOnly = shippingBranch?.isPricingOnly ?? false;
  const isStandardAccessCashCustomer = Boolean(
    standardUserRoles.includes(ecommUser?.role?.name ?? '') &&
      billToErpAccount?.alwaysCod
  );
  /**
   * Effect
   */
  useEffect(() => {
    if (props.deliveryMethod === DeliveryMethodEnum.Willcall && isPricingOnly) {
      setDeliveryMethod(DeliveryMethodEnum.Delivery);
    }
  }, [props.deliveryMethod, isPricingOnly]);

  /**
   * Memo
   */
  const branchInfo = useMemo(branchInfoMemo, [
    contractAddress,
    contractData?.jobName,
    shippingBranch?.address1,
    shippingBranch?.name,
    t
  ]);
  /**
   * Render
   */
  return (
    <Box
      borderRadius="10px"
      bgcolor="secondary04.main"
      display="flex"
      flexDirection={isSmallScreen ? 'column' : 'row'}
      alignItems={isSmallScreen ? 'flex-start' : 'center'}
      pl={isSmallScreen ? 3 : 4}
      pr={4}
      py={3.5}
    >
      <Typography
        variant="body1"
        fontWeight={500}
        pr={4}
        pb={isSmallScreen ? 3 : 0}
        color="primary"
      >
        {t('common.deliveryMethod')}
      </Typography>
      <FormControl
        component="fieldset"
        sx={{ width: isSmallScreen ? 1 : 'auto' }}
      >
        <RadioGroup
          row
          aria-label="delivery-method"
          name="deliveryMethod"
          value={deliveryMethod}
          data-testid="delivery-method-options"
          onChange={handleDeliveryMethodChange}
          sx={{ justifyContent: isSmallScreen ? 'space-around' : 'flex-start' }}
        >
          <Box
            component={FormControlLabel}
            value={DeliveryMethodEnum.Willcall}
            control={radio('will-call-radio-button')}
            label={t('common.willCall') as string}
            disabled={props.isDisabled || isPricingOnly}
            sx={{ pr: 2 }}
          />
          <Box
            component={FormControlLabel}
            value={DeliveryMethodEnum.Delivery}
            control={radio('delivery-radio-button')}
            label={t('common.delivery') as string}
            disabled={props.isDisabled || isStandardAccessCashCustomer}
          />
        </RadioGroup>
      </FormControl>
      {isWillCall && !contract && isPricingOnly && (
        <Box
          flex="1"
          display="flex"
          flexDirection={isSmallScreen ? 'column' : 'row'}
          justifyContent={isSmallScreen ? 'flex-start' : 'flex-end'}
          alignItems={isSmallScreen ? 'flex-start' : 'center'}
          pt={isSmallScreen ? 3 : 0}
        >
          <Typography
            color="textPrimary"
            display="inline"
            data-testid="willCall-availability-info"
          >
            {t('branch.willCallUnavailability')}
          </Typography>
        </Box>
      )}
      {((isWillCall && !contract && !isPricingOnly) ||
        (isDelivery && !!contract)) && (
        <Box
          flex="1"
          display="flex"
          flexDirection={isSmallScreen ? 'column' : 'row'}
          justifyContent={isSmallScreen ? 'flex-start' : 'flex-end'}
          alignItems={isSmallScreen ? 'flex-start' : 'center'}
          pt={isSmallScreen ? 3 : 0}
        >
          {!shippingBranchLoading ? (
            <>
              <Typography
                color="textPrimary"
                display="inline"
                data-testid="cart-branch-info"
              >
                {branchInfo}
              </Typography>
              {!contract && (
                <Button
                  variant="inline"
                  color="primaryLight"
                  data-testid="change-branch-button"
                  onClick={() => setBranchSelectOpen(true)}
                  sx={{ ml: isSmallScreen ? 0 : 2 }}
                >
                  {t('branch.changeBranch')}
                </Button>
              )}
            </>
          ) : (
            <Box flex="1" display="flex" justifyContent="center">
              <CircularProgress color="primary02.main" size={28} />
            </Box>
          )}
        </Box>
      )}
    </Box>
  );

  /**
   * Memo Def
   */
  function branchInfoMemo() {
    if (contractData?.jobName && contractAddress) {
      return `${contractData.jobName} : ${contractAddress}`;
    }
    if (shippingBranch?.name && shippingBranch?.address1) {
      return `${shippingBranch.name} : ${shippingBranch.address1}`;
    }
    return t('branch.noSelectedBranch');
  }

  function handleDeliveryMethodChange(
    event: React.ChangeEvent<HTMLInputElement>
  ) {
    const { value } = event.target;
    setDeliveryMethod(value as DeliveryMethodEnum);
    if (cartId && updateCart && props.deliveryMethod !== value) {
      updateCart(cartId, { deliveryMethod: value as DeliveryMethodEnum });
    }
  }
}

export default DeliveryMethod;
