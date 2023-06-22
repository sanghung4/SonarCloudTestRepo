import { render } from 'test-utils/TestWrapper';
import Loader from 'common/Loader';

describe('Common - Loader', () => {
  it('expect loader to match snapshot', () => {
    const { container } = render(<Loader />);
    expect(container).toMatchSnapshot();
  });

  it('expect loader to match snapshot with backdrop', () => {
    const { container } = render(<Loader backdrop />);
    expect(container).toMatchSnapshot();
  });

  it('expect loader to match snapshot with `page` size', () => {
    const { container } = render(<Loader size="page" />);
    expect(container).toMatchSnapshot();
  });
  it('expect loader to match snapshot with `flex` size', () => {
    const { container } = render(<Loader size="flex" />);
    expect(container).toMatchSnapshot();
  });
});
