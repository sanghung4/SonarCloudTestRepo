import { useSnackbar } from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { useHistory, useLocation } from 'react-router-dom';

import Button from 'components/Button';
import { useResendVerificationEmailMutation } from 'generated/graphql';
import { useDomainInfo } from 'hooks/useDomainInfo';
import { RegistrationCompleteIcon } from 'icons';
import { testIds } from 'test-utils/testIds';

/**
 * Constants
 */
const TEST_IDS = testIds.SignIn;

/**
 * Component
 */
function RegisterComplete() {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();
  const location = useLocation<{
    isEmployee: boolean;
    userId: string;
  }>();
  const history = useHistory();
  const { pushAlert } = useSnackbar();
  const { isWaterworks } = useDomainInfo();
  const { isEmployee, userId } = location.state;

  /**
   * useMutations
   */
  const [resendVerificationEmailMutation] = useResendVerificationEmailMutation({
    onCompleted: () => {
      pushAlert(t('register.emailSentNotification'), { variant: 'success' });
    },
    onError: () => {
      pushAlert(t('register.emailSentErrorNotification'), { variant: 'error' });
    }
  });

  /**
   * Render
   */
  return (
    <div className="register-registration-complete" data-testid={TEST_IDS.page}>
      <RegistrationCompleteIcon className="register-registration-complete__icon" />
      <h1 className="register-registration-complete__title">
        {isEmployee
          ? t('register.checkYourEmail')
          : t('register.registrationComplete')}
      </h1>
      <span
        className="register-registration-complete__text"
        data-testid={TEST_IDS.title}
      >
        {isEmployee
          ? t('register.verifyEmailAddress')
          : t('register.signIntoShop')}
      </span>
      <div
        className="register-registration-complete__actions"
        data-testid={TEST_IDS.text}
      >
        <Button
          type="submit"
          label={isEmployee ? t('register.resendEmail') : t('common.signIn')}
          testId={TEST_IDS.signInButton}
          onClick={() => handleButton()}
        />
      </div>
    </div>
  );

  /**
   * Callback Definitions
   */
  function handleButton() {
    location.state.isEmployee ? onResendEmail() : handleSignIn();
  }

  function handleSignIn() {
    history.push('/login');
  }

  function onResendEmail() {
    resendVerificationEmailMutation({
      variables: {
        userId,
        isWaterworksSubdomain: isWaterworks
      }
    });
  }
}

export default RegisterComplete;
