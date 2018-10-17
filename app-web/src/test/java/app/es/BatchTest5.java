package app.es;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import org.elasticsearch.action.index.IndexRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BatchTest5 {

	private static final Logger LOG = LoggerFactory.getLogger(BatchTest5.class);

	private static final String INDEX_NAME = "coa-index";
	private static final String INDEX_TYPE = "fulltext";

//	private static final String 研究結案報告 = "\\\\AP4-File\\e\\COA-TmpPDF\\fullText\\研究結案報告";
//	private static final String 計畫書 = "\\\\AP4-File\\e\\COA-TmpPDF\\fullText\\計畫書";
//	private static final String 頁面呈現資訊 = "\\\\AP4-File\\e\\COA-TmpPDF\\fullText\\頁面呈現資訊";

	 private static final String 研究結案報告 = "D:/全文檢索/fullText20181002/研究結案報告";
	 private static final String 計畫書 = "D:/全文檢索/fullText20181002/計畫書";
	 private static final String 頁面呈現資訊 = "D:/全文檢索/fullText20181002/頁面呈現資訊";

	private final String[] eFields = { "cpid", "cname", "yr", "category", "categoryName",
			"type", "typeName", "direcotrName", "directorDeptid", "directorDeptName",
			"expertDivision_id", "expertDivision_name", "expertSegid", "expertSegName", "expert_id",
			"expert_name", "real_domain_id", "real_domain_name", "domain_id", "domain_name",
			"promote_id", "promote_name", "budget_type", "budget_typeName", "open_yr" };

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		LOG.debug("start");
		BatchTest5 test2 = new BatchTest5();
		TreeSet<String> indexDataList = test2.loadFileList();
		LOG.debug("indexDataList.size[{}]", indexDataList.size());
		LOG.debug("load file Using Time:{}", (double) (System.currentTimeMillis() - startTime) / 1000d + " s");
		LOG.debug("create doc");
		test2.bulkCreateDoc(indexDataList);
		EsClient.close();
		LOG.debug("Using Time:{}", (double) (System.currentTimeMillis() - startTime) / 1000d + " s");
		LOG.debug("end");
	}

	private TreeSet<String> loadFileList() {
		TreeSet<String> result = new TreeSet<String>();
		File file = new File(頁面呈現資訊);
		LOG.debug("file.path[{}]", file.getPath());
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			LOG.debug("files.size[{}]", files.length);
			for (File f : files)
				result.add(f.getName());
		}
		return result;
	}

	/**
	 * 讀取基本資訊
	 * 
	 * @param filePath
	 * @return
	 */
	private Map<String, Object> loadInfoFile(String filePath) {
		return loadInfoFile(new File(filePath));
	}

	private Map<String, Object> loadInfoFile(File file) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (file != null && file.exists() && file.isFile()) {
			BufferedReader br = null;
			try {
				int count = 0;
				br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
				// br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "BIG5"));
				while (br.ready()) {
					String line = processSpecial(br.readLine());
					result.put(eFields[count++], line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null) {
						br.close();
						br = null;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
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
		if (value != null)
			return value.replaceAll(specialString, "");
		return value;
	}

	private String loadContentX(File file) {
		StringBuilder result = new StringBuilder();
		if (file != null && file.exists() && file.isFile()) {
			BufferedReader br = null;
			try {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
				// br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "BIG5"));
				while (br.ready()) {
					String line = processSpecial(br.readLine());
					result.append(line);
					// 最後一個字為英文時,補空白
					if (line.matches("^.+[a-zA-Z]$"))
						result.append(" ");
					result.append('\n');
				}
				//
				int len = result.length() / 10;
				result.delete(len, result.length());
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null) {
						br.close();
						br = null;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (result.length() == 0)
			LOG.warn("no Content [{}]", file.getName());
		return result.toString();
	}

	private String loadContent(File file) {
		StringBuilder result = new StringBuilder();
		if (file != null && file.exists() && file.isFile()) {
			BufferedReader br = null;
			try {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
				// br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "BIG5"));
				while (br.ready()) {
					String line = processSpecial(br.readLine());
					result.append(line);
					// 最後一個字為英文時,補空白
					if (line.matches("^.+[a-zA-Z]$"))
						result.append(" ");
					result.append('\n');
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null) {
						br.close();
						br = null;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (result.length() == 0)
			LOG.warn("no Content [{}]", file.getName());
		return result.toString();
	}

	/**
	 * 批量新增
	 * 
	 * @param client
	 * @param list
	 */
	private void bulkCreateDoc(TreeSet<String> set) {
		if (set.size() > 0) {
			EsClient.setTotalCount(set.size());
			for (String value : set)
				bulkCreateDoc(value);

			// Map<String, Object> map = list.get(0);
			// System.out.println(map);
			// bulkCreateDoc(map);
		}
	}

	/**
	 * 批量新增
	 * 
	 * @param bulkRequest
	 * @param indexData
	 */
	private void bulkCreateDoc(String value) {
		Map<String, Object> data = loadInfoFile(頁面呈現資訊 + "/" + value);
		// if ("1012488.txt".equals(value))
		// System.out.println(data);
		if (data.size() > 0) {
			String pid = value.replaceAll(".txt", "");
			data.put("pid", pid);
			data.put("content1", loadContent(計畫書 + "/" + value));
			data.put("content2", loadContent(研究結案報告 + "/" + value));
			IndexRequest indexRequest = new IndexRequest(INDEX_NAME, INDEX_TYPE, pid)
					.source(data);
			EsClient.getBulkProcessor().add(indexRequest);
			data.clear();
		} else {
			LOG.warn("no info [{}]", value);
		}
	}
}
