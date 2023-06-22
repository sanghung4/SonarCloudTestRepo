import {
  Box,
  Divider,
  Grid,
  useScreenSize
} from '@dialexa/reece-component-library';
import Dotdotdot from 'react-dotdotdot';

type Props = {
  comment?: string;
  isLast?: boolean;
};

export default function ContractListComment(props: Props) {
  /**
   * Custom hooks
   */
  const { isSmallScreen } = useScreenSize();

  /**
   * Render
   */
  return (
    <Box height={48} mx={isSmallScreen ? 0 : 2}>
      <Grid
        container
        alignItems="center"
        justifyContent="center"
        height="100%"
        px={isSmallScreen ? 2 : 0}
      >
        <Grid
          item
          xs={12}
          overflow="hidden"
          color="secondary02.main"
          fontWeight={500}
          fontSize={isSmallScreen ? 16 : 20}
          textAlign="center"
          mx={isSmallScreen ? 2 : 4}
        >
          <Dotdotdot clamp={1}>{props.comment ?? ''}</Dotdotdot>
        </Grid>
      </Grid>
      {!!props.isLast && <Divider />}
    </Box>
  );
}
