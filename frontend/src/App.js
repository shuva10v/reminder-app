import './App.css';
import {Provider as AlertProvider, useAlert} from 'react-alert'
import AlertTemplate from 'react-alert-template-mui'
import Reminders from "./Reminders";
import {HashRouter, Route, Routes} from "react-router-dom";
import {useState} from "react";
import {useEffect} from "react";
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
