import { Route, Routes } from "react-router-dom";
import "./App.css";
import "./init.js"
import HomePage from "./pages/HomePage";
import Status from './pages/Status';
import StatusViewer from './pages/StatusViewer';
import Signin from "./pages/Signin";
import Signup from "./pages/Signup";

function App() {
  return (
    <Routes>
      <Route path="/" element={<HomePage />}></Route>
      <Route path="/status" element={<Status />}></Route>
      <Route path="/status/:userId" element={<StatusViewer />}></Route>
      <Route path="/signin" element={<Signin />}></Route>
      <Route path="/signup" element={<Signup />}></Route>
    </Routes>
  );
}

export default App;
