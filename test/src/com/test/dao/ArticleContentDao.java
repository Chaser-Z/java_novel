package com.test.dao;

import com.miger.commons.hibernate.SimpleDao;
import com.test.model.ArticleContent;
import com.test.model.ArticleHot;

public interface ArticleContentDao extends SimpleDao<ArticleContent, Integer>{
	
	ArticleContent getbyDirectoryLink(Integer id);


}
