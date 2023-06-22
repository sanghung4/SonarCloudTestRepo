import { act, fireEvent } from '@testing-library/react';

import CompanyList from 'common/Header/CompanyList';
import { Configuration } from 'utils/configuration';
import { render } from 'test-utils/TestWrapper';
import { setBreakpoint } from 'test-utils/mockMediaQuery';

const mockHistory = { push: jest.fn() };
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useHistory: () => mockHistory
}));

describe('common - Header - CompanyList', () => {
  afterEach(() => {
    mockHistory.push = jest.fn();
  });

  it('Should match snapshot on desktop', async () => {
    setBreakpoint('desktop');
    const { container } = render(<CompanyList />);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('Should match snapshot on mobile', async () => {
    setBreakpoint('mobile');
    const { container } = render(<CompanyList />);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(container).toMatchSnapshot();
  });

  it('Should link based off the brand', async () => {
    setBreakpoint('desktop');

    const { getByText, getByTestId } = render(<CompanyList />);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    const companyButton = getByText('Reece Companies');
    fireEvent.click(companyButton);
    const morrisonsupplyLink = getByTestId('morrisonsupply-link');
    await act(() => new Promise((res) => setTimeout(res, 0)));

    expect(morrisonsupplyLink.getAttribute('href')).toEqual(
      'https://morrisonsupply.test.ecomm.reecedev.us'
    );
  });

  it('Should have the correct link based on environment', async () => {
    setBreakpoint('desktop');
    Configuration.environment = 'production';

    const { getByText, getByTestId } = render(<CompanyList />);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    const companyButton = getByText('Reece Companies');
    fireEvent.click(companyButton);
    const morrisonsupplyLink = getByTestId('morrisonsupply-link');
    await act(() => new Promise((res) => setTimeout(res, 0)));
    expect(morrisonsupplyLink.getAttribute('href')).toEqual(
      'https://morrisonsupply.reece.com'
    );
  });

  it('Expect Support Button to call to move to support page', async () => {
    setBreakpoint('desktop');
    const { getByTestId } = render(<CompanyList />);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    fireEvent.click(getByTestId('support-button'));
    expect(mockHistory.push).toBeCalledWith('/support');
  });

  it('Expect Support Button to call to move to location search page', async () => {
    setBreakpoint('desktop');
    const { getByTestId } = render(<CompanyList />);
    await act(() => new Promise((res) => setTimeout(res, 0)));
    fireEvent.click(getByTestId('location-search-button'));
    expect(mockHistory.push).toBeCalledWith('/location-search');
  });
});
