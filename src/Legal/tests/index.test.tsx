import React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import { render } from 'test-utils/TestWrapper';

import Legal from 'Legal';
import DoNotSellMyInfo from 'Legal/DoNotSellMyInfo';
import PrivacyPolicy from 'Legal/PrivacyPolicy';
import TermsOfAccess from 'Legal/TermsOfAccess';
import TermsOfSale from 'Legal/TermsOfSale';
import { setBreakpoint } from 'test-utils/mockMediaQuery';

describe('Legal Compliance', () => {
  it('Snapshot match for "Legal"', () => {
    setBreakpoint('desktop');
    const { container } = render(
      <Router>
        <Legal />
      </Router>
    );
    expect(container).toMatchSnapshot();
  });

  it('Snapshot match for "Privacy Policy"', () => {
    setBreakpoint('desktop');
    const { container } = render(<PrivacyPolicy />);
    expect(container).toMatchSnapshot();
  });

  it('Snapshot match for "Terms of Access"', () => {
    setBreakpoint('desktop');
    const { container } = render(<TermsOfAccess />);
    expect(container).toMatchSnapshot();
  });

  it('Snapshot match for "Terms of Sale"', () => {
    setBreakpoint('desktop');
    const { container } = render(<TermsOfSale />);
    expect(container).toMatchSnapshot();
  });

  it('Snapshot match for "Do Not Sell My Info"', () => {
    setBreakpoint('desktop');
    const { container } = render(<DoNotSellMyInfo />);
    expect(container).toMatchSnapshot();
  });

  it('Snapshot match for "Do Not Sell My Info" on mobile display', () => {
    setBreakpoint('mobile');
    const { container } = render(<DoNotSellMyInfo />);
    expect(container).toMatchSnapshot();
  });
});
