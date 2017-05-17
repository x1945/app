package app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.beans.App0Usr;
import app.dao.App0UsrDao;
import sys.mybatis.SQLParams;

@Service
public class IndexService {

	private static final Logger log = LoggerFactory.getLogger(IndexService.class);

	@Autowired
	App0UsrDao app0UsrDao;

	public Map<String, Object> loadData() {
		Map<String, Object> result = new HashMap<String, Object>();

		// 輪播
		SQLParams sp = new SQLParams();

		return result;
	}

	public List<App0Usr> loadUsers() {
		SQLParams sp = new SQLParams();
		List<App0Usr> list = app0UsrDao.list(sp);
		return list;
	}
}
