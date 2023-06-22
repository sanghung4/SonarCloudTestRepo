import {
  useCallback,
  useMemo,
  useReducer,
  useEffect,
  useContext,
  useState
} from 'react';

import {
  Box,
  Container,
  Step as StepperStep,
  StepLabel,
  Stepper,
  Typography,
  useScreenSize,
  useSnackbar
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { useHistory, useLocation } from 'react-router-dom';

import {
  CreateUserMutation,
  AccountQuery,
  ErpAccount,
  useCreateUserMutation,
  useGetContactInfoLazyQuery,
  useAccountLazyQuery,
  useGetUserInviteLazyQuery,
  GetUserInviteQuery,
  useInvitedUserEmailSentLazyQuery,
  useResendLegacyInviteEmailMutation,
  ErpSystemEnum
} from 'generated/graphql';
import Complete from 'Register-old/Complete';
import ContactInformation from 'Register-old/ContactInformation';
import CreatePassword from 'Register-old/CreatePassword';
import EnterAccount from 'Register-old/EnterAccount';
import { Step, Actions, initialState, reducer } from 'Register-old/reducer';
import { useOnError } from 'Register-old/util/useOnError';
import useSearchParam from 'hooks/useSearchParam';
import { AuthContext } from 'AuthProvider';
import Loader from 'common/Loader';
import { identify, trackRegistration } from 'utils/analytics';
import { timestamp } from 'utils/dates';
import { useDomainInfo } from 'hooks/useDomainInfo';
import useDocumentTitle from 'hooks/useDocumentTitle';

function Register() {
  /**
   * Custom hooks
   */
  const history = useHistory();
  const location = useLocation();
  const { isSmallScreen } = useScreenSize();
  const inviteId = useSearchParam('inviteId');
  const { pushAlert } = useSnackbar();
  const { t } = useTranslation();
  const { brand, isWaterworks } = useDomainInfo();
  useDocumentTitle(t('common.registerForMaX'));

  /**
   * Context
   */
  const { authState, handleLogout } = useContext(AuthContext);

  /**
   * State
   */
  const [checkingAuth, setCheckingAuth] = useState(true);
  const [userHasInvite, setUserHasInvite] = useState(false);

  /**
   * Reducer
   */
  const [state, dispatch] = useReducer(reducer, initialState);

  /**
   * Memo
   */
  const stepNumber = useMemo(stepNumberMemo, [state]);

  /**
   * Effect
   */
  useEffect(() => {
    if (inviteId) {
      getUserInvite({ variables: { inviteId } });
    }
    // eslint-disable-next-line
  }, [inviteId]);

  useEffect(() => {
    if (authState?.isAuthenticated) {
      handleLogout?.(location.pathname);
    } else {
      setCheckingAuth(false);
    }
  }, [authState, handleLogout, location]);

  /**
   * Errors
   */
  const {
    onAccountMismatchError,
    onAccountQueryError,
    onCreateUserError,
    onUserApproverError,
    onUserInviteQueryError,
    onAccountQueryNotBillToError
  } = useOnError(dispatch);

  /**
   * Data
   */
  const [createUser, { loading: createLoading }] = useCreateUserMutation({
    onCompleted: onCreateUserCompleted,
    onError: onCreateUserError
  });

  const [
    userApprover,
    { data: contactInfoQuery, loading: contactInfoLoading }
  ] = useGetContactInfoLazyQuery({
    onError: onUserApproverError
  });

  const [getAccount, { loading: accountLoading }] = useAccountLazyQuery({
    onCompleted: onAccountQueryCompleted,
    onError: onAccountQueryError,
    fetchPolicy: 'cache-and-network'
  });

  const [getUserInvite, { data: userInviteData, loading: inviteLoading }] =
    useGetUserInviteLazyQuery({
      onCompleted: onUserInviteQueryCompleted,
      onError: onUserInviteQueryError
    });

  const [isInviteSentQuery] = useInvitedUserEmailSentLazyQuery({
    onCompleted(data) {
      if (!data.invitedUserEmailSent) {
        sendInviteEmail({
          variables: { legacyUserEmail: state.userInput.email ?? '' }
        });
      }
      if (!inviteId) {
        setUserHasInvite(true);
        history.replace({
          pathname: '/max-welcome',
          state: {
            email: state.userInput.email
          }
        });
      }
    }
  });

  const [sendInviteEmail] = useResendLegacyInviteEmailMutation();

  /**
   * Callbacks
   */
  const handleResetAccountError = () => {
    dispatch(Actions.SET_ACCOUNT_ERROR({ error: false }));
  };

  const handleCreatePassword = useCallback(handleCreatePasswordCb, [
    createUser,
    inviteId,
    state,
    isWaterworks
  ]);

  // Renders the correct step based on the reducer
  function getStepComponent() {
    switch (state.step) {
      case Step.ENTER_ACCOUNT_NUMBER:
        return (
          <EnterAccount
            accountNotFound={state.accountError}
            loading={accountLoading || inviteLoading}
            onSubmitAccount={(id, erpName) => {
              if (!id && !erpName) {
                dispatch(Actions.EMPLOYEE_SKIP_SET_ACCOUNT());
              } else {
                dispatch(
                  Actions.SET_ERP_NAME({
                    erpName:
                      userInviteData?.userInvite?.erpSystemName || erpName || ''
                  })
                );
                getAccount({ variables: { accountId: id ?? '', brand } });
              }
            }}
            resetAccountNotFound={handleResetAccountError}
          />
        );

      case Step.ENTER_CONTACT_INFO:
        return (
          <ContactInformation
            contactInfo={state.userInput}
            reenterEmail={state.reenterEmail}
            onSubmitContact={(userInput) => {
              isInviteSentQuery({
                variables: { email: userInput.email ?? '' }
              });
              if (!userHasInvite) {
                dispatch(Actions.SET_CONTACT_INFO({ userInput }));
              }
            }}
            userInviteData={userInviteData}
          />
        );
      case Step.CREATE_PASSWORD:
        return (
          <CreatePassword
            loading={createLoading || contactInfoLoading}
            onCreatePassword={handleCreatePassword}
            userInput={state.userInput}
          />
        );
      case Step.COMPLETE_REGISTRATION:
        return (
          <Complete
            isBranchInfo={contactInfoQuery?.contactInfo?.isBranchInfo}
            email={contactInfoQuery?.contactInfo?.emailAddress}
            phone={contactInfoQuery?.contactInfo?.phoneNumber}
            isInvite={!!inviteId}
          />
        );
      default:
        return null;
    }
  }

  // TODO: tw - handle loader styling
  return checkingAuth || (inviteId && (inviteLoading || accountLoading)) ? (
    <Loader />
  ) : (
    <Container maxWidth="sm">
      {state.step < 3 ? (
        <Box
          mt={isSmallScreen ? 4 : 6}
          display="flex"
          flexDirection="column"
          alignItems="center"
        >
          <Stepper
            activeStep={stepNumber - 1}
            hideLabels
            highlightCompleted={false}
            sx={{
              width: isSmallScreen ? 1 : 300
            }}
          >
            {[1, 2, 3].map((key) => (
              <StepperStep key={key}>
                <StepLabel>&nbsp;</StepLabel>
              </StepperStep>
            ))}
          </Stepper>
          <Typography
            sx={{
              display: 'flex',
              justifyContent: 'center',
              color: 'primary02.main'
            }}
            data-testid="register-step-tracker"
          >
            {t('register.step')} {stepNumber} {t('register.of')} 3
          </Typography>
        </Box>
      ) : null}
      {getStepComponent() ?? t('register.invalidState')}
    </Container>
  );

  /**
   * Data Callbacks
   */
  function onAccountQueryCompleted(data: AccountQuery) {
    if (data?.account) {
      const account = data.account.filter(
        (el) => el?.erpName === state.erpName
      )!;
      account.length === 0
        ? onAccountMismatchError()
        : !data?.account[0].billToFlag &&
          data?.account[0].erpName === ErpSystemEnum.Eclipse
        ? onAccountQueryNotBillToError()
        : dispatch(
            Actions.SET_ACCOUNTS({
              accounts: data.account as ErpAccount[],
              selectedAccount: account.filter(
                (el) => el?.erpName === state.erpName
              )[0] as ErpAccount
            })
          );
    }
  }

  function onCreateUserCompleted(data: CreateUserMutation) {
    if (data.createUser?.id) {
      if (!Boolean(inviteId)) {
        userApprover({ variables: { userId: data.createUser.id } });
      }
      if (data.createUser.isEmployee) {
        history.replace({
          pathname: '/verify',
          state: { userId: data.createUser.id }
        });
      } else {
        dispatch(
          Actions.COMPLETE_REGISTRATION({ userId: data.createUser.id! })
        );
      }
    }

    trackRegistration({
      user: state.userInput.email,
      billTo: state.selectedAccount?.erpAccountId ?? '',
      homeBranch: state.selectedAccount?.branchId,
      timestamp: timestamp
    });
  }

  function onUserInviteQueryCompleted({ userInvite }: GetUserInviteQuery) {
    if (userInvite?.completed) {
      pushAlert(t('register.invalidInvite'), { variant: 'error' });
      history.replace({ pathname: '/register', search: '' });
    } else {
      dispatch(Actions.SET_ERP_NAME({ erpName: userInvite.erpSystemName }));
      getAccount({
        variables: { accountId: userInvite.erpAccountId, brand }
      });
    }
  }

  /**
   * Memo definitions
   */

  function stepNumberMemo() {
    switch (state.step) {
      case Step.ENTER_ACCOUNT_NUMBER:
        return 1;
      case Step.ENTER_CONTACT_INFO:
        return 2;
      case Step.CREATE_PASSWORD:
        return 3;
      default:
        return 0;
    }
  }

  /**
   * Callback definitions
   */
  function handleCreatePasswordCb(password: string) {
    const userInput = {
      ...state.userInput,
      branchId: state.selectedAccount?.branchId ?? null,
      password,
      accountInfo: {
        erpId: state.selectedAccount?.erpName ?? ErpSystemEnum.Eclipse,
        accountNumber: state.selectedAccount?.erpAccountId ?? ''
      },
      userContactTitle: '',
      preferredLocationId: '',
      customerCategory: '',
      tosAccepted: true,
      ppAccepted: true,
      isEmployee: true,
      isWaterworksSubdomain: isWaterworks
    };

    createUser({
      variables: { user: userInput, inviteId }
    });

    identify(state.userInput.email ?? '');
  }
}

export default Register;
