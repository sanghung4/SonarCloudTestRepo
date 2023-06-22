import { render } from 'test-utils/TestWrapper';
import Overlay from 'common/Overlay';

const mockChildren = <div>test</div>;
describe('common - Overlay', () => {
  it('Should match snapshot with the bare minimum props', () => {
    const { container } = render(<Overlay>{mockChildren}</Overlay>);
    expect(container).toMatchSnapshot();
  });

  it('Should match snapshot with the valid props', () => {
    const msg = ['test1', 'test2', 'test3'];
    const { container } = render(
      <Overlay show infoMessages={msg}>
        {mockChildren}
      </Overlay>
    );
    expect(container).toMatchSnapshot();
  });
});
