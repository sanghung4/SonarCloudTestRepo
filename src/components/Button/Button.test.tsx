import React from 'react';
import { render } from 'test-utils/render';
import { ThemeProvider } from 'react-native-elements';
import Button from './Button';

import { ButtonProps, TEST_IDS } from './types';

const TEST_TEXT = {
  TITLE: 'Test Title',
};

const mockProps = (props?: Partial<ButtonProps>) => ({
  title: TEST_TEXT.TITLE,
  ...props,
});

describe('<Button>', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('renders', () => {
    const utils = render(<Button {...mockProps()} />);
    const buttonByText = utils.getByText(TEST_TEXT.TITLE);

    expect(buttonByText).toBeTruthy();
  });
  it('renders secondary', () => {
    const utils = render(
      <Button {...mockProps({ color: 'secondary', type: 'clear' })} />
    );
    const buttonByID = utils.getByTestId(TEST_IDS.BUTTON_DEFAULT);

    expect(buttonByID).toBeTruthy();
  });
  it('renders with different primary theme', () => {
    const utils = render(
      <ThemeProvider theme={{ colors: { primary: null } }}>
        <Button {...mockProps()} />
      </ThemeProvider>
    );
    const buttonByID = utils.getByTestId(TEST_IDS.BUTTON_DEFAULT);
    expect(buttonByID).toBeTruthy();
  });
  it('renders with different secondary theme', () => {
    const utils = render(
      <ThemeProvider theme={{ colors: { secondary: null } }}>
        <Button {...mockProps({ color: 'secondary' })} />
      </ThemeProvider>
    );
    const buttonByID = utils.getByTestId(TEST_IDS.BUTTON_DEFAULT);
    expect(buttonByID).toBeTruthy();
  });
});
