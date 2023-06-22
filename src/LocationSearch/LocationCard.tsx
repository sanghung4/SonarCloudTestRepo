import React, { useState } from 'react';

import {
  Box,
  Button,
  Collapse,
  Grid,
  Link,
  Skeleton,
  Typography
} from '@dialexa/reece-component-library';
import { Link as RouterLink } from 'react-router-dom';

import { useTranslation } from 'react-i18next';

import { Branch } from 'generated/graphql';
import { LocationBubbleIcon } from 'icons';
import { COMING_SOON } from 'LocationSearch';
import { ChevronUpIcon, ChevronDownIcon } from 'icons';
import { EHG_SCHEDULE_URL } from 'Home/Landing';

type Props = {
  loading?: boolean;
  branch?: Branch;
  index: number;
};

function LocationCard(props: Props) {
  /**
   * Custom hooks
   */
  const { t } = useTranslation();

  /**
   * State
   */
  const [locationDetails, setLocationDetails] = useState(false);

  return (
    <Grid
      container
      spacing={1}
      data-testid={`branch-number-in-list-${props.branch?.branchId}`}
    >
      <Grid item xs="auto">
        <Box position="relative">
          <LocationBubbleIcon />
          <Box
            position="absolute"
            top={2}
            textAlign="center"
            width={26}
            sx={{ color: 'primary02.main' }}
            data-testid={`branch-icon-${props.index}`}
          >
            {props.index}
          </Box>
        </Box>
      </Grid>
      <Grid item container direction="column" xs spacing={1}>
        <Grid item>
          <Typography
            variant="h5"
            color="primary"
            data-testid={`branch-name-${props.index}`}
          >
            {props.loading ? <Skeleton /> : props.branch?.name}
          </Typography>
        </Grid>
        <Grid item>
          <Typography
            variant="body1"
            color="primary"
            data-testid={`branch-address-one-${props.index}`}
          >
            {props.loading ? <Skeleton /> : props.branch?.address1}
          </Typography>
          <Typography
            variant="body1"
            color="primary"
            data-testid={`branch-address-two-${props.index}`}
          >
            {props.loading ? <Skeleton /> : props.branch?.address2}
          </Typography>
          <Typography
            variant="body1"
            color="primary"
            data-testid={`city-state-zipcode-${props.index}`}
          >
            {props.loading ? (
              <Skeleton />
            ) : (
              `${props.branch?.city}, ${props.branch?.state} ${props.branch?.zip}`
            )}
          </Typography>
        </Grid>
        {props.loading ? (
          <Skeleton />
        ) : (
          Boolean(props.branch?.phone || props.branch?.businessHours) && (
            <Collapse in={locationDetails} sx={{ m: '0 .5em' }}>
              <Grid item container spacing={1}>
                <Grid item>
                  <Typography
                    color="primary02.main"
                    fontWeight="700"
                    marginTop={2}
                  >
                    {t('common.hours')}:
                  </Typography>
                </Grid>
                <Grid item>
                  {props.loading ? (
                    <Skeleton />
                  ) : (
                    props.branch?.businessHours?.split('; ').map((h, i) => (
                      <Typography
                        key={i}
                        color={h === COMING_SOON ? 'success' : 'inherit'}
                        data-testid={`branch-hours-${props.index}`}
                        marginTop={i === 0 ? 2 : 0.5}
                      >
                        {h.replace(':', '')}
                      </Typography>
                    )) ?? <Typography>{t('locationSearch.noHours')}</Typography>
                  )}
                </Grid>
              </Grid>
              <Grid item marginTop={1}>
                <Typography color="primary02.main" fontWeight={700}>
                  {t('common.phone')}:{' '}
                  <Box display="inline" component="span">
                    {props.loading ? (
                      <Skeleton />
                    ) : (
                      <Link
                        href={`tel:${props.branch?.phone}`}
                        color="text.primary"
                        fontWeight={400}
                        data-testid={`branch-phone-${props.index}`}
                      >
                        {props.branch?.phone}
                      </Link>
                    )}
                  </Box>
                </Typography>
              </Grid>
            </Collapse>
          )
        )}
        <Grid item container spacing={1}>
          <Grid item>
            <Button
              data-testid={`${props.branch?.branchId}${
                locationDetails ? `-hide-details` : `-view-location-details`
              }`}
              onClick={() => setLocationDetails(!locationDetails)}
              color="gray"
              iconColor="primary"
              startIcon={
                locationDetails ? <ChevronUpIcon /> : <ChevronDownIcon />
              }
              variant="text"
              size="small"
              sx={{ mt: '1rem', p: 0, whiteSpace: 'nowrap' }}
            >
              <Typography
                color={locationDetails ? 'primary.main' : 'primary02.main'}
                fontWeight={locationDetails ? undefined : '700'}
              >
                {locationDetails
                  ? t('locationSearch.hideDetails')
                  : t('locationSearch.locationDetails')}
              </Typography>
            </Button>
          </Grid>
          {Boolean(props.branch?.isBandK) && (
            <Grid item container alignItems="flex-end" xs>
              <Link
                to={EHG_SCHEDULE_URL}
                component={RouterLink}
                target="_blank"
              >
                <Button size="small" sx={{ whiteSpace: 'nowrap' }}>
                  {t('home.scheduleNow')}
                </Button>
              </Link>
            </Grid>
          )}
        </Grid>
      </Grid>
      <Grid
        item
        xs="auto"
        container
        alignItems="center"
        justifyContent="flex-end"
      >
        <Box pr={1} data-testid={`branch-distance-${props.index}`}>
          {props.loading ? (
            <Skeleton />
          ) : (
            `${props.branch?.distance?.toFixed(1)} ${t('common.miles')}`
          )}
        </Box>
      </Grid>
    </Grid>
  );
}

export default LocationCard;
