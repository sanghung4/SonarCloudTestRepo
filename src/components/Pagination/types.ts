export interface PaginationProps {
  currentPage: number;
  totalPages: number;
  updatePage: (newPage: number) => void;
  totalCount: number;
}

export interface PageButtonProps {
  active?: boolean;
  dots?: boolean;
  pageNumber?: number;
  onClick?: (page: number) => void;
}
