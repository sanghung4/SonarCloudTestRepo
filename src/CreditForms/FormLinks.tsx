import { useContext } from 'react';

import {
  Box,
  Grid,
  List,
  ListItem,
  ListItemIcon,
  ListItemText
} from '@dialexa/reece-component-library';

import { SavePdfIcon } from 'icons';
import { useDomainInfo } from 'hooks/useDomainInfo';
import { AuthContext } from 'AuthProvider';
import { ErpSystemEnum } from 'generated/graphql';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';

interface CreditFormURLType {
  url: string;
  title: string;
  authorizationRequired?: boolean;
}

const commmonFormURLS: CreditFormURLType[] = [
  {
    url: 'MORSCO-Credit-Application-All-Brands-January-2023.pdf',
    title: 'Customer Account Credit Application'
  },
  {
    url: 'MORSCO-Credit-Application-All-Brands-Spanish-January-2023.pdf',
    title: 'Solicitud de Crédito'
  },
  {
    url: 'INVOICE-AND-DELIVERY-TICKET-Terms-of-Sale_12-29-2014.pdf',
    title: 'Invoice & Delivery Ticket Terms of Sale'
  },
  {
    url: 'EFT-Customer-Authorization-Form-Morrison.pdf',
    title: 'Electronic Funds Transfer Authorization Form'
  },
  {
    url: 'Morsco_Tool_Rental_Agreement_Form_20211105.pdf',
    title: 'Equipment Rental Agreement',
    authorizationRequired: true
  }
];

const fortilineFormURLS: CreditFormURLType[] = [
  {
    url: 'MORSCO-Credit-Application-All-Brands-January-2023.pdf',
    title: 'Customer Account Credit Application'
  },
  {
    url: 'MORSCO-Credit-Application-All-Brands-Spanish-January-2023.pdf',
    title: 'Solicitud de Crédito'
  },
  {
    url: 'EFT-One-Time-Form-WW-V2.pdf',
    title: 'Electronic Funds Transfer Authorization Form'
  }
];

function FormLinks() {
  const { isWaterworks } = useDomainInfo();
  const { authState } = useContext(AuthContext);
  const { selectedAccounts } = useSelectedAccountsContext();

  const isMincron = selectedAccounts.erpSystemName === ErpSystemEnum.Mincron;
  const creditFormURLS: CreditFormURLType[] =
    isMincron || isWaterworks ? fortilineFormURLS : commmonFormURLS;

  return (
    <Grid container>
      <Grid item xs={12} md={4}>
        <List>
          {creditFormURLS
            .filter(
              (url) => !url.authorizationRequired || authState?.isAuthenticated
            )
            .map((item, i, arr) => (
              <ListItem
                key={item.title}
                component="a"
                href={`/files/credit-forms/${item.url}`}
                target="_blank"
                divider={i < arr.length - 1}
                disableGutters
              >
                <ListItemIcon>
                  <SavePdfIcon />
                </ListItemIcon>
                <ListItemText
                  primary={
                    <Box component="span" color="primary02.main">
                      {item.title}
                    </Box>
                  }
                />
              </ListItem>
            ))}
        </List>
      </Grid>
    </Grid>
  );
}
export default FormLinks;
