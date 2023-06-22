import cn from 'classnames';

import './styles.scss';

/**
 * Types
 */
type CardProps = {
  className?: string;
  children?: React.ReactNode;
  testId?: string;
};

/**
 * Component
 */
function Card(props: CardProps) {
  /**
   * Props
   */
  const { className, children, testId } = props;

  /**
   * Render
   */
  return (
    <div className={cn('card', className)} data-testid={testId}>
      {children}
    </div>
  );
}

export default Card;
