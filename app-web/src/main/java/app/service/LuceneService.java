package app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.beans.App0Txt;
import app.dao.App0TxtDao;
import fantasy.ibatis.other.SQLParams;

@Service
public class LuceneService {

	private static final Logger LOG = LoggerFactory.getLogger(LuceneService.class);

	@Autowired
	App0TxtDao app0TxtDao;

	public Map<String, Object> loadData() {
		Map<String, Object> result = new HashMap<String, Object>();
		SQLParams sp = new SQLParams();
		List<App0Txt> list = app0TxtDao.list(sp);
		app0TxtDao.query(sp);
		return result;
	}

	public List<App0Txt> loadUsers() {
		SQLParams sp = new SQLParams();
		List<App0Txt> list = app0TxtDao.list(sp);
		app0TxtDao.query(sp);
		return list;
	}
}
