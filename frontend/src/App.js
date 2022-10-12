import './App.css';
import { Provider as AlertProvider } from 'react-alert'
import AlertTemplate from 'react-alert-template-mui'
import Reminders from "./Reminders";
import {HashRouter, Route, Routes} from "react-router-dom";

function App() {
  return (
    <div className="App">
      <AlertProvider template={AlertTemplate}>
        <HashRouter>
          <Routes>
            <Route path="/" element={<Reminders />} />
          </Routes>
        </HashRouter>
      </AlertProvider>
    </div>
  );
}

export default App;
