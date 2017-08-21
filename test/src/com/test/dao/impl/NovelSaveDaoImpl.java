package com.test.dao.impl;

import org.springframework.stereotype.Repository;

import com.miger.commons.hibernate.impl.SimpleDaoImpl;
import com.test.dao.NovelSaveDao;
import com.test.model.NovelSave;

@Repository("novelSave")
public class NovelSaveDaoImpl extends SimpleDaoImpl<NovelSave, String> implements NovelSaveDao{

}
