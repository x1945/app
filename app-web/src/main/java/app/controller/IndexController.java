package app.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import app.annotation.PageSet;
import app.beans.App0Usr;
import app.service.IndexService;

@Controller
@RequestMapping("/index")
public class IndexController {

	private static final Logger log = LoggerFactory.getLogger(IndexController.class);

	@Autowired
	IndexService indexService;

	@PageSet
	@RequestMapping
	public String index(Model model) {
		log.info("index");

		List<App0Usr> list = indexService.loadUsers();
		// Map<String, Object> data = indexService.loadData();
		// model.addAllAttributes(data);

		// Map<String, List<Map<String, Object>>> data2 =
		// indexService.loadData2();
		// model.addAllAttributes(data2);

		// model.addAttribute("best_products", data2.get("best_products"));
		// result.put("best_products", data2.get("best_products"));

		return "index";
	}

}
