import { Component, ReactNode, isValidElement } from 'react';
import { Redirect } from 'react-router-dom';

interface Props {
  children: ReactNode;
  mockErrorForTest?: Error;
}
interface State {
  hasError: boolean;
  error?: Error;
}

const defaultState: State = {
  hasError: false,
  error: undefined
};
export default class ErrorBoundary extends Component<Props, State> {
  constructor(p: Props) {
    super(p);
    this.state = defaultState;
    // Used for unit test
    if (p.mockErrorForTest) {
      this.state = {
        hasError: true,
        error: p.mockErrorForTest
      };
    }
  }

  // There is not way to access/test this
  // istanbul ignore next
  static getDerivedStateFromError(error: Error): State {
    return { hasError: true, error };
  }

  render() {
    if (this.state.hasError) {
      return (
        <Redirect
          to={{
            pathname: '/error',
            state: { error: this.state.error }
          }}
        />
      );
    }

    return isValidElement(this.props.children) ? (
      this.props.children
    ) : (
      <Redirect to="/" />
    );
  }
}
