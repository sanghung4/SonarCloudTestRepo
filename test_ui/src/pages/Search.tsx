import SearchField from "../components/SearchField/SearchField";
import CategorizationResult from "../components/CategorizationResult/CategorizationResult";

import ReeceBanner from "../assets/images/reece-logo-small.png";

import "./styles/Search.css";
import SearchResult from "../components/SearchResult/SearchResult";
import { useCallback, useState } from "react";

const Search = () => {

  const [searchText, setSearchText] = useState<string>("");

  const handleSearchText = useCallback(
    (value: string) => {
      setSearchText((prev) => value)
      localStorage.setItem('search_text', value);
    },
    []
  )

  return (
    <div className="search-wrapper">
      <div>
        <img src={ReeceBanner} alt="Reece Logo" width={230} height={105} />
      </div>

      <SearchField searchText={searchText} setSearchText={handleSearchText} />

      <CategorizationResult />

      <SearchResult searchText={searchText} />
    </div>
  );
};

export default Search;
