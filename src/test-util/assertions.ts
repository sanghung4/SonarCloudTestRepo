import { screen } from '@testing-library/react';

type ExpectTestIdType = {
  id: string;
  expect: string;
};

export function expectTestIdsTexts(config: ExpectTestIdType[]) {
  config.forEach((item) => {
    const element = screen.queryByTestId(item.id);
    if (!element) {
      console.error(`❌ TEST ERROR - TESTID "${item.id}" NOT FOUND!`);
    }
    expect(element).toHaveTextContent(item.expect);
  });
}

/**
 * assert the number of rows rendered to the table (excluding header)
 * @param testId - use the testid that was applied to the <Table> component
 * @param expectedCount - the number of rows expected
 */
export function expectRowCount(testId: string, expectedCount: number) {
  const pattern = new RegExp(`((${testId})+-tr-[0-9]+)`);
  const rows = screen.queryAllByTestId<HTMLTableRowElement>(pattern);
  expect(rows.length).toBe(expectedCount);
}
