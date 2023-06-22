import React, {
  ChangeEvent,
  Fragment,
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useRef,
  useState
} from 'react';

import {
  Box,
  Button,
  Divider,
  FormControl,
  FormControlLabel,
  Grid,
  Hidden,
  InputLabel,
  MenuItem,
  Pagination,
  RadioGroup,
  Select,
  styled,
  TextField,
  Typography,
  useScreenSize,
  useTheme
} from '@dialexa/reece-component-library';
import {
  Autocomplete,
  GoogleMap,
  Marker,
  useLoadScript
} from '@react-google-maps/api';
import { useTranslation } from 'react-i18next';
import { useCallbackRef } from 'hooks/useCallbackRef';
import { stateCode } from 'utils/states';
import { AuthContext } from 'AuthProvider';
import Loader from 'common/Loader';
import {
  Branch,
  MileRadiusEnum,
  useFindBranchesQuery,
  useGetHomeBranchQuery
} from 'generated/graphql';
import LocationBubbleFilled from 'icons/location-bubble-filled.svg';
import LocationBubbleFilledGreen from 'icons/location-bubble-filled-green.svg';
import LocationBubbleFilledRed from 'icons/location-bubble-filled-red.svg';
import LocationCard from 'LocationSearch/LocationCard';
import Popover from 'LocationSearch/Popover';
import { Configuration } from 'utils/configuration';
import { useGeolocation } from 'hooks/useGeolocation';
import { GpsIcon } from 'icons';
import { SelectChangeEvent } from '@mui/material/Select';
import useDocumentTitle from 'hooks/useDocumentTitle';
import { radio } from 'utils/inputTestId';
import { useSelectedAccountsContext } from 'providers/SelectedAccountsProvider';

/**
 * Constants
 */
export const COMING_SOON = 'Coming Soon';
export const PERMANENTLY_CLOSED = 'Permanently Closed';
const LIBRARIES = ['places'];
const PAGE_SIZE = 5;
const NA_CENTER = {
  LATITUDE: 39.5,
  LONGITUDE: -98.35
};
const GridStyled = styled(Grid)(({ theme }) => ({
  height: `calc(170vh - 7.5rem)`,
  [theme.breakpoints.down('md')]: {
    height: 'auto'
  }
}));
const RadioGroupStyled = styled(RadioGroup)(({ theme }) => ({
  justifyContent: 'flex-start',
  [theme.breakpoints.down('md')]: {
    justifyContent: 'space-around'
  }
}));

/**
 * Types
 */
type Location = { lat: number; lng: number };
type View = 'map' | 'list';
const filters = ['none', 'plumbing', 'hvac', 'waterworks', 'bandk'] as const;
type Filter = typeof filters[number];
type SelectedBranch = {
  branch: Branch;
  latLng: google.maps.LatLng | null;
};

