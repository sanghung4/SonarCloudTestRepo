import { TableRendererProps } from 'common/TablePageLayout/TableRenderer';
import { TableRendererFooter } from 'common/TablePageLayout/util';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { mockTableInstance } from 'test-utils/mockTableInstance';
import { render } from 'test-utils/TestWrapper';

let mockProps: TableRendererProps = {
  tableInstance: mockTableInstance,
  testId: 'test',
  primaryKey: 'key'
};

describe('common - TablePageLayout - util - TableRendererFooter', () => {
  it('expect to match snapshot on desktop', () => {
    setBreakpoint('desktop');
    const { container } = render(<TableRendererFooter {...mockProps} />);
    expect(container).toMatchSnapshot();
  });
  it('expect to match snapshot on mobile', () => {
    setBreakpoint('mobile');
    const { container } = render(<TableRendererFooter {...mockProps} />);
    expect(container).toMatchSnapshot();
  });
});
