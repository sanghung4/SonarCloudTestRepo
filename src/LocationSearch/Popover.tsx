import React, { useMemo } from 'react';

import { Box, Grid, Link, Typography } from '@dialexa/reece-component-library';
import { InfoWindow } from '@react-google-maps/api';
import { useTranslation } from 'react-i18next';

import { Branch } from 'generated/graphql';
import { COMING_SOON } from 'LocationSearch';

type Props = {
  branch: Branch;
  position: google.maps.LatLng;
  onCloseClick: () => void;
};

function Popover(props: Props) {
  /**
   * Custom Hooks
   */
  const { t } = useTranslation();

  /**
   * Memos
   */
  const directionsUrl = useMemo(directionsUrlMemo, [props.branch]);
  const address = useMemo(addressMemo, [
    props.branch.address1,
    props.branch.address2,
    props.branch.city,
    props.branch.state,
    props.branch.zip
  ]);

  return (
    <InfoWindow
      position={{
        lat: props.position.lat() ?? 0,
        lng: props.position.lng() ?? 0
      }}
      onCloseClick={props.onCloseClick}
      options={{
        pixelOffset: new google.maps.Size(0, -15)
      }}
    >
      <Box
        overflow="hidden"
        py={1}
        px={2}
        data-testid={`google-maps-box-${props?.branch?.branchId}`}
      >
        <Grid container spacing={1} direction="column">
          <Grid item>
            <Typography
              variant="h5"
              color="primary"
              data-testid={`google-maps-branch-name-${props?.branch?.branchId}`}
            >
              {props.branch.name}
            </Typography>
          </Grid>
          <Grid item>
            <Typography
              variant="body2"
              data-testid={`google-maps-branch-address-${props?.branch?.branchId}`}
            >
              {address}
            </Typography>
          </Grid>
          <Grid item container spacing={1}>
            <Grid item>
              <Typography variant="body2">{t('common.hours')}:</Typography>
            </Grid>
            <Grid item data-testid={`google-maps-branch-hours`}>
              {props.branch.businessHours?.split('; ').map((h, i) => (
                <Typography
                  key={i}
                  color={h === COMING_SOON ? 'success' : 'inherit'}
                  variant="body2"
                >
                  {h.replace(':', '')}
                </Typography>
              )) ?? (
                <Typography variant="body2">
                  {t('locationSearch.noHours')}
                </Typography>
              )}
            </Grid>
          </Grid>
          <Grid item>
            <Link
              href={`tel:${props.branch.phone}`}
              sx={{
                color: 'primary02.main',
                fontWeight: 500
              }}
              data-testid={`google-maps-branch-phone-${props?.branch?.branchId}`}
            >
              {props.branch.phone}
            </Link>
          </Grid>
          <Grid item container alignItems="flex-end">
            <Grid item xs>
              <Link
                href={directionsUrl}
                target="_blank"
                rel="noreferer"
                sx={{ color: 'primary02.main', pt: 1.5 }}
                data-testid={`google-maps-branch-directions-${props?.branch?.branchId}`}
              >
                {t('locationSearch.directions')}
              </Link>
            </Grid>
            <Grid
              item
              data-testid={`google-maps-branch-distance-${props?.branch?.branchId}`}
            >
              {`${props.branch?.distance?.toFixed(1)} ${t('common.miles')}`}
            </Grid>
          </Grid>
        </Grid>
      </Box>
    </InfoWindow>
  );

  /**
   * Memo defs
   */

  function directionsUrlMemo() {
    let address = `${props.branch.address1} ${props.branch.address2 ?? ''} ${
      props.branch.city
    } ${props.branch.state} ${props.branch.zip}`;
    return `https://www.google.com/maps/dir/?api=1&destination=${encodeURIComponent(
      address
    )}`;
  }
  function addressMemo() {
    const secondaryAddress = props.branch.address2
      ? ` ${props.branch.address2}`
      : '';
    const cityStateZip = `, ${props.branch.city}, ${props.branch.state} ${props.branch.zip}`;
    return `${props.branch.address1}${secondaryAddress}${cityStateZip}`;
  }
}

export default Popover;
