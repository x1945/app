package app.es;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.elasticsearch.action.index.IndexRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sys.util.Util;

public class BatchTest {

	private static final Logger LOG = LoggerFactory.getLogger(BatchTest.class);

	private static final String INDEX_NAME = "coa-index";
	private static final String INDEX_TYPE = "fulltext";
	private static final String 研究結案報告 = "D:/全文檢索/研究結案報告";
	private static final String 計畫書 = "D:/全文檢索/計畫書";
	private static final String 頁面呈現資訊 = "D:/全文檢索/頁面呈現資訊";
	// private static final String[] Fields = { "計畫編號", "計畫名稱", "年度", "計畫類別", "計畫型式", "計畫主持人", "計畫主持人單位", "主辦專家機關",
	// "主辦專家單位", "主辦專家", "領域", "綱要", "推動小組" };
	private static final String[] eFields = { "cpid", "cname", "yr", "category", "type", "director_name",
			"director_dept", "divisioin_id", "segid", "eid", "real_domain_id", "domain_id", "promote_id" };

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		LOG.debug("start");
		BatchTest test2 = new BatchTest();
		Map<String, Map<String, String>> info = test2.loadInfoFileList(頁面呈現資訊);
		LOG.debug("info.size[{}]", info.keySet().size());
		List<IndexData> indexDataListA = test2.loadFileList(研究結案報告, info);
		LOG.debug("indexDataListA.size[{}]", indexDataListA.size());
		List<IndexData> indexDataListB = test2.loadFileList(計畫書, info);
		LOG.debug("indexDataListB.size[{}]", indexDataListB.size());
		info.clear();
		System.out.println("load file Using Time:" + (double) (System.currentTimeMillis() - startTime) / 1000d + " s");
		LOG.debug("create 研究結案報告");
		test2.bulkCreateDoc(indexDataListA);
		LOG.debug("create 計畫書");
		test2.bulkCreateDoc(indexDataListB);
		EsClient.close();
		LOG.debug("end");
		System.out.println("Using Time:" + (double) (System.currentTimeMillis() - startTime) / 1000d + " s");
	}

	private List<IndexData> loadFileList(String fileName, Map<String, Map<String, String>> info) {
		List<IndexData> result = new ArrayList<IndexData>();
		String type = "";
		switch (fileName) {
		case 研究結案報告:
			type = "A";
			break;
		case 計畫書:
			type = "B";
			break;
		}
		File file = new File(fileName);
		LOG.debug("fileName[{}] file.path[{}] type[{}]", fileName, file.getPath(), type);
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			LOG.debug("files.size[{}]", files.length);
			for (File f : files) {
				Map<String, String> map = info.get(f.getName());
				if (map != null) {
					// String content = loadContent(f);
					IndexData bean = new IndexData();
					Util.mapToBean(map, bean);
					bean.setPid(f.getName().replaceAll(".txt", "_" + type));
					// bean.setContent(content);
					bean.setFilePath(f.getPath());
					result.add(bean);
				} else {
					LOG.warn("{} is not info!!", f.getName());
				}
			}
		}
		return result;
	}

	private Map<String, Map<String, String>> loadInfoFileList(String fileName) {
		Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
		File file = new File(fileName);
		LOG.debug("fileName[{}] file.path[{}]", fileName, file.getPath());
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			LOG.debug("files.size[{}]", files.length);
			for (File f : files) {
				Map<String, String> map = loadFile(f);
				result.put(f.getName(), map);
			}
		}
		return result;
	}

	private Map<String, String> loadFile(File file) {
		Map<String, String> result = new HashMap<String, String>();
		if (file != null) {
			// LOG.debug("[{}] loadFile", file.getName());
			try (Scanner scanner = new Scanner(file)) {
				int count = 0;
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					result.put(eFields[count++], line);
				}
				scanner.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private String loadContent(String filePath) {
		return loadContent(new File(filePath));
	}

	private String loadContent(File file) {
		StringBuilder result = new StringBuilder();
		if (file != null) {
			// LOG.debug("[{}] loadContent", file.getName());
			try (Scanner scanner = new Scanner(file)) {
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					result.append(line).append('\n');
				}
				scanner.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result.toString();
	}

	/**
	 * 批量新增
	 * 
	 * @param client
	 * @param list
	 */
	private void bulkCreateDoc(List<IndexData> list) {
		if (list.size() > 0) {
			for (IndexData indexData : list)
				bulkCreateDoc(indexData);
		}
	}

	/**
	 * 批量新增
	 * 
	 * @param bulkRequest
	 * @param indexData
	 */
	private void bulkCreateDoc(IndexData indexData) {
		Map<String, Object> json = indexDataToMap(indexData);
		IndexRequest indexRequest = new IndexRequest(INDEX_NAME, INDEX_TYPE, indexData.getPid())
				.source(json);
		EsClient.getBulkProcessor().add(indexRequest);
	}

	private Map<String, Object> indexDataToMap(IndexData indexData) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("pid", indexData.getPid());
		result.put("cid", indexData.getCpid());
		result.put("cname", indexData.getCname());
		result.put("yr", indexData.getYr());
		result.put("category", indexData.getCategory());
		result.put("type", indexData.getType());
		result.put("director_name", indexData.getDirector_name());
		result.put("director_dept", indexData.getDirector_dept());
		result.put("divisioin_id", indexData.getDivisioin_id());
		result.put("segid", indexData.getSegid());
		result.put("eid", indexData.getEid());
		result.put("real_domain_id", indexData.getReal_domain_id());
		result.put("domain_id", indexData.getDomain_id());
		result.put("promote_id", indexData.getPromote_id());
		// result.put("content", indexData.getContent());
		result.put("content", loadContent(indexData.getFilePath()));
		return result;
	}
}
