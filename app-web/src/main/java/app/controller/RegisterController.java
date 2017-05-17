package app.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import app.annotation.PageSet;
import sys.service.I18nService;
import sys.service.UserService;
import sys.system.WebException;
import sys.util.StringUtil;
import sys.util.Util;

@Controller
@RequestMapping("/register")
public class RegisterController {

	private static final Logger log = LoggerFactory.getLogger(RegisterController.class);

	@Autowired
	UserService userService;

	@Autowired
	I18nService i18n;

	@PageSet
	@RequestMapping
	public String index() {
		log.info("register");
		return "register";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> add(@RequestParam Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		String pw = StringUtil.trim(params.get("password"));
		String cp = StringUtil.trim(params.get("confirmPassword"));
		if (Util.notEquals(pw, cp)) {
			throw new WebException("密碼確認不符，請重新輸入！");
		}

		userService.add(params);
		return result;
	}

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> test(@RequestParam Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
//		Org_member_data bean = userService.findByEmail("xx@xx.xx");
//		log.debug("bean : {}", bean);
		log.debug("account : {}", i18n.getMessage("account"));
		log.debug("login : {}", i18n.getMessage("login"));
		log.debug("register : {}", i18n.getMessage("register"));
		return result;
	}
}
