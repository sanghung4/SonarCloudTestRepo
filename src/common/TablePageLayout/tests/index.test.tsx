import TablePageLayout, { TablePageLayoutProps } from 'common/TablePageLayout';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

let mockTablePageProps: TablePageLayoutProps = {
  pageTitle: ''
};
describe('common - TablePageLayout', () => {
  afterEach(() => {
    mockTablePageProps = {
      pageTitle: ''
    };
  });

  it('expect to match snapshot on basic props on desktop', () => {
    setBreakpoint('desktop');
    const { container } = render(<TablePageLayout {...mockTablePageProps} />);
    expect(container).toMatchSnapshot();
  });

  it('expect to match snapshot on basic props on mobile', () => {
    setBreakpoint('mobile');
    const { container } = render(<TablePageLayout {...mockTablePageProps} />);
    expect(container).toMatchSnapshot();
  });

  it('expect to match snapshot while loading on desktop', () => {
    setBreakpoint('desktop');
    const { container } = render(
      <TablePageLayout {...mockTablePageProps} loading />
    );
    expect(container).toMatchSnapshot();
  });

  it('expect to match snapshot while loading on mobile', () => {
    setBreakpoint('mobile');
    const { container } = render(
      <TablePageLayout {...mockTablePageProps} loading />
    );
    expect(container).toMatchSnapshot();
  });

  it('expect to match snapshot on filled props on desktop', () => {
    setBreakpoint('desktop');
    mockTablePageProps.breadcrumbConfig = [{ text: 'test' }];
    mockTablePageProps.table = <div>table</div>;
    mockTablePageProps.customContent = <div>content</div>;
    mockTablePageProps.headerAction = <div>header</div>;
    mockTablePageProps.filters = <div>filters</div>;
    mockTablePageProps.pageTitle = 'title';
    const { container } = render(<TablePageLayout {...mockTablePageProps} />);
    expect(container).toMatchSnapshot();
  });

  it('expect to match snapshot on filled props on mobile', () => {
    setBreakpoint('mobile');
    mockTablePageProps.breadcrumbConfig = [{ text: 'test' }];
    mockTablePageProps.table = <div>table</div>;
    mockTablePageProps.customContent = <div>content</div>;
    mockTablePageProps.headerAction = <div>header</div>;
    mockTablePageProps.filters = <div>filters</div>;
    mockTablePageProps.pageTitle = 'title';
    const { container } = render(<TablePageLayout {...mockTablePageProps} />);
    expect(container).toMatchSnapshot();
  });

  it('expect to match snapshot on basic props on mobile with flatCard', () => {
    setBreakpoint('mobile');
    mockTablePageProps.flatCards = true;
    const { container } = render(<TablePageLayout {...mockTablePageProps} />);
    expect(container).toMatchSnapshot();
  });
});
