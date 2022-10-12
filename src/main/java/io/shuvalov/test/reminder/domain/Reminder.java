package io.shuvalov.test.reminder.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.OffsetDateTime;

@Entity
@Data
public class Reminder {
	@Id
	@GeneratedValue
	private Long id;

	private String name;

	private String description;

	private OffsetDateTime time;

	private Boolean notified = false;
}
