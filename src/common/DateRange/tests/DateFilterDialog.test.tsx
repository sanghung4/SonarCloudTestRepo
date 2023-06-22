import { fireEvent } from '@testing-library/react';

import DateRange from 'common/DateRange';
import DateFilterDialog from 'common/DateRange/DateFilterDialog';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

const initialRange = {
  from: new Date('01/01/2020'),
  to: new Date('06/01/2020')
};

describe('common - DateRange - DateFilterDialog', () => {
  it('Should match snapshot on desktop', () => {
    setBreakpoint('desktop');
    const wrapper = render(
      <DateRange value={initialRange} onChange={jest.fn()}>
        <DateFilterDialog open onClose={jest.fn()} onViewResults={jest.fn()} />
      </DateRange>
    );
    expect(wrapper).toMatchSnapshot();
  });

  it('Should match snapshot on mobile', () => {
    setBreakpoint('mobile');
    const wrapper = render(
      <DateRange value={initialRange} onChange={jest.fn()}>
        <DateFilterDialog open onClose={jest.fn()} onViewResults={jest.fn()} />
      </DateRange>
    );
    expect(wrapper).toMatchSnapshot();
  });

  it('Should match snapshot with valid onClear', () => {
    setBreakpoint('desktop');
    const wrapper = render(
      <DateRange value={initialRange} onChange={jest.fn()} onClear={jest.fn()}>
        <DateFilterDialog open onClose={jest.fn()} onViewResults={jest.fn()} />
      </DateRange>
    );
    expect(wrapper).toMatchSnapshot();
  });

  it('Should expect prop function to be called when clicking "View Results" button', () => {
    setBreakpoint('desktop');
    const [closeFn, viewResultsFn] = [jest.fn(), jest.fn()];
    const wrapper = render(
      <DateRange onChange={jest.fn()} onClear={jest.fn()}>
        <DateFilterDialog
          open
          onClose={closeFn}
          onViewResults={viewResultsFn}
        />
      </DateRange>
    );
    const button = wrapper.getByTestId('view-results-button');
    fireEvent.click(button);

    expect(viewResultsFn).toBeCalled();
    expect(closeFn).toBeCalled();
  });
});
