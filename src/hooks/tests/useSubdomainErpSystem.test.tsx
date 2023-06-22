import { ErpSystemEnum } from 'generated/graphql';
import { render } from 'test-utils/TestWrapper';
import { useDomainInfo } from '../useDomainInfo';
import { useSubdomainErpSystem } from 'hooks/useSubdomainErpSystem';

const mockDomain: ReturnType<typeof useDomainInfo> = {
  companyList: [],
  companyNameLink: '',
  companyNameList: '',
  engine: '',
  isWaterworks: false,
  rootDomain: '',
  subdomain: '',
  port: '',
  brand: ''
};

jest.mock('hooks/useDomainInfo', () => ({
  useDomainInfo: () => mockDomain
}));

function MockComponent() {
  const { currentSubdomain } = useSubdomainErpSystem();
  return <span data-testid="data">{currentSubdomain}</span>;
}

describe('Utils - useSubdomainErpSystem', () => {
  it('Expect Eclipse when isAuthenticated is true', () => {
    mockDomain.subdomain = 'eclipse';
    mockDomain.isWaterworks = false;
    const { getByTestId } = render(<MockComponent />, {
      authConfig: {
        authState: { isAuthenticated: true }
      }
    });
    expect(getByTestId('data')).toHaveTextContent(ErpSystemEnum.Eclipse);
  });

  it('Expect Eclipse when isAuthenticated is false an subdomain is not "fortiline"', () => {
    mockDomain.subdomain = 'eclipse';
    mockDomain.isWaterworks = false;
    const { getByTestId } = render(<MockComponent />, {
      authConfig: {
        authState: { isAuthenticated: false }
      }
    });
    expect(getByTestId('data')).toHaveTextContent(ErpSystemEnum.Eclipse);
  });

  it('Expect Mincron when isAuthenticated is false an subdomain is "fortiline"', () => {
    mockDomain.subdomain = 'fortiline';
    mockDomain.isWaterworks = true;
    const { getByTestId } = render(<MockComponent />, {
      authConfig: {
        authState: { isAuthenticated: false }
      }
    });
    expect(getByTestId('data')).toHaveTextContent(ErpSystemEnum.Mincron);
  });
});
