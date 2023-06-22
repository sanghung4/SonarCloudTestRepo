import { useContext, useEffect, useRef, useState } from 'react';

import {
  Box,
  Button,
  Card,
  ClickAwayListener,
  Grid,
  Grow,
  Link,
  Popper,
  Skeleton,
  Typography,
  alpha,
  useMediaQuery,
  useTheme
} from '@dialexa/reece-component-library';
import { Instance } from '@popperjs/core';
import { useTranslation } from 'react-i18next';
import { Link as RouterLink, useLocation } from 'react-router-dom';
import useResizeObserver from 'use-resize-observer';

import {
  ListStyled,
  ListItemButtonStyled,
  ListItemTextStyled
} from 'Categories/CategoriesStyles';
import { CategoriesContext } from 'Categories/CategoriesProvider';
import Loader from 'common/Loader';
import { Category, Maybe } from 'generated/graphql';
import { ArrowDropDownIcon } from 'icons';

function CategoriesButtonWrapper() {
  /**
   * Custom Hooks
   */
  const location = useLocation();
  const theme = useTheme();
  const { t } = useTranslation();
  const isLargeScreen = useMediaQuery(theme.breakpoints.up('lg'));

  /**
   * Context
   */
  const { categories, categoriesLoading } = useContext(CategoriesContext);

  /**
   * State
   */
  const [open, setOpen] = useState(false);
  const [selectedCategory, setSelectedCategory] = useState<Maybe<Category>>();

  /**
   * Refs
   */
  const containerEl = useRef<HTMLDivElement>(null);
  const popperRef = useRef<Instance>(null);

  const { ref: windowRef, width: windowWidth } =
    useResizeObserver<HTMLDivElement>({
      box: 'border-box',
      round: (n) => Math.round(n * 10) / 10
    });

  /**
   * Effects
   */
  useEffect(popperEffect, [categories, popperRef]);
  useEffect(locationChangeEffect, [location]);
  useEffect(defaultToFirstCategory, [categories, setSelectedCategory]);

  return (
    <Box component="div" ref={containerEl} sx={{ flex: '1 0 auto' }}>
      <Box
        component="div"
        ref={windowRef}
        sx={{
          position: 'absolute',
          width: '100vw',
          height: 0,
          zIndex: -1
        }}
      />
      <Button
        color={open ? 'primaryLight' : 'primary'}
        variant="text"
        onMouseEnter={() => setOpen(true)}
        endIcon={
          <Box
            component={ArrowDropDownIcon}
            sx={{ transform: open ? 'scaleY(-1)' : undefined }}
          />
        }
        data-testid="browse-products-button"
        sx={(theme) => ({
          borderRadius: '4px',
          bgcolor: open ? alpha(theme.palette.primary02.main, 0.1) : undefined,
          '&:hover': {
            bgcolor: alpha(theme.palette.primary02.main, 0.1)
          }
        })}
      >
        <Box fontWeight={500} component="span">
          {t('common.browseProducts')}
        </Box>
      </Button>
      <ClickAwayListener
        mouseEvent="onMouseDown"
        onClickAway={() => {
          setOpen(false);
        }}
      >
        <Popper
          style={{ zIndex: theme.zIndex.modal }}
          id={open ? 'categories-listing' : undefined}
          open={open}
          onMouseLeave={() => setOpen(false)}
          anchorEl={containerEl.current}
          placement="bottom-start"
          popperRef={popperRef}
          disablePortal={process.env.NODE_ENV === 'test'}
          modifiers={[
            {
              name: 'flip',
              options: {
                enabled: false
              }
            },
            {
              name: 'offset',
              options: {
                offset: [0, 8]
              }
            },
            {
              name: 'preventOverflow',
              options: {
                priority: ['left', 'right'],
                padding:
                  windowWidth && isLargeScreen
                    ? (windowWidth - theme.breakpoints.values.lg + 48) / 2
                    : theme.spacing(3)
              }
            }
          ]}
          transition
        >
          {({ TransitionProps }) => (
            <Grow style={{ transformOrigin: '0 0 0' }} {...TransitionProps}>
              <Card
                square
                elevation={0}
                data-testid="categories-listing"
                sx={(theme) => ({
                  border: 1,
                  borderColor: 'lighterGray.main',
                  minHeight: 200,
                  maxHeight: 600,
                  display: 'flex',
                  flexDirection: 'column',
                  p: 3,
                  [theme.breakpoints.between('md', 'lg')]: {
                    width: `calc(100vw - ${theme.spacing(6)})`
                  },
                  [theme.breakpoints.up('lg')]: {
                    width: `${
                      theme.breakpoints.values.lg - parseInt(theme.spacing(6))
                    }px`
                  }
                })}
              >
                {categoriesLoading ? (
                  <Loader />
                ) : (
                  <Box display="flex" overflow="hidden">
                    <Box component={ListStyled} sx={{ overflowY: 'auto' }}>
                      {!categories?.length
                        ? [...new Array(10)].map((_, i) => (
                            <Typography key={i} variant="h2">
                              <Skeleton width="calc(100vw / 5)" />
                            </Typography>
                          ))
                        : categories.map((category1, i) => (
                            <ListItemButtonStyled
                              key={category1?.name}
                              dense
                              data-testid={`category1-${i}`}
                              selected={
                                selectedCategory?.name === category1?.name
                              }
                              onMouseEnter={() =>
                                category1 && setSelectedCategory(category1)
                              }
                            >
                              <ListItemTextStyled
                                disableTypography
                                primary={
                                  <Typography
                                    variant="caption"
                                    color="textPrimary"
                                    component="span"
                                  >
                                    {category1?.name}
                                  </Typography>
                                }
                              />
                            </ListItemButtonStyled>
                          ))}
                    </Box>
                    {selectedCategory && selectedCategory.children ? (
                      <Box mx={8} my={2} width={1}>
                        <Box mb={5} display="flex" alignItems="center">
                          <Typography variant="h5">
                            {t('product.productCategories')}{' '}
                            <Box fontWeight={300} component="span">
                              ({selectedCategory?.children?.length ?? 0})
                            </Box>
                          </Typography>
                          <Link
                            variant="body2"
                            onClick={handleClose}
                            component={RouterLink}
                            to={{
                              pathname: '/search',
                              search: `?categories=${encodeURIComponent(
                                selectedCategory?.name ?? ''
                              ).trim()}`
                            }}
                            sx={{
                              color: 'primary02.main',
                              ml: 2
                            }}
                          >
                            {t('common.viewAll')}
                          </Link>
                        </Box>
                        <Grid
                          container
                          spacing={2}
                          sx={() => ({
                            height: 'calc(100% - 64px)',
                            overflow: 'hidden auto',
                            '&::-webkit-scrollbar': {
                              WebkitAppearance: 'none',
                              width: '5px',
                              borderRadius: '5px',
                              bgcolor: '#e4e4e4'
                            },
                            '&::-webkit-scrollbar-thumb': {
                              borderRadius: '5px',
                              bgcolor: 'primary02.main'
                            }
                          })}
                        >
                          {selectedCategory?.children?.map(
                            (category2, idx2) => (
                              <Grid
                                container
                                item
                                direction="column"
                                xs={3}
                                key={category2?.name}
                              >
                                <Link
                                  variant="body2"
                                  data-testid={`category2-${idx2}`}
                                  onClick={handleClose}
                                  component={RouterLink}
                                  sx={{ pb: 1, fontWeight: 500 }}
                                  to={{
                                    pathname: '/search',
                                    search: `&categories=${[
                                      selectedCategory.name,
                                      category2?.name
                                    ]
                                      .map((c) =>
                                        encodeURIComponent(c ?? '').trim()
                                      )
                                      .join('&categories=')}`
                                  }}
                                >
                                  {category2?.name}
                                </Link>
                                {category2?.children?.map((category3, idx3) => (
                                  <Link
                                    key={category3?.name}
                                    data-testid={`category3-${idx2}-${idx3}`}
                                    color="textPrimary"
                                    variant="caption"
                                    onClick={handleClose}
                                    component={RouterLink}
                                    sx={{ pb: 2 }}
                                    to={{
                                      pathname: '/search',
                                      search: `&categories=${[
                                        selectedCategory?.name,
                                        category2?.name,
                                        category3?.name
                                      ]
                                        .map((c) =>
                                          encodeURIComponent(c ?? '').trim()
                                        )
                                        .join('&categories=')}`
                                    }}
                                  >
                                    {category3?.name}
                                  </Link>
                                ))}
                              </Grid>
                            )
                          )}
                        </Grid>
                      </Box>
                    ) : null}
                  </Box>
                )}
              </Card>
            </Grow>
          )}
        </Popper>
      </ClickAwayListener>
      {/* <Button color={open ? 'primaryLight' : 'primary'}
        variant="text">
        <Typography fontFamily="Roboto" fontWeight="400" fontSize="14px" lineHeight="24px">
      <Box >
        <Link href='/brands' color="grey" bgcolor="white" fontFamily="Roboto" fontWeight="400" fontSize="14px" lineHeight="24px">
          {t('common.brands')}
        </Link>
        </Box>
        </Typography>
      </Button> */}
    </Box>
  );

  /**
   * Effect Defs
   */
  function popperEffect() {
    if (categories && popperRef.current) {
      popperRef.current.update();
    }
  }

  function locationChangeEffect() {
    setOpen(false);
  }

  function defaultToFirstCategory() {
    setSelectedCategory(categories?.[0]);
  }

  /**
   * Event Handlers
   */
  function handleClose() {
    setOpen(false);
  }
}

export default CategoriesButtonWrapper;
