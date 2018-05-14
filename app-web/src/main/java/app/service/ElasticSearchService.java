package app.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.action.admin.indices.analyze.AnalyzeAction;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequestBuilder;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse.AnalyzeToken;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequestBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ElasticSearchService {

	private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchService.class);

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
		TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
				.addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));
		return client;
	}

	/**
	 * 分析文字
	 * 
	 * @param client
	 */
	public void Analyze(TransportClient client) {
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
	 * 分析文字
	 * 
	 * @param client
	 */
	public Set<String> analyze(TransportClient client, String word) {
		Set<String> result = new HashSet<String>();
		// 呼叫 IK 分詞分詞
		AnalyzeRequestBuilder ikRequest = new AnalyzeRequestBuilder(client,
				AnalyzeAction.INSTANCE);
		 ikRequest.setAnalyzer("ik_smart");
//		ikRequest.setAnalyzer("ik_max_word");
		ikRequest.setText(word);
		List<AnalyzeToken> ikTokenList = ikRequest.execute().actionGet().getTokens();
		// 迴圈賦值
		for (AnalyzeToken at : ikTokenList) {
			System.out.println(at.getPosition() + " " + at.getTerm());
			result.add(at.getTerm());
		}
		return result;
	}

	/**
	 * 查詢文件
	 * 
	 * @param client
	 */
	public void searchDocByScripts(TransportClient client) {
		// PutStoredScriptRequestBuilder pb = client.admin().cluster().preparePutStoredScript();
		// System.out.println("pb:" + pb);

		Map<String, Object> template_params = new HashMap<>();
		template_params.put("query", "計畫");
		// template_params.put("query", "glycerides");

		SearchResponse sr = new SearchTemplateRequestBuilder(client)
				.setScript("{\n" +
						"    \"query\" : {\n" +
						"        \"term\" : {\n" +
						"            \"content\": \"{{content}}\"\n" +
						"      }\n" +
						"    },\n" +
						"    \"highlight\" : {\n" +
						"        \"pre_tags\" : [\"<tag1>\", \"<tag2>\"],\n" +
						"        \"post_tags\" : [\"</tag1>\", \"</tag2>\"],\n" +
						"        \"fields\" : {\n" +
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

	/**
	 * 查詢文件
	 * 
	 * @param client
	 */
	public SearchResponse searchDoc(TransportClient client, String word) {
		Map<String, Object> template_params = new HashMap<>();
		template_params.put("content", word);

		SearchResponse sr = new SearchTemplateRequestBuilder(client)
				.setScript("{\n" +
						"    \"query\" : {\n" +
						"        \"term\" : {\n" +
						"            \"content\": \"{{content}}\"\n" +
						"      }\n" +
						"    },\n" +
						"    \"highlight\" : {\n" +
						"        \"pre_tags\" : [\"<tag1>\", \"<tag2>\"],\n" +
						"        \"post_tags\" : [\"</tag1>\", \"</tag2>\"],\n" +
						"        \"fields\" : {\n" +
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
		return sr;
	}
}
