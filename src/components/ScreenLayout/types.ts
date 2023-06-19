import { CustomButtonProps } from './../CustomButton/types';
import {
  ViewProps,
  ScrollViewProps,
  KeyboardAvoidingViewProps,
} from 'react-native';
import { BannerProps } from 'components/Banner';
import { DropDownBoxProps } from 'components/DropDownBox';
import { MessageProps } from 'components/Message/types';
import { TabFiltersProps } from 'components/TabFilters';

export type PageAction = CustomButtonProps & { hide?: boolean };

export interface ScreenLayoutProps extends KeyboardAvoidingViewProps {
  pageAction?: PageAction;
  loading?: boolean;
}

interface ScreenMessage extends MessageProps {
  open: boolean;
}

interface ContentBase {
  padding?: boolean;
  paddingVertical?: boolean;
  paddingHorizontal?: boolean;
  banner?: BannerProps;
  dropdown?: DropDownBoxProps;
  messages?: ScreenMessage[];
  tabFilters?: TabFiltersProps;
}

export interface ScrollContentProps extends ContentBase, ScrollViewProps {}

export interface StaticContentProps extends ContentBase, ViewProps {
  centered?: boolean;
}
