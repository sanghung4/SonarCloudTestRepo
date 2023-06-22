import { Skeleton, Typography } from '@dialexa/reece-component-library';
import { kebabCase } from 'lodash-es';

type OrderInfoToopTipProps = {
  key: string;
  value?: string;
  loading?: boolean;
  testIdKey?: string;
};

// Has to be a separate component to capture the test coverage because
// It is impossible to capture MUI's <Tooltip> and its contents
export default function OrderInfoToolTip(props: OrderInfoToopTipProps) {
  return (
    <Typography
      noWrap
      data-testid={kebabCase(`order-info-${props.testIdKey ?? ''}`)}
    >
      {props.loading ? <Skeleton /> : props.value || '-'}
    </Typography>
  );
}
