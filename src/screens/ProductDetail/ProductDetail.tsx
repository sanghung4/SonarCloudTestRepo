import React, { useEffect, useState } from 'react';
import { StyleSheet, View } from 'react-native';
import { Colors, FontWeight } from 'constants/style';
import { Text } from 'components/Text';
import { useAddToCountMutation } from 'api';
import { useOverlay } from 'providers/Overlay';
import { getError } from 'utils/error';
import { ErrorType } from 'constants/error';
import { getImageUri, orNA } from 'utils/stringUtils';
import { SvgIcons } from 'components/SVG';
import { Input } from 'components/Input';
import { validateQuantity } from 'utils/validation';
import { useLocation } from 'hooks/useLocation';
import { testIds } from './utils';
import {
  handleMutationComplete,
  isMincron,
  transformKourierProduct,
  transformProduct,
} from 'utils/apollo';
import { useLoading } from 'hooks/useLoading';
import { RouteNames } from 'constants/routes';
import { InputLength } from 'constants/form';
import { useConfig } from 'hooks/useConfig';
import { Image } from 'components/Image';
import { AppScreenProps } from 'navigation/types';
import { ScreenLayout } from 'components/ScreenLayout';
import { CustomButton } from 'components/CustomButton';
import useRenderListener from 'hooks/useRenderListener';
import { getScreenTestingIds } from 'test-utils/testIds';

const ProductDetail = ({
  navigation,
  route,
}: AppScreenProps<'ProductDetail'>) => {
  
  useRenderListener();

  const testIds = getScreenTestingIds('AddProductDetail');

  const { showAlert, toggleLoading } = useOverlay();
  const [{ count }] = useConfig();
  const { location, loading: locationLoading, refetchLocation } = useLocation({
    onCompleted: () => {
      toggleLoading(false);
      navigation.navigate(RouteNames.LOCATION_ITEMS);
    },
  });

  const product = route.params.product;
  const [qtyValue, setQtyValue] = useState('');
  const [isQtyValid, setIsQtyValid] = useState(
    !qtyValue ? false : validateQuantity(qtyValue).valid
  );

  useEffect(() => {
    setIsQtyValid(!qtyValue ? false : validateQuantity(qtyValue).valid);
  }, [qtyValue]);

  const [addToCount, { loading }] = useAddToCountMutation({
    onCompleted: (resp) => {
      handleMutationComplete(resp.addToCount);
      refetchLocation();
    },
    onError: async (error) => {
      showAlert(getError(ErrorType.ADD_TO_COUNT, error));
    },
  });

  useLoading([loading, locationLoading]);

  
  
  if (!location || !product) {
    return (
      <ScreenLayout testID={testIds.noProduct}>
        <ScreenLayout.StaticContent centered>
          <Text>There was a problem</Text>
        </ScreenLayout.StaticContent>
      </ScreenLayout>
    );
  }

  const productItem = transformKourierProduct(product);

  const onAddProductPress = () => {
    if (!location || !isQtyValid) {
      return;
    }

    const item = {
      productId: productItem.productNumber || '',
      locationId: location.id,
      quantity: parseInt(qtyValue, 10),
    };
    addToCount({ variables: { item } });
  };

  return (
    <ScreenLayout testID={testIds.product}>
      <ScreenLayout.ScrollContent banner={{ title: 'Add Product' }} padding>
        {/* Name */}
        <Text fontSize={14} fontWeight={FontWeight.MEDIUM} selectable testID={`${testIds.textName}`}>
          {orNA(productItem.displayField)}
        </Text>

        {/* Image */}
        <View style={styles.imageWrapper}>
          <Image
            imageUri={getImageUri(productItem.productImageUrl)}
            style={styles.image}
            resizeMode="contain"
            testID={`${testIds.productImage}`}
            placeholder={
              <SvgIcons name={'PlaceholderWithLabelImage'} size={128} />
            }
            placeholderStyle={styles.placeholder}
          />
        </View>

        {/* Details */}
        <View style={styles.detailsWrapper}>
          {/* Product Number */}
          {productItem.productNumber ? (
            <Text
              fontSize={14}
              fontWeight={FontWeight.MEDIUM}
              color={Colors.SECONDARY_2100}
              style={styles.detailText}
              testID={`${testIds.textPN}`}
            >
              PN# {productItem.productNumber}
            </Text>
          ) : null}
          {/* Catalog Number */}
          {productItem.catalogNumber ? (
            <Text
              fontSize={14}
              fontWeight={FontWeight.MEDIUM}
              color={Colors.SECONDARY_2100}
              style={styles.detailText}
              testID={`${testIds.textCAT}`}
            >
              CAT# {productItem.catalogNumber}
            </Text>
          ) : null}
          {/* Product Item */}
          {productItem.upc ? (
            <Text
              fontSize={14}
              fontWeight={FontWeight.MEDIUM}
              color={Colors.SECONDARY_2100}
              style={styles.detailText}
              testID={`${testIds.textUPC}`}
            >
              UPC# {productItem.upc}
            </Text>
          ) : null}
        </View>
        {/* Quantity Input */}
        <Input
          testID={`${testIds.productDetailQty}`}
          value={qtyValue}
          label="Quantity*"
          containerStyle={styles.inputContainerStyle}
          keyboardType="number-pad"
          onChangeText={(text: string) => {
            setQtyValue(text);
          }}
          errorMessage={isQtyValid ? '' : 'Must be a whole number.'}
          maxLength={
            isMincron(count) ? InputLength.MINCRON : InputLength.ECLIPSE
          }
        />
        {/* Page actions */}
        <View style={styles.actionsWrapper}>
          <CustomButton
            title="Back"
            type="secondary"
            onPress={navigation.goBack}
            disabled={loading}
            containerStyle={styles.actionLeft}
            testID={`${testIds.buttonBack}`}
          />

          <CustomButton
            title="Add"
            onPress={onAddProductPress}
            disabled={!isQtyValid || loading}
            loading={loading || locationLoading}
            containerStyle={styles.actionRight}
            testID={`${testIds.buttonAdd}`}
          />
        </View>
      </ScreenLayout.ScrollContent>
    </ScreenLayout>
  );
};

const styles = StyleSheet.create({
  inputContainerStyle: {
    width: '100%',
    paddingHorizontal: 0,
    marginTop: 12,
  },
  imageWrapper: { alignItems: 'center' },
  image: { height: 128, width: 128 },
  placeholder: { backgroundColor: 'transparent' },
  detailsWrapper: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    flexWrap: 'wrap',
  },
  detailText: {
    flexBasis: '50%',
    marginBottom: 12,
  },
  actionsWrapper: {
    flexDirection: 'row',
    marginTop: 32,
  },
  actionLeft: {
    flex: 1,
    paddingRight: 8,
  },
  actionRight: {
    flex: 1,
    paddingLeft: 8,
  },
});

export default ProductDetail;
