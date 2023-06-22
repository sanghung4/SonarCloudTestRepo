import { Fragment, useState } from 'react';

import {
  Button,
  Grid,
  Image,
  Link,
  useScreenSize
} from '@dialexa/reece-component-library';
import { useTranslation } from 'react-i18next';

import {
  FinishButton,
  FinishTypography,
  PoshProductDivider,
  PoshTypography as Typography,
  usePoshStyles,
  ProductImage,
  ProductImageGrid,
  ProductGrid,
  FinishesGrid,
  ChromeButton,
  GoldButton,
  BlackButton
} from 'Brands/Posh/util/styles';
import goldButton from 'images/posh/faucets/GoldButton.png';
import blackButton from 'images/posh/faucets/BlackButton.png';
import chromeButton from 'images/posh/faucets/ChromeButton.png';
import nickelButton from 'images/posh/faucets/NickelButton.png';
import notfound from 'images/notfound.png';
import { productList } from 'Brands/Posh/util/productList';

export default function PoshProductList() {
  /**
   * Custom Hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();
  const sx = usePoshStyles(isSmallScreen);
  const [imagesIndex, setImagesIndex] = useState([0, 0, 0, 0, 0, 0]);

  const handleFinishChange = (productIndex: number, finishIndex: number) => {
    const newIndex = imagesIndex.map((item, i) => {
      return i === productIndex ? finishIndex : item;
    });
    setImagesIndex(newIndex);
  };

  /**
   * Render
   */
  return (
    <Grid container bgcolor="warmGray.main">
      {productList.map((item, index) => (
        <Fragment key={index}>
          <ProductGrid
            container
            direction={index % 2 ? 'row' : 'row-reverse'}
            justifyContent="center"
          >
            <ProductImageGrid
              item
              container
              display="flex"
              xs={12}
              md={6}
              justifyContent="center"
              direction="column"
            >
              <ProductImage
                container
                direction="column"
                justifyContent="center"
              >
                <Grid item alignItems="center">
                  <img
                    src={item.imageFinishes[imagesIndex[index]]}
                    alt={item.title}
                    style={{
                      width:
                        isSmallScreen && index !== 3 && index !== 5
                          ? '90%'
                          : undefined,
                      transform:
                        index === 3
                          ? 'scale(1.25,1.25)'
                          : index === 2
                          ? 'scale(.8,.8)'
                          : undefined
                    }}
                    data-testid={`posh-product-image-${index}`}
                  />
                </Grid>
              </ProductImage>
              <Grid item alignItems="left" justifyContent="left">
                <FinishTypography>
                  {t('poshMarketing.availableFinishes')}
                </FinishTypography>
              </Grid>
              <FinishesGrid item alignItems="center" justifyContent="center">
                <Grid container xs={12} direction="row" spacing={1}>
                  <Grid item xs={3}>
                    <ChromeButton
                      component="button"
                      bgcolor="warmGray.main"
                      data-testid={`chrome-finish-button-${index}`}
                      onClick={() => {
                        handleFinishChange(index, 0);
                      }}
                    >
                      <Image
                        fallback={notfound}
                        alt={t('poshMarketing.chromeButton')}
                        src={chromeButton}
                        sx={{
                          width: isSmallScreen ? '70%' : undefined,
                          border: imagesIndex[index] === 0 ? 2 : 0,
                          borderRadius: imagesIndex[index] === 0 ? 10 : 0,
                          borderColor:
                            imagesIndex[index] === 0 ? 'poshRed.main' : 'none',
                          '&:hover': {
                            transform: 'scale(1.1,1.1)'
                          }
                        }}
                      />
                      <FinishTypography>
                        {t('poshMarketing.chromeButton')}
                      </FinishTypography>
                    </ChromeButton>
                  </Grid>
                  <Grid item xs={3}>
                    <FinishButton
                      data-testid={`nickel-finish-button-${index}`}
                      onClick={() => {
                        handleFinishChange(index, 1);
                      }}
                    >
                      <Image
                        fallback={notfound}
                        alt={t('poshMarketing.nickelButton')}
                        src={nickelButton}
                        sx={{
                          width: isSmallScreen ? '70%' : undefined,
                          border: imagesIndex[index] === 1 ? 2 : 0,
                          borderRadius: imagesIndex[index] === 1 ? 10 : 0,
                          borderColor:
                            imagesIndex[index] === 1 ? 'poshRed.main' : 'none',
                          '&:hover': {
                            transform: 'scale(1.1,1.1)'
                          }
                        }}
                      />
                      <FinishTypography>
                        {t('poshMarketing.nickelButton')}
                      </FinishTypography>
                    </FinishButton>
                  </Grid>
                  <Grid item xs={3}>
                    <GoldButton
                      component="button"
                      bgcolor="warmGray.main"
                      data-testid={`gold-finish-button-${index}`}
                      onClick={() => {
                        handleFinishChange(index, 2);
                      }}
                    >
                      <Image
                        fallback={notfound}
                        alt={t('poshMarketing.goldButton')}
                        src={goldButton}
                        sx={{
                          width: isSmallScreen ? '70%' : undefined,
                          border: imagesIndex[index] === 2 ? 2 : 0,
                          borderRadius: imagesIndex[index] === 2 ? 10 : 0,
                          borderColor:
                            imagesIndex[index] === 2 ? 'poshRed.main' : 'none',
                          '&:hover': {
                            transform: 'scale(1.1,1.1)'
                          }
                        }}
                      />
                      <FinishTypography>
                        {t('poshMarketing.goldButton')}
                      </FinishTypography>
                    </GoldButton>
                  </Grid>
                  <Grid item xs={3}>
                    <BlackButton
                      component="button"
                      bgcolor="warmGray.main"
                      data-testid={`black-finish-button-${index}`}
                      onClick={() => {
                        handleFinishChange(index, 3);
                      }}
                    >
                      <Image
                        fallback={notfound}
                        alt={t('poshMarketing.blackButton')}
                        src={blackButton}
                        sx={{
                          width: isSmallScreen ? '70%' : undefined,
                          border: imagesIndex[index] === 3 ? 2 : 0,
                          borderRadius: imagesIndex[index] === 3 ? 10 : 0,
                          borderColor:
                            imagesIndex[index] === 3 ? 'poshRed.main' : 'none',
                          '&:hover': {
                            transform: 'scale(1.1,1.1)'
                          }
                        }}
                      />
                      <FinishTypography>
                        {t('poshMarketing.blackButton')}
                      </FinishTypography>
                    </BlackButton>
                  </Grid>
                </Grid>
              </FinishesGrid>
            </ProductImageGrid>
            <Grid
              item
              xs={isSmallScreen ? 12 : 6}
              px={isSmallScreen ? 4.5 : 6}
              py={isSmallScreen ? 3.5 : 12}
              justifyContent="center"
              alignItems="center"
            >
              <Typography
                color="textPrimary"
                fontSize={isSmallScreen ? 30 : 40}
                fontWeight={700}
                lineHeight={1.2}
              >
                {item.title}
              </Typography>
              <Grid mt={isSmallScreen ? 2 : 4}>
                <Typography>{t('poshMarketing.features')}:</Typography>
              </Grid>
              <ul
                style={{
                  marginBlockStart: 0,
                  paddingInlineStart: 20
                }}
              >
                {item.features.map((feature, i2) => (
                  <li key={`${index}${i2}`}>
                    <Typography
                      color="textPrimary"
                      fontSize={16}
                      fontWeight={500}
                      lineHeight={1.5}
                    >
                      {feature}
                    </Typography>
                  </li>
                ))}
              </ul>
              <Grid
                mt={isSmallScreen ? 2 : 4}
                display="flex"
                justifyContent={isSmallScreen ? 'center' : undefined}
              >
                <Link href={item.url}>
                  <Button variant="secondary" sx={sx.button}>
                    {t('aboutUs.shopNow')}
                  </Button>
                </Link>
              </Grid>
            </Grid>
          </ProductGrid>
          {index < productList.length - 1 ? (
            <PoshProductDivider
              mobile={isSmallScreen}
              style={{
                width: '-moz-available'
              }}
            />
          ) : null}
        </Fragment>
      ))}
    </Grid>
  );
}
