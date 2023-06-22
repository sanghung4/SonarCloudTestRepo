import ContractMoreDetailsDesktop from 'Contract/ProductMoreDetailsDesktop';
import ContractMoreDetailsMobile from 'Contract/ProductMoreDetailsMobile';
import { GetContractDetailsMock } from 'Contract/tests/mocks';
import { ContractProduct } from 'generated/graphql';
import { render } from 'test-utils/TestWrapper';

describe('Contract Details - Line Item Details', () => {
  const listData = GetContractDetailsMock.result.data.contract
    .contractProducts as ContractProduct[];

  it('Desktop snapshot match', () => {
    const { container } = render(
      <ContractMoreDetailsDesktop product={listData[0]} />
    );

    expect(container).toMatchSnapshot();
  });

  it('Desktop snapshot match for empty data', () => {
    const { container } = render(<ContractMoreDetailsDesktop />);

    expect(container).toMatchSnapshot();
  });

  it('Mobile snapshot match', () => {
    const { container } = render(
      <ContractMoreDetailsMobile product={listData[0]} />
    );

    expect(container).toMatchSnapshot();
  });

  it('Mobile snapshot match for empty data', () => {
    const { container } = render(<ContractMoreDetailsMobile />);

    expect(container).toMatchSnapshot();
  });
});
