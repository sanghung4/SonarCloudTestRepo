import { TFunction } from 'i18next';

import { BrandCardProps } from 'Brands/BrandCard';
import poshImage from 'Brands/images/posh-hero-image.jpg';
import bpressImage from 'Brands/images/bpress-hero-image.jpg';
import bigDogImage from 'Brands/images/bigdog-hero-image.jpg';

export const brandList = (t: TFunction): BrandCardProps[] => [
  {
    thumbnail: poshImage,
    title: t('brands.brandCardPosh.title'),
    description: t('brands.brandCardPosh.description'),
    url: 'brands/posh',
    sx: false
  },
  {
    thumbnail: bpressImage,
    title: t('brands.brandCardBpress.title'),
    description: t('brands.brandCardBpress.description'),
    url: 'https://reece.conexbanninger.com/',
    sx: true
  },
  {
    thumbnail: bigDogImage,
    title: t('brands.brandCardBigDog.title'),
    description: t('brands.brandCardBigDog.description'),
    url: 'brands/bigdog',
    sx: false
  }
];
