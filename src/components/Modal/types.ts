import { WrapperProps } from '@reece/global-types';

export type ModalProps = WrapperProps & {
  className?: string;
  'data-testid'?: string;
  disableClose?: boolean;
  onClose?: () => void;
  open: boolean;
  title?: string;
};
