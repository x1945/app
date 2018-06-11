package app.service;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.common.xcontent.json.JsonXContentParser;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonFactory;

import sys.util.StringUtil;

@Service
public class ElasticSearchService3 {

	private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchService3.class);

	// private final String host = "localhost";
	private final String host = "192.168.1.95";
	private final int port = 9200;

	private RestClient restClient;

	public RestClient getClient() {
		if (restClient == null)
			restClient = createClient();
		return restClient;
	}

	/**
	 * 建立Client
	 * 
	 * @return
	 * @throws UnknownHostException
	 */
	public RestClient createClient() {
		LOG.debug("createClient host[{}] port[{}]", host, port);
		RestClient restClient = RestClient.builder(
				new HttpHost(host, port, "http")).build();
		return restClient;
	}

	/**
	 * 分析文字
	 * 
	 * @param client
	 */
	public List<String> analyze(String word) {
		List<String> result = new ArrayList<String>();
		// // 呼叫 IK 分詞分詞
		// AnalyzeRequestBuilder ikRequest = new AnalyzeRequestBuilder(client,
		// AnalyzeAction.INSTANCE);
		// ikRequest.setAnalyzer("ik_smart");
		// // ikRequest.setAnalyzer("keyword");
		// // ikRequest.setAnalyzer("ik_max_word");
		// ikRequest.setText(word);
		// List<AnalyzeToken> ikTokenList = ikRequest.execute().actionGet().getTokens();
		// // 迴圈賦值
		// for (AnalyzeToken at : ikTokenList) {
		// LOG.debug("[{}][{}]", at.getPosition(), at.getTerm());
		// result.add(at.getTerm());
		// }
		return result;
	}

	/**
	 * 查詢文件
	 * 
	 * @param client
	 */
	public SearchResponse searchCount(String word) {
		word = StringUtil.processSpecial(word);
		Map<String, Object> params = new HashMap<>();
		params.put("from", 0);
		params.put("word", word);
		params.put("divisioin_id", "01");

		String queryString = parseQueryString("count", params);

		try {
			HttpEntity entity = new NStringEntity(queryString, ContentType.APPLICATION_JSON);
			Response response = getClient().performRequest("GET", "/coa-index/fulltext/_count", Collections.emptyMap(),
					entity);
			int statusCode = response.getStatusLine().getStatusCode();
			LOG.debug("statusCode:{}", statusCode);
			String responseBody = EntityUtils.toString(response.getEntity());

			// SearchResponse sr = parseSearchResponse(responseBody);
			// if (sr != null)
			// LOG.debug("term.getTotalHits[{}]", sr.getHits().getTotalHits());

			 LOG.debug("responseBody:{}", responseBody);
//			String jsonResult = responseBody.replaceAll("\"hits\":\\[.+\\]", "\"hits\":\\[\\]");
//			LOG.debug("count jsonResult:{}", jsonResult);

//			JSONObject json = new JSONObject(jsonResult);
//			LOG.debug("json:{}", json);
//			LOG.debug("hits:{}", new JSONObject(json.get("hits").toString()).get("total"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 查詢文件
	 * 
	 * @param client
	 */
	public SearchResponse searchDoc(String word) {
		if (true)
			return searchCount(word);
		word = StringUtil.processSpecial(word);
		Map<String, Object> params = new HashMap<>();
		params.put("from", 0);
		params.put("word", word);
		params.put("divisioin_id", "01");

		String queryString = parseQueryString("term2", params);

		try {
			HttpEntity entity = new NStringEntity(queryString, ContentType.APPLICATION_JSON);
			Response response = getClient().performRequest("GET", "/coa-index/fulltext/_search", Collections.emptyMap(),
					entity);
			int statusCode = response.getStatusLine().getStatusCode();
			LOG.debug("statusCode:{}", statusCode);
			String responseBody = EntityUtils.toString(response.getEntity());

			// SearchResponse sr = parseSearchResponse(responseBody);
			// if (sr != null)
			// LOG.debug("term.getTotalHits[{}]", sr.getHits().getTotalHits());

			// LOG.debug("responseBody:{}", responseBody.replaceAll("[\u4e00-\u9fa5]", ""));
			String jsonResult = responseBody.replaceAll("\"hits\":\\[.+\\]", "\"hits\":\\[\\]");
			LOG.debug("jsonResult:{}", jsonResult);

			JSONObject json = new JSONObject(jsonResult);
			LOG.debug("json:{}", json);
			LOG.debug("hits:{}", new JSONObject(json.get("hits").toString()).get("total"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private SearchResponse parseSearchResponse(String responseJson) {
		SearchResponse response = null;
		try {
			JsonXContentParser xContentParser = new JsonXContentParser(NamedXContentRegistry.EMPTY,
					new JsonFactory().createParser(responseJson));
			response = SearchResponse.fromXContent(xContentParser);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	private String parseQueryString(String scriptName, Map<String, Object> params) {
		String script = loadFile("scripts3/" + scriptName + ".json");
		LOG.debug("{}", StringUtil.zip(script));
		// script =
		// "{\"from\":\"{{from}}\",\"size\":10,\"query\":{\"bool\":{\"should\":[{\"term\":{\"content1\":\"{{word}}\"}},{\"term\":{\"content2\":\"{{word}}\"}}]}}}";
		for (String key : params.keySet()) {
			Object v = params.get(key);
			String value = (v == null ? "null" : v.toString());
			if (v instanceof String) {
				script = script.replaceAll("\\{\\{" + key + "\\}\\}", value);
			} else {
				script = script.replaceAll("\\\"\\{\\{" + key + "\\}\\}\\\"", value);
			}
		}
		// script = StringUtil.processSpecial(script);
		LOG.debug("-------------------------------");
		LOG.debug("{}", script);
		LOG.debug("-------------------------------");
		// script
		// ="{\"from\":0,\"size\":10,\"query\":{\"bool\":{\"should\":[{\"term\":{\"content1\":\"大豆\"}},{\"term\":{\"content2\":\"大豆\"}}]}}}";
		// not
		// script =
		// "{\"from\":0,\"size\":10,\"query\":{\"bool\":{\"should\":[{\"term\":{\"content1\":\"﻿﻿﻿大豆\"}},{\"term\":{\"content2\":\"﻿﻿﻿大豆\"}}]}}}";
		// ok
		// script =
		// "{\"from\":0,\"size\":10,\"query\":{\"bool\":{\"should\":[{\"term\":{\"content1\":\"大豆\"}},{\"term\":{\"content2\":\"大豆\"}}]}}}";
		return StringUtil.zip(script);
	}

	private String getScript(String scriptName) {
		String result = loadFile("scripts3/" + scriptName + ".json");
		LOG.debug("-------------------------------");
		LOG.debug("{}", result);
		LOG.debug("-------------------------------");
		// result = result.replaceAll("(\\s|\\n)", "");
		// LOG.debug("{}", result);
		// LOG.debug("-------------------------------");
		return StringUtil.zip(result);
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
		// return StringUtil.processUnicode2(StringUtil.processUnicode(result.toString()));
		return result.toString();
	}

	/**
	 * 關閉
	 */
	public void close() {
		try {
			if (restClient != null)
				restClient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
