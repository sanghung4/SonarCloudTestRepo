import { Feature } from 'generated/graphql';
import { useDomainInfo } from 'hooks/useDomainInfo';

export const defaultWindowLocation: Location = {
  ancestorOrigins: {
    contains: jest.fn(),
    item: jest.fn(),
    length: 0,
    [Symbol.iterator]: jest.fn()
  },
  hash: '',
  href: '',
  origin: '',
  host: '',
  hostname: '',
  pathname: '',
  port: '',
  protocol: '',
  search: '',
  assign: jest.fn(),
  reload: jest.fn(),
  replace: jest.fn()
};
export const mockDomains = {
  reece: {
    local: {
      ...defaultWindowLocation,
      host: 'localhost:3000',
      hostname: 'localhost',
      port: '3000'
    },
    dev: {
      ...defaultWindowLocation,
      host: 'app.ecomm.reecedev.us',
      hostname: 'app.ecomm.reecedev.us',
      port: ''
    }
  },
  fortiline: {
    local: {
      ...defaultWindowLocation,
      host: 'fortiline.localhost:3000',
      hostname: 'fortiline.localhost',
      port: '3000'
    },
    dev: {
      ...defaultWindowLocation,
      host: 'fortiline.ecomm.reecedev.us',
      hostname: 'fortiline.ecomm.reecedev.us',
      port: ''
    }
  }
};

export const mockUseDomainInfoReturn: ReturnType<typeof useDomainInfo> = {
  companyList: [],
  companyNameLink: '',
  companyNameList: '',
  brand: '',
  engine: '',
  isWaterworks: false,
  port: '',
  rootDomain: '',
  subdomain: '',
  salesforceLink: ''
};

export const waterworksFeature: Feature = {
  id: '',
  name: 'WATERWORKS',
  isEnabled: false
};

export const expressionsHomeGalleryFeature: Feature = {
  id: '',
  name: 'EXPRESSIONSHOMEGALLERY',
  isEnabled: false
};
