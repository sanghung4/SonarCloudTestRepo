import { fireEvent } from '@testing-library/react';
import BackToTop from 'common/BackToTop';
import { render } from 'test-utils/TestWrapper';

describe('common - BackToTop', () => {
  it('should match snapshot', () => {
    const { container } = render(<BackToTop />);
    expect(container).toMatchSnapshot();
  });

  it('should call `window.scrollTo` when the button is clicked', () => {
    const oldWindowScrollTo = window.scrollTo;
    window.scrollTo = jest.fn();
    const { getByTestId } = render(<BackToTop />);
    fireEvent.click(getByTestId('back-to-top-button'));
    expect(window.scrollTo).toBeCalled();
    window.scrollTo = oldWindowScrollTo;
  });
});
