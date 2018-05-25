package app.es;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.service.ElasticSearchService;

public class QueryTest {

	private static final Logger log = LoggerFactory.getLogger(QueryTest.class);

	public static void main(String[] args) throws Exception {
		ElasticSearchService ess = new ElasticSearchService();
		Map<String, Object> result = new HashMap<String, Object>();
		 String word = "朝陽科技大學";
//		String word = "世界重要糧食作物";
		log.debug("word:{}", word);
		TransportClient client = null;
		try {
			client = ess.getClient();
			SearchResponse sr = ess.searchDoc(client, word);
//			 SearchResponse sr = ess.searchDocTest(client, word);
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
			if (client != null)
				client.close();
		}
		log.debug("end");
	}
}
