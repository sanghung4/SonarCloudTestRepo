import { ChangeEvent, Dispatch } from 'react';

import { CreditCardContextType } from 'CreditCard/util/types';
import { CardHolderInput, Maybe } from 'generated/graphql';

// Have to split this because it's impossible to trigger onChange event
//  from MUI radio group as this is a known MUI bug
export function handleSavedCardChange(setSelectedCreditCard: Dispatch<string>) {
  return (event: ChangeEvent<HTMLInputElement>) =>
    setSelectedCreditCard((event.target as HTMLInputElement).value);
}

export function handleSubmit(
  setShouldSaveCreditCard: Dispatch<boolean>,
  getCreditCardSetupUrl: CreditCardContextType['getCreditCardSetupUrl'],
  setIsCreditSelectionChanging?: Dispatch<boolean>,
  erpAccountId?: Maybe<string>
) {
  return (cardHolderInput: CardHolderInput, saveCard: boolean) => {
    setIsCreditSelectionChanging?.(false);
    setShouldSaveCreditCard(saveCard);
    getCreditCardSetupUrl?.({
      variables: { accountId: erpAccountId ?? '', cardHolderInput }
    });
  };
}
