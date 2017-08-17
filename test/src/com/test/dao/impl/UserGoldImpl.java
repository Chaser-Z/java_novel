package com.test.dao.impl;

import org.springframework.stereotype.Repository;

import com.miger.commons.hibernate.impl.SimpleDaoImpl;
import com.test.dao.UserGoldDao;
import com.test.model.UserGold;

@Repository("userGoldDao")
public class UserGoldImpl extends SimpleDaoImpl<UserGold, String> implements UserGoldDao {

}
