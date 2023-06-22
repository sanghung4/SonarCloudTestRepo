import { ReactNode } from 'react';

import { Branch } from 'generated/graphql';
import LocationCard from 'LocationSearch/LocationCard';
import { render } from 'test-utils/TestWrapper';
import { fireEvent, waitFor } from '@testing-library/react';
import { setBreakpoint } from 'test-utils/mockMediaQuery';

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

describe('LocationSearch LocationCard tests', () => {
  it('should render while loading', async () => {
    const { getByTestId } = render(<LocationCard index={1} loading />);
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    const bubbleIcon = getByTestId(`branch-icon-1`);
    expect(bubbleIcon).toBeTruthy();
  });

  it('should render the branch details correctly', async () => {
    const { getByTestId } = render(
      <LocationCard branch={branch} index={1} loading={false} />
    );
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    const branchName = getByTestId(`branch-name-1`);
    expect(branchName).toBeTruthy();
  });

  it('should render the branch location details', async () => {
    const { getByTestId } = render(
      <LocationCard branch={branch} index={1} loading={false} />
    );
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    const viewLocationDetailsButton = getByTestId(
      `${branch.branchId}-view-location-details`
    );
    expect(viewLocationDetailsButton).toBeTruthy();
    fireEvent.click(viewLocationDetailsButton);
    const indexedList = getByTestId(`branch-hours-1`);
    expect(indexedList).toBeTruthy();
    const hideDetailsButton = getByTestId(`${branch.branchId}-hide-details`);
    expect(hideDetailsButton).toBeTruthy();
    fireEvent.click(hideDetailsButton);
    expect(viewLocationDetailsButton).toBeTruthy();
  });

  it('should render the branch location details in mobile', async () => {
    setBreakpoint('mobile');
    const { getByTestId } = render(
      <LocationCard branch={branch} index={1} loading={false} />
    );
    await waitFor(() => new Promise((res) => setTimeout(res, 0)));
    const viewLocationDetailsButton = getByTestId(
      `${branch.branchId}-view-location-details`
    );
    expect(viewLocationDetailsButton).toBeTruthy();
    fireEvent.click(viewLocationDetailsButton);
    const indexedList = getByTestId(`branch-hours-1`);
    expect(indexedList).toBeTruthy();
  });
});
