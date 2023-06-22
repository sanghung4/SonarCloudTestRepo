import { useContext, useState } from 'react';

import {
  Box,
  Button,
  Card,
  CardActions,
  CardContent,
  CardHeader,
  Container,
  Dialog,
  Divider,
  Grid,
  List,
  Typography
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { CreditCardContext } from 'CreditCard/CreditCardProvider';
import CreditCardForm from 'CreditCard/CreditCardForm';
import CreditCardListItem from 'CreditCard/CreditCardListItem';
import { CreditCardState } from 'CreditCard/util/config';
import Breadcrumbs from 'common/Breadcrumbs';
import Loader from 'common/Loader';
import PaymentDialog from './PaymentDialog';
import { AddIcon } from 'icons';
import { formatDate } from 'utils/dates';
import { CardHolderInput, CreditCard } from 'generated/graphql';
import useDocumentTitle from 'hooks/useDocumentTitle';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import {
  PaymentInfoAddCardGrid,
  PaymentInfoCaptionGrid,
  PaymentInfoContainerCard,
  PaymentInfoListItem
} from 'PaymentInformation/util/styles';

function PaymentInformation() {
  /**
   * Custom hooks
   */
  const { t } = useTranslation();
  useDocumentTitle(t('common.paymentInformation'));

  /**
   * State
   */
  const [cardToDelete, setCardToDelete] = useState<CreditCard>();

  /**
   * Context
   */
  const {
    creditCardData,
    creditCardListLoading,
    creditCardState,
    deleteCreditCard,
    deleteCreditCardLoading,
    expiredCreditCards,
    getCreditCardSetupUrl,
    iframeUrl,
    parsingCCResponse,
    setCreditCardState,
    setShouldSaveCreditCard,
    updatingList
  } = useContext(CreditCardContext);
  const { selectedAccounts } = useSelectedAccountsContext();

  /**
   * Render
   */
  return (
    <>
      <Breadcrumbs pageTitle="Payment Information" />
      <Container data-testid="payment-information-container">
        <PaymentInfoContainerCard raised>
          <Grid container spacing={2}>
            <PaymentInfoCaptionGrid container item md={10} xs={12}>
              <Typography variant="h5">
                {t('common.paymentInformation')}
              </Typography>
            </PaymentInfoCaptionGrid>
            {creditCardState !== CreditCardState.ADD && (
              <PaymentInfoAddCardGrid container item md={2} xs={12}>
                <Button
                  onClick={() => setCreditCardState(CreditCardState.ADD)}
                  color="primaryLight"
                  variant="inline"
                  startIcon={<AddIcon />}
                  data-testid="add-card-button"
                >
                  {t('creditCard.addCard')}
                </Button>
              </PaymentInfoAddCardGrid>
            )}
          </Grid>
        </PaymentInfoContainerCard>
        <Box mb={15}>
          {creditCardListLoading ||
          parsingCCResponse ||
          (updatingList && creditCardState === CreditCardState.ADD) ? (
            <Loader />
          ) : (
            Boolean(creditCardData) &&
            (creditCardState === CreditCardState.ADD ? (
              <Grid container p={3} data-testid="payment-information-add-card">
                <Grid item xs={12}>
                  <Typography variant="h4">
                    {t('creditCard.addCard')}
                  </Typography>
                </Grid>
                <Grid item xs={12} md={9}>
                  <Box my={2}>
                    <Divider />
                  </Box>
                </Grid>
                <Grid item md={3} />
                <Grid item xs={12} md={6}>
                  <CreditCardForm
                    onCancel={handleCancel}
                    onSubmit={handleSubmit}
                    hideSaveCard
                  />
                </Grid>
              </Grid>
            ) : (
              <Grid
                container
                spacing={3}
                position="relative"
                data-testid="payment-information-subcontainer"
              >
                {Boolean(deleteCreditCardLoading || updatingList) && (
                  <Loader backdrop size="page" />
                )}
                {creditCardData!.map((card, i) => (
                  <Grid item xs={12} md={6} key={i}>
                    <Card raised>
                      {expiredCreditCards[card.elementPaymentAccountId] ? (
                        <CardHeader
                          title={t('creditCard.expired')}
                          titleTypographyProps={{ color: 'error' }}
                          data-testid="card-header-expired"
                        />
                      ) : (
                        <CardHeader title={'\u200B'} />
                      )}
                      <CardContent>
                        <List>
                          <PaymentInfoListItem disableGutters>
                            <Box flexGrow={1}>
                              <Typography>
                                {t('creditCard.nameOnCard')}:
                              </Typography>
                            </Box>
                            <Typography component="span" fontWeight={700}>
                              {card.cardHolder}
                            </Typography>
                          </PaymentInfoListItem>
                          <Divider />
                          <PaymentInfoListItem disableGutters>
                            <Box flexGrow={1}>
                              <Typography>
                                {t('creditCard.cardNumber')}:
                              </Typography>
                            </Box>
                            <CreditCardListItem
                              creditCard={card}
                              expired={
                                expiredCreditCards[card.elementPaymentAccountId]
                              }
                              hideDate
                              hideExpired
                            />
                          </PaymentInfoListItem>
                          <Divider />
                          <PaymentInfoListItem disableGutters>
                            <Box flexGrow={1}>
                              <Typography
                                color={
                                  expiredCreditCards[
                                    card.elementPaymentAccountId
                                  ]
                                    ? 'error'
                                    : 'initial'
                                }
                              >
                                {t('creditCard.expirationDate')}:
                              </Typography>
                            </Box>
                            <Typography
                              component="span"
                              color={
                                expiredCreditCards[card.elementPaymentAccountId]
                                  ? 'error'
                                  : 'initial'
                              }
                              fontWeight={700}
                            >
                              {formatDate(card.expirationDate.date, 'MM/yy')}
                            </Typography>
                          </PaymentInfoListItem>
                        </List>
                      </CardContent>
                      <CardActions>
                        <Button
                          variant="text"
                          color="primaryLight"
                          onClick={() => {
                            setCardToDelete(card);
                            setCreditCardState(CreditCardState.DELETE);
                          }}
                          data-testid="delete-card-button"
                        >
                          {t('creditCard.deleteCard')}
                        </Button>
                      </CardActions>
                    </Card>
                  </Grid>
                ))}
              </Grid>
            ))
          )}
        </Box>
      </Container>
      <PaymentDialog
        open={
          !deleteCreditCardLoading &&
          Boolean(cardToDelete) &&
          creditCardState === CreditCardState.DELETE
        }
        onClose={handleClose}
        handleDelete={() => handleDeleteCreditCard(cardToDelete!)}
        cardToDelete={cardToDelete}
      />
      <Dialog open={Boolean(iframeUrl)} maxWidth={false}>
        <iframe
          id="world-pay-iframe"
          title="Add Credit Card"
          width="800"
          height="500"
          src={iframeUrl}
        />
      </Dialog>
    </>
  );

  function handleSubmit(cardHolderInput: CardHolderInput) {
    setShouldSaveCreditCard(true);
    getCreditCardSetupUrl?.({
      variables: {
        accountId: selectedAccounts.billTo?.erpAccountId ?? '',
        cardHolderInput
      }
    });
  }

  function handleCancel() {
    setCreditCardState(CreditCardState.NONE);
  }

  function handleClose() {
    setCardToDelete(undefined);
    setCreditCardState(CreditCardState.NONE);
  }

  function handleDeleteCreditCard(card: CreditCard) {
    const creditCard = { ...card };
    const { date } = creditCard.expirationDate;
    creditCard.expirationDate = { date: formatDate(date, 'MM/dd/yyyy') };

    deleteCreditCard(
      selectedAccounts.billTo?.erpAccountId ?? '',
      creditCard.elementPaymentAccountId
    );

    setCreditCardState(CreditCardState.NONE);
  }
}

export default PaymentInformation;
