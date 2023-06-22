import { ReactComponent as Devore } from 'images/brands/devore.svg';
import { ReactComponent as Expressions } from 'images/brands/expressions.svg';
import { ReactComponent as Farnsworth } from 'images/brands/farnsworth.svg';
import { ReactComponent as Fortiline } from 'images/brands/fortiline.svg';
import { ReactComponent as Morrison } from 'images/brands/morrison.svg';
import { ReactComponent as MorscoHVAC } from 'images/brands/morscoHvac.svg';
import { ReactComponent as Murray } from 'images/brands/murray.svg';
import { ReactComponent as Wholesale } from 'images/brands/wholesale.svg';
import landbpipe from 'images/brands/lbpipe.svg';
import irvinePipe from 'images/brands/irvine-pipe-supply.svg';
import { states } from 'utils/states';
import { Environment } from 'utils/configuration';

const { AL, AZ, CA, CO, FL, GA, KS, KY, NV, NC, NM, OK, SC, TN, TX, VA } =
  states;

export interface Brand {
  name: string;
  states: string[];
  sub: string;
  Logo: React.FunctionComponent;
  canUseLogo?: boolean;
  svgPath?: string;
}

export function generateCompanyUrl(
  sub: string,
  port: string,
  env: Environment,
  waterworksEnabled: boolean
) {
  // Local
  if (port === '3000') {
    return `http://${sub}.localhost:3000`;
  }
  // Dev
  if (env === 'development') {
    return `https://${sub}.ecomm.reecedev.us`;
  }
  // Test
  if (env === 'test') {
    return `https://${sub}.test.ecomm.reecedev.us`;
  }
  // UAT
  if (env === 'uat') {
    return `https://${sub}.uat.ecomm.reecedev.us`;
  }
  // Sandbox
  if (env === 'production-backup') {
    return `https://${sub}.production.ecomm.reecedev.us`;
  }
  // Prod
  return `https://${sub}.reece.com`;
}

const brands: Brand[] = [
  {
    name: 'Morrison Supply Company',
    states: [KS, NM, OK, TX],
    sub: 'morrisonsupply',
    Logo: Morrison
  },
  {
    name: 'Morsco HVAC Supply',
    states: [AZ, OK, SC, TX],
    sub: 'morscohvacsupply',
    Logo: MorscoHVAC
  },
  {
    name: 'Expressions Home Gallery',
    states: [AZ, CA, OK, TX],
    sub: 'expressionshomegallery',
    Logo: Expressions
  },
  {
    name: 'Murray Supply Company',
    states: [NC, SC],
    sub: 'murraysupply',
    Logo: Murray
  },
  {
    name: 'Fortiline Waterworks',
    states: [AL, AZ, FL, GA, KS, KY, NC, OK, SC, TN, TX, VA],
    sub: 'fortiline',
    Logo: Fortiline
  },
  {
    name: 'DeVore & Johnson',
    states: [GA],
    sub: 'devoreandjohnson',
    Logo: Devore
  },
  {
    name: 'Wholesale Specialties',
    states: [CO],
    sub: 'wholesalespecialties',
    Logo: Wholesale
  },
  {
    name: 'Farnsworth Wholesale Companyy',
    states: [AZ, NV],
    sub: 'fwcaz',
    Logo: Farnsworth
  },
  {
    name: 'L&B Pipe',
    states: [CA],
    sub: 'landbpipe',
    Logo: Farnsworth,
    canUseLogo: false,
    svgPath: landbpipe
  },
  {
    name: 'Irvine Pipe Supply',
    states: [CA],
    sub: 'irvinepipe',
    Logo: Farnsworth,
    canUseLogo: false,
    svgPath: irvinePipe
  }
];

export default brands;
