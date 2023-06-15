import React, { JSXElementConstructor, ReactElement, ReactNode } from "react";
import { MockedProvider, MockedResponse } from "@apollo/client/testing";
import { MemoryRouter } from "react-router-dom";
import { render as jestRender } from "@testing-library/react";

export const mockApollo = jest.mock(
  "@apollo/client/testing",
  () => MockedProvider
);

interface Props {
  children?: ReactNode;
  mocks?: ReadonlyArray<MockedResponse>;
}

function TestWrapper(props: Props) {
  return (
    <MockedProvider mocks={props.mocks ?? []}>{props.children}</MockedProvider>
  );
}

export function render(component: ReactNode, config?: Props) {
  const wrapped = <TestWrapper {...config}>{component}</TestWrapper>;
  return jestRender(wrapped, {
    wrapper: MemoryRouter as
      | JSXElementConstructor<{
          children: ReactElement<any, string | JSXElementConstructor<any>>;
        }>
      | undefined,
  });
}
