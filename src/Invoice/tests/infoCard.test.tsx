import InfoCard from 'Invoice/InfoCard';
import { successMincron } from 'Invoice/mocks';
import { render } from 'test-utils/TestWrapper';

describe('Invoice - InfoCard', () => {
  it('Should match snapshot when loading', () => {
    const results = render(<InfoCard loading />);
    expect(results).toMatchSnapshot();
  });

  it('Should match snapshot when loading invoice data', () => {
    const results = render(<InfoCard loading />);
    expect(results).toMatchSnapshot();
  });

  it('Should match snapshot after invoice data is loaded', () => {
    const results = render(
      <InfoCard loading={false} invoice={successMincron.result.data.invoice} />
    );
    expect(results).toMatchSnapshot();
  });

  it('Should match snapshot with undefined Invoice data', () => {
    const results = render(<InfoCard loading={false} />);
    expect(results).toMatchSnapshot();
  });
});
