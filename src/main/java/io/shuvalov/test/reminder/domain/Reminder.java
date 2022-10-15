package io.shuvalov.test.reminder.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.ZonedDateTime;

@Entity
@Data
public class Reminder {
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(name="ownerId")
	private User owner;

	private String name;

	private String description;

	private ZonedDateTime time;

	private ZonedDateTime created;

	private Boolean notified = false;
}
