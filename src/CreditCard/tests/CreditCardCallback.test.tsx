import CreditCardCallback from 'CreditCard/CreditCardCallback';
import { render } from 'test-utils/TestWrapper';

describe('CreditCard - CreditCardCallback', () => {
  it('Expect to match snapshot', () => {
    const { container } = render(<CreditCardCallback />);
    expect(container).toMatchSnapshot();
  });
});
