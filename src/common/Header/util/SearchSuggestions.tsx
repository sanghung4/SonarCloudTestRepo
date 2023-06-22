import { Fragment } from 'react';

import {
  Box,
  Divider,
  Grid,
  ListItemText,
  Skeleton,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import SearchTopResult from './SearchTopResult';
import { SearchSuggestionResult } from 'generated/graphql';
import escapeRegex from 'utils/escapeRegex';
import {
  SuggestionList,
  SkeletonListItem,
  SuggestionListItem,
  SuggestionListSubItem
} from './styles';

type Props = {
  loading: boolean;
  searchTerm: string;
  onSuggestionClick: (suggestion: string, category?: string | null) => void;
  onResultClick: (id: string) => void;
  results?: SearchSuggestionResult;
};

function SearchSuggestions(props: Props) {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();
  const { isSmallScreen } = useScreenSize();

  /**
   * Render
   */
  const matchExpression = new RegExp(`^${escapeRegex(props.searchTerm)}`);

  const renderSuggestion = (text: string) => {
    const matchText = text
      .replace(/[^a-z0-9\s]/gi, '')
      .match(matchExpression)?.[0];
    const restText = text.replace(matchExpression, '');

    return (
      <ListItemText disableTypography>
        <strong>{matchText}</strong>
        {restText}
      </ListItemText>
    );
  };

  return (
    <Grid container>
      {/* Suggestion */}
      <Grid item md={5} sm={12} container>
        <SuggestionList dense>
          {props.loading
            ? [...Array(10).keys()].map((x) => (
                <SkeletonListItem key={x}>
                  <ListItemText>
                    <Skeleton />
                  </ListItemText>
                </SkeletonListItem>
              ))
            : props.results?.suggestions?.map((sug, sIndex) => (
                <Fragment key={sug}>
                  <SuggestionListItem
                    onClick={() => props.onSuggestionClick(sug)}
                  >
                    {renderSuggestion(sug)}
                  </SuggestionListItem>
                  {sIndex === 0 &&
                    props.results?.topCategories.slice(0, 2).map((cat) => (
                      <SuggestionListSubItem
                        key={`${sug}_${cat.value}-cat`}
                        onClick={() => props.onSuggestionClick(sug, cat.value)}
                      >
                        <ListItemText disableTypography>
                          in{' '}
                          <Box
                            component="span"
                            color="primary02.main"
                            fontWeight={600}
                          >
                            {cat.value}
                          </Box>
                        </ListItemText>
                      </SuggestionListSubItem>
                    ))}
                </Fragment>
              ))}
        </SuggestionList>
      </Grid>

      {/* Top Results  */}
      {!isSmallScreen && (
        <>
          <Divider flexItem orientation="vertical" />
          <Grid item md paddingX={2}>
            <Typography
              variant="h5"
              fontWeight={600}
              letterSpacing={0}
              paddingBottom={1}
            >
              {t('search.topResults')}
            </Typography>
            {props.loading
              ? [...Array(3).keys()].map((x) => (
                  <SearchTopResult loading key={x} />
                ))
              : props.results?.topProducts?.map((prod) => (
                  <SearchTopResult
                    product={prod}
                    key={prod.id}
                    onClick={props.onResultClick}
                  />
                ))}
          </Grid>
        </>
      )}
    </Grid>
  );
}

export default SearchSuggestions;
