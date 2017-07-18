package com.test.dao.impl;

import org.springframework.stereotype.Repository;

import com.miger.commons.hibernate.impl.SimpleDaoImpl;
import com.test.dao.NovelUserDao;
import com.test.model.NovelUser;

@Repository("novelUserDao")
public class NovelUserDaoImpl extends SimpleDaoImpl<NovelUser, String> implements NovelUserDao {
    
}