import { Skeleton } from '@dialexa/reece-component-library';
import { InfoChipContainer } from './utils/styled';

/**
 * Types
 */
type InfoChipProps = {
  contained?: boolean;
  color?: string;
  text: string;
  icon: JSX.Element;
  loading?: boolean;
  onClick?: () => void;
  testId?: string;
};

/**
 * Component
 */
function InfoChip(props: InfoChipProps) {
  /**
   * Props
   */
  const { icon, text, contained, color, loading, onClick, testId } = props;

  /**
   * Utils
   */
  const getColor = () => {
    switch (color) {
      case 'primary':
        return 'primary.main';
      case 'success':
        return 'success.main';
      default:
        return color ?? 'text.primary';
    }
  };

  const getBackground = () => {
    switch (color) {
      case 'primary':
        return '#F3F7FE';
      case 'success':
        return '#ECF2E9';
    }
  };

  /**
   * Render
   */
  if (loading) {
    return (
      <Skeleton
        width="100%"
        height={24}
        variant="rectangular"
        sx={{ borderRadius: 24 }}
      />
    );
  }

  return (
    <InfoChipContainer
      contained={contained}
      color={getColor()}
      backgroundColor={getBackground()}
      onClick={onClick}
      title={text}
      data-testid={testId}
    >
      {icon}
      {` ${text}`}
    </InfoChipContainer>
  );
}

export default InfoChip;
