import React, { useEffect, useState } from 'react';
import { StyleSheet } from 'react-native';
import { ApolloError } from '@apollo/client';

import {
  useUpdateCountMutation,
  UpdateCountMutation,
  ItemInput,
  GetLocationDocument,
} from 'api';

import { validateQuantity } from 'utils/validation';
import { getImageUri, orNA } from 'utils/stringUtils';
import { useOverlay } from 'providers/Overlay';

import { Text } from 'components/Text';
import { Product } from 'components/Product';
import { getError } from 'utils/error';
import { ErrorType } from 'constants/error';
import { isNumber, toString } from 'lodash';
import { LocationItemsBanner } from 'components/LocationItemsBanner';
import { SvgIcons } from 'components/SVG';
import { useLocation } from 'hooks/useLocation';
import { DetailComponent } from './DetailComponent';
import { getInitialValue } from './utils';
import { useConfig } from 'hooks/useConfig';
import { handleMutationComplete, isMincron } from 'utils/apollo';
import { RouteNames } from 'constants/routes';
import { AppScreenProps } from 'navigation/types';
import { ScreenLayout } from 'components/ScreenLayout';
import { getScreenTestingIds } from 'test-utils/testIds';
import useRenderListener from 'hooks/useRenderListener';

const LocationItemDetail = ({
  navigation,
  route,
}: AppScreenProps<'LocationItemDetail'>) => {
  useRenderListener();

  const item = route.params.item;
  const [{ count }] = useConfig();

  const { location, loading: loadingLocation, refetchLocation } = useLocation({
    onCompleted: () => {
      navigation.navigate(RouteNames.LOCATION_ITEMS);
    },
  });

  const [value, setValue] = useState(getInitialValue(item));
  const [valid, setValid] = useState(true);

  const { showAlert } = useOverlay();

  const [updateCount, { loading }] = useUpdateCountMutation({
    refetchQueries: [
      {
        query: GetLocationDocument,
        variables: {
          id: location?.id || '',
        },
      },
    ],
    awaitRefetchQueries: true,
    onCompleted: (resp: UpdateCountMutation) => {
      handleMutationComplete(resp.updateCount);
      if (item.tagNum) {
        refetchLocation();
      }
    },
    onError: (error: ApolloError) => {
      showAlert(getError(ErrorType.UPDATE_COUNT, error));
    },
  });

  useEffect(() => {
    setValue(item?.quantity || null);
  }, [item]);

  const onUpdatePress = () => {
    const input: ItemInput = {
      productId: item.tagNum || '',
      locationId: location?.id || '',
      quantity: value,
    };
    updateCount({ variables: { item: input } });
  };

  const handleTextChange = (text: string) => {
    const validation = validateQuantity(text);
    const quantity =
      validation.valid && text.length ? parseInt(text, 10) : null;
    setValid(validation.valid);
    setValue(quantity);
  };

  if (!location || !item) {
    return (
      <ScreenLayout>
        <ScreenLayout.StaticContent centered>
          <Text>There was a problem</Text>
        </ScreenLayout.StaticContent>
      </ScreenLayout>
    );
  }

  const testIds = getScreenTestingIds('LocationItemDetail');

  return (
    <ScreenLayout
      testID={testIds.component}
      loading={loading || loadingLocation}
      pageAction={{
        disabled: !valid || !isNumber(value),
        title: location.committed ? 'Save Recount Quantity' : 'Update',
        onPress: onUpdatePress,
      }}
    >
      <LocationItemsBanner
        title={location.committed ? 'Inventory Recount' : 'Update Inventory'}
        isRecount={location.committed}
        testID={testIds.banner}
      />
      <ScreenLayout.ScrollContent testID={testIds.scrollContent}>
        <Product
          style={[
            styles.productContainer,
            location?.committed && styles.productContainerRecount,
          ]}
        >
          <Product.Header
            title={orNA(item.prodDesc)}
            testID={testIds.component}
          />
          <Product.Image
            src={getImageUri(item.productImageUrl)}
            PlaceholderComponent={
              <SvgIcons name={'PlaceholderWithLabelImage'} size={128} />
            }
            testID={testIds.component}
          />
          <Product.Content>
            <DetailComponent
              item={item}
              valid={valid}
              value={toString(value)}
              location={location}
              onChange={handleTextChange}
              mincron={isMincron(count)}
              testID={testIds.component}
            />
          </Product.Content>
        </Product>
      </ScreenLayout.ScrollContent>
    </ScreenLayout>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, width: '100%' },
  productContainer: {
    flex: 1,
    width: '100%',
    paddingTop: 0,
  },
  productContainerRecount: {
    paddingTop: 15,
  },
});

export default LocationItemDetail;
