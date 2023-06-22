import { act, fireEvent, waitFor } from '@testing-library/react';

import ContractHeaderDesktop from 'Contract/HeaderDesktop';
import ContractHeaderMobile from 'Contract/HeaderMobile';
import { GetContractDetailsMock } from 'Contract/tests/mocks';
import { ContractDetails } from 'generated/graphql';
import { render } from 'test-utils/TestWrapper';

describe('Contract Details - Header', () => {
  const data = GetContractDetailsMock.result.data.contract;
  const emptyData = {} as ContractDetails;

  it('Desktop snapshot match', () => {
    const { container } = render(<ContractHeaderDesktop data={data} />);
    expect(container).toMatchSnapshot();
  });

  it('Desktop snapshot match for blank data', () => {
    const { container } = render(<ContractHeaderDesktop data={emptyData} />);
    expect(container).toMatchSnapshot();
  });

  it('Desktop open/collapse', async () => {
    const { getByTestId } = render(<ContractHeaderDesktop data={data} />);

    const clickToggle = () => {
      fireEvent.click(getByTestId('contract-header-details-toggle-desktop'));
    };

    const detailsContent = getByTestId('contract-header-details-box-desktop');
    expect(detailsContent).toBeInTheDocument();

    act(clickToggle);
    await waitFor(() => new Promise((res) => setTimeout(res, 300)));
    expect(detailsContent).not.toBeVisible();

    act(clickToggle);
    expect(detailsContent).toBeVisible();
  });

  it('Mobile snapshot match', () => {
    const { container } = render(<ContractHeaderMobile data={data} />);
    expect(container).toMatchSnapshot();
  });

  it('Mobile snapshot match for blank data', () => {
    const { container } = render(<ContractHeaderMobile data={emptyData} />);
    expect(container).toMatchSnapshot();
  });

  it('Header Mobile open/collapse', async () => {
    const { getByTestId } = render(<ContractHeaderMobile data={data} />);

    const clickToggle = () => {
      fireEvent.click(getByTestId('contract-header-details-toggle-mobile'));
    };

    const detailsContent = getByTestId('contract-header-details-box-mobile');
    expect(detailsContent).toBeInTheDocument();

    act(clickToggle);
    await waitFor(() => new Promise((res) => setTimeout(res, 300)));
    expect(detailsContent).not.toBeVisible();

    act(clickToggle);
    expect(detailsContent).toBeVisible();
  });

  it('Mobile more details open/collapse', async () => {
    const { getByTestId } = render(<ContractHeaderMobile data={data} />);

    const clickToggle = () => {
      fireEvent.click(getByTestId('contract-header-moredetails-toggle-mobile'));
    };
    const simpleContent = getByTestId(
      'contract-header-moredetails-box-simple-mobile'
    );
    const detailsContent = getByTestId(
      'contract-header-moredetails-box-more-mobile'
    );
    expect(simpleContent).toBeInTheDocument();
    expect(detailsContent).toBeInTheDocument();

    act(clickToggle);
    await waitFor(() => new Promise((res) => setTimeout(res, 300)));
    expect(simpleContent).not.toBeVisible();
    expect(detailsContent).toBeVisible();

    act(clickToggle);
    await waitFor(() => new Promise((res) => setTimeout(res, 300)));
    expect(simpleContent).toBeVisible();
    expect(detailsContent).not.toBeVisible();
  });
});
