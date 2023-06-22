import { useContext, useState } from 'react';

import {
  Box,
  Button,
  Divider,
  IconButton,
  Link,
  Slide,
  Typography,
  useTheme
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';
import { Link as RouterLink, useHistory } from 'react-router-dom';

import { CategoriesContext } from 'Categories/CategoriesProvider';
import {
  ListStyled,
  ListItemButtonStyled,
  ListItemTextStyled
} from 'Categories/CategoriesStyles';
import Loader from 'common/Loader';
import { Category } from 'generated/graphql';
import { ChevronLeftIcon, CloseIcon } from 'icons';

type Props = {
  in: boolean;
  handleClose: (drawer: boolean) => void;
};

function CategoriesMobileWrapper(props: Props) {
  /**
   * Custom Hooks
   */
  const history = useHistory();
  const theme = useTheme();
  const { t } = useTranslation();

  /**
   * Context
   */
  const { categories, categoriesLoading } = useContext(CategoriesContext);

  /**
   * State
   */
  const [selectedCategories, setSelectedCategories] = useState<Category[]>();

  /**
   * Effects
   */

  return (
    <Slide direction="left" in={props.in}>
      <Box
        position="fixed"
        display="flex"
        flexDirection="column"
        flex="1 0 auto"
        top={0}
        left={0}
        right={0}
        height={1}
        zIndex={1}
        sx={{ bgcolor: 'common.white' }}
      >
        <Box
          mt={1.5}
          mb={1.5}
          mx={3}
          display="flex"
          justifyContent="space-between"
          alignItems="center"
        >
          <Box display="flex" flex="1" />
          <Typography
            variant="h5"
            color="primary"
            align="center"
            sx={{ fontWeight: 700 }}
          >
            {t('common.browse')}
          </Typography>
          <Box display="flex" flex="1" justifyContent="flex-end">
            <IconButton
              onClick={() => props.handleClose(true)}
              size="large"
              sx={{ p: 0 }}
            >
              <CloseIcon />
            </IconButton>
          </Box>
        </Box>
        <Divider />
        <Box pt={2} pb={3}>
          <Button
            variant="text"
            data-testid="category-back"
            startIcon={<ChevronLeftIcon />}
            onClick={() => handleBack()}
          >
            {t('common.back')}
          </Button>
        </Box>
        {categoriesLoading ? (
          <Loader />
        ) : (
          <>
            {/* CATEGORY HEADING - start */}
            <Box
              px={4}
              pb={1.5}
              display="flex"
              justifyContent="space-between"
              alignItems="flex-start"
              color="primary02.main"
            >
              <Typography
                variant="body1"
                data-testid="category-header"
                color="textPrimary"
                sx={{ pr: 2, fontWeight: 700 }}
              >
                {selectedCategories?.length
                  ? `${
                      selectedCategories[selectedCategories.length - 1].name
                    } (${
                      selectedCategories[selectedCategories.length - 1]
                        ?.children?.length
                    })`
                  : `${t('product.productCategories')} (${
                      categories?.length ?? 0
                    })`}
              </Typography>
              {selectedCategories?.length ? (
                <Link
                  color="inherit"
                  variant="body1"
                  data-testid="view-all-categories"
                  onClick={() => handleClose()}
                  component={RouterLink}
                  sx={{ flex: '0 0 auto' }}
                  to={{
                    pathname: '/search',
                    search: `categories=${selectedCategories
                      .map((c) => encodeURIComponent(c.name).trim())
                      .join('&categories=')}`
                  }}
                >
                  {t('common.viewAll')}
                </Link>
              ) : null}
            </Box>
            {/* CATEGORY HEADING - end */}
            {/* CATEGORY 1 - start */}
            <Box
              display="flex"
              flex={1}
              flexDirection="column"
              overflow="hidden"
              position="relative"
            >
              <ListStyled sx={{ zIndex: 1 }}>
                {categories?.map((category1, i) => (
                  <ListItemButtonStyled
                    key={category1?.name}
                    data-testid={`category1-${i}`}
                    onClick={() => category1 && handleCategorySelect(category1)}
                  >
                    <ListItemTextStyled primary={category1?.name} />
                  </ListItemButtonStyled>
                ))}
              </ListStyled>
              {/* CATEGORY 1 - end */}
              {/* CATEGORY 2 - start */}
              <Box
                position="absolute"
                top="0"
                left="0"
                right="0"
                bottom="0"
                zIndex={selectedCategories?.length ? 2 : 0}
                display="flex"
                flexDirection="column"
                sx={{ bgcolor: 'common.white' }}
              >
                <Slide
                  direction="left"
                  in={selectedCategories?.length === 1}
                  timeout={{
                    enter: theme.transitions.duration.enteringScreen,
                    exit: theme.transitions.duration.leavingScreen / 2
                  }}
                >
                  <ListStyled>
                    {(selectedCategories?.length
                      ? selectedCategories?.[0]?.children ?? []
                      : []
                    ).map((category, i) => (
                      <ListItemButtonStyled
                        key={category?.name}
                        data-testid={`category2-${i}`}
                        onClick={() =>
                          category && handleCategorySelect(category)
                        }
                      >
                        <ListItemTextStyled primary={category?.name} />
                      </ListItemButtonStyled>
                    ))}
                  </ListStyled>
                </Slide>
              </Box>
              {/* CATEGORY 2 - end */}
              {/* CATEGORY 3 - start */}
              <Box
                position="absolute"
                top="0"
                left="0"
                right="0"
                bottom="0"
                zIndex={selectedCategories?.length === 2 ? 3 : 0}
                display="flex"
                flexDirection="column"
                sx={{ bgcolor: 'common.white' }}
              >
                <Slide
                  direction="left"
                  in={selectedCategories?.length === 2}
                  timeout={{
                    enter: theme.transitions.duration.enteringScreen,
                    exit: theme.transitions.duration.leavingScreen / 2
                  }}
                >
                  <ListStyled>
                    {(selectedCategories?.length === 2
                      ? selectedCategories?.[1]?.children ?? []
                      : []
                    ).map((category, i) => (
                      <ListItemButtonStyled
                        key={category?.name}
                        data-testid={`category3-${i}`}
                        onClick={() =>
                          category && handleCategorySelect(category)
                        }
                      >
                        <ListItemTextStyled primary={category?.name} />
                      </ListItemButtonStyled>
                    ))}
                  </ListStyled>
                </Slide>
              </Box>
              {/* CATEGORY 3 - end */}
            </Box>
          </>
        )}
      </Box>
    </Slide>
  );

  /**
   * Event Handlers
   */

  function handleBack() {
    if (selectedCategories?.length) {
      setSelectedCategories(selectedCategories.slice(0, -1));
    } else {
      handleClose(false);
    }
  }

  /**
   *
   * @param closeDrawer Whether to close the whole drawer or just the Browse Products
   */
  function handleClose(closeDrawer: boolean = true) {
    props.handleClose(closeDrawer);

    // Wait until the drawer has closed before resetting the Category data
    setTimeout(() => {
      setSelectedCategories([]);
    }, theme.transitions.duration.leavingScreen);
  }

  function handleCategorySelect(category: Category) {
    if (category?.children?.length) {
      setSelectedCategories([...(selectedCategories ?? []), category]);
    } else {
      history.push({
        pathname: '/search',
        search: `categories=${[...(selectedCategories ?? []), category]
          .map((c) => encodeURIComponent(c?.name ?? '').trim())
          .join('&categories=')}`
      });

      handleClose();
    }
  }
}

export default CategoriesMobileWrapper;
