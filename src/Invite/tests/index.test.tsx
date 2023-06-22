import Invite from 'Invite';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

let mockLoading = false;

jest.mock('generated/graphql', () => ({
  ...jest.requireActual('generated/graphql'),
  useInviteUserMutation: () => [jest.fn(), { loading: mockLoading }]
}));

describe('Invite', () => {
  afterEach(() => {
    mockLoading = false;
  });

  it('Expect to match snapshot on desktop', () => {
    setBreakpoint('desktop');
    const { container } = render(<Invite />);
    expect(container).toMatchSnapshot();
  });

  it('Expect to match snapshot on mobile', () => {
    setBreakpoint('mobile');
    const { container } = render(<Invite />);
    expect(container).toMatchSnapshot();
  });

  it('Expect to match snapshot when loading', () => {
    mockLoading = true;
    setBreakpoint('desktop');
    const { container } = render(<Invite />);
    expect(container).toMatchSnapshot();
  });
});
