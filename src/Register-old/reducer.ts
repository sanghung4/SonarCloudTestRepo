import { UnionOf, unionize, ofType } from 'unionize';

import { ErpAccount, UserInput } from 'generated/graphql';

export enum Step {
  ENTER_ACCOUNT_NUMBER,
  ENTER_CONTACT_INFO,
  CREATE_PASSWORD,
  COMPLETE_REGISTRATION
}

export type State = {
  accountError: boolean;
  accountId?: string;
  selectedAccount?: ErpAccount;
  erpAccounts?: ErpAccount[];
  erpName?: string;
  reenterEmail: boolean;
  step: Step;
  userInput: UserInput;
};

export const Actions = unionize(
  {
    COMPLETE_REGISTRATION: ofType<{ userId: string }>(),
    EMPLOYEE_SKIP_SET_ACCOUNT: {},
    REENTER_ACCOUNT_NUMBER: {},
    REENTER_EMAIL: {},
    SET_ACCOUNT_ERROR: ofType<{ error: boolean }>(),
    SET_ACCOUNTS: ofType<{
      accounts: ErpAccount[];
      selectedAccount: ErpAccount;
    }>(),
    SET_ERP_NAME: ofType<{ erpName: string }>(),
    SET_CONTACT_INFO: ofType<{ userInput: UserInput }>()
  },
  {
    tag: 'type',
    value: 'payload'
  }
);

export type Action = UnionOf<typeof Actions>;

export const initialState: State = {
  accountError: false,
  reenterEmail: false,
  step: Step.ENTER_ACCOUNT_NUMBER,
  userInput: {}
};

export const reducer = (state: State, action: Action): State =>
  Actions.match(action, {
    COMPLETE_REGISTRATION: () => ({
      ...state,
      step: Step.COMPLETE_REGISTRATION
    }),
    EMPLOYEE_SKIP_SET_ACCOUNT: () => ({
      ...state,
      step: Step.ENTER_CONTACT_INFO
    }),
    REENTER_ACCOUNT_NUMBER: () => initialState,
    REENTER_EMAIL: () => ({
      ...state,
      reenterEmail: true,
      step: Step.ENTER_CONTACT_INFO
    }),
    SET_ACCOUNT_ERROR: ({ error }) => ({ ...state, accountError: error }),
    SET_ACCOUNTS: ({ accounts, selectedAccount }) => ({
      ...state,
      accountId: undefined,
      selectedAccount: selectedAccount,
      erpAccounts: accounts,
      step: Step.ENTER_CONTACT_INFO
    }),
    SET_CONTACT_INFO: ({ userInput }) => ({
      ...state,
      userInput,
      step: Step.CREATE_PASSWORD
    }),
    SET_ERP_NAME: ({ erpName }) => ({ ...state, erpName: erpName }),
    default: () => state
  });
