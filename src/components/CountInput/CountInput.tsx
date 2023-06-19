import React, { useEffect, useState } from 'react';
import { Text } from 'components/Text';
import { StyleSheet, TextInput, TouchableOpacity, View } from 'react-native';
import { CountInputProps } from './types';
import { useConfig } from 'hooks/useConfig';
import { getImageUri, orNA } from 'utils/stringUtils';
import { cleanValue } from 'screens/LocationItems/utils';
import { Image } from 'components/Image';
import { Colors, FontWeight } from 'constants/style';
import { Input } from 'react-native-elements';
import { isMincron } from 'utils/apollo';
import { InputLength } from 'constants/form';
import { SvgIcons } from 'components/SVG';
import { getComponentTestingIds } from 'test-utils/testIds';

const CountInput = React.forwardRef<TextInput, CountInputProps>(
  (
    {
      item,
      inputAccessoryViewID,
      disableDetails,
      onPress,
      onFocus,
      onBlur,
      onChangeText,
      onSubmitEditing,
      testID,
    },
    ref
  ) => {
    // Hooks
    const [{ count }] = useConfig();

    // State
    const [inputValue, setInputValue] = useState('');

    // Effects
    useEffect(() => {
      const itemQty =
        item.quantity || item.quantity === 0 ? `${item.quantity}` : '';
      setInputValue(itemQty);
    }, [item]);

    // Functions
    const handleChangeText = (text: string) => {
      const formattedValue = cleanValue(text);
      setInputValue(formattedValue);
      onChangeText(formattedValue);
    };

    const testIds = getComponentTestingIds('CountInput', testID);

    return (
      <View style={styles.container} testID={testIds.component}>
        {/* Image */}
        <TouchableOpacity
          onPress={onPress}
          style={styles.imageWrapper}
          testID={`${testIds.imageWrapper}-${item.prodNum}`}
          disabled={disableDetails}
        >
          <Image
            style={styles.image}
            imageUri={getImageUri(item.productImageUrl)}
            resizeMode="contain"
            placeholder={
              <SvgIcons
                name={'PlaceholderImage'}
                size={64}
                style={styles.placeholder}
              />
            }
          />
          {disableDetails ? null : (
            <Text color={Colors.PRIMARY_2100} fontWeight={FontWeight.MEDIUM}>
              Details
            </Text>
          )}
        </TouchableOpacity>
        {/* Product information */}
        <View style={[styles.productInformationWrapper]}>
          {/* Description */}
          <Text
            fontWeight={FontWeight.MEDIUM}
            testID={`${testIds.productDescription}-${item.prodNum}`}
          >
            {orNA(item.prodDesc).replace(/\s\s+/g, ' ')}
          </Text>
          {/* Product details */}
          <View style={styles.itemDetailsWrapper}>
            <Text
              fontSize={14}
              color={Colors.SECONDARY_290}
              selectable
              style={styles.itemDetails}
              testID={`${testIds.productNumber}-${item.prodNum}`}
            >
              PN# {item.prodNum || '-'}
            </Text>
            <Text
              fontSize={14}
              color={Colors.SECONDARY_290}
              selectable
              style={styles.itemDetails}
              testID={`${testIds.catalogNumber}-${item.prodNum}`}
            >
              CAT# {item.catalogNum || '-'}
            </Text>
          </View>
          {/* Input */}
          <Input
            testID={`${testIds.input}-${item.prodNum}`}
            ref={ref}
            textAlign="right"
            /* multiline and numberOfLines needed to allow scrolling even if the input is the scroll origin */
            multiline={true}
            numberOfLines={1}
            scrollEnabled={false}
            keyboardType="number-pad"
            blurOnSubmit={false}
            renderErrorMessage={false}
            selectTextOnFocus
            value={inputValue}
            onFocus={onFocus}
            onBlur={() => onBlur(inputValue)}
            onChangeText={handleChangeText}
            onSubmitEditing={onSubmitEditing}
            inputAccessoryViewID={inputAccessoryViewID}
            inputStyle={styles.inputText}
            containerStyle={styles.inputGeneralContainerStyle}
            style={styles.input}
            inputContainerStyle={styles.inputContainer}
            rightIconContainerStyle={styles.inputRightIcon}
            rightIcon={
              <Text
                color={Colors.SECONDARY_3100}
                fontWeight={FontWeight.MEDIUM}
                fontSize={14}
                testID={`${testIds.uom}-${item.prodNum}`}
              >
                {orNA(item.uom, 'ea')}
              </Text>
            }
            maxLength={
              isMincron(count) ? InputLength.MINCRON : InputLength.ECLIPSE
            }
          />
        </View>
      </View>
    );
  }
);

const styles = StyleSheet.create({
  container: {
    borderBottomWidth: 2,
    borderBottomColor: Colors.SECONDARY_3100,
    marginHorizontal: 24,
    paddingHorizontal: 12,
    flexDirection: 'row',
    flexWrap: 'nowrap',
    marginBottom: 24,
    alignItems: 'center',
  },

  imageWrapper: {
    alignItems: 'center',
    marginRight: 16,
    paddingBottom: 8,
  },
  image: {
    height: 64,
    width: 64,
  },
  placeholder: {
    backgroundColor: 'transparent',
  },

  productInformationWrapper: {
    flex: 1,
    height: '100%',
  },
  itemDetailsWrapper: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    flex: 1,
  },
  itemDetails: {
    flexGrow: 1,
    textAlign: 'left',
    minWidth: '50%',
  },

  inputText: {
    fontWeight: FontWeight.MEDIUM,
    color: Colors.PRIMARY_3100,
    fontSize: 16,
    paddingVertical: 0,
  },
  inputGeneralContainerStyle: { paddingHorizontal: 0 },
  input: { paddingRight: 8, minHeight: 0 },
  inputError: { display: 'none' },
  inputRightIcon: { marginVertical: 0, height: 'auto' },
  inputContainer: {
    flex: 1,
    justifyContent: 'center',
    borderBottomWidth: 0,
    marginBottom: 4,
  },
});

export default CountInput;
