import { Box, Skeleton, Typography } from '@dialexa/reece-component-library';

import { CustomAddressInput } from 'Checkout/util/types';
import { Branch, ContractDetails, ErpAccount } from 'generated/graphql';
import { every } from 'lodash-es';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import { useMemo } from 'react';

type Props = {
  location?: ContractDetails | Branch | ErpAccount | CustomAddressInput;
  boldName?: boolean;
  includePhone?: boolean;
};

export default function Location(props: Props) {
  const {
    selectedAccounts: { billToErpAccount }
  } = useSelectedAccountsContext();

  /*
   * Memo
   */
  const address = useMemo(() => {
    // Nothing
    if (!props.location || every(props.location, (val) => !val)) {
      return undefined;
    }

    // When location type is `ContractDetails`
    if ('jobName' in props.location) {
      return {
        name: props.location.jobName,
        street1: props.location.accountInformation?.shipToAddress?.address1,
        street2: props.location.accountInformation?.shipToAddress?.address2,
        city: props.location.accountInformation?.shipToAddress?.city,
        state: props.location.accountInformation?.shipToAddress?.state,
        zip: props.location.accountInformation?.shipToAddress?.zip,
        phoneNumber: billToErpAccount?.phoneNumber ?? ''
      };
    }

    if ('name' in props.location) {
      return {
        name: props.location.name,
        street1: props.location.address1,
        street2: props.location.address2,
        city: props.location.city,
        state: props.location.state,
        zip: props.location.zip,
        phoneNumber: props.location.phone
      };
    }

    if ('companyName' in props.location) {
      return {
        name: props.location.companyName,
        street1: props.location.street1,
        street2: props.location.street2,
        city: props.location.city,
        state: props.location.state,
        zip: props.location.zip,
        phoneNumber: props.location.phoneNumber
      };
    }
  }, [props.location, billToErpAccount]);

  /**
   * Render
   */
  if (!address) {
    return (
      <Box data-testid="checkout-pickup-location-address-loading">
        <Typography variant="body1">
          <Skeleton width={150} />
        </Typography>
        <Typography variant="body1">
          <Skeleton width={200} />
        </Typography>
        <Typography variant="body1">
          <Skeleton width={100} />
        </Typography>
        <Typography variant="body1">
          <Skeleton width={200} />
        </Typography>
      </Box>
    );
  }

  return (
    <Box data-testid="checkout-pickup-location-address">
      {!!address?.name && (
        <Typography variant="body1" fontWeight={props.boldName ? 700 : 500}>
          {address.name}
        </Typography>
      )}
      {!!address?.street1 && (
        <Typography variant="body1">{address.street1}</Typography>
      )}
      {!!address?.street2 && (
        <Typography variant="body1">{address.street2}</Typography>
      )}
      {!!address?.city && !!address.state && !!address.zip && (
        <Typography variant="body1">
          {`${address.city}, ${address.state} ${address.zip}`}
        </Typography>
      )}
      {!!props.includePhone && !!address?.phoneNumber && (
        <Typography variant="body1">{address.phoneNumber}</Typography>
      )}
    </Box>
  );
}
