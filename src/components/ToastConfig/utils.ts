import { CustomToastProps } from './types';
import Toast, { ToastShowParams } from 'react-native-toast-message';

type ShowSuccessToastPayload = Omit<
  ToastShowParams & CustomToastProps,
  'props' | 'type'
>;
type ShowErrorToastPayload = Omit<
  ToastShowParams & CustomToastProps,
  'props' | 'type' | 'closeable'
>;

export const showSuccessToast = ({
  title,
  closeable,
  ...rest
}: ShowSuccessToastPayload) => {
  Toast.show({
    type: 'success',
    position: 'bottom',
    props: { title, closeable },
    ...rest,
  });
};

export const showErrorToast = ({ title, ...rest }: ShowErrorToastPayload) => {
  Toast.show({
    type: 'error',
    position: 'bottom',
    autoHide: false,
    props: { title, closeable: true },
    ...rest,
  });
};

export const hideToast = () => {
  Toast.hide();
};
