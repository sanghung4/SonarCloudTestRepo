import { render } from 'test-utils/TestWrapper';
import inputTestId, { radio } from 'utils/inputTestId';

describe('utils - inputTestId', () => {
  it('expect to return data-testid', () => {
    const data = 'test';
    const result = inputTestId(data) as any;
    expect(result['data-testid']).toBe(data);
  });

  it('expect to match snapshot of radio', () => {
    const { container } = render(radio('test'));
    expect(container).toMatchSnapshot();
  });
});
