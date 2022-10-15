package io.shuvalov.test.reminder.repository;

import io.shuvalov.test.reminder.domain.Reminder;
import io.shuvalov.test.reminder.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
public class ReminderRepositoryTest {
	@Autowired
	private ReminderRepository repository;

	@Autowired
	private UserRepository userRepository;

	@Test
	void createAndFind() {
		Reminder reminder = new Reminder();
		reminder.setName("test1");
		reminder.setTime(ZonedDateTime.now());
		Reminder saved = repository.save(reminder);
		Assertions.assertEquals(reminder.getName(), saved.getName());
		Assertions.assertEquals(reminder.getTime(), saved.getTime());
		Assertions.assertNotNull(saved.getId());
	}

	@Test
	void searchForReadyNotifications() {
		Reminder reminder1 = new Reminder();
		reminder1.setName("test1");
		reminder1.setTime(ZonedDateTime.of(2022, 10, 10, 15, 5, 5, 0, ZoneId.systemDefault()));
		reminder1 = repository.save(reminder1);

		Reminder reminder2 = new Reminder();
		reminder2.setName("test2");
		reminder2.setTime(ZonedDateTime.of(2022, 10, 11, 15, 5, 5, 0, ZoneId.systemDefault()));
		reminder2 = repository.save(reminder2);

		Reminder reminder3 = new Reminder();
		reminder3.setName("test2");
		reminder3.setTime(ZonedDateTime.of(2022, 10, 12, 15, 5, 5, 0, ZoneId.systemDefault()));
		reminder3 = repository.save(reminder3);

		Collection<Reminder> list = repository.findAllByNotifiedIsFalseAndTimeLessThanEqual(ZonedDateTime
				.of(2022, 10, 9, 15, 5, 5, 0, ZoneId.systemDefault()));
		Assertions.assertEquals(0, list.size());

		list = repository.findAllByNotifiedIsFalseAndTimeLessThanEqual(ZonedDateTime
				.of(2022, 10, 10, 17, 5, 5, 0, ZoneId.systemDefault()));
		Assertions.assertEquals(1, list.size());
		Assertions.assertEquals(List.of(reminder1), list);

		list = repository.findAllByNotifiedIsFalseAndTimeLessThanEqual(ZonedDateTime
				.of(2022, 10, 11, 17, 5, 5, 0, ZoneId.systemDefault()));
		Assertions.assertEquals(2, list.size());
		Assertions.assertEquals(List.of(reminder1, reminder2), list);

		reminder1.setNotified(true);
		repository.save(reminder1);

		list = repository.findAllByNotifiedIsFalseAndTimeLessThanEqual(ZonedDateTime
				.of(2022, 10, 11, 17, 5, 5, 0, ZoneId.systemDefault()));
		Assertions.assertEquals(1, list.size());
		Assertions.assertEquals(List.of(reminder2), list);
	}

	@Test
	void reminderOwnership() {
		User user1 = new User();
		user1.setEmail("user1");
		userRepository.save(user1);
		User user2 = new User();
		user2.setEmail("user2");
		userRepository.save(user2);

		Reminder reminder1 = new Reminder();
		reminder1.setName("test1");
		reminder1.setTime(ZonedDateTime.of(2022, 10, 10, 15, 5, 5, 0, ZoneId.systemDefault()));
		reminder1.setOwner(user1);
		repository.save(reminder1);

		Reminder reminder2 = new Reminder();
		reminder2.setName("test1");
		reminder2.setTime(ZonedDateTime.of(2022, 10, 10, 15, 5, 5, 0, ZoneId.systemDefault()));
		reminder2.setOwner(user2);
		repository.save(reminder2);

		Assertions.assertEquals(List.of(reminder1), repository.findAllByOwner(user1));
		Assertions.assertEquals(List.of(reminder2), repository.findAllByOwner(user2));
	}
}
