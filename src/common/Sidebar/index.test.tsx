import { act } from 'react-dom/test-utils';

import Sidebar from 'common/Sidebar';
import Backdrop from 'common/Sidebar/Backdrop';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

const mockChild = <div>test</div>;

describe('common - Sidebar', () => {
  it('expect to match snapshot with basic props', () => {
    setBreakpoint('desktop');
    const { container } = render(
      <Sidebar close={jest.fn()} on>
        {mockChild}
      </Sidebar>
    );
    expect(container).toMatchSnapshot();
  });

  it('expect to match snapshot with error', () => {
    setBreakpoint('desktop');
    const { container } = render(
      <Sidebar close={jest.fn()} on error>
        {mockChild}
      </Sidebar>
    );
    expect(container).toMatchSnapshot();
  });

  it('expect to match snapshot on desktop', async () => {
    setBreakpoint('desktop');
    const { container } = render(
      <Sidebar close={jest.fn()} on>
        {mockChild}
      </Sidebar>
    );
    await act(() => new Promise((res) => setTimeout(res, 10)));
    expect(container).toMatchSnapshot();
  });

  it('expect to match snapshot on mobile', async () => {
    setBreakpoint('mobile');
    const { container } = render(
      <Sidebar close={jest.fn()} on>
        {mockChild}
      </Sidebar>
    );
    await act(() => new Promise((res) => setTimeout(res, 10)));
    expect(container).toMatchSnapshot();
  });

  it('expect to match snapshot for Backdrop', () => {
    setBreakpoint('desktop');
    const { container } = render(<Backdrop onClick={jest.fn()} on />);
    expect(container).toMatchSnapshot();
  });
});
