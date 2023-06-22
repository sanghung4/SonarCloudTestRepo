import InvoiceItem from 'Invoice/InvoiceItem';
import { successMincron } from 'Invoice/mocks';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

describe('Invoice - InvoiceItem', () => {
  it('Should match snapshot when loading on desktop', () => {
    setBreakpoint('desktop');
    const { container } = render(<InvoiceItem loading />);
    expect(container).toMatchSnapshot();
  });

  it('Should match snapshot when loading on mobile', () => {
    setBreakpoint('mobile');
    const { container } = render(<InvoiceItem loading />);
    expect(container).toMatchSnapshot();
  });

  it('Should match snapshot with undefined product on desktop', () => {
    setBreakpoint('desktop');
    const { container } = render(<InvoiceItem />);
    expect(container).toMatchSnapshot();
  });

  it('Should match snapshot with undefined product on mobile', () => {
    setBreakpoint('mobile');
    const { container } = render(<InvoiceItem />);
    expect(container).toMatchSnapshot();
  });

  it('Should match snapshot with an invoice', () => {
    const item = successMincron.result.data.invoice.invoiceItems[0];
    const { container } = render(<InvoiceItem invoiceItem={item} />);
    expect(container).toMatchSnapshot();
  });

  it('Should match snapshot with an invoice as Mincron', () => {
    const item = successMincron.result.data.invoice.invoiceItems[0];
    const { container } = render(<InvoiceItem invoiceItem={item} isMincron />);
    expect(container).toMatchSnapshot();
  });
});
