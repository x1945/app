package app.service;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.elasticsearch.action.admin.indices.analyze.AnalyzeAction;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequestBuilder;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse.AnalyzeToken;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequestBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import sys.util.StringUtil;

@Service
public class ElasticSearchService4 {

	private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchService4.class);

	// private final String host = "localhost";
	private final String host = "192.168.1.95";
	private final int port = 9300;
	private SearchRequest searchRequest = new SearchRequest("coa-index").types("fulltext");

	private TransportClient client;

	public TransportClient getClient() throws UnknownHostException {
		if (client == null)
			client = createClient();
		return client;
	}

	/**
	 * 建立Client
	 * 
	 * @return
	 * @throws UnknownHostException
	 */
	public TransportClient createClient() throws UnknownHostException {
		LOG.debug("createClient host[{}] port[{}]", host, port);
		Settings settings = Settings.builder()
				.put("client.transport.sniff", true).build();
		TransportClient client = new PreBuiltTransportClient(settings);
		// TransportClient client = new PreBuiltTransportClient(Settings.EMPTY);
		// TransportClient client = new PreBuiltTransportClient(Settings.EMPTY);
		client.addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));
		return client;
	}

	/**
	 * 分析文字
	 * 
	 * @param client
	 */
	public List<String> analyze(TransportClient client, String word) {
		List<String> result = new ArrayList<String>();
		// 呼叫 IK 分詞分詞
		AnalyzeRequestBuilder ikRequest = new AnalyzeRequestBuilder(client,
				AnalyzeAction.INSTANCE);
		ikRequest.setAnalyzer("ik_smart");
		// ikRequest.setAnalyzer("keyword");
		// ikRequest.setAnalyzer("ik_max_word");
		ikRequest.setText(word);
		List<AnalyzeToken> ikTokenList = ikRequest.execute().actionGet().getTokens();
		// 迴圈賦值
		for (AnalyzeToken at : ikTokenList) {
			LOG.debug("[{}][{}]", at.getPosition(), at.getTerm());
			result.add(at.getTerm());
		}
		return result;
	}
	
	/**
	 * 查詢文件
	 * 
	 * @param client
	 */
	public SearchResponse searchDoc(TransportClient client, String word) {
		word = StringUtil.processSpecial(word);
		Map<String, Object> template_params = new HashMap<>();
		template_params.put("from", 0);
		template_params.put("word", word);
		template_params.put("divisioin_id", "01");

//		SearchRequest sr = new SearchRequest("coa-index").types("fulltext");

		
//		SearchRequest searchRequest = new SearchRequest("posts");
//		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//		searchSourceBuilder.query(matchQuery("title", "Elasticsearch"));
//		searchSourceBuilder.size(size); 
//		searchRequest.source(searchSourceBuilder);
//		searchRequest.scroll(TimeValue.timeValueMinutes(1L));
		
		
		// create SearchTemplateRequestBuilder
		SearchTemplateRequestBuilder strb = new SearchTemplateRequestBuilder(client)
				.setScriptType(ScriptType.INLINE)
				.setScriptParams(template_params)
				.setRequest(searchRequest);
		// set script to search
		SearchResponse sr = strb.setScript(getScript("term"))
				.get().getResponse();
		LOG.debug("Scroll ID:{}", sr.getScrollId());
		LOG.debug("term.getTotalHits[{}]", sr.getHits().getTotalHits());
		if (sr.getHits().getTotalHits() == 0) {

			LOG.debug("SearchResponse:{}", sr.toString());
			// set script to search
			sr = strb.setScript(getScript("match"))
					.get().getResponse();
			LOG.debug("match.getTotalHits[{}]", sr.getHits().getTotalHits());
		}
		return sr;
	}

	/**
	 * 查詢文件
	 * 
	 * @param client
	 */
	public SearchResponse searchDoc2(TransportClient client, String word) {
		word = StringUtil.processSpecial(word);
		Map<String, Object> template_params = new HashMap<>();
		template_params.put("from", 0);
		template_params.put("word", word);
		template_params.put("divisioin_id", "01");
	
//		SearchRequest searchRequest = new SearchRequest("posts");
//		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//		searchSourceBuilder.query(matchQuery("title", "Elasticsearch"));
//		searchSourceBuilder.size(size); 
//		searchRequest.source(searchSourceBuilder);
//		searchRequest.scroll(TimeValue.timeValueMinutes(1L));
		
		
		// create SearchTemplateRequestBuilder
		SearchTemplateRequestBuilder strb = new SearchTemplateRequestBuilder(client)
				.setScriptType(ScriptType.INLINE)
				.setScriptParams(template_params)
				.setRequest(searchRequest);
		// set script to search
		SearchResponse sr = strb.setScript(getScript("term"))
				.get().getResponse();
		LOG.debug("Scroll ID:{}", sr.getScrollId());
		LOG.debug("term.getTotalHits[{}]", sr.getHits().getTotalHits());
		if (sr.getHits().getTotalHits() == 0) {

			LOG.debug("SearchResponse:{}", sr.toString());
			// set script to search
			sr = strb.setScript(getScript("match_phrase"))
					.get().getResponse();
			LOG.debug("match_phrase.getTotalHits[{}]", sr.getHits().getTotalHits());
		}
		return sr;
	}

	/**
	 * 查詢文件
	 * 
	 * @param client
	 */
	public SearchResponse searchDocX(TransportClient client, String word) {
		word = StringUtil.processSpecial(word);
		Map<String, Object> template_params = new HashMap<>();
		template_params.put("from", 0);
		template_params.put("word", word);
		template_params.put("divisioin_id", "01");

		SearchResponse response = client.prepareSearch("coa-index")
				.setTypes("fulltext")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(QueryBuilders.termQuery("content1", word))                 // Query
				// .setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(18)) // Filter
				.setFrom(0).setSize(10).setExplain(true)
				.get();
		LOG.debug("response.getTotalHits[{}]", response.getHits().getTotalHits());
		// LOG.debug("response:{}", response.toString());
		// create SearchTemplateRequestBuilder
		SearchTemplateRequestBuilder strb = new SearchTemplateRequestBuilder(client)
				.setScriptType(ScriptType.INLINE)
				.setScriptParams(template_params)
				.setRequest(searchRequest);
		// set script to search
		SearchResponse sr = strb.setScript(getScript("term"))
				.get().getResponse();

		LOG.debug("term.getTotalHits[{}]", sr.getHits().getTotalHits());
		if (sr.getHits().getTotalHits() == 0) {
			LOG.debug("SearchResponse:{}", sr.toString());
			// set script to search
			sr = strb.setScript(getScript("match_phrase"))
					.get().getResponse();
			LOG.debug("match_phrase.getTotalHits[{}]", sr.getHits().getTotalHits());
		}
		return sr;
	}

	/**
	 * 查詢文件
	 * 
	 * @param client
	 */
	public SearchResponse searchDocTest(TransportClient client, String word) {
		Map<String, Object> template_params = new HashMap<>();
		template_params.put("content", word);

		List<String> analyze = analyze(client, word);
		LOG.debug("analyze: {}", analyze);

		SearchResponse sr = new SearchTemplateRequestBuilder(client)
				.setScript(getScript("term"))
				.setScriptType(ScriptType.INLINE)
				.setScriptParams(template_params)
				// .setRequest(new SearchRequest())
				.setRequest(searchRequest)
				.get()
				.getResponse();

		LOG.debug("term.getTotalHits[{}]", sr.getHits().getTotalHits());
		if (sr.getHits().getTotalHits() == 0 && analyze.size() > 0) {
			LOG.debug("must[{}]", analyze.get(0));
			template_params.put("must", analyze.get(0));
			sr = new SearchTemplateRequestBuilder(client)
					.setScript(getScript("must2"))
					.setScriptType(ScriptType.INLINE)
					.setScriptParams(template_params)
					// .setRequest(new SearchRequest())
					.setRequest(searchRequest)
					.get()
					.getResponse();
			LOG.debug("must.getTotalHits[{}]", sr.getHits().getTotalHits());
			if (true) {
				return sr;
			}
		}

		if (sr.getHits().getTotalHits() == 0) {
			template_params.put("content", analyze.toArray(new String[] {}));
			sr = new SearchTemplateRequestBuilder(client)
					.setScript(getScript("match_phrase"))
					// .setScript(getScript("terms_from_size"))
					.setScriptType(ScriptType.INLINE)
					.setScriptParams(template_params)
					// .setRequest(new SearchRequest())
					.setRequest(searchRequest)
					.get()
					.getResponse();
			LOG.debug("terms.getTotalHits[{}]", sr.getHits().getTotalHits());
		}
		return sr;
	}

	private String getScript(String scriptName) {
		String result = loadFile("scripts2/" + scriptName + ".json");
		LOG.debug("-------------------------------");
		LOG.debug("{}", result);
		LOG.debug("-------------------------------");
		// result = StringUtil.zip(result);
		// LOG.debug("{}", result);
		// LOG.debug("-------------------------------");
		return result;
	}

	private String loadFile(String fileName) {
		ClassLoader classLoader = this.getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		return loadFile(file);
	}

	private String loadFile(File file) {
		StringBuilder result = new StringBuilder();
		if (file != null) {
			try (Scanner scanner = new Scanner(file)) {
				while (scanner.hasNextLine()) {
					if (result.length() > 0)
						result.append("\n");
					String line = scanner.nextLine();
					result.append(line);
				}
				scanner.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return StringUtil.processSpecial(result.toString());
	}
}
