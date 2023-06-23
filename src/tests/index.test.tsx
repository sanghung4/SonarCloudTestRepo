import { createRoot } from 'react-dom/client';
import { main } from 'index';

/**
 * Types
 */
type Mocks = {
  render: jest.Mock;
  element: HTMLElement | null;
};

/**
 * Mocks
 */
const defaultMocks: Mocks = {
  render: jest.fn(),
  element: null
};
let mocks = { ...defaultMocks };

/**
 * Mock methods
 */
jest.mock('react-dom/client', () => ({
  ...jest.requireActual('react-dom/client'),
  createRoot: jest.fn()
}));

/**
 * TEST
 */
describe('ROOT', () => {
  // ⚪ Reset mocks
  afterEach(() => {
    mocks = { ...defaultMocks };
  });

  // 🔵 Mock methods
  beforeEach(() => {
    // 🔹 React.createRoot function
    (createRoot as jest.Mock).mockImplementation(() => ({
      render: mocks.render
    }));
    // 🔹 document.getElementById function
    jest
      .spyOn(document, 'getElementById')
      .mockImplementation(() => mocks.element);
  });

  // 🟢 1 - null element
  it('Expect `render` not to be called when element is not found', () => {
    main();
    expect(mocks.render).not.toBeCalled();
  });

  // 🟢 2 - has element
  it('Expect `render` to be called when element is found', () => {
    mocks.element = (<div />) as unknown as HTMLElement;
    main();
    expect(mocks.render).toBeCalled();
  });
});
