import { useContext } from 'react';

import {
  Box,
  Button,
  Dialog,
  Divider,
  FormControlLabel,
  Grid,
  Radio,
  RadioGroup,
  Skeleton,
  Typography
} from '@dialexa/reece-component-library';
import { find } from 'lodash';
import { useTranslation } from 'react-i18next';

import CreditCardForm from 'CreditCard/CreditCardForm';
import CreditCardListItem from 'CreditCard/CreditCardListItem';
import { CreditCardContext } from 'CreditCard/CreditCardProvider';
import { CreditCardState } from 'CreditCard/util/config';
import Loader from 'common/Loader';
import {
  handleSavedCardChange,
  handleSubmit
} from 'CreditCard/util/creditCardHandles';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import { CheckoutContext } from 'Checkout/CheckoutProvider';
import { Permission, checkUserPermission } from 'common/PermissionRequired';
import { AuthContext } from 'AuthProvider';

export default function CreditCardPayment() {
  /**
   * Custom Hooks
   */
  const { selectedAccounts } = useSelectedAccountsContext();
  const { t } = useTranslation();

  /**
   * Context
   */
  const {
    addCreditCardLoading,
    creditCard,
    setCreditCard,
    creditCardData,
    creditCardListLoading,
    creditCardState,
    setCreditCardState,
    expiredCreditCards,
    getCreditCardLoading,
    getCreditCardSetupUrl,
    iframeUrl,
    selectedCreditCard,
    setSelectedCreditCard,
    setShouldSaveCreditCard,
    updateCartCreditCard,
    updatingList
  } = useContext(CreditCardContext);
  const { profile } = useContext(AuthContext);
  const { setIsCreditSelectionChanging } = useContext(CheckoutContext);

  const canManagePaymentMethods = checkUserPermission(profile, [
    Permission.MANAGE_PAYMENT_METHODS
  ]);

  const isChangeCC = creditCardState === CreditCardState.CHANGE;
  const isAddCC = creditCardState === CreditCardState.ADD;
  const isNoneCC = creditCardState === CreditCardState.NONE;
  const isSelectedCC = creditCardState === CreditCardState.SELECTED;

  /**
   * Components
   */
  const CreditCardListItemSkeleton = () => (
    <Box display="flex">
      <Skeleton width={38} sx={{ transform: 'none', mr: 8 }} />
      <Skeleton width={140} sx={{ transform: 'none', mr: 8 }} />
      <Skeleton width={43} sx={{ transform: 'none', mr: 8 }} />
      <Skeleton width={55} sx={{ transform: 'none' }} />
    </Box>
  );

  /**
   * Render
   */
  return creditCardListLoading ? (
    <Box minHeight={124} position="relative">
      <Loader />
    </Box>
  ) : (
    <Box mt={7} position="relative">
      {!!(addCreditCardLoading || updatingList) && <Loader backdrop />}
      {!!(creditCard || updatingList) && !isChangeCC && (
        <Grid container alignItems="center">
          <Grid item xs={12} md={3}>
            <Typography variant="body1" color="textSecondary">
              {t('creditCard.selectedCard')}
            </Typography>
          </Grid>
          <Grid item xs={12} md={6}>
            {!creditCard || updatingList ? (
              <CreditCardListItemSkeleton />
            ) : (
              <CreditCardListItem
                creditCard={creditCard}
                expired={expiredCreditCards[creditCard.elementPaymentAccountId]}
              />
            )}
          </Grid>
          <Grid item md={3} />
        </Grid>
      )}
      {isChangeCC && (
        <Grid container>
          <Grid item xs={12} md={3}>
            <Typography variant="body1" color="textSecondary" py={0.25}>
              {t('creditCard.savedCards')}
            </Typography>
          </Grid>
          <Grid item xs={12} md={6}>
            <RadioGroup
              aria-label="saved-cards"
              name="savedCards"
              value={selectedCreditCard}
              onChange={handleSavedCardChange(setSelectedCreditCard)}
            >
              {!!creditCardData &&
                creditCardData.map((creditCardListItem, i) => (
                  <FormControlLabel
                    key={`credit-card-${i}`}
                    sx={{ mt: i > 0 ? 2 : 0 }}
                    value={creditCardListItem.elementPaymentAccountId}
                    control={<Radio />}
                    disabled={
                      expiredCreditCards[
                        creditCardListItem.elementPaymentAccountId
                      ]
                    }
                    label={
                      <CreditCardListItem
                        creditCard={creditCardListItem}
                        expired={
                          expiredCreditCards[
                            creditCardListItem.elementPaymentAccountId
                          ]
                        }
                      />
                    }
                  />
                ))}
            </RadioGroup>
            <Button
              variant="inline"
              color="primaryLight"
              onClick={() => setCreditCardState(CreditCardState.ADD)}
              data-testid="add-card-button"
              sx={{ mt: 2, ml: 4.125 }}
            >
              {t('creditCard.addCard')}
            </Button>
          </Grid>
          <Grid item md={3} />
        </Grid>
      )}
      {isAddCC && (
        <>
          {(updatingList || getCreditCardLoading) && <Loader backdrop />}
          <Grid container>
            <Grid item xs={12}>
              {!!creditCard && (
                <Box mt={4}>
                  <Divider light />
                </Box>
              )}
              <Box mt={creditCard ? 4 : 0}>
                <CreditCardForm
                  onCancel={creditCardData?.length ? handleCancel : undefined}
                  onSubmit={handleSubmit(
                    setShouldSaveCreditCard,
                    getCreditCardSetupUrl,
                    setIsCreditSelectionChanging,
                    selectedAccounts.billTo?.erpAccountId
                  )}
                  hideSaveCard={!canManagePaymentMethods}
                  canSaveCard={canManagePaymentMethods}
                />
              </Box>
            </Grid>
          </Grid>
        </>
      )}
      <Grid container>
        <Grid item md={3} />
        <Grid item xs={12} md={9}>
          <Box mt={4} display="flex" alignItems="center">
            {isChangeCC && (
              <>
                <Button
                  variant="alternative"
                  onClick={() => {
                    const newCreditCard = find(creditCardData ?? [], {
                      elementPaymentAccountId: selectedCreditCard
                    });
                    setCreditCard(newCreditCard);
                    updateCartCreditCard(newCreditCard);
                    setIsCreditSelectionChanging(false);
                    setCreditCardState(CreditCardState.SELECTED);
                    setSelectedCreditCard('');
                  }}
                  data-testid="use-selected-card-button"
                  disabled={selectedCreditCard === ''}
                >
                  {t('creditCard.useSelectedCard')}
                </Button>
                {!!creditCard && (
                  <Button
                    variant="text"
                    color="primaryLight"
                    onClick={handleCancel}
                    data-testid="cancel-button"
                  >
                    {t('common.cancel')}
                  </Button>
                )}
              </>
            )}
            {isSelectedCC &&
              !!creditCardData &&
              creditCardData.length > 1 &&
              canManagePaymentMethods && (
                <>
                  <Button
                    variant="inline"
                    color="primaryLight"
                    onClick={() => {
                      setIsCreditSelectionChanging(true);
                      setCreditCardState(CreditCardState.CHANGE);
                    }}
                    data-testid="change-card-button"
                  >
                    {t('creditCard.changeCard')}
                  </Button>
                  <Divider orientation="vertical" flexItem sx={{ mx: 1 }} />
                </>
              )}
            {(isNoneCC || isSelectedCC) && (
              <Button
                variant="inline"
                color="primaryLight"
                onClick={() => {
                  setIsCreditSelectionChanging(true);
                  setCreditCardState(CreditCardState.ADD);
                }}
                data-testid="add-card-button"
              >
                {t('creditCard.addCard')}
              </Button>
            )}
          </Box>
        </Grid>
      </Grid>
      <Dialog open={Boolean(iframeUrl)} maxWidth={false}>
        <iframe
          id="world-pay-iframe"
          title="Add Credit Card"
          width="800"
          height="500"
          src={iframeUrl}
        />
      </Dialog>
    </Box>
  );

  /**
   * Event callbacks
   */
  function handleCancel() {
    setIsCreditSelectionChanging(false);
    setCreditCardState(
      creditCard ? CreditCardState.SELECTED : CreditCardState.CHANGE
    );
  }
}
