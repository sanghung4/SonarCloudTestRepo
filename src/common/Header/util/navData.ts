import { AuthState } from '@okta/okta-auth-js';
import { TFunction } from 'i18next';

import { Permission } from 'common/PermissionRequired';
import { ApprovedUser, ErpSystemEnum, Maybe } from 'generated/graphql';

type Profile = {
  permissions: Array<string>;
  userId: string;
  isEmployee: boolean;
  isVerified: boolean;
};

export type NavProps = {
  activeFeatures?: string[];
  authState: Maybe<AuthState>;
  ecommUser?: Maybe<ApprovedUser>;
  erpSystemName?: Maybe<ErpSystemEnum>;
  isWaterworks: boolean;
  profile?: Profile;
  t: TFunction;
};

export type NavItem = {
  id: string;
  name: string;
  to: string;
  condition: boolean;
  comingSoon: boolean;
};

export type NavData = {
  max: NavItem[];
  profile: NavItem[];
  resources: NavItem[];
};

export function getNavData(props: NavProps) {
  /**
   * Props
   */
  const {
    activeFeatures,
    authState,
    ecommUser,
    erpSystemName,
    isWaterworks,
    profile,
    t
  } = props;

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
   * Values
   */
  const values: NavData = {
    max: [
      {
        id: 'contracts',
        name: t('common.contracts'),
        to: '/contracts',
        condition: (hasInvoiceOnly && isMincron) || isWaterworks,
        comingSoon: false
      },
      {
        id: 'orders',
        name: t('common.orders'),
        to: '/orders',
        condition: true,
        comingSoon: false
      },
      {
        id: 'previously-purchased-products',
        name: t('common.previouslyPurchasedProducts'),
        to: '/previously-purchased-products',
        condition: isAuthenticated && !isWWOrMincron,
        comingSoon: false
      },
      {
        id: 'invoices',
        name: t('common.invoices'),
        to: '/invoices',
        condition: !isAuthenticated || hasPermission(Permission.VIEW_INVOICE),
        comingSoon: false
      },
      {
        id: 'purchase-approvals',
        name: t('common.purchaseApprovals'),
        to: '/purchase-approvals',
        condition:
          (!isAuthenticated || hasPermission(Permission.APPROVE_CART)) &&
          !isWWOrMincron,
        comingSoon: false
      },
      {
        id: 'quotes',
        name: t('common.quotes'),
        to: '/quotes',
        condition: !isWWOrMincron,
        comingSoon: false
      },
      {
        id: 'lists',
        name: t('common.lists'),
        to: '/lists',
        condition: hasFeature('LISTS') && !isWWOrMincron,
        comingSoon: false
      },
      {
        id: 'features',
        name: t('common.toggleFeatures'),
        to: '/features',
        condition: hasPermission(Permission.TOGGLE_FEATURES),
        comingSoon: false
      }
    ],
    profile: [
      {
        id: 'customer-approval',
        name: t('common.customerApproval'),
        to: '/customer-approval',
        condition: hasPermission(Permission.APPROVE_ALL_USERS),
        comingSoon: false
      },
      {
        id: 'user-management',
        name: t('common.userManagement'),
        to: '/user-management',
        condition: hasPermission(Permission.MANAGE_ROLES),
        comingSoon: false
      },
      {
        id: 'account-information',
        name: t('common.accountInformation'),
        to: '/account',
        condition: !!profile?.permissions?.length,
        comingSoon: false
      },
      {
        id: 'payment-information',
        name: t('common.paymentInformation'),
        to: '/payment-information',
        condition:
          hasPermission(Permission.MANAGE_PAYMENT_METHODS) && !isMincron,
        comingSoon: false
      }
    ],
    resources: [
      {
        id: 'credit-forms',
        name: t('common.creditForms'),
        to: '/credit-forms',
        condition: true,
        comingSoon: false
      },
      {
        id: 'job-form',
        name: t('common.jobForm'),
        to: isWWOrMincron ? '/jobform' : '/job-form',
        condition: true,
        comingSoon: false
      },
      {
        id: 'about',
        name: t('common.about'),
        to: '/about',
        condition: true,
        comingSoon: false
      },
      {
        id: 'news',
        name: t('news.title'),
        to: '/news',
        condition: hasFeature('NEWS_BLOG'),
        comingSoon: false
      },
      {
        id: 'works-for-you',
        name: t('common.worksForYou'),
        to: '/works-for-you',
        condition: true,
        comingSoon: false
      }
    ]
  };
  return values;
}
