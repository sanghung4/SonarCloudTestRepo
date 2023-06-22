import { useState, useRef } from "react";
import { useAppSelector } from "../../hooks/redux";

import "./CategorizationResult.css";

const CategorizationResult = () => {
  const [showMore, setShowMore] = useState<boolean>(false);
  const { categorizedResult, isLoading } = useAppSelector((state) => state.userReducer);

  const parentRef = useRef<HTMLDivElement>(null);

  const handleShowToggle = () => setShowMore(!showMore);

  return (
    <div className="categorization-wrapper">
      <p style={{ paddingBottom: "10px" }}>
        <b className="categorization-title">PART OF SPEECH TAGGER</b>
        {isLoading && " searching..."}
      </p>
      {Object.keys(categorizedResult).length > 0 && (
        <>
          <div className="category" ref={parentRef}>
            {Object.keys(categorizedResult).map((key) => (
              <div key={key} className="category-item">
                <p>{categorizedResult[key]?.pos?.toUpperCase()}</p>
                <p className="category-item-text">{categorizedResult[key]?.text?.toUpperCase()}</p>
              </div>
            ))}
          </div>
          <span className="show-more" onClick={handleShowToggle}>
            Show {showMore ? "less" : "more"}
          </span>
          {showMore ? (
            <div
              className="category-other-info"
              style={{
                width: parentRef.current?.offsetWidth,
                maxWidth: parentRef.current?.offsetWidth,
              }}
            >
              {Object.keys(categorizedResult).map((key) => (
                <pre key={key} style={{ width: 160 }} className="json-field">
                  {JSON.stringify(categorizedResult[key], null, 2).slice(1, -1)}
                </pre>
              ))}
            </div>
          ) : (
            <></>
          )}
        </>
      )}
    </div>
  );
};

export default CategorizationResult;
