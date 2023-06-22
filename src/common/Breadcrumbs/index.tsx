import { useMemo } from 'react';

import {
  Container,
  Box,
  Breadcrumbs as MuiBreadcrumbs,
  Button,
  Link,
  Typography,
  useScreenSize
} from '@dialexa/reece-component-library';
import * as H from 'history';
import { isUndefined, kebabCase } from 'lodash-es';
import { useTranslation } from 'react-i18next';
import { Link as RouterLink } from 'react-router-dom';

import { ChevronLeftIcon, ChevronRightIcon } from 'icons';

export type BreadcrumbConfig = {
  text: string;
  to?: string | H.Location;
};

type Props = {
  pageTitle: string;
  config?: BreadcrumbConfig[];
};

export default function Breadcrumbs(props: Props) {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();
  /**
   * Memos
   */
  const configWithHome = useMemo(configWithHomeMemo, [props.config, t]);
  const previousPath = useMemo(previousPathMemo, [configWithHome]);
  /**
   * Render
   */
  return (
    <Container disableGutters={isSmallScreen} sx={{ displayPrint: 'none' }}>
      {isSmallScreen ? (
        <Box
          width={1}
          py={1}
          justifyContent="left"
          data-testid="back-button-mobile"
        >
          {typeof previousPath.to === 'string' ? ( // No need for undefined check as it will never occur due to previousPath memo
            <Link href={previousPath.to}>
              <Button startIcon={<ChevronLeftIcon />} variant="text">
                {previousPath.text !== '.'
                  ? t('breadcrumbs.backTo', { text: previousPath.text })
                  : t('common.back')}
              </Button>
            </Link>
          ) : (
            <Link component={RouterLink} to={previousPath.to!}>
              <Button startIcon={<ChevronLeftIcon />} variant="text">
                {previousPath.text !== '.'
                  ? t('breadcrumbs.backTo', { text: previousPath.text })
                  : t('common.back')}
              </Button>
            </Link>
          )}
        </Box>
      ) : (
        <MuiBreadcrumbs
          aria-label="breadcrumb"
          separator={<ChevronRightIcon />}
        >
          {configWithHome
            .filter((c) => c.text !== '.')
            .map((c) =>
              typeof c.to === 'string' ? ( // No need for undefined check as it will never occur due to previousPath memo
                <Link color="primary" key={c.text} href={c.to}>
                  {c.text}
                </Link>
              ) : (
                <Link
                  color="inherit"
                  key={c.text}
                  component={RouterLink}
                  to={c.to!}
                  data-testid={kebabCase(`${c.text}-breadcrumb`)}
                >
                  {c.text}
                </Link>
              )
            )}
          <Typography
            color="textPrimary"
            variant="caption"
            data-testid={kebabCase(`${props.pageTitle}-breadcrumb`)}
          >
            {props.pageTitle}
          </Typography>
        </MuiBreadcrumbs>
      )}
    </Container>
  );

  /**
   * Memo def
   */
  function configWithHomeMemo() {
    return [
      {
        text: t('common.home'),
        to: '/'
      },
      ...(props.config || [])
    ];
  }

  function previousPathMemo() {
    const previousLinks = configWithHome.filter(
      (prev) => !isUndefined(prev.to)
    );

    if (previousLinks.length > 1) {
      return previousLinks.slice(-1)[0];
    }
    return configWithHome[0];
  }
}
