import { Dispatch, Fragment, ReactNode, useContext, useState } from 'react';

import {
  Box,
  Collapse,
  Link,
  List,
  ListItem
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import { AuthContext } from 'AuthProvider';
import { getNavData, NavData } from 'common/Header/util';
import { Configuration } from 'utils/configuration';
import { ArrowDropDownIcon, ArrowDropUpIcon } from 'icons';
import { useDomainInfo } from 'hooks/useDomainInfo';
import { generateCompanyUrl } from 'utils/brandList';
import {
  CompanyDomain,
  expressionsHomeGallery,
  isWaterworksEnabled,
  isExpressionsHomeGalleryEnabled,
  waterworksCompany
} from 'hooks/utils/useDomainInfo';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';

type Props = {
  children: ReactNode;
  item: keyof NavData;
  open: boolean;
  setOpen: Dispatch<boolean>;
  urlHandler: (path: string, auth?: boolean | undefined) => () => void;
};

export function MobileDrawerCollapseList(props: Props) {
  /**
   * Custom hooks
   */
  const { t } = useTranslation();
  const { isWaterworks } = useDomainInfo();

  /**
   * Context
   */
  const { activeFeatures, authState, ecommUser, profile } =
    useContext(AuthContext);
  const {
    selectedAccounts: { erpSystemName }
  } = useSelectedAccountsContext();

  /**
   * Data
   */
  const list = getNavData({
    activeFeatures,
    authState,
    ecommUser,
    erpSystemName,
    isWaterworks,
    profile,
    t
  })[props.item];
  /**
   * Render
   */
  return (
    <>
      <ListItem
        data-testid={`${props.item}-dropdown-button`}
        button
        onClick={() => props.setOpen(!props.open)}
      >
        {props.children}
        <Box
          component={props.open ? ArrowDropUpIcon : ArrowDropDownIcon}
          color="primary.main"
        />
      </ListItem>
      <Collapse in={props.open}>
        <List sx={{ ml: 4.5 }}>
          {list.map((item, index) => (
            <Fragment key={`mobile-nav-${props.item}-${index}`}>
              {item.condition && (
                <ListItem
                  button
                  onClick={props.urlHandler(item.to)}
                  data-testid={`mobile-nav-${item.id}`}
                >
                  {item.name}
                  {item.comingSoon && ` - ${t('common.comingSoon')}`}
                </ListItem>
              )}
            </Fragment>
          ))}
        </List>
      </Collapse>
    </>
  );
}

export function MobileDrawerCollapseCompanyList() {
  /**
   * Hooks
   */
  const [open, setOpen] = useState(false);
  const { companyList, port } = useDomainInfo();
  const { t } = useTranslation();

  /**
   * Context
   */
  const { features } = useContext(AuthContext);

  /**
   * Logic
   */
  let filteredCompanyList: CompanyDomain[] = [];
  if (!isWaterworksEnabled(features)) {
    filteredCompanyList = companyList.filter(
      (c) => c.sub !== waterworksCompany.sub
    );
  } else {
    filteredCompanyList = companyList;
  }

  if (!isExpressionsHomeGalleryEnabled(features)) {
    filteredCompanyList = companyList.filter(
      (c) => c.sub !== expressionsHomeGallery.sub
    );
  }

  /**
   * Render
   */
  return (
    <>
      <ListItem
        data-testid="reece-companies-button"
        button
        onClick={() => setOpen(!open)}
      >
        {t('common.reeceCompanies')}
        <Box
          component={open ? ArrowDropUpIcon : ArrowDropDownIcon}
          color="primary.main"
        />
      </ListItem>
      <Collapse in={open}>
        <List sx={{ ml: 4.5 }}>
          {filteredCompanyList.map((company, i) => (
            <ListItem
              button
              key={i}
              data-testid={`${company.link
                .toLowerCase()
                .replaceAll(' ', '-')}-button`}
            >
              <Link
                key={`mobile-nav-compoany-${i}`}
                href={generateCompanyUrl(
                  company.sub,
                  port,
                  Configuration.environment,
                  isWaterworksEnabled(features) ?? false
                )}
                underline="none"
                color="secondary02.main"
              >
                {company.link}
              </Link>
            </ListItem>
          ))}
        </List>
      </Collapse>
    </>
  );
}
