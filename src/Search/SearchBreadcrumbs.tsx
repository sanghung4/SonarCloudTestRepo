import React from 'react';

import {
  Breadcrumbs as MuiBreadcrumbs,
  Button,
  Link,
  useScreenSize
} from '@dialexa/reece-component-library';
import { kebabCase } from 'lodash-es';
import { useTranslation } from 'react-i18next';
import { useHistory } from 'react-router-dom';

import useSearchQueryParams from 'Search/util/useSearchQueryParams';
import { ChevronLeftIcon, ChevronRightIcon } from 'icons';

function Breadcrumbs() {
  /**
   * Custom Hooks
   */
  const history = useHistory();
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();

  const [params, setParams] = useSearchQueryParams();
  const { categories = [], criteria: searchTerm } = params;

  /**
   * Callbacks
   */
  const goToDepth = (i: number) => {
    setParams({
      ...params,
      categories: categories.slice(0, i)
    });
  };

  const goBackOne = () => {
    if (categories.length) {
      goToDepth(categories.length - 1);
    } else {
      history.push('/');
    }
  };

  return isSmallScreen ? (
    <Link>
      <Button
        startIcon={<ChevronLeftIcon fontSize="small" />}
        variant="inline"
        onClick={goBackOne}
        sx={{
          p: 0,
          right: (theme) => theme.spacing(1)
        }}
        data-testid="home-button-mobile"
      >
        {categories.length ? categories?.slice(-1)?.[0] : t('common.home')}
      </Button>
    </Link>
  ) : categories.length ? (
    <MuiBreadcrumbs
      aria-label="breadcrumb"
      separator={<ChevronRightIcon />}
      sx={{ p: 0 }}
    >
      {[t('common.home'), ...categories].map((c, i) => (
        <Button
          key={c ?? i}
          onClick={() => goToDepth(i)}
          variant="inline"
          data-testid={kebabCase(`${c ?? i}-breadcrumb-button`)}
          sx={[
            {
              py: 0,
              px: 1,
              minWidth: 0,
              fontWeight: 400
            },
            Boolean(
              i === categories.length && (!searchTerm || searchTerm === '')
            ) && {
              color: 'darkGray.main',
              cursor: 'default',
              '&:hover': {
                color: 'darkGray.main'
              }
            },
            i === 0 && { pl: 0 }
          ]}
        >
          {c}
        </Button>
      ))}
      {searchTerm ? (
        <Button
          variant="inline"
          data-testid={kebabCase(`${searchTerm}-breadcrumb-button`)}
          sx={{
            py: 0,
            px: 1,
            minWidth: 0,
            fontWeight: 400,
            color: 'darkGray.main',
            cursor: 'default',
            '&:hover': {
              color: 'darkGray.main'
            }
          }}
        >
          {searchTerm}
        </Button>
      ) : null}
    </MuiBreadcrumbs>
  ) : null;
}

export default Breadcrumbs;
