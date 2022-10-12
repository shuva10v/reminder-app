package io.shuvalov.test.reminder.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Reminder {
	@Id
	@GeneratedValue
	private Long id;

	private String name;

	

	// TODO
}
