import Portal from 'Portal';
import { render } from 'test-utils/TestWrapper';

describe('Portal', () => {
  it('Should match snapshot', () => {
    const { container } = render(<Portal />);
    expect(container).toMatchSnapshot();
  });
});
