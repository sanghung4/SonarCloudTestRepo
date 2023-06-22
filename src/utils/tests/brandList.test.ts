import { generateCompanyUrl } from 'utils/brandList';

describe('Utils - BrandList', () => {
  it('Expect `generateCompanyUrl` to return localhost', () => {
    const result = generateCompanyUrl('test', '3000', 'development', true);
    expect(result).toBe('http://test.localhost:3000');
  });
  it('Expect `generateCompanyUrl` to return dev', () => {
    const result = generateCompanyUrl('test', '80', 'development', true);
    expect(result).toBe('https://test.ecomm.reecedev.us');
  });
  it('Expect `generateCompanyUrl` to return test', () => {
    const result = generateCompanyUrl('test', '80', 'test', true);
    expect(result).toBe('https://test.test.ecomm.reecedev.us');
  });
  it('Expect `generateCompanyUrl` to return uat', () => {
    const result = generateCompanyUrl('test', '80', 'uat', true);
    expect(result).toBe('https://test.uat.ecomm.reecedev.us');
  });
  it('Expect `generateCompanyUrl` to return prod', () => {
    const result = generateCompanyUrl('test', '80', 'production', true);
    expect(result).toBe('https://test.reece.com');
  });
});
