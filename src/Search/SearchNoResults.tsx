import React from 'react';

import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';

import Container from 'components/Container';
import searchResultsIcon from 'images/search-results-icon.svg';
import 'Search/styles/searchNoResults.scss';

interface Props {
  searchTerm: string;
}

function SearchNoResults(props: Props) {
  const { t } = useTranslation();

  return (
    <div className="search-noresults">
      <Container maxWidth="lg">
        <div className="search-noresults__header">
          <span className="search-noresults__header__search-term">{`${props.searchTerm} `}</span>
          <span className="search-noresults__header__count">
            {t('search.returnedZeroResults')}
          </span>
        </div>
        <div className="search-noresults__body">
          <div className="search-noresults__body__icon">
            <img src={searchResultsIcon} alt="search icon" />
          </div>
          <div className="search-noresults__zeroresultsmessage">
            <div
              className="search-noresults__messagebold"
              data-testid="zero-results-message"
            >
              {t('search.zeroResults')}
            </div>
            <div className="search-noresults__searchsuggestions">
              <ul>
                <li>{t('search.searchSuggestionsHint1')}</li>
                <li>{t('search.searchSuggestionsHint2')}</li>
                <li>{t('search.searchSuggestionsHint3')}</li>
                <li>
                  <span>{t('search.searchSuggestionsHint4')}</span>
                  <Link
                    to="/location-search"
                    className="search-noresults__contact-branch"
                  >
                    {t('search.contactBranch')}
                  </Link>
                  <span>{` ${t('search.forSpecialOrders')}`}</span>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </Container>
    </div>
  );
}

export default SearchNoResults;
