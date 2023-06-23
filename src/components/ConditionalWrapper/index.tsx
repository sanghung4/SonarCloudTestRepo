import { ReactNode } from 'react';

import { WrapperProps } from '@reece/global-types';

type ConditionalWrapperProps = WrapperProps & {
  condition: boolean;
  wrapper: (children: ReactNode) => JSX.Element;
};

function ConditionalWrapper(props: ConditionalWrapperProps) {
  if (props.condition) {
    return props.wrapper(props.children);
  }
  return <>{props.children}</>;
}

export default ConditionalWrapper;
