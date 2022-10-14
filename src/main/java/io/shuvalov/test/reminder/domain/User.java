package io.shuvalov.test.reminder.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;

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

	// to avoid password leak to frontend
	@Getter(onMethod_ = @JsonIgnore)
	private String password;
}
