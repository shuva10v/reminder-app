package io.shuvalov.test.reminder;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.shuvalov.test.reminder.domain.Reminder;
import io.shuvalov.test.reminder.service.EmailNotificationService;
import io.shuvalov.test.reminder.service.SchedulerJob;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.timeout;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class ReminderIT {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private EmailNotificationService notificationService;

	@Autowired
	private SchedulerJob schedulerJob;

	@Test
	public void signUpAndLogin() throws Exception {
		String authRequest = "{\"email\":\"pasha@mail.ru\", \"password\":\"password\"}";
		// Auth without sign up doesn't work
		mockMvc.perform(post("/user/authenticate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(authRequest)
				).andDo(print()).andExpect(status().isUnauthorized());

		// Sign up returns jwttoken
		mockMvc.perform(post("/user/signup")
						.contentType(MediaType.APPLICATION_JSON)
						.content(authRequest)
				).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("jwttoken")));

		// sign up with already used email returns error
		mockMvc.perform(post("/user/signup")
						.contentType(MediaType.APPLICATION_JSON)
						.content(authRequest)
				).andDo(print()).andExpect(status().isConflict());

		// Auth after sign up works
		MvcResult result = mockMvc.perform(post("/user/authenticate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(authRequest)
				).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("jwttoken"))).andReturn();

		// Extract JWT token from the Auth request
		String jwtToken = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Map.class)
				.get("jwttoken").toString();

		// Whoami returns proper email
		mockMvc.perform(get("/user/whoami")
						.header("Authorization", "Bearer " + jwtToken)
				).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string("{\"email\":\"pasha@mail.ru\"}"));
	}

	@Test
	public void reminderNotifications() throws Exception {
		String authRequest = "{\"email\":\"pasha2@mail.ru\", \"password\":\"password\"}";
		mockMvc.perform(post("/user/signup")
						.contentType(MediaType.APPLICATION_JSON)
						.content(authRequest)
				).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("jwttoken")));

		MvcResult result = mockMvc.perform(post("/user/authenticate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(authRequest)
				).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("jwttoken"))).andReturn();

		// Extract JWT token from the Auth request
		String jwtToken = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Map.class)
				.get("jwttoken").toString();

		// reminders list is empty
		mockMvc.perform(get("/reminders")
						.header("Authorization", "Bearer " + jwtToken)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string("[]"));


		// create new reminder in the near feature
		String time = ZonedDateTime.now().plus(1, ChronoUnit.SECONDS)
				.format(DateTimeFormatter.ISO_DATE_TIME);
		ObjectMapper mapper = new ObjectMapper();
		mockMvc.perform(post("/reminders")
						.header("Authorization", "Bearer " + jwtToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(Map.of(
								"name", "test1",
								"description", "test2",
								"time", time
						)))
				).andDo(print()).andExpect(status().isCreated())
				.andExpect(content().string(containsString("\"name\":\"test1\"")));

		// reminders list has one item
		mockMvc.perform(get("/reminders")
						.header("Authorization", "Bearer " + jwtToken)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("\"name\":\"test1\"")));

		ArgumentCaptor<Reminder> argument = ArgumentCaptor.forClass(Reminder.class);

		Mockito.verify(notificationService, timeout(2000).times(1)).notifyUser(argument.capture());
		assertEquals("pasha2@mail.ru", argument.getValue().getOwner().getEmail());
	}
}
