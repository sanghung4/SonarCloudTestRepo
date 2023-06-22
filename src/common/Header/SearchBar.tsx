import {
  FormEvent,
  useContext,
  useEffect,
  useMemo,
  useRef,
  useState
} from 'react';

import { useHistory, useLocation } from 'react-router-dom';
import {
  Box,
  Card,
  ClickAwayListener,
  Grow,
  Popper,
  useScreenSize,
  Zoom
} from '@dialexa/reece-component-library';
import useResizeObserver from 'use-resize-observer';

import { AuthContext } from 'AuthProvider';
import { HeaderContext } from 'common/Header/HeaderProvider';
import { useResizeObserverRoundFn } from 'common/Header/util';
import SearchDrawer from 'common/Header/util/SearchDrawer';
import SearchInput from 'common/Header/util/SearchInput';
import SearchSuggestions from 'common/Header/util/SearchSuggestions';
import { useSearchSuggestionQuery } from 'generated/graphql';
import useDebounce from 'hooks/useDebounce';
import { useDomainInfo } from 'hooks/useDomainInfo';
import { useQueryParams } from 'hooks/useSearchParam';
import { useSubdomainErpSystem } from 'hooks/useSubdomainErpSystem';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';
import { SearchParams } from 'Search/util/useSearchQueryParams';
import { trackSearch } from 'utils/analytics';
import { timestamp } from 'utils/dates';
import { setItem, getItem } from 'utils/localStorage';

const MAX_SEARCH_TERM_COUNT = 10;

