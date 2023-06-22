import { useEffect } from 'react';

import { Link } from '@dialexa/reece-component-library';
import { RadioGroup } from '@headlessui/react';
import { yupResolver } from '@hookform/resolvers/yup';
import cn from 'classnames';
import { Controller, useForm, useWatch } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { useHistory, useLocation } from 'react-router-dom';
import * as yup from 'yup';

import Button from 'components/Button';
import FormRadioInput from 'components/FormRadioInput';
import { useDomainInfo } from 'hooks/useDomainInfo';
import { defaultCompany } from 'hooks/utils/useDomainInfo';
import { GreenCheckmark } from 'icons';
import { testIds } from 'test-utils/testIds';
import { generateCompanyUrl } from 'utils/brandList';
import { Configuration } from 'utils/configuration';
// TO DO: ADD and Test https://cloud.e.reece.com/newaccountreg?emailaddress= in .env file
/**
 * Constants
 */
const TEST_IDS = testIds.Register.StepTwo;
const NEW_ACCOUNT_URL = 'https://cloud.e.reece.com/newaccountreg?emailaddress=';

/**
 * Types
 */
enum AccountTypes {
  New = 'new',
  Existing = 'existing'
}
/**
 * Component
 */
function StepTwo() {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();
  const location = useLocation<{
    isEmployee: boolean;
    email: string;
    accountType: string;
    fortilineCustomer: string;
    account: string;
  }>();
  const history = useHistory();
  const { salesforceLink, subdomain, port, brand } = useDomainInfo();

  /**
   * Form
   */
  const { control, handleSubmit, resetField } = useForm({
    defaultValues: {
      accountType:
        (location?.state?.accountType || location?.state?.account) ?? '',
      fortilineCustomer: location?.state?.fortilineCustomer ?? ''
    },
    resolver: yupResolver(
      yup.object({
        accountType: yup.string().required(t('validation.accountTypeRequired')),
        fortilineCustomer: yup.string().when('accountType', {
          is: (accountType: string) => {
            return accountType === 'existing' && brand === 'Reece';
          },
          then: yup.string().required(t('validation.yesNoRequired'))
        })
      })
    )
  });
  const accountType = useWatch({ control, name: 'accountType' });
  const fortilineCustomer = useWatch({ control, name: 'fortilineCustomer' });
  /**
   * useEffects
   */
  // eslint-disable-next-line react-hooks/exhaustive-deps
  useEffect(resetFortilinCustomerField, [accountType]);
  /**
   * Render
   */
  return (
    <div className="register-company" data-testid={TEST_IDS.page}>
      {location.state.isEmployee ? (
        <div
          className="register-company__employee"
          data-testid={TEST_IDS.employee}
        >
          <h2 className="register-company__employee__title">
            {t('register.reeceEmployee')}
          </h2>
          <p className="register-company__employee__text">
            {t('register.isCorrect')}
          </p>
          <p className="register-company__employee__subtext">
            {t('register.differentEmail')}
          </p>
          {/* Links to previous page or step-4 */}
          <div className="register-company__employee__actions">
            <Link href="/register/step-1">
              <Button label={t('common.no')} variant="secondary" />
            </Link>
            <Link onClick={() => handleStepFour()}>
              <Button label={t('register.amEmployee')} />
            </Link>
          </div>
        </div>
      ) : (
        <form
          className="register-company__form"
          onSubmit={handleSubmit(handleFormSubmit)}
          data-testid={TEST_IDS.form}
        >
          {/* Account type selector */}
          <Controller
            control={control}
            name="accountType"
            render={({ field, fieldState }) => (
              <RadioGroup
                className="register-company__radio-group"
                {...field}
                data-testid={TEST_IDS.accountRadio}
              >
                <RadioGroup.Label>{t('register.accountType')}</RadioGroup.Label>
                {/* Options */}
                <div className="register-company__radio-group__options-wrapper">
                  {/* New option */}
                  <RadioGroup.Option
                    value={AccountTypes.New}
                    className={({ checked }) =>
                      cn('register-company__radio-group__option', {
                        checked: checked
                      })
                    }
                  >
                    {({ checked }) => (
                      <>
                        {checked && (
                          <GreenCheckmark className="register-company__radio-group__option__check" />
                        )}
                        <span className="register-company__radio-group__option__title">
                          {t('register.newAccount')}
                        </span>
                        <span className="register-company__radio-group__option__description">
                          {t('register.registerNewAccountDescription')}
                        </span>
                      </>
                    )}
                  </RadioGroup.Option>
                  {/* Existing option */}
                  <RadioGroup.Option
                    value={AccountTypes.Existing}
                    className={({ checked }) =>
                      cn('register-company__radio-group__option', {
                        checked: checked
                      })
                    }
                  >
                    {({ checked }) => (
                      <>
                        {checked && (
                          <GreenCheckmark className="register-company__radio-group__option__check" />
                        )}
                        <span className="register-company__radio-group__option__title">
                          {t('register.existing')}
                        </span>
                        <span className="register-company__radio-group__option__description">
                          {t('register.registerExistingAccountDescription')}
                        </span>
                      </>
                    )}
                  </RadioGroup.Option>
                </div>
                {/* Error Message */}
                <span className="register-company__radio-group__message">
                  {fieldState.error?.message}
                </span>
              </RadioGroup>
            )}
          />
          {/* Are you a Fortiline Waterworks customer? */}
          {accountType === AccountTypes.Existing && brand === 'Reece' && (
            <FormRadioInput
              testId={TEST_IDS.customerRadio}
              options={[
                { label: t('common.yes'), value: 'yes' },
                { label: t('common.no'), value: 'no' }
              ]}
              label="Are you a Fortiline Waterworks customer?"
              control={control}
              name="fortilineCustomer"
            />
          )}
          {fortilineCustomer === 'yes' && (
            <span
              className="register-company__form__waterworks__account"
              data-testid={TEST_IDS.fortiline}
            >
              {t('register.registerWaterworksAccountDescription')}
              <Link
                href={generateCompanyUrl(
                  'fortiline',
                  port,
                  Configuration.environment,
                  true
                )}
              >
                {t('register.fortilineLink')}
              </Link>
            </span>
          )}

          <div className="register-company__form__actions">
            <Button
              label={t('common.previous')}
              variant="secondary"
              onClick={handlePreviousButton}
            />
            <Link onClick={handlePreviousButton}>
              <Button label={t('common.previous')} variant="text-link-dark" />
            </Link>
            <Button
              testId={TEST_IDS.submitButton}
              label={t('common.next')}
              type="submit"
              disabled={fortilineCustomer === 'yes'}
            />
          </div>
        </form>
      )}
    </div>
  );

  /**
   * Hoisted useEffect Definitions
   */
  function resetFortilinCustomerField() {
    resetField('fortilineCustomer');
  }

  /**
   * Callback Definitions
   */
  function handleFormSubmit(values: {
    accountType: string;
    fortilineCustomer: string;
  }) {
    if (values.accountType === AccountTypes.Existing) {
      history.push('/register/step-3', {
        isEmployee: Boolean(location?.state?.isEmployee),
        email: location?.state?.email ?? '',
        registerAccount: accountType,
        fortilineCustomer: fortilineCustomer
      });
    } else if (values.accountType === AccountTypes.New) {
      handleSalesforceData(location?.state?.email, salesforceLink);
    }
  }
  function handleStepFour() {
    history.push('/register/step-4', {
      isEmployee: Boolean(location?.state?.isEmployee),
      email: location?.state?.email ?? '',
      account: (location?.state?.accountType || location?.state?.account) ?? '',
      fortilineCustomer:
        (location?.state?.fortilineCustomer ||
          location?.state?.fortilineCustomer) ??
        ''
    });
  }
  function handlePreviousButton() {
    history.push('/register/step-1', {
      email: location?.state?.email ?? '',
      account: (location?.state?.accountType || location?.state?.account) ?? ''
    });
  }

  function handleSalesforceData(email: string, salesforceLink: string) {
    let newAccountLink = NEW_ACCOUNT_URL + encodeURIComponent(email).trim();

    if (subdomain !== defaultCompany.sub) {
      newAccountLink += `&brand=${encodeURIComponent(salesforceLink).trim()}`;
    }
    window.open(newAccountLink);
  }
}

export default StepTwo;
