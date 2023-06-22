import { Box } from '@dialexa/reece-component-library';
import { CheckIcon, DeleteIcon } from 'icons';

/**
 * Types
 */
type FlagCellProps = {
  value: boolean;
};

/**
 * Component
 */
function FlagCell(props: FlagCellProps) {
  if (props.value) {
    return (
      <Box component={CheckIcon} color="success.main" height={16} width={16} />
    );
  }

  return (
    <Box component={DeleteIcon} color="orangeRed.main" height={16} width={16} />
  );
}

export default FlagCell;
