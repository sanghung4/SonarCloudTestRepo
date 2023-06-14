import { getLocationStatus } from 'utils/locationUtils';
import { makeVar, useReactiveVar, ApolloError } from '@apollo/client';
import {
  GetLocationLazyQueryHookResult,
  GetLocationQuery,
  Location,
  useGetLocationLazyQuery,
} from 'api';

interface useLocationArgs {
  initialLocationId?: string;
  onCompleted?: (location: Location) => void;
  onError?: (error: ApolloError) => void;
}

interface useLocationResp {
  location: Location | undefined;
  loading: boolean;
  getLocation: (locationId: string) => void;
  refetchLocation: () => void;
  refetch: GetLocationLazyQueryHookResult['1']['refetch'];
  status: string;
}

const locationVar = makeVar<Location | undefined>(undefined);

export const useLocation = (args?: useLocationArgs): useLocationResp => {
  const loc = useReactiveVar(locationVar);

  const locationStatus = loc ? getLocationStatus(loc) : '';

  const [getLocation_internal, { loading, refetch }] = useGetLocationLazyQuery({
    fetchPolicy: 'network-only',
    onCompleted: ({ location: newLoc }: GetLocationQuery) => {
      locationVar(newLoc);
      args?.onCompleted && args?.onCompleted(newLoc);
    },
    onError: (e: ApolloError) => {
      locationVar(undefined);
      args?.onError && args?.onError(e);
    },
  });

  const getLocation = (locationId: string) => {
    getLocation_internal({ variables: { id: locationId } });
  };

  const refetchLocation = () => {
    getLocation_internal({ variables: { id: loc?.id || '' } });
  };

  return {
    location: loc,
    loading,
    getLocation,
    refetchLocation,
    refetch,
    status: locationStatus,
  };
};
