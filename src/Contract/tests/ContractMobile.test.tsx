import { noop } from 'lodash-es';

import ContractLineItem from 'Contract/ContractLineItem';
import ContractListComment from 'Contract/ContractListComment';
import { ContractProvider } from 'Contract/ContractProvider';
import ContractProductDetailsModal from 'Contract/ProductDetailsModal';
import ContractProductList from 'Contract/ProductList';
import ContractProductListControls from 'Contract/ProductListControls';
import { GetContractDetailsMock } from 'Contract/tests/mocks';
import { ContractProduct } from 'generated/graphql';
import { render } from 'test-utils/TestWrapper';

jest.mock('@dialexa/reece-component-library', () => {
  const originalModule = jest.requireActual('@dialexa/reece-component-library');
  return {
    __esModule: true,
    ...originalModule,
    useScreenSize: () => ({ isSmallScreen: true })
  };
});

describe('Contract Details - MOBILE', () => {
  // ---------- Product List ----------
  const listData = GetContractDetailsMock.result.data.contract
    .contractProducts as ContractProduct[];

  it('ContractProductList - Snapshot match', () => {
    const { container } = render(
      <ContractProvider>
        <ContractProductList list={listData} />
      </ContractProvider>
    );

    expect(container).toMatchSnapshot();
  });

  it('ContractProductList - Snapshot match for empty list', async () => {
    const { getByTestId, container } = render(
      <ContractProvider>
        <ContractProductList list={[]} />
      </ContractProvider>
    );
    const noResult = getByTestId('contract-product-list-empty');

    expect(noResult).toBeInTheDocument();
    expect(container).toMatchSnapshot();
  });

  // ---------- Product List Controls ----------
  it('ContractProductListControls - Snapshot match', () => {
    const { baseElement } = render(
      <ContractProvider>
        <ContractProductListControls
          count={1}
          searchApplied=""
          setSearchApplied={noop}
          testId="test"
        />
      </ContractProvider>
    );
    expect(baseElement).toMatchSnapshot();
  });

  // ---------- Contract Line Item ----------
  const itemData = GetContractDetailsMock.result.data.contract
    .contractProducts![0] as ContractProduct;
  const emptyData = {} as ContractProduct;

  it('ContractLineItem - Snapshot match with data', () => {
    const { container } = render(
      <ContractProvider>
        <ContractLineItem sequence={0} data={itemData} loading={false} />
      </ContractProvider>
    );
    expect(container).toMatchSnapshot();
  });

  it('ContractLineItem - Snapshot match while loading', () => {
    const { container } = render(
      <ContractProvider>
        <ContractLineItem sequence={0} data={itemData} loading />
      </ContractProvider>
    );
    expect(container).toMatchSnapshot();
  });

  it('ContractLineItem - Snapshot match with no data', () => {
    const { container } = render(
      <ContractProvider>
        <ContractLineItem sequence={0} data={emptyData} loading={false} />
      </ContractProvider>
    );
    expect(container).toMatchSnapshot();
  });

  it('ContractListComment - Snapshot match with comment', () => {
    const { container } = render(
      <ContractListComment comment="TEST COMMENT" />
    );
    expect(container).toMatchSnapshot();
  });

  // ---------- Contract Line Comment ----------
  it('ContractListComment - Snapshot match without comment', () => {
    const { container } = render(<ContractListComment />);
    expect(container).toMatchSnapshot();
  });

  // ---------- Product Detail Modal ----------
  it('ContractProductDetailsModal - Snapshot match with data', () => {
    const { baseElement } = render(
      <ContractProductDetailsModal product={itemData} onClose={noop} />
    );
    expect(baseElement).toMatchSnapshot();
  });

  it('ContractProductDetailsModal - Snapshot match without data', () => {
    const { baseElement } = render(
      <ContractProductDetailsModal onClose={noop} />
    );
    expect(baseElement).toMatchSnapshot();
  });
});
