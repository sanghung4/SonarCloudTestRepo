import { useMemo } from "react";
import { ChevronLeftIcon, ChevronRightIcon } from "@heroicons/react/solid";
import { PageButton } from "./PageButton";
import { PaginationProps } from "./types";
import { DOTS, arrayOfRange } from "./utils";

const SIBLING_COUNT = 2;

const Pagination = ({ currentPage, totalPages, totalCount, updatePage }: PaginationProps) => {
  const handleBackButtonClick = () => {
    updatePage(currentPage - 1);
  };
  const handleNextButtonClick = () => {
    updatePage(currentPage + 1);
  };
  const handlePageClick = (page: number) => {
    updatePage(page);
  };

  const paginationRange = useMemo(() => {
    // Pages count is determined as siblingCount + firstPage + lastPage + currentPage + 2*DOTS
    const totalPageNumbers = SIBLING_COUNT + 5;

    /*
      Case 1:
      If the number of pages is less than the page numbers we want to show in our
      paginationComponent, we return the range [1..totalPageCount]
    */
    if (totalPageNumbers >= totalPages) {
      return arrayOfRange(1, totalPages);
    }

    /*
      Calculate left and right sibling index and make sure they are within range 1 and totalPageCount
    */
    const leftSiblingIndex = Math.max(currentPage - SIBLING_COUNT, 1);
    const rightSiblingIndex = Math.min(currentPage + SIBLING_COUNT, totalPages);

    /*
      We do not show dots just when there is just one page number to be inserted between the extremes of sibling and the page limits i.e 1 and totalPageCount. Hence we are using leftSiblingIndex > 2 and rightSiblingIndex < totalPageCount - 2
    */
    const shouldShowLeftDots = leftSiblingIndex > 2;
    const shouldShowRightDots = rightSiblingIndex < totalPages - 2;

    const firstPageIndex = 1;
    const lastPageIndex = totalPages;

    /*
      Case 2: No left dots to show, but rights dots to be shown
    */
    if (!shouldShowLeftDots && shouldShowRightDots) {
      const leftItemCount = 3 + 2 * SIBLING_COUNT;
      const leftRange = arrayOfRange(1, leftItemCount);

      return [...leftRange, DOTS, totalPages];
    }

    /*
      Case 3: No right dots to show, but left dots to be shown
    */
    if (shouldShowLeftDots && !shouldShowRightDots) {
      const rightItemCount = 3 + 2 * SIBLING_COUNT;
      const rightRange = arrayOfRange(totalPages - rightItemCount + 1, totalPages);
      return [firstPageIndex, DOTS, ...rightRange];
    }

    /*
      Case 4: Both left and right dots to be shown
    */
    if (shouldShowLeftDots && shouldShowRightDots) {
      const middleRange = arrayOfRange(leftSiblingIndex, rightSiblingIndex);
      return [firstPageIndex, DOTS, ...middleRange, DOTS, lastPageIndex];
    }
  }, [totalPages, SIBLING_COUNT, currentPage]);

  return (
    <div className='bg-white px-4 py-3 flex items-center rounded-b justify-between border-t border-gray-200'>
      <div className='flex-1 flex justify-between sm:hidden'>
        <button className='relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50'>
          Previous
        </button>
        <button className='ml-3 relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50'>
          Next
        </button>
      </div>
      <div className='hidden sm:flex-1 sm:flex sm:items-center sm:justify-between'>
        <div>
          <p className='text-sm text-gray-700'>
            Showing <span className='font-medium'>{currentPage * 10 - 9}</span> to{" "}
            <span className='font-medium'>{Math.min(currentPage * 10, totalCount)}</span> of{" "}
            <span className='font-medium'>{totalCount}</span> results
          </p>
        </div>
        <div>
          <nav
            className='relative z-0 inline-flex rounded-md shadow-sm -space-x-px'
            aria-label='Pagination'
          >
            <button
              disabled={currentPage === 1}
              onClick={handleBackButtonClick}
              className='relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50'
            >
              <span className='sr-only'>Previous</span>
              <ChevronLeftIcon className='h-5 w-5' aria-hidden='true' />
            </button>
            {paginationRange?.map((page, index) => (
              <PageButton
                dots={page === DOTS}
                active={page === currentPage}
                pageNumber={page}
                key={index}
                onClick={handlePageClick}
              />
            ))}
            <button
              disabled={currentPage === totalPages}
              onClick={() => handleNextButtonClick()}
              className='relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50'
            >
              <span className='sr-only'>Next</span>
              <ChevronRightIcon className='h-5 w-5' aria-hidden='true' />
            </button>
          </nav>
        </div>
      </div>
    </div>
  );
};
export default Pagination;
