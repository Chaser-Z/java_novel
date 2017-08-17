package com.test.service;

import java.util.List;

import com.test.model.ArticleContent;

public interface ArticleContentService {

	ArticleContent getbyDirectoryLink(Integer id) throws Exception;
	
	List<ArticleContent> getContentsById(String article_id, Integer id) throws Exception;
}
