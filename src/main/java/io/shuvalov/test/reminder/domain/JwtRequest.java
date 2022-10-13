package io.shuvalov.test.reminder.domain;

import lombok.Data;

@Data
public class JwtRequest {
	private String email;
	private String password;
}
