import { act, fireEvent } from '@testing-library/react';

import { mockCartContext } from 'Cart/tests/mocks';
import {
  ContractsProviderMocks,
  mockReviewRelease
} from 'Contract/tests/mocks';
import { MockReviewReleaseContext } from 'Contract/tests/mockComponents';
import { render } from 'test-utils/TestWrapper';
import { mockUseDomainInfoReturn } from 'hooks/tests/mocks/useDomainInfo.mocks';
import { useDomainInfo } from 'hooks/useDomainInfo';
import { valueOfReleasingContract } from 'Contract/util';
import { CartContext, CartContextType } from 'providers/CartProvider';

const mockProvider = { ...ContractsProviderMocks };
const mockCart: CartContextType = { ...mockCartContext };
let mockDomainInfo: ReturnType<typeof useDomainInfo> = {
  ...mockUseDomainInfoReturn
};

jest.mock('hooks/useDomainInfo', () => {
  return {
    ...jest.requireActual('hooks/useDomainInfo'),
    useDomainInfo: () => mockDomainInfo
  };
});

jest.mock('Contract/util', () => {
  return {
    ...jest.requireActual('Contract/util'),
    valueOfReleasingContract: jest.fn()
  };
});

describe('Contract - Review Release', () => {
  afterEach(() => {
    mockDomainInfo = { ...mockUseDomainInfoReturn };
  });

  it('Should match snapshot when it is not open', () => {
    mockProvider.qtyInputMap = {};
    const wrapper = render(
      <MockReviewReleaseContext products={[]} context={mockProvider} />
    );
    expect(wrapper).toMatchSnapshot();
  });

  it('Should match snapshot when it is open with no products', () => {
    mockProvider.qtyInputMap = {};
    const wrapper = render(
      <MockReviewReleaseContext
        isReviewReady
        products={[]}
        context={mockProvider}
      />
    );
    expect(wrapper).toMatchSnapshot();
  });

  it('Should match snapshot before & after closing', async () => {
    mockProvider.qtyInputMap = {};
    const wrapper = render(
      <MockReviewReleaseContext
        isReviewReady
        products={[]}
        context={mockProvider}
      />
    );
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(wrapper).toMatchSnapshot();
    const closeButton = wrapper.getByTestId('slider-close');
    fireEvent.click(closeButton);
    await act(() => new Promise((res) => setTimeout(res, 100)));
    expect(wrapper).toMatchSnapshot();
  });

  it('Expect the handleClose and handleReleaseOver10mil functions to be called when button is clicked and value over 10mil', async () => {
    mockProvider.qtyInputMap = mockReviewRelease.map;

    mockDomainInfo.isWaterworks = true;
    valueOfReleasingContract.mockReturnValue(99999999);

    const wrapper = render(
      <MockReviewReleaseContext
        isReviewReady
        products={mockReviewRelease.items}
        context={mockProvider}
      />,
      { cartConfig: mockCart }
    );
    await act(() => new Promise((res) => setTimeout(res, 0)));
    const releaseButton = wrapper.getByTestId('review-release-button');
    fireEvent.click(releaseButton);
    await act(() => new Promise((res) => setTimeout(res, 100)));
    expect(mockProvider.handleReleaseOver10mil).toBeCalled();
  });

  it('Expect the release functions to be called when the button is clicked', async () => {
    mockProvider.qtyInputMap = mockReviewRelease.map;
    const wrapper = render(
      <MockReviewReleaseContext
        isReviewReady
        products={mockReviewRelease.items}
        context={mockProvider}
      />,
      { cartConfig: mockCart }
    );
    await act(() => new Promise((res) => setTimeout(res, 0)));
    const releaseButton = wrapper.getByTestId('review-release-button');
    fireEvent.click(releaseButton);
    await act(() => new Promise((res) => setTimeout(res, 100)));
    expect(mockProvider.goToCart).toBeCalled();
  });
});
