package app.es;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.service.ElasticSearchService4;

public class QueryTest4 {

	private static final Logger log = LoggerFactory.getLogger(QueryTest.class);

	public static void main(String[] args) throws Exception {
		ElasticSearchService4 ess4 = new ElasticSearchService4();
		Map<String, Object> result = new HashMap<String, Object>();
//		 String word = "朝陽科技大學";
		 String word = "在線互動式UPS的逆電器平時作為充電器";
//		 String word = "﻿消化酶"; // "稻熱病";
		// String word = "﻿﻿﻿茶屬"; // "稻熱病";
//		String word = "﻿﻿﻿大豆"; // "稻熱病";
//		log.debug("word:{}", word);
		TransportClient client = null;
		try {
			client = ess4.getClient();
			long startTime = System.currentTimeMillis();
			SearchResponse sr = ess4.searchDoc(client, word);
			System.out.println("search Time:" + (double) (System.currentTimeMillis() - startTime) / 1000d + " s");
			// SearchResponse sr = ess.searchDocTest(client, word);
			SearchHits hits = sr.getHits();
			log.debug("hit size:{}", hits.getHits().length);
			for (SearchHit hit : hits.getHits()) {
				log.debug("hit id:{}", hit.getId());
			}

			result.put("sr", sr.toString());
			log.debug("{}", sr.toString());
			
			List<String> analyze = ess4.analyze(client, word);
			result.put("analyze", analyze);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} finally {
			if (client != null)
				client.close();
		}
		log.debug("end");
	}
}
