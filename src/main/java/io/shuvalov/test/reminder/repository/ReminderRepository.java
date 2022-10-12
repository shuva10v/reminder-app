package io.shuvalov.test.reminder.repository;

import io.shuvalov.test.reminder.domain.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {
}
