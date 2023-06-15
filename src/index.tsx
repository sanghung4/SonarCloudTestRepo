import App from "./App";
import reportWebVitals from "./reportWebVitals";
import { render } from "react-dom";
import { ApolloProvider } from "@apollo/client";
import { client } from "./apolloClient";
import "react-date-range/dist/styles.css"; // main style file
import "react-date-range/dist/theme/default.css"; // theme css file
import "react-toastify/dist/ReactToastify.css";
import "./index.css";
import "./polyfills";

render(
  <ApolloProvider client={client}>
    <App />
  </ApolloProvider>,

  document.getElementById("root")
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
