import { Box, Fade, SxProps } from '@dialexa/reece-component-library';

type Props = {
  on: boolean;
  onClick: () => void;
  sx?: SxProps;
};

export default function Backdrop(props: Props) {
  return (
    <Fade in={props.on}>
      <Box
        data-testid="branch-sidebar-backdrop"
        onClick={props.onClick}
        zIndex={1}
        position="absolute"
        left={0}
        right={0}
        width={1}
        height={1}
        sx={{ backgroundColor: 'rgba(0, 0, 0, 0.5)', ...props.sx }}
      />
    </Fade>
  );
}
