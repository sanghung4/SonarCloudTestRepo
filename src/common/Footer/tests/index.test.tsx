import Footer from 'common/Footer';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';
import { testIds } from 'test-utils/testIds';

class MockDate extends Date {
  constructor() {
    super('2017/07/09');
  }
}
const oldDateObj = global.Date;

const mockUseLocation = jest.fn();

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useLocation: () => mockUseLocation()
}));

describe('common - Footer', () => {
  beforeAll(() => {
    global.Date = MockDate as DateConstructor;
  });
  afterAll(() => {
    global.Date = oldDateObj;
  });

  it('expect to match snapshot in desktop', () => {
    mockUseLocation.mockReturnValue({ pathname: '/' });

    setBreakpoint('desktop');
    const { container } = render(<Footer />);
    expect(container).toMatchSnapshot();
  });
  it('expect to match snapshot in mobile', () => {
    mockUseLocation.mockReturnValue({ pathname: '/' });

    setBreakpoint('mobile');
    const { container } = render(<Footer />);
    expect(container).toMatchSnapshot();
  });

  it('Should remove footer links in select-accounts', () => {
    mockUseLocation.mockReturnValue({ pathname: '/select-accounts' });

    const { queryByTestId } = render(<Footer />);

    const linksToolbar = queryByTestId(testIds.Footer.linksToolbar);
    const legalLinks = queryByTestId(testIds.Footer.legalLinks);

    expect(linksToolbar).toBeFalsy();
    expect(legalLinks).toBeFalsy();
  });
});
