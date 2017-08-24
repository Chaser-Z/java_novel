package com.test.dao.impl;

import org.springframework.stereotype.Repository;

import com.miger.commons.hibernate.impl.SimpleDaoImpl;
import com.test.dao.NovelContentSaveDao;
import com.test.model.NovelContentSave;

@Repository("novelContentSaveDao")
public class NovelContentSaveDaoImpl extends SimpleDaoImpl<NovelContentSave, Integer> implements NovelContentSaveDao{

}