function SearchBar() {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const history = useHistory();
  const { pathname, search } = useLocation();
  const { currentSubdomain } = useSubdomainErpSystem();
  const { engine } = useDomainInfo();
  const [, setParams] = useQueryParams<SearchParams>({
    arrayKeys: ['categories', 'filters']
  });

  /**
   * Refs
   */
  const inputContainerRef = useRef<HTMLDivElement>(null);
  const { ref: headerRef, height: headerHeight } =
    useResizeObserver<HTMLDivElement>({
      box: 'border-box',
      round: useResizeObserverRoundFn
    });

  /**
   * State
   */
  const [searchTerm, setSearchTerm] = useState('');
  const [searchedOnPage, setSearchedOnPage] = useState(pathname);
  const { value: debouncedSearchTerm, loading: debouncedSearchTermLoading } =
    useDebounce(searchTerm, 500);

  /**
   * Context
   */
  const { profile, authState, user } = useContext(AuthContext);
  const { searchOpen, setSearchOpen, setTrackedSearchTerm, setSearchPage } =
    useContext(HeaderContext);
  const { selectedAccounts } = useSelectedAccountsContext();

  /**
   * Data
   */
  const { data: searchSuggestionsData, loading: searchSuggestionsLoading } =
    useSearchSuggestionQuery({
      skip: !debouncedSearchTerm || debouncedSearchTermLoading,
      variables: {
        term: debouncedSearchTerm,
        userId: profile?.userId,
        shipToAccountId: selectedAccounts.shipTo?.id,
        erpSystem: currentSubdomain,
        state: selectedAccounts?.shipToErpAccount?.state?.toLowerCase(),
        engine
      }
    });

  /**
   * Memos
   */
  const headerHeightAdjusted = useMemo(headerHeightAdjustedMemo, [
    headerHeight
  ]);

  /**
   * Effects
   */
  useEffect(() => {
    const isSearchPage = pathname === '/search' && search.includes('criteria');
    const isSearchedOnPage = pathname === searchedOnPage;
    if ((!isSearchPage && !isSearchedOnPage) || isSearchedOnPage) {
      setSearchTerm('');
    }
    /**
     * NOTICE:
     * The hook exhaustive deps is only pointed towards `pathname` is because
     * we only need to reset searchTerm only on page change. Not something like `searchedOnPage`.
     * Otherwise, it'll reset on the first key press after a page change
     */
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [pathname]);

  /**
   * Mobile Render
   */
  if (isSmallScreen) {
    return (
      <>
        {/******************************/}
        {/* Search Input               */}
        {/******************************/}
        <Zoom in={searchOpen}>
          <Box
            sx={(theme) => ({
              [theme.breakpoints.down('md')]: {
                position: 'absolute',
                top: 0,
                bottom: 0,
                left: theme.spacing(1.5),
                right: theme.spacing(1.5),
                justifyContent: 'center',
                alignItems: 'center',
                bgcolor: 'background.paper',
                zIndex: 'drawer',
                display: 'flex',
                transformOrigin: 'calc(100% - 80px) 24px'
              }
            })}
            ref={headerRef}
          >
            <SearchInput
              value={searchTerm}
              onChange={handleChange}
              onSubmit={handleSubmit}
              onFocus={handleFocus}
            />
          </Box>
        </Zoom>
        {/******************************/}
        {/* Drawer                     */}
        {/******************************/}
        <SearchDrawer
          open={searchOpen}
          headerHeight={headerHeightAdjusted}
          onBackClick={handleDrawerBackClick}
        >
          {!!searchTerm && (
            <SearchSuggestions
              loading={debouncedSearchTermLoading || searchSuggestionsLoading}
              results={searchSuggestionsData?.searchSuggestion}
              searchTerm={debouncedSearchTerm}
              onSuggestionClick={handleSuggestionClick}
              onResultClick={handleResultClick}
            />
          )}
        </SearchDrawer>
      </>
    );
  }

  /**
   * Desktop Render
   */
  return (
    <Box flex="1">
      <ClickAwayListener onClickAway={handleClickAway}>
        <div ref={inputContainerRef}>
          {/******************************/}
          {/* Search Input               */}
          {/******************************/}
          <SearchInput
            value={searchTerm}
            onChange={handleChange}
            onSubmit={handleSubmit}
            onFocus={handleFocus}
          />
          {/******************************/}
          {/* Search Suggestions         */}
          {/******************************/}
          <Popper
            id={searchOpen ? 'orders-range' : undefined}
            open={!!searchTerm && searchOpen}
            anchorEl={inputContainerRef.current}
            placement="bottom"
            modifiers={[
              { name: 'preventOverflow', enabled: false },
              { name: 'flip', enabled: false }
            ]}
            disablePortal={process.env.NODE_ENV === 'test'}
            transition
            style={{ zIndex: 10001 }}
          >
            {({ TransitionProps }) => (
              <Grow {...TransitionProps}>
                <Card
                  data-testid="search-suggestions"
                  sx={{
                    width: inputContainerRef?.current?.clientWidth,
                    py: 4,
                    px: 2
                  }}
                >
                  <SearchSuggestions
                    loading={
                      debouncedSearchTermLoading || searchSuggestionsLoading
                    }
                    results={searchSuggestionsData?.searchSuggestion}
                    searchTerm={debouncedSearchTerm}
                    onSuggestionClick={handleSuggestionClick}
                    onResultClick={handleResultClick}
                  />
                </Card>
              </Grow>
            )}
          </Popper>
        </div>
      </ClickAwayListener>
    </Box>
  );

  /**
   * Memo Definitions
   */
  function headerHeightAdjustedMemo() {
    return (headerHeight ?? 0) + 1;
  }

  /**
   * Callback Definitions
   */
  function handleChange(value: string) {
    setSearchTerm(value);
    setSearchedOnPage(pathname);
  }

  function handleFocus() {
    if (!isSmallScreen) {
      setSearchOpen(true);
    }
  }

  function handleSubmit(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();

    if (searchTerm) {
      const authenticated = authState?.isAuthenticated;

      trackSearch({
        authenticated: authenticated,
        searchTerm,
        timestamp,
        user: authenticated ? user?.email : null
      });

      handleSearchTermLocalStorage(searchTerm);

      setSearchPage(1);
      setSearchOpen(false);

      // Blur the Search input on submit so the Search Suggestions will appear
      // on subsequent searches
      // Also ignoring test coverage since we cannot mock activeElement as not an instance of HTMLElement
      // istanbul ignore next
      if (document.activeElement instanceof HTMLElement) {
        document.activeElement.blur();
      }

      // The & Character causes problems when being used in a string in the url
      const urlSafeSearchTerm: string = encodeURIComponent(searchTerm);

      history.push({
        pathname: '/search',
        search: `?criteria=${urlSafeSearchTerm}`
      });
    }
  }

  function handleClickAway(e: MouseEvent | TouchEvent) {
    e.stopPropagation();
    setSearchOpen(false);
  }

  function handleDrawerBackClick() {
    setSearchOpen(false);
  }

  function handleSuggestionClick(criteria: string, category?: string | null) {
    setParams({ criteria, categories: [category ?? ''] }, '/search');
    setSearchPage(1);
    setSearchOpen(false);
    setSearchTerm('');
  }

  function handleResultClick(id: string) {
    // Go to the product page
    history.push(`/product/${id}`);
    // Close the search box
    setSearchOpen(false);
    // Set the saved term
    setTrackedSearchTerm(searchTerm);
    // Reset the page
    setSearchPage(0);
    setSearchTerm('');
  }

  function handleSearchTermLocalStorage(searchTerm: string) {
    const searchTermsLocalStorageRaw = getItem('searchTerms');
    const searchTerms: String[] = searchTermsLocalStorageRaw
      ? JSON.parse(searchTermsLocalStorageRaw)
      : [];

    if (!searchTerms) {
      setItem('searchTerms', JSON.stringify([searchTerm]));
      return;
    }

    if (searchTerms.includes(searchTerm)) {
      const newSearchTerms = searchTerms.filter((t) => t !== searchTerm);
      newSearchTerms.unshift(searchTerm);
      setItem('searchTerms', JSON.stringify(newSearchTerms));
      return;
    }

    if (searchTerms.length >= MAX_SEARCH_TERM_COUNT) {
      searchTerms.pop();
    }
    searchTerms.unshift(searchTerm);
    setItem('searchTerms', JSON.stringify(searchTerms));
  }
}

export default SearchBar;
