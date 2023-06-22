import { UserContextType } from 'AuthProvider';
import { Permission } from 'common/PermissionRequired';

export const authConfigNoPerm = {
  authState: {
    isAuthenticated: true
  },
  profile: {
    isEmployee: true,
    isVerified: true
  } as UserContextType['profile'],
  activeFeatures: ['LISTS']
};
export const authConfig = {
  authState: {
    isAuthenticated: true
  },
  profile: {
    userId: 'test',
    permissions: [Permission.EDIT_LIST, Permission.APPROVE_CART],
    isEmployee: true,
    isVerified: true
  } as UserContextType['profile'],
  activeFeatures: ['LISTS']
};
