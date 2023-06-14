import React from 'react';
import { render, RenderAPI } from '@testing-library/react-native';
import { MockedProvider, MockedResponse } from '@apollo/react-testing';
import { ThemeProvider } from 'react-native-elements';
import OverlayProvider from 'providers/Overlay';

const WrapperComponent = (props: {
  children: React.ReactNode;
  mocks: ReadonlyArray<MockedResponse>;
}) => {
  return (
    <MockedProvider mocks={props.mocks} addTypename={false}>
      <OverlayProvider>
        <ThemeProvider>{props.children}</ThemeProvider>
      </OverlayProvider>
    </MockedProvider>
  );
};

const customRender = (
  ui: React.ReactElement<any>,
  mocks: MockedResponse[] = []
): RenderAPI =>
  render(ui, {
    wrapper: (props) => <WrapperComponent mocks={mocks} {...props} />,
  });

// re-export everything
export * from '@testing-library/react-native';

// override render method
export { customRender as render };
