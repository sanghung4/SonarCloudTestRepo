import { useQueryParams } from 'hooks/useSearchParam';
import { defaultRange } from 'Orders';

/**
 * Types
 */
export type OrdersParams = {
  searchBy: string;
  from: string | Date;
  to: string | Date;
  page: string;
  sortBy: string[];
};

/**
 * Const
 */
const paramConfig = { arrayKeys: ['sortBy'] };

/**
 * Hook
 */
export function useOrdersQueryParam(): [
  OrdersParams,
  (update: Partial<OrdersParams>) => void
] {
  // Main queryParam hook
  const [qP, setQP] = useQueryParams<OrdersParams>(paramConfig);

  // initial values
  const {
    searchBy = '',
    from = defaultRange.from,
    to = defaultRange.to,
    page = '1',
    sortBy = ['orderDate']
  } = qP;

  // Prearranged setter
  const setQueryParams = (update: Partial<OrdersParams>) => {
    setQP({ ...qP, ...update });
  };

  // output
  return [{ searchBy, from, to, page, sortBy }, setQueryParams];
}
