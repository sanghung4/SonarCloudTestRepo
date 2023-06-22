import Card from 'components/Card';
import { render } from 'test-utils/TestWrapper';

describe('components/Card', () => {
  it('expect element to be rendered', () => {
    const { container } = render(<Card />);
    expect(container).toBeInTheDocument();
  });
});
