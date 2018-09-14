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

import sys.util.StringUtil;

public class BatchTest3 {

	private static final Logger LOG = LoggerFactory.getLogger(BatchTest3.class);

	private static final String INDEX_NAME = "coa-index";
	private static final String INDEX_TYPE = "fulltext";

	private static final String 研究結案報告 = "\\\\AP4-File\\e\\COA-TmpPDF\\fullText\\研究結案報告";
	private static final String 計畫書 = "\\\\AP4-File\\e\\COA-TmpPDF\\fullText\\計畫書";
	private static final String 頁面呈現資訊 = "\\\\AP4-File\\e\\COA-TmpPDF\\fullText\\頁面呈現資訊";

	private final String[] eFields = { "cpid", "cname", "yr", "category", "categoryName",
			"type", "typeName", "direcotrName", "directorDeptid", "directorDeptName",
			"expertDivision_id", "expertDivision_name", "expertSegid", "expertSegName", "expert_id",
			"expert_name", "real_domain_id", "real_domain_name", "domain_id", "domain_name",
			"promote_id", "promote_name", "budget_type", "budget_typeName" };

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		LOG.debug("start");
		BatchTest3 test2 = new BatchTest3();
		List<Map<String, Object>> indexDataList = test2.loadFileList();
		LOG.debug("indexDataList.size[{}]", indexDataList.size());
		LOG.debug("load file Using Time:{}", (double) (System.currentTimeMillis() - startTime) / 1000d + " s");
		LOG.debug("create doc");
		test2.bulkCreateDoc(indexDataList);
		EsClient.close();
		LOG.debug("Using Time:{}", (double) (System.currentTimeMillis() - startTime) / 1000d + " s");
		LOG.debug("end");
	}

	private List<Map<String, Object>> loadFileList() {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		File file = new File(頁面呈現資訊);
		LOG.debug("file.path[{}]", file.getPath());
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			LOG.debug("files.size[{}]", files.length);
			for (File f : files) {
				Map<String, Object> map = loadFile(f);
				if (map.size() > 0) {
					map.put("pid", f.getName().replaceAll(".txt", ""));
					map.put("filePath1", 計畫書 + "/" + f.getName());
					map.put("filePath2", 研究結案報告 + "/" + f.getName());
					result.add(map);
				} else {
					LOG.warn("{} is not info!!", f.getName());
				}
			}
		}
		return result;
	}

	private Map<String, Object> loadFile(File file) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (file != null) {
			try (Scanner scanner = new Scanner(file)) {
				int count = 0;
				while (scanner.hasNextLine())
					result.put(eFields[count++], scanner.nextLine());
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
	
	/**
	 * 特殊字元(空格)
	 */
	private final String specialString = new String(new byte[] { -17, -69, -65 });
	/**
	 * 處理特殊字元
	 * 
	 * @param value
	 * @return
	 */
	private String processSpecial(String value) {
//		LOG.debug("{} {}", value, value.toCharArray().length);
//		String t = "螢光觀賞魚";
//		LOG.debug("{} {}", t, t.toCharArray().length);
		if (value != null)
			return value.replaceAll(specialString, "");
		return value;
	}

	private String loadContent(File file) {
		StringBuilder result = new StringBuilder();
		if (file != null) {
			if (file.exists() && file.isFile()) {
				try (Scanner scanner = new Scanner(file)) {
					while (scanner.hasNextLine())
						result.append(processSpecial(scanner.nextLine())).append(" ").append('\n');
					scanner.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
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
	private void bulkCreateDoc(List<Map<String, Object>> list) {
		if (list.size() > 0) {
			for (Map<String, Object> map : list)
				bulkCreateDoc(map);
		}
	}

	/**
	 * 批量新增
	 * 
	 * @param bulkRequest
	 * @param indexData
	 */
	private void bulkCreateDoc(Map<String, Object> data) {
		String pid = StringUtil.trim(data.get("pid"));
		String filePath1 = StringUtil.trim(data.get("filePath1"));
		String filePath2 = StringUtil.trim(data.get("filePath2"));
		data.put("content1", loadContent(filePath1));
		data.put("content2", loadContent(filePath2));
		IndexRequest indexRequest = new IndexRequest(INDEX_NAME, INDEX_TYPE, pid)
				.source(data);
		EsClient.getBulkProcessor().add(indexRequest);
	}
}
