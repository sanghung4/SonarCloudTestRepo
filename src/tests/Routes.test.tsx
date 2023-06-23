import { render } from 'test-util';

import Routes from 'Routes';

describe('Routes', () => {
  it('Expect route to be rendered', () => {
    const { container } = render(<Routes />);
    expect(container).toBeInTheDocument();
  });
});
