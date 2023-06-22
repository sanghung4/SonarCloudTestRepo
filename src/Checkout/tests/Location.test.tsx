import { createMockBranches } from 'Branches/tests/mocks';
import Location from 'Checkout/Location';
import { mockErpAccount } from 'Checkout/tests/mocks/accounts.mocks';
import { mockCustomAddress } from 'Checkout/tests/mocks/deliveryMethodObject.mock';
import { mockData as mockContractDetails } from 'Contract/tests/mocks';
import { Branch } from 'generated/graphql';
import { render } from 'test-utils/TestWrapper';

describe('Checkout - Location', () => {
  it('should match snapshot with undefined props', () => {
    const { container } = render(<Location />);
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot as ContractDetails', () => {
    const { container } = render(
      <Location location={mockContractDetails} boldName includePhone />
    );
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot as Branch', () => {
    const mockBranch: Branch = {
      ...createMockBranches(1)[0],
      phone: '1234567890'
    };
    const { container } = render(
      <Location location={mockBranch} boldName includePhone />
    );
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot as ErpAccount', () => {
    const { container } = render(
      <Location location={mockErpAccount} boldName includePhone />
    );
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot as CustomAddressInput', () => {
    const { container } = render(
      <Location location={mockCustomAddress} boldName includePhone />
    );
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot without boldnam,e', () => {
    const { container } = render(
      <Location location={mockCustomAddress} includePhone />
    );
    expect(container).toMatchSnapshot();
  });
});
