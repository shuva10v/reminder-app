import {useAlert} from "react-alert";
import {useEffect, useState} from "react";
import {Container, Typography} from "@mui/material";

function Reminders() {
  const alert = useAlert();
  const [reminders, setReminders] = useState([]);

  useEffect(() => {
    console.log("Reminders")
    fetch("/reminders")
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
