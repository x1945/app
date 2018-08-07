package app.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/bootstrap")
public class BootstrapController {

	private static final Logger log = LoggerFactory.getLogger(BootstrapController.class);

	@RequestMapping
	public String index(Model model) {
		log.info("bootstrap index");
		return "bootstrap";
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> search(@RequestParam Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		return result;
	}

}
