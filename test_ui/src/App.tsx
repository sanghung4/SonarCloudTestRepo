import { Route, Routes } from "react-router";

import Search from "./pages/Search";

import "./App.css";

function App() {
  return (
    <div className="App">
      <Routes>
        <Route path="/" element={<Search />} />
      </Routes>
    </div>
  );
}

export default App;
