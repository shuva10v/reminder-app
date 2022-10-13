import {useAlert} from "react-alert";
import {useEffect, useState} from "react";
import {Container, Typography} from "@mui/material";
import jwtHeaders from "./utils";

function Reminders(props) {
  const alert = useAlert();
  const [reminders, setReminders] = useState([]);

  useEffect(() => {
    fetch("/reminders", jwtHeaders(props.jwtToken))
      .then(async(response) => {
        if (!response.ok) {
          const err = await response.text();
          throw new Error("Wrong response: " + response.status + '\n' + err);
        } else {
          return response.json();
        }
      })
      .then(res => setReminders(res))
      .catch(error => alert.show("Request error: " + error));
  })

  console.log("reminders", reminders);
  return (
    <Container>
      <Typography>Reminders:</Typography>
      {reminders.map((idx, reminder) => (<Container key={idx}>
        {reminder.id} - {reminder.name} - {reminder.description}
      </Container>))}
    </Container>);
}

export default Reminders;
