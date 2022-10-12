package io.shuvalov.test.reminder.controller;

import io.shuvalov.test.reminder.domain.Reminder;
import io.shuvalov.test.reminder.repository.ReminderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/reminders")
public class ReminderController {
	private final ReminderRepository repository;

	public ReminderController(ReminderRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	public List<Reminder> list() {
		return repository.findAll();
	}

	@PostMapping
	public ResponseEntity create(@RequestBody Reminder reminder) throws URISyntaxException {
		Reminder saved = repository.save(reminder);
		return ResponseEntity.created(new URI("/reminder/" + saved.getId())).body(saved);
	}
}