function LocationSearch() {
  /**
   * Custom hooks
   */
  const { isSmallScreen } = useScreenSize();
  const { t } = useTranslation();
  const { selectedAccounts } = useSelectedAccountsContext();
  const { profile } = useContext(AuthContext);
  const { isLoaded: googleLoaded } = useLoadScript({
    googleMapsApiKey: Configuration.googleApiKey || 'no-key',
    // @ts-ignore
    libraries: LIBRARIES
  });
  const { position, positionLoading } = useGeolocation({});
  const theme = useTheme();
  useDocumentTitle(t('common.findALocation'));

  /**
   * Refs
   */
  const [inputEl, inputElCallback] = useCallbackRef<HTMLInputElement>();
  const mapInstance = useRef<google.maps.Map | null>(null);

  /**
   * State
   */
  const [autocomplete, setAutocomplete] =
    useState<google.maps.places.Autocomplete>();
  const [inputError, setInputError] = useState(false);
  const [place, setPlace] = useState<Location>();
  const [page, setPage] = useState(1);
  const [view, setView] = useState<View>('list');
  const [filter, setFilter] = useState<Filter>('none');
  const [distance, setDistance] = useState<MileRadiusEnum>(
    MileRadiusEnum.Miles_25
  );
  const [selectedBranch, setSelectedBranch] = useState<SelectedBranch | null>(
    null
  );

  /**
   * Data
   */
  const {
    data: findBranchesQuery,
    loading: branchesLoading,
    called: branchesCalled
  } = useFindBranchesQuery({
    skip: positionLoading && !place,
    variables: {
      branchSearch: {
        latitude: place ? place.lat : position?.coords.latitude ?? 0,
        longitude: place ? place.lng : position?.coords.longitude ?? 0,
        count: 100,
        branchSearchRadius: distance,
        isStoreFinder: true
      }
    },
    onCompleted: () => {
      setPage(1);
    }
  });

  const { data: homeBranchQuery, loading: homeBranchLoading } =
    useGetHomeBranchQuery({
      variables: {
        shipToAccountId: selectedAccounts?.shipTo?.id ?? ''
      },
      skip: positionLoading && !position
    });

  /**
   * Memos
   */
  const filteredData = useMemo(filteredDataMemo, [filter, findBranchesQuery]);

  /**
   * Effects
   */
  useEffect(setPlaceIfPositionIsSet, [
    autocomplete,
    position,
    t,
    inputEl,
    homeBranchQuery?.homeBranch,
    profile?.userId,
    homeBranchQuery
  ]);

  useEffect(() => {
    if (!page) return;
    // istanbul ignore next
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }, [page]);

  /**
   * Callbacks
   */
  const onMapLoad = useCallback(onMapLoadCb, []);

  return googleLoaded ? (
    <Box sx={{ bgcolor: 'common.white' }}>
      <GridStyled container>
        <Grid
          item
          md={5}
          xs={12}
          sx={{
            maxHeight: 1,
            px: isSmallScreen ? 2 : 5
          }}
        >
          <Grid
            container
            spacing={2}
            direction="column"
            wrap="nowrap"
            sx={{
              height: isSmallScreen ? 'auto' : '100%'
            }}
          >
            <Grid item>
              <Box pt={4}>
                <Typography
                  color="primary"
                  component="h1"
                  variant="h4"
                  data-testid="branch-locations-title"
                >
                  {t('locationSearch.header')}
                </Typography>
              </Box>
            </Grid>
            <Grid item>
              <Typography
                variant="body2"
                data-testid="location-search-helpText"
              >
                {t('locationSearch.helpText')}
              </Typography>
            </Grid>
            <Grid item>
              <Autocomplete
                onLoad={(test) => setAutocomplete(test)}
                onPlaceChanged={handlePlaceChanged}
                types={['postal_code', 'locality', 'sublocality']}
                fields={['geometry']}
                restrictions={{ country: ['us'] }}
              >
                <TextField
                  label={t('locationSearch.searchLabel')}
                  placeholder={t('locationSearch.searchPlaceholder')}
                  data-testid="location-search-input"
                  fullWidth
                  onFocus={handleAutocompleteFocus}
                  error={inputError}
                  helperText={
                    inputError ? t('locationSearch.zipCityInvalid') : null
                  }
                  inputRef={inputElCallback}
                  InputProps={{
                    startAdornment: (
                      <Box pr={1}>
                        <Box
                          component={GpsIcon}
                          sx={{
                            color:
                              inputEl?.value === t('locationSearch.current')
                                ? 'primary02.main'
                                : 'lightGray.main'
                          }}
                        />
                      </Box>
                    ),
                    endAdornment: (
                      <Button
                        variant="inline"
                        color="primaryLight"
                        onClick={() => {
                          setPlace(undefined);
                          setSelectedBranch(null);
                          if (inputEl) {
                            inputEl.value = '';
                            inputEl.focus();
                          }
                        }}
                        data-testid="clear-button"
                      >
                        {t('common.clear').toLocaleLowerCase()}
                      </Button>
                    )
                  }}
                />
              </Autocomplete>
            </Grid>
            <Grid item container spacing={2}>
              <Grid item xs={8} md={12}>
                <Box py={2} width={1}>
                  <FormControl variant="outlined" fullWidth>
                    <InputLabel
                      id="branch-filter-label"
                      data-testid="location-types-label"
                      shrink
                    >
                      {t('locationSearch.locationTypesLabel')}
                    </InputLabel>
                    <Select
                      labelId="branch-filter-label"
                      id="branch-filter-select"
                      data-testid="branch-filter-select"
                      value={filter}
                      onChange={handleApplyFilter}
                      renderValue={(v) => t(`locationSearch.${v}`)}
                    >
                      {filters.map((f) => (
                        <MenuItem
                          value={f}
                          key={`branch-filter-select-${f}`}
                          data-testid={`${f}-list-item`}
                        >
                          {t(`locationSearch.${f}`)}
                        </MenuItem>
                      ))}
                    </Select>
                  </FormControl>
                </Box>
              </Grid>
              <Hidden mdUp>
                <Grid item xs={4} container alignContent="flex-end">
                  <Button
                    fullWidth
                    variant="secondary"
                    color="primaryLight"
                    onClick={() => setView(view === 'list' ? 'map' : 'list')}
                    sx={{
                      mb: 2.25,
                      height: 45
                    }}
                    data-testid="map-button"
                  >
                    {view === 'map'
                      ? t('locationSearch.listView')
                      : t('locationSearch.mapView')}
                  </Button>
                </Grid>
              </Hidden>
            </Grid>
            <Grid marginLeft={2} marginTop={-3}>
              <Box pt={4}>
                <Typography fontWeight={400} paddingBottom={1}>
                  {t('locationSearch.distance')}
                </Typography>
                <FormControl component="fieldset" sx={{ width: 'auto' }}>
                  <RadioGroupStyled
                    aria-label="distance-radius"
                    name="distanceRadius"
                    value={distance}
                    onChange={handleDistanceChange}
                    data-testid="distance-radius-options"
                  >
                    <Grid marginLeft={1}>
                      <Grid>
                        <FormControlLabel
                          value={MileRadiusEnum.Miles_25}
                          control={radio('distance')}
                          label={`${t('locationSearch.25Miles')}`}
                          sx={{ pr: 8 }}
                        />

                        <FormControlLabel
                          value={MileRadiusEnum.Miles_100}
                          control={radio('distance')}
                          label={`${t('locationSearch.100Miles')}`}
                          sx={{ pr: 2 }}
                        />
                      </Grid>

                      <Grid marginTop={1}>
                        <FormControlLabel
                          value={MileRadiusEnum.Miles_50}
                          control={radio('distance')}
                          label={`${t('locationSearch.50Miles')}`}
                          data-testid="distance-radius-50miles"
                          sx={{ pr: 8 }}
                        />
                        <FormControlLabel
                          value={MileRadiusEnum.Miles_200}
                          control={radio('distance')}
                          label={`${t('locationSearch.200Miles')}`}
                          sx={{ pr: 2 }}
                        />
                      </Grid>
                    </Grid>
                  </RadioGroupStyled>
                </FormControl>
              </Box>
            </Grid>

            {branchesCalled && (!isSmallScreen || view === 'list') ? (
              <>
                <Grid item>
                  <Typography variant="body1" data-testid="result-count">
                    {filteredData.length}{' '}
                    {t('common.result', {
                      count: filteredData.length
                    })}
                  </Typography>
                </Grid>
                <Grid item>
                  <Divider />
                </Grid>
                <Grid
                  item
                  sx={{
                    overflowY: 'scroll',
                    width: 1
                  }}
                >
                  {branchesLoading || positionLoading || homeBranchLoading
                    ? [0, 1, 2].map((x) => (
                        <LocationCard
                          loading
                          index={x + 1}
                          key={`branch-result-card-${x}`}
                        />
                      ))
                    : null}
                  {filteredData.length ? (
                    filteredData
                      .map((b, i, array) => (
                        <Fragment
                          key={`branch-result-card-filtered-${b.branchId}`}
                        >
                          <Box py={3} pr={1}>
                            <LocationCard index={i + 1} branch={b as Branch} />
                          </Box>
                          {i % PAGE_SIZE < PAGE_SIZE - 1 &&
                          i < array.length - 1 ? (
                            <Divider />
                          ) : null}
                        </Fragment>
                      ))
                      .slice((page - 1) * PAGE_SIZE, page * PAGE_SIZE)
                  ) : (
                    <Typography
                      align="center"
                      variant="h6"
                      sx={{
                        color: 'secondary03.main',
                        py: 3
                      }}
                      data-testid="no-results-message"
                    >
                      {t('locationSearch.noResults')}
                    </Typography>
                  )}
                </Grid>
                {page && filteredData.length ? (
                  <>
                    <Grid item>
                      <Divider />
                    </Grid>
                    <Grid
                      container
                      item
                      direction="row"
                      justifyContent="space-between"
                      alignItems="center"
                    >
                      <Typography
                        variant="body1"
                        data-testid="record-count-footer"
                      >
                        {filteredData.length}&nbsp;
                        {t('common.result', {
                          count: filteredData.length
                        })}
                      </Typography>
                      <Pagination
                        current={page}
                        count={Math.ceil(filteredData.length / PAGE_SIZE)}
                        ofText={t('common.of')}
                        onChange={setPage}
                        onNext={setPage}
                        onPrev={setPage}
                        data-testid="pagination-container"
                        dataTestIdCurrentPage="current-page-number"
                        dataTestIdTotalNumberOfPages="total-number-of-pages"
                      />
                    </Grid>
                  </>
                ) : null}
                {isSmallScreen ? <Grid item sx={{ height: 40 }} /> : null}
              </>
            ) : null}
          </Grid>
        </Grid>
        {!isSmallScreen || view === 'map' ? (
          <Grid item md={7} xs={12}>
            <GoogleMap
              options={{
                streetViewControl: false
              }}
              onLoad={onMapLoad}
              mapContainerStyle={{
                height: isSmallScreen ? '60vh' : '100%',
                width: '100%'
              }}
              center={{
                lat: !findBranchesQuery?.branchSearch
                  ? NA_CENTER.LATITUDE
                  : findBranchesQuery?.branchSearch.latitude ?? 0,
                lng: !findBranchesQuery?.branchSearch
                  ? NA_CENTER.LONGITUDE
                  : findBranchesQuery?.branchSearch.longitude ?? 0
              }}
              zoom={!findBranchesQuery ? 4 : 10}
            >
              {branchesLoading || positionLoading || homeBranchLoading ? (
                <Loader backdrop size="parent" />
              ) : null}
              {selectedBranch && selectedBranch.latLng ? (
                <Popover
                  branch={selectedBranch.branch}
                  position={selectedBranch.latLng}
                  onCloseClick={() => setSelectedBranch(null)}
                />
              ) : null}
              {filteredData.map((b, idx) =>
                b.latitude && b.longitude ? (
                  <Marker
                    key={`branch-result-gmap-${b.branchId}-${idx}`}
                    icon={{
                      anchor: new google.maps.Point(21, 15),
                      labelOrigin: new google.maps.Point(21, 15),
                      url: getMarkerUrl(
                        b.businessHours ?? t('locationSearch.noHours')
                      )
                    }}
                    label={{
                      text: (idx + 1).toString(),
                      color: theme.palette.primary.contrastText,
                      fontWeight: 'bold',
                      fontSize: '1rem'
                    }}
                    onClick={(e) => {
                      setSelectedBranch({
                        branch: b as Branch,
                        latLng: e.latLng
                      });
                    }}
                    position={{ lat: b.latitude, lng: b.longitude }}
                    title={`map-icon-${b.branchId}` ?? ''}
                    data-testid={`map-icon-${b.branchId}` ?? ''}
                    zIndex={idx}
                  />
                ) : null
              )}
            </GoogleMap>
          </Grid>
        ) : null}
      </GridStyled>
    </Box>
  ) : null;

  /**
   * Event Callbacks
   */
  function handlePlaceChanged() {
    if (autocomplete && inputEl) {
      const placeRes = autocomplete.getPlace();

      /**
       * If the user pressed 'Enter' instead of clicking on a suggestion
       * then manually query the Places API with the user input
       */
      if (placeRes.place_id === undefined) {
        inputEl.blur();

        const predictionsRequest: google.maps.places.AutocompletionRequest = {
          input: inputEl.value,
          types: ['(regions)'],
          componentRestrictions: { country: ['us'] }
        };

        const autocompleteService =
          new google.maps.places.AutocompleteService();
        autocompleteService.getPlacePredictions(
          predictionsRequest,
          (predictions, status) => {
            if (
              predictions &&
              status === google.maps.places.PlacesServiceStatus.OK
            ) {
              const prediction = predictions[0].description;
              inputEl.value = prediction;
              const placesRequest: google.maps.places.FindPlaceFromQueryRequest =
                {
                  query: inputEl.value,
                  fields: ['geometry']
                };

              const placesService = new google.maps.places.PlacesService(
                mapInstance.current ?? document.createElement('div')
              );

              placesService.findPlaceFromQuery(
                placesRequest,
                (places, status) => {
                  if (
                    places &&
                    status === google.maps.places.PlacesServiceStatus.OK
                  ) {
                    setPlace({
                      lat: places[0].geometry?.location?.lat() ?? 0,
                      lng: places[0].geometry?.location?.lng() ?? 0
                    });
                  } else {
                    console.error('Places Request', status);
                  }
                }
              );
            } else {
              setInputError(true);
              setPlace(undefined);
            }
          }
        );
      } else {
        setPlace({
          lat: placeRes.geometry?.location?.lat() ?? 0,
          lng: placeRes.geometry?.location?.lng() ?? 0
        });
      }
    }
  }

  function handleAutocompleteFocus() {
    setInputError(false);
    if (inputEl) {
      inputEl.value = '';
    }
  }

  function handleApplyFilter(e: SelectChangeEvent<unknown>) {
    setFilter(e.target.value as Filter);
    setPage(1);
  }

  function handleDistanceChange(e: ChangeEvent<HTMLInputElement>) {
    setDistance(e.target.value as MileRadiusEnum);
  }

  /**
   * Effect Defs
   */

  function setPlaceIfPositionIsSet() {
    if (position && inputEl) {
      inputEl.value = t('locationSearch.current');
      setPlace({
        lat: position.coords.latitude ?? 0,
        lng: position.coords.longitude ?? 0
      });
    } else if (inputEl && profile?.userId) {
      let homeBranchDisplay = '';
      if (homeBranchQuery) {
        const { city, state } = homeBranchQuery.homeBranch;
        homeBranchDisplay = `${city}, ${stateCode(state ?? '') ?? state}`;
      }
      inputEl.value =
        homeBranchDisplay || t('locationSearch.homeBranchLocation');
      setPlace({
        lat: homeBranchQuery?.homeBranch.latitude ?? 0,
        lng: homeBranchQuery?.homeBranch.longitude ?? 0
      });
    }
  }

  /**
   * Memo defs
   */

  function filteredDataMemo() {
    let data = findBranchesQuery?.branchSearch.branches ?? ([] as Branch[]);

    return data.filter((b) => {
      switch (filter) {
        case 'waterworks':
          return b.isWaterworks;
        case 'hvac':
          return b.isHvac;
        case 'plumbing':
          return b.isPlumbing;
        case 'bandk':
          return b.isBandK;
        default:
          return true;
      }
    });
  }

  /**
   * Callback Defs
   */
  function onMapLoadCb(mapInstanceVal: google.maps.Map) {
    mapInstance.current = mapInstanceVal;
  }
}

// TODO: validate text w/ Tim Perez
function getMarkerUrl(businessHours: string) {
  switch (businessHours) {
    case COMING_SOON:
      return LocationBubbleFilledGreen;
    case PERMANENTLY_CLOSED:
      return LocationBubbleFilledRed;
    default:
      return LocationBubbleFilled;
  }
}

export default LocationSearch;
