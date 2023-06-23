import { Maybe } from 'yup';

export type FileUploadProps = {
  accepts?: string;
  className?: string;
  displayAccepts?: string;
  disabled?: boolean;
  loading?: boolean;
  multiple?: boolean;
  onSelectingFiles: (file?: Maybe<FileList>) => void;
  'data-testid'?: string;
};
