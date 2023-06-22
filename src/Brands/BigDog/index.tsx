import { Container } from '@dialexa/reece-component-library';

import BigDogHeader from 'Brands/BigDog/sections/BigDogHeader';
import BigDogFeaturedItems from 'Brands/BigDog/sections/BigDogFeaturedItems';
import BigDogMission from 'Brands/BigDog/sections/BigDogMission';
import BigDogFooter from 'Brands/BigDog/sections/BigDogFooter';

function BigDogMarketing() {
  /**
   * Render
   */
  return (
    <Container
      disableGutters
      sx={{ backgroundColor: 'common.white' }}
      style={{ maxWidth: 1440 }}
    >
      <BigDogHeader />
      <BigDogMission />
      <BigDogFeaturedItems />
      <BigDogFooter />
    </Container>
  );
}

export default BigDogMarketing;
