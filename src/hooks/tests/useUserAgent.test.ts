import useUserAgent from 'hooks/useUserAgent';

const windowsUA =
  'Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0';
const iOSUA =
  'Mozilla/5.0 (iPhone; CPU iPhone OS 13_5_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.1.1 Mobile/15E148 Safari/604.1';

describe('Utils - useUserAgent', () => {
  let mockUA: jest.SpyInstance<string, []> | null = null;

  beforeEach(() => {
    mockUA = jest.spyOn(window.navigator, 'userAgent', 'get');
  });

  it('Verify UA parameters as Windows', () => {
    mockUA!.mockReturnValue(windowsUA);
    process.browser = true;
    expect(useUserAgent().ua).toBe(windowsUA);
    expect(useUserAgent().isiOS).toBe(false);
  });

  it('Verify UA parameters as iOS', () => {
    mockUA!.mockReturnValue(iOSUA);
    process.browser = true;
    expect(useUserAgent().ua).toBe(iOSUA);
    expect(useUserAgent().isiOS).toBe(true);
  });

  it('Verify UA parameters as iOS but browser is false', () => {
    mockUA!.mockReturnValue(iOSUA);
    process.browser = false;
    expect(useUserAgent().ua).toBe(iOSUA);
    expect(useUserAgent().isiOS).toBe(false);
  });
});
