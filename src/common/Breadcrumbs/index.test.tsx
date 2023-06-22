import Breadcrumbs, { BreadcrumbConfig } from 'common/Breadcrumbs';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

describe('common - BreadCrumbs', () => {
  it('should match snapshot with no config on desktop', () => {
    setBreakpoint('desktop');
    const { container } = render(<Breadcrumbs pageTitle="test" />);
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot with no config on mobile', () => {
    setBreakpoint('mobile');
    const { container } = render(<Breadcrumbs pageTitle="test" />);
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot with . as text on desktop', () => {
    setBreakpoint('desktop');
    const config: BreadcrumbConfig[] = [{ text: '.', to: 'test' }];
    const { container } = render(
      <Breadcrumbs pageTitle="test" config={config} />
    );
    expect(container).toMatchSnapshot();
  });
  it('should match snapshot with . as text on mobile', () => {
    setBreakpoint('mobile');
    const config: BreadcrumbConfig[] = [{ text: '.', to: 'test' }];
    const { container } = render(
      <Breadcrumbs pageTitle="test" config={config} />
    );
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot with test as text on desktop', () => {
    setBreakpoint('desktop');
    const config: BreadcrumbConfig[] = [{ text: 'test', to: 'test' }];
    const { container } = render(
      <Breadcrumbs pageTitle="test" config={config} />
    );
    expect(container).toMatchSnapshot();
  });
  it('should match snapshot with test as text on mobile', () => {
    setBreakpoint('mobile');
    const config: BreadcrumbConfig[] = [{ text: 'test', to: 'test' }];
    const { container } = render(
      <Breadcrumbs pageTitle="test" config={config} />
    );
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot with to as Location on desktop', () => {
    setBreakpoint('desktop');
    const toLocation = {
      hash: 'test',
      pathname: '/test',
      search: 'test',
      state: 'test'
    };
    const config: BreadcrumbConfig[] = [{ text: 'test', to: toLocation }];
    const { container } = render(
      <Breadcrumbs pageTitle="test" config={config} />
    );
    expect(container).toMatchSnapshot();
  });
  it('should match snapshot with to as Location on mobile', () => {
    setBreakpoint('mobile');
    const toLocation = {
      hash: 'test',
      pathname: '/test',
      search: 'test',
      state: 'test'
    };
    const config: BreadcrumbConfig[] = [{ text: 'test', to: toLocation }];
    const { container } = render(
      <Breadcrumbs pageTitle="test" config={config} />
    );
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot with to as Location and . as text on mobile', () => {
    setBreakpoint('mobile');
    const toLocation = {
      hash: 'test',
      pathname: '/test',
      search: 'test',
      state: 'test'
    };
    const config: BreadcrumbConfig[] = [{ text: '.', to: toLocation }];
    const { container } = render(
      <Breadcrumbs pageTitle="test" config={config} />
    );
    expect(container).toMatchSnapshot();
  });
});
