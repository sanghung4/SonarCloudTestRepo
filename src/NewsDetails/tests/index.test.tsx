import { act, fireEvent, waitFor } from '@testing-library/react';
import NewsDetails, { NewsRouterState } from 'NewsDetails';
import { success } from 'NewsDetails/tests/mocks';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

const mockHistory = {
  push: jest.fn()
};

const params = {
  id: '26DWkCGphEQfhtn1BrPBPK'
};
const location: { state: NewsRouterState } = {
  state: {}
};

jest.mock('react-router-dom', () => ({
  useParams: () => params,
  useLocation: () => location,
  useHistory: () => mockHistory
}));

describe('NewsDetails', () => {
  it('Should render loader when loading contentful data', () => {
    setBreakpoint('desktop');
    const { getByTestId } = render(<NewsDetails />);
    expect(getByTestId('loader-component')).toBeInTheDocument();
  });

  it('Should render News Details page on desktop with contentful data', async () => {
    setBreakpoint('desktop');
    const { getByTestId } = render(<NewsDetails />, {
      mocks: [success]
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    expect(getByTestId('news-details')).toBeInTheDocument();
    expect(getByTestId('go-back-button')).toBeInTheDocument();
    expect(getByTestId('news-images-slider')).toBeInTheDocument();
    expect(getByTestId('follow-us-on-social-box')).toBeInTheDocument();
  });

  it('Should render News Details on mobile with contentful data', async () => {
    setBreakpoint('mobile');
    const { getByTestId } = render(<NewsDetails />, {
      mocks: [success]
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    expect(getByTestId('news-details')).toBeInTheDocument();
    expect(getByTestId('go-back-button')).toBeInTheDocument();
    expect(getByTestId('follow-us-on-social-box')).toBeInTheDocument();
  });
});
