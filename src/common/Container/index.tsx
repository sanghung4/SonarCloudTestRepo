import { WrapperProps } from '@reece/global-types';
import clsx from 'clsx';

export type ContainerProps = {
  className?: string;
  'data-testid'?: string;
} & WrapperProps;

const Container = (props: ContainerProps) => (
  <div
    className="bg-common-background h-screen px-6 py-6"
    data-testid={props['data-testid']}
  >
    <div
      className={clsx('bg-white w-full py-6 px-8 shadow-md', props.className)}
    >
      {props.children}
    </div>
  </div>
);

export default Container;
