import { Input } from 'components/Input';
import { Section } from 'components/Section';
import { Product } from 'components/Product';
import { Text } from 'components/Text';
import { InputLength } from 'constants/form';
import React from 'react';
import { StyleSheet, View } from 'react-native';
import { orNA } from 'utils/stringUtils';
import { DetailComponentProps } from './types';
import { Colors } from 'constants/style';
import { getComponentTestingIds } from 'test-utils/testIds';
import { useProductDetailsQuery } from 'api';

export const DetailComponent = ({
  item,
  value,
  location,
  valid,
  onChange,
  mincron,
  testID,
}: DetailComponentProps) => {
  const testIds = getComponentTestingIds('ProductDetail', testID);

  const { data, loading } = useProductDetailsQuery({ variables: { productId: item.prodNum } });

  return (
    <View>
      <Section flexDirection="row" marginBottom={6} minWidth={175}>
        <Product.FeaturedTitle testID={testIds.catField}>
          CAT# {orNA(data?.productDetails.catalogNumber)}
        </Product.FeaturedTitle>
        <Product.FeaturedTitle testID={testIds.pnField}>
          PN# {orNA(item.prodNum)}
        </Product.FeaturedTitle>
      </Section>
      <Section flexDirection="row" marginBottom={6} minWidth={175}>
        <Product.FeaturedTitle testID={testIds.upcField}>
          UPC# {orNA(data?.productDetails.upc)}
        </Product.FeaturedTitle>
        <Product.FeaturedTitle testID={testIds.ctrlField}>
          Ctrl# {orNA(item?.controlNum)}
        </Product.FeaturedTitle>
      </Section>
      <Text
        numberOfLines={3}
        color={Colors.SECONDARY_2100}
        testID={testIds.productOverviewField}
      >
        {orNA(item.prodDesc)}
      </Text>
      <Section marginTop={36} marginHorizontal={-10}>
        <Input
          disabled
          label="Location"
          value={location?.id}
          containerStyle={styles.fullInput}
          inputStyle={styles.inputMargin}
          testID={testIds.locationInput}
        />
      </Section>
      <Section flexDirection="row" marginHorizontal={-10}>
        <Input
          testID={testIds.qtyInput}
          value={value}
          label="Qty."
          containerStyle={styles.halfInput}
          keyboardType="number-pad"
          inputStyle={styles.inputMargin}
          onChangeText={(text: string) => {
            if (!item || !item.tagNum) {
              return;
            }
            onChange(text);
          }}
          errorMessage={valid ? '' : 'Must be a whole number.'}
          maxLength={mincron ? InputLength.MINCRON : InputLength.ECLIPSE}
        />
        <Input
          disabled
          label="UOM"
          value={orNA(item.uom, 'EA')}
          containerStyle={styles.halfInput}
          inputStyle={styles.inputMargin}
          testID={testIds.uomInput}
        />
      </Section>
    </View>
  );
};

const styles = StyleSheet.create({
  halfInput: { flex: 1 },
  fullInput: { width: '100%' },
  inputMargin: { marginTop: -12 },
});
