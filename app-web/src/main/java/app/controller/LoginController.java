package app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import app.annotation.PageSet;

@Controller
@RequestMapping("/login")
public class LoginController {

	private static final Logger log = LoggerFactory.getLogger(LoginController.class);

	@PageSet
	@RequestMapping
	public String index() {
		log.info("login");
		return "login";
	}

}
