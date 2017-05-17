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
@RequestMapping("/page")
public class PageController {

	private static final Logger log = LoggerFactory.getLogger(PageController.class);

	@Autowired
	IndexService indexService;

	@PageSet
	@RequestMapping
	public String index(Model model) {
		log.info("page");
		List<App0Usr> list = indexService.loadUsers();
		model.addAttribute("list", list);
		return "page";
	}

}
