package com.test.dao.impl;

import org.springframework.stereotype.Repository;

import com.miger.commons.hibernate.impl.SimpleDaoImpl;
import com.test.dao.NovelChapterSaveDao;
import com.test.model.NovelChapterSave;

@Repository("novelChapterSaveDao")
public class NovelChapterSaveDaoImpl extends SimpleDaoImpl<NovelChapterSave, Integer> implements NovelChapterSaveDao{

}
