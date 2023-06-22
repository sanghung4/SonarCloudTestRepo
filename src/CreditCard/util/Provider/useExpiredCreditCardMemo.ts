import { Dispatch, useMemo } from 'react';

import { add, isBefore } from 'date-fns';

import { ExpiredCreditCardsType } from 'CreditCard/util/types';
import { CreditCard } from 'generated/graphql';

type Props = {
  creditCardData?: CreditCard[];
  setUpdatingList: Dispatch<boolean>;
};

export default function useExpiredCreditCardMemo(props: Props) {
  function expiredCardReducer(res: ExpiredCreditCardsType, card: CreditCard) {
    const expDate = add(new Date(card.expirationDate.date), { months: 1 });
    res[card.elementPaymentAccountId] = !isBefore(new Date(), expDate);
    return res;
  }
  function expiredCreditCardsMemo() {
    if (props.creditCardData?.length) {
      props.setUpdatingList(true);
      const expiredCards = props.creditCardData.reduce(expiredCardReducer, {});
      props.setUpdatingList(false);
      return expiredCards;
    }
    return {};
  }

  // eslint-disable-next-line react-hooks/exhaustive-deps
  return useMemo(expiredCreditCardsMemo, [props.creditCardData]);
}
