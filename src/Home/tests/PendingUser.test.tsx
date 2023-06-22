import { ContactInfo } from 'generated/graphql';
import PendingUser from 'Home/PendingUser';
import { render } from 'test-utils/TestWrapper';

const mockContact: {
  data: { contactInfo: ContactInfo };
  loading: boolean;
} = {
  data: { contactInfo: {} },
  loading: false
};

jest.mock('generated/graphql', () => ({
  ...jest.requireActual('generated/graphql'),
  useGetContactInfoQuery: () => mockContact
}));

describe('Home - PendingUser', () => {
  it('should match snapshot when contact info is loading', () => {
    mockContact.data.contactInfo.emailAddress = undefined;
    mockContact.data.contactInfo.isBranchInfo = undefined;
    mockContact.data.contactInfo.phoneNumber = undefined;
    mockContact.loading = true;
    const { container } = render(<PendingUser />);
    expect(container).toMatchSnapshot();
  });
  it('should match snapshot when contact info is empty', () => {
    mockContact.data.contactInfo.emailAddress = undefined;
    mockContact.data.contactInfo.isBranchInfo = undefined;
    mockContact.data.contactInfo.phoneNumber = undefined;
    mockContact.loading = false;
    const { container } = render(<PendingUser />);
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot with data while branchInfo is true', () => {
    mockContact.data.contactInfo.emailAddress = 'user@email.com';
    mockContact.data.contactInfo.isBranchInfo = true;
    mockContact.data.contactInfo.phoneNumber = '123-456-7890';
    mockContact.loading = false;
    const { container } = render(<PendingUser />);
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot with data while branchInfo is false', () => {
    mockContact.data.contactInfo.emailAddress = 'user@email.com';
    mockContact.data.contactInfo.isBranchInfo = false;
    mockContact.data.contactInfo.phoneNumber = '123-456-7890';
    mockContact.loading = false;
    const { container } = render(<PendingUser />);
    expect(container).toMatchSnapshot();
  });
});
