import { useContext } from 'react';

import { useSnackbar } from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { Redirect, useHistory, Link } from 'react-router-dom';

import { AuthContext } from 'AuthProvider';
import useDocumentTitle from 'hooks/useDocumentTitle';
import Container from 'components/Container';

import './styles.scss';
import { useForm, useWatch } from 'react-hook-form';
import FormTextInput from 'components/FormTextInput';
import Button from 'components/Button';
import CheckboxInput from 'components/CheckboxInput';
import { Clock, MyList, ShippingBox, USDCircle } from 'icons';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';
import {
  useInvitedUserEmailSentLazyQuery,
  useResendLegacyInviteEmailMutation
} from 'generated/graphql';
import { testIds } from 'test-utils/testIds';

const TEST_IDS = testIds.SignIn;

export default function Login() {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();
  const { pushAlert } = useSnackbar();
  const history = useHistory();
  useDocumentTitle(t('common.logInToMaX'));

  /**
   * Form
   */
  const { control, handleSubmit } = useForm({
    defaultValues: { email: '', password: '' },
    resolver: yupResolver(
      yup.object({
        email: yup
          .string()
          .email(t('validation.emailInvalid'))
          .required(t('validation.emailRequired')),
        password: yup.string().required(t('validation.passwordRequired'))
      })
    )
  });
  const email = useWatch({ control, name: 'email' });

  /**
   * Context
   */
  const { authState, login } = useContext(AuthContext);

  /**
   * Mutations
   */
  const [sendInviteEmail] = useResendLegacyInviteEmailMutation();

  /**
   * Queries
   */
  const [isInviteSentQuery] = useInvitedUserEmailSentLazyQuery({
    onCompleted: (data) => {
      if (!data.invitedUserEmailSent) {
        sendInviteEmail({ variables: { legacyUserEmail: email } });
      }
      history.replace({
        pathname: '/max-welcome',
        state: { email }
      });
    },
    onError: () => {
      pushAlert(t('validation.incorrectCreds'), { variant: 'error' });
    }
  });

  /**
   * Render
   */
  if (authState?.isAuthenticated) {
    return <Redirect to="/" />;
  }
  return (
    <Container className="login" testId={TEST_IDS.page}>
      <div className="login__card">
        <div className="login__card__sign-in">
          <h1 className="login__card__title">{t('common.signIn')}</h1>
          {/* sign in form */}
          <form
            className="login__card__sign-in__form"
            onSubmit={handleSubmit(handleFormSubmit)}
          >
            {/* email input */}
            <FormTextInput
              control={control}
              name="email"
              label={t('common.emailAddress')}
              testId={TEST_IDS.emailInput}
              required
            />
            {/* password input */}
            <FormTextInput
              control={control}
              name="password"
              type="password"
              label={t('common.password')}
              testId={TEST_IDS.passwordInput}
              required
            />
            {/* forgot password link */}
            <Link
              to={`/forgot-password?email=${encodeURIComponent(email)}`}
              className="login__card__sign-in__form__forgot-password"
            >
              <Button
                label={t('common.forgotPassword')}
                variant="text-link"
                size="small"
                type="button"
              />
            </Link>
            {/* keep logged in checkbox */}
            <CheckboxInput
              className="login__card__sign-in__form__checkbox"
              label={t('common.keepLoggedIn')}
            />
            {/* submit button */}
            <Button
              className="login__card__sign-in__form__submit"
              type="submit"
              label={t('common.signIn')}
              testId={TEST_IDS.signInButton}
            />
          </form>
        </div>
        {/* Benefits card */}
        <div className="login__card__sign-up">
          <h1 className="login__card__title">{t('signIn.notSignedUp')}</h1>
          <h3 className="login__card__subtitle">
            {t('signIn.createTradeAccount')}
          </h3>
          <Link to="/register">
            <Button
              variant="alternative"
              size="large"
              label="Register for maX"
            />
          </Link>
          <h3 className="login__card__sign-up__benefits-title">
            {t('signIn.missingOut')}
          </h3>
          <div className="login__card__sign-up__benefits">
            <div className="login__card__sign-up__benefits__item">
              <USDCircle />
              <h3>{t('signIn.pricing')}</h3>
              <p>{t('signIn.pricingText')}</p>
            </div>
            <div className="login__card__sign-up__benefits__item">
              <Clock />
              <h3>{t('signIn.fastOrdering')}</h3>
              <p>{t('signIn.fastOrderingText')}</p>
            </div>
            <div className="login__card__sign-up__benefits__item">
              <ShippingBox />
              <h3>{t('signIn.purchaseHistory')}</h3>
              <p>{t('signIn.purchaseHistoryText')}</p>
            </div>
            <div className="login__card__sign-up__benefits__item">
              <MyList />
              <h3>{t('signIn.lists')}</h3>
              <p>{t('signIn.listsText')}</p>
            </div>
          </div>
        </div>
      </div>
    </Container>
  );

  /**
   * Callback Definitions
   */
  async function handleFormSubmit(values: { email: string; password: string }) {
    try {
      await login?.({ username: values.email, password: values.password });
    } catch {
      isInviteSentQuery({ variables: { email: values.email } });
    }
  }
}
