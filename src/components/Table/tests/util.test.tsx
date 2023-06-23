import { render, screen } from '@testing-library/react';

import {
  DefaultCell,
  defaultHeader,
  paginationInputSize,
  paginationInputSizeConfig
} from 'components/Table';

/**
 * TEST
 */
describe('components/Table/util', () => {
  // 🟢 1 - defaultHeader
  it('Expect to render `defaultHeader()`', () => {
    // act
    render(defaultHeader()('test'));
    const cell = screen.queryByText('test');
    // assert
    expect(cell).toBeInTheDocument();
  });

  // 🟢 2 - DefaultCell
  it('Expect to render <DefaultCell />', () => {
    // act
    render(<DefaultCell>test</DefaultCell>);
    const cell = screen.queryByText('test');
    // assert
    expect(cell).toBeInTheDocument();
  });

  // 🟢 3 - paginationInputSize - 0
  it('Expect `paginationInputSize` to match when 0', () => {
    // arrange
    const { gap, offset } = paginationInputSizeConfig;
    // act
    const res = paginationInputSize(0);
    // assert
    expect(res).toBe(gap + offset);
  });

  // 🟢 4 - paginationInputSize - 4
  it('Expect `paginationInputSize` to match when 4', () => {
    // arrange
    const { gap, offset } = paginationInputSizeConfig;
    // act
    const res = paginationInputSize(4);
    // assert
    expect(res).toBe(4 * gap + offset);
  });

  // 🟢 5 - paginationInputSize - NaN
  it('Expect `paginationInputSize` to match when NaN', () => {
    // arrange
    const { gap, offset } = paginationInputSizeConfig;
    // act
    const res = paginationInputSize(NaN);
    // assert
    expect(res).toBe(gap + offset);
  });
});
