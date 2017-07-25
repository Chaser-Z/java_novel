package com.test.service;

import java.util.List;

import com.test.model.ArticleContent;

public interface ArticleContentService {

	ArticleContent getbyDirectoryLink(String article_id, String directoryLink) throws Exception;
	
	List<ArticleContent> getContentsById(String id) throws Exception;
}
