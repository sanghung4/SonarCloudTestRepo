import { WriteInFormKey } from 'constants/form';

export interface CreateWriteInOverlayProps {
  onDismiss: () => void;
  isVisible: boolean;
}

export type WriteInForm = Record<WriteInFormKey, string>;
