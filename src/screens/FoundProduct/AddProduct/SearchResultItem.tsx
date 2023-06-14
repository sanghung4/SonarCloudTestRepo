import React from 'react';
import { StyleSheet, TouchableOpacity, View } from 'react-native';
import { ListItem } from 'react-native-elements';
import { Colors, fontSize, FontWeight } from 'constants/style';
import { Section } from 'components/Section';
import { Text } from 'components/Text';
import { SvgIcons } from 'components/SVG';
import { SearchResultProps } from './types';
import { transformProduct,transformKourierProduct } from 'utils/apollo';
import { getImageUri, removeLineBreaks } from 'utils/stringUtils';
import { Image } from 'components/Image';
import { getComponentTestingIds } from 'test-utils/testIds';

export const SearchResultItem = ({
  product,
  onPress,
  index,
  testID,
}: SearchResultProps) => {
  const productItem = transformKourierProduct(product);
  const testIds = getComponentTestingIds('SearchResultItem', testID);

  return (
    <ListItem key={product.productId} onPress={onPress}>
      <ListItem.Content style={styles.listItemContent}>
        <TouchableOpacity onPress={onPress}>
          <Image
            imageUri={getImageUri(product.productImageUrl)}
            placeholder={
              <SvgIcons
                name={'PlaceholderImage'}
                size={64}
                style={styles.listItemPlaceholder}
              />
            }
            style={styles.listItemImage}
            resizeMode="contain"
          />
          <Text
            fontSize={fontSize.SMALL}
            color={Colors.PRIMARY_2100}
            style={{ alignSelf: 'center', marginRight: 16 }}
          >
            Details
          </Text>
        </TouchableOpacity>
        <Section flex={1} paddingLeft={16}>
          {productItem.displayField ? (
            <Text
              numberOfLines={4}
              ellipsizeMode="tail"
              fontSize={14}
              fontWeight={FontWeight.MEDIUM}
              color={Colors.PRIMARY_3100}
              testID={`${testIds.productName}-${index}`}
            >
              {removeLineBreaks(productItem.displayField)}
            </Text>
          ) : null}
          <View style={styles.numberWrapper}>
            {productItem.productNumber ? (
              <Text
                color={Colors.PRIMARY_1100}
                fontWeight={FontWeight.MEDIUM}
                fontSize={14}
                testID={`${testIds.pn}-${index}`}
              >
                PN# {productItem.productNumber}
              </Text>
            ) : null}

            {productItem.catalogNumber ? (
              <Text
                color={Colors.PRIMARY_1100}
                fontWeight={FontWeight.MEDIUM}
                fontSize={14}
                testID={`${testIds.cat}-${index}`}
              >
                CAT# {productItem.catalogNumber}
              </Text>
            ) : null}

            {productItem.upc ? (
              <Text
                color={Colors.PRIMARY_1100}
                fontWeight={FontWeight.MEDIUM}
                fontSize={14}
                testID={`${testIds.upc}-${index}`}
              >
                UPC# {productItem.upc}
              </Text>
            ) : null}
          </View>
        </Section>
      </ListItem.Content>
    </ListItem>
  );
};

const styles = StyleSheet.create({
  listItemContent: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    borderBottomWidth: 1,
    borderBottomColor: Colors.SECONDARY_3100,
  },
  listItemImage: {
    height: 64,
    width: 64,
    marginRight: 16,
  },
  listItemPlaceholder: {
    backgroundColor: 'transparent',
    marginRight: 16,
  },
  numberWrapper: {
    paddingVertical: 12,
  },
});
