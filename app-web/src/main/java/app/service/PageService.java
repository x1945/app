package app.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.dao.ViewDao;
import fantasy.ibatis.other.SQLParams;
import sys.util.StringUtil;

@Service
public class PageService {

	private static final Logger log = LoggerFactory.getLogger(PageService.class);

	@Autowired
	ViewDao viewDao;

	public Map<String, Object> getInfo() {
		Map<String, Object> result = new HashMap<String, Object>();
		SQLParams sp = new SQLParams();

		return result;
	}

	public Map<String, List<Map<String, Object>>> findItems() {
		Map<String, List<Map<String, Object>>> result = new HashMap<String, List<Map<String, Object>>>();

		List<Map<String, Object>> list = viewDao.findItems();
		log.debug("list: {}", list);
		for (Map<String, Object> map : list) {
			String pv = StringUtil.trim(map.get("pv"));
			List<Map<String, Object>> list2 = result.get(pv);
			if (list2 == null) {
				list2 = new ArrayList<Map<String, Object>>();
				result.put(pv, list2);
			}
			list2.add(map);
		}

		return result;
	}

	public void flushCache() {
		viewDao.flushCache();
	}

	// public Org_website_data getFooterInfo() {
	// SQLParams sp = new SQLParams();
	// sp.put("org_uuid", PropUtil.getOrg_uuid());
	// Org_website_data bean = org_website_dataDao.query(sp);
	// return bean;
	// }
}
