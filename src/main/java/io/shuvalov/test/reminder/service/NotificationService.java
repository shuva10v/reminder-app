package io.shuvalov.test.reminder.service;

import io.shuvalov.test.reminder.domain.Reminder;

public interface NotificationService {
	void notifyUser(Reminder reminder);
}
