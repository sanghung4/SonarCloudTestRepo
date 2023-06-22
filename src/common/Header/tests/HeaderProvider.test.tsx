import { act, fireEvent } from '@testing-library/react';
import { render } from 'test-utils/TestWrapper';
import HeaderProvider, { HeaderContext } from '../HeaderProvider';

/******************************/
/* Testing Values             */
/******************************/
const testIds = {
  searchOpen: 'HeaderProvider-searchOpen',
  searchPage: 'HeaderProvider-searchPage',
  trackedSearchTerm: 'HeaderProvider-trackedSearchTerm',
  setSearchOpen: 'HeaderProvider-setSearchOpen',
  setSearchPage: 'HeaderProvider-setSearchPage',
  setTrackedSearchTerm: 'HeaderProvider-setTrackedSearchTerm'
};

const testValues = {
  open: true,
  page: 1,
  term: 'test'
};

/******************************/
/* Render Setup               */
/******************************/
const setup = () => {
  return render(
    <HeaderProvider>
      <HeaderContext.Consumer>
        {({
          searchOpen,
          setSearchOpen,
          searchPage,
          setSearchPage,
          trackedSearchTerm,
          setTrackedSearchTerm
        }) => (
          <div>
            <p data-testid={testIds.searchOpen}>{`${searchOpen}`}</p>
            <p data-testid={testIds.searchPage}>{searchPage}</p>
            <p data-testid={testIds.trackedSearchTerm}>{trackedSearchTerm}</p>
            <button
              data-testid={testIds.setSearchOpen}
              onClick={() => setSearchOpen(testValues.open)}
            >
              setSearchOpen
            </button>
            <button
              data-testid={testIds.setSearchPage}
              onClick={() => setSearchPage(testValues.page)}
            >
              setSearchPage
            </button>
            <button
              data-testid={testIds.setTrackedSearchTerm}
              onClick={() => setTrackedSearchTerm(testValues.term)}
            >
              setTrackedSearchTerm
            </button>
          </div>
        )}
      </HeaderContext.Consumer>
    </HeaderProvider>
  );
};

/******************************/
/* Testing                    */
/******************************/
describe('common - Header - HeaderProvider', () => {
  it('Should allow user to change values', async () => {
    const { getByTestId } = setup();

    const searchOpen = getByTestId(testIds.searchOpen);
    const searchPage = getByTestId(testIds.searchPage);
    const trackedSearchTerm = getByTestId(testIds.trackedSearchTerm);

    const setSearchOpen = getByTestId(testIds.setSearchOpen);
    const setSearchPage = getByTestId(testIds.setSearchPage);
    const setTrackedSearchTerm = getByTestId(testIds.setTrackedSearchTerm);

    await act(async () => {
      fireEvent.click(setSearchOpen);
      fireEvent.click(setSearchPage);
      fireEvent.click(setTrackedSearchTerm);
    });

    expect(searchOpen).toHaveTextContent(`${testValues.open}`);
    expect(searchPage).toHaveTextContent(`${testValues.page}`);
    expect(trackedSearchTerm).toHaveTextContent(testValues.term);
  });
});
