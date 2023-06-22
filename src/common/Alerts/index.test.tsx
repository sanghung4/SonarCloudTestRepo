import CommonAlert from 'common/Alerts';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

const props = {
  title: 'test',
  message: 'test',
  confirmBtnTitle: 'test',
  onConfirm: jest.fn(),
  open: false
};
describe('Common - Alerts', () => {
  it('Should match the snapshot that renders nothing', () => {
    setBreakpoint('desktop');
    const wrapper = render(<CommonAlert {...props} />);
    expect(wrapper).toMatchSnapshot();
  });
  it('Should match the snapshot that renders the alert dialog on desktop', () => {
    setBreakpoint('desktop');
    const wrapper = render(<CommonAlert {...props} open />);
    expect(wrapper).toMatchSnapshot();
  });
  it('Should match the snapshot that renders the alert dialog on mobile', () => {
    setBreakpoint('mobile');
    const wrapper = render(<CommonAlert {...props} open />);
    expect(wrapper).toMatchSnapshot();
  });
});
