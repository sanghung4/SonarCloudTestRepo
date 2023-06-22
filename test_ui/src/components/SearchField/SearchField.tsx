import { useState, useEffect } from "react";
import { useAppDispatch, useAppSelector } from "../../hooks/redux";
import Form from "react-bootstrap/Form";
import { categorizeText, searchProduct } from "../../store/reducers/UserActionCreator";
import {clearApiFilters} from "../../store/reducers/UserSlice"

import "./SearchField.css";

const SearchField = ({
  searchText,
  setSearchText,
}: {
  searchText: string;
  setSearchText: (v: string) => void;
}) => {
  const dispatch = useAppDispatch();
  const { isLoading, apiFilters } = useAppSelector((state) => state.userReducer);

  const [suppressSearch, setSuppressSearch] = useState<boolean>(false);

  function handleSearch(clearFilters?: boolean) {
    if (searchText) {
      dispatch(categorizeText(searchText));
      localStorage.setItem("search_query_submit", "new");
      if (!suppressSearch) {
        if (clearFilters === true){
          dispatch(clearApiFilters())
          dispatch(searchProduct({ searchData: searchText, page: 1, apiFilters: {} }));
        }
        else
          dispatch(searchProduct({ searchData: searchText, page: 1, apiFilters: apiFilters }));
      }
    }
  }

  useEffect(() => {
    handleSearch(false)
  }, [apiFilters])
  

  return (
    <div style={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
      <div className="search-field">
        <input
          type="text"
          onChange={(e) => setSearchText(e.target.value)}
          onKeyDown={(e) => { e.key === "Enter" && handleSearch(true)}}
        />
        <button className="search-btn" disabled={isLoading} onClick={(e) => {handleSearch(true)}}>
          Search
        </button>
      </div>

      <Form>
        <Form.Group className="mb-3">
          <Form.Check
            type="checkbox"
            label="Suppress Product Search"
            checked={suppressSearch}
            onChange={(e) => setSuppressSearch(e.target.checked)}
          />
        </Form.Group>
      </Form>
    </div>
  );
};

export default SearchField;
