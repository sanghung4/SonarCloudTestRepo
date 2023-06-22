import {
  Link as MuiLink,
  LinkProps as MuiLinkProps
} from '@dialexa/reece-component-library';
import { Link as RouterLink } from 'react-router-dom';

interface LinkProps extends MuiLinkProps {
  to: string;
}
export default function Link(props: LinkProps) {
  return (
    <MuiLink
      {...props}
      component={RouterLink}
      to={props.to}
      sx={{
        color: 'primary02.main',
        cursor: 'pointer',
        userSelect: 'none'
      }}
    >
      {props.children}
    </MuiLink>
  );
}
