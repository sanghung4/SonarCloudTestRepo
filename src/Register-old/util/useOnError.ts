import { useSnackbar } from '@dialexa/reece-component-library';
import { Dispatch } from 'react';
import { useTranslation } from 'react-i18next';
import { useHistory } from 'react-router-dom';
import { Action, Actions } from 'Register-old/reducer';

export function useOnError(dispatch: Dispatch<Action>) {
  /**
   * Custom hooks
   */
  const { pushAlert } = useSnackbar();
  const { t } = useTranslation();
  const history = useHistory();

  /**
   * Methods
   */
  function onAccountMismatchError() {
    dispatch(Actions.SET_ACCOUNT_ERROR({ error: true }));
    pushAlert(t('register.invalidAccount'), { variant: 'error' });
  }

  function onAccountQueryError() {
    dispatch(Actions.SET_ACCOUNT_ERROR({ error: true }));
    pushAlert(t('register.accountNotFound'), { variant: 'error' });
  }

  function onCreateUserError(error: Error) {
    const message = t('register.createFailed');
    tryParseError({ error, message, pushAlert });
    dispatch(Actions.REENTER_EMAIL());
  }

  function onUserApproverError(error: Error) {
    const message = t('register.createFailed');
    tryParseError({ error, message, pushAlert });
  }

  function onUserInviteQueryError(error: Error) {
    const message = t('register.invalidInvite');
    tryParseError({ error, message, pushAlert });
    history.replace({ pathname: '/register', search: '' });
  }

  function onAccountQueryNotBillToError() {
    dispatch(Actions.SET_ACCOUNT_ERROR({ error: true }));
    pushAlert(t('register.accountNotBillTo'), { variant: 'error' });
  }

  /**
   * Output
   */
  return {
    onAccountMismatchError,
    onAccountQueryError,
    onCreateUserError,
    onUserApproverError,
    onUserInviteQueryError,
    onAccountQueryNotBillToError
  };
}

type TryParseErrorProps = {
  error: Error;
  message: string;
  pushAlert: ReturnType<typeof useSnackbar>['pushAlert'];
};
export function tryParseError({
  error,
  message,
  pushAlert
}: TryParseErrorProps) {
  try {
    const errorMessage = JSON.parse(error.message)?.error;
    if (!errorMessage) {
      throw new Error();
    }
    pushAlert(errorMessage, { variant: 'error' });
  } catch {
    console.error('Could not parse error response');
    pushAlert(message, { variant: 'error' });
  }
}
