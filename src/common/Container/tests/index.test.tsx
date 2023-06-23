import { screen } from '@testing-library/react';

import Container from 'common/Container';
import { render } from 'test-util';

/**
 * TEST
 */
describe('common/Container', () => {
  // 🟢 1 - Default
  it('expect to render container and its child component', () => {
    // act
    render(
      <Container data-testid="container">
        <div data-testid="child" />
      </Container>
    );
    const container = screen.queryByTestId('container');
    const child = screen.queryByTestId('child');
    // assert
    expect(container).toBeInTheDocument();
    expect(child).toBeInTheDocument();
  });
});
