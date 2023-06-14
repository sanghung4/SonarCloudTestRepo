import { CustomIconNames } from 'components/CustomIcon';

export interface CustomToastProps {
  title?: string;
  closeable?: boolean;
  testID?: string;
}

export type CustomToastStyles = {
  [key: string]: {
    icon:
      | CustomIconNames.Warning
      | CustomIconNames.Info
      | CustomIconNames.CircleCheck;
    color: string;
    background: string;
    paddingHorizontal: boolean;
  };
};
