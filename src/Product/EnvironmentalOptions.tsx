import { Box, Grid, useScreenSize } from '@dialexa/reece-component-library';

import WaterSense from 'images/ws-logo-seal.png';
import EnergyStar from 'images/energy_star.png';
import LeadFree from 'images/lead_free.png';
import HazardousMaterial from 'images/hazardous_material.png';
import MecuryFree from 'images/mercury_free.png';
import { Product } from 'generated/graphql';
import { EnvironmentalImage } from 'Product/util/styled';

const LOW_LEAD_COMPLIANT = 'Low lead compliant';
const MERCURY_FREE = 'Mercury free';
const WATER_SENSE_COMPLIANT = 'WaterSense compliant';
const ENERGY_STAR = 'Energy Star';
const HAZARDOUS_MATERIAL = 'Hazardous material';

type Props = {
  product?: Product | null;
};

function EnvironmentalOptions({ product }: Props) {
  const { isSmallScreen } = useScreenSize();

  const hasOption = (input: string) =>
    !!product?.environmentalOptions?.includes(input);

  return (
    <>
      <Box pt={!isSmallScreen ? 3 : 0}>
        <Grid
          container
          justifyContent={isSmallScreen ? 'center' : 'flex-start'}
        >
          {hasOption(WATER_SENSE_COMPLIANT) && (
            <Grid item xs={2}>
              <EnvironmentalImage alt="WaterSense" src={WaterSense} />
            </Grid>
          )}
          {hasOption(ENERGY_STAR) && (
            <Grid item xs={2}>
              <EnvironmentalImage alt="EnergyStar" src={EnergyStar} />
            </Grid>
          )}
          {hasOption(LOW_LEAD_COMPLIANT) && (
            <Grid item xs={2}>
              <EnvironmentalImage alt="LeadFree" src={LeadFree} />
            </Grid>
          )}
          {hasOption(HAZARDOUS_MATERIAL) && (
            <Grid item xs={2}>
              <EnvironmentalImage
                alt="HazardousMaterial"
                src={HazardousMaterial}
              />
            </Grid>
          )}
          {hasOption(MERCURY_FREE) && (
            <Grid item xs={2}>
              <EnvironmentalImage alt="MecuryFree" src={MecuryFree} />
            </Grid>
          )}
        </Grid>
      </Box>
    </>
  );
}

export default EnvironmentalOptions;
