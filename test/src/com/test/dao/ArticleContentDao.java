package com.test.dao;

import java.util.List;

import com.miger.commons.hibernate.SimpleDao;
import com.test.model.ArticleContent;
import com.test.model.NovelList;

public interface ArticleContentDao extends SimpleDao<ArticleContent, Integer>{
	
	ArticleContent getbyDirectoryLink(Integer id);

	List<ArticleContent> getContentsById(String article_id, Integer id) throws Exception;

}
