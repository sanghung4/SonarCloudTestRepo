import Image from 'components/Image';
import { render } from 'test-utils/TestWrapper';

describe('components/Image', () => {
  it('expect to render Image', () => {
    const { container } = render(<Image />);
    expect(container).toBeInTheDocument();
  });
});
