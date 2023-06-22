import {
  alpha,
  Box,
  BoxProps,
  CircularProgress,
  CircularProgressProps,
  SxProps,
  Theme
} from '@dialexa/reece-component-library';

type Props = {
  backdrop?: boolean;
  size?: 'parent' | 'page' | 'flex';
  loaderSize?: CircularProgressProps['size'];
  containerProps?: BoxProps;
};

const pageSx: SxProps<Theme> = {
  position: 'fixed',
  height: '100vh',
  width: '100vw'
};
const defaultSx: SxProps<Theme> = {
  position: 'absolute',
  bottom: 0,
  right: 0
};

function Loader(props: Props) {
  const isDefaultSize = props.size !== 'flex';
  const isPageSize = props.size === 'page';

  const notPageSize = isDefaultSize ? defaultSx : ({} as SxProps<Theme>);
  const sizeProps = isPageSize ? pageSx : notPageSize;

  return (
    <Box
      sx={sizeProps}
      data-testid="loader-component"
      top={0}
      left={0}
      display="flex"
      justifyContent="center"
      alignItems="center"
      zIndex={999}
      bgcolor={(theme) =>
        props.backdrop ? alpha(theme.palette.common.white, 0.6) : 'transparent'
      }
      {...props.containerProps}
    >
      <CircularProgress color="primary02.main" size={props.loaderSize} />
    </Box>
  );
}

export default Loader;
