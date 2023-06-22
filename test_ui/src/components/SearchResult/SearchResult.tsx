import { useEffect, useState } from "react";
import { useAppSelector, useAppDispatch } from "../../hooks/redux";
import {setFilters} from "../../store/reducers/UserSlice"
import { throttle } from "lodash";
import { ArrowUp } from "react-bootstrap-icons";
import {Alert, CircularProgress} from "@mui/material";


import Table from "react-bootstrap/Table";

import "./SearchResult.css";
import { searchProduct, DEFAULT_PAGE_SIZE } from "../../store/reducers/UserActionCreator";
import Sidebar from "../Sidebar/Sidebar";

const REECE_URL = "https://morrisonsupply.reece.com/product/"

const SearchResult = ({ searchText} : { searchText: string}) => {
  const dispatch = useAppDispatch();
  const { searchResult, isLoading, userError, apiFilters } = useAppSelector((state) => state.userReducer);
  const [results, setResults] = useState<any>([]);
  const [error, setError] = useState<string>("");
  const [page, setPage] = useState(-1);
  const [previousPage, setPreviousPage] = useState(0);
  const [currentScrollY, setCurrentScrollY] = useState(0);
  const [apiCall, setApiCall] = useState<boolean>(true);

  const errorMessages: any = {
    204: "No Products Found.",
  };

  useEffect(() => {
    setError("");
 
    if (localStorage.getItem("search_query_submit") === "new") setResults([]);
    if (searchResult.data && !isLoading) {
      if (localStorage.getItem("search_query_submit") === "new") {
        if (searchResult?.data?.results.length === 0) setResults([]);
        else setResults(searchResult?.data?.results);

        if (searchResult?.data?.results.length < DEFAULT_PAGE_SIZE) setPage(-1);
        else {
          setPage(2);
          setPreviousPage(1);
        }
        createFilters()
        localStorage.setItem("search_query_submit", "old");
      } else if (localStorage.getItem("search_query_submit") === "old") {
        if (searchResult?.data?.results.length === DEFAULT_PAGE_SIZE) {
          setResults((prev: any) => [...prev, ...searchResult?.data?.results]);
          setPage(page + 1);
          setPreviousPage(page);
        } else {
          setPage(-1);
        }
      }
    } else {
      if (searchResult && searchResult.status && searchResult.status !== 200) {
        setPage(-1);
        if (localStorage.getItem("search_query_submit") === "new") {
          setError(
            `${searchResult?.status} - ${
              errorMessages[searchResult?.status] ?? searchResult?.statusText
            } `
          );
          setResults([]);
        }
        localStorage.removeItem("search_query_submit");
      }
    }
    setApiCall(false);
  }, [searchResult?.data]);

  useEffect(() => {
    // Add scroll event listener to window
    window.addEventListener("scroll", handleScroll);
    // Cleanup
    return () => window.removeEventListener("scroll", handleScroll);
  }, [page, isLoading]);

  useEffect(() => {
    window.addEventListener("scroll", () => {
      setCurrentScrollY(window.scrollY);
    });

    return () =>
      window.removeEventListener("scroll", () => {
        setCurrentScrollY(window.scrollY);
      });
  }, []);

  const createFilters = ( ) => {
    const filters = searchResult?.data?.facets
    dispatch(setFilters(filters))
}

  const handleScroll = throttle(() => {
    if (page !== -1 && previousPage !== page && !isLoading) {
      // Get the current scroll position
      const scrollTop = window.pageYOffset;

      // Get the current height of the document
      const documentHeight = document.body.offsetHeight;
      const windowHeight = window.innerHeight;

      // If the scroll position is close to the bottom of the document,
      // initiate a fetch for the next page of data
      if (scrollTop + windowHeight > documentHeight - 50) {
        callSearchAPI();
      }
    }
  }, 2000);

  const callSearchAPI = () => {
    if (page !== -1 && !apiCall) {
      setApiCall(true);
      dispatch(
        searchProduct({
          searchData: localStorage.getItem("search_text") as string,
          page: page,
          apiFilters: apiFilters
        })
      );
    }
  };

  return (
    <div className="search-result-wrapper">
      <hr />

      <p className="mb-3 search-result-title">
        <b>SEARCH RESULTS</b>
      </p>
      <div style={{display: "flex"}}>
         <Sidebar />
      <>
      {userError.length > 0 &&  <Alert severity="error" sx={{ whiteSpace: "pre-wrap" }}>{userError}</Alert>}

      <div style={{display:"flex", flexDirection:"column", width: "100%"}}>
      {error.length > 0 ? (
        !isLoading ? (
          <Alert severity="error" sx={{ whiteSpace: "pre-wrap" }}>{error}</Alert>
        ) : (
          <></>
        )
      ) : results.length > 0 ? (
        <Table className="search-table" responsive bordered>
          <thead>
            <tr>
              <th style={{ width: "120px" }}>INTERNAL ID</th>
              <th>VENDOR PART #</th>
              <th>MFR FULL NAME</th>
              <th>CATEGORY3</th>
              <th>DESCRIPTION</th>
              <th>IMAGE</th>
            </tr>
          </thead>
          <tbody>
            {results.map((item: any, key: number) => (
              <tr key={key}>
                <td>
                  <a
                    href={REECE_URL + item?.erp_product_id?.raw}
                    target="_blank"
                    rel="noreferrer"
                  >
                    {" "}
                    {item?.erp_product_id?.raw}{" "}
                  </a>
                </td>
                <td>{item?.vendor_part_nbr?.raw}</td>
                <td>{item?.mfr_full_name?.raw}</td>
                <td>{item?.category_3_name?.raw}</td>
                <td>
                  {item?.web_description?.raw
                    ? item?.web_description?.raw
                    : " " + item?.product_overview_description?.raw
                    ? item?.product_overview_description?.raw
                    : ""}
                </td>
                <td>
                  <a href={REECE_URL + item?.erp_product_id?.raw}>
                    <img
                      src={item?.thumbnail_image_url_name?.raw?.replace(" .jpg", ".jpg")}
                      alt={item?.mfr_full_name?.raw !== null ? item?.mfr_full_name?.raw : item?.erp_product_id?.raw}
                    />
                  </a>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      ) : (
        <></>
      )}
      {isLoading ? (
        <div className="spinner-container">
          <CircularProgress />
        </div>
      ) : (
        <></>
      )}
      </div>
      {currentScrollY > 400 && (
        <div
          className="scroll-to-top"
          onClick={() => {
            window.scrollTo({
              top: 0,
              behavior: "smooth",
            });
          }}
        >
          <ArrowUp height={20} width={20} color="white" />
        </div>
      )}
      </>
    </div>
    </div>
  );
};

export default SearchResult;
