import cn from 'classnames';

import './styles.scss';

/**
 * Types
 */
type ContainerProps = {
  className?: string;
  children?: React.ReactNode;
  testId?: string;
  maxWidth?: 'sm' | 'md' | 'lg' | 'xl';
};

/**
 * Component
 */
function Container(props: ContainerProps) {
  /**
   * Props
   */
  const { className, children, testId, maxWidth = 'lg' } = props;

  /**
   * Render
   */
  return (
    <div className={cn(className, 'container', maxWidth)} data-testid={testId}>
      {children}
    </div>
  );
}

export default Container;
