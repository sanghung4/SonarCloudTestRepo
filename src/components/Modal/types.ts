export interface ModalProps {
  open: boolean;
  children: React.ReactNode;
  closeText?: string;
  confirmText?: string;
  title?: string;
  confirmDisabled?: boolean;
  onConfirm: () => void;
  onClose: () => void;
}
