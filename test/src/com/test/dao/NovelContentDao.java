package com.test.dao;

import java.util.List;

import com.miger.commons.hibernate.SimpleDao;
import com.test.model.NovelContent;

public interface NovelContentDao extends SimpleDao<NovelContent, Integer>{
	
	NovelContent getbyDirectoryLink(Integer id);

	List<NovelContent> getContentsById(String article_id, Integer id) throws Exception;

}
