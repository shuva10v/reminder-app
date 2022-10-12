package io.shuvalov.test.reminder.repository;

import io.shuvalov.test.reminder.domain.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.Collection;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {
	Collection<Reminder> findAllByNotifiedIsFalseAndTimeLessThanEqual(OffsetDateTime time);
}
