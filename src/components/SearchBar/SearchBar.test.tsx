import React from 'react';
import { render } from 'test-utils/render';
import SearchBar from './SearchBar';
import * as RNE from 'react-native-elements';
import { getComponentTestingIds } from 'test-utils/testIds';

const mockProps = (props?: Partial<RNE.SearchBarProps>) => ({
  ...props,
});

const testIds = getComponentTestingIds('SearchBar');

describe('<SearchBar>', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('renders SearchBar Component', () => {
    const utils = render(<SearchBar {...mockProps()} />);
    expect(utils.getByTestId(testIds.component)).toBeTruthy();
  });
});
