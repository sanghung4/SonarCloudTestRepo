import { act, fireEvent, waitFor } from '@testing-library/react';
import { noop } from 'lodash-es';

import { render } from 'test-utils/TestWrapper';
import FeedbackButton from 'common/FeedbackButton';

jest.useFakeTimers('modern');

describe('common - FeedbackButton', () => {
  it('Expect to match snapshot', () => {
    const { container } = render(
      <FeedbackButton value="test" valueDone="test" />
    );
    expect(container).toMatchSnapshot();
  });

  it('Expect to match snapshot with arrayed sx', () => {
    const { container } = render(
      <FeedbackButton
        value="test"
        valueDone="test"
        sx={[{ maxWidth: 32 }, { pt: 2 }]}
      />
    );
    expect(container).toMatchSnapshot();
  });

  it('Expect to update the button correctly upon clicking and waiting', async () => {
    const { getByTestId } = render(
      <FeedbackButton
        value="OK"
        valueDone="DONE"
        timeout={50}
        onClick={noop}
        testId="test-feedback-button"
      />
    );

    const button = getByTestId('test-feedback-button');
    expect(button).toHaveTextContent('OK');
    fireEvent.click(button);
    expect(button).toHaveTextContent('DONE');

    act(() => {
      jest.runOnlyPendingTimers();
    });

    expect(button).toHaveTextContent('OK');
  });

  it('Expect nothing happens if onClick is undefined', async () => {
    const { getByTestId } = render(
      <FeedbackButton
        value="OK"
        valueDone="DONE"
        timeout={10}
        testId="test-feedback-button"
      />
    );
    const button = getByTestId('test-feedback-button');

    expect(button).toHaveTextContent('OK');
    fireEvent.click(button);
    expect(button).toHaveTextContent('OK');
  });
});
