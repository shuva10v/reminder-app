import './App.css';
import {Provider as AlertProvider} from 'react-alert'
import AlertTemplate from 'react-alert-template-mui'
import Main from "./Main";

function App() {
  return (
    <div className="App">
      <AlertProvider template={AlertTemplate}>
        <Main/>
      </AlertProvider>
    </div>
  );
}

export default App;
