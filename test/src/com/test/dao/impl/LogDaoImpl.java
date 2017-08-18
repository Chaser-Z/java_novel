package com.test.dao.impl;

import org.springframework.stereotype.Repository;

import com.miger.commons.hibernate.impl.SimpleDaoImpl;
import com.test.dao.LogDao;
import com.test.model.manager.Log;

@Repository("systemLogDao")
public class LogDaoImpl extends SimpleDaoImpl<Log, String> implements LogDao {
}