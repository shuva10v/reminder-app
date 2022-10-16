package io.shuvalov.test.reminder.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;

@RestController
@RequestMapping("/ddd")
public class RootController {
	@GetMapping
	public RedirectView redirect(Principal principal) {
		         return null;
	}
}
