import dayjs from 'dayjs';
import { ApolloError, QueryHookOptions } from '@apollo/client';
import { GetWriteInsQuery, GetWriteInsQueryVariables, WriteIn } from 'api';
import { OverlayContextValues } from 'providers/Overlay/types';
import { ErrorType } from 'constants/error';
import { getError } from 'utils/error';
import { WriteInFilter } from './types';

export function getWriteInData(
  data: GetWriteInsQuery | null,
  filter: WriteInFilter
): WriteIn[] {
  if (!data) {
    return [];
  }

  const writeIns = data.writeIns.content;

  switch (filter) {
    case WriteInFilter.RESOLVED:
      return writeIns.filter((writeIn) => writeIn.resolved);
    case WriteInFilter.UNRESOLVED:
      return writeIns.filter((writeIn) => !writeIn.resolved);
    case WriteInFilter.ALL:
    default:
      return writeIns;
  }
}

export function getQueryOptions({
  showAlert,
  toggleLoading,
}: OverlayContextValues): QueryHookOptions<
  GetWriteInsQuery,
  GetWriteInsQueryVariables
> {
  return {
    variables: {
      options: {
        sort: {
          property: 'locationName',
          direction: 'asc',
        },
        page: 0,
        size: 999,
      },
    },
    fetchPolicy: 'cache-and-network',
    nextFetchPolicy: 'network-only',
    onCompleted: () => {
      toggleLoading(false);
    },
    onError: (error: ApolloError) => {
      showAlert(getError(ErrorType.WRITE_INS, error));
    },
  };
}

export function getWriteInTitle(writeIn: WriteIn): string {
  return [writeIn.locationId, '-', writeIn.quantity, writeIn.uom].join(' ');
}

export function getTimestamp(writeIn: WriteIn): string {
  return dayjs(writeIn.createdAt).format('hh:mm A');
}
