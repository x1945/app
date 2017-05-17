/*
 * Adv_AbstractDao.java
 * 
 * Copyright (c) 2011-2012 JC Software Services, Inc.
 * 9F, No.30, Sec.1, Ming Sheng E. Rd., Taipei, Taiwan, R.O.C
 * All Rights Reserved.
 * 
 * Licensed Materials - Property of JC Software Services, Inc.
 * 
 * This software is confidential and proprietary information of
 * JC Software Services, Inc. (&quot;Confidential Information&quot;).
 */

package app.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Options.FlushCachePolicy;

//@CacheNamespace(flushInterval = MybatisUtil.CACHE_TIME)
@CacheNamespace
public interface ViewDao {

	public List<Map<String, Object>> findItems();

	@Select("select 1")
	@Options(flushCache=FlushCachePolicy.TRUE)
	int flushCache();
}
