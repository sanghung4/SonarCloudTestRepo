import { useContext } from 'react';

import { DataMap } from '@reece/global-types';
import { useTranslation } from 'react-i18next';

import { AuthContext } from 'AuthProvider';
import { ErpSystemEnum } from 'generated/graphql';
import { useDomainInfo } from 'hooks/useDomainInfo';
import { Permission } from 'common/PermissionRequired';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';

export type FooterItem = {
  id: string;
  name: string;
  to: string;
  condition: boolean;
};

export default function useFooterData() {
  /**
   * Custom hooks
   */

  const { isWaterworks } = useDomainInfo();
  const { t } = useTranslation();

  /**
   * Context
   */
  const { activeFeatures, authState, ecommUser, profile } =
    useContext(AuthContext);
  const {
    selectedAccounts: { erpSystemName }
  } = useSelectedAccountsContext();

  /**
   * Conditions
   */
  const hasFeature = (find: string) => !!activeFeatures?.includes(find);
  const hasInvoiceOnly = ecommUser?.role?.name !== t('roles.invoiceOnly');
  const hasPermission = (find: string) => !!profile?.permissions.includes(find);
  const isAuthenticated = !!authState?.isAuthenticated;
  const isMincron = erpSystemName === ErpSystemEnum.Mincron;
  const isWWOrMincron = isWaterworks || isMincron;

  /**
   * Data
   */
  const footerData: DataMap<FooterItem[]> = {
    max: [
      {
        id: 'contracts',
        name: 'common.contracts',
        to: '/contracts',
        condition: (hasInvoiceOnly && isMincron) || isWaterworks
      },
      {
        id: 'lists',
        name: 'common.lists',
        to: '/lists',
        condition: hasFeature('LISTS') && !isWWOrMincron
      },
      {
        id: 'quotes',
        name: 'common.quotes',
        to: '/quotes',
        condition: !isWWOrMincron
      },
      {
        id: 'purchase-approvals',
        name: 'common.purchaseApprovals',
        to: '/purchase-approvals',
        condition:
          (!isAuthenticated || hasPermission(Permission.APPROVE_CART)) &&
          !isWWOrMincron
      },
      {
        id: 'orders',
        name: 'common.orders',
        to: '/orders',
        condition: true
      },
      {
        id: 'invoices',
        name: 'common.invoices',
        to: '/invoices',
        condition: !isAuthenticated || hasPermission(Permission.VIEW_INVOICE)
      },
      {
        id: 'previously-purchased-products',
        name: 'common.previouslyPurchasedProducts',
        to: '/previously-purchased-products',
        condition: isAuthenticated && !isWWOrMincron
      }
    ],
    'common.resources': [
      {
        id: 'myaccount',
        name: 'common.myAccount',
        to: '/account',
        condition: true
      },
      {
        id: 'help',
        name: 'support.help',
        to: '/support',
        condition: true
      },
      {
        id: 'credit_forms',
        name: 'common.creditForms',
        to: '/credit-forms',
        condition: true
      },
      {
        id: 'jobform',
        name: 'common.jobForm',
        to: isWWOrMincron ? '/jobform' : '/job-form',
        condition: true
      },
      {
        id: 'storefinder',
        name: 'common.storeFinder',
        to: '/location-search',
        condition: true
      }
    ],
    'aboutUs.title': [
      {
        id: 'aboutreece',
        name: 'aboutUs.aboutReece',
        to: '/about',
        condition: true
      },
      {
        id: 'worksforyou',
        name: 'common.worksForYou',
        to: '/works-for-you',
        condition: true
      },
      {
        id: 'carrers',
        name: 'aboutUs.careers',
        to: 'https://careers.reece.com/us',
        condition: true
      },
      {
        id: 'reecegroup',
        name: 'aboutUs.reeceGroup',
        to: 'https://group.reece.com/us',
        condition: true
      },
      {
        id: 'company',
        name: 'aboutUs.company',
        to: '/company',
        condition: true
      }
    ]
  };

  /**
   * Output
   */
  return footerData;
}
