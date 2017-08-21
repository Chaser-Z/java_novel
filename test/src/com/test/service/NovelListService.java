package com.test.service;

import java.util.List;
import java.util.Map;

import com.test.model.NovelList;

public interface NovelListService {

	NovelList load(String id) throws Exception;
	
	List<NovelList> getHot() throws Exception;
	
	List<NovelList> getArticleByType(String type) throws Exception;
	
	List<NovelList> getHome() throws Exception;

	List<NovelList> getArticleByKeyword(String keyword) throws Exception;
	
	Map<String, Object> page(String rows, String curr);
	
}
