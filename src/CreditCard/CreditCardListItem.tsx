import {
  Box,
  Typography,
  TypographyProps
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import CreditCardTypeIcon from 'CreditCard/util/CreditCardTypeIcon';
import { CreditCard } from 'generated/graphql';
import { formatDate } from 'utils/dates';

type Props = {
  creditCard: CreditCard;
  expired: boolean;
  hideType?: boolean;
  hideDate?: boolean;
  hideExpired?: boolean;
  noEmphasis?: boolean;
  TypographyProps?: TypographyProps;
};

export default function CreditCardListItem(props: Props) {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();

  /**
   * Render
   */
  return (
    <Typography
      variant="body1"
      color="textSecondary"
      noWrap
      display="flex"
      alignItems="center"
      flexWrap="nowrap"
      {...props.TypographyProps}
    >
      {!props.hideType && (
        <CreditCardTypeIcon
          expired={props.expired}
          creditCard={props.creditCard}
        />
      )}
      <Box
        component="span"
        fontWeight={props.noEmphasis ? 400 : 700}
        ml={props.hideType ? 0 : 1}
        mr={props.hideDate ? 0 : 1}
      >
        {'**** '}
        {props.creditCard.creditCardNumber}
      </Box>{' '}
      {!props.hideDate &&
        formatDate(props.creditCard.expirationDate.date, 'MM/yy')}
      {!props.hideExpired && !!props.expired && (
        <Box ml={1} fontWeight={700} component="span" color="error.main">
          {t('creditCard.expired')}
        </Box>
      )}
    </Typography>
  );
}
