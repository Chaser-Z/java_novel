package com.test.service;

import java.util.List;

import com.test.model.ArticleHot;

public interface ArticleHotService {

	List<ArticleHot> getHot() throws Exception;
	
	List<ArticleHot> getArticleByType(String type) throws Exception;
	
	List<ArticleHot> getHome() throws Exception;

}
