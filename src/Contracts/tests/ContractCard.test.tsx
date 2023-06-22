import ContractCard from 'Contracts/ContractCard';
import { success } from 'Contracts/tests/mocks';
import { render } from 'test-utils/TestWrapper';

jest.mock('react-router-dom', () => ({
  useParams: () => ({
    id: '5432705'
  }),
  useLocation: () => ({
    state: {}
  })
}));

describe('Contracts - ContractCard', () => {
  const mockContract = success.result.data.contracts.results[0];

  it('matches the snapshot', () => {
    const { container } = render(
      <ContractCard contract={mockContract} index={0} />
    );
    expect(container).toMatchSnapshot();
  });

  it('matches the snapshot when isEven is set', () => {
    const { container } = render(
      <ContractCard contract={mockContract} isEven index={0} />
    );
    expect(container).toMatchSnapshot();
  });

  it('matches the snapshot when contract data is undefined', () => {
    const { container } = render(<ContractCard index={0} />);
    expect(container).toMatchSnapshot();
  });

  it('contract link stores location details', () => {
    const { container, getByTestId } = render(
      <ContractCard contract={mockContract} index={0} />
    );
    const contractNumber = getByTestId('contractNumber-0');
    expect(contractNumber).toHaveAttribute('to');
    expect(container).toMatchSnapshot();
  });
});
