import { fireEvent } from '@testing-library/react';
import { mockTestData } from 'common/TablePageLayout/tests/TableRenderer.mocks';
import {
  TableBodyRowProp,
  TableRendererBodyRow
} from 'common/TablePageLayout/util';
import {
  mockCell,
  mockRow,
  mockTableInstance
} from 'test-utils/mockTableInstance';
import { render } from 'test-utils/TestWrapper';

const defaultMockRow = mockRow(mockTestData[9], mockTestData[9], {});
const defaultCells = mockTestData.map((item) => {
  const cell = mockCell(item, 'id', {});
  cell.isGrouped = true;
  return cell;
});
const defaultSubRows = mockTestData.map((item) => mockRow(item, item, {}));

const mockProps: TableBodyRowProp = {
  tableInstance: mockTableInstance,
  testId: 'test',
  primaryKey: 'key',
  row: defaultMockRow,
  rowIndex: 9
};

describe('common - TablePageLayout - util - TableRendererBodyRow', () => {
  afterEach(() => {
    mockProps.row = defaultMockRow;
  });
  it('Expect to match snapshot as ungrouped', () => {
    const { container } = render(<TableRendererBodyRow {...mockProps} />);
    expect(container).toMatchSnapshot();
  });
  it('Expect to match snapshot as grouped but no subrows', () => {
    mockProps.row.isGrouped = true;
    const { container } = render(<TableRendererBodyRow {...mockProps} />);
    expect(container).toMatchSnapshot();
  });
  it('Expect to match snapshot with only cells', () => {
    mockProps.row.cells = defaultCells;
    mockProps.row.isGrouped = false;
    mockProps.hasGroups = true;
    const { container } = render(<TableRendererBodyRow {...mockProps} />);
    expect(container).toMatchSnapshot();
    delete mockProps.hasGroups;
  });
  it('Expect to match snapshot as grouped with subRows', () => {
    mockProps.row.isGrouped = true;
    mockProps.row.subRows = defaultSubRows;
    mockProps.row.cells = defaultCells;
    const { container } = render(<TableRendererBodyRow {...mockProps} />);
    expect(container).toMatchSnapshot();
  });
  it('Expect to match snapshot as grouped with subRows while expanded', () => {
    mockProps.row.isGrouped = true;
    mockProps.row.isExpanded = true;
    mockProps.row.cells = defaultCells;
    mockProps.row.subRows = defaultSubRows;
    const { container } = render(<TableRendererBodyRow {...mockProps} />);
    expect(container).toMatchSnapshot();
  });
  it('Expect to match snapshot with a cell that is not grouped', () => {
    mockProps.row.isGrouped = true;
    mockProps.row.isExpanded = true;
    mockProps.row.cells = defaultCells;
    mockProps.row.subRows = defaultSubRows;
    mockProps.row.cells[0].isGrouped = false;
    const { container } = render(<TableRendererBodyRow {...mockProps} />);
    expect(container).toMatchSnapshot();
  });
  it('Expect to match snapshot with a cell that is not grouped and is aggregated', () => {
    mockProps.row.isGrouped = true;
    mockProps.row.isExpanded = true;
    mockProps.row.cells = defaultCells;
    mockProps.row.subRows = defaultSubRows;
    mockProps.row.cells[0].isGrouped = false;
    mockProps.row.cells[0].isAggregated = true;
    const { container } = render(<TableRendererBodyRow {...mockProps} />);
    expect(container).toMatchSnapshot();
  });
  it('Expect to call onRowClick when it is valid', () => {
    const onRowClick = jest.fn();
    mockProps.row.values = { [mockProps.primaryKey]: 'test' };
    const { getByTestId } = render(
      <TableRendererBodyRow {...mockProps} onRowClick={onRowClick} />
    );
    fireEvent.click(getByTestId('row_test'));
    expect(onRowClick).toBeCalled();
  });
});
