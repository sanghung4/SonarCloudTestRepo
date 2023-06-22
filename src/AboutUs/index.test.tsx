import { waitFor } from '@testing-library/react';
import AboutUs from 'AboutUs';
import { mockNullResponse, success } from 'AboutUs/tests/mocks';
import { setBreakpoint } from 'test-utils/mockMediaQuery';
import { render } from 'test-utils/TestWrapper';
import { Configuration } from 'utils/configuration';

describe('AboutUs', () => {
  it('Should match snapshop on desktop', () => {
    setBreakpoint('desktop');
    const { container } = render(<AboutUs />);
    expect(container).toMatchSnapshot();
  });
  it('Should match snapshop on mobile', () => {
    setBreakpoint('mobile');
    const { container } = render(<AboutUs />);
    expect(container).toMatchSnapshot();
  });

  it('Should render aboutus page on desktop with contentful data', async () => {
    setBreakpoint('desktop');
    Configuration.contentfulAboutUsId = '13j7MQQu4r6gSXv6KLiTYX';
    Configuration.contentfulPreviewEnable = false;
    const { getByTestId } = render(<AboutUs />, {
      mocks: [success]
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    expect(getByTestId('main-background')).toBeInTheDocument();
  });

  it('Should render aboutus page on mobile with contentful data', async () => {
    setBreakpoint('mobile');
    Configuration.contentfulAboutUsId = '13j7MQQu4r6gSXv6KLiTYX';
    Configuration.contentfulPreviewEnable = false;
    const { getByTestId } = render(<AboutUs />, {
      mocks: [success]
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    expect(getByTestId('main-background')).toBeInTheDocument();
  });

  it('Should render error message on desktop without contentful data', async () => {
    setBreakpoint('desktop');
    Configuration.contentfulAboutUsId = '13j7MQQu4r6gSXv6KLiTYX';
    Configuration.contentfulPreviewEnable = true;
    const { getByTestId } = render(<AboutUs />, {
      mocks: [success]
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    expect(getByTestId('error-message')).toBeInTheDocument();
  });

  it('Should render error message on mobile with contentful data', async () => {
    setBreakpoint('mobile');
    Configuration.contentfulAboutUsId = 'A13j7MQQu4r6gSXv6KLiTYX';
    Configuration.contentfulPreviewEnable = false;
    const { getByTestId } = render(<AboutUs />, {
      mocks: [mockNullResponse]
    });
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));

    expect(getByTestId('error-message')).toBeInTheDocument();
  });
});
