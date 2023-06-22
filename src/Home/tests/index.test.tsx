import Home from 'Home';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';
import { mocks } from 'Home/tests/mocks';

describe('Home', () => {
  it('should render the Home page', async () => {
    setBreakpoint('desktop');
    const { container, findByTestId } = render(<Home />, {
      authConfig: mocks.authorized
    });

    const homePage = await findByTestId('main-content');
    expect(homePage).toBeInTheDocument();

    expect(container).toMatchSnapshot();
  });

  it('should render the Home page as guest', async () => {
    setBreakpoint('desktop');
    const { container, findByTestId } = render(<Home />, {
      authConfig: mocks.guest
    });

    const signInForm = await findByTestId('sign-in-form-main');
    expect(signInForm).toBeInTheDocument();

    expect(container).toMatchSnapshot();
  });

  it('should render the Verify Email page', async () => {
    setBreakpoint('desktop');
    const { container, findByTestId } = render(<Home />, {
      authConfig: mocks.verify
    });

    const verifyEmail = await findByTestId('verifyemail-component');
    expect(verifyEmail).toBeInTheDocument();
    expect(container).toMatchSnapshot();
  });

  it('should render the Pending User page', async () => {
    setBreakpoint('desktop');
    const { container, findByTestId } = render(<Home />, {
      authConfig: mocks.pending
    });

    const pendingUser = await findByTestId('pendinguser-component');
    expect(pendingUser).toBeInTheDocument();
    expect(container).toMatchSnapshot();
  });

  it('should render the Home page on mobile display', async () => {
    setBreakpoint('mobile');
    const { container, findByTestId } = render(<Home />, {
      authConfig: mocks.authorized
    });

    const homePage = await findByTestId('main-content');
    expect(homePage).toBeInTheDocument();

    expect(container).toMatchSnapshot();
  });

  it('should render the Home page as guest on mobile display', async () => {
    setBreakpoint('mobile');
    const { container, findByTestId } = render(<Home />, {
      authConfig: mocks.guest
    });

    const signInForm = await findByTestId('sign-in-form-main');
    expect(signInForm).toBeInTheDocument();

    expect(container).toMatchSnapshot();
  });

  it('should render the Verify Email page on mobile display', async () => {
    setBreakpoint('mobile');
    const { container, findByTestId } = render(<Home />, {
      authConfig: mocks.verify
    });

    const verifyEmail = await findByTestId('verifyemail-component');
    expect(verifyEmail).toBeInTheDocument();
    expect(container).toMatchSnapshot();
  });

  it('should render the Pending User page on mobile display', async () => {
    setBreakpoint('mobile');
    const { container, findByTestId } = render(<Home />, {
      authConfig: mocks.pending
    });

    const pendingUser = await findByTestId('pendinguser-component');
    expect(pendingUser).toBeInTheDocument();
    expect(container).toMatchSnapshot();
  });
});
