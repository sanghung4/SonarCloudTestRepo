import { Dispatch } from 'react';

import { userButtonStyles } from 'common/Header/util';
import { SignOutIcon } from 'icons';

type Props = {
  isSignOut?: boolean;
  label: string;
  path: string;
  push: Dispatch<string>;
  setOpen: Dispatch<boolean>;
};

export function UserItemButton({
  isSignOut,
  label,
  path,
  push,
  setOpen
}: Props) {
  const onClick = () => {
    setOpen(false);
    push(path);
  };
  return (
    <userButtonStyles.MenuButton
      variant="text"
      startIcon={isSignOut ? <SignOutIcon /> : undefined}
      color={isSignOut ? 'primaryLight' : 'gray'}
      fullWidth
      onClick={onClick}
    >
      {label}
    </userButtonStyles.MenuButton>
  );
}
