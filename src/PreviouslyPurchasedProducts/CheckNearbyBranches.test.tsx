import React from 'react';
import { useTranslation } from 'react-i18next';
import { render } from 'test-utils/TestWrapper';
import CheckNearbyBranches from './CheckNearbyBranches';

describe('Previously Purchased Products', () => {
  it('Render CheckNearbyBranches', () => {
    const { container } = render(<CheckNearbyBranches />);

    expect(container).toBeInTheDocument();
  });
});
