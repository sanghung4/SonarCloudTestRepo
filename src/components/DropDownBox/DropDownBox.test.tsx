import React from 'react';
import { Text, View } from 'react-native';
import { render, fireEvent } from 'test-utils/render';
import { getComponentTestingIds } from 'test-utils/testIds';
import DropDownBox, { DropDownBoxProps } from './DropDownBox';

const mockProps = (props?: Partial<DropDownBoxProps>) => ({
  title: 'test box',
  children: (
    <View>
      <Text>test</Text>
    </View>
  ),
  ...props,
});

const testIds = getComponentTestingIds('DropDownBox');

describe('<DropDownBox>', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('renders DropDownBox Component', () => {
    const utils = render(<DropDownBox {...mockProps()} />);
    expect(utils.getByTestId(testIds.component)).toBeTruthy();
  });

  it('toggles showChildren correctly', () => {
    const utils = render(<DropDownBox {...mockProps()} />);
    const buttonElem = utils.getByTestId(testIds.openToggle);
    fireEvent.press(buttonElem);
    const children = utils.getByTestId(testIds.childrenContainer);
    expect(children).toBeTruthy();
  });
});
