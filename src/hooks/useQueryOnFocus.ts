import { useCallback } from 'react';
import { useFocusEffect } from '@react-navigation/native';
import {
  useQuery,
  QueryResult,
  QueryHookOptions,
  DocumentNode,
  TypedDocumentNode,
} from '@apollo/client';

export const useQueryOnFocus = <Data = any, Variables = Record<string, any>>(
  query: DocumentNode | TypedDocumentNode<Data, Variables>,
  options?: QueryHookOptions<Data, Variables>
): QueryResult<Data, Variables> => {
  const { refetch, ...resp } = useQuery(query, options);

  useFocusEffect(
    useCallback(() => {
      let fetched = false;

      if (!fetched) {
        refetch();
        fetched = true;
      }

      return () => {
        fetched = false;
      };
    }, [refetch])
  );

  return { ...resp, refetch };
};
