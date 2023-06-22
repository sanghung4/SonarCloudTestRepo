import { Feature } from 'generated/graphql';

export type CompanyDomain = {
  link: string;
  list: string;
  sub: string;
  engine: string;
  sf: string;
};

export const waterworksCompany: CompanyDomain = {
  link: 'Fortiline',
  list: 'Fortiline Waterworks',
  sub: 'fortiline',
  engine: 'waterworks',
  sf: 'Fortiline'
};

export const expressionsHomeGallery: CompanyDomain = {
  link: 'Expressions Home Gallery',
  list: 'Expressions Home Gallery',
  sub: 'expressionshomegallery',
  engine: 'bath_kitchen',
  sf: 'Expressions Home Gallery'
};

export const mainCompanyList: CompanyDomain[] = [
  {
    link: 'Morrison Supply Company',
    list: 'Morrison Supply',
    sub: 'morrisonsupply',
    engine: 'plumbing_hvac',
    sf: 'Morrison Supply'
  },
  {
    link: 'DeVore & Johnson',
    list: 'DeVore & Johnson',
    sub: 'devoreandjohnson',
    engine: 'plumbing_hvac',
    sf: 'DeVore And Johnson'
  },
  {
    link: 'Murray Supply',
    list: 'Murray Supply Company',
    sub: 'murraysupply',
    engine: 'plumbing_hvac',
    sf: 'Murray Supply'
  },
  {
    link: 'Morsco HVAC',
    list: 'Morsco HVAC Supply',
    sub: 'morscohvacsupply',
    engine: 'plumbing_hvac',
    sf: 'Morsco Hvac'
  },
  {
    link: 'Farnsworth Wholesale',
    list: 'Farnsworth Wholesale Company',
    sub: 'fwcaz',
    engine: 'plumbing_hvac',
    sf: 'Farnsworth'
  },
  expressionsHomeGallery,
  {
    link: 'Wholesale Specialties',
    list: 'Wholesale Specialties',
    sub: 'wholesalespecialties',
    engine: 'plumbing_hvac',
    sf: 'Wholesale Supply'
  },
  waterworksCompany,
  {
    link: 'L&B Pipe',
    list: 'L&B Pipe',
    sub: 'landbpipe',
    engine: 'plumbing_hvac',
    sf: 'L&B Pipe'
  },
  {
    link: 'Irvine Pipe Supply',
    list: 'Irvine Pipe Supply',
    sub: 'irvinepipe',
    engine: 'plumbing_hvac',
    sf: 'Irvine Pipe Supply'
  }
];

export const defaultCompany: CompanyDomain = {
  link: 'Reece',
  list: 'Reece',
  sub: 'reece',
  engine: 'plumbing_hvac',
  sf: ''
};

export function isWaterworksEnabled(features?: Feature[]) {
  return features?.find((f) => f.name === 'WATERWORKS')?.isEnabled ?? false;
}

export function isExpressionsHomeGalleryEnabled(features?: Feature[]) {
  return (
    features?.find((f) => f.name === 'EXPRESSIONSHOMEGALLERY')?.isEnabled ??
    false
  );
}

export function isCommonSubdomain(subdomain: string) {
  return (
    subdomain === 'localhost' || subdomain === 'reece' || subdomain === 'app'
  );
}
