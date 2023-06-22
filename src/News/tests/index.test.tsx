import { act, fireEvent, waitFor } from '@testing-library/react';
import News from 'News';
import { success } from 'News/tests/mocks';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';

describe('News', () => {
  it('Should render loader when loading contentful data', () => {
    setBreakpoint('desktop');
    const { getByTestId } = render(<News />);
    expect(getByTestId('loader-component')).toBeInTheDocument();
  });

  it('Should render News page on desktop with contentful data', async () => {
    setBreakpoint('desktop');
    const { getByTestId } = render(<News />, {
      mocks: [success]
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    expect(getByTestId('news-0')).toBeInTheDocument();
    expect(getByTestId('learn-more-0')).toBeInTheDocument();
    expect(getByTestId('Twitter-Reece-Social-social-item')).toBeInTheDocument();
    expect(getByTestId('contact-us-button')).toBeInTheDocument();
  });

  it('Should render News page on mobile with contentful data', async () => {
    setBreakpoint('mobile');
    const { getByTestId } = render(<News />, {
      mocks: [success]
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    expect(getByTestId('news-0')).toBeInTheDocument();
    expect(getByTestId('Twitter-Reece-Social-social-item')).toBeInTheDocument();
    expect(getByTestId('contact-us-button')).toBeInTheDocument();
  });

  it('Should render next page with news when next page selected', async () => {
    setBreakpoint('desktop');
    const { getByTestId } = render(<News />, {
      mocks: [success]
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    const button = getByTestId('ChevronRightIcon');
    expect(button).toBeInTheDocument();

    fireEvent.click(button);
    await act(() => new Promise((res) => setTimeout(res, 0)));

    expect(getByTestId('news-6')).toBeInTheDocument();
  });
});
