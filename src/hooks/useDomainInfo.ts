import { find } from 'lodash-es';
import { getEnvironment } from 'utils/configuration';
import {
  defaultCompany,
  isCommonSubdomain,
  mainCompanyList,
  waterworksCompany
} from 'hooks/utils/useDomainInfo';

export function useDomainInfo() {
  /**
   * Variables
   */
  const { host, hostname, port } = window.location;
  const companyList = [...mainCompanyList];
  let engine = '';
  let rootDomain = host;
  let subdomain = hostname.split('.')[0];
  let companyNameList: string;
  let companyNameLink: string;
  let salesforceLink = '';
  // root domain
  if (!isCommonSubdomain(subdomain)) {
    rootDomain = host.split('.').slice(1).join('.');
    if (!/localhost/.test(rootDomain) && !getEnvironment().isProd) {
      rootDomain = `app.${rootDomain}`;
    }
  }

  // remaining company data
  const companyData = find(companyList, ['sub', subdomain]);
  if (!companyData) {
    companyNameLink = defaultCompany.link;
    companyNameList = defaultCompany.list;
    subdomain = defaultCompany.sub;
    engine = defaultCompany.engine;
    salesforceLink = defaultCompany.sf;
  } else {
    companyNameLink = companyData.link;
    companyNameList = companyData.list;
    subdomain = companyData.sub;
    engine = companyData.engine;
    salesforceLink = companyData.sf;
  }

  // waterworks
  const isWaterworks = subdomain === waterworksCompany.sub;

  /**
   * Output
   */
  return {
    companyList,
    companyNameLink,
    companyNameList, // Deprecated in favor of "brand"
    brand: companyNameList,
    engine,
    isWaterworks,
    port,
    rootDomain,
    subdomain,
    salesforceLink
  };
}
