import { ReactNode } from 'react';

import { Branch } from 'generated/graphql';
import Popover from 'LocationSearch/Popover';
import { mockGoogle } from 'test-utils/mockGlobals';
import { render } from 'test-utils/TestWrapper';

const branch: Branch = {
  actingBranchManager: 'C.R. Kirby ',
  actingBranchManagerEmail: 'seth+reecebranchmanager@dialexa.com',
  actingBranchManagerPhone: '817-659-3953',
  address1: '15850 Dallas Parkway',
  address2: 'Suite 105',
  branchId: '1149',
  businessHours: 'M-F: 7:00am - 5:00pm',
  city: 'Dallas',
  distance: 1.7589941,
  entityId: '546983',
  isBandK: false,
  isHvac: false,
  isPlumbing: true,
  isWaterworks: false,
  latitude: 32.9643304,
  longitude: -96.8202783,
  name: 'Morrison Supply',
  phone: '(469) 680-3291',
  state: 'Texas',
  website: 'https://www.morrisonsupply.com',
  workdayId: null,
  workdayLocationName: null,
  zip: '75248',
  __typename: 'Branch'
};

const mockLatLng = {
  lat: () => 0,
  lng: () => 0,
  equals: jest.fn(),
  toJSON: jest.fn(),
  toUrlValue: jest.fn()
};

type EmptyCompProps = {
  children: ReactNode;
};

jest.mock('@react-google-maps/api', () => ({
  InfoWindow: ({ children }: EmptyCompProps) => <div>{children}</div>
}));

describe('LocationSearch Popover tests', () => {
  beforeEach(() => {
    mockGoogle();
  });

  it('should render correctly', () => {
    const { container } = render(
      <Popover branch={branch} position={mockLatLng} onCloseClick={() => {}} />
    );

    expect(container).toMatchSnapshot();
  });
});
