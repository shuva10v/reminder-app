package io.shuvalov.test.reminder.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(	name = "users",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = "email")
		})
@Data
public class User {
	@Id
	@GeneratedValue
	private Long id;

	private String email;

	private String password;


}
