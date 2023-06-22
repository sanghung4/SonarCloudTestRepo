import TableRenderer, {
  TableRendererProps
} from 'common/TablePageLayout/TableRenderer';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { mockTableInstance } from 'test-utils/mockTableInstance';
import { render } from 'test-utils/TestWrapper';

const mockProps: TableRendererProps = {
  tableInstance: mockTableInstance,
  testId: 'test',
  primaryKey: 'key'
};

describe('common - TablePageLayout - TableRenderer', () => {
  it('Expect to match snapshot with basic props on desktop', () => {
    setBreakpoint('desktop');
    const { container } = render(<TableRenderer {...mockProps} />);
    expect(container).toMatchSnapshot();
  });
  it('Expect to match snapshot with basic props on mobile', () => {
    setBreakpoint('mobile');
    const { container } = render(<TableRenderer {...mockProps} />);
    expect(container).toMatchSnapshot();
  });
});
