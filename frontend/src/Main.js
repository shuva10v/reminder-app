import {useAlert} from 'react-alert'
import Reminders from "./Reminders";
import {HashRouter, Route, Routes} from "react-router-dom";
import {useState} from "react";
import {useEffect} from "react";
import {
  Box,
  Button,
  CircularProgress,
  Container,
  FormControl,
  Input,
  InputLabel,
  TextField,
  Typography
} from "@mui/material";
import jwtHeaders from "./utils";

function Main() {
  const alert = useAlert();
  const [user, setUser] = useState();
  const [jwtToken, setJwtToken] = useState();
  const [email, setEmail] = useState();
  const [password, setPassword] = useState();

  useEffect(() => {
    if (user !== undefined) {
      return;
    }
    fetch("/user/whoami", jwtHeaders(jwtToken))
      .then(response => {
        if (response.status === 401) {
          setJwtToken(-1);
        } else if (response.status === 200) {
          // console.log("User ok", response.json())
          response.json().then(obj => setUser(obj));
        } else {
          return response.text().then(text => alert.show("Unknown error: " + text));
        }
      })
      .catch(error => alert.show("Request error: " + error));
  }, [user, jwtToken]);

  function signupOrAuth(signup) {
    if (signup) {
      // TODO validation
    }
    fetch(signup ? "/user/signup" : "/user/authenticate", {
      method: 'POST',
      body: JSON.stringify({email: email, password: password}),
      headers: {'Content-type': 'application/json'}
    }).then(response => {
      if (response.ok) {
        response.json().then(obj => setJwtToken(obj.jwttoken));
      } else if (response.status === 409) {
        console.log("Conflict detected");
        alert.show("Email already registered");
      } else {
        response.text().then(text => alert.show("Signup error: " + text));
      }
    })
      .catch(error => alert.show("Request error: " + error));
  }

  console.log("User", user);
  if (jwtToken === undefined) {
    return <CircularProgress/>
  } else if (jwtToken === -1) {
    return (<Container>
     <Box>
       <Typography>Login:</Typography>
       <FormControl margin='normal'  sx={{
         '& > :not(style)': { m: 1},
       }}>
         <TextField id="email" label="Email" variant="filled" onChange={(e) =>
           setEmail(e.target.value)}/>
         <TextField id="password" type="password" label="Password" variant="filled" onChange={(e) =>
           setPassword(e.target.value)}/>
         <Button variant='contained' onClick={() => signupOrAuth(false)}>Login</Button>
         <Button variant='contained' onClick={() => signupOrAuth(true)}>Sign up</Button>
       </FormControl>
     </Box>
    </Container>)
  } else {
    return (
      <div>
        <HashRouter>
          <Routes>
            <Route path="/reminders" element={<Reminders />} />
          </Routes>
        </HashRouter>
      </div>
    );
  }
}

export default Main;
