package app.controller;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import app.annotation.PageSet;
import app.service.ElasticSearchService;
import sys.util.StringUtil;

@Controller
@RequestMapping("/es")
public class ElasticSearchController {

	private static final Logger log = LoggerFactory.getLogger(ElasticSearchController.class);

	@Autowired
	ElasticSearchService ess;

	@PageSet
	@RequestMapping
	public String index(Model model) {
		log.info("es index");
		return "es";
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> search(@RequestParam Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		String word = StringUtil.trim(params.get("word")).toLowerCase();
		log.debug("word:{}", word);
		TransportClient client = null;
		try {
			client = ess.getClient();
			// System.out.println("createIndex");
			// createIndex(client);
			// System.out.println("getIndex");
			// getIndex(client);

			SearchResponse sr = ess.searchDoc(client, word);
			result.put("sr", sr.toString());
			List<String> analyze = ess.analyze(client, word);
			result.put("analyze", analyze);
			// System.out.println("searchDoc");
			// searchDoc(client);
			// System.out.println("searchDocByScripts");
			// searchDocByScripts(client);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} finally {
			// if (client != null)
			// client.close();
		}
		return result;
	}

}
