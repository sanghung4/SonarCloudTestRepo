import featuredItem1 from 'Brands/BigDog/images/BigdogMeasuringTape.jpg';
import featuredItem2 from 'Brands/BigDog/images/BigdogPointShovel.jpg';
import featuredItem3 from 'Brands/BigDog/images/BigdogTorpedoLevel.jpg';
import featuredItem4 from 'Brands/BigDog/images/BigdogToolPouch.jpg';
import featuredItem5 from 'Brands/BigDog/images/BigdogTieDownStrap.jpg';

import { links } from 'utils/links';

type BigDogFeaturedItems = {
  title: string;
  image: string;
  url: string;
};

const titles = [
  '26 ft Magnetic Tape Measure',
  'Round Point Shovel',
  'Torpedo Level',
  'Multi Purpose Tool Pouch',
  'Ratchet Tie Down Strap'
];
const images = [
  featuredItem1,
  featuredItem2,
  featuredItem3,
  featuredItem4,
  featuredItem5
];
const urls = links.bigDog.featuredItems;

export const FeaturedItems: BigDogFeaturedItems[] = titles.map(
  (item, index) => {
    const image = images[index];
    const url = urls[index];
    const title = item;
    return { title, image, url };
  }
);
