import { Link } from '@dialexa/reece-component-library';
import { yupResolver } from '@hookform/resolvers/yup';
import { useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { useHistory, useLocation } from 'react-router-dom';
import * as yup from 'yup';

import Button from 'components/Button';
import FormMaskedInput from 'components/FormMaskedInput';
import FormSelectInput from 'components/FormSelectInput';
import FormTextInput from 'components/FormTextInput';
import { SelectInputOption } from 'components/SelectInput';
import { AccountType } from 'Register/StepThree';
import { testIds } from 'test-utils/testIds';
import 'Register/styles.scss';

/**
 * Types
 */
interface PhoneTypeOption {
  label: string;
  value: string;
}

export interface UserType {
  firstName: string;
  lastName: string;
  phoneNumber: string;
  phoneType: PhoneTypeOption;
  phoneTypeValue?: string;
  isEmployee: boolean;
  email: string;
  accountNumber: string;
  accountType: string;
  fortilineCustomer?: string;
  accountName: string;
  zipcode: string;
  accountFound: boolean;
  isTradeAccount: boolean;
  showMessage: boolean;
}
/**
 * Constants
 */
const TEST_IDS = testIds.Register.StepFour;

/**
 * Component
 */
function StepFour() {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();
  const location = useLocation<{
    isEmployee: boolean;
    email: string;
    accountInfo: AccountType;
    contactInfo: UserType;
  }>();
  const history = useHistory();
  const PHONE_TYPE_OPTIONS: SelectInputOption<string>[] = [
    { label: t('register.mobile'), value: 'MOBILE' },
    { label: t('register.home'), value: 'HOME' },
    { label: t('register.office'), value: 'OFFICE' }
  ];
  const { control, handleSubmit } = useForm({
    defaultValues: {
      firstName: location?.state?.contactInfo?.firstName ?? '',
      lastName: location?.state?.contactInfo?.lastName ?? '',
      phoneNumber: location?.state?.contactInfo?.phoneNumber ?? '',
      phoneType: location?.state?.contactInfo?.phoneType ?? {}
    },
    resolver: yupResolver(
      yup.object().shape({
        firstName: yup
          .string()
          .trim()
          .required(t('validation.firstNameRequired')),
        lastName: yup
          .string()
          .trim()
          .required(t('validation.lastNameRequired')),
        phoneNumber: yup
          .string()
          .trim()
          .required(t('validation.phoneNumberRequired'))
          .matches(/^\(\d{3}\) \d{3}-\d{4}$/, {
            message: t('validation.phoneNumberInvalid')
          }),
        phoneType: yup
          .object()
          .shape({
            label: yup.string(),
            value: yup.string().required(t('validation.phoneTypeRequired'))
          })
          .required(t('validation.phoneTypeRequired'))
      })
    )
  });

  const accountInfo: AccountType = {
    isEmployee: Boolean(
      location?.state?.isEmployee ||
        location?.state.accountInfo?.isEmployee ||
        location?.state?.contactInfo?.isEmployee
    ),
    email:
      (location?.state.accountInfo?.email ||
        location?.state?.contactInfo?.email ||
        location?.state?.email) ??
      '',
    accountNumber:
      (location?.state.accountInfo?.accountNumber ||
        location?.state?.contactInfo?.accountNumber) ??
      '',
    zipCode:
      (location?.state.accountInfo?.zipCode ||
        location?.state?.contactInfo?.zipcode) ??
      '',
    accountFound: Boolean(
      location?.state.accountInfo?.accountFound ||
        location?.state?.contactInfo?.accountFound
    ),
    isTradeAccount: Boolean(
      location?.state.accountInfo?.isTradeAccount ||
        location?.state?.contactInfo?.isTradeAccount
    ),
    fortilineCustomer:
      (location?.state.accountInfo?.fortilineCustomer ||
        location?.state?.contactInfo?.fortilineCustomer) ??
      '',
    accountType:
      (location?.state.accountInfo?.accountType ||
        location?.state?.contactInfo?.accountType) ??
      '',
    accountName:
      (location?.state.accountInfo?.accountName ||
        location?.state?.contactInfo?.accountName) ??
      '',
    showMessage:
      (location?.state.accountInfo?.showMessage ||
        location?.state?.contactInfo?.showMessage) ??
      ''
  };

  /**
   * Render
   */
  return (
    <div className="register-contact-info" data-testid={TEST_IDS.page}>
      <div className="register-contact-info__information">
        <span className="register-contact-info__information__info">
          {t('register.information')}
        </span>
        <span className="register-contact-info__information__privacy-policy">
          {t('register.read')}
          <Button
            variant="text-link"
            label={t('common.privacyPolicy')}
            size="small"
            onClick={handlePrivacyPolicy}
          />
        </span>
      </div>
      <form
        className="register-contact-info__form"
        onSubmit={handleSubmit(handleFormSubmit)}
      >
        <div className="register-contact-info__form__input-wrapper">
          <FormTextInput
            control={control}
            name="firstName"
            label={t('common.firstName')}
            required
            placeholder={t('register.enterFirstName')}
            testId={TEST_IDS.firstName}
          />
          <FormTextInput
            control={control}
            name="lastName"
            label={t('common.lastName')}
            required
            placeholder={t('register.enterLastName')}
            testId={TEST_IDS.lastName}
          />
          <FormMaskedInput
            control={control}
            name="phoneNumber"
            label={t('common.phoneNumber')}
            mask="phone"
            required
            placeholder={t('register.phoneNumberPlaceholder')}
            testId={TEST_IDS.phoneNumber}
          />
          <FormSelectInput
            control={control}
            name="phoneType"
            options={PHONE_TYPE_OPTIONS}
            label={t('common.phoneType')}
            required
            testId={TEST_IDS.phoneType}
          />
        </div>
        <div className="register-contact-info__actions">
          <Button
            label={t('register.previous')}
            variant="secondary"
            onClick={() => handlePreviousButton()}
          />
          <Link onClick={() => handlePreviousButton()}>
            <Button label={t('register.previous')} variant="text-link-dark" />
          </Link>
          <Button
            type="submit"
            label={t('common.next')}
            testId={TEST_IDS.submitButton}
          />
        </div>
      </form>
    </div>
  );

  /**
   * Callback Definitions
   */

  function handlePrivacyPolicy() {
    window.open('/privacy-policy');
  }
  function handlePreviousButton() {
    accountInfo.isEmployee
      ? history.push('/register/step-2', {
          isEmployee: true,
          email: accountInfo?.email
        })
      : history.push('/register/step-3', {
          accountInfo
        });
  }

  function handleFormSubmit(values: {
    firstName: string;
    lastName: string;
    phoneNumber: string;
    phoneType: PhoneTypeOption;
  }) {
    const userInfo: UserType = {
      firstName: values.firstName,
      lastName: values.lastName,
      phoneNumber: values.phoneNumber,
      phoneType: values.phoneType,
      phoneTypeValue: values.phoneType.value,
      isEmployee: Boolean(
        location?.state?.isEmployee ||
          location?.state.accountInfo?.isEmployee ||
          location?.state?.contactInfo?.isEmployee
      ),
      email: accountInfo.email,
      accountNumber: accountInfo?.accountNumber ?? '',
      accountType: accountInfo?.accountType ?? '',
      fortilineCustomer: accountInfo?.fortilineCustomer ?? '',
      accountName: accountInfo?.accountName ?? '',
      zipcode: accountInfo?.zipCode ?? '',
      accountFound: Boolean(accountInfo?.accountFound),
      isTradeAccount: Boolean(accountInfo?.isTradeAccount),
      showMessage: Boolean(accountInfo?.showMessage)
    };
    history.push('/register/step-5', { contactInfo: userInfo });
  }
}

export default StepFour;
