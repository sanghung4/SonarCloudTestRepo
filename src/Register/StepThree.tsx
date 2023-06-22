import { useState, MouseEvent } from 'react';

import { yupResolver } from '@hookform/resolvers/yup';
import cn from 'classnames';
import { useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { useHistory, useLocation } from 'react-router-dom';
import * as yup from 'yup';

import Button from 'components/Button';
import FormTextInput from 'components/FormTextInput';
import FormMaskedInput from 'components/FormMaskedInput';
import {
  useVerifyAccountNewLazyQuery,
  VerifyAccountInput
} from 'generated/graphql';
import { useDomainInfo } from 'hooks/useDomainInfo';
import { defaultCompany } from 'hooks/utils/useDomainInfo';
import { WarningIcon, GreenCheckmark, TradePartnerIcon } from 'icons';
import AccountNumberModal from 'Register/ReceiptModal';
import { testIds } from 'test-utils/testIds';
import 'Register/styles.scss';
/**
 * Types
 */
export interface AccountType {
  isEmployee?: boolean;
  email: string;
  accountNumber: string;
  zipCode: string;
  accountFound: boolean;
  isTradeAccount: boolean;
  accountType: string;
  fortilineCustomer: string;
  accountName: string;
  showMessage: boolean;
}
/**
 * Constants
 */
const TEST_IDS = testIds.Register.StepThree;
const NEW_ACCOUNT_URL = 'https://cloud.e.reece.com/newaccountreg?emailaddress=';

/**
 * Component
 */
function StepThree() {
  /**
   * Props
   */

  /**
   * Custom Hooks
   */
  const { t } = useTranslation();
  const location = useLocation<{
    isEmployee: boolean;
    email: string;
    registerAccount: string;
    accountInfo: AccountType;
    fortilineCustomer: string;
  }>();
  const history = useHistory();
  const { salesforceLink, subdomain, brand, isWaterworks } = useDomainInfo();
  const { control, handleSubmit, watch } = useForm({
    defaultValues: {
      accountNumber: location?.state?.accountInfo?.accountNumber ?? '',
      zipcode: location?.state?.accountInfo?.zipCode ?? ''
    },
    resolver: yupResolver(
      yup.object({
        accountNumber: yup
          .string()
          .when('$isWaterworks', {
            is: (accountNumber: string) => isWaterworks,
            then: yup
              .string()
              .matches(/^\d{6}$/, t('register.accountNumberValidation'))
          })
          .required(t('register.accountNumberIsRequired')),
        zipcode: yup
          .string()
          .required(t('register.BilltoZipCode'))
          .matches(/^\d{5}$/, t('register.BilltoZipCode'))
      })
    )
  });

  /**
   * useState
   */
  const [accountNumberModalOpen, setAccountNumberModalOpen] = useState(false);
  const [accountFound, setAccountFound] = useState(
    Boolean(location?.state?.accountInfo?.accountFound)
  );
  const [accountName, setAccountName] = useState(
    location?.state?.accountInfo?.accountName ?? ''
  );
  const [accountNumber, setAccountNumber] = useState(
    location?.state?.accountInfo?.accountNumber ?? ''
  );
  const [zipCode, setZipCode] = useState(
    location?.state?.accountInfo?.zipCode ?? ''
  );
  const [isTradeAccount, setIsTradeAccount] = useState(
    Boolean(location?.state?.accountInfo?.isTradeAccount)
  );
  const [showMessage, setShowMessage] = useState(
    Boolean(location?.state?.accountInfo?.showMessage)
  );
  const [isLoading, setIsLoading] = useState(false);

  const accountInfo: AccountType = {
    isEmployee: Boolean(
      location?.state?.isEmployee || location?.state?.accountInfo?.isEmployee
    ),
    email: location?.state?.email || location?.state?.accountInfo?.email,
    accountNumber,
    zipCode,
    accountFound,
    isTradeAccount,
    accountType:
      location?.state?.registerAccount ||
      location?.state?.accountInfo?.accountType,
    fortilineCustomer:
      location?.state?.fortilineCustomer ||
      location?.state?.accountInfo?.fortilineCustomer,
    accountName,
    showMessage
  };
  /**
   * useQueries
   */
  const [verifyAccountNew] = useVerifyAccountNewLazyQuery({
    fetchPolicy: 'network-only',
    onCompleted: (data) => {
      if (!data) {
        return;
      }
      const verifyAccountNew = data.verifyAccountNew;
      setShowMessage(true);
      if (verifyAccountNew.accountName && !isWaterworks) {
        setAccountNumber(accountNumberInput);
        setZipCode(zipCodeInput);
        setAccountFound(true);
        setAccountName(verifyAccountNew.accountName);
        setIsTradeAccount(Boolean(verifyAccountNew.isTradeAccount));
      } else if (verifyAccountNew.accountName && isWaterworks) {
        setAccountNumber(accountNumberInput);
        setZipCode(zipCodeInput);
        setAccountFound(true);
        setAccountName(verifyAccountNew.accountName);
        setIsTradeAccount(true);
      }
      setIsLoading(false);
    },
    onError: () => {
      setAccountNumber(accountNumberInput);
      setZipCode(zipCodeInput);
      setShowMessage(true);
      setAccountFound(false);
      setIsTradeAccount(true);
      setIsLoading(false);
    }
  });

  /**
   * Constants
   */
  const accountNumberInput = watch('accountNumber');
  const zipCodeInput = watch('zipcode');
  const isFieldUpdating =
    accountNumberInput !== accountNumber || zipCodeInput !== zipCode;
  /**
   * Render
   */
  return (
    <>
      <AccountNumberModal
        open={accountNumberModalOpen}
        onClose={() => setAccountNumberModalOpen(false)}
      />

      <div className="register-account-info" data-testid={TEST_IDS.page}>
        <form
          className="register-account-info__form"
          onSubmit={handleSubmit(handleAccountMatch)}
        >
          <div className="register-account-info__form__input-wrapper">
            <FormTextInput
              control={control}
              name="accountNumber"
              label={
                isWaterworks
                  ? t('register.BillToAccountNumber')
                  : t('register.accountNumber')
              }
              required
              testId={TEST_IDS.accountNumberInput}
              placeholder="000000"
            />
            <span className="register-account-info__form__subtext mobile">
              {t('register.dontKnowAccountNumber')}
              <Button
                variant="text-link"
                label={t('register.clickHere')}
                size="small"
                onClick={handleReceipt}
                testId={TEST_IDS.openReceiptModal}
              />
            </span>
            <FormMaskedInput
              mask="zipcode"
              className="register-account-info__form__input-wrapper__zipCode"
              control={control}
              name="zipcode"
              label={t('register.zipCode')}
              required
              testId={TEST_IDS.zipCodeInput}
              placeholder="00000"
              pattern="99999"
            />
          </div>
          <span className="register-account-info__form__subtext">
            {t('register.dontKnowAccountNumber')}
            <Button
              variant="text-link"
              label={t('register.clickHere')}
              size="small"
              onClick={handleReceipt}
              testId={TEST_IDS.openReceiptModal}
            />
          </span>
          <div
            className={cn('register-account-info__form__button-wrapper', {
              show: showMessage,
              loading: isLoading,
              found: accountFound,
              findAccount:
                (isFieldUpdating && accountFound) ||
                (isFieldUpdating && isTradeAccount)
            })}
          >
            <Button
              type="submit"
              variant="secondary"
              label={
                isLoading
                  ? t('register.findingAccount')
                  : (isFieldUpdating && accountFound) ||
                    (isFieldUpdating && isTradeAccount)
                  ? t('register.findAccount')
                  : accountFound
                  ? t('register.accountFound')
                  : t('register.findAccount')
              }
              disabled={accountFound && !isFieldUpdating}
              testId={TEST_IDS.findAccountButton}
            />
          </div>
          {showMessage && (
            <div
              className={cn('register-account-info__form__confirmation', {
                found: accountFound,
                notFound: !accountFound,
                tradeNotFound: !isTradeAccount
              })}
              data-testid={TEST_IDS.accountMessage}
            >
              {isTradeAccount &&
                (accountFound ? (
                  <GreenCheckmark className="register-account-info__form__confirmation__icon green-checkmark" />
                ) : (
                  <WarningIcon className="register-account-info__form__confirmation__icon warning" />
                ))}
              {!isTradeAccount && (
                <TradePartnerIcon className="register-account-info__form__confirmation__icon info" />
              )}
              <div className="register-account-info__form__confirmation__subtext ">
                <span className="register-account-info__form__confirmation__subtext__title">
                  {isTradeAccount &&
                    (accountFound
                      ? t('register.accountFound')
                      : t('register.accountNumberNotFound'))}
                  {!isTradeAccount && t('register.notTradeAccount')}
                </span>
                <span
                  className={cn(
                    'register-account-info__form__confirmation__subtext__message',
                    { found: accountFound }
                  )}
                >
                  {isTradeAccount &&
                    (accountFound ? accountName : t('register.incorrectInput'))}
                  {!isTradeAccount &&
                    accountFound &&
                    t('register.tradeAccountMessage')}
                </span>
              </div>
            </div>
          )}
          {showMessage && isTradeAccount && (
            <span className="register-account-info__form__link ">
              {accountFound
                ? `${
                    t('register.notYourAccount') + '\n' + t('register.please')
                  }`
                : `${t('register.dontKnowAccountNumber')}`}
              <Button
                variant="text-link"
                label={
                  accountFound
                    ? `${t('register.contactBranch').toLowerCase()}`
                    : `${t('register.clickHere')}`
                }
                size="small"
                onClick={accountFound ? handleContactBranch : handleReceipt}
                testId={TEST_IDS.contactBranchButton}
              />
            </span>
          )}
          <div className="register-account-info__form__actions">
            <Button
              label={t('register.previous')}
              variant="secondary"
              testId={TEST_IDS.previousButtonMobile}
              onClick={handlePreviousStep}
            />

            <Button
              label={t('register.previous')}
              variant="text-link-dark"
              testId={TEST_IDS.previousButtonDesktop}
              onClick={handlePreviousStep}
            />

            <Button
              type="button"
              label={t('common.next')}
              testId={TEST_IDS.submitButton}
              disabled={!accountFound}
              onClick={handleNextStep}
            />
          </div>
        </form>
      </div>
    </>
  );

  /**
   * Callback Definitions
   */
  function handleReceipt(e: MouseEvent<HTMLButtonElement>) {
    e.preventDefault();
    setAccountNumberModalOpen(true);
  }

  function handleContactBranch(e: MouseEvent<HTMLButtonElement>) {
    e.preventDefault();
    window.open('/location-search');
  }
  async function handleAccountMatch(values: {
    accountNumber: string;
    zipcode: string;
  }) {
    if (
      (!values.accountNumber && !values.zipcode) ||
      ((accountFound || !accountFound) && !isFieldUpdating)
    ) {
      return;
    }
    setTimeout(() => {
      setIsLoading(true);
      verifyAccount(values);
    }, 100);
  }
  async function verifyAccount(values: {
    accountNumber: string;
    zipcode: string;
  }) {
    await verifyAccountNew({
      variables: {
        input: {
          accountNumber: values.accountNumber,
          zipcode: values.zipcode,
          brand: brand
        } as VerifyAccountInput
      }
    });
  }

  function handlePreviousStep(e: MouseEvent<HTMLButtonElement>) {
    e.preventDefault();
    history.push('/register/step-2', {
      isEmployee: Boolean(
        location?.state?.isEmployee || location?.state?.accountInfo?.isEmployee
      ),
      email:
        (location?.state?.email || location?.state?.accountInfo?.email) ?? '',

      accountType:
        (location?.state?.registerAccount ||
          location?.state?.accountInfo?.accountType) ??
        '',
      fortilineCustomer:
        (location?.state?.fortilineCustomer ||
          location?.state?.accountInfo?.fortilineCustomer) ??
        ''
    });
  }
  function handleNextStep() {
    if (accountFound && !isTradeAccount) {
      handleSalesforceData(location?.state?.email, salesforceLink);
    } else if (
      ((!isWaterworks && accountFound) ||
        (isWaterworks && accountNumberInput.length === 6)) &&
      zipCodeInput.length === 5
    ) {
      history.push('/register/step-4', {
        accountInfo
      });
    }
  }
  function handleSalesforceData(email: string, salesforceLink: string) {
    let newAccountLink = NEW_ACCOUNT_URL + email;

    if (subdomain !== defaultCompany.sub) {
      newAccountLink += `&brand=${salesforceLink}`;
    }
    window.open(newAccountLink);
  }
}

export default StepThree;
