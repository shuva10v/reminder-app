package io.shuvalov.test.reminder.controller;

import io.shuvalov.test.reminder.domain.Reminder;
import io.shuvalov.test.reminder.domain.User;
import io.shuvalov.test.reminder.repository.ReminderRepository;
import io.shuvalov.test.reminder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reminders")
public class ReminderController {
	@Autowired
	private ReminderRepository reminderRepository;

	@Autowired
	private UserRepository userRepository;

	@GetMapping
	public ResponseEntity<List<Reminder>> list(Principal principal) {
		Optional<User> user = userRepository.findByEmail(principal.getName());
		if (user.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(reminderRepository.findAllByOwner(user.get()));
	}

	@DeleteMapping("/{reminderId}")
	public ResponseEntity delete(@PathVariable("reminderId") Long reminderId) {
		reminderRepository.deleteById(reminderId);
		return ResponseEntity.ok("OK");
	}

	@PostMapping
	public ResponseEntity create(@RequestBody Reminder reminder, Principal principal) throws URISyntaxException {
		Optional<User> user = userRepository.findByEmail(principal.getName());
		if (user.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		reminder.setOwner(user.get());
		reminder.setCreated(ZonedDateTime.now());
		Reminder saved = reminderRepository.save(reminder);
		return ResponseEntity.created(new URI("/reminder/" + saved.getId())).body(saved);
	}
}
