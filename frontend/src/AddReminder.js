import {useAlert} from "react-alert";
import {useEffect, useState} from "react";
import {
  Box, Button,
  CircularProgress,
  Container,
  Dialog, DialogActions,
  DialogContent,
  DialogTitle, FormControl,
  IconButton, TextField,
  Typography
} from "@mui/material";
import jwtHeaders from "./utils";
import {AddCircle} from "@mui/icons-material";

function AddReminder(props) {
  const alert = useAlert();
  const [open, setOpen] = useState(false);
  const [name, setName] = useState();
  const [description, setDescription] = useState();
  const [date, setDate] = useState();

  function handleClose() {
    setOpen(false);
  }

  function create() {
    fetch("/reminders",
      {...jwtHeaders(props.jwtToken, {'Content-type': 'application/json'}), ...{
        method: 'POST',
        body: JSON.stringify({name: name, description: description}),
      }})
      .then(async(response) => {
        if (!response.ok) {
          const err = await response.text();
          throw new Error("Wrong response: " + response.status + '\n' + err);
        } else {
          console.log(response.json());
          setName(undefined);
          setDescription(undefined);
          setDate(undefined);
          setOpen(false);
          alert.show("Reminder created");
          props.update();
        }
      })
      .catch(error => {
        setOpen(false);
        alert.show("Request error: " + error);
      });
  }

  return (
    <Container>
      <IconButton onClick={() => setOpen(true)}>
        <AddCircle/>
      </IconButton>
      <Dialog onClose={handleClose} open={open}>
        <DialogTitle>Create reminder</DialogTitle>
        <DialogContent>
          <FormControl margin='normal'  sx={{
            '& > :not(style)': { m: 1},
          }}>
            <TextField id="name" label="Name" variant="filled" onChange={(e) =>
              setName(e.target.value)}/>
            <TextField id="description" label="Description" variant="filled" onChange={(e) =>
              setDescription(e.target.value)}/>
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
