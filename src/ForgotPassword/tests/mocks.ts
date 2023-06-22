import { AuthParam } from 'ForgotPassword';
import useForgetPasswordData from 'ForgotPassword/util/useForgetPasswordData';
import { UseForgetPasswordEffectProps } from 'ForgotPassword/util/useForgetPasswordEffect';

export const mockAuthParam: AuthParam = {};

export const mockUseData: ReturnType<typeof useForgetPasswordData> = {
  email: '',
  isInviteSentQuery: jest.fn(),
  requestSent: false,
  setEmail: jest.fn(),
  setRequestSent: jest.fn(),
  setTransaction: jest.fn(),
  transaction: undefined
};

export const mockUseForgetPasswordEffectProps: UseForgetPasswordEffectProps = {
  setEmail: jest.fn(),
  setTransaction: jest.fn()
};
