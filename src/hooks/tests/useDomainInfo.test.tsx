import { AuthContext } from 'AuthProvider';
import { mockAuthContext } from 'AuthProvider.mocks';
import { render } from 'test-utils/TestWrapper';
import {
  CompanyDomain,
  defaultCompany,
  isCommonSubdomain,
  isExpressionsHomeGalleryEnabled,
  isWaterworksEnabled,
  waterworksCompany
} from 'hooks/utils/useDomainInfo';
import {
  expressionsHomeGalleryFeature,
  mockDomains,
  mockUseDomainInfoReturn,
  waterworksFeature
} from 'hooks/tests/mocks/useDomainInfo.mocks';
import { useDomainInfo } from 'hooks/useDomainInfo';

/**
 * Mock values
 */
const mocks = {
  auth: { ...mockAuthContext }
};

/**
 * Setup
 */
function setup(m: typeof mocks) {
  let result: ReturnType<typeof useDomainInfo> = {
    ...mockUseDomainInfoReturn
  };
  function MockComponent() {
    Object.assign(result, useDomainInfo());
    return null;
  }
  render(
    <AuthContext.Provider value={m.auth}>
      <MockComponent />
    </AuthContext.Provider>
  );
  return result;
}
function findCompany(l: CompanyDomain[], t: CompanyDomain) {
  return l.some(
    (c) =>
      c.engine === t.engine &&
      c.link === t.link &&
      c.list === t.list &&
      c.sub === t.sub
  );
}

/**
 * Test
 */
describe('util - useDomainInfo', () => {
  beforeEach(() => {
    Reflect.deleteProperty(global.window, 'location');
  });
  afterEach(() => {
    mocks.auth = { ...mockAuthContext };
  });

  it('expect to match data as reece local', () => {
    const { local } = mockDomains.reece;
    window.location = { ...local };
    const result = setup(mocks);

    expect(result.companyNameLink).toEqual(defaultCompany.link);
    expect(result.companyNameList).toEqual(defaultCompany.list);
    expect(result.rootDomain).toEqual('localhost:3000');
    expect(result.subdomain).toEqual(defaultCompany.sub);
    expect(result.port).toEqual(local.port);
  });

  it('expect to match data as reece dev', () => {
    const { dev } = mockDomains.reece;
    window.location = { ...dev };
    const result = setup(mocks);

    expect(result.companyNameLink).toEqual(defaultCompany.link);
    expect(result.companyNameList).toEqual(defaultCompany.list);
    expect(result.rootDomain).toEqual('app.ecomm.reecedev.us');
    expect(result.subdomain).toEqual(defaultCompany.sub);
    expect(result.port).toEqual(dev.port);
  });

  it('expect to match data as fortiline local', () => {
    const { local } = mockDomains.fortiline;
    window.location = { ...local };
    mocks.auth.features = [{ ...waterworksFeature, isEnabled: true }];
    const result = setup(mocks);

    expect(result.companyNameLink).toEqual(waterworksCompany.link);
    expect(result.companyNameList).toEqual(waterworksCompany.list);
    expect(result.rootDomain).toEqual('localhost:3000');
    expect(result.subdomain).toEqual(waterworksCompany.sub);
    expect(result.port).toEqual(local.port);
  });

  it('expect to match data as fortiline dev', () => {
    const { dev } = mockDomains.fortiline;
    window.location = { ...dev };
    mocks.auth.features = [{ ...waterworksFeature, isEnabled: true }];
    const result = setup(mocks);

    expect(result.companyNameLink).toEqual(waterworksCompany.link);
    expect(result.companyNameList).toEqual(waterworksCompany.list);
    expect(result.rootDomain).toEqual('app.ecomm.reecedev.us');
    expect(result.subdomain).toEqual(waterworksCompany.sub);
    expect(result.port).toEqual(dev.port);
  });

  it('expect to have waterworks', () => {
    window.location = { ...mockDomains.reece.local };
    mocks.auth.features = [{ ...waterworksFeature, isEnabled: true }];
    const result = setup(mocks).companyList;

    const findWaterworks = findCompany(result, waterworksCompany);
    expect(findWaterworks).toBeTruthy();
  });

  it('expect isWaterworksEnabled to return truthy', () => {
    const features = [{ ...waterworksFeature, isEnabled: true }];
    const result = isWaterworksEnabled(features);

    expect(result).toBeTruthy();
  });

  it('expect isWaterworksEnabled to return false', () => {
    const features = [{ ...waterworksFeature, isEnabled: false }];
    const result = isWaterworksEnabled(features);

    expect(result).toBeFalsy();
  });

  it('expect isExpressionsHomeGalleryEnabled to return truthy', () => {
    const features = [{ ...expressionsHomeGalleryFeature, isEnabled: true }];
    const result = isExpressionsHomeGalleryEnabled(features);

    expect(result).toBeTruthy();
  });

  it('expect isExpressionsHomeGalleryEnabled to return false', () => {
    const features = [{ ...expressionsHomeGalleryFeature, isEnabled: false }];
    const result = isExpressionsHomeGalleryEnabled(features);

    expect(result).toBeFalsy();
  });

  it('expect isCommonDomain to return true if localhost', () => {
    const subdomain = 'localhost';
    const isCommon = isCommonSubdomain(subdomain);

    expect(isCommon).toBe(true);
  });

  it('expect isCommonDomain to return true if reece', () => {
    const subdomain = 'reece';
    const isCommon = isCommonSubdomain(subdomain);

    expect(isCommon).toBe(true);
  });

  it('expect isCommonDomain to return true if app', () => {
    const subdomain = 'app';
    const isCommon = isCommonSubdomain(subdomain);

    expect(isCommon).toBe(true);
  });

  it('expect isCommonDomain to return false if not common subdomain', () => {
    const subdomain = 'fortiline';
    const isCommon = isCommonSubdomain(subdomain);

    expect(isCommon).toBe(false);
  });
});
