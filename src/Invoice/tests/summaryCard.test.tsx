import { render } from 'test-utils/TestWrapper';

import SummaryCard from 'Invoice/SummaryCard';
import { successMincron } from 'Invoice/mocks';

jest.mock('@dialexa/reece-component-library', () => {
  const originalModule = jest.requireActual('@dialexa/reece-component-library');
  return {
    __esModule: true,
    ...originalModule,
    useSnackbar: () => ({
      pushAlert: jest.fn()
    })
  };
});

describe('Invoice - SummaryCard', () => {
  it('Should match snapshot when loading', () => {
    const { container } = render(<SummaryCard loading />);
    expect(container).toMatchSnapshot();
  });

  it('Should match snapshot with undefined invoice data', () => {
    const { container } = render(<SummaryCard />);
    expect(container).toMatchSnapshot();
  });

  it('Should match snapshot with an invoice', () => {
    const { container } = render(
      <SummaryCard invoice={successMincron.result.data.invoice} />
    );
    expect(container).toMatchSnapshot();
  });
});
