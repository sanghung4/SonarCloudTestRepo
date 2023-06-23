import { render, screen } from '@testing-library/react';

import { Pagination, PaginationProps } from 'components/Table';
import { fillTextInput, fireEvent } from 'test-util';

/**
 * Mocks
 */
const defaultMockProps: PaginationProps = { 'data-testid': 'pagination' };
let mockProps: PaginationProps = { ...defaultMockProps };

/**
 * TEST
 */
describe('components/Pagination', () => {
  // ⚪ Reset mocks
  afterEach(() => {
    mockProps = { ...defaultMockProps };
  });

  // 🟢 1 - default
  it('Expect to match text and disable state on default', () => {
    // act
    render(<Pagination {...mockProps} />);
    const input = screen.queryByTestId('pagination-input');
    const total = screen.queryByTestId('pagination-total');
    const prev = screen.queryByTestId<HTMLButtonElement>('pagination-previous');
    const next = screen.queryByTestId<HTMLButtonElement>('pagination-next');
    // assert
    expect(input).toHaveValue('1');
    expect(total).toHaveTextContent('1');
    expect(prev?.disabled).toBeTruthy();
    expect(next?.disabled).toBeTruthy();
  });

  // 🟢 1 - filled
  it('Expect to match text and disable state on filled custom values', () => {
    // arrange
    mockProps.currentPage = 2;
    mockProps.totalPages = 5;
    // act
    render(<Pagination {...mockProps} />);
    const input = screen.queryByTestId('pagination-input');
    const total = screen.queryByTestId('pagination-total');
    const prev = screen.queryByTestId<HTMLButtonElement>('pagination-previous');
    const next = screen.queryByTestId<HTMLButtonElement>('pagination-next');
    // assert
    expect(input).toHaveValue(`${mockProps.currentPage}`);
    expect(total).toHaveTextContent(`${mockProps.totalPages}`);
    expect(prev?.disabled).toBeFalsy();
    expect(next?.disabled).toBeFalsy();
  });

  // 🟢 2 - previous click
  it('Expect `onChange` is called when previous button is pressed with updated value', async () => {
    // arrange
    mockProps.currentPage = 2;
    mockProps.totalPages = 3;
    mockProps.onChange = jest.fn();
    // act 1
    render(<Pagination {...mockProps} />);
    const prev = screen.queryByTestId<HTMLButtonElement>('pagination-previous');
    // assert 1
    expect(prev?.disabled).toBeFalsy();
    // act 2
    await fireEvent('click', 'pagination-previous');
    // assert 2
    expect(prev?.disabled).toBeTruthy();
    expect(mockProps.onChange).toBeCalledWith(mockProps.currentPage - 1);
  });

  // 🟢 3 - next click
  it('Expect `onChange` is called when next button is pressed with updated value', async () => {
    // arrange
    mockProps.currentPage = 2;
    mockProps.totalPages = 3;
    mockProps.onChange = jest.fn();
    // act 1
    render(<Pagination {...mockProps} />);
    const next = screen.queryByTestId<HTMLButtonElement>('pagination-next');
    // assert 1
    expect(next?.disabled).toBeFalsy();
    // act 2
    await fireEvent('click', 'pagination-next');
    // assert 2
    expect(next?.disabled).toBeTruthy();
    expect(mockProps.onChange).toBeCalledWith(mockProps.currentPage + 1);
  });

  // 🟢 4 - input
  it('Expect `onChange` is called when to update input', async () => {
    // arrange
    mockProps.currentPage = 2;
    mockProps.totalPages = 5;
    mockProps.onChange = jest.fn();
    // act
    render(<Pagination {...mockProps} />);
    await fillTextInput('pagination-input', '4');
    // assert
    expect(mockProps.onChange).toBeCalledWith(4);
  });

  // 🟢 5 - unchanged input
  it('Expect `onChange` is NOT called when to update input with the same value', async () => {
    // arrange
    mockProps.currentPage = 2;
    mockProps.totalPages = 5;
    mockProps.onChange = jest.fn();
    // act
    render(<Pagination {...mockProps} />);
    await fillTextInput('pagination-input', `${mockProps.currentPage}`);
    // assert
    expect(mockProps.onChange).not.toBeCalled();
  });

  // 🟢 6 - letter
  it('Expect `onChange` is called with 1 when input is a letter', async () => {
    // arrange
    mockProps.currentPage = 2;
    mockProps.totalPages = 5;
    mockProps.onChange = jest.fn();
    // act
    render(<Pagination {...mockProps} />);
    await fillTextInput('pagination-input', 'a');
    // assert
    expect(mockProps.onChange).toBeCalledWith(1);
  });

  // 🟢 7 - blank
  it('Expect `onChange` is called with 1 when input is blank', async () => {
    // arrange
    mockProps.currentPage = 2;
    mockProps.totalPages = 5;
    mockProps.onChange = jest.fn();
    // act
    render(<Pagination {...mockProps} />);
    await fillTextInput('pagination-input', '');
    // assert
    expect(mockProps.onChange).toBeCalledWith(1);
  });

  // 🟢 8 - out of bound input
  it('Expect `onChange` is called with out of bound input', async () => {
    // arrange
    mockProps.currentPage = 2;
    mockProps.totalPages = 5;
    mockProps.onChange = jest.fn();
    // act
    render(<Pagination {...mockProps} />);
    await fillTextInput('pagination-input', '45');
    // assert
    expect(mockProps.onChange).toBeCalledWith(mockProps.totalPages);
  });

  // 🟢 9 - out of bound input (highest page)
  it('Expect `onChange` NOT called with out of bound input on the highest page', async () => {
    // arrange
    mockProps.currentPage = 5;
    mockProps.totalPages = 5;
    mockProps.onChange = jest.fn();
    // act
    render(<Pagination {...mockProps} />);
    await fillTextInput('pagination-input', '45');
    // assert
    expect(mockProps.onChange).not.toBeCalledWith(mockProps.totalPages);
  });

  // 🟢 10 - input with [Enter]
  it('Expect `onChange` is called with Enter', async () => {
    // arrange
    mockProps.currentPage = 2;
    mockProps.totalPages = 5;
    mockProps.onChange = jest.fn();
    // act
    render(<Pagination {...mockProps} />);
    await fillTextInput('pagination-input', '45', true);
    await fireEvent('keyDown', 'pagination-input', { key: 'b' });
    await fireEvent('keyDown', 'pagination-input', { key: 'Enter' });
    // assert
    expect(mockProps.onChange).toBeCalledWith(mockProps.totalPages);
  });
});
