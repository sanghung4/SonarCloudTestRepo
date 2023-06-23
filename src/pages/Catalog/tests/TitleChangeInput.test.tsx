import { screen } from '@testing-library/react';

import { useApiPostCatalogTitle } from 'api/catalog.api';
import TitleChangeInput, {
  TitleChangeInputProps
} from 'pages/Catalog/TitleChangeInput';
import { fireEvent, render } from 'test-util';

/**
 * Types
 */
type Mocks = {
  setFieldValue: jest.Mock;
  titleChange: jest.Mock;
  props: TitleChangeInputProps;
};

/**
 * Mocks
 */
const defaultMocks: Mocks = {
  setFieldValue: jest.fn(),
  titleChange: jest.fn(),
  props: { updateCatalogTitle: jest.fn(), catalogTitle: '', id: '' }
};
let mocks: Mocks = { ...defaultMocks };

/**
 * Mock Methods
 */
jest.mock('api/catalog.api', () => ({
  ...jest.requireActual('api/catalog.api'),
  useApiPostCatalogTitle: jest.fn()
}));

/**
 * Setup
 */
function setup(props: TitleChangeInputProps) {
  render(<TitleChangeInput {...props} />);
}

/**
 * TEST
 */
describe('components/TitleChangeInput', () => {
  // ⚪ Reset mocks
  afterEach(() => {
    mocks = { ...defaultMocks };
  });

  // 🔵 Mock API functions
  beforeEach(() => {
    (useApiPostCatalogTitle as jest.Mock).mockImplementation(() => ({
      call: mocks.titleChange
    }));
  });

  // 🟢 1 - Click edit icon to edit
  it('Expect an input to appear when the edit button is clicked', async () => {
    // arrange
    mocks.props.catalogTitle = 'moo';
    mocks.props.id = '875b7e90-fa26-4e21-95e7-0ae696d14758';
    // act
    setup(mocks.props);
    await fireEvent('click', 'catalog-edit');
    // assert
    expect(screen.getByTestId('catalog-name-input')).toBeInTheDocument();
  });

  // 🟢 2 - Click h3 to edit
  it('Expect an input to appear when the h3 div is clicked', async () => {
    // arrange
    mocks.props.id = '875b7e90-fa26-4e21-95e7-0ae696d14758';
    // act
    setup(mocks.props);
    await fireEvent('click', 'catalog-edit');
    await fireEvent('click', 'catalog-name-input');
    await fireEvent('change', 'catalog-name-input', {
      target: { value: 'New Catalog Name' }
    });
    await fireEvent('click', 'catalog-edit');
    // assert
    expect(mocks.props.updateCatalogTitle).toBeCalled();
    expect(screen.getByTestId('catalog-name')).toBeInTheDocument();
  });

  // 🟢 3 - validation error message
  it('Expect a validation error message to be displayed for invalid input', async () => {
    // arrange
    mocks.props.id = '875b7e90-fa26-4e21-95e7-0ae696d14758';
    // act
    setup(mocks.props);
    await fireEvent('click', 'catalog-name');
    await fireEvent('click', 'catalog-name-input');
    await fireEvent('change', 'catalog-name-input', {
      target: { value: 'x' } // This value is invalid
    });
    await fireEvent('blur', 'catalog-edit');
    await fireEvent('blur', 'catalog-name-input');
    const helperTextElement = screen.getByTestId(
      'catalog-name-input-helper-text'
    );
    // assert
    expect(helperTextElement).toHaveTextContent(
      'Catalog name must contain more than one character'
    );
    expect(helperTextElement).toHaveClass('text-red-500');
  });

  // 🟢 4 - Update title call
  it('Expects non-error side of edit click event to be reached by this test', async () => {
    // arrange
    mocks.props.catalogTitle = 'N/A';
    const newValue = 'xsxsxsxxssxx';
    // act
    setup(mocks.props);
    await fireEvent('click', 'catalog-name');
    await fireEvent('click', 'catalog-name-input');
    await fireEvent('change', 'catalog-name-input', {
      target: { value: newValue }
    });
    await fireEvent('click', 'catalog-edit');
    // assert
    expect(mocks.props.updateCatalogTitle).toHaveBeenCalledWith(newValue);
  });

  // 🟢 5 - Update title not to be called on Error
  it('Expects error side of edit click event to be reached by this test', async () => {
    // arrange
    mocks.props.catalogTitle = 'N/A';
    // act
    setup(mocks.props);
    await fireEvent('click', 'catalog-name');
    await fireEvent('click', 'catalog-name-input');
    await fireEvent('change', 'catalog-name-input', {
      target: { value: 'x' }
    });
    await fireEvent('click', 'catalog-edit');
    // assert
    expect(mocks.props.updateCatalogTitle).not.toHaveBeenCalled();
  });
});
