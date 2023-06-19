import { CustomButtonProps } from './../../components/CustomButton/types';
import React from 'react';
import * as SVG from 'components/SVG';
import { SortByEnum } from 'screens/VarianceLocationList/types';

export interface AlertAction {
  title: string;
  onPress: () => void;
  type?: CustomButtonProps['type'];
  loading?: boolean;
  disabled?: boolean;
  testID?: string;
  key?: string;
}

export interface AlertOptions {
  fullScreen?: boolean;
  centered?: boolean;
}

export interface AlertPayload {
  svg?: keyof typeof SVG;
  title: React.ReactNode;
  description: React.ReactNode;
  error?: React.ReactNode;
  actions?: AlertAction[];
  options?: AlertOptions;
  onClose?: () => void;
}
export interface OverlayContextValues {
  alertPayload: AlertPayload;
  visibleOverlay: OverlayTypes;
  showAlert: (payload: AlertPayload) => void;
  hideAlert: () => void;
  toggleLoading: (state: boolean) => void;
  toggleSortBy: (state: boolean) => void;
  handleSortBy: (state: SortByEnum) => void;
  activeSortBy: SortByEnum;
}

export type OverlayTypes = 'alert' | 'loading' | 'sortBy' | '';
