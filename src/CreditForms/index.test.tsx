import CreditForms from 'CreditForms';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';
import { useDomainInfo } from 'hooks/useDomainInfo';

const mockDomainInfo: ReturnType<typeof useDomainInfo> = {
  companyList: [],
  companyNameLink: '',
  companyNameList: '',
  engine: '',
  isWaterworks: false,
  rootDomain: '',
  subdomain: '',
  port: ''
};

jest.mock('hooks/useDomainInfo', () => {
  const originalModule = jest.requireActual('hooks/useDomainInfo');
  return {
    __esModule: true,
    ...originalModule,
    useDomainInfo: () => mockDomainInfo
  };
});

describe('CreditForms', () => {
  it('CreditForms should be matching the snapshot', () => {
    setBreakpoint('desktop');
    mockDomainInfo.isWaterworks = false;
    const { container } = render(<CreditForms />);
    expect(container).toMatchSnapshot();
  });

  it('CreditForms for fortiline should be matching the snapshot', () => {
    setBreakpoint('desktop');
    mockDomainInfo.isWaterworks = true;
    const { container } = render(<CreditForms />);
    expect(container).toMatchSnapshot();
  });

  it('CreditForms should be matching the snapshot on mobile', () => {
    setBreakpoint('mobile');
    mockDomainInfo.isWaterworks = false;
    const { container } = render(<CreditForms />);
    expect(container).toMatchSnapshot();
  });

  it('CreditForms for fortiline should be matching the snapshot on mobile', () => {
    setBreakpoint('mobile');
    mockDomainInfo.isWaterworks = true;
    const { container } = render(<CreditForms />);
    expect(container).toMatchSnapshot();
  });
});
