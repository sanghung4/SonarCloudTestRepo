import { fireEvent } from '@testing-library/react';
import { noop } from 'lodash-es';

import RemoveCartItemsDialog from 'Cart/RemoveCartItemsDialog';
import { render } from 'test-utils/TestWrapper';

describe('Remove Cart Items Dialog', () => {
  it('Snapshot match with data', () => {
    const { baseElement } = render(
      <RemoveCartItemsDialog open={true} onClose={noop} />
    );
    expect(baseElement).toMatchSnapshot();
  });

  it('Close Button click test', async () => {
    const testFunction = jest.fn();
    const { findByTestId } = render(
      <RemoveCartItemsDialog open={true} onClose={testFunction} />
    );
    const closeButtonTop = await findByTestId('remove-all-items-dialog-cancel');
    fireEvent.click(closeButtonTop);
    expect(testFunction).toBeCalledTimes(1);
  });
});
