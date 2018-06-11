package app.es;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.service.ElasticSearchService3;

public class QueryTest3 {

	private static final Logger log = LoggerFactory.getLogger(QueryTest.class);

	public static void main(String[] args) throws Exception {
		ElasticSearchService3 ess3 = new ElasticSearchService3();
		// String word = "朝陽科技大學";
		// String word = "世界重要糧食作物";
//		 String word = "﻿消化酶"; // "稻熱病";
		// String word = "﻿﻿﻿茶屬"; // "稻熱病";
		String word = "﻿﻿﻿大豆"; // "稻熱病";
		log.debug("word:{}", word);
		ess3.getClient();
		long startTime = System.currentTimeMillis();
		ess3.searchDoc(word);
		System.out.println("search Time:" + (double) (System.currentTimeMillis() - startTime) / 1000d + " s");
		ess3.close();
		log.debug("end");
	}
}
