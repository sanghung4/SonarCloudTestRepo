import React from 'react';
import { FlatList } from 'react-native';
import { Text } from 'components/Text';
import { Colors, FontWeight as fw } from 'constants/style';
import { useConfig } from 'hooks/useConfig';
import { isArray, isString, reverse, startCase } from 'lodash';
import { ListItem } from 'react-native-elements';
import { getAPIError } from 'utils/error';
import { ApolloError } from '@apollo/client';
import { GraphQLError } from 'graphql';
import { ScreenLayout } from 'components/ScreenLayout';
import useRenderListener from 'hooks/useRenderListener';

type ReadableError = {
  message?: string;
  stacktrace?: string;
};

const toReadableError = (graphQLError: GraphQLError): ReadableError => {
  const apolloError = new ApolloError({ graphQLErrors: [graphQLError] });
  const apiError = getAPIError(apolloError);

  const readableError: ReadableError = {};

  if (graphQLError) {
    readableError.message =
      isArray(graphQLError.path) && isString(graphQLError.path[0])
        ? startCase(graphQLError.path[0]) + ': ' + graphQLError.message
        : graphQLError.message;

    readableError.stacktrace =
      graphQLError.extensions &&
      isArray(graphQLError.extensions.exception?.stacktrace) &&
      graphQLError.extensions.exception.stacktrace.join('\n');
  }

  if (apiError) {
    readableError.message = apiError.message
      ? apiError.code + ' ' + apiError.message
      : apiError.code;
  }

  return readableError;
};

const ErrorLog: React.FC = () => {
  const [{ errors: graphQLErrors }] = useConfig();
  useRenderListener();

  return (
    <ScreenLayout>
      <ScreenLayout.StaticContent padding>
        <FlatList
          style={styles.content}
          data={reverse(graphQLErrors)}
          keyExtractor={(_, idx) => `${idx}`}
          renderItem={({ item: graphQLError }) => {
            const error = toReadableError(graphQLError);

            return (
              <ListItem bottomDivider>
                <ListItem.Content>
                  <Text fontWeight={fw.MEDIUM} style={styles.message}>
                    {error.message}
                  </Text>
                  {error.stacktrace ? (
                    <Text small color={Colors.SUPPORT_2100}>
                      {error.stacktrace}
                    </Text>
                  ) : null}
                </ListItem.Content>
              </ListItem>
            );
          }}
        />
      </ScreenLayout.StaticContent>
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
  title: {
    marginBottom: 24,
  },
  message: {
    marginBottom: 12,
  },
} as const;

export default ErrorLog;
