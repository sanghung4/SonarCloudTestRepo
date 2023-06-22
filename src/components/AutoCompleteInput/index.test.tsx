import AutoCompleteInput from 'components/AutoCompleteInput';
import { render } from 'test-utils/TestWrapper';

function setup() {
  return render(
    <AutoCompleteInput<string>
      required
      label="test"
      message="test"
      value={null}
      options={[{ label: 'a', value: 'A' }]}
      onChange={jest.fn()}
      ref={null}
    />
  );
}

// to-do: finish this later on
describe('components/AutoCompleteInput', () => {
  it('expect element to be rendered', () => {
    const { container } = setup();
    expect(container).toBeInTheDocument();
  });
});
