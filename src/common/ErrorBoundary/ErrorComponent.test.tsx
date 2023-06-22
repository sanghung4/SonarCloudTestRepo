import ErrorComponent, {
  ErrorTypes
} from 'common/ErrorBoundary/ErrorComponent';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

type MockRedirectToObj = {
  pathname?: string;
  state?: unknown;
};
type MockRedirectProp = {
  to: string | MockRedirectToObj;
};
type MockState = { errorType?: string };

const mockState: MockState = {
  errorType: ErrorTypes.NOT_FOUND
};
function MockRedirect({ to }: MockRedirectProp) {
  return (
    <span data-testid="redirect">
      {typeof to === 'string' ? to : to.pathname}
    </span>
  );
}

jest.mock('react-router-dom', () => ({
  useLocation: () => ({
    state: mockState
  }),
  Redirect: MockRedirect
}));

const setup = () => {
  const utils = render(<ErrorComponent />);

  const productNotFoundText = utils.getByTestId('product-not-found');
  const backToHomeButton = utils.getByTestId('back-to-home-button');
  return {
    utils,
    productNotFoundText,
    backToHomeButton
  };
};

describe('common - ErrorBoundary - ErrorComponent', () => {
  it('should match snapshot on desktop as NOT_FOUND', () => {
    setBreakpoint('desktop');
    mockState.errorType = ErrorTypes.NOT_FOUND;
    const {
      utils: { container }
    } = setup();
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot on mobile as NOT_FOUND', () => {
    setBreakpoint('mobile');
    mockState.errorType = ErrorTypes.NOT_FOUND;
    const {
      utils: { container }
    } = setup();
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot on desktop as BRANCH_ERROR', () => {
    setBreakpoint('desktop');
    mockState.errorType = ErrorTypes.BRANCH_ERROR;
    const {
      utils: { container }
    } = setup();
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot on mobile as BRANCH_ERROR', () => {
    setBreakpoint('mobile');
    mockState.errorType = ErrorTypes.BRANCH_ERROR;
    const {
      utils: { container }
    } = setup();
    expect(container).toMatchSnapshot();
  });

  it('Should navigate to Error page with Product Not Found error type', () => {
    setBreakpoint('desktop');
    mockState.errorType = ErrorTypes.NOT_FOUND;
    const { productNotFoundText, backToHomeButton } = setup();

    expect(productNotFoundText).toBeTruthy();
    expect(backToHomeButton).toBeTruthy();
  });

  it('should match snapshot on desktop as OTHER', () => {
    setBreakpoint('desktop');
    mockState.errorType = ErrorTypes.OTHER;
    const {
      utils: { container }
    } = setup();
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot on mobile as OTHER', () => {
    setBreakpoint('mobile');
    mockState.errorType = ErrorTypes.OTHER;
    const {
      utils: { container }
    } = setup();
    expect(container).toMatchSnapshot();
  });

  it('should expect Redirect to be "rendered" as `undefined`', () => {
    setBreakpoint('desktop');
    mockState.errorType = undefined;
    const { getByTestId } = render(<ErrorComponent />);
    expect(getByTestId('redirect')).toHaveTextContent('/');
  });
});
