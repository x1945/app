package app.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.elasticsearch.client.transport.TransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.service.ElasticSearchService5;

public class ContentTest {

	private static final Logger log = LoggerFactory.getLogger(ContentTest.class);

	static String[] files = { "人力發展處", "人事室", "主計室", "經發處", "資管處", "管考處" };
	// static String[] files = { "人力發展處" };

	public static void main(String[] args) throws Exception {
		long startTime = System.currentTimeMillis();
		SvmUtil.init();
		ContentTest kt = new ContentTest();
		List<SvmModelData> list = kt.read(files);

		// List<String> analyze = ess.analyze(client, word);
		int count = 0;
		for (SvmModelData smd1 : list) {
			for (String key1 : smd1.getContentMap().keySet()) {
				String content1 = smd1.getContentMap().get(key1);
				for (SvmModelData smd2 : list) {
					if (smd1.getLabel() != smd2.getLabel()) {
						for (String key2 : smd2.getContentMap().keySet()) {
							String content2 = smd2.getContentMap().get(key2);
							if (content1.equals(content2)) {
								System.out.println(smd1.getName() + "[" + key1 + "] = " + smd2.getName() + "[" + key2
										+ "]");
								count++;
							}
						}
					}
				}
			}
//			for (String content : smd1.getContentMap().values()) {
//				for (SvmModelData smd2 : list) {
//					if (smd1.getLabel() != smd2.getLabel()) {
//						for (String content2 : smd2.getContentMap().values()) {
//							if (content.equals(content2)) {
//								System.out.println(smd1.getName() + "|" + smd2.getName());
//								count++;
//							}
//						}
//					}
//				}
//			}
		}
		System.out.println(">>>>>>>" + count);

		System.out.println("exe Time:" + (double) (System.currentTimeMillis() - startTime) / 1000d + " s");
		log.debug("end");
	}

	private class SvmModelData {

		Double label = 0d;

		String name;

		Map<String, String> contentMap = new TreeMap<String, String>();

		public Double getLabel() {
			return label;
		}

		public void setLabel(Double label) {
			this.label = label;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Map<String, String> getContentMap() {
			return contentMap;
		}

		public void setContentMap(Map<String, String> contentMap) {
			this.contentMap = contentMap;

		}
	}

	public List<SvmModelData> read(String[] files) {
		List<SvmModelData> result = new ArrayList<SvmModelData>();
		for (String file : files) {
			InputStream in = null;
			try {
				in = this.getClass().getClassLoader().getResourceAsStream(file + ".xlsx");
				SvmModelData smd = read(in);
				smd.setLabel(SvmUtil.nameToLabel(file));
				smd.setName(file);
				result.add(smd);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (in != null) {
						in.close();
						in = null;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public SvmModelData read(InputStream in) {
		SvmModelData result = new SvmModelData();
		Map<String, String> contentMap = new TreeMap<String, String>();
		Workbook wb = null;
		try {
			wb = WorkbookFactory.create(in);
			Sheet sheet = wb.getSheetAt(0);
			for (Row row : sheet) {
				// 排除第一行tilte
				if (row.getRowNum() > 0) {
					// id
					String id = SvmUtil.trim(SvmUtil.getCellValue(row.getCell(0)));
					// contents
					String content = SvmUtil.trim(SvmUtil.getCellValue(row.getCell(1)));
					contentMap.put(id, content);
					// keywords
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (wb != null) {
					wb.close();
					wb = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		result.setContentMap(contentMap);
		return result;
	}
}
