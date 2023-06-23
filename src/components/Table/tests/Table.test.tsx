import { render, screen } from '@testing-library/react';

import { Table } from 'components/Table';
import mocks, { mockTableData } from 'components/Table/tests/table.mocks';
import { expectRowCount } from 'test-util';

/**
 * TEST
 */
describe('components/Table', () => {
  // 🟢 1 - Match rows
  it('Expect to match rows count when data rendered to the table', () => {
    // act
    render(<Table table={mocks.full} data-testid="table" />);
    // assert
    expectRowCount('table', mockTableData.length);
  });

  // 🟢 2 - Loading
  it('Expect to render the loading circle when table is loading', () => {
    // act
    render(<Table table={mocks.full} data-testid="table" loading />);
    const loadingCircle = screen.queryByTestId('table-loading');
    // assert
    expectRowCount('table', 0);
    expect(loadingCircle).toBeInTheDocument();
  });

  // 🟢 3 - No Results
  it('Expect to render "empty" when table data is empty', () => {
    // act
    render(
      <Table table={mocks.empty} data-testid="table" noResultsMessage="empty" />
    );
    const loadingCircle = screen.queryByTestId('table-no-results');
    // assert
    expectRowCount('table', 0);
    expect(loadingCircle).toHaveTextContent('empty');
  });

  // 🟢 4 - Pagination
  it('Expect pagination to be rendered to the table', () => {
    // act
    render(
      <Table
        table={mocks.full}
        data-testid="table"
        showPagination
        currentPage={1}
        pages={10}
      />
    );
    const pagination = screen.queryByTestId('table-pagination');
    // assert
    expect(pagination).toBeInTheDocument();
  });

  // 🟢 5 - Item count
  it('Expect itemCount to be rendered to the table', () => {
    // act
    render(<Table table={mocks.full} data-testid="table" showItemCount />);
    const count = screen.queryByTestId('table-count');
    // assert
    expect(count).toBeInTheDocument();
  });
});
