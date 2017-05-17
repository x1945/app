package app.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import app.beans.App0Usr;
import app.dao.App0UsrDao;
import sys.mybatis.SQLParams;
import sys.service.I18nService;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("dev")
@ContextConfiguration(locations = { "classpath*:spring-test.xml" })
public class SpringTest {

	private static final Logger LOG = LoggerFactory.getLogger(SpringTest.class);

	@Autowired
	I18nService i18n;

	@Autowired
	App0UsrDao userDao;

	// @Test
	public void MyBatisTest() {
		Map<String, Object> args = new HashMap<String, Object>();
		int count = userDao.count(args);
		LOG.debug("count : {}", count);

		List<App0Usr> list = userDao.list(args);
		LOG.debug("list : {}", list);
	}

	// @Test
	public void I18nTest() {
		LOG.debug("xxxx : xxxxx");
		LOG.debug("xxxx : {}", i18n.getMessage("xxxx"));
		LOG.debug("account : {}", i18n.getMessage("account"));
		LOG.debug("login : {}", i18n.getMessage("login"));
		LOG.debug("register : {}", i18n.getMessage("register"));
	}

	@Test
	public void testDao() {
		SQLParams sp = new SQLParams();
		sp.put("org_uuid", "xx");
	}
}
