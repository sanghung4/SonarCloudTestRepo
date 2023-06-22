import { ReactNode, createContext, useState } from 'react';

type Props = {
  children: ReactNode;
};

type HeaderContextType = {
  searchOpen: boolean;
  setSearchOpen: (value: boolean) => void;
  searchPage: number;
  setSearchPage: (value: number) => void;
  trackedSearchTerm: string;
  setTrackedSearchTerm: (value: string) => void;
  pageIndex: number;
  setPageIndex: (value: number) => void;
};

export const HeaderContext = createContext<HeaderContextType>({
  searchOpen: false,
  setSearchOpen: () => {},
  searchPage: 0,
  setSearchPage: () => {},
  trackedSearchTerm: '',
  setTrackedSearchTerm: () => {},
  pageIndex: 0,
  setPageIndex: () => {}
});

export default function HeaderProvider(props: Props) {
  /**
   * State
   */
  const [searchOpen, setSearchOpen] = useState(false);
  const [searchPage, setSearchPage] = useState(0);
  const [trackedSearchTerm, setTrackedSearchTerm] = useState('');
  const [pageIndex, setPageIndex] = useState(0);

  /**
   * Provider Render
   */
  return (
    <HeaderContext.Provider
      value={{
        searchOpen,
        setSearchOpen,
        searchPage,
        setSearchPage,
        trackedSearchTerm,
        setTrackedSearchTerm,
        pageIndex,
        setPageIndex
      }}
    >
      {props.children}
    </HeaderContext.Provider>
  );
}
