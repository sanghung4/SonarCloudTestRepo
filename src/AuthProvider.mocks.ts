import { UserContextType } from 'AuthProvider';

export const authConfig = {
  authState: {
    accessToken: {
      accessToken:
        'eyJraWQiOiI4YjVQdDNHT1M2SGNHam1fMUVsVE51bTJueUFtUkE1MGVIb3JlZnY4dG9vIiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULjQ2LTVfbFVMQWZhV3lfMXh4MDY1d2RFVG9xX29tOThRdkFBVjZTWGpmLTAiLCJpc3MiOiJodHRwczovL2Rldi00MzI1NDYub2t0YS5jb20vb2F1dGgyL2RlZmF1bHQiLCJhdWQiOiJhcGk6Ly9kZWZhdWx0IiwiaWF0IjoxNjUwNDcxMjc0LCJleHAiOjE2NTA1NTc2NzQsImNpZCI6IjBvYTEzYjBiNWJNS1haZkpVNHg3IiwidWlkIjoiMDB1Nno0d3NsbEY1Q3N6aVo0eDciLCJzY3AiOlsicHJvZmlsZSIsIm9wZW5pZCIsImVtYWlsIl0sImF1dGhfdGltZSI6MTY1MDQ2NzcwMCwic3ViIjoiamFtZXMuZmVpZ2VsK3Rlc3Q1OEBkaWFsZXhhLmNvbSIsImlzRW1wbG95ZWUiOnRydWUsImVjb21tVXNlcklkIjoiYjE5YTE1N2MtZGM4Zi00MWYwLWI5NTItYjU3ODJkMWVmOTBlIiwiaXNWZXJpZmllZCI6ZmFsc2UsImVjb21tUGVybWlzc2lvbnMiOltdfQ.piFWMeWbRlpVBxkIrbd6PhzIOc42_psk6WUBLGzaecHY1F0zS-5tCtXqvXZ-atsgIl0Xyty0KSkDBD1KAqpU0-1R1hBATvDa_82XFZzUsaYKyX4M2pnS0zf4AkRJXfww-FJAORAZ2c6BIA8YSKMqSI6dTXSpArBNkafCv-ae_9v0FiIsXzIBjy9VHz4sOIjjDk0jjvD0pJe19kY5F2PnP3UyamDBFzABAI6lnrC4cBD_baBPF36Vmt9MOB91jIiQmzSPBKIiAfU3Cbq87vzkdAFALVSmePQZ64GPxTKoYjsS2xzVC-cZWZbNZEMH7MfTOpXJZYxia8pAsIkRyVCvgg',
      claims: {
        ver: 1,
        jti: '',
        iss: '',
        aud: '',
        iat: 1650471274,
        exp: 1650557674,
        cid: '0oa13b0b5bMKXZfJU4x7',
        uid: '00u6z4wsllF5CsziZ4x7',
        scp: ['profile', 'openid', 'email'],
        auth_time: 1650467700,
        sub: 'testuser@morsco.com',
        isEmployee: true,
        ecommUserId: 'testuser',
        isVerified: false,
        ecommPermissions: []
      },
      expiresAt: 1650557674,
      tokenType: 'Bearer',
      scopes: ['profile', 'openid', 'email'],
      authorizeUrl: '',
      userinfoUrl: ''
    },
    idToken: {
      idToken: '',
      claims: {
        sub: '',
        name: 'Test User',
        email: 'testuser@morsco.com',
        ver: 1,
        iss: '',
        aud: '',
        iat: 1650471274,
        exp: 1650474874,
        jti: '',
        amr: ['pwd'],
        idp: '',
        nonce: '',
        preferred_username: 'testuser@morsco.com',
        auth_time: 1650467700,
        at_hash: '',
        groups: ['Everyone']
      },
      expiresAt: 1650474874,
      scopes: ['profile', 'openid', 'email'],
      authorizeUrl: '',
      issuer: '',
      clientId: ''
    },
    isAuthenticated: true
  },
  profile: {
    userId: 'testuser',
    permissions: [
      'approve_all_users',
      'submit_cart_without_approval',
      'manage_roles',
      'edit_list',
      'edit_profile',
      'submit_quote_order',
      'approve_account_user',
      'approve_cart',
      'invite_user',
      'manage_payment_methods',
      'view_invoice',
      'refresh_contact'
    ],
    isEmployee: true,
    isVerified: false
  }
};

export const mockAuthContext: UserContextType = {
  activeFeatures: [],
  authState: null,
  ecommUser: null,
  handleLogout: jest.fn(),
  login: jest.fn(),
  setFeature: jest.fn()
};
