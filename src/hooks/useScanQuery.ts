import {
  ApolloError,
  LazyQueryHookOptions,
  LazyQueryResult,
  useLazyQuery,
} from '@apollo/client';
import { useNavigation } from '@react-navigation/native';
import {
  GetLocationDocument,
  GetLocationQuery,
  GetLocationQueryVariables,
} from 'api';
import { BarCodeEvent } from 'expo-barcode-scanner';
import { useOverlay } from 'providers/Overlay';
import { ErrorType } from 'constants/error';
import { getError } from 'utils/error';
import { useConfig } from './useConfig';
import { RouteNames } from 'constants/routes';

type ScanResult<Data, Variables> = LazyQueryResult<Data, Variables> & {
  hasPermission: boolean;
  scanning: boolean;
};

type ScanQuery = GetLocationQuery;

type ScanVariables = GetLocationQueryVariables;

export type ScanTuple<Query, Variables> = [
  (code: BarCodeEvent) => void,
  ScanResult<Query, Variables>
];

export enum ScanType {
  LOCATION = 0,
  PRODUCT,
}

export const useScanQuery = (
  type: ScanType,
  baseOptions: LazyQueryHookOptions<ScanQuery, ScanVariables> = {}
): ScanTuple<ScanQuery, ScanVariables> => {
  const { showAlert, toggleLoading, visibleOverlay } = useOverlay();
  const { navigate } = useNavigation();
  const [_, setConfig] = useConfig();

  const locationOptions: LazyQueryHookOptions = {
    ...baseOptions,
    fetchPolicy: 'network-only',
    onCompleted: (params: GetLocationQuery) => {
      toggleLoading(false);
      setConfig({ location: params.location });
      navigate(RouteNames.LOCATION_ITEMS);
    },
    onError: (error: ApolloError) => {
      showAlert(getError(ErrorType.LOCATION, error));
    },
  };

  const location = useLazyQuery(GetLocationDocument, locationOptions);
  const [scan, scanRes] = location;

  const onScan = (code: BarCodeEvent) => {
    scan({ variables: { id: code.data.toUpperCase() } });
  };

  return [
    onScan,
    {
      scanning: scanRes.loading || visibleOverlay === 'alert',
      ...scanRes,
    } as ScanResult<ScanQuery, ScanVariables>,
  ];
};
