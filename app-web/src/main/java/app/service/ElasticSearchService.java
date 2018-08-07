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
		LOG.debug("createClient host[{}] port[{}]", "localhost", 9300);
		TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
				.addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));
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
		Map<String, Object> template_params = new HashMap<>();
		template_params.put("from", 0);
		template_params.put("content", word);
		template_params.put("divisioin_id", "01");

		SearchRequest sReq = new SearchRequest("coa-index");
		SearchResponse sr = new SearchTemplateRequestBuilder(client)
				.setScript(getScript("term"))
				// .setScript(getScript("term_filter"))
				.setScriptType(ScriptType.INLINE)
				.setScriptParams(template_params)
				// .setRequest(new SearchRequest())
				.setRequest(sReq)
				.get()
				.getResponse();

		LOG.debug("term.getTotalHits[{}]", sr.getHits().getTotalHits());
		if (sr.getHits().getTotalHits() == 0) {
			sr = new SearchTemplateRequestBuilder(client)
					// .setScript(getScript("match_phrase"))
					.setScript(getScript("match_phrase_filter"))
					// .setScript(getScript("match_phrase_analyzer"))
					.setScriptType(ScriptType.INLINE)
					.setScriptParams(template_params)
					// .setRequest(new SearchRequest())
					.setRequest(sReq)
					.get()
					.getResponse();
			LOG.debug("match_phrase.getTotalHits[{}]", sr.getHits().getTotalHits());
			if (true) {
				return sr;
			}
		}

		if (sr.getHits().getTotalHits() == 0) {
			List<String> analyze = analyze(client, word);
			LOG.debug("analyze: {}", analyze);
			template_params.put("content", analyze.toArray(new String[] {}));
			sr = new SearchTemplateRequestBuilder(client)
					.setScript(getScript("terms"))
					// .setScript(getScript("terms_from_size"))
					.setScriptType(ScriptType.INLINE)
					.setScriptParams(template_params)
					// .setRequest(new SearchRequest())
					.setRequest(sReq)
					.get()
					.getResponse();
			LOG.debug("terms.getTotalHits[{}]", sr.getHits().getTotalHits());
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
		SearchRequest sReq = new SearchRequest("coa-index");
		SearchResponse sr = new SearchTemplateRequestBuilder(client)
				.setScript(getScript("term"))
				.setScriptType(ScriptType.INLINE)
				.setScriptParams(template_params)
				// .setRequest(new SearchRequest())
				.setRequest(sReq)
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
					.setRequest(sReq)
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
					.setRequest(sReq)
					.get()
					.getResponse();
			LOG.debug("terms.getTotalHits[{}]", sr.getHits().getTotalHits());
		}
		return sr;
	}

	private String getScript(String scriptName) {
		String result = loadFile("scripts/" + scriptName + ".json");
		LOG.debug("-------------------------------");
		LOG.debug("{}", result);
		LOG.debug("-------------------------------");
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
		return result.toString();
	}
}
