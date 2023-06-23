import { useMemo } from 'react';

import { useApiCustomerList } from 'api/customer.api';
import { Table } from 'components/Table';
import { customerListTableConfig } from 'pages/Home/tableConfig';

/**
 * Component
 */
function Home() {
  /**
   * Data
   */
  const { data, loading } = useApiCustomerList({});

  /**
   * Memo
   */
  const tableInstance = useMemo(
    () => customerListTableConfig(data?.customers),
    [data?.customers]
  );

  /**
   * Render
   */
  return (
    <div className="bg-common-background h-screen" data-testid="home_container">
      <div className="bg-white w-full py-6 text-center shadow-md">
        <span
          className="text-primary-1-100 font-semibold text-4xl mb-8"
          data-testid="home_header"
        >
          Existing Punchout Customers
        </span>
      </div>
      <div>
        <Table
          className="w-[calc(100%-80px)] my-6 mx-10"
          noResultsMessage="No Customers Found!"
          loading={loading}
          table={tableInstance}
          data-testid="home_table"
        />
      </div>
    </div>
  );
}

export default Home;
