import {useAlert} from "react-alert";
import {useEffect, useState} from "react";
import {
  Box, Button,
  CircularProgress,
  Container,
  IconButton,
  Paper,
  Table, TableBody, TableCell,
  TableContainer, TableHead, TableRow,
  Typography
} from "@mui/material";
import jwtHeaders from "./utils";
import {AddCircle, RefreshOutlined} from "@mui/icons-material";
import AddReminder from "./AddReminder";

function Reminders(props) {
  const alert = useAlert();
  const [reminders, setReminders] = useState();

  function updateReminders() {
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
  }

  useEffect(() => {
    if (reminders !== undefined) {
      return;
    }
    updateReminders();
  })

  function remove(reminderId) {
    fetch("/reminders/" + reminderId,
      {...jwtHeaders(props.jwtToken), ...{method: 'DELETE'}})
      .then(async(response) => {
        if (!response.ok) {
          const err = await response.text();
          throw new Error("Wrong response: " + response.status + '\n' + err);
        } else {
          alert.show("Reminder removed");
          updateReminders();
        }
      })
      .catch(error => alert.show("Request error: " + error));
  }
  
  if (reminders === undefined) {
    return <CircularProgress/>
  } else {
    return (
      <Container>
          <Box sx={{display: 'flex'}}>
            <Typography variant="h6" sx={{ flexGrow: 1 }}>
              Reminders
            </Typography>
            <Box sx={{display: 'flex'}}>
              <AddReminder
                jwtToken={props.jwtToken}
                update={updateReminders}
              />
              <IconButton onClick={updateReminders}>
                <RefreshOutlined/>
              </IconButton>
            </Box>
          </Box>

        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Name</TableCell>
                <TableCell>Description</TableCell>
                <TableCell>Reminder time</TableCell>
                <TableCell>Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {reminders.map((reminder, idx) => (
                <TableRow key={idx}>
                  <TableCell>{reminder.name}</TableCell>
                  <TableCell>{reminder.description}</TableCell>
                  <TableCell>{reminder.time} {reminder.notified ? "(notified)" : null}</TableCell>
                  <TableCell><Button onClick={() => remove(reminder.id)}>Remove</Button></TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </Container>);
  }
}

export default Reminders;
