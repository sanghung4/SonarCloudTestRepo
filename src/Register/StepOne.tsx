import { useEffect, useState } from 'react';

import { yupResolver } from '@hookform/resolvers/yup';
import { useTranslation } from 'react-i18next';
import { useForm, useWatch } from 'react-hook-form';
import { Link, useHistory, useLocation } from 'react-router-dom';
import * as yup from 'yup';

import Button from 'components/Button';
import FormTextInput from 'components/FormTextInput';
import { useVerifyUserEmailLazyQuery } from 'generated/graphql';
import useSearchParam from 'hooks/useSearchParam';
import 'Register/styles.scss';
import { testIds } from 'test-utils/testIds';

/**
 * Types
 */
export type ContactParams = {
  email: string;
};

/**
 * Constants
 */
const TEST_IDS = testIds.Register.StepOne;

/**
 * Component
 */
function StepOne() {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();
  const location =
    useLocation<{ isEmployee: boolean; email: string; account: string }>();
  const history = useHistory();
  const emailAddress = useSearchParam('email');
  /**
   * Form
   */
  const { control, handleSubmit } = useForm({
    defaultValues: {
      email: (emailAddress || location?.state?.email) ?? ''
    },
    resolver: yupResolver(
      yup.object({
        email: yup
          .string()
          .email(t('validation.emailInvalid'))
          .required(t('validation.emailRequired'))
      })
    )
  });
  const email = useWatch({ control, name: 'email' });

  /**
   * useState
   */
  const [accountExists, setAccountExists] = useState(false);

  /**
   * useQueries
   */
  const [verifyUserEmail] = useVerifyUserEmailLazyQuery({
    fetchPolicy: 'network-only',
    onCompleted: ({ verifyUserEmail }) => {
      if (verifyUserEmail.isValid) {
        history.push('/register/step-2', {
          isEmployee: verifyUserEmail.isEmployee,
          email: email,
          account: location?.state?.account ?? ''
        });
      } else {
        throw new Error();
      }
    },
    onError: () => setAccountExists(true)
  });

  /**
   * useEffects
   */
  useEffect(resetAccountExists, [email]);

  /**
   * Render
   */
  return (
    <div className="register-email" data-testid={TEST_IDS.page}>
      <form
        className="register-email__form"
        onSubmit={handleSubmit(handleFormSubmit)}
      >
        <FormTextInput
          control={control}
          name="email"
          label={t('common.emailAddress')}
          required
          testId={TEST_IDS.emailInput}
          placeholder={t('register.enterEmailAddress')}
        />
        {accountExists && (
          <span
            className="register-email__form__account-exists"
            data-testid={TEST_IDS.alreadyExists}
          >
            {t('register.accountExists')}
            <Link to="/login">{t('register.logInHere')}</Link>
          </span>
        )}

        <Button
          type="submit"
          className="register-email__form__submit-button"
          label={t('common.next')}
          testId={TEST_IDS.submitButton}
        />
      </form>
    </div>
  );

  /**
   * Hoisted useEffect Definitions
   */
  function resetAccountExists() {
    setAccountExists(false);
  }

  /**
   * Callback Definitions
   */
  function handleFormSubmit(values: { email: string }) {
    verifyUserEmail({ variables: { email: values.email } });
  }
}

export default StepOne;
