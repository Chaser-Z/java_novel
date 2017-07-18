package com.test.dao.impl;

import org.springframework.stereotype.Repository;

import com.miger.commons.hibernate.impl.SimpleDaoImpl;
import com.test.dao.UserDao;
import com.test.model.manager.User;

@Repository("userDao")
public class UserDaoImpl extends SimpleDaoImpl<User, String> implements UserDao {
}