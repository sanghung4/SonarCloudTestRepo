import React from 'react';
import {
  useGetVarianceSummaryQuery,
  useLoadVarianceDetailsMutation,
} from 'api';
import { CustomIconNames } from 'components/CustomIcon';
import { CustomButton } from 'components/CustomButton';
import { ScreenLayout } from 'components/ScreenLayout';
import { Text } from 'components/Text';
import { Colors, FontWeight } from 'constants/style';
import { StyleSheet, View } from 'react-native';
import { getScreenTestingIds } from 'test-utils/testIds';
import { toNumberString } from 'utils/stringUtils';
import { useOverlay } from 'providers/Overlay';
import { kebabCase, noop } from 'lodash';
import { handleMutationComplete } from 'utils/apollo';
import { showErrorToast, showSuccessToast } from 'components/ToastConfig';
import useRenderListener from 'hooks/useRenderListener';

const VarianceSummary = () => {
  useRenderListener();

  const { showAlert } = useOverlay();

  const {
    data: summaryData,
    refetch: summaryRefetch,
    loading: summaryLoading,
  } = useGetVarianceSummaryQuery({
    fetchPolicy: 'network-only',
    notifyOnNetworkStatusChange: true,
    onCompleted: (resp) => {
    },
    onError: () => {
    },
  });

  const [
    loadVarianceDetailsMutation,
    { loading: recountLoading },
  ] = useLoadVarianceDetailsMutation({
    onCompleted: (resp) => {
      handleMutationComplete(resp.loadVarianceDetails);
      showSuccessToast({
        title: 'Recount sent',
        text1: 'Locations are ready for recount.',
      });
    },
    onError: () => {
      showErrorToast({
        title: 'Recount not sent',
        text1: "Please select 'Recount All' again.",
      });
    },
  });

  const summaryRowData: {
    title: string;
    value: string;
  }[] = [
    {
      title: 'Gross ($)',
      value: toNumberString(summaryData?.varianceSummary.grossTotalCost),
    },
    {
      title: 'Net gain & loss ($)',
      value: toNumberString(summaryData?.varianceSummary.netTotalCost),
    },
    {
      title: 'No. of locations',
      value: toNumberString(summaryData?.varianceSummary.locationQuantity),
    },
    {
      title: 'No. of products',
      value: toNumberString(summaryData?.varianceSummary.productQuantity),
    },
    {
      title: 'Quantity (EA)',
      value: toNumberString(summaryData?.varianceSummary.differenceQuantity),
    },
  ];

  const handleRecountAllPress = () => {
    showAlert({
      title: 'Recount All Locations?',
      description:
        'Are you sure you want to send all locations with a variance for recount?',
      actions: [
        { title: 'Cancel', type: 'link', onPress: noop, key: 'cancel' },
        {
          title: 'Yes, recount all',
          onPress: () => {
            loadVarianceDetailsMutation()
          },
          key: 'recount',
        },
      ],
    });
  };

  const testIds = getScreenTestingIds('VarianceSummary');

  return (
    <ScreenLayout
      testID={testIds.screenLayout}
      loading={summaryLoading || recountLoading}
    >
      <ScreenLayout.ScrollContent
        banner={{ title: 'Variance Summary' }}
        paddingHorizontal
        testID={testIds.scrollContent}
      >
        {/* Table */}
        <View style={styles.tableContainer} testID={testIds.table}>
          {/* Header */}
          <View style={styles.tableHeader}>
            <Text
              fontWeight={FontWeight.MEDIUM}
              style={styles.tableHeaderText}
              testID={testIds.tableTitle}
            >
              Total variances in the branch:
            </Text>
          </View>

          {/* Rows */}
          {summaryRowData.map((row, index) => (
            <View
              style={[
                styles.tableRowBase,
                index % 2 === 0 && styles.tableRowAltBackground,
              ]}
              key={index}
            >
              {/* Row Title */}
              <Text
                style={styles.tableRowTitle}
                testID={`${testIds.tableRowTitle}-${kebabCase(row.title)}`}
              >
                {row.title}
              </Text>
              {/* Row Value */}
              <Text
                style={styles.tableRowValue}
                testID={`${testIds.tableRowValue}-${kebabCase(row.title)}`}
              >
                {row.value}
              </Text>
            </View>
          ))}
          {/* Table Actions */}
          <View style={styles.tableActionRow}>
            {/* Refresh Button */}
            <CustomButton
              title="Refresh"
              type="secondary"
              icon={CustomIconNames.Refresh}
              onPress={() => summaryRefetch()}
              testID={testIds.refreshButton}
            />
            {/* Recount Button */}
            <CustomButton
              title="Recount All"
              type="link"
              onPress={handleRecountAllPress}
              testID={testIds.recountAllButton}
            />
          </View>
        </View>
      </ScreenLayout.ScrollContent>
    </ScreenLayout>
  );
};

const styles = StyleSheet.create({
  tableContainer: {
    backgroundColor: Colors.WHITE,
    borderRadius: 4,
    shadowColor: Colors.BLACK,
    shadowOpacity: 0.25,
    shadowRadius: 5,
    shadowOffset: { width: 0, height: 6 },
    elevation: 2,
  },
  tableHeader: {
    alignItems: 'center',
    paddingTop: 20,
    paddingBottom: 16,
  },
  tableHeaderText: {
    letterSpacing: 0.5,
  },
  tableRowBase: {
    paddingVertical: 8,
    paddingHorizontal: 40,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  tableRowAltBackground: {
    backgroundColor: Colors.SECONDARY_450,
  },
  tableActionRow: {
    paddingHorizontal: 32,
    paddingVertical: 24,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  tableRowTitle: {
    color: Colors.SECONDARY_2100,
    fontWeight: FontWeight.MEDIUM,
    fontSize: 14,
  },
  tableRowValue: {
    color: Colors.PRIMARY_1100,
    fontWeight: FontWeight.MEDIUM,
  },
  refreshButtonIconContainer: { marginRight: 8 },
  refreshButton: {
    borderWidth: 2,
    paddingVertical: 10,
    paddingHorizontal: 16,
    borderRadius: 2,
  },
  refreshButtonTitle: {
    fontWeight: FontWeight.MEDIUM,
    fontSize: 16,
    lineHeight: 24,
  },
  recountButtonTitle: {
    textDecorationLine: 'underline',
    fontWeight: FontWeight.MEDIUM,
    fontSize: 16,
    color: Colors.PRIMARY_2100,
    lineHeight: 24,
  },
  recountButton: { paddingVertical: 10, paddingHorizontal: 16 },
});

export default VarianceSummary;
