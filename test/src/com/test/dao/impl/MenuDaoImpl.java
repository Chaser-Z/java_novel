package com.test.dao.impl;

import org.springframework.stereotype.Repository;

import com.miger.commons.hibernate.impl.SimpleDaoImpl;
import com.test.dao.MenuDao;
import com.test.model.manager.Menu;

@Repository("menuDao")
public class MenuDaoImpl extends SimpleDaoImpl<Menu, String> implements MenuDao {
}
