import { fireEvent } from '@testing-library/react';
import { noop } from 'lodash-es';

import ContractProductDetailsModal from 'Contract/ProductDetailsModal';
import { GetContractDetailsMock } from 'Contract/tests/mocks';
import { ContractProduct } from 'generated/graphql';
import { render } from 'test-utils/TestWrapper';

describe('Contract Details - Product Detail Modal', () => {
  const itemData = GetContractDetailsMock.result.data.contract
    .contractProducts![0] as ContractProduct;

  it('Snapshot match with data', () => {
    const { baseElement } = render(
      <ContractProductDetailsModal product={itemData} onClose={noop} />
    );
    expect(baseElement).toMatchSnapshot();
  });

  it('Snapshot match without data', () => {
    const { baseElement } = render(
      <ContractProductDetailsModal onClose={noop} />
    );
    expect(baseElement).toMatchSnapshot();
  });

  it('Close Button click test', async () => {
    const testFunction = jest.fn();
    const { findByTestId } = render(
      <ContractProductDetailsModal product={itemData} onClose={testFunction} />
    );
    const closeButtonTop = await findByTestId(
      'contract-product-modal-close-button-top'
    );
    const closeButtonBottom = await findByTestId(
      'contract-product-modal-close-button-bottom'
    );
    fireEvent.click(closeButtonTop);
    fireEvent.click(closeButtonBottom);
    expect(testFunction).toBeCalledTimes(2);
  });
});
