package com.test.dao.impl;

import org.springframework.stereotype.Repository;

import com.miger.commons.hibernate.impl.SimpleDaoImpl;
import com.test.dao.NovelListSaveDao;
import com.test.model.NovelListSave;

@Repository("novelListSaveDao")
public class NovelListSaveDaoImpl extends SimpleDaoImpl<NovelListSave, String> implements NovelListSaveDao{

}
