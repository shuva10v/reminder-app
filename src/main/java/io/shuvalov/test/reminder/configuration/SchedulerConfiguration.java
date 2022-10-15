package io.shuvalov.test.reminder.configuration;

import io.shuvalov.test.reminder.service.SchedulerJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import java.io.IOException;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@Configuration
@Slf4j
public class SchedulerConfiguration {
	@Value("${reminder.scheduler.interval.s}")
	private Integer interval;

	@Autowired
	private ApplicationContext applicationContext;

	@Bean
	public SpringBeanJobFactory springBeanJobFactory() {
		AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
		jobFactory.setApplicationContext(applicationContext);
		return jobFactory;
	}

	@Bean
	public Scheduler scheduler(Trigger trigger, JobDetail job, SchedulerFactoryBean factory) throws SchedulerException {
		Scheduler scheduler = factory.getScheduler();
		scheduler.scheduleJob(job, trigger);
		
		scheduler.start();
		return scheduler;
	}

	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
		SchedulerFactoryBean factory = new SchedulerFactoryBean();
		factory.setJobFactory(springBeanJobFactory());
		factory.setQuartzProperties(quartzProperties());
		return factory;
	}

	public Properties quartzProperties() throws IOException {
		PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
		propertiesFactoryBean.afterPropertiesSet();
		return propertiesFactoryBean.getObject();
	}

	@Bean
	public JobDetail jobDetail() {

		return newJob().ofType(SchedulerJob.class).storeDurably()
				.withIdentity(JobKey.jobKey("Reminder scheduler"))
				.withDescription("Reminder scheduler").build();
	}

	@Bean
	public Trigger trigger(JobDetail job) {
		return newTrigger().forJob(job).withIdentity(TriggerKey
				.triggerKey("trigger")).withDescription("Reminder scheduler")
				.withSchedule(simpleSchedule()
						.withIntervalInSeconds(interval).repeatForever()).build();
	}
}
