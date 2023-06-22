import React from 'react';

import Link from './';
import { render } from 'test-utils/TestWrapper';

describe('Test link', () => {
  it('Render link', async () => {
    const { container } = render(<Link to="test">Test</Link>);
    expect(container).toMatchSnapshot();
  });
});
