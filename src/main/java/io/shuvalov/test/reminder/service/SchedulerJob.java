package io.shuvalov.test.reminder.service;

import io.shuvalov.test.reminder.domain.Reminder;
import io.shuvalov.test.reminder.repository.ReminderRepository;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@Slf4j
public class SchedulerJob implements Job {
	@Autowired
	private ReminderRepository reminderRepository;

	@Autowired
	private NotificationService notificationService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("Scheduling notifications check");
		for(Reminder reminder: reminderRepository.findAllByNotifiedIsFalseAndTimeLessThanEqual(ZonedDateTime.now())) {
			log.info("Reminder {} is fired", reminder.getId());
			notificationService.notifyUser(reminder);
			reminder.setNotified(true);
			reminderRepository.save(reminder);
			log.info("Notification successfully sent for reminder {}", reminder.getId());
		}
	}
}
