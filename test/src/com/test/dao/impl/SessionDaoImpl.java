package com.test.dao.impl;

import org.springframework.stereotype.Repository;

import com.miger.commons.hibernate.impl.SimpleDaoImpl;
import com.test.dao.SessionDao;
import com.test.model.Session;

@Repository("sessionDao")
public class SessionDaoImpl extends SimpleDaoImpl<Session, String> implements SessionDao {

}
