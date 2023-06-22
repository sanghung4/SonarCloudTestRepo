import { AuthContext } from 'AuthProvider';
import { mockAuthContext } from 'AuthProvider.mocks';
import FeatureToggle from 'FeatureToggle';
import { mockFeatures } from 'FeatureToggle/mocks';
import { useToggleFeatures } from 'FeatureToggle/util';
import * as t from 'locales/en/translation.json';
import { ChangeEvent } from 'react';
import { render } from 'test-utils/TestWrapper';

/**
 * Mock values
 */
const mocks = {
  auth: { ...mockAuthContext },
  pushAlert: jest.fn()
};

/**
 * Mock methods
 */
jest.mock('@dialexa/reece-component-library', () => ({
  ...jest.requireActual('@dialexa/reece-component-library'),
  useSnackbar: () => ({
    pushAlert: mocks.pushAlert
  })
}));

/**
 * Setup test functions
 */
function setupIndex(p: typeof mocks) {
  return render(
    <AuthContext.Provider value={p.auth}>
      <FeatureToggle />
    </AuthContext.Provider>
  );
}
function setupHook(p: typeof mocks) {
  const result: ReturnType<typeof useToggleFeatures> = {
    setFeature: jest.fn()
  };
  function MockComponent() {
    Object.assign(result, useToggleFeatures());
    return null;
  }
  render(
    <AuthContext.Provider value={p.auth}>
      <MockComponent />
    </AuthContext.Provider>
  );
  return result;
}

/**
 * Tests
 */
describe('FeatureToggle', () => {
  afterEach(() => {
    mocks.auth = { ...mockAuthContext };
    mocks.pushAlert = jest.fn();
  });

  it('should match snapshot', () => {
    const { container } = setupIndex(mocks);
    expect(container).toMatchSnapshot();
  });
  it('should match snapshot with features', () => {
    mocks.auth.authState = { isAuthenticated: true };
    mocks.auth.features = [...mockFeatures];
    const { container } = setupIndex(mocks);
    expect(container).toMatchSnapshot();
  });

  it('expect to call functions from `useToggleFeatures`', () => {
    const setFeatureFn = jest.fn();
    mocks.auth.setFeature = setFeatureFn;
    const mockEvent = {
      target: { checked: true }
    } as ChangeEvent<HTMLInputElement>;
    const { setFeature } = setupHook(mocks);
    setFeature(mockFeatures[0])(mockEvent);
    expect(setFeatureFn).toBeCalledWith(mockFeatures[0].id, true);
    expect(mocks.pushAlert).toBeCalledWith(t.featureToggle.changeSaved, {
      variant: 'success'
    });
  });
});
