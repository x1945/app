package app.es;

import java.net.UnknownHostException;

import org.elasticsearch.client.transport.TransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.service.ElasticSearchService5;

public class QueryTest5 {

	private static final Logger log = LoggerFactory.getLogger(QueryTest.class);

	public static void main(String[] args) throws Exception {
		ElasticSearchService5 ess5 = new ElasticSearchService5();

		TransportClient client = null;
		try {
			client = ess5.getClient();
			long count = ess5.count();
			log.debug("count:{}", count);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} finally {
			if (client != null)
				client.close();
		}
		log.debug("end");
	}
}
