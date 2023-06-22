import { Box } from '@dialexa/reece-component-library';

import { CreditCard } from 'generated/graphql';
import {
  CCAmexIcon,
  CCDiscoverIcon,
  CCGenericIcon,
  CCMastercardIcon,
  CCVisaIcon
} from 'icons';

type Props = {
  creditCard: CreditCard;
  expired: boolean;
};

export default function CreditCardTypeIcon(props: Props) {
  return (
    <Box
      data-testId={`${props.creditCard.creditCardType}`}
      component={getCreditCardIcon(props.creditCard.creditCardType)}
      filter={props.expired ? 'grayscale(100%)' : undefined}
      width={38}
      height={24}
    />
  );
}

export function getCreditCardIcon(types: string) {
  switch (types) {
    case 'Amex':
      return CCAmexIcon;
    case 'Discover':
      return CCDiscoverIcon;
    case 'Mastercard':
      return CCMastercardIcon;
    case 'Visa':
      return CCVisaIcon;
    default:
      return CCGenericIcon;
  }
}
