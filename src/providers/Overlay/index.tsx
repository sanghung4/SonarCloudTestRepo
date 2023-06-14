import React, { FC, createContext, useState, useContext } from 'react';
import { SortByEnum } from 'screens/VarianceLocationList/types';
import {
  OverlayContextValues,
  AlertPayload,
  AlertAction,
  OverlayTypes,
} from './types';

const initialAlertAction: AlertAction = {
  title: 'Ok',
  onPress: (): void => {},
};

const initialAlertPayload: AlertPayload = {
  title: '',
  description: '',
  actions: [],
  options: {},
};

const initialContext: OverlayContextValues = {
  alertPayload: initialAlertPayload,
  visibleOverlay: '',
  showAlert: () => {},
  hideAlert: () => {},
  toggleLoading: () => {},
  toggleSortBy: () => {},
  handleSortBy: () => {},
  activeSortBy: SortByEnum.ASCENDING,
};

const OverlayContext = createContext(initialContext);

const OverlayProvider: FC = ({ children }) => {
  const [alertPayload, setAlertPayload] = useState(initialAlertPayload);
  const [visibleOverlay, setVisibleOverlay] = useState<OverlayTypes>('');
  const [activeSortBy, setActiveSortBy] = useState<SortByEnum>(
    SortByEnum.ASCENDING
  );

  const showAlert = (payload: AlertPayload) => {
    setAlertPayload({
      ...initialAlertPayload,
      actions: [initialAlertAction],
      ...payload,
    });
    setVisibleOverlay('alert');
  };

  const hideAlert = () => {
    setAlertPayload(initialAlertPayload);
    setVisibleOverlay('');
  };

  const toggleLoading = (state: boolean) => {
    setVisibleOverlay(state ? 'loading' : '');
  };

  const handleSortBy = (state: SortByEnum) => {
    setActiveSortBy(state);
  };

  const toggleSortBy = (state: boolean) => {
    setVisibleOverlay(state ? 'sortBy' : '');
  };

  return (
    <OverlayContext.Provider
      value={{
        alertPayload,
        visibleOverlay,
        showAlert,
        hideAlert,
        toggleLoading,
        toggleSortBy,
        handleSortBy,
        activeSortBy,
      }}
    >
      {children}
    </OverlayContext.Provider>
  );
};

export default OverlayProvider;

export const useOverlay = (): OverlayContextValues =>
  useContext(OverlayContext);
