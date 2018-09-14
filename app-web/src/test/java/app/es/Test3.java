package app.es;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.elasticsearch.action.index.IndexRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sys.util.StringUtil;
import sys.util.WriteFile;

public class Test3 {

	private static final Logger LOG = LoggerFactory.getLogger(BatchTest3.class);

	private static final String INDEX_NAME = "coa-index";
	private static final String INDEX_TYPE = "fulltext";

	private static final String 研究結案報告 = "\\\\AP4-File\\e\\COA-TmpPDF\\fullText\\研究結案報告";
	private static final String 計畫書 = "\\\\AP4-File\\e\\COA-TmpPDF\\fullText\\計畫書";
	private static final String 頁面呈現資訊 = "\\\\AP4-File\\e\\COA-TmpPDF\\fullText\\頁面呈現資訊";
	private static final String 關鍵詞 = "D:\\BTDownload\\main3.dic";

	private final String[] eFields = { "cpid", "cname", "yr", "category", "categoryName",
			"type", "typeName", "direcotrName", "directorDeptid", "directorDeptName",
			"expertDivision_id", "expertDivision_name", "expertSegid", "expertSegName", "expert_id",
			"expert_name", "real_domain_id", "real_domain_name", "domain_id", "domain_name",
			"promote_id", "promote_name", "budget_type", "budget_typeName" };

	public static void main(String[] args) {
		System.out.println("start");
		
		String test = "abc測試一下。";
		
		System.out.println(test.matches("^.+[\\u4E00-\\u9FA5]$"));
		System.out.println("-----------");
		System.out.println(test.matches("^.+[a-zA-Z]$"));
		
		
		// File file = new File(頁面呈現資訊 + "\\1042738.txt");
		// File file = new File(頁面呈現資訊 + "\\1000005.txt");
		 File file = new File(計畫書 + "\\1000005.txt");
		 
		 System.out.println(file.getAbsolutePath());
		 System.out.println(file.getName());
		 System.out.println(file.getPath());
		 
	//	File file = new File(關鍵詞);
		// loadFile(file);
		// toWrite(file, "D:\\BTDownload\\main2.dic");
		// toWrite2(file, "D:\\BTDownload\\main2.dic");
		// toWrite2(file, "D:\\BTDownload\\main3.dic");

		// String s = "觀賞";
		// String v = "觀賞魚";
		// if (v.startsWith(s) || v.endsWith(s))
		// System.out.println(true);

		//toWrite3(file, "D:\\BTDownload\\main7.dic");
		// printChar("螢光觀賞魚");
		// printB("螢光觀賞魚");
		// String line = "觀賞魚";
		// String line = "螢光";
		// String line = "螢光觀賞魚";
		// System.out.println("-----------------------------");
		// System.out.println(line);
		// printByteLen(line);
		System.out.println("end");
	}

	private static void toWrite3(File file, String fileName) {
		StringBuilder sb = new StringBuilder();

		Map<Integer, TreeSet<String>> map = new TreeMap<Integer, TreeSet<String>>(
				new Comparator<Integer>() {

					public int compare(Integer obj1, Integer obj2) {
						// 順序排序
						// return obj1.compareTo(obj2);
						return obj2.compareTo(obj1);
					}
				});

		if (file != null) {
			try (Scanner scanner = new Scanner(file)) {
				while (scanner.hasNextLine()) {
					String line = processSpecial(StringUtil.trim(scanner.nextLine()));
					int len = line.length();
					if (len > 1) {
						if (!isWith(map, line)) {
							System.out.println(line);
							TreeSet<String> set = map.get(len);
							if (set == null) {
								set = new TreeSet<String>();
								map.put(len, set);
							}
							set.add(line);
						}
					}
				}
				scanner.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Set<Integer> keys = map.keySet();
		for (Integer key : keys) {
			TreeSet<String> set = map.get(key);
			for (String s : set) {
				if (sb.length() > 0)
					sb.append('\r').append('\n');
				sb.append(s);
			}
		}

		WriteFile.write(fileName, sb.toString());
	}

	private static boolean isWith(Map<Integer, TreeSet<String>> map, String v) {
		if (v.length() > 3) {
			Set<Integer> keys = map.keySet();
			for (Integer key : keys) {
				TreeSet<String> set = map.get(key);
				for (String s : set) {
					if (v.startsWith(s) || v.endsWith(s))
						return true;
				}
			}
		}
		return false;
	}

	private static void toWrite2(File file, String fileName) {
		StringBuilder sb = new StringBuilder();

		Map<Integer, TreeSet<String>> map = new TreeMap<Integer, TreeSet<String>>(
				new Comparator<Integer>() {

					public int compare(Integer obj1, Integer obj2) {
						// 順序排序
						// return obj1.compareTo(obj2);
						return obj2.compareTo(obj1);
					}
				});

		// Map<Integer, Set<String>> map = new HashMap<Integer, Set<String>>();
		if (file != null) {
			try (Scanner scanner = new Scanner(file)) {
				while (scanner.hasNextLine()) {
					String line = processSpecial(StringUtil.trim(scanner.nextLine()));
					int len = line.length();
					if (len > 1) {
						TreeSet<String> set = map.get(len);
						if (set == null) {
							set = new TreeSet<String>();
							map.put(len, set);
						}
						set.add(line);
					}
				}
				scanner.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Set<Integer> keys = map.keySet();
		for (Integer key : keys) {
			TreeSet<String> set = map.get(key);
			for (String s : set) {
				if (sb.length() > 0)
					sb.append('\r').append('\n');
				sb.append(s);
			}
		}

		WriteFile.write(fileName, sb.toString());
	}

	private static void toWrite(File file, String fileName) {
		StringBuilder sb = new StringBuilder();
		if (file != null) {
			try (Scanner scanner = new Scanner(file)) {
				while (scanner.hasNextLine()) {
					String line = processSpecial(StringUtil.trim(scanner.nextLine()));
					if (line.length() > 1) {
						if (sb.length() > 0)
							sb.append('\r').append('\n');
						sb.append(line);
					}
				}
				scanner.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		WriteFile.write(fileName, sb.toString());
	}

	private static Map<String, Object> loadFile(File file) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (file != null) {
			try (Scanner scanner = new Scanner(file)) {
				while (scanner.hasNextLine()) {
					String line = processSpecial(StringUtil.trim(scanner.nextLine()));
					System.out.println(line.length() + " " + line);
				}
				scanner.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private static void printChar(String v) {
		char[] cs = v.toCharArray();
		for (char c : cs) {
			System.out.println(c);
		}
	}

	private static void printB(String v) {
		StringBuffer out = new StringBuffer(v);
		for (int i = 0; i < out.length(); i++) {
			System.out.println(out.charAt(i));
		}
	}

	private static void printByteLen(String v) {
		byte[] bs = v.getBytes();
		System.out.println(bs.length);
	}

	private String loadContent(String filePath) {
		return loadContent(new File(filePath));
	}

	/**
	 * 特殊字元(空格)
	 */
	private static final String specialString = new String(new byte[] { -17, -69, -65 });

	/**
	 * 處理特殊字元
	 * 
	 * @param value
	 * @return
	 */
	private static String processSpecial(String value) {
		// LOG.debug("{} {}", value, value.toCharArray().length);
		// String t = "螢光觀賞魚";
		// LOG.debug("{} {}", t, t.toCharArray().length);
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
