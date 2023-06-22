//import { ContactInfo } from 'generated/graphql';
import UserPanel from 'Home/UserPanel';
import { render } from 'test-utils/TestWrapper';
import { mocks } from 'Home/tests/mocks';

describe('Home - UserPanel', () => {
  it('Match snapshot with no permissions', () => {
    const { container } = render(<UserPanel />, {
      authConfig: mocks.guest
    });
    expect(container).toMatchSnapshot();
  });

  it('Match snapshot with unauthorized eclipse account', () => {
    const { container } = render(<UserPanel />, {
      authConfig: mocks.notAuthorized
    });
    expect(container).toMatchSnapshot();
  });

  it('Match snapshot with unauthorized mincron account', () => {
    const { container } = render(<UserPanel />, {
      authConfig: mocks.notAuthorized
    });
    expect(container).toMatchSnapshot();
  });

  it('Match snapshot with authorized eclipse account', () => {
    const { container } = render(<UserPanel />, {
      authConfig: mocks.pending
    });
    expect(container).toMatchSnapshot();
  });

  it('Match snapshot with authorized mincron account', () => {
    const { container } = render(<UserPanel />, {
      authConfig: mocks.pending
    });
    expect(container).toMatchSnapshot();
  });

  it('Match snapshot with admin eclipse account', () => {
    const { container } = render(<UserPanel />, {
      authConfig: mocks.admin
    });
    expect(container).toMatchSnapshot();
  });

  it('Match snapshot with admin mincron account', () => {
    const { container } = render(<UserPanel />, {
      authConfig: mocks.admin
    });
    expect(container).toMatchSnapshot();
  });
});

describe('Home - Welcome message', () => {
  const welcmText = 'Hi, {{name}}!';
  it('Match welcome message snapshot with authorized eclipse account', () => {
    const { getByTestId } = render(<UserPanel />, {
      authConfig: mocks.pending
    });
    const welcomeMsg = getByTestId('user-panel-title');
    expect(welcomeMsg).toHaveTextContent(welcmText);
  });

  it('Match welcome message snapshot with authorized mincron account', () => {
    const { getByTestId } = render(<UserPanel />, {
      authConfig: mocks.pending
    });
    const welcomeMsg = getByTestId('user-panel-title');
    expect(welcomeMsg).toHaveTextContent(welcmText);
  });
});
