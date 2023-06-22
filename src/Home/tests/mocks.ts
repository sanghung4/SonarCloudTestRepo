import { UserContextType } from 'AuthProvider';
import { Permission } from 'common/PermissionRequired';
import useHomeSignInForm from 'Home/util/useHomeSignInForm';

type HomeAuthMock = {
  guest: UserContextType;
  verify: UserContextType;
  pending: UserContextType;
  authorized: UserContextType;
  notAuthorized: UserContextType;
  admin: UserContextType;
};

export const mocks: HomeAuthMock = {
  guest: {
    authState: { isAuthenticated: false },
    profile: {
      userId: '',
      permissions: [],
      isEmployee: false,
      isVerified: false
    },
    activeFeatures: []
  },
  verify: {
    authState: { isAuthenticated: true },
    profile: {
      userId: 'testuser',
      permissions: [],
      isEmployee: true,
      isVerified: false
    },
    activeFeatures: []
  },
  pending: {
    authState: { isAuthenticated: true },
    profile: {
      userId: 'testuser',
      permissions: [],
      isEmployee: true,
      isVerified: true
    },
    activeFeatures: []
  },
  authorized: {
    authState: { isAuthenticated: true },
    profile: {
      userId: 'testuser',
      permissions: [Permission.EDIT_PROFILE],
      isEmployee: true,
      isVerified: true
    },
    activeFeatures: []
  },
  notAuthorized: {
    authState: { isAuthenticated: undefined },
    profile: {
      userId: 'testuser',
      permissions: [Permission.EDIT_PROFILE, Permission.VIEW_INVOICE],
      isEmployee: true,
      isVerified: true
    },
    activeFeatures: []
  },
  admin: {
    authState: { isAuthenticated: true },
    profile: {
      userId: 'testuser',
      permissions: [
        Permission.EDIT_PROFILE,
        Permission.APPROVE_ALL_USERS,
        Permission.TOGGLE_FEATURES,
        Permission.APPROVE_CART,
        Permission.VIEW_INVOICE
      ],
      isEmployee: true,
      isVerified: true
    },
    activeFeatures: ['LISTS']
  }
};

export const mockSignInFormHook: ReturnType<typeof useHomeSignInForm> = {
  email: '',
  formik: {
    initialValues: { username: '', password: '' },
    initialErrors: {},
    initialTouched: {},
    initialStatus: undefined,
    handleBlur: jest.fn(),
    handleChange: jest.fn(),
    handleReset: jest.fn(),
    handleSubmit: jest.fn(),
    resetForm: jest.fn(),
    setErrors: jest.fn(),
    setFormikState: jest.fn(),
    setFieldTouched: jest.fn(),
    setFieldValue: jest.fn(),
    setFieldError: jest.fn(),
    setStatus: jest.fn(),
    setSubmitting: jest.fn(),
    setTouched: jest.fn(),
    setValues: jest.fn(),
    submitForm: jest.fn(),
    validateForm: jest.fn(),
    validateField: jest.fn(),
    isValid: false,
    dirty: false,
    unregisterField: jest.fn(),
    registerField: jest.fn(),
    getFieldProps: jest.fn(),
    getFieldMeta: jest.fn(),
    getFieldHelpers: jest.fn(),
    validateOnBlur: false,
    validateOnChange: false,
    validateOnMount: false,
    values: { username: '', password: '' },
    errors: { username: '', password: '' },
    touched: {},
    isSubmitting: false,
    isValidating: false,
    status: undefined,
    submitCount: 0
  },
  onCompleted: jest.fn(),
  onError: jest.fn(),
  onSubmit: jest.fn()
};
