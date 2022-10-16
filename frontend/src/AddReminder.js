import {useAlert} from "react-alert";
import { useState} from "react";
import {
  Button,
  Container,
  Dialog, DialogActions,
  DialogContent,
  DialogTitle, FormControl,
  IconButton, TextField
} from "@mui/material";
import jwtHeaders from "./utils";
import {AddCircle} from "@mui/icons-material";
import {DateTimePicker} from "@mui/x-date-pickers/DateTimePicker";
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import {AdapterDayjs} from "@mui/x-date-pickers/AdapterDayjs";
import dayjs from "dayjs";

function AddReminder(props) {
  const alert = useAlert();
  const [open, setOpen] = useState(false);
  const [name, setName] = useState();
  const [description, setDescription] = useState();
  const [time, setTime] = useState(dayjs());

  function handleClose() {
    setOpen(false);
  }

  function create() {
    console.log(time);
    if (name === undefined || description === undefined) {
      alert.show("Name or description is empty");
      return;
    }
    if (!time.isValid()) {
      alert.show("Date time is invalid")
      return;
    }
    fetch("/reminders",
      {...jwtHeaders(props.jwtToken, {'Content-type': 'application/json'}), ...{
        method: 'POST',
        body: JSON.stringify({name: name, description: description, time: time.format()}),
      }})
      .then(async(response) => {
        if (!response.ok) {
          if (response.status === 401) {
            throw new Error("Your session is expired, please logout and login");
          } else {
            const err = await response.text();
            throw new Error("Wrong response: " + response.status + '\n' + err);
          }
        } else {
          setName(undefined);
          setDescription(undefined);
          setTime(undefined);
          setOpen(false);
          alert.show("Reminder created");
          props.update();
        }
      })
      .catch(error => {
        alert.error("Request error: " + error);
      });
  }

  return (
    <Container>
      <IconButton onClick={() => setOpen(true)}>
        <AddCircle/>
      </IconButton>
      <Dialog onClose={handleClose} open={open} disableEnforceFocus>
        <DialogTitle>Create reminder</DialogTitle>
        <DialogContent>
          <FormControl margin='normal'  sx={{
            '& > :not(style)': { m: 1},
          }}>
            <TextField id="name" label="Name" variant="filled" onChange={(e) =>
              setName(e.target.value)}/>
            <TextField id="description" label="Description" variant="filled" onChange={(e) =>
              setDescription(e.target.value)}/>

            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <DateTimePicker
                label="Reminder date and time"
                // value={time}
                type="datetime-local"
                onChange={(e) => setTime(e)}
                renderInput={(params) => <TextField {...params} />}
              />
            </LocalizationProvider>
            
          </FormControl>
        </DialogContent>
        <DialogActions>
          <Button onClick={create}>Create</Button>
          <Button onClick={handleClose}>Close</Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
}

export default AddReminder;
