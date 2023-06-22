import { Maybe } from 'graphql/jsutils/Maybe';
import { useMemo } from 'react';

export default function useSavings(
  cmp: Maybe<number> | undefined,
  sellPrice: number | undefined
) {
  return useMemo(() => {
    const price = (cmp || 0) - (sellPrice || 0);
    const isSavings = price >= 0.01;
    const percentage = ((price / (cmp || 0)) * 100).toFixed(0);
    return { price, percentage, isSavings };
  }, [cmp, sellPrice]);
}
