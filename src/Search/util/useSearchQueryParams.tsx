import { useQueryParams } from 'hooks/useSearchParam';

export type SearchParams = {
  criteria?: string;
  categories?: string[];
  filters?: string[]; // delimited pairs as type|value
  page?: string;
};

export default function useSearchQueryParams() {
  return useQueryParams<SearchParams>({
    arrayKeys: ['categories', 'filters']
  });
}
