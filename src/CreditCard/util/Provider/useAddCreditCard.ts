import { Dispatch } from 'react';
import {
  AddCreditCardMutation,
  CreditCard,
  useAddCreditCardMutation
} from 'generated/graphql';

type Props = {
  setCreditCardData: Dispatch<CreditCard[] | undefined>;
  setUpdatingList: Dispatch<boolean>;
};

export function useAddCreditCard(props: Props) {
  const [addCreditCardMutation, { loading: addCreditCardLoading }] =
    useAddCreditCardMutation({
      refetchQueries: ['CreditCardList'],
      onCompleted: addComplete(props)
    });
  return { addCreditCardLoading, addCreditCardMutation };
}

export function addComplete(props: Props) {
  return (data: AddCreditCardMutation) => {
    props.setCreditCardData(data.addCreditCard.creditCardList.creditCard ?? []);
    props.setUpdatingList(false);
  };
}
