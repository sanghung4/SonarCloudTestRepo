import { Box } from '@dialexa/reece-component-library';
import {
  tableCellSx,
  tableHeaderCellGridAlign
} from 'common/TablePageLayout/util';
import { render } from 'test-utils/TestWrapper';

describe('common - TablePageLayout - util - styles', () => {
  // tableHeaderCellGridAlign
  it('expect `tableHeaderCellGridAlign` to return "flex-start" as "center"', () => {
    const result = tableHeaderCellGridAlign('center');
    expect(result).toBe('flex-start');
  });
  it('expect `tableHeaderCellGridAlign` to return "flex-start" as undefined', () => {
    const result = tableHeaderCellGridAlign();
    expect(result).toBe('flex-start');
  });
  it('expect `tableHeaderCellGridAlign` to return "flex-end" as "right', () => {
    const result = tableHeaderCellGridAlign('right');
    expect(result).toBe('flex-end');
  });

  // tableCellSx
  it('expect `tableCellSx` to handle sx when all properties are false/undefined', () => {
    const sx = tableCellSx(false, false);
    const { container } = render(<Box sx={sx} />);
    expect(container).toMatchSnapshot();
  });
  it('expect `tableCellSx` to handle sx when all properties are true/etc', () => {
    const sx = tableCellSx(true, true, jest.fn());
    const { container } = render(<Box sx={sx} />);
    expect(container).toMatchSnapshot();
  });
});
