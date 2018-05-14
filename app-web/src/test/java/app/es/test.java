package app.es;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.admin.indices.analyze.AnalyzeAction;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequestBuilder;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse.AnalyzeToken;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequestBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class test {

	private static final Logger log = LoggerFactory.getLogger(test.class);

	public static void main(String[] args) throws Exception {
		log.debug("log test");
		System.out.println("start");
		TransportClient client = null;
		try {
			System.out.println("createClient");
			client = createClient();
			// System.out.println("createIndex");
			// createIndex(client);
			// System.out.println("getIndex");
			// getIndex(client);

			System.out.println("Analyze");
			Analyze(client);
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
		System.out.println("end");
	}

	/**
	 * 建立Client
	 * 
	 * @return
	 * @throws UnknownHostException
	 */
	public static TransportClient createClient() throws UnknownHostException {
		TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
				.addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));
		return client;
	}

	/**
	 * 建立Index
	 * 
	 * @param client
	 */
	public static void createIndex(TransportClient client) {
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("user", "kimchy");
		json.put("postDate", new Date());
		json.put("message", "trying out Elasticsearch");

		IndexResponse response = client.prepareIndex("twitter", "tweet")
				.setSource(json)
				.get();

		// Index name
		String _index = response.getIndex();
		// Type name
		String _type = response.getType();
		// Document ID (generated or not)
		String _id = response.getId();
		// Version (if it's the first time you index this document, you will get: 1)
		long _version = response.getVersion();
		// status has stored current instance statement.
		RestStatus status = response.status();

		System.out.println("_index:" + _index);
		System.out.println("_type:" + _type);
		System.out.println("_id:" + _id);
		System.out.println("_version:" + _version);
		System.out.println("status:" + status);
	}

	/**
	 * 取得Index
	 * 
	 * @param client
	 */
	public static void getIndex(TransportClient client) {
		// _id:kcHmTGMBLC-JyDImsAew
		// GetResponse response = client.prepareGet("twitter", "tweet", "1").get();
		// GetResponse response = client.prepareGet("twitter", "tweet", "kcHmTGMBLC-JyDImsAew").get();
		GetResponse response = client.prepareGet("my-index", "fulltext", "1").get();
		// Index name
		String _index = response.getIndex();
		// Type name
		String _type = response.getType();
		// Document ID (generated or not)
		String _id = response.getId();
		// Version (if it's the first time you index this document, you will get: 1)
		long _version = response.getVersion();

		System.out.println("_index:" + _index);
		System.out.println("_type:" + _type);
		System.out.println("_id:" + _id);
		System.out.println("_version:" + _version);
	}

	public static void Analyze(TransportClient client) {
		// 呼叫 IK 分詞分詞
		AnalyzeRequestBuilder ikRequest = new AnalyzeRequestBuilder(client,
				AnalyzeAction.INSTANCE);
		ikRequest.setAnalyzer("ik_smart");
		ikRequest.setAnalyzer("ik_max_word");
		ikRequest.setText("菸鹼醯胺腺嘌呤二核苷酸磷酸葡萄糖水解酶");
		List<AnalyzeToken> ikTokenList = ikRequest.execute().actionGet().getTokens();
		// 迴圈賦值
		for (AnalyzeToken at : ikTokenList) {
			System.out.println(at.getPosition() + " " + at.getTerm());
		}
	}

	/**
	 * 建立文件
	 * 
	 * @param client
	 */
	public static void createDoc(TransportClient client) {
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("user", "kimchy");
		json.put("postDate", new Date());
		json.put("message", "trying out Elasticsearch");

		IndexRequest indexRequest = new IndexRequest("twitter", "tweet", "kcHmTGMBLC-JyDImsAew")
				.source(json);
		UpdateRequest updateRequest = new UpdateRequest("twitter", "tweet", "kcHmTGMBLC-JyDImsAew")
				.doc(json)
				.upsert(indexRequest);
		try {
			client.update(updateRequest).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查詢文件
	 * 
	 * @param client
	 */
	public static void searchDoc(TransportClient client) {
		SearchResponse response = client.prepareSearch("my-index")
				.setTypes("fulltext")
				// .setSearchType(SearchType.DEFAULT)
				// .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setSearchType(SearchType.QUERY_AND_FETCH)
				// .setSearchType(SearchType.QUERY_THEN_FETCH)
				.setQuery(QueryBuilders.termQuery("title", "計畫"))                 // Query
				// .setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(18)) // Filter
				.setFrom(0).setSize(60).setExplain(true)
				.get();
		System.out.println("response" + response);
	}

	/**
	 * 查詢文件
	 * 
	 * @param client
	 */
	public static void searchDocByScripts(TransportClient client) {
		// PutStoredScriptRequestBuilder pb = client.admin().cluster().preparePutStoredScript();
		// System.out.println("pb:" + pb);

		Map<String, Object> template_params = new HashMap<>();
		template_params.put("query", "計畫");
		// template_params.put("query", "glycerides");

		SearchResponse sr = new SearchTemplateRequestBuilder(client)
				.setScript("{\n" +
						"    \"query\" : {\n" +
						"        \"multi_match\" : {\n" +
						"            \"query\":    \"{{query}}\",\n" +
						"            \"fields\": [ \"title\", \"content\" ]\n" +
						"      }\n" +
						"    },\n" +
						"    \"highlight\" : {\n" +
						"        \"pre_tags\" : [\"<tag1>\", \"<tag2>\"],\n" +
						"        \"post_tags\" : [\"</tag1>\", \"</tag2>\"],\n" +
						"        \"fields\" : {\n" +
						"            \"title\" : {},\n" +
						"            \"content\" : {}\n" +
						"        }\n" +
						"    }\n" +
						"}\n")
				.setScriptType(ScriptType.INLINE)
				.setScriptParams(template_params)
				.setRequest(new SearchRequest())
				.get()
				.getResponse();
		System.out.println("sr.toString() >> " + sr.toString());
	}
}
