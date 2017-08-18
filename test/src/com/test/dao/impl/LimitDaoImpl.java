package com.test.dao.impl;

import org.springframework.stereotype.Repository;

import com.miger.commons.hibernate.impl.SimpleDaoImpl;
import com.test.dao.LimitDao;
import com.test.model.manager.Limit;

@Repository("limitDao")
public class LimitDaoImpl extends SimpleDaoImpl<Limit, String> implements LimitDao {
		
	
}