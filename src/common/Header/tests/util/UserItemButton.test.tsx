import { fireEvent } from '@testing-library/react';
import { UserItemButton } from 'common/Header/util';
import { render } from 'test-utils/TestWrapper';

describe('common - Header - util - UserItemButton', () => {
  it('should match snapshot', () => {
    const props = {
      label: 'test',
      path: '/test',
      push: jest.fn(),
      setOpen: jest.fn()
    };
    const { container } = render(<UserItemButton {...props} />);
    expect(container).toMatchSnapshot();
  });

  it('should match snapshot with isSignOut', () => {
    const props = {
      isSignOut: true,
      label: 'test',
      path: '/test',
      push: jest.fn(),
      setOpen: jest.fn()
    };
    const { container } = render(<UserItemButton {...props} />);
    expect(container).toMatchSnapshot();
  });

  it('expect setOpen and push to be called when clicked', () => {
    const props = {
      isSignOut: true,
      label: 'test',
      path: '/test',
      push: jest.fn(),
      setOpen: jest.fn()
    };
    const { getByText } = render(<UserItemButton {...props} />);
    fireEvent.click(getByText(props.label));
    expect(props.setOpen).toBeCalledWith(false);
    expect(props.push).toBeCalledWith(props.path);
  });
});
