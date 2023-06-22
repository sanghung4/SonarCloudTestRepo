import AccordionWrapper, {
  AccordionWrapperProps
} from 'common/AccordionWrapper';
import { render } from 'test-utils/TestWrapper';

const mockAccordionWrapperProps: AccordionWrapperProps = {
  label: 'test',
  testId: 'test123',
  children: <div>test</div>
};

describe('common - AccordionWrapper', () => {
  it('expect to match snapshot', () => {
    const { container } = render(
      <AccordionWrapper {...mockAccordionWrapperProps} />
    );
    expect(container).toMatchSnapshot();
  });

  it('expect to match snapshot as warning variant', () => {
    mockAccordionWrapperProps.variant = 'warning';
    const { container } = render(
      <AccordionWrapper {...mockAccordionWrapperProps} />
    );
    expect(container).toMatchSnapshot();
  });
});
