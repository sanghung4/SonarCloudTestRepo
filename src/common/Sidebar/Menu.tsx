import { ReactNode } from 'react';

import clsx from 'clsx';
import { useLocation, useNavigate } from 'react-router-dom';

import { Button } from 'components/Button';
import { ReactComponent as ListIcon } from 'resources/icons/list.svg';
import { ReactComponent as UsersIcon } from 'resources/icons/users.svg';

/**
 * Types
 */
type MenuItem = {
  icon: ReactNode;
  name: string;
  path: string;
};

/**
 * Config
 */
export const menuItems: MenuItem[] = [
  { icon: <UsersIcon />, name: 'Existing Customers', path: '/' },
  { icon: <ListIcon />, name: 'All Catalogs', path: '#' }
];

/**
 * Component
 */
function Menu() {
  /**
   * Custom Hooks
   */
  const { pathname } = useLocation();
  const navigate = useNavigate();

  /**
   * Render
   */
  return (
    <div>
      {menuItems.map((item, i) => {
        const selected = item.path === pathname;
        return (
          <Button
            className={clsx('w-full text-left', {
              'bg-primary-2-80': selected,
              'text-secondary-2-30': !selected,
              'text-white': selected,
              'font-normal': !selected,
              'font-bold': selected
            })}
            data-testid={`sidebar-menu_item${i}`}
            iconPosition="left"
            icon={item.icon}
            align="justify-start"
            key={`nav-item${i}`}
            title={item.name}
            onClick={() => navigate(item.path)}
          />
        );
      })}
    </div>
  );
}
export default Menu;
