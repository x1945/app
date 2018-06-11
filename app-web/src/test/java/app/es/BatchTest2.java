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

public class BatchTest2 {

	private static final Logger LOG = LoggerFactory.getLogger(BatchTest2.class);

	private static final int INDEX_NUM = 1;
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
		BatchTest2 test2 = new BatchTest2();
		List<IndexData2> indexDataList = test2.loadFileList();
		LOG.debug("indexDataList.size[{}]", indexDataList.size());
		LOG.debug("load file Using Time:{}", (double) (System.currentTimeMillis() - startTime) / 1000d + " s");
		LOG.debug("create doc");
		test2.bulkCreateDoc(indexDataList);
		EsClient.close();
		LOG.debug("Using Time:{}", (double) (System.currentTimeMillis() - startTime) / 1000d + " s");
		LOG.debug("end");
	}

	private List<IndexData2> loadFileList() {
		List<IndexData2> result = new ArrayList<IndexData2>();
		File file = new File(頁面呈現資訊);
		LOG.debug("file.path[{}]", file.getPath());
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			LOG.debug("files.size[{}]", files.length);
			for (File f : files) {
				Map<String, String> map = loadFile(f);
				if (map.size() > 0) {
					for (int i = 1; i <= INDEX_NUM; i++) {
						IndexData2 bean = new IndexData2();
						Util.mapToBean(map, bean);
						bean.setPid(f.getName().replaceAll(".txt", "_" + i));
						bean.setFilePath1(計畫書 + "/" + f.getName());
						bean.setFilePath2(研究結案報告 + "/" + f.getName());
						bean.setFilePath2(計畫書 + "/" + f.getName()); // TEST
						result.add(bean);
					}
				} else {
					LOG.warn("{} is not info!!", f.getName());
				}
			}
		}
		return result;
	}

	private Map<String, String> loadFile(File file) {
		Map<String, String> result = new HashMap<String, String>();
		if (file != null) {
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
			if (file.exists() && file.isFile()) {
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
		}
		return result.toString();
	}

	/**
	 * 批量新增
	 * 
	 * @param client
	 * @param list
	 */
	private void bulkCreateDoc(List<IndexData2> list) {
		if (list.size() > 0) {
			for (IndexData2 indexData : list)
				bulkCreateDoc(indexData);
		}
	}

	/**
	 * 批量新增
	 * 
	 * @param bulkRequest
	 * @param indexData
	 */
	private void bulkCreateDoc(IndexData2 indexData) {
		Map<String, Object> json = indexDataToMap(indexData);
		IndexRequest indexRequest = new IndexRequest(INDEX_NAME, INDEX_TYPE, indexData.getPid())
				.source(json);
		EsClient.getBulkProcessor().add(indexRequest);
	}

	private Map<String, Object> indexDataToMap(IndexData2 indexData) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("pid", indexData.getPid());
		result.put("cpid", indexData.getCpid());
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
		result.put("content1", loadContent(indexData.getFilePath1()));
		result.put("content2", loadContent(indexData.getFilePath2()));
		return result;
	}
}
