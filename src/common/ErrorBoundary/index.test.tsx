import ErrorBoundary from 'common/ErrorBoundary';
import { render } from 'test-utils/TestWrapper';

type MockRedirectToObj = {
  pathname?: string;
  state?: unknown;
};
type MockRedirectProp = {
  to: string | MockRedirectToObj;
};
function MockRedirect({ to }: MockRedirectProp) {
  return (
    <span data-testid="redirect">
      {typeof to === 'string' ? to : to.pathname}
    </span>
  );
}

jest.mock('react-router-dom', () => ({
  Redirect: MockRedirect
}));

const MockElement = () => <div data-testid="test-element" />;
describe('common - ErrorBoundary', () => {
  it('Expect to render MockElement as children with no options', () => {
    const { getByTestId } = render(
      <ErrorBoundary>
        <MockElement />
      </ErrorBoundary>
    );
    expect(getByTestId('test-element')).toBeInTheDocument();
  });

  it('Expect to branch to Redirect with text children', () => {
    const { getByTestId } = render(<ErrorBoundary>test</ErrorBoundary>);
    expect(getByTestId('redirect')).toHaveTextContent('/');
  });

  it('Expect to branch to Redirect with error', () => {
    const mockError: Error = {
      name: 'test',
      message: 'this is a test error!'
    };
    const { getByTestId } = render(
      <ErrorBoundary mockErrorForTest={mockError}>test</ErrorBoundary>
    );
    expect(getByTestId('redirect')).toHaveTextContent('/error');
  });
});
