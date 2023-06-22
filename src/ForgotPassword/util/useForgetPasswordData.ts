import { Dispatch, useContext, useState } from 'react';

import { useSnackbar } from '@dialexa/reece-component-library';
import { AuthTransaction, OktaAuth } from '@okta/okta-auth-js';
import { TFunction, useTranslation } from 'react-i18next';
import { useHistory } from 'react-router-dom';

import { AuthContext } from 'AuthProvider';
import {
  InvitedUserEmailSentQuery,
  useInvitedUserEmailSentLazyQuery,
  useResendLegacyInviteEmailMutation
} from 'generated/graphql';

type SendInviteEmailFn = ReturnType<
  typeof useResendLegacyInviteEmailMutation
>[0];
export type OnCompleteProps = {
  email: string;
  history: ReturnType<typeof useHistory>;
  sendInviteEmail: SendInviteEmailFn;
};
export type OnErrorProps = {
  email: string;
  oktaAuth?: OktaAuth;
  pushAlert: ReturnType<typeof useSnackbar>['pushAlert'];
  setRequestSent: Dispatch<boolean>;
  t: TFunction;
};

export default function useForgetPasswordData() {
  /**
   * Custom hooks
   */
  const history = useHistory();
  const { pushAlert } = useSnackbar();
  const { t } = useTranslation();

  /**
   * Context
   */
  const { oktaAuth } = useContext(AuthContext);

  /**
   * State
   */
  const [email, setEmail] = useState('');
  const [requestSent, setRequestSent] = useState(false);
  const [transaction, setTransaction] = useState<AuthTransaction>();

  /**
   * Queries
   */
  const [sendInviteEmail] = useResendLegacyInviteEmailMutation();
  const [isInviteSentQuery] = useInvitedUserEmailSentLazyQuery({
    onCompleted: onComplete({
      email,
      history,
      sendInviteEmail
    }),
    onError: onError({
      email,
      oktaAuth,
      pushAlert,
      setRequestSent,
      t
    })
  });

  /**
   * Output
   */
  return {
    email,
    isInviteSentQuery,
    requestSent,
    setEmail,
    setRequestSent,
    setTransaction,
    transaction
  };
}

/**
 * Util functions (split off for easier unit testing)
 */
export function onComplete(props: OnCompleteProps) {
  const { email, history, sendInviteEmail } = props;
  return (data: InvitedUserEmailSentQuery) => {
    if (!data.invitedUserEmailSent) {
      sendInviteEmail({ variables: { legacyUserEmail: email } });
    }
    history.replace({
      pathname: '/max-welcome',
      state: { email }
    });
  };
}

export function onError(props: OnErrorProps) {
  const { email, oktaAuth, pushAlert, setRequestSent, t } = props;
  return () => {
    try {
      oktaAuth?.forgotPassword({
        username: email,
        factorType: 'EMAIL'
      });
      setRequestSent(true);
      pushAlert(t('forgotPassword.resetSent'), {
        variant: 'info'
      });
    } catch {
      pushAlert(t('forgotPassword.unableToReset'), {
        variant: 'error'
      });
    }
  };
}
