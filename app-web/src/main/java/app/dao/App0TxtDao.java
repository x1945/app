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

import org.apache.ibatis.annotations.CacheNamespace;

import app.beans.App0Txt;
import fantasy.ibatis.dao.GenericDao;

@CacheNamespace
public interface App0TxtDao extends GenericDao<App0Txt> {

}
