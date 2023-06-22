import { render } from '@testing-library/react';

import { mockGeolocation } from 'test-utils/mockGlobals';
import { GeolocationMode, useGeolocation } from 'hooks/useGeolocation';

const [latitude, longitude] = [32.972, -96.7914];
type MockComponentProps = {
  skip?: boolean;
  mode?: GeolocationMode;
  stop?: boolean;
  timeout?: number;
  maximumAge?: number;
  enableHighAccuracy?: boolean;
};

export function MockComponent(props: MockComponentProps) {
  const { position, positionError } = useGeolocation(props);
  return (
    <>
      {positionError ? <span data-testid="err" /> : null}
      <span data-testid="latitude">{position?.coords?.latitude}</span>
      <span data-testid="longitude">{position?.coords?.longitude}</span>
    </>
  );
}

describe('Utils - useGeolocation', () => {
  it('should return empty without any mocks', () => {
    const { getByTestId } = render(<MockComponent />);
    expect(getByTestId('latitude')).toBeEmptyDOMElement();
    expect(getByTestId('longitude')).toBeEmptyDOMElement();
  });

  it('should return empty when stop is enabled', () => {
    mockGeolocation(latitude, longitude);
    const { getByTestId } = render(<MockComponent stop />);
    expect(getByTestId('latitude')).toBeEmptyDOMElement();
    expect(getByTestId('longitude')).toBeEmptyDOMElement();
  });

  it('should return empty when skip is enabled', () => {
    mockGeolocation(latitude, longitude);
    const { getByTestId } = render(<MockComponent skip />);
    expect(getByTestId('latitude')).toBeEmptyDOMElement();
    expect(getByTestId('longitude')).toBeEmptyDOMElement();
  });

  it('should return coords with mocks', () => {
    mockGeolocation(latitude, longitude);
    const { getByTestId } = render(<MockComponent />);
    expect(getByTestId('latitude')).toHaveTextContent(latitude.toString());
    expect(getByTestId('longitude')).toHaveTextContent(longitude.toString());
  });

  it('should return coords with mocks on WATCH mode', () => {
    mockGeolocation(latitude, longitude);
    const { getByTestId } = render(
      <MockComponent mode={GeolocationMode.WATCH} />
    );
    expect(getByTestId('latitude')).toHaveTextContent(latitude.toString());
    expect(getByTestId('longitude')).toHaveTextContent(longitude.toString());
  });

  it('should return empty with errors', () => {
    mockGeolocation(NaN, NaN, true);
    const { getByTestId } = render(<MockComponent />);
    expect(getByTestId('err')).toBeInTheDocument();
    expect(getByTestId('latitude')).toHaveTextContent('NaN');
    expect(getByTestId('longitude')).toHaveTextContent('NaN');
  });

  it('should return empty with errors on WATCH mode', () => {
    mockGeolocation(NaN, NaN, true);
    const { getByTestId } = render(
      <MockComponent mode={GeolocationMode.WATCH} />
    );
    expect(getByTestId('err')).toBeInTheDocument();
    expect(getByTestId('latitude')).toHaveTextContent('NaN');
    expect(getByTestId('longitude')).toHaveTextContent('NaN');
  });
});
