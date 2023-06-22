import { useState } from 'react';

import { Link } from '@dialexa/reece-component-library';
import { yupResolver } from '@hookform/resolvers/yup';
import cn from 'classnames';
import { useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { useHistory, useLocation } from 'react-router-dom';
import * as yup from 'yup';

import Button from 'components/Button';
import Loader from 'common/Loader';
import CheckboxInput from 'components/CheckboxInput';
import FormTextInput from 'components/FormTextInput';
import {
  useCreateNewUserMutation,
  useCreateNewEmployeeMutation,
  PhoneType
} from 'generated/graphql';
import { useDomainInfo } from 'hooks/useDomainInfo';
import { Checkmark, InfoIcon, WarningIcon } from 'icons';
import { regexPatterns } from 'JobForm/utils/forms';
import { UserType } from 'Register/StepFour';
import { testIds } from 'test-utils/testIds';

/**
 * Constants
 */
const TEST_IDS = testIds.Register.StepFive;

/**
 * Component
 */

function StepFive() {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();
  const location = useLocation<{
    contactInfo: UserType;
  }>();
  const history = useHistory();
  const { brand } = useDomainInfo();

  /**
   * Form
   */
  const { control, handleSubmit, watch } = useForm({
    defaultValues: { password: '' },
    resolver: yupResolver(
      yup.object({
        password: yup
          .string()
          .required(t('register.passwordRequired'))
          .min(8, t('register.eightCharacters'))
          .matches(/^(?=.*[A-Z])/, t('register.atLeastOneUppercase'))
          .matches(/^(?=.*\d)/, t('register.number'))
      })
    )
  });

  const passwordInput = watch('password');

  /**
   * useState
   */
  const [isTermOfSales, setIsTermOfSales] = useState(false);
  const [isPrivacyPolicy, setIsPrivacyPolicy] = useState(false);
  const [showErrorMessage, setShowErrorMessage] = useState(false);
  const [showFailedMessage, setShowFailedMessage] = useState(false);
  const [criticalFailure, setCriticalFailure] = useState(false);

  /**
   * GraphQL Hooks
   */
  const [createNewUser, { loading: userLoading }] = useCreateNewUserMutation();
  const [createNewEmployee, { loading: employeeLoading }] =
    useCreateNewEmployeeMutation();

  /**
   * Render
   */
  return (
    <form
      className="register-complete-registration__form"
      data-testid={TEST_IDS.page}
      onSubmit={handleSubmit(handleFormSubmit)}
    >
      {(userLoading || employeeLoading) && !criticalFailure && (
        <Loader backdrop />
      )}
      {showFailedMessage && (
        <>
          <div className="register-complete-registration__form__register-error desktop">
            <WarningIcon className="register-complete-registration__form__register-error desktop__warning-icon" />
            <span className="register-complete-registration__form__register-error desktop__text">
              <b className="register-complete-registration__form__register-error desktop__text__error">
                {t('register.error')}
              </b>
              {t('register.registerFailedDesktop')}
              <br />
              {t('register.IfIssuePersist')}
              <Button
                variant="text-link"
                label={t('register.contactBranch').toLowerCase()}
                size="small"
                onClick={handleContactBranch}
                testId={TEST_IDS.contactBranchLink}
              />
            </span>
          </div>
          <div className="register-complete-registration__form__register-error mobile">
            <WarningIcon className="register-complete-registration__form__register-error mobile__warning-icon" />
            <span className="register-complete-registration__form__register-error mobile__text">
              <b className="register-complete-registration__form__register-error mobile__text__error">
                {t('register.error')}
              </b>
              {t('register.registerFailed')}
              <Button
                variant="text-link"
                label={t('register.contactBranch').toLowerCase()}
                size="small"
                onClick={handleContactBranch}
                testId={TEST_IDS.contactBranchLink}
              />
            </span>
          </div>
        </>
      )}
      <FormTextInput
        className="register-complete-registration__form__password-field"
        control={control}
        name="password"
        label={t('common.password')}
        type="password"
        required
        testId={TEST_IDS.passwordInput}
      />
      <div className="register-complete-registration__form__password-requirements">
        <InfoIcon className="register-complete-registration__form__password-requirements__icon" />
        <span className="register-complete-registration__form__password-requirements__text">
          {t('register.passwordRequirements')}
        </span>
      </div>

      <div className="register-complete-registration__form__password-validation-list">
        <div className="register-complete-registration__form__password-validation-list__item">
          <span
            className={cn(
              'register-complete-registration__form__password-validation-list__item__icon',
              { show: regexPatterns.atLeastEightCharacters.test(passwordInput) }
            )}
          >
            <Checkmark />
          </span>

          <span
            className="register-complete-registration__form__password-validation-list__item__text"
            data-testid={TEST_IDS.containEightCharacters}
          >
            {t('register.eightCharacters')}
          </span>
        </div>
        <div className="register-complete-registration__form__password-validation-list__item">
          <Checkmark
            className={cn(
              'register-complete-registration__form__password-validation-list__item__icon',
              {
                show: regexPatterns.atLeastOneUpperCaseLetter.test(
                  passwordInput
                )
              }
            )}
          />

          <span
            className="register-complete-registration__form__password-validation-list__item__text"
            data-testid={TEST_IDS.containOneUppercaseLetter}
          >
            {t('register.atLeastOneUppercase')}
          </span>
        </div>
        <div className="register-complete-registration__form__password-validation-list__item">
          <Checkmark
            className={cn(
              'register-complete-registration__form__password-validation-list__item__icon',
              { show: regexPatterns.atLeastOneNumber.test(passwordInput) }
            )}
          />
          <span
            className="register-complete-registration__form__password-validation-list__item__text"
            data-testid={TEST_IDS.containOneNumber}
          >
            {t('register.number')}
          </span>
        </div>
      </div>
      <div className="register-complete-registration__form__checkboxes__term-of-sales">
        <CheckboxInput
          className={
            'register-complete-registration__form__checkboxes__term-of-sales__text'
          }
          label={t('register.IAgree')}
          checked={isTermOfSales}
          onChange={(termOfSalesChecked) => {
            setIsTermOfSales(termOfSalesChecked);
          }}
          testId={TEST_IDS.checkboxTermsOfSale}
        />
        <Button
          type="button"
          variant="text-link"
          label={t('legal.termsOfSale')}
          onClick={() => handleTermsofSales()}
          testId={TEST_IDS.termsOfSaleLink}
        />
      </div>
      <div className="register-complete-registration__form__checkboxes__privacy-policy">
        <CheckboxInput
          className="register-complete-registration__form__checkboxes__privacy-policy__text"
          label={t('register.IAgree')}
          name="termOfSalesChecked"
          checked={isPrivacyPolicy}
          onChange={(isPrivacyPolicy) => {
            setIsPrivacyPolicy(isPrivacyPolicy);
          }}
          testId={TEST_IDS.checkPrivacyPolicy}
        />
        <Button
          type="button"
          variant="text-link"
          label={t('legal.privacyPolicy')}
          onClick={() => handlePrivacyPolicy()}
          testId={TEST_IDS.privacyPolicyLink}
        />
      </div>
      {showErrorMessage && (!isTermOfSales || !isPrivacyPolicy) && (
        <div
          className="register-complete-registration__form__error-message"
          data-testid={TEST_IDS.errorMessage}
        >
          {t('register.pleaseCheckBothBoxes')}
        </div>
      )}
      <div className="register-complete-registration__form__actions">
        <Button
          label={t('register.previous')}
          variant="secondary"
          onClick={() => handlePreviousButton()}
          testId={TEST_IDS.previousButtonMobile}
        />
        <Link onClick={() => handlePreviousButton()}>
          <Button
            label={t('register.previous')}
            variant="text-link-dark"
            testId={TEST_IDS.previousButtonDesktop}
          />
        </Link>
        <Button
          type="submit"
          label={t('common.register')}
          testId={TEST_IDS.submitButton}
        />
      </div>
    </form>
  );

  /**
   * Callback Definitions
   */
  function handleContactBranch() {
    window.open('/location-search');
  }

  async function handleFormSubmit(values: { password: string }) {
    // Error handling
    if (!isPrivacyPolicy || !isTermOfSales) {
      setShowErrorMessage(true);
      return;
    }
    // Start
    setCriticalFailure(false);
    try {
      // -- Employee --
      if (location?.state?.contactInfo?.isEmployee) {
        // Prepare
        const employee = {
          email: location?.state?.contactInfo?.email,
          password: values.password,
          firstName: location?.state?.contactInfo?.firstName,
          lastName: location?.state?.contactInfo?.lastName,
          phoneType: location?.state?.contactInfo?.phoneTypeValue as PhoneType,
          phoneNumber: location?.state?.contactInfo?.phoneNumber,
          tosAccepted: isTermOfSales,
          ppAccepted: isPrivacyPolicy,
          brand
        };
        // GQL
        const res = await createNewEmployee({ variables: { employee } });
        // Error handling
        if (!res.data?.createNewEmployee || res.errors?.length) {
          setShowFailedMessage(true);
          return;
        }
        // Go to page
        history.push('/confirmation', {
          isEmployee: location?.state?.contactInfo?.isEmployee,
          userId: res.data.createNewEmployee.id
        });
        return;
      }
      // -- User --
      // Prepare data
      const user = {
        email: location?.state?.contactInfo?.email,
        password: values.password,
        firstName: location?.state?.contactInfo?.firstName,
        lastName: location?.state?.contactInfo?.lastName,
        accountNumber: location?.state?.contactInfo?.accountNumber,
        zipcode: location?.state?.contactInfo?.zipcode,
        phoneType: location?.state?.contactInfo?.phoneTypeValue as PhoneType,
        phoneNumber: location?.state?.contactInfo?.phoneNumber,
        tosAccepted: isTermOfSales,
        ppAccepted: isPrivacyPolicy,
        brand
      };
      // API Call
      const res = await createNewUser({ variables: { user, inviteId: '' } });
      // Error handling
      if (!res.data?.createNewUser || res.errors?.length) {
        setShowFailedMessage(true);
        return;
      }
      // Go to page
      history.push('/confirmation', {
        isEmployee: location?.state?.contactInfo?.isEmployee,
        userId: res.data.createNewUser.id
      });
    } catch (e) {
      // EXCEPTION
      setShowFailedMessage(true);
      setCriticalFailure(true);
    }
  }

  function handlePreviousButton() {
    history.push('/register/step-4', {
      contactInfo: location.state.contactInfo
    });
  }
  function handlePrivacyPolicy() {
    window.open('/privacy-policy');
  }
  function handleTermsofSales() {
    window.open('/terms-of-sale');
  }
}

export default StepFive;
