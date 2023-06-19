import React, { useEffect, useState } from 'react';
import { View } from 'react-native';
import { pagePadding } from 'constants/style';

import { useGetWriteInQuery, useResolveWriteInMutation } from 'api';
import { Text } from 'components/Text';
import { FormInput } from 'components/FormInput';
import { isNil, map } from 'lodash';
import { useOverlay } from 'providers/Overlay';
import { getError } from 'utils/error';
import { ErrorType } from 'constants/error';
import { writeInForm, WriteInFormKey } from 'constants/form';
import { useLoading } from 'hooks/useLoading';
import { RouteNames } from 'constants/routes';
import { SvgIcons } from 'components/SVG';
import { TouchableOpacity } from 'react-native-gesture-handler';
import { AppScreenProps } from 'navigation/types';
import { ScreenLayout } from 'components/ScreenLayout';
import { handleMutationComplete } from 'utils/apollo';
import { getScreenTestingIds } from 'test-utils/testIds';
import useRenderListener from 'hooks/useRenderListener';

const WriteInDetail = ({
  navigation,
  route,
}: AppScreenProps<'WriteInDetail'>) => {
  useRenderListener();
  const { showAlert, toggleLoading } = useOverlay();
  const [writeIn, setWriteIn] = useState(route.params?.writeIn);

  const options = {
    fetchPolicy: 'network-only',
    variables: {
      id: route.params ? route?.params?.writeIn?.id : '',
    },
  } as const;

  const query = useGetWriteInQuery(options);
  const [resolveWriteIn, { loading }] = useResolveWriteInMutation({
    onCompleted: (resp) => {
      handleMutationComplete(resp.resolveWriteIn);
    },
    onError: (error) => {
      showAlert(getError(ErrorType.RESOLVE_WRITE_IN, error));
    },
  });

  useLoading([query.loading], true);

  useLoading([loading]);

  useEffect(() => {
    if (query?.data) {
      setWriteIn(query.data ? query.data.writeIn : route.params?.writeIn);
    }
  }, [query]);

  if (!writeIn) {
    return (
      <ScreenLayout>
        <ScreenLayout.StaticContent centered>
          <Text>There was a problem</Text>
        </ScreenLayout.StaticContent>
      </ScreenLayout>
    );
  }

  const handleResolved = () => {
    resolveWriteIn({
      variables: { id: writeIn.id },
    }).then(({ data }) => {
      if (data && data.resolveWriteIn.success) {
        toggleLoading(false);
        navigation.goBack();
      }
    });
  };

  const testIds = getScreenTestingIds('WriteInDetails');

  return (
    <ScreenLayout
      pageAction={
        writeIn.resolved
          ? undefined
          : {
              title: 'Mark as Resolved',
              onPress: handleResolved,
            }
      }
      testID={testIds.screenlayout}
    >
      <ScreenLayout.ScrollContent
        banner={{
          title: 'Write-In Details',
          rightComponent: (
            <TouchableOpacity
              onPress={() => {
                navigation.navigate(RouteNames.UPDATE_WRITE_IN, {
                  writeIn,
                });
              }}
              testID={testIds.editIcon}
            >
              <SvgIcons name="Pencil" testID={testIds.screen} />
            </TouchableOpacity>
          ),
        }}
      >
        <View style={styles.form}>
          {map(writeInForm, (field, key: WriteInFormKey) => (
            <FormInput
              {...field}
              key={key}
              testID={`${testIds.form}-${key}`}
              placeholder={field.label}
              multiline={!!field.numberOfLines}
              value={isNil(writeIn[key]) ? '' : `${writeIn[key]}`}
              labelStyle={styles.label}
              disabled
            />
          ))}
        </View>
      </ScreenLayout.ScrollContent>
    </ScreenLayout>
  );
};

const styles = {
  container: {
    flexGrow: 1,
  },
  content: {
    width: '100%',
  },
  banner: {
    width: '100%',
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  label: {
    opacity: 0.5,
  },
  form: {
    paddingVertical: pagePadding.Y,
    paddingHorizontal: pagePadding.X,
  },
} as const;

export default WriteInDetail;
