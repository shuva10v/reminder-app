package io.shuvalov.test.reminder.repository;

import io.shuvalov.test.reminder.domain.Reminder;
import io.shuvalov.test.reminder.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {
	List<Reminder> findAllByNotifiedIsFalseAndTimeLessThanEqual(OffsetDateTime time);

	List<Reminder> findAllByOwner(User owner);
}
